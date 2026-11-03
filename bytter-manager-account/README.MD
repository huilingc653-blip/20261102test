# 财务核算模块（账务/流水/记账）（bytter-manager-account）

## 模块介绍

### 主要业务
- 总账与分录记账（流水账主档/明细、记账凭证导入、手工账）
- 内部账户核算（科目余额、累计余额、冲销、在线台账、每日开关机结）
- 计息管理（存贷款计息明细、个别计息、预提、冲预提、不足款）
- 费用计提（费用类型、计提规则、费用计提）
- 表外流水（表外主档/明细、表外科目日结）
- 期末结转与报表（月度台账、余额/损益表、月结区间、报表格式）

### 代码分层
```
- bytter-manager-account

    - bytter-finacial-adapter

      适配层，对应 MVC 的 Controller，使用 web|mobile|wap 按包名隔离，VM 资源存放于此层
    - bytter-finacial-client

      定义暴露的 API 接口，供其他服务调用
    - bytter-finacial-domain

      主要业务处理接口实现、数据库访问与数据组装，后续 DDD 改造升级重点重构模块
    - bytter-finacial-start

      启动类、yml 配置文件
```

### 数据库

- 数据库：Oracle，schema 用户 `bank430`（`jdbc:oracle:thin:@192.168.0.217:1521/orcl`），与全平台其他服务共用同一模式，无独立 schema。
- 本服务代码 `@Table` 注解共映射 70 张表：业务表 64 张、历史归档表 4 张、临时表 2 张。

### 领域内业务表

> 业务表口径：与 increment 一致，历史归档表（`*_HIS`）、临时表（`*_TMP`/`*_TEMP`）单列于本页末尾；表名为代码 `@Table` 实体注解映射值，`说明`取自实体类 Javadoc `@Description` 或 SQL `COMMENT ON TABLE`，空项以 `—` 表示（对应表可查 Oracle `ALL_TAB_COMMENTS` 或实体注释确认语义）。同表被多实体映射时，实体以 `/` 分隔。

业务表按财务业务子域（包结构）分组，共 64 张：

#### 流水账（5 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACCBOOK_BLNED` | 已结流水账主档表 |
| `FC_ACCBOOK_BLNED_DTL` | 已结流水账明细表 |
| `FC_ACCBOOK_BLNED_EXT` | 已结流水账主档扩展表 |
| `FC_ACCBOOK_MSG` | 流水档日志 |
| `FC_CHARGE_ACCRUE` | 手续费计提表 |

#### accbook/tally（3 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACCBOOK` | 记账凭证主表 |
| `FC_ACCBOOK_DTL` | 记账凭证明细表 |
| `FC_ACCBOOK_EXT` | 记账凭证扩展表 |

#### 期末结转/结账（9 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACC_MONTH_BALANCE` | 账户月结算量表 |
| `FC_BALANCE_SHEET` | 资产负债表 |
| `FC_CARRY` | 结转记录表 |
| `FC_CASH_FLOW` | 现金流量表 |
| `FC_CORP_INTEREST_DTL` | 单位利息明细表 |
| `FC_DAY_CLOSE` | 日结日期表 |
| `FC_DAY_START` | 日初日期表 |
| `FC_INCOME_STATEMENT` | 损益表 |
| `FC_MONTH_LEDGER` | 月总账表 |

#### 内部账户（3 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACC_BALANCE` | 账户批量余额表 |
| `FC_ACC_WRITEOFF` | 账户注销记录表 |
| `FC_ONLINE_LEDGER` | 联机分户账表 |

#### 计息（10 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACCU_BALANCE` | 积数余额表 |
| `FC_INTEREST_DTL` | 利息明细表 |
| `FC_INTEREST_SINGLE` | 个别计息表 |
| `FC_INTEREST_SINGLE_NOD_BOOK` | 企业通知存款个别计息-通知预约 |
| `FC_LESS_PAY_BILL` | 不足款清单 |
| `FC_LOAN_BALANCE_DTL` | 还款积数对应表 |
| `FC_LOAN_REPAY` | 企业放款还款表 |
| `FC_PREDRAW_DTL` | 预提明细表 |
| `FC_PREDRAW_RUSH_DTL` | 冲预提信息表 |
| `fc_interest_dtl_rule` | 利息流水规则表。 |

#### 表外流水（5 张）

| 表名 | 说明 |
| --- | --- |
| `FC_OUT_ACCBOOK` | 表外流水账主档表 |
| `FC_OUT_ACCBOOK_DTL` | 表外流水账明细表 |
| `FC_OUT_ACC_BALANCE` | 表外账号余额表 |
| `FC_OUT_SUBJECT_DAY` | 表外科目日结单表 |
| `FC_PRE_OUT_ACCBOOK` | 表外流水账主档历史表 |

#### 参数配置（18 张）

| 表名 | 说明 |
| --- | --- |
| `BT_SUMMARY` | BT_SUMMARY摘要词组维护 |
| `CHARGE_TYPE_BANK_TYPE_REL` | 费用类型与银行类型关联表 |
| `CHARGE_TYPE_CHARGE_RULE_REL` | 费用类型与费用计提维护 |
| `FC_BUSINESS_RULE_DEFINE` | 业务规则定义 |
| `FC_BUSINESS_RULE_DEFINE_POOL` | 资金池业务规则定义 |
| `FC_BUS_RACE_POOL` | 资金池业务种类表 |
| `FC_CHARGE_RULE` | 费用计提维护表 |
| `FC_CHARGE_TYPE` | 费用类型表 |
| `FC_CHARGE_TYPE_DTL` | 费用类型明细 |
| `FC_NIS_BUS_TYPE_RULE` | 业务种类规则表 |
| `FC_OTHER_BUSINESS_RULE_DEFINE` | 其他业务规则定义表 |
| `FC_REPORT_FORMAT` | 格式定义表 |
| `FC_SET_MONTH` | 月结区间 |
| `FC_SYS_BUS_RACE` | 系统业务品种表 |
| `FC_SYS_BUS_RACE_POOL` | 资金池系统业务品种表 |
| `FC_VOUCHER_CASH` | 现金流量项目科目关系表 |
| `FC_VOUCHER_ENTRY_DEFINE` | 凭证分录定义 |
| `FC_VOUCHER_ENTRY_DEFINE_POOL` | 资金池凭证分录定义 |

#### paramconfig/tally（2 张）

| 表名 | 说明 |
| --- | --- |
| `FC_BUS_RACE` | 业务种类表 |
| `FC_CASH_ITEM` | 现金流量科目 |

#### 记账/表外（7 张）

| 表名 | 说明 |
| --- | --- |
| `BT_NET_CHALK` | 银企接口收款表 |
| `CMS_LOAN_REPAY` | 贷款还款表 |
| `CMS_LOAN_SEND` | 贷款发放表 |
| `FC_BANK_ACC_DTL` | 资金池账户流水记账关联表 |
| `FC_MANUAL_FINACIAL_COLUMN` | 手工账回写列值对应表 |
| `FC_MANUAL_FINACIAL_TYPE` | 手工帐类型表 |
| `FC_TALLY_REMIND_DTL` | 记账提醒明细表 |

#### tally/tally（2 张）

| 表名 | 说明 |
| --- | --- |
| `FC_ACCBOOK_IMPORT` | 记账凭证导入表 |
| `FC_UNION_ACCBOOK` | 网点往来账务信息表 |

### 历史归档表与临时表

以下表非业务主表，仅归档/临时用途，不计入领域业务表口径：

| 表名 | 类型 | 说明 |
| --- | --- | --- |
| `FC_ACCBOOK_DTL_HIS` | 历史归档表 | 流水账明细历史表 |
| `FC_ACCBOOK_EXT_HIS` | 历史归档表 | 流水账主档扩展历史表 |
| `FC_ACCBOOK_HIS` | 历史归档表 | 流水账主档历史表 |
| `FC_OUT_ACCBOOK_DTL_HIS` | 历史归档表 | 表外流水账明细历史表 |
| `FC_INTEREST_DTL_TEMP` | 临时表 | 利息明细临时表 |
| `FC_PREDRAW_DTL_TEMP` | 临时表 | 预提明细临时表 |

### 接口能力

> 说明：本服务对外 API 定义于 `-client` 模块（见仓库代码），此处不再重复罗列。

### 依赖服务

> 说明：跨服务调用关系见仓库实现（Dubbo 引用），此处不再重复罗列。
