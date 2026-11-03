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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.security.dao.CmsSecurityPlanDao;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.entity.CmsSecurityPlan;
import com.bytter.finance.domain.security.entity.CmsSecurityPlanDetail;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("cmsSecurityPlanService")
@WorkFlow({ CmsWorkFlowConstant.CMS_SECURITY_PLAN, CmsWorkFlowConstant.CMS_SECURITY_PLAN_UPDATE })
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityPlanService extends BaseService<CmsSecurityPlan, String> implements IWorkFlowListener {
    @Autowired
    private CmsSecurityPlanDao cmsSecurityPlanDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;
    @Autowired
    private CmsSecurityPlanDetailService cmsSecurityPlanDetailService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsSecurityRepaySureService cmsSecurityRepaySureService;
    @Override
    public BaseDao<CmsSecurityPlan, String> getEntityDao() {
        return cmsSecurityPlanDao;
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
        return cmsSecurityPlanDao.getDataTableInfo(dataTablesInput, corpAuth, bizStatus);
    }
    /**
     * 资产证券化登记主信息查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getupDataTableInfo(DataTablesInput dataTablesInput, ICurrentUser user,
            List<String> bizStatus) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_SECURITY_PLAN_UPDATE_QUERY,
                user.getId());
        return cmsSecurityPlanDao.getDataTableInfo(dataTablesInput, corpAuth, bizStatus);
    }
    /**
     * 资产证券化还款计划登记审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化到账确认审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_PLAN,
                input);
        DataTablesOutput dp = cmsSecurityPlanDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 资产证券化还款计划修改审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化到账确认审批列表数据
     */
    public DataTablesOutput dataTableUpdateApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_PLAN_UPDATE, input);
        DataTablesOutput dp = cmsSecurityPlanDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 :保存<br>
     *
     * @param vo
     */
    public void Save(CmsSecurityPlanVO vo, ICurrentUser user, String isPass) {
        CmsSecurityPlan cmsSecurityPlan;
        if (StringUtils.isNotEmpty(vo.getId())) {
            cmsSecurityPlan = get(vo.getId());
            if (user != null) {
                cmsSecurityPlan.setUpdateBy(user.getLoginName());
            }
            cmsSecurityPlan.setUpdateDate(new Date());
        } else {
            cmsSecurityPlan = new CmsSecurityPlan();
            if (user != null) {
                cmsSecurityPlan.setCreateBy(user.getLoginName());
            }
            cmsSecurityPlan.setCreateDate(new Date());
        }
        BeanUtils.copyNotNullProperties(vo, cmsSecurityPlan);
        if (EmptyUtils.isEmpty(isPass)) {
            cmsSecurityPlan.setStatus(CmsConstant.LEASE_COMMITAPPROVE_11);
        } else {
            cmsSecurityPlan.setStatus(CmsConstant.APPROVED);
        }
        if (StringUtils.isNotEmpty(vo.getId())) {
            // 更新计划主表数据
            cmsSecurityPlanDao.update(cmsSecurityPlan);
        } else {
            // 保存计划主表数据
            cmsSecurityPlanDao.save(cmsSecurityPlan);
        }
        // 更新ids
        List<String> detailUpdateIds = new ArrayList<>();
        // 新增集合
        List<CmsSecurityPlanDetail> planAddDetails = new ArrayList<>();
        // 更新集合
        List<CmsSecurityPlanDetail> planUpdateDetails = new ArrayList<>();
        // 组装计划明细表数据
        if (vo.getDetailDtl().size() > 0) {
            CmsSecurityPlanDetail securityPlanDetail;
            for (CmsSecurityPlanDetailVO planDetailVO : vo.getDetailDtl()) {
                if (!EmptyUtils.isEmpty(planDetailVO.getRepayDate())) {
                    if (StringUtils.isNotEmpty(planDetailVO.getId())) {
                        detailUpdateIds.add(planDetailVO.getId());
                        securityPlanDetail = cmsSecurityPlanDetailService.get(planDetailVO.getId());
                        BeanUtils.copyNotNullProperties(planDetailVO, securityPlanDetail);
                        if (EmptyUtils.isEmpty(securityPlanDetail.getIsRepay())) {
                            securityPlanDetail.setIsRepay(CmsConstant.IS_FALSE);
                        }
                        securityPlanDetail.setUpdateBy(user.getLoginName());
                        securityPlanDetail.setUpdateDate(new Date());
                        planUpdateDetails.add(securityPlanDetail);
                    } else {
                        securityPlanDetail = new CmsSecurityPlanDetail();
                        BeanUtils.copyNotNullProperties(planDetailVO, securityPlanDetail);
                        securityPlanDetail.setSecurityPlanId(cmsSecurityPlan.getId());// 计划主表id
                        securityPlanDetail.setValidSign(CmsConstant.VALIDSIGN_1);
                        if (EmptyUtils.isEmpty(securityPlanDetail.getIsRepay())) {
                            securityPlanDetail.setIsRepay(CmsConstant.IS_FALSE);
                        }
                        securityPlanDetail.setCreateBy(user.getLoginName());
                        securityPlanDetail.setCreateDate(new Date());
                        planAddDetails.add(securityPlanDetail);
                    }
                }
            }
        }
        // 删除除更新id以外的明细
        if (StringUtils.isNotEmpty(vo.getId())) {
            cmsSecurityPlanDetailService.deleteOtherDetail(cmsSecurityPlan.getId(), detailUpdateIds);
        }
        // 新增
        if (!CollectionUtils.isEmpty(planAddDetails)) {
            cmsSecurityPlanDetailService.batchSave(planAddDetails);
        }
        // 修改
        if (!CollectionUtils.isEmpty(planUpdateDetails)) {
            cmsSecurityPlanDetailService.batchUpdate(planUpdateDetails);
        }
        if (EmptyUtils.isEmpty(isPass)) {
            CmsSecurityIssue securityIssue = cmsSecurityIssueService
                    .getSecurityIssueByCode(cmsSecurityPlan.getSecurityIssueCode(), null).get(0);
            if (StringUtils.isEmpty(vo.getId())) {
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_31);
                cmsSecurityIssueService.merge(securityIssue);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_PLAN, cmsSecurityPlan.getId(),
                        cmsSecurityPlan, cmsSecurityPlan.getCorpId(), user.getId());
            } else {
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_41);
                cmsSecurityIssueService.merge(securityIssue);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_PLAN_UPDATE, cmsSecurityPlan.getId(),
                        cmsSecurityPlan, cmsSecurityPlan.getCorpId(), user.getId());
            }
        }
    }
    /**
     * 根据编号查主计划信息
     */
    public CmsSecurityPlanVO getPlanBySecurityCode(String securityIssueCode) {
        return cmsSecurityPlanDao.getPlanBySecurityCode(securityIssueCode);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String key = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsSecurityPlan securityPlan = this.get(businessKey,true);
        CmsSecurityIssue securityIssue = cmsSecurityIssueService
                .getSecurityIssueByCode(securityPlan.getSecurityIssueCode(), null).get(0);
        // 无审批流事务未及时更新解决办法
        securityIssue = cmsSecurityIssueService.get(securityIssue.getId());
        if (CmsWorkFlowConstant.CMS_SECURITY_PLAN.equals(key)) {
            if (result) {
                // 审批通过
                securityPlan.setStatus(CmsConstant.APPROVED);
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_97);
                cmsSecurityRepaySureService.addRepaySure(securityIssue.getSecurityIssueCode());// 生成还款确认数据
            } else {
                // 打回
                securityPlan.setStatus(CmsConstant.BACK);
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS__3);
            }
        } else if (CmsWorkFlowConstant.CMS_SECURITY_PLAN_UPDATE.equals(key)) {
            if (result) {
                // 审批通过
                securityPlan.setStatus(CmsConstant.APPROVED);
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_98);
                cmsSecurityRepaySureService.addRepaySure(securityIssue.getSecurityIssueCode());// 生成还款确认数据
            } else {
                // 打回
                securityPlan.setStatus(CmsConstant.BACK);
                securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS__4);
            }
        }
        cmsSecurityIssueService.merge(securityIssue);
        this.updateEntity(securityPlan);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
