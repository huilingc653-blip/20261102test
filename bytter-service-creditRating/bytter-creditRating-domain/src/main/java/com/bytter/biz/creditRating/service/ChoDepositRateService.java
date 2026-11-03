package com.bytter.biz.creditRating.service;



import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;

import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.utils.EmptyUtils;

import com.bytter.increment.api.dto.ChoDepositRateDTO;
import com.bytter.increment.api.dto.ChoProductRedeemDetailDTO;
import com.bytter.increment.api.dto.ChoProductReqQueryDTO;
import com.bytter.increment.api.service.IChoDepositRateService;
import com.bytter.increment.api.service.IChoProductRedeemService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 理财利率数据源服务层
 */
@Service("choDepositRateService")
public class ChoDepositRateService {

    @DubboReference(check = false)
    private IChoDepositRateService choDepositRateService;

    @DubboReference(check = false)
    private IChoProductRedeemService choProductRedeemService;

    @Autowired
    private CrdRawDataService crdRawDataService;
    //@Autowired
    //private BtInputBankInfoService btInputBankInfoService;
    @Autowired
    private CrInstitutionalRawDataService institutionalRawDataService;

    /**
     * 返回一个机构对应一个组织的利率平均值
     *
     * @param corpId
     * @param institutionIds
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public Map<String, BigDecimal> getDepositRate(String corpId, List<String> institutionIds, String scoringStartDate, String scoringEndDate) {
        Map<String, BigDecimal> map = new HashMap<>();
        List<InstitutionDetailsVo> institutionByIds = crdRawDataService.findInstitutionByIds(institutionIds);
        for (InstitutionDetailsVo institutionById : institutionByIds) {
           String bankCode=null;
           String bankTypeId=null;
            //判断是总行还是支行
            if ("001".equals(institutionById.getInstitutionType())) {
                bankTypeId=institutionById.getBankTypeId();
            } else {
                bankCode=institutionById.getBankCode();
            }
            BigDecimal rate = candidateList(corpId, bankCode,bankTypeId, scoringStartDate, scoringEndDate);
            //用机构的id为key，
            map.put(institutionById.getInsId(), rate);
        }
        return map;
    }

    /**
     * 计算存款类业务和理财类业务的利率平均数
     *
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public BigDecimal candidateList(String corpId, String bankCode,String bankTypeId, String scoringStartDate, String scoringEndDate) {
        List<ChoDepositRateDTO> choDepositRateVoList = new ArrayList<>();
        //查询保本收益型的理财数据
        //平均收益率:如果没有赎回记录，直接取登记的参考收益率(referInterestRate)，如果有赎回记录的averageIncomeRate的平均值
        List<ChoProductReqQueryDTO> choDepositRateVosIsQuery = choDepositRateService.queryChoList(corpId, bankCode,bankTypeId, scoringStartDate, scoringEndDate);
        for (ChoProductReqQueryDTO choDepositRateVo : choDepositRateVosIsQuery) {
            //查询已赎回的理财数据
            List<ChoProductRedeemDetailDTO> productRedeems = choProductRedeemService.listProductRedeemApproved(choDepositRateVo.getId());
            BigDecimal averageIncomeRate = BigDecimal.ZERO;//平均收益率
            if (!EmptyUtils.isEmpty(productRedeems)) {
                for (ChoProductRedeemDetailDTO redeem : productRedeems) {
                    if (redeem.getIncomeRate() != null) {
                        averageIncomeRate = averageIncomeRate.add(redeem.getIncomeRate());
                    }
                }
                averageIncomeRate = averageIncomeRate.divide(BigDecimal.valueOf(productRedeems.size()), 10, RoundingMode.HALF_UP);
            } else {
                averageIncomeRate = choDepositRateVo.getReferIncomeRate();
            }
            ChoDepositRateDTO choDepositRateVo1 = new ChoDepositRateDTO();
            choDepositRateVo1.setRate(averageIncomeRate);
            choDepositRateVoList.add(choDepositRateVo1);
        }
        //查询非保本收益型的理财数据
        List<ChoDepositRateDTO> choDepositRateVos1 = choDepositRateService.queryNotChoList(corpId, bankCode,bankTypeId, scoringStartDate, scoringEndDate);
        choDepositRateVoList.addAll(choDepositRateVos1);
        //查询内部存款的数据
        List<ChoDepositRateDTO> choDepositRateVos = choDepositRateService.queryDepositList(corpId, bankCode,bankTypeId, scoringStartDate, scoringEndDate);
        choDepositRateVoList.addAll(choDepositRateVos);
        //stream流对choDepositRateVoList的rate字段求和
        if(choDepositRateVoList.size()>0) {
            BigDecimal sum = choDepositRateVoList.stream().map(ChoDepositRateDTO::getRate).reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(BigDecimal.valueOf(choDepositRateVoList.size()), 6, RoundingMode.HALF_UP);
        }else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 理财存款业务与理财业务剩余金额的合计
     */
    public BigDecimal getSumChoDepositForReportDate(String institutionalId, List<String> corpIds, Date scoringEndDate){
        //获取机构信息
        CrInstitutionalRawData crInstitutionalRawData=institutionalRawDataService.get(institutionalId);
        BigDecimal returnData= BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(crInstitutionalRawData)) {
            //根据机构是总行还是支行获取对应授信余额
            if (crInstitutionalRawData.getBankCode() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(crInstitutionalRawData.getInstitutionType())) {
                //支行
               BigDecimal returnDataFin=choDepositRateService.getChoFinanceForReportDate(corpIds,scoringEndDate,null,crInstitutionalRawData.getBankCode());
               BigDecimal returnDataPro=choDepositRateService.getChoProductReqForReportDate(corpIds,scoringEndDate,null,crInstitutionalRawData.getBankCode());
               returnData=returnDataFin.add(returnDataPro);
            }else if(crInstitutionalRawData.getBankTypeId() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(crInstitutionalRawData.getInstitutionType())) {
                //总行
                BigDecimal returnDataFin=choDepositRateService.getChoFinanceForReportDate(corpIds,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);
                BigDecimal returnDataPro=choDepositRateService.getChoProductReqForReportDate(corpIds,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);
                returnData=returnDataFin.add(returnDataPro);
            }
        }
        return returnData;
    }
}
