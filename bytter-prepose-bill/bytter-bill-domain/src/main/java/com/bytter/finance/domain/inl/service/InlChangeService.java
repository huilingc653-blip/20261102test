package com.bytter.finance.domain.inl.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.constant.InlWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlChangeDao;
import com.bytter.finance.domain.inl.dao.InlDeferredDao;
import com.bytter.finance.domain.inl.entity.CmsInlChange;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlChangeVO;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.InlMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WorkFlow(InlWorkFlowConstant.CMS_INL_REPLAY_CHANGE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_REPLAY_CHANGE)
@Service("inlChangeService")
public class InlChangeService extends BaseService<CmsInlChange, String> implements IWorkFlowListener {
    @Autowired
    private InlChangeDao inlChangeDao;
    @Autowired
    private InlDeferredDao inlDeferredDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private InlSendService inlSendService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;

    /**
     * 申请列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
        DataTablesOutput output = inlChangeDao.dataTable(input, corpIds);
        // 设置最新得展期到期日期
        setEndDate(output);
        return output;
    }

    /**
     * 设置到期日期
     *
     * @param output
     */
    public void setEndDate(DataTablesOutput output) {
        List<CmsInlChangeVO> cmsInlAdjustrateVOS = output.getData();
        Map<String, Object> params = new HashMap<>();
        for (CmsInlChangeVO cmsInlChangeVO : cmsInlAdjustrateVOS) {
            params.put("inlSendId", cmsInlChangeVO.getInlSendId());
            List<CmsInlDeferredVO> cmsInlDeferredVOS1 = inlDeferredDao.getCmsInlDeferredVOBySendId(params);
            if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS1)) {
                cmsInlChangeVO.setDeferredDate(cmsInlDeferredVOS1.get(0).getNowDeferredDate());
            }
        }
        output.setData(cmsInlAdjustrateVOS);
    }

    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_REPLAY_CHANGE, input);
        DataTablesOutput output = inlChangeDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList());
        // 设置最新得展期到期日期
        setEndDate(output);
        return workFlowBusiness.merge(output);
    }

    /**
     * 保存数据
     *
     * @param cmsInlChangeVO
     * @param user
     */
    public void save(CmsInlChangeVO cmsInlChangeVO, ICurrentUser user) {
        CmsInlChange cmsInlChange;
        if (StringUtils.isNotEmpty(cmsInlChangeVO.getId())) {
            cmsInlChange = inlChangeDao.get(cmsInlChangeVO.getId());
            BeanUtils.copyNotNullProperties(cmsInlChangeVO, cmsInlChange);
            cmsInlChange.setUpdateBy(user.getId());
            cmsInlChange.setUpdateDate(new Date());
        } else {
            cmsInlChange = new CmsInlChange();
            BeanUtils.copyNotNullProperties(cmsInlChangeVO, cmsInlChange);
            cmsInlChange.setCreateBy(user.getId());
            cmsInlChange.setCreateDate(new Date());
            cmsInlChange.setBillNo(InlUtil.getInlReplayChangeNo());
        }
        inlChangeDao.saveOrUpdate(cmsInlChange);
        saveReplayPlan(cmsInlChangeVO, user);
        if (InlConstant.COMMIT_APPROVE == cmsInlChange.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_REPAY_CHANGE, cmsInlChange.getId(), user.getId());
            getStartHandler().handler(InlWorkFlowConstant.CMS_INL_REPLAY_CHANGE, cmsInlChange.getId(), cmsInlChange,
                    cmsInlChange.getCorpId(), user.getId());
        }
    }

    public void checkInterest(String inlInterestId, String sendBillCode) {
        CmsInlSendVO cmsInlSendVO = inlSendService.getSendByBillNo(sendBillCode);
        List<CmsInlChangeVO> cmsInlChangeVOS = this.getApproveChangeBySendId(cmsInlSendVO.getId());
        if (cmsInlChangeVOS.size() > 0) {
            throw new BusinessException(InlMessageEnum.INL_CHANGE_APPROVE_MSG);
        }

        CmsProvideInterest cmsProvideInterest = cmsProvideInterestService.get(inlInterestId, true);
        if (EmptyUtils.isEmpty(cmsProvideInterest)) {
            throw new BusinessException(InlMessageEnum.INL_CHANGE_DELETE_MSG);
        }

    }

    public void saveReplayPlan(CmsInlChangeVO cmsInlChangeVO, ICurrentUser user) {
        // 删除旧审批中数据，保存新数据
        inlChangeDao.deleteRepaymentApproveByBillCode(cmsInlChangeVO.getInlSendId());
        CmsRepaymentSchedule cms = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(cmsInlChangeVO.getInlSendId());
        CmsRepaymentSchedule cmsRepaymentSchedule = new CmsRepaymentSchedule();
        BeanUtils.copyProperties(cms, cmsRepaymentSchedule);
        cmsRepaymentSchedule.setId(null);
        cmsRepaymentSchedule.setStatus("11");
        repaymentScheduleService.getEntityDao().save(cmsRepaymentSchedule);
        subSave(cmsRepaymentSchedule.getId(), cmsInlChangeVO, user);
    }

    @Override
    public BaseDao<CmsInlChange, String> getEntityDao() {
        return inlChangeDao;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlChange cmsInlChange = inlChangeDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            cmsInlChange.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlChange.getBillNo(), InlConstant.CMS_INL_REPAYCAHNEG_NO, CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
        } else {
            cmsInlChange.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlChange.getId(), cmsInlChange.getCorpId(), NotificationConstant.CMS.INL_REPAY_CHANGE, cmsInlChange);
            msService.insertProcessMsg(cmsInlChange.getBillNo(), InlConstant.CMS_INL_REPAYCAHNEG_NO, CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        // updateBudget(dfDirectFinanceInfo, user);
        inlChangeDao.update(cmsInlChange);
        /**
         * 更新还款计划及利息明细
         */
        if (isPass) {
            updateInterest(cmsInlChange, user);
        }
    }

    /**
     * 更新利息明细
     */
    public void updateInterest(CmsInlChange cmsInlChange, ICurrentUser user) {
        CmsInlSend cmsInlSend = inlSendService.get(cmsInlChange.getInlSendId());
        cmsInlSend.setRepayType(cmsInlChange.getRepayType());
        // 删除旧数据
        inlChangeDao.deleteRepaymentByBillCode(cmsInlChange.getInlSendId());
        // 更新待审批数据为已审数据
        inlChangeDao.updateRepaymentByBillCode(cmsInlChange.getInlSendId());
        inlChangeDao.flush();
        /**
         * 跟新利息明细
         */
        inlSendService.adjustProvideInterest(cmsInlSend, user);
    }

    /**
     * 通过发放id获取所有还款计划修改记录
     *
     * @return
     */
    public List<CmsInlChangeVO> getCmsInlChangeBySendId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("inlSendId", id);
        params.put("status",InlConstant.APPROVED);
        return inlChangeDao.getCmsInlChangeBySendId(params);
    }

    /**
     * 通过发放id获取审批中计划
     *
     * @return
     */
    public List<CmsInlChangeVO> getApproveChangeBySendId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("inlSendId", id);
        params.put("status", InlConstant.COMMIT_APPROVE);
        return inlChangeDao.getCmsInlChangeBySendId(params);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        CmsInlChange cmsInlChange = inlChangeDao.get(id);
        cmsInlChange.setStatus(InlConstant.DELETE);
        inlChangeDao.update(cmsInlChange);
        // 删除旧审批中数据
        inlChangeDao.deleteRepaymentApproveByBillCode(cmsInlChange.getInlSendId());
    }

    /**
     * 查询所有有效的还款计划数据
     *
     * @param id
     */
    public List<RepaymentSchedule> listRepayPlan(String id) {
        return repaymentScheduleService.listRepaymentScheduleByBillNo(id);
    }

    /**
     * 查询所有审批中的还款计划数据
     *
     * @param id
     */
    public List<RepaymentSchedule> listRepaymentScheduleApproveByBillNo(String id) {
        return inlChangeDao.listRepaymentScheduleApproveByBillNo(id);
    }

    /**
     * 获取还款计划主表信息
     *
     * @param id
     * @return
     */
    public CmsRepaymentSchedule getCmsRepaymentSchedule(String id) {
        return repaymentScheduleService.getCmsRepaymentScheduleByBillCode(id);
    }

    /**
     * 获取审批中还款计划主表信息
     *
     * @param id
     * @return
     */
    public CmsRepaymentSchedule getApproveSchedule(String id) {
        return repaymentScheduleService.getApproveScheduleByBillCode(id);
    }

    /**
     * 保存还款计划子表
     *
     * @param id
     */
    public void subSave(String id, CmsInlChangeVO cmsInlChangeVO, ICurrentUser user) {
        // 当还款方式未其他时，则无还款周期，所以需要判断还款方式
//        if (StringUtils.isEmpty(cmsInlChangeVO.getRepayCycel()) && !cmsInlChangeVO.getRepayType().equals("03")) { // 如果还款周期为空直接返回，不保存计划字表
//            return;
//        }
        if (EmptyUtils.isEmptyList(cmsInlChangeVO.getRepayMents())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY);
        }
        for (int i = 0; i < cmsInlChangeVO.getRepayMents().size(); i++) {
            RepayPlanDTO rs = cmsInlChangeVO.getRepayMents().get(i);
            /*
             * if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(rs.getIsRepay())) { continue; }
             */
            // 还款计划明细表
            RepaymentSchedule entity = new RepaymentSchedule();
            if (rs.getPrediceRepayDate() != null) {
                entity.setBillCode(cmsInlChangeVO.getInlSendId());// 单号
                entity.setRepayCycel(cmsInlChangeVO.getRepayCycel());// 还款周期
                entity.setPrediceRepayDate(DateUtils.str2Date(rs.getPrediceRepayDate(), DateUtils.date_sdf()));// 预计还款日
                entity.setRepaymentBeginDate(cmsInlChangeVO.getRepayStartDate());// 还款开始日
                entity.setRepaymentMoney(rs.getRepaymentMoney());// 还款金额
                entity.setRepaymentIst(rs.getRepaymentIst());// 利息
                entity.setCurrentRepayMoney(rs.getCurrentRepayMoney());// 本期还款金额
                entity.setSurplusMoney(rs.getSurplusMoney());// 剩余本金
                entity.setIsRepay(rs.getIsRepay());// 是否已还
                entity.setRemark(rs.getRemark());// 备注
                entity.setCreateBy(user.getLoginName());
                entity.setCreateName(user.getUsername());
                entity.setCreateDate(new Date());
                entity.setValid_Sign("11");// 有效标志 1为有效 -2为无效
                entity.setInternalSign("N2");
                entity.setRelationId(id);
                repaymentScheduleService.saveOrUpdate(entity);
            }
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
