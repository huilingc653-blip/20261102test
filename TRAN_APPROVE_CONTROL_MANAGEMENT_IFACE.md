# 落地限额审批 企业端 ↔ 内管端 接口联动契约（内管端实现用）

> **读者**：内管端（bt-saas-management / 银行端限额服务）落地限额开发同事。
> **用途**：描述企业端（bt-saas / bytter-service-payment）与内管端之间落地限额审批的联动方式，供内管端实现落地判断、
> 建案、审批与回调。**核心变化（2026-09-03）：企业端不再主动"生成落地处理单"，只调 `isLandingApprovalRequired` 询问，
> 由内管端自行判断、自行建案、自行审批，出结果后回调企业端。**
>
> **术语**：本文件"**内管端**" = 银行端限额服务 = bt-saas-management；"**企业端**" = bt-saas（bytter-service-payment）。
> **落地处理单**归属内管端，表 `BT_TR_LIMIT_CASE`。
>
> 配套参考：企业端设计 `TRAN_APPROVE_CONTROL_DESIGN.md`、联调/确认清单 `TRAN_APPROVE_CONTROL_LIANDIAO.md`。

---

## 0. 一句话口径

- **企业端审批通过后、生成银企指令前**，调内管端 `IApiPaymentLimitService.isLandingApprovalRequired(PaymentLimitContextDto)` **询问是否需要落地审批**。
- **内管端自行判断 + 自行创建内管审批（BT_TR_LIMIT_CASE）**，企业端**不建 case、不维护 caseId**，只等结果回调。
- 命中落地 → 企业端把单据**预置为审批通过（status 11→95）但内容（发指令/资金计划/现金流）延后**，落地标识 `LANDING_LIMIT_RESULT` 置 `0`（待执行）。
- 内管端出结果 → 回调企业端 `ITranCaseCallbackService.caseWorkflowCallback(TranCaseCallbackDTO)` → 企业端按落地标识幂等收尾。

---

## 1. 联动模型

落地限额只作用于**企业端审批通过后、会生成线上银行支付指令**的单据（资金收付交易现金流 / 付款办理·含对私报销 / 代发工资）。

```
企业端 (bytter-service-payment)                         内管端 (bt-saas-management)
审批通过 flowCompleted（仅线上发指令单据）
  └─① 调 isLandingApprovalRequired(PaymentLimitContextDto) ─▶  内管自行判断 + 自行创建内管审批(BT_TR_LIMIT_CASE)
        ├─ false（无需落地）→ 正常收尾：11→95 + 内容（资金计划/现金流/发指令）
        └─ true（需要落地）→ 企业端 markApprovePassForLanding：
             单据 11→95（内容延后）+ LANDING_LIMIT_RESULT=0（等待银行审批）
             → 记录"已提交银行端落地限额审批，审批通过后自动生效"
     ③ 结果回调 caseWorkflowCallback ◀──── Dubbo ─────────────  内管出结果后调用（企业端已实现）
        → 按 sourceBizType 分发 handler：LANDING_LIMIT_RESULT=0（落地待执行）
          → 置 A/R + 复位 95→11 → 真正收尾（同意→95 内容执行 / 拒绝→-1 打回）
```

- **① 落地判断（企业端 → 内管端）**：企业端只调 `isLandingApprovalRequired` 询问；是否落地、建案、审批全部内管负责——**本文件 §2**。
- **③ 结果回调（内管端 → 企业端）**：企业端已实现并对外暴露 Dubbo `ITranCaseCallbackService.caseWorkflowCallback`，内管端出结果后调用——**本文件 §3**。

---

## 2. 接口①：落地判断（企业端 → 内管端）——内管端实现、企业端已接入调用

### 2.1 调用坐标

| 项 | 值 |
|---|---|
| Dubbo 接口 | `com.bytter.saas.management.exchange.api.service.sys.IApiPaymentLimitService` |
| 方法 | `boolean isLandingApprovalRequired(PaymentLimitContextDto context)` |
| 提供方 | 内管端（bt-saas-management，对外 jar `bt-saas-management-exchange-client`） |
| 消费方 | 企业端 `EnterpriseLimitControlService`（`@DubboReference(check=false)`），经 `buildEnterpriseLimitContext` 组装 `PaymentLimitContextDto` 后调用 |
| 返回语义 | `true` = 需要落地审批（内管端**已自行建案**）；`false` = 无需落地，企业端正常收尾 |

> 注意：内管端返回 `true` 时应当已经**自行创建好内管审批/落地处理单**，企业端拿到 `true` 后只做"预置 95 + 落地标识=0"，
> 不再向企业端回传任何 caseId。企业端也不调用 `checkLandingAndCreateCase`（该接口保留给其他场景，落地本轮不用）。

### 2.2 入参 `PaymentLimitContextDto`（内管端定义，企业端按来源业务组装）

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

### 2.3 语义要求（内管端实现要点）

1. 内管端基于 `PaymentLimitContextDto` 做落地规则匹配；**需要落地时返回 `true`，且内管端自行创建落地处理单/审批**（`BT_TR_LIMIT_CASE`，定位字段用 §2.2 的 `sourceBizType`/`sourceBizId`/`businessNo` + `tenantId`）。
2. 内管端需**保存 `sourceBizType` 原值**，出结果回调企业端时**原样回传**（企业端据此分发）。
3. 企业端在返回 `true` 后不向企业端回传 caseId/caseNo；内管端也不依赖企业端任何"生成处理单"结果。

---

## 3. 接口②：落地处理单结果回调（内管端 → 企业端）——企业端已实现，内管端调用

### 3.1 企业端对外暴露

| 项 | 值 |
|---|---|
| Dubbo 接口 | `com.bytter.biz.payment.api.service.ITranCaseCallbackService` |
| 方法 | `void caseWorkflowCallback(TranCaseCallbackDTO dto)` |
| 实现/注解 | `@DubboService`（`com.bytter.biz.foreign.service.impl.TranCaseCallbackServiceImpl`） |
| 依赖 | 内管端引入企业端 **`bytter-payment-client`**（含接口 + `TranCaseCallbackDTO`）后 `@DubboReference` 调用 |

### 3.2 入参 `TranCaseCallbackDTO` 字段

| 字段 | 必填 | 说明 |
|---|---|---|
| `tenantId` | 是 | 企业端租户ID（回调线程据此还原上下文） |
| `sourceBizType` | 是 | `FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE`（= §2.2 原值；必传，缺省企业端直接报错） |
| `businessId` / `businessNo` | 二者至少一 | 来源单据ID / 单据号（建议两者都传） |
| `corpId` | 否 | 单位ID（有则传，企业端做归属校验） |
| `caseId` / `caseNo` | 否 | 落地处理单 ID / 处理单号（企业端仅留痕） |
| `caseStatus` | 建议传 | 审批状态：`2` 审批通过 / `3` 打回 |
| `handleResult` | 建议传 | 处理结果：`A` 同意 / `R` 拒绝 |
| `approveUser` / `approveComment` / `approveDate` | 否 | 审批人登录名 / 意见 / 时间 |

### 3.3 结果语义（企业端判定口径）

- **通过**：`handleResult=A` 或 `caseStatus=2`。
  - 落地预置单（status=95 且 `LANDING_LIMIT_RESULT=0`）→ 置 `A` → 复位 `95→11` → 复用原收尾逻辑真正执行（`11→95` + 资金计划 + 现金流 + 发指令）。
  - 普通 11 单据 → 直接原收尾逻辑。
- **打回**：`handleResult=R` 或 `caseStatus=3`。
  - 落地预置单 → 置 `R` → 复位 `95→11` → 原收尾置 `11→-1`（可重新编辑）。
  - 普通 11 单据 → 直接打回。
- **幂等**：重复回调（单据已 `95` 且 `LANDING_LIMIT_RESULT`≠`0`，或已终态）→ 仅记幂等日志、正常返回，不重复收尾。
- **异常**：`tenantId`/定位字段缺失、`sourceBizType` 不支持、单据既非"落地待执行 95"也不在待审批（11）→ 抛 `ParameterException`（`暂不支持的落地限额来源业务类型: xxx` / `xxx单据不在待审批状态`），内管端需捕获并按错误处理（重试/保留），不要当作成功。

### 3.4 落地标识 `LANDING_LIMIT_RESULT`（三来源实体字段，内管端仅需了解语义）

| 值 | 含义 | 何时置 |
|---|---|---|
| `0` | 等待银行审批（落地待执行，内容未跑） | 企业端命中落地预置 95 时 |
| `A` | 银行同意支付（内容已执行） | 回调通过收尾时 |
| `R` | 银行拒绝支付（单据打回） | 回调打回收尾时 |
| `null` | 未涉及落地（普通收尾） | 正常收尾 |

> 内管端回调 DTO 的 `handleResult`（`A`/`R`）与上述 `A`/`R` 对齐；`0` 为企业端内部占位，不进入内管字典。

### 3.5 时序建议（重要）

回调会触发企业端 DB 收尾；**请内管端在本地事务提交后、异步回调企业端**（勿在长事务内同步占用两方连接，避免回滚牵连——历史 SCF 教训）。

---

## 4. 联调场景（建议两方按顺序验证）

> 企业端单据状态口径：`0 暂存 / 11 待审批 / -1 打回 / 95 审批通过`。落地预置后单据为 `95` 且 `LANDING_LIMIT_RESULT=0`。

| # | 场景 | 触发 | 预期结果 |
|---|---|---|---|
| 1 | 审批通过不落地 | `isLandingApprovalRequired` 返回 `false` | 单据 `11→95`，资金计划通过、现金流/指令正常 |
| 2 | 审批通过命中落地 | `isLandingApprovalRequired` 返回 `true`（内管已建案） | 单据 `11→95` + `LANDING_LIMIT_RESULT=0`；**无现金流/指令**、资金计划不动；记录"已提交银行端落地限额审批" |
| 3 | 场景2 → 拒绝支付 | 回调 `handleResult=R` | `LANDING_LIMIT_RESULT=R` → 复位 `95→11` → 原收尾 `11→-1` 打回、可改 |
| 4 | 场景2 → 同意·审批通过 | 回调 `handleResult=A`+`caseStatus=2` | `LANDING_LIMIT_RESULT=A` → 复位 `95→11` → 原收尾 `11→95`，资金计划通过、现金流 + 指令发送 |
| 5 | 场景2 → 审批流打回 | 回调 `caseStatus=3` | 同场景3打回（`LANDING_LIMIT_RESULT=R`） |
| 6 | 重复回调 | 落地预置单已收尾后再回调 | 仅幂等日志、正常返回 |
| 7 | 单据状态不合法回调 | 单据已打回后再回调 | 企业端抛"单据不在待审批状态"，内管端捕获/重试 |

---

## 5. 内管端交付物清单（请按此自检）

- [ ] `bt-saas-management-exchange-client` 发布含 `IApiPaymentLimitService.isLandingApprovalRequired(PaymentLimitContextDto)`（provider + DTO + group/version 与注册环境）。
- [ ] 实现落地规则匹配：需要落地时返回 `true` **并自行创建内管审批/落地处理单**（带齐定位字段 `sourceBizType`/`sourceBizId`/`businessNo` + `tenantId`）。
- [ ] 处理单审批流（同意/拒绝/打回）；出结果后经 `ITranCaseCallbackService.caseWorkflowCallback` 回调企业端（**提交后异步**，字段见 §3.2/§3.3）。
- [ ] 回调 `sourceBizType` 原样回传 `FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE`。
- [ ] 旧 ad-hoc 类（`ManageFexTranReqApproveService` 等）随新 jar 移除/改造，避免编译失败。

---

## 6. 两方待确认（联调前逐条对齐）

- `isLandingApprovalRequired` 返回 `true` 的语义 = "已命中且内管已建案"（企业端据此只预置 95 + 落地标识=0，等回调）。
- `sourceBizType` 三取值（`FEX_CASH_FLOW` / `NIS_BILLHEAD` / `NIS_WAGE`）与 `handleResult`/`caseStatus` 组合语义。
- 落地标识 `0` 为企业端占位（内管不感知）；`A`/`R` 与内管 `BT_TR_LIMIT_CASE.HANDLE_RESULT` 对齐。
- 资金收付落地对象为**现金流 FexCashFlow**（非交易 FexTranReq）；回调定位字段为现金流 `id`/`billNo`。
- 联调环境：两应用 Dubbo 注册中心互通。
