package com.bytter.biz.creditRating.service;


import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/1/2<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("bankAccountService")
public class BankAccountService extends BaseService {

    @Autowired
    private CrInstitutionalRawDataService institutionalRawDataService;

    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 计算日期账户余额之和（某机构，在这些单位的合计）
     */
    public BigDecimal getSumBankAccBalForReportDate(String institutionalId, List<String> corpIds, Date scoringEndDate){
        //获取机构信息
        CrInstitutionalRawData crInstitutionalRawData=institutionalRawDataService.get(institutionalId);
        BigDecimal returnData= BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(crInstitutionalRawData)) {
            //根据机构是总行还是支行获取对应账户余额
            if (crInstitutionalRawData.getBankCode() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(crInstitutionalRawData.getInstitutionType())) {
                //支行
                returnData=bisAccBalService.getBankAccBalForReportDate(corpIds,scoringEndDate,null,crInstitutionalRawData.getBankCode());
            }else if(crInstitutionalRawData.getBankTypeId() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(crInstitutionalRawData.getInstitutionType())) {
                //总行
                returnData=bisAccBalService.getBankAccBalForReportDate(corpIds,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);

            }
        }
        return returnData;
    }

    public BigDecimal getSumBankAccDtlForReportDate(String institutionalId, List<String> corpIds,Date scoringStartDate, Date scoringEndDate){
        //获取机构信息
        CrInstitutionalRawData crInstitutionalRawData=institutionalRawDataService.get(institutionalId);
        BigDecimal returnData= BigDecimal.ZERO;
        if(!EmptyUtils.isEmpty(crInstitutionalRawData)) {
            //根据机构是总行还是支行获取对应账户收支
            if (crInstitutionalRawData.getBankCode() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(crInstitutionalRawData.getInstitutionType())) {
                //支行
                returnData=bisAccDtlService.getBnakAccDtlForReportDate(corpIds,scoringStartDate,scoringEndDate,null,crInstitutionalRawData.getBankCode());
            }else if(crInstitutionalRawData.getBankTypeId() != null &&
                    CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(crInstitutionalRawData.getInstitutionType())) {
                //总行
                returnData=bisAccDtlService.getBnakAccDtlForReportDate(corpIds,scoringStartDate,scoringEndDate,crInstitutionalRawData.getBankTypeId(),null);

            }
        }
        return returnData;
    }
}
