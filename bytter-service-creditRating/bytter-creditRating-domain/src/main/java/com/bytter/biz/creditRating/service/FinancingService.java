package com.bytter.biz.creditRating.service;


import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;

import com.bytter.finance.api.IFinancingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 融资数据源服务
 */
@Service("financingService")
public class FinancingService extends BaseService {

    @DubboReference(check = false)
    private IFinancingService financingService;
    @Autowired
    private CrdRawDataService crdRawDataService;
    @Autowired
    private CrInstitutionalRawDataService institutionalRawDataService;
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
    /**
     *
     * @param corpId
     * @param institutionIds
     * @param scoringStartDate
     * @param scoringEndDate
     * @param type 获取组织与机构之间 1:贷款余额之和 2:发放额度合计 3:发放利率平均值
     * @return
     */
    public Map<String, BigDecimal> getFinancingdata(String corpId, List<String> institutionIds, Date scoringStartDate, Date scoringEndDate,String type){
        Map<String, BigDecimal> returnMap = new HashMap<>();
        //获取机构信息
        List<InstitutionDetailsVo> institutionDetailsVoList = crdRawDataService.findInstitutionByIds(institutionIds);
        List<String> bankCodes=new ArrayList<>();//支行code集合
        List<String> bankTypes=new ArrayList<>();//总行类型集合
        Map<String, BigDecimal> bankCodeReturnMap=new HashMap<>();
        Map<String, BigDecimal> bankTypeReturnMap=new HashMap<>();
        for(InstitutionDetailsVo item:institutionDetailsVoList){
            if (item.getBankCode() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(item.getInstitutionType())) {
                //分行处理逻辑
                bankCodes.add(item.getBankCode());
            }
            if (item.getBankTypeId() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(item.getInstitutionType())) {
                //总行处理逻辑
                bankTypes.add(item.getBankTypeId());
            }
        }
        if(bankCodes.size()>0){
            if(CreditRatingConstant.FINANCING_TYPE_1.equals(type)) {
                bankCodeReturnMap = financingService.getSumProvideBal(corpId, bankCodes, null, scoringEndDate);
            }else if(CreditRatingConstant.FINANCING_TYPE_2.equals(type)) {
                bankCodeReturnMap = financingService.getSumProvideSendMoney(corpId, bankCodes, null, scoringStartDate,scoringEndDate);
            }else if(CreditRatingConstant.FINANCING_TYPE_3.equals(type)) {
                bankCodeReturnMap = financingService.getAvgProvideSendRate(corpId, bankCodes, null, scoringStartDate,scoringEndDate);
            }
        }
        if(bankTypes.size()>0){

            if(CreditRatingConstant.FINANCING_TYPE_1.equals(type)) {
                bankTypeReturnMap = financingService.getSumProvideBal(corpId, null, bankTypes, scoringEndDate);
            }else if(CreditRatingConstant.FINANCING_TYPE_2.equals(type)) {
                bankTypeReturnMap = financingService.getSumProvideSendMoney(corpId, null, bankTypes, scoringStartDate,scoringEndDate);
            }else if(CreditRatingConstant.FINANCING_TYPE_3.equals(type)) {
                bankTypeReturnMap = financingService.getAvgProvideSendRate(corpId, null, bankTypes, scoringStartDate,scoringEndDate);
            }
        }

        for(InstitutionDetailsVo item:institutionDetailsVoList){
            if (item.getBankCode() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(item.getInstitutionType())) {
                //分行处理逻辑
                if(bankCodeReturnMap.containsKey(item.getBankCode())) {
                    returnMap.put(item.getInsId(), bankCodeReturnMap.get(item.getBankCode()));
                }else {
                    returnMap.put(item.getInsId(), BigDecimal.ZERO);
                }
            }
            if (item.getBankTypeId() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(item.getInstitutionType())) {
                //总行处理逻辑
                if(bankTypeReturnMap.containsKey(item.getBankTypeId())) {
                    returnMap.put(item.getInsId(), bankTypeReturnMap.get(item.getBankTypeId()));
                }else {
                    returnMap.put(item.getInsId(), BigDecimal.ZERO);
                }
            }
        }
        return returnMap;
    }

    /**
     * 计算报表日期借款余额之和（某机构，在这些单位的合计）
     */
    public BigDecimal getSumProvideBalForReportDate(String institutionalId, List<String> corpIds, Date scoringEndDate){
        //获取机构信息
        CrInstitutionalRawData crInstitutionalRawData=institutionalRawDataService.get(institutionalId);
        BigDecimal returnData= BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(crInstitutionalRawData)) {
            //根据机构是总行还是支行获取对应借款余额
            if (crInstitutionalRawData.getBankCode() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(crInstitutionalRawData.getInstitutionType())) {
                //支行
                returnData=financingService.getProvideBalForReportDate(corpIds,scoringEndDate,null,crInstitutionalRawData.getBankCode());
            }else if(crInstitutionalRawData.getBankTypeId() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(crInstitutionalRawData.getInstitutionType())) {
                //总行
                returnData=financingService.getProvideBalForReportDate(corpIds,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);

            }
        }
        return returnData;
    }
}
