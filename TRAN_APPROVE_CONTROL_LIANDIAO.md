# 落地限额 联调对接文档（企业端 ↔ 内管端）——多来源业务共用

> 配套设计文档：`TRAN_APPROVE_CONTROL_DESIGN.md`
> **术语对照：本文"内管端" = 原"银行端限额服务" = bt-saas-management**（落地限额/处理单审批归属方，对外 jar `bt-saas-management-exchange-client`）。
> 本文只描述**落地限额**这一轮两边要对接的**接口契约 / 调用关系**，方便双方开发直接对接口、对参数、对状态。**不包含**内管端落地判断/审批内部规则/台账/审批流设计，也不包含企业端内部非接口改造。
> 日期：2026-09-02（🔄 **2026-09-03 更新**：交互口径改为——企业端**不再主动"生成落地处理单"**，只在审批通过后调 `isLandingApprovalRequired` **询问**；内管端自行判断、自行建案、自行审批，出结果后回调企业端。落地对象/词汇/标识见 §0 / §2 / §3）

---

## 0. 一句话口径（联调前提）

- 触发点：**企业端审批通过后、生成银企指令前**，调内管端 `IApiPaymentLimitService.isLandingApprovalRequired(PaymentLimitContextDto)` **询问是否需要落地审批**；内管端**自行判断 + 自行创建内管审批（BT_TR_LIMIT_CASE）**，企业端**不建 case、不维护 caseId**，只等结果回调。
- 命中落地 → 企业端把单据**预置为审批通过（status 11→95）但内容（发指令/资金计划/现金流）延后**，落地标识 `LANDING_LIMIT_RESULT` 置 `0`（待执行）。
- 内管端出结果 → 回调企业端 `ITranCaseCallbackService.caseWorkflowCallback(TranCaseCallbackDTO)` → 企业端按落地标识幂等收尾。
- 落地限额**只对"审批通过后会生成线上银行支付指令"的单据生效**，线下不走。

---

## 1. 双方角色与接口方向

| 角色 | 服务 / 仓库 | 归属 |
|---|---|---|
| **企业端** | `bt-saas` → `bytter-service-payment`（`bytter-payment-client` 提供接口 jar） | 本仓库 / 本设计已就绪 |
| **内管端（内管端限额服务 / bt-saas-management）** | 落地处理单（`BT_TR_LIMIT_CASE`）归属方；对外经 `bt-saas-management-exchange-client` 提供接口 | **内管端实现：接口① `isLandingApprovalRequired`（判断+建案）、接口②回调待内管端调用** |

```
企业端 (bytter-service-payment)                               内管端 (bt-saas-management)
  企业端审批通过(flowCompleted)
   ① 调 isLandingApprovalRequired(PaymentLimitContextDto) ──▶  内管自行判断 + 自行创建内管审批(BT_TR_LIMIT_CASE)
        ├─ false（无需落地）→ 正常收尾：11→95 + 资金计划 + 现金流/指令
        └─ true（需要落地）→ 企业端 markApprovePassForLanding：
             单据 11→95（内容延后）+ LANDING_LIMIT_RESULT=0（等待银行审批）
  ② 落地处理单结果 ◀──── caseWorkflowCallback ─────────────────  [接口② 内管端→企业端，企业端已实现、内管端待调用]
      → 按 sourceBizType 分发 handler：落地待执行(95+0) → 置 A/R + 复位 95→11 → 真正收尾
```

- **接口①（企业端 → 内管端）**：企业端只调 `isLandingApprovalRequired` **询问**；是否落地、建案、审批全部内管负责（§3）。
- **接口②（内管端 → 企业端）**：落地处理单结果回调 `caseWorkflowCallback`。企业端**已实现并对外暴露**，内管端处理单出结果后调用（§2）。

---

## 2. 接口②：落地处理单结果回调（内管端 → 企业端）——企业端已就绪

### 2.1 调用坐标

| 项 | 值 |
|---|---|
| Dubbo 接口 | `com.bytter.biz.payment.api.service.ITranCaseCallbackService` |
| 方法 | `void caseWorkflowCallback(TranCaseCallbackDTO dto)` |
| 提供方应用 | `bytter-service-payment`（`bootstrap.yml` `spring.application.name`） |
| 接口所在 jar | `com.bytter:bytter-payment-client:2.0.0.RELEASE` |
| 暴露注解 | 实现类 `@DubboService`（默认 group/version） |
| 消费方写法（内管端参考） | 引入 `bytter-payment-client` 依赖 + `@DubboReference(check=false) ITranCaseCallbackService` |
| 调用时机 | 内管端对 `BT_TR_LIMIT_CASE` 给出结果（同意/拒绝/审批流打回）后 |

### 2.2 入参 DTO：`TranCaseCallbackDTO` 字段契约

> 内管端需按下表**逐字段落 BT_TR_LIMIT_CASE / 审批流结果**后回调。

| 字段 | 类型 | 必填 | 说明 / 取值 |
|---|---|---|---|
| `tenantId` | String | **是** | 签约客户ID = **企业端租户ID**；企业端回调线程用它还原租户上下文 |
| `sourceBizType` | String | **是** | 来源业务类型：`FEX_CASH_FLOW`（资金收付现金流）/ `NIS_BILLHEAD`（付款办理）/ `NIS_WAGE`（代发工资）；**缺省直接报错**；传了不支持的取值企业端直接抛异常 |
| `businessId` | String | 见下 | 业务对应ID = 来源业务 sourceBizId（现金流ID / 付款单ID / 代发工资单ID） |
| `businessNo` | String | 见下 | 业务单号（billNo / wageNo） |
| `corpId` | String | 否 | 单位ID。传了企业端会校验单据确属该单位，不符抛异常 |
| `caseId` | String | 否 | 落地处理单ID（`BT_TR_LIMIT_CASE.ID`） |
| `caseNo` | String | 否 | 落地处理单号（`BT_TR_LIMIT_CASE.CASE_NO`） |
| `caseStatus` | Integer | 建议传 | 审批状态：`2`=审批通过 / `3`=打回 |
| `handleResult` | String | 建议传 | 处理结果：`A`=同意 / `R`=拒绝 |
| `approveUser` | String | 否 | 审批人登录名；为空时企业端取单据 `updateBy` 兜底 |
| `approveComment` / `approveDate` | String / Date | 否 | 审批意见 / 时间（当前仅透传，不落业务） |

> **必填口径**：`businessId` 与 `businessNo` 企业端**二选一**即可定位单据（businessId 优先）；两者都空才报"缺少业务对应ID/业务单号"。**建议两者都传**，最稳。

### 2.3 收尾方向判定（企业端按回调内容决定"通过 or 打回"）

| 内管端动作 | 应传值 | 企业端行为 |
|---|---|---|
| **拒绝支付** | `handleResult=R`（或 `caseStatus=3`） | 打回收尾：落地待执行单 `LANDING_LIMIT_RESULT=R` → 复位 `95→11` → 原收尾 `11→-1`（可修改）；普通 11 单直接打回 |
| **同意支付·审批通过** | `handleResult=A` 或 `caseStatus=2` | 审批通过收尾：落地待执行单 `LANDING_LIMIT_RESULT=A` → 复位 `95→11` → 原收尾 `11→95` + 资金计划 + **生成现金流并发送指令** |
| **审批流打回** | `caseStatus=3` | 同"打回收尾"（`LANDING_LIMIT_RESULT=R`） |

判定实现（内管端据此核对语义）：
```
pass = "A".equals(handleResult) || caseStatus == 2
```
即 `handleResult=A 或 caseStatus=2` 任一命中即按"审批通过"收尾；其余按"打回"。

### 2.4 幂等与状态前置（内管端需了解）

企业端 `handle` 内对单据当前状态做前置判断，核心区分"**落地待执行**（status=95 且 `LANDING_LIMIT_RESULT=0`）"与"普通待审批（11）"：

| 单据当前状态 | 回调行为 |
|---|---|
| `95` 且 `LANDING_LIMIT_RESULT=0`（落地待执行，等待银行） | 置 `A`/`R` → 复位 `95→11` → 复用原收尾逻辑真正收尾 |
| `95`（已收尾，可能重复回调） | 不重复收尾，仅记**幂等日志**，正常返回 |
| `11`（普通待审批） | 直接执行收尾（§2.3，无复位） |
| 其他（如已 `-1` 打回、已 `0` 暂存等） | 抛异常"单据不在待审批状态" |

> 结论：**重复回调同一结果不会重复收尾**；但若单据已被另一路径收尾（如已打回），再回调会报错——内管端应保证回调时机正确、并对异常有处理/重试策略。

### 2.5 线程 / 事务约定

- 回调发生在**内管端发起的调用线程**；`DubboUserProviderFilter` 已把请求附件的内管端操作员还原成线程用户（连带操作员租户），它不是企业端身份——门面入口**先清空再还原**：`removeUser()` 清 TTL 用户/租户 + `RpcContext.getServerAttachment().clearAttachments()` 清请求附件（防 `getCurrentUser()` 空值兜底复活操作员），再按 `tenantId` `setTTLTenant` 还原企业端租户（2026-09-09）。清空原因：① 业务收尾不把内管端操作员写成当前用户（updateBy 等）；② 回调线程再调 Dubbo 时 consumer filter 见 currentUser 非空只打包 shiroUser、丢租户附件（else if 互斥），租户传不到 system——清成"无用户、仅租户"后附件才能带 `ttlTenant` 跨进程。
- **租户释放（门面统一）**：`TranCaseCallbackServiceImpl.caseWorkflowCallback` 还原租户后整个分发（含三来源 handler 查询收尾）包 `try/finally` 释放（成功/异常均清），防 TTL 租户残留 Dubbo 线程池污染后续请求（2026-09-09 收敛为门面单点；用户线程由 provider filter finally 兜底恢复，业务代码无需自理）。
- 企业端收尾含 DB 写操作；内管端建议**不要在自己事务内同步长链路调用本回调**，避免相互占用/回滚牵连（沿用 SCF rollbackOnly 教训：回调尽量提交后异步触发）。

### 2.6 相关代码位置（企业端）

- 接口：`bytter-payment-client/.../api/service/ITranCaseCallbackService.java`
- DTO：`bytter-payment-client/.../api/dto/TranCaseCallbackDTO.java`
- 门面实现：`bytter-payment-domain/.../foreign/service/impl/TranCaseCallbackServiceImpl.java`
- 分发 SPI：`bytter-payment-domain/.../payment/control/ITranCaseCallbackHandler.java`
- 资金收付 handler：`bytter-payment-domain/.../payment/control/FexCashFlowTranCaseCallbackHandler.java`（`sourceBizType=FEX_CASH_FLOW`）
- 资金收付收尾：`bytter-payment-domain/.../foreign/service/FexCashFlowService.java#completeTranApprove / doFlowCompleted / flowCompleted`
- **付款办理 handler**：`bytter-payment-domain/.../payment/control/NisBillheadTranCaseCallbackHandler.java`（`sourceBizType=NIS_BILLHEAD`）
- **付款办理收尾**：`bytter-payment-domain/.../netbank/service/NisBillheadService.java#completeTranApprove / approvePassEnd`
- **代发工资 handler**：`bytter-payment-domain/.../payment/control/NisWageTranCaseCallbackHandler.java`（`sourceBizType=NIS_WAGE`）
- **代发工资收尾**：`bytter-payment-domain/.../wage/service/NisWageService.java#completeTranApprove / approvePassEnd / getByWageNo`
- 落地判断封装（消费内管接口）：`bytter-payment-domain/.../limit/service/EnterpriseLimitControlService.java`

---

## 3. 接口①：落地判断（企业端 → 内管端）——内管端实现、企业端已接入调用

> **核心变化（2026-09-03）**：企业端**不再主动"生成落地处理单"**。只在审批通过后调 `isLandingApprovalRequired` **询问**；内管端返回 `true` 即表示**已命中且内管已自行建案**，企业端据此只做"预置 95 + 落地标识=0"并等回调。

### 3.1 调用坐标

| 项 | 值 |
|---|---|
| Dubbo 接口 | `com.bytter.saas.management.exchange.api.service.sys.IApiPaymentLimitService` |
| 方法 | `boolean isLandingApprovalRequired(PaymentLimitContextDto context)` |
| 提供方 | 内管端（bt-saas-management，对外 jar `bt-saas-management-exchange-client`） |
| 消费方 | 企业端 `EnterpriseLimitControlService`（`@DubboReference(check=false)`），经 `buildEnterpriseLimitContext` 组装 `PaymentLimitContextDto` 后调用 |
| 返回语义 | `true` = 需要落地审批（内管端**已自行建案**）；`false` = 无需落地，企业端正常收尾 |

### 3.2 入参 `PaymentLimitContextDto`（内管端定义，企业端按来源业务组装）

| 字段 | 类型 | 说明 | 企业端取值（按来源） |
|---|---|---|---|
| `tenantId` | String | 租户ID = 签约客户ID | `UserContextHelper` 租户上下文 |
| `bankOrgId` | String | 银行机构ID（签约银行机构） | 企业端不填（内管端自行解析维护） |
| `sourceBizType` | String | 来源业务类型 | `FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE` |
| `sourceBizId` | String | 来源单据ID | 资金收付现金流ID / 付款单ID / 代发工资单ID |
| `businessNo` | String | 业务单号 | `billNo` / `wageNo` |
| `unitId` | String | 付款单位ID | `corpId` |
| `accountId` | String | 付款账户ID | 账户ID（付款办理用 `accId`，代发工资用付款账户 `getId()`） |
| `accountMask` | String | 脱敏付款账号 | 前后各 4 位、中间 `****` |
| `currencyId` / `currencyCode` | String | 币别ID / 币别英文编码 | `curId` / 币别 `englishCode` |
| `amount` | BigDecimal | 付款金额 | 单据原币金额（`payMoney` / `wages`） |
| `businessDate` | Date | 业务日期/期望支付日期 | `wishPayday`（代发工资优先 `wageDay`，资金收付取 `settleDate`/`cashFlowDate`） |
| `bizStatus` | String | 业务单据状态 | `String.valueOf(status)` |
| `operatorId` / `operatorName` | String | 操作人ID / 名称 | 审批人（空则内管忽略） |

### 3.2.1 已接线来源业务（同一 `isLandingApprovalRequired` 判断，仅 ctx 组装不同）

| 来源业务 | `sourceBizType` | 触发口径（哪些单据才判断落地） | 落地对象 |
|---|---|---|---|
| 资金收付交易 | `FEX_CASH_FLOW` | 仅 `tranType=4`（生成汇款指令的现金流），且付款账户在线、非线下、金额>0 | **现金流 FexCashFlow** |
| **付款办理** | `NIS_BILLHEAD` | **仅"线上生成银行指令"的付款**：不记账(isAccount=0) 且 结算方式∈{02,33,34,35,48} 且 isPayed≠1；含报销对私付款 | 付款单 billhead |
| **代发工资** | `NIS_WAGE` | **仅"线上生成银行指令"的代发工资**：接口业务规则在线(isBankOnline=YES) 且 付款账户在线 | 代发工资单 wage |

> 三个来源都经各自 `buildEnterpriseLimitContext(...)` 组装；`ctx == null` 表示"不适用落地"（非线上/账户不在线/金额为0等）→ 企业端**直接正常收尾**，不发落地询问。落地命中判定**全在内管端**（企业端只传快照，不判规则）。

### 3.3 接口① 语义（内管端实现要点）

- 内管端基于 `PaymentLimitContextDto` 做落地规则匹配；**需要落地时返回 `true`，且内管端自行创建落地处理单/审批**（`BT_TR_LIMIT_CASE`，定位字段用 §3.2 的 `sourceBizType`/`sourceBizId`/`businessNo` + `tenantId`）。
- 内管端需**保存 `sourceBizType` 原值**，出结果回调企业端时**原样回传**（企业端据此分发）。
- 企业端拿到 `true` 后**不向企业端回传 caseId/caseNo**；内管端也不依赖企业端任何"生成处理单"结果。
- **落地校验未命中（`false`）**企业端正常收尾，不进入落地流程。

---

## 4. 联调场景用例（建议两方按顺序走）

> 企业端单据状态口径：`0 暂存 / 11 待审批(审批中·暂停) / -1 打回 / 95 审批通过`。落地预置后单据为 `95` 且 `LANDING_LIMIT_RESULT=0`。

> 查询展示口径：前端统一用字典 `enterpriseLimit.landingResult` 渲染 `landingLimitResult`：`0` 等待银行限额审批、`A` 银行同意支付、`R` 银行拒绝支付；表头国际化 `table.enterpriseLimit.landingResult`。已接入付款办理查询、资金收付现金流查询、代发工资查询、外汇付款查询（tranType=4，接口 `/foreign/fextranreq/datatable`）四张列表（列放在"状态"之后）；第四条列表主体是 `fex_tran_req`，落地标识取自关联现金流 `fex_cash_flow`，接口已补 `f.LANDING_LIMIT_RESULT as landingLimitResult` 列。
>
> **补丁（2026-09-17）— 交易单口径列表/详情该列为空**：`FexTranReqDao` 的 4 个通用 SQL 构造器中，此前只有 `getCommonSqlJoinCashFlow`（仅 `tranType=4 && opType='query'` 命中）带 `f.LANDING_LIMIT_RESULT`；交易单详情（`getByIdUnion`/`getByBillNoUnion`）、审批列表（`dataTableApprove`/`getDataTablesForeignApprove`）及非 `query` 页签回落到的 `getCommonSql`/`getCommonSqlJoin`/`getCommonSqlJoinForeign` 均缺列，接口返回 `landingLimitResult=null`，前端字典取不到值 ⇒ 表现为"落地审批被拒 → 重新提交 → 审批通过后落地限额处理结果空白"（内管侧处理单、企业端库内 `LANDING_LIMIT_RESULT=0` 都是对的，**仅接口取数缺列**）。现三个构造器统一用标量子查询补列：`(select max(cf.LANDING_LIMIT_RESULT) from fex_cash_flow cf where cf.tran_bill_id = t.id and cf.status <> -2) as landingLimitResult`（避免 join 放大交易行；落地标识只写在付款现金流上）。现金流本表查询不受影响。
>
> 前后端分离接口：四个 VO 除 `landingLimitResult`（码值 `0`/`A`/`R`）外另返回 `landingLimitResultName`（当前语言文案），分离层前端直接展示该字段即可，不必再接字典。**由 Service 层手动查字典回填**（`EnterpriseLimitControlService.LANDING_RESULT_DICT`，批量取一次再循环塞值）：`NisBillheadService.injectValues`（字典组并入 `listDictMap`）、`FexCashFlowService.injectListValues`、`NisWageService.setJoinField`、`FexTranReqService.injectValue`。码值为 null（未涉及落地）或字典组未建时该字段为空（不返回脏值）。

| # | 场景 | 落地判断 / 接口①②行为 | 预期企业端结果（验证点） |
|---|---|---|---|
| 1 | 审批通过不落地 | `isLandingApprovalRequired` 返回 `false` | 单据 `11→95`，资金计划审批通过、现金流生成、指令正常发送 |
| 2 | 审批通过命中落地 | `isLandingApprovalRequired` 返回 `true`（内管已建案） | 单据 `11→95` + `LANDING_LIMIT_RESULT=0`；**无现金流/指令**、资金计划不动；记录"已提交银行端落地限额审批，审批通过后自动生效"；处理单定位字段 `SOURCE_BIZ_TYPE/SOURCE_BIZ_ID/BUSINESS_NO` 正确 |
| 3 | 场景2 → 银行**拒绝支付** | 接口② `handleResult=R` | `LANDING_LIMIT_RESULT=R` → 复位 `95→11` → 原收尾 `11→-1` 打回、可重新编辑 |
| 4 | 场景2 → 银行**同意·审批通过** | 接口② `handleResult=A` + `caseStatus=2` | `LANDING_LIMIT_RESULT=A` → 复位 `95→11` → 原收尾 `11→95`，现金流生成 + 指令发送 |
| 5 | 场景2 → 银行**审批流打回** | 接口② `caseStatus=3` | 同场景3打回（`LANDING_LIMIT_RESULT=R`） |
| 6 | 重复回调（同一结果再调） | 落地预置单已收尾后再回调 | 不重复收尾，仅幂等日志，正常返回 |
| 7 | 单据状态不合法回调 | 单据已打回后回调 | 企业端抛"单据不在待审批状态"；内管端需捕获/重试策略 |
| 8 | **自动支付·命中落地** | 自动支付任务 `autoPayBill`：限额占用通过后 `isLandingApprovalRequired` 返回 `true` | 单据 `0→95` + `LANDING_LIMIT_RESULT=0`（不生成指令、事务提交）；返回失败结果（码 `LANDING_APPROVAL_PENDING`），任务日志记录后跳过本笔、不减可用余额 |
| 9 | 场景8 → 银行同意 / 拒绝 | 接口②回调（`A` / `R`），付款单落地 handler 按单据 `isAutoPay=YES` 分流 | `completeTranApproveAutoPay` 收尾：同意→执行自动支付指令段（单据保持 95，指令生成+记账）；拒绝→95→-1 打回并释放限额占用（无资金计划/票据收尾） |

---

## 5. 沟通确认清单（内管端开发前逐条确认）

- [ ] `isLandingApprovalRequired` 返回 `true` 的语义 = "已命中且内管已建案"（企业端据此只预置 95 + 落地标识=0，等回调）。
- [ ] 接口② `sourceBizType` 三取值（`FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE`）与 `handleResult`/`caseStatus` 组合语义。
- [ ] 接口② `handleResult` / `caseStatus` 组合语义：企业端按"`A` 或 `2` 任一即通过"判定，确认内管端同意且审批通过时**两个字段都会带上**。
- [ ] 接口② 定位字段：`businessId`（单据ID）与 `businessNo`（billNo/wageNo）企业端二选一即可，建议内管端**两者都传**。
- [ ] 落地标识 `0` 为企业端内部占位（内管不感知）；`A`/`R` 与内管 `BT_TR_LIMIT_CASE.HANDLE_RESULT` 对齐。
- [ ] 资金收付落地对象为**现金流 FexCashFlow**（非交易 FexTranReq）；回调定位字段为现金流 `id`/`billNo`。
- [ ] **联调环境**：Dubbo 注册中心（Nacos namespace）、两边应用都要在同一环境可互通。
- [ ] 时序上接口② **不得在企业端事务内被同步长链路调用**，避免回滚牵连（建议内管端提交后异步回调）。

---

## 6. 范围外（本轮不做，避免联调误期）

- 内管端落地规则维护 / 处理单审批流配置 / `BT_TR_LIMIT_CASE` 等落地相关建表——内管端职责。
- 费用报销（`expense`）独立来源接线——**不设独立来源**：报销单自身审批不生成银行指令，报销款经"付款办理（对私）billhead"承载、落地已随 `NIS_BILLHEAD` 覆盖。
