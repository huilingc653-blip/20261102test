package com.bytter.biz.payment.api.constant;
/**
 * 外汇管理工作流常量类
 * @author huangx
 * @since 2019-07-15
 */
public class FexWorkFlowConstant {
    /**
     * 外币汇款
     */
    public final static String FEX_FOREIGN_REQ = "fexForeignReq";
    /**
     * 交易对手银行账户审批
     */
    public final static String FEX_COTP_BANK_ACC = "fexCotpBankAcc";
    /**
     * 主体单位默认银行账户
     */
    public final static String FEX_CORP_BANK_ACC = "fexCorpBankAcc";
    /**
     * 交易对手默认银行账户
     */
    public final static String FEX_COTP_DEFAULT_ACC = "fexCotpDefaultAcc";
    /**
     * 交易对手/对手组
     */
    public final static String FEX_COTP = "fexCotp";
    /**
     * 外汇交易需求申请
     */
    public final static String FEX_TRAN_APP = "fexTranApp";
    /**
     * 外汇买卖交易
     */
    public final static String FEX_TRAN_REQ_FOREIGN = "fexTranReqForeign";

    /**
     * 即期交易
     */
    public final static String FEX_TRAN_REQ_SPOT_FOREIGN = "fexTranReqSpotForeign";
    /**
     * 远期交易
     */
    public final static String FEX_TRAN_REQ_FORWARD_FOREIGN = "fexTranReqForwardForeign";
    /**
     * 掉交易
     */
    public final static String FEX_TRAN_REQ_SWAP_FOREIGN = "fexTranReqSwapForeign";

    /**
     * 外汇期权交易
     */
    public final static String FEX_TRAN_REQ_OPTION = "fexTranReqOption";
    /**
     * 资金调拨交易
     */
    public final static String FEX_TRAN_REQ_ALLOCATION = "fexTranReqAllocation";
    /**
     * 资金收付交易
     */
    public final static String FEX_TRAN_REQ_PAY = "fexTranReqPay";
    /**
     *  提前交割或展期e
     */
    public final static String FEX_TRAN_DEAL_DF = "fexTranDealDf";
    /**
     *  违约
     */
    public final static String FEX_TRAN_DEAL_DE = "fexTranDealDe";
    /**
     *  NDF交割
     */
    public final static String FEX_TRAN_DEAL_NDF = "fexTranDealNdf";
    /**
     * 结算执行
     */
    public final static String FEX_CASH_FLOW = "fexCashFlow";
    /**
     * 外汇敞口
     */
    public final static String FEX_EXPOSURE_MAIN = "fexExposureMain";

    /**
     * 生命周期
     */
    public final static String FEX_TRAN_LIFE_CYCLE = "fexTranLifeCycle";
}
