package com.bytter.biz.foreign.utils;


import com.bytter.biz.foreign.enums.FexTranTypeEnum;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 外汇交易工具
 */
public class FexTranReqUtil {
    // 查询即期列表状态数据
    public static final List<Integer> SPOT_STATUS = new ArrayList<>();
    public static final List<Integer> FORWARD_STATUS = new ArrayList<>();
    public static final List<Integer> SWAP_STATUS = new ArrayList<>();
    // 自定义字段和实体类字段映射
    public static final Map<String, String> FIELD_MAP = new HashMap<>();
    // 需要忽略的字段
    public static final Set<String> UN_REQUIRED_SET = new HashSet<>();
    // 使用Map缓存映射关系，获取更高效
    private static final Map<String, String> TRAN_TYPE_MAP;



    static {
        // 即期状态列表
        SPOT_STATUS.add(-3);
        SPOT_STATUS.add(NetbankConstant.STATUE_TEMP);
        SPOT_STATUS.add(NetbankConstant.STATUE_BACK);
        SPOT_STATUS.add(NetbankConstant.STATUE_CHECK);
        SPOT_STATUS.add(ForeignConstant.STATUE_EARLY_DELIVERY);
        SPOT_STATUS.add(ForeignConstant.STATUE_RATE_CONFIRM);
        SPOT_STATUS.add(NetbankConstant.STATUE_WAIT_CHECK);
        SPOT_STATUS.add(ForeignConstant.STATUE_DELIVERY);
        SPOT_STATUS.add(ForeignConstant.STATUE_CLOSE_POSITION);
        SPOT_STATUS.add(ForeignConstant.STATUE_TOBE_RATE_CONFIRM);
        SPOT_STATUS.add(ForeignConstant.ONGOING);
        SPOT_STATUS.add(ForeignConstant.STATUE_END);

        // 远期状态列表
        FORWARD_STATUS.add(-3);
        FORWARD_STATUS.add(NetbankConstant.STATUE_TEMP);
        FORWARD_STATUS.add(NetbankConstant.STATUE_BACK);
        FORWARD_STATUS.add(NetbankConstant.STATUE_CHECK);
        FORWARD_STATUS.add(NetbankConstant.STATUE_WAIT_CHECK);
        FORWARD_STATUS.add(ForeignConstant.STATUE_EARLY_DELIVERY);
        FORWARD_STATUS.add(ForeignConstant.STATUE_RATE_CONFIRM);
        FORWARD_STATUS.add(ForeignConstant.STATUE_TOBE_RATE_CONFIRM);
        FORWARD_STATUS.add(ForeignConstant.STATUE_EARLY);
        FORWARD_STATUS.add(ForeignConstant.STATUE_DEFERRED);
        FORWARD_STATUS.add(ForeignConstant.STATUE_DELIVERY);
        FORWARD_STATUS.add(ForeignConstant.STATUE_CLOSE_POSITION);
        FORWARD_STATUS.add(ForeignConstant.ONGOING);
        FORWARD_STATUS.add(ForeignConstant.STATUE_END);

        // 掉期状态列表
        SWAP_STATUS.add(-3);
        SWAP_STATUS.add(NetbankConstant.STATUE_TEMP);
        SWAP_STATUS.add(NetbankConstant.STATUE_BACK);
        SWAP_STATUS.add(NetbankConstant.STATUE_CHECK);
        SWAP_STATUS.add(NetbankConstant.STATUE_WAIT_CHECK);
        SWAP_STATUS.add(ForeignConstant.STATUE_EARLY_DELIVERY);
        SWAP_STATUS.add(ForeignConstant.STATUE_EARLY);
        SWAP_STATUS.add(ForeignConstant.STATUE_DEFERRED);
        SWAP_STATUS.add(ForeignConstant.STATUE_DELIVERY);
        SWAP_STATUS.add(ForeignConstant.STATUE_CLOSE_POSITION);
        SWAP_STATUS.add(ForeignConstant.ONGOING);
        SWAP_STATUS.add(ForeignConstant.STATUE_END);
        SWAP_STATUS.add(ForeignConstant.STATUE_RATE_CONFIRM);
        SWAP_STATUS.add(ForeignConstant.STATUE_TOBE_RATE_CONFIRM);

        TRAN_TYPE_MAP = new HashMap<>(3);
        for (FexTranTypeEnum type : FexTranTypeEnum.values()) {
            TRAN_TYPE_MAP.put(type.getCode(), type.getForward());
        }


        FIELD_MAP.put("cotpAccCurNameDiv", "cotpAccCurId");
        UN_REQUIRED_SET.add("corpInfo");
        UN_REQUIRED_SET.add("cotpInfo");
        UN_REQUIRED_SET.add("dealer");
    }


    /**
     * 根据交易类型返回不同的跳转页面
     * @param tranType
     * @return
     */
    public static String getForwardByTranType(String tranType){
        /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
        String forward = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(tranType)){
            //外汇买卖交易
            forward = "Foreign";
        }else if(ForeignConstant.TRAN_TYPE_2.equals(tranType)){
            //外汇期权产品
            forward = "Option";
        }else if(ForeignConstant.TRAN_TYPE_3.equals(tranType)){
            //资金调拨交易
            forward = "Allocation";
        }else if(ForeignConstant.TRAN_TYPE_4.equals(tranType)){
            //资金收付交易
            forward = "Pay";
        }else if(ForeignConstant.TRAN_TYPE_5.equals(tranType)){
            //暂时未规划
            //外汇买卖交易
            forward = "Allocation";
        }else if(ForeignConstant.TRAN_TYPE_6.equals(tranType)){
            //暂时未规划
        }else{
            //交易组交易
            forward = "Trade";
        }
        return forward;
    }

    /**
     * 根据交易类型返回不同买卖交易的跳转页面
     * @param fexTranType 外汇交易类型
     * @return
     */
    public static String getForeignForwardByTranType(String fexTranType){
        return TRAN_TYPE_MAP.getOrDefault(fexTranType, FexTranTypeEnum.SPOT.getForward());
    }

    public static Integer getReqStatusByOpType(String opType){
        if(ForeignConstant.CYCLE_OP_TYPE_1.equals(opType)){
            return ForeignConstant.STATUE_INVALID;
        }else if(ForeignConstant.CYCLE_OP_TYPE_2.equals(opType)){
            return ForeignConstant.STATUE_ENTERAVG;
        }else if(ForeignConstant.CYCLE_OP_TYPE_3.equals(opType)){
            return ForeignConstant.STATUE_EXERCISE;
        }else if(ForeignConstant.CYCLE_OP_TYPE_4.equals(opType)){
            return ForeignConstant.STATUE_KNOCKOUT;
        }else if(ForeignConstant.CYCLE_OP_TYPE_5.equals(opType)){
            return ForeignConstant.STATUE_KNOCKIN;
        }else if(ForeignConstant.CYCLE_OP_TYPE_6.equals(opType)){
            return ForeignConstant.STATUE_CLOSE_POSITION;
        }else if(ForeignConstant.CYCLE_OP_TYPE_7.equals(opType)){
            return ForeignConstant.STATUE_WAIVER;
        }else if(ForeignConstant.CYCLE_OP_TYPE_8.equals(opType)){
            return ForeignConstant.STATUE_EARLY_DELIVERY;
        }else if(ForeignConstant.CYCLE_OP_TYPE_9.equals(opType)){
            return ForeignConstant.STATUE_EARLY;
        }else if(ForeignConstant.CYCLE_OP_TYPE_10.equals(opType)){
            return ForeignConstant.STATUE_DEFERRED;
        }else if(ForeignConstant.CYCLE_OP_TYPE_11.equals(opType)){
            return ForeignConstant.STATUE_DELIVERY;
        }else if(ForeignConstant.CYCLE_OP_TYPE_12.equals(opType)){
            return ForeignConstant.STATUE_RATE_CONFIRM;
        }
        return CommonConstant.STATUE_CHECK;
    }

    /**
     * 转换近端保存数据转换
     * @param fexTranReq
     * @param user
     * @return
     */
    public static FexTranReqVO converterNearTran(FexTranReqVO fexTranReq, ICurrentUser user){
        FexTranReqVO nearTran = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranReq, nearTran);
        nearTran.setProId(fexTranReq.getProId());
        nearTran.setCotpId(fexTranReq.getCotpId1());
        nearTran.setCotpName(fexTranReq.getCotpName1());
        nearTran.setOppId(fexTranReq.getOppId1());
        nearTran.setFexTranType(fexTranReq.getFexTranType1());
        nearTran.setFexBusType(fexTranReq.getFexBusType1());
        nearTran.setDeliveryType(fexTranReq.getDeliveryType1());
        nearTran.setDeliveryCurId(fexTranReq.getDeliveryCurId1());
        nearTran.setBuyCurId(fexTranReq.getBuyCurId1());
        nearTran.setBuyMoney(fexTranReq.getBuyMoney1());
        nearTran.setSellCurId(fexTranReq.getSellCurId1());
        nearTran.setSellMoney(fexTranReq.getSellMoney1());
        nearTran.setAutoCal(fexTranReq.getAutoCal1());
        nearTran.setSettleBusType(fexTranReq.getSettleBusType1());
        nearTran.setContractRate(fexTranReq.getContractRate1());
        nearTran.setSpotRate(fexTranReq.getSpotRate1());
        nearTran.setForwardPoints(fexTranReq.getForwardPoints1());
        nearTran.setTranCode1(fexTranReq.getTranCode11());
        nearTran.setBankNote(fexTranReq.getBankNote1());
        nearTran.setTranDate(fexTranReq.getTranDate1());
        nearTran.setSettleDate(fexTranReq.getSettleDate1());
        nearTran.setDeliveryDeadline(fexTranReq.getDeliveryDeadline1());
        nearTran.setBoodType(fexTranReq.getBoodType1());
        nearTran.setBoodAccId(fexTranReq.getBoodAccId1());
        nearTran.setBoodAcc(fexTranReq.getBoodAcc1());
        nearTran.setBoodBankNote(fexTranReq.getBoodBankNote1());
        nearTran.setBoodRatio(fexTranReq.getBoodRatio1());
        nearTran.setBondCurId(fexTranReq.getBondCurId1());
        nearTran.setContractNo(fexTranReq.getContractNo1());
        nearTran.setPayMoney(fexTranReq.getPayMoney1());
        nearTran.setBusNo(fexTranReq.getBusNo1());
        nearTran.setPurpose(fexTranReq.getPurpose1());
        nearTran.setPurposeDtl(fexTranReq.getPurposeDtl1());
        nearTran.setFundsSource(fexTranReq.getFundsSource1());
        nearTran.setInvoiceNo(fexTranReq.getInvoiceNo1());
        nearTran.setAccNature(fexTranReq.getAccNature1());
        nearTran.setFexProp(fexTranReq.getFexProp1());
        nearTran.setCorpContact(fexTranReq.getCorpContact1());
        nearTran.setCorpContactTel(fexTranReq.getCorpContactTel1());
        nearTran.setBoodRmk(fexTranReq.getBoodRmk1());
        nearTran.setRmk(fexTranReq.getRmk1());
        nearTran.setRmkLocal(fexTranReq.getRmkLocal1());
        nearTran.setTranAddr(fexTranReq.getTranAddr1());
        nearTran.setTranAddrId(fexTranReq.getTranAddrId1());
        nearTran.setCotpNo(fexTranReq.getCotpNo1());
        nearTran.setTranGroup(fexTranReq.getTranGroup1());
        nearTran.setRemitType(fexTranReq.getRemitType1());
        nearTran.setFixedMoney(fexTranReq.getFixedMoney1());
        nearTran.setCorpAccId(fexTranReq.getCorpAccId1());
        nearTran.setCorpBankAcc(fexTranReq.getCorpBankAcc1());
        nearTran.setCorpAccName(fexTranReq.getCorpAccName1());
        nearTran.setCorpBank(fexTranReq.getCorpBank1());
        nearTran.setCotpAccId(fexTranReq.getCotpAccId1());
        nearTran.setCotpBankAcc(fexTranReq.getCotpBankAcc1());
        nearTran.setCotpAccName(fexTranReq.getCotpAccName1());
        nearTran.setCotpBank(fexTranReq.getCotpBank1());
        nearTran.setSrcAccId(fexTranReq.getSrcAccId1());
        nearTran.setSrcBankAcc(fexTranReq.getSrcBankAcc1());
        nearTran.setSrcAccName(fexTranReq.getSrcAccName1());
        nearTran.setSrcBank(fexTranReq.getSrcBank1());
        nearTran.setTargetAccId(fexTranReq.getTargetAccId1());
        nearTran.setTargetBankAcc(fexTranReq.getTargetBankAcc1());
        nearTran.setTargetAccName(fexTranReq.getTargetAccName1());
        nearTran.setTargetBank(fexTranReq.getTargetBank1());
        nearTran.setBudgetInfo(fexTranReq.getBudget1());
        nearTran.setDifBank(fexTranReq.getDifBank1());
        nearTran.setIsPayed(fexTranReq.getIsPayed1());
        nearTran.setAddWord(fexTranReq.getAddWord1());
        nearTran.setBankPayType(fexTranReq.getBankPayType1());
        nearTran.setForeignContractNo(fexTranReq.getForeignContractNo1());
        nearTran.setBnfRelate(fexTranReq.getBnfRelate1());
        nearTran.setBillFrom(ForeignConstant.BILL_FROM_0);
        nearTran.setCotpBicCode(fexTranReq.getCotpBicCode1());
        nearTran.setIsPaymentAmount(fexTranReq.getIsPaymentAmount1());
        nearTran.setTranRateType(fexTranReq.getTranRateType1());
        nearTran.setQuotationMethod(fexTranReq.getQuotationMethod1());
        nearTran.setPositionMoney(fexTranReq.getPositionMoney1());
        nearTran.setProfitLossCur(fexTranReq.getProfitLossCur1());
        nearTran.setProfitLossMoney(fexTranReq.getProfitLossMoney1());
        nearTran.setConvertCurType(fexTranReq.getConvertCurType1());
        nearTran.setConvertRate(fexTranReq.getConvertRate1());
        nearTran.setConvertMoney(fexTranReq.getConvertMoney1());
        nearTran.setTranCurPairId(fexTranReq.getTranCurPairId1());
        nearTran.setQuotationCurId(fexTranReq.getQuotationCurId1());
        nearTran.setQuotationReferCurId(fexTranReq.getQuotationReferCurId1());
        nearTran.setTranCurPairBaseId(fexTranReq.getTranCurPairBaseId1());
        nearTran.setTranCurPairReferId(fexTranReq.getTranCurPairReferId1());
        nearTran.setBusDirection(fexTranReq.getBusDirection1());
        return nearTran;
    }


    /**
     * 转换远端保存数据转换
     * @param fexTranReq
     * @param user
     * @return
     */
    public static FexTranReqVO converterRemoteTran(FexTranReqVO fexTranReq, ICurrentUser user){
        FexTranReqVO remoteTran = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranReq, remoteTran);
        remoteTran.setProId(fexTranReq.getProId());
        remoteTran.setCotpId(fexTranReq.getCotpId2());
        remoteTran.setCotpName(fexTranReq.getCotpName2());
        remoteTran.setOppId(fexTranReq.getOppId2());
        remoteTran.setFexTranType(fexTranReq.getFexTranType2());
        remoteTran.setFexBusType(fexTranReq.getFexBusType2());
        remoteTran.setDeliveryType(fexTranReq.getDeliveryType2());
        remoteTran.setDeliveryCurId(fexTranReq.getDeliveryCurId2());
        remoteTran.setBuyCurId(fexTranReq.getBuyCurId2());
        remoteTran.setBuyMoney(fexTranReq.getBuyMoney2());
        remoteTran.setSellCurId(fexTranReq.getSellCurId2());
        remoteTran.setSellMoney(fexTranReq.getSellMoney2());
        remoteTran.setAutoCal(fexTranReq.getAutoCal2());
        remoteTran.setSettleBusType(fexTranReq.getSettleBusType2());
        remoteTran.setContractRate(fexTranReq.getContractRate2());
        remoteTran.setSpotRate(fexTranReq.getSpotRate2());
        remoteTran.setForwardPoints(fexTranReq.getForwardPoints2());
        remoteTran.setTranCode1(fexTranReq.getTranCode12());
        remoteTran.setBankNote(fexTranReq.getBankNote2());
        remoteTran.setTranDate(fexTranReq.getTranDate2());
        remoteTran.setSettleDate(fexTranReq.getSettleDate2());
        remoteTran.setDeliveryDeadline(fexTranReq.getDeliveryDeadline2());
        remoteTran.setBoodType(fexTranReq.getBoodType2());
        remoteTran.setBoodAccId(fexTranReq.getBoodAccId2());
        remoteTran.setBoodAcc(fexTranReq.getBoodAcc2());
        remoteTran.setBoodBankNote(fexTranReq.getBoodBankNote2());
        remoteTran.setBoodRatio(fexTranReq.getBoodRatio2());
        remoteTran.setBondCurId(fexTranReq.getBondCurId2());
        remoteTran.setContractNo(fexTranReq.getContractNo2());
        remoteTran.setPayMoney(fexTranReq.getPayMoney2());
        remoteTran.setBusNo(fexTranReq.getBusNo2());
        remoteTran.setPurpose(fexTranReq.getPurpose2());
        remoteTran.setPurposeDtl(fexTranReq.getPurposeDtl2());
        remoteTran.setFundsSource(fexTranReq.getFundsSource2());
        remoteTran.setInvoiceNo(fexTranReq.getInvoiceNo2());
        remoteTran.setAccNature(fexTranReq.getAccNature2());
        remoteTran.setFexProp(fexTranReq.getFexProp2());
        remoteTran.setCorpContact(fexTranReq.getCorpContact2());
        remoteTran.setCorpContactTel(fexTranReq.getCorpContactTel2());
        remoteTran.setBoodRmk(fexTranReq.getBoodRmk2());
        remoteTran.setRmk(fexTranReq.getRmk2());
        remoteTran.setRmkLocal(fexTranReq.getRmkLocal2());
        remoteTran.setTranAddr(fexTranReq.getTranAddr2());
        remoteTran.setTranAddrId(fexTranReq.getTranAddrId2());
        remoteTran.setCotpNo(fexTranReq.getCotpNo2());
        remoteTran.setTranGroup(fexTranReq.getTranGroup2());

        remoteTran.setRemitType(fexTranReq.getRemitType2());
        remoteTran.setFixedMoney(fexTranReq.getFixedMoney2());
        remoteTran.setCorpAccId(fexTranReq.getCorpAccId2());
        remoteTran.setCorpBankAcc(fexTranReq.getCorpBankAcc2());
        remoteTran.setCorpAccName(fexTranReq.getCorpAccName2());
        remoteTran.setCorpBank(fexTranReq.getCorpBank2());
        remoteTran.setCotpAccId(fexTranReq.getCotpAccId2());
        remoteTran.setCotpBankAcc(fexTranReq.getCotpBankAcc2());
        remoteTran.setCotpAccName(fexTranReq.getCotpAccName2());
        remoteTran.setCotpBank(fexTranReq.getCotpBank2());
        remoteTran.setSrcAccId(fexTranReq.getSrcAccId2());
        remoteTran.setSrcBankAcc(fexTranReq.getSrcBankAcc2());
        remoteTran.setSrcAccName(fexTranReq.getSrcAccName2());
        remoteTran.setSrcBank(fexTranReq.getSrcBank2());
        remoteTran.setTargetAccId(fexTranReq.getTargetAccId2());
        remoteTran.setTargetBankAcc(fexTranReq.getTargetBankAcc2());
        remoteTran.setTargetAccName(fexTranReq.getTargetAccName2());
        remoteTran.setTargetBank(fexTranReq.getTargetBank2());
        remoteTran.setBudgetInfo(fexTranReq.getBudget2());
        remoteTran.setDifBank(fexTranReq.getDifBank2());
        remoteTran.setIsPayed(fexTranReq.getIsPayed2());
        remoteTran.setAddWord(fexTranReq.getAddWord2());
        remoteTran.setBankPayType(fexTranReq.getBankPayType2());

        remoteTran.setForeignContractNo(fexTranReq.getForeignContractNo2());
        remoteTran.setCotpBicCode(fexTranReq.getCotpBicCode2());
        remoteTran.setBnfRelate(fexTranReq.getBnfRelate2());
        // 单据来源
        remoteTran.setBillFrom(ForeignConstant.BILL_FROM_2);

        remoteTran.setIsPaymentAmount(fexTranReq.getIsPaymentAmount2());
        remoteTran.setTranRateType(fexTranReq.getTranRateType2());
        remoteTran.setQuotationMethod(fexTranReq.getQuotationMethod2());
        remoteTran.setPositionMoney(fexTranReq.getPositionMoney2());
        remoteTran.setProfitLossCur(fexTranReq.getProfitLossCur2());
        remoteTran.setProfitLossMoney(fexTranReq.getProfitLossMoney2());
        remoteTran.setConvertCurType(fexTranReq.getConvertCurType2());
        remoteTran.setConvertRate(fexTranReq.getConvertRate2());
        remoteTran.setConvertMoney(fexTranReq.getConvertMoney2());
        remoteTran.setTranCurPairId(fexTranReq.getTranCurPairId2());
        remoteTran.setQuotationCurId(fexTranReq.getQuotationCurId2());
        remoteTran.setQuotationReferCurId(fexTranReq.getQuotationReferCurId2());
        remoteTran.setTranCurPairBaseId(fexTranReq.getTranCurPairBaseId2());
        remoteTran.setTranCurPairReferId(fexTranReq.getTranCurPairReferId2());
        remoteTran.setBusDirection(fexTranReq.getBusDirection2());
        return remoteTran;
    }

    /**
     *
     * 设置扩展信息
     *
     * @param fexTranReq
     * @param fexTranReqExtVo
     */
    public static void setFexTranReqExtData(FexTranReqVO fexTranReq, FexTranReqExtVo fexTranReqExtVo) {
        fexTranReq.setIsPaymentAmount(fexTranReqExtVo.getIsPaymentAmount());
        fexTranReq.setTranRateType(fexTranReqExtVo.getTranRateType());
        fexTranReq.setQuotationMethod(fexTranReqExtVo.getQuotationMethod());
        fexTranReq.setPositionMoney(fexTranReqExtVo.getPositionMoney());
        fexTranReq.setProfitLossCur(fexTranReqExtVo.getProfitLossCur());
        fexTranReq.setProfitLossMoney(fexTranReqExtVo.getProfitLossMoney());
        fexTranReq.setConvertCurType(fexTranReqExtVo.getConvertCurType());
        fexTranReq.setConvertRate(fexTranReqExtVo.getConvertRate());
        fexTranReq.setConvertMoney(fexTranReqExtVo.getConvertMoney());
        fexTranReq.setTranCurPairId(fexTranReqExtVo.getTranCurPairId());
        fexTranReq.setQuotationCurId(fexTranReqExtVo.getQuotationCurId());
        fexTranReq.setQuotationReferCurId(fexTranReqExtVo.getQuotationReferCurId());
        fexTranReq.setTranCurPairBaseId(fexTranReqExtVo.getTranCurPairBaseId());
        fexTranReq.setTranCurPairReferId(fexTranReqExtVo.getTranCurPairReferId());
        fexTranReq.setProfitLossCurName(fexTranReqExtVo.getProfitLossCurName());
        fexTranReq.setProfitLossCurEnglishCode(fexTranReqExtVo.getProfitLossCurEnglishCode());
    }



    // 辅助方法：将List<Integer>转换为逗号分隔的字符串
    public static String listToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
