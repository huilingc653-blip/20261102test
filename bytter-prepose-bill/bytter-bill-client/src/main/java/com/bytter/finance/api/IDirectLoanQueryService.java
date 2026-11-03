package com.bytter.finance.api;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 直接融资查询接口 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/12/8 0008 <br>
 */
public interface IDirectLoanQueryService {
    /**
     * <br>
     * <b>功能：</b> 查询直接融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    List<FinanceLoanDTO> queryInlFinance(List<String> corpAuth, String curId, Date queryDate);
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的合计融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    List<FinanceTypeLoanDTO> queryInlTypeFinance(List<String> corpAuth, String curId, Date queryDate);
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的利息支持情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInlInterest(List<String> corpAuth, String curId, Date queryDate);
}
