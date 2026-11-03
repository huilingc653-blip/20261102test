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
import com.bytter.finance.domain.lease.dao.LeaseBuyBackDao;
import com.bytter.finance.domain.lease.dao.LeaseRefundDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseBuyBackInfo;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
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
@Service("cmsLeaseAfterBuyBackService")
@WorkFlow(InlWorkFlowConstant.LEASE_AFTER_BUYBACK)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.LEASE_AFTER_BUYBACK)
@Transactional
public class CmsLeaseAfterBuyBackService extends BaseService<LeaseBuyBackInfo, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener {
    @Autowired
    private LeaseBuyBackDao leaseBuyBackDao;
    @Autowired
    private LeaseRefundDao leaseRefundDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /**
     * 融资租赁回购列表
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        return leaseBuyBackDao.getDataLeaseTable(dataTablesInput, corpAuth);
    }
    /**
     * 融资租赁退租页面主信息
     *
     * @param leaseNo
     * @return
     */
    public CmsAfterLeaseInfoVO getLeaseById(String leaseNo) {
        CmsAfterLeaseInfoVO cmsAfterLeaseInfoVO = leaseBuyBackDao.getLeaseById(leaseNo);
        List<LeaseExtendVO> vo = leaseRefundDao.getExtend(cmsAfterLeaseInfoVO.getId());
        if (!EmptyUtils.isEmpty(vo)) {
            cmsAfterLeaseInfoVO.setExpendedDate(vo.get(0).getExpendedDate());
        }
        return cmsAfterLeaseInfoVO;
    }
    /**
     * 获取最新回购信息
     *
     * @param id
     * @return
     */
    public CmsAfterLeaseInfoVO getRepo(String id) {
        List<CmsAfterLeaseInfoVO> vo = leaseBuyBackDao.getRepo(id);
        return vo.get(0);
    }
    /**
     * 存储回购数据
     *
     * @param
     */
    public void saveLeaseBuyBackInfo(LeaseBuyBackInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        leaseBuyBackDao.save(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setControlStatus("Y");
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_46);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_BUYBACK, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_45);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    /**
     * 更新回购数据
     *
     * @param
     */
    public void updateLeaseBuyBackInfo(LeaseBuyBackInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getId());
        entity.setCreateDate(new Date());
        leaseBuyBackDao.update(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setControlStatus("Y");
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_46);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_BUYBACK, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_45);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseBuyBackInfo leaseBuyBackInfo = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        leaseBuyBackInfo.setStatus(status);
        leaseBuyBackInfo.setUpdateBy(user.getLoginName());
        leaseBuyBackInfo.setUpdateDate(DateUtils.getDate());
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseBuyBackInfo.getLeaseId());
        if (status.equals(BtBankAccConstant.STATUE_CHECK)) {
            cmsFinancingLeaseInfo.setStatus(-6);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_104);
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
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__11);
        }
        cmsFinancingLeaseInfo.setControlStatus("N");
        cmsFinancingLeaseInfo.setEndDate(leaseBuyBackInfo.getRepoDate());
        cmsFinancingLeaseInfoService.update(cmsFinancingLeaseInfo, user);
        this.saveOrUpdate(leaseBuyBackInfo);
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseBuyBackInfo leaseBuyBackInfo = this.get(workFlowResult.getBusinessKey());
        leaseBuyBackInfo.setStatus(CommonConstant.STATUE_TEMP);
        leaseBuyBackInfo.setUpdateBy(user.getLoginName());
        leaseBuyBackInfo.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(leaseBuyBackInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao<LeaseBuyBackInfo, String> getEntityDao() {
        return leaseBuyBackDao;
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.LEASE_AFTER_BUYBACK,
                input);
        return workFlowBusiness
                .merge(leaseBuyBackDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    public CmsAfterLeaseInfoVO queryLeaseObjectInfo(String id) {
        return leaseBuyBackDao.queryLeaseObjectInfo(id);
    }
}
