# 支付结算模块（bytter-service-payment）

## 模块介绍

### 主要业务
- 对外支付（多级单位归集 / 调拨 / 自动支付策略）
- 内部结算（内结、外汇结算、敞口/资金池）
- 银企直连（回单下载勾对、电子回单）
- 第三方支付（三方支付通道、退款、限额）
- 银行账户（虚拟账号、冻结、账户组、付款方式分配）

### 代码分层
```
- bytter-service-payment

    - bytter-payment-adapter

      适配层，对应 MVC 的 Controller，使用 web|mobile|wap 按包名隔离，VM 资源存放于此层
    - bytter-payment-client

      定义暴露的 API 接口，供其他服务调用
    - bytter-payment-domain

      主要业务处理接口实现、数据库访问与数据组装，后续 DDD 改造升级重点重构模块
    - bytter-payment-start

      启动类、yml 配置文件
```

### 数据库

- 数据库：Oracle，schema 用户 `bank430`（`jdbc:oracle:thin:@192.168.0.217:1521/orcl`），与全平台其他服务共用同一模式，无独立 schema。
- 本服务代码 `@Table` 注解共映射 156 张表：业务表 154 张、历史归档表 1 张、临时表 1 张。

### 领域内业务表

> 业务表口径：与 increment 一致，历史归档表（`*_HIS`）、临时表（`*_TMP`/`*_TEMP`）单列于本页末尾；表名为代码 `@Table` 实体注解映射值，`说明`取自实体类 Javadoc `@Description` 或 SQL `COMMENT ON TABLE`，空项以 `—` 表示（对应表可查 Oracle `ALL_TAB_COMMENTS` 或实体注释确认语义）。同表被多实体映射时，实体以 `/` 分隔。

业务表按表名前缀分组，共 154 张：

#### `BIS_*` 前缀（27 张）

| 表名 | 说明 |
| --- | --- |
| `BIS_ACC_DTL_IMPORT_CONFIG` | 离线流水智能导入配置表 |
| `BIS_ACC_DTL_IMPORT_CONFIGDTL` | 离线流水智能导入配置表 |
| `BIS_ACC_GATHER_POLICY` | 银行账号关系策略维护表 |
| `BIS_BANK_EXECUTION_STRATEGY` | 三单关联执行策略表 |
| `BIS_BANK_RECEIPT_DTL` | 银行电子回单 |
| `BIS_BANK_RECEIPT_IMPORT` | 银行回单导入表 |
| `BIS_BANK_RECEIPT_PDF` | 银行电子回单 |
| `BIS_CORP_POOL_BAL` | 多级单位余额核算-单位归集余额表 |
| `BIS_CORP_POOL_CONFIG` | 多级单位余额核算-单位归集配置表 |
| `BIS_CORP_POOL_CONFIG_RULE` | 多级单位余额核算-单位归集配置规则表 |
| `BIS_CORP_POOL_OVERDRAFT` | 多级单位余额核算-单位归集透支金额表 |
| `BIS_EXC` | 付款指令表 |
| `BIS_EXC_MID_ACC` | SYS_CA_INFO |
| `BIS_FAIL_REASON` | 交易失败原因表 |
| `BIS_FOREIGN_EXC` | 外币汇款指令表 |
| `BIS_GATHER_CMD` | 收款指令表 |
| `BIS_GATHER_POLICY` | 调拨策略表 |
| `BIS_GATHER_POLICY_ACCOUNT` | 资金中心调拨账户配置 |
| `BIS_GATHER_POLICY_AGREEMENT` | 内部资金归集管理协议签订 |
| `BIS_PAY_POLICY` | 自动支付策略表 |
| `BIS_PAY_POLICY_BUSTYPE` | 自动支付策略业务类别关联表 |
| `BIS_PAY_POLICY_PAYEE` | 自动支付策略收款人关联表 |
| `BIS_PAY_POLICY_PAYERACC` | 自动支付策略支付账号关联表 |
| `BIS_PAY_POLICY_SORT` | 自动支付优先策略表 |
| `BIS_POLICY_FILTER` | 例外规则表 |
| `BIS_RECEIPT_DTL` | 银行电子回单勾对中心表 |
| `BIS_VIRTUAL_ACC_EXC` | 虚拟账号操作指令表 |

#### `BP_*` 前缀（1 张）

| 表名 | 说明 |
| --- | --- |
| `BP_DUBIOUS_TRADE` | BP_DUBIOUS_TRADE 可疑交易 |

#### `BT_*` 前缀（11 张）

| 表名 | 说明 |
| --- | --- |
| `BT_ASSIGN_PAY_WAY` | 付款方式分配表 |
| `BT_ASSIGN_PAY_WAY_USER` | 付款方式用户分配表 |
| `BT_BANKACC_MESSAGE` | BtBankAccMessage |
| `BT_BANK_ACC_BAL` | 银行账户实时余额表 |
| `BT_BANK_ACC_DTL` | 银行账号明细录入表 |
| `BT_BANK_ACC_FREEZE` | 银行账号冻结解冻申请表 |
| `BT_BANK_ACC_PLAN_FREEZE` | 计划冻结申请表 |
| `BT_BANK_ACC_USE` | 账号用途维护 |
| `BT_BANK_ACC_VIRTUAL` | 虚拟账号记账单元信息 |
| `BT_CHARGE_BACK_TYPE` | 退单类型表 |
| `BT_PAY_TYPE` | 系统结算方式表 |

#### `CGS_*` 前缀（1 张）

| 表名 | 说明 |
| --- | --- |
| `CGS_BILL` | 委托收款 |

#### `E_*` 前缀（15 张）

| 表名 | 说明 |
| --- | --- |
| `E_ACCEPT_BILL` | 承兑汇票电子回单表 |
| `E_ACCRUAL_BILL` | 利息清单电子回单 |
| `E_BANK_BILL_ATTACHMENT` | 银行电子回单附件关联表 |
| `E_BILL_MESSAGE` | 电子回单消息表 |
| `E_BILL_PRINT` | 电子回单打印 |
| `E_COMPARE_BILL` | 对账单电子回单表 |
| `E_CONSIGN_BILL` | 委托收款电子回单表 |
| `E_EXCHANGE_BILL` | 内部结售汇电子回单 |
| `E_LOAN_BILL` | 贷款电子回单表 |
| `E_NOTICE_BILL` | 通知单电子回单表 |
| `E_POUNDAGE_BILL` | 手续费电子回单表 |
| `E_PRECATIVE_LOAN_BILL` | 委托贷款电子回单表 |
| `E_RECEIPT_TYPE` | 电子回单类型表 |
| `E_TRANS_BILL` | 账户转换电子回单表 |
| `E_VISA_TYPE` | 签章类型维护 |

#### `FC_*` 前缀（7 张）

| 表名 | 说明 |
| --- | --- |
| `FC_BANK_ACC_GROUP` | 银行账户组 |
| `FC_BANK_ACC_GROUP_AREA` | 银行账户组区域关联表 |
| `FC_CORP_COMPARE_BILL` | 单位对账功能 |
| `FC_FREEZE` | 内部账号冻结解冻 |
| `FC_INIT_ACC` | 内部账号初始化 |
| `FC_RECEIPT_TYPE` | 单据类型表 |
| `FC_VOUCHER_TYPE` | 自定义付款方式表 |

#### `FEX_*` 前缀（53 张）

| 表名 | 说明 |
| --- | --- |
| `FEX_ADD_WORD` | 交易附言表 |
| `FEX_CASH_FLOW` | 结算现金流表 |
| `FEX_CASH_FLOW_CUST_INFO` | 交易管理自定义信息表 |
| `FEX_CORP_BANKACC` | 主体单位默认银行账户实体 |
| `FEX_CORP_BANKACC_DTL` | 主体单位默认银行账户明细实体 |
| `FEX_CORP_EXPOSURE` | 单位敞口设置表 |
| `FEX_CORP_EXPOSURE_TEMPLATE` | 单位敞口模板表 |
| `FEX_CORP_POOL` | 外汇资金池表 |
| `FEX_CORP_POOL_BANKACC` | 外汇资金池账户表 |
| `FEX_CORP_POOL_LIMIT` | 外汇资金池限额设置表 |
| `FEX_CORP_POOL_TRAN` | 外汇资金池交易表 |
| `FEX_COTP` | 交易对手 |
| `FEX_COTP_BANKACC` | 交易对手银行账户信息 |
| `FEX_COTP_BANKACC_CUR` | 交易对手账号币种关系表 |
| `FEX_COTP_CUST_INFO` | 自定义字段 |
| `FEX_CUST_FIELD_RULE` | 自定义字段维护 |
| `FEX_CUST_FIELD_RULE_TENANT` | 租户自定义字段规则实体。 |
| `FEX_CUST_INFO_TENANT` | 租户交易自定义信息实体。 |
| `FEX_EXPOSURE` | 单位敞口上报表 |
| `FEX_EXPOSURE_DETAIL` | 单位敞口上报明细表 |
| `FEX_EXPOSURE_ITEM` | 敞口科目表 |
| `FEX_EXPOSURE_MAIN` | 单位敞口上报表 |
| `FEX_EXPOSURE_TEMPLATE` | 敞口模板表 |
| `FEX_EXPOSURE_TEMPLATE_ITEM` | 敞口模板科目表 |
| `FEX_FOREIGN_REQ` | 外币汇款申请表 |
| `FEX_LINKMAN_INFO` | 汇款联系人信息表 |
| `FEX_MESSAGE` | 外汇管理日志表 |
| `FEX_PAYER_INFO` | 汇款人信息表 |
| `FEX_PRODUCT` | 外汇产品表 |
| `FEX_PROFIT_LOSS_MANAGE` | 损益信息 |
| `FEX_PROFIT_LOSS_MANAGE_DTL` | 损益信息 |
| `FEX_RATE_EXC` | 外汇牌价查询指令表 |
| `FEX_RATE_INFO` | 外汇牌价表 |
| `FEX_RATE_TYPE` | 外汇牌价类型表 |
| `FEX_SETTLE_FILL_RULE` | 结算自动填充规则 |
| `FEX_SETTLE_FILL_RULE_ADD` | 结算自动填充规则补充表 |
| `FEX_TRAN_APP` | 交易需求申请主表 |
| `FEX_TRAN_APP_DTL` | 交易需求申请从表 |
| `FEX_TRAN_CUST_INFO` | 交易管理自定义信息表 |
| `FEX_TRAN_DEAL` | 交易管理处理表 |
| `FEX_TRAN_GROUP` | 交易组 |
| `FEX_TRAN_GROUP_RULE` | 交易组规则表 |
| `FEX_TRAN_GROUP_RULE_DTL` | 交易组规则明细表 |
| `FEX_TRAN_INFO` | 汇款人信息表 |
| `FEX_TRAN_LIFE_CYCLE` | 生命周期表 |
| `FEX_TRAN_OPTION_DEAL` | 外汇期权交易处理表 |
| `FEX_TRAN_REQ` | 交易管理表 |
| `FEX_TRAN_REQ_EXT` | 交易管理扩展表 |
| `fex_tran_config_operation_log` | 操作日志 |
| `fex_tran_req_config` | 交易页面配置 |
| `fex_tran_req_config_version` | 外汇交易模板配置版本表 |
| `fex_tran_req_field` | 交易请求字段配置表 |
| `fex_tran_template_config` | 交易模板配置 |

#### `NIS_*` 前缀（28 张）

| 表名 | 说明 |
| --- | --- |
| `NIS_ALLOCATION_REQ` | 资金调拨制单表 |
| `NIS_BILL` | 付款票据关联表 |
| `NIS_BILLHEAD` | 付款通知表 |
| `NIS_BILLHEAD_POOL` | 付款通知表 |
| `NIS_BUSINESS_TYPE` | 业务类型表 |
| `NIS_BUS_TYPE` | 业务类别表 |
| `NIS_BUS_TYPE_RULE` | 业务种类规则表 |
| `NIS_CAPITAL_RECORD` | 资金备案台账 |
| `NIS_CORP_BUS` | 单位业务类别对照表 |
| `NIS_CORP_CASHIER` | 单位出纳表 |
| `NIS_CORP_CASHIER_USER` | 单位出纳表用户表 |
| `NIS_DEMAND` | 付款通知表 |
| `NIS_EXPENSE_DETAIL` | 费用报销表 |
| `NIS_FLOW_RULE` | 资金调拨流水规则表 |
| `NIS_FLOW_RULE_PURPOSE` | 资金调拨流水规则用途表 |
| `NIS_MESSAGE` | 内网日志表 |
| `NIS_OPERATION_TYPE` | 操作类型表 |
| `NIS_PAY_SCHEDULING_ELEMENTS` | 付款排期要素实体 |
| `NIS_PERSONNEL_DETAIL` | 职工信息明细表 |
| `NIS_PROJECT` | 项目维护表 |
| `NIS_RECEIVABLE` | 收款通知表 |
| `NIS_RELATED_PARTY` | 关联方维护表 |
| `NIS_SCF_INFO` | 付款单-供应链金融债权凭证关联表（中间表） |
| `NIS_SHARE_BUS_TYPE` | 共享业务类型表 |
| `NIS_WAGE` | NIS_WAGE 代发工资总表 |
| `NIS_WAGE_DETAIL` | NIS_WAGE_DETAIL 代发工资明细表 |
| `NIS_WAGE_FLOAT_RATIO_CONFIG` | 代发工资浮动比率配置 |
| `nis_wage_release_config` | 代发工资下拨金额配置 |

#### `TP_*` 前缀（11 张）

| 表名 | 说明 |
| --- | --- |
| `TP_BUSINESS` | 商户表 |
| `TP_BUSINESS_CORP` | 商户表 |
| `TP_BUSINESS_TEXT` | 商户关联信息表 |
| `TP_LOG` | 第三方支付日志 |
| `TP_MERCHANT` | 商户信息对应实体 |
| `TP_MERCHANT_VAR` | 商户参数信息对应实体 |
| `TP_PAYMENT` | 第三方支付实体类 |
| `TP_PAY_TYPE` | 支付类型配置表 |
| `TP_QUOTA` | 限额管理对应实体 |
| `TP_REFUND_ORDER` | 退款信息entity |
| `TP_SHOPTYPE` | 商户类别维护 |

### 历史归档表与临时表

以下表非业务主表，仅归档/临时用途，不计入领域业务表口径：

| 表名 | 类型 | 说明 |
| --- | --- | --- |
| `BIS_BANK_RECEIPT_DTL_HIS` | 历史归档表 | 银行电子回单 |
| `FEX_TRAN_APP_DTL_TMP` | 临时表 | 交易需求申请从表 |

### 接口能力

> 说明：本服务对外 API 定义于 `-client` 模块（见仓库代码），此处不再重复罗列。

### 依赖服务

> 说明：跨服务调用关系见仓库实现（Dubbo 引用），此处不再重复罗列。
