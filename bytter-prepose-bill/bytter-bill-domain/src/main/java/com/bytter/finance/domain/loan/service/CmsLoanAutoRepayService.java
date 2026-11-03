package com.bytter.finance.domain.loan.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.loan.dao.CmsLoanRepayDao;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("cmsLoanAutoRepayServiceBase")
@Transactional
public class CmsLoanAutoRepayService extends BaseService<CmsLoanRepay, String> implements ITaskService {
    @Autowired
    private CmsLoanRepayDao cmsLoanRepayDao;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;
    @DubboReference(check = false)
    private IUserService userServiceImpl;

    @Override
    public BaseDao<CmsLoanRepay, String> getEntityDao() {
        return cmsLoanRepayDao;
    }

    @Override
    @XxlJob("CmsLoanAutoRepayService")
    public void run(String arguments) {
        try {
            this.logger.info("---启用内部贷款自动还款定时任务---");
            List<CmsLoanSendVO> loanRepayVOList = cmsLoanSendService.getAutoDataAppTables();// 贷款自动还款数据
            if (!EmptyUtils.isEmpty(loanRepayVOList)) {
                for (CmsLoanSendVO cmsLoanSendVO : loanRepayVOList) {
                    cmsLoanSendService.setNewSchedule(cmsLoanSendVO);
                    if (!EmptyUtils.isEmpty(cmsLoanSendVO.getRepaymentScheduleVO()) && DateUtils
                            .format(cmsLoanSendVO.getRepaymentScheduleVO().getPrediceRepayDate(), "yyyy-MM-dd")
                            .equals(DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd"))) {// 还款日是今天
                        CmsLoanRepay cmsLoanRepay = new CmsLoanRepay();
                        cmsLoanRepay.setCorpId(cmsLoanSendVO.getCorpId());
                        cmsLoanRepay.setContractCode(cmsLoanSendVO.getContractCode());
                        cmsLoanRepay.setSendCode(cmsLoanSendVO.getBillCode());
                        cmsLoanRepay.setRepayMoney(cmsLoanSendVO.getRepaymentScheduleVO().getRepaymentMoney());
                        cmsLoanRepay.setRepayDate(cmsLoanSendVO.getRepaymentScheduleVO().getPrediceRepayDate());
                        cmsLoanRepay.setRepayway(cmsLoanSendVO.getRepayType());
                        cmsLoanRepay.setLoanAcc(cmsLoanSendVO.getLoanAcc());
                        cmsLoanRepay.setRepayAcc(cmsLoanSendVO.getProvideLoanAcc());
                        cmsLoanRepay.setRepayType("2");
                        cmsLoanRepay.setStatus(1);
                        cmsLoanRepay.setRepaymentScheduleId(cmsLoanSendVO.getRepaymentScheduleVO().getId());
                        cmsLoanRepay.setRmk("自动还款");
                        cmsLoanRepay.setCreateDate(new Date());
                        ICurrentUser user = userServiceImpl.getScheduler();
                        cmsLoanRepayService.saveLoanRepay(cmsLoanRepay, user);
                    }
                }
            }
            this.logger.info("---结束内部贷款自动还款定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
