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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.lease.dao.LeaseContinueDao;
import com.bytter.finance.domain.lease.dao.LeaseRefundDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseContinueInfo;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseContinueInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseContinueVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Service("cmsLeaseAfterContinueService")
@WorkFlow(InlWorkFlowConstant.LEASE_AFTER_CONTINUE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.LEASE_AFTER_CONTINUE)
@Transactional
public class CmsLeaseAfterContinueService extends BaseService<LeaseContinueInfo, String>
        implements IWorkFlowListener, IWorkFlowRevokeListener {
    @Autowired
    private LeaseContinueDao leaseContinueDao;
    @Autowired
    private LeaseRefundDao leaseRefundDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    /**
     * 融资续租数据查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        DataTablesOutput output = leaseContinueDao.getDataLeaseTable(dataTablesInput, corpAuth);
        List<CmsLeaseContinueInfoVO> list = output.getData();
        for (CmsLeaseContinueInfoVO infoVO : list) {
            getContinueStatusValue(infoVO);
        }
        return output;
    }
    /**
     * 融资租赁续租页面主信息
     *
     * @param leaseNo
     * @return
     */
    public CmsAfterLeaseInfoVO getLeaseById(String leaseNo) {
        CmsAfterLeaseInfoVO cmsAfterLeaseInfoVO = leaseContinueDao.getLeaseById(leaseNo);
        setExtend(cmsAfterLeaseInfoVO);// 取最新的展期
        setContinue(cmsAfterLeaseInfoVO);// 取最新的续租
        return cmsAfterLeaseInfoVO;
    }
    public void setContinue(CmsAfterLeaseInfoVO cmsLeaseInfoVO) {
        List<CmsAfterLeaseInfoVO> vo = leaseContinueDao.getContinueDate(cmsLeaseInfoVO.getId());
        if (!EmptyUtils.isEmpty(vo)) {
            if (!EmptyUtils.isEmpty(vo.get(0).getContinueDate())) {
                cmsLeaseInfoVO.setContinueDate(vo.get(0).getContinueDate());
            }
            if (!EmptyUtils.isEmpty(vo.get(0).getContinueCode())) {
                cmsLeaseInfoVO.setContinueCode(vo.get(0).getContinueCode());
            }
            if (!EmptyUtils.isEmpty(vo.get(0).getRmk())) {
                cmsLeaseInfoVO.setRmk(vo.get(0).getRmk());
            }
        }
    }
    /*
     * 取最新的展期
     */
    public void setExtend(CmsAfterLeaseInfoVO cmsLeaseInfoVO) {
        List<LeaseExtendVO> vo = leaseRefundDao.getExtend(cmsLeaseInfoVO.getId());
        if (!EmptyUtils.isEmpty(vo)) {
            cmsLeaseInfoVO.setExpendedDate(vo.get(0).getExpendedDate());
        }
    }
    /**
     * 获取最新续租信息
     *
     * @param id
     * @return
     */
    public CmsAfterLeaseInfoVO getContinue(String id) {
        List<CmsAfterLeaseInfoVO> vo = leaseContinueDao.getContinue(id);
        return vo.get(0);
    }
    public void getContinueStatusValue(CmsLeaseContinueInfoVO infoVO) {
        List<LeaseContinueVO> list = leaseContinueDao.getContinueStatus(infoVO.getId());
        if (!EmptyUtils.isEmpty(list)) {
            infoVO.setContinueVO(list.get(0));
        }
    }
    public CmsAfterLeaseInfoVO getLeaseContInfoById(String leaseId) {
        LeaseContinueInfo leaseContinueInfo = leaseContinueDao.get(leaseId);
        CmsAfterLeaseInfoVO cmsAfterLeaseInfoVO = new CmsAfterLeaseInfoVO();
        BeanUtils.copyProperties(leaseContinueInfo, cmsAfterLeaseInfoVO);
        return cmsAfterLeaseInfoVO;
    }
    /**
     * 存储续租数据
     *
     * @param
     */
    public void saveLeaseContinueInfo(LeaseContinueInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getUsername());
        entity.setCreateDate(new Date());
        leaseContinueDao.save(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setControlStatus("Y");// 占用
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_36);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_CONTINUE, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_35);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    /**
     * 修改续租数据
     *
     * @param
     */
    public void updateLeaseContinueInfo(LeaseContinueInfo entity, ICurrentUser user) {
        entity.setCreateBy(user.getUsername());
        entity.setCreateDate(new Date());
        leaseContinueDao.update(entity);
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(entity.getLeaseId());
        if (InlConstant.COMMIT_APPROVE == entity.getStatus()) {
            lease.setControlStatus("Y");// 占用
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_36);
             getStartHandler().handler(InlWorkFlowConstant.LEASE_AFTER_CONTINUE, entity.getId(), entity, lease.getCorpId(),
                    user.getId());
        } else {
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_35);
        }
        cmsFinancingLeaseInfoService.updateEntity(lease);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseContinueInfo leaseContinueInfo = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        leaseContinueInfo.setStatus(status);
        leaseContinueInfo.setUpdateBy(user.getUsername());
        leaseContinueInfo.setUpdateDate(DateUtils.getDate());
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(leaseContinueInfo.getLeaseId());
        cmsFinancingLeaseInfo.setControlStatus("N");// 当前租赁合同释放占用
        if (status.equals(BtBankAccConstant.STATUE_CHECK)) {
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_103);
        } else {
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__10);
        }
        cmsFinancingLeaseInfo.setEndDate(leaseContinueInfo.getContinueDate());
        if (EmptyUtils.isEmpty(cmsFinancingLeaseInfo.getContinueCount())) {
            cmsFinancingLeaseInfo.setContinueCount(0);
        }
        if (status.equals(BtBankAccConstant.STATUE_CHECK)) {
            cmsFinancingLeaseInfo.setContinueCount(cmsFinancingLeaseInfo.getContinueCount() + 1);
            cmsFinancingLeaseInfoService.update(cmsFinancingLeaseInfo, user);
        }
        cmsFinancingLeaseInfoService.update(cmsFinancingLeaseInfo, user);
        this.saveOrUpdate(leaseContinueInfo);
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        LeaseContinueInfo leaseContinueInfo = this.get(workFlowResult.getBusinessKey());
        leaseContinueInfo.setStatus(CommonConstant.STATUE_TEMP);
        leaseContinueInfo.setUpdateBy(user.getUsername());
        leaseContinueInfo.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(leaseContinueInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao<LeaseContinueInfo, String> getEntityDao() {
        return leaseContinueDao;
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(InlWorkFlowConstant.LEASE_AFTER_CONTINUE, input);
        return workFlowBusiness
                .merge(leaseContinueDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    public CmsAfterLeaseInfoVO queryLeaseContinueInfo(String id) {
        return leaseContinueDao.queryLeaseContinueInfo(id);
    }
    @Transactional(readOnly = true)
    public DataTablesOutput getDataListMsgData(DataTablesInput dataTablesInput, String leaseId) {
        return leaseContinueDao.getDataListMsgData(dataTablesInput, leaseId);
    }
}
