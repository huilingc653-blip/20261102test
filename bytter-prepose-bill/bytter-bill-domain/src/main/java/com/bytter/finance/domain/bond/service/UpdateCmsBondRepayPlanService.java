package com.bytter.finance.domain.bond.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondRepayPlanDao;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券还款计划修改
 * @date 2022-06-07 10:07:46
 */
@WorkFlow(CmsWorkFlowConstant.UPDATE_CMSBONDREPAY_PLAN)
@Service("updateCmsBondRepayPlanService")
@Transactional
public class UpdateCmsBondRepayPlanService extends BaseService<CmsBondRepayPlan, String> {
    @Autowired
    private CmsBondRepayPlanDao cmsBondRepayPlanDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Override
    public BaseDao<CmsBondRepayPlan, String> getEntityDao() {
        return cmsBondRepayPlanDao;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        // 获取该用户有权限的单位id集
        String resource = ResourceConstant.CMS_BOND_UPDATECMSBONDREPAYPLAN_QUERY;
        List<String> corpAuthList = corpService.getUserCorpIds(resource, dataTablesInput.getCurrentUserId());
        // 筛选已登记的，并且已经发行的债券信息
        List<String> registerIdList = cmsBondRepayPlanService.getByRepayPlan(corpAuthList, CmsConstant.IS_FALSE);
        DataTablesOutput output = cmsBondRegisterInfoService.getDataTablesByIds(dataTablesInput, registerIdList);
        return output;
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     *
     * @param dataTablesInput
     *            <b>日期：</b>2017-8-1<br>
     *            <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.UPDATE_CMSBONDREPAY_PLAN, dataTablesInput);
        DataTablesOutput dp = cmsBondRepayPlanDao.getDataUpdateApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    // 审批功能取消
    // /*****************审批BEGIN********************/
    //
    // /**
    // * 审批启动完成 执行
    // */
    // public void flowStarted(IWorkFlowResult workFlowResult) {
    //
    // }
    //
    // /**
    // * 审批流程结束 执行
    // */
    // public void flowCompleted(IWorkFlowResult workFlowResult) {
    // String businessKey = workFlowResult.getBusinessKey();
    // String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
    // boolean result = workFlowResult.isPass();
    // ICurrentUser user = workFlowResult.getUser();
    //
    // CmsBondRepayPlan plan = this.get(businessKey);
    // // 修改债券发行表信息
    // CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(plan.getIssueId());
    //
    // if (result) {//审批通过
    // plan.setStatus(CmsConstant.APPROVED);
    // cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_100);
    // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_CHECKOK,
    // CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
    // //notificationService.send(plan.getId(), plan.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAIR,
    // plan);//添加补录的消息提示
    // } else {//打回
    // plan.setStatus(CmsConstant.BACK);
    // cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__6);
    // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_ROLLBACK,
    // CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
    // //notificationService.send(plan.getId(), plan.getCorpId(), NotificationConstant.CMS.PROVIDE_ADD,
    // plan);//添加录入申请的消息提示
    //
    // }
    // cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);
    // cmsBondRepayPlanService.saveOrUpdate(plan);
    // }
    // /*****************审批END********************/
}
