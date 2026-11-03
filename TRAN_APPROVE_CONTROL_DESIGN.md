# 落地限额审批 设计方案（企业端 × 内管端〔银行端限额服务〕）

> 依据：《银行端限额管理_概要设计》（2026-08-28）＋ `D:\gs\银行端限额管理_mysql_ddl.sql`
> **术语对照：本设计"内管端" = 银行端限额服务 = bt-saas-management**（落地限额判断/审批归属方，对外 jar `bt-saas-management-exchange-client`）。
> 涉及：企业端（`bt-saas` / `bytter-service-payment`）× 内管端（`bt-saas-management`）
> 场景：会**生成线上银行支付指令**的来源业务（资金收付交易现金流 / 付款办理 / 代发工资；费用报销支付经付款办理-对私 billhead 承载，不设独立来源，见 §0）在**企业端审批通过后**的**落地限额审批**。本轮只做落地，提交/录入环节不做任何限额处理。
> 日期：2026-08-31；🔄 2026-09-02 更新（通用组件收口 + 多来源接线）；🔄 **2026-09-03 更新**（交互收敛：企业端只调 `isLandingApprovalRequired` 询问，内管端自行建案；落地对象定为现金流 FexCashFlow；落地标识 `0/A/R`；回调沿用 `ITranCaseCallbackService`）

> 🔄 **2026-09-03 更新说明（本轮核心）**：
> 1. **资金收付落地管理点纠正**：正确位置是 `FexCashFlowService.flowCompleted` 里**生成汇款指令之前**，对象是资金收付**现金流 FexCashFlow**（`tranType=4`），**不是** `FexTranReqService`（旧 WIP 误接，已回退、不加字段）。
> 2. **交互收敛**：企业端不再主动调内管【生成落地处理单】。改为审批通过后、生成指令前调 `IApiPaymentLimitService.isLandingApprovalRequired(PaymentLimitContextDto)` —— 由**内管端自行判断 + 自行创建内管审批**，企业端不建 case、不维护 caseId。
> 3. **企业端只做两件事**：命中落地后做**审批状态改造**（单据 11→95 但内容延后，落地标识 `LANDING_LIMIT_RESULT=0`）；暴露**回调方法**给内管端出结果后调。
> 4. **落地标识三状态** `"0"（等待银行审批）/"A"（银行同意支付）/"R"（银行拒绝支付）`；**sourceBizType 对齐内管**：`FEX_CASH_FLOW / NIS_BILLHEAD / NIS_WAGE`；**回调沿用** `ITranCaseCallbackService`。
> **接口契约与各来源差异见 `TRAN_APPROVE_CONTROL_LIANDIAO.md`；内管端实现契约见 `TRAN_APPROVE_CONTROL_MANAGEMENT_IFACE.md`。**

---

## 0. 范围与职责边界（落地限额审批）

| 事项 | 归属 | 说明 |
|---|---|---|
| 落地判断 + 建案 | **内管端（bt-saas-management）** | 企业端调 `IApiPaymentLimitService.isLandingApprovalRequired(ctx)` 询问；内管端自行判断、**命中则自行创建内管审批（BT_TR_LIMIT_CASE）** |
| 企业端命中落地后的审批状态改造 | **企业端（本设计）** | 命中 → 单据预置 95 + 落地标识 `LANDING_LIMIT_RESULT=0`，内容（发指令/资金计划/现金流）延后 |
| 落地处理单（`BT_TR_LIMIT_CASE`）审批 | 内管端（bt-saas-management） | 内管端生成/维护处理单与审批流；出结果后回调企业端（接口②） |
| 落地处理单结果回调（caseWorkflowCallback） | **企业端（本设计）** | 企业端提供**通用** `ITranCaseCallbackService.caseWorkflowCallback(TranCaseCallbackDTO)`，按 `sourceBizType` 分发到各来源业务处理器 |
| 企业端多来源接线（审批通过处落地判断 + 回调收尾） | **企业端（本设计）** | 资金收付现金流、付款办理、代发工资已接；费用报销支付经付款办理（对私）billhead 承载、**不设独立来源** |
| 签约机构 `bankOrgId` | 企业端不处理 | 内管端自行维护 |
| 签约客户 `signedCustId` | = 企业端租户ID | `UserContextHelper` 认证上下文 / 回调 DTO 携带 `tenantId` |

> 本文件只覆盖**落地限额审批**（触发于企业端审批通过后）；本轮不涉及提交/支付环节的任何限额处理。

---

## 1. 需求背景（落地限额）

落地限额是"企业审批通过后、真正送银行执行前"的一道额度复核：企业端审批通过的单据若命中银行落地限额，则**暂缓送银行执行**，转为银行端落地处理单（`BT_TR_LIMIT_CASE`）走复核/审批，出结果后再回到企业端继续或中止。

对企业端各来源业务（资金收付现金流、付款办理、代发工资；费用报销支付随"付款办理-对私"同法）：

1. **企业端审批通过**（`flowCompleted`，pass=true）：仅对"审批通过后会生成线上银行支付指令"的单据，在**生成指令前**调内管端 `isLandingApprovalRequired(ctx)` **询问是否落地**；返回 `false` → 正常收尾（审批通过、照常发送指令）。
2. **命中落地**（返回 `true`，内管已自行建案）：企业端**预置单据为审批通过（95）但内容延后**，落地标识 `LANDING_LIMIT_RESULT=0`（等待银行审批），**不生成线上指令**。
3. **内管端审批处理单**：拒绝支付 → 回调 → 企业端单据打回（-1，可修改）；同意支付 → 回调 → 企业端继续审批通过收尾（发送指令等）；审批流打回 → 单据回可修改。
4. **回调收尾幂等**：处理单结果可重复回调；落地待执行单（95+"0"）复位收尾、已收尾（95 且非"0"）仅记日志不重复处理。

---

## 2. 总体架构

```
企业端 (bt-saas / bytter-service-payment)                    内管端 (bt-saas-management)
  来源业务 flowCompleted（审批通过，仅线上发指令单据）
    ├─① 调 isLandingApprovalRequired(PaymentLimitContextDto) ──▶ 内管自行判断 + 自行创建内管审批(BT_TR_LIMIT_CASE)
    │     ├─ false（无需落地）→ 正常收尾：11→95 + 资金计划 + 现金流/指令
    │     └─ true（需要落地）→ markApprovePassForLanding：
    │           单据 11→95（内容延后）+ LANDING_LIMIT_RESULT=0
  ② 处理单结果回调 caseWorkflowCallback ◀──────────────────────┘
     （内管端 → 企业端）→ 按 sourceBizType 分发 handler：
       落地待执行(95+0) → 置 A/R + 复位 95→11 → 真正收尾（95 / -1）
```

- **① 落地判断（企业端 → 内管端）**：企业端只调 `isLandingApprovalRequired` **询问**；是否落地、建案、审批全部内管负责。
- **② 处理单结果回调（内管端 → 企业端）**：内管端对 `BT_TR_LIMIT_CASE` 给出结果后，经 `caseWorkflowCallback` 回调企业端，携带 `sourceBizType`＋业务定位＋审批状态＋租户ID，企业端按 `sourceBizType` 分发到来源业务收尾。

> 交互走**专用 DTO**（`PaymentLimitContextDto` 承载落地判断入参快照；`TranCaseCallbackDTO` 承载回调结果），**不**再使用 `IWorkFlowResult`，也不存在"企业端生成处理单"的调用。

---

## 3. 核心流程（落地限额审批）

### 3.1 企业端审批通过 → 落地判断 → 命中则预置延后

```
来源业务企业端审批通过（flowCompleted, pass=true）
   └─ 仅"审批通过后会生成线上银行支付指令"的单据（线上口径见 LIANDIAO §3.2.1）
   └─ 生成指令前，buildEnterpriseLimitContext(单据, user) 组装 PaymentLimitContextDto
         ├─ ctx == null（不适用落地：非线上/账户不在线/金额0等）→ 正常收尾：11→95 + 资金计划 + 现金流/指令
         ├─ isLandingApprovalRequired(ctx) == false → 正常收尾
         └─ isLandingApprovalRequired(ctx) == true → markApprovePassForLanding：
               单据 11→95（内容延后）+ LANDING_LIMIT_RESULT=0 + 记消息"已提交银行端落地限额审批，审批通过后自动生效"
```

### 3.2 银行处理 BT_TR_LIMIT_CASE

| 银行动作 | 回调企业端方向 | 企业端行为 |
|---|---|---|
| **拒绝支付** | 打回 | 落地待执行单 `LANDING_LIMIT_RESULT=R` → 复位 `95→11` → 原收尾 `11→-1`（可修改） |
| **同意支付（审批通过）** | 通过 | 落地待执行单 `LANDING_LIMIT_RESULT=A` → 复位 `95→11` → 原收尾 `11→95`（或记账通过 `100`）＋资金计划＋发送指令 |
| **审批流打回** | 打回 | 同拒绝：单据回可修改 |

### 3.3 企业端回调收尾（幂等）

处理单结果回调 → 回调门面还原租户上下文 → 按 `sourceBizType` 分发到来源业务 handler → 守卫判定 → `completeTranApprove` 执行收尾。

**守卫判定（三来源 handler 统一）**：
1. `isLandingPending`（status=95 且 `LANDING_LIMIT_RESULT=0`）→ `completeTranApprove`（内部置 A/R → 复位 95→11 → 收尾），return。
2. 否则 status=95（NisBillhead 补 `|| 100`）→ 幂等日志，return。
3. 否则非 status=11 → 抛"单据不在待审批状态"。
4. 否则（普通 11）→ `completeTranApprove`（无复位）。

---

## 4. 数据模型（银行端落地限额相关表 + 企业端落地标识列）

### 4.1 银行端 `BT_TR_*`（内管端职责）

`BT_TR_*` 落库归属银行端限额服务；企业端仅消费接口、不建表。本轮企业端对接以**落地处理单**为主：

- **`BT_TR_LIMIT_CASE`（落地限额处理单）**：本轮对接核心。关键字段：
  `CASE_NO` 处理单号 / `SOURCE_BIZ_TYPE`+`SOURCE_BIZ_ID` 来源业务与来源ID / `BUSINESS_NO` 业务单号 /
  `UNIT_ID`+`ACCOUNT_ID` 付款单位与账户 / `PAY_AMOUNT`+`CURRENCY_CODE` / `APPLICANT_ID`+`APPLICANT_NAME` 申请人 /
  `CASE_STATUS`(0待处理/1待审批/2审批通过/3打回) / `HANDLE_RESULT`(A同意/R拒绝) /
  `HANDLE_BY`+`HANDLE_TIME`+`HANDLE_DESC` / `PROCESS_INSTANCE_ID` 审批流实例 / `TENANT_ID`。
  > 处理单 `SOURCE_BIZ_TYPE`/`SOURCE_BIZ_ID`/`BUSINESS_NO` 必须能回到企业端来源单据，接口②（§5.2）据此定位。

### 4.2 企业端落地标识列 `LANDING_LIMIT_RESULT`（三来源实体）

| 表 / 实体 | 落地对象 | 字段 | 状态 |
|---|---|---|---|
| `fex_cash_flow`（FexCashFlow） | 资金收付现金流 | `LANDING_LIMIT_RESULT varchar(1)` | 已存在 |
| `nis_billhead`（NisBillhead） | 付款办理 | `LANDING_LIMIT_RESULT varchar(1)` | 已存在 |
| `nis_wage`（NisWage） | 代发工资 | `LANDING_LIMIT_RESULT varchar(1)` | **本轮新增** |

取值（与内管 `BT_TR_LIMIT_CASE.HANDLE_RESULT` 的 `A/R` 对齐；`0` 为企业端内部占位）：

| 值 | 含义 |
|---|---|
| `0` | 等待银行审批（落地待执行，内容未跑） |
| `A` | 银行同意支付（内容已执行） |
| `R` | 银行拒绝支付（单据打回） |
| `null` | 未涉及落地（普通收尾） |

> 查询展示：前端列表统一用字典 `enterpriseLimit.landingResult`（`bt.dict("enterpriseLimit.landingResult", full.landingLimitResult)`）渲染该列，表头国际化 `table.enterpriseLimit.landingResult`。已接入四张查询列表，统一放在"状态"列之后：
>
> | 列表 | VM | JS | 接口 / 取数 SQL |
> |---|---|---|---|
> | 付款办理查询 | `netbank/listNisBillheadQuery.vm` | `nisBillhead.query.js` | `/netbank/nisbillhead/datatable` → `NisBillheadDao.getDataTablesQuery`（`n.LANDING_LIMIT_RESULT`） |
> | 资金收付现金流查询 | `foreign/listFexCashFlowQuery.vm` | `fexCashFlow.listQuery.js` | `/foreign/fexcashflow/datatable` → `FexCashFlowDao.getCommonSql`（`t.LANDING_LIMIT_RESULT`） |
> | 代发工资查询 | `wage/listNisWageQuery.vm` | `nisWage.listQuery.js` | `/wage/niswage/datatable` → `NisWageDao.getDataTablesUnion`（`n.LANDING_LIMIT_RESULT`） |
> | 外汇付款查询（tranType=4） | `foreign/listFexTranReqPayQuery.vm` | `fexTranReqPay.listQuery.js` | `/foreign/fextranreq/datatable` → `FexTranReqDao.getCommonSqlJoinCashFlow`（`f.LANDING_LIMIT_RESULT`） |
>
> 其中前三条取本实体/本表的落地标识；第四条列表主体是 `fex_tran_req`，落地标识取自关联现金流 `fex_cash_flow`，故 `FexTranReqVO` 新增只读属性 `landingLimitResult`（`@BtCompositeSearch(fieldName = "f.LANDING_LIMIT_RESULT")`）并在该 SQL 补列——仅用于展示，不改变"资金收付落地管理点在现金流"的口径（`FexTranReq` 实体/表仍不加字段、无 `completeTranApprove`）。
>
> **补丁（2026-09-17）——交易单侧其余取数 SQL 漏列，落地限额处理结果空白**：`FexTranReqDao` 共有 4 个通用 SQL 构造器，09-12 只在 `getCommonSqlJoinCashFlow`（仅 `tranType=4 && opType='query'` 命中）补了 `f.LANDING_LIMIT_RESULT`，`getCommonSql` / `getCommonSqlJoin` / `getCommonSqlJoinForeign` 三个都没有该列。而交易单**详情**（`getByIdUnion` / `getByIds` / `getByBillNoUnion`）、**审批列表**（`dataTableApprove` / `getDataTablesForeignApprove`）以及**非 `query` 页签**的列表全部回落在这三个构造器上，`FexTranReqVO.landingLimitResult` 恒为 `null`，前端 `bt.dict("enterpriseLimit.landingResult", null)` 取不到值 ⇒ 落地审批被拒 → 重新提交 → 审批通过落到"已审批"页签时该列空白（单据状态 95、银行端处理单待审批均正常）。现已在上述三个构造器补齐该列，写法用**标量子查询**而非再 join 现金流：
>
> ```sql
> (select max(cf.LANDING_LIMIT_RESULT) from fex_cash_flow cf where cf.tran_bill_id = t.id and cf.status <> -2) as landingLimitResult
> ```
>
> 不直接 join 的原因：`getCommonSqlJoin` / `getCommonSqlJoinForeign` 里别名 `f` 已被"流水勾兑状态"聚合子查询占用；且一个交易可能有多条现金流（资金收付为付款 `cdSign=1` + 收款 `cdSign=0` 两行），再 join 会把交易行放大。落地标识只写在付款现金流上、收款行为 `null`，`max()` 忽略 `null` 即取到唯一有效值。
> **范围提示**：现金流本表（`FexCashFlowDao.getCommonSql`）一直带列，所以该现象只在"交易单口径"的页面（资金收付交易列表/详情、审批列表）出现。
>
> 字典（`sys_dict_group` / `sys_dict` / `sys_internation_base` 中英文）脚本见 `sql/saas/mysql/bytter-service-paymentlimit_mysql_dml.sql`、`sql/saas/oracle/bytter-service-paymentlimit_oracle_dml.sql`。
>
> **前后端分离接口的文案字段**：上述四个 VO（`NisBillheadVO` / `FexCashFlowVO` / `NisWageVO` / `FexTranReqVO`）均提供 `landingLimitResultName`，供 `/v1-payment/...` 分离层直接展示，前端无需再接字典。**取值在 Service 层手动查字典回填**（不用 `@Dict` 切面自动注入），一律「批量取一次、循环内取值」避免逐行调用字典服务：
>
> | Service | 回填位置 | 写法 |
> |---|---|---|
> | `NisBillheadService` | `injectValues(vo, listDictMap)`，字典组加进 `listDictMap` 的批量入参 | `listDictMap.get(LANDING_RESULT_DICT)` + `Optional` |
> | `FexCashFlowService` | `injectListValues` 的 `forEach` | `dictService.listMap(...)` + `getOrDefault(code, DictDTO.EMPTY).getName()` |
> | `NisWageService` | `setJoinField` 的 `for` 循环 | 同上 |
> | `FexTranReqService` | `injectValue(List)` 的 `for` 循环 | `dictService.listMap(...)` + `injectLandingLimitResultValue` 辅助方法 |
>
> 字典组 code 统一取常量 `EnterpriseLimitControlService.LANDING_RESULT_DICT`（`= "enterpriseLimit.landingResult"`）。**码值为 null（未涉及落地）或字典组未建**时 `landingLimitResultName` 为空，不写脏值。方法上不加 `@Dict` 注解——两条链路（老 `@Controller` 与 `/v1-payment` `@RestController`）复用同一批 Service 方法，Service 回填一处即两处生效。
>
> 分离层列表接口现状：`POST /v1-payment/foreign/fexcashflow/datatable`（`FexCashFlowVO`）、`POST /v1-payment/netbank/nisbillhead/datatable`（`NisBillheadVO`，另接 `setDtlReceipt`）、`POST /v1-payment/wage/niswage/datatable`（`NisWageVO`）三条，均落到上表的回填点；`/v1-payment/foreign/fextranreq` 目前只有 `GET /view/{id}`，无列表接口（`FexTranReqVO` 的回填随 `injectValue` 一并生效，为后续补接口预留）。
>
> 老页面**保留不动**（已确认）：`fexCashFlow.list.js` / `nisBillhead.list.js` / `nisExpenseDetail.deal.js` 继续走 `bt.lang('enterpriseLimit.landingResult.approved'/'rejected')` 旧写法，不迁到新字典。`NisBillheadService.injectValues` 里原先硬编码中文（0 显示"待银行端审批"）的那段已按本条改造为查字典，文案随之统一为字典的"等待银行限额审批"。

> SQL 迁移：`sql/saas/mysql/bytter-service-paymentlimit_mysql_ddl.sql`（幂等 ALTER，三表补 `LANDING_LIMIT_RESULT`；Oracle 见 `sql/saas/oracle/bytter-service-paymentlimit_oracle_ddl.sql`）。**重要**：`updateFexCashFlowStatus`/`updateNisBillheadStatus`/`updateNisWageStatus` 的大 HQL 均不含 `landingLimitResult`，落地字段一律走各自 `XxxDao.updateLandingLimitResult(id, val)` 单独落库，不改大 HQL。

---

## 5. 交互对象与接口契约（专用 DTO，替代 IWorkFlowResult）

### 5.1 接口①：落地判断（企业端 → 内管端）——内管端实现

| 项 | 值 |
|---|---|
| Dubbo 接口 | `com.bytter.saas.management.exchange.api.service.sys.IApiPaymentLimitService` |
| 方法 | `boolean isLandingApprovalRequired(PaymentLimitContextDto context)` |
| 提供方 | 内管端（对外 jar `bt-saas-management-exchange-client`） |
| 消费方 | 企业端 `EnterpriseLimitControlService`（`@DubboReference(check=false)`） |
| 返回语义 | `true` = 需要落地（内管已自行建案）；`false` = 无需落地 |

入参 `PaymentLimitContextDto`（15 字段快照）字段与各来源组装映射见 `LIANDIAO §3.2`。企业端三来源经各自 `buildEnterpriseLimitContext(...)` 组装；`ctx == null` = 不适用落地 → 直接正常收尾，不发询问。

### 5.2 接口②：落地处理单结果回调 caseWorkflowCallback（内管端 → 企业端，企业端已实现）

| 字段 | 说明 |
|---|---|
| `tenantId` | 租户ID（**银行端限额服务传参必需**，签约客户=租户ID；回调线程据此还原上下文） |
| `sourceBizType` | 来源业务类型（= `BT_TR_LIMIT_CASE.SOURCE_BIZ_TYPE`）：`FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE`；回调门面按此分发 |
| `businessId` | 业务对应 ID（来源业务 sourceBizId） |
| `businessNo` / `corpId` | 业务单号（billNo/wageNo）/ 单位ID |
| `caseNo` / `caseId` | 落地处理单号 / 处理单ID |
| `caseStatus` | 审批状态（2审批通过 / 3打回） |
| `handleResult` | 处理结果（A同意 / R拒绝） |
| `approveUser` / `approveComment` / `approveDate` | 审批人 / 意见 / 时间 |

回调处理（企业端 `caseWorkflowCallback` 门面 → 按 `sourceBizType` 分发到来源业务 handler → `completeTranApprove`）：
- `handleResult=R`（拒绝）或 `caseStatus=3`（审批流打回）→ 落地待执行单 `LANDING_LIMIT_RESULT=R` → 复位 `95→11` → 打回（-1）；
- `handleResult=A`（同意，审批通过）→ 落地待执行单 `LANDING_LIMIT_RESULT=A` → 复位 `95→11` → 审批通过收尾（status->95，发送指令等）；
- 回调线程先**清空内管端用户上下文再按 `tenantId` 还原租户**并**统一在门面释放**：回调请求由内管端发起，`DubboUserProviderFilter` 已把请求附件中的内管端操作员还原成线程用户（连带写入操作员租户）——内管端操作员不是企业端身份，`TranCaseCallbackServiceImpl.caseWorkflowCallback` 入口先 `removeUser()` 清 TTL 用户/租户、`RpcContext.getServerAttachment().clearAttachments()` 清请求附件（防 `getCurrentUser()` 空值兜底从附件复活操作员），再按 `dto.tenantId` `setTTLTenant` 还原企业端租户（2026-09-09）。清空原因：① 业务收尾 `getUserId()`/updateBy 不误写内管端操作员；② 回调线程再调 Dubbo（查 bis_bif 规则等）时 consumer filter 见 currentUser 非空只打包 shiroUser、丢弃租户附件（else if 互斥），企业租户传不到 system——清成"无用户、仅租户"后附件才能带 `ttlTenant` 跨进程。整个分发（含各来源 handler 查询收尾）包 `try/finally` `clearTTLTenant`，成功/异常均清，防止 TTL 租户残留到 Dubbo 线程池污染后续请求（用户线程由 provider filter finally 兜底恢复，无需自理）；三来源 handler 不再各自 set/clear（2026-09-09 收敛为门面单点，曾试过 handler 级、已还原）；
- 幂等：落地待执行单（95+"0"）复位收尾；已 95/100 仅记日志；不在 11 且非落地待执行抛异常。

---

## 6. 企业端改造点（bt-saas / bytter-service-payment）

| 位置 | 改动 |
|---|---|
| **资金收付现金流（FexCashFlow）** | `FexCashFlowService`：`flowCompleted` **顶层落地门**（`pass && isLandingApprovalRequired(entity,user)` → `markApprovePassForLanding` + 记消息 + return；否则 `doFlowCompleted`）；抽离 `doFlowCompleted` = 原收尾段（含 `saveBisForeignExc` 发指令）；新增 `completeTranApprove`/`markApprovePassForLanding`/`isLandingPending`/`resetLandingPendingToWaitCheck`/`buildFallbackUser`；`buildEnterpriseLimitContext`（已存在，返回 null 表示不适用） |
| **付款办理（NisBillhead）** | `NisBillheadService`：注入 `EnterpriseLimitControlService`；`isLandingLimitReached` 真实现（`buildEnterpriseLimitContext` → `isLandingApprovalRequired`）；`markApprovePassForLanding` 加 `updateLandingLimitResult(id,"0")`；`completeTranApprove` 加落地待执行分支（置 A/R + 复位 95→11）；新增 `buildEnterpriseLimitContext`/`isLandingPending`/`resetLandingPendingToWaitCheck`/`maskEnterpriseLimitAccount`/`resolveEnterpriseLimitTenantId` |
| **自动支付策略（autoPayBill）** | `NisBillheadService.autoPayBill`（返回 `PaymentLimitResultDto`）：限额占用失败回滚并返回失败结果；命中落地则单据 `0→95` + `LANDING_LIMIT_RESULT=0` 预置（不生成指令、事务提交），返回失败结果（码 `LANDING_APPROVAL_PENDING`）供任务记录后跳过；抽离指令段 `saveAutoPayExc`（非落地与落地回调通过共用）；回调收尾 `completeTranApproveAutoPay`（经付款单落地 handler 按 `isAutoPay=YES` 分流：通过=执行指令段保持 95；打回=95→-1 + 释放占用） |
| **代发工资（NisWage）** | `NisWage` 实体 + `NisWageVO` + `NisWageDao`（3 SELECT 补列 + `updateLandingLimitResult`）新增 `landingLimitResult`；`NisWageService` 同 NisBillhead 接入（`buildEnterpriseLimitContext` 的 `accountId` 用 `getBankAccByBankAccAndCurId(payerAcc,curId).getId()`，`businessDate` 用 `wageDay!=null?wageDay:wishPayday`，`sourceBizType=NIS_WAGE`） |
| **回调 handler（三来源）** | `FexCashFlowTranCaseCallbackHandler`（新建，`FEX_CASH_FLOW`，删旧 `FexTranReqTranCaseCallbackHandler`）；`NisBillheadTranCaseCallbackHandler`（`NIS_BILLHEAD`）；`NisWageTranCaseCallbackHandler`（`NIS_WAGE`）。三 handler 统一守卫逻辑（§3.3） |
| **回调门面 + SPI** | `ITranCaseCallbackService.caseWorkflowCallback(TranCaseCallbackDTO)` 按 `sourceBizType` 分发（**必传**，缺省报错）；`ITranCaseCallbackHandler` 各来源实现 |
| **落地判断封装** | `EnterpriseLimitControlService`：`BIZ_TYPE_FEX_CASH_FLOW`/`BIZ_TYPE_NIS_BILLHEAD`/`BIZ_TYPE_NIS_WAGE`、`LANDING_RESULT_PENDING/AGREE/REJECT`（`0/A/R`）、`isLandingApprovalRequired(ctx)`（`@DubboReference` 消费内管接口） |
| 收尾 | 各来源业务自身 `approvePassEnd`/`doFlowCompleted`（不打通用化；只通用化"落地判断 + 回调分发"） |

> 落地审批只发生在**企业端审批通过后**；各业务收尾逻辑保持各自原有实现（仅命中落地时由处理单结果驱动收尾）。

---

## 7. 内管端落地限额职责（供联调对齐；完整接口契约见 `TRAN_APPROVE_CONTROL_MANAGEMENT_IFACE.md`）

> ⚠️ 本块**不由企业端开发**；企业端仅引入 exchange-client 消费其对外接口（接口①），后续联调。以下为内管端（bt-saas-management）职责：

- **接口①【落地判断 `isLandingApprovalRequired`】**：接收企业端 `PaymentLimitContextDto` 快照，做落地规则匹配；**需要落地时返回 `true` 并自行创建内管审批（BT_TR_LIMIT_CASE）**（定位字段 `SOURCE_BIZ_TYPE`/`SOURCE_BIZ_ID`/`BUSINESS_NO` + `TENANT_ID`，见 §4.1）；返回 `true` 时**不向企业端回传 caseId**；
- 处理单审批流（同意 / 拒绝 / 打回）＋ 出结果后 **接口② `caseWorkflowCallback` 回调企业端**（调用企业端 Dubbo `ITranCaseCallbackService.caseWorkflowCallback`，按 `sourceBizType` 分发）；
- 落地规则/处理单/审批流/命中快照等维护（内管端职责）。落地命中**判定在内管端**（企业端只传快照，不判规则）。

> 旧 ad-hoc 类（`IManageFexTranReqService`/`ManageFexTranReqApproveService`/`ManageFexTranApprove` 等）为**企业端早期临时方案**，已废弃，由内管端开发时替换删除。
> ⚠️ **联调前必须处理**：企业端回调门面已改为通用 `ITranCaseCallbackService`（替换原 `IFexTranReqService`），且已移除 `executeTranAfterApprove`/`TranApproveResultDTO`，`bt-saas-management` 中旧 `ManageFexTranReqApproveService`（调 `executeTranAfterApprove`）在换新 jar 后**编译会失败**，需一并移除。

---

## 8. 待确认 / 风险点

| # | 事项 | 说明 |
|---|---|---|
| 1 | **`isLandingApprovalRequired` 返回 `true` 语义** | = "已命中且内管已建案"（企业端据此只预置 95 + 落地标识=0，等回调）；需与内管确认（vs `checkLandingAndCreateCase`，本轮不用后者） |
| 2 | **落地相关 DDL 入库** | `BT_TR_LIMIT_CASE` 等落地相关表尚未合入真实仓库（银行端限额服务开发时处理） |
| 3 | **signedCustId 口径** ✅已明确 | = 企业端租户ID；`bankOrgId` 企业端不处理 |
| 4 | **审批流** | 银行同意支付走审批流需在银行端流程设计器配置；`BT_TR_LIMIT_CASE.PROCESS_INSTANCE_ID` 关联（银行端职责） |
| 5 | **落地标识 `0` 占位** | 企业端内部占位（内管不感知）；`A`/`R` 与内管 `HANDLE_RESULT` 对齐，需确认不与内管回调 DTO/字典冲突 |
| 6 | **事务边界** | 企业端回调收尾含 DB 写；银行端建议**提交后异步回调**，避免事务内长链路相互占用/回滚牵连（沿用 SCF rollbackOnly 教训） |
| 7 | **多来源落地口径** ✅已接线 | 资金收付现金流（`FEX_CASH_FLOW`，`tranType=4`）与付款办理（`NIS_BILLHEAD`）、代发工资（`NIS_WAGE`）已接入；付款办理仅"线上生成银行指令"付款触发（isAccount=0 且结算方式∈{02,33,34,35,48} 且 isPayed≠1），代发工资仅"接口规则在线且付款账户在线"触发。**费用报销（expense）不设独立来源**：报销单自身审批不生成银行指令，报销款由"付款办理（对私）billhead"承载 → 落地随 `NIS_BILLHEAD` 接线已覆盖 |
| 8 | **NisBillhead/NisWage `accountId` 口径** | 付款办理用 `getAccId()`，代发工资用 `getBankAccByBankAccAndCurId(payerAcc,curId).getId()`，与内管对齐确认 |

---

## 9. 变更清单（Checklist）

**企业端（已完成）**
- [x] 清理企业端 ad-hoc 内管端限额代码（`routeManageTranControl` / `flowCompletedFromManage` / `executeTranAfterApprove`）
- [x] 回调接口 `caseWorkflowCallback(TranCaseCallbackDTO)`，替换 `executeTranAfterApprove`；回调实现还原租户上下文（签约客户=租户ID）
- [x] 回调门面通用化 `ITranCaseCallbackService`（按 `sourceBizType` 分发）＋落地限额回调 SPI `ITranCaseCallbackHandler`
- [x] 清理遗留 echo/查询接口（`testReturn`/`getTranReqDetail`/`queryDailyTranUsage` 及配套 DTO、`getDailyUsage` 取数）
- [x] **资金收付落地管理点纠正到现金流**：`FexCashFlowService.flowCompleted` 顶层落地门 + `doFlowCompleted` 抽离 + `completeTranApprove`/`markApprovePassForLanding`/`isLandingPending`/`resetLandingPendingToWaitCheck`；删 `FexTranReqTranCaseCallbackHandler`，新建 `FexCashFlowTranCaseCallbackHandler`（`FEX_CASH_FLOW`）；回退 `FexTranReqService`/`FexTranReqDao`（不加字段、无 `completeTranApprove`）
- [x] **付款办理（NIS_BILLHEAD）接线**：`NisBillheadService` 落地判断 + `markApprovePassForLanding` 加 `"0"` + `completeTranApprove` 落地待执行分支；`NisBillheadTranCaseCallbackHandler`（`NIS_BILLHEAD`）
- [x] **代发工资（NIS_WAGE）接线**：`NisWage`/`NisWageVO`/`NisWageDao` 新增 `landingLimitResult` + `updateLandingLimitResult`；`NisWageService` 落地判断 + 收尾分支；`NisWageTranCaseCallbackHandler`（`NIS_WAGE`）
- [x] `EnterpriseLimitControlService`：三来源 `BIZ_TYPE_*` 常量 + 落地标识 `0/A/R` 常量 + `isLandingApprovalRequired(ctx)`（`@DubboReference` 消费内管接口）
- [x] SQL 迁移：`sql/saas/mysql/bytter-service-paymentlimit_mysql_ddl.sql`（三表幂等补 `LANDING_LIMIT_RESULT CHAR(1)`；Oracle 同名 `_oracle_ddl.sql`）
- [x] **落地限额处理结果字典**：新增字典组 `enterpriseLimit.landingResult`（`0` 等待银行限额审批 / `A` 银行同意支付 / `R` 银行拒绝支付）＋ 字典项中英文，三张查询列表（付款办理 / 资金收付现金流 / 代发工资）表头与列渲染接入
- [x] 文档同步（DESIGN / LIANDIAO / MANAGEMENT_IFACE）到新口径

**待联调（内管端）**
- [ ] 内管端（bt-saas-management）：实现 `IApiPaymentLimitService.isLandingApprovalRequired(PaymentLimitContextDto)`（判断 + 自行建案）＋ 处理单审批 ＋ 处理单结果回调企业端 `caseWorkflowCallback`（接口②）
- [ ] 内管端（bt-saas-management）：替换删除旧 ad-hoc 类（`IManageFexTranReqService` / `ManageFexTranReqApproveService` / `ManageFexTranApprove`）
- [ ] 共享库执行落地相关 `BT_TR_*` DDL
- [ ] 联调：企业端 ↔ 内管端（bt-saas-management）：审批通过 → 调 `isLandingApprovalRequired` → 内管自行建案 → 审批 → 接口② `caseWorkflowCallback` 回企业端收尾
