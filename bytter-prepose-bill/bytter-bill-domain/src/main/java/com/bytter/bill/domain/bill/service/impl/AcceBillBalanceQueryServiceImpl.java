package com.bytter.bill.domain.bill.service.impl;
import com.bytter.bill.domain.bill.service.AbBillQueryService;
import com.bytter.bill.api.bill.dto.FinanceLoanDTO;
import com.bytter.bill.api.bill.dto.FinanceTypeLoanDTO;
import com.bytter.bill.api.bill.service.IAcceBillBalanceQueryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 应付票据融资查询接口 <br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2020/12/8  <br>
 */
@Service("acceBillBalanceQueryServiceImpl")
@Transactional
@DubboService
public class AcceBillBalanceQueryServiceImpl implements IAcceBillBalanceQueryService {
    @Autowired
    private AbBillQueryService abBillQueryService;
    /**
     * <br>
     * <b>功能：</b> 查询应付票据余额融资情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/8 <br>
     */
    @Override
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate) {
        return abBillQueryService.queryProvideFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询应付票据敞口余额情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/8 <br>
     */
    @Override
    public List<FinanceLoanDTO> queryExposureFinance(List<String> corpAuth, String curId, Date queryDate) {
        return abBillQueryService.queryProvideFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询票据利息支出情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/10 <br>
     */
    @Override
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        return abBillQueryService.queryInterestFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询票据融资品种融资余额（第三方票据品种）,融资品种融资余额、保证金金额、敞口金额（票据金额-保证金） <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/11 <br>
     */
    @Override
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        return abBillQueryService.queryProvideTypeFinance(corpAuth, curId, queryDate);
    }
}
