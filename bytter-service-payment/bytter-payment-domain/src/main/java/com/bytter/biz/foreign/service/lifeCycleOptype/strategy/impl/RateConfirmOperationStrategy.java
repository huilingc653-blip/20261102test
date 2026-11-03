package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCorpBankAccDtlService;
import com.bytter.biz.foreign.service.FexTranLifeCycleService;
import com.bytter.biz.foreign.service.FexTranReqExtService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.BisAccBalConstant;
import com.bytter.system.api.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("rateConfirm")
public class RateConfirmOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired(required = false)
    private IConfigService configService;


    /**
     * 通过VO处理字段封装为ENTITY
     * @param lifeCycleVo
     * @param user
     * @return
     */
    @Override
    public void dealField(FexTranLifeCycleVo lifeCycleVo, ICurrentUser user, FexTranLifeCycle entity) {
        entity.setOpDate(lifeCycleVo.getOpDate());
        entity.setContractRate(lifeCycleVo.getContractRate());
        entity.setBuyMoney(lifeCycleVo.getBuyMoney());
        entity.setSellMoney(lifeCycleVo.getSellMoney());
        entity.setSpotRate(lifeCycleVo.getSpotRate());
        entity.setProfitLossMoney(lifeCycleVo.getProfitLossMoney());
        entity.setConvertCurType(lifeCycleVo.getConvertCurType());
        entity.setConvertRate(lifeCycleVo.getConvertRate());
        entity.setConvertLossMoney(lifeCycleVo.getConvertLossMoney());
        entity.setTranAddr(lifeCycleVo.getTranAddr());
        entity.setTranAddrId(lifeCycleVo.getTranAddrId());
        entity.setRmk(lifeCycleVo.getRmk());
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {
        FexTranReq fexTranReq = fexTranReqService.get(fexTranLifeCycle.getReqId());
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranReq.getId());
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReqVO.getId());
        FexTranReqExt fexTranReqExt = fexTranReqExtService.get(fexTranReqExtVo.getId());
        //固定金额：1-买入金额（收款流水） 2-卖出金额（支出流水）
        if (ForeignConstant.FIXED_MONEY_2.equals(fexTranReq.getFixedMoney())) {
            saveExpectFexCashFLow(fexTranReqVO, user, fexTranLifeCycle);
            fexTranReq.setBuyMoney(fexTranLifeCycle.getBuyMoney());
        } else if (ForeignConstant.FIXED_MONEY_1.equals(fexTranReq.getFixedMoney())) {
            saveExpectFexCashFLow(fexTranReqVO, user, fexTranLifeCycle);
            fexTranReq.setSellMoney(fexTranLifeCycle.getSellMoney());
        }
        if (fexTranReqVO.getTranCurPairBaseId().equals(fexTranReq.getBuyCurId())) {
            fexTranReqExt.setPositionMoney(fexTranReq.getBuyMoney());
        } else {
            fexTranReqExt.setPositionMoney(fexTranReq.getSellMoney());
        }
        fexTranReq.setContractRate(fexTranLifeCycle.getContractRate());
        fexTranReq.setStatus(ForeignConstant.ONGOING);
        fexTranReqExtService.updateEntity(fexTranReqExt);
        fexTranReqService.updateEntity(fexTranReq);
        // 即期自动交割 && 线下自动交割（线上的需要交易成功）
        if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType()) &&
                ForeignConstant.FEX_TRAN_RATE_TYPE_LISTED.equals(fexTranReqVO.getTranRateType())) {
            fexTranLifeCycleService.autoSpotDelivery(fexTranReqVO, user, fexTranLifeCycle);
        }
    }

    /**
     * 预测现金流生成(用于买卖交易，审批通过时生成预测现金流，不推结算执行)与实际现金流的数据区别在于不会生成billcode
     * @param fexTranReq
     */
    public void saveExpectFexCashFLow(FexTranReqVO fexTranReq, ICurrentUser user, FexTranLifeCycle fexTranLifeCycle) {
        FexCashFlow entity = new FexCashFlow();
        entity.setCashFlowDate(fexTranLifeCycle.getOpDate());
        entity.setSettleDate(fexTranReq.getSettleDate());
        //现金流类型：0本金，1利息,2-违约金，3-期权费，4-退款
        entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        entity.setTranBillId(fexTranReq.getId());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        //现金流项目
        entity.setCashItem(fexTranReq.getCashItem());
        entity.setTranType(fexTranReq.getTranType());
        //跨行标志
        entity.setDifBank(fexTranReq.getDifBank());
        //备注
        entity.setRmk(fexTranReq.getRmk());
        //单据来源
        entity.setBillFrom(ForeignConstant.BILL_FROM_3);
        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        entity.setCotpId(fexTranReq.getCotpId());
        entity.setCorpId(fexTranReq.getCorpId());
        if (ForeignConstant.FIXED_MONEY_1.equals(fexTranReq.getFixedMoney())) { // 交易.固定金额=买入金额时，取卖出
            entity.setCurId(fexTranLifeCycle.getSellCurId());
            entity.setPayMoney(fexTranLifeCycle.getSellMoney().abs());
            entity.setCorpAccId(fexTranReq.getCorpAccId());
            entity.setCorpBank(fexTranReq.getCorpBank());
            entity.setCorpAccName(fexTranReq.getCorpAccName());
            entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());
            entity.setCdSign(BisAccBalConstant.CDSIGN_OUT);

            entity.setCotpBank(fexTranReq.getSrcBank());
        }else{
            entity.setCurId(fexTranLifeCycle.getBuyCurId());
            entity.setPayMoney(fexTranLifeCycle.getBuyMoney().abs());
            entity.setCorpAccId(fexTranReq.getSrcAccId());
            entity.setCorpBank(fexTranReq.getSrcBank());
            entity.setCorpAccName(fexTranReq.getSrcAccName());
            entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());
            entity.setCdSign(BisAccBalConstant.CDSIGN_IN);

            entity.setCotpBank(fexTranReq.getCorpBank());
        }
        //外汇买卖交易-现金流付款账号对应的卖出币种
        entity.setPayerCurId(fexTranReq.getSellCurId());
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setCorpId(fexTranReq.getCorpId());
        corpSearchVO.setProId(fexTranReq.getProId());
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            //支付方式
            entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
        }

        //是否跳过结算执行
        String isSkipSettle = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISSKIPSETTLE).getValue();
        if(CommonConstant.YES.equals(isSkipSettle)) {
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(ForeignConstant.PAY_STATUS_1);
        } else {
            entity.setStatus(CommonConstant.STATUE_TEMP);
            entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        }
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        fexCashFlowService.saveOrUpdate(entity);
    }
}
