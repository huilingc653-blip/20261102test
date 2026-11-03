# bytter-service-bdlink（银企直连）

> 2026-09-10 依据代码审读与 DDL 事实新建（原无 README）：含 11 张领域表清单、主流程与状态机、7 个定时任务、
> Dubbo 导出/引用清单，以及与 payment / prepose-check 的边界核对结论。

## 模块介绍

银行**银企直连（bdlink）**后台服务：承载银行账户的**余额、流水、电子回单、月度对账单、银企查询指令、接口监控预警、
余额流水异常核查**的采集、维护、勾兑与核算，并向上层支付/结算服务输出账户资金数据。

### 代码分层

```
- bytter-service-bdlink
    - bytter-bdlink-adapter  controller（JSP 页）+ saas/controller（v1 REST）
    - bytter-bdlink-client   对外 Dubbo API / DTO / VO / 常量 / 枚举
    - bytter-bdlink-domain   entity / dao / service / receipt / reconcile / job
    - bytter-bdlink-start    启动类（@EnableDubbo、@MapperScan("com.bytter.biz.*.*.dao")）、yml
```

### 子域（按包）

| 包 | 职责 |
|---|---|
| `com.bytter.biz.bdlink.*` | 银企直连核心域：余额、流水、查询指令、接口监控、异常核查、回单处理配置 |
| `com.bytter.biz.receipt.*` | 电子回单域：回单主表/PDF、回单与流水勾兑 |
| `com.bytter.biz.reconcile.*` | 银行对账域：月度对账单（仅查询） |
| `com.bytter.biz.job.*` | 定时任务：余额落库、系统冻结金额同步 |
| `com.bytter.biz.bdlink.controller` / `.saas.controller` | 传统 JSP 页入口 + 前后端分离 v1 REST 入口 |

## 领域业务表（11 张）

| 表名 | 中文说明 |
|---|---|
| BIS_QUERY_CMD | 银企查询指令表 |
| BIS_ACC_BAL | 银行账户余额表 |
| BIS_ACC_DTL | 银行账户流水表 |
| BIS_ACC_BAL_AUTO | 银行账户离线余额计算表 |
| BIS_ACC_DTL_CHECK | 余额流水异常核查表 |
| BIS_BANK_RECEIPT_DTL | 电子回单表 |
| BIS_BANK_RECEIPT_PDF | 电子回单 PDF 表 |
| BIS_BANK_RECEIPT_DTL_DISPOSE | 电子回单处理配置表 |
| BIS_BIF_MONITOR | 银行接口监控预警表 |
| BIS_BANK_MONTH_STATEMENT_DTL | 银行月度对账单明细表 |
| BIS_BANK_RECEIPT_DTL_HIS | 电子回单历史表 |

## 业务主流程

1. **查询指令下发**：`BisQueryCmdService.addBisQueryCmd` 生成 `BIS_QUERY_CMD`（手动/自动；服务码 900010 实时余额 /
   900020 当日明细 / 900030 历史明细 / 900040 历史余额），银行回执回写 `cmdStat`；失败可**重发**（状态重置 `3 准备发送`）。
2. **余额与流水采集**：外部接口按 DTO 写入 `BIS_ACC_BAL` / `BIS_ACC_DTL`；离线账户由 `BisAccBalAutoServiceImpl.doBal`
   按「前日余额 + 当日流水结余」滚动生成余额（遇未审批余额单则中断不更新）。
3. **余额单审批**：`batchSubmit` 置 `11 待审批` → 工作流 `netbankBankaccBal` → 通过置 `95`、打回置 `-1`、撤回回置 `0`。
4. **电子回单落库与勾兑**：`saveBankBill` 落 `BIS_BANK_RECEIPT_DTL` + `BIS_BANK_RECEIPT_PDF` → 定时任务自动勾兑
   （精确/模糊匹配、重复命中跳过、滚动超期结束追踪）或人工勾兑/取消勾兑，更新 `blendSign`。
5. **异常核查与预警**：`BisAccDtlCheckService.run` 按「期初 + 昨日流水 − 期末」算差额，非 0 生成核查记录并通知，
   人工批量置已处理；`BisBifMonitorService.run` 巡检接口超阈值未取余额并通知；`BisAccBalWarnTaskService` 推送余额异常告警。
6. **数据回流**：余额经 `IBtBankAccBalService.batchSaveByDTO` 回写 payment；付款指令流水号经
   `IBisExcService.getAllBisExcBankSerialNo` 反查用于票据认领过滤。

### 状态机

- **余额单 `BIS_ACC_BAL.status`**：`0 暂存 / 11 待审批 / 95 通过 / -1 打回 / -2 删除 / 100 办理完成`（`BisAccBalConstant`）。
- **查询指令 `BIS_QUERY_CMD.cmdStat`**：`0 交易成功 / 1 等待结果 / 2 银行处理失败 / 3 准备发送 / 6 待发送 / 7 指令无效 /
  8 已打印 / 9 接口处理失败 / w 待审批 / p 审批通过 / B 退回 / R 打回 / D 删除 / T 可疑 / W 已消费 / Y 异常 / Z 验签不通过`（`BdlinkConstant`）。
- **异常核查 `BIS_ACC_DTL_CHECK.status`**：`0 未处理 / 1 已处理`。
- **回单勾兑 `blendSign`**：`0 未勾兑 / 1 已勾兑`（`BtBankAccConstant`）。

### 审批流

流程 key `netbankBankaccBal`（`NisWorkFlowConstant.BT_BANKACC_BAL`），实现 `IWorkFlowListener` + `IWorkFlowRevokeListener`：
`flowStarted`（空）/ `flowCompleted`（通过置 95、打回置 -1）/ `flowRevoke`（撤回置 0）/ `submitVerify`。

## 接口能力与依赖

- **Dubbo 导出**：`IBisAccBalService`、`IBisAccDtlService`、`IBisAccBalAutoService`、`IBisBankReceiptBlendingServer`、
  `IBisBankReceiptDtlService`、`IBisBankReceiptDtlHisService`、`IBisBankReceiptDtlDisposeService`、
  `IBisBankMonthStatementDtlService`、`IWorkFlowListener`(group `netbankBankaccBal`)。
- **Dubbo 引用**：system（`ICorpService`/`IBtBankAccService`/`IBtCurrencyService`/`IDictService`/`IUserService`/`INotificationService`/
  `IConfigService`/`IBtBankAccFreezeService`/`IBtBankAccMessageService`/`IAccountGrantService`/`IBankTypeService`/`IBisBifInitService`/`IRegionService`）、
  workflow（`IWorkFlowService`）、payment（`IBtBankAccBalService`/`IBisExcService`/`IFcBankAccGroupService`）、
  increment（`IChoInnerFinanceService`）、fundwarn（`IFundWarnExceptionDataService`）。
- **定时任务（7 个）**：`BisAccDtlCheckService`（异常核查）、`BisBifMonitorService`（接口监控巡检）、
  `BisAccBalAutoService`（离线余额自动计算）、`BisBankReceiptBlendingServer`（回单自动勾兑）、
  `BisAccBalWarnTaskService`（余额异常告警）、`SaveBalByDtl`（按流水补余额）、`AutoSyncSysFrzBalJob`（系统冻结金额同步）。
- **MQ**：未见证据（全模块无 MQ 依赖与注解）；通知统一走 system `INotificationService`。

## 域边界核对

- 与 **bytter-service-payment**：本服务**引用** payment 的余额/指令流水号接口，但**付款指令（BisExc）的下发与回执处理在
  payment 服务**（`bytter-service-payment/.../biz/bdlinks/BisExcService.java`），本服务不参与付款指令审批与下发。
- 与 **bytter-prepose-check**：**未见依赖证据**（本服务依赖仅 system / increment / workflow / payment / fundwarn；代码中无
  `prepose` 引用），两者无直接接力关系。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `bdlink_erd.png` | bytter-service-bdlink•银企直连•数据库表关系图 | 11 表 / 0 边（全表入图） |
| `bdlink_business_flow.workflow.tables.html` | 银企直连数据闭环业务主流程 | 7 节点 / 11 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/bdlink/`（账本 + `*.workflow.json` + 节点×表 map + 规格文件）。
