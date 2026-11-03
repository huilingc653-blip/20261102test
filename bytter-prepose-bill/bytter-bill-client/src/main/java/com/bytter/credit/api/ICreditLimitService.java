package com.bytter.credit.api;

import com.bytter.credit.dto.RatBkrationDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICreditLimitService {

    /**
     * 查询总行历史数据
     *
     * @param corpId         单位id
     * @param scoringEndDate 评价结束时间
     * @param bankTypeList   银行类别
     * @return 授信数据
     */
    public List<RatBkrationDTO> countCreditExposureLimitByBankType(String corpId, String scoringEndDate, List<String> bankTypeList);

    public List<RatBkrationDTO> countCreditExposureLimitByBankCode(String corpId, String scoringEndDate, List<String> bankCodeList);

    /**
     * 获取某机构在报表日期多个单位的授信额合计
     */
    public BigDecimal getSumCreditBalForReportDate(List<String> corpIds, Date scoringEndDate, String bankTypeId, String bankCode);
}
