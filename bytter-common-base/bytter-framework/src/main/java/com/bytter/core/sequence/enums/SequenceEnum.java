package com.bytter.core.sequence.enums;
import com.bytter.core.sequence.entity.MultiTable;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
/**
 * 流水号生成枚举类<br/>
 * 定义流水号的种类以及生成策略<br/>
 * <b>注意：</b><br/>1、新增枚举类型时，请确保填写的表名和字段名真实存在。<br/>
 *
 * @author 周亮
 * @date 17/5/27
 */
public enum SequenceEnum {
    /**
     * SequenceSingleton.getSequence(SequenceEnum.S1);  ---> 0001 <br>
     * 测试流水号，正常业务开发请勿使用
     */
    S1(SequenceTypeEnum.NORMAL, 4, "BYTTER_TEST", "NAME"),
    /**
     * SequenceSingleton.getSequence(SequenceEnum.S2);  ---> S2201706090001 <br>
     * 测试流水号，正常业务开发请勿使用
     */
    S2(SequenceTypeEnum.NORMAL, "S2", 6, "TEST", "NAME", SequenceDateEnum.yyyyMMdd),
    /**
     * SequenceSingleton.getSequence(SequenceEnum.S1);  ---> SC201706090001 <br>
     * 测试流水号，正常业务开发请勿使用
     */
    S3(SequenceTypeEnum.BANK, "SC", 6, "Test", "A1"),
    /**
     * 流水号生成
     */
    ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 6, SequenceDateEnum.yyyyMMdd, new MultiTable("FC_ACCBOOK", "ORDER_NO"), new MultiTable("FC_ACCBOOK_BLNED", "ORDER_NO")),
//    ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 6, SequenceDateEnum.yyyyMMdd, true, new MultiTable("FC_ACCBOOK", "ORDER_NO"), new MultiTable("FC_ACCBOOK_BLNED", "ORDER_NO")),
    /**
     * 凭证号生成
     */
    VOUCHER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 5, SequenceDateEnum.yyyyMM, new MultiTable("FC_ACCBOOK", "VOUCHER_NO"), new MultiTable("FC_ACCBOOK_BLNED", "VOUCHER_NO")),
    VOUCHER_NO_TALLY(SequenceTypeEnum.SPECIAL_PREFIX, 5, null, new MultiTable("FC_ACCBOOK", "VOUCHER_NO"), new MultiTable("FC_ACCBOOK_BLNED", "VOUCHER_NO")),
//    VOUCHER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 5, SequenceDateEnum.yyyyMM, true, new MultiTable("FC_ACCBOOK", "VOUCHER_NO"), new MultiTable("FC_ACCBOOK_BLNED", "VOUCHER_NO")),
    /**
     * 流水号生成
     */
    OUT_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 6, "FC_OUT_ACCBOOK", "ORDER_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 流水号生成
     */
    OUT_VOUCHER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 5, "FC_OUT_ACCBOOK", "VOUCHER_NO", SequenceDateEnum.yyyyMM),
    /**
     * 套打格式编号 001\002
     */
    BV_FMT_NO(SequenceTypeEnum.NORMAL, 3, "BV_PRINT_FORMAT", "FMT_NO"),
    /**
     * 银行账号申请流水号
     */
    ACC_NO(SequenceTypeEnum.NORMAL, 6, "BT_BANK_ACC_APP", "ACC_NO", SequenceDateEnum.yyMMdd),
    /**
     * 付款流水号
     */
    NIS_BILLHEAD(SequenceTypeEnum.NORMAL, "N", 6, "NIS_BILLHEAD", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 付款流水号
     */
    NIS_BILLHEAD_ERP(SequenceTypeEnum.NORMAL, "E", 6, "NIS_BILLHEAD", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 待付款池流水号
     */
    NIS_BILLHEAD_POOL(SequenceTypeEnum.NORMAL, "P", 6, "NIS_BILLHEAD_POOL", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 第三方支付 付款单号
     */
    TP_PAYMENT(SequenceTypeEnum.NORMAL, "T", 6, "TP_PAYMENT", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 第三方支付 商户订单号（指令编号）
     */
    TP_PAYMENT_BIS_EXC(SequenceTypeEnum.NORMAL, 6, "TP_PAYMENT", "TRAND_NO", SequenceDateEnum.yyMMdd),
    /**
     * 费用报销明细单号
     */
    NIS_EXPENSE_DETAIL(SequenceTypeEnum.NORMAL, "F", 6, "NIS_EXPENSE_DETAIL", "EXPENSE_ID", SequenceDateEnum.yyMMdd),
    /**
     * 保证金申请单号
     */
    NIS_FCBOND_DETAIL(SequenceTypeEnum.NORMAL, "B", 3, "FC_BOND", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 追加保证金业务单号
     */
    NIS_FCBOND_ADD(SequenceTypeEnum.NORMAL, "B1", 3, "FC_BOND_ADD", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 保证金占用业务单号
     */
    NIS_FCBOND_APPPLY(SequenceTypeEnum.NORMAL, "B2", 3, "FC_BOND_APPLY", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 保证金转回业务单号
     */
    NIS_FCBOND_BACK(SequenceTypeEnum.NORMAL, "B3", 3, "FC_BOND_BACK", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 资金调拨单号
     */
    NIS_ALLOCATION_REQ(SequenceTypeEnum.NORMAL, "A", 6, "NIS_ALLOCATION_REQ", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 代发工资单号
     */
    NIS_WAGE(SequenceTypeEnum.NORMAL, "W", 6, "NIS_WAGE", "WAGE_NO", SequenceDateEnum.yyMMdd),
    /**
     * 代发工资明细
     */
    NIS_WAGE_DETAIL(SequenceTypeEnum.NORMAL, 6, "NIS_WAGE_DETAIL", "WAGE_DTL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 委托收款委托号码
     */
    CGS_BILL_CONSIGN_CODE(SequenceTypeEnum.NORMAL, 6, "CGS_BILL", "CONSIGN_CODE", SequenceDateEnum.yyMMdd),
    /**
     * 银行账号余额
     */
    BIS_ACC_BAL(SequenceTypeEnum.NORMAL, 6, "BIS_ACC_BAL", "ACC_NO", SequenceDateEnum.yyMMdd),
    /**
     * 银行账号明细
     */
    BT_BANK_ACC_DTL(SequenceTypeEnum.NORMAL, 6, "BT_BANK_ACC_DTL", "ACC_NO", SequenceDateEnum.yyMMdd),
    /**
     * 银行账号冻结申请流水号
     */
    FREEZE_NO(SequenceTypeEnum.NORMAL, 6, "BT_BANK_ACC_FREEZE", "FREEZE_NO", SequenceDateEnum.yyMMdd),
    /**
     * 银行账号冻结申请流水号
     */
    PLAN_FREEZE_NO(SequenceTypeEnum.NORMAL, "JHDJ",6, "BT_BANK_ACC_PLAN_FREEZE", "PLAN_FREEZE_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 付款指令编码
     */
    BIS_EXC(SequenceTypeEnum.NORMAL, 6, "BIS_EXC", "VOUCHER_NO", SequenceDateEnum.yyMMdd),
    /**
     * 收款指令编码
     */
    BIS_GATHER_CMD(SequenceTypeEnum.NORMAL, "9", 6, "BIS_GATHER_CMD", "VOUCHER_NO", SequenceDateEnum.yyMMdd),
    /**
     * 查询指令编码
     */
    BIS_QUERY_CMD(SequenceTypeEnum.NORMAL, 6, "BIS_QUERY_CMD", "VOUCHER_NO", SequenceDateEnum.yyMMdd),
    /**
     * 票据下拨申请单号
     */
    BILL_TRANSFERDOWNAPPLY_NO(SequenceTypeEnum.SPECIAL_PREFIX, "SQ", 5, "AB_BILL_TRANSFER", "ALLOCATED_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 票据下拨单号
     */
    BILL_TRANSFERDOWNMANAGE_NO(SequenceTypeEnum.SPECIAL_PREFIX, "XB", 5, "AB_BILL_TRANSFER", "ALLOCATED_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 票据认领中间表序列号
     */
    BILL_CLAIM_MIDDLE_SEQUENCE(SequenceTypeEnum.NORMAL, "CM", 6, "AB_CLAIM_MIDDLE", "CLAIM_SEQUENCE", SequenceDateEnum.yyMMdd),
    /**
     * 开票登记申请单号
     */
    BILL_ABEMITBILL_APPNO(SequenceTypeEnum.NORMAL, "AB", 6, "AB_VOUCHERINFO_VICE", "APP_NO", SequenceDateEnum.yyMMdd),
    /**
     * 开票登记批量申请单号
     */
    BILL_ABEMITBILL_BATCHNO(SequenceTypeEnum.NORMAL, "BH", 6, "AB_VOUCHERINFO_VICE", "BATCH_NO", SequenceDateEnum.yyMMdd),
    /**
     * 背书请求流水号
     */
    BILL_ENDORSE_REQUEST_SERIAL_NO(SequenceTypeEnum.NORMAL, "BS", 6, "AB_ACCRECORDS", "SERIAL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 智能选票规则申请单号
     */
    BILL_RULE_APPLY_NO(SequenceTypeEnum.NORMAL, "RL", 6, "AB_INTELLECT_RULE", "APPLY_NO", SequenceDateEnum.yyMMdd),
    /**
     * 智能选票规则组申请单号
     */
    BILL_RULE_GROUP_APPLY_NO(SequenceTypeEnum.NORMAL, "RG", 6, "AB_INTELLECT_RULE_GROUP", "APPLY_NO", SequenceDateEnum.yyMMdd),
    /**
     * 委托贷款票据号
     */
    CMS_PRECATIVE_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PRECATIVE_LOAN_INFO", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 委托贷款发放票据号
     */
    CMS_PRECATIVE_SEND_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PRECATIVE_LOAN_SEND", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 委托贷款归还票据号
     */
    CMS_PRECATIVE_REPAY_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PRECATIVE_LOAN_REPAY", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 委托贷款展期票据号
     */
    CMS_PRECATIVE_DEFERRED_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PRECATIVE_LOAN_DEFERRED", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 银行对账数据表序号
     */
    FC_BANK_BILL_BANK_ID(SequenceTypeEnum.SPECIAL_PREFIX, 4, "FC_BANK_BILL", "BANK_ID", SequenceDateEnum.yyyyMMdd),
    /**
     * 贷款管理票据号
     */
    CMS_LOANINFO_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_LOAN_INFO", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 贷款发放票据号
     */
    CMS_LOANSEND_ORDER_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_LOAN_SEND", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 贷款还款票据号
     */
    CMS_LOANREPAY_BILL_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_LOAN_REPAY", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 贷款展期票据号
     */
    CMS_LOANDEFERRED_BILL_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_LOAN_DEFERRED", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信录入编号
     */
    RAT_BKRATION_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION", "RAT_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 银行授信分配编号
     */
    RAT_BKRATION_DISCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_DIS", "DIS_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信追加编号
     */
    RAT_BKRATION_ADDCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_ADD", "ADD_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信调剂编号
     */
    RAT_BKRATION_REFCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_RELIEF", "REF_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信占用申请编号
     */
    RAT_BKRATION_APPLYCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_URATION_APPLY", "APPLY_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信台账编号
     */
    RAT_BKRATION_URATIONCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_URATION", "URATION_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 授信调整编号
     */
    RAT_BKRATION_ADJUSTCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_ADJUST", "ADJUST_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信录入编号
     */
    RAT_CPRATION_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION", "RAT_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信追加编号
     */
    RAT_CPRATION_ADDCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_ADD", "ADD_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信分配编号
     */
    RAT_CPRATION_DISCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_DIS", "DIS_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信占用申请编号
     */
    RAT_CPRATION_APPLYCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_URATION_APPLY", "APPLY_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信台账编号
     */
    RAT_CPRATION_URATIONCODE(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_URATION", "URATION_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 内部授信调剂申请编号
     */
    RAT_CPRATION_RELIEF(SequenceTypeEnum.SPECIAL_PREFIX, 6, "RAT_BKRATION_RELIEF", "REF_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 融资租赁lease_no
     */
    CMS_FINANCING_LEASE_LEASE_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_FINANCING_LEASE_INFO", "LEASE_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 股权融资equity_dode
     */
    CMS_EQUITY_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_EQUITY_INFO_DETAIL", "EQUITY_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 借款申请Bill_code
     */
    CMS_PROVIDE_LOANINFO_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PROVIDE_LOAN_INFO", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 借款发放Bill_code
     */
    CMS_PROVIDE_LOANSEND_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PROVIDE_LOAN_SEND", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 借款还款Bill_code
     */
    CMS_PROVIDE_LOANREPAY_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PROVIDE_LOAN_REPAY", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 借款展期Bill_code
     */
    CMS_PROVIDE_LOANDEFERRED_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_PROVIDE_LOAN_DEFERRED", "BILL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 直融业务登记
     */
    DF_DIRECT_FINANCE_INFO_BILL_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 4, "DF_DIRECT_FINANCE_INFO", "BILL_CODE", SequenceDateEnum.yyMMdd),
    /**
     * 直融业务还款
     */
    DF_DIRECT_FINANCE_REPAY_BILL_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 4, "DF_DIRECT_FINANCE_REPAY", "BILL_CODE", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借申请
     */
    CMS_INL_INFO_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_INFO", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借发放
     */
    CMS_INL_SEND_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_SEND", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借发放
     */
    CMS_INL_REPAY_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_REPAY", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借作废
     */
    CMS_INL_ABOLISH_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_ABOLISH", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借展期
     */
    CMS_INL_DEFERRED_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_DEFERRED", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借利率调整
     */
    CMS_INL_ADJUSTRATE_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_ADJUSTRATE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部拆借还款计划调整
     */
    CMS_INL_REPLAYCHANGE_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "CMS_INL_REPAYPLAN_CHANGE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 信用证Bill_code
     */
    LC_ENROL_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_ENROL", "LC_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 交单Bill_code
     */
    LC_BILL_ARRIVAL_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_BILL_ARRIVAL", "ARRIVAL_CODE", SequenceDateEnum.yyyyMMdd),
    /**
     * 付汇Bill_code
     */
    LC_PAYMENT_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_PAYMENT", "PAYMENT_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 押汇Bill_code
     */
    LC_DEPOST_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_DEPOSIT", "DEPOSIT_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 押汇展期Bill_code
     */
    LC_DEPOST_DELAY_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_DEPOSIT_DELAY", "DELAY_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 还押汇Bill_code
     */
    LC_DEPOST_REPAY_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_DEPOSIT_REPAY", "REPAY_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 到期收款Bill_code
     */
    LC_INCOMING_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_INCOMING", "INCOME_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 出口押汇Bill_code
     */
    LC_MORTGAGE_BILL_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "LC_MORTGAGE", "MORTGAGE_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 担保管理_抵质押物编号编号
     */
    CMS_GUA_PLEDGE_NO(SequenceTypeEnum.SPECIAL_PREFIX, 4, "GUA_PLEDGE_INFO", "PLEDGE_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 保函管理_保函更改申请编号
     */
    CMS_GUARANTEE_CHANGE_NO(SequenceTypeEnum.SPECIAL_PREFIX, 3, "GUA_GUARANTEE_CHANGE_INFO", "GUARA_CHANGE_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 银行理财表
     */
    CHO_BANK_FINANCE(SequenceTypeEnum.NORMAL, 6, "CHO_BANK_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-活转定
     */
    CHO_INNER_FINANCE_C2T(SequenceTypeEnum.NORMAL, "DT", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-定转活
     */
    CHO_INNER_FINANCE_T2C(SequenceTypeEnum.NORMAL, "DW", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-活转通知
     */
    CHO_INNER_FINANCE_C2N(SequenceTypeEnum.NORMAL, "DN", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-通知预约
     */
    CHO_INNER_FINANCE_N2B(SequenceTypeEnum.NORMAL, "DA", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-取消通知预约
     */
    CHO_INNER_FINANCE_B2N(SequenceTypeEnum.NORMAL, "DC", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 内部理财表-通知转活期
     */
    CHO_INNER_FINANCE_N2C(SequenceTypeEnum.NORMAL, "DQ", 4, "CHO_INNER_FINANCE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /*** 利息清单电子回单*/
    E_ACCRUAL_BILL_NO_I(SequenceTypeEnum.NORMAL, "I", 6, "e_accrual_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 贷款电子回单*/
    E_LOAN_BILL_NO_L(SequenceTypeEnum.NORMAL, "L", 6, "e_loan_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 通知单电子回单*/
    E_NOTICE_BILL_NO_N(SequenceTypeEnum.NORMAL, "N", 6, "e_notice_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 账户转换电子回单*/
    E_TRANS_BILL_NO_T(SequenceTypeEnum.NORMAL, "T", 6, "e_trans_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 手续费电子回单*/
    E_POUNDAGE_BILL_NO_P(SequenceTypeEnum.NORMAL, "P", 6, "e_poundage_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 委托收款电子回单*/
    E_CONSIGN_BILL_NO_C(SequenceTypeEnum.NORMAL, "C", 6, "e_consign_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 对账单电子回单*/
    E_COMPARE_BILL_NO_D(SequenceTypeEnum.NORMAL, "D", 6, "e_compare_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 承兑汇票电子回单*/
    E_ACCEPT_BILL_NO_B(SequenceTypeEnum.NORMAL, "B", 6, "e_accept_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 内部结售汇电子回单*/
    E_EXCHANGE_BILL_NO_E(SequenceTypeEnum.NORMAL, "E", 6, "e_exchange_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /*** 委托贷款电子回单表*/
    E_PRECATIVE_LOAN_BILL_NO_R(SequenceTypeEnum.NORMAL, "R", 6, "e_precative_loan_bill", "bill_no", SequenceDateEnum.yyMMdd),
    /**
     * 银行理财指令编码
     */
    BIS_FINANCE_EXC(SequenceTypeEnum.NORMAL, 6, "BIS_FINANCE_EXC", "VOUCHER_NO", SequenceDateEnum.yyMMdd),
    /**
     * 理财登记流水号
     */
    CHO_PRODUCT_REQ(SequenceTypeEnum.NORMAL, 6, "CHO_PRODUCT_REQ", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 理财赎回流水号
     */
    CHO_PRODUCT_REDEEM(SequenceTypeEnum.NORMAL, 6, "CHO_PRODUCT_REDEEM", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 资金备案台账流水号
     */
    NIS_CAPITAL_RECORD(SequenceTypeEnum.NORMAL, 6, "NIS_CAPITAL_RECORD", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 工作流转授配置序列号
     */
    FLOW_DELEGATION_NO(SequenceTypeEnum.NORMAL, 6, "ACT_EXTEND_DELEGATION", "DELEGATION_NO", SequenceDateEnum.yyMMdd),
    /**
     * 营收稽核稽自动稽核稽核码
     */
    RAS_AUDIT_NOZD(SequenceTypeEnum.NORMAL, "ZD", 8, "RAS_AUDIT_INFO", "AUDIT_NO", SequenceDateEnum.yyMMdd),
    /**
     * 营收稽核稽手动稽核稽核码
     */
    RAS_AUDIT_NOJG(SequenceTypeEnum.NORMAL, "JG", 8, "RAS_AUDIT_INFO", "AUDIT_NO", SequenceDateEnum.yyMMdd),
    /**
     * 营收稽核稽差异稽核稽核码
     */
    RAS_AUDIT_NOCG(SequenceTypeEnum.NORMAL, "CG", 8, "RAS_AUDIT_INFO", "AUDIT_NO", SequenceDateEnum.yyMMdd),
    /**
     * 收款流水号
     */
    NIS_RECEIVABLE(SequenceTypeEnum.NORMAL, "R", 6, "NIS_RECEIVABLE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 汇款单号
     */
    FEX_FOREIGN_REQ(SequenceTypeEnum.NORMAL, "F", 6, "FEX_FOREIGN_REQ", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 交易需求号
     */
    FEX_TRAN_APP(SequenceTypeEnum.NORMAL, "R", 4, "FEX_TRAN_APP", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 交易号码
     */
    FEX_TRAN_REQ(SequenceTypeEnum.NORMAL, "D", 4, "FEX_TRAN_REQ", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 现金流号
     */
    FEX_CASH_FLOW(SequenceTypeEnum.NORMAL, "CF", 6, "FEX_CASH_FLOW", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 净额结算号
     */
    FEX_CASH_FLOW_SETTLENO(SequenceTypeEnum.NORMAL, "N", 3, "FEX_CASH_FLOW", "SETTLE_NO"),
    /**
     * 交易组号码
     */
    FEX_TRAN_GROUP(SequenceTypeEnum.NORMAL, "DS", 3, "FEX_TRAN_GROUP", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 外币汇款指令编码
     */
    BIS_FOREIGN_EXC(SequenceTypeEnum.NORMAL, "F", 5, "BIS_FOREIGN_EXC", "VOUCHER_NO", SequenceDateEnum.yyMMdd),
    /**
     * 敞口模板代码(外汇敞口管理)
     */
    FEX_EXPOSURE_TEMPLATE(SequenceTypeEnum.NORMAL, "", 5, "FEX_EXPOSURE_TEMPLATE", "TEMPLATE_CODE", SequenceDateEnum.yyMMdd),
    /**
     * 生命周期号码
     */
    FEX_TRAN_LIFE_CYCLE(SequenceTypeEnum.NORMAL, "LC", 4, "FEX_TRAN_LIFE_CYCLE", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 交易配置模板编号
     */
    FEX_TRAN_TEMPLATE_CONFIG_NO(SequenceTypeEnum.NORMAL, "T", 6, "fex_tran_template_config", "template_no", SequenceDateEnum.yyMMdd),
    /**
     * 交易配置模板版本号
     */
    FEX_TEMPLATE_CONFIG_VERSION_NO(SequenceTypeEnum.NORMAL, "V", 4, "FEX_TRAN_REQ_CONFIG_VERSION", "version_no", SequenceDateEnum.yyMMdd),

    /**
     * 第三方支付退款单号
     */
    TTPAY_REFUND_NO(SequenceTypeEnum.NORMAL, 6, "TP_REFUND_ORDER", "BACK_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 票据池融资申请单号
     */
    BILL_POOL_EMITFINA_APPNO(SequenceTypeEnum.NORMAL, "PF", 6, "AB_POOL_DTL", "APP_NO", SequenceDateEnum.yyMMdd),
    /**
     * 票据池出票申请单号
     */
    BILL_POOL_EMIT_APPNO(SequenceTypeEnum.NORMAL, "PE", 6, "AB_POOL_DTL", "APP_NO", SequenceDateEnum.yyMMdd),
    /**
     * 票据池出入池申请单号
     */
    BILL_POOL_INTO_APPNO(SequenceTypeEnum.NORMAL, "AP", 6, "AB_POOL_DTL", "APP_NO", SequenceDateEnum.yyMMdd),
    /**
     * 电票自动签收策略维护编码
     */
    BILL_AUTO_SIGN_CODE(SequenceTypeEnum.NORMAL, "AT", 2, "AB_BILL_SIGN_STRATEGY", "CODE", SequenceDateEnum.yyMM),
    /**
     * 协定存款申请编码
     */
    FC_AGREED_DEPOSIT(SequenceTypeEnum.NORMAL, "F", 6, "FC_AGREED_DEPOSIT", "BILL_NO", SequenceDateEnum.yyMMdd),
    /**
     * 企业通知存款个别计息-通知预约CRM_MODEL_BATCH_NUMBER
     */
    FC_INTEREST_SINGLE_NOD_BOOK(SequenceTypeEnum.NORMAL, "DA", 4, "FC_INTEREST_SINGLE_NOD_BOOK", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 资金需求编号A
     */
    NIS_DEMAND_A(SequenceTypeEnum.NORMAL, "RA", 4, "NIS_DEMAND", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 资金需求编号B
     */
    NIS_DEMAND_B(SequenceTypeEnum.NORMAL, "RB", 4, "NIS_DEMAND", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    /**
     * 资金需求编号C
     */
    NIS_DEMAND_C(SequenceTypeEnum.NORMAL, "RC", 4, "NIS_DEMAND", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    NIS_DEMAND_D(SequenceTypeEnum.NORMAL, "RD", 4, "NIS_DEMAND", "BILL_NO", SequenceDateEnum.yyyyMMdd),
    NIS_DEMAND_E(SequenceTypeEnum.NORMAL, "RE", 4, "NIS_DEMAND", "BILL_NO", SequenceDateEnum.yyyyMMdd),

    CMS_REPAY_CODE(SequenceTypeEnum.NORMAL, "", 3, "CMS_REPAY_TYPE", "REPAY_CODE", SequenceDateEnum.yyyyMMdd),

    /**
     * 生成批次号
     */
    EVALUATION_BATCH(SequenceTypeEnum.NORMAL, "E", 4, "EVALUATION_BATCH", "EVALUATION_BATCH_NUMBER", SequenceDateEnum.yyMMdd),

    /**
     * 评价任务-任务编号
     */
    CR_TASK_NO(SequenceTypeEnum.NORMAL, "T", 4, "CR_TASK_INFO", "TASK_NO", SequenceDateEnum.yyMMdd),

    /**
     * 信用评级批次号
     */
    CRM_MODEL_BATCH_NUMBER(SequenceTypeEnum.SPECIAL_PREFIX, 4, "MODEL_BATCH", "MODEL_BATCH_NUMBER", SequenceDateEnum.yyyyMMdd),

    CREDIT_SCORING_INDICATOR_CODE(SequenceTypeEnum.NORMAL, "", 4, "CR_SCORING_INDICATOR", "SCORING_INDICATOR_CODE"),

    /**
     * 机构编号
     */
    INSTITUTION_CODE(SequenceTypeEnum.NORMAL, "IN", 3, "INSTITUTIONAL_RAW_DATA", "INSTITUTION_CODE", SequenceDateEnum.yyyyMMdd),

    /**
     * 计算模型编号，仅用于 COMPUTATIONAL_MODEL.MODEL_CODE 自身；其他业务表编号请勿复用本枚举
     */
    CRM_COMPUTATION_MODEL_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 3, "COMPUTATIONAL_MODEL", "MODEL_CODE"),

    /**
     * 评级标准编号
     */
    SCORING_GRADE_CODE(SequenceTypeEnum.SPECIAL_PREFIX, 3, "SCORING_GRADE", "SCORING_GRADE_CODE"),

    /**
     * 单位归集配置编号
     */
    BIS_CORP_POOL_CONFIG_NO(SequenceTypeEnum.NORMAL, 3, "BIS_CORP_POOL_CONFIG", "CONFIG_CODE", SequenceDateEnum.yyyyMMdd),


    /**
     * 应收款管理-生成单据编号
     */
    ACCOUNT_RECEIVABLE_DOC_MANAGE(SequenceTypeEnum.NORMAL, "DOC", 6, "DOC_MANAGE", "CODE", null,true),

    /**
     * 逾期账款预警设置/预警策略-策略代码
     */
    YJ_BTWARNSTRATEGY_CODE(SequenceTypeEnum.NORMAL, "CL", 6, "BT_WARN_STRATEGY", "CODE"),

    /**
     * 应收款管理-生成核销批次号
     */
    ACCOUNT_RECEIVABLE_WRITE_OFF(SequenceTypeEnum.NORMAL, "WO", 6, "DOC_WIRTE_OFF_RELATION", "BATCH_NO", SequenceDateEnum.yyyyMMdd,true),
    /**
     * 供应链金融-应付债券凭证单据编号
     */
    SCF_PAYABLE_VOUCHER_NO(SequenceTypeEnum.NORMAL,"AP",3,"SCF_PAYABLE_VOUCHER","VOUCHER_NO",SequenceDateEnum.yyyyMMdd),
    /**
     * 供应链金融-应付债券凭证兑付单据编号
     */
    SCF_PAYABLE_VOUCHER_REDEEM_NO(SequenceTypeEnum.NORMAL,"DF",3,"SCF_VOUCHER_REDEMPTION","CASH_NO",SequenceDateEnum.yyyyMMdd),

    /**
     * 供应链金融-应付债券凭证指令编号
     */
    SCF_PAYABLE_INSTRUCTION_NO(SequenceTypeEnum.NORMAL,"PI",3,"SCF_INSTRUCTION","INSTR_CODE",SequenceDateEnum.yyyyMMdd),

    /**
     * 供应链金融-应收债券凭证单据编号
     */
    SCF_RECEIVABLE_VOUCHER_NO(SequenceTypeEnum.NORMAL,"AR",3,"SCF_RECEIVABLE_VOUCHER_INFO","VOUCHER_NO",SequenceDateEnum.yyyyMMdd),

    /**
     * 供应链金融-融资编号
     */
    SCF_RZ_LOAN_NO(SequenceTypeEnum.NORMAL,"RZ",3,"SCF_LOAN_INFO","LOAN_NO",SequenceDateEnum.yyyyMMdd),

    /**
     * 供应链金融-转让
     */
    SCF_ZR_TRANSFER_NO(SequenceTypeEnum.NORMAL,"ZR",3,"SCF_RECEIVABLE_TRANSFER_INFO","TRANSFER_NO",SequenceDateEnum.yyyyMMdd),

    /**
     * 供应链金融-转让批次号
     */
    SCF_ZR_TRANSFER_BATCH_NO(SequenceTypeEnum.NORMAL,"PC",3,"SCF_RECEIVABLE_TRANSFER_INFO","TRANSFER_BATCH_NO",SequenceDateEnum.yyyyMMdd),
    /**
     * 供应链金融-应收债券凭证-收款单号
     */
    SCF_RECEIVABLE_PAYMENT_INFO(SequenceTypeEnum.NORMAL,"SK",3,"SCF_RECEIVABLE_PAYMENT_INFO","PAYMENT_NO",SequenceDateEnum.yyyyMMdd);




    /**
     * 流水号类型
     */
    private SequenceTypeEnum sequenceTypeEnum;
    /**
     * 流水号前缀
     */
    private String prefix;
    /**
     * 流水号数字部分长度
     */
    private Integer sequenceLength;
    /**
     * 业务表名
     */
    private String tableName;
    /**
     * 业务表中的字段名
     */
    private String fieldName;
    /**
     * 时间格式
     */
    private SequenceDateEnum dateEnum;
    /**
     * 需要根据多个数据库表查询最大值
     */
    private MultiTable[] multiTables;
    /**
     * 是否为连续流水号（为了保证连续流水号，程序将会启动内存事务锁，效率会有所下降）
     */
    private boolean continueSequence;
    /**
     * @param sequenceTypeEnum 生成流水号策略类型
     * @param sequenceLength   流水号自增长长度
     * @param tableName        表名
     * @param fieldName        字段名
     */
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, Integer sequenceLength, String tableName, String fieldName) {
        this(sequenceTypeEnum, "", sequenceLength, tableName, fieldName);
    }
    /**
     * @param sequenceTypeEnum 生成流水号策略类型
     * @param prefix           流水号前缀
     * @param sequenceLength   流水号自增长长度
     * @param tableName        表名
     * @param fieldName        字段名
     */
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, String prefix, Integer sequenceLength, String tableName, String fieldName) {
        this.sequenceTypeEnum = sequenceTypeEnum;
        this.prefix = prefix;
        this.sequenceLength = sequenceLength;
        this.tableName = tableName;
        this.fieldName = fieldName;
    }
    /**
     * @param sequenceTypeEnum 生成流水号策略类型
     * @param prefix           流水号前缀
     * @param sequenceLength   流水号自增长长度
     * @param tableName        表名
     * @param fieldName        字段名
     * @param dateEnum         时间类型，SequenceDateEnum可以获得
     */
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, String prefix, Integer sequenceLength, String tableName, String fieldName, SequenceDateEnum dateEnum) {
        this(sequenceTypeEnum, prefix, sequenceLength, tableName, fieldName);
        this.dateEnum = dateEnum;
    }
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, String prefix, Integer sequenceLength, String tableName, String fieldName, SequenceDateEnum dateEnum, boolean isContinueSequence) {
        this(sequenceTypeEnum, prefix, sequenceLength, tableName, fieldName);
        this.dateEnum = dateEnum;
        this.continueSequence = isContinueSequence;
    }
    /**
     * @param sequenceTypeEnum 生成流水号策略类型
     * @param sequenceLength   流水号自增长长度
     * @param tableName        表名
     * @param fieldName        字段名
     * @param dateEnum         时间类型，SequenceDateEnum可以获得
     */
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, Integer sequenceLength, String tableName, String fieldName, SequenceDateEnum dateEnum) {
        this(sequenceTypeEnum, sequenceLength, tableName, fieldName);
        this.dateEnum = dateEnum;
    }
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, Integer sequenceLength, SequenceDateEnum dateEnum, MultiTable... multiTables) {
        this.sequenceTypeEnum = sequenceTypeEnum;
        this.sequenceLength = sequenceLength;
        this.multiTables = multiTables;
        this.dateEnum = dateEnum;
    }
    SequenceEnum(SequenceTypeEnum sequenceTypeEnum, Integer sequenceLength, SequenceDateEnum dateEnum, boolean isContinueSequence, MultiTable... multiTables) {
        this.sequenceTypeEnum = sequenceTypeEnum;
        this.sequenceLength = sequenceLength;
        this.multiTables = multiTables;
        this.dateEnum = dateEnum;
        this.continueSequence = isContinueSequence;
    }
    public SequenceTypeEnum getSequenceTypeEnum() {
        return sequenceTypeEnum;
    }
    public String getPrefix() {
        return StringUtils.isBlank(prefix) ? "" : prefix;
    }
    public Integer getSequenceLength() {
        return sequenceLength;
    }
    public String getTableName() {
        return tableName;
    }
    public String getFieldName() {
        return fieldName;
    }
    public SequenceDateEnum getDateEnum() {
        return dateEnum;
    }
    public void setDateEnum(SequenceDateEnum dateEnum) {
        this.dateEnum = dateEnum;
    }
    public MultiTable[] getMultiTables() {
        return multiTables;
    }
    public void setMultiTables(MultiTable[] multiTables) {
        this.multiTables = multiTables;
    }
    public boolean isContinueSequence() {
        return continueSequence;
    }
}
