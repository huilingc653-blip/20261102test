package com.bytter.finance.domain.loan.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.loan.dao.CmsLoanDeferredDao;
import com.bytter.finance.domain.loan.entity.CmsLoanDeferred;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.vo.CmsLoanDeferredVO;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款展期申请表
 * @date 2017-08-04 14:22:49
 */
@WorkFlow(CmsWorkFlowConstant.CMS_LOAN_DEFERRED)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LOAN_DEFERRED)
@Service("cmsLoanDeferredService")
@Transactional
public class CmsLoanDeferredService extends BaseService<CmsLoanDeferred, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private CmsLoanDeferredDao cmsLoanDeferredDao;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ICmsFcAccService cmsFcAccService;
    /**
     * <b>功能介绍：</b>查询可编辑展期数据<br>
     * <b>日期：</b>2017年8月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getEditDataTables(DataTablesInput input, String type, String userId) {
        List<String> corpAuth = new ArrayList<String>();
        if (CmsConstant.IS_FALSE.equals(type)) {// 展期修改
            corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_DEFERRED_QUERY, userId);
        } else {
            corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_DEFERREDPRO_QUERY, userId);
        }
        return cmsLoanDeferredDao.getEditDataTables(input, type, corpAuth, userId);
    }
    /**
     * <b>功能介绍：</b>查询列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput listDeferredApproveData(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LOAN_DEFERRED,
                dataTablesInput);
        DataTablesOutput dp = cmsLoanDeferredDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * <b>功能介绍：</b>查询贷款发放信息<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanSend getCmsLoanSend(String billCode) {
        return cmsLoanSendService.getLoanSendByBillCode(billCode);
    }
    /**
     * <b>功能介绍：</b>查询贷款申请信息<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanInfoVO getCmsLoanInfo(String billCode) {
        return cmsLoanSendService.getLoanInfoByLoanSend(cmsLoanSendService.getLoanSendByBillCode(billCode));
    }
    /**
     * <b>功能介绍：</b>查询已还金额信息<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getRepayedMoney(String sendCode) {
        return cmsLoanSendService.getSendRepayedMoney(sendCode);
    }
    /**
     * <b>功能介绍：</b>生成展期申请票据号<br>
     * <b>日期：</b>2017年8月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getLoanDeferredBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_LOANDEFERRED_BILL_CODE, prefix);
    }
    /**
     * <b>功能介绍：</b>是否多次展期<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    public String isMoreDeferred() {
        return cmsLoanSendService.isMoreDeferred();
    }
    /**
     * <b>功能介绍：</b>保存展期申请<br>
     * <b>日期：</b>2017年8月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void saveLoanDeferred(CmsLoanDeferred entity, ICurrentUser user) {
        if (cmsLoanDeferredDao.existsContractCode(entity.getContractCode(), null)) {// 校验编号重复
            throw new ParameterException(LoanMessageEnum.CMS_LOANDEFERRED_CONTRACTCODE_EXISTS);
        }
        entity.setBillCode(getLoanDeferredBillCode(CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX));
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.TEMP);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);
            setSendDeferreding(entity, CmsConstant.EXTENDDATING);// 修改发放状态为展期申请中
        } else if (entity.getStatus() == 1) {// 提交
            entity.setStatus(CmsConstant.COMMITAPPROVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);
        }
        if (EmptyUtils.isEmpty(entity.getLoanAcc())) {
            entity.setLoanAcc("null");
        }
        entity.setCreateBy(user.getId());
        entity.setCreateName(user.getUsername());
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(new Date());
        }
        super.saveEntity(entity);
        if (entity.getStatus() == CmsConstant.COMMITAPPROVE) {// 启动审批
            //TODO 审批流程不通
            loanDeferredApply(entity, user);
            setSendDeferreding(entity, CmsConstant.EXTENDDATING);// 修改状态为展期申请中
            msService.insertProcessMsg(entity.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_APPLY_SUBMIT, user);
            msService.insertProcessMsg(entity.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_APPLY_SUBMIT, user);
        } else {
            msService.insertProcessMsg(entity.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
            msService.insertProcessMsg(entity.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
        }
    }
    /**
     * <b>功能介绍：</b>修改展期申请<br>
     * <b>日期：</b>2017年8月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void modifyCmsLoanDeferred(CmsLoanDeferred entity, ICurrentUser user) {
        if (cmsLoanDeferredDao.existsContractCode(entity.getContractCode(), entity.getId())) {// 校验编号重复
            throw new ParameterException(LoanMessageEnum.CMS_LOANDEFERRED_CONTRACTCODE_EXISTS);
        }
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.TEMP);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);
        } else if (entity.getStatus() == 1) {// 提交
            entity.setStatus(CmsConstant.COMMITAPPROVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);
        }
        CmsLoanDeferred t = getEntityDao().get(entity.getId());
        BeanUtils.copyNotNullProperties(entity, t);
        // 请求侧会去掉 createDate，copyNotNull 不会用 null 覆盖；库中原本为空时再补写
        if (t.getCreateDate() == null) {
            t.setCreateDate(new Date());
        }
        super.saveOrUpdate(t);
        notificationService.complete(NotificationConstant.CMS.LOAN_DEFERRED, t.getId(), user.getId());
        if (t.getStatus() == CmsConstant.COMMITAPPROVE) {
            //TODO 工作流
            loanDeferredApply(t, user);
            msService.insertProcessMsg(t.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_SUBMIT, user);
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_SUBMIT, user);
        } else {
            msService.insertProcessMsg(t.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_TEMP, user);
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_TEMP, user);
        }
    }
    /**
     * <b>功能介绍：</b>展期处理操作<br>
     * <b>日期：</b>2017年11月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param entity
     * @version 1.0
     */
    public void doHandleLoanDeferred(CmsLoanDeferred entity, ICurrentUser user) {
        CmsLoanDeferred deferred = this.get(entity.getId());
        notificationService.complete(NotificationConstant.CMS.LOAN_DEFERRED_CONFIRM, deferred.getId(), user.getId());
        if (entity.getStatus() == 0) {// 打回
            deferred.setStatus(CmsConstant.BACK);
            deferred.setBizStatus(CmsConstant.BUSINESSSTATE_69);
            msService.insertProcessMsg(deferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            msService.insertProcessMsg(deferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            notificationService.sendToUser(deferred.getId(), deferred.getCreateBy(),
                    NotificationConstant.CMS.LOAN_DEFERRED, deferred);
            notificationService.complete(NotificationConstant.CMS.LOAN_DEFERRED_CONFIRM, entity.getId(), user.getId());
        } else {// 确认通过
            deferred.setStatus(CmsConstant.COMPELETOTAL);
            deferred.setBizStatus(CmsConstant.BUSINESSSTATE_65);
            CmsLoanInfo cmsLoanInfo = cmsLoanInfoService.get(getCmsLoanInfo(deferred.getLiCode()).getId());
            cmsLoanInfo.setDeferredRate(deferred.getDeferredRate());
            CmsLoanSend cmsLoanSend = cmsLoanSendService.getLoanSendByBillCode(deferred.getLiCode());
            cmsLoanSend.setStatus(CmsConstant.COMPELTEXTENDATE);
            cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_65);
            cmsLoanSend.setDeferredRate(deferred.getDeferredRate());
            cmsLoanSend.setDeferredDate(deferred.getDeferredRepayDate());
            cmsLoanInfoService.saveOrUpdate(cmsLoanInfo);// 修改贷款申请的展期利率
            cmsLoanSendService.saveOrUpdate(cmsLoanSend);// 修改发放状态为展期申请完成,展期利率,结束时间
            if (EmptyUtils.isNotEmpty(cmsLoanSend.getLoanAcc())) {
                //TODO 更新贷款账号展期日期
                cmsFcAccService.updateLoanAccDeferredDate(cmsLoanSend.getLoanAcc(), deferred.getDeferredRepayDate(),
                        deferred.getDeferredRate());
//                cmsLoanDeferredDao.updateLoanAccDeferredDate(cmsLoanSend.getLoanAcc(), deferred.getDeferredRepayDate(),
//                        deferred.getDeferredRate());
            }
            msService.insertProcessMsg(deferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_SUCCESS, user);
            msService.insertProcessMsg(deferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_SUCCESS, user);
        }
        this.saveOrUpdate(deferred);
    }
    /**
     * <b>功能介绍：</b>启动展期审批<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void loanDeferredApply(CmsLoanDeferred deferred, ICurrentUser user) {
         getStartHandler().handler(CmsWorkFlowConstant.CMS_LOAN_DEFERRED, deferred.getId(), deferred, deferred.getCorpId(),
                user.getId());
    }
    /**
     * <b>功能介绍：</b>根据发放申请编号查询已审批通过的展期申请<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanDeferred> getCmsLoanDeferredByLiCode(String liCode) {
        return cmsLoanDeferredDao.getCmsLoanDeferredByLiCode(liCode);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsLoanDeferred entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsLoanDeferred entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsLoanDeferred entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsLoanDeferred entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<CmsLoanDeferred, String> getEntityDao() {
        return cmsLoanDeferredDao;
    }
    /**
     * <b>功能介绍：</b>发放申请对应的展期申请列表<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanDeferredVO> getCmsLoanDeferredItems(String liCode) {
        return cmsLoanDeferredDao.getCmsLoanDeferredItems(liCode);
    }
    /**
     * <b>功能介绍：</b>设置发放申请为展期申请中(记账完成)<br>
     * <b>日期：</b>2017年9月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param deferred
     * @version 1.0
     */
    public void setSendDeferreding(CmsLoanDeferred deferred, int status) {
        CmsLoanSend cmsLoanSend = cmsLoanSendService.getLoanSendByBillCode(deferred.getLiCode());
        cmsLoanSend.setStatus(status);
        if (status == CmsConstant.EXTENDDATING) {// 展期申请中
            cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_68);
        }
        if (status == CmsConstant.COMPELETOTAL) {
            if (cmsLoanSendService.getInwayMoney(cmsLoanSend.getLoanCode()).intValue() > 0) {// 在途金额大于0为还款中
                cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_33);
            } else {// 待还款
                cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            }
        }
        cmsLoanSendService.saveOrUpdate(cmsLoanSend);// 修改发放状态
    }
    /**
     * 审批启动回调
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // String businessKey = workFlowResult.getBusinessKey();
        // if(StringUtils.isEmpty(businessKey)){
        // throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        // }
        // CmsLoanDeferred deferred = this.get(businessKey,true);
        // setSendDeferreding(deferred,CmsConstant.EXTENDDATING);//修改发放状态为展期申请中
    }
    /**
     * 审批完成处理
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsLoanDeferred deferred = this.get(businessKey, true);
        if (result) {
            deferred.setStatus(CmsConstant.APPROVED);
            deferred.setBizStatus(CmsConstant.BUSINESSSTATE_78);
            notificationService.send(businessKey, deferred.getCorpId(), NotificationConstant.CMS.LOAN_DEFERRED_CONFIRM,
                    deferred);
            msService.insertProcessMsg(deferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_CHECKOK, user);
            msService.insertProcessMsg(deferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_CHECKOK, user);
        } else {
            deferred.setStatus(CmsConstant.BACK);// 打回
            deferred.setBizStatus(CmsConstant.BUSINESSSTATE_67);
            msService.insertProcessMsg(deferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_APPLY_BACK, user);
            msService.insertProcessMsg(deferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_APPLY_BACK, user);
        }
        this.saveOrUpdate(deferred);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsLoanDeferredDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_LOAN_INFO_ERROR2));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
}
