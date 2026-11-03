package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.api.ICmsLoanRepayService;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.api.ICmsProcessMsgService;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.dto.CmsLoanInfoDTO;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.tally.entity.TallyCmsLoanRepay;
import com.bytter.tally.vo.FcAccbookNotifyVO;
import com.bytter.tally.vo.FcVoucherCashVo;
import com.bytter.velocity.VelocityUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
@Service("enterpriseRepaymentService")
@Transactional
@Slf4j
public class EnterpriseRepaymentService implements ITaskService {
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @DubboReference(check = false)
    private ICmsLoanService iCmsLoanService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private ICmsProcessMsgService cmsProcessMsgService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IRatCpUrationService ucprationService;
    @DubboReference(check = false)
    private IGuaContractOccupServie contractOccupServie;
    @DubboReference(check = false)
    private IConService conService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private ProvideTallyService provideTallyService;
    @DubboReference
    private IFcVoucherCashService iFcVoucherCashService;
    @DubboReference(check = false)
    private IFcCashItemService iFcCashItemService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @DubboReference(check = false)
    private ICmsLoanRepayService iCmsLoanRepayService;
    /**
     * <b>功能介绍：</b>保存账务<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param repayId
     * @param opState
     * @param pageType
     * @param user
     * @version 1.0
     */
    public void saveAccbook(FcAccbookVoucherDTO fcAccbook, String repayId, String opState, String pageType, ICurrentUser user) {
        validata(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());//验证
        BigDecimal rushMoney = fcAccbook.getBookDtls().get(0).getRushMoney() == null ? BigDecimal.ZERO : fcAccbook.getBookDtls().get(0).getRushMoney();
        CmsLoanRepayDTO repay = iCmsLoanService.getLoanRepayById(repayId);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());  // fcAccbook 里面的accbook.getOrderNo 数据在哪里？
        if (NbconsoleConstant.OpType.INIT.get().equals(pageType)) {
            voucher = processVoucher(voucher, "0".equals(opState) ? OperateType.ADD : OperateType.SUBMIT);
        } else if (NbconsoleConstant.OpType.EDIT.get().equals(pageType)) {
            voucher = processVoucher(voucher, OperateType.MODIFY);
        }
        updateRepayment(repay, voucher, opState, user, rushMoney);
    }
    /**
     * <b>功能介绍：</b>改账打回<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param iuser
     * @version 1.0
     */
    public void editBackAccbook(FcAccbookVoucherDTO fcAccbook, ICurrentUser iuser) {
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(getOriType(), fcAccbook.getAccbook().getBusGlobalId());
        processVoucher(voucher, OperateType.DELETE);
        CmsLoanRepayDTO repay = iCmsLoanService.getRepayUniqueBy(voucher.getBookVoucher().getAccbook().getOrigVoucherNo());
        updateRepayment(repay, voucher, "2", iuser, BigDecimal.ZERO);
    }
    /**
     * <b>功能介绍：</b>复核账务<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param opState
     * @param busGlobalId
     * @version 1.0
     */
    public void checkAccbook(String opState, String busGlobalId, ICurrentUser user) {
        //获取记账凭证对象
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(getOriType(), busGlobalId);
        validata(voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());//验证
        validataBack(voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());//验证
        voucher.setUserCode(user.getId());
        voucher = processVoucher(voucher, "1".equals(opState) ? OperateType.CHECK : OperateType.BACK);
        CmsLoanRepayDTO repay = iCmsLoanService.getRepayUniqueBy(voucher.getBookVoucher().getAccbook().getOrigVoucherNo());
        updateRepayment(repay, voucher, "1".equals(opState) ? "4" : "3", user, BigDecimal.ZERO);
    }
    /**
     * <b>功能介绍：</b>构建凭证封装对象<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param userId
     * @return
     * @version 1.0
     */
    public VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        fcAccbook.getAccbook().setBusOriType(getOriType());
        fcAccbook.getAccbook().setOrigVoucherNo(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.setOriType(getOriType());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }
    /**
     * <b>功能介绍：</b>构建凭证传输对象<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repay
     * @param opType
     * @param user
     * @return
     * @version 1.0
     */
    public FcAccbookVoucherDTO createFcAccbook(TallyCmsLoanRepay repay, String opType, ICurrentUser user) {
        CmsLoanRepayDTO cmsRepay = iCmsLoanService.getLoanRepayById(repay.getId());//get(repay.getId(),false);
        CmsLoanSendDTO send = iCmsLoanService.getLoanSendByBillCode(cmsRepay.getSendCode());
        CmsLoanInfoDTO info = iCmsLoanService.getLoanInfoBybillCode(send.getLoanCode());
        FcAccbookVoucherDTO accbook = new FcAccbookVoucherDTO();
        FcAccbookDTO book = new FcAccbookDTO();
        List<FcAccbookDtlDTO> bookDtls = null;
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal accruedMoney = BigDecimal.ZERO;//冲预提金额
        if (!EmptyUtils.isEmpty(cmsRepay) && !NbconsoleConstant.OpType.CHECK.get().equals(opType)) {
            String orderNo = "";
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                orderNo = cmsRepay.getOrderNo();
            }
            if (CmsConstant.REPAY_TYPE_2.equals(cmsRepay.getRepayType())) {
                interest = iFcInterestService.calTellerMoneyInterest(cmsRepay.getLoanAcc(), cmsRepay.getRepayMoney(), orderNo, cmsRepay.getRepayDate(), user);
                accruedMoney = iFcInterestService.getRushPreDrawMoney(cmsRepay.getLoanAcc(), cmsRepay.getRepayMoney(), cmsRepay.getId());
            } else if (CmsConstant.REPAY_TYPE_3.equals(cmsRepay.getRepayType())) {
                interest = iFcInterestService.calTellerMoneyInterest(cmsRepay.getLoanAcc(), null, orderNo, cmsRepay.getRepayDate(), user);
                accruedMoney = iFcInterestService.getRushPreDrawMoney(cmsRepay.getLoanAcc(), null, cmsRepay.getId());
            } else if (CmsConstant.REPAY_TYPE_4.equals(cmsRepay.getRepayType())) {
                interest = iFcInterestService.calTellerMoneyInterest(cmsRepay.getLoanAcc(), null, orderNo, cmsRepay.getRepayDate(), user);
                accruedMoney = iFcInterestService.getRushPreDrawMoney(cmsRepay.getLoanAcc(), null, cmsRepay.getId());
                cmsRepay.setRepayMoney(BigDecimal.ZERO);
            }
            cmsRepay.setRepayAccrual(interest);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", getSysRaceCode());//企业贷款还款
        FcBusinessRuleDefineDTO ruleDefine = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {//记账
            bookDtls = getAccbookDtls(cmsRepay, ruleDefine, accruedMoney, info.getPurpose(), user.getId());
            accbook.setBookDtls(bookDtls);
            book.setTallyDate(cmsRepay.getRepayDate());
            book.setInterestStartDate(cmsRepay.getRepayDate());
            book.setVoucherType(ruleDefine.getVoucherType());
            accbook.setAccbook(book);
            accbook.setNetCode(innerAccountService.getByBlnctrAcc(cmsRepay.getLoanAcc()).getNetId());
        } else {
//			String BusGlobalId = fcAccbookService.getBookVOByOrderNo(cmsRepay.getOrderNo(),FinacialConstant.BOOLEAN_FALSE).getBusGlobalId();
            String BusGlobalId = iFcAccbookService.getBookVOByOrderNo(cmsRepay.getOrderNo(), FinacialConstant.BOOLEAN_FALSE).getBusGlobalId();
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                accbook = iFcAccbookService.getVoucher(getOriType(), BusGlobalId).getBookVoucher();
                if (DateUtils.dayDiffs(repay.getRepayDate(), accbook.getAccbook().getTallyDate()) != 0) {
                    cmsRepay.setRepayDate(repay.getRepayDate());
                    bookDtls = getAccbookDtls(cmsRepay, ruleDefine, accruedMoney, info.getPurpose(), user.getId());
                    accbook.setBookDtls(bookDtls);
                }
            }
        }
        return accbook;
    }
    /**
     * <b>功能介绍：</b>生成账务明细<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repay
     * @param ruleDefine
     * @param accruedMoney
     * @return
     * @version 1.0
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(CmsLoanRepayDTO repay, FcBusinessRuleDefineDTO ruleDefine, BigDecimal accruedMoney, String purpose, String userId) {
        FcAccbookDtlDTO accbookDtl = null;
        CmsLoanSendDTO send = iCmsLoanService.getLoanSendByBillCode(repay.getSendCode());
        String loanAcc = send.getLoanAcc();//贷款账号
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(loanAcc);
        Map<String, Object> variables = BeanUtils.beanToMap(repay);
        variables.putAll(BeanUtils.beanToMap(fcAccDTO));
        variables.put("tallyDate", repay.getRepayDate());
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        BigDecimal rushMoney = BigDecimal.ZERO;
        for (FcVoucherEntryDefineDTO define : ruleDefine.getVouchers()) {
            /**
             *  借：103001应收企业放款利息 参数控制开户单位(冲预提)
             贷：501001利息收入-放款 参数控制开户单位(冲预提)
             借：2120企业活期  单位(其他)
             贷：501001利息收入-放款  参数控制开户单位(结息)
             贷：1120企业放款  单位(其他)
             */
            accbookDtl = new FcAccbookDtlDTO();
            VoucherRenderControlDTO controlDTO = new VoucherRenderControlDTO();
            controlDTO.setSummaryEditable(true);
            accbookDtl.setControlVo(controlDTO);
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            com.bytter.system.api.dto.FcAccDTO acc = null;
            if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_10) {//其他
                if (define.getBalanceWay().intValue() == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue()) {//借2120企业活期
                    acc = new com.bytter.system.api.dto.FcAccDTO();
                    acc.setBlnctrAcc(StringUtils.isEmpty(repay.getRepayAcc().trim()) ? "" : repay.getRepayAcc().trim());
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                    accbookDtl.setDebitMoney(repay.getRepayMoney().add(repay.getRepayAccrual()));
                } else {//贷：1120企业放款  单位(其他)
                    acc = new com.bytter.system.api.dto.FcAccDTO();
                    acc.setBlnctrAcc(StringUtils.isEmpty(repay.getLoanAcc().trim()) ? "" : repay.getLoanAcc().trim());
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                    accbookDtl.setCreditMoney(repay.getRepayMoney());
                }
            } else if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_30) {//冲预提
                if (define.getBalanceWay().intValue() == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue()) {//借  应收企业利息 103001应收企业放款利息 CMS_ACCBOOK_103001_NOTEXISTS
                    acc = new com.bytter.system.api.dto.FcAccDTO();
                    if (fcAccDTO != null && EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {// 应收
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
//                        List<FcAccDTO> list = fcAccbookService.getInterestAcc(send.getCurCode(),send.getNetId(),send.getCorpId(),define.getSubjectCode(),"","",userId,"应收企业放款利息");
                        List<FcAccDTO> list = iFcAccbookService.getInterestAcc(send.getCurCode(), send.getNetId(), send.getCorpId(), define.getSubjectCode(), "", "", userId, "应收企业放款利息");
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(send.getNetId());
                            throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_103001_NOTEXISTS, corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        acc.setBlnctrAcc(dto.getBlnctrAcc());
                        acc.setSubjectId(define.getSubjectCode());
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(accruedMoney));
                    rushMoney = BigDecimal.ZERO.subtract(accruedMoney);
                } else {//贷  501001利息收入-放款  CMS_ACCBOOK_501001_NOTEXISTS
                    if (fcAccDTO != null && EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//利息收入
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        acc = new com.bytter.system.api.dto.FcAccDTO();
//                        List<FcAccDTO> list = fcAccbookService.getInterestAcc(send.getCurCode(),send.getNetId(),send.getCorpId(),define.getSubjectCode(),"","",userId,"利息收入-放款");
                        List<FcAccDTO> list = iFcAccbookService.getInterestAcc(send.getCurCode(), send.getNetId(), send.getCorpId(), define.getSubjectCode(), "", "", userId, "利息收入-放款");
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(send.getNetId());
                            throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_501001_NOTEXISTS, corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        acc.setBlnctrAcc(dto.getBlnctrAcc());
                        acc.setSubjectId(define.getSubjectCode());
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(accruedMoney));
                    rushMoney = BigDecimal.ZERO.subtract(accruedMoney);
                }
            } else if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_40) {//结息   贷  501001利息收入-放款  参数控制开户单位(结息)
                acc = new com.bytter.system.api.dto.FcAccDTO();
                if (fcAccDTO != null && EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {//利息收入
                    acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                } else {
//                    List<FcAccDTO> list = fcAccbookService.getInterestAcc(send.getCurCode(),send.getNetId(),send.getCorpId(),define.getSubjectCode(),"","",userId,"利息收入-放款");
                    List<FcAccDTO> list = iFcAccbookService.getInterestAcc(send.getCurCode(), send.getNetId(), send.getCorpId(), define.getSubjectCode(), "", "", userId, "利息收入-放款");
                    if (EmptyUtils.isEmptyList(list)) {
                        CorpDTO corpDTO = corpService.get(send.getNetId());
                        throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_501001_NOTEXISTS, corpDTO.getName());
                    }
                    FcAccDTO dto = list.get(0);
                    acc.setBlnctrAcc(dto.getBlnctrAcc());
                    acc.setSubjectId(define.getSubjectCode());
                    accbookDtl.setAccDTO(dto);
                }
                accbookDtl.setCreditMoney(repay.getRepayAccrual());
            }
            if (!EmptyUtils.isEmpty(accbookDtl.getAccDTO()) && EmptyUtils.isNotEmpty(accbookDtl.getAccDTO().getBlnctrAcc())) {
                com.bytter.system.api.dto.FcAccDTO fcAcc = innerAccountService.getByBlnctrAcc(accbookDtl.getAccDTO().getBlnctrAcc());
                if (!EmptyUtils.isEmpty(fcAcc)) {
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAcc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
            }
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setOrigVoucherId(repay.getId());
            //accbookDtl.setVoucherType(ruleDefine.getVoucherType());
            //	if(StringUtils.isNotEmpty(repay.getRmk())){
            String summaryContent = define.getSummary();
            if (!EmptyUtils.isEmpty(summaryContent)) {
                accbookDtl.setSummaryContent(parseSummary(variables, summaryContent));//摘要
            }
            if (EmptyUtils.isEmpty(accbookDtl.getSummaryContent())) {
                accbookDtl.setSummaryContent(purpose);
            }
//        	}else{
//        		accbookDtl.setSummaryContent(define.getSummary());
//        	}
//			FcVoucherCashVo fcVoucherCashVo;
            FcVoucherCashVo fcVoucherCashVo1 = new FcVoucherCashVo();
            accbookDtl.setRushMoney(rushMoney);
//			设置现金流项目
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
//				fcVoucherCashVo = fcVoucherCashService.getItemBySubject(accbookDtl.getAccDTO().getSubjectId(), null);
                FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(accbookDtl.getAccDTO().getSubjectId(), null);
                BeanUtils.copyProperties(fcVoucherCashDTO, fcVoucherCashVo1);
                if (null != fcVoucherCashVo1) {
                    accbookDtl.setCashFlux(fcVoucherCashVo1.getDebitItem());
                }
            } else if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) {
//				fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, accbookDtl.getAccDTO().getSubjectId());
                FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(null, accbookDtl.getAccDTO().getSubjectId());
                BeanUtils.copyProperties(fcVoucherCashDTO, fcVoucherCashVo1);
                if (null != fcVoucherCashVo1) {
                    accbookDtl.setCashFlux(fcVoucherCashVo1.getCreditItem());
                }
            }
            if (accbookDtl.getCreditMoney().doubleValue() != 0D || accbookDtl.getDebitMoney().doubleValue() != 0D) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    public String parseSummary(Map<String, Object> variables, String summary) {
        int formatIndex = summary.indexOf("${");
        if (-1 != formatIndex) {
            int formatEnd = summary.indexOf("}", formatIndex);
            String formatMark = summary.substring(formatIndex, formatEnd + 1);//日期内容
            String startsummary = summary.substring(0, formatIndex);//开始段内容
            String endsummary = summary.substring(formatEnd + 1);//结束段内容
            startsummary = VelocityUtils.processTemplate(startsummary, variables);
            endsummary = VelocityUtils.processTemplate(endsummary, variables);
            return startsummary + parseSummaryDate(variables, formatMark) + endsummary;
        } else {
            return VelocityUtils.processTemplate(summary, variables);
        }
    }
    public String pareseDateFormat(String summary) {
        String dateFormat = "yyyy-MM-dd";
        String pattern = ",";
        int formatIndex = summary.indexOf(pattern);
        if (-1 != formatIndex) {
            int formatEnd = summary.lastIndexOf("}");
            dateFormat = summary.substring(formatIndex + 1, formatEnd).trim();
            if (EmptyUtils.isEmpty(dateFormat)) {
                dateFormat = "yyyy-MM-dd";
            }
            dateFormat = dateFormat.replaceAll("mm", "MM");
            return dateFormat.trim();
        }
        return dateFormat;
    }
    public String parseSummaryDate(Map<String, Object> variables, String summary) {
        int formatIndex = summary.indexOf("{");
        if (-1 != formatIndex) {
            int formatEnd = summary.indexOf(",", formatIndex);
            String formatMark = summary.substring(formatIndex + 1, formatEnd);//日期字段
            String dateFormat = pareseDateFormat(summary);
            try {
                Date formatDate = (Date) variables.get(formatMark.trim());
                if (!EmptyUtils.isEmpty(formatDate)) {
                    String dateStr = DateUtils.format(formatDate, dateFormat);
                    return dateStr;
                }
            } catch (Exception e) {
                return summary;
            }
        }
        return summary;
    }
    /**
     * <b>功能介绍：</b>获取明细展示html<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param bookDtls
     * @return
     * @version 1.0
     */
    public String getDtlHtml(List<FcAccbookDtlDTO> bookDtls) {
        String html = "";
        int i = 0;
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            FcVoucherCashVo fcVoucherCashVo = new FcVoucherCashVo();
            String cashItemName = null;
            String cashItemId = null;
            for (FcAccbookDtlDTO dtl : bookDtls) {
                html += "<tr><td>";
                html += "<input type='text' name='bookDtls[" + i + "].summaryContent' class='layui-input' maxlength='50'  value='" + dtl.getSummaryContent() + "'>";
//				html += "<span>"+dtl.getSummaryContent()+"</span>";
//				html += "<div class='bt-biz-selectType' data-biz-name='summary"+i+"' data-search='true' data-url='${basePath}/finacial/paramconfig/btsummary/json' data-valuefield='summaryContent' data-textfield='summaryContent' data-name='bookDtls["+i+"].summaryContent' data-defaultvalue='"+dtl.getSummaryContent()+"' data-inputvalue ='inputvalue'></div>";
                html += "</td>";
                html += "<td align='center'>";
                html += "<input type='hidden' class='layui-input' name='bookDtls[" + i + "].account' value='" + dtl.getAccount() + "'>";
                html += "<span>" + dtl.getAccount() + "</span>";
                html += "</td>";
                html += "<td align='center'>";
                html += "<input type='hidden' name='bookDtls[" + i + "].accName' value='" + dtl.getAccName() + "'>";
                html += "<span>" + dtl.getAccName() + "</span>";
                html += "</td>";
                html += "<td align='center'>";
                html += "<input type='hidden' name='bookDtls[" + i + "].origVoucherNo' value='" + dtl.getOrigVoucherNo() + "'>";
                html += "<span>" + dtl.getOrigVoucherNo() + "</span>";
                html += "</td>";
                html += "<td class='textRight'>";
                html += "<input type='hidden' name='bookDtls[" + i + "].debitMoney' value='" + dtl.getDebitMoney() + "'>";
                html += "<span>" + NumberUtil.formatNumeric(dtl.getDebitMoney()) + "</span>";
                html += "</td>";
                html += "<td class='textRight'>";
                html += "<input type='hidden' name='bookDtls[" + i + "].creditMoney' value='" + dtl.getCreditMoney() + "'>";
                html += "<span>" + NumberUtil.formatNumeric(dtl.getCreditMoney()) + "</span>";
                html += "<input type='hidden' name='bookDtls[" + i + "].rushMoney' value='" + dtl.getRushMoney() + "'>";
                html += "</td>";
                if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || dtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
                    if (StringUtils.isNotEmpty(dtl.getCashFlux())) {
//						FcCashItem fcCashItem = fcCashItemService.get(dtl.getCashFlux());
                        FcCashItemDTO fcCashItemDTO = iFcCashItemService.get(dtl.getCashFlux());
                        if (null != fcCashItemDTO) {
                            cashItemName = fcCashItemDTO.getItemName();
                            cashItemId = fcCashItemDTO.getId();
                        }
                    } else {
                        com.bytter.system.api.dto.FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(dtl.getAccount());
//						fcVoucherCashVo = fcVoucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
                        FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
                        BeanUtils.copyProperties(fcVoucherCashDTO, fcVoucherCashVo);
                        if (null != fcVoucherCashVo) {
                            cashItemName = fcVoucherCashVo.getDebitItemName();
                            cashItemId = fcVoucherCashVo.getDebitItem();
                        }
                    }
                } else if (dtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 || dtl.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) {
                    if (StringUtils.isNotEmpty(dtl.getCashFlux())) {
//						FcCashItem fcCashItem = fcCashItemService.get(dtl.getCashFlux());
                        FcCashItemDTO fcCashItemDTO = iFcCashItemService.get(dtl.getCashFlux());
                        if (null != fcCashItemDTO) {
                            cashItemName = fcCashItemDTO.getItemName();
                            cashItemId = fcCashItemDTO.getId();
                        }
                    } else {
                        com.bytter.system.api.dto.FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(dtl.getAccount());
//						fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
                        FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
                        BeanUtils.copyProperties(fcVoucherCashDTO, fcVoucherCashVo);
                        if (null != fcVoucherCashVo) {
                            cashItemName = fcVoucherCashVo.getCreditItemName();
                            cashItemId = fcVoucherCashVo.getCreditItem();
                        }
                    }
                }
                if (StringUtils.isNotEmpty(cashItemName) && StringUtils.isNotEmpty(cashItemId)) {
                    html += "<td><span id=\"cashFlowItem" + i + "\"><input type=\"hidden\" value=\"" + cashItemId + "\") name=\"bookDtls['" + i + "'].cashFlux\">" + cashItemName + "</span></td>";
                } else {
                    html += "<td><span id=\"cashFlowItem" + i + "\"></span></td>";
                }
                cashItemName = null;
                cashItemId = null;
                html += "</tr>";
                i++;
            }
            String emergencySign = multiLangServiceI.getLang("table.fcAccbook.emergencySign");//加急标志
            String rmk = multiLangServiceI.getLang("table.fcAccbook.rmk");//备注
            html += "<tr>";
            html += "<td class='textRight'>" + emergencySign + "：</td>";
            html += "<td>";
            html += "<div class='bt-biz-dict' data-type='radio' data-biz-name='emergencySign' data-name='accbook.emergencySign' data-group='Accbook.EmergencySign' data-defaultvalue='G' data-disabled='true'></div>";
            html += "</td>";
            html += "<td class='textRight'>" + rmk + "：</td>";
            html += "<td colspan='4'>";
            html += "<input type='text' id='rmk' name='accbook.rmk' class='layui-input' style='width:100%;'>";
            html += "</td>";
            html += "</tr>";
        }
        return html;
    }
    /**
     * <b>功能介绍：</b>处理账务<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param voucher
     * @param opType
     * @version 1.0
     */
    public VoucherWrapperDTO processVoucher(VoucherWrapperDTO voucher, OperateType opType) {
        if (isAutoCheck()) { //获取自动复核员用户 setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        if (opType == OperateType.ADD) {
            return voucher = iFcAccbookService.saveVoucher(voucher);
        } else if (opType == OperateType.SUBMIT) {
            return voucher = iFcAccbookService.submitVoucher(voucher);
        } else if (opType == OperateType.CHECK) {
            return voucher = iFcAccbookService.checkVoucher(voucher);
        } else if (opType == OperateType.BACK) {//打回
            return voucher = iFcAccbookService.backVoucher(voucher);
        } else if (opType == OperateType.DELETE) {//删除
            return voucher = iFcAccbookService.deleteVoucher(voucher);
        } else if (opType == OperateType.MODIFY) {//改账
            return voucher = iFcAccbookService.submitVoucher(voucher);
        }
        return null;
    }
    /**
     * <br>
     * <b>功能：</b> 复核通过后利息处理 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    public void processInterest(CmsLoanRepayDTO repay, VoucherWrapperDTO voucher, ICurrentUser user, BigDecimal tellerMoney) {
        //复核通过
        BigDecimal repayMoney = repay.getRepayMoney();//还款金额
        if (CmsConstant.REPAY_TYPE_2.equals(repay.getRepayType())) {
            //处理利息及冲预提
            iFcInterestService.processTellerMoneyBalance(repay.getLoanAcc(), repayMoney, voucher.getBookVoucher().getAccbook().getOrderNo(), user, null, repay.getRepayDate());
            //iFcInterestService.processRushPredraw(repay.getLoanAcc(),user, repay.getId(), tellerMoney,voucher.getBookVoucher().getAccbook().getTallyDate());
        } else if (CmsConstant.REPAY_TYPE_3.equals(repay.getRepayType())) {
            //处理利息及冲预提
            iFcInterestService.processTellerMoneyBalance(repay.getLoanAcc(), null, voucher.getBookVoucher().getAccbook().getOrderNo(), user, null, repay.getRepayDate());
            //iFcInterestService.processRushPredraw(repay.getLoanAcc(),user, repay.getId(),null,voucher.getBookVoucher().getAccbook().getTallyDate());
        }
    }
    /**
     * <b>功能介绍：</b>更新业务状态<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repay
     * @param voucher
     * @param opState
     * @version 1.0
     */
    public void updateRepayment(CmsLoanRepayDTO repay, VoucherWrapperDTO voucher, String opState, ICurrentUser user, BigDecimal rushMoney) {
        CmsLoanSendDTO info = iCmsLoanService.getLoanSendByBillCode(repay.getSendCode());//发放信息
        BigDecimal repayMoney = iCmsLoanService.getSendRepayedMoney(repay.getSendCode());//总的还款金额
        if ("0".equals(opState)) {//暂存
            repay.setOrderNo(voucher.getBookVoucher().getAccbook().getOrderNo());
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_TEMP.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_92);
            repay.setTallyDate(voucher.getBookVoucher().getAccbook().getTallyDate());
            repay.setInterestStartDate(voucher.getBookVoucher().getAccbook().getInterestStartDate());
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_REPAY_TALLY_TEMP, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_REPAY_TALLY_TEMP, user);
            //待记账消息提醒完成
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, repay.getId(), user.getId());
        } else if ("1".equals(opState)) {//提交
            repay.setOrderNo(voucher.getBookVoucher().getAccbook().getOrderNo());
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_SUBMIT.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_93);
            repay.setTallyDate(voucher.getBookVoucher().getAccbook().getTallyDate());
            repay.setInterestStartDate(voucher.getBookVoucher().getAccbook().getInterestStartDate());
            //待记账消息提醒完成
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, repay.getId(), user.getId());
            if (isAutoCheck()) {
                BigDecimal num = new BigDecimal(-1);
                BigDecimal tellerMoney = rushMoney.multiply(num);
                processInterest(repay, voucher, user, tellerMoney);//利息处理
                CmsLoanRepayDTO repayDTO = new CmsLoanRepayDTO();
                if (info.getLoanMoney().compareTo(repayMoney.add(repay.getRepayMoney())) == 0) {
//					repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_FINISH.get());
//					repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                    cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
                    cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
                    BeanUtils.copyProperties(repay, repayDTO);
                    releaseGuaContract(repay);//释放担保合同
                    CmsLoanRepayDTO cmsLoanRepayDTO = iCmsLoanService.isFinishedRepay(repayDTO);
                    BeanUtils.copyProperties(cmsLoanRepayDTO, repay);
                    releaseMoney(repay);//占用授信时需释放授信额度
                    completeCheckNotificationInfo(voucher, user);
                    completeEditNotificationInfo(voucher, user);
                    // 处理对接的合同信息
                    toDealConInfo(repayDTO);
                } else {
                    cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
                    cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
                    repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_PASS.get());
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_34);
                    releaseMoney(repay);//占用授信时需释放授信额度
                    cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
                    cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
                }
            } else {
                cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
                cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
                completeEditNotificationInfo(voucher, user);
                completeCheckNotificationInfo(voucher, user);
                sendCheckNotificationInfo(voucher, user);
            }
        } else if ("2".equals(opState)) {//改账打回
            repay.setOrderNo("");
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_MODIFY_BACK.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_95);
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_EDITTALLY_BACK, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_EDITTALLY_BACK, user);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(repay.getRepayMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_LOAN_TALLY_APPLY, NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, repay.getCorpId());
        } else if ("3".equals(opState)) {//复核打回
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_CHECK_BACK.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_94);
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKBACK, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKBACK, user);
            completeEditNotificationInfo(voucher, user);
            sendBackNotificationInfo(voucher);
        } else if ("4".equals(opState)) {//复核通过
//			repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_PASS.get());
//			repay.setBizStatus(CmsConstant.BUSINESSSTATE_34);
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
            CmsLoanRepayDTO repayDTO = new CmsLoanRepayDTO();
            BeanUtils.copyProperties(repay, repayDTO);
            releaseGuaContract(repay);//释放担保合同
            CmsLoanRepayDTO cmsLoanRepayDTO = iCmsLoanService.isFinishedRepay(repayDTO);
            BeanUtils.copyProperties(cmsLoanRepayDTO, repay);
            releaseMoney(repay);//占用授信时需释放授信额度
            // 处理对接的合同信息
            toDealConInfo(repayDTO);
            BigDecimal num = new BigDecimal(-1);
            BigDecimal tellerMoney = rushMoney.multiply(num);
            processInterest(repay, voucher, user, tellerMoney);//利息处理
            completeCheckNotificationInfo(voucher, user);
            completeEditNotificationInfo(voucher, user);

        } else if ("-1".equals(opState)) {//记账打回
            repay.setOrderNo("");
            repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_96);
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_BACK, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "A3", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_BACK, user);
            //记账打回 cms_repayment_scheduledetailed 的是否已还款状态 需要改为 0
            String repaymentScheduleId = repay.getRepaymentScheduleId();
            iCmsLoanRepayService.setRepayMentScheduleIsRepay(repaymentScheduleId,"0");
        }
        iCmsLoanService.updateRepayEntity(repay);
    }
    /**
     * <b>功能介绍：</b>根据还款单号查找凭证信息<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param billCode
     * @return
     * @version 1.0
     */
    public VoucherWrapperDTO getFcAccbookTempInfo(String billCode) {
        FcAccbookDTO fcAccbook = iFcAccbookService.getAccBookVOByOrigVoucherNo(billCode);
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(getOriType(), fcAccbook.getBusGlobalId());
        return voucher;
    }
    public String getSysRaceCode() {
        return SysRaceCode.NB_ENTERPRISE_REPAYMENT.get();
    }
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.NB_ENTERPRISE_REPAYMENT.get();
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ENTERPRISELOAN_CHECK, notifyVO, user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ENTERPRISELOAN_EDIT, notifyVO);
        }
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 还款记账复核完成，释放授信额度
     */
    private void releaseMoney(CmsLoanRepayDTO repay) {
        CmsLoanSendDTO sendInfo = iCmsLoanService.getLoanSendByBillCode(repay.getSendCode());
        CmsLoanInfoDTO loanInfo = iCmsLoanService.getLoanInfoBybillCode(sendInfo.getLoanCode());
        if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit()) && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
            BigDecimal repayMoney = repay.getRepayMoney();
            if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, loanInfo.getId());
                urationDTO.setMoney(repayMoney);
                if (loanInfo.getLoanMoney().compareTo(repayMoney) == 0) {//借款金额等于还款总金额
                    urationDTO.setAllRelease(true);//标识为全部释放
                }
                ucprationService.releaseRation(urationDTO);
            }
        }
    }
    /**
     * 还款记账复核完成，释放担保合同
     */
    private void releaseGuaContract(CmsLoanRepayDTO repay) {
        CmsLoanSendDTO sendInfo = iCmsLoanService.getLoanSendByBillCode(repay.getSendCode());
        CmsLoanInfoDTO loanInfo = iCmsLoanService.getLoanInfoBybillCode(sendInfo.getLoanCode());
        CmsLoanRepayDTO repayDTO = new CmsLoanRepayDTO();
        BeanUtils.copyNotNullProperties(repay, repayDTO);
        if (iCmsLoanService.checkFinishedRepay(repayDTO)) {//还款完成
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(loanInfo.getId());
            contractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
        }
    }
    private void validata(String sendBillCode) {
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            CmsLoanRepayDTO repayInfo = iCmsLoanService.getCmsLoanRepayByBillCode(sendBillCode);
            if (repayInfo != null) {
                if ("48".equals(repayInfo.getBizStatus())) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                }
            }
        }
    }
    private void validataBack(String sendBillCode) {
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            CmsLoanRepayDTO repayInfo = iCmsLoanService.getCmsLoanRepayByBillCode(sendBillCode);
            if (repayInfo != null) {
                if (StringUtils.isNotEmpty(repayInfo.getBizStatus()) && "94".equals(repayInfo.getBizStatus()) || "95".equals(repayInfo.getBizStatus())) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                }
            }
        }
    }
    @Override
    @XxlJob("EnterpriseRepaymentService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, BaseDataConstant.LOANREPAY_TALLY);//内部贷款还款待记账提醒
        //查询待记账数据
        List<CmsLoanRepayDTO> dataList = iCmsLoanService.queryRepayTallyData();
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (CmsLoanRepayDTO repay : dataList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(repay.getRepayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_LOAN_TALLY_APPLY, NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, repay.getCorpId());
                log.info("您有一笔内部贷款凭证待记账，金额：" + repay.getRepayMoney());
                repay.setRemindSign(CommonConstant.SEND_SIGN_YES);//更改发送标识
            }
            iCmsLoanService.batchUpdateRepay(dataList);
        }
    }
    /**
     * @return void
     * @Description 对接的合同信息处理
     * @Param [repay, user]
     * @Date 2019/11/14 14:31
     * @Author fusb
     */
    public void toDealConInfo(CmsLoanRepayDTO repay) {
        CmsLoanSendDTO send = iCmsLoanService.getLoanSendByBillCode(repay.getSendCode());
        CmsLoanInfoDTO loan = iCmsLoanService.getLoanInfoBybillCode(send.getLoanCode());
        ConInfoDTO conDTO = conService.getConInfo("", loan.getId());
        if (!EmptyUtils.isEmpty(conDTO)) { // 如果合同终止不产生履约信息和终止信息
            // 业务发起人
            UserDTO user = null;
            if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
                user = userService.getByUserId(repay.getUpdateBy());
            } else {
                user = userService.getByUserId(repay.getCreateBy());
            }
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) { // 如果对接合同管理
                String segment = configService.getConfig(ConfigConstant.CMS, "loan_relate_con_segment").toFormatString();
                if (segment.contains(CmsConstant.SEGMENT_REPAY)) {  // 生成履约信息
                    ConInfoRecordDTO conReDTO = new ConInfoRecordDTO();
                    conReDTO.setConId(conDTO.getId());
                    conReDTO.setOuterId(repay.getId());
                    conReDTO.setOpMsg(CmsConstant.INNER_LOAN_REPAY);
                    conReDTO.setOpDate(repay.getRepayDate());
                    conReDTO.setOpNo(repay.getBillCode());
                    conReDTO.setOpMoney(repay.getRepayMoney());
                    conService.performanceConOp(conReDTO, user);
                }
                if (iCmsLoanService.checkFinishedRepay(repay)) { // 还款完成终止合同
                    ConInfoRecordDTO conReDTO = new ConInfoRecordDTO();
                    conReDTO.setConId(conDTO.getId());
                    conReDTO.setOuterId(repay.getId());
                    conReDTO.setOpMsg(CmsConstant.INNER_LOAN_REPAY);
                    conReDTO.setOpDate(DateUtils.getDate());
                    conReDTO.setOpNo(repay.getBillCode());
                    conService.terminateConOp(conReDTO, user);
                }
            }
        }
    }
}
