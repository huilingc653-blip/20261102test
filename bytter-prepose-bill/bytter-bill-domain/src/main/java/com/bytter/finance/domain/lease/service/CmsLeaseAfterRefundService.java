package com.bytter.finance.domain.lease.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.constant.InlWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.lease.dao.LeaseRefundDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseRefundInfo;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.vo.*;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service("cmsLeaseAfterRefundService")
@WorkFlow(InlWorkFlowConstant.LEASE_AFTER_REFUND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.LEASE_AFTER_REFUND)
@Transactional
public class CmsLeaseAfterRefundService extends BaseService<LeaseRefundInfo, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener {
    @Autowired
    private LeaseRefundDao leaseRefundDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired(required = false)
    private LeaseExtendService leaseExtendService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /**
     * 融资退租列表 - 数据
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        DataTablesOutput output = leaseRefundDao.getDataLeaseTable(dataTablesInput, corpAuth);
        List<CmsLeaseContinueInfoVO> list = output.getData();
        for (CmsLeaseContinueInfoVO infoVO : list) {
            getRefundStatusValue(infoVO);
        }
        return output;
    }
    public void getRefundStatusValue(CmsLeaseContinueInfoVO infoVO) {
        List<LeaseContinueVO> list = leaseRefundDao.getRefundStatus(infoVO.getId());
        if (!EmptyUtils.isEmpty(list)) {
            infoVO.setContinueVO(list.get(0));
        }
    }
    /**
     * 融资租赁退租页面主信息
     *
     * @param leaseNo
     * @return
     */
    public LeaseRefundVO getLeaseById(String leaseNo) {
        LeaseRefundVO leaseRefundVO = leaseRefundDao.getLeaseById(leaseNo);
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.getLeaseById(leaseNo);
        leaseRefundVO.setCorpName(cmsFinancingLeaseInfo.getCorpName());
        leaseRefundVO.setOriCurName(cmsFinancingLeaseInfo.getOriCurName());
        List<LeaseExtendVO> vo = leaseRefundDao.getExtend(leaseRefundVO.getId());
        if (!EmptyUtils.isEmpty(vo)) {
            leaseRefundVO.setExpendedDate(vo.get(0).getExpendedDate());
        }
        return leaseRefundVO;
    }
    /**
     * 获取最新退租信息
     *
     * @param id
     * @return
     */
    public LeaseRefundUpdateVO getRefund(String id) {
        List<LeaseRefundUpdateVO> vo = leaseRefundDao.getRefund(id);
        return vo.get(0);
    }
    /**
     * 存储退租数据
     *
     * @param
     */
    public void saveLeaseRefundInfo(LeaseRefundInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        leaseRefundDao.save(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setControlStatus("Y");
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_26);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_REFUND, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_25);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    /**
     * 存储退租数据
     *
     * @param
     */
    public void updateLeaseRefundInfo(LeaseRefundInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        leaseRefundDao.update(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_26);
            lease.setControlStatus("Y");
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_REFUND, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_25);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseRefundInfo leaseRefundInfo = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        leaseRefundInfo.setStatus(status);
        leaseRefundInfo.setUpdateBy(user.getLoginName());
        leaseRefundInfo.setUpdateDate(DateUtils.getDate());
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseRefundInfo.getLeaseId());
        if (status.equals(BtBankAccConstant.STATUE_CHECK)) {
            cmsFinancingLeaseInfo.setStatus(-5);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_102);
            // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsFinancingLeaseInfo.getId(),
                    cmsFinancingLeaseInfo.getLeaseNo());
            if (dto != null) {// 不为空说明挂接了保证金
                dto.setStatus(FcBondConstant.STATUE_RELEASE);
                dto.setUpdateBy(user.getLoginName());
                dto.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(dto);
            }
            // notificationService.complete(NotificationConstant.CMS.PROVIDE_ADD, id, user.getId());
            // 抵质押物处理,释放冻结额度
            if (EmptyUtils.isNotEmpty(cmsFinancingLeaseInfo.getIsRelatePledge())
                    && CmsConstant.IS_TRUE.equals(cmsFinancingLeaseInfo.getIsRelatePledge())) {
                pledgeOccupyService.releasePledgeOccupy(cmsFinancingLeaseInfo.getId(),
                        cmsFinancingLeaseInfo.getLeaseNo());
            }
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            LeaseUtils.handleGua(cmsFinancingLeaseInfo, null, guaContractOccupServie, 3,
                    cmsFinancingLeaseInfo.getGuaContract());
        } else {
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__9);
        }
        cmsFinancingLeaseInfo.setControlStatus("N");
        cmsFinancingLeaseInfo.setEndDate(leaseRefundInfo.getRefundDate());
        cmsFinancingLeaseInfoService.update(cmsFinancingLeaseInfo, user);
        this.saveOrUpdate(leaseRefundInfo);
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseRefundInfo leaseRefundInfo = this.get(workFlowResult.getBusinessKey());
        leaseRefundInfo.setStatus(CommonConstant.STATUE_TEMP);
        leaseRefundInfo.setUpdateBy(user.getLoginName());
        leaseRefundInfo.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(leaseRefundInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao<LeaseRefundInfo, String> getEntityDao() {
        return leaseRefundDao;
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.LEASE_AFTER_REFUND,
                input);
        return workFlowBusiness
                .merge(leaseRefundDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    public CmsAfterLeaseInfoVO queryLeaseObjectInfo(String id) {
        return leaseRefundDao.queryLeaseRefundInfo(id);
    }
}
