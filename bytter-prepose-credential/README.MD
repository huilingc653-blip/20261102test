# bytter-prepose-credential（国际结算 · 信用证）

> 2026-09-10 依据代码审读与 DDL 事实修订：按当前实现（`com.bytter.credential.*`）重写为**国际结算/信用证**口径，
> 补齐 10 张领域表清单、两条业务主流程、审批流与依赖；旧文中的「空白凭证 / 支票管理」在本仓库**未见实现代码**，
> 仅保留为迁移背景说明。

### 迁移旧代码模块说明

- T6 `bt-biz-international` 国际结算 → 本服务当前实现主体（信用证域）
- T6 `bt-biz-cashVoucher` 空白凭证 → 本仓库**未迁移**（无 Java 业务类、无对应表；历史描述见文末说明）

### 模块介绍

前置国际结算服务，承载**信用证（L/C）全流程**：进口开证与出口收证、交单/到单、付汇、进口押汇（含还押汇与展期）、
到期收款与出口押汇；并对接外部**授信额度（RAT）、保证金、担保、资金计划、审批流**能力。

#### 主要业务

- 开证链（进口）：开证申请/暂存 → 提交审批 → 占用授信额度·保证金·担保并写资金计划 → 补录提交与入库
- 收证链（出口）：收证登记入库 → 交单登记与审批 → 到单登记入库 → 分叉**付汇** / **进口押汇**
- 押汇后续：还押汇（释放额度与保证金）、展期
- 出口侧：到期收款、出口押汇（抵押）

##### 代码分层

```
- bytter-prepose-credential
    - bytter-credential-adapter   MVC controller(web/mobile/wap) + flow 入口 + saas/v1 REST；vm 资源
    - bytter-credential-client    对外 Dubbo API（ILcEnrolService / DTO / 常量 / 枚举）
    - bytter-credential-domain    业务接口实现、数据库访问（dao / entity / service / utils）
    - bytter-credential-start     启动类、yml 配置
```

### 领域业务表（10 张 · `com.bytter.credential.domain.international.entity`）

| 表名 | 描述 |
|---|---|
| LC_ENROL | 信用证主表（开证 lcType=0 / 收证 lcType=1，贯穿全链） |
| LC_BILL_ARRIVAL | 信用证到单表（到单子状态 1 已到单 / 2 付汇 / 3 押汇 / 4 已抵押） |
| LC_PAYMENT | 信用证付汇表 |
| LC_PAYMENT_COST | 信用证付汇费用表 |
| LC_DEPOSIT | 信用证押汇表 |
| LC_DEPOSIT_DELAY | 信用证押汇展期表 |
| LC_DEPOSIT_REPAY | 信用证还押汇表 |
| LC_MORTGAGE | 信用证出口押汇表 |
| LC_INCOMING | 信用证到期收款表 |
| LC_MESSAGE | 信用证报文日志表（操作台账，`LcMessageService.insertProcessMsg` 写入） |

### 状态机与审批流

- 统一状态常量 `InternationalConstant`：暂存 0 / 暂存(入库) 1 / 补录暂存 2 / 补录打回 3 / 提交审批 11 / 审批通过 99 /
  入库 105 / 打回 -1 / 删除 -2 / 取消 -3；付汇·押汇通过 95，押汇跳过补录 96，还押汇部分 97、还清 999，展期后押汇 99，
  到期收款完成 5，出口押汇通过后到单 4。
- 审批流统一实现 `IWorkFlowListener.flowCompleted` 并以 `@WorkFlow` 注册：
  `lcCreateCredit`（开证）、`lcSupplementCredit`（开证补录）、`lcReceiveStorage`（收证入库）、`lcReceiveArrival`（交单）、
  `lcPayment`（付汇）、`lcDeposit`（押汇）、`lcDepositDelay`（展期）、`lcReceiveMortgage`（出口押汇）、`lcReceiveIncoming`（到期收款）。
- 授权/占用链：暂存 `tempUseRation` → 提交 `addUseRation` → 通过 `passUseRation` → 打回 `backUseRation` → 删除 `deleteUseRation`
  → 业务性释放 `releaseRation`（付汇补录、还押汇）。

### 接口能力

- Dubbo 导出：`ILcEnrolService`（挂接/删除担保、流水认领标记、按 id 取到单）、`ArrivalLetterService`（以 `IWorkFlowListener` 暴露给工作流）。
- 定时任务：`@XxlJob("CreatedCreditService")` —— 按配置「信用证到期提醒天数」扫描临近有效期的 `LC_ENROL` 并推送提醒。
- **无 MQ**：服务间协作全部走 Dubbo + 本地工作流（全仓检索 MQ 相关注解与 exchange/routingKey 均无命中）。

### 依赖服务

| 方向 | 接口 / 方式 | 说明 |
|---|---|---|
| bytter-service-ras / 信贷域 | Dubbo `IRatUrationService` | 授信额度占用与释放（占用—释放总时序见上） |
| 担保域 | Dubbo `IFcBondHookService`、`IGuaWayServie`、`IGuaContractOccupServie` | 保证金合同额度、担保方式与担保合同占用 |
| bytter-system | Dubbo `ICorpService`/`IBtCurrencyService`/`IBtBankAccService`/`IBtBankService`/`IConfigService`/`IDictService`/`INotificationService`/`IUserService` 等 | 单位、币别、银行与账号、参数、字典、通知等基础资料 |
| 资金计划 | Dubbo `IFbsBudgetProcessService` | 开证/收证写计划执行（占用与回退） |
| bytter-service-bdlink | Dubbo `IBisAccDtlService` | 银行流水认领标记 |
| bytter-workflow | `IWorkFlowService` + `@WorkFlow` 回调 | 9 条审批流程（key 见上） |

> 说明：旧 README 中的「空白凭证 / 支票管理」及 `BV_PAYEE_CHEQUE`、`BV_VOU_INPUT`、`BV_VOU_EMPLOY_INFO` 等表
> 属于 T6 `bt-biz-cashVoucher` 模块，**本仓库无对应代码与表**（同类能力现由 `bytter-prepose-cashvoucher` 服务承载），
> 故从本服务表清单中移除。

### 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `credential_erd.png` | bytter-prepose-credential•信用证•数据库表关系图 | 10 表 / 3 边（全表入图） |
| `credential_open_business_flow.workflow.tables.html` | 进口开证业务主流程 | 7 节点 / 7 表引用（交互式节点×表透镜） |
| `credential_recv_business_flow.workflow.tables.html` | 收证与到单业务主流程 | 7 节点 / 15 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/credential/`（账本 + 两份 `*.workflow.json` + 两份节点×表 map + 规格文件）。
