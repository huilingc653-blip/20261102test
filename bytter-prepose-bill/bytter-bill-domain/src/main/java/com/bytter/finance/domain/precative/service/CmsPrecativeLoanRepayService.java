package com.bytter.finance.domain.precative.service;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanRepayDao;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanRepay;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
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
 * @Description: 委托贷款还款信息表
 * @date 2017-08-02 14:05:07
 */
@WorkFlow(CmsWorkFlowConstant.CMS_PRECATIVE_REPAY)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_PRECATIVE_REPAY)
@Service("cmsPrecativeLoanRepayService")
@Transactional
public class CmsPrecativeLoanRepayService extends BaseService<CmsPrecativeLoanRepay, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private CmsPrecativeLoanRepayDao cmsPrecativeLoanRepayDao;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @DubboReference(check = false)
    private IFcInterestService fcInterestService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsPrecativeLoanRepay entity) {
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
    public void save(CmsPrecativeLoanRepay entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsPrecativeLoanRepay entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsPrecativeLoanRepay entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 加载审核列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PRECATIVE_REPAY,
                dataTablesInput);
        DataTablesOutput dp = cmsPrecativeLoanRepayDao.dataTableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 利息确认列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRepayInteretAffirm(DataTablesInput input, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_INTERETAFFIRM_QUERY, userId);
        return cmsPrecativeLoanRepayDao.listRepayInteretAffirm(input, corpAuth);
    }
    /**
     * 还款补录列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRepayAdditional(DataTablesInput input, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_INTERETAFFIRM_QUERY, userId);
        return cmsPrecativeLoanRepayDao.listRepayAdditional(input, corpAuth);
    }
    /***
     * 查询归还列表
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getLoanRepay(DataTablesInput input, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_REPAY_QUERY, userId);
        return cmsPrecativeLoanRepayDao.getLoanRepay(input, corpAuth);
    }
    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataRepayPlanable(DataTablesInput input,ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LOAN_CMSLOANSEND_APPLY, user.getId());
        return cmsPrecativeLoanRepayDao.dataRepayPlanable(input, corpAuth);
    }
    /***
     * 查询修改打回列表
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getEditLoanRepay(DataTablesInput input, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_REPAY_QUERY, userId);
        return cmsPrecativeLoanRepayDao.getEditLoanRepay(input, corpAuth);
    }
    /***
     * 根据billCode查询repayVo
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanRepayVo getReapyByBillCode(String billCode) {
        return cmsPrecativeLoanRepayDao.getRepayByBillCode(billCode, null);
    }
    /***
     * 根据id查询repayVo
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanRepayVo getReapyById(String id) {
        return cmsPrecativeLoanRepayDao.getRepayByBillCode(id, "id");
    }
    /**
     * 查询归还金额
     */
    @Transactional(readOnly = true)
    public BigDecimal queryRepayMoney(String billCode) {
        CmsPrecativeLoanRepayVo vo = cmsPrecativeLoanRepayDao.queryPassageMoney(billCode, "repay");
        return EmptyUtils.isEmpty(vo) ? BigDecimal.ZERO
                : (vo.getPassageMoney() == null ? BigDecimal.ZERO : vo.getPassageMoney());
    }
    /**
     * 查询归还利息
     */
    @Transactional(readOnly = true)
    public BigDecimal queryRepayAccrual(String billCode) {
        CmsPrecativeLoanRepayVo vo = cmsPrecativeLoanRepayDao.queryPassageMoney(billCode, "repay");
        return EmptyUtils.isEmpty(vo) ? BigDecimal.ZERO
                : (vo.getRepayAccrual() == null ? BigDecimal.ZERO : vo.getRepayAccrual());
    }
    /**
     * 查询在途金额
     */
    @Transactional(readOnly = true)
    public BigDecimal queryPassageMoney(String billCode) {
        CmsPrecativeLoanRepayVo vo = cmsPrecativeLoanRepayDao.queryPassageMoney(billCode, "passage");
        return EmptyUtils.isEmpty(vo) ? BigDecimal.ZERO : vo.getPassageMoney();
    }
    /**
     * 根据voucherNo获取委托贷款还款单据的本金
     */
    @Transactional(readOnly = true)
    public BigDecimal getPrecativeLoanRepayMoney(String voucherNo) {
        return cmsPrecativeLoanRepayDao.getPrecativeLoanRepayMoney(voucherNo);
    }
    /**
     * @Description 根据委托贷款主表信息编号查询委托贷款已还总金额
     * @Param [infoBillCode]
     * @return java.math.BigDecimal
     * @Date 2019/12/2 16:36
     * @Author fusb
     */
    @Transactional(readOnly = true)
    public BigDecimal getPrecativeLoanRepayMoneySum(String infoBillCode) {
        return cmsPrecativeLoanRepayDao.getInfoRepayMoneySum(infoBillCode);
    }
    /***
     * 归还提交
     */
    public void submit(CmsPrecativeLoanRepayVo RepayVo, String type, ICurrentUser user) {
        String repayId = (!EmptyUtils.isEmpty(RepayVo.getId()) || !"".equals(RepayVo.getId())) ? RepayVo.getId() : null;
        // CmsPrecativeLoanInfoVo loanInfo =
        // cmsPrecativeLoanInfoService.getLoanInfoByBillCode(RepayVo.getInfoBillCode());
        check(RepayVo);
        CmsPrecativeLoanRepay entity = new CmsPrecativeLoanRepay();
        BeanUtils.copyNotNullProperties(RepayVo, entity);
        CmsPrecativeLoanSend temp = cmsPrecativeLoanSendService.getByBillCode(entity.getSendCode());
        // 归还提交后，将发放状态改为归还中
        if (!CmsConstant.BUSINESSSTATE_33.equals(temp.getBizStatus())) {
            temp.setBizStatus(CmsConstant.BUSINESSSTATE_33);
        }
        cmsPrecativeLoanSendService.saveOrUpdate(temp);
        if (repayId == null) {
            String billCode = getPrecativeBillCode(PrecativeConstant.REPAY_BILL_CODE_PREFIX);
            entity.setCreateBy(user.getId());
            entity.setCreateName(user.getUsername());
            entity.setCreateDate(DateUtils.getDate());
            entity.setBillCode(billCode);
            RepayVo.setBillCode(billCode);
        } else {
            entity.setUpdateBy(user.getId());
            entity.setUpdateName(user.getUsername());
            entity.setUpdateDate(DateUtils.getDate());
        }
        if (RepayVo.getStatus() == 1) {
            entity.setStatus(CmsConstant.COMMITAPPROVE);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_41);
            /*
             * if(EmptyUtils.isNotEmpty(entity.getRepaymentScheduleId())){ RepaymentSchedule schedule =
             * repaymentScheduleDao.get(entity.getRepaymentScheduleId()); schedule.setIsRepay("1");//已还
             * schedule.setRealityRepayDate(entity.getRepayDate());//实际还款日
             * schedule.setRealityRepayMoney(entity.getRepayMoney());//实际还款本金
             * repaymentScheduleDao.saveOrUpdate(schedule); }
             */
        } else {
            entity.setStatus(CmsConstant.TEMP);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_40);
        }
        entity.setId(repayId);
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY, entity.getId(), user.getId());
        saveOrUpdate(entity);
        if (RepayVo.getStatus() == 1) {
            if (cmsPrecativeLoanInfoService.isBudget(RepayVo.getPrecativeId(), BaseDataConstant.BRIEF_STATUS_3000)) {
                RepayVo.setStatus(CmsConstant.COMMITAPPROVE);
                // 归还提交和归还修改提交占用资金计划
                this.otherBatchEngross(RepayVo, user, BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY);
            }
            if (repayId == null) {
                msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT_CHECK, user);
                msService.insertProcessMsg(temp.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT_CHECK, user);
            } else {
                msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_CHECK, user);
                msService.insertProcessMsg(temp.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_CHECK, user);
            }
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PRECATIVE_REPAY, entity.getId(), entity, entity.getCorpId(),
                    user.getId());
        } else {
            if (cmsPrecativeLoanInfoService.isBudget(RepayVo.getPrecativeId(), BaseDataConstant.BRIEF_STATUS_3000)) {
                // 录入提交和修改提交占用资金计划
                RepayVo.setStatus(CmsConstant.TEMP);
                this.otherBatchEngross(RepayVo, user, BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY);
            }
            if (repayId == null) {
                msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_APPLY_TEMP, user);
                msService.insertProcessMsg(temp.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_APPLY_TEMP, user);
                notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.CMS.PRECATIVE_REPAY,
                        entity);
            } else {
                msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                msService.insertProcessMsg(temp.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.CMS.PRECATIVE_REPAY,
                        entity);
            }
        }
    }
    /**
     * 功能介绍 归还补录提交。<br>
     * <b>日期：</b>2018年3月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public void repayAdditionalSubmit(CmsPrecativeLoanRepayVo pre, ICurrentUser user) {
        checkRepair(pre);
        if (this.isBudget(pre.getLoanOpp(), BaseDataConstant.BRIEF_STATUS_3000)) {
            // 录入提交和修改提交占用资金计划
            pre.setStatus(CmsConstant.APPROVED); // 资金计划状态，11-待审批
            this.batchEngross(pre, user);
        }
        CmsPrecativeLoanRepay loanRepay = cmsPrecativeLoanRepayDao.getRepay(pre.getBillCode());
        loanRepay.setStatus(CmsConstant.REPAYADDITIONAL_OK);
        loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_59);
        update(loanRepay);
    }
    /**
     * 利息确认通过
     */
    public void interetAffirmPass(CmsPrecativeLoanRepay loanRepay, ICurrentUser user) {
        CmsPrecativeLoanRepay entity = this.get(loanRepay.getId(), true);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(entity.getSendCode());
        if (loanRepay.getStatus() == 1) {// 通过
            notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY_CONFIRM, entity.getId(),
                    user.getId());
            entity.setRepayAccrual(loanRepay.getRepayAccrual());
            CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
            // 通知委托人收到还款信息
            // notificationService.send(loanRepay.getId(), loanInfo.getPrecativeId(),
            // NotificationConstant.CMS.PRECATIVE_REPAY_SUCCESS, entity);
            if (EmptyUtils.isNotEmpty(entity.getRepaymentScheduleId())) {
                RepaymentSchedule schedule = repaymentScheduleDao.get(entity.getRepaymentScheduleId());
                schedule.setIsRepay("1");// 已还
                schedule.setRealityRepayDate(entity.getRepayDate());// 实际还款日
                schedule.setRealityRepayMoney(entity.getRepayMoney());// 实际还款本金
                repaymentScheduleDao.saveOrUpdate(schedule);
            }
            if (CmsConstant.REPAY_TYPE_4.equals(loanRepay.getRepayType())) {// 归还利息
                entity.setRepayMoney(new BigDecimal(0));
            }
            if (!CmsConstant.REPAY_TYPE_1.equals(loanRepay.getRepayType())) {// 需要归还利息
                // TODO 利息处理
                if (EmptyUtils.isNotEmpty(entity.getLoanAcc())
                        && EmptyUtils.isNotEmpty(entity.getRepayMoney().toString())
                        && entity.getRepayMoney().compareTo(new BigDecimal(0)) > 0
                        && !EmptyUtils.isEmpty(entity.getRepayDate())) {
                    fcInterestService.processTellerMoneyBalance(entity.getLoanAcc(), entity.getRepayMoney(), null,
                            user, entity.getId(), DateUtils.addDay(entity.getRepayDate(), -1));
                }
            }
            String isEntrustTally = configService.getConfig("cms", "is_entrust_tally").getValue();// 查询记账参数
            String isEntrustAutomaticTally = configService.getConfig("cms", "is_entrust_automatic_tally").getValue();
            if (CmsConstant.IS_BANK_0.equals(loanInfo.getIsBank())) {
                // 查询记账参数
                if (CmsConstant.IS_TRUE.equals(isEntrustTally)) { // 是否记账
                    // 自动记账
                    if (CmsConstant.IS_TRUE.equals(isEntrustAutomaticTally)) {
                        CorpDTO loan = corpService.get(loanInfo.getLoanOpp());
                        CorpDTO precative = corpService.get(send.getPrecativeId());
                        String netId = loanInfo.getCorpId();
                        if (CorpConstant.CORP_TYPE_CODE_CORP.equals(loan.getType())
                                && CorpConstant.CORP_TYPE_CODE_CORP.equals(precative.getType())) {
                            if (netId.equals(loan.getNetId()) && netId.equals(precative.getNetId())) {
                                this.submit(entity, send, user);
                            }
                        }
                        // 委托贷款对接合同管理，履约环节为还款，且为自动记账，生成履约信息
                        this.createSegmentInfo(entity, loanInfo);
                    } else {
                        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
                    }
                } else { // 不记账
                    // 委托贷款对接合同管理，履约环节为还款，且为不记账，生成履约信息
                    this.createSegmentInfo(entity, loanInfo);
                }
            }
            entity.setStatus(CmsConstant.COMPELETOTAL);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            // shezhi
            if (CmsConstant.IS_TRUE.equals(isEntrustTally) && CmsConstant.IS_FALSE.equals(isEntrustAutomaticTally)
                    && CmsConstant.IS_BANK_0.equals(loanInfo.getIsBank())) {
                entity.setStatus(CmsConstant.WAITTOTAL);
                entity.setBizStatus(CmsConstant.BUSINESSSTATE_38);
            }
            this.update(entity);
            this.repayComplete(entity, user);// 还款完处理
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INTERETAFFIRM, CmsProcessMsgConstant.MSGA_ISTSURE_PASS, user);
            // 还款完成后将归还表的日志信息添加到发放表
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INTERETAFFIRM, CmsProcessMsgConstant.MSGA_ISTSURE_PASS, user);
            // 通知委托人收到还款信息
            notificationService.send(entity.getId(), loanInfo.getPrecativeId(),
                    NotificationConstant.CMS.PRECATIVE_REPAY_SUCCESS, entity);
        } else {// 打回
            backLoanRepay(entity, user);
            // notificationService.send(entity.getId(), loanRepay.getCorpId(), NotificationConstant.CMS.PRECATIVE_REPAY,
            // entity);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_ISTSURE_BACK, user);
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_ISTSURE_BACK, user);
        }
    }
    /**
     * 利息确认打回
     */
    public void backLoanRepay(CmsPrecativeLoanRepay loanRepay, ICurrentUser user) {
        loanRepay.setStatus(CmsConstant.BACK);// 更改为打回状态
        loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_46);
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY_CONFIRM, loanRepay.getId(), user.getId());
        notificationService.send(loanRepay.getId(), loanRepay.getCorpId(), NotificationConstant.CMS.PRECATIVE_REPAY,
                loanRepay);
        this.update(loanRepay);
        if (EmptyUtils.isNotEmpty(loanRepay.getRepaymentScheduleId())) {// 还款计划设置为未归还
            cmsLoanRepayService.setRepayMentScheduleIsRepay(loanRepay.getRepaymentScheduleId(), CmsConstant.IS_FALSE);
        }
    }
    /***
     * 发放删除暂存的数据
     * 
     * @param id
     */
    public void deletePrecative(String id, ICurrentUser user) {
        CmsPrecativeLoanRepay temp = this.get(id);
        // 修改还款计划
        if (EmptyUtils.isNotEmpty(temp.getRepaymentScheduleId())) {
            RepaymentSchedule schedule = repaymentScheduleDao.get(temp.getRepaymentScheduleId());
            if (!EmptyUtils.isEmpty(schedule)) {
                schedule.setIsRepay("0");// 设置未还
                schedule.setRealityRepayDate(null);// 实际还款日
                schedule.setRealityRepayMoney(null);// 实际还款本金
                repaymentScheduleDao.saveOrUpdate(schedule);
            }
        }
        String sendCode = temp.getSendCode();
        temp.setStatus(CmsConstant.DELETED);
        notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY, temp.getId(), user.getId());
        this.update(temp);
        cmsPrecativeLoanRepayDao.flush();
        BigDecimal passageMoney = cmsPrecativeLoanRepayDao.queryPassageMoney(temp.getSendCode(), "passage")
                .getPassageMoney();
        if (passageMoney.equals(BigDecimal.ZERO)) {
            CmsPrecativeLoanSend send = cmsPrecativeLoanSendService.getByBillCode(sendCode);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            cmsPrecativeLoanSendService.update(send);
        }
    }
    /**
     * 功能介绍 : 还款完成 修改send 与 info表状态
     */
    private void repayComplete(CmsPrecativeLoanRepay entity, ICurrentUser user) {
        cmsPrecativeLoanRepayDao.flush();
        CmsPrecativeLoanSend send = cmsPrecativeLoanSendService.getByBillCode(entity.getSendCode());
        // 如果已还金额等于发放金额 修改发放表状态为业务完成
        BigDecimal sendBalance = this.completeSendBalance(entity.getSendCode());
        // 修改send表状态为还款中
        /*
         * CmsPrecativeLoanSend temp = new CmsPrecativeLoanSend(); BeanUtils.copyNotNullProperties(send, temp);
         */
        if (!CmsConstant.BUSINESSSTATE_33.equals(send.getBizStatus())) {
            send.setBizStatus(CmsConstant.BUSINESSSTATE_33);
        }
        if (sendBalance.compareTo(BigDecimal.ZERO) == 0) {// 发放金额等于还款总金额,修改send表状态为还款完成 ，将发放开户账号注销
            // 判断业务已完成的 发放金额是否等于借款合同金额
            send.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            send.setStatus(CmsConstant.FINISH);
            // 将发放开户账号注销
            if (CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_tally").getValue())) {
                String loanAcc = send.getLoanAcc();
                if (!EmptyUtils.isEmpty(loanAcc)) {
                    innerAccountService.cancelFcAcc(loanAcc, entity.getRepayDate(), user);
                }
                String depositAcc = send.getDepositAcc();
                if (!EmptyUtils.isEmpty(depositAcc)) {
                    innerAccountService.cancelFcAcc(depositAcc, entity.getRepayDate(), user);
                }
            }
        }
        cmsPrecativeLoanSendService.saveOrUpdate(send);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        BigDecimal repaySum = cmsPrecativeLoanRepayDao.getInfoRepayMoneySum(info.getBillCode());
        if (info.getMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
            info.setStatus(CmsConstant.FINISH);
            info.setBizStatus(CmsConstant.BUSINESSSTATE_110);
            CmsPrecativeLoanInfo infoTemp = new CmsPrecativeLoanInfo();
            BeanUtils.copyNotNullProperties(info, infoTemp);
            // 委托贷款对接合同管理，最后一笔还款利息确认后终止合同
            this.terminateConOp(info, entity);
            cmsPrecativeLoanInfoService.update(infoTemp);
        }
    }
    /**
     * 功能介绍 : 查询发放剩余额度（发放金额 - 已还金额）<br>
     */
    public BigDecimal completeSendBalance(String sendBillCode) {
        BigDecimal repayMoney = this.queryRepayMoney(sendBillCode);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(sendBillCode);
        return send.getMoney().subtract(repayMoney);
    }
    /**
     * 还款获取利息
     */
    public BigDecimal getRepayAccrual(String account, BigDecimal money, String orderNo, Date endInstDate,
            ICurrentUser user) {
        return fcInterestService.calTellerMoneyInterest(account, money, orderNo, endInstDate, user);
    }
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批回调方法
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg;
        // String appMsg=workFlowResult.isPass()?CmsConstant.CMS_APPROVE_SUCC:CmsConstant.CMS_APPROVE_BACK;
        CmsPrecativeLoanRepay entity = this.get(workFlowResult.getBusinessKey(), true);
        if (workFlowResult.isPass()) {
            appMsg = CmsConstant.CMS_APPROVE_SUCC;
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.APPROVED,
                    workFlowResult.getUser());
        } else {
            appMsg = CmsConstant.CMS_APPROVE_BACK;
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.BACK,
                    workFlowResult.getUser());
        }
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    /**
     * 调用审批
     */
    public void approveOver(String businessKey, boolean isPassOK, String appMsg, ICurrentUser user) {
        CmsPrecativeLoanRepay entity = get(businessKey);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(entity.getSendCode());
        if (isPassOK) {
            // 审批通过
            CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
            // //查询记账参数
            // if(CmsConstant.IS_BANK_0.equals(loanInfo.getIsBank()) &&
            // CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_tally").getValue())){
            // if(isAutoAccounting()){
            // CorpDTO loanOpp=corpService.get(loanInfo.getLoanOpp());
            // CorpDTO precative=corpService.get(loanInfo.getPrecativeId());
            // String netId=loanInfo.getCorpId();
            // if(CorpConstant.CORP_TYPE_CODE_CORP.equals(loanOpp.getType()) &&
            // CorpConstant.CORP_TYPE_CODE_CORP.equals(precative.getType())){
            // if(netId.equals(loanOpp.getNetId()) && netId.equals(precative.getNetId())){
            // this.submit(entity, send, user);
            // }
            // }
            // }else{
            // entity.setStatus(CmsConstant.WAITTOTAL);
            // entity.setBizStatus(CmsConstant.BUSINESSSTATE_38);
            // }
            // //notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY_CONFIRM, entity.getId());
            // //还款确认消息提醒
            // notificationService.send(businessKey, entity.getCorpId(),
            // NotificationConstant.CMS.PRECATIVE_REPAY_CONFIRM, entity);
            // this.update(entity);
            // }else{
            // if(EmptyUtils.isNotEmpty(entity.getRepaymentScheduleId())){
            // RepaymentSchedule schedule = repaymentScheduleDao.get(entity.getRepaymentScheduleId());
            // schedule.setIsRepay("1");//已还
            // schedule.setRealityRepayDate(entity.getRepayDate());//实际还款日
            // schedule.setRealityRepayMoney(entity.getRepayMoney());//实际还款本金
            // repaymentScheduleDao.saveOrUpdate(schedule);
            // }
            // //还款成功，通知委托人查看
            // notificationService.send(businessKey, loanInfo.getPrecativeId(),
            // NotificationConstant.CMS.PRECATIVE_REPAY_SUCCESS, entity);
            // entity.setStatus(CmsConstant.COMPELETOTAL);
            // entity.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            // this.update(entity);
            // this.repayComplete(entity,user);//还款完处理
            // }
            entity.setStatus(CmsConstant.APPROVED);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_43);
            this.update(entity);
            // this.repayComplete(entity);//还款完处理
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
            notificationService.send(entity.getId(), entity.getCorpId(),
                    NotificationConstant.CMS.PRECATIVE_REPAY_CONFIRM, entity);
        } else {
            // 审批打回
            entity.setStatus(CmsConstant.BACK);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_49);
            this.update(entity);
            if (EmptyUtils.isNotEmpty(entity.getRepaymentScheduleId())) {// 还款计划设置为未归还
                cmsLoanRepayService.setRepayMentScheduleIsRepay(entity.getRepaymentScheduleId(), CmsConstant.IS_FALSE);
            }
            msService.insertProcessMsg(entity.getBillCode(), PrecativeConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            msService.insertProcessMsg(send.getBillCode(), PrecativeConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            // notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.CMS.PRECATIVE_REPAY,
            // entity);
        }
    }
    /**
     * 调用记账
     */
    public void submit(CmsPrecativeLoanRepay entity, PrecativeLoanSendVo send, ICurrentUser user) {
        // 构建凭证传输对象
        FcAccbookVoucherDTO accbook = processAccbook(send, entity, user);
        // 记账提交暂存
        VoucherWrapperDTO voucher = cmsPrecativeLoanSendService.submit(accbook, BusOriType.NB_PRECATIVE_REPAYMENT.get(),
                user);
        // 设置记账返回数据
        FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
        // List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
        // 记账流水号
        entity.setOrderNo(acc.getOrderNo());
        // 记账凭证号
        entity.setVoucherNo(acc.getVoucherNo());
    }
    /**
     * 判断是否自动记账
     */
    public boolean isAutoAccounting() {
        if (CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_entrust_automatic_tally").getValue())) {
            return true;
        }
        return false;
    }
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(PrecativeLoanSendVo send, CmsPrecativeLoanRepay repay,
            ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsPrecativeLoanSendService
                .getBusinessRuleExp(SysRaceCode.NB_PRECATIVE_REPAYMENT.get());
        entity = processTallyAccbook(send, repay, ruleDefine, user);
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookVoucherDTO processTallyAccbook(PrecativeLoanSendVo send, CmsPrecativeLoanRepay repay,
            FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, send, repay);
        accBook.setBusRaceId(cmsPrecativeLoanSendService.getFcBusRaceId(send.getPrecativeId(),
                SysRaceCode.NB_PRECATIVE_REPAYMENT, user.getId()));
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_F);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, PrecativeLoanSendVo send,
            CmsPrecativeLoanRepay repay) {
        CmsPrecativeLoanInfoVo loanInfo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        cmsPrecativeLoanSendService.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                // 业务规则定义里的组号不能为空
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            // 归还利息
            if (!CmsConstant.REPAY_TYPE_1.equals(repay.getRepayType())) {
                if (Integer.valueOf(FinacialConstant.ENTRY_TYPE_40).intValue() == entry.getEntryType().intValue()) {
                    /**
                     * 借：2120企业活期-借款方 1 贷：2120企业活期-委托方 4
                     */
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                        // 借：2120企业活期-借款方 1
                        List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(),
                                loanInfo.getCorpId(), repay.getCorpId(), entry.getSubjectCode());
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(repay.getCorpId());
                            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                    corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setDebitMoney(repay.getRepayAccrual());// 只计算计息
                        accbookDtl.setAccName(dto.getAccName());
                    } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                        // 贷：2120企业活期-委托方 4
                        List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(),
                                loanInfo.getCorpId(), send.getPrecativeId(), entry.getSubjectCode());
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(send.getPrecativeId());
                            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                    corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setAccName(dto.getAccName());
                        accbookDtl.setCreditMoney(repay.getRepayAccrual());// 只计算计息
                    }
                }
            }
            if (!CmsConstant.REPAY_TYPE_4.equals(repay.getRepayType())) {
                if (Integer.valueOf(FinacialConstant.ENTRY_TYPE_10).intValue() == entry.getEntryType().intValue()) {
                    /**
                     * 借：2120企业活期-借款方 1 贷：1125委托贷款-新开 2 借：2910委托存款-新开 3 贷：2120企业活期-委托方 4
                     */
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay
                            && (1 == groupNum || 3 == groupNum)) {
                        if (3 == groupNum) {
                            // 借：2910委托存款-新开 3
                            FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getDepositAcc());
                            accbookDtl.setAccount(send.getDepositAcc());
                            accbookDtl.setDebitMoney(repay.getRepayMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        } else {
                            // 借：2120企业活期-借款方 1
                            List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(),
                                    loanInfo.getCorpId(), repay.getCorpId(), entry.getSubjectCode());
                            if (EmptyUtils.isEmptyList(list)) {
                                CorpDTO corpDTO = corpService.get(loanInfo.getLoanOpp());
                                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                        corpDTO.getName());
                            }
                            FcAccDTO dto = list.get(0);
                            accbookDtl.setAccount(dto.getBlnctrAcc());
                            accbookDtl.setDebitMoney(repay.getRepayMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        }
                    } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay
                            && (2 == groupNum || 4 == groupNum)) {
                        if (2 == groupNum) {
                            // 贷：1125委托贷款-新开 2
                            FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());
                            accbookDtl.setAccount(send.getLoanAcc());
                            accbookDtl.setCreditMoney(repay.getRepayMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        } else {
                            // 贷：2120企业活期-委托方 4
                            List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(),
                                    loanInfo.getCorpId(), send.getPrecativeId(), entry.getSubjectCode());
                            if (EmptyUtils.isEmptyList(list)) {
                                CorpDTO corpDTO = corpService.get(loanInfo.getPrecativeId());
                                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL,
                                        corpDTO.getName());
                            }
                            FcAccDTO dto = list.get(0);
                            accbookDtl.setAccount(dto.getBlnctrAcc());
                            accbookDtl.setCreditMoney(repay.getRepayMoney());
                            accbookDtl.setAccName(dto.getAccName());
                        }
                    }
                }
            }
            accbookDtl.setSummaryContent(repay.getRmk());
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setCurCode(entry.getCurCode());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 归还输入数据校验
     */
    public void check(CmsPrecativeLoanRepayVo pre) {
        if (EmptyUtils.isEmpty(pre.getRepayDate())) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_REPAYDATE_NOT_NULL);
        }
        if (EmptyUtils.isEmpty(pre.getRepayMoney())) {
            // 还款金额不能为空
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DEADLINEDATE_NOT_NULL);
        }
    }
    public BaseDao<CmsPrecativeLoanRepay, String> getEntityDao() {
        return cmsPrecativeLoanRepayDao;
    }
    /**
     * 获取凭证号
     */
    protected String getPrecativeBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PRECATIVE_REPAY_NO, prefix);
    }
    /**
     * 查询归还账务数据
     */
    public DataTablesOutput getPrecativeRepayDataTables(DataTablesInput dataTablesInput, ICurrentUser user, List<String> corpAuth) {
//        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_REPAY_QUERY, user.getId());
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        return cmsPrecativeLoanRepayDao.getPrecativeRepayDataTables(dataTablesInput, corpAuth, opType, user);
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
     * 占用预算
     */
    public void batchEngross(CmsPrecativeLoanRepayVo entity, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(entity.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < entity.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(entity.getStatus());// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setItemCode(entity.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(entity.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(entity.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(DateUtils.getDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAYADDITION);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                if (entity.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                }
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                fbsBudgetProcessDTO.setInnerSign("1");
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAYADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
            fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAYADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode(), CmsConstant.APPROVED, user);
        }
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
    public void checkRepair(CmsPrecativeLoanRepayVo pre) {
        if (pre.getTotal() != null) {
            BigDecimal repayMoney = pre.getRepayMoney();// 归还金额
            BigDecimal repayAccrual = pre.getRepayAccrual();// 利息
            BigDecimal tt = repayMoney.add(repayAccrual);
            int i = 3;
            if ("4".equals(pre.getRepayType())) {
                i = pre.getTotal().compareTo(repayAccrual);
                if (i == -1 || i == 1) {
                    // 执行金额必须等于归还利息
                    throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DETAILMONEY_EQ_REPAYACCRUAL);
                }
            } else if ("3".equals(pre.getRepayType())) {
                i = pre.getTotal().compareTo(tt);
                if (i == -1 || i == 1) {
                    // 执行金额必须等于归还金额及所有利息
                    throw new ParameterException(
                            PrecativeMessageEnum.CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY_AND_ACCRUAL);
                }
            } else if ("2".equals(pre.getRepayType())) {
                i = pre.getTotal().compareTo(tt);
                if (i == -1 || i == 1) {
                    // 执行金额必须等于归还金额及归还金额利息
                    throw new ParameterException(
                            PrecativeMessageEnum.CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY_AND_REPAYACCRUAL);
                }
            } else if ("1".equals(pre.getRepayType())) {
                i = pre.getTotal().compareTo(repayMoney);
                if (i == -1 || i == 1) {
                    // 执行金额必须等于归还金额
                    throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_DETAILMONEY_EQ_REPAYMONEY);
                }
            }
        }
    }
    /**
     * 占用预算
     */
    public void otherBatchEngross(CmsPrecativeLoanRepayVo entity, ICurrentUser user, String sign) {
        if (!EmptyUtils.isEmptyArray(entity.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < entity.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(entity.getStatus());// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setItemCode(entity.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(entity.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(entity.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(DateUtils.getDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getPrecativeId());// 单位id 改成委托人
                fbsBudgetProcessDTO.setCurId(entity.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(sign);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                if (entity.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                }
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                if (entity.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(entity.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(entity.getProcessCurMoney()[i]);
                }
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
     * @return void
     * @Description 生成合同履约信息
     * @Param [repay, loanInfo, user]
     * @Date 2019/11/18 11:14
     * @Author fusb
     */
    public void createSegmentInfo(CmsPrecativeLoanRepay repay, CmsPrecativeLoanInfoVo loanInfo) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
            user = userService.getByUserId(repay.getUpdateBy());
        } else {
            user = userService.getByUserId(repay.getCreateBy());
        }
        if (StringUtils.isNotEmpty(loanInfo.getConId())
                && cmsPrecativeLoanSendService.conSegment().contains(CmsConstant.SEGMENT_LOAN)) {
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (null != conDTO && !CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                ConInfoRecordDTO dto = new ConInfoRecordDTO();
                dto.setConId(loanInfo.getConId());
                dto.setOuterId(repay.getId());
                dto.setOpMsg(CmsConstant.PRECATIVE_LOAN_REPAY);
                dto.setOpDate(repay.getRepayDate());
                dto.setOpNo(repay.getBillCode());
                dto.setOpMoney(repay.getRepayMoney());
                conCommonService.performanceConOp(dto, user);
            }
        }
    }
    /**
     * @return void
     * @Description 委托贷款终止对接的合同
     * @Param [loanInfo, repay, user]
     * @Date 2019/11/18 15:49
     * @Author fusb
     */
    public void terminateConOp(CmsPrecativeLoanInfoVo loanInfo, CmsPrecativeLoanRepay repay) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
            user = userService.getByUserId(repay.getUpdateBy());
        } else {
            user = userService.getByUserId(repay.getCreateBy());
        }
        if (StringUtils.isNotEmpty(loanInfo.getConId())) {
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (null != conDTO && !CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                ConInfoRecordDTO recDTO = new ConInfoRecordDTO();
                recDTO.setConId(loanInfo.getConId());
                recDTO.setOuterId(repay.getId());
                recDTO.setOpMsg(CmsConstant.PRECATIVE_LOAN_REPAY);
                recDTO.setOpDate(repay.getRepayDate());
                recDTO.setOpNo(repay.getBillCode());
                conCommonService.terminateConOp(recDTO, user);
            }
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
        if (cmsPrecativeLoanRepayDao.verifyCorpDisableByCorpIdTally(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        // 未全部还款的贷款
        if (cmsPrecativeLoanRepayDao.verifyCorpDisableByPrecativeId(corpId) > 0) {// 委托单位不允许禁用
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR1));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsPrecativeLoanRepayDao.verifyCorpDisableByCorpId(corpId) > 0) {// 委托单位不允许禁用
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR4));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsPrecativeLoanRepayDao.verifyCorpDisableByCorpId1(corpId) > 0) {// 还款待补录、还款利息待确认
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_PRECATIVE_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PRECATIVE_LOAN_INFO_ERROR2));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    /**
     * 查询还款待记账数据
     */
    public List<CmsPrecativeLoanRepayDTO> queryTallyPrecativeRepay() {
        return cmsPrecativeLoanRepayDao.queryTallyPrecativeRepay();
    }
}
