package com.bytter.finance.domain.lease.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.LeaseConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.lease.dao.LeaseCapitalPlanDao;
import com.bytter.finance.domain.lease.dao.LeaseCapitalPlanDelDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlanDel;
import com.bytter.system.api.INotificationService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 租赁计划子表修改
 * @date 2022-04-02 11:09:43
 */
@WorkFlow(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_CHANGE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_CHANGE)
@Service("updateLeaseCapitalPlanDelService")
@Transactional
public class UpdateLeaseCapitalPlanService extends BaseService<LeaseCapitalPlanDel, String>
        implements IWorkFlowListener {
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private LeaseCapitalPlanDao leaseCapitalPlanDao;
    @Autowired
    private LeaseCapitalPlanDelDao leaseCapitalPlanDelDao;
    @Autowired
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService financingLeaseInfoService;
    @Override
    public BaseDao<LeaseCapitalPlanDel, String> getEntityDao() {
        return null;
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
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_CHANGE, dataTablesInput);
        DataTablesOutput dp = leaseCapitalPlanDao.getDataUpdateApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批流程结束 执行
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        LeaseCapitalPlan leaseCapitalPlan = leaseCapitalPlanService.get(businessKey);
        // 修改租赁表信息
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = financingLeaseInfoService.get(leaseCapitalPlan.getLeaseId());
        if (result) {// 审批通过
            leaseCapitalPlan.setStatus(CmsConstant.APPROVED);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_99);
            msService.insertProcessMsg(leaseCapitalPlan.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
            // notificationService.send(leaseCapitalPlan.getId(), leaseCapitalPlan.getCorpId(),
            // NotificationConstant.CMS.PROVIDE_REPAIR, leaseCapitalPlan);//添加补录的消息提示
        } else {// 打回
            leaseCapitalPlan.setStatus(CmsConstant.BACK);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__6);
            msService.insertProcessMsg(leaseCapitalPlan.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
            // notificationService.send(leaseCapitalPlan.getId(), leaseCapitalPlan.getCorpId(),
            // NotificationConstant.CMS.PROVIDE_ADD, leaseCapitalPlan);//添加录入申请的消息提示
        }
        financingLeaseInfoService.saveOrUpdate(cmsFinancingLeaseInfo);
        leaseCapitalPlanService.saveOrUpdate(leaseCapitalPlan);
    }
    /***************** 审批END ********************/
}
