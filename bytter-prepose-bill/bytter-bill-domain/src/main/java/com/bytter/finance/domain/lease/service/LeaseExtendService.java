package com.bytter.finance.domain.lease.service;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.lease.dao.LeaseExtendDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseExtend;
import com.bytter.finance.domain.lease.vo.CmsLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.IRateMaintenceService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service("leaseExtendService")
@Transactional
@WorkFlow(InlWorkFlowConstant.LEASE_EXTEND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.LEASE_EXTEND)
public class LeaseExtendService extends BaseService<LeaseExtend, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener {
    @Autowired
    private LeaseExtendDao leaseExtendDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IRateMaintenceService iRateMaintenceService;
    /*
     * 融资租赁展期列表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        DataTablesOutput output = leaseExtendDao.getDataLeaseTable(dataTablesInput, corpAuth);
        List<CmsLeaseInfoVO> list = output.getData();
        for (CmsLeaseInfoVO cmsLeaseInfoVO : list) {
            setExtend(cmsLeaseInfoVO);// 取最新的展期状态
        }
        return output;
    }
    /*
     * 取最新的展期状态
     */
    public void setExtend(CmsLeaseInfoVO cmsLeaseInfoVO) {
        List<LeaseExtendVO> vo = leaseExtendDao.getExtend(cmsLeaseInfoVO.getId());
        if (!EmptyUtils.isEmpty(vo)) {
            cmsLeaseInfoVO.setExtend(vo.get(0));
        }
    }
    /*
     * 取最新的展期状态
     */
    public LeaseExtendVO getExtend(String id) {
        List<LeaseExtendVO> vo = leaseExtendDao.getExtend(id);
        return vo.get(0);
    }
    /*
     * 日志列表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataListMsgData(DataTablesInput dataTablesInput, String leaseId) {
        return leaseExtendDao.getDataListMsgData(dataTablesInput, leaseId);
    }
    /**
     * 数据更新
     *
     * @param leaseExtendVO
     * @param user
     */
    public void modifyLeaseExtend(LeaseExtendVO leaseExtendVO, ICurrentUser user) {
        LeaseExtend entity = new LeaseExtend();
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        BeanUtils.copyNotNullProperties(leaseExtendVO, entity);
        leaseExtendDao.save(entity);
        notificationService.complete(NotificationConstant.CMS.LEASE_EXTEND, entity.getId(), user.getId());
        CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId());
        info.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_55);
        cmsFinancingLeaseInfoService.updateEntity(info);
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId());
            cmsFinancingLeaseInfo.setControlStatus("Y");// 占用合同
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_56);
            cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_EXTEND, entity.getId(), entity,
                    cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getCorpId(), user.getId());
        }
    }
    public TbRateMaintenceDTO getLoanRateMaintence(CmsFinancingLeaseInfo cmsFinancingLeaseInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId", cmsFinancingLeaseInfo.getOriCurid());
        params.put("baseRateId", cmsFinancingLeaseInfo.getReferenceRateType());
        params.put("creditPeriodId", cmsFinancingLeaseInfo.getRepayCycle());
        TbRateMaintenceDTO tbLoanRateMaintence = null;
        List<TbRateMaintenceDTO> tbLoanRateMaintences = iRateMaintenceService.listRateMaintenceSort(params);
        if (ListUtils.isNotNullOrEmpty(tbLoanRateMaintences)) {
            for (int i = 0; i < tbLoanRateMaintences.size(); i++) {
                if (tbLoanRateMaintences.get(i).getTimeOfTakeEffect()
                        .compareTo(cmsFinancingLeaseInfo.getBeginDate()) <= 0) {
                    tbLoanRateMaintence = tbLoanRateMaintences.get(i);
                    break;
                }
            }
        }
        return tbLoanRateMaintence;
    }
    /**
     * 数据更新
     *
     * @param leaseExtendVO
     * @param user
     */
    public void editLeaseExtend(LeaseExtendVO leaseExtendVO, ICurrentUser user) {
        LeaseExtend entity = new LeaseExtend();
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        BeanUtils.copyNotNullProperties(leaseExtendVO, entity);
        leaseExtendDao.update(entity);
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseExtendVO.getLeaseId());
            cmsFinancingLeaseInfo.setControlStatus("Y");// 占用合同
            cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_EXTEND, entity.getId(), entity,
                    cmsFinancingLeaseInfoService.get(entity.getLeaseId()).getCorpId(), user.getId());
        }
    }
    /**
     * 审批数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.LEASE_EXTEND,
                input);
        return workFlowBusiness
                .merge(leaseExtendDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /*
     * 查看审批租赁物
     */
    public LeaseExtendVO queryLeaseObjectInfo(String id) {
        return leaseExtendDao.queryLeaseObjectInfo(id);
    }
    @Override
    public BaseDao<LeaseExtend, String> getEntityDao() {
        return leaseExtendDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseExtend leaseExtend = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        leaseExtend.setStatus(status);
        leaseExtend.setUpdateBy(user.getLoginName());
        leaseExtend.setUpdateDate(DateUtils.getDate());
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseExtend.getLeaseId());
        cmsFinancingLeaseInfo.setControlStatus("N");// 当前租赁合同释放占用
        if (EmptyUtils.isEmpty(cmsFinancingLeaseInfo.getExpendedCount())) {
            cmsFinancingLeaseInfo.setExpendedCount(0);
        }
        if (status.equals(BtBankAccConstant.STATUE_CHECK)) {
            cmsFinancingLeaseInfo.setEndDate(leaseExtend.getExpendedDate());// 到期日期
            dayLimit(leaseExtend, cmsFinancingLeaseInfo); // 审批通过，重新设置租赁期限
            cmsFinancingLeaseInfo.setExpendedCount(cmsFinancingLeaseInfo.getExpendedCount() + 1);// 展期次数+1
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_105);
            cmsFinancingLeaseInfoService.update(cmsFinancingLeaseInfo, user);
        } else if (status.equals(BtBankAccConstant.STATUE_BACK)) {
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__12);
            cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
            String submitUser = StringUtils.isEmpty(leaseExtend.getUpdateBy()) ? leaseExtend.getCreateBy()
                    : leaseExtend.getUpdateBy();// 提交人
            notificationService.sendToUser(leaseExtend.getId(), submitUser, NotificationConstant.CMS.LEASE_EXTEND,
                    leaseExtend);// 添加租赁申请的消息提示
        }
        this.saveOrUpdate(leaseExtend);
    }
    /*
     * 重新设置租赁期限
     */
    public void dayLimit(LeaseExtend leaseExtend, CmsFinancingLeaseInfo info) {
        if (info.getTernUnit().equals(CmsConstant.CMS_LOAN_TERN_UNIT_DAY)) {// 期限单位是天
            // 计算租赁开始日与展期到期日相差天数
            int itvday = (int) ((leaseExtend.getExpendedDate().getTime() - info.getBeginDate().getTime())
                    / (1000 * 3600 * 24));
            info.setDayLimit(itvday);
        } else if (info.getTernUnit().equals(CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH)) {// 期限单位是月
            Calendar beginDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            beginDate.setTime(info.getBeginDate());
            endDate.setTime(leaseExtend.getExpendedDate());
            int result = endDate.get(Calendar.MONTH) - beginDate.get(Calendar.MONTH);// 相差月数
            int months = (endDate.get(Calendar.YEAR) - beginDate.get(Calendar.YEAR)) * 12;// 计算跨年的时候相差月数
            int monthsDiff = result + months;
            if (endDate.get(Calendar.DAY_OF_MONTH) - beginDate.get(Calendar.DAY_OF_MONTH) > 0) {
                monthsDiff++;
            }
            info.setDayLimit(monthsDiff);
        }
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseExtend leaseExtend = this.get(workFlowResult.getBusinessKey());
        leaseExtend.setStatus(CommonConstant.STATUE_TEMP);
        leaseExtend.setUpdateBy(user.getLoginName());
        leaseExtend.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(leaseExtend);
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseExtend.getLeaseId());
        cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_56);
        cmsFinancingLeaseInfoService.updateEntity(cmsFinancingLeaseInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
