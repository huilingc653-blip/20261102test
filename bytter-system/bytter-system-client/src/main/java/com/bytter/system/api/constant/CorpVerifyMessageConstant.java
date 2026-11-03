package com.bytter.system.api.constant;
/**
 * @author zhengg
 * @version 1.0
 * @Title: CorpVerifyMessageConstant
 * @Description: 错误信息国际化异常常量定义
 * @date 2020-05-19 17:53:54
 */
public class CorpVerifyMessageConstant {
    /**
     * sys.user.validuser.error 单位存在状态是正常的用户，不允许删除!
     */
    public static String SYS_USER_VALIDUSER_ERROR = "sys.user.validuser.error";
    /**
     * sys.user.validuser.error 单位存在状态是正常的用户，不允许禁用!
     */
    public static String SYS_USER_VALIDUSER_DISABLE_ERROR = "sys.user.validuser.disable.error";
    /**
     * sys.user.validuserauth.error 单位，在特殊权限中已经分配给用户使用，不允许删除!
     */
    public static String SYS_USER_VALIDUSERAUTH_ERROR = "sys.user.validuserauth.error";
    /**
     * accebill.valid.del.billemit.error 单位，在应付票据开票登记中已经被使用，不允许删除!
     */
    public static String ACCEBILL_VALID_DEL_BILLEMIT_ERROR = "accebill.valid.del.billemit.error";
    /**
     * accebill.valid.del.billreceive.error 单位，在应收票据登记中已经被使用，不允许删除!
     */
    public static String ACCEBILL_VALID_DEL_BILLRECEIVE_ERROR = "accebill.valid.del.billreceive.error";
    /**
     * accebill.valid.del.billelectric.error 单位，有票据签收信息，不允许删除!
     */
    public static String ACCEBILL_VALID_DEL_BILLELECTRIC_ERROR = "accebill.valid.del.billelectric.error";
    /**
     * accebill.valid.disable.billemit.error 单位有未结清的应付票据，不允许禁用！
     */
    public static String ACCEBILL_VALID_DISABLE_BILLEMIT_ERROR = "accebill.valid.disable.billemit.error";
    /**
     * accebill.valid.disable.billreceive.error 单位有库存中的应收票据，不允许禁用！
     */
    public static String ACCEBILL_VALID_DISABLE_BILLRECEIVE_ERROR = "accebill.valid.disable.billreceive.error";
    /**
     * accebill.approve.disable.billreceive.error 单位存在暂存、待审批、审批打回的应收票据，不允许禁用！
     */
    public static String ACCEBILL_APPROVE_DISABLE_BILLRECEIVE_ERROR = "accebill.approve.disable.billreceive.error";
    /**
     * accebill.approve.disable.billreceive.error 单位存在暂存、待审批、审批打回的应付票据，不允许禁用！
     */
    public static String ACCEBILL_APPROVE_DISABLE_BILLEMIT_ERROR = "accebill.approve.disable.billemit.error";
    /**
     * accebill.valid.disable.billelectric.error 单位，有票据签收信息，不允许禁用!
     */
    public static String ACCEBILL_VALID_DISABLE_BILLELECTRIC_DISB_ERROR = "accebill.valid.disable.billelectric.error";
    /**
     * accebill.valid.disable.billemit.notover.error 单位票据存在记账未完成的数据，不允许禁用！
     */
    public static String ACCEBILL_VALID_DISABLE_BILLEMIT_NOTOVER_ERROR = "accebill.valid.disable.billemit.notover.error";
    /**
     * accebill.valid.disable.bill.electric.error 单位有电票业务指令未结束，不允许禁用！
     */
    public static String ACCEBILL_VALID_DISABLE_BILL_ELECTRIC_ERROR = "accebill.valid.disable.bill.electric.error";
    /**
     * accebill.valid.disable.bill.discount.error 单位有贴现在途的票据，不允许禁用！
     */
    public static String ACCEBILL_VALID_DISABLE_BILL_DISCOUNT_ERROR = "accebill.valid.disable.bill.discount.error";
    /**
     * cashvoucher.valid.del.bvinput.error 单位有入库登记的空白凭证，不允许删除！
     */
    public static String CASHVOUCHER_VALID_BVINPUT_ERROR = "cashvoucher.valid.del.bvinput.error";
    /**
     * cashvoucher.valid.del.bvpayeecheque.error 单位有登记的支票，不允许删除！
     */
    public static String CASHVOUCHER_VALID_BVPAYEECHEQUE_ERROR = "cashvoucher.valid.del.bvpayeecheque.error";
    /**
     * billpool.valid.del.poollimit_error 单位有额度申领申请单，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLLIMIT_ERROR = "billpool.valid.del.poollimit.error";
    /**
     * billpool.valid.del.poollimit_error 单位有额度分配单，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLLIMITALLO_ERROR = "billpool.valid.del.poollimitallo.error";
    /**
     * billpool.valid.del.poolbail.error 单位有保证金申请单，不允许删除
     */
    public static String BILLPOOL_VALID_POOLBAIL_ERROR = "billpool.valid.del.poolbail.error";
    /**
     * billpool.valid.del.poolmanage_error 单位有入池申请单，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLMANAGE_ERROR = "billpool.valid.del.poolmanage.error";
    /**
     * billpool.valid.del.poolsign.error 单位有签收信息，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLASIGN_ERROR = "billpool.valid.del.poolsign.error";
    /**
     * billpool.valid.del.poolaccebill.error 单位有出票申请信息，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLACCEBILL_ERROR = "billpool.valid.del.poolaccebill.error";
    /**
     * billpool.valid.del.poolemitfina.error 单位有出票融资信息，不允许删除！
     */
    public static String BILLPOOL_VALID_POOLEMITFINA_ERROR = "billpool.valid.del.poolemitfina.error";
    /**
     * fundWarn.valid.del.warn.error 单位有预警策略信息，不允许删除！
     */
    public static String FUNDWARN_VALID_WARN_ERROR = "fundWarn.valid.del.warn.error";
    /**
     * fundWarn.valid.del.rasRevenue.error 单位有营收信息，不允许删除！
     */
    public static String RAS_VALID_RASREVENUE_ERROR = "ras.valid.del.revenue.error";
    /**
     * fundWarn.valid.del.rasReceived.error 单位有实收信息，不允许删除！
     */
    public static String RAS_VALID_RASRECEIVED_ERROR = "ras.valid.del.received.error";
    /**
     * con.valid.del.contract.error 单位有关联合同信息，不允许删除！
     */
    public static String CON_VALID_CONTRACT_ERROR = "con.valid.del.contract.error";
    /**
     * billpool.valid.disable.poollimit.error 单位有额度申领申请单，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLLIMIT_ERROR = "billpool.valid.disable.poollimit.error";
    /**
     * billpool.valid.disable.poollimit.check.error 单位有额度申请复核数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLLIMIT_CHECK_ERROR = "billpool.valid.disable.poollimit.check.error";
    /**
     * billpool.valid.disable.poolallot.error 单位有额度分配数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLALLOT_ERROR = "billpool.valid.disable.poolallot.error";
    /**
     * billpool.valid.disable.poolallot.check.error 单位有额度申请复核数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLALLOT_CHECK_ERROR = "billpool.valid.disable.poolallot.check.error";
    /**
     * billpool.valid.disable.poolbail.error 单位有保证金申请单，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLBAIL_ERROR = "billpool.valid.disable.poolbail.error";
    /**
     * billpool.valid.disable.poolmanage.error 单位有入池申请单，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLMANAGE_ERROR = "billpool.valid.disable.poolmanage.error";
    /**
     * billpool.valid.disable.pool.out.error 单位有出池申请单，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOL_OUT_ERROR = "billpool.valid.disable.pool.out.error";
    /**
     * billpool.valid.disable.pool.pdtst.error 单位有质押托管转换数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOL_PDTST_ERROR = "billpool.valid.disable.pool.pdtst.error";
    /**
     * billpool.valid.disable.poolsign.error 单位有电票签收中的数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLASIGN_ERROR = "billpool.valid.disable.poolsign.error";
    /**
     * billpool.valid.disable.poolaccebill.error 单位有出票申请信息，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLACCEBILL_ERROR = "billpool.valid.disable.poolaccebill.error";
    /**
     * billpool.valid.disable.poolbillbrief.error 单位有贴现申请中的数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLBILLBRIEF_ERROR = "billpool.valid.disable.poolbillbrief.error";
    /**
     * billpool.valid.disable.poolbill.brief010.error 单位有背书申请中的数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLBILL_BRIEF010_ERROR = "billpool.valid.disable.poolbill.brief010.error";
    /**
     * billpool.valid.disable.poolbill.brief020.error 单位有提示付款中的数据，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLBILL_BRIEF020_ERROR = "billpool.valid.disable.poolbill.brief020.error";
    /**
     * billpool.valid.disable.poolemitfina.error 单位有出票融资信息，不允许禁用！
     */
    public static String BILLPOOL_VALID_DISABLE_POOLEMITFINA_ERROR = "billpool.valid.disable.poolemitfina.error";
    /**
     * con.valid.disable.contract.error 单位有关联合同信息，不允许禁用！
     */
    public static String CON_VALID_DISABLE_CONTRACT_ERROR = "con.valid.disable.contract.error";
    /**
     * ttpay.valid.disable.refund.error 单位有退款申请信息，不允许禁用！
     */
    public static String TTPAY_VALID_DISABLE_REFUND_ERROR = "ttpay.valid.disable.refund.error";
    /**
     * cms.valid.del.df.error 单位存在直接融资申请单，不允许删除！
     */
    public static String CMS_VALID_DEL_DF_ERROR = "cms.valid.del.df.error";
    /**
     * cms.valid.disable.df.error 单位存在直接融资申请单，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_DF_ERROR = "cms.valid.disable.df.error";
    /**
     * cms.valid.del.loan.info.error 单位存在内部贷款申请单，不允许删除！
     */
    public static String CMS_VALID_DEL_LOAN_INFO_ERROR = "cms.valid.del.loan.info.error";
    /**
     * cms.valid.disable.loan.info 单位存在暂存、待审批、审批打回的内部贷款单据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO = "cms.valid.disable.loan.info";
    /**
     * cms.valid.disable.loan.info1 单位存在补录、待发放、待生成合同的内部贷款单据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO1 = "cms.valid.disable.loan.info1";
    /**
     * cms.valid.disable.loan.info.error1 单位有内部贷款利息待确认的记录，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO_ERROR1 = "cms.valid.disable.loan.info.error1";
    /**
     * cms.valid.disable.loan.info.error2 单位有展期申请待处理的记录，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO_ERROR2 = "cms.valid.disable.loan.info.error2";
    /**
     * cms.valid.disable.loan.info.error3 单位有内部贷款存在未全部还清的贷款，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO_ERROR3 = "cms.valid.disable.loan.info.error3";
    /**
     * cms.valid.disable.loan.info.error4 单位有内部贷款申请待补录、待生成合同的记录，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_LOAN_INFO_ERROR4 = "cms.valid.disable.loan.info.error4";
    /**
     * cms.valid.disable.precatice.info 单位存在暂存、待审批、审批打回的委托贷款单据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATICE_INFO = "cms.valid.disable.precatice.info";
    /**
     * cms.valid.disable.precatice.loanopp.info 单位存在待审批的委托贷款单据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATICE_LOANOPP_INFO = "cms.valid.disable.precatice.loanopp.info";
    /**
     * cms.valid.del.precative.loan.info.error 单位存在委托贷款申请单，不允许删除！
     */
    public static String CMS_VALID_DEL_PRECATIVE_LOAN_INFO_ERROR = "cms.valid.del.precative.loan.info.error";
    /**
     * cms.valid.disable.precative.loan.info.error1 单位有委托贷款存在未全部还款的贷款，委托单位不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR1 = "cms.valid.disable.precative.loan.info.error1";
    /**
     * cms.valid.disable.precative.loan.info.error2 单位有委托贷款贷款还款待补录、还款利息待确认的数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR2 = "cms.valid.disable.precative.loan.info.error2";
    /**
     * cms.valid.disable.precative.loan.info.error3 单位有委托贷款展期待补录、待处理的数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR3 = "cms.valid.disable.precative.loan.info.error3";
    /**
     * cms.valid.disable.precative.loan.info.error4 单位有委托贷款存在未全部还款的贷款，贷款单位不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR4 = "cms.valid.disable.precative.loan.info.error4";
    /**
     * cms.valid.disable.precative.info1 单位存在补录、待发放的委托贷款单据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PRECATIVE_INFO1 = "cms.valid.disable.precative.info1";
    /**
     * international.valid.del.lc1.error 有国际结算开证登记的记录，不允许删除！
     */
    public static String INTERNATIONAL_VALID_DEL_LC1_ERROR = "international.valid.del.lc1.error";
    /**
     * international.valid.del.lc2.error 有国际结算收证登记的记录，不允许删除！
     */
    public static String INTERNATIONAL_VALID_DEL_LC2_ERROR = "international.valid.del.lc2.error";
    /**
     * international.valid.disable.lc1.error 单位有开出信用证没有全部付汇或还押汇，不允许禁用！
     */
    public static String INTERNATIONAL_VALID_DISBALE_LC1_ERROR = "international.valid.disable.lc1.error";
    /**
     * international.valid.disable.lc2.error 单位有收到信用证，没有全部到期限收款，不允许禁用！
     */
    public static String INTERNATIONAL_VALID_DISABLE_LC2_ERROR = "international.valid.disable.lc2.error";
    /**
     * international.valid.disable.lc3.error 单位有开出信用证在途数据，不允许禁用！
     */
    public static String INTERNATIONAL_VALID_DISABLE_LC3_ERROR = "international.valid.disable.lc3.error";
    /**
     * international.valid.disable.lc4.error 单位有收到信用证在途数据，不允许禁用！
     */
    public static String INTERNATIONAL_VALID_DISABLE_LC4_ERROR = "international.valid.disable.lc4.error";
    /**
     * nis.valid.del.inneracc.error 单位已经开设内部账户，不允许删除！
     */
    public static String NIS_VALID_DEL_INNERACC_ERROR = "nis.valid.del.inneracc.error";
    /**
     * nis.valid.del.bankacc.error 单位已经维护银行账户，不允许删除！
     */
    public static String NIS_VALID_DEL_BANKACC_ERROR = "nis.valid.del.bankacc.error";
    /**
     * nis.valid.del.payway.error 单位有分配付款方式，不允许删除！
     */
    public static String NIS_VALID_DEL_PAYWAY_ERROR = "nis.valid.del.payway.error";
    /**
     * nis.valid.del.bustype.error 单位有设置业务种类，不允许删除！
     */
    public static String NIS_VALID_DEL_BUSTYPE_ERROR = "nis.valid.del.bustype.error";
    /**
     * nis.valid.del.personnel.error 单位有维护职工信息，不允许删除！
     */
    public static String NIS_VALID_DEL_PERSONNEL_ERROR = "nis.valid.del.personnel.error";
    /**
     * nis.valid.del.pool.error 单位是资金池主体单位或子单位，不允许删除！
     */
    public static String NIS_VALID_DEL_POOL_ERROR = "nis.valid.del.pool.error";
    /**
     * fbs.valid.del.collectflag.error 单位设置为资金计划汇总单位，不允许删除！
     */
    public static String FBS_VALID_DEL_COLLECTFLAG_ERROR = "fbs.valid.del.collectflag.error";
    /**
     * fbs.valid.del.controlbudget.error 单位设置为资金计划上报或控制单位，不允许删除！
     */
    public static String FBS_VALID_DEL_CONTROLBUDGET_ERROR = "fbs.valid.del.controlbudget.error";
    /**
     * fbs.valid.del.item.error 单位有分配资金计划科目，不允许删除！
     */
    public static String FBS_VALID_DEL_ITEM_ERROR = "fbs.valid.del.item.error";
    /**
     * fbs.valid.del.input.error 单位有上报资金计划，不允许删除！
     */
    public static String FBS_VALID_DEL_INPUT_ERROR = "fbs.valid.del.input.error";
    /**
     * cho.valid.del.productreq.error 单位有理财登记的记录，不允许删除！
     */
    public static String CHO_VALID_DEL_PRODUCTREQ_ERROR = "cho.valid.del.productreq.error";
    /**
     * fex.valid.del.tranapp.error 单位有交易需求单据，不允许删除！
     */
    public static String FEX_VALID_DEL_TRANAPP_ERROR = "fex.valid.del.tranapp.error";
    /**
     * fex.valid.del.tranforeign.error 单位有外汇买卖交易单据，不允许删除！
     */
    public static String FEX_VALID_DEL_TRANFOREIGN_ERROR = "fex.valid.del.tranforeign.error";
    /**
     * fex.valid.del.tranoption.error 单位有外汇期权交易单据，不允许删除！
     */
    public static String FEX_VALID_DEL_TRANOPTION_ERROR = "fex.valid.del.tranoption.error";
    /**
     * fex.valid.del.tranallocation.error 单位有资金调拨交易单据，不允许删除！
     */
    public static String FEX_VALID_DEL_TRANALLOCATION_ERROR = "fex.valid.del.tranallocation.error";
    /**
     * fex.valid.del.tranpay.error 单位有资金收付交易单据，不允许删除！
     */
    public static String FEX_VALID_DEL_TRANPAY_ERROR = "fex.valid.del.tranpay.error";
    /**
     * nis.valid.disable.inneracc.error 单位存在有效的内部账户，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_INNERACC_ERROR = "nis.valid.disable.inneracc.error";
    /**
     * nis.valid.disable.btbankacc.error 单位存在有效的银行账户，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BTBANKACC_ERROR = "nis.valid.disable.btbankacc.error";
    /**
     * nis.valid.disable.expense.error 单位有在途的费用报销单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_EXPENSE_ERROR = "nis.valid.disable.expense.error";
    /**
     * nis.valid.disable.billhead.error 单位有在途的付款单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BILLHEAD_ERROR = "nis.valid.disable.billhead.error";
    /**
     * nis.valid.disable.receivable.error 单位有在途的收款单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_RECEIVABLE_ERROR = "nis.valid.disable.receivable.error";
    /**
     * nis.valid.disable.allocation.up.error 单位有在途的上划单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_UP_ERROR = "nis.valid.disable.allocation.up.error";
    /**
     * nis.valid.disable.allocation.down.error 单位有在途的下拨单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_DOWN_ERROR = "nis.valid.disable.allocation.down.error";
    /**
     * nis.valid.disable.allocation.ask.error 单位有在途的请款单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_ASK_ERROR = "nis.valid.disable.allocation.ask.error";
    /**
     * nis.valid.disable.allocation.turn.error 单位有在途的上缴单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_TURN_ERROR = "nis.valid.disable.allocation.turn.error";
    /**
     * nis.valid.disable.allocation.pos.error 单位有在途的头寸调拨单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_POS_ERROR = "nis.valid.disable.allocation.pos.error";
    /**
     * nis.valid.disable.wage.error 单位有在途的代发工资单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_WAGE_ERROR = "nis.valid.disable.wage.error";
    /**
     * nis.valid.disable.inputexc.error 单位有在途的自营付款单据，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_INPUTEXC_ERROR = "nis.valid.disable.inputexc.error";
    /**
     * nis.valid.disable.exc.error 单位有在途的付款指令，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_EXC_ERROR = "nis.valid.disable.exc.error";
    /**
     * nis.valid.disable.gathercmd.error 单位有在途的收款指令，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_GATHERCMD_ERROR = "nis.valid.disable.gathercmd.error";
    /**
     * nis.valid.disable.financeexc.error 单位有在途的理财指令，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_FINANCEEXC_ERROR = "nis.valid.disable.financeexc.error";
    /**
     * nis.valid.disable.querycmd.error 单位有在途的查询指令，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_QUERYCMD_ERROR = "nis.valid.disable.querycmd.error";
    /**
     * nis.valid.disable.bankacc.error 单位有在办的银行账户信息，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BANKACC_ERROR = "nis.valid.disable.bankacc.error";
    /**
     * nis.valid.disable.bankacc.dtl.error 单位有在办的银行账户流水信息，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BANKACC_DTL_ERROR = "nis.valid.disable.bankacc.dtl.error";
    /**
     * nis.valid.disable.bankacc.bal.error 单位有在办的银行账户余额信息，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BANKACC_BAL_ERROR = "nis.valid.disable.bankacc.bal.error";
    /**
     * cho.valid.disable.productreq.error 单位有在途的理财登记单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_PRODUCTREQ_ERROR = "cho.valid.disable.productreq.error";
    /**
     * cho.valid.disable.productredeem.error 单位有在途的理财赎回单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_PRODUCTREDEEM_ERROR = "cho.valid.disable.productredeem.error";
    /**
     * cho.valid.disable.productreq.redeem.error 单位有未全部赎回的理财登记单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_PRODUCTREQ_REDEEM_ERROR = "cho.valid.disable.productreq.redeem.error";
    /**
     * cho.valid.disable.bankfinance.c2t.error 单位有在途的银行存款（活期转定期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_C2T_ERROR = "cho.valid.disable.bankfinance.c2t.error";
    /**
     * cho.valid.disable.bankfinance.t2c.error 单位有在途的银行存款（定期转活期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_T2C_ERROR = "cho.valid.disable.bankfinance.t2c.error";
    /**
     * cho.valid.disable.bankfinance.c2n.error 单位有在途的银行存款（活期转通知）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_C2N_ERROR = "cho.valid.disable.bankfinance.c2n.error";
    /**
     * cho.valid.disable.bankfinance.n2c.error 单位有在途的银行存款（通知转活期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_N2C_ERROR = "cho.valid.disable.bankfinance.n2c.error";
    /**
     * cho.valid.disable.bankfinance.c2t.bl.error 单位有待补录的银行存款银行存款（活期转定期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_C2T_BL_ERROR = "cho.valid.disable.bankfinance.c2t.bl.error";
    /**
     * cho.valid.disable.bankfinance.c2n.bl.error 单位有待补录的银行存款（活期转通知）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_BANKFINANCE_C2N_BL_ERROR = "cho.valid.disable.bankfinance.c2n.bl.error";
    /**
     * cho.valid.disable.innerfinance.c2t.error 单位有在途的内部存款（活期转定期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_C2T_ERROR = "cho.valid.disable.innerfinance.c2t.error";
    /**
     * cho.valid.disable.innerfinance.t2c.error 单位有在途的内部存款（定期转活期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_T2C_ERROR = "cho.valid.disable.innerfinance.t2c.error";
    /**
     * cho.valid.disable.innerfinance.c2n.error 单位有在途的内部存款（活期转通知）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_C2N_ERROR = "cho.valid.disable.innerfinance.c2n.error";
    /**
     * cho.valid.disable.innerfinance.n2b.error 单位有在途的内部存款（通知预约）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_N2B_ERROR = "cho.valid.disable.innerfinance.n2b.error";
    /**
     * cho.valid.disable.innerfinance.b2n.error 单位有在途的内部存款（取消通知预约）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_B2N_ERROR = "cho.valid.disable.innerfinance.b2n.error";
    /**
     * cho.valid.disable.innerfinance.n2c.error 单位有在途的内部存款（通知转活期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_N2C_ERROR = "cho.valid.disable.innerfinance.n2c.error";
    /**
     * cho.valid.disable.agreeddeposit.error 单位有在途的内部存款（协定存款）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_AGREEDDEPOSIT_ERROR = "cho.valid.disable.agreeddeposit.error";
    /**
     * cho.valid.disable.agreeddeposit.notend.error 单位有未终止的协定存款单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_AGREEDDEPOSIT_NOTEND_ERROR = "cho.valid.disable.agreeddeposit.notend.error";
    /**
     * cho.valid.disable.innerfinance.c2t.draw.error 单位有未完成取款的内部存款（活期转定期）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_C2T_DRAW_ERROR = "cho.valid.disable.innerfinance.c2t.draw.error";
    /**
     * cho.valid.disable.innerfinance.c2n.draw.error 单位有未完成取款的内部存款（活期转通知）单据，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_C2N_DRAW_ERROR = "cho.valid.disable.innerfinance.c2n.draw.error";
    /**
     * fbs.valid.disable.input.error 单位有资金计划上报的在途数据，不允许禁用！
     */
    public static String FBS_VALID_DISABLE_INPUT_ERROR = "fbs.valid.disable.input.error";
    /**
     * fbs.valid.disable.superadd.error 单位有资金计划追加的在途数据，不允许禁用！
     */
    public static String FBS_VALID_DISABLE_SUPERADD_ERROR = "fbs.valid.disable.superadd.error";
    /**
     * fbs.valid.disable.process.error 单位有资金计划执行的在途数据，不允许禁用！
     */
    public static String FBS_VALID_DISABLE_PROCESS_ERROR = "fbs.valid.disable.process.error";
    /**
     * fex.valid.disable.tranapp.error 单位有在途的交易需求单据，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_TRANAPP_ERROR = "fex.valid.disable.tranapp.error";
    /**
     * fex.valid.disable.tranforeign.error 单位有在途的外汇买卖交易单据，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_TRANFOREIGN_ERROR = "fex.valid.disable.tranforeign.error";
    /**
     * fex.valid.disable.tranoption.error 单位有在途的外汇期权交易单据，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_TRANOPTION_ERROR = "fex.valid.disable.tranoption.error";
    /**
     * fex.valid.disable.tranallocation.error 单位有在途的资金调拨交易单据，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_TRANALLOCATION_ERROR = "fex.valid.disable.tranallocation.error";
    /**
     * fex.valid.disable.tranpay.error 单位有在途的资金收付交易单据，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_TRANPAY_ERROR = "fex.valid.disable.tranpay.error";
    /**
     * fex.valid.disable.cashflow.error 单位有在途的现金流，不允许禁用！
     */
    public static String FEX_VALID_DISABLE_CASHFLOW_ERROR = "fex.valid.disable.cashflow.error";
    /**
     * nis.valid.disable.billhead.tally.error 单位有记账中的付款单，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_BILLHEAD_TALLY_ERROR = "nis.valid.disable.billhead.tally.error";
    /**
     * nis.valid.disable.allocation.tally.error 单位有记账中的资金调拨单，不允许禁用！
     */
    public static String NIS_VALID_DISABLE_ALLOCATION_TALLY_ERROR = "nis.valid.disable.allocation.tally.error";
    /**
     * cho.valid.disable.innerfinance.tally.error 单位有记账中的内部存款单，不允许禁用！
     */
    public static String CHO_VALID_DISABLE_INNERFINANCE_TALLY_ERROR = "cho.valid.disable.innerfinance.tally.error";
    /**
     * nis.valid.change.allocation.up.error 单位有在途的上划单据，不允许变更！
     */
    public static String NIS_VALID_CHANGE_ALLOCATION_UP_ERROR = "nis.valid.change.allocation.up.error";
    /**
     * nis.valid.change.allocation.down.error 单位有在途的下拨单据，不允许变更！
     */
    public static String NIS_VALID_CHANGE_ALLOCATION_DOWN_ERROR = "nis.valid.change.allocation.down.error";
    /**
     * nis.valid.change.allocation.ask.error 单位有在途的请款单据，不允许变更！
     */
    public static String NIS_VALID_CHANGE_ALLOCATION_ASK_ERROR = "nis.valid.change.allocation.ask.error";
    /**
     * nis.valid.change.allocation.turn.error 单位有在途的上缴单据，不允许变更！
     */
    public static String NIS_VALID_CHANGE_ALLOCATION_TURN_ERROR = "nis.valid.change.allocation.turn.error";
    /**
     * nis.valid.change.gathercmd.error 单位有在途的资金调拨收款指令，不允许变更！
     */
    public static String NIS_VALID_CHANGE_GATHERCMD_ERROR = "nis.valid.change.gathercmd.error";
    /**
     * nis.valid.change.exc.error 单位有在途的资金调拨付款指令，不允许变更！
     */
    public static String NIS_VALID_CHANGE_EXC_ERROR = "nis.valid.change.exc.error";
    /**
     * fbs.valid.change.input.error 单位有资金计划上报的在途数据，不允许变更！
     */
    public static String FBS_VALID_CHANGE_INPUT_ERROR = "fbs.valid.change.input.error";
    /**
     * fbs.valid.change.superadd.error 单位有资金计划追加的在途数据，不允许变更！
     */
    public static String FBS_VALID_CHANGE_SUPERADD_ERROR = "fbs.valid.change.superadd.error";
    /**
     * fbs.valid.change.process.error 单位有资金计划执行的在途数据，不允许变更！
     */
    public static String FBS_VALID_CHANGE_PROCESS_ERROR = "fbs.valid.change.process.error";
    /**
     * bond.valid.del.error 单位有保证金申请单，不允许删除！
     */
    public static String BOND_VALID_DEL_ERROR = "bond.valid.del.error";
    /**
     * gua.valid.del.contract.error 单位有登记的被担保合同，不允许删除！
     */
    public static String GUA_VALID_DEL_CONTRACT_ERROR = "gua.valid.del.contract.error";
    /**
     * gua.valid.disable.contract.error 单位被担保合同登记的在途数据，不允许禁用！
     */
    public static String GUA_VALID_DISABLE_CONTRACT_ERROR = "gua.valid.disable.contract.error";
    /**
     * gua.valid.del.extContract.error 单位有登记的对外担保合同，不允许删除！
     */
    public static String GUA_VALID_DEL_EXTCONTRACT_ERROR = "gua.valid.del.extContract.error";
    /**
     * gua.valid.disable.extContract.error 单位有对外担保合同登记的在途数据，不允许禁用！
     */
    public static String GUA_VALID_DISABLE_EXTCONTRACT_ERROR = "gua.valid.disable.extContract.error";
    /**
     * gua.valid.disable.extContractEnd.error 单位有对外担保合同终止的在途数据，不允许禁用！
     */
    public static String GUA_VALID_DISABLE_EXTCONTRACTEND_ERROR = "gua.valid.disable.extContractEnd.error";
    /**
     * rat.valid.del.bankRation.error 单位有录入或分配的银行授信，不允许删除！
     */
    public static String RAT_VALID_DEL_BANKRATION_ERROR = "rat.valid.del.bankRation.error";
    /**
     * rat.valid.disable.bankRation.error 单位有银行授信录入的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_ERROR = "rat.valid.disable.bankRation.error";
    /**
     * rat.valid.disable.bankRatOccupy.error 单位银行授信管理存在占用未释放的授信，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATOCCUPY_ERROR = "rat.valid.disable.bankRatOccupy.error";
    /**
     * rat.valid.disable.bankRationDis.error 单位有银行授信分配的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATIONDIS_ERROR = "rat.valid.disable.bankRationDis.error";
    /**
     * rat.valid.disable.bankRation.bankAdd.error 单位有银行授信追加的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_BANKADD_ERROR = "rat.valid.disable.bankRation.bankAdd.error";
    /**
     * rat.valid.disable.bankRation.cpAdd.error 单位有集团授信追加的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_CPADD_ERROR = "rat.valid.disable.bankRation.cpAdd.error";
    /**
     * rat.valid.disable.bankRation.adjust.error 单位有银行授信额度调整的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_ADJUST_ERROR = "rat.valid.disable.bankRation.adjust.error";
    /**
     * rat.valid.disable.bankRation.apply.error 单位有银行授信占用的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_APPLY_ERROR = "rat.valid.disable.bankRation.apply.error";
    /**
     * rat.valid.disable.bankRation.delay.error 单位有银行授信延期的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_DELAY_ERROR = "rat.valid.disable.bankRation.delay.error";
    /**
     * rat.valid.disable.bankRation.relief.error 单位有银行授信调剂的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_BANKRATION_RELIEF_ERROR = "rat.valid.disable.bankRation.relief.error";
    /**
     * rat.valid.del.cpRation.error 单位有录入或分配的内部授信，不允许删除！
     */
    public static String RAT_VALID_DEL_CPRATION_ERROR = "rat.valid.del.cpRation.error";
    /**
     * rat.valid.disable.cpRation.error 单位有内部授信录入的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATION_ERROR = "rat.valid.disable.cpRation.error";
    /**
     * rat.valid.disable.cpRatOccupy.error 单位内部授信管理，存在占用未释放的授信，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATOCCUPY_ERROR = "rat.valid.disable.cpRatOccupy.error";
    /**
     * rat.valid.disable.cpRationDis.error 单位有内部授信分配的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATIONDIS_ERROR = "rat.valid.disable.cpRationDis.error";
    /**
     * rat.valid.disable.cpRationAdd.error 单位有内部授信追加的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATIONADD_ERROR = "rat.valid.disable.cpRationAdd.error";
    /**
     * rat.valid.disable.cpRationApply.error 单位有内部授信占用的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATIONAPPLY_ERROR = "rat.valid.disable.cpRationApply.error";
    /**
     * rat.valid.disable.cpRationRelief.error 单位有内部授信调剂的在途数据，不允许禁用！
     */
    public static String RAT_VALID_DISABLE_CPRATIONRELIEF_ERROR = "rat.valid.disable.cpRationRelief.error";
    /**
     * cms.valid.disable.proLoanApply.error 单位存在银行借款申请单，不允许删除！
     */
    public static String CMS_VALID_DEL_PROLOANAPPLY_ERROR = "cms.valid.disable.proLoanApply.error";
    /**
     * cms.valid.disable.proLoanRepair.error 单位有银行借款申请待补录的申请，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOANREPAIR_ERROR = "cms.valid.disable.proLoanRepair.error";
    /**
     * cms.valid.disable.proLoanRepair.error 单位有银行借款申请的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_ERROR = "cms.valid.disable.proLoan.error";
    /**
     * cms.valid.disable.proLoan.repair.error 单位有银行借款申请补录的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_REPAIR_ERROR = "cms.valid.disable.proLoan.repair.error";
    /**
     * cms.valid.disable.proLoan.repayWaitRepair.error 单位有银行借款还款申请待补录的申请，不允许禁用!
     */
    public static String CMS_VALID_DISABLE_PROLOAN_REPAYWAITREPAIR_ERROR = "cms.valid.disable.proLoan.repayWaitRepair.error";
    /**
     * cms.valid.disable.proLoan.defering.error 单位有银行借款展期待补录 、待处理的申请，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_DEFERING_ERROR = "cms.valid.disable.proLoan.defering.error";
    /**
     * cms.valid.disable.proLoan.repaying.error 单位有银行借款存在未全部还清的贷款，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_REPAYING_ERROR = "cms.valid.disable.proLoan.repaying.error";
    /**
     * cms.valid.disable.proLoan.send.error 单位有银行借款发放的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_SEND_ERROR = "cms.valid.disable.proLoan.send.error";
    /**
     * cms.valid.disable.proLoan.repay.error 单位有银行借款还款申请的在途数据，不允许禁用!
     */
    public static String CMS_VALID_DISABLE_PROLOAN_REPAY_ERROR = "cms.valid.disable.proLoan.repay.error";
    /**
     * cms.valid.disable.proLoan.repayRepair.error 单位有银行借款还款补录的在途数据，不允许禁用!
     */
    public static String CMS_VALID_DISABLE_PROLOAN_REPAYREPAIR_ERROR = "cms.valid.disable.proLoan.repayRepair.error";
    /**
     * cms.valid.disable.proLoan.defer.error 单位有银行借款展期申请的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_DEFER_ERROR = "cms.valid.disable.proLoan.defer.error";
    /**
     * cms.valid.disable.proLoan.deferRepair.error 单位有银行借款展期补录的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_DEFERREPAIR_ERROR = "cms.valid.disable.proLoan.deferRepair.error";
    /**
     * cms.valid.disable.proLoan.abolish.error 单位有银行借款作废的在途数据，不允许禁用！
     */
    public static String CMS_VALID_DISABLE_PROLOAN_ABOLISH_ERROR = "cms.valid.disable.proLoan.abolish.error";
    /**
     * gua.valid.del.guarantee.error 单位存在保函申请单，不允许删除！
     */
    public static String GUA_VALID_DEL_GUARANTEE_ERROR = "gua.valid.del.guarantee.error";
    /**
     * gua.valid.disable.guarantee.error 单位存在保函申请的在途数据，不允许禁用！
     */
    public static String GUA_VALID_DISABLE_GUARANTEE_ERROR = "gua.valid.disable.guarantee.error";
    /**
     * gua.valid.del.pledge.error 单位有登记的抵质押物，不允许删除！
     */
    public static String GUA_VALID_DEL_PLEDGE_ERROR = "gua.valid.del.pledge.error";
    /**
     * gua.valid.disable.pledge.error 单位有抵质押物登记的在途数据，不允许禁用！
     */
    public static String GUA_VALID_DISABLE_PLEDGE_ERROR = "gua.valid.disable.pledge.error";
    /**
     * fin.tally.valid.disable.error 单位存在网络记账待记账数据，不允许禁用！
     */
    public static String FIN_TALLY_VALID_DISABLE_ERROR = "fin.tally.valid.disable.error";
    /**
     * fin.tally.end.valid.disable.error 单位存在网络记账记账未完成数据，不允许禁用！
     */
    public static String FIN_TALLY_END_VALID_DISABLE_ERROR = "fin.tally.end.valid.disable.error";
    /**
     * fin.hand.tally.end.valid.disable.error 单位存在手工记账记账未完成数据，不允许禁用！
     */
    public static String FIN_HAND_TALLY_END_VALID_DISABLE_ERROR = "fin.hand.tally.end.valid.disable.error";
    /**
     * fin.interest.end.valid.disable.error 单位存在批量计息计息未完成数据，不允许禁用！
     */
    public static String FIN_INTEREST_END_VALID_DISABLE_ERROR = "fin.interest.end.valid.disable.error";
}
