package com.bytter.bill.api.bill.service;
import com.bytter.bill.api.bill.dto.FinanceLoanDTO;
import com.bytter.bill.api.bill.dto.FinanceTypeLoanDTO;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 票据融资查询接口 <br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2020/12/8 <br>
 */
public interface IAcceBillBalanceQueryService {
    /**
     * <br>
     * <b>功能：</b> 查询票据余额融资情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/8 <br>
     */
    List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate);
    /**
     * <br>
     * <b>功能：</b> 查询票据敞口余额情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/8 <br>
     */
    List<FinanceLoanDTO> queryExposureFinance(List<String> corpAuth, String curId, Date queryDate);
    /**
     * <br>
     * <b>功能：</b> 查询票据利息支出情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/10 <br>
     */
    List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate);
    /**
     * <br>
     * <b>功能：</b> 查询票据融资品种融资余额（第三方票据品种）,融资品种融资余额、保证金金额、敞口金额（票据金额-保证金） <br> <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/8 <br>
     */
    List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate);
}
