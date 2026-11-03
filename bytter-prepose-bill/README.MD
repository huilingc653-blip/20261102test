# bytter-prepose-bill（承兑汇票 / 票据服务）

> 2026-09-09 依据代码审读与 DDL 事实全面修订：补齐各业务子域表清单、中文名、依赖与交付图；领域业务表（历史/日志/中间类型基表未全列，详见 `tools/service-db-diagram/sources/bill/` 账本）。

## 模块介绍

承兑汇票业务（T6 `bt-biz-bill / billBankImp / billPool` 迁移）。domain 代码中除票据本体外，还内嵌了大量历史遗留 / 引用子系统表（授信 CMS、供应链融资 SCF、担保 GUA、评级 RAT、合同 CON 等），这些子域与票据主流程耦合极低（多为票据池融资申请挂接授信合同），故数据库关系图按主题拆分交付。

### 代码分层
```
- bytter-prepose-bill
    - bytter-bill-adapter    MVC controller(web/mobile/wap) + vm
    - bytter-bill-client      对外暴露 API
    - bytter-bill-domain      业务接口实现 + 数据库访问（bill / billbank / billPool 三包）
    - bytter-bill-start       启动类 + yml
```

### 子域职责

| 子域 | 职责 | 主表 |
|---|---|---|
| bill | 应收/应付票据主生命周期（登记-持有-背书/贴现/质押-托收/兑付-结清） | AB_VOUCHERINFO / AB_VOUCHERINFO_VICE / AB_ACCRECORDS |
| billbank | 电票指令与报文执行（签收/承兑/背书/贴现/质押等银行指令） | AB_BILL_RECEIVEBIZ / AB_BACKBILLINFO |
| billPool | 集团票据池（入池/出池/额度/保证金/池融资） | AB_POOL / AB_POOL_DTL / AB_POOL_LIMIT |
| 遗留引用 | 授信 CMS/融资 SCF/担保 GUA/评级 RAT/合同 CON 等（耦合低） | 各自前缀簇，独立画图 |

## 领域业务表（票据主域，44 张 · 应收/应付共用主从表+流水链）

| 表名 | 中文说明 |
|---|---|
| AB_VOUCHERINFO | 应收/应付票据主表 |
| AB_VOUCHERINFO_VICE | 票据从表(持有记录/箱位) |
| AB_ACCRECORDS | 票据流水表(业务状态时间轴) |
| AB_BACKBILLINFO | 票据背面信息表 |
| AB_BILL_SOURCE_DTL | 票据业务来源明细表 |
| AB_BILL_MIDDLE | 票据中间表(提醒/补录) |
| AB_BILL_ATTACHMENT | 票据附件表 |
| AB_BILL_TRANSFER | 票据下拨表 |
| AB_BILL_TRANSFER_DTL | 票据下拨与流水对应表 |
| AB_EMITBILL_PAY | 应付票据兑付信息表 |
| AB_DISCOUNT | 票据贴现信息表 |
| AB_BILL_ACC_BAL | 票据账户余额表 |
| AB_BILL_ACC_DTL | 票据账户余额明细表 |
| AB_BILL_RECEIVEBIZ | 电票签收表 |
| AB_LOG | 票据日志表 |
| AB_BILL_STATUS | 票据主状态字典 |
| AB_BRIEF_STATUS | 票据流水业务状态字典 |
| AB_CREDITINFO_RESPONSE | 支付信用/额度响应表 |
| AB_BILL_BANK_AUTO_DTL | 银行自动业务配置表 |
| AB_BILL_SIGN_STRATEGY | 电票签收策略配置表 |
| AB_BILL_SIGN_STRATEGY_DATA | 电票签收策略数据表 |
| AB_ADD_CREDIT_INFO | 补充授信信息表 |
| AB_BILL_CREDIT_INFO_MAINTAIN | 授信信息维护表 |
| AB_DRAFT_EXC | 电票指令异常表 |
| AB_POOL | 集团票据池主表 |
| AB_POOL_DTL | 票据池明细表 |
| AB_POOL_LIMIT | 票据池额度表 |
| AB_POOL_LIMIT_DTL | 票据池额度明细表 |
| AB_POOL_LIMIT_HIS | 票据池额度历史表 |
| AB_POOL_LIMIT_APPLY | 票据池额度申请单 |
| AB_POOL_LIMIT_APPLYCHECK | 票据池额度申请复核表 |
| AB_POOL_LIMIT_ALLOT | 票据池额度分配表 |
| AB_POOL_LIMIT_ALLOTCHECK | 票据池额度分配复核表 |
| AB_POOL_BAIL | 票据池保证金表 |
| AB_POOL_BAIL_BOOK | 票据池保证金登记簿 |
| AB_POOL_BAIL_DTL | 票据池保证金明细表 |
| AB_POOL_CONTRACT | 票据池合同表 |
| AB_POOL_CORP | 票据池成员单位表 |
| AB_POOL_BUSTYPE | 票据池业务类型表 |
| AB_POOL_BUS_STATUS | 票据池业务状态字典 |
| AB_POOL_SIGN_INFO | 票据池签收信息表 |
| AB_POOLIF | 票据池接口信息表 |
| AB_POOL_FINAAPPLY_BOOK | 票据池融资申请单 |
| AB_POOL_FINAAPPLY_DTL | 票据池融资申请明细 |

## 遗留/引用子域表（各簇独立，目录见 `tools/service-db-diagram/sources/bill/`）

### 授信/融资(CMS)（79 张）—— 信贷/融资申请、合同、还款计划等自闭环子域

关系图：`tools/service-db-diagram/work/bill_credit_erd.png`

| 表名 | 中文说明 |
|---|---|
| CMS_ACCRUAL_TYPE | 债劵展期表 |
| CMS_PROCESS_MSG | 信贷业务处理消息表 |
| CMS_REPAYMENT_SCHEDULE | 还款计划主表 |
| CMS_LOAN_TYPE | 贷款类型表 |
| CMS_REPAYMENT_SCHEDULEDETAILED | 还款计划明细表 |
| CMS_REPAY_TYPE | 利息还款类型（整体推断） |
| CMS_BOND_EXTEND | 债券展期表 |
| CMS_BOND_INTEREST | 债劵利息预计明细 |
| CMS_BOND_ISSUE_INFO | 债券发行 |
| CMS_BOND_PAY | 债劵还款支付表 |
| CMS_BOND_REGISTER_INFO | 债券注册 |
| CMS_BOND_REPAY_PLAN | 债劵还款计划主表 |
| CMS_BOND_REPAY_PLAN_DTL | 债劵还款计划明细表 |
| CMS_BOND_SWAP | 可交债信息表 |
| CMS_BOND_TRANSFER | 可转债信息表 |
| CMS_BOND_TYPE | 债券类型 |
| CMS_EQUITY_INFO | 股权融资表 |
| CMS_EQUITY_INFO_DETAIL | 股权融资明细表 |
| CMS_EQUITY_PAY_PLAN | 缴付/过户计划主表 |
| CMS_EQUITY_PAY_PLAN_DETAIL | 缴付/过户计划明细表 |
| CMS_EQUITY_PAY_THE | 股权融资缴付到账表 |
| CMS_EQUITY_REPO | 股权融资回购表 |
| CMS_EQUITY_REPO_PLAN | 股权融资回购计划主表 |
| CMS_EQUITY_REPO_PLAN_DETAIL | 股权融资回购计划明细表 |
| CMS_EQUITY_TYPES | 融资租赁类型 |
| CMS_INL_ABOLISH | 内部贷款注销（整体推断） |
| CMS_INL_ADJUSTRATE | 内部贷款利率调整（整体推断） |
| CMS_INL_REPAYPLAN_CHANGE | 内部贷款还款计划变更（整体推断） |
| CMS_INL_DEFERRED | 利率内部贷款展期（整体推断） |
| CMS_IN_HISTORY | 还款入账历史（整体推断） |
| CMS_INL_INFO | 利率内部贷款信息（整体推断） |
| CMS_INL_REPAY | 账户内部贷款还款（整体推断） |
| CMS_INL_SEND | 利率内部贷款发放（整体推断） |
| CMS_FINANCING_LEASE_INFO | 融资租赁表 |
| CMS_LEASE_REPO | 融资租赁退租数据 |
| CMS_LEASE_CAPITAL_PLAN | 租赁计划主表 |
| CMS_LEASE_CAPITAL_PLANDEL | 租赁计划明细表 |
| CMS_LEASE_CONTINUE | 融资租赁退租数据 |
| CMS_LEASE_EXTENDED | 租赁展期表 |
| CMS_LEAS_INTEREST | 租赁利息预计明细 |
| CMS_TENANCY_ITEMS_TYPES | 融资租赁基础数据 |
| CMS_LEASE_SUB_MATER | 租赁标的物表 |
| CMS_LEASE_PAY | 租赁租金支付表 |
| CMS_LEASE_REFUND | 融资租赁退租数据 |
| CMS_LEASE_UPDATE | 融资租赁变更表 |
| CMS_CONTRACT | 贷款合同表 |
| CMS_LOAN_DEFERRED | 贷款展期申请表 |
| CMS_LOAN_INFO | 贷款信息表 |
| CMS_LOAN_REPAY | 贷款还款表 |
| CMS_LOAN_SEND | 贷款发放表 |
| CMS_PRECATIVE_LOAN_DEFERRED | 委托贷款展期申请表 |
| CMS_PRECATIVE_LOAN_INFO | 委托贷款信息表 |
| CMS_PRECATIVE_LOAN_REPAY | 委托贷款还款信息表 |
| CMS_PRECATIVE_LOAN_SEND | 委托贷款发放表 |
| CMS_FLOATING_MODE | 利率浮动方式（整体推断） |
| CMS_INTEREST_WAY | 利息方式（整体推断） |
| CMS_PROVIDE_HISTORY | 功能介绍 : <br> |
| CMS_PROVIDE_INTEREST | 借款利息预计明细 |
| CMS_PROVIDE_INTEREST_TEMP | 借款利息预计明细暂存表 |
| CMS_PROVIDE_LOAN_ABOLISH | 借款作废申请表 |
| CMS_PROVIDE_LOAN_INFO | 借款信息表 |
| CMS_PROVIDE_LOAN_RATE | 借款利率调整表 |
| CMS_PROVIDE_LOAN_REPAY | 借款还款 |
| CMS_PROVIDE_LOAN_DEFERRED | 借款展期申请表 |
| CMS_PROVIDE_LOAN_SEND | 借款发放表 |
| CMS_PROVIDE_SYNDICATED | 银团明细信息 |
| CMS_PROVIDE_SYNDICATEDDTL | 银团贷款还款明细表 |
| CMS_PROVIDE_SYNDICATED_SEND | 银团贷款发放表 |
| CMS_SCHEDULE_DTL_TEMP | 还款计划明细表暂存表 |
| CMS_SECURITY_ASSET_TYPE | 基础资产类型表 |
| CMS_SECURITY_INFO | 资产证券化登记主信息表 |
| CMS_SECURITY_ISSUE | 资产证券化发行表 |
| CMS_SECURITY_LEVEL | 资产证券化层级表 |
| CMS_SECURITY_LEVEL_INFO | 资产分层信息表 |
| CMS_SECURITY_PLAN | 资产证券化还款计划主表 |
| CMS_SECURITY_PLAN_DETAIL | 资产证券化还款计划明细表 |
| CMS_SECURITY_REPAY_SURE | 资产证券化还款确认表 |
| CMS_SECURITY_TO_ACCOUNT | 资产证券化到账表 |
| CMS_SECURITY_TYPE | 资产证券化类型表 |

### 供应链融资(SCF)（19 张）—— 供应链付款凭证/融资指令子域

关系图：`tools/service-db-diagram/work/bill_scf_erd.png`

| 表名 | 中文说明 |
|---|---|
| SCF_INSTRUCTION | 指令管理 |
| SCF_INVOICE_INFO | 凭证发票信息（整体推断） |
| SCF_CUST_FIELD_CONFIG | 客户自定义字段配置 |
| SCF_PAYABLE_VOUCHER | 合同应付凭证（整体推断） |
| SCF_PAY_CONFIG | 应付债权页面字段配置表 |
| SCF_PAY_CONFIG_VERSION | 应付债权模板配置版本表 |
| SCF_PAY_FIELD_CONFIG | 应付债权字段预设表 |
| SCF_PAY_TEMPLATE_CONFIG | 应付债权模板配置 |
| SCF_VOUCHER_CUSTOM_FIELD | 凭证自定义字段（整体推断） |
| SCF_VOUCHER_REDEMPTION | 应付凭证赎回（整体推断） |
| SCF_BANK_OPEN | 接口开通情况表 |
| SCF_ENT_BANK_OPEN | 平台接口开通配置 |
| SCF_FUNDER_INFO | 资金方信息 |
| SCF_PLATFORM | 平台（整体推断） |
| SCF_PRODUCT | 供应链融资产品表 |
| SCF_LOAN_INFO | 融资业务主信息 |
| SCF_RECEIVABLE_PAYMENT_INFO | 凭证应收支付信息（整体推断） |
| SCF_RECEIVABLE_TRANSFER_INFO | 凭证应收转让信息（整体推断） |
| SCF_RECEIVABLE_VOUCHER_INFO | 应收债权凭证信息 |

### 担保(GUA)（16 张）—— 担保/保函合约子域

关系图：`tools/service-db-diagram/work/bill_gua_erd.png`

| 表名 | 中文说明 |
|---|---|
| GUA_CONRRANT_CHANGE_TRACK | 质押担保变更变更轨迹（整体推断） |
| GUA_CONTRACT_INFO | 担保合同信息 |
| GUA_CONTRACT_OCCUP | 担保合同占用（整体推断） |
| GUA_CONTRACT_PLEDGE | 担保合同关联抵质押物表 |
| GUA_CONTRACT_WAY | 担保合同关联担保方式表 |
| GUA_EXGUARANTEE_AMOUNT | 对外担保金额（整体推断） |
| GUA_EXGUARANTEE_CORP | 对外担保额度与单位中间表 |
| GUA_GUARANTEE_CHANGE_INFO | 保函信息变更 |
| GUA_GUARANTEE_INFO | 保函申请 |
| GUA_LOG | 担保日志（整体推断） |
| GUA_PLEDGE_INFO | 抵质押物信息表 |
| GUA_PLEDGE_INFO_HIS | 抵质押物历史表 |
| GUA_PLEDGE_OCCUP | 抵质押物占用 |
| GUA_PLEDGE_TYPE | 抵质押类型 |
| GUA_WAY | 担保方式 |
| GUA_PERFORM_REG | 保函类型登记 |

### 授信评级(RAT)（23 张）—— 授信评级/限额子域

关系图：`tools/service-db-diagram/work/bill_rating_erd.png`

| 表名 | 中文说明 |
|---|---|
| RAT_BKRATION | 授信额度录入表 |
| RAT_BKRATION_ADD | 授信追加信息表 |
| RAT_BKRATION_ADD_DETAIL | 授信追加明细表 |
| RAT_BKRATION_ADJUST | 授信调整 |
| RAT_BKRATION_ADJUST_DETAIL | 授信调整明细 |
| RAT_BKRATION_DELAY | 授信延期表 |
| RAT_BKRATION_DETAIL | 授信录入明细表 |
| RAT_BKRATION_DIS | 授信分配信息表 |
| RAT_BKRATION_DIS_DETAIL | 授信分配明细表 |
| RAT_BKRATION_RELIEF | 授信额度调剂表 |
| RAT_BKRATION_RELIEF_DETAIL | 授信调剂明细表 |
| RAT_BUSINESS_RACE | 授信合作银行维护表 |
| RAT_CREDIT_COOPERATION_BANK | 授信合作银行维护表 |
| RAT_CREDIT_TYPE | 授信额度类型 |
| RAT_RATION_DETAIL_DISHIS | 授信分配历史明细表 |
| RAT_ITEM_CUSTOM | 授信品种 |
| RAT_ITEM_TYPE | 授信品种 |
| RAT_PROCESS_LOG | 授信操作日志 |
| RAT_RATION_LOG | 授信日志 |
| RAT_URATION | 授信占用台账表 |
| RAT_URATION_APPLY | 授信占用申请表 |
| RAT_URATION_APPLY_DTL | 授信占用明细表 |
| RAT_YEARFIN_PLAN_REGISTER | 年度融资规划登记表 |

### 合同(CON)（10 张）—— 合同登记/挂接子域

关系图：`tools/service-db-diagram/work/bill_contract_erd.png`

| 表名 | 中文说明 |
|---|---|
| CON_BUS_HOOK | 合同业务挂接表 |
| CON_INFO | 合同主表 |
| CON_INFO_BUSDTL | 合同主信息-合同字段明细表 |
| CON_INFO_RECORD | 合同申请流水表 |
| CON_LOG | 合同管理日志 |
| CON_PD_MAP | 合产品类型映射主表 |
| CON_PD_MAPDTL | 合产品类型映射明细表 |
| CON_TYPE | 合同类型表 |
| CON_TYPE_FIELD | 合同类型字段明细表 |
| CON_WARN_RULES | 合同预警规则 |

## 主要业务主路径（应收/应付双主臂）

- 应收：建票登记(6001/电票签收) → 审批(95) → 入库持有 → 背书/贴现/质押/入池 → 到期托收到账 → 结清
- 应付：开票登记 → 审批(95) → 到期兑付(NIS 付款单回写) → 结清释放授信/保证金
- 异常：审批打回 -1 回流重编；退票/拒付走追索
- 业务事件统一记 AB_ACCRECORDS 流水链（brief_id 业务状态 010/011/018/6120…），主从表 AB_VOUCHERINFO(+_VICE)

## 依赖服务与接力

| 方向 | 方式 | 说明 |
|---|---|---|
| bytter-workflow | Dubbo | 各类票据流程(EMIT/RECEIVEBILL/…)：各 Service 起流程，`@DubboService(IWorkFlowListener, group=流程key)` 收回调 |
| bytter-service-payment | Dubbo | NIS 付款单/到账（INisBillheadService/IBisExcService 等） |
| bytter-service-payment | MQ | 背书-付款链路由 payment 推 MQ → BillEndorsementToPaymentEvent 收 |
| bytter-bdlink | 常量/直查 | 网银/报文状态常量（NetbankConstant） |
| bytter-prepose-accountReceivable | — | 未发现直接引用（应收/应付票据凭证在模块内自建） |

## 交付图（tools/service-db-diagram/work/）

| 图 | 主题 | 说明 |
|---|---|---|
| bill_erd.png | 票据业务·数据库表关系图 | 44 表/18 边，按业务 6 列（登记·流水·持有·应付·池·信用配置） |
| bill_credit_erd.png | 授信/融资(CMS) | 79 表/25 边，次前缀多列 |
| bill_scf_erd.png | 供应链融资(SCF) | 19 表/17 边 |
| bill_gua_erd.png | 担保(GUA) | 16 表 |
| bill_rating_erd.png | 授信评级(RAT) | 23 表 |
| bill_contract_erd.png | 合同(CON) | 10 表/3 边 |
| bill_business_flow.workflow.tables.html | 票据业务主流程(双主臂) | 9 节点/25 表引用，交互式透镜 |
| bill_credit_business_flow.workflow.tables.html | 授信/融资业务主流程 | 7 节点/14 表引用（内部贷款主干） |
| bill_scf_business_flow.workflow.tables.html | 供应链融资业务主流程 | 7 节点/13 表引用（应收债权凭证主干） |
| bill_gua_business_flow.workflow.tables.html | 担保业务主流程 | 7 节点/15 表引用（担保合同+抵质押物） |
| bill_rating_business_flow.workflow.tables.html | 授信额度业务主流程 | 7 节点/13 表引用（录入→分配→占用→释放） |
| bill_contract_business_flow.workflow.tables.html | 合同业务主流程 | 7 节点/15 表引用（起草→登记→签署→变更/履约→终止） |

渲染源与再生成命令见 `tools/service-db-diagram/README.md` 与项目技能 `service-db-evidence`（render-preference）。
