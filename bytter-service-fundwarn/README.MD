# bytter-service-fundwarn（资金预警）

> 2026-09-11 依据代码审读与 DDL/实体事实新建（原无 README）：含 6 张领域表清单、三类主流程（定时扫描 / 事中预警 / 异常告警）、
> 预警类型与状态枚举、调度与依赖清单，以及与 payment / bdlink / system 的调用方向。

## 模块介绍

**资金预警**服务：维护预警策略（类型·维度·阈值·条件）→ 按调度或业务事中触发扫描 → 命中生成预警数据与明细 →
站内通知与留痕；另承担**异常告警**（数据获取异常时经企微/钉钉群机器人推送）。

### 代码分层

```
- bytter-service-fundwarn
    - bytter-fundwarn-adapter  Controller（预警策略 / 预警数据 / 异常告警）+ views
    - bytter-fundwarn-client   对外 SPI：IFundWarnService、IFundWarnExceptionDataService、FundWarnConstant、枚举
    - bytter-fundwarn-domain   entity / dao / service / util / vo（BtFundWarnUtils 为阈值 SQL 与常量核心）
    - bytter-fundwarn-start    启动类（@EnableDubbo、@ComponentScan("com.bytter")）、yml
```

### 子域职责

| 子域（类族） | 职责 |
|---|---|
| `BtWarnStrategyService` / `BtWarnStrategyController` | 预警策略与条件维护、扫描参数（启用+CRON）、复制、逾期类一键初始化 |
| `FundWarnStrategyAnalysisService` + `dao/FundWarnStrategyAnalysisDao` | **策略解析引擎**：按类型×维度分派、拼阈值 SQL、查源表、写预警数据 |
| `FundWarnTaskService`（XxlJob） | 定时扫描入口（参数=策略 ID）与 Job 注册同步（`IXxlJobTemplate`） |
| `FundWarnTimelyService`（Dubbo SPI） | **事中预警**：payment 付款/审批时同步调用，服务侧异步线程处理 |
| `BtWarnDataService` / `BtWarnDataDtlService` / `BtWarningLogService` | 预警数据、明细（先删后插重写）、资金监控日志与首页统计 |
| `FundWarnNotificationService` | 命中新增时经 system `INotificationService` 推送 |
| `BtWarnExceptionDataService`（Dubbo SPI） | **异常告警**：按渠道（企微/钉钉）群机器人推送并无论成败留痕 |

## 领域业务表（6 张）

| 表名 | 中文说明 |
|---|---|
| BT_WARN_STRATEGY | 预警策略表 |
| BT_WARN_CONDITION | 预警策略条件表 |
| BT_WARN_DATA | 预警数据表 |
| BT_WARN_DATA_DTL | 预警数据明细表 |
| BT_WARN_EXCEPTION_DATA | 异常告警数据表 |
| BT_WARNING_LOG | 资金监控日志表 |

> 关系：`BT_WARN_CONDITION.WARN_ID → BT_WARN_STRATEGY.ID`（一条策略多条条件，SQL 以多条 inner join 同时命中才算匹配）；
> `BT_WARN_DATA.WS_ID → BT_WARN_STRATEGY.ID` 与 `BT_WARN_DATA.CODE → BT_WARN_STRATEGY.CODE`（编号回关联）；
> `BT_WARN_DATA_DTL.DATA_ID → BT_WARN_DATA.ID`。
> `BT_WARNING_LOG.QUOTE_ID` 为**多态引用**（外部业务对象 ID，无 SQL 证据，未建边）；`BT_WARN_EXCEPTION_DATA` 无外键（独立留痕表）。
> ⚠️ `sql/oracle/bytter-service-fundwarn_ddl.sql` 只含 5 张表，**`BT_WARN_EXCEPTION_DATA` 不在该 DDL 内**（后续新增，以实体为准）。

## 业务主流程

### 1. 定时扫描（主链）

`@XxlJob("FundWarnTaskService")`（参数=策略 ID）→ 取策略 → `FundWarnStrategyAnalysisService.executeStrategy` 按
`typeCode` + `dimensionCode` 分派 → 拼阈值条件 SQL（`BtFundWarnUtils.getConditionSql`）→ 查源表
（`nis_billhead` / `bis_acc_bal` / `bis_acc_dtl` / `fc_online_ledger` / `fc_acc` / `sys_corp` / `FBS_BUDGET_PROCESS`）→
**命中分支**：取旧预警数据，存在则记「修改」日志，不存在则新建并置 `sendMsgSign=1` 记「新增」日志 → 保存预警数据 →
有明细则 `batchStrategyDataDtl` **先删后插**重写 → 仅新增命中才 `sendNotification` → 最终必写「执行」日志。
**未命中分支**：不写预警数据，仍写执行日志。

### 2. 事中预警（payment 同步调用）

payment 付款单保存/审批时调 `IFundWarnService`（未记账 → 相同公司付款+大额账户支出；待审批 → 追加单日累计支付；现金 → 大额现金支出）→
服务侧**复制租户上下文后另起线程异步处理**（不影响主业务）→ 按账户属性/类型/性质反查策略并做**金额区间过滤** → 命中则逐个写预警数据+通知+日志。
（`TpPaymentService` 侧同名调用已被注释，当前仅 `NisBillheadService` 生效。）

### 3. 异常告警（bdlink 调用）

bdlink 定时任务发现「银行余额超时未取」→ 调 `IFundWarnExceptionDataService.sendWarnExceptionDataByGroupChatBot` →
按 `sendChannel`（all / 0 企微 / 1 钉钉）调 system 的群机器人推送 → **无论成败**写 `BT_WARN_EXCEPTION_DATA`（reqMsg/respMsg/sendStatus）。

### 枚举与状态

- **预警类型 `typeCode`（12 类）**：余额超标 / 相同公司付款 / 大额账户支出 / 净流入透支 / 余额持续下降 / 系统外付款 /
  支出户流入 / 库存现金超标 / 存贷比超标 / 大额现金支出 / 久悬户 / 预算执行进度 / 单日累计支付（`FundWarnConstant`）。
- **预警维度 `dimensionCode`（3 类）**：单位 corp / 银行账户 bankAcc / 内部账户 fcAcc。
- **预警级别 `warnLevel`**：0 低 / 1 中 / 2 高。
- **策略状态 `status`**：1 启动 / 0 停止（新增默认停止，由扫描参数维护并同步 Job `triggerStatus`）。
- **日志类型 `logType`**：01 新增 / 02 修改 / 03 删除 / 04 执行。
- **异常告警**：`sendStatus` 0 成功 / 1 失败；`channel` 0 企微 / 1 钉钉；`warnType` 0 应用接口 / 1 前置机服务。

### 审批流

**未见证据**——本服务无 `flowStarted/flowCompleted/flowRevoke` 回调与流程 key，预警与审批无关。

## 接口能力与依赖

- **Dubbo 导出**：`IFundWarnService`（事中预警 6 个方法）← payment 调用；`IFundWarnExceptionDataService`（异常告警）← bdlink 调用。
- **Dubbo 引用**：system（`ICorpService` / `IBtBankAccService` / `IBtCurrencyService` / `IBankTypeService` / `IDictService` /
  `INotificationService` / `ICorpWeiXinMessageService` / `IDingTalkMessageService` / `IXxlJobTemplate` / `IMultiLangService`）、
  budget（`IFbsBudgetPeriodService`，预算执行进度类预警取预算期间）。
- **定时任务（1 个）**：`@XxlJob("FundWarnTaskService")`，执行器 `bytter-service-fundwarn`，按策略 CRON 扫描；Job 增改由 `IXxlJobTemplate` 下发。
- **MQ**：未见证据。

## 域边界

- 调用方向：**payment → fundwarn**（事中预警）、**bdlink → fundwarn**（异常告警）、**fundwarn → system / budget**（主数据与推送）。
- 本服务不写付款单/账户等业务表，只**读**源表做判定，产出全部落在自身 6 张表内。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `fundwarn_erd.png` | bytter-service-fundwarn•资金预警•数据库表关系图 | 6 表 / 4 边（3 个业务分组；4 条边均为 DAO 原生 SQL 与关联写入证据） |
| `fundwarn_business_flow.workflow.tables.html` | 资金预警与异常告警主流程 | 7 节点 / 14 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/fundwarn/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
