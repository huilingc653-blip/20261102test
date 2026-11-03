package com.bytter.finance.domain.loan.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
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
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.loan.dao.CmsLoanRepayDao;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanRepayTallyQueryVO;
import com.bytter.finance.dto.CmsLoanInfoDTO;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.finance.dto.CmsLoanRepayMoneyDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款还款表
 * @date 2017-08-03 10:35:09
 */
@WorkFlow(CmsWorkFlowConstant.CMS_LOAN_REPAY)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LOAN_REPAY)
@Service("cmsLoanRepayService")
@Transactional
public class CmsLoanRepayService extends BaseService<CmsLoanRepay, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private CmsLoanRepayDao cmsLoanRepayDao;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsContractService cmsContractService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private ICmsLoanService cmsLoanServiceImpl;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private ConCommonService conCommonService;
    @Autowired(required = false)
    IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @Resource
    private IMultiLangService multiLangServiceI;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

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
     * <b>功能介绍：</b>生成贷款发送票据号<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getLoanRepayBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_LOANREPAY_BILL_CODE, prefix);
    }

    /**
     * <b>功能介绍：</b>保存还款申请<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void saveLoanRepay(CmsLoanRepay loanRepay, ICurrentUser user) {
        if (loanRepay.getStatus() == 1) {// 提交
            loanRepay.setStatus(CmsConstant.COMMITAPPROVE);
            loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_41);
        } else {// 暂存
            loanRepay.setStatus(CmsConstant.TEMP);
            loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_40);
        }
        if (EmptyUtils.isEmpty(loanRepay.getLoanAcc())) {
            loanRepay.setLoanAcc("null");
        }
        if (EmptyUtils.isEmpty(loanRepay.getRepayAcc())) {
            loanRepay.setRepayAcc("null");
        }
        loanRepay.setBillCode(getLoanRepayBillCode(CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX));// 设置还款申请编号
        loanRepay.setCreateBy(user.getId());
        loanRepay.setCreateName(user.getUsername());
        if (loanRepay.getCreateDate() == null) {
            loanRepay.setCreateDate(new Date());
        }
        loanRepay.setOperatorCode(user.getId());
        loanRepay.setUpdateBy(user.getId());
        loanRepay.setUpdateName(user.getUsername());
        loanRepay.setUpdateDate(new Date());
        //loanRepay.setRepayway(loanRepay.getRepayType());
        super.saveEntity(loanRepay);
        CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(loanRepay.getSendCode());
        send.setBizStatus(CmsConstant.BUSINESSSTATE_33);// 发放记录改为还款中
        cmsLoanSendService.saveOrUpdate(send);
        if (loanRepay.getStatus() == CmsConstant.COMMITAPPROVE) {
            loanRepayApply(loanRepay, user);
            if (EmptyUtils.isNotEmpty(loanRepay.getRepaymentScheduleId())) {// 还款计划设置为已归还
                setRepayMentScheduleIsRepay(loanRepay.getRepaymentScheduleId(), CmsConstant.IS_TRUE);
            }
            msService.insertProcessMsg(loanRepay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT_CHECK, user);
            msService.insertProcessMsg(loanRepay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT_CHECK, user);
        } else {
            msService.insertProcessMsg(loanRepay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_APPLY_TEMP, user);
            msService.insertProcessMsg(loanRepay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_APPLY_TEMP, user);
        }
    }

    /**
     * <b>功能介绍：</b>更新还款申请<br>
     * <b>日期：</b>2017年8月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateLoanRepay(CmsLoanRepay loanRepay, ICurrentUser user) {
        CmsLoanRepay t = this.get(loanRepay.getId());
        if ("4".equals(loanRepay.getRepayType())) {
            t.setRepaymentScheduleId("");// 只还利息不占用还款计划
        }
        if (loanRepay.getStatus() == 1) {// 提交
            loanRepay.setStatus(CmsConstant.COMMITAPPROVE);
            loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_41);
        } else {// 暂存
            loanRepay.setStatus(CmsConstant.TEMP);
            loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_40);
        }
        if (EmptyUtils.isEmpty(loanRepay.getLoanAcc())) {
            loanRepay.setLoanAcc("null");
        }
        if (EmptyUtils.isEmpty(loanRepay.getRepayAcc())) {
            loanRepay.setRepayAcc("null");
        }
        loanRepay.setUpdateBy(user.getId());
        loanRepay.setUpdateName(user.getUsername());
        loanRepay.setUpdateDate(new Date());
        loanRepay.setRepayAcc(t.getRepayAcc());
        BeanUtils.copyNotNullProperties(loanRepay, t);
        super.saveOrUpdate(t);// 更新操作
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAY, t.getId(), user.getId());
//        CmsLoanSend cmsLoanSend = cmsLoanSendService.getLoanSendByBillCode(t.getSendCode());
//        cmsLoanSend.setRepayType(loanRepay.getRepayway());
//        cmsLoanSendService.saveOrUpdate(cmsLoanSend);// 更新发放表的还款方式
        if (t.getStatus() == CmsConstant.COMMITAPPROVE) {
            loanRepayApply(t, user);// 启动审批
            if (EmptyUtils.isNotEmpty(t.getRepaymentScheduleId())) {// 还款计划设置为未归还
                setRepayMentScheduleIsRepay(t.getRepaymentScheduleId(), CmsConstant.IS_TRUE);
            }
            msService.insertProcessMsg(t.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_CHECK, user);
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_CHECK, user);
        } else {
            msService.insertProcessMsg(t.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
        }
    }

    /**
     * <b>功能介绍：</b>利息确认打回<br>
     * <b>日期：</b>2017年10月27日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanRepay
     * @version 1.0
     */
    public void backLoanRepay(CmsLoanRepay loanRepay, ICurrentUser user) {
        loanRepay.setStatus(CmsConstant.BACK);// 更改为打回状态
        loanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_46);
        if (EmptyUtils.isNotEmpty(loanRepay.getRepaymentScheduleId())) {// 还款计划设置为未归还
            setRepayMentScheduleIsRepay(loanRepay.getRepaymentScheduleId(), CmsConstant.IS_FALSE);
        }
        msService.insertProcessMsg(loanRepay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_ISTSURE_BACK, user);
        msService.insertProcessMsg(loanRepay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_HANDLEBACK, CmsProcessMsgConstant.MSGA_ISTSURE_BACK, user);
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAY_CONFIRM, loanRepay.getId(), user.getId());
        this.saveOrUpdate(loanRepay);
    }

    /**
     * <br>
     * <b>功能：</b> 复核通过后利息处理 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    public void processInterest(CmsLoanRepay repay, String orderNo, ICurrentUser user) {
        // 复核通过
        BigDecimal repayMoney = repay.getRepayMoney();// 还款金额
        if (CmsConstant.REPAY_TYPE_2.equals(repay.getRepayType())) {
            // 处理利息及冲预提
            iFcInterestService.processTellerMoneyBalance(repay.getLoanAcc(), repayMoney, orderNo, user, null,
                    repay.getRepayDate());
        } else if (CmsConstant.REPAY_TYPE_3.equals(repay.getRepayType())) {
            // 处理利息及冲预提
            iFcInterestService.processTellerMoneyBalance(repay.getLoanAcc(), null, orderNo, user, null,
                    repay.getRepayDate());
        }
    }

    /**
     * <b>功能介绍：</b>利息确认通过<br>
     * <b>日期：</b>2017年10月27日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanRepay
     * @version 1.0
     */
    public void interetAffirmPass(CmsLoanRepay loanRepay, ICurrentUser iuser) {
        CmsLoanRepay repayInfo = this.get(loanRepay.getId(), true);
        cmsLoanRepayDao.clear();
        CmsLoanRepay repay = new CmsLoanRepay();
        BeanUtils.copyProperties(repayInfo, repay);
        if (loanRepay.getStatus() == 1) {// 通过
            CmsLoanSend sendInfo = cmsLoanSendService.getLoanSendByBillCode(repay.getSendCode());
            CmsLoanInfoVO loanInfo = getCmsLoanInfo(repay.getSendCode());
            repay.setRepayAccrual(loanRepay.getRepayAccrual());
            repay.setRepayType(loanRepay.getRepayType());
            repay.setStatus(CmsConstant.COMPELETOTAL);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            if (CmsConstant.REPAY_TYPE_4.equals(loanRepay.getRepayType())) {// 归还利息
                repay.setRepayMoney(new BigDecimal(0));
            }
            if (cmsContractService.isAccounting()) {// 是否记账
                // if (cmsContractService.isAutoAccounting()) {
                submitFcAccBook(sendInfo, repay, iuser);
                /*
                 * } else {
                 *
                 * }
                 */
                if (cmsContractService.isAutoAccounting()) { // 自动记账
                    // 如果对接合同管理，内部贷款为自动记账，利息确认后生成履约信息
                    this.createSegmentInfo(loanInfo, repay);
                }
            }
            if (!CmsConstant.REPAY_TYPE_1.equals(loanRepay.getRepayType())) {// 需要归还利息
                // 利息处理
                if (EmptyUtils.isNotEmpty(repay.getLoanAcc()) && EmptyUtils.isNotEmpty(repay.getRepayMoney().toString())
                        && repay.getRepayMoney().compareTo(new BigDecimal(0)) > 0
                        && !EmptyUtils.isEmpty(repay.getRepayDate())) {
                    processInterest(repay, repay.getOrderNo(), iuser);
                    // iFcInterestService.processTellerMoneyBalance(repay.getLoanAcc(), repay.getRepayMoney(), null,
                    // iuser, repay.getId(), DateUtils.addDay(repay.getRepayDate(), -1));
                }
            }
            CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(repay.getSendCode());
            if (isSendRepayOver(repay)) {// 本次发放是否还清
                send.setBizStatus(CmsConstant.BUSINESSSTATE_34);// 还款完成
                this.terminateConOp(loanInfo, repay); // 还款完成终止合同
                if (EmptyUtils.isNotEmpty(send.getProvideLoanAcc())) {
                    innerAccountService.cancelFcAcc(send.getLoanAcc(), repayInfo.getRepayDate(), iuser);// 贷款账号销户
                }
            } else {
                send.setBizStatus(CmsConstant.BUSINESSSTATE_33);// 还款中
            }
            cmsLoanSendService.saveOrUpdate(send);
            if (isRepayOver(repay)) {// 如果还款完成设置业务结束
                repay.setStatus(CmsConstant.FINISH);
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                setLoanInfoFinished(repay);// 贷款申请设置为业务结束
                setLoanSendFinished(repay);// 贷款发放设为业务结束
            }
            msService.insertProcessMsg(repay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_ISTSURE_PASS, iuser);
            msService.insertProcessMsg(repay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_ISTSURE_PASS, iuser);
            this.saveOrUpdate(repay);
            if (loanInfo.getIsHangCredit() != null && loanInfo.getIsHangCredit().equals(CmsConstant.IS_TRUE)) {// 挂接授信
                releaseMoney(repay, loanInfo);// 释放授信额度
            }
            if (isRepayOver(repay)) {
                // 判断是否挂接担保,释放担保合同
                if (loanInfo.getIsHangGuarantee() != null
                        && loanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {// 挂接担保
                    GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                    guaContractOccupDTO.setBusinessId(loanInfo.getId());
                    contractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
                }
            }
        } else {// 打回
            backLoanRepay(repay, iuser);
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAY_CONFIRM, repay.getId(), iuser.getId());
    }

    /**
     * <b>功能介绍：</b>启动还款流程<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void loanRepayApply(CmsLoanRepay repay, ICurrentUser user) {
        getStartHandler().handler(CmsWorkFlowConstant.CMS_LOAN_REPAY, repay.getId(), repay, repay.getCorpId(),
                user.getId());
    }

    /**
     * <b>功能介绍：</b>更新还款计划归还标识<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repaymentId
     * @version 1.0
     */
    public void setRepayMentScheduleIsRepay(String repaymentId, String isRepay) {
        RepaymentSchedule reps = repaymentScheduleService.get(repaymentId);
        reps.setIsRepay(isRepay);// 更新归还标识为已(未)归还
        repaymentScheduleService.updateEntity(reps);
    }

    /**
     * <b>功能介绍：</b>贷款发放设置为还款完成<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void setLoanSendFinished(CmsLoanRepay loanRepay) {
        CmsLoanSend cmsLoanSend = cmsLoanSendService.getLoanSendByBillCode(loanRepay.getSendCode());
        cmsLoanSend.setStatus(CmsConstant.FINISH);
        cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_48);
        cmsLoanSendService.saveOrUpdate(cmsLoanSend);
    }

    /**
     * <b>功能介绍：</b>贷款申请设置为业务完成<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void setLoanInfoFinished(CmsLoanRepay loanRepay) {
        CmsLoanSend cmsLoanSend = cmsLoanSendService.getLoanSendByBillCode(loanRepay.getSendCode());
        CmsLoanInfo loanInfo = cmsLoanInfoService.getCmsLoanInfoBybillCode(cmsLoanSend.getLoanCode());
        loanInfo.setStatus(CmsConstant.FINISH);
        loanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_110);
        cmsLoanInfoService.saveOrUpdate(loanInfo);
    }

    /**
     * <b>功能介绍：</b>验证还款计划是否有效<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repaymentId
     * @return
     * @version 1.0
     */
    public boolean validataRepayMent(String repaymentId) {
        RepaymentSchedule reps = repaymentScheduleService.get(repaymentId, true);
        if (EmptyUtils.isEmpty(reps)) {
            return false;
        } else {
            return !String.valueOf(CmsConstant.DELETED).equals(reps.getValid_Sign());
        }
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(CmsLoanRepay entity) {
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
    public void save(CmsLoanRepay entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsLoanRepay entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(CmsLoanRepay entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    public BaseDao<CmsLoanRepay, String> getEntityDao() {
        return cmsLoanRepayDao;
    }

    /**
     * <b>功能介绍：</b>还款申请修改列表数据<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getEditDataTables(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_REPAY_QUERY, userId);
        return cmsLoanRepayDao.listCmsLoanRepayEdit(input, corpAuth, userId);
    }

    /**
     * <b>功能介绍：</b>利息确认列表数据<br>
     * <b>日期：</b>2017年10月27日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param input
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listRepayInteretAffirm(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_INTERETAFFIRM_QUERY, userId);
        return cmsLoanRepayDao.listRepayInteretAffirm(input, corpAuth);
    }

    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput listRepayApproveData(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LOAN_REPAY,
                dataTablesInput);
        DataTablesOutput dp = cmsLoanRepayDao.dataTableApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }

    /**
     * <b>功能介绍：</b>判断是否还款完成<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public boolean isRepayOver(CmsLoanRepay loanRepay) {
        CmsLoanInfoVO loanInfo = getCmsLoanInfo(loanRepay.getSendCode());
        BigDecimal repayMoney = cmsLoanRepayDao.getRepayedMoney(loanInfo.getBillCode());// 已还款金额
        BigDecimal currentRepayMoney = loanRepay.getRepayMoney().add(repayMoney);// 当前还款金额
        return currentRepayMoney.compareTo(loanInfo.getLoanMoney()) >= 0;
    }

    /**
     * <b>功能介绍：</b>判断本次发放是否还款完成<br>
     * <b>日期：</b>2017年9月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanRepay
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public boolean isSendRepayOver(CmsLoanRepay loanRepay) {
        CmsLoanSend sendInfo = cmsLoanSendService.getLoanSendByBillCode(loanRepay.getSendCode());
        BigDecimal repayMoney = cmsLoanRepayDao.getSendRepayedMoney(loanRepay.getSendCode());
        BigDecimal currentRepayMoney = loanRepay.getRepayMoney().add(repayMoney);
        return currentRepayMoney.compareTo(sendInfo.getLoanMoney()) >= 0;
    }

    /**
     * <b>功能介绍：</b>获取发放申请的剩余还款金额<br>
     * <b>日期：</b>2017年8月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getRemainMoney(String sendCode, String repayCode) {
        return cmsLoanRepayDao.getRemainMoney(sendCode, repayCode);
    }

    /**
     * <b>功能介绍：</b>还款中记录数<br>
     * <b>日期：</b>2017年9月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public Integer getRepayingCount(String sendCode) {
        return cmsLoanRepayDao.getRepayingCount(sendCode);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
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
        CmsLoanRepay repay = this.get(businessKey, true);
        CmsLoanInfoVO loanInfo = getCmsLoanInfo(repay.getSendCode());
        // TODO 审批暂未获取用户信息
        if (result) {// 审批通过
            if (cmsContractService.isAccounting()) {// 记账
                if (cmsContractService.isAutoAccounting()) {// 自动流转到利息确认环节
                    repay.setStatus(CmsConstant.APPROVED);
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_43);
                    notificationService.send(businessKey, repay.getCorpId(),
                            NotificationConstant.CMS.LOAN_REPAY_CONFIRM, repay);
                } else {// 手工记账
                    repay.setStatus(CmsConstant.WAITTOTAL);
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_91);
                    // 新增记账待办
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("money", NumberUtil.formatNumeric(repay.getRepayMoney(), "#,##0.00"));
                    notificationService.sendToUserByResPermission(repay.getId(),
                            com.bytter.biz.finacial.api.constant.ResourceConstant.FCL_LOAN_TALLY_APPLY,
                            NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, repay.getCorpId());
                }
            } else {// 跳过利息确认环节
                repay.setStatus(CmsConstant.COMPELETOTAL);
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                isFinishedRepay(repay);
                if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit())
                        && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
                    releaseMoney(repay, loanInfo);// 释放授信额度
                }
                // 如果对接合同管理，且还款款履约，则生成还款履约信息
                this.createSegmentInfo(loanInfo, repay);
                if (isRepayOver(repay)) {
                    // 判断是否挂接担保,释放担保合同
                    if (loanInfo.getIsHangGuarantee() != null
                            && loanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {// 挂接担保
                        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                        guaContractOccupDTO.setBusinessId(loanInfo.getId());
                        contractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
                    }
                    // 如果对接合同管理，终止合同进行合同终止操作
                    this.terminateConOp(loanInfo, repay);
                }
                /*
                 * CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(repay.getSendCode());
                 * if(isSendRepayOver(repay)){//本次发放是否还清 send.setBizStatus(CmsConstant.BUSINESSSTATE_34);//还款完成 }else{
                 * send.setBizStatus(CmsConstant.BUSINESSSTATE_33);//还款中 } cmsLoanSendService.saveOrUpdate(send);
                 * if(isRepayOver(repay)){//如果还款完成设置业务结束 repay.setStatus(CmsConstant.FINISH);
                 * repay.setBizStatus(CmsConstant.BUSINESSSTATE_110); setLoanInfoFinished(repay);//贷款申请设置为业务结束
                 * setLoanSendFinished(repay);//贷款发放设为业务结束 }
                 */
            }
            msService.insertProcessMsg(repay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
            msService.insertProcessMsg(repay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
        } else {
            repay.setStatus(CmsConstant.BACK);// 不通过设为打回状态
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_47);
            if (EmptyUtils.isNotEmpty(repay.getRepaymentScheduleId())) {// 还款计划设置为未归还
                setRepayMentScheduleIsRepay(repay.getRepaymentScheduleId(), CmsConstant.IS_FALSE);
            }
            msService.insertProcessMsg(repay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            msService.insertProcessMsg(repay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
        }
        this.saveOrUpdate(repay);
    }

    /**
     * 释放授信额度
     */
    private void releaseMoney(CmsLoanRepay repay, CmsLoanInfoVO loanInfo) {
        BigDecimal repayMoney = repay.getRepayMoney();
        if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, loanInfo.getId());
            urationDTO.setMoney(repayMoney);
            if (loanInfo.getLoanMoney().compareTo(repayMoney) == 0) {// 借款金额等于还款总金额
                urationDTO.setAllRelease(true);// 标识为全部释放
            }
            ucprationService.releaseRation(urationDTO);
        }
    }

    /**
     * <b>功能介绍：</b>是否结束还款<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repay
     * @version 1.0
     */
    public void isFinishedRepay(CmsLoanRepay repay) {
        CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(repay.getSendCode());
        if (isSendRepayOver(repay)) {// 本次发放是否还清
            send.setBizStatus(CmsConstant.BUSINESSSTATE_48);// 还款完成
        } else {
            send.setBizStatus(CmsConstant.BUSINESSSTATE_33);// 还款中
        }
        cmsLoanSendService.saveOrUpdate(send);
        if (isRepayOver(repay)) {// 如果还款完成设置业务结束
            repay.setStatus(CmsConstant.FINISH);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            setLoanInfoFinished(repay);// 贷款申请设置为业务结束
            setLoanSendFinished(repay);// 贷款发放设为还款完成
        } else {
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_PASS.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_34);
        }
    }

    /**
     * <b>功能介绍：</b>记账处理<br>
     * <b>日期：</b>2017年9月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendInfo
     * @param repay
     * @version 1.0
     */
    public void submitFcAccBook(CmsLoanSend sendInfo, CmsLoanRepay repay, ICurrentUser user) {
        // 构建凭证传输对象
        FcAccbookVoucherDTO accbookVourcher = processAccbook(sendInfo, repay, user);
        // 记账提交暂存
        VoucherWrapperDTO voucher = cmsContractService.submit(accbookVourcher, user.getId(), "repay");
        // 设置记账返回数据
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        repay.setOrderNo(accbook.getOrderNo());
        repay.setVoucherNo(accbook.getVoucherNo());
        //TODO 临时登记还款记账相关信息，核算中‘记账日期’变更也需要同时变更 还款里的记账日期
        repay.setTallyDate(accbook.getTallyDate());
        repay.setInterestStartDate(accbook.getInterestStartDate());
        //TODO 注意 记账员代码不需要设置，统一由 核算去更新
    }

    /**
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(CmsLoanSend sendInfo, CmsLoanRepay repay, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsContractService
                .getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_ENTERPRISE_REPAYMENT.get(), user);
        entity = processTallyAccbook(sendInfo, repay, ruleDefine, user.getId());
        return entity;
    }

    /**
     * 设置凭证信息
     */
    public FcAccbookVoucherDTO processTallyAccbook(CmsLoanSend sendInfo, CmsLoanRepay repay,
                                                   FcBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        CmsLoanInfoDTO info = cmsLoanServiceImpl.getLoanInfoBybillCode(sendInfo.getLoanCode());
        List<FcAccbookDtlDTO> accbookDtls = null;
        String voucherType = ruleDefine.getVoucherType();// 获取凭证种类
        if (EmptyUtils.isEmpty(voucherType) || EmptyUtils.isEmpty(voucherType.trim())) {
            voucherType = iFcVoucherTypeService.getSysFcVoucherType().getId();// 获取系统默认凭证种类
        }
        accbookDtls = getAccbookDtls(sendInfo, repay, ruleDefine, info.getPurpose());
        accBook.setBusRaceId(
                cmsContractService.getFcBusRaceId(cmsLoanInfoService.getCenNetId(repay.getCorpId()).get("corpId"),
                        FinaBookConstant.SysRaceCode.NB_ENTERPRISE_REPAYMENT, userId));// 设置业务种类ID
        accBook.setVoucherType(voucherType);
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }

    /**
     * <b>功能介绍：</b>构建明细分录<br>
     * <b>日期：</b>2017年9月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendInfo
     * @param repay
     * @param ruleDefine
     * @return
     * @version 1.0
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(CmsLoanSend sendInfo, CmsLoanRepay repay,
                                                FcBusinessRuleDefineDTO ruleDefine, String purpose) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        cmsContractService.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay;
        String repayType = repay.getRepayType();// 还款类型
        BigDecimal repayMoney = new BigDecimal(0);
        if (CmsConstant.REPAY_TYPE_1.equals(repayType)) {
            repayMoney = repayMoney.add(repay.getRepayMoney());
        } else if (CmsConstant.REPAY_TYPE_4.equals(repayType)) {
            repayMoney = repayMoney.add(repay.getRepayAccrual());
        } else {
            repayMoney = repayMoney.add(repay.getRepayMoney()).add(repay.getRepayAccrual());
        }
        String cmsLoanAcc = sendInfo.getLoanAcc();// 贷款账号
        FcAccDTO loanAccDTO = innerAccountService.getByBlnctrAcc(cmsLoanAcc);
        String summaryContent = "";
        BigDecimal rushMoney = iFcInterestService.getRushPreDrawMoney(repay.getLoanAcc(), repay.getRepayMoney(),
                repay.getId());
        for (FcVoucherEntryDefineDTO entry : entrys) {
            summaryContent = purpose;
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCurCode(sendInfo.getCurCode());
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setSummaryContent(summaryContent);
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            // 借：103001应收企业放款利息 参数控制开户单位(冲预提)
            // 贷：501001利息收入-放款 参数控制开户单位(冲预提)
            // 借：2120企业活期 单位(其他)
            // 贷：501001利息收入-放款 参数控制开户单位(结息)
            // 贷：1120企业放款 单位(其他)
            balanceWay = entry.getBalanceWay().intValue();
            // if(EmptyUtils.isNotEmpty(repay.getRmk())){
            // }else if(!EmptyUtils.isEmpty(entry.getSummary()) &&
            // !EmptyUtils.isEmpty(entry.getSummary().trim())){//获取摘要
            // summaryContent = entry.getSummary();
            // }else{
            // summaryContent = "内部贷款还款";
            // }
            Map<String, String> cenNet = cmsLoanInfoService.getCenNetId(repay.getCorpId());// 网点信息
            // 冲预提金额
            if (FinacialConstant.ENTRY_TYPE_30 == entry.getEntryType().intValue()) {// 冲预提
                FcAccDTO interestAcc = null;
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) { // 借
                    if (EmptyUtils.isNotEmpty(loanAccDTO.getInterestRecPay())) {// 应收
                        interestAcc = innerAccountService.getByBlnctrAcc(loanAccDTO.getInterestRecPay());
                    } else {
                        if (null != entry.getSubjectCode()) {
                            List<com.bytter.biz.finacial.api.dto.FcAccDTO> list = fcAccbookService.getInterestAcc(
                                    sendInfo.getCurCode(), cenNet.get("corpId"), repay.getCorpId(),
                                    entry.getSubjectCode(), "", null, purpose, "应收企业放款利息");
                            if (EmptyUtils.isEmptyList(list)) {// 未维护应收企业放款利息
                                throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_103001_NOTEXISTS,
                                        cenNet.get("corpName"));
                            }
                            // todo:数据转换
                            com.bytter.biz.finacial.api.dto.FcAccDTO fcAccDTO = list.get(0);
                            interestAcc = BeanUtils.copyNewInstanceProperties(fcAccDTO, FcAccDTO.class);
                        }
                    }
                    accbookDtl.setDebitMoney(rushMoney.multiply(new BigDecimal(-1)));// 账号金额冲预提金额(正负相反)
                } else {
                    if (EmptyUtils.isNotEmpty(loanAccDTO.getInterestInExp())) {// 利息收入
                        interestAcc = innerAccountService.getByBlnctrAcc(loanAccDTO.getInterestInExp());
                    } else {
                        if (null != entry.getSubjectCode()) {
                            List<com.bytter.biz.finacial.api.dto.FcAccDTO> list = fcAccbookService.getInterestAcc(
                                    sendInfo.getCurCode(), cenNet.get("corpId"), repay.getCorpId(),
                                    entry.getSubjectCode(), "", null, purpose, "利息收入-放款账号");
                            if (EmptyUtils.isEmptyList(list)) {// 未维护利息收入-放款账号
                                throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_501001_NOTEXISTS,
                                        cenNet.get("corpName"));
                            }
                            // todo:数据转换
                            com.bytter.biz.finacial.api.dto.FcAccDTO fcAccDTO = list.get(0);
                            interestAcc = BeanUtils.copyNewInstanceProperties(fcAccDTO, FcAccDTO.class);
                        }
                    }
                    accbookDtl.setCreditMoney(rushMoney.multiply(new BigDecimal(-1)));// 账号金额冲预提金额(正负相反)
                }
                if (!EmptyUtils.isEmpty(interestAcc)) {
                    accbookDtl.setAccount(interestAcc.getBlnctrAcc());
                    accbookDtl.setAccName(interestAcc.getAccName());
                }
            } else { // 结息、其他
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {// 借
                    FcAccDTO loanAcc = iFcAccService.getAccDTOByAccount(sendInfo.getProvideLoanAcc());// 放款账号(企业活期)
                    if (EmptyUtils.isEmpty(loanAcc)) {// 放款账号不存在
                        throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_ISNOTTALLY);
                    }
                    accbookDtl.setAccount(loanAcc.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repayMoney);// 设置借方发生额
                    accbookDtl.setAccName(loanAcc.getAccName());
                } else { // 贷
                    FcAccDTO creditAccDTO = null;
                    if (FinacialConstant.ENTRY_TYPE_40 == entry.getEntryType().intValue()) {// 结息
                        if (!CmsConstant.REPAY_TYPE_1.equals(repayType)) {// 非还本金
                            if (EmptyUtils.isNotEmpty(loanAccDTO.getInterestInExp())) {// 利息收入
                                creditAccDTO = innerAccountService.getByBlnctrAcc(loanAccDTO.getInterestInExp());
                            } else {
                                if (null != entry.getSubjectCode()) {
                                    accbookDtl = new FcAccbookDtlDTO();
                                    List<com.bytter.biz.finacial.api.dto.FcAccDTO> list = fcAccbookService
                                            .getInterestAcc(sendInfo.getCurCode(), cenNet.get("corpId"),
                                                    repay.getCorpId(), entry.getSubjectCode(), "", null, purpose,
                                                    "利息收入-放款账号");
                                    if (EmptyUtils.isEmptyList(list)) {// 未维护利息收入-放款账号
                                        throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_501001_NOTEXISTS,
                                                cenNet.get("corpName"));
                                    }
                                    // todo:数据转换
                                    com.bytter.biz.finacial.api.dto.FcAccDTO fcAccDTO = list.get(0);
                                    creditAccDTO = BeanUtils.copyNewInstanceProperties(fcAccDTO, FcAccDTO.class);
                                }
                                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                                accbookDtl.setCurCode(sendInfo.getCurCode());
                                accbookDtl.setOrigVoucherNo(repay.getBillCode());
                                accbookDtl.setSummaryContent(summaryContent);
                            }
                            accbookDtl.setCreditMoney(repay.getRepayAccrual());// 设置贷方发生额(还款利息)
                        }
                    } else {// 其他
                        if (FinacialConstant.ENTRY_TYPE_10 == entry.getEntryType().intValue()
                                && !CmsConstant.REPAY_TYPE_4.equals(repayType)) {
                            creditAccDTO = iFcAccService.getAccDTOByAccount(sendInfo.getLoanAcc());// 贷款账号(企业放款)
                            if (EmptyUtils.isEmpty(creditAccDTO)) {// 贷款账号不存在
                                throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_ISNOTTALLY);
                            }
                        }
                        accbookDtl.setCreditMoney(repay.getRepayMoney());// 设置贷方发生额
                    }
                    if (!EmptyUtils.isEmpty(creditAccDTO)) {
                        accbookDtl.setAccount(creditAccDTO.getBlnctrAcc());
                        accbookDtl.setAccName(creditAccDTO.getAccName());
                    }
                }
            }
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0
                    || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }

    public DataTablesOutput getLoanTallyList(DataTablesInput input, String opType, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds("fcl:loan:tally:apply", userId);
        DataTablesOutput output = cmsLoanRepayDao.getLoanTallyList(input, opType, userId, corpAuth);
        List<CmsLoanRepayTallyQueryVO> data = output.getData();
        List<String> netIds = data.stream().map(CmsLoanRepayTallyQueryVO::getNetCode).collect(Collectors.toList());
        List<String> curIds = data.stream().map(CmsLoanRepayTallyQueryVO::getCurId).collect(Collectors.toList());
        List<String> corpIds = data.stream().map(CmsLoanRepayTallyQueryVO::getCorpId).collect(Collectors.toList());
        Map<String, String> corpNameMap = iCorpService.getNameByIds(corpIds);
        Map<String, String> netNameMap = iCorpService.getNameByNetIds(netIds);
        Map<String, String> curNameMap = iBtCurrencyService.getCurrencyNameByIds(curIds);

        data.forEach(a -> {
            if (corpNameMap != null) a.setCorpName(corpNameMap.get(a.getCorpId()));
            if (netNameMap != null) a.setNetName(netNameMap.get(a.getNetCode()));
            if (curNameMap != null) a.setCurName(curNameMap.get(a.getCurId()));
        });

        return output;
    }

    public CmsLoanRepayDTO getLoanRepayById(String id) {
        return cmsLoanRepayDao.getCmsLoanRepayById(id);
    }

    public CmsLoanRepayDTO getCmsLoanRepayByBillCode(String billCode) {
        return cmsLoanRepayDao.getCmsLoanRepayByBillCode(billCode);
    }

    /**
     * 获取还款金额状态为102的
     **/
    public BigDecimal getSendRepayedMoney(String sendCode) {
        BigDecimal repayMoney = cmsLoanRepayDao.getSendRepayedMoney(sendCode);
        return repayMoney;
    }

    /**
     * 查询还款待记账的数据
     *
     * @return
     */
    public List<CmsLoanRepayDTO> queryRepayTallyData() {
        return cmsLoanRepayDao.queryRepayTallyData();
    }

    /**
     * @return java.lang.String
     * @Description 查询信贷参数履约环节
     * @Param []
     * @Date 2019/11/13 19:31
     * @Author fusb
     */
    @Transactional(readOnly = true)
    public String conSegment() {
        return configService.getConfig(ConfigConstant.CMS, "loan_relate_con_segment").toFormatString();
    }

    /**
     * @return void
     * @Description 如果对接合同管理生成履约信息
     * @Param [loanInfo, repay, user]
     * @Date 2019/11/15 10:59
     * @Author fusb
     */
    public void createSegmentInfo(CmsLoanInfoVO loanInfo, CmsLoanRepay repay) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
            user = userService.getByUserId(repay.getUpdateBy());
        } else {
            user = userService.getByUserId(repay.getCreateBy());
        }
        if (StringUtils.isNotEmpty(loanInfo.getConId()) && this.conSegment().contains(CmsConstant.SEGMENT_REPAY)) {
            ConInfoRecordDTO dto = new ConInfoRecordDTO();
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                dto.setConId(conDTO.getId());
                dto.setOuterId(repay.getId());
                dto.setOpMsg(CmsConstant.INNER_LOAN_REPAY);
                dto.setOpDate(repay.getRepayDate());
                dto.setOpNo(repay.getBillCode());
                dto.setOpMoney(repay.getRepayMoney());
                conCommonService.performanceConOp(dto, user);
            }
        }
    }

    /**
     * @return void
     * @Description 终止合同操作
     * @Param [loanInfo, repay, user]
     * @Date 2019/11/15 11:06
     * @Author fusb
     */
    public void terminateConOp(CmsLoanInfoVO loanInfo, CmsLoanRepay repay) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
            user = userService.getByUserId(repay.getUpdateBy());
        } else {
            user = userService.getByUserId(repay.getCreateBy());
        }
        if (StringUtils.isNotEmpty(loanInfo.getConId())) {
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                ConInfoRecordDTO dto = new ConInfoRecordDTO();
                dto.setConId(loanInfo.getConId());
                dto.setOuterId(repay.getId());
                dto.setOpMsg(CmsConstant.INNER_LOAN_REPAY);
                dto.setOpDate(repay.getRepayDate());
                dto.setOpNo(repay.getBillCode());
                conCommonService.terminateConOp(dto, user);
            }
        }
    }

    /**
     * 单位禁用校验-待记账
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsLoanRepayDao.verifyCorpDisableByCorpIdTally(corpId) > 0) {// 待记账
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsLoanRepayDao.verifyCorpDisableByCorpId(corpId) > 0) { // 利息待确认
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_LOAN_INFO_ERROR1));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsLoanRepayDao.verifyCorpDisableByCorpId1(corpId) > 0) { // 未全部还清的贷款
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO.setErrorMsg(
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_LOAN_INFO_ERROR3));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }

    /**
     * <br>
     * <b>功能：</b> 查询贷款合同金额及还款金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    public List<CmsLoanRepayMoneyDTO> queryLoanRepayMoneyByLoanAcc(String loanAcc, Date endDate) {
        return cmsLoanRepayDao.queryLoanRepayMoneyByLoanAcc(loanAcc, endDate);
    }

    public CmsLoanRepayDTO getRepayUniqueBy(String origVoucherNo) {
        CmsLoanRepay repay = cmsLoanRepayDao.getUniqueBy("billCode", origVoucherNo);
        CmsLoanRepayDTO dto = new CmsLoanRepayDTO();
        BeanUtils.copyNotNullProperties(repay, dto);
        return dto;
    }
}
