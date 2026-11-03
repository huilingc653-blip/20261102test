package com.bytter.biz.foreign.utils;

import com.bytter.biz.foreign.entity.FexProfitLossManage;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;


/**
 *  买卖交易转换类
 */
public class FexForeignConverter {
    /**
     *
     * 即期生命周期
     *
     * @param req
     * @return
     */
    public static FexTranLifeCycleVo converterFexTranLifeCycleBean(FexTranReqVO req){
        FexTranLifeCycleVo fexTranLifeCycle = new FexTranLifeCycleVo();
        fexTranLifeCycle.setReqId(req.getId());
        fexTranLifeCycle.setCorpId(req.getCorpId());
        fexTranLifeCycle.setOpDate(req.getSettleDate());
        fexTranLifeCycle.setSpotRate(req.getSpotRate());
        fexTranLifeCycle.setContractRate(req.getContractRate());
        fexTranLifeCycle.setProfitLossCur(req.getProfitLossCur());
        fexTranLifeCycle.setProfitLossMoney(req.getProfitLossMoney());
        fexTranLifeCycle.setConvertCurType(req.getConvertCurType());
        fexTranLifeCycle.setConvertRate(req.getConvertRate());
        fexTranLifeCycle.setConvertLossMoney(req.getConvertMoney());
        fexTranLifeCycle.setBuyMoney(req.getBuyMoney());
        fexTranLifeCycle.setSellMoney(req.getSellMoney());
        fexTranLifeCycle.setBuyCurId(req.getBuyCurId());
        fexTranLifeCycle.setSellCurId(req.getSellCurId());
        fexTranLifeCycle.setTranDate(req.getTranDate());
        fexTranLifeCycle.setSettleDate(req.getSettleDate());
        fexTranLifeCycle.setBusDirection(req.getBusDirection());
        fexTranLifeCycle.setPositionMoney(req.getPositionMoney());
        fexTranLifeCycle.setQuotationMethod(req.getQuotationMethod());
        fexTranLifeCycle.setQuotationCurId(req.getQuotationCurId());
        fexTranLifeCycle.setQuotationReferCurId(req.getQuotationReferCurId());
        fexTranLifeCycle.setTranCurPairId(req.getTranCurPairId());
        fexTranLifeCycle.setTranCurPairBaseId(req.getTranCurPairBaseId());
        fexTranLifeCycle.setTranCurPairReferId(req.getTranCurPairReferId());
        return fexTranLifeCycle;
    }



    public static FexProfitLossManage converterProfitLoss(FexTranReqVO req){
        FexProfitLossManage fexProfitLossManage = new FexProfitLossManage();
        //交易表id
        fexProfitLossManage.setReqId(req.getId());
        //损益币种
        fexProfitLossManage.setCurId(req.getProfitLossCur());
        //单位id
        fexProfitLossManage.setCorpId(req.getCorpId());
        //生命周期id
        //fexProfitLossManage.setLifeCycle("");
        //报价日期
        //fexProfitLossManage.setQuotationDate(null);
        //结算日期
        fexProfitLossManage.setSettleDate(req.getSettleDate());
        //合同汇率
        fexProfitLossManage.setContractRate(req.getContractRate());
        //估值利率
        //fexProfitLossManage.setValuationRate(null);
        //折本位币汇率
        fexProfitLossManage.setConvertRate(req.getConvertRate());

        //折本位币浮动损益
        fexProfitLossManage.setConvertFloatMoney(null);
        //已实现损益
        fexProfitLossManage.setRealMoneyTotal(null);
        //折本位币已实现损益
        fexProfitLossManage.setConvertRealMoneyTotal(null);
        //状态
        return fexProfitLossManage;
    }

}
