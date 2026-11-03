package com.bytter.increment.api.service;

import com.bytter.increment.api.dto.ChoDepositRateDTO;
import com.bytter.increment.api.dto.ChoProductReqQueryDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IChoDepositRateService {

    /**
     * 查询存款的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoDepositRateDTO> queryDepositList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate);

    /**
     * 查询理财类型为非保本理财的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoDepositRateDTO> queryNotChoList(String corpId,String bankCode,String bankTypeId,String scoringStartDate,String scoringEndDate);

    /**
     * 查询理财类型为保本理财的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoProductReqQueryDTO> queryChoList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate);

    /**
     * 存款类业务查询剩余本金合计
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getChoFinanceForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode);

    /**
     *理财类业务查询待赎回金额合计
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getChoProductReqForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode);
}
