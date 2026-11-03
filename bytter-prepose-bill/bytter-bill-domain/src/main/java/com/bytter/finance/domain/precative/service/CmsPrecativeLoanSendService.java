package com.bytter.finance.domain.precative.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.vo.CmsPrecativeLoanSendCheckVO;
import com.bytter.finance.domain.loan.service.CmsContractService;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanSendDao;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 委托贷款发放表
 * @author onlineGenerator
 * @date 2017-07-29 10:45:29
 * @version 1.0
 *
 */
@WorkFlow(CmsWorkFlowConstant.CMS_PRECATIVE_SEND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_PRECATIVE_SEND)
@Service("cmsPrecativeLoanSendService")
@Transactional
public class CmsPrecativeLoanSendService extends BaseService<CmsPrecativeLoanSend, String>
        implements IWorkFlowListener, ITaskService, ICorpDisableVerifyService {
    @Autowired
    private CmsPrecativeLoanSendDao cmsPrecativeLoanSendDao;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcBusRaceService fcBusRaceService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private CmsContractService cmsContractService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsPrecativeLoanSend entity) {
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
    public void save(CmsPrecativeLoanSend entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsPrecativeLoanSend entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsPrecativeLoanSend entity) {
        super.saveOrUpdate(entity);
    }
    @Transactional(readOnly = true)
    @SuppressWarnings({ "unchecked" })
    public DataTablesOutput<PrecativeLoanSendVo> getLoanSend(DataTablesInput dataTablesInput, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_SEND_QUERY, userId);
        DataTablesOutput<PrecativeLoanSendVo> result = cmsPrecativeLoanSendDao.getLoanSend(dataTablesInput, corpAuth);
        List<PrecativeLoanSendVo> loanInfos = result.getData();
        for (PrecativeLoanSendVo loanInfo : loanInfos) {// 设置放款次数
            loanInfo.setSendTime(cmsPrecativeLoanSendDao.getCmsLoanSendCountByloanCode(loanInfo.getInfoBillCode()));
        }
        result.setData(loanInfos);
        return result;
    }
    public DataTablesOutput getPrecativeDataTables(DataTablesInput dataTablesInput, ICurrentUser user ,List<String> corpAuth) {
//        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_SEND_QUERY, user.getId());
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        return cmsPrecativeLoanSendDao.getPrecativeDataTables(dataTablesInput, corpAuth, opType, user);
    }
    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        cmsPrecativeLoanSendDao.updateRepayType(id, repayType);
    }
    /***
     * 返回发放次数
     */
    @Transactional(readOnly = true)
    public int sendCount(String fatherCode) {
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendDao.getSendByFatherCode(fatherCode);
        return EmptyUtils.isEmptyList(list) ? 1 : list.size() + 1;
    }
    @Transactional(readOnly = true)
    public PrecativeLoanSendVo getLoanSend(String fatherCode) {
        List<PrecativeLoanSendVo> list = cmsPrecativeLoanSendDao.getPrecativeLoanSendVoByBillCode(fatherCode);
        return EmptyUtils.isEmptyList(list) ? new PrecativeLoanSendVo() : list.get(0);
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PRECATIVE_SEND,
                dataTablesInput);
        DataTablesOutput dp = cmsPrecativeLoanSendDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据billCode查询sendVo
     */
    @Transactional(readOnly = true)
    public PrecativeLoanSendVo getSendByBillCode(String billCode) {
        if (EmptyUtils.isEmpty(billCode)) {
            return new PrecativeLoanSendVo();
        }
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendDao.getSendByBillCode(billCode, null);
        //TODO 补充利息相关信息
        Optional.ofNullable(sendVo).ifPresent(sendInfo -> {
            if (StringUtils.isNotEmpty(sendInfo.getReferenceRateType())) {
                TbBaseRateDTO baseRateInfo = iBaseRateService.getBaseRateById(sendInfo.getReferenceRateType());
                Optional.ofNullable(baseRateInfo).ifPresent(baseRate -> sendVo.setBaseTypeName(baseRate.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(sendInfo.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodInfo = iCreditPeriodService.getById(sendInfo.getRepayCycle());
                Optional.ofNullable(creditPeriodInfo).ifPresent(creditPeriod -> sendVo.setPeriodName(creditPeriod.getPeriodName()));
            }
            // 点数或浮动比例
            if (StringUtils.isNotEmpty(sendInfo.getPointsProportionSelect())) {
                sendVo.setPointsProportionName(dictService.get("Cms.PointsProportion", sendInfo.getPointsProportionSelect()));
            }
        });
        return sendVo;
    }
    @Transactional(readOnly = true)
    public CmsPrecativeLoanSend getByBillCode(String billCode) {
        return cmsPrecativeLoanSendDao.getByBillCode(billCode);
    }
    /**
     * 查询委托贷款发放到期<br>
     */
    @Transactional(readOnly = true)
    public List<CmsPrecativeLoanSendCheckVO> getPrecativeLoanInfoByEndDate(String precativeId, Date endDate) {
        return cmsPrecativeLoanSendDao.getPrecativeLoanInfoByEndDate(precativeId, endDate);
    }
    /**
     * 根据id查询sendVo
     */
    @Transactional(readOnly = true)
    public PrecativeLoanSendVo getSendById(String id) {
        if (EmptyUtils.isEmpty(id)) {
            return new PrecativeLoanSendVo();
        }
        return cmsPrecativeLoanSendDao.getSendByBillCode(id, "id");
    }
    /**
     * 根据send表billCode查询infoVo
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanInfoVo getInfoBySendCode(String billCode) {
        return cmsPrecativeLoanSendDao.getInfoBySendCode(billCode);
    }
    /**
     * 判断是否本位币 不含单位版本
     * 委托人可以是银行
     *
     */
    @Transactional(readOnly = true)
    public boolean isNatualCurrency(String curId, String corpId) {
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        if (StringUtils.isNotBlank(corpId)){
            natualCurrency = btCurrencyService.getObjectById(corpService.get(corpId).getCurId());
        }
        return curId.equals(natualCurrency.getId());
    }

    /**
     * 启用日期必须大于当前网点已日结日期
     *
     * @param date1
     * @param date2
     */
    protected void dateCompare(Date date1, Date date2, String corpId) {
        if (DateUtils.dayDiffs(date1, date2) <= 0) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDDATE_PASS_CLOSEDATE, corpId,
                    DateUtils.formatDate(date2));
        }
    }
    /***
     * 发放提交或暂存 1 提交 其他为暂存 flag 是否跳过发放
     */
    public void submitOrTemp(PrecativeLoanSendVo pre, String type, ICurrentUser user, boolean flag) {
        String sendId = (!EmptyUtils.isEmpty(pre.getId()) || !"".equals(pre.getId())) ? pre.getId() : null;
        CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getInfoBillCode());
        if (isTally()) {
            if (!EmptyUtils.isEmpty(loanInfo.getCorpId())) {// 非银行办理
                Date tallyDate = fcDayCloseService.getDayCloseFinalDate(loanInfo.getCorpId());// 获取网点日结日期
                if ("1".equals(loanInfo.getSkipLoanSendOut())) {
                    if (DateUtils.dayDiffs(pre.getSendDate(), tallyDate) < 0) {// 当跳过发放时，设的发放日期为开始日期，所以这里相当于是拿开始日期与网点日结日期比较
                        pre.setSendDate(DateUtils.addDay(tallyDate, 1));
                    }
                }
                // notificationService.complete(NotificationConstant.CMS.PRECATIVE_SEND, loanInfo.getId(),user.getId());
                // if(CmsConstant.IS_BANK_0.equals(loanInfo.getIsBank())){
                // 发放日期必须大于当前网点已日结日期
                dateCompare(pre.getSendDate(), tallyDate, loanInfo.getCorpName());
                // }
            }
        }
        pre.setFatherCode(loanInfo.getBillCode());
        check(pre, type);
        if (isMoreLoanMoney(pre, loanInfo.getMoney())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_MONEY_ISMORE);
        }
        if (sendId == null) {
            pre.setBillCode(getPrecativeBillCode(PrecativeConstant.SEND_BILL_CODE_PREFIX));
            checkSave(pre);
        }
        CmsPrecativeLoanSend entity = new CmsPrecativeLoanSend();
        BeanUtils.copyNotNullProperties(loanInfo, entity);
        BeanUtils.copyNotNullProperties(pre, entity);
        entity.setId(sendId);
        if (StringUtils.isEmpty(entity.getId())) { // 新增
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
            entity.setCreateName(user.getUsername());
        } else { // 暂存
            entity.setUpdateBy(user.getId());
            entity.setUpdateDate(DateUtils.getDate());
            entity.setUpdateName(user.getUsername());
        }
        if ("1".equals(type)) {
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_35);
            entity.setStatus(CmsConstant.COMMITAPPROVE);
        } else {
            entity.setStatus(CmsConstant.TEMP);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_30);
        }
        if ("1".equals(type)) {
            notificationService.complete(NotificationConstant.CMS.PRECATIVE_SEND, loanInfo.getId(), user.getId());
            if (!flag) {
                saveOrUpdate(entity);
                // 正常发放
                if (!EmptyUtils.isEmpty(entity.getPlanId())) {// 还款计划
                    repaymentScheduleDao.updateRepayMentBill(entity.getPlanId(),
                            PrecativeConstant.SEND_BILL_CODE_PREFIX, entity.getId());// 更新还款计划的编号和类型
                }
                if (sendId == null) {// 提交
                    msService.insertProcessMsg(entity.getBillCode(), "C2", CmsProcessMsgConstant.MSGA_SUBMIT,
                            CmsProcessMsgConstant.MSGA_SEND_SUBMIT, user);
                } else {// 修改提交
                    msService.insertProcessMsg(entity.getBillCode(), "C2", CmsProcessMsgConstant.MSGA_REPAIR_MODIFY,
                            CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "", user);
                }
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_SEND, entity.getId(), entity,
                        entity.getPrecativeId(), user.getId());
            } else {
                // 补录跳过发放
                entity.setStatus(CmsConstant.COMPELETOTAL);
                entity.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                // 修改info表状态为发放中
                if (CmsConstant.BUSINESSSTATE_31.equals(loanInfo.getBizStatus())) {
                    CmsPrecativeLoanInfo temp = new CmsPrecativeLoanInfo();
                    BeanUtils.copyNotNullProperties(loanInfo, temp);
                    temp.setBizStatus(CmsConstant.BUSINESSSTATE_32);
                    cmsPrecativeLoanInfoService.update(temp);
                }
                if (isAccounting(loanInfo)) {
                    if (isAutoAccounting()) {
                        CorpDTO loan = corpService.get(loanInfo.getLoanOpp());
                        CorpDTO precative = corpService.get(loanInfo.getPrecativeId());
                        String netId = loanInfo.getCorpId();
                        // if(CorpConstant.CORP_TYPE_CODE_CORP.equals(loan.getType()) &&
                        // CorpConstant.CORP_TYPE_CODE_CORP.equals(precative.getType())){
                        // if(netId.equals(loan.getNetId())&& netId.equals(precative.getNetId())){
                        if (precative.getParentId().equals(loan.getParentId())) {
                            this.submit(entity, loanInfo, user);
                        }
                        // }
                        // }
                        entity.setCreateBy(pre.getCreateBy());
                        entity.setCreateName(pre.getCreateName());
                        entity.setUpdateBy(pre.getUpdateBy());
                        entity.setUpdateName(pre.getUpdateName());
                        // 如果对接合同管理，且履约环节为放款，补录跳过发放
                        loanInfo.setConId(pre.getConId());
                        this.createSegmentInfo(loanInfo, entity);
                    } else {
                        entity.setStatus(CmsConstant.WAITTOTAL);
                        entity.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                    }
                }
                saveOrUpdate(entity);
                // notificationService.send(entity.getId(), loanInfo.getLoanOpp(),
                // NotificationConstant.CMS.PRECATIVE_REPAY, entity);
            }
        } else {
            saveOrUpdate(entity);
            // notificationService.send(entity.getId(), entity.getPrecativeId(),
            // NotificationConstant.CMS.PRECATIVE_SEND, entity);
            if (!EmptyUtils.isEmpty(entity.getPlanId())) {// 还款计划
                repaymentScheduleDao.updateRepayMentBill(entity.getPlanId(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        entity.getBillCode());// 更新还款计划的编号和类型
            }
            if (sendId == null) {// 暂存
                msService.insertProcessMsg(entity.getBillCode(), "C2", CmsProcessMsgConstant.MSGA_INPUT,
                        CmsProcessMsgConstant.MSGA_SEND_TEMP, user);
            } else {// 修改暂存
                msService.insertProcessMsg(entity.getBillCode(), "C2", CmsProcessMsgConstant.MSGA_MODIFY,
                        CmsProcessMsgConstant.MSGA_SEND_UPDATE_TEMP, user);
            }
        }
    }
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批回调方法
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? CmsConstant.CMS_APPROVE_SUCC : CmsConstant.CMS_APPROVE_BACK;
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    /**
     * 调用审批
     */
    public void approveOver(String businessKey, boolean isPassOK, String appMsg, ICurrentUser user) {
        CmsPrecativeLoanSend entity = get(businessKey);
        CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(entity.getFatherCode());
        if (isPassOK) {
            // 审批通过
            entity.setStatus(CmsConstant.COMPELETOTAL);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            // 修改info表状态为发放中
            if (CmsConstant.BUSINESSSTATE_31.equals(loanInfo.getBizStatus())) {
                CmsPrecativeLoanInfo temp = new CmsPrecativeLoanInfo();
                BeanUtils.copyNotNullProperties(loanInfo, temp);
                temp.setBizStatus(CmsConstant.BUSINESSSTATE_32);
                cmsPrecativeLoanInfoService.update(temp);
            }
            if (isAccounting(loanInfo)) {
                if (isAutoAccounting()) {
                    CorpDTO loan = corpService.get(loanInfo.getLoanOpp());
                    CorpDTO precative = corpService.get(loanInfo.getPrecativeId());
                    String netId = loanInfo.getCorpId();
                    if (CorpConstant.CORP_TYPE_CODE_CORP.equals(loan.getType())
                            && CorpConstant.CORP_TYPE_CODE_CORP.equals(precative.getType())) {
                        if (netId.equals(loan.getNetId()) && netId.equals(precative.getNetId())) {
                            this.submit(entity, loanInfo, user);
                        }
                    }
                    // 如果记账且自动记账，委托贷款对接了合同管理且履约环节为发放，生成履约信息
                    this.createSegmentInfo(loanInfo, entity);
                } else {
                    // notificationService.send(businessKey, entity.getPrecativeId(),
                    // NotificationConstant.FINACIAL.BISEXC_EDIT, entity);
                    entity.setStatus(CmsConstant.WAITTOTAL);
                    entity.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                }
            } else { // 如果不记账，委托贷款对接了合同管理且履约环节为发放，生成履约信息
                this.createSegmentInfo(loanInfo, entity);
            }
            // notificationService.send(businessKey, loanInfo.getLoanOpp(), NotificationConstant.CMS.PRECATIVE_REPAY,
            // entity);归还之后不发消息
            // notificationService.send(businessKey, entity.getPrecativeId(), NotificationConstant.CMS.PRECATIVE_REPAY,
            // entity);
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_SEND_CHECKOK, user);
        } else {
            // 审批打回
            entity.setStatus(CmsConstant.BACK);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_37);
            // notificationService.send(businessKey,
            // entity.getPrecativeId(),NotificationConstant.CMS.PRECATIVE_SEND,entity);
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_SEND_CHECKBACK, user);
        }
        this.update(entity);
    }
    /**
     * 判断是否记账
     */
    public boolean isAccounting(CmsPrecativeLoanInfoVo loanInfo) {
        // 受托人不为银行时才记账
        if (CmsConstant.IS_BANK_0.equals(loanInfo.getIsBank())) {
            return CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_tally").getValue());
        }
        return false;
    }
    /**
     * 判断是否自动记账
     */
    public boolean isAutoAccounting() {
        // 自动记账
        return CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_automatic_tally").getValue());
    }
    /**
     * 判断是否记账
     */
    public boolean isTally() {
        // 是否记账
        return CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_tally").getValue());
    }
    /**
     * 调用记账
     */
    public void submit(CmsPrecativeLoanSend entity, CmsPrecativeLoanInfoVo loanInfo, ICurrentUser user) {
        // 构建凭证传输对象
        FcAccbookVoucherDTO accbook = processAccbook(loanInfo, entity, user);
        // 记账提交暂存
        VoucherWrapperDTO voucher = submit(accbook, FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.get(), user);
        // 设置记账返回数据
        FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
        // List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
        // 记账流水号
        entity.setOrderNo(acc.getOrderNo());
        // 记账凭证号
        entity.setVoucherNo(acc.getVoucherNo());
    }
    /***
     * 判断补录是否跳过发放
     */
    public boolean loanInfoSend(CmsPrecativeLoanInfo loanInfo, ICurrentUser user) {
        String skip = loanInfo.getSkipLoanSendOut();
        boolean flag = false;
        if (EmptyUtils.isNotEmpty(skip) && CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(skip)) {
            PrecativeLoanSendVo entity = new PrecativeLoanSendVo();
            BeanUtils.copyNotNullProperties(loanInfo, entity);
            entity.setId(null);
            entity.setFatherCode(loanInfo.getBillCode());
            entity.setInfoBillCode(loanInfo.getBillCode());
            entity.setBillCode(null);
            entity.setInfoId(loanInfo.getId());
            // 当跳过发放时，设的发放日期为开始日期，如果开始日期已日结，则取日结日期的下一天
            Date sendDate = loanInfo.getStartDate();
            if (!EmptyUtils.isEmpty(loanInfo.getCorpId())) {// 非银行办理
                Date tallyDate = fcDayCloseService.getDayCloseFinalDate(loanInfo.getCorpId());// 获取网点日结日期
                if (DateUtils.dayDiffs(sendDate, tallyDate) < 0) {
                    sendDate = DateUtils.addDay(tallyDate, 1);
                }
            }
            entity.setSendDate(sendDate);
            entity.setDeadlineDate(loanInfo.getEndDate());
            entity.setSendTime(1);
            entity.setConId(loanInfo.getConId()); // 对接的合同信息id
            entity.setCreateBy(loanInfo.getCreateBy());
            entity.setCreateName(loanInfo.getCreateName());
            entity.setUpdateBy(loanInfo.getUpdateBy());
            entity.setCreateName(loanInfo.getCreateName());

            //委托贷款表里面没有计划ID 根据单号找一下还款计划表
            CmsRepaymentSchedule schedule = repaymentScheduleDao.findById(loanInfo.getId());
            Optional.ofNullable(schedule).ifPresent(item -> {
                entity.setPlanId(item.getId());//补齐计划ID
            });
            submitOrTemp(entity, "1", user, true);
            // 设置info表数据为记账完成
            loanInfo.setStatus(CmsConstant.COMPELETOTAL);
            // cmsPrecativeLoanInfoService.update(loanInfo);
            flag = true;
        }
        return flag;
    }
    /***
     * 发放打回 只有当已发放金额为零时，才可以发放打回 挂担保，发放打回到贷款补录页面，需要释放担保合同金额 挂授信，发放打回到贷款补录页面，需要释放授信金额 将贷款打回到发放状态 将发放表的记录改成删状态
     */
    public void back(String infoId, ICurrentUser user) {
        CmsPrecativeLoanInfo loanInfo = cmsPrecativeLoanInfoService.get(infoId);
        boolean flag = cmsPrecativeLoanSendDao.isCanCallBack(loanInfo.getBillCode());
        if (flag) {
            loanInfo.setStatus(CmsConstant.APPROVED);
            loanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_27);
            cmsPrecativeLoanInfoService.update(loanInfo);
            List<CmsPrecativeLoanSend> list = cmsPrecativeLoanSendDao.getLoanSendByFatherCode(loanInfo.getBillCode());
            if (!EmptyUtils.isEmptyList(list)) {
                for (CmsPrecativeLoanSend cm : list) {
                    deleteEntity(cm);
                }
            }
            // 委托贷款发放打回,对接了合同管理，取消合同对接
            if (StringUtils.isNotEmpty(loanInfo.getConId())) {
                if (StringUtils.isNotEmpty(loanInfo.getConTypeId())) {
                    loanInfo.setConId("");
                    loanInfo.setConTypeId("");
                }
                conCommonService.unHookConOp(loanInfo.getConId(), loanInfo.getId(), user);
            }
            msService.insertProcessMsg(loanInfo.getBillCode(), PrecativeConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_SEND_BACK, user);
            notificationService.complete(NotificationConstant.CMS.PRECATIVE_SEND, loanInfo.getId(), user.getId());
            notificationService.send(loanInfo.getId(), loanInfo.getLoanOpp(), NotificationConstant.CMS.PRECATIVE_REPAIR,
                    loanInfo);
        }
    }
    /***
     * 根据info表billCode查询所有的发放记录（VIEW）
     */
    @Transactional(readOnly = true)
    public List<PrecativeLoanSendVo> getLoanSendVoByFatherCode(String fatherCode) {
        return cmsPrecativeLoanSendDao.getLoanSendVoByFatherCode(fatherCode);
    }
    /**
     * 功能介绍 ： 获取凭证号 日期：2017年7月24日
     */
    protected String getPrecativeBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PRECATIVE_SEND_ORDER_NO, prefix);
    }
    public BaseDao<CmsPrecativeLoanSend, String> getEntityDao() {
        return cmsPrecativeLoanSendDao;
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public VoucherWrapperDTO submit(FcAccbookVoucherDTO fcAccbook, Integer type, ICurrentUser user) {
        setAccBookVoucher(fcAccbook, type);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user);
        //使用返回数据 dubbo调用导致某些属性丢失
        voucher = fcAccbookService.submitVoucher(voucher);
        return voucher;
    }
    /***
     * 凭证封装对象
     */
    public VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, ICurrentUser user) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(user.getId());// 设置用户
        voucher.setAutoCheck(true);
        return voucher;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    public void setAccBookVoucher(FcAccbookVoucherDTO voucher, Integer type) {
        voucher.getAccbook().setBusOriType(type);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<FcAccbookDtlDTO>();
        // 初始化数据
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount())
                    && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        // 明细进行先借后贷的排序
        Collections.sort(bookDtls, new Comparator<FcAccbookDtlDTO>() {
            public int compare(FcAccbookDtlDTO o1, FcAccbookDtlDTO o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
    }
    /**
     *
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(CmsPrecativeLoanInfoVo loanInfo, CmsPrecativeLoanSend send,
            ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_PRECATIVE_LOAN.get());
        entity = processTallyAccbook(loanInfo, send, ruleDefine, user);
        return entity;
    }
    /**
     * 功能：根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 设置凭证信息 1、补录跳过发放的情况下，起息日期取开始日期；不跳过发放的情况下，起息日期取发放日期
     * 2、在补录跳过发放的情况下，先判断开始日期是否已经日结，如果没有日结记账凭证的记账日期是开始日期；如果已经日结，记账日期是日结日期后一天。同发放日期
     */
    public FcAccbookVoucherDTO processTallyAccbook(CmsPrecativeLoanInfoVo loanInfo, CmsPrecativeLoanSend send,
            FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, loanInfo, send, user);
        accBook.setBusRaceId(getFcBusRaceId(loanInfo.getPrecativeId(), SysRaceCode.NB_PRECATIVE_LOAN,
                user.getId()));
        String voucherType = ruleDefine.getVoucherType();
        if (EmptyUtils.isEmpty(voucherType)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        }
        accBook.setVoucherType(voucherType);
        accBook.setTallyDate(send.getSendDate());
        /*
         * if("1".equals(loanInfo.getSkipLoanSendOut())){ accBook.setInterestStartDate(loanInfo.getStartDate()); }else{
         * }
         */
        accBook.setInterestStartDate(send.getSendDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_F);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsPrecativeLoanInfoVo loanInfo,
            CmsPrecativeLoanSend send, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            FcSubjectDTO sub = fcSubjectService.findSubjectById(entry.getSubjectCode());
            if (Integer.valueOf(FinacialConstant.ENTRY_TYPE_10).intValue() == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                groupNum = entry.getGroupNum().intValue();
                /**
                 * 1.借：企业活期存款 （委托方） 2.贷：委托存款 （新开户） 3.借：委托贷款 （新开户） 4.贷：企业活期存款（贷款方）
                 */
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay
                        && (1 == groupNum || 3 == groupNum)) {
                    if (3 == groupNum) {
                        // 贷款方 新开委托贷款账号
                        if (null != entry.getSubjectCode()) {
                            FcAccDTO dto = new FcAccDTO();
                            dto.setNetId(loanInfo.getCorpId());
                            dto.setCorpId(loanInfo.getLoanOpp());
                            dto.setCurId(loanInfo.getCurId());
                            // 账户类型
                            dto.setAccType(InnerAccountConstant.FCACC_LOANBYMANDATE);
                            dto.setSubjectId(entry.getSubjectCode());
                            // 自动复核
                            dto.setAutoCheck(true);
                            // 账户状态
                            // dto.setAccState("N");
                            dto.setAccName(loanInfo.getLoanOppName());
                            // 余额方向
                            dto.setBalanceWay(sub.getBalanceWay());
                            // 计息标志
                            dto.setInterestSign(CmsConstant.IS_TRUE);
                            dto.setInterestRate(send.getRate());
                            // 开户日期
                            dto.setAccOpenTime(new Date());
                            // 账号启用日期
                            dto.setAccStartDate(send.getSendDate());
                            // 委托存款单位
                            String corpCode = loanInfo.getPrecativeId();
                            // 委托贷款方
                            dto.setPrecationCorp(corpCode);
                            // 设置委托存款账号
                            dto.setPrecationAcc(send.getDepositAcc());
                            /*
                             * //设置期限 dto.setNotifyType((int)((send.getDeadlineDate().getTime() -
                             * send.getSendDate().getTime())/(1000*60*60*24))); //设置期限单位
                             * dto.setEndUnit(CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
                             */
                            // 设置期限
                            if (CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())) {
                                dto.setNotifyType(
                                        cmsContractService.getLoanTermNew(send.getSendDate(), send.getDeadlineDate()));
                            } else {
                                dto.setNotifyType(DateUtils.dayDiffs(send.getDeadlineDate(), send.getSendDate()));
                            }
                            // 设置期限单位
                            dto.setEndUnit(CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())
                                    ? CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH : CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
                            // 余额方向
                            dto.setBalanceWay(balanceWay);
                            // 委托存款的手续费率
                            dto.setPoundageRate(loanInfo.getPoundageRate());
                            dto.setPredrawSign(loanInfo.getInterestWay());
                            // 逾期利率
                            dto.setOverdueRate(send.getOverdueRate());
                            // 利率 取send贷款利率
                            dto.setInterestRate(send.getRate());
                            // 起息日期 取send发放日期
                            dto.setInterestStartDate(send.getSendDate());
                            // 到期日期
                            dto.setDeadlineDate(send.getDeadlineDate());
                            // 合同编号
                            dto.setContractNo(send.getContractCode());
                            String account = innerAccountService.saveOrUpdate(dto, user);
                            // 委贷账号
                            send.setLoanAcc(account);
                            accbookDtl.setAccount(account);
                            accbookDtl.setDebitMoney(send.getMoney());
                            accbookDtl.setAccName(dto.getAccName());
                            // 自动授权
                            String netId = corpService.getNetByCorpId(loanInfo.getCorpId()).getNetId();
                            List<String> tallyUsers = getAutoCheckUser(netId);
                            tallyUsers.add(user.getId());
                            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(account);
                            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), tallyUsers);
                        }
                    } else {
                        // 委托方企业活期账号
                        if (null != entry.getSubjectCode()) {
                            List<FcAccDTO> list = innerAccountService.listAccountByParams(loanInfo.getCurId(),
                                    loanInfo.getCorpId(), loanInfo.getPrecativeId(), entry.getSubjectCode());
                            if (EmptyUtils.isEmptyList(list)) {
                                CorpDTO corpDTO = corpService.get(loanInfo.getPrecativeId());
                                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                        corpDTO.getName());
                            }
                            FcAccDTO dto = list.get(0);
                            accbookDtl.setAccount(dto.getBlnctrAcc());
                            accbookDtl.setDebitMoney(send.getMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        }
                    }
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay
                        && (2 == groupNum || 4 == groupNum)) {
                    if (2 == groupNum) {
                        // 贷：委托存款 （新开户）
                        FcAccDTO dto = new FcAccDTO();
                        if (null != entry.getSubjectCode()) {
                            dto.setNetId(loanInfo.getCorpId());
                            dto.setCorpId(loanInfo.getPrecativeId());
                            dto.setCurId(loanInfo.getCurId());
                            dto.setAccType(InnerAccountConstant.FCACC_ENTRUSTED_DEPOSIT);
                            dto.setSubjectId(entry.getSubjectCode());
                            dto.setAutoCheck(true);
                            // dto.setAccState("N");
                            dto.setAccName(loanInfo.getPrecativeName());
                            dto.setBalanceWay(sub.getBalanceWay());
                            // 计息标志
                            dto.setInterestSign(CmsConstant.IS_FALSE);
                            // 开户日期
                            dto.setAccOpenTime(new Date());
                            // 账号启用日期
                            dto.setAccStartDate(send.getSendDate());
                            // 合同编号
                            dto.setContractNo(send.getContractCode());
                            /*
                             * //设置期限(天数) dto.setNotifyType((int)((send.getDeadlineDate().getTime() -
                             * send.getSendDate().getTime())/(1000*60*60*24))); //设置期限单位
                             * dto.setEndUnit(CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
                             */
                            // 设置期限
                            if (CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())) {
                                dto.setNotifyType(
                                        cmsContractService.getLoanTermNew(send.getSendDate(), send.getDeadlineDate()));
                            } else {
                                dto.setNotifyType(DateUtils.dayDiffs(send.getDeadlineDate(), send.getSendDate()));
                            }
                            // 设置期限单位
                            dto.setEndUnit(CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())
                                    ? CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH : CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
                            /*
                             * //利率 dto.setInterestRate(send.getRate());
                             */
                            dto.setPredrawSign("5");
                            // 委托存款的手续费率
                            dto.setPoundageRate(loanInfo.getPoundageRate());
                            String account = innerAccountService.saveOrUpdate(dto, user);
                            // 委存账号
                            send.setDepositAcc(account);
                            accbookDtl.setAccount(account);
                            accbookDtl.setCreditMoney(send.getMoney());
                            accbookDtl.setAccName(dto.getAccName());
                            // 自动授权
                            List<String> tallyUsers = getAutoCheckUser(loanInfo.getCorpId());
                            tallyUsers.add(user.getId());
                            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(account);
                            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), tallyUsers);
                        }
                    } else {
                        // 4.贷：企业活期存款（贷款方）
                        if (null != entry.getSubjectCode()) {
                            List<FcAccDTO> list = innerAccountService.listAccountByParams(loanInfo.getCurId(),
                                    loanInfo.getCorpId(), loanInfo.getLoanOpp(), entry.getSubjectCode());
                            if (EmptyUtils.isEmptyList(list)) {
                                CorpDTO corpDTO = corpService.get(loanInfo.getLoanOpp());
                                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                        corpDTO.getName());
                            }
                            FcAccDTO dto = list.get(0);
                            accbookDtl.setAccount(dto.getBlnctrAcc());
                            accbookDtl.setCreditMoney(send.getMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        }
                    }
                }
                accbookDtl.setCurCode(loanInfo.getCurId());
                accbookDtl.setOrigVoucherNo(send.getBillCode());
                accbookDtl.setSummaryContent(loanInfo.getPurpose());
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 获取业务总类id
     */
    public String getFcBusRaceId(String netCode, ConstantBean<String> sysBean, String userId) {
        List<FcBusRaceDTO> raceList = fcBusRaceService.findBySysRaceCode(sysBean.get());
        // 比较网点是否相同，相同则取网点维护的业务品种ID，否则取
        FcBusRaceDTO tempRace = null;
        String busRaceId = "";
        for (FcBusRaceDTO busRace : raceList) {
            String netId = busRace.getNetId();
            if (!StringUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                busRaceId = busRace.getId();
                break;
            }
        }
        if (null == tempRace) {
            if (raceList.isEmpty() || raceList.size() == 0) {
                tempRace = new FcBusRaceDTO();
                tempRace.setNetId(netCode);
                tempRace.setRaceCode(sysBean.get());
                tempRace.setRaceName(sysBean.getName());
                tempRace.setSysRaceCode(sysBean.get());
                fcBusRaceService.save(tempRace, new String[] { netCode }, userId);
                raceList = fcBusRaceService.findByNetIdSysRaceCode(sysBean.get(), netCode);
                busRaceId = raceList.get(0).getId();
            } else {
                busRaceId = raceList.get(0).getId();
            }
        }
        return busRaceId;
    }
    /***
     * 发放数据校验
     */
    public void check(PrecativeLoanSendVo send, String type) {
        /*
         * if(cmsPrecativeLoanSendDao.validateSendStatus(send.getFatherCode())){ //验证当前贷款是否存在未生成合同的有效发放记录 throw new
         * ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FAILE); }
         */
        if (cmsPrecativeLoanSendDao.validateSendTime(send.getFatherCode(), send.getSendTime())) {
            // 验证当前发放次数是否正确
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDTIME_EXISTS,
                    String.valueOf(send.getSendTime()));
        }
        if (EmptyUtils.isEmpty(send.getContractCode())) {
            // 委贷发放合同编码不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_CONTRACTCODE_NOT_NULL);
        }
        if (cmsPrecativeLoanSendDao.isExsitContractCode(send.getContractCode(), send.getBillCode())) {
            // 发放编号重复
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SEND_CONTRACTCODE_NOT_UNIQUE);
        }
        if (EmptyUtils.isEmpty(send.getMoney())) {
            // 发放金额不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SEND_MONEY_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(send.getSendDate())) {
            // 发放日期不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDDATE_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(send.getDeadlineDate())) {
            // 到期日期不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEADLINEDATE_NOT_NULL);
        }
        if (send.getSendDate().getTime() > send.getDeadlineDate().getTime()) {
            // 发放日期不能大于到期日期
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDATE_PASS_DEADLINEDATE);
        }
        if (EmptyUtils.isEmpty(send.getRepayType())) {
            // 还款方式不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_REPAYTYPE_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(send.getInterestWay())) {
            // 计息方式不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_INTERESTWAY_NOT_NULL);
        }
        if (!CmsConstant.FLOAT_WAY_01.equals(send.getFloatWay())
                && EmptyUtils.isEmpty(send.getRateEntriesEffectDate())) {
            // 浮动利率不为固定利率时，利率调整生效日期不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_RATEENTRIESEFFECTDATE_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(send.getRate())) {
            // 贷款利率不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_RATE_NOT_NULL);
        }
        if(EmptyUtils.isEmpty(send.getPlanId())){
            // 还款计划不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_PLANID_IS_NULL);
        }
    }
    /**
     * 查询放款金额是否超出贷款总金额
     */
    public boolean isMoreLoanMoney(PrecativeLoanSendVo pre, BigDecimal loanMoney) {
        String fatherCode = pre.getInfoBillCode();
        BigDecimal sendMoney = BigDecimal.ZERO;
        BigDecimal repayedMoney = cmsPrecativeLoanSendDao.getSendedMoney(fatherCode);
        if (EmptyUtils.isEmpty(pre.getBillCode())) {
            sendMoney = sendMoney.add(loanMoney);
        } else {
            sendMoney = repayedMoney;
        }
        return sendMoney.compareTo(loanMoney) > 0;
    }
    /***
     * 校验提交的数据
     */
    public void checkSave(PrecativeLoanSendVo send) {
        if (cmsPrecativeLoanSendDao.isExsitBusinessSend(send.getFatherCode())) {
            // 该委托贷款存在业务发放记录
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_EXISTS_SEND);
        }
    }
    /**
     * 定时任务，提醒发放到期
     */
    @Override
    @XxlJob("CmsPrecativeLoanSendService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.PRECATIVE_CONFIG_NOTIFYDAYS);// 授信到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询授信到期数据
        List<CmsPrecativeLoanSend> sends = cmsPrecativeLoanSendDao.querySendListDTOs(endDate);
        if (!EmptyUtils.isEmptyList(sends)) {
            for (CmsPrecativeLoanSend send : sends) {
                PrecativeLoanSendVo sendVo = new PrecativeLoanSendVo();
                BeanUtils.copyProperties(send, sendVo);
                notificationService.send(send.getId(), send.getPrecativeId(), NotificationConstant.CMS.PRECATIVE_MATURE,
                        sendVo);
                logger.info("委托贷款发放到期提醒，编码：" + send.getBillCode() + "，合同：" + send.getContractCode());
                send.setSendSign(CommonConstant.SEND_SIGN_YES);// 更改发送标识
                update(send);
            }
        }
    }
    /**
     * @Description 查询委托贷款信贷参数委托贷款合同履约环节
     * @Param []
     * @return java.lang.String
     * @Date 2019/11/18 9:37
     * @Author fusb
     */
    public String conSegment() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.PRECATIVE_SEGMENT)
                .toFormatString();
    }
    /**
     * @Description 委托贷款挂接合同生成履约信息
     * @Param [entity, user]
     * @return void
     * @Date 2019/11/18 9:37
     * @Author fusb
     */
    public void createSegmentInfo(CmsPrecativeLoanInfoVo loanInfo, CmsPrecativeLoanSend send) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(send.getUpdateBy())) {
            user = userService.getByUserId(send.getUpdateBy());
        } else {
            user = userService.getByUserId(send.getCreateBy());
        }
        if (StringUtils.isNotEmpty(loanInfo.getConId()) && this.conSegment().contains(CmsConstant.SEGMENT_LOAN)) {
            ConInfoRecordDTO dto = new ConInfoRecordDTO();
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                dto.setConId(conDTO.getId());
                dto.setOuterId(send.getId());
                dto.setOpMsg(CmsConstant.PRECATIVE_LOAN_SEND);
                dto.setOpDate(send.getSendDate());
                dto.setOpNo(send.getBillCode());
                dto.setOpMoney(send.getMoney());
                conCommonService.performanceConOp(dto, user);
            }
        }
    }
    /**
     * 获取自动记账人
     */
    public List<String> getAutoCheckUser(String netId) {
        ConfigDTO tallyConfig = configService.getConfig(ConfigConstant.FCL, "autoAccounter");
        ConfigDTO checkeConfig = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String tallyValue = tallyConfig.getValue();
        if (EmptyUtils.isEmpty(tallyValue)) {// 自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL);
        }
        String checkValue = checkeConfig.getValue();
        if (EmptyUtils.isEmpty(checkValue)) {// 自动复核人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        tallyValue = tallyValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        checkValue = checkValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netTallyUsers = tallyValue.split(";");// 分割
        String[] netCheckUsers = checkValue.split(";");// 分割
        Map<String, String> tallyUserInfo = new HashMap<String, String>();
        for (String netUser : netTallyUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            tallyUserInfo.put(temps[0].trim(), temps[1].trim());
        }
        CorpDTO corpDTO1 = corpService.get(netId);
        String userCode1 = tallyUserInfo.get(corpDTO1.getCode());// 获取记账人
        UserDTO userDTO1 = userService.getByLoginName(userCode1);
        if (null == corpDTO1 || null == userCode1) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL);
        }
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netCheckUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            userInfo.put(temps[0].trim(), temps[1].trim());
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());// 获取复核人
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null == userDTO || null == corpDTO) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        if (null != userDTO && null != userDTO1) {
            List<String> users = new ArrayList<>();
            users.add(userDTO.getId());
            users.add(userDTO1.getId());
            return users;
        }
        return new ArrayList<>();
    }
    private void setAccountGrant(String accountId, String corpId, List<String> users) {
        users.removeAll(Collections.singleton(null));
        for (String user : users) {
            AccountGrantDTO ag = new AccountGrantDTO();
            ag.setAccountId(accountId);
            ag.setAccountType(1);
            ag.setUserId(user);
            ag.setCorpId(corpId);
            iAccountGrantService.saveAccountGrant(ag);
        }
    }
    /**
     * 单位禁用校验-待记账
     *
     * @Param corpId
     * @return
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsPrecativeLoanSendDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    /** 查询待记账发放数据 */
    public List<CmsPrecativeLoanSendDTO> queryTallyPrecativeSend() {
        return cmsPrecativeLoanSendDao.queryTallyPrecativeSend();
    }
}
