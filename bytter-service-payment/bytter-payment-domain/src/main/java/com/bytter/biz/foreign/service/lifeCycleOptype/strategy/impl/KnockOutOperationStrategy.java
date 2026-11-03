package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCorpBankAccDtlService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
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
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component("knockOut")
public class KnockOutOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexTranReqExtService fexTranReqExtService;
    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;
    
    @Autowired
    private IBtBankAccService btBankAccService;

    @Autowired(required = false)
    private IConfigService configService;


    @Override
    public void dealField(FexTranLifeCycleVo vo, ICurrentUser user, FexTranLifeCycle entity) {
        //生命周期编号
        entity.setBillNo(vo.getBillNo());
        //操作类型
        entity.setOpType(vo.getOpType());
        //操作日期
        entity.setOpDate(vo.getOpDate());
        //设置创建人
        entity.setCreateBy(user.getId());
        //设置创建时间
        entity.setCreateDate(new Date());
        //判断是敲出1还是敲出2
        //如果是涉及退款，则为敲出2
        if(CommonConstant.YES.equals(vo.getIsRefund())){
            //结算日期
            entity.setSettleDate(vo.getSettleDate());
            //结算币种
            entity.setCurId(vo.getCurId());
            //结算金额
            entity.setMoney(vo.getMoney());
            //结算账号
            entity.setBankAcc(vo.getBankAcc());
            //操作日记账汇率
            entity.setRecordRate(vo.getRecordRate());
            //损益币种
            entity.setProfitLossCur(vo.getProfitLossCur());
            //损益
            entity.setProfitLossMoney(vo.getProfitLossMoney());
        }
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranLifeCycle.getReqId());
        FexTranReq fexTranReq = fexTranReqService.get(fexTranReqVO.getId());
        //持仓金额反写为0
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranLifeCycle.getReqId());
        FexTranReqExt fexTranReqExt = fexTranReqExtService.get(fexTranReqExtVo.getId());
        fexTranReqExt.setPositionMoney(BigDecimal.ZERO);;
        fexTranReqExtService.updateEntity(fexTranReqExt);
        if (CommonConstant.YES.equals(fexTranReqVO.getIsRefund())) {
            //反写现金流
            saveExpectFexCashFLow(fexTranReqVO, user, fexTranLifeCycle);
            //反写损益信息
            saveManageProfitLoss(fexTranLifeCycle, BigDecimal.ZERO, user);
        } else {
            fexProfitLossManageService.refreshFloatingProfitLoss(
                    fexTranLifeCycle.getReqId(), BigDecimal.ZERO, user.getLoginName());
        }
        // 更新为敲出
        fexTranReq.setStatus(ForeignConstant.STATUE_KNOCKOUT);
        fexTranReqService.updateEntity(fexTranReq);
    }



    /**
     * 预测现金流生成(用于买卖交易，审批通过时生成预测现金流，不推结算执行)与实际现金流的数据区别在于不会生成billcode
     * @param fexTranReq
     */
    public void saveExpectFexCashFLow(FexTranReqVO fexTranReq, ICurrentUser user, FexTranLifeCycle fexTranLifeCycle) {
        FexCashFlow entity = new FexCashFlow();
        entity.setCashFlowDate(fexTranLifeCycle.getOpDate());
        entity.setSettleDate(fexTranLifeCycle.getSettleDate());
        //现金流类型：0本金，1利息,2-违约金，3-期权费，4-退款
        if (CommonConstant.YES.equals(fexTranReq.getIsRefund())) {
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_4);
        } else {
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        }

        //这里是与实际现金流的区别
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
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
        //获取交易对手账户
        FexCorpBankAccVO cotpSearchVO = new FexCorpBankAccVO();
        entity.setCorpId(fexTranReq.getCorpId());
        entity.setCurId(fexTranLifeCycle.getCurId());
        entity.setPayMoney(fexTranLifeCycle.getMoney().abs());
        //期权退款由卖方支付给买方：买入期权为收入，卖出期权为支出
        entity.setCdSign(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection())
                ? BisAccBalConstant.CDSIGN_IN : BisAccBalConstant.CDSIGN_OUT);

        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(fexTranLifeCycle.getBankAcc());
        entity.setCorpAccId(bankAccInfo.getId());
        entity.setCorpBank(bankAccInfo.getBankTypeId());
        entity.setCorpAccName(bankAccInfo.getAccName());
        entity.setCorpBankAcc(bankAccInfo.getBankAcc());

        if (!ForeignConstant.CASH_FLOW_TYPE_4.equals(entity.getCashFlowType())
                && "1".equals(fexTranReq.getDeliveryType())) {
            entity.setCurId(fexTranLifeCycle.getCurId());
            entity.setPayMoney(fexTranLifeCycle.getMoney().abs());
            if (BigDecimal.ZERO.compareTo(fexTranLifeCycle.getMoney()) > 0) {
                entity.setCdSign(BisAccBalConstant.CDSIGN_OUT);
            } else {
                entity.setCdSign(BisAccBalConstant.CDSIGN_IN);
            }
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



    /**
     * 保存损益
     */
    public void saveManageProfitLoss(FexTranLifeCycle fexTranLifeCycle,
                                     BigDecimal currentPosition, ICurrentUser user){
        FexTranLifeCycleVo fexTranLifeCycleVo = new FexTranLifeCycleVo();
        fexTranLifeCycleVo.setReqId(fexTranLifeCycle.getReqId());
        fexTranLifeCycleVo.setId(fexTranLifeCycle.getId());
        fexTranLifeCycleVo.setBillNo(fexTranLifeCycle.getBillNo());
        fexTranLifeCycleVo.setOpType(fexTranLifeCycle.getOpType());
        fexTranLifeCycleVo.setOpDate(fexTranLifeCycle.getOpDate());
        fexTranLifeCycleVo.setProfitLossMoney(fexTranLifeCycle.getProfitLossMoney());
        fexTranLifeCycleVo.setConvertLossMoney(fexTranLifeCycle.getConvertLossMoney());
        fexProfitLossManageService.saveManageProfitLoss(fexTranLifeCycleVo, currentPosition, user.getLoginName());
    }
}
