package com.bytter.system.constants;
import java.util.HashMap;
import java.util.Map;
/**
 * 电子回单资源常量类
 * @author zhangxb
 * @since 2017/7/24.
 */
public class ResourceConstant {
    /**内部利率维护资源*/
    public static final String NETBANK_INNERACCOUNT_RATE_ADJUST = "netbank:innerAccount:rate:adjust";
    /**职工信息维护应用资源*/
    public static final String NETBANK_NISPERSONNELDETAIL_APPLY = "netbank:nispersonneldetail:apply";
    /**职工信息维护查询资源*/
    public static final String NETBANK_NISPERSONNELDETAIL_QUERY = "netbank:nispersonneldetail:query";
    /*** 内部账户开户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_QUERY = "netbank:innerAccount:fcAcc:query";
    /*** 内部账户开户申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_APPLY = "netbank:innerAccount:fcAcc:apply";
    /*** 资金池内部账户开户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_QUERY = "netbank:innerAccount:fcAcc:pool:query";
    /*** 资金池内部账户开户申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_APPLY = "netbank:innerAccount:fcAcc:pool:apply";
    /*** 内部账户改户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY = "netbank:innerAccount:fcAcc:changeQuery";
    /*** 内部账户改户申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_CHANGEAPPLY = "netbank:innerAccount:fcAcc:changeApply";
    /*** 资金池内部账户改户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY = "netbank:innerAccount:fcAcc:pool:changeQuery";
    /*** 资金池内部账户改户申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEAPPLY = "netbank:innerAccount:fcAcc:pool:changeApply";
    /*** 内部账户注销查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_CANCELQUERY = "netbank:innerAccount:fcAcc:cancelQuery";
    /*** 内部账户注销申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY = "netbank:innerAccount:fcAcc:cancelApply";
    /*** 资金池内部账户注销查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY = "netbank:innerAccount:fcAcc:pool:cancelQuery";
    /*** 资金池内部账户注销申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY = "netbank:innerAccount:fcAcc:pool:cancelApply";
    /*** 内部账户冻结查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCFREEZE_QUERY = "netbank:innerAccount:fcFreeze:query";
    /*** 内部账户冻结申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCFREEZE_APPLY = "netbank:innerAccount:fcFreeze:apply";
    /*** 内部账户解冻查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY = "netbank:innerAccount:fcFreeze:unQuery";
    /*** 内部账户解冻申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCFREEZE_UNAPPLY = "netbank:innerAccount:fcFreeze:unApply";
    /*** 内部账户初始化查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCINITACC_QUERY = "netbank:innerAccount:fcInitAcc:query";
    /*** 内部账户初始化申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCINITACC_APPLY = "netbank:innerAccount:fcInitAcc:apply";
    /*** 内部账户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_ALLQUERY = "netbank:innerAccount:fcAcc:allQuery";
    /*** 内部账户查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY = "netbank:innerAccount:fcAcc:pool:allQuery";
    /*** 内部账户冻结解冻查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCFREEZE_ALLQUERY = "netbank:innerAccount:fcFreeze:allQuery";
    /*** 内部账户余额查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY = "netbank:innerAccount:fcAccBalance:query";
    /*** 资金池内部账户余额查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY = "netbank:innerAccount:fcAccBalance:pool:query";
    /*** 内部账户流水查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACCDETAIL_QUERY = "netbank:innerAccount:fcAccDetail:query";
    /*** 内部账户流水资金计划申请资源*/
    public static final String NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_APPLY = "netbank:innerAccount:fcAccDetailBudgetMatch:apply";
    /*** 内部账户流水资金计划查询资源*/
    public static final String NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY = "netbank:innerAccount:fcAccDetailBudgetMatch:query";
    /**内部账户增减情况表查询权限***/
    public static final String NETBANK_INNERACCOUNT_CHANGING_QUERY = "netbank:inneraccount:changing:query";
    /*** 付款办理查询资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_QUERY = "netbank:netBank:nisBillHead:query";
    /*** 付款办理新增资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_ADD = "netbank:netBank:nisBillHead:add";
    /*** 付款办理修改资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_UPDATE = "netbank:netBank:nisBillHead:update";
    /*** 付款办理删除资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_DELETE = "netbank:netBank:nisBillHead:delete";
    /*** 付款办理合并拆分资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT = "netbank:netBank:nisBillHead:mergeAndSplit";
    /*** 付款办理合并批量提交资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_SUBMITBATCH = "netbank:netBank:nisBillHead:submitBatch";
    /*** 付款办理合并批量选择账号资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_BATCHSETPAYERACC = "netbank:netBank:nisBillHead:batchSetPayerAcc";
    /*** 付款查询资源*/
    public static final String NETBANK_NETBANKQUERY_NISBILLHEAD_QUERY = "netbank:netBankQuery:nisBillHead:query";
    /*** 作废付款单查询资源*/
    public static final String NETBANK_NETBANKQUERYDEL_NISBILLHEAD_QUERY = "netbank:netBankQueryDel:nisBillHead:query";
    /*** 指令查看资源*/
    public static final String NETBANK_NETBANKQUERY_NISBILLHEAD_BISQUERY = "netbank:netBankQuery:nisBillHead:bisQuery";
    /*** 资金备案台账查询资源*/
    public static final String NETBANK_NETBANK_NISCAPITALRECORD_QUERY = "netbank:netBank:nisCapitalRecord:query";
    /**资金备案台账删除资源*/
    public static final String NETBANK_NETBANK_NISCAPITALRECORD_DELETE = "netbank:netBank:nisCapitalRecord:delete";
    /*** 费用报销制单查询资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHQUERY = "netbank:expense:nisExpenseDetail:touchQuery";
    /*** 费用报销制单应用资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY = "netbank:expense:nisExpenseDetail:touchApply";
    /*** 费用报销汇总查询资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTQUERY = "netbank:expense:nisExpenseDetail:collectQuery";
    /*** 费用报销汇总应用资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTAPPLY = "netbank:expense:nisExpenseDetail:collectApply";
    /*** 费用报销处理查询资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_HANDLEQUERY = "netbank:expense:nisExpenseDetail:handleQuery";
    /*** 费用报销处理应用资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_HANDLEAPPLY = "netbank:expense:nisExpenseDetail:handleApply";
    /*** 费用报销查询资源*/
    public static final String NETBANK_EXPENSE_NISEXPENSEDETAIL_QUERY = "netbank:expense:nisExpenseDetail:query";
    /*** 付款指令发送查询资源*/
    public static final String NETBANK_BDLINK_BISEXC_SENDQUERY = "netbank:bdLink:bisExc:sendQuery";
    /*** 付款指令发送应用资源*/
    public static final String NETBANK_BDLINK_BISEXC_SENDAPPLY = "netbank:bdLink:bisExc:sendApply";
    /*** 付款指令追踪查询资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACEQUERY = "netbank:bdLink:bisExc:traceQuery";
    /*** 付款指令追踪应用资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACEAPPLY = "netbank:bdLink:bisExc:traceApply";
    /*** 付款指令追踪重发资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACERESEND = "netbank:bdLink:bisExc:traceResend";
    /*** 付款指令追踪作废资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACEDEFEAT = "netbank:bdLink:bisExc:traceDefeat";
    /*** 付款指令追踪打回资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACEBACK = "netbank:bdLink:bisExc:traceBack";
    /*** 付款指令追踪恢复资源*/
    public static final String NETBANK_BDLINK_BISEXC_TRACERECOVER = "netbank:bdLink:bisExc:traceRecover";
    /*** 付款指令追踪设为支付成功/失败资源*/
    public static final String NETBANK_BDLINK_BISEXC_SETRESULT = "netbank:bdLink:bisExc:setResult";
    /*** 付款指令可疑指令查询资源*/
    public static final String NETBANK_BDLINK_BISEXC_CONFIRMQUERY = "netbank:bdLink:bisExc:confirmQuery";
    /*** 付款指令可疑指令发送资源*/
    public static final String NETBANK_BDLINK_BISEXC_CONFIRMAPPLY = "netbank:bdLink:bisExc:confirmApply";
    /*** 直营付款查询资源*/
    public static final String NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY = "netbank:bdLink:bisExc:salePaymentQuery";
    /*** 直营付款应用资源*/
    public static final String NETBANK_BDLINK_BISEXC_SALEPAYMENTAPPLY= "netbank:bdLink:bisExc:salePaymentApply";
    /*** 付款执行查询资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_EXECUTEQUERY = "netbank:netBank:nisBillHead:executeQuery";
    /*** 付款执行应用资源*/
    public static final String NETBANK_NETBANK_NISBILLHEAD_EXECUTEAPPLY = "netbank:netBank:nisBillHead:executeApply";
    /*** 收款指令发送查询资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_SENDQUERY = "netbank:bdLink:bisGatherCmd:sendQuery";
    /*** 收款指令发送应用资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_SENDAPPLY = "netbank:bdLink:bisGatherCmd:sendApply";
    /*** 收款指令追踪查询资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_TRACEQUERY = "netbank:bdLink:bisGatherCmd:traceQuery";
    /*** 收款指令追踪应用资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_TRACEAPPLY = "netbank:bdLink:bisGatherCmd:traceApply";
	/*** 收款指令追踪重发资源*/
	public static final String NETBANK_BDLINK_BISGATHERCMD_TRACERESEND = "netbank:bdLink:bisGatherCmd:traceResend";
	/*** 收款指令追踪作废资源*/
	public static final String NETBANK_BDLINK_BISGATHERCMD_TRACEDEFEAT = "netbank:bdLink:bisGatherCmd:traceDefeat";
	/*** 收款指令追踪打回资源*/
	public static final String NETBANK_BDLINK_BISGATHERCMD_TRACEBACK = "netbank:bdLink:bisGatherCmd:traceBack";
	/*** 收款指令追踪恢复资源*/
	public static final String NETBANK_BDLINK_BISGATHERCMD_TRACERECOVER = "netbank:bdLink:bisGatherCmd:traceRecover";
    /*** 收款指令追踪设为支付成功/失败资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_SETRESULT = "netbank:bdLink:bisGatherCmd:setResult";
    /*** 查询指令发送查询资源*/
    public static final String NETBANK_BDLINK_BISQUERYCMD_SENDQUERY = "netbank:bdLink:bisQueryCmd:sendQuery";
    /*** 查询指令发送应用资源*/
    public static final String NETBANK_BDLINK_BISQUERYCMD_SENDAPPLY = "netbank:bdLink:bisQueryCmd:sendApply";
    /*** 理财指令发送查询资源*/
    public static final String NETBANK_BDLINK_BISFINANCEEXC_SENDQUERY = "netbank:bdLink:bisFinanceExc:sendQuery";
    /*** 理财指令发送应用资源*/
    public static final String NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY = "netbank:bdLink:bisFinanceExc:sendApply";
    /*** 理财指令追踪查询资源*/
    public static final String NETBANK_BDLINK_BISFINANCEEXC_TRACEQUERY = "netbank:bdLink:bisFinanceExc:traceQuery";
    /*** 理财指令追踪应用资源*/
    public static final String NETBANK_BDLINK_BISFINANCEEXC_TRACEAPPLY = "netbank:bdLink:bisFinanceExc:traceApply";
    /*** 付款指令查询资源*/
    public static final String NETBANK_BDLINK_BISEXC_QUERY = "netbank:bdLink:bisExc:query";
    /*** 收款指令查询资源*/
    public static final String NETBANK_BDLINK_BISGATHERCMD_QUERY = "netbank:bdLink:bisGatherCmd:query";
    /*** 查询指令查询资源*/
    public static final String NETBANK_BDLINK_BISQUERYCMD_QUERY = "netbank:bdLink:bisQueryCmd:query";
    /*** 理财指令查询资源*/
    public static final String NETBANK_BDLINK_BISFINANCEEXC_QUERY = "netbank:bdLink:bisFinanceExc:query";
    /**内部托收查询资源*/
    public static final String NETBANK_NETBANK_CGSBILL_QUERY = "netbank:netBank:cgsBill:query";
    /**内部托收应用资源*/
    public static final String NETBANK_NETBANK_CGSBILL_APPLY = "netbank:netBank:cgsBill:apply";
    /*** 银行账户开户查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCAPP_QUERY = "netbank:bankAcc:btBankAccApp:query";
    /*** 银行账户开户申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCAPP_APPLY = "netbank:bankAcc:btBankAccApp:apply";
    /*** 银行账户开户办理查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACC_QUERY = "netbank:bankAcc:btBankAcc:query";
    /*** 银行账户开户办理申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACC_APPLY = "netbank:bankAcc:btBankAcc:apply";
	/*** 银行账户开户办理新增资源*/
	public static final String NETBANK_BANKACC_BTBANKACC_CREATE = "netbank:bankAcc:btBankAcc:create";
	/*** 银行账户开户办理修改资源*/
	public static final String NETBANK_BANKACC_BTBANKACC_UPDATE = "netbank:bankAcc:btBankAcc:update";
    /*** 银行账户开户办理删除资源*/
    public static final String NETBANK_BANKACC_BTBANKACC_DELETE = "netbank:bankAcc:btBankAcc:delete";
    /*** 银行账号变更查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCREV_QUERY = "netbank:bankAcc:btBankAccRev:query";
    /*** 银行账号变更申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCREV_APPLY = "netbank:bankAcc:btBankAccRev:apply";
    /*** 银行账号变更办理查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY = "netbank:bankAcc:btBankAccRevHandle:query";
    /*** 银行账号变更办理申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCREVHANDLE_APPLY = "netbank:bankAcc:btBankAccRevHandle:apply";
    /*** 银行账号注销查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDEL_QUERY = "netbank:bankAcc:btBankAccDel:query";
    /*** 银行账号注销申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDEL_APPLY = "netbank:bankAcc:btBankAccDel:apply";
    /*** 银行账号注销办理查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY = "netbank:bankAcc:btBankAccDelHandle:query";
    /*** 银行账号注销办理申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDELHANDLE_APPLY = "netbank:bankAcc:btBankAccDelHandle:apply";
    /*** 银行账户解冻查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCUNFREEZE_QUERY = "netbank:bankAcc:btBankAccUnFreeze:query";
    /*** 银行账户解冻申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCUNFREEZE_APPLY = "netbank:bankAcc:btBankAccUnFreeze:apply";
    /*** 银行账户冻结查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCFREEZE_QUERY = "netbank:bankAcc:btBankAccFreeze:query";
    /*** 银行账户冻结申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCFREEZE_APPLY = "netbank:bankAcc:btBankAccFreeze:apply";
    /*** 银行账户冻结/解冻查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCFREEZE_REPORTQUERY = "netbank:bankAcc:btBankAccFreeze:reportquery";
    /*** 银行账户查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCQUERY_QUERY = "netbank:bankAcc:btBankAccQuery:query";
    /*** 银行账户余额查询资源*/
    public static final String NETBANK_BANKACC_BISACCBAL_QUERY = "netbank:bankAcc:bisAccBal:query";
    /*** 银行账户流水查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDTL_QUERY = "netbank:bankAcc:btBankAccDtl:query";
    /***  银行账户变更查询资源*/
    public static final String NETBANK_BANKACC_ACCCHANGE_QUERY = "netbank:bankAcc:accChange:query";
    /*** 付款明细勾兑查询*/
    public static final String NETBANK_BANKACC_PAYMENTBLENDING_QUERY = "netbank:bankAcc:paymentBlending:query";
    /*** 付款明细勾兑*/
    public static final String NETBANK_BANKACC_PAYMENTBLENDING_APPLY = "netbank:bankAcc:paymentBlending:apply";
    /*** 退款勾兑查询*/
    public static final String NETBANK_BANKACC_REFUNDBLENDING_QUERY = "netbank:bankAcc:refundblending:query";
    /*** 退款勾兑*/
    public static final String NETBANK_BANKACC_REFUNDBLENDING_APPLY = "netbank:bankAcc:refundblending:apply";
    /*** 银行与内部账号父子关系维护查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCMAINTAIN_QUERY = "netbank:bankAcc:btBankAccMaintain:query";
    /*** 银行与内部账号父子关系维护申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCMAINTAIN_APPLY = "netbank:bankAcc:btBankAccMaintain:apply";
    /*** 资金池银行与内部账号父子关系维护查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_QUERY = "netbank:bankAcc:btBankAccMaintain:pool:query";
    /*** 资金池银行与内部账号父子关系维护申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCMAINTAIN_POOL_APPLY = "netbank:bankAcc:btBankAccMaintain:pool:apply";
    /*** 银行账号父子关系维护查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCRELATION_QUERY = "netbank:bankAcc:btBankAccRelation:query";
    /*** 银行账号父子关系维护申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCRELATION_APPLY = "netbank:bankAcc:btBankAccRelation:apply";
    /*** 资金池银行账号父子关系维护查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY = "netbank:bankAcc:btBankAccRelation:pool:query";
    /*** 资金池银行账号父子关系维护申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCRELATION_POOL_APPLY = "netbank:bankAcc:btBankAccRelation:pool:apply";
    /*** 银行账户维护查询资源*/
    public static final String NETBANK_BANKACC_BTBANKACCHANDLE_QUERY = "netbank:bankAcc:btBankAccHandle:query";
    /*** 银行账户维护申请资源*/
    public static final String NETBANK_BANKACC_BTBANKACCHANDLE_APPLY = "netbank:bankAcc:btBankAccHandle:apply";
    /*** 银行账户余额管理查询资源*/
    public static final String NETBANK_BANKACC_BISACCBAL_MANAGERQUERY = "netbank:bankAcc:bisAccBal:managerQuery";
    /*** 银行账户余额管理申请资源*/
    public static final String NETBANK_BANKACC_BISACCBAL_APPLY = "netbank:bankAcc:bisAccBal:apply";
	/*** 银行账户流水管理查询资源*/
	public static final String NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY = "netbank:bankAcc:btBankAccDtl:managerQuery";
	/*** 银行账户流水管理申请资源*/
	public static final String NETBANK_BANKACC_BTBANKACCDTL_APPLY = "netbank:bankAcc:btBankAccDtl:apply";
    /*** 银行账户流水管理变更资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDTL_CHANGE = "netbank:bankAcc:btBankAccDtl:change";
    /*** 银行账户流水管理作废资源*/
    public static final String NETBANK_BANKACC_BTBANKACCDTL_DEFEAT = "netbank:bankAcc:btBankAccDtl:defeat";
	/*** 银行账户流水补录查询资源*/
	public static final String NETBANK_BANKACC_BISACCDTL_MAKEUPQUERY = "netbank:bankAcc:bisAccDtl:makeupQuery";
	/*** 银行账户流水补录申请资源*/
	public static final String NETBANK_BANKACC_BISACCDTL_MAKEUPAPPLY = "netbank:bankAcc:bisAccDtl:makeupApply";
    /*** 账户明细截止时间维护申请资源*/
    public static final String NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND = "netbank:bankAcc:bisAccBal:selectBankAccEnd";
    /*** 银行账户流水资金计划申请资源*/
    public static final String NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY = "netbank:bankAcc:bisAccDtlBudgetRule:apply";
    /*** 银行账户流水资金计划查询资源*/
    public static final String NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY = "netbank:bankAcc:bisAccDtlBudgetRule:query";
    /*** 上划查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_UP_QUERY ="netbank:allocation:typeUp:query";
    /*** 上划申请资源*/
    public static final String NETBANK_ALLOCATION_TYPE_UP_APPLY ="netbank:allocation:typeUp:apply";
    /*** 下拨查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_DOWN_QUERY ="netbank:allocation:typeDown:query";
    /*** 下拨申请资源*/
    public static final String NETBANK_ALLOCATION_TYPE_DOWN_APPLY ="netbank:allocation:typeDown:apply";
    /*** 请款查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_ASK_QUERY ="netbank:allocation:typeAsk:query";
    /*** 请款申请资源*/
    public static final String NETBANK_ALLOCATION_TYPE_ASK_APPLY ="netbank:allocation:typeAsk:apply";
    /*** 上缴申请资源*/
    public static final String NETBANK_ALLOCATION_TYPE_TURN_QUERY ="netbank:allocation:typeTurn:query";
    /*** 上缴查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_TURN_APPLY ="netbank:allocation:typeTurn:apply";
    /*** 头寸调拨申请资源*/
    public static final String NETBANK_ALLOCATION_TYPE_POS_QUERY ="netbank:allocation:typePos:query";
    /*** 头寸调拨查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_POS_APPLY ="netbank:allocation:typePos:apply";
    /*** 资金调拨查询资源*/
    public static final String NETBANK_ALLOCATION_TYPE_FUND_QUERY ="netbank:allocation:typeFund:query";
	/*** 资金调拨记账查询资源*/
	public static final String NETBANK_ALLOCATION_TYPE_ACCOUNT_QUERY = "netbank:allocation:typeAccount:query";
    /*** 调拨策略维护查询资源*/
    public static final String NETBANK_ALLOCATION_BISGATHERPOLICY_QUERY ="netbank:allocation:bisGatherPolicy:query";
    /*** 调拨策略维护申请资源*/
    public static final String NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY ="netbank:allocation:bisGatherPolicy:apply";
    /*** 银行账号关系策略维护查询资源*/
    public static final String NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY ="netbank:allocation:bisAccGatherPolicy:query";
    /*** 银行账号关系策略维护申请资源*/
    public static final String NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY ="netbank:allocation:bisAccGatherPolicy:apply";
    /*** 例外规则管理查询资源*/
    public static final String NETBANK_ALLOCATION_EXCRULESMANAGE_QUERY ="netbank:allocation:excRulesManage:query";
    /*** 例外规则管理申请资源*/
    public static final String NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY ="netbank:allocation:excRulesManage:apply";
    /*** 电子回单类型操作资源*/
    public static final String NETBANK_RECEIPT_EVISATYPE_OP ="netbank:receipt:evisaType:op";
    /*** 电子回单查询*/
    public static final String NETBANK_RECEIPT_ELEBILL_QUERY ="netbank:receipt:eleBill:query";
    /*** 电子回单签章*/
    public static final String NETBANK_RECEIPT_SIGNATURE ="netbank:receipt:signature";
    /*** 银行电子回单查询*/
    public static final String NETBANK_RECEIPT_BANKBILL_QUERY ="netbank:receipt:bankbill:query";
    /*** 银行历史电子回单查询*/
    public static final String NETBANK_RECEIPT_BANKBILL_QUERYHIS = "netbank:receipt:bankbill:queryhis";
    /*** 银行电子勾兑查询*/
    public static final String NETBANK_RECEIPT_BISBANKRECEIPTBLENDING_QUERY ="netbank:receipt:bisbankreceiptBlending:query";
    /*** 银行电子勾兑应用*/
    public static final String NETBANK_RECEIPT_BISBANKRECEIPTBLENDING_APPLY ="netbank:receipt:bisbankreceiptBlending:apply";
    /*** 三单关联执行策略查询*/
    public static final String NETBANK_RECEIPT_BISBANKEXECUTIONSTRATEGY_QUERY ="netbank:receipt:bisBankExecutionStrategy:query";
    /*** 三单关联执行策略应用*/
    public static final String NETBANK_RECEIPT_BISBANKEXECUTIONSTRATEGY_APPLY ="netbank:receipt:bisBankExecutionStrategy:apply";
    /*** 代发工资制单查询资源*/
    public static final String NETBANK_WAGE_NISWAGE_TOUCHQUERY = "netbank:wage:nisWage:touchQuery";
    /*** 代发工资制单应用资源*/
    public static final String NETBANK_WAGE_NISWAGE_TOUCHAPPLY = "netbank:wage:nisWage:touchApply";
    /*** 代发工资结果异常处理查询资源*/
    public static final String NETBANK_WAGE_NISWAGE_DEALQUERY = "netbank:wage:nisWage:dealQuery";
    /*** 代发工资结果异常处理应用资源*/
    public static final String NETBANK_WAGE_NISWAGE_DEALAPPLY = "netbank:wage:nisWage:dealApply";
    /*** 代发工资查询资源*/
    public static final String NETBANK_WAGE_NISWAGE_QUERY = "netbank:wage:nisWage:query";
    /***资金调拔明细表查询资源*/
    public static final String NETBANK_REPORT_ALLOCATION_QUERY = "netbank:report:allocation:query";
    /***网点资金划拨汇总表查询资源*/
    public static final String NETBANK_REPORT_ALLOCATIONSUM_QUERY = "netbank:report:allocationSum:query";
    /***下拨金额分析表查询资源*/
    public static final String NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY = "netbank:report:downAllocationAnalysis:query";
    /***资金余额统计表查询资源*/
    public static final String NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY = "netbank:report:fundsBalanceStatistics:query";
    /***资金归集、下拨情况表以及内部、银行账户余额统计分析情况表查询资源*/
    public static final String NETBANK_REPORT_NISALLOCATIONREQ_QUERY = "netbank:report:nisallocationreq:query";
    /**资金结算量统计表，按单位查询以及按银行类型查询的查询资源*/
    public static final String NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY = "netbank:report:allocationsettlement:query";
    /**客商收入资金量统计表的查询资源*/
    public static final String NETBANK_REPORT_MERCHANTRECEIPT_QUERY = "netbank:report:merchantreceipt:query";
    /**内部存款利息收支综合表的查询资源*/
    public static final String NETBANK_REPORT_FCACCINTEREST_QUERY = "netbank:report:fcAccInterest:query";
    /**银行账号数量统计表查询资源*/
    public static final String NETBANK_REPORT_BANKACCOUNTCOUNT_QUERY = "netbank:report:bankAccountCount:query";
    /**银行账号余额分布表查询资源*/
    public static final String NETBANK_REPORT_BANKBALDISTRIBUTE_QUERY = "netbank:report:bankBalDistribute:query";
    /**银行存款余额趋势查询资源*/
    public static final String NETBANK_REPORT_BANKACCBALTREND_QUERY = "netbank:report:bankaccbaltrend:query";
    /**存款余额对比分析查询资源*/
    public static final String DEPOSIT_BALANCE_ANALYSIS_QUERY = "deposit:balance:analysis:query";
    /**银行账户余额概览查询资源*/
    public static final String NETBANK_REPORT_HOMEBANKACCBALDISTRIBUTE_QUERY = "netbank:report:homeBankAccBalDistribute:query";
    /**结算业务量统计表查询资源*/
    public static final String NETBANK_REPORT_SETTLEMENT_STATISTICE_QUERY = "netbank:report:settlement:statistics:query";
    /**结算业务统计表查询资源*/
    public static final String NETBANK_REPORT_SETTLEMENT_BUSINESS_QUERY = "netbank:report:settlement:business:query";
    /*** 协定存款登记应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY = "netbank:agreeddeposit:fcagreeddeposit:reg:apply";
    /*** 协定存款登记查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY = "netbank:agreeddeposit:fcagreeddeposit:reg:query";
    /*** 协定存款登记复核应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_APPLY = "netbank:agreeddeposit:fcagreeddeposit:regCheck:apply";
    /*** 协定存款登记复核查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY = "netbank:agreeddeposit:fcagreeddeposit:regCheck:query";
    /*** 协定存款延期应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY = "netbank:agreeddeposit:fcagreeddeposit:pos:apply";
    /*** 协定存款延期查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY = "netbank:agreeddeposit:fcagreeddeposit:pos:query";
    /*** 协定存款延期复核应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_APPLY = "netbank:agreeddeposit:fcagreeddeposit:posCheck:apply";
    /*** 协定存款延期复核查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY = "netbank:agreeddeposit:fcagreeddeposit:posCheck:query";
    /*** 协定存款终止应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY = "netbank:agreeddeposit:fcagreeddeposit:term:apply";
    /*** 协定存款终止查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY = "netbank:agreeddeposit:fcagreeddeposit:term:query";
    /*** 协定存款终止复核应用资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_APPLY = "netbank:agreeddeposit:fcagreeddeposit:termCheck:apply";
    /*** 协定存款终止复核查询资源*/
    public static final String NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY = "netbank:agreeddeposit:fcagreeddeposit:termCheck:query";
    /*** 单位资金池应用权限*/
    public static final String NETBANK_POOL_FCCORPPOOL_APPLY = "netbank:pool:fcCorpPool:apply";
    /*** 付款转移应用权限*/
    public static final String ERP_PAYINFO_APPLY = "erp:payinfo:apply";
    /*** 收款办理查询资源*/
    public static final String NETBANK_NETBANK_NISRECEIVABLE_QUERY = "netbank:netBank:nisReceivable:query";
    /*** 收款办理新增资源*/
    public static final String NETBANK_NETBANK_NISRECEIVABLE_APPLY = "netbank:netBank:nisReceivable:apply";
    /*** 收款查询资源*/
    public static final String NETBANK_NETBANKQUERY_NISRECEIVABLE_QUERY = "netbank:netBankQuery:nisReceivable:query";
    /*** 网银信息查询资源*/
    public static final String NETBANK_BANKACC_BTNETUKEY_QUERY = "netbank:bankAcc:btnetukey:query";
    /*** 网银信息应用资源*/
    public static final String NETBANK_BANKACC_BTNETUKEY_UPDATE = "netbank:bankAcc:btnetukey:update";
    /*** 资金存量汇总表查询资源*/
    public static final String BI_STOCK_CAPITALSTOCK_QUERY = "bi:stock:capitalStock:query";
    /*** 对账单查询资源*/
    public static final String NETBANK_BIS_BISBANKMONTHSTATEMENTDTL_QUERY = "netbank:bis:bisBankMonthStatementDtl:query";
    public static Map<Integer,String> ALLOCATION_TYPE_MAP = new HashMap<Integer,String>();
    static {
        ALLOCATION_TYPE_MAP.put(AllocationConstant.ALLOCATION_TYPE_UP, NETBANK_ALLOCATION_TYPE_UP_QUERY);
        ALLOCATION_TYPE_MAP.put(AllocationConstant.ALLOCATION_TYPE_ASK, NETBANK_ALLOCATION_TYPE_ASK_QUERY);
        ALLOCATION_TYPE_MAP.put(AllocationConstant.ALLOCATION_TYPE_DOWN, NETBANK_ALLOCATION_TYPE_DOWN_QUERY);
        ALLOCATION_TYPE_MAP.put(AllocationConstant.ALLOCATION_TYPE_POS, NETBANK_ALLOCATION_TYPE_POS_QUERY);
        ALLOCATION_TYPE_MAP.put(AllocationConstant.ALLOCATION_TYPE_TURN, NETBANK_ALLOCATION_TYPE_TURN_QUERY);
    }
    /*** 内部账户管理参数应用资源*/
    public static final String NETBANK_CONFIG_FCSCONFIG_APPLY = "netbank:config:fcsConfig:apply";
    /*** 支付结算参数应用资源*/
    public static final String NETBANK_CONFIG_NISCONFIG_APPLY = "netbank:config:nisConfig:apply";
    /*** 费用报销参数应用资源*/
    public static final String NETBANK_CONFIG_EXPCONFIG_APPLY = "netbank:config:expConfig:apply";
    /*** 资金调拨参数应用资源*/
    public static final String NETBANK_CONFIG_ALLOTCONFIG_APPLY = "netbank:config:allotConfig:apply";
    /*** 代发工资参数应用资源*/
    public static final String NETBANK_CONFIG_WAGECONFIG_APPLY = "netbank:config:wageConfig:apply";
    /*** 资金池参数应用资源*/
    public static final String NETBANK_CONFIG_POOLCONFIG_APPLY = "netbank:config:poolConfig:apply";
    /*** 电子回单参数应用资源*/
    public static final String NETBANK_CONFIG_ELECONFIG_APPLY = "netbank:config:eleConfig:apply";
    /*** 银行流水认领参数应用资源*/
    public static final String NETBANK_CONFIG_RECEIVABLECONFIG_APPLY = "netbank:config:receivableConfig:apply";
    /*** 系统结算方式应用资源*/
    public static final String NETBANK_BASICDATA_BTPAYTYPE_APPLY = "netbank:basicdata:btpaytype:apply";
    /*** 付款方式维护应用资源*/
    public static final String NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY = "netbank:basicdata:fcvouchertype:apply";
    /*** 付款方式分配应用资源*/
    public static final String NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY = "netbank:basicdata:btassignpayway:apply";
    /*** 账户性质维护应用资源*/
    public static final String NETBANK_BASICDATA_BTACCNATURE_APPLY = "netbank:basicdata:btaccnature:apply";
    /*** 账户属性维护应用资源*/
    public static final String NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY = "netbank:basicdata:bisaccattribute:apply";
    /*** 接口代码业务规则维护应用资源*/
    public static final String NETBANK_BASICDATA_BISBIFRULE_APPLY = "netbank:basicdata:bisbifrule:apply";
    /*** 业务种类维护应用资源*/
    public static final String NETBANK_BASICDATA_NISBUSTYPE_APPLY = "netbank:basicdata:nisbustype:apply";
    /*** 单位业务种类设置应用资源*/
    public static final String NETBANK_BASICDATA_NISCORPBUS_APPLY = "netbank:basicdata:niscorpbus:apply";
    /*** 退单类型维护应用资源*/
    public static final String NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY = "netbank:basicdata:btchargebacktype:apply";
    /*** 交易失败原因维护应用资源*/
    public static final String NETBANK_BASICDATA_BISFAILREASON_APPLY = "netbank:basicdata:bisfailreason:apply";
    /*** 项目维护管理应用资源*/
    public static final String NETBANK_BASICDATA_NISPROJECT_APPLY = "netbank:basicdata:nisproject:apply";
    /*** 离线流水智能导入配置应用资源*/
    public static final String NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY = "netbank:basicdata:bisaccdtlimportconfig:apply";
    /*** 付款排期维护应用资源*/
    public static final String NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY = "netbank:basicdata:nispayschedulingelements:apply";
    /*** 保证金类型维护应用资源*/
    public static final String NETBANK_BOND_FCBONDTYPE_APPLY = "netbank:bond:fcbondtype:apply";
    /*** 保证金账户查询应用资源*/
    public static final String NETBANK_BOND_FCBONDBACK_APPLY = "netbank:bond:fcbondback:apply";
    /*** 保证金项目查询应用资源*/
    public static final String NETBANK_BOND_FCBONDINFO_APPLY = "netbank:bond:fcbondInfo:apply";
    /*** 对账处理应用资源*/
    public static final String NETBANK_CORPCOMPAREBILL_INDEX_APPLY = "netbank:corpCompareBill:index:apply";
    /*** 对账查询应用资源*/
    public static final String NETBANK_CORPCOMPAREBILL_QUERY_APPLY = "netbank:corpCompareBill:query:apply";
    /*** 按账号查询 查询资源*/
    public static final String REPORT_ALLOCATIONSETTLEMENT_ACCLIST_QUERY = "report:allocationsettlement:acclist:query";
    /*** 按开户行查询 查询资源*/
    public static final String REPORT_ALLOCATIONSETTLEMENT_ACCOUNTBANKLIST_QUERY = "report:allocationsettlement:accountbanklist:query";
    /*** 自动支付策略 查询资源*/
    public static final String NETBANK_AUTO_PAY_POLICY_QUERY = "netbank:netBank:bisPayPolicy:query";
    /*** 自动支付策略 应用资源*/
    public static final String NETBANK_AUTO_PAY_POLICY_APPLY = "netbank:netbank:bisPayPolicy:apply";
    /*** 待付款池 查询资源*/
    public static final String NETBANK_NETBANK_NISBILLHEADPOOL_QUERY = "netbank:netBank:nisBillHeadPool:query";
    /*** 待付款池 应用资源*/
    public static final String NETBANK_NETBANK_NISBILLHEADPOOL_APPLY = "netbank:netBank:nisBillHeadPool:apply";
}
