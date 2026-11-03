package com.bytter.finance.domain.provide.service;

import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.provide.dao.CmsProvideLoanAbolishDao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanAbolish;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.LoanAbolishVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Title: Service
 * @Description: 银行借款作废
 * @author onlineGenerator
 * @date 2017-08-15 15:39:47
 * @version 1.0
 *
 */
@Service("cmsProvideLoanAbolishService")
@Transactional
@WorkFlow(value = { CmsWorkFlowConstant.CMS_PROVIDE_LOAN_ABOLISH })
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_PROVIDE_LOAN_ABOLISH)
public class CmsProvideLoanAbolishService extends BaseService<CmsProvideLoanAbolish, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private CmsProvideLoanAbolishDao cmsProvideLoanAbolishDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Resource
    private IConService conCommonService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 功能介绍：借款作废列表<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_CANCEL_QUERY, user.getId());
        DataTablesOutput<LoanAbolishVO> dataTablesOutput = cmsProvideLoanAbolishDao.queryTable(input, user.getLoginName(), corpAuth);
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, DictDTO> dictMap = dictService.listMap("PrecativeLoanInfo.ternUnit");
            listData.forEach(item -> Optional.ofNullable(item.getTernUnit())
                    .flatMap(ternUnit -> Optional.ofNullable(dictMap.get(ternUnit)))
                    .ifPresent(dict -> item.setTernUnitName(dict.getName())));
        });
        return dataTablesOutput;
    }
    /**
     * 功能介绍：借款作废审批列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getListDataAbolishApproveTable(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_ABOLISH, dataTablesInput);
        DataTablesOutput<LoanAbolishVO> dp = cmsProvideLoanAbolishDao.getListDataAbolishApproveTable(dataTablesInput, workFlowBusiness.getBusinessIdList());
        Optional.ofNullable(dp.getData()).ifPresent(listData -> {
            Map<String, DictDTO> dictMap = dictService.listMap("PrecativeLoanInfo.ternUnit");
            listData.forEach(item -> Optional.ofNullable(item.getTernUnit())
                    .flatMap(ternUnit -> Optional.ofNullable(dictMap.get(ternUnit)))
                    .ifPresent(dict -> item.setTernUnitName(dict.getName())));
        });
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍：借款作废办理列表<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryStartTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_ABOLISH_HANDLE_QUERY,
                user.getId());
        return cmsProvideLoanAbolishDao.queryStartTable(input, corpAuth);
    }
    /**
     * 功能介绍：根据infobillCode查询abolish对象<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public CmsProvideLoanAbolish getLoanAbolishByBillCode(String billCode) {
        return cmsProvideLoanAbolishDao.getLoanAbolishByBillCode(billCode);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsProvideLoanAbolish entity, ICurrentUser user) {
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(entity.getPliCode());// 获取info信息
        if (entity.getStatus() == 11) {
            entity.setStatus(CmsConstant.COMMITAPPROVE);// 提交审批
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_81);
        }
        entity.setOp(user.getId());
        entity.setAbolishedDate(new Date());
        entity.setAbolishedStatus(info.getStatus());// 将inof当前状态保存，用于作废打回、删除、撤销之后还原状态
        if (EmptyUtils.isEmpty(entity.getId())) {
            super.saveEntity(entity);
        } else {
            super.saveOrUpdate(entity);
        }
        // 将info表状态修成-4作废办理中
        info.setStatus(CmsConstant.ABOLISHDISPOSE);
        loanInfoService.update(info, user);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_ABOLISH, entity.getId(), user.getId());
        if (entity.getStatus() == 11) {
            msService.insertProcessMsg(entity.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_ABOLISH_SUBMIT, user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_ABOLISH, entity.getId(), entity,
                    entity.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(entity.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_ABOLISH_TEMP, user);
        }
    }
    /** 审批处理 */
    public void appoved(String id, ICurrentUser user) {
        CmsProvideLoanAbolish abolish = get(id);
        abolish.setStatus(CmsConstant.APPROVED);
        abolish.setBizStatus(CmsConstant.BUSINESSSTATE_87);
        saveOrUpdate(abolish);
        msService.insertProcessMsg(abolish.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_ABOLISH_CHECKOK, user);
        notificationService.send(abolish.getId(), abolish.getCorpId(), NotificationConstant.CMS.PROVIDE_ABOLISH_HANDLE,
                abolish);
    }
    /** 作废办理 **/
    public void Handle(String id, ICurrentUser user) {
        CmsProvideLoanAbolish abolish = get(id);
        abolish.setStatus(CmsConstant.FINISH);
        abolish.setBizStatus(CmsConstant.BUSINESSSTATE_110);
        // cmsProvideLoanAbolishDao.updateInfoStatus(CmsConstant.ABOLISH, abolish.getPliCode());
        CmsProvideLoanInfo loan = loanInfoService.getLoanInfoByBillCode(abolish.getPliCode());
        // 授信挂接
        if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
            // ，释放授信
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, loan.getId());
            urationDTO.setMoney(loan.getPlMoney());// 所有金额
            if (CmsConstant.BUSINESSSTATE_31.equals(loan.getBizStatus())) {// 待发放则直接释放
                urationDTO.setAllRelease(true);// 全部释放
                urationDTO.setReleaseNonRecycleRat(true); // 释放不循环授信的占用额度
                urationService.releaseRation(urationDTO);
            } else {
                urationService.backUseRation(urationDTO);
            }
        }
        // 如果挂接了保证金,作废后释放
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(loan.getId(), loan.getBillCode());
        if (dto != null) {
            dto.setStatus(FcBondConstant.STATUE_RELEASE);// 释放占用的保证金额度
            dto.setUpdateBy(user.getLoginName());
            dto.setUpdateDate(DateUtils.getDate());
            fcBondHookService.updateFcBondApply(dto);
        }
        // 担保处理 删除使用轨迹
        if (CmsConstant.IS_TRUE.equals(loan.getIsHangGuarantee())) {
            guaContractOccupServie.deleteGuaContractOccup(loan.getId(), ProvideConstant.PROVIDE_GUA_SYSCODE);
            // ProvideUtils.handleGua(loan,null,guaContractOccupServie,3);
        }
        // 合同处理,终止合同
        this.endContract(loan, abolish, user);
        // 释放抵质押物
        if (EmptyUtils.isNotEmpty(loan.getIsRelatePledge()) && CmsConstant.IS_TRUE.equals(loan.getIsRelatePledge())) {
            pledgeOccupyService.releasePledgeOccupy(loan.getId(), loan.getBillCode());
        }
        // TODO 删除申请补录预算信息
        loan.setStatus(CmsConstant.ABOLISH);
        loan.setBizStatus(CmsConstant.BUSINESSSTATE_85);
        loanInfoService.saveOrUpdate(loan);
        List<ProvideLoanSend> sendList = cmsProvideLoanSendService.getLoanSendListByBillCode(abolish.getPliCode());
        cmsProvideLoanAbolishDao.updateSendStatus(CmsConstant.ABOLISH, abolish.getPliCode());
        if (!EmptyUtils.isEmptyList(sendList)) {
            for (ProvideLoanSend send : sendList) {
                // 删除预算信息
                this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.PROCESS_SYSNAME_CMS,
                        BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, send.getId());
                // send.setStatus(CmsConstant.ABOLISH);
                // send.setBizStatus(CmsConstant.BUSINESSSTATE_85);
                // cmsProvideLoanSendService.saveOrUpdate(send);
            }
        }
        saveOrUpdate(abolish);
        msService.insertProcessMsg(abolish.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_ABOLISH_HANDLE, user);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_ABOLISH_HANDLE, abolish.getId(), user.getId());
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsProvideLoanAbolish entity, String userId) {
        notificationService.complete(NotificationConstant.CMS.PROVIDE_ABOLISH_HANDLE, entity.getId(), userId);
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
     * 更新
     *
     * @param entity
     */
    public void update(CmsProvideLoanAbolish entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsProvideLoanAbolish entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<CmsProvideLoanAbolish, String> getEntityDao() {
        return cmsProvideLoanAbolishDao;
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
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_ABOLISH.equals(processDefinitionKey)) {// 还款申请审批
            if (result) {// 审批通过
                appoved(businessKey, user);
            } else {// 打回
                CmsProvideLoanAbolish abolish = this.get(businessKey,true);
                abolish.setStatus(CmsConstant.BACK);
                abolish.setBizStatus(CmsConstant.BUSINESSSTATE_88);
                this.saveOrUpdate(abolish);
                // 打回之后还原 info状态
                CmsProvideLoanInfo loan = loanInfoService.getLoanInfoByBillCode(abolish.getPliCode());
                if (abolish.getAbolishedStatus() != null) {
                    loan.setStatus(abolish.getAbolishedStatus());
                    loanInfoService.update(loan, user);
                }
                // this.deleteEntity(abolish);
                // TODO
                msService.insertProcessMsg(abolish.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ABOLISH_CHECKBACK, user);
                notificationService.send(abolish.getId(), abolish.getCorpId(), NotificationConstant.CMS.PROVIDE_ABOLISH,
                        abolish);
            }
        }
    }
    /**
     * 终止合同处理
     */
    public void endContract(CmsProvideLoanInfo loan, CmsProvideLoanAbolish abolish, ICurrentUser user) {
        if (StringUtils.isNotEmpty(loan.getConId())) {
            ConInfoDTO conInfoDTO = conCommonService.getConInfo(loan.getConId(), loan.getId());
            if (!conInfoDTO.getStatus().equals(ConConstant.CON_INFO_STATUS_90)) {// 如果合同状态为已终止,不生成终止记录
                ConInfoRecordDTO dto = new ConInfoRecordDTO();
                dto.setConId(loan.getConId());
                dto.setOuterId(loan.getId());// 终止业务挂接ID
                dto.setOpMsg(CmsConstant.PROVIDE_LOAN_ABOLISH);// 终止原因：合同作废
                dto.setOpDate(abolish.getAbolishedDate());
                dto.setOpNo(abolish.getPliCode());
                conCommonService.terminateConOp(dto, user);
            }
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 借款作废审批打回，待审批数据
            int dataSize = cmsProvideLoanAbolishDao.queryBusBillByCorpId(corpId,
                    CmsConstant.COMMITAPPROVE + "," + CmsConstant.BACK, null);
            if (dataSize > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_ABOLISH_ERROR)));
            }
        }
        return corpVerifyErrorDTOS;
    }
}
