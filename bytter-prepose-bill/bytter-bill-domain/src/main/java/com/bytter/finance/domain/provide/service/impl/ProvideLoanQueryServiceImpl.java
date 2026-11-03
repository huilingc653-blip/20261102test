package com.bytter.finance.domain.provide.service.impl;
import com.bytter.finance.api.IDirectLoanQueryService;
import com.bytter.finance.api.IProvideLoanQueryService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 借款融资查询接口 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/12/8 0008 <br>
 */
@Service("provideLoanQueryServiceImpl")
@Transactional
@DubboService
public class ProvideLoanQueryServiceImpl implements IProvideLoanQueryService {
    @Autowired
    private CmsProvideLoanSendService sendService;
    @Autowired
    private IDirectLoanQueryService directService;
    @Override
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate) {
        return sendService.queryProvideFinance(corpAuth, curId, queryDate);
    }
    @Override
    public List<FinanceLoanDTO> queryInlFinance(List<String> corpAuth, String curId, Date queryDate) {
        return directService.queryInlFinance(corpAuth, curId, queryDate);
    }
    @Override
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        return sendService.queryInterestFinance(corpAuth, curId, queryDate);
    }
    @Override
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        return sendService.queryProvideTypeFinance(corpAuth, curId, queryDate);
    }
    @Override
    public List<FinanceTypeLoanDTO> queryInlTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        return directService.queryInlTypeFinance(corpAuth, curId, queryDate);
    }
    @Override
    public List<FinanceLoanDTO> queryInlInterest(List<String> corpAuth, String curId, Date queryDate) {
        return directService.queryInlInterest(corpAuth, curId, queryDate);
    }
}
