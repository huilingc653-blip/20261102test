package com.bytter.finance.domain.security.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.security.dao.CmsSecurityToAccountDao;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.entity.CmsSecurityToAccount;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.finance.domain.security.vo.CmsSecurityToAccountVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 资产证券化发行
 *
 * @author tongqr
 */
@Service("cmsSecurityToAccountService")
@WorkFlow(CmsWorkFlowConstant.CMS_SECURITY_TOACCOUNT)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_SECURITY_TOACCOUNT)
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityToAccountService extends BaseService<CmsSecurityToAccount, String>
        implements IWorkFlowListener {
    @Autowired
    private CmsSecurityToAccountDao cmsSecurityToAccountDao;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsSecurityPlanDetailService cmsSecurityPlanDetailService;
    @Autowired
    private CmsSecurityRepaySureService cmsSecurityRepaySureService;
    @Override
    public BaseDao<CmsSecurityToAccount, String> getEntityDao() {
        return cmsSecurityToAccountDao;
    }
    /**
     * 资产证券化登记主信息查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTableInfo(DataTablesInput dataTablesInput, ICurrentUser user,
            List<String> bizStatus) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_SECURITY_TOACCOUNT_QUERY, user.getId());
        return cmsSecurityToAccountDao.getDataTableInfo(dataTablesInput, corpAuth, bizStatus);
    }
    /**
     * 资产证券化到账确认审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化到账确认审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_TOACCOUNT, input);
        DataTablesOutput dp = cmsSecurityToAccountDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 到账确认的数据
     *
     * @param Code
     *            资产证券化编号
     * @return 到账确认列表
     */
    public List<CmsSecurityToAccountVO> getIssueByCode(String Code) {
        return cmsSecurityToAccountDao.getIssueByCode(Code);
    }
    /**
     * 新增资产证券化到账确认信息
     *
     * @param toAccount
     *            到账确认信息
     * @param user
     *            当前用户
     */
    public void save(CmsSecurityToAccount toAccount, ICurrentUser user) {
        toAccount.setCreateBy(user.getUsername());
        toAccount.setCreateDate(new Date());
        toAccount.setStatus(CmsConstant.COMMITAPPROVE);
        this.saveEntity(toAccount);
        CmsSecurityIssue securityIssue = cmsSecurityIssueService
                .getSecurityIssueByCode(toAccount.getSecurityIssueCode(), null).get(0);
        securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_21);
        securityIssue.setReToAccountMoney(securityIssue.getReToAccountMoney().subtract(toAccount.getToAccountMoney()));
        cmsSecurityIssueService.merge(securityIssue);
         getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_TOACCOUNT, toAccount.getId(), toAccount,
                toAccount.getCorpId(), user.getId());
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsSecurityToAccount securityToAccount = this.get(businessKey,true);
        CmsSecurityIssue securityIssue = cmsSecurityIssueService
                .getSecurityIssueByCode(securityToAccount.getSecurityIssueCode(), null).get(0);
        // 无审批流事务未及时更新解决办法
        securityIssue = cmsSecurityIssueService.get(securityIssue.getId());
        if (result) {
            // 审批通过
            securityToAccount.setStatus(CmsConstant.APPROVED);
        } else {
            // 打回
            securityToAccount.setStatus(CmsConstant.BACK);
            securityIssue.setReToAccountMoney(
                    securityIssue.getReToAccountMoney().add(securityToAccount.getToAccountMoney()));
        }
        if (securityIssue.getReToAccountMoney().compareTo(BigDecimal.ZERO) <= 0) {
            List<CmsSecurityPlanDetailVO> detailVOS = cmsSecurityPlanDetailService
                    .getPlanDetailByCode(securityIssue.getSecurityIssueCode());
            if (detailVOS.size() > 0) {// 已经维护过计划
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_97);
                cmsSecurityRepaySureService.addRepaySure(securityIssue.getSecurityIssueCode());// 生成还款确认数据
            } else {
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_96);
            }
        } else {
            securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_25);
        }
        cmsSecurityIssueService.merge(securityIssue);
        this.updateEntity(securityToAccount);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
