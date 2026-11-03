package com.bytter.finance.domain.provide.service;

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
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.provide.dao.ProvideLoanDeferredDao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.ProvideLoanDeferredVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: CMS_PROVIDE_LOAN_SEND
 * @author onlineGenerator
 * @date 2017-07-24 14:13:13
 * @version 1.0
 *
 */
@WorkFlow({ CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED, CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED_REPAIR })
@Service("provideLoanDeferredService")
@Transactional
public class ProvideLoanDeferredService extends BaseService<ProvideLoanDeferred, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private ProvideLoanDeferredDao provideLoanDeferredDao;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private IWorkFlowService workFlowService;
   
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 功能介绍 :借款展期申请查询页面table表<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(DataTablesInput input, ICurrentUser user) {
        boolean loanDfNum = !configService.getConfig("cms", "BorrowDfNum").getValue().equals("1");// 多次
        // 一次
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_DEFERRED_APPLY_QUERY,
                user.getId());
        DataTablesOutput res = provideLoanDeferredDao.queryAll(input, loanDfNum, corpAuth);
        List<ProvideLoanDeferredVO> deferred = res.getData();
        List<ProvideLoanDeferredVO> list = new ArrayList<ProvideLoanDeferredVO>();
        for (ProvideLoanDeferredVO vo : deferred) {
            int num = provideLoanDeferredDao.getProvideLoanSendCountByBillCode(vo.getProvideCode());
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
     * 功能介绍 :借款展期申请页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public ProvideLoanDeferredVO application(String id) {
        return provideLoanDeferredDao.application(id);
    }
    /**
     * 功能介绍：借款展期补录列表<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>更新者：</b>zhengxn<br>
     *         <b>日期：</b> 2017-5-1 <br>
     *         <b>更新内容：</b> 修改XXXX <br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryTable(DataTablesInput input, ICurrentUser user, String pageType) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_DEFERRED_ADDIT_QUERY,
                user.getId());
        return provideLoanDeferredDao.queryTable(input, pageType, user.getLoginName(), corpAuth);
    }
    /**
     * 功能介绍：借款展期处理列表<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>更新者：</b>zhengxn<br>
     *         <b>日期：</b> 2017-5-1 <br>
     *         <b>更新内容：</b> 修改XXXX <br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryHandle(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_DEFERRED_HANDLE_QUERY,
                user.getId());
        DataTablesOutput output = provideLoanDeferredDao.queryHandle(input, corpAuth);
        List<ProvideLoanDeferredVO> data = output.getData();
        List<String> corpIds = data.stream().map(ProvideLoanDeferredVO::getCorpId).collect(Collectors.toList());
        Map<String, String> corpNameMap = iCorpService.getNameByIds(corpIds);
        data.forEach(a -> {
            a.setCorpName(corpNameMap.get(a.getCorpId()));
            a.setNetName(corpNameMap.get(a.getCorpId()));
        });
        return output;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        DataTablesOutput dp = provideLoanDeferredDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 加载补录审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getListDataTable(@RequestBody DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED_REPAIR, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        DataTablesOutput dp = provideLoanDeferredDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍：查询在途金额<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>更新者：</b>zhengxn<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public ProvideLoanDeferredVO queryPassageMoney(String id) {
        return provideLoanDeferredDao.queryPassageMoney(id);
    }
    /**
     * 功能介绍：更改发放表状态<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>更新者：</b>zhengxn<br>
     *         <b>日期：</b> 2017-5-1 <br>
     */
    public void updateSend(ProvideLoanDeferred entity, ProvideLoanSend send) {
        if (CmsConstant.TEMP == entity.getStatus().intValue()) {// 暂存
            send.setStatus(CmsConstant.EXTENDDATING);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_60);
        } else if (CmsConstant.COMMITAPPROVE == entity.getStatus().intValue()) {// 提交
            send.setStatus(CmsConstant.EXTENDDATING);// 展期申请中
            send.setBizStatus(CmsConstant.BUSINESSSTATE_61);// 展期待审批
        } else if (CmsConstant.FINISH == entity.getStatus().intValue()) {// 业务结束
            send.setStatus(CmsConstant.COMPELTEXTENDATE);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_65);
            send.setDeferredDate(entity.getDeferredRepayDate());
            send.setDeferredRate(entity.getDeferredRate());// 设置展期利率
        }
    }
    /**
     * 功能介绍：借款展期修改删除<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     */
    public int deleteDef(Integer status, String id, ICurrentUser user) {
        ProvideLoanDeferred deferred = this.get(id);
        // 将发放表状态修改成还款
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
        send.setStatus(CmsConstant.COMPELETOTAL);
        send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
        cmsProvideLoanSendService.saveOrUpdate(send);
        msService.insertProcessMsg(deferred.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_DELETE, user);
        msService.insertProcessMsg(deferred.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_DELETE, user);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED, id, user.getId());
        return provideLoanDeferredDao.deleteDef(status, id);
    }
    /**
     * 功能介绍：保存<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     */
    public void save(ProvideLoanDeferred entity, ICurrentUser user) {
        verify(entity);
        if (entity.getStatus() == 11) {// 提交审批
            entity.setStatus(CmsConstant.COMMITAPPROVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);// 展期待审批
        } else if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.TEMP);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);// 展期暂存
        }
        entity.setCreateBy(user.getLoginName());
        entity.setCreateName(user.getUsername());
        entity.setCreateDate(new Date());
        entity.setBillCode(getLoanDeferredBillCode());
        super.saveEntity(entity);
        // 更改发放表状态
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(entity.getLiCode());
        updateSend(entity, send);
        cmsProvideLoanSendService.update(send, user);
        // 审批
        if (entity.getStatus() == 11) {// 提交审批
            msService.insertProcessMsg(entity.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_SUBMIT, user);
            msService.insertProcessMsg(entity.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_SUBMIT, user);
            // 默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED, entity.getId(), entity,
                    entity.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(entity.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
            msService.insertProcessMsg(entity.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_DEF_APPLY_TEMP, user);
        }
    }
    // 审批启动完成回调
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    // 审批结果回调方法
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        ProvideLoanDeferred deferred = this.get(workFlowResult.getBusinessKey(), true);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
        Integer status = null;
        String bizStatus = null;
        Integer bondStatus = null;// 定义一个用于存放保证金状态的变量
        ICurrentUser user = workFlowResult.getUser();
        if (workFlowResult.getProcessDefinitionKey().equals(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED)) {// 提交审批
            status = workFlowResult.isPass() ? CmsConstant.APPROVED : CmsConstant.BACK;
            bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_72 : CmsConstant.BUSINESSSTATE_67;// 展期待补录or展期审批打回
            send.setBizStatus(bizStatus);// 设置发放的展期显示状态
            if (workFlowResult.isPass()) {
                msService.insertProcessMsg(deferred.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_CHECKOK,
                        workFlowResult.getUser());
                msService.insertProcessMsg(deferred.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_CHECKOK,
                        workFlowResult.getUser());
                notificationService.send(deferred.getId(), deferred.getCorpId(),
                        NotificationConstant.CMS.PROVIDE_DEFERRED_REPAIR, deferred);
                bondStatus = FcBondConstant.STATUE_APPLY;// 保证金占用信息的状态
            } else {
                msService.insertProcessMsg(deferred.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_CHECKBACK,
                        workFlowResult.getUser());
                msService.insertProcessMsg(deferred.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_PROVIDE_DEF_CHECKBACK,
                        workFlowResult.getUser());
                notificationService.send(deferred.getId(), deferred.getCorpId(),
                        NotificationConstant.CMS.PROVIDE_DEFERRED, deferred);
                bondStatus = FcBondConstant.STATUE_BACK;// 保证金占用信息的状态
            }
        } else if (workFlowResult.getProcessDefinitionKey().equals(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED_REPAIR)) {// 补录审批
            CorpDTO corp = iCorpService.get(deferred.getCorpId());
            if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是网点
                status = workFlowResult.isPass() ? CmsConstant.COMPELTEXTENDATE : CmsConstant.APPROVED;
                bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_78 : CmsConstant.BUSINESSSTATE_80;// 展期待处理or展期补录打回
                send.setBizStatus(bizStatus);// 设置发放的展期显示状态
            } else {
                status = workFlowResult.isPass() ? CmsConstant.FINISH : CmsConstant.APPROVED;
                bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_65 : CmsConstant.BUSINESSSTATE_77;
                send.setStatus(CmsConstant.COMPELETOTAL);// 展期成功修改成待还款状态
                send.setBizStatus(CmsConstant.BUSINESSSTATE_65);// 设置发放的展期显示状态
                deferred.setStatus(status);
                updateSend(deferred, send);
            }
            if (workFlowResult.isPass()) {
                msService.insertProcessMsg(deferred.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKOK,
                        workFlowResult.getUser());
                msService.insertProcessMsg(deferred.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKOK,
                        workFlowResult.getUser());
                if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是网点
                    notificationService.send(deferred.getId(), deferred.getCorpId(),
                            NotificationConstant.CMS.PROVIDE_DEFERRED_CONFIRM, deferred);
                } else {
                    // 根据还款计划和计息方式生成利息明细数据
                    cmsProvideInterestService.adjustProvideInterest(send, user);
                }
                bondStatus = FcBondConstant.STATUE_APPLY;// 保证金占用信息的状态
            } else {
                msService.insertProcessMsg(deferred.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK,
                        workFlowResult.getUser());
                msService.insertProcessMsg(deferred.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_CHECKBACK,
                        workFlowResult.getUser());
                notificationService.send(deferred.getId(), deferred.getCorpId(),
                        NotificationConstant.CMS.PROVIDE_DEFERRED_REPAIR, deferred);
                bondStatus = FcBondConstant.STATUE_BACK;// 保证金占用信息的状态
            }
        }
        deferred.setStatus(status);
        deferred.setBizStatus(bizStatus);
        provideLoanDeferredDao.saveOrUpdate(deferred);
        cmsProvideLoanSendService.merge(send);// 更新发放信息
        // 展期审批通过更新保证金占用信息的到期日期
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(info.getId(), info.getBillCode());
        if (bondStatus.equals(FcBondConstant.STATUE_CHECK)) {
            if (dto != null) {
                String loginName = workFlowResult.getUser().getLoginName();
                dto.setUpdateBy(loginName);
                dto.setUpdateDate(DateUtils.getDate());
                dto.setEndDate(deferred.getDeferredRepayDate());
                fcBondHookService.updateFcBondApplyDate(dto);// 更新保证金占用信息的状态
            }
        }
    }
    /**
     * 修改操作
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     */
    public void saveOrUpdate(String modifySign, ProvideLoanDeferred entity, ICurrentUser user) {
        String contractCode = entity.getContractCode();
        ProvideLoanDeferred t = provideLoanDeferredDao.get(entity.getId());
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(t.getLiCode());
        Integer pageStatus = entity.getStatus();
        Integer bondStatus = null;
        if (pageStatus == CmsConstant.REPAIRAPPRAVE) {// 补录审批
            verifyAdd(entity);
            boolean flag = provideLoanDeferredDao.verifyContractCode(contractCode, entity.getBillCode());
            if (!flag) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_CONTRACTCODE_NOT_UNIQUE);
            }
            entity.setStatus(CmsConstant.REPAIRAPPRAVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_71);// 展期补录待审批
            send.setBizStatus(CmsConstant.BUSINESSSTATE_71);
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_REPAIR_SUBMIT, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_REPAIR_SUBMIT, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED_REPAIR, entity.getId(),
                    user.getId());
        } else if (pageStatus == CmsConstant.BACK) {// 打回
            entity.setStatus(CmsConstant.BACK);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_66);// 展期打回
            send.setBizStatus(CmsConstant.BUSINESSSTATE_66);
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_BACK, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_DEF_REPAIR_BACK, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED_REPAIR, entity.getId(),
                    user.getId());
        } else if (pageStatus == CmsConstant.FINISH) {// 业务结束
            entity.setStatus(CmsConstant.FINISH);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_65);// 展期完成
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_HANDLE, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_HANDLE, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED_CONFIRM, entity.getId(),
                    user.getId());
            updateSend(entity, send); // 更改发放表状态
            updateFcAccDeferredInfo(send);
            // 根据还款计划和计息方式生成利息明细数据
            cmsProvideInterestService.adjustProvideInterest(send, user);
        } else if (pageStatus == -10) {
            entity.setStatus(CmsConstant.BACK);// 展期处理打回
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_69);// 展期处理打回
            send.setBizStatus(CmsConstant.BUSINESSSTATE_69);
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_DEF_HANDLE_BACK, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED_CONFIRM, entity.getId(),
                    user.getId());
        } else if (pageStatus == CmsConstant.COMMITAPPROVE) {// 提交审批
            verify(entity);
            entity.setStatus(CmsConstant.COMMITAPPROVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);// 展期待审批
            send.setBizStatus(CmsConstant.BUSINESSSTATE_61);
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_MODIFY_SUBMIT, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_DEF_MODIFY_SUBMIT, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED, entity.getId(), user.getId());
            bondStatus = FcBondConstant.STATUE_WAIT_CHECK;
        } else if (pageStatus == CmsConstant.TEMP) {
            verify(entity);
            entity.setStatus(CmsConstant.TEMP);// 暂存
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);// 展期暂存
            send.setBizStatus(CmsConstant.BUSINESSSTATE_60);
            msService.insertProcessMsg(t.getLiCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_MODIFY_TEMP, user);
            msService.insertProcessMsg(t.getBillCode(), ProvideConstant.DEFERRED_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_DEF_MODIFY_TEMP, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_DEFERRED, entity.getId(), user.getId());
            bondStatus = FcBondConstant.STATUE_TEMP;
        }
        entity.setUpdateName(user.getUsername());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(new Date());
        BeanUtils.copyNotNullProperties(entity, t);
        provideLoanDeferredDao.saveOrUpdate(t);
        cmsProvideLoanSendService.update(send, user);// 更新发放信息
        // 审批
        if (entity.getStatus() == 21) {// 提交审批
            this.getEntityDao().flush();
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED_REPAIR, t.getId(), t, t.getCorpId(),
                    user.getId());
        }
        if (entity.getStatus() == 11) {
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_DEFERRED, t.getId(), t, t.getCorpId(),
                    user.getId());
        }
    }
    /**
     * <b>功能介绍：</b>多次展期列表明细<br>
     * <b>日期：</b>2017年9月27日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    @PostMapping("/deferredInfoItems")
    @ResponseBody
    public List<ProvideLoanDeferredVO> deferredInfoItems(String liCode) {
        List<ProvideLoanDeferredVO> listData = provideLoanDeferredDao.getProvideLoanSendListByBillCode(liCode);
        Optional.ofNullable(listData).ifPresent(data -> {
            Map<String, DictDTO> dictMap = dictService.listMap("PrecativeLoanInfo.ternUnit");
            data.forEach(item -> Optional.ofNullable(dictMap).flatMap(listDict -> Optional.ofNullable(item.getTernUnit()))
                    .flatMap(ternUnit -> Optional.ofNullable(dictMap.get(ternUnit)))
                    .ifPresent(dict -> item.setUnit(dict.getName())));
        });
        return listData;
    }

    /**
     * <b>功能介绍：</b>根据send_Code获取所有已展期处理完成的数据<br>
     * <b>日期：</b>2023年5月6日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public List<ProvideLoanDeferredVO> getFinishDeferredListByBillCode(String sendCode) {
        return provideLoanDeferredDao.getFinishDeferredListByBillCode(sendCode);
    }

    /**
     * <b>功能介绍：</b>生成展期单号<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     */
    public String getLoanDeferredBillCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PROVIDE_LOANDEFERRED_BILL_NO,
                ProvideConstant.DEFERRED_BILL_CODE_PREFIX);
    }
    /**
     * 查询最大的展期日期 根据send_Code
     */
    @Transactional(readOnly = true)
    public Date getMaxDeferredDate(String sendCode) {
        return provideLoanDeferredDao.getMaxDeferredDate(sendCode);
    }
    /**
     *
     * 功能介绍 根据发放表billcode获取展期表最后一次展期日期及展期利率。<br>
     * <b>日期：</b>2018年3月30日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public ProvideLoanDeferred getDeferredBySendBillCode(String sendBillCode) {
        return provideLoanDeferredDao.getDeferredBySendBillCode(sendBillCode);
    }
    @Override
    public BaseDao<ProvideLoanDeferred, String> getEntityDao() {
        // TODO Auto-generated method stub
        return provideLoanDeferredDao;
    }
    /**
     * 后台验证补录信息
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     **/
    public void verifyAdd(ProvideLoanDeferred pld) {
        /** 到期处理类型不能为空 **/
        /*
         * if (EmptyUtils.isEmpty(pld.getExpiryHandleType())) { throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_EXPIRYHANDLETYPEEMPTY); }
         */
        /** 展期到期日期不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredRepayDate())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDREPAYDATEEMPTY);
        }
        /** 展期期限不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredLimt())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDLIMTEMPTY);
        }
        /** 展期利率不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredRate())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDRATEEMPTY);
        }
        /** 展期合同号不能为空 **/
        if (EmptyUtils.isEmpty(pld.getContractCode())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_CONTRACTCODEEMPTY);
        }
        /** 日期对比 **/
        int i = pld.getDeferredRepayDate().compareTo(pld.getMatureDate());
        if (i < 0) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_NOT_UNIQUE);
        }
    }
    /**
     * 后台验证展期申请信息
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     **/
    public void verify(ProvideLoanDeferred pld) {
        /** 到期处理类型不能为空 **//*
                           * if (EmptyUtils.isEmpty(pld.getExpiryHandleType())) { throw new
                           * ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_EXPIRYHANDLETYPEEMPTY); }
                           */
        /** 展期到期日期不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredRepayDate())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDREPAYDATEEMPTY);
        }
        /** 展期期限不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredLimt())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDLIMTEMPTY);
        }
        /** 展期利率不能为空 **/
        if (EmptyUtils.isEmpty(pld.getDeferredRate())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_DEFERREDRATEEMPTY);
        }
        /** 日期对比 **/
        int i = pld.getDeferredRepayDate().compareTo(pld.getMatureDate());
        if (i < 0) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_DEFERRED_NOT_UNIQUE);
        }
    }
    public void updateFcAccDeferredInfo(ProvideLoanSend send) {
        // 修该银行借款帐号的预提标志
        ProvideLoanDeferred deferred = getDeferredBySendBillCode(send.getBillCode());
        if (EmptyUtils.isNotEmpty(send.getLoanAcc())) {
            FcAccDTO fcAccDto = innerAccountService.getByBlnctrAcc(send.getLoanAcc());
            fcAccDto.setDelayDeadlineDate(deferred.getDeferredRepayDate());
            fcAccDto.setDelayRate(deferred.getDeferredRate());
            if (!EmptyUtils.isEmpty(fcAccDto)) {
                innerAccountService.updateFcAcc(fcAccDto);
            }
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 借款展期暂存，审批打回，待审批数据
            int dataSize1 = provideLoanDeferredDao.queryBusBillByCorpId(corpId,
                    CmsConstant.TEMP + "," + CmsConstant.COMMITAPPROVE + "," + CmsConstant.BACK, null);
            if (dataSize1 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_DEFER_ERROR)));
            }
            // 借款展期补录暂存，审批打回，待审批数据
            int dataSize2 = provideLoanDeferredDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_70 + ","
                    + CmsConstant.BUSINESSSTATE_71 + "," + CmsConstant.BUSINESSSTATE_80);
            if (dataSize2 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN, multiLangServiceI
                                .getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_DEFERREPAIR_ERROR)));
            }
        }
        return corpVerifyErrorDTOS;
    }
}
