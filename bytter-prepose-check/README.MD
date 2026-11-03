# bytter-prepose-check（财务对账服务）

> 2026-09-10 依据代码审读与 DDL 事实全面修订：补齐两个业务主题（银企对账 / ERP 数据交换）的表清单、
> 中文名、依赖与交付图；渲染源见 `tools/service-db-diagram/sources/check/`。

## 模块介绍

前置财务对账服务，承载两条耦合度极低的业务线：

1. **银企对账（FC_ / BIS_CHECK_ 域）**：银行对账单与企业账（中心账 / ERP 账）双向勾对、未达处理、
   余额调节表结转、自动对账策略，以及银企直联余额对账（同步账单 → 发指令 → 确认 → 复核）与资金勾兑。
2. **ERP 数据交换（ERP_ / DSL_ 域）**：与外部 ERP 的交换配置（接口类型 / 数据类型 / 基础与自定义映射）、
   凭证模板与补录（模板头·分录·类型、交换参数、收付补录、模板与业务数据关联）、付款与代发工资数据转移、接口日志。

两条业务线之间无跨包调用（`com.bytter.check` 与 `com.bytter.biz.*` 仅共享实体扫描），故数据库关系图按主题拆分交付。

### 代码分层

```
- bytter-prepose-check
    - bytter-check-adapter    MVC controller(web/mobile/wap) + vm
    - bytter-check-client     对外暴露 API
    - bytter-check-domain     业务接口实现 + 数据库访问
        - com.bytter.check            对账主域（含 entity / dao / service）
        - com.bytter.check.reconcile  银企直联余额对账、资金勾兑、对账跟踪
        - com.bytter.biz.dataexchange.basic   数据交换基础配置、付款数据转移
        - com.bytter.biz.transfer.swap        凭证模板、交换参数、收付补录
    - bytter-check-start      启动类 + yml
```

### 子域职责

| 子域 | 职责 | 主表 |
|---|---|---|
| 对账主域 `check` | 余额初始化、银行/中心(ERP)对账数据导入、自动核对、手工勾兑与未达处理、余额调节表结转、自动对账策略、单位对账单审批 | FC_CHECK_INIT / FC_BANK_BILL / FC_COMPARE_ACC / FC_BANK_BALANCE_ADJUST / FC_CORP_COMPARE_BILL |
| 银企直联 `reconcile` | 银企余额对账（同步账单、发送对账指令、对账确认、复核与状态跟踪）、对账原因维护、资金勾兑与流水认领 | BIS_CHECK_DTL / BIS_CHECK_EXC / BIS_CHECK_REASON |
| 数据交换基础 `dataexchange.basic` | 接口类型与数据类型维护、基础/自定义数据映射、接口调用日志、付款与代发工资数据转移 | ERP_TYPE / ERP_DATA_TYPE / ERP_BASIC_DATA / ERP_PAYINFO / ERP_WAGE_DETAIL / DSL_INTERFACE_LOG |
| 模板与补录 `transfer.swap` | 凭证模板（头/分录/类型）、交换参数与字段设置、收付业务匹配模板、收付补录、模板与业务数据关联 | ERP_TEMPLATE / ERP_INTER_CONFIG_SET / ERP_HIS_DTL_TEMPLATE / ERP_HIS_DTL_COMFIELD |

## 领域业务表（41 张）

### 银企对账域（20 张 · FC_* 17 + BIS_CHECK_* 3）

| 表名 | 中文说明 |
|---|---|
| FC_CHECK_INIT | 银行中心余额初始表 |
| FC_BANK_BILL | 银行对账数据表 |
| FC_BANK_BILL_TEMP | 银行对账数据临时表 |
| FC_BANK_BILL_PROC | 银行对账数据结转表 |
| FC_COMPARE_ACC | 企业对账数据表 |
| FC_COMPARE_ACC_TEMP | 企业对账数据临时表 |
| FC_COMPARE_ACC_PROC | 企业对账数据结转表 |
| FC_COMPARE_CHECK_TYPE | 核对标准表 |
| FC_CHECK_TYPE_DTL | 核对标准对应表 |
| FC_BANK_CHECK_LOG | 银行对账日志表 |
| FC_BANK_CHECK_DTL | 银行对账日志明细表 |
| FC_BANK_BALANCE_ADJUST | 对账余额调节表 |
| FC_BANK_BALANCE_ADJUST_DETAIL | 对账余额调节明细表，记录对账未达信息 |
| FC_AUTO_CHECK_CONFIG | 自动对账配置 |
| FC_AUTO_CHECK_CORP | 自动对账配置关联单位 |
| FC_AUTO_CHECK_RESULT | 自动对账执行结果表 |
| FC_CORP_COMPARE_BILL | 单位对账单表 |
| BIS_CHECK_DTL | 银企对账表 |
| BIS_CHECK_EXC | 银企对账指令表 |
| BIS_CHECK_REASON | 对账原因表 |

### ERP 数据交换域（21 张 · ERP_* 20 + DSL_* 1）

| 表名 | 中文说明 |
|---|---|
| ERP_TYPE | 接口类型表 |
| ERP_DATA_TYPE | 数据类型表 |
| ERP_DATATYPE_FIELD | 补录配置字段表 |
| ERP_BASIC_DATA | 基础数据映射表 |
| ERP_DYNAMIC_DATA | 自定义数据映射表 |
| ERP_DYNAMIC_DATA_TYPE | 自定义数据类型表 |
| ERP_INTER_CONFIG | 数据交换参数配置表 |
| ERP_INTER_CONFIG_FILL | 数据交换自定义字段设置页面配置表 |
| ERP_INTER_CONFIG_SET | 数据交换发送数据设置配置表 |
| ERP_TEMPLATE | 数据交换凭证模版表 |
| ERP_TEMPLATE_DTL | 数据交换凭证模版分录表 |
| ERP_TEMPLATE_TYPE | 数据交换模版类型表 |
| ERP_TEMPLATE_TYPE_DTL | 数据交换模版类型详情表 |
| ERP_TEMPLATE_DATADEFINE | 模板类型凭证头和明细初始数据表 |
| ERP_TEMPLATE_STORE | 数据交换凭证模版和业务数据关联表 |
| ERP_TEMPLATE_DTL_STORE | 数据交换凭证模版分录和业务数据关联表 |
| ERP_HIS_DTL_TEMPLATE | 数据交换业务和模板中间表 |
| ERP_HIS_DTL_COMFIELD | 收付补录信息表 |
| ERP_PAYINFO | 付款数据转移表 |
| ERP_WAGE_DETAIL | ERP代发工资明细表 |
| DSL_INTERFACE_LOG | 接口日志表 |

## 业务主流程

### 银企对账主链路

余额初始化 → 导入银行对账单（手工/Excel/接口）→ 录入中心(ERP)对账数据 → 自动核对（按核对标准逐笔匹配）
→ 手工勾兑与未达处理（勾兑 / 确认未达 / 反勾对 / 未达还原）→ 编制余额调节表结转（落余额调节主从表与两侧结转表）。

- 定时旁路：`FcAutoCheckConfigService`（`@XxlJob(XxlJobConstant.AUTO_CHECK_CONFIG)`）按 `FC_AUTO_CHECK_CONFIG` /
  `FC_AUTO_CHECK_CORP` 策略发起核对与结转，结果写 `FC_AUTO_CHECK_RESULT`。
- 单位对账单：`FC_CORP_COMPARE_BILL` 独立走 `bytter-workflow` 审批（`CorpCompareBillService.flowStarted/flowCompleted`），
  与上方主流程无代码调用。
- 银企直联对账（并行子域）：同步银企对账单 → 发送对账指令（`BisBankEnterpriseCheckService.sendExc`）→
  对账确认（`opSure`）→ 复核与状态跟踪（`BisTrackingService`）。

### ERP 数据交换主链路

接口类型维护 → 数据类型维护（含补录配置字段）→ 基础/自定义数据映射（内部代码 ↔ 外部代码）→ 凭证模板维护
（模板头/分录/类型 + 类型初始数据）→ 交换参数与字段设置 → 收付业务匹配模板 → 收付补录与模板关联 →
付款/代发工资数据转移发送 → 接口调用日志；发送失败或资金系统打回后改状态重发。

- 数据关联：`ERP_HIS_DTL_TEMPLATE` 为业务数据与凭证模板的中间表（`TEMPLATE_ID` → `ERP_TEMPLATE`，
  `SERIAL_ID` 为业务数据 ID）；`ERP_TEMPLATE_STORE.DATA_ID` → `ERP_HIS_DTL_TEMPLATE`；
  `ERP_TEMPLATE_DTL_STORE.STORE_ID` → `ERP_TEMPLATE_STORE`；`ERP_HIS_DTL_COMFIELD.TEMPLATE_ID` → `ERP_HIS_DTL_TEMPLATE`。
- 费用与工资：`ERP_WAGE_DETAIL.SERIAL_NO` 以「主表外系统 ID」关联 `ERP_PAYINFO.SERIAL_NO_ERP`。

## 关键定时任务与对外服务

| 类型 | 名称 | 说明 |
|---|---|---|
| XXL-Job | `XxlJobConstant.AUTO_CHECK_CONFIG` | 自动对账配置驱动核对与结转（`FcAutoCheckConfigService`） |
| XXL-Job | `PaymentBlendingService` / `RefundBlendingService` | 付款/退款单与银行流水勾兑 |
| XXL-Job | `BisPaymentBlendingService` | 银企流水勾兑 |
| XXL-Job | `BisAccDtlBudgetRuleService` / `BisAccDtlBudgetRulePayService` | 银行流水认领与预算规则 |
| Dubbo 暴露 | `FcCompareCenterService`、`CentralityDataManageService` | 对账数据与中心数据管理服务 |

## 依赖服务与接力

| 方向 | 方式 | 说明 |
|---|---|---|
| bytter-workflow | Dubbo | 单位对账单等流程：`CorpCompareBillService` 起流程并实现 `flowStarted/flowCompleted` 收回调 |
| bytter-system | Dubbo | 单位（ICorpService）、用户（IUserService）、参数（IConfigService）、账号授权（IAccountGrantService）、币别（IBtCurrencyService） |
| bytter-service-bdlink / 资金域 | Dubbo | 账号与余额（IFcAccService / IFcAccBalanceService）、银行流水（BIS_ACC_DTL，经 PaymentBlendingDao 查询） |
| bytter-service-payment | Dubbo | 付款单/退款单信息用于资金勾兑 |
| 外部 ERP | 接口 / 报文 | 由本服务组装报文推送 ERP；网银流水采集（ErpRPABank）经银企直联通道，不落本域表 |

## 交付图（tools/service-db-diagram/work/）

| 图 | 主题 | 说明 |
|---|---|---|
| check_reconcile_erd.png | bytter-prepose-check•银企对账•数据库表关系图 | 20 表 / 13 边（FC_* 与 BIS_CHECK_* 两组，全部入图） |
| check_dataexchange_erd.png | bytter-prepose-check•ERP数据交换•数据库表关系图 | 21 表 / 14 边（ERP_* 与 DSL_INTERFACE_LOG，全部入图） |
| check_reconcile_business_flow.workflow.tables.html | 银企对账与余额调节业务主流程 | 9 节点 / 17 表引用（交互式节点×表透镜） |
| check_dataexchange_business_flow.workflow.tables.html | ERP 数据交换与凭证补录业务主流程 | 10 节点 / 21 表引用（交互式节点×表透镜） |

> 说明：`FC_BANK_BILL_TEMP` / `FC_COMPARE_ACC_TEMP` 等 `*_TEMP` 表与 `ERP_HIS_DTL_*` 等含 `_HIS` 的表
> **均已正常入图**——表名不足以判定归档/临时语义，渲染器不再按表名分类（2026-09-10 修正）。

渲染源与再生成命令见 `tools/service-db-diagram/README.md` 与项目技能 `service-db-evidence`（render-preference）。
