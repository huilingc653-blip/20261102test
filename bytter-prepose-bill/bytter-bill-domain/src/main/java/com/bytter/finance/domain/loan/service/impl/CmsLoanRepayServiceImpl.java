package com.bytter.finance.domain.loan.service.impl;

import com.bytter.finance.api.ICmsLoanRepayService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luoyj
 * @date 2023/11/21.
 */
@Service("cmsLoanRepayServiceImpl")
@DubboService
@Transactional
public class CmsLoanRepayServiceImpl implements ICmsLoanRepayService {
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;

    @Override
    public void setRepayMentScheduleIsRepay(String repaymentId, String isRepay) {
        cmsLoanRepayService.setRepayMentScheduleIsRepay(repaymentId,isRepay);
    }
}
