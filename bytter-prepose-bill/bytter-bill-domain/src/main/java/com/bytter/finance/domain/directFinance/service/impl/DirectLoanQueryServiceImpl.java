package com.bytter.finance.domain.directFinance.service.impl;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceInfoService;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.finance.api.IDirectLoanQueryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 直接融资查询实现类 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/12/8 0008 <br>
 */
@Service("DirectLoanQueryServiceImpl")
@Transactional
@DubboService
public class DirectLoanQueryServiceImpl implements IDirectLoanQueryService {
    @Autowired
    private DfDirectFinanceInfoService directService;
    @Override
    public List<FinanceLoanDTO> queryInlFinance(List<String> corpAuth, String curId, Date queryDate) {
        return directService.queryInlFinance(corpAuth, curId, queryDate);
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
