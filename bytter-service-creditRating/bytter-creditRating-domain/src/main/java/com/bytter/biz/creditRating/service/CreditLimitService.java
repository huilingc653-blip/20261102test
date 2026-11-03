package com.bytter.biz.creditRating.service;


import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.api.ICreditLimitService;
import com.bytter.credit.dto.RatBkrationDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 授信额度数据源服务层
 */
@Service("creditLimitService")
public class CreditLimitService extends BaseService {

    @DubboReference(check = false)
    private ICreditLimitService creditLimitService;
    @Autowired
    private CrdRawDataService crdRawDataService;
    @Autowired
    private CrInstitutionalRawDataService institutionalRawDataService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 获取组织与机构之间的授信敞口额度
     *
     * @param corpId           组织id
     * @param institutionIds   机构id
     * @param scoringStartDate 评价开始日期
     * @param scoringEndDate   评价结束如期
     * @return 金额
     */
    public Map<String, BigDecimal> getCreditExposureLimit(String corpId, List<String> institutionIds, String scoringStartDate, String scoringEndDate) {
        Map<String, BigDecimal> map = new HashMap<>();
        //获取机构信息
        List<InstitutionDetailsVo> institutionList = crdRawDataService.findInstitutionByIds(institutionIds);
        countCreditExposureLimit(corpId, institutionList, scoringStartDate, scoringEndDate, map);
        return map;
    }

    /**
     * 获取组织与机构之间的授信额度增减变化
     *
     * @param corpId           组织id
     * @param institutionIds   机构id
     * @param scoringStartDate 评价开始日期
     * @param scoringEndDate   评价结束如期
     * @return 结果
     */
    public Map<String, BigDecimal> getCreditLimitChange(String corpId, List<String> institutionIds, String scoringStartDate, String scoringEndDate) {
        Map<String, BigDecimal> map = new HashMap<>();
        //获取机构信息
        List<InstitutionDetailsVo> institutionList = crdRawDataService.findInstitutionByIds(institutionIds);
        countCreditLimitChange(corpId, institutionList, scoringStartDate, scoringEndDate, map);
        return map;
    }

    /**
     * 计算组织与机构之间的授信敞口额度
     *
     * @param corpId           单位id
     * @param institutionList  已维护机构信息集合
     * @param scoringStartDate 评价开始日期
     * @param scoringEndDate   评价结束日期
     * @param resultMap        结果Map
     */
    public void countCreditExposureLimit(String corpId, List<InstitutionDetailsVo> institutionList, String scoringStartDate, String scoringEndDate, Map<String, BigDecimal> resultMap) {
        Map<String, List<InstitutionDetailsVo>> institutionMap = institutionList.stream().collect(Collectors.groupingBy(InstitutionDetailsVo::getInstitutionType));
        //总行
        if (institutionMap.containsKey(CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE)) {
            List<InstitutionDetailsVo> institutionDetailsList = institutionMap.get(CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE);
            List<String> bankTypeList = institutionDetailsList.stream().map(InstitutionDetailsVo::getBankTypeId).distinct().collect(Collectors.toList());
            //获取授信数据
            List<RatBkrationDTO> beanList = creditLimitService.countCreditExposureLimitByBankType(corpId, scoringEndDate, bankTypeList);
            Map<String, RatBkrationDTO> ratBkrationMap = beanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankType, Function.identity()));
            for (InstitutionDetailsVo institutionById : institutionDetailsList) {
                if (ratBkrationMap.containsKey(institutionById.getBankTypeId())) {
                    resultMap.put(institutionById.getInsId(), ratBkrationMap.get(institutionById.getBankTypeId()).getMoney());
                }
            }
        }

        //支行
        if (institutionMap.containsKey(CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO)) {
            List<InstitutionDetailsVo> institutionDetailsList = institutionMap.get(CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO);
            List<String> bankCodeList = institutionDetailsList.stream().map(InstitutionDetailsVo::getBankCode).distinct().collect(Collectors.toList());
            //获取授信数据
            List<RatBkrationDTO> beanList = creditLimitService.countCreditExposureLimitByBankCode(corpId, scoringEndDate, bankCodeList);
            Map<String, RatBkrationDTO> ratBkrationMap = beanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankCode, Function.identity()));
            for (InstitutionDetailsVo institutionById : institutionDetailsList) {
                if (ratBkrationMap.containsKey(institutionById.getBankCode())) {
                    resultMap.put(institutionById.getInsId(), ratBkrationMap.get(institutionById.getBankCode()).getMoney());
                }
            }
        }
    }

    /**
     * 计算组织与机构之间的授信额度增减变化
     *
     * @param corpId           单位id
     * @param institutionList  机构信息
     * @param scoringStartDate 开始日期
     * @param scoringEndDate   结束日期
     */
    public void countCreditLimitChange(String corpId, List<InstitutionDetailsVo> institutionList, String scoringStartDate, String scoringEndDate, Map<String, BigDecimal> resultMap) {
        Map<String, List<InstitutionDetailsVo>> institutionMap = institutionList.stream().collect(Collectors.groupingBy(InstitutionDetailsVo::getInstitutionType));
        //总行
        if (institutionMap.containsKey(CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE)) {
            List<InstitutionDetailsVo> institutionDetailsList = institutionMap.get(CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE);
            List<String> bankTypeList = institutionDetailsList.stream().map(InstitutionDetailsVo::getBankTypeId).distinct().collect(Collectors.toList());

            List<RatBkrationDTO> endBeanList = creditLimitService.countCreditExposureLimitByBankType(corpId, scoringEndDate, bankTypeList);
            Map<String, RatBkrationDTO> endRatBkrationMap = endBeanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankType, Function.identity()));
            List<RatBkrationDTO> staBeanList = creditLimitService.countCreditExposureLimitByBankType(corpId, scoringStartDate, bankTypeList);
            Map<String, RatBkrationDTO> staRatBkrationMap = staBeanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankType, Function.identity()));
            for (InstitutionDetailsVo institutionById : institutionDetailsList) {
                BigDecimal endMoney = BigDecimal.ZERO;
                BigDecimal staMoney = BigDecimal.ZERO;
                if (endRatBkrationMap.containsKey(institutionById.getBankTypeId())) {
                    endMoney = endRatBkrationMap.get(institutionById.getBankTypeId()).getMoney();
                }
                if (staRatBkrationMap.containsKey(institutionById.getBankTypeId())) {
                    staMoney = staRatBkrationMap.get(institutionById.getBankTypeId()).getMoney();
                }
                resultMap.put(institutionById.getInsId(), endMoney.subtract(staMoney));
            }
        }

        //支行
        if (institutionMap.containsKey(CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO)) {
            List<InstitutionDetailsVo> institutionDetailsList = institutionMap.get(CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO);
            List<String> bankCodeList = institutionDetailsList.stream().map(InstitutionDetailsVo::getBankCode).distinct().collect(Collectors.toList());
            List<RatBkrationDTO> endBeanList = creditLimitService.countCreditExposureLimitByBankCode(corpId, scoringEndDate, bankCodeList);
            Map<String, RatBkrationDTO> endRatBkrationMap = endBeanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankCode, Function.identity()));
            List<RatBkrationDTO> staBeanList = creditLimitService.countCreditExposureLimitByBankCode(corpId, scoringStartDate, bankCodeList);
            Map<String, RatBkrationDTO> staRatBkrationMap = staBeanList.stream().collect(Collectors.toMap(RatBkrationDTO::getBankCode, Function.identity()));
            for (InstitutionDetailsVo institutionById : institutionDetailsList) {
                BigDecimal endMoney = BigDecimal.ZERO;
                BigDecimal staMoney = BigDecimal.ZERO;
                if (endRatBkrationMap.containsKey(institutionById.getBankCode())) {
                    endMoney = endRatBkrationMap.get(institutionById.getBankCode()).getMoney();
                }
                if (staRatBkrationMap.containsKey(institutionById.getBankCode())) {
                    staMoney = staRatBkrationMap.get(institutionById.getBankCode()).getMoney();
                }
                resultMap.put(institutionById.getInsId(), endMoney.subtract(staMoney));
            }
        }
    }

    /**
     * 计算报表日期授信额度之和（某机构，在这些单位的合计）
     */
    public BigDecimal getSumCreditBalForReportDate(String institutionalId, List<String> corpIds, Date scoringEndDate){
        //获取机构信息
        CrInstitutionalRawData crInstitutionalRawData=institutionalRawDataService.get(institutionalId);
        BigDecimal returnData= BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(crInstitutionalRawData)) {
            //根据机构是总行还是支行获取对应授信余额
            if (crInstitutionalRawData.getBankCode() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(crInstitutionalRawData.getInstitutionType())) {
                //支行
                returnData=creditLimitService.getSumCreditBalForReportDate(corpIds,scoringEndDate,null,crInstitutionalRawData.getBankCode());
            }else if(crInstitutionalRawData.getBankTypeId() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(crInstitutionalRawData.getInstitutionType())) {
                //总行
                returnData=creditLimitService.getSumCreditBalForReportDate(corpIds,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);

            }
        }
        return returnData;
    }

}
