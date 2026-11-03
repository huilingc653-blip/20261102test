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
import com.bytter.finance.domain.security.dao.CmsSecurityRepaySureDao;
import com.bytter.finance.domain.security.entity.*;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.finance.domain.security.vo.CmsSecurityRepaySureVO;
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
import java.util.ArrayList;
import java.util.List;
/**
 * 资产证券化还款确认
 *
 * @author tongqr
 */
@WorkFlow({ CmsWorkFlowConstant.CMS_SECURITY_REPAYSURE })
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_SECURITY_REPAYSURE )
@Service("cmsSecurityRepaySureService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityRepaySureService extends BaseService<CmsSecurityRepaySure, String>
        implements IWorkFlowListener {
    @Autowired
    private CmsSecurityRepaySureDao cmsSecurityRepaySureDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsSecurityPlanDetailService cmsSecurityPlanDetailService;
    @Autowired
    private CmsSecurityPlanService cmsSecurityPlanService;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Override
    public BaseDao<CmsSecurityRepaySure, String> getEntityDao() {
        return cmsSecurityRepaySureDao;
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
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_SECURITY_PLAN_QUERY, user.getId());
        return cmsSecurityRepaySureDao.getDataTableInfo(dataTablesInput, corpAuth, bizStatus);
    }
    /**
     * 资产证券化还款确认审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化到账确认审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_REPAYSURE, input);
        DataTablesOutput dp = cmsSecurityRepaySureDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 获取主信息对应的还款确认信息
     */
    public List<CmsSecurityRepaySure> listRepaySure(CmsSecurityRepaySureVO vo) {
        return cmsSecurityRepaySureDao.listRepaySure(vo);
    }
    /**
     * 获取全部还款确认信息
     */
    public List<CmsSecurityRepaySure> listRepaySureView(CmsSecurityRepaySureVO vo) {
        return cmsSecurityRepaySureDao.listRepaySureView(vo);
    }
    /**
     * 计划生成完成后，新增借款还款数据
     */
    public void addRepaySure(String securityIssueCode) {
        cmsSecurityRepaySureDao.deleteBySecurityIssueCode(securityIssueCode);// 将未还的数据删掉，重新添加
        List<CmsSecurityPlanDetailVO> detailVOS = cmsSecurityPlanDetailService.getPlanDetailByCode(securityIssueCode);
        CmsSecurityPlan cmsSecurityPlan;
        CmsSecurityIssue cmsSecurityIssue;
        CmsSecurityInfo cmsSecurityInfo;
        List<CmsSecurityRepaySure> repaySureList = new ArrayList<>();
        if (detailVOS.size() > 0) {
            for (CmsSecurityPlanDetailVO vo : detailVOS) {
                if (!CmsConstant.IS_TRUE.equals(vo.getIsRepay())) {
                    CmsSecurityRepaySure repaySure = new CmsSecurityRepaySure();
                    cmsSecurityPlan = cmsSecurityPlanService.get(vo.getSecurityPlanId());
                    cmsSecurityIssue = cmsSecurityIssueService.getSecurityIssueByCode(vo.getSecurityIssueCode(), null)
                            .get(0);
                    cmsSecurityInfo = cmsSecurityInfoService.get(cmsSecurityIssue.getSecurityInfoId());
                    repaySure.setSecurityPlanId(cmsSecurityPlan.getId());
                    repaySure.setSecurityPlanDetailId(vo.getId());
                    repaySure.setSecurityIssueCode(vo.getSecurityIssueCode());
                    repaySure.setSecurityInfoId(cmsSecurityInfo.getId());
                    repaySure.setCorpId(cmsSecurityInfo.getCorpId());
                    repaySure.setLevelName(cmsSecurityIssue.getLevelName());
                    repaySure.setProductName(cmsSecurityIssue.getProductName());
                    repaySure.setRepayDate(vo.getRepayDate());
                    repaySure.setPlanRepayDate(vo.getPlanRepayDate());
                    repaySure.setRepayMoney(vo.getRepayMoney());
                    repaySure.setRate(cmsSecurityIssue.getParRate());
                    repaySure.setRepayInterest(vo.getRepayInterest());
                    repaySure.setRepayAmount(vo.getRepayAmount());
                    repaySure.setRemainMoney(vo.getRemainMoney());
                    repaySure.setIsRepay(vo.getIsRepay());
                    repaySure.setIsChose(CmsConstant.IS_FALSE);
                    repaySure.setRmk(vo.getRmk());
                    repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE_0);
                    repaySureList.add(repaySure);
                }
            }
        }
        if (repaySureList.size() > 0) {
            this.batchSave(repaySureList);
        }
    }
    /**
     * 对数据的选择
     *
     * @param vo
     * @return
     */
    public void addPaySure(CmsSecurityRepaySureVO vo) {
        CmsSecurityRepaySure repaySure = this.get(vo.getId());
        repaySure.setRepayDate(vo.getRepayDate());
        repaySure.setRepayMoney(vo.getRepayMoney());
        repaySure.setRepayInterest(vo.getRepayInterest());
        repaySure.setRate(vo.getRate());
        repaySure.setRmk(vo.getRmk());
        repaySure.setRepayAmount(vo.getRepayMoney().add(vo.getRepayInterest()));
        repaySure.setIsChose(CmsConstant.IS_TRUE);
        repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE_1);
        this.updateEntity(repaySure);
    }
    /**
     * 对选择数据进行删除
     *
     * @param vo
     * @return
     */
    public void deletePaySure(CmsSecurityRepaySureVO vo) {
        CmsSecurityRepaySure repaySure = this.get(vo.getId());
        repaySure.setIsChose(CmsConstant.IS_FALSE);
        repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE_0);
        this.updateEntity(repaySure);
    }
    /**
     * 对已选数据的保存
     */
    public void save(CmsSecurityRepaySure repaySure, ICurrentUser user) {
        repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE_3);
        repaySure.setIsChose(CmsConstant.IS_FALSE);
        CmsSecurityIssue securityIssue = cmsSecurityIssueService
                .getSecurityIssueByCode(repaySure.getSecurityIssueCode(), null).get(0);
        securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_51);
        cmsSecurityIssueService.merge(securityIssue);
        this.updateEntity(repaySure);
         getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_REPAYSURE, repaySure.getId(), repaySure,
                repaySure.getCorpId(), user.getId());
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsSecurityRepaySure repaySure = this.get(businessKey,true);
        CmsSecurityIssue securityIssue = cmsSecurityIssueService
                .getSecurityIssueByCode(repaySure.getSecurityIssueCode(), null).get(0);
        // 无审批流事务未及时更新解决办法
        securityIssue = cmsSecurityIssueService.get(securityIssue.getId());
        CmsSecurityPlanDetail detail = cmsSecurityPlanDetailService.get(repaySure.getSecurityPlanDetailId());
        if (result) {
            // 审批通过
            repaySure.setIsRepay(CmsConstant.IS_TRUE);
            // 计划更改状态
            detail.setIsRepay(CmsConstant.IS_TRUE);
            repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE_5);
            if (repaySure.getRemainMoney().compareTo(BigDecimal.ZERO) <= 0) {
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_100);
            } else {
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_55);
            }
        } else {
            // 打回
            repaySure.setStatus(CmsConstant.SECURITY_REPAY_SURE__1);
            securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_55);
        }
        cmsSecurityPlanDetailService.merge(detail);
        cmsSecurityIssueService.merge(securityIssue);
        this.updateEntity(repaySure);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
