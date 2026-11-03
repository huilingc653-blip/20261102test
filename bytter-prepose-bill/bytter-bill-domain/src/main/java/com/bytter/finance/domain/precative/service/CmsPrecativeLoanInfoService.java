package com.bytter.finance.domain.precative.service;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanInfoDao;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Title: Service
 * @Description: 委托贷款信息表
 * @author onlineGenerator
 * @date 2017-07-20 14:00:39
 * @version 1.0
 *
 */
@WorkFlow({ CmsWorkFlowConstant.CMS_PRECATIVE_INFO, CmsWorkFlowConstant.CMS_PRECATIVE_INFO_LOAN,
        CmsWorkFlowConstant.CMS_PRECATIVE_REPAIR })
@Service("cmsPrecativeLoanInfoService")
@Transactional
public class CmsPrecativeLoanInfoService extends BaseService<CmsPrecativeLoanInfo, String>
        implements IWorkFlowListener, ICorpDeleteVerifyService, ICorpDisableVerifyService, DictInjectProvider {
    @Autowired
    private CmsPrecativeLoanInfoDao cmsPrecativeLoanInfoDao;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtBankService btBankService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;

    @Autowired
    private RepayTypeService repayTypeService;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(CmsPrecativeLoanInfo entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsPrecativeLoanInfo entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsPrecativeLoanInfo entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(CmsPrecativeLoanInfo entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<CmsPrecativeLoanInfo, String> getEntityDao() {
        return cmsPrecativeLoanInfoDao;
    }
    /**
     * 录入修改数据删除
     */
    public void deleteCmsEntity(String id, String userId) {
        CmsPrecativeLoanInfo entity = this.get(id);
        entity.setStatus(CmsConstant.DELETED);
        // 资金计划删除
        this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());// 录入或修改
        if (entity.getBizStatus().equals(CmsConstant.BUSINESSSTATE_12)) {// 如果是补录打回，还要删除补录相关的资金计划
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());// 补录提交
        }
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_ADD, entity.getId(), userId);
        this.update(entity);
    }
    /**
     * 录入和修改提交 pre.getSaveType() 1 提交
     */
    public void submit(CmsPrecativeLoanInfoVo pre, String type, ICurrentUser user) {
        // cmsBalnceGatherService.queryGatherList(new CmsBalanceGatherVO());
        check(pre, user);
        CmsPrecativeLoanInfo tt = new CmsPrecativeLoanInfo();
        BeanUtils.copyNotNullProperties(pre, tt);
        tt.setPurpose(pre.getPurpose());
        if ("1".equals(pre.getSaveType())) {// 提交
            tt.setStatus(CmsConstant.COMMITAPPROVE);
            pre.setStatus(CmsConstant.COMMITAPPROVE); // 资金计划状态，11-待审批
            tt.setBizStatus(CmsConstant.BUSINESSSTATE_15);
        } else {// 暂存
            tt.setStatus(CmsConstant.TEMP);
            pre.setStatus(CmsConstant.TEMP); // 资金计划状态，0-暂存
            tt.setBizStatus(CmsConstant.BUSINESSSTATE_10);
        }
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_ADD, tt.getId(), user.getId());
        if (StringUtils.isNotBlank(pre.getId())) {
            if (this.isBudget(pre.getLoanOpp(), BaseDataConstant.BRIEF_STATUS_3000)) {
                // 录入提交和修改提交占用资金计划
                this.batchEngross(pre, pre.getItemCode(), pre.getItemName(), pre.getProcessMoney(), user,
                        BudgetConstant.BUSINESS_NAME_PRECATIVE);
            }
            tt.setUpdateBy(user.getId());
            tt.setUpdateDate(DateUtils.getDate());
            update(tt);
            if ("1".equals(pre.getSaveType())) {// 修改提交
                msService.insertProcessMsg(tt.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_REPAIR_MODIFY,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "", user);
            } else {// 修改暂存
                msService.insertProcessMsg(tt.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_MODIFY,
                        CmsProcessMsgConstant.MSGA_MODIFY + "", user);
            }
        } else {// 录入
                // String billCode=getPrecativeBillCode(PrecativeConstant.INFO_BILL_CODE_PREFIX);
                // tt.setBillCode(billCode);
                // pre.setBillCode(billCode);
            if (this.isBudget(pre.getLoanOpp(), BaseDataConstant.BRIEF_STATUS_3000)) {
                // 录入提交和修改提交占用资金计划
                this.batchEngross(pre, pre.getItemCode(), pre.getItemName(), pre.getProcessMoney(), user,
                        BudgetConstant.BUSINESS_NAME_PRECATIVE);
            }
            tt.setCreateBy(user.getId());
            tt.setCreateDate(DateUtils.getDate());
            saveEntity(tt);
            if ("1".equals(pre.getSaveType())) {// 录入提交
                msService.insertProcessMsg(tt.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_SUBMIT,
                        CmsProcessMsgConstant.MSGA_INPUT_SUBMIT, user);
            } else {// 录入暂存
                notificationService.send(tt.getId(), tt.getPrecativeId(), NotificationConstant.CMS.PRECATIVE_ADD, tt);// 发送消息提醒
                msService.insertProcessMsg(tt.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_INPUT,
                        CmsProcessMsgConstant.MSGA_INPUT + "", user);
            }
        }
        if (CmsConstant.IS_BANK_1.equals(pre.getIsBank())) {
            // 保存银行信息到btBank表
            btBankService.saveBtBank(pre.getBankCode(), pre.getBankTypeId());
        }
        if ("1".equals(pre.getSaveType())) {
            //TODO 暂不支持工作流
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_INFO, tt.getId(), tt, tt.getPrecativeId(),
                    user.getId());
        }
    }
    /***
     * 通过billCode查询Info信息
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanInfoVo getLoanInfoByBillCode(String billCode) {
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoDao.getLoanInfoByBillCode(billCode);
        //TODO 补充利率相关信息
        Optional.ofNullable(preVo).ifPresent(precativeDTO -> {
            if (StringUtils.isNotEmpty(precativeDTO.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(precativeDTO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> preVo.setBaseTypeName(rateDTO.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(precativeDTO.getRepayCycle())){
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(precativeDTO.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> preVo.setPeriodName(periodDTO.getPeriodName()));
            }
            if (StringUtils.isNotEmpty(precativeDTO.getLoanSendOutType())) {
                preVo.setSendOutType(dictService.get("cms.loan.loanSendOutType", precativeDTO.getLoanSendOutType()));
            }
            // 期限单位
            if (StringUtils.isNotEmpty(precativeDTO.getTernUnit())) {
                preVo.setTernUnitStatus(dictService.get("PrecativeLoanInfo.ternUnit", precativeDTO.getTernUnit()));
            }
        });
        return preVo;
    }
    @Transactional(readOnly = true)
    public CmsPrecativeLoanInfoVo getLoanInfoById(String id) {
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoDao.getLoanInfoById(id);
        //TODO 补充利率相关信息
        Optional.ofNullable(preVo).ifPresent(precativeDTO -> {
            if (StringUtils.isNotEmpty(precativeDTO.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(precativeDTO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> preVo.setBaseTypeName(rateDTO.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(precativeDTO.getRepayCycle())){
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(precativeDTO.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> preVo.setPeriodName(periodDTO.getPeriodName()));
            }
        });
        return preVo;
    }
    /**
     * 查询修改和打回数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryChangeLoanInfo(DataTablesInput input, String type, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_INFO_QUERY, userId);
        return cmsPrecativeLoanInfoDao.queryPrecativeLoanInfoDao(input, type, corpAuth);
    }
    /**
     * 加载审核列表数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String opType) {
        String flowType = CmsWorkFlowConstant.CMS_PRECATIVE_INFO;
        if ("repair".equals(opType)) {// 补录流程
            flowType = CmsWorkFlowConstant.CMS_PRECATIVE_REPAIR;
        } else if ("loan".equals(opType)) {
            flowType = CmsWorkFlowConstant.CMS_PRECATIVE_INFO_LOAN;
        }
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(flowType, dataTablesInput);
        DataTablesOutput dp = cmsPrecativeLoanInfoDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据传入的委托人查询出所有的借款人
     */
    @Transactional(readOnly = true)
    public List<String> getLoanOpps(List<String> ids) {
        List<CmsPrecativeLoanInfo> corpList = cmsPrecativeLoanInfoDao.getUnionCorps(ids);
        if (!EmptyUtils.isEmptyList(corpList)) {
            for (CmsPrecativeLoanInfo info : corpList) {
                String pre = info.getPrecativeId();
                String loan = info.getLoanOpp();
                if (!ids.contains(pre)) {
                    ids.add(pre);
                }
                if (!ids.contains(loan)) {
                    ids.add(loan);
                }
            }
        }
        return ids;
    }
    /**
     * 查询编辑页面数据
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanInfoVo queryInfo(String billCode) {
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoDao.getLoanInfoByBillCode(billCode);
        if(!EmptyUtils.isEmpty(preVo)){
            if(StringUtils.isNotBlank(preVo.getRepayType())){
                RepayType repayType = repayTypeService.get(preVo.getRepayType(),true);
                if(repayType != null){
                    preVo.setRepayTypeName(repayType.getRepayName());
                }
            }
        }
        // repayType = 02a35d581b4f410fa87ab9101fd5a2c4 CMS_REPAY_TYPE
        //TODO 补充利率相关信息
        Optional.ofNullable(preVo).ifPresent(precativeDTO -> {
            if (StringUtils.isNotEmpty(precativeDTO.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(precativeDTO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> preVo.setBaseTypeName(rateDTO.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(precativeDTO.getRepayCycle())){
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(precativeDTO.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> preVo.setPeriodName(periodDTO.getPeriodName()));
            }
            //TODO repayType 返回的数据 有问题。数据可能是 字典的，也可能是 还款方式自定义的
        });
        return preVo;
    }


    public CmsPrecativeLoanInfoVo getLoanInfoRepayBillCode(String RBillCode) {
        return cmsPrecativeLoanInfoDao.getLoanInfoRepayBillCode(RBillCode);
    }
    /**
     * 补录提交
     */
    public void repairSubmit(CmsPrecativeLoanInfoVo pre, ICurrentUser user) {
        checkRepair(pre, user);
        CmsPrecativeLoanInfo temp = get(pre.getId(), true);
        BeanUtils.copyNotNullProperties(pre, temp);
        temp.setStatus(CmsConstant.REPAIRAPPRAVE);
        temp.setBizStatus(CmsConstant.BUSINESSSTATE_25);
        pre.setStatus(CmsConstant.COMMITAPPROVE); // 资金计划状态，11-待审批
        pre.setCurId(temp.getCurId());
        // 分次发放时不会跳发放功能
        if (CmsConstant.LOAN_SEND_OUT_TYPE_TWO.equals(pre.getLoanSendOutType())) {
            temp.setSkipLoanSendOut(CmsConstant.IS_FALSE);
        }
        if (this.isBudget(pre.getLoanOpp(), BaseDataConstant.BRIEF_STATUS_3000)) {
            // 录入提交和修改提交占用资金计划
            this.otherbatchEngross(pre, pre.getItemCode(), pre.getItemName(), pre.getProcessMoney(), user,
                    BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION);
        }
        // notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAIR, temp.getId());
        // 委托贷款对接合同管理
        if (CmsConstant.RELATED_CONTRACT_NO.equals(relateCon())) { // 为对接合同管理，置空相关信息
            temp.setConId("");
            temp.setConTypeId("");
        }
        update(temp);
        if (CmsConstant.IS_BANK_1.equals(pre.getIsBank())) {
            // 保存银行信息到btBank表
            btBankService.saveBtBank(pre.getBankCode(), pre.getBankTypeId());
        }
        if (!EmptyUtils.isEmpty(temp.getPlanId())) {// 还款计划
            repaymentScheduleDao.updateRepayMentBill(temp.getPlanId(), PrecativeConstant.INFO_BILL_CODE_PREFIX,
                    temp.getId());// 更新还款计划的编号和类型
        }
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAIR, temp.getId(), user.getId());
         getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_REPAIR, temp.getId(), temp, temp.getPrecativeId(),
                user.getId());
        msService.insertProcessMsg(temp.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_REPAIR,
                CmsProcessMsgConstant.MSGA_REPAIROP, user);
    }
    /**
     * 补录打回
     */
    public void repairBack(String id, String refuseWhy, ICurrentUser user) {
        CmsPrecativeLoanInfo temp = get(id);
        temp.setRefuseWhy(refuseWhy);
        temp.setStatus(CmsConstant.BACK);
        temp.setBizStatus(CmsConstant.BUSINESSSTATE_12);
        // 删除资金计划
        /*
         * if(this.isBudget(temp.getLoanOpp(),BaseDataConstant.BRIEF_STATUS_3000)){
         * this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE
         * ,BudgetConstant.PROCESS_SYSNAME_CMS, temp.getBillCode()); }
         */
        update(temp);
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                BudgetConstant.PROCESS_SYSNAME_CMS, temp.getBillCode(), CmsConstant.BACK, user);
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAIR, temp.getId(), user.getId());
        notificationService.send(id, temp.getPrecativeId(), NotificationConstant.CMS.PRECATIVE_ADD, temp);
        msService.insertProcessMsg(temp.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_REPAIRBACK,
                CmsProcessMsgConstant.MSGA_REPAIR_BACK, user);
    }
    /**
     * 获取凭证号
     */
    public String getPrecativeBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PRECATIVE_ORDER_NO, prefix);
    }
    /**
     * 获取利率
     */
    public LoanRateMaintenceVO getRate(String plDate, String referenceRateType, String curId, String repayCycle) {
        List<LoanRateMaintenceVO> list = loanRateMaintenceService.getRateMaintence(plDate, referenceRateType, curId,
                repayCycle);
        if (EmptyUtils.isEmptyList(list)) {
            // 没有满足条件的贷款利率
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_NO_RATE);
        } else {
            return list.get(0);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批完成回调
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsPrecativeLoanInfo entity = this.get(workFlowResult.getBusinessKey(), true);
         ICurrentUser user = workFlowResult.getUser();
        Integer status = 0;
        String bizStatus = "";
        if (CmsWorkFlowConstant.CMS_PRECATIVE_INFO.equals(workFlowResult.getProcessDefinitionKey())) {// 新增
            if (workFlowResult.isPass()) {
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_INFO_LOAN, entity.getId(), entity,
                        entity.getLoanOpp(), workFlowResult.getUser().getId());
            } else {
                msService.insertProcessMsg(entity.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_ROLLBACK,
                        CmsProcessMsgConstant.MSGA_INPUT_CHECKBACK, workFlowResult.getUser());
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.BACK,
                        workFlowResult.getUser());
                // notificationService.send(workFlowResult.getBusinessKey(), entity.getPrecativeId(),
                // NotificationConstant.CMS.PRECATIVE_ADD, entity);
                entity.setStatus(CmsConstant.BACK);// 更新状态
                entity.setBizStatus(CmsConstant.BUSINESSSTATE_17);// 更新业务状态
                this.update(entity);
            }
        } else if (CmsWorkFlowConstant.CMS_PRECATIVE_INFO_LOAN.equals(workFlowResult.getProcessDefinitionKey())) {// 新增
            status = workFlowResult.isPass() ? CmsConstant.APPROVED : CmsConstant.BACK;// 判断是否审批通过
            bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_21 : CmsConstant.BUSINESSSTATE_17;// 判断是否审批通过
            if (workFlowResult.isPass()) {
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.APPROVED,
                        workFlowResult.getUser());
                msService.insertProcessMsg(entity.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_CHECKOK,
                        CmsProcessMsgConstant.MSGA_INPUT_CHECKOK, workFlowResult.getUser());
                notificationService.send(workFlowResult.getBusinessKey(), entity.getLoanOpp(),
                        NotificationConstant.CMS.PRECATIVE_REPAIR, entity);
            } else {
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.BACK,
                        workFlowResult.getUser());
                notificationService.send(workFlowResult.getBusinessKey(), entity.getPrecativeId(),
                        NotificationConstant.CMS.PRECATIVE_ADD, entity);
                msService.insertProcessMsg(entity.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_ROLLBACK,
                        CmsProcessMsgConstant.MSGA_INPUT_CHECKBACK, workFlowResult.getUser());
            }
            entity.setStatus(status);// 更新状态
            entity.setBizStatus(bizStatus);// 更新业务状态
            this.update(entity);
        } else {// 补录
            status = workFlowResult.isPass() ? CmsConstant.PREPARESEND : CmsConstant.APPROVED;// 判断是否审批通过
            bizStatus = workFlowResult.isPass() ? CmsConstant.BUSINESSSTATE_31 : CmsConstant.BUSINESSSTATE_26;// 判断是否审批通过
            if (workFlowResult.isPass()) {
                // 如果对接合同管理，则生成关联的合同信息
                this.toRelateCon(entity);
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.APPROVED,
                        workFlowResult.getUser());
                msService.insertProcessMsg(entity.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_CHECKOK,
                        CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK, workFlowResult.getUser());
                // notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAIR, businessKey);
            } else {
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.BACK,
                        workFlowResult.getUser());
                msService.insertProcessMsg(entity.getBillCode(), "C1", CmsProcessMsgConstant.MSGA_ROLLBACK,
                        CmsProcessMsgConstant.MSGA_REPAIR_CHECKBACK, workFlowResult.getUser());
                // notificationService.send(workFlowResult.getBusinessKey(), entity.getLoanOpp(),
                // NotificationConstant.CMS.PRECATIVE_REPAIR, entity);
            }
            if (status == CmsConstant.PREPARESEND) {
                // 判断是否跳过发放
                boolean flag = cmsPrecativeLoanSendService.loanInfoSend(entity, workFlowResult.getUser());
                if (!flag) {
                    // 发送消息提醒，委托贷款发放
                    notificationService.send(workFlowResult.getBusinessKey(), entity.getPrecativeId(),
                            NotificationConstant.CMS.PRECATIVE_SEND, entity);
                }
            }
            entity.setStatus(status);// 更新状态
            entity.setBizStatus(bizStatus);// 更新业务状态
            this.update(entity);
        }
        if (this.isBudget(entity.getLoanOpp(), BaseDataConstant.BRIEF_STATUS_3000)) {
            approvalBudget(entity, workFlowResult.isPass(), workFlowResult.getUser());
        }
        cmsPrecativeLoanInfoDao.flush();
    }
    /**
     * 录入修改暂存校验输入数据
     */
    public void check(CmsPrecativeLoanInfoVo pre, ICurrentUser user) {
        // TODO 判断用户是否可以对单位进行相关操作
        // 委托人不能为空
        if (StringUtils.isEmpty(pre.getPrecativeId())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PRECATIVEID_NOT_NULL);
        }
        // 受托人或受托行不能为空
        if (CmsConstant.IS_BANK_0.equals(pre.getIsBank())) {
            if (StringUtils.isBlank(pre.getCorpId())) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CORPID_NOT_NULL);
            }
            //TODO 受托人名称补充
            CorpDTO corpInfo = corpService.get(pre.getCorpId());
            Optional.ofNullable(corpInfo).ifPresent(crop -> pre.setCorpName(crop.getName()));
        } else if (CmsConstant.IS_BANK_1.equals(pre.getIsBank())) {
            if (StringUtils.isBlank(pre.getBankCode())) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_BANKCODE_NOT_NULL);
            }
            //TODO 补充开户行名称
            BtBankDTO btBankDTO = btBankService.getByBankCode(pre.getBankCode());
            Optional.ofNullable(btBankDTO).ifPresent(bank -> pre.setBankName(bank.getBankName()));
        }
        // 借款人不能为空
        if (StringUtils.isEmpty(pre.getLoanOpp())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_LOANOPP_NOT_NULL);
        }
        // 借款人和委托人不能为同一单位
        if (pre.getPrecativeId().equals(pre.getLoanOpp())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PRECATIVEIDLOAN_NOT_ONE);
        }
        CorpDTO precative = corpService.get(pre.getPrecativeId());
        CorpDTO loan = corpService.get(pre.getLoanOpp());
        //TODO 补充借款及委托人单位名称
        Optional.ofNullable(precative).ifPresent(precativeDTO -> pre.setPrecativeName(precativeDTO.getName()));
        Optional.ofNullable(loan).ifPresent(loanDTO -> pre.setLoanOppName(loanDTO.getName()));
        // 委托方和借款方应为同一网点下的成员单位
        if ((StringUtils.isEmpty(precative.getParentId()) || StringUtils.isEmpty(loan.getParentId()))
                || !precative.getParentId().equals(loan.getParentId())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_NOT_NET);
        }
        // 受托方为网点时，委托方和借款方为受托方单位
        if (CmsConstant.IS_BANK_0.equals(pre.getIsBank())) {
            String corpId = pre.getCorpId();
            if (!corpId.equals(precative.getNetId()) || !corpId.equals(loan.getNetId())) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_NOT_SAME_CORP);
            }
            // 参数为记账时委托方必须存在企业活期
            if (isAccounting()) {
                List<FcAccDTO> list = innerAccountService.listAccountByParamsAndGrant(pre.getCurId(), pre.getCorpId(),
                        pre.getPrecativeId(), null, SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get(),
                        user.getId());
                if (EmptyUtils.isEmptyList(list)) {
                    throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CURRENT_IS_NULL);
                }
            }
        } else {// 受托方为银行时，委托方和借款方为同一网点下的单位
            if (!precative.getNetId().equals(loan.getNetId())) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_NOT_SAME_CORP);
            }
        }
        // 金额不能为空
        if (EmptyUtils.isEmpty(pre.getMoney())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_MONEY_NOT_NULL);
        }
        // 币别不能为空
        if (StringUtils.isEmpty(pre.getCurId())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CURID_NOT_NULL);
        }
        //TODO 币种名称设置
        BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(pre.getCurId());
        Optional.ofNullable(currencyDTO).ifPresent(currency -> pre.setCurName(currency.getCurName()));
        // 开始日期不能为空
        if (EmptyUtils.isEmpty(pre.getStartDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_STARTDATE_NOT_NULL);
        }
        // 结束日期不能为空
        if (EmptyUtils.isEmpty(pre.getEndDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_ENDDATE_NOT_NULL);
        }
        // 期限不能为空
        if (EmptyUtils.isEmpty(pre.getTerm())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_TERM_NOT_NULL);
        }
        // 手续费率不能为空
        if (EmptyUtils.isEmpty(pre.getPoundageRate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_POUNDAGERATE_NOT_NULL);
        }
        // 担保方式不能为空
        if (" ".equals(pre.getPlType()) && StringUtils.isEmpty(pre.getPlType())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PLTYPE_NOT_NULL);
        }
        // 贷款用途不能为空
        if (StringUtils.isEmpty(pre.getPurpose())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PURPOSE_NOT_NULL);
        }
        // 担保方式不为信用时，担保说明不能为空 TODO 未挂接担保
        /*
         * if(CmsConstant.PL_TYPE_1.equals(pre.getPlType()) && pre.getSuretyExplain() == null){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SURETYEXPLAIN_NOT_NULL); }
         */
        /*
         * if(pre.getPurpose().length() > 50){
         *
         * }
         */
    }
    /**
     * 判断是否记账
     */
    public boolean isAccounting() {
        // 受托人不为银行时才记账
        if (CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_tally").getValue())) {
            return true;
        }
        return false;
    }
    /**
     * 补录数据校验
     */
    public void checkRepair(CmsPrecativeLoanInfoVo pre, ICurrentUser user) {
        /*
         * //受托人或受托行不能为空 if(CmsConstant.IS_BANK_0.equals(pre.getIsBank())){ if(EmptyUtils.isEmpty(pre.getCorpId()) ||
         * " ".equals(pre.getCorpId())){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CORPID_NOT_NULL); } }else
         * if(CmsConstant.IS_BANK_1.equals(pre.getIsBank())){ if(EmptyUtils.isEmpty(pre.getBankCode()) ||
         * " ".equals(pre.getBankCode())){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_BANKCODE_NOT_NULL); } } //借款人不能为空
         * if(EmptyUtils.isEmpty(pre.getLoanOpp())){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_LOANOPP_NOT_NULL); } //借款人和委托人不能为同一单位
         * if(pre.getPrecativeId().equals(pre.getLoanOpp())){ throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PRECATIVEIDLOAN_NOT_ONE); }
         */
        // 浮动利率时判断是否维护了基准利率，贷款周期，点数或者浮动比例选择
        if (!CmsConstant.FLOAT_WAY_01.equals(pre.getFloatWay())) {
            /*
             * if(EmptyUtils.isEmpty(pre.getReferenceRateType())){ throw new
             * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_REFERENCERATETYPE_ISNULL); }
             */
            if (EmptyUtils.isEmpty(pre.getRepayCycle())) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_REPAYCYCLE_ISNULL);
            }
            /*
             * if(EmptyUtils.isEmpty(pre.getPointsProportionSelect())){ throw new
             * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_POINTSPROPORTIONSELECT_ISNULL); }
             */
        }
        if (CmsConstant.IS_BANK_0.equals(pre.getIsBank())) {
            // 参数为记账时借款方必须存在企业活期
            if (isAccounting()) {
                List<FcAccDTO> list = innerAccountService.listAccountByParamsAndGrant(pre.getCurId(), pre.getCorpId(),
                        pre.getLoanOpp(), null, SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get(),
                        user.getId());
                if (EmptyUtils.isEmptyList(list)) {
                    throw new ParameterException(PrecativeMessageEnum.CMS_LOANOPP_CURRENT_IS_NULL);
                }
            }
        }
        // 贷款合同号不能为空
        if (EmptyUtils.isEmpty(pre.getContractCode())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CONTRACTCODE_NOT_NULL);
        }
        // 发放编号重复
        if (cmsPrecativeLoanInfoDao.isExistsContractCode(pre.getContractCode(), pre.getBillCode())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SEND_CONTRACTCODE_NOT_UNIQUE);
        }
        // 还款方式不能为空
        if (EmptyUtils.isEmpty(pre.getRepayType())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_REPAYTYPE_NOT_NULL);
        }
        // 计息方式不能为空
        if (EmptyUtils.isEmpty(pre.getInterestWay())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_INTERESTWAY_NOT_NULL);
        }
        // 发放方式不能为空
        if (EmptyUtils.isEmpty(pre.getLoanSendOutType())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_LOANSENDOUTTYPE_NOT_NULL);
        }
        // 浮动方式不为固定利率时利率调整生效日期不能为空
        if (!EmptyUtils.isEmpty(pre.getFloatWay()) && !CmsConstant.FLOAT_WAY_01.equals(pre.getFloatWay())
                && EmptyUtils.isEmpty(pre.getRateEntriesEffectDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_RATEENTRIESEFFECTDATE_NOT_NULL);
        }
        // 贷款利率不能为空
        if (EmptyUtils.isEmpty(pre.getRate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_RATE_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(pre.getPointsProportionText())) {
            pre.setPointsProportionText(BigDecimal.ZERO);
        }
        //判断是否跳过发放 判断还款计划
        if (StringUtils.isNotBlank(pre.getSkipLoanSendOut()) && pre.getSkipLoanSendOut().equals(CommonConstant.YES)){
            if (StringUtils.isBlank(pre.getPlanId())){
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PLANID_IS_NULL);
            }
        }

        if (pre.getTotal() != null) {
            int i = pre.getTotal().compareTo(pre.getMoney());
            if (i == -1 || i == 1) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DETAILMONEY_EQ_LOANMONEY);
            }
        }
    }
    /**
     * 是否挂机预算系统
     */
    public boolean isBudget(String corpId, String briefId) {
        if (StringUtils.isNotEmpty(corpId) && fbsBudgetProcessService.corpCtlBudget(corpId) && isBudget(briefId)) {
            return true;
        }
        return false;
    }
    /**
     * 是否挂接预算系统
     *
     * @param briefId
     *            业务流水 1000银行借款 2000内部贷款 3000委托贷款
     */
    public boolean isBudget(String briefId) {
        String type = "";
        if ("1000".equals(briefId)) {
            type = BaseDataConstant.IS_BORROW_BUDGET;
        } else if ("2000".equals(briefId)) {
            type = BaseDataConstant.IS_LOAN_BUDGET;
        } else if ("3000".equals(briefId)) {
            type = BaseDataConstant.IS_ENTRUST_BUDGET;
        }
        if (configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, type).toBoolean()) {
            if (StringUtils.isNotEmpty(getBudgetItem(briefId))) {
                return true;
            }
        }
        return isBudget(null, briefId);
    }
    /**
     * 根据业务获取预算科目
     *
     * @param briefId
     *            业务流水 001开票，6001库存登记，010背书
     */
    public String getBudgetItem(String briefId) {
        String name = "";
        if (BaseDataConstant.BRIEF_STATUS_1000.equals(briefId)) {
            // name=BaseDataConstant.AB_CONFIG_EMITITEM;
        } else if (BaseDataConstant.BRIEF_STATUS_2000.equals(briefId)) {
            // name=BaseDataConstant.AB_CONFIG_RECITEM;
        } else if (BaseDataConstant.BRIEF_STATUS_3000.equals(briefId)) {
            name = BaseDataConstant.WTLOAN_BUDGET_SEL;
        }
        String budgetItem = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, name).toString();
        return budgetItem;
    }
    /**
     * 补录占用预算
     */
    public void otherbatchEngross(CmsPrecativeLoanInfoVo entity, String[] itemCode, String[] itemName,
            BigDecimal[] processMoney, ICurrentUser user, String sign) {
        if (!EmptyUtils.isEmptyArray(itemCode)) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < itemCode.length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(entity.getStatus());// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setItemCode(itemCode[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(itemName[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(DateUtils.getDate());// 执行日期,即资金计划创建时间，也就是执行这个方法的当前时间
                fbsBudgetProcessDTO.setCorpId(entity.getLoanOpp());// 单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(sign);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                if (entity.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                }
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setInnerSign("1");
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(sign, BudgetConstant.PROCESS_SYSNAME_CMS,
                    entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 占用预算
     */
    public void batchEngross(CmsPrecativeLoanInfoVo entity, String[] itemCode, String[] itemName,
            BigDecimal[] processMoney, ICurrentUser user, String sign) {
        if (!EmptyUtils.isEmptyArray(itemCode)) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < itemCode.length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(entity.getStatus());// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setItemCode(itemCode[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(itemName[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(DateUtils.getDate());// 执行日期,即资金计划创建时间，也就是执行这个方法的当前时间
                fbsBudgetProcessDTO.setCorpId(entity.getPrecativeId());// 单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(sign);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setInnerSign("1");
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(sign, BudgetConstant.PROCESS_SYSNAME_CMS,
                    entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     *
     * 通过单位及类型查出借款用途集合
     */
    @Transactional(readOnly = true)
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        return btPurposeService.listByCorpIdOrBusType(corpId, BtPurposeConstant.SYS_BUS_TYPE_CMS);
    }
    /**
     * 占用预算 审批通过
     */
    public void approvalBudget(CmsPrecativeLoanInfo entity, boolean isPassOK, ICurrentUser user) {
        if (!isPassOK) {
            // 删除资金计划
            // this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE
            // ,BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());
        } else {
            if (EmptyUtils.isNotEmpty(entity.getContractCode())) {
                // 补录提交时修改资金计划为占用状态
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE,
                        BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), BudgetConstant.STATUE_CHECK, user);
            }
        }
    }
    /**
     * @Description 委托贷款对接合同管理
     * @Param [pre, user]
     * @return void
     * @Date 2019/11/16 17:23
     * @Author fusb
     */
    public void toRelateCon(CmsPrecativeLoanInfo entity) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(entity.getUpdateBy())) {
            user = userService.getByUserId(entity.getUpdateBy());
        } else {
            user = userService.getByUserId(entity.getCreateBy());
        }
        // 查询业务是否已经生成了合同信息
        ConInfoDTO searchDTO = conCommonService.getConInfo("", entity.getId());
        // 如果未对接合同管理
        if (EmptyUtils.isEmpty(searchDTO)) {
            if (StringUtils.isNotEmpty(entity.getConId()) && StringUtils.isEmpty(entity.getConTypeId())) { // 业务选择合同
                conCommonService.hookConOp(entity.getConId(), entity.getId(), user);
            }
            if (StringUtils.isNotEmpty(entity.getConTypeId())) { // 业务生成合同
                CmsPrecativeLoanInfoVo preVo = this.queryInfo(entity.getBillCode());
                BeanUtils.copyNotNullProperties(entity, preVo);
                ConInfoDTO conDTO = new ConInfoDTO();
                conDTO.setCorpId(preVo.getPrecativeId());
                conDTO.setBusId(preVo.getId());
                conDTO.setConName(preVo.getPrecativeName() + CmsConstant.PRECATIVE_LOAN_CON);
                conDTO.setConNo(entity.getContractCode());
                preVo.setContractCode(entity.getContractCode());
                conDTO.setTypeId(entity.getConTypeId());
                conDTO.setBeginDate(entity.getStartDate());
                conDTO.setEndDate(entity.getEndDate());
                conDTO.setOutBusData(preVo);
                String conId = conCommonService.registerConOpEntrustLoan(conDTO, user);
                entity.setConId(conId);
            }
        }
    }
    /**
     * @Description 查询委托贷款信贷参数是否对接合同管理
     * @Param []
     * @return java.lang.String
     * @Date 2019/11/16 16:47
     * @Author fusb
     */
    public String relateCon() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.PRECATIVE_RELATE_CON)
                .getValue();
    }
    /**
     * @Description 查询委托贷款信贷参数对接合同管理模式
     * @Param []
     * @return java.lang.String
     * @Date 2019/11/16 16:49
     * @Author fusb
     */
    public String conType() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.PRECATIVE_CON_TYPE)
                .getValue();
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsPrecativeLoanInfoDao.verifyCorpDeleteByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DEL_PRECATIVE_LOAN_INFO_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsPrecativeLoanInfoDao.verifyCorpDisableByCorpId(corpId) > 0) {// 暂存,待审批,审批打回
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATICE_INFO));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsPrecativeLoanInfoDao.verifyCorpDisableByCorpId2(corpId) > 0) {// 待审批 借款方
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATICE_LOANOPP_INFO));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsPrecativeLoanInfoDao.verifyCorpDisableByCorpId1(corpId) > 0) {// 补录、待发放
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATIVE_INFO1));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
