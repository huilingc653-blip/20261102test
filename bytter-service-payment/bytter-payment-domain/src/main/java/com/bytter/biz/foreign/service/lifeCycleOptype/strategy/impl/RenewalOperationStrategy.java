package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCorpBankAccDtlService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
import com.bytter.biz.foreign.service.FexTranReqExtService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.service.lifeCycleOptype.LifeCycleBalanceUtil;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.utils.DeliveryDeadlineCalculatorUtil;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
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
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 展期
 */
@Component("renewal")
public class RenewalOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @Autowired(required = false)
    private IConfigService configService;

    @Autowired
    private IBtBankAccService btBankAccService;

    /**
     * 通过VO处理字段封装为ENTITY
     * @param lifeCycleVo
     * @param user
     * @return
     */
    @Override
    public void dealField(FexTranLifeCycleVo lifeCycleVo, ICurrentUser user, FexTranLifeCycle entity) {
        entity.setOpDate(lifeCycleVo.getOpDate());
        entity.setSettleDate(lifeCycleVo.getSettleDate());
        entity.setDeferredType(lifeCycleVo.getDeferredType());
        entity.setDeferredRate(lifeCycleVo.getDeferredRate());
        // 列表「即期汇率/意向汇率」取 spotRate，展期操作页面录入的是 deferredRate，需同步写入
        entity.setSpotRate(lifeCycleVo.getDeferredRate());
        entity.setIsPaymentAmount(lifeCycleVo.getIsPaymentAmount());
        entity.setBuyMoney(lifeCycleVo.getBuyMoney());
        entity.setSellMoney(lifeCycleVo.getSellMoney());
        entity.setRealBuyMoney(lifeCycleVo.getRealBuyMoney());
        entity.setRealSellMoney(lifeCycleVo.getRealSellMoney());
        entity.setOpMoney(lifeCycleVo.getOpMoney());
        entity.setTranAddr(lifeCycleVo.getTranAddr());
        entity.setTranAddrId(lifeCycleVo.getTranAddrId());
        entity.setRmk(lifeCycleVo.getRmk());
        if ("2".equals(lifeCycleVo.getDeferredType())) {
            entity.setCloseSettleDate(lifeCycleVo.getCloseSettleDate());
            entity.setCloseForwardRate(lifeCycleVo.getCloseForwardRate());
            entity.setCurId(lifeCycleVo.getCurId());
            entity.setMoney(lifeCycleVo.getMoney());
            entity.setBankAcc(lifeCycleVo.getBankAcc());
            entity.setRecordRate(lifeCycleVo.getRecordRate());
            entity.setProfitLossCur(lifeCycleVo.getProfitLossCur());
            entity.setProfitLossMoney(lifeCycleVo.getProfitLossMoney());
            entity.setConvertCurType(lifeCycleVo.getConvertCurType());
            entity.setConvertRate(lifeCycleVo.getConvertRate());
            entity.setConvertLossMoney(lifeCycleVo.getConvertLossMoney());
        } else {
            entity.setCloseSettleDate(null);
            entity.setCloseForwardRate(null);
            entity.setCurId(null);
            entity.setMoney(null);
            entity.setBankAcc(null);
            entity.setRecordRate(null);
            entity.setProfitLossCur(null);
            entity.setProfitLossMoney(null);
            entity.setConvertCurType(null);
            entity.setConvertRate(null);
            entity.setConvertLossMoney(null);
        }
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {
        FexTranReq fexTranReq = fexTranReqService.get(fexTranLifeCycle.getReqId());
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranLifeCycle.getReqId());
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranLifeCycle.getReqId());
        FexTranReqExt fexTranReqExt = fexTranReqExtService.get(fexTranReqExtVo.getId());
        // 交易.持仓金额反写为余额第一行
        BigDecimal positionBalance = LifeCycleBalanceUtil.resolveForeignPositionBalance(fexTranReq, fexTranLifeCycle);
        fexTranReqExt.setPositionMoney(positionBalance);
        fexTranReqExtService.updateEntity(fexTranReqExt);
        // 更新预测现金流
        List<FexCashFlowVO> cashFlowVOS = fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId());
        List<FexCashFlowVO> safeCashFlowVOS = Optional.ofNullable(cashFlowVOS).orElse(Collections.emptyList());
        boolean isBalanceZero = LifeCycleBalanceUtil.isPositionBalanceZero(positionBalance);
        if (isBalanceZero) {
            // 余额为0：删除预测的现金流
            List<String> idsToDelete = safeCashFlowVOS.stream()
                    .filter(vo -> StringUtils.isEmpty(vo.getBillNo()))
                    .map(FexCashFlowVO::getId)
                    .collect(Collectors.toList());

            if (EmptyUtils.isNotEmpty(idsToDelete)) {
                fexCashFlowService.delete(idsToDelete);
            }
            fexTranReq.setStatus(ForeignConstant.STATUE_END);
        } else {
            fexTranReq.setStatus(ForeignConstant.ONGOING);
            // 余额非0：更新预测现金流的金额
            String tranCurPairBaseId = fexTranReqVO.getTranCurPairBaseId();
            BigDecimal positionMoney = fexTranReqExt.getPositionMoney();
            BigDecimal contractRate = fexTranReqVO.getContractRate();
            String tranCurPairIdName = fexTranReqVO.getTranCurPairIdName();
            String quotationMethod = fexTranReqVO.getQuotationMethod();
            // 筛选出需要更新的现金流VO（billNo为空）
            safeCashFlowVOS.stream()
                    .filter(vo -> StringUtils.isEmpty(vo.getBillNo()))
                    .forEach(vo -> {
                        FexCashFlow cashFlow = fexCashFlowService.get(vo.getId());
                        // 根据币种匹配设置支付金额
                        if (cashFlow.getCurId().equals(tranCurPairBaseId)) {
                            cashFlow.setPayMoney(positionMoney);
                        } else {
                            BigDecimal payMoney = tranCurPairIdName.equals(quotationMethod)
                                    ? positionMoney.multiply(contractRate)
                                    : positionMoney.divide(contractRate, 2, RoundingMode.HALF_UP);
                            cashFlow.setPayMoney(payMoney);
                        }
                        fexCashFlowService.updateEntity(cashFlow);
                    });
        }
        // 保存已确认现金流
        if ("2".equals(fexTranLifeCycle.getDeferredType())) {
            saveExpectFexCashFLow(fexTranReqVO, fexTranLifeCycle);
        }

        // 生成一张新的已审批的远期或掉期交易
        FexTranReqVO fexTranReqNew = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranReqVO, fexTranReqNew);
        BeanUtils.copyNotNullProperties(fexTranReq, fexTranReqNew);
        //关联交易号码=原交易号码
        fexTranReqNew.setRelBillNo(fexTranReq.getBillNo());
        fexTranReqNew.setId(null);
        fexTranReqNew.setContractRate(fexTranLifeCycle.getDeferredRate());
        fexTranReqNew.setBuyMoney(fexTranLifeCycle.getRealBuyMoney());
        fexTranReqNew.setSellMoney(fexTranLifeCycle.getRealSellMoney());
        fexTranReqNew.setTimeStamp(String.valueOf(new Date().getTime()));
        fexTranReqNew.setTranDate(fexTranLifeCycle.getOpDate());
        fexTranReqNew.setSettleDate(fexTranLifeCycle.getSettleDate());
        fexTranReqNew.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        // 计算交割期限
        int deliveryDeadline = DeliveryDeadlineCalculatorUtil.calculateDeliveryDeadline(
                DateUtils.formatDate(fexTranLifeCycle.getOpDate()), DateUtils.formatDate(fexTranLifeCycle.getSettleDate()));
        fexTranReqNew.setDeliveryDeadline(String.valueOf(deliveryDeadline));
        if (fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getBuyCurId())) {
            fexTranReqNew.setPositionMoney(fexTranLifeCycle.getBuyMoney());
        } else {
            fexTranReqNew.setPositionMoney(fexTranLifeCycle.getSellMoney());
        }
        fexTranReqService.saveFexTranReq(fexTranReqNew, user);
        // 保存损益
        if ("2".equals(fexTranLifeCycle.getDeferredType())) {
            saveManageProfitLoss(fexTranReqVO, fexTranLifeCycle, positionBalance, user);
        }
        fexTranReqService.updateEntity(fexTranReq);
    }

    /**
     * 预测现金流生成(用于买卖交易，审批通过时生成预测现金流，不推结算执行)与实际现金流的数据区别在于不会生成billcode
     * @param fexTranReq
     */
    public void saveExpectFexCashFLow(FexTranReqVO fexTranReq, FexTranLifeCycle fexTranLifeCycle) {
        FexCashFlow entity = new FexCashFlow();
        entity.setCashFlowDate(fexTranLifeCycle.getOpDate());
        entity.setSettleDate(fexTranLifeCycle.getCloseSettleDate());
        //现金流类型：0本金，1利息,2-违约金，3-期权费，4-退款
        entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);

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

        entity.setCorpId(fexTranReq.getCorpId());
        if (BigDecimal.ZERO.compareTo(fexTranLifeCycle.getMoney()) > 0) {
            entity.setCdSign(BisAccBalConstant.CDSIGN_OUT);
        } else {
            entity.setCdSign(BisAccBalConstant.CDSIGN_IN);
        }
        entity.setCurId(fexTranLifeCycle.getCurId());
        entity.setPayMoney(fexTranLifeCycle.getMoney().abs());
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(fexTranLifeCycle.getBankAcc());
        if (null != bankAccInfo) {
            entity.setCorpAccId(bankAccInfo.getId());
            entity.setCorpBank(bankAccInfo.getBankTypeId());
            entity.setCorpAccName(bankAccInfo.getAccName());
            entity.setCorpBankAcc(bankAccInfo.getBankAcc());
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
        entity.setPayStatus(ForeignConstant.PAY_STATUS_1);
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
    public void saveManageProfitLoss(FexTranReqVO fexTranReq, FexTranLifeCycle fexTranLifeCycle,
                                     BigDecimal currentPosition, ICurrentUser user){
        FexTranLifeCycleVo fexTranLifeCycleVo = new FexTranLifeCycleVo();
        fexTranLifeCycleVo.setReqId(fexTranReq.getId());
        fexTranLifeCycleVo.setId(fexTranLifeCycle.getId());
        fexTranLifeCycleVo.setBillNo(fexTranLifeCycle.getBillNo());
        fexTranLifeCycleVo.setOpType(fexTranLifeCycle.getOpType());
        fexTranLifeCycleVo.setOpDate(fexTranLifeCycle.getOpDate());
        fexTranLifeCycleVo.setProfitLossMoney(fexTranLifeCycle.getProfitLossMoney());
        fexTranLifeCycleVo.setConvertLossMoney(fexTranLifeCycle.getConvertLossMoney());
        fexProfitLossManageService.saveManageProfitLoss(fexTranLifeCycleVo, currentPosition, user.getLoginName());
    }
}
