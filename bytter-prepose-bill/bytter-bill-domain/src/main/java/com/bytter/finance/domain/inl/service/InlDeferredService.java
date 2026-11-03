package com.bytter.finance.domain.inl.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.constant.InlWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlDeferredDao;
import com.bytter.finance.domain.inl.entity.CmsInlDeferred;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@WorkFlow(InlWorkFlowConstant.CMS_INL_DEFERRED)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_DEFERRED)
@Service("inlDeferredService")
public class InlDeferredService extends BaseService<CmsInlDeferred, String> implements IWorkFlowListener {
    @Autowired
    private InlDeferredDao inlDeferredDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private InlAdjustrateService inlAdjustrateService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Override
    public BaseDao<CmsInlDeferred, String> getEntityDao() {
        return inlDeferredDao;
    }
    /**
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
        return inlDeferredDao.dataTable(input, corpIds);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_DEFERRED,
                input);
        return workFlowBusiness
                .merge(inlDeferredDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /**
     * 删除大户暂存数据
     *
     * @param id
     */
    public void deleteById(String id) {
        CmsInlDeferred cmsInlDeferred = inlDeferredDao.get(id);
        cmsInlDeferred.setStatus(InlConstant.DELETE);
        inlDeferredDao.update(cmsInlDeferred);
    }
    /**
     * 查询所有已经审批通过的展期数据
     *
     * @param id
     * @return
     */
    public List<CmsInlDeferredVO> getAllCmsInlDeferredBySendId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("inlSendId", id);
        return inlDeferredDao.getCmsInlDeferredVOBySendId(params);
    }
    /**
     * 保存数据
     *
     * @param cmsInlDeferredVO
     * @param user
     */
    public void save(CmsInlDeferredVO cmsInlDeferredVO, ICurrentUser user) {
        CmsInlDeferred cmsInlDeferred;
        if (StringUtils.isNotEmpty(cmsInlDeferredVO.getId())) {
            cmsInlDeferred = inlDeferredDao.get(cmsInlDeferredVO.getId());
            BeanUtils.copyNotNullProperties(cmsInlDeferredVO, cmsInlDeferred);
            cmsInlDeferred.setUpdateBy(user.getId());
            cmsInlDeferred.setUpdateDate(new Date());
        } else {
            cmsInlDeferred = new CmsInlDeferred();
            BeanUtils.copyNotNullProperties(cmsInlDeferredVO, cmsInlDeferred);
            cmsInlDeferred.setCreateBy(user.getId());
            cmsInlDeferred.setCreateDate(new Date());
            cmsInlDeferred.setBillNo(InlUtil.getInlDefereedNo());
        }
        inlDeferredDao.saveOrUpdate(cmsInlDeferred);
        if (InlConstant.COMMIT_APPROVE == cmsInlDeferred.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_DEFERRED, cmsInlDeferred.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_DEFERRED, cmsInlDeferred.getId(), cmsInlDeferred,
                    cmsInlDeferred.getCorpId(), user.getId());
        }
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlDeferred cmsInlDeferred = inlDeferredDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        CmsInlSend cmsInlSend = null;
        if (isPass) {
            cmsInlDeferred.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlDeferred.getBillNo(), InlConstant.CMS_INL_DEFERRED_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
            cmsInlSend = inlSendService.get(cmsInlDeferred.getInlSendId());
            cmsInlSend.setDeferredRate(cmsInlDeferred.getDeferredRate());
            cmsInlSend.setIsDeferred(CommonConstant.YES);
            cmsInlSend.setDeferredEndDate(cmsInlDeferred.getNowDeferredDate());
            inlSendService.updateEntity(cmsInlSend);
        } else {
            cmsInlDeferred.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlDeferred.getId(), cmsInlDeferred.getCorpId(),
                    NotificationConstant.CMS.INL_DEFERRED, cmsInlDeferred);
            msService.insertProcessMsg(cmsInlDeferred.getBillNo(), InlConstant.CMS_INL_DEFERRED_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        // updateBudget(dfDirectFinanceInfo, user);
        inlDeferredDao.update(cmsInlDeferred);
        /*
         * if (isPass) { // 展期审批通过,更新还款计划还款方式不为其他时则更新还款计划 if
         * (!CmsConstant.REPAY_TYPE_03.equals(cmsInlSend.getRepayType())) {
         * inlAdjustrateService.updateReplayPlan(cmsInlSend.getId(), user); } }
         */
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
