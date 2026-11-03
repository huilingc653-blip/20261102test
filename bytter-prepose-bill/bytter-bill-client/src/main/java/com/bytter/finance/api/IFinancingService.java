package com.bytter.finance.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFinancingService {

    /**
     * 获取贷款余额之和
     * @param corpId   单位
     * @param bankCodes  机构对应的银行
     * @param endDate    结束时间
     * @return
     */
    public Map<String, BigDecimal> getSumProvideBal(String corpId, List<String> bankCodes, List<String> bankTypeIds, Date endDate);

    /**
     * 获取一段时间内总行与支行的发放总额
     * @param corpId
     * @param bankCodes
     * @param bankTypeIds
     * @param StartDate
     * @param endDate
     * @return
     */
    public Map<String,BigDecimal> getSumProvideSendMoney(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date StartDate,Date endDate);

    /**
     * 获取一段时间内总行与支行的平均发放利率
     * @param corpId
     * @param bankCodes
     * @param bankTypeIds
     * @param StartDate
     * @param endDate
     * @return
     */
    public Map<String,BigDecimal> getAvgProvideSendRate(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date StartDate,Date endDate);

    /**
     * 获取机构子啊多个单位的，报表日期的借款余额合计
     * @param corpIds
     * @param reportDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getProvideBalForReportDate(List<String> corpIds, Date reportDate,String bankTypeId,String bankCode);
}
