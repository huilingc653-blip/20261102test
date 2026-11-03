package com.bytter.finance.domain.precative.service;
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
import com.bytter.finance.constant.PrecativeConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanDeferredDao;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanDeferred;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanDeferredVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.dto.CmsPrecativeLoanDeferredDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.PrecativeMessageEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: 委托贷款展期申请表
 * @author onlineGenerator
 * @date 2017-08-03 10:48:22
 * @version 1.0
 *
 */
@WorkFlow({ CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED, CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED_REPAIR })
@Service("cmsPrecativeLoanDeferredService")
@Transactional
public class CmsPrecativeLoanDeferredService extends BaseService<CmsPrecativeLoanDeferred, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private CmsPrecativeLoanDeferredDao cmsPrecativeLoanDeferredDao;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 委托贷款展期申请table表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryApply(DataTablesInput input, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        boolean loanDfNum = !configService.getConfig("cms", "LoanDfNum").getValue().equals("1");// 多次
        // 一次
        DataTablesOutput res = cmsPrecativeLoanDeferredDao.queryApply(input, loanDfNum, corpAuth);
        List<PrecativeLoanDeferredVo> deferred = res.getData();
        List<PrecativeLoanDeferredVo> list = new ArrayList<PrecativeLoanDeferredVo>();
        for (PrecativeLoanDeferredVo vo : deferred) {
            int num = cmsPrecativeLoanDeferredDao.getPrecativeLoanSendCountByBillCode(vo.getSendNo());
            if (loanDfNum) {
                vo.setDeferredNum(num);
                list.add(vo);
            } else {
                if (num == 0) {
                    vo.setDeferredNum(num);
                    list.add(vo);
                }
            }
        }
        res.setData(list);
        return res;
    }
    /**
     * 获取凭证号
     */
    protected String getPrecativeBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PRECATIVE_DEFERRED_ORDER_NO, prefix);
    }
    /**
     * 委托贷款展期保存
     */
    public void save(CmsPrecativeLoanDeferred entity, ICurrentUser user) {
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(entity.getLiCode());
        verify(entity);
        if (entity.getStatus() == CmsConstant.COMMITAPPROVE) {// 11
            entity.setStatus(CmsConstant.COMMITAPPROVE);// 提交审批
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);// 展期待审批
            entity.setBillCode(getPrecativeBillCode("C4"));
            cmsPrecativeLoanDeferredDao.updateSendStatus(entity, CmsConstant.EXTENDDATING,
                    CmsConstant.BUSINESSSTATE_68);// 更改发放表状态
        }
        if (entity.getStatus() == CmsConstant.TEMP) {// 0
            entity.setBillCode(getPrecativeBillCode("C4"));
            entity.setStatus(CmsConstant.TEMP);// 暂存
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);// 展期暂存
            cmsPrecativeLoanDeferredDao.updateSendStatus(entity, CmsConstant.EXTENDDATING,
                    CmsConstant.BUSINESSSTATE_68);
        }
        entity.setCreateBy(user.getLoginName());
        entity.setCreateName(user.getUsername());
        entity.setCreateDate(new Date());
        super.saveEntity(entity);
        if (entity.getStatus() == CmsConstant.COMMITAPPROVE) {// 提交审批
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_APPLY_SUBMIT, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_APPLY_SUBMIT, user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED, entity.getId(), entity,
                    entity.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
        }
    }
    /**
     * 委托贷款展期修改/打回table表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryTable(DataTablesInput input, String pageType, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        DataTablesOutput res = cmsPrecativeLoanDeferredDao.queryTable(input, pageType, corpAuth);
        List<PrecativeLoanDeferredVo> deferred = res.getData();
        /*
         * for (PrecativeLoanDeferredVo vo : deferred) {
         * vo.setDeferredNum(cmsPrecativeLoanDeferredDao.getPrecativeLoanSendCountByBillCode(vo.getSendNo())); }
         * res.setData(deferred);
         */
        return res;
    }
    /**
     * 委托贷款展期处理列表
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryHandle(DataTablesInput input, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        return cmsPrecativeLoanDeferredDao.queryHandle(input, corpAuth);
    }
    /**
     * 委托贷款展期修改/打回页面明细
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanDeferred getDeferredByLiCode(String sendNo) {
        return cmsPrecativeLoanDeferredDao.getDeferredByLiCode(sendNo);
    }
    /**
     * 委托贷款展期修改/打回页面明细
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanDeferred getDeferredByBillCode(String sendNo) {
        return cmsPrecativeLoanDeferredDao.getDeferredByBillCode(sendNo);
    }
    /**
     * 获取展期次数明细列表
     */
    @Transactional(readOnly = true)
    public List<PrecativeLoanDeferredVo> getPrecativeLoanSendListByBillCode(String liCode) {
        return cmsPrecativeLoanDeferredDao.getPrecativeLoanSendListByBillCode(liCode);
    }
    /**
     * 委托贷款修改/打回保存or更新
     */
    public void addUpdate(CmsPrecativeLoanDeferred entity, ICurrentUser user) {
        String contractCode = entity.getContractCode();
        String workFlow = "";
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(entity.getLiCode());
        if (entity.getStatus() == CmsConstant.COMMITAPPROVE) {
            verify(entity);
            entity.setStatus(CmsConstant.COMMITAPPROVE);// 提交审批
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);// 展期待审批
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_SUBMIT, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_SUBMIT, user);
            workFlow = CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED;
        }
        if (entity.getStatus() == CmsConstant.BACK) {
            entity.setStatus(CmsConstant.BACK);// 打回
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_66);// 展期打回
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_APPLY_BACK, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_APPLY_BACK, user);
        }
        if (entity.getStatus() == -3) {
            entity.setStatus(CmsConstant.BACK);// 打回
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_69);// 展期打回
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            notificationService.complete(NotificationConstant.CMS.PRECATIVE_DEFERRED_CONFIRM, entity.getId(),
                    user.getId());
        }
        if (entity.getStatus() == CmsConstant.TEMP) {
            verify(entity);
            entity.setStatus(CmsConstant.TEMP);// 暂存
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);// 展期暂存
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_TEMP, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_UPDATE_TEMP, user);
        }
        if (entity.getStatus() == CmsConstant.REPAIRAPPRAVE) {// 补录提交
            verifyAdd(entity);
            boolean flag = cmsPrecativeLoanDeferredDao.verifyContractCode(contractCode, entity.getBillCode());
            if (!flag) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CONTRACTCODE_NOT_UNIQUE);
            }
            entity.setStatus(CmsConstant.REPAIRAPPRAVE);// 补录审批
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_71);// 展期补录待审批
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR, CmsProcessMsgConstant.MSGA_DEF_APPLY_REPAIR, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR, CmsProcessMsgConstant.MSGA_DEF_APPLY_REPAIR, user);
            workFlow = CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED_REPAIR;
        }
        if (entity.getStatus() == CmsConstant.FINISH) {// 110业务结束
            // verify(entity);
            CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
            notificationService.complete(NotificationConstant.CMS.PRECATIVE_DEFERRED_CONFIRM, entity.getId(),
                    user.getId());
            entity.setStatus(CmsConstant.FINISH);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_65);// 业务结束
            cmsPrecativeLoanDeferredDao.updateSendStatus(entity, CmsConstant.COMPELTEXTENDATE,
                    CmsConstant.BUSINESSSTATE_65);// 更改发放表状态
            notificationService.send(entity.getId(), info.getPrecativeId(),
                    NotificationConstant.CMS.PRECATIVE_DEFERRED_SUCCESS, entity);
            notificationService.send(entity.getId(), info.getLoanOpp(),
                    NotificationConstant.CMS.PRECATIVE_DEFERRED_SUCCESS, entity);
        }
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_DEFERRED, entity.getId(), user.getId());
        // notificationService.complete(NotificationConstant.CMS.PRECATIVE_DEFERRED, entity.getId());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateName(user.getUsername());
        entity.setUpdateDate(new Date());
        CmsPrecativeLoanDeferred t = super.get(entity.getId());
        BeanUtils.copyNotNullProperties(entity, t);
        super.updateEntity(t);
        if (EmptyUtils.isNotEmpty(workFlow)) {
             getStartHandler().handler(workFlow, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
    }
    /**
     * 委托贷款展期删除
     */
    public int deleteDef(String id, String userId) {
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredDao.get(id);
        cmsPrecativeLoanDeferredDao.updateSendStatus(deferred, CmsConstant.COMPELETOTAL, CmsConstant.BUSINESSSTATE_42);// 更改发放表状态
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_DEFERRED, deferred.getId(), userId);
        return cmsPrecativeLoanDeferredDao.deleteDef(deferred);
    }
    /** 审批启动完成回调 **/
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /** 审批结果回调方法 **/
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {// 业务id
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsPrecativeLoanDeferred deferred = this.get(workFlowResult.getBusinessKey());
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(deferred.getLiCode());
        Integer status = null;
        String bizStatus = null;
        if (workFlowResult.getProcessDefinitionKey().equals(CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED)) {// 委托贷款展期申请
            status = workFlowResult.isPass() ? CmsConstant.APPROVED : CmsConstant.BACK;// 通过or打回
            bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_72 : CmsConstant.BUSINESSSTATE_67;// 展期待补录or展期审批打回
            if (status == CmsConstant.APPROVED) {// 加入日志
                // 提醒有权限的用户进行处理
                notificationService.send(workFlowResult.getBusinessKey(), deferred.getCorpId(),
                        NotificationConstant.CMS.PRECATIVE_DEFERRED_REPAIR, deferred);
                msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_CHECKOK, null);
                msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_CHECKOK, null);
            } else {
                msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_CHECKBACK, null);
                msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_CHECKBACK, null);
            }
        } else if (workFlowResult.getProcessDefinitionKey().equals(CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED_REPAIR)) {// 委托贷款展期补录
            CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
            if (CmsConstant.IS_FALSE.equals(configService.getConfig("cms", "is_entrust_tally").getValue()) // 跳过展期处理
                    || CmsConstant.IS_BANK_1.equals(info.getIsBank())) {
                /*
                 * deferred.setStatus(CmsConstant.FINISH);//业务结束 deferred.setBizStatus(CmsConstant.BUSINESSSTATE_110);
                 */
                status = workFlowResult.isPass() ? CmsConstant.FINISH : CmsConstant.APPROVED;// 通过or打回
                bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_65 : CmsConstant.BUSINESSSTATE_77;// 展期完成or展期打回
                cmsPrecativeLoanDeferredDao.updateSendStatus(deferred, CmsConstant.COMPELTEXTENDATE,
                        CmsConstant.BUSINESSSTATE_65);// 更改发放表状态
                // 通知消息给委托人，借款人展期成功
                if (status == CmsConstant.FINISH) {// 加入日志
                    notificationService.send(workFlowResult.getBusinessKey(), info.getPrecativeId(),
                            NotificationConstant.CMS.PRECATIVE_DEFERRED_SUCCESS, deferred);
                    notificationService.send(workFlowResult.getBusinessKey(), info.getLoanOpp(),
                            NotificationConstant.CMS.PRECATIVE_DEFERRED_SUCCESS, deferred);
                    msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEFERRED_APPLY_SUCCESS,
                            null);
                    msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEFERRED_APPLY_SUCCESS,
                            null);
                } else {
                    msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK, null);
                    msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK, null);
                }
            } else {
                status = workFlowResult.isPass() ? CmsConstant.COMPELTEXTENDATE : CmsConstant.APPROVED;// 通过or打回
                bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_78 : CmsConstant.BUSINESSSTATE_77;// 展期待处理or展期打回
                if (status == CmsConstant.COMPELTEXTENDATE) {
                    notificationService.send(workFlowResult.getBusinessKey(), deferred.getCorpId(),
                            NotificationConstant.CMS.PRECATIVE_DEFERRED_CONFIRM, deferred);
                    msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKOK, null);
                    msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKOK, null);
                } else {
                    msService.insertProcessMsg(deferred.getBillCode(), PrecativeConstant.DEFERRED_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK, null);
                    msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK, null);
                }
            }
        }
        deferred.setStatus(status);
        deferred.setBizStatus(bizStatus);
        cmsPrecativeLoanDeferredDao.update(deferred);
    }
    /**
     * 加载申请审核列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        DataTablesOutput dp = cmsPrecativeLoanDeferredDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 加载补录审核列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableRepairApprove(@RequestBody DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PRECATIVE_DEFERRED_REPAIR, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        DataTablesOutput dp = cmsPrecativeLoanDeferredDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 获取委托贷款展期申请
     */
    @Transactional(readOnly = true)
    public List<CmsPrecativeLoanDeferredDTO> getCmsPrecativeDeferreds() {
        return cmsPrecativeLoanDeferredDao.getCmsPrecativeDeferreds();
    }
    /**
     * 后台验证申请信息
     */
    public void verify(CmsPrecativeLoanDeferred plf) {
        /** 展期开始日期不能为空 **/
        /*
         * if (EmptyUtils.isEmpty(plf.getDeferredStartDate())) { throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDSTARTDATE_NOT_EMPTY); }
         */
        /** 展期到期日期不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredRepayDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDREPAYDATE_NOT_EMPTY);
        }
        /** 展期期限不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredLimt())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDLIMT_NOT_EMPTY);
        }
        /** 展期利率不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredRate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDRATE_NOT_EMPTY);
        }
        /** 日期对比 **/
        /*
         * int i = plf.getDeferredStartDate().compareTo( plf.getDeferredRepayDate()); if(i >= 0){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERRED_NOT_UNIQUE); }
         */
        /** 日期对比--展期开始日期和贷款到期日 **/
        /*
         * int b = plf.getDeferredStartDate().compareTo( plf.getMatureDate()); if(b<0){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERRED_NOT_UNIQUE_DATE); }
         */
    }
    /**
     * 后台验证补录信息
     */
    public void verifyAdd(CmsPrecativeLoanDeferred plf) {
        /** 展期开始日期不能为空 **/
        /*
         * if (EmptyUtils.isEmpty(plf.getDeferredStartDate())) { throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDSTARTDATE_NOT_EMPTY); }
         */
        /** 展期到期日期不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredRepayDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDREPAYDATE_NOT_EMPTY);
        }
        /** 展期期限不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredLimt())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDLIMT_NOT_EMPTY);
        }
        /** 展期利率不能为空 **/
        if (EmptyUtils.isEmpty(plf.getDeferredRate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERREDRATE_NOT_EMPTY);
        }
        /** 展期合同号 **/
        if (EmptyUtils.isEmpty(plf.getContractCode())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CONTRACTCODE_NOT_EMPTY);
        }
        /** 日期对比--展期开始日期和展期到期日期 **/
        /*
         * int i = plf.getDeferredStartDate().compareTo( plf.getDeferredRepayDate()); if(i >= 0){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERRED_NOT_UNIQUE); }
         */
        /** 日期对比--展期开始日期和贷款到期日 **/
        /*
         * int b = plf.getDeferredStartDate().compareTo( plf.getMatureDate()); if(b<0){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEFERRED_NOT_UNIQUE_DATE); }
         */
    }
    public BaseDao<CmsPrecativeLoanDeferred, String> getEntityDao() {
        return cmsPrecativeLoanDeferredDao;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsPrecativeLoanDeferredDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR3));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
    }
}
