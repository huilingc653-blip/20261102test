package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexProfitLossCalculateVo;
import com.bytter.biz.foreign.dao.FexProfitLossManageDao;
import com.bytter.biz.foreign.dao.FexProfitLossManageDtlDao;
import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.entity.FexProfitLossManage;
import com.bytter.biz.foreign.entity.FexProfitLossManageDtl;
import com.bytter.biz.foreign.utils.FexForeignConverter;
import com.bytter.biz.foreign.vo.FexProfitLossManageVO;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtCurrencyPairDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyPairService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/9/10<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("fexProfitLossManageService")
@Transactional
public class FexProfitLossManageService extends BaseService<FexProfitLossManage, String> {
    @Autowired
    private FexProfitLossManageDao fexProfitLossManageDao;
    @Autowired
    private FexProfitLossManageDtlDao fexProfitLossManageDtlDao;

    @Autowired(required=false)
    private ICorpService corpService;

    @Autowired(required=false)
    private IConfigService configService;

    @Autowired
    private FexRateInfoService fexRateInfoService;

    @DubboReference(check = false)
    private IBtCurrencyPairService currencyPairService;

    @Autowired
    private IBtCurrencyService btCurrencyService;

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexProductService fexProductService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @Override
    public BaseDao<FexProfitLossManage, String> getEntityDao() {
        return fexProfitLossManageDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,  ICurrentUser user) {
       List<String> corpIds= corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEXPROFITLOSSMANAGE_OP, user.getId());
        DataTablesOutput output = fexProfitLossManageDao.getDataTablesUnion(dataTablesInput, corpIds);
        if (!EmptyUtils.isEmpty(output.getData())) {
            //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            for (Object data : output.getData()) {
                FexProfitLossManageVO fexProfitLossManageVO = (FexProfitLossManageVO)data;
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexProfitLossManageVO.getTranCurPairId());
                fexProfitLossManageVO.setTranCurPairName(btCurrencyPairDTO.getPairCode());
                //外汇期权交易-已实现损益=已实现净损益，折本位币已实现损益=折本位币已实现净损益
                if(ForeignConstant.TRAN_TYPE_2.equals(fexProfitLossManageVO.getTranType())){
                    fexProfitLossManageVO.setFloatMoney(fexProfitLossManageVO.getNetFloatMoney());
                    fexProfitLossManageVO.setConvertFloatMoney(fexProfitLossManageVO.getNetConvertFloatMoney());
                    fexProfitLossManageVO.setRealMoneyTotal(fexProfitLossManageVO.getNetRealMoneyTotal());
                    fexProfitLossManageVO.setConvertRealMoneyTotal(fexProfitLossManageVO.getNetConvertRealMoneyTotal());
                }
            }
        }
        return output;
    }

    public FexProfitLossManageVO getByReqId(String reqId) {
        FexProfitLossManageVO profitLossManageVO = fexProfitLossManageDao.getByReqId(reqId);
        if (null != profitLossManageVO) {
            // 设置货币对
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(profitLossManageVO.getTranCurPairId());
            if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                profitLossManageVO.setTranCurPairName(btCurrencyPairDTO.getPairCode());
            }
        }
        return profitLossManageVO;
    }

    /**
     * 外汇交易损益生成
     * @param fexTranReqVO
    * @param loginName
     */
    public void saveManageProfitLoss(FexTranReqVO fexTranReqVO, String loginName) {
        // 交易.持仓金额反写为余额第一行
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())){
            if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReqVO.getBusDirection())) {
                fexTranReqVO.setPositionMoney(fexTranReqVO.getBuyMoney());
            } else if (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())) {
                fexTranReqVO.setPositionMoney(fexTranReqVO.getSellMoney());
            }
        } else {
            if ((ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReqVO.getBusDirection()) && ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())) ||
                    (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection()) && ForeignConstant.FEX_TRAN_DIRECTION_DOWN.equals(fexTranReqVO.getUpdownDirection()))) {
                fexTranReqVO.setPositionMoney(fexTranReqVO.getBuyMoney());
            } else {
                fexTranReqVO.setPositionMoney(fexTranReqVO.getSellMoney());
            }
        }
        if (null == fexTranReqVO.getPositionMoney()) {
            fexTranReqVO.setPositionMoney(BigDecimal.ZERO);
        }
        FexProfitLossManage fexProfitLossManage = FexForeignConverter.converterProfitLoss(fexTranReqVO);
        //交易的默认外汇牌价类型
        String defValuationRateType = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFTRANRATETYPE).getValue();
        //默认损益币种
        String defProfitLossCur = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFPROFITANDLOSSCUR).getValue();
        //估值汇率取
        String valuationExchangeRate = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_VALUATIONEXCHANGERATE).getValue();
        String profitLossCur = "";
        if(defProfitLossCur.equals(ForeignConstant.FEX_CONFIG_PROFITLOSSCURTYPE_BASE)){
            //标准币种
            profitLossCur = fexTranReqVO.getTranCurPairBaseId();
        }else{
            //报价币种
            profitLossCur = fexTranReqVO.getTranCurPairReferId();
        }
        //浮动损益
        BigDecimal floatMoney = BigDecimal.ZERO;
        //折本位币浮动损益
        BigDecimal convertFloatMoney = BigDecimal.ZERO;
        BigDecimal referenceRate = BigDecimal.ZERO;
        //获取参考汇率
        FexRateInfoVO lastFexRateForSP = null;
        //外汇交易类型 0-即期、1-远期
        String fexTranType = fexTranReqVO.getFexTranType();
        String tranType = fexTranReqVO.getTranType();
        //报价时间
        Date returnTime = null;
        //报价方式
        String quotationMethodCode = "";
        String quotationCurId = "";
        String quotationReferCurId = "";
        String convertCurId = "";
        String convertCurReferId = "";
        //校验交易的默认外汇牌价类型是否设置
        if(StringUtils.isNotBlank(defValuationRateType)){
            String type = "";
            //估值汇率取中间价
            if(valuationExchangeRate.equals("1")){
                type = "3";
            }else {
                //参考汇率
                if(fexTranReqVO.getBuyCurId().equals(fexTranReqVO.getQuotationCurId())){
                    //买入币种=报价方式的左侧
                    type = "1";
                }else{
                    //买入币种=报价方式的右侧
                    type = "2";
                }
            }
            //获取远期汇率
            lastFexRateForSP = fexRateInfoService.getRateForEnd(defValuationRateType, fexTranReqVO.getBuyCurId(), fexTranReqVO.getSellCurId(), fexTranReqVO.getSettleDate(), type);
            if(null != lastFexRateForSP){
                referenceRate = lastFexRateForSP.getReferenceRate();
                //校验报价时间是否为空
                if(null != lastFexRateForSP.getReturnTime()){
                    returnTime = lastFexRateForSP.getReturnTime();
                } else if(null != lastFexRateForSP.getEffectDate()){
                    returnTime = lastFexRateForSP.getEffectDate();
                }
                //校验报价币种是否为空
                if(StringUtils.isNotBlank(lastFexRateForSP.getCurEnCode()) && StringUtils.isNotBlank(lastFexRateForSP.getReferCurEnCode())){
                    quotationMethodCode = lastFexRateForSP.getCurEnCode() + "/" + lastFexRateForSP.getReferCurEnCode();
                    quotationCurId = lastFexRateForSP.getCur();
                    quotationReferCurId = lastFexRateForSP.getReferCur();
                }
            }

        }
        //校验参考汇率是否为空
        if(null != lastFexRateForSP){
            if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())) {
                //校验参考汇率是否为空
                if(null != referenceRate && ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqVO.getTranRateType())){
                    //交易货币对=报价方式
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        floatMoney = referenceRate.subtract(fexTranReqVO.getContractRate()).multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }else{
                        BigDecimal one = BigDecimal.ONE;
                        BigDecimal exchangeRate1 = BigDecimal.ZERO;
                        BigDecimal exchangeRate2 = BigDecimal.ZERO;
                        BigDecimal exchangeRate3 = BigDecimal.ZERO;
                        exchangeRate1 = one.divide(referenceRate,6, RoundingMode.HALF_UP);
                        exchangeRate2 = one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP);
                        exchangeRate3 = exchangeRate1.subtract(exchangeRate2);
                        //交易货币对≠报价方式
                        floatMoney = exchangeRate3.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }
                }
            }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
                //外汇期权交易
                Boolean conditionalBoolean = false;
                //外汇期权品种不为空
                if(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_2.equals(fexTranReqVO.getFexOption2()) || ForeignConstant.FEX_OPTION2_3.equals(fexTranReqVO.getFexOption2())){
                    //交易货币对=报价方式
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        if(null != fexTranReqVO.getFexOption3()){
                            //障碍类型 Down&In  && 参考汇率<=障碍值
                            if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                                //障碍类型 Down&Out  && 参考汇率>障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                                //障碍类型 Up&In  && 参考汇率>障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                                //障碍类型 Up&Out  && 参考汇率<=障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && isDoubleInEffective(referenceRate, fexTranReqVO.getExceptLowVal(), fexTranReqVO.getExceptHignVal())){
                                //障碍类型 Double In  && 参考汇率<低障碍值或参考汇率>高障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                                //障碍类型 Double Out  低障碍值<=参考汇率<=高障碍值
                                conditionalBoolean = true;
                            }
                        }
                    }else{
                        //交易货币对≠报价方式
                        //障碍类型 Down&In  && 1/参考汇率<=1/障碍值（参考汇率>=障碍值）
                        if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                            //障碍类型 Down&Out  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                            //障碍类型 Up&In  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                            //障碍类型 Up&Out  && 1/参考汇率<=1/障碍值（参考>=障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) < 0 || referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) > 0)){
                            //障碍类型 Double In  && 1/参考汇率<1/高障碍值 or 1/参考汇率>1/低障碍值（参考汇率<低障碍值or参考汇率>高障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                            //障碍类型 Double Out  1/高障碍值<=1/参考汇率值<=1/低障碍值（低障碍值<=参考汇率<=高障碍值）
                            conditionalBoolean = true;
                        }
                    }
                }else if((null != fexTranReqVO.getFexOption1() && ForeignConstant.FEX_OPTION1_0.equals(fexTranReqVO.getFexOption1()))
                        ||(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_1.equals(fexTranReqVO.getFexOption2()))){
                    //外汇期权品种=普通期权 or 亚式期权
                    conditionalBoolean = true;
                }
                //校验条件是否满足
                if(conditionalBoolean){
                    //计算浮动损益
                    //交易货币对=报价方式
                    BigDecimal exchangeRate = BigDecimal.ZERO;
                    BigDecimal exchangeRate1 = BigDecimal.ZERO;
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        //涨跌方向
                        if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                            //看涨
                            exchangeRate = calculateCallPayoff(referenceRate, fexTranReqVO.getContractRate());
                        }else{
                            //看跌
                            exchangeRate = calculateCallPayoff(fexTranReqVO.getContractRate(), referenceRate);
                        }
                        floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }else{
                        //交易货币对≠报价方式
                        BigDecimal one = BigDecimal.ONE;
                        exchangeRate1 = one.divide(referenceRate,6, RoundingMode.HALF_UP);
                        BigDecimal contractRate = one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP);
                        //涨跌方向
                        if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                            //看涨
                            exchangeRate = calculateCallPayoff(exchangeRate1, contractRate);
                        }else{
                            //看跌
                            exchangeRate = calculateCallPayoff(contractRate, exchangeRate1);
                        }
                        floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }
                }
            }
        }

        if(floatMoney.compareTo(BigDecimal.ZERO) != 0 && profitLossCur.equals(fexTranReqVO.getTranCurPairBaseId())){
            //交易货币对 = 报价方式
            if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                floatMoney = floatMoney.divide(referenceRate, 2, RoundingMode.HALF_UP);
            }else{
                floatMoney = floatMoney.multiply(referenceRate).setScale(2, RoundingMode.HALF_UP);
            }
        }
        //单位信息
        CorpDTO corpDTO = corpService.get(fexTranReqVO.getCorpId());
        //折本位币汇率
        BigDecimal convertRate = BigDecimal.ZERO;
        //判断损益币种是否等于单位币种
        if(!corpDTO.getCurId().equals(profitLossCur)){
            //折本位币报价方式
            FexRateInfoVO fexRateInfoVO = fexRateInfoService.getQuoMethodForSP(defValuationRateType, profitLossCur, corpDTO.getCurId());
            if(null != fexRateInfoVO){
                if(profitLossCur.equals(fexRateInfoVO.getCur())){
                    convertRate = fexRateInfoVO.getPurchaseRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.multiply(convertRate)
                            .setScale(2, RoundingMode.HALF_UP);
                }else{
                    convertRate = fexRateInfoVO.getSellRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.divide(convertRate, 2, RoundingMode.HALF_UP);
                }
                fexProfitLossManage.setConvertRate(convertRate);//折本位币汇率
                fexProfitLossManage.setConvertDate(fexRateInfoVO.getReturnTime());//折本位币时间
                fexProfitLossManage.setConvertCurEnCode(fexRateInfoVO.getCurEnCode() + "/" + fexRateInfoVO.getReferCurEnCode());//折本位币币种
                fexProfitLossManage.setConvertCurId(fexRateInfoVO.getCur());
                fexProfitLossManage.setConvertCurReferId(fexRateInfoVO.getReferCur());

            }
        }else{
            //损益币种=单位的本位币
            convertFloatMoney = floatMoney;
        }

        if(fexTranReqVO.getTranType().equals(ForeignConstant.TRAN_TYPE_2)){
            FexProfitLossManageVO fexProfitLossManageVO = new FexProfitLossManageVO();
            fexProfitLossManageVO.setCurId(profitLossCur);
            fexProfitLossManageVO.setQuotationCurId(quotationCurId);
            Map<String,BigDecimal> result = this.calculatePremiumMoney(fexProfitLossManageVO,fexTranReqVO);
            //持仓归0（合约结束）时净浮动损益固定为0：期权费已计入已实现净损益，浮动侧不再重复计入
            fexProfitLossManage.setFloatMoneyTotal(resolveNetFloatMoney(floatMoney,
                    result.get("premiumMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));//
            fexProfitLossManage.setConvertFloatMoneyTotal(resolveNetFloatMoney(convertFloatMoney,
                    result.get("convertMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));
            //新建交易时已实现损益合计为0，净已实现损益即为±期权费，一并初始化避免查看页、列表页为空
            fexProfitLossManage.setCurId(profitLossCur);//损益币种
            fexProfitLossManage.setQuotationCurId(quotationCurId);
            Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(fexProfitLossManage, fexTranReqVO);
            fexProfitLossManage.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
            fexProfitLossManage.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
            fexProfitLossManage.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
            fexProfitLossManage.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
        }

        String fexProfitLossManageTranType = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())){
            if(StringUtils.isNotBlank(fexTranReqVO.getIsSwapTran()) && "1".equals(fexTranReqVO.getIsSwapTran())){
                fexProfitLossManageTranType = ForeignConstant.TRAN_TYPE_9;
            }else{
                if(ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReqVO.getFexTranType())){
                    fexProfitLossManageTranType = ForeignConstant.TRAN_TYPE_7;


                }else if(ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReqVO.getFexTranType())){
                    fexProfitLossManageTranType = ForeignConstant.TRAN_TYPE_8;

                }
            }
        }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
            fexProfitLossManageTranType = ForeignConstant.TRAN_TYPE_2;
        }
        fexProfitLossManage.setTranType(fexProfitLossManageTranType);
        fexProfitLossManage.setReferenceInterestRate(referenceRate);//参考利率
        fexProfitLossManage.setQuotationDate(returnTime);//报价时间
        fexProfitLossManage.setQuotationMethod(quotationMethodCode);//报价方式
        fexProfitLossManage.setQuotationCurId(quotationCurId);
        fexProfitLossManage.setQuotationReferCurId(quotationReferCurId);
        fexProfitLossManage.setCurId(profitLossCur);//损益币种
        fexProfitLossManage.setFloatMoney(floatMoney);//浮动损益
        fexProfitLossManage.setConvertFloatMoney(convertFloatMoney);//折本位币浮动损益
        fexProfitLossManage.setCreateBy(loginName);//创建人
        fexProfitLossManage.setCreateDate(new Date());//创建时间
        this.saveEntity(fexProfitLossManage);
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        // 如果 a 大于 b，返回 a；否则返回 b
        return (a.compareTo(b) > 0) ? a : b;
    }

    public static BigDecimal calculateCallPayoff(BigDecimal spotRate, BigDecimal strikeRate) {
        // 1. 进行减法：汇率1 - 汇率2
        BigDecimal difference = spotRate.subtract(strikeRate);

        // 2. 定义零的 BigDecimal 对象
        BigDecimal zero = BigDecimal.ZERO;

        // 3. 取 difference 和 zero 的最大值
        return max(difference, zero);
    }

    /**
     *
     * 计算损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    public Map<String, BigDecimal> calculateFexProfitLoss(FexProfitLossCalculateVo profitLossCalculateVo) {
        Map<String, BigDecimal> calculateResult = new HashMap<>();
        BigDecimal rate = new BigDecimal(1);
        // 即期汇率
        BigDecimal spotRateTemp = profitLossCalculateVo.getSpotRate();
        // 计算损益汇率
        BigDecimal calculateRate = null;
        // 货币对左侧金额
        BigDecimal baseProfitLossMoney = null;
        // 损益
        BigDecimal profitLossMoney = BigDecimal.ZERO;
        // 折本位币损益
        BigDecimal convertLossMoney = BigDecimal.ZERO;

        FexRateInfoVO fexRateForSP = fexRateInfoService.getLastFexRateForSP(profitLossCalculateVo.getRateTypeId(),
                profitLossCalculateVo.getTranCurPairBaseId(), profitLossCalculateVo.getQuotationReferCurId());
        if (null != fexRateForSP) {
            calculateResult.put("spotRate", fexRateForSP.getPurchaseRate());
            if (EmptyUtils.isEmpty(profitLossCalculateVo.getSpotRate())) {
                profitLossCalculateVo.setSpotRate(fexRateForSP.getPurchaseRate());
            }
        }
        if (profitLossCalculateVo.getTranCurPairBaseId().equals(profitLossCalculateVo.getBuyCurId())) {
            baseProfitLossMoney = profitLossCalculateVo.getBuyMoney();
        } else {
            baseProfitLossMoney = profitLossCalculateVo.getSellMoney();
        }

        if (EmptyUtils.isEmpty(profitLossCalculateVo.getSpotRate()) || EmptyUtils.isEmpty(profitLossCalculateVo.getContractRate())
                || EmptyUtils.isEmpty(baseProfitLossMoney) || 0 == BigDecimal.ZERO.compareTo(baseProfitLossMoney)) {
            calculateResult.put("profitLossMoney", profitLossMoney);
            calculateResult.put("convertLossMoney", convertLossMoney);
            return calculateResult;
        }

        // 交易货币对!=报价方式
        if(!profitLossCalculateVo.getTranCurPairBaseId().equals(profitLossCalculateVo.getQuotationCurId())
                || !profitLossCalculateVo.getTranCurPairReferId().equals(profitLossCalculateVo.getQuotationReferCurId())) {
            profitLossCalculateVo.setSpotRate(rate.divide(profitLossCalculateVo.getSpotRate(),6, BigDecimal.ROUND_HALF_UP));
            profitLossCalculateVo.setContractRate(rate.divide(profitLossCalculateVo.getContractRate(),6, BigDecimal.ROUND_HALF_UP));
        }
        if (ForeignConstant.TRAN_TYPE_2.equals(profitLossCalculateVo.getTranType())) {
            if (ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(profitLossCalculateVo.getUpdownDirection())) {
                calculateRate = calculateCallPayoff(profitLossCalculateVo.getSpotRate(), profitLossCalculateVo.getContractRate());
            } else {
                calculateRate = calculateCallPayoff(profitLossCalculateVo.getContractRate(), profitLossCalculateVo.getSpotRate());
            }
            if (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(profitLossCalculateVo.getBusDirection())) {
                calculateRate = calculateRate.negate();
            }
        } else {
            // 买入
            if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(profitLossCalculateVo.getBusDirection())) {
                calculateRate = profitLossCalculateVo.getSpotRate().subtract(profitLossCalculateVo.getContractRate());
            } else {
                calculateRate = profitLossCalculateVo.getSpotRate().subtract(profitLossCalculateVo.getContractRate()).negate();
            }
        }

        profitLossMoney = baseProfitLossMoney.multiply(calculateRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        // 损益币种=交易货币对的左侧(损益=损益/*即期汇率)
        if (profitLossCalculateVo.getProfitLossCur().equals(profitLossCalculateVo.getTranCurPairBaseId())) {
            // 交易货币对=报价方式，取“/”号
            if(profitLossCalculateVo.getTranCurPairBaseId().equals(profitLossCalculateVo.getQuotationCurId())
                    && profitLossCalculateVo.getTranCurPairReferId().equals(profitLossCalculateVo.getQuotationReferCurId())) {
                profitLossMoney = profitLossMoney.divide(spotRateTemp, 2, BigDecimal.ROUND_HALF_UP);
            } else { // 交易货币对≠报价方式，取“*”号
                profitLossMoney = profitLossMoney.multiply(spotRateTemp);
            }
        }
        calculateResult.put("profitLossMoney", profitLossMoney);


        if (StringUtils.isNotEmpty(profitLossCalculateVo.getRateTypeId())) {
            // 损益
            profitLossCalculateVo.setProfitLossMoney(profitLossMoney);
            convertLossMoney = calculateFexLifeCycleConvertLossMoney(profitLossCalculateVo);
            calculateResult.put("convertRate", profitLossCalculateVo.getConvertRate());
            calculateResult.put("convertLossMoney", convertLossMoney);
        }

        return calculateResult;
    }

    /**
     *
     * 计算生命周期损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    public Map<String, BigDecimal> calculateFexLifeCycleProfitLoss(FexProfitLossCalculateVo profitLossCalculateVo) {
        // 损益
        BigDecimal profitLossMoney = null;
        // 折本位币损益
        BigDecimal convertLossMoney = null;
        if (profitLossCalculateVo.getSettleCur().equals(profitLossCalculateVo.getProfitLossCur())) {
            profitLossMoney = profitLossCalculateVo.getSettleMoney();
        } else if (profitLossCalculateVo.getSettleCur().equals(profitLossCalculateVo.getQuotationCurId())) {
            profitLossMoney = profitLossCalculateVo.getSettleMoney().multiply(profitLossCalculateVo.getRecordRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            profitLossMoney = profitLossCalculateVo.getSettleMoney().divide(profitLossCalculateVo.getRecordRate(),2, BigDecimal.ROUND_HALF_UP);
        }
        profitLossCalculateVo.setProfitLossMoney(profitLossMoney);

        Map<String, BigDecimal> calculateResult = new HashMap<>();
        calculateResult.put("profitLossMoney", profitLossMoney);
        // 折本位币损益
        convertLossMoney = calculateFexLifeCycleConvertLossMoney(profitLossCalculateVo);
        if (!EmptyUtils.isEmpty(convertLossMoney)) {
            calculateResult.put("convertLossMoney", convertLossMoney);
        }
        return calculateResult;
    }

    /**
     *
     * 计算折本位币损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    public BigDecimal calculateFexLifeCycleConvertLossMoney(FexProfitLossCalculateVo profitLossCalculateVo) {
        BigDecimal convertLossMoney = null;
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyEnglishCodeMap();
        CorpDTO corpDTO=corpService.get(profitLossCalculateVo.getCorpId());
        // 损益币种等于本位币无需查询
        if (corpDTO.getCurId().equals(profitLossCalculateVo.getProfitLossCur())) {
            return profitLossCalculateVo.getProfitLossMoney();
        }
        FexRateInfoVO lastFexRateForSP = fexRateInfoService.getLastFexRateForSP(profitLossCalculateVo.getRateTypeId(),
                corpDTO.getCurId(), profitLossCalculateVo.getProfitLossCur());
        profitLossCalculateVo.setConvertCurType(lastFexRateForSP.getCurEnCode()+"/"+lastFexRateForSP.getReferCurEnCode());
        if (EmptyUtils.isEmpty(profitLossCalculateVo.getConvertRate())) {
            // 折本位币汇率
            if (profitLossCalculateVo.getProfitLossCur().equals(lastFexRateForSP.getCur())) {
                // 损益币种=折本位币报价方式的左侧：取买入价
                profitLossCalculateVo.setConvertRate(lastFexRateForSP.getPurchaseRate());
            } else {
                profitLossCalculateVo.setConvertRate(lastFexRateForSP.getSellRate());
            }
        }
        // 折本位币报价方式的左侧
        String quotationCurId = currencyMap.get(lastFexRateForSP.getCurEnCode()).getId();
        if (profitLossCalculateVo.getProfitLossCur().equals(quotationCurId)) { // 损益币种=折本位币报价方式的左侧
            // 折本位币损益=损益（损益币种）*折本位币汇率
            convertLossMoney = profitLossCalculateVo.getProfitLossMoney().multiply(profitLossCalculateVo.getConvertRate());
        } else {
            convertLossMoney = profitLossCalculateVo.getProfitLossMoney().divide(profitLossCalculateVo.getConvertRate(),2, BigDecimal.ROUND_HALF_UP);
        }
        return convertLossMoney;
    }

    /**
     *
     * 计算交割损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    public BigDecimal calculateFexLifeCycleDeliveryLossMoney(FexProfitLossCalculateVo profitLossCalculateVo) {
        BigDecimal deliveryLossMoney = null;
        BigDecimal deliveryRate = BigDecimal.ZERO;
        Boolean isSameCurPairAndQuotation = Boolean.TRUE;
        BigDecimal spotRate = profitLossCalculateVo.getSpotRate();
        BigDecimal earlySettlementRate = profitLossCalculateVo.getEarlySettlementRate();
        BigDecimal buyMoney = profitLossCalculateVo.getBuyMoney();
        BigDecimal sellMoney = profitLossCalculateVo.getSellMoney();
        String tranCurPairBaseId = profitLossCalculateVo.getTranCurPairBaseId();
        // 交易货币对!=报价方式
        if(!profitLossCalculateVo.getTranCurPairBaseId().equals(profitLossCalculateVo.getQuotationCurId())
                || !profitLossCalculateVo.getTranCurPairReferId().equals(profitLossCalculateVo.getQuotationReferCurId())) {
            isSameCurPairAndQuotation = Boolean.FALSE;
        }

        // 是否买入
        boolean isBuyDirection = ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(profitLossCalculateVo.getBusDirection());

        // 计算结算汇率和基础金额
        if (isSameCurPairAndQuotation) {
            if (isBuyDirection) {
                deliveryRate = spotRate.subtract(earlySettlementRate);
                deliveryLossMoney = (buyMoney != null) ? buyMoney : BigDecimal.ZERO;
            } else {
                deliveryRate = spotRate.subtract(earlySettlementRate).negate();
                deliveryLossMoney = (sellMoney != null) ? sellMoney : BigDecimal.ZERO;
            }
        } else {
            // 除法运算指定精度和舍入模式
            BigDecimal oneOverRate = BigDecimal.ONE.divide(spotRate, 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal oneOverContractRate = BigDecimal.ONE.divide(earlySettlementRate, 6, BigDecimal.ROUND_HALF_UP);
            if (isBuyDirection) {
                deliveryRate = oneOverRate.add(oneOverContractRate);
                deliveryLossMoney = (buyMoney != null) ? buyMoney : BigDecimal.ZERO;
            } else {
                deliveryRate = oneOverRate.subtract(oneOverContractRate);
                deliveryLossMoney = (sellMoney != null) ? sellMoney : BigDecimal.ZERO;
            }
        }

        // 计算初步交割损益
        deliveryLossMoney = deliveryLossMoney.multiply(deliveryRate);

        if (profitLossCalculateVo.getProfitLossCur().equals(tranCurPairBaseId)) {
            if (isSameCurPairAndQuotation) {
                deliveryLossMoney = deliveryLossMoney.divide(spotRate, 2, BigDecimal.ROUND_HALF_UP);
            } else {
                deliveryLossMoney = deliveryLossMoney.multiply(spotRate);
            }
        }
        return deliveryLossMoney;
    }

    /**
     * 生命周期损益生成
     * @param fexTranLifeCycleVo 生命周期VO
     * @param loginName 用户名称
     */
    public void saveManageProfitLoss(FexTranLifeCycleVo fexTranLifeCycleVo, String loginName) {
        saveManageProfitLoss(fexTranLifeCycleVo, null, loginName);
    }

    /**
     * 生命周期损益生成，优先使用生命周期本次计算出的最新持仓，避免事务内读取到尚未刷新的旧持仓。
     *
     * @param fexTranLifeCycleVo 生命周期VO
     * @param currentPosition 生命周期处理后的最新持仓
     * @param loginName 用户名称
     */
    public void saveManageProfitLoss(FexTranLifeCycleVo fexTranLifeCycleVo,
                                     BigDecimal currentPosition,
                                     String loginName) {
        // 折本位币损益（如无，取损益）
        if (EmptyUtils.isEmpty(fexTranLifeCycleVo.getConvertLossMoney())) {
            fexTranLifeCycleVo.setConvertLossMoney(fexTranLifeCycleVo.getProfitLossMoney());
        }
        FexProfitLossManageDtl fexProfitLossManageDtl = new FexProfitLossManageDtl();

        FexProfitLossManage entity = fexProfitLossManageDao.getFexProfitLossManageByReqId(fexTranLifeCycleVo.getReqId());
        BigDecimal realMoneyTotal = BigDecimal.ZERO;
        BigDecimal convertRealMoneyTotal = BigDecimal.ZERO;
        BigDecimal profitLossMoney = BigDecimal.ZERO;
        BigDecimal convertLossMoney = BigDecimal.ZERO;
        if(null != entity){
            fexProfitLossManageDtl.setFexId(entity.getId());//损益主表id
            if(null != entity.getRealMoneyTotal()){
                realMoneyTotal = entity.getRealMoneyTotal();
            }
            if(null != entity.getConvertRealMoneyTotal()){
                convertRealMoneyTotal = entity.getConvertRealMoneyTotal();
            }
            if(null != fexTranLifeCycleVo.getProfitLossMoney()){
                profitLossMoney = fexTranLifeCycleVo.getProfitLossMoney();
            }
            if(null != fexTranLifeCycleVo.getConvertLossMoney()){
                convertLossMoney = fexTranLifeCycleVo.getConvertLossMoney();
            }
            realMoneyTotal = realMoneyTotal.add(profitLossMoney);
            convertRealMoneyTotal = convertRealMoneyTotal.add(convertLossMoney);

            entity.setRealMoneyTotal(realMoneyTotal);
            entity.setConvertRealMoneyTotal(convertRealMoneyTotal);
            FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranLifeCycleVo.getReqId());
            if (currentPosition != null) {
                fexTranReqVO.setPositionMoney(currentPosition);
            }
            if(fexTranReqVO.getTranType().equals(ForeignConstant.TRAN_TYPE_2)){
                //期权交易的净已实现损益=合计（已实现损益）±期权费，统一走同一口径计算
                Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(entity, fexTranReqVO);
                entity.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
                entity.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
                entity.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
                entity.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
            }
            // 浮动损益=±（参考汇率-合同汇率）*持仓金额，生命周期操作后持仓金额已减少，
            // 合约结束（持仓金额=0）时浮动损益需归0，因此按最新持仓金额与参考汇率重算
            FexProfitLossManageVO floatCalc = calculateManageProfitLoss(fexTranReqVO, entity, loginName);
            if(null != floatCalc.getQuotationDate()){
                entity.setReferenceInterestRate(floatCalc.getReferenceInterestRate());//参考汇率
                entity.setQuotationDate(floatCalc.getQuotationDate());//报价时间
                if(StringUtils.isNotBlank(floatCalc.getQuotationMethod())){
                    entity.setQuotationMethod(floatCalc.getQuotationMethod());//报价方式
                }
                if(null != floatCalc.getConvertRate()){
                    entity.setConvertRate(floatCalc.getConvertRate());//折本位币汇率
                    entity.setConvertDate(floatCalc.getConvertDate());//折本位币报价时间
                    entity.setConvertCurEnCode(floatCalc.getConvertCurEnCode());//折本位币报价方式
                    entity.setConvertCurId(floatCalc.getConvertCurId());
                    entity.setConvertCurReferId(floatCalc.getConvertCurReferId());
                }
            }
            // 合约是否已结束（持仓金额=0）与报价时间无关：只要有报价时间就刷新汇率元数据，
            // 但持仓归0后浮动损益与净浮动损益一律归0，避免残留历史值；
            // 期权交易的期权费已在已实现净损益中体现，浮动侧不再重复计入
            if(isPositionClosed(fexTranReqVO.getPositionMoney())){
                entity.setFloatMoney(BigDecimal.ZERO);
                entity.setConvertFloatMoney(BigDecimal.ZERO);
                if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
                    entity.setFloatMoneyTotal(BigDecimal.ZERO);
                    entity.setConvertFloatMoneyTotal(BigDecimal.ZERO);
                }
            }else{
                entity.setFloatMoney(floatCalc.getFloatMoney());//浮动损益
                entity.setConvertFloatMoney(floatCalc.getConvertFloatMoney());//折本位币浮动损益
                if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
                    entity.setFloatMoneyTotal(floatCalc.getNetFloatMoney());
                    entity.setConvertFloatMoneyTotal(floatCalc.getNetConvertFloatMoney());
                }
            }

            entity.setUpdateBy(loginName);
            entity.setUpdateDate(DateUtils.getDate());
            fexProfitLossManageDao.update(entity);
        }
        fexProfitLossManageDtl.setReqId(fexTranLifeCycleVo.getReqId());//交易id
        fexProfitLossManageDtl.setOpType(fexTranLifeCycleVo.getOpType());//操作类型
        fexProfitLossManageDtl.setLifeCycleId(fexTranLifeCycleVo.getId());//生命周期id
        fexProfitLossManageDtl.setLifeCycleBillNo(fexTranLifeCycleVo.getBillNo());//生命周期单号
        fexProfitLossManageDtl.setMoney(fexTranLifeCycleVo.getProfitLossMoney());//已实现损益
        fexProfitLossManageDtl.setConvertMoney(fexTranLifeCycleVo.getConvertLossMoney());//本位币已实现损益
        fexProfitLossManageDtl.setDeliverlyDate(fexTranLifeCycleVo.getOpDate());//操作日期
        fexProfitLossManageDtl.setCreateDate(DateUtils.getDate());//创建时间
        fexProfitLossManageDtl.setCreateBy(loginName);//创建人
        fexProfitLossManageDtlDao.save(fexProfitLossManageDtl);



    }

    /**
     * 根据交易id获取损益集合
     * @param reqId
     * @return
     */
    public List<FexProfitLossManageVO> listFexProfitLossManage(String reqId) {
        return fexProfitLossManageDao.listFexProfitLossManage(reqId);
    }

    /**
     * 历史数据报价时间为空时，按估值汇率回填并落库，供编辑/查看页展示
     */
    public void fillMissingQuotationDate(FexProfitLossManageVO vo, String loginName) {
        if (vo == null || vo.getQuotationDate() != null || StringUtils.isBlank(vo.getId())) {
            return;
        }
        FexProfitLossManage entity = this.get(vo.getId());
        if (entity == null || entity.getQuotationDate() != null) {
            if (entity != null && entity.getQuotationDate() != null) {
                vo.setQuotationDate(entity.getQuotationDate());
            }
            return;
        }
        FexTranReqVO fexTranReqVO = fexTranReqService.getNewByIdUnion(entity.getReqId());
        if (fexTranReqVO == null) {
            return;
        }
        FexProfitLossManageVO calc = calculateManageProfitLoss(fexTranReqVO, entity, loginName);
        if (calc.getQuotationDate() == null) {
            return;
        }
        entity.setQuotationDate(calc.getQuotationDate());
        if (StringUtils.isBlank(entity.getQuotationMethod()) && StringUtils.isNotBlank(calc.getQuotationMethod())) {
            entity.setQuotationMethod(calc.getQuotationMethod());
        }
        this.updateEntity(entity);
        vo.setQuotationDate(calc.getQuotationDate());
        if (StringUtils.isBlank(vo.getQuotationMethod()) && StringUtils.isNotBlank(calc.getQuotationMethod())) {
            vo.setQuotationMethod(calc.getQuotationMethod());
        }
    }

    /**
     * 历史数据已实现净损益为空时，按合计（已实现损益）±期权费补算并落库，供查看页、列表页展示。
     *
     * @param vo 损益信息
     * @param loginName 操作人登录名
     */
    public void fillMissingOptionNetRealMoneyTotal(FexProfitLossManageVO vo, String loginName) {
        if (vo == null || StringUtils.isBlank(vo.getId()) || vo.getNetRealMoneyTotal() != null) {
            return;
        }
        FexProfitLossManage entity = this.get(vo.getId());
        if (entity == null) {
            return;
        }
        FexTranReqVO fexTranReqVO = fexTranReqService.getNewByIdUnion(entity.getReqId());
        if (fexTranReqVO == null || !ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
            return;
        }
        Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(entity, fexTranReqVO);
        entity.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
        entity.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
        entity.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
        entity.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(DateUtils.getDate());
        this.updateEntity(entity);
        vo.setRealMoneyTotal(entity.getRealMoneyTotal());
        vo.setConvertRealMoneyTotal(entity.getConvertRealMoneyTotal());
        vo.setNetRealMoneyTotal(entity.getNetRealMoneyTotal());
        vo.setNetConvertRealMoneyTotal(entity.getNetConvertRealMoneyTotal());
    }

    /**
     * 根据交易id获取生命周期集合
     * @param reqId
     * @return
     */
    public List<FexTranLifeCycleVo> listFexTranLifeCycle(String reqId) {
        return fexTranLifeCycleService.listFexTranLifeCycle(reqId);
    }

    /**
     * 外汇交易、损益信息变更
     * @param vo
     */
    public void modify(FexProfitLossManageVO vo, String loginName){
        FexProfitLossManage fexProfitLossManage = this.get(vo.getId());
        FexTranReqVO fexTranReqVO = validateProfitLossModify(vo, fexProfitLossManage);
        fexProfitLossManage.setCurId(vo.getCurId());//损益币种
        fexProfitLossManage.setConvertRate(vo.getConvertRate());//折本位币汇率
        fexProfitLossManage.setReferenceInterestRate(vo.getReferenceInterestRate());//参考利率
        fexProfitLossManage.setConvertCurId(vo.getConvertCurId());
        fexProfitLossManage.setConvertCurReferId(vo.getConvertCurReferId());
        fexProfitLossManage.setConvertCurEnCode(vo.getConvertCurEnCode());
        fexProfitLossManage.setConvertDate(vo.getConvertDate());
        //外汇期权交易
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
            fexProfitLossManage.setFloatMoney(vo.getFloatMoney());//浮动损益
            fexProfitLossManage.setConvertFloatMoney(vo.getConvertFloatMoney());//折本位币浮动损益
            FexProfitLossManageVO premiumVO = new FexProfitLossManageVO();
            BeanUtils.copyNotNullProperties(fexProfitLossManage, premiumVO);
            Map<String, BigDecimal> premiumResult = this.calculatePremiumMoney(premiumVO, fexTranReqVO);
            //持仓归0（合约结束）时净浮动损益固定为0：期权费已计入已实现净损益，浮动侧不再重复计入
            fexProfitLossManage.setFloatMoneyTotal(resolveNetFloatMoney(vo.getFloatMoney(),
                    premiumResult.get("premiumMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));
            fexProfitLossManage.setConvertFloatMoneyTotal(resolveNetFloatMoney(vo.getConvertFloatMoney(),
                    premiumResult.get("convertMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));
            //损益币种、折本位币汇率变更后，期权费折算口径随之变化，需同步重算已实现净损益
            Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(fexProfitLossManage, fexTranReqVO);
            fexProfitLossManage.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
            fexProfitLossManage.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
            fexProfitLossManage.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
            fexProfitLossManage.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
        }else{
            //即期、远期、掉期交易
            fexProfitLossManage.setFloatMoney(vo.getFloatMoney());//浮动损益
            fexProfitLossManage.setConvertFloatMoney(vo.getConvertFloatMoney());//折本位币浮动损益
        }
        fexProfitLossManage.setUpdateBy(loginName);
        fexProfitLossManage.setUpdateDate(DateUtils.getDate());
        this.updateEntity(fexProfitLossManage);
    }

    /**
     * 批量提交
     *
     * @param ids
     * @param user
     */
    public void batchUpdate(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexProfitLossManage fexProfitLossManage = this.get(id);
            FexTranReqVO fexTranReqVO = fexTranReqService.getNewByIdUnion(fexProfitLossManage.getReqId());
            FexProfitLossManageVO fexProfitLossManageVO = calculateManageProfitLoss(fexTranReqVO,fexProfitLossManage, user.getLoginName());
            fexProfitLossManage.setReferenceInterestRate(fexProfitLossManageVO.getReferenceInterestRate());//参考利率
            fexProfitLossManage.setQuotationDate(fexProfitLossManageVO.getQuotationDate());//报价时间
            fexProfitLossManage.setQuotationMethod(fexProfitLossManageVO.getQuotationMethod());//报价方式
            fexProfitLossManage.setConvertRate(fexProfitLossManageVO.getConvertRate());//折本位币汇率
            fexProfitLossManage.setConvertDate(fexProfitLossManageVO.getConvertDate());//折本位币时间
            fexProfitLossManage.setConvertCurEnCode(fexProfitLossManageVO.getConvertCurEnCode());//折本位币报价方式
            fexProfitLossManage.setConvertCurId(fexProfitLossManageVO.getConvertCurId());
            fexProfitLossManage.setConvertCurReferId(fexProfitLossManageVO.getConvertCurReferId());
            fexProfitLossManage.setValuationRate(fexProfitLossManageVO.getReferenceInterestRate());//估值利率
            fexProfitLossManage.setFloatMoney(fexProfitLossManageVO.getFloatMoney());//浮动损益
            fexProfitLossManage.setConvertFloatMoney(fexProfitLossManageVO.getConvertFloatMoney());//折本位币浮动损益
            if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
                fexProfitLossManage.setFloatMoneyTotal(fexProfitLossManageVO.getNetFloatMoney());
                fexProfitLossManage.setConvertFloatMoneyTotal(fexProfitLossManageVO.getNetConvertFloatMoney());
                //批量刷新时同步补齐期权交易的已实现净损益，保持与查看页、列表页口径一致
                Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(fexProfitLossManage, fexTranReqVO);
                fexProfitLossManage.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
                fexProfitLossManage.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
                fexProfitLossManage.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
                fexProfitLossManage.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
            }
            fexProfitLossManage.setUpdateBy(user.getLoginName());
            fexProfitLossManage.setUpdateDate(DateUtils.getDate());
            this.updateEntity(fexProfitLossManage);

        }
    }

    /**
     * 计算损益
     * @param fexTranReqVO
     * @param loginName
     */
    public FexProfitLossManageVO calculateManageProfitLoss(FexTranReqVO fexTranReqVO, FexProfitLossManage fexProfitLossManage,String loginName) {
        FexProfitLossManageVO fexProfitLossManageVO = new FexProfitLossManageVO();
        //交易的默认外汇牌价类型
        String defValuationRateType = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFTRANRATETYPE).getValue();
        //默认损益币种
        //String defProfitLossCur = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFPROFITANDLOSSCUR).getValue();
        //估值汇率取
        String valuationExchangeRate = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_VALUATIONEXCHANGERATE).getValue();
        //损益币种
        String profitLossCur = fexProfitLossManage.getCurId();
        boolean hasOpenPosition = !EmptyUtils.isEmpty(fexTranReqVO.getPositionMoney())
                && fexTranReqVO.getPositionMoney().compareTo(BigDecimal.ZERO) != 0;
        //浮动损益
        BigDecimal floatMoney = BigDecimal.ZERO;
        //折本位币浮动损益
        BigDecimal convertFloatMoney = BigDecimal.ZERO;
        BigDecimal referenceRate = fexProfitLossManage.getReferenceInterestRate() == null
                ? BigDecimal.ZERO : fexProfitLossManage.getReferenceInterestRate();
        //获取参考汇率
        FexRateInfoVO lastFexRateForSP = null;
        //外汇交易类型 0-即期、1-远期
        String fexTranType = fexTranReqVO.getFexTranType();
        String tranType = fexTranReqVO.getTranType();
        //报价时间
        Date returnTime = fexProfitLossManage.getQuotationDate();
        //报价方式
        String quotationMethodCode = fexProfitLossManage.getQuotationMethod();
        String quotationCurId = fexProfitLossManage.getQuotationCurId();
        String quotationReferCurId = fexProfitLossManage.getQuotationReferCurId();
        //校验交易的默认外汇牌价类型是否设置
        if(StringUtils.isNotBlank(defValuationRateType)){
            //获取参考汇率
            String type = "";
            //估值汇率取中间价
            if(valuationExchangeRate.equals("1")){
                type = "3";
            }else {
                //参考汇率
                if(fexTranReqVO.getBuyCurId().equals(fexTranReqVO.getQuotationCurId())){
                    //买入币种=报价方式的左侧
                    type = "1";
                }else{
                    //买入币种=报价方式的右侧
                    type = "2";
                }
            }
            //获取远期汇率
            lastFexRateForSP = fexRateInfoService.getRateForEnd(defValuationRateType, fexTranReqVO.getBuyCurId(), fexTranReqVO.getSellCurId(), fexTranReqVO.getSettleDate(), type);
            if(null != lastFexRateForSP){
                referenceRate = lastFexRateForSP.getReferenceRate();
                //校验报价时间是否为空
                if(null != lastFexRateForSP.getReturnTime()){
                    returnTime = lastFexRateForSP.getReturnTime();
                } else if(null != lastFexRateForSP.getEffectDate()){
                    returnTime = lastFexRateForSP.getEffectDate();
                }
                //校验报价币种是否为空
                if(StringUtils.isNotBlank(lastFexRateForSP.getCurEnCode()) && StringUtils.isNotBlank(lastFexRateForSP.getReferCurEnCode())){
                    quotationMethodCode = lastFexRateForSP.getCurEnCode() + "/" + lastFexRateForSP.getReferCurEnCode();
                    quotationCurId = lastFexRateForSP.getCur();
                    quotationReferCurId = lastFexRateForSP.getReferCur();
                }
            }
        }
        if (lastFexRateForSP == null && hasOpenPosition) {
            floatMoney = fexProfitLossManage.getFloatMoney() == null
                    ? BigDecimal.ZERO : fexProfitLossManage.getFloatMoney();
        }
        //校验参考汇率是否为空
        if(null != lastFexRateForSP){
            if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())) {
                //校验参考汇率是否为空
                if(null != referenceRate && ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqVO.getTranRateType())){
                    //交易货币对=报价方式
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        floatMoney = referenceRate.subtract(fexTranReqVO.getContractRate()).multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }else{
                        BigDecimal one = BigDecimal.ONE;
                        //交易货币对≠报价方式
                        floatMoney = one.divide(referenceRate,6, RoundingMode.HALF_UP).subtract(one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP)).multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }
                }
            }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
                //外汇期权交易
                Boolean conditionalBoolean = false;
                //外汇期权品种不为空
                if(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_2.equals(fexTranReqVO.getFexOption2()) || ForeignConstant.FEX_OPTION2_3.equals(fexTranReqVO.getFexOption2())){
                    //交易货币对=报价方式
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        if(null != fexTranReqVO.getFexOption3()){
                            //障碍类型 Down&In  && 参考汇率<=障碍值
                            if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                                //障碍类型 Down&Out  && 参考汇率>障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                                //障碍类型 Up&In  && 参考汇率>障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                                //障碍类型 Up&Out  && 参考汇率<=障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && isDoubleInEffective(referenceRate, fexTranReqVO.getExceptLowVal(), fexTranReqVO.getExceptHignVal())){
                                //障碍类型 Double In  && 参考汇率<低障碍值或参考汇率>高障碍值
                                conditionalBoolean = true;
                            } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                                //障碍类型 Double Out  低障碍值<=参考汇率<=高障碍值
                                conditionalBoolean = true;
                            }
                        }
                    }else{
                        //交易货币对≠报价方式
                        //障碍类型 Down&In  && 1/参考汇率<=1/障碍值（参考汇率>=障碍值）
                        if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                            //障碍类型 Down&Out  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                            //障碍类型 Up&In  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                            //障碍类型 Up&Out  && 1/参考汇率<=1/障碍值（参考>=障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && isDoubleInEffective(referenceRate, fexTranReqVO.getExceptLowVal(), fexTranReqVO.getExceptHignVal())){
                            //障碍类型 Double In  && 1/参考汇率<1/高障碍值 or 1/参考汇率>1/低障碍值（参考汇率<低障碍值or参考汇率>高障碍值）
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                            //障碍类型 Double Out  1/高障碍值<=1/参考汇率值<=1/低障碍值（低障碍值<=参考汇率<=高障碍值）
                            conditionalBoolean = true;
                        }
                    }
                }else if((null != fexTranReqVO.getFexOption1() && ForeignConstant.FEX_OPTION1_0.equals(fexTranReqVO.getFexOption1()))
                        ||(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_1.equals(fexTranReqVO.getFexOption2()))){
                    //外汇期权品种=普通期权 or 亚式期权
                    conditionalBoolean = true;
                }
                //校验条件是否满足
                if(conditionalBoolean){
                    //计算浮动损益
                    //交易货币对=报价方式
                    BigDecimal exchangeRate = BigDecimal.ZERO;
                    if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                        //涨跌方向
                        if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                            //看涨
                            exchangeRate = calculateCallPayoff(referenceRate, fexTranReqVO.getContractRate());
                        }else{
                            //看跌
                            exchangeRate = calculateCallPayoff(fexTranReqVO.getContractRate(), referenceRate);
                        }
                        floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }else{
                        //交易货币对≠报价方式
                        BigDecimal one = BigDecimal.ONE;
                        BigDecimal invertedReferenceRate = one.divide(referenceRate,6, RoundingMode.HALF_UP);
                        BigDecimal contractRate = one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP);
                        //涨跌方向
                        if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                            //看涨
                            exchangeRate = calculateCallPayoff(invertedReferenceRate, contractRate);
                        }else{
                            //看跌
                            exchangeRate = calculateCallPayoff(contractRate, invertedReferenceRate);
                        }
                        floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                        //买卖方向 1-买入 2-卖出
                        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                            floatMoney = floatMoney.negate();
                        }
                    }
                }
            }
        }
        //损益币种=交易货币对的左侧
        if(lastFexRateForSP != null && floatMoney.compareTo(BigDecimal.ZERO) != 0
                && profitLossCur.equals(fexTranReqVO.getTranCurPairBaseId())){
            //交易货币对 = 报价方式
            if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                floatMoney = floatMoney.divide(referenceRate, 2, RoundingMode.HALF_UP);
            }else{
                floatMoney = floatMoney.multiply(referenceRate).setScale(2, RoundingMode.HALF_UP);
            }
        }
        //单位信息
        CorpDTO corpDTO = corpService.get(fexTranReqVO.getCorpId());
        //折本位币汇率
        BigDecimal convertRate = fexProfitLossManage.getConvertRate() == null
                ? BigDecimal.ZERO : fexProfitLossManage.getConvertRate();
        fexProfitLossManageVO.setConvertRate(fexProfitLossManage.getConvertRate());
        fexProfitLossManageVO.setConvertDate(fexProfitLossManage.getConvertDate());
        fexProfitLossManageVO.setConvertCurEnCode(fexProfitLossManage.getConvertCurEnCode());
        fexProfitLossManageVO.setConvertCurId(fexProfitLossManage.getConvertCurId());
        fexProfitLossManageVO.setConvertCurReferId(fexProfitLossManage.getConvertCurReferId());
        //判断损益币种是否等于单位币种
        if(!corpDTO.getCurId().equals(profitLossCur)){
            //折本位币报价方式
            FexRateInfoVO fexRateInfoVO = fexRateInfoService.getQuoMethodForSP(defValuationRateType, profitLossCur, corpDTO.getCurId());
            if(null != fexRateInfoVO){
                if(profitLossCur.equals(fexRateInfoVO.getCur())){
                    convertRate = fexRateInfoVO.getPurchaseRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.multiply(convertRate)
                            .setScale(2, RoundingMode.HALF_UP);
                }else{
                    convertRate = fexRateInfoVO.getSellRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.divide(convertRate, 2, RoundingMode.HALF_UP);
                }
                fexProfitLossManageVO.setConvertRate(convertRate);//折本位币汇率
                fexProfitLossManageVO.setConvertDate(fexRateInfoVO.getReturnTime());//折本位币时间
                fexProfitLossManageVO.setConvertCurEnCode(fexRateInfoVO.getCurEnCode() + "/" + fexRateInfoVO.getReferCurEnCode());//折本位币币种
                fexProfitLossManageVO.setConvertCurId(fexRateInfoVO.getCur());
                fexProfitLossManageVO.setConvertCurReferId(fexRateInfoVO.getReferCur());
            } else if (hasOpenPosition) {
                convertFloatMoney = fexProfitLossManage.getConvertFloatMoney() == null
                        ? BigDecimal.ZERO : fexProfitLossManage.getConvertFloatMoney();
            }
        }else{
            //损益币种=单位的本位币
            convertFloatMoney = floatMoney;
        }

        fexProfitLossManageVO.setReferenceInterestRate(referenceRate);//参考利率
        fexProfitLossManageVO.setQuotationDate(returnTime);//报价时间
        fexProfitLossManageVO.setQuotationMethod(quotationMethodCode);//报价方式
        fexProfitLossManageVO.setQuotationCurId(quotationCurId);
        fexProfitLossManageVO.setQuotationReferCurId(quotationReferCurId);
        fexProfitLossManageVO.setCurId(profitLossCur);//损益币种
        fexProfitLossManageVO.setFloatMoney(floatMoney);//浮动损益
        fexProfitLossManageVO.setConvertFloatMoney(convertFloatMoney);//折本位币浮动损益
        if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
            Map<String, BigDecimal> premiumResult = this.calculatePremiumMoney(fexProfitLossManageVO, fexTranReqVO);
            //持仓归0（合约结束）时净浮动损益固定为0：期权费已计入已实现净损益，浮动侧不再重复计入
            fexProfitLossManageVO.setNetFloatMoney(resolveNetFloatMoney(floatMoney,
                    premiumResult.get("premiumMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));
            fexProfitLossManageVO.setNetConvertFloatMoney(resolveNetFloatMoney(convertFloatMoney,
                    premiumResult.get("convertMoney"), fexTranReqVO.getBusDirection(), fexTranReqVO.getPositionMoney()));
        }
        fexProfitLossManageVO.setUpdateBy(loginName);
        fexProfitLossManageVO.setUpdateDate(DateUtils.getDate());

        return fexProfitLossManageVO;
    }

    /**
     * 生命周期终止但不产生已实现损益明细时，按最新持仓刷新浮动损益。
     *
     * @param reqId 交易单据ID
     * @param loginName 操作人登录名
     */
    public void refreshFloatingProfitLoss(String reqId, String loginName) {
        refreshFloatingProfitLoss(reqId, null, loginName);
    }

    /**
     * 按指定的生命周期处理后持仓刷新浮动损益。
     *
     * @param reqId 交易单据ID
     * @param currentPosition 生命周期处理后的最新持仓
     * @param loginName 操作人登录名
     */
    public void refreshFloatingProfitLoss(String reqId, BigDecimal currentPosition, String loginName) {
        FexProfitLossManage entity = fexProfitLossManageDao.getFexProfitLossManageByReqId(reqId);
        if (entity == null) {
            return;
        }
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(reqId);
        if (currentPosition != null) {
            fexTranReqVO.setPositionMoney(currentPosition);
        }
        FexProfitLossManageVO calculated = calculateManageProfitLoss(fexTranReqVO, entity, loginName);
        if (calculated.getQuotationDate() != null) {
            entity.setReferenceInterestRate(calculated.getReferenceInterestRate());
            entity.setValuationRate(calculated.getReferenceInterestRate());
            entity.setQuotationDate(calculated.getQuotationDate());
            entity.setQuotationMethod(calculated.getQuotationMethod());
            entity.setQuotationCurId(calculated.getQuotationCurId());
            entity.setQuotationReferCurId(calculated.getQuotationReferCurId());
        }
        if (calculated.getConvertRate() != null) {
            entity.setConvertRate(calculated.getConvertRate());
            entity.setConvertDate(calculated.getConvertDate());
            entity.setConvertCurEnCode(calculated.getConvertCurEnCode());
            entity.setConvertCurId(calculated.getConvertCurId());
            entity.setConvertCurReferId(calculated.getConvertCurReferId());
        }
        entity.setFloatMoney(calculated.getFloatMoney());
        entity.setConvertFloatMoney(calculated.getConvertFloatMoney());
        if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())) {
            entity.setFloatMoneyTotal(calculated.getNetFloatMoney());
            entity.setConvertFloatMoneyTotal(calculated.getNetConvertFloatMoney());
            //放弃行权、敲出、作废等终止类操作不产生已实现损益明细，但期权费已实际发生，
            //需同步刷新已实现净损益，避免查看页「已实现损益-净损益」为空
            Map<String, BigDecimal> netRealResult = resolveNetRealMoneyTotal(entity, fexTranReqVO);
            entity.setRealMoneyTotal(netRealResult.get("realMoneyTotal"));
            entity.setConvertRealMoneyTotal(netRealResult.get("convertRealMoneyTotal"));
            entity.setNetRealMoneyTotal(netRealResult.get("netRealMoneyTotal"));
            entity.setNetConvertRealMoneyTotal(netRealResult.get("netConvertRealMoneyTotal"));
        }
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(DateUtils.getDate());
        fexProfitLossManageDao.update(entity);
    }

    /**
     * 计算期权费金额
     * @param fexProfitLossManageVO
     * @param fexTranReqVO
     */
    public Map<String,BigDecimal> calculatePremiumMoney(FexProfitLossManageVO fexProfitLossManageVO, FexTranReqVO fexTranReqVO){
        Map<String, BigDecimal> result = new HashMap<>();
        //期权费金额（损益币种）
        BigDecimal premiumMoney = BigDecimal.ZERO;
        //期权费金额（本位币）
        BigDecimal convertMoney = BigDecimal.ZERO;
        //计算净损益
        if(null != fexProfitLossManageVO && null != fexTranReqVO){
            //损益币种=期权费币种
            if(fexProfitLossManageVO.getCurId().equals(fexTranReqVO.getPremiumCur())){
                premiumMoney = fexTranReqVO.getPremiumMoneyOfPremiumCur();
            }else{
                //期权费币种=报价方式的左侧
                if(fexTranReqVO.getPremiumCur().equals(fexProfitLossManageVO.getQuotationCurId())){
                    //期权费金额（损益币种）=期权费金额（期权费币种）* 执行汇率
                    premiumMoney = fexTranReqVO.getPremiumMoneyOfPremiumCur().multiply(fexTranReqVO.getContractRate()).setScale(2, RoundingMode.HALF_UP);
                }else{
                    //期权费金额（损益币种）=期权费金额（期权费币种）/ 执行汇率
                    premiumMoney = fexTranReqVO.getPremiumMoneyOfPremiumCur().divide(fexTranReqVO.getContractRate(), 2, RoundingMode.HALF_UP);
                }
            }
            if(null != fexTranReqVO.getConvertMoney() && fexTranReqVO.getConvertMoney().compareTo(BigDecimal.ZERO) > 0){
                convertMoney = fexTranReqVO.getConvertMoney();
            }else{
                convertMoney = fexTranReqVO.getPremiumMoneyOfPremiumCur();
            }
        }
        result.put("premiumMoney",premiumMoney);
        result.put("convertMoney",convertMoney);
        return result;
    }


    /**
     * 计算浮动损益
     * @param vo
     * @return
     */
    public FexProfitLossManageVO calculatePositionProfit(FexProfitLossManageVO vo){
        FexProfitLossManageVO fexProfitLossManageVO = new FexProfitLossManageVO();
        BigDecimal floatMoney = BigDecimal.ZERO;
        BigDecimal referenceRate = vo.getReferenceInterestRate();
        //损益信息
        FexProfitLossManage fexProfitLossManage = this.get(vo.getId(), true);
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexProfitLossManage.getReqId());
        FexProduct fexProduct = fexProductService.get(fexTranReqVO.getProId());
        fexTranReqVO.setFexOption1(fexProduct.getFexOption1());
        fexTranReqVO.setFexOption2(fexProduct.getFexOption2());
        fexTranReqVO.setFexOption3(fexProduct.getFexOption3());

        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())) {
            //交易货币对=报价方式
            if(fexTranReqVO.getTranCurPairBaseId().equals(fexProfitLossManage.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexProfitLossManage.getQuotationReferCurId())){
                //+-（参考汇率-合同汇率）*持仓金额
                floatMoney = referenceRate.subtract(fexTranReqVO.getContractRate()).multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                //买卖方向 1-买入 2-卖出
                if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                    floatMoney = floatMoney.negate();
                }
            }else{
                BigDecimal one = BigDecimal.ONE;
                //交易货币对≠报价方式
                //+-（1/参考汇率-1/合同汇率）*持仓金额
                floatMoney = one.divide(referenceRate,6, RoundingMode.HALF_UP).subtract(one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP)).multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                //买卖方向 1-买入 2-卖出
                if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                    floatMoney = floatMoney.negate();
                }
            }
        }else{
            Boolean conditionalBoolean = false;
            //外汇期权品种不为空
            if(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_2.equals(fexTranReqVO.getFexOption2()) || ForeignConstant.FEX_OPTION2_3.equals(fexTranReqVO.getFexOption2())){
                //交易货币对=报价方式
                if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                    if(null != fexTranReqVO.getFexOption3()){
                        //障碍类型 Down&In  && 参考汇率<=障碍值
                        if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                            //障碍类型 Down&Out  && 参考汇率>障碍值
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) > 0){
                            //障碍类型 Up&In  && 参考汇率>障碍值
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) <= 0){
                            //障碍类型 Up&Out  && 参考汇率<=障碍值
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) < 0 || referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) > 0)){
                            //障碍类型 Double In  && 参考汇率<低障碍值或参考汇率>高障碍值
                            conditionalBoolean = true;
                        } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                            //障碍类型 Double Out  低障碍值<=参考汇率<=高障碍值
                            conditionalBoolean = true;
                        }
                    }
                }else{
                    //交易货币对≠报价方式
                    //障碍类型 Down&In  && 1/参考汇率<=1/障碍值（参考汇率>=障碍值）
                    if(ForeignConstant.FEX_OPTION3_4.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                        conditionalBoolean = true;
                    } else if(ForeignConstant.FEX_OPTION3_3.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                        //障碍类型 Down&Out  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                        conditionalBoolean = true;
                    } else if(ForeignConstant.FEX_OPTION3_1.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) < 0){
                        //障碍类型 Up&In  && 1/参考汇率>1/障碍值（参考汇率<障碍值）
                        conditionalBoolean = true;
                    } else if(ForeignConstant.FEX_OPTION3_2.equals(fexTranReqVO.getFexOption3()) && referenceRate.compareTo(fexTranReqVO.getExceptVal()) >= 0){
                        //障碍类型 Up&Out  && 1/参考汇率<=1/障碍值（参考>=障碍值）
                        conditionalBoolean = true;
                    } else if(ForeignConstant.FEX_OPTION3_5.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) < 0 || referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) > 0)){
                        //障碍类型 Double In  && 1/参考汇率<1/高障碍值 or 1/参考汇率>1/低障碍值（参考汇率<低障碍值or参考汇率>高障碍值）
                        conditionalBoolean = true;
                    } else if(ForeignConstant.FEX_OPTION3_6.equals(fexTranReqVO.getFexOption3()) && (referenceRate.compareTo(fexTranReqVO.getExceptLowVal()) >= 0 && referenceRate.compareTo(fexTranReqVO.getExceptHignVal()) <= 0)){
                        //障碍类型 Double Out  1/高障碍值<=1/参考汇率值<=1/低障碍值（低障碍值<=参考汇率<=高障碍值）
                        conditionalBoolean = true;
                    }
                }
            }else if((null != fexTranReqVO.getFexOption1() && ForeignConstant.FEX_OPTION1_0.equals(fexTranReqVO.getFexOption1()))
                    ||(null != fexTranReqVO.getFexOption2() && ForeignConstant.FEX_OPTION2_1.equals(fexTranReqVO.getFexOption2()))){
                //外汇期权品种=普通期权 or 亚式期权
                conditionalBoolean = true;
            }
            //校验条件是否满足
            if(conditionalBoolean){
                //计算浮动损益
                //交易货币对=报价方式
                BigDecimal exchangeRate = BigDecimal.ZERO;
                if(fexTranReqVO.getTranCurPairBaseId().equals(fexProfitLossManage.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexProfitLossManage.getQuotationReferCurId())){
                    //涨跌方向
                    if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                        //看涨
                        exchangeRate = calculateCallPayoff(referenceRate, fexTranReqVO.getContractRate());
                    }else{
                        //看跌
                        exchangeRate = calculateCallPayoff(fexTranReqVO.getContractRate(), referenceRate);
                    }
                    floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                    //买卖方向 1-买入 2-卖出
                    if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                        floatMoney = floatMoney.negate();
                    }
                }else{
                    //交易货币对≠报价方式
                    BigDecimal one = BigDecimal.ONE;
                    BigDecimal invertedReferenceRate = one.divide(referenceRate,6, RoundingMode.HALF_UP);
                    BigDecimal contractRate = one.divide(fexTranReqVO.getContractRate(),6, RoundingMode.HALF_UP);
                    //涨跌方向
                    if(ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(fexTranReqVO.getUpdownDirection())){
                        //看涨
                        exchangeRate = calculateCallPayoff(invertedReferenceRate, contractRate);
                    }else{
                        //看跌
                        exchangeRate = calculateCallPayoff(contractRate, invertedReferenceRate);
                    }
                    floatMoney = exchangeRate.multiply(fexTranReqVO.getPositionMoney()).setScale(2, RoundingMode.HALF_UP);;
                    //买卖方向 1-买入 2-卖出
                    if(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())){
                        floatMoney = floatMoney.negate();
                    }
                }
            }
        }

        //交易的默认外汇牌价类型
        String defValuationRateType = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFTRANRATETYPE).getValue();
        CorpDTO corpDTO = corpService.get(fexTranReqVO.getCorpId());
        String profitLossCur = vo.getCurId();
        BigDecimal convertRate = BigDecimal.ZERO;
        BigDecimal convertFloatMoney = BigDecimal.ZERO;
        //损益币种=交易货币对的左侧
        if(floatMoney.compareTo(BigDecimal.ZERO) != 0 && profitLossCur.equals(fexTranReqVO.getTranCurPairBaseId())){
            //交易货币对 = 报价方式
            if(fexTranReqVO.getTranCurPairBaseId().equals(fexTranReqVO.getQuotationCurId()) && fexTranReqVO.getTranCurPairReferId().equals(fexTranReqVO.getQuotationReferCurId())){
                floatMoney = floatMoney.divide(referenceRate, 2, RoundingMode.HALF_UP);
            }else{
                floatMoney = floatMoney.multiply(referenceRate).setScale(2, RoundingMode.HALF_UP);
            }
        }
        //判断损益币种是否等于单位币种
        if(!corpDTO.getCurId().equals(profitLossCur)){
            //折本位币报价方式
            FexRateInfoVO fexRateInfoVO = fexRateInfoService.getQuoMethodForSP(defValuationRateType, profitLossCur, corpDTO.getCurId());
            if(null != fexRateInfoVO){
                if(profitLossCur.equals(fexRateInfoVO.getCur())){
                    convertRate = fexRateInfoVO.getPurchaseRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.multiply(convertRate)
                            .setScale(2, RoundingMode.HALF_UP);
                }else{
                    convertRate = fexRateInfoVO.getSellRate();
                    //折本位币浮动损益=浮动损益*折本位币汇率
                    convertFloatMoney = floatMoney.divide(convertRate, 2, RoundingMode.HALF_UP);
                }
                fexProfitLossManageVO.setConvertDate(fexRateInfoVO.getReturnTime());//折本位币时间
                fexProfitLossManageVO.setConvertCurEnCode(fexRateInfoVO.getCurEnCode() + "/" + fexRateInfoVO.getReferCurEnCode());//折本位币币种
                fexProfitLossManageVO.setConvertCurId(fexRateInfoVO.getCur());
                fexProfitLossManageVO.setConvertCurReferId(fexRateInfoVO.getReferCur());
            }
        }else{
            //损益币种=单位的本位币
            convertFloatMoney = floatMoney;
        }

        //外汇期权交易-计算期权费金额
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
            FexProfitLossManageVO FexProfitLossManageVO1 = new FexProfitLossManageVO();
            BeanUtils.copyNotNullProperties(fexProfitLossManage,FexProfitLossManageVO1);
            FexProfitLossManageVO1.setCurId(vo.getCurId());
            Map<String, BigDecimal> result = this.calculatePremiumMoney(FexProfitLossManageVO1,fexTranReqVO);
            fexProfitLossManageVO.setPremiumMoney(result.get("premiumMoney"));
            fexProfitLossManageVO.setConvertMoney(result.get("convertMoney"));
        }

        fexProfitLossManageVO.setConvertRate(convertRate);
        fexProfitLossManageVO.setFloatMoney(floatMoney);
        fexProfitLossManageVO.setConvertFloatMoney(convertFloatMoney);

        return fexProfitLossManageVO;
    }

    /**
     * 判断双障碍敲入是否越过任一边界。
     *
     * @param referenceRate 参考汇率
     * @param lowBarrier 低障碍值
     * @param highBarrier 高障碍值
     * @return 是否生效
     */
    static boolean isDoubleInEffective(BigDecimal referenceRate, BigDecimal lowBarrier, BigDecimal highBarrier) {
        return referenceRate.compareTo(lowBarrier) < 0 || referenceRate.compareTo(highBarrier) > 0;
    }

    /**
     * 校验损益编辑的必填项、币种范围和生命周期锁定规则。
     *
     * @param vo 页面提交的损益信息
     * @param entity 损益实体
     * @return 对应交易信息
     */
    private FexTranReqVO validateProfitLossModify(FexProfitLossManageVO vo, FexProfitLossManage entity) {
        if (entity == null || StringUtils.isBlank(vo.getCurId()) || EmptyUtils.isEmpty(vo.getReferenceInterestRate())
                || EmptyUtils.isEmpty(vo.getFloatMoney()) || EmptyUtils.isEmpty(vo.getConvertFloatMoney())) {
            throw new ParameterException(ForeignMessageEnum.FEX_PROFIT_LOSS_REQUIRED);
        }
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(entity.getReqId());
        boolean currencyInPair = vo.getCurId().equals(fexTranReqVO.getTranCurPairBaseId())
                || vo.getCurId().equals(fexTranReqVO.getTranCurPairReferId());
        if (!currencyInPair) {
            throw new ParameterException(ForeignMessageEnum.FEX_PROFIT_LOSS_CUR_INVALID);
        }
        List<FexTranLifeCycleVo> lifeCycles = fexTranLifeCycleService.listFexTranLifeCycle(entity.getReqId());
        if (!EmptyUtils.isEmpty(lifeCycles) && !vo.getCurId().equals(entity.getCurId())) {
            throw new ParameterException(ForeignMessageEnum.FEX_PROFIT_LOSS_CUR_LOCKED);
        }
        CorpDTO corpDTO = corpService.get(entity.getCorpId());
        if (corpDTO != null && !vo.getCurId().equals(corpDTO.getCurId()) && EmptyUtils.isEmpty(vo.getConvertRate())) {
            throw new ParameterException(ForeignMessageEnum.FEX_PROFIT_LOSS_CONVERT_RATE_REQUIRED);
        }
        return fexTranReqVO;
    }

    /**
     * 判断交易持仓是否已清空（合约是否已结束）。
     *
     * @param positionMoney 持仓金额
     * @return 持仓为空或为0时返回true
     */
    static boolean isPositionClosed(BigDecimal positionMoney) {
        return EmptyUtils.isEmpty(positionMoney) || positionMoney.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 计算净浮动损益。
     * 持仓未清空时=浮动损益±期权费（买入减、卖出加）；持仓已清空（合约结束）时固定为0，
     * 因为此时期权费已计入已实现净损益，若浮动侧再计入会与已实现重复累计。
     *
     * @param floatMoney 浮动损益
     * @param premiumMoney 期权费金额（与floatMoney同币种）
     * @param busDirection 买卖方向
     * @param positionMoney 持仓金额
     * @return 净浮动损益
     */
    static BigDecimal resolveNetFloatMoney(BigDecimal floatMoney, BigDecimal premiumMoney,
                                           String busDirection, BigDecimal positionMoney) {
        if (isPositionClosed(positionMoney)) {
            return BigDecimal.ZERO;
        }
        if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(busDirection)) {
            return floatMoney.subtract(premiumMoney);
        }
        return floatMoney.add(premiumMoney);
    }

    /**
     * 计算期权交易的已实现净损益。
     * 已实现净损益=合计（已实现损益）±期权费金额（损益币种），买卖方向为买入时取减号，否则取加号；
     * 同时把为空的已实现损益合计归一为0，避免查看页、列表页的合计与净损益展示为空。
     *
     * @param entity 损益主表实体，提供已实现损益合计、损益币种与报价方式
     * @param fexTranReqVO 交易信息，提供期权费金额、期权费币种与买卖方向
     * @return 已实现损益合计、折本位币已实现损益合计、已实现净损益、折本位币已实现净损益
     */
    private Map<String, BigDecimal> resolveNetRealMoneyTotal(FexProfitLossManage entity, FexTranReqVO fexTranReqVO) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal realMoneyTotal = EmptyUtils.isEmpty(entity.getRealMoneyTotal())
                ? BigDecimal.ZERO : entity.getRealMoneyTotal();
        BigDecimal convertRealMoneyTotal = EmptyUtils.isEmpty(entity.getConvertRealMoneyTotal())
                ? BigDecimal.ZERO : entity.getConvertRealMoneyTotal();
        FexProfitLossManageVO premiumVO = new FexProfitLossManageVO();
        premiumVO.setCurId(entity.getCurId());
        premiumVO.setQuotationCurId(entity.getQuotationCurId());
        Map<String, BigDecimal> premiumResult = this.calculatePremiumMoney(premiumVO, fexTranReqVO);
        BigDecimal netRealMoneyTotal;
        BigDecimal netConvertRealMoneyTotal;
        if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReqVO.getBusDirection())) {
            netRealMoneyTotal = realMoneyTotal.subtract(premiumResult.get("premiumMoney"));
            netConvertRealMoneyTotal = convertRealMoneyTotal.subtract(premiumResult.get("convertMoney"));
        } else {
            netRealMoneyTotal = realMoneyTotal.add(premiumResult.get("premiumMoney"));
            netConvertRealMoneyTotal = convertRealMoneyTotal.add(premiumResult.get("convertMoney"));
        }
        result.put("realMoneyTotal", realMoneyTotal);
        result.put("convertRealMoneyTotal", convertRealMoneyTotal);
        result.put("netRealMoneyTotal", netRealMoneyTotal);
        result.put("netConvertRealMoneyTotal", netConvertRealMoneyTotal);
        return result;
    }
}
