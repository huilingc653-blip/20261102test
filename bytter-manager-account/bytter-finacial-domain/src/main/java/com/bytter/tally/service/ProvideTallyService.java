package com.bytter.tally.service;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.BudgetConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.api.ICmsProcessMsgService;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.AccountGrantDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.tally.api.constant.FinaBookConstant;
import com.bytter.tally.api.constant.FinacialConstant;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.api.constant.NbconsoleConstant;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * @author
 * @version 1.0
 * @Title: Service
 * @Description: 银行借款发放还款手工账
 * @date 2018-01-10
 */
@Service("provideTallyService")
@Transactional
public class ProvideTallyService {
    @DubboReference(check = false)
    private ITallyService tallyService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    /*@Autowired
    private FcBusRaceService busRaceService;*/
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICmsLoanService iCmsLoanService;
    /*@Autowired
    private FcAccbookDtlService fcAccbookDtlService;*/
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    /*@Autowired
    private FcAccbookService fcAccbookService;*/
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    /*@Autowired
    private FcAccbookExtService fcAccbookExtService;*/
    @DubboReference(check = false)
    private IFcAccbookExtService iFcAccbookExtService;
    @DubboReference(check = false)
    private INisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService iFbsBudgetProcessService;
    @DubboReference(check = false)
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private ICmsProcessMsgService cmsProcessMsgService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private IConService iConService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @Autowired
    private PrecativeTallyService precativeTallyService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private FinacialTallyRemindService finacialTallyRemindService;
    /**
     * 查询CmsProvideLoanSendDTO
     */
    @Transactional(readOnly = true)
    public CmsProvideSendDTO queryProvideSend(String billCode) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getCmsProvideLoanSendByBillCode(billCode);//ok
    }
    /**
     * 查询CmsProvideLoanSendDTO
     */
    @Transactional(readOnly = true)
    public CmsProvideLoanInfoDTO queryProvideInfo(String sendBillCode) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getLoanInfoVOByBillCode(sendBillCode);
    }
    @Transactional(readOnly = true)
    public BigDecimal getInfoRepayMoneySum(String infoBillCode) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getInfoRepayMoneySum(infoBillCode);
    }
    /**
     * 查询CmsPrecativeLoanRepayDTO
     */
    @Transactional(readOnly = true)
    public CmsProvideLoanRepayDTO queryProvideRepay(String billCode, String onlineSign) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getCmsProvideLoanRepayByBillCode(billCode, onlineSign);
    }
    // 查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput getProvideDateTable(DataTablesInput dataTablesInput, ICurrentUser user, String onlineSign, String confirm) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getProvideDateTable(dataTablesInput, user, onlineSign, confirm);
    }
    /**
     * 构建凭证
     */
    public FcAccbookPageVO createAccbook(String key, String type, String menuType, ICurrentUser user, String onlineSign) {
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            return processAccbook(key, type, user);
        } else if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_TOPREPAY.equals(menuType)) {
            return onlineProcessAccbook(key, type, user, onlineSign);//线上还款
        } else if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_DOWNREPAY.equals(menuType)) {
            return processAccbookDownRepay(key, type, user, onlineSign);//线下还款
        } else {
            return processAccbookConfirmRepay(key, type, user, onlineSign);//还款确认
        }
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlVo> getControlBeanEdit(int size) {
        List<VoucherRenderControlVo> controBeans = new ArrayList<VoucherRenderControlVo>();
        VoucherRenderControlVo controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlVo();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setAccountEditable(false);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        if (EmptyUtils.isEmpty(fcBusinessRuleDefineService)) {
            fcBusinessRuleDefineService = (IFcBusinessRuleDefineService) ApplicationContextUtil.getContext().getBean("fcBusinessRuleDefineServiceImpl");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;//ok
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageVO processAccbook(String billCode, String opType, ICurrentUser user) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        CmsProvideSendDTO send = this.queryProvideSend(billCode);//sqlmeiwenti
        CmsProvideLoanInfoDTO info = queryProvideInfo(send.getBillCode());
        List<VoucherRenderControlVo> controlBeans = getControlBeanEdit(2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_BANK_LOAN.get());//银行借款，不用改

            entity = processTallyAccbook(send, controlBeans, ruleDefine, user);//ok
            Date close = iFcDayCloseService.getDayCloseFinalDate(send.getCorpId());
            if (EmptyUtils.isNotEmpty(info.getAcrossSend()) && info.getAcrossSend().equals("1")) {
                if (send.getSendDate().compareTo(close) < 1 || send.getSendDate().compareTo(close) == 0) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(close);
                    calendar.add(Calendar.DATE, 1); //把日期往后增加一天,整数  往后推,负数往前移动
                    Date closeDate = calendar.getTime(); //这个时间就是日期往后推一天的结果
                    entity.getAccbook().setTallyDate(closeDate);
                    entity.getAccbook().setInterestStartDate(info.getPlDate());
                } else {
                    entity.getAccbook().setTallyDate(info.getPlDate());
                    entity.getAccbook().setInterestStartDate(info.getPlDate());
                }
            } else {
                entity.getAccbook().setTallyDate(send.getSendDate());
                entity.getAccbook().setInterestStartDate(send.getSendDate());
            }
        } else {
            if (EmptyUtils.isEmpty(send.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(send.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            BeanUtils.copyProperties(fcAccbookDTO, accbook);
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
            FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            BeanUtils.copyProperties(fcAccbookExtDTO, bookExt);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
        }
        return entity;//ok
    }

    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbook(CmsProvideSendDTO send, List<VoucherRenderControlVo> controlBeans,
                                               FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        //查询provideInfo信息
        CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());//sqlmeiwenti
        //凭证传输对象，用于页面显示
        FcAccbookPageVO entity = new FcAccbookPageVO();
        //账务凭证保存对象
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, controlBeans, info, send, user);//记账时的会计分录构造ok
        accBook.setRmk(info.getPlPurpose());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(send.getCorpId(), FinaBookConstant.SysRaceCode.NB_BANK_LOAN, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);//ok
        return entity;
    }

    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, ConstantBean<String> sysBean, String userId) {
        CorpDTO corp = iCorpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysBean.get());
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!StringUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(sysBean.get());
            tempRace.setRaceName(sysBean.getName());
            tempRace.setSysRaceCode(sysBean.get());
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysBean.get());
            FcBusRaceDTO fcBusRaceDTO1 = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO1, tempRace);
        }
        return tempRace;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlVO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans,
                                               CmsProvideLoanInfoDTO loanInfo, CmsProvideSendDTO send, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                groupNum = entry.getGroupNum().intValue();
                /**
                 * 借：1020银行活期存款 网点
                 * 贷：2110银行借款 网点
                 */
                VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                    if (null != entry.getSubjectCode()) {
                        bean.setAccountEditable(true);
                        accbookDtl.setDebitMoney(send.getMoney());
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                    }
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                    bean.setCreateAccountAble(true);
                    accbookDtl.setCreditMoney(send.getMoney());
                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                }
                accbookDtl.setCurCode(loanInfo.getCurId());
                accbookDtl.setOrigVoucherNo(send.getBillCode());
                accbookDtl.setOrigVoucherId(send.getId());
                accbookDtl.setSummaryContent(loanInfo.getPlPurpose());
                accbookDtls.add(accbookDtl);
                accbookDtl.setControlVo(bean);
            }
        }
        return accbookDtls;
    }
    /**
     * 功能：还款时的会计分录构造
     */
    public List<FcAccbookDtlVO> getRepayAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans,
                                                    CmsProvideLoanRepayDTO repay, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        CmsProvideSendDTO send = iCmsLoanService.getCmsProvideLoanSendByBillCode(repay.getSendCode());
        //CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());
        FcAccbookDtlVO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            accbookDtl.setSummaryContent(EmptyUtils.isEmpty(entry.getSummary()) ? repay.getRmk() : entry.getSummary());
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo beanNum = controlBeans.get(groupNum - 1);
            /**
             * 借：2110银行借款 网点 1
             * 借：521004利息支出-银行借款 3
             * 贷：1020银行活期存款 网点 2
             */
            /**
             * 本金
             * 借    x
             * 贷    x
             *
             * 没本金
             * 贷	x
             * 借     x
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repay.getRepayMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {//利息支持
                    List<FcAccDTO> dtos = innerAccountService.listAccountByParams(send.getCurCode(), repay.getCorpId(), repay.getCorpId(), entry.getSubjectCode());//利息支出-银行借款
                    if (EmptyUtils.isEmptyList(dtos)) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);//结算中心未开设利息支出-银行借款
                    }
                    FcAccDTO dto = dtos.get(0);//
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setCreditMoney(repay.getRepayAccrual());
                    accbookDtl.setAccName(dto.getAccName());
                }
            } else {
                FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getBlnctrAcc());//发放时使用的银行活期帐号 (本金) 记账为0会自动过滤掉
                accbookDtl.setAccount(dto.getBlnctrAcc());
                accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getRepayAccrual()));//利息加本金
                accbookDtl.setAccName(dto.getAccName());
            }
            accbookDtl.setCurCode(send.getCurCode());//使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setOrigVoucherId(repay.getId());
            accbookDtl.setSummaryContent(repay.getRmk());
            accbookDtl.setControlVo(beanNum);
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            @Override
            public int compare(FcVoucherEntryDefineDTO o1,
                               FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String billCode, String menuType, ICurrentUser user, String onlineSign, String confirm) {
        check(fcAccbook);
        boolean isAutoCheck = this.isAutoCheck();
        String opType = fcAccbook.getOpType().name();
        //开户
        validata(billCode, billCode, onlineSign);
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, billCode);
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        voucher = iFcAccbookService.submitVoucher(voucher);
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            updateEntity(voucher, billCode, isAutoCheck, opType, user, isAutoCheck);
        } else {
            updateRepayEntity(voucher, billCode, isAutoCheck, opType, user, confirm, onlineSign);
        }
        // 如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, user);
        completeCheckNotificationInfo(voucher, user);
        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, user);
        }
    }
    private void updateEntity(VoucherWrapperDTO voucher, String key, boolean flag, String opType, ICurrentUser user, boolean isAutoCheck) {
        CmsProvideSendDTO entity = this.queryProvideSend(key);
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
        cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_SEND_TALLY_SUBMIT, user);
        if (flag) { //获取自动复核员用户 setCheckUser(voucher);
            entity.setStatus(102);
            entity.setBizStatus("42");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKOK, user);
            this.handleCon(entity, user);//合同履约处理
        } else {
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        }
        entity.setOrderNo(accbook.getOrderNo());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setBookId(accbook.getId());
        entity.setLoanAcc(bookDtls.get(1).getAccount());//设置银行账号
        entity.setBlnctrAcc(bookDtls.get(0).getAccount());
        iCmsLoanService.updateProvide(entity, user);
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            String netId = corpService.getNetByCorpId(entity.getCorpId()).getNetId();
            List<String> users = precativeTallyService.getAutoCheckUser(netId);
            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(1).getAccount());
            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
        }
        notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, entity.getId(), user.getId());
        //cmsProcessMsgService.insertProcessMsg(entity.getBillCode(),"B2", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_SEND_TALLY_SUBMIT, user);
    }
    private void updateRepayEntity(VoucherWrapperDTO voucher, String key, boolean flag, String opType, ICurrentUser user, String confirm, String onlineSign) {
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        CmsProvideLoanRepayDTO entity = this.queryProvideRepay(key, onlineSign);
        if (flag) { //获取自动复核员用户 setCheckUser(voucher);
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
            cmsProcessMsgService.insertProcessMsg(entity.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
            if (EmptyUtils.isNotEmpty(onlineSign) && "1".equals(onlineSign)) {
                entity.setStatus(CmsConstant.FLOWWAIT);//使用流程等待状态
                entity.setBizStatus(CmsConstant.BUSINESSSTATE_58);//还款待确认扣款
            } else if (EmptyUtils.isNotEmpty(onlineSign) && "0".equals(onlineSign)) {
                entity.setStatus(CmsConstant.COMPELETOTAL);
                entity.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            }/*else{
				entity.setStatus(CmsConstant.COMPELETOTAL);
				entity.setBizStatus(CmsConstant.BUSINESSSTATE_48);
			}*/
            if (EmptyUtils.isNotEmpty(onlineSign) && "1".equals(onlineSign)) {//线上还款
                /**数据推送到头寸调拨*/
                this.createInsAllocationReq(entity, user);
            } else {//线下还款时才处理合同
                this.handleContract(entity, user);//合同履约处理
                this.endContract(entity, user);
                this.releasePledge(entity, user);//抵质押物额度释放
            }
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
            cmsProcessMsgService.insertProcessMsg(entity.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
        } else {
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
            entity.setOnlineSign(onlineSign);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_93);
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
            cmsProcessMsgService.insertProcessMsg(entity.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_REPAY_TALLY_SUBMIT, user);
        }
        if (EmptyUtils.isNotEmpty(onlineSign) && "1".equals(onlineSign)) {
            List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
            entity.setOnlineOtherAccCharge(bookDtls.get(bookDtls.size() - 1).getAccount());
        }
        entity.setOrderNo(accbook.getOrderNo());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setVoucherNo(accbook.getVoucherNo());
        iCmsLoanService.updateProvideRepay(entity, user);
        if (EmptyUtils.isNotEmpty(onlineSign) && !"1".equals(onlineSign)) {
            iCmsLoanService.provideIsRepayComplete(entity.getId(), user);//线下还款完成的数据处理
        }
        notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, entity.getId(), user.getId());
    }
    /**
     * 内部帐号开户
     */
    private void createFcAcc(List<FcAccbookDtlDTO> bookDtls, ICurrentUser user, String opType, String billCode) {
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            for (FcAccbookDtlDTO dtl : bookDtls) {
                com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtl.getAccDTO();
                if (!EmptyUtils.isEmpty(accDTO)) {
                    if ("SUBMIT".equals(opType)) {
                        accDTO.setAutoCheck(true);
                        // todo:数据转换
                        FcAccDTO fcAcc = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);
                        iInnerAccountService.saveOrUpdate(fcAcc, user);
                        dtl.setAccDTO(null);
                        FcAccDTO newAcc = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        CmsProvideSendDTO send = this.queryProvideSend(billCode);
                        List<String> users = new ArrayList<>();
                        users.add(user.getId());
                        users.add(send.getCreateBy());
                        users.add(send.getInfoCreateBy());
                        users.add(send.getInfoUpdateBy());
                        setAccountGrant(newAcc.getId(), newAcc.getCorpId(), users);
                    } else {
                        FcAccDTO account = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        BeanUtils.copyNotNullProperties(accDTO, account);
                        iInnerAccountService.updateFcAcc(account);
                    }
                }
            }
        }
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 提交前数据校验
     */
    private void check(FcAccbookVoucherDTO fcAccbook) {
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getVoucherType())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getInterestStartDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getTallyDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getEmergencySign())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getBusRaceId())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
        }
        for (FcAccbookDtlDTO accbookDtl : fcAccbook.getBookDtls()) {
            if (EmptyUtils.isEmpty(accbookDtl.getAccount())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
            }
            if (EmptyUtils.isEmpty(accbookDtl.getSummaryContent())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
            }
        }
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String billCode, ICurrentUser user, String opType, String onlineSign, String menuType) {
        check(fcAccbook);
        validata(billCode, billCode, onlineSign);
        CmsProvideSendDTO entity = null;
        CmsProvideLoanRepayDTO repay = null;
        //开户
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, billCode);
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        voucher = iFcAccbookService.saveVoucher(voucher);
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            entity = this.queryProvideSend(billCode);
            entity.setOrderNo(accbook.getOrderNo());
            entity.setTallyCode(accbook.getOperatorCode());
            entity.setBookId(accbook.getId());
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
            iCmsLoanService.updateProvide(entity, user);
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_SEND_TALLY_TEMP, user);
            notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, entity.getId(), user.getId());
        } else {
            repay = this.queryProvideRepay(billCode, onlineSign);
            repay.setOrderNo(accbook.getOrderNo());
            repay.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
            repay.setTallyCode(accbook.getOperatorCode());
            repay.setOnlineSign(onlineSign);
            iCmsLoanService.updateProvideRepay(repay, user);
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_REPAY_TALLY_TEMP, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_REPAY_TALLY_TEMP, user);
            notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, repay.getId(), user.getId());
        }
    }
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        //设置记账员ID
        fcAccbook.getAccbook().setOperatorCode(userId);
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher, Integer type, String menuType) {
        if (type != null) {
            voucher.getAccbook().setBusOriType(type);
        }
        createVoucher(voucher, menuType);
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
            if (StringUtils.isEmpty(bookDtl.getSummaryContent())
                    && StringUtils.isEmpty(bookDtl.getAccount())
                    && null == bookDtl.getDebitMoney()
                    && null == bookDtl.getCreditMoney()) {
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
    private void createVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        voucher.getAccbook().setBusOriType(getOriType(menuType));
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String menuType) {
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            return FinaBookConstant.BusOriType.NB_BANK_LOAN.get();
        } else if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_TOPREPAY.equals(menuType)) {
            return FinaBookConstant.BusOriType.NB_BANK_REPAYMENT_ONLINE.get();
        } else {
            return FinaBookConstant.BusOriType.NB_BANK_REPAYMENT.get();
        }
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String billCode,
                              ICurrentUser user, String menuType, String onlineSign) {
        createVoucher(fcAccbook, menuType);
        String opType = fcAccbook.getOpType().name();
        CmsProvideSendDTO send = null;
        CmsProvideLoanRepayDTO repay = null;
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getCorpId());
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user);
        completeCheckNotificationInfo(Voucher, user);
        if ("MODIFY".equals(opType)) {
            validata(billCode, billCode, onlineSign);
            Map<String, Object> map = new HashMap<String, Object>();
            if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
                String account = fcAccbook.getBookDtls().get(1).getAccount();
                send = queryProvideSend(billCode);
                send.setBookId(null);
                send.setOrderNo(null);
                send.setTallyCode(null);
                send.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
                send.setBizStatus(CmsConstant.BUSINESSSTATE_95);
                iFcAccbookService.deleteVoucher(Voucher);
                iCmsLoanService.updateProvide(send, user);
                innerAccountService.cancelFcAcc(account, fcAccbook.getAccbook().getTallyDate(), user);// 贷款账号销户
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_SEND_EDITTALLY_BACK, user);
                map.put("money", NumberUtil.formatNumeric(send.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(send.getId(), ResourceConstant.FCL_PROVIDE_TALLY_APPLY, NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, map, send.getCorpId());
            } else {
                repay = queryProvideRepay(billCode, onlineSign);
                send = queryProvideSend(repay.getSendCode());
                repay.setOrderNo(null);
                repay.setOnlineSign(onlineSign);
                repay.setTallyCode(null);
                repay.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_95);
                iFcAccbookService.deleteVoucher(Voucher);
                iCmsLoanService.updateProvideRepay(repay, user);
                cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_EDITTALLY_BACK, user);
                cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_EDITTALLY_BACK, user);
                if ("1".equals(onlineSign)) {
                    map.put("money", NumberUtil.formatNumeric(repay.getOnlineMoney(), "#,##0.00"));
                } else {
                    map.put("money", NumberUtil.formatNumeric(repay.getRepayAccrual(), "#,##0.00"));
                }
                notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_PROVIDE_TALLY_APPLY, NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, map, send.getCorpId());
            }
        } else if ("CHECK".equals(opType)) {
            validataBack(billCode, billCode, onlineSign);
            if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
                send = queryProvideSend(billCode);
                send.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
                send.setBizStatus(CmsConstant.BUSINESSSTATE_94);
                iCmsLoanService.updateProvide(send, user);
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKBACK, user);
            } else {
                repay = queryProvideRepay(billCode, onlineSign);
                repay.setOnlineSign(onlineSign);
                repay.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_94);
                iCmsLoanService.updateProvideRepay(repay, user);
                cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKBACK, user);
                cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKBACK, user);
            }
            iFcAccbookService.backVoucher(Voucher);
            sendBackNotificationInfo(Voucher);
        } else if ("BACK".equals(opType)) {//记账打回
            String backReason = fcAccbook.getBookExt().getBackReason();
            if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
                send = queryProvideSend(billCode);
                send.setBookId(null);
                send.setOrderNo(null);
                send.setTallySign(null);
                send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
                send.setBizStatus(CmsConstant.BUSINESSSTATE_96);
                send.setRefuseWhy(backReason);
                iCmsLoanService.updateProvide(send, user);
                String isBudget = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue();
                if ("1".equals(isBudget)) {//释放预算
                    CmsProvideLoanInfoDTO info = queryProvideInfo(send.getBillCode());
                    if ("1".equals(info.getAcrossSend())) {
                        //this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION ,BudgetConstant.PROCESS_SYSNAME_CMS, send.getLoanCode());
                        iFbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION, BudgetConstant.PROCESS_SYSNAME_CMS, send.getLoanCode());
                    }
                }
                //this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND ,BudgetConstant.PROCESS_SYSNAME_CMS, send.getBillCode());
                iFbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS, send.getBillCode());
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_SEND_TALLY_BACK, user);
                finacialTallyRemindService.deleteByBusId(send.getId());
                notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, send.getId(), user.getId());
            } else {
                repay = queryProvideRepay(billCode, onlineSign);
                repay.setOrderNo(null);
                repay.setOnlineSign(onlineSign);
                repay.setTallyCode(null);
                repay.setTallySign(null);
                repay.setRefuseWhy(backReason);
                repay.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_96);
                iCmsLoanService.updateProvideRepay(repay, user);
                String isBudget = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue();
                if ("1".equals(isBudget)) {//修改预算
                    //this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY ,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode());
                    iFbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode());
                }
                cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_BACK, user);
                cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_REPAY_TALLY_BACK, user);
                finacialTallyRemindService.deleteByBusId(repay.getId());
                notificationService.complete(NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, repay.getId(), user.getId());
            }
        }
    }
    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id,
                              ICurrentUser user, String onlineSign, String menuType) {
        createVoucher(fcAccbook, menuType);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user, onlineSign, menuType);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String billCode, ICurrentUser user, String onlineSign, String menuType) {
        //TODO 复核成功后需要修改send表status 为102  bizStatus 42
        CmsProvideSendDTO entity = null;
        CmsProvideLoanRepayDTO repay = null;
        validata(billCode, billCode, onlineSign);
        validataBack(billCode, billCode, onlineSign);
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            entity = this.queryProvideSend(billCode);
            Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
            iFcAccbookService.checkVoucher(Voucher);
            FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
            entity.setVoucherNo(accbook.getVoucherNo());
            entity.setStatus(CmsConstant.COMPELETOTAL);
            entity.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
            cmsProcessMsgService.insertProcessMsg(entity.getBillCode(), "B2", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKOK, user);
            List<String> users = new ArrayList<>();
            users.add(user.getId());
            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(Voucher.getBookVoucher().getBookDtls().get(1).getAccount());
            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
        } else {
            repay = this.queryProvideRepay(billCode, onlineSign);
            CmsProvideLoanInfoDTO info = queryProvideInfo(repay.getSendCode());
            BigDecimal repaySum = getInfoRepayMoneySum(info.getBillCode()).add(repay.getRepayMoney());//累计已还金额+本次还款金额
            iFcAccbookService.checkVoucher(Voucher);
            FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
            repay.setVoucherNo(accbook.getVoucherNo());
            if (EmptyUtils.isNotEmpty(onlineSign) && "1".equals(onlineSign)) {
                repay.setStatus(CmsConstant.FLOWWAIT);//使用流程等待状态
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_58);//还款待确认扣款
            } else if (EmptyUtils.isNotEmpty(onlineSign) && "0".equals(onlineSign)) {
                repay.setStatus(CmsConstant.COMPELETOTAL);
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            }/*else{
				repay.setStatus(CmsConstant.COMPELETOTAL);
				repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
			}*/
            cmsProcessMsgService.insertProcessMsg(repay.getBillCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
            cmsProcessMsgService.insertProcessMsg(repay.getSendCode(), "B3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_REPAY_TALLY_CHECKOK, user);
            if (EmptyUtils.isNotEmpty(onlineSign) && "1".equals(onlineSign)) {
                /**数据推送到头寸调拨*/
                this.createInsAllocationReq(repay, user);
            } else {
                if (!EmptyUtils.isEmpty(info.getRatmodel()) && CmsConstant.IS_TRUE.equals(info.getRatmodel())) {
                    //授信还款，释放还款的授信额度，如果是还利息则不释放授信额度
                    BigDecimal repayMoney = repay.getRepayMoney();//还款金额
                    if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                        RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, info.getId());
                        urationDTO.setMoney(repayMoney);
                        urationDTO.setBailRate(info.getBailmoneyRate());//保证金比例
                        if (info.getPlMoney().compareTo(repaySum) == 0) {//借款金额等于还款总金额
                            urationDTO.setAllRelease(true);//标识为全部释放
                        }
                        urationService.releaseRation(urationDTO);
                    }
                }
                this.handleContract(repay, user);//履约合同处理
                if (info.getPlMoney().compareTo(repaySum) == 0) {//借款金额等于还款总金额
                    //担保处理
                    this.handleGua(info);
                    this.endContract(repay, user);//终止合同
                    pledgeOccupyService.releasePledgeOccupy(info.getId(), info.getBillCode());//释放抵质押物占用额度
                }
            }
            repay.setVoucherNo(accbook.getVoucherNo());
            repay.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        }
        if (FinacialManualConstant.MANUAL_TYPE_PROVIDE_SEND.equals(menuType)) {
            iCmsLoanService.updateProvide(entity, user);
            this.handleCon(entity, user);//合同处理
        } else {
            iCmsLoanService.updateProvideRepay(repay, user);
            if (EmptyUtils.isNotEmpty(onlineSign) && !"1".equals(onlineSign)) {
                iCmsLoanService.provideIsRepayComplete(repay.getId(), user);//线下还款完成的数据处理
            }
        }
        completeCheckNotificationInfo(Voucher, user);
        completeEditNotificationInfo(Voucher, user);
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher,
                                              ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(
                    NotificationConstant.FINACIAL.CMSPROVIDE_EDIT,
                    accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher,
                                               ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            notificationService.complete(
                    NotificationConstant.FINACIAL.CMSPROVIDE_CHECK,
                    accbook.getId(), user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            // 发送消息复核提醒
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(),
                    NotificationConstant.FINACIAL.CMSPROVIDE_CHECK, notifyVO, user.getId());
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
            notificationService.sendToUser(accbook.getId(),
                    accbook.getOperatorCode(), NotificationConstant.FINACIAL.CMSPROVIDE_EDIT, notifyVO);
        }
    }
    /****************************************线上还款记账*****************************************************/
    /**
     * 创建线上还款凭证信息
     *
     * @param repayBillCode
     * @param user
     * @return
     */
    public FcAccbookPageVO onlineProcessAccbook(String repayBillCode, String opType, ICurrentUser user, String onlineSign) {
        CmsProvideLoanRepayDTO repay = queryProvideRepay(repayBillCode, onlineSign);
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal accruedMoney = BigDecimal.ZERO;//冲预提金额
        if (repay.getRepayMoney() != null && repay.getRepayMoney().compareTo(BigDecimal.ZERO) != 0) {
            accruedMoney = iFcInterestService.getRushPreDrawMoney(repay.getLoanAcc(), repay.getRepayMoney(), repay.getId());
        } else {
            accruedMoney = iFcInterestService.getRushPreDrawMoney(repay.getLoanAcc(), null, repay.getId());
        }
        //repay.setRepayAccrual(interest);
        repay.setRushMoney(accruedMoney);
        List<VoucherRenderControlVo> controlBeans = new ArrayList<VoucherRenderControlVo>();
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT_ONLINE.get());         //记账规则管理-利息的科目-分录类型不能选择其他
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            entity = onlineProcessTallyAccbook(repay, ruleDefine, user, controlBeans);
            //entity.getAccbook().setTallyDate(repay.getSendData());
        } else {
            if (EmptyUtils.isEmpty(repay.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(repay.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            BeanUtils.copyProperties(fcAccbookDTO, accbook);
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
            FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            BeanUtils.copyProperties(fcAccbookExtDTO, bookExt);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
        }
        return entity;
    }
    /**
     * 线上还款设置凭证信息
     */
    public FcAccbookPageVO onlineProcessTallyAccbook(CmsProvideLoanRepayDTO repay, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user, List<VoucherRenderControlVo> controlBeans) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookVO accBook = new FcAccbookVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getOnlineAccbookDtls(ruleDefine, repay, controlBeans, user.getId());
        FcBusRaceVo busRace = getFcBusRaceId(repay.getCorpId(), FinaBookConstant.SysRaceCode.NB_BANK_REPAYMENT_ONLINE, user == null ? " " : user.getId());
        accBook.setBusRaceId(busRace.getId());
        accBook.setRaceName(busRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：线上还款记账时的会计分录构造
     */
    public List<FcAccbookDtlVO> getOnlineAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsProvideLoanRepayDTO repay, List<VoucherRenderControlVo> controlBeans, String user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        CmsProvideSendDTO send = iCmsLoanService.getCmsProvideLoanSendByBillCode(repay.getSendCode());
        String loanAcc = send.getLoanAcc();//贷款账号
        FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(loanAcc);
        CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            /**
             * 借：银行借款
             * 借：利息支出-银行借款
             * 贷：其他应付款
             */
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo beanNum = controlBeans.get(groupNum - 1);
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());//银行借款
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repay.getRepayMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    FcAccDTO dto = null;
                    //获取利息支出账号
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//获取利息支出-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = iFcAccbookService.getInterestAcc(send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide", info.getBankId(), user, "利息支出-银行借款");//利息支出-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);//结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//利息支出
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {//利息  利息支出-银行借款
                        accbookDtl.setDebitMoney(repay.getOnlineRealityInterest());
                    } else if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {//冲预提 利息支出-银行借款
                        accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(repay.getRushMoney()));
                    }
                }
            } else {
                FcAccDTO dto = null;
                if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {//冲预提 应付利息-银行借款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {//获取应付利息-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = iFcAccbookService.getInterestAcc(send.getCurCode(),
                                info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide", info.getBankId(), user, "应付利息-银行借款");//应付利息-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);//结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(repay.getRushMoney()));
                } else {
                    List<FcAccDTO> dtos = innerAccountService.listAccountByParams(send.getCurCode(), repay.getCorpId(), repay.getCorpId(), entry.getSubjectCode());//其他应付款
                    if (EmptyUtils.isEmptyList(dtos)) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_OTHERACC_IS_NULL);//结算中心未开设 其他应付款
                    }
                    //信贷参数
                    String con = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_OTHER_ACCOUNTS_SELECT).getValue();
                    sort(dtos, con);
                    dto = dtos.get(0);//其他应付款
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    //accbookDtl.setCreditMoney(repay.getOnlineMoney());//线上还款金额
                    accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));//线上还款金额可修改，会导致借贷不平
                    accbookDtl.setAccName(dto.getAccName());
                }
                repay.setOnlineOtherAccCharge(dto.getBlnctrAcc());//保存其他应付款帐号
            }
            accbookDtl.setCurCode(send.getCurCode());//使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setOrigVoucherId(repay.getId());
            accbookDtl.setSummaryContent(info.getPlPurpose());
            accbookDtl.setControlVo(beanNum);
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0
                    || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    public void sort(List<FcAccDTO> dtos, String config) {
        final String con = config;
        // 明细进行先借后贷的排序
        Collections.sort(dtos, new Comparator<FcAccDTO>() {
            @Override
            public int compare(FcAccDTO o1, FcAccDTO o2) {
                if ("2".equals(con)) {
                    return o1.getFcBalance().compareTo(o2.getFcBalance());//从小到大
                } else if ("3".equals(con)) {
                    return o2.getFcBalance().compareTo(o1.getFcBalance());//从小到大
                } else {
                    return o2.getAccOpenTime().compareTo(o1.getAccOpenTime());//开户日期
                }
            }
        });
    }
    /*******************************************线下还款记账********************************************************/
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookPageVO processAccbookDownRepay(String repayBillCode, String opType, ICurrentUser user, String onlineSign) {
        //添加repay实体
        CmsProvideLoanRepayDTO repay = queryProvideRepay(repayBillCode, onlineSign);
        BigDecimal accruedMoney = BigDecimal.ZERO;//冲预提金额
        if (repay.getRepayMoney() != null && repay.getRepayMoney().compareTo(BigDecimal.ZERO) != 0) {
            accruedMoney = iFcInterestService.getRushPreDrawMoney(repay.getLoanAcc(), repay.getRepayMoney(), repay.getId());
        } else {
            accruedMoney = iFcInterestService.getRushPreDrawMoney(repay.getLoanAcc(), null, repay.getId());
        }
        repay.setRushMoney(accruedMoney);
        List<VoucherRenderControlVo> controlBeans = new ArrayList<VoucherRenderControlVo>();
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT.get());
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            entity = processTallyAccbookRepay(repay, ruleDefine, user, controlBeans);
        } else {
            if (EmptyUtils.isEmpty(repay.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(repay.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            BeanUtils.copyProperties(fcAccbookDTO, accbook);
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
            FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            BeanUtils.copyProperties(fcAccbookExtDTO, bookExt);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
        }
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbookRepay(CmsProvideLoanRepayDTO repay, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user, List<VoucherRenderControlVo> controlBeans) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookVO accBook = new FcAccbookVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getAccbookDtlsRepay(ruleDefine, controlBeans, repay, user.getId());
        FcBusRaceVo fcBusRace = getFcBusRaceId(repay.getCorpId(), FinaBookConstant.SysRaceCode.NB_BANK_REPAYMENT, user == null ? " " : user.getId());
        //TODO
        accBook.setBusRaceId(fcBusRace.getId());
        accBook.setRaceName(fcBusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlVO> getAccbookDtlsRepay(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, CmsProvideLoanRepayDTO repay, String user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        CmsProvideSendDTO send = queryProvideSend(repay.getSendCode());
        CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());
        String loanAcc = send.getLoanAcc();//借款账号
        FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(loanAcc);
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            /**
             * "借 利息支出-银行借款"
             * "贷 应付利息-银行借款"
             * 借：2110银行借款 网点 1
             * 借：521004利息支出-银行借款 3
             * 贷：1020银行活期存款 网点 2
             */
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo beanNum = controlBeans.get(groupNum - 1);
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repay.getRepayMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    FcAccDTO dto = null;
                    //获取利息支出账号
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//获取利息支出-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = iFcAccbookService.getInterestAcc(send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide", info.getBankId(), user, "利息支出-银行借款");//利息支出-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);//结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {//利息支出
                        accbookDtl.setDebitMoney(repay.getRepayAccrual());
                    } else if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {//利息支出-银行借款
                        accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(repay.getRushMoney()));
                    }
                }
            } else {
                FcAccDTO dto = null;
                if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {//应付利息-银行借款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {//获取应付利息-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                    } else {
//                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = fcAccbookService.getInterestAcc(send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide", info.getBankId(), user, "应付利息-银行借款");//应付利息-银行借款
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = iFcAccbookService.getInterestAcc(send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide", info.getBankId(), user, "应付利息-银行借款");//应付利息-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);//结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(repay.getRushMoney()));
                } else {
                    dto = fcAccService.getAccDTOByAccount(send.getBlnctrAcc());//发放时使用的银行活期帐号 (本金) 记账为0会自动过滤掉
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getRepayAccrual()));//利息加本金
                    accbookDtl.setAccName(dto.getAccName());
                }
            }
            accbookDtl.setCurCode(send.getCurCode());//使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setOrigVoucherId(repay.getId());
            accbookDtl.setSummaryContent(info.getPlPurpose());
            accbookDtl.setControlVo(beanNum);
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0
                    || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /****************************************还款确认*******************************************/
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookPageVO processAccbookConfirmRepay(String repayBillCode, String opType, ICurrentUser user, String onlineSign) {
        CmsProvideLoanRepayDTO repay = queryProvideRepay(repayBillCode, onlineSign);
        List<VoucherRenderControlVo> controlBeans = new ArrayList<VoucherRenderControlVo>();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT_ONLINE_OK.get());
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        FcAccbookPageVO entity = new FcAccbookPageVO();
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            entity = onlineOKProcessTallyAccbook(repay, ruleDefine, user, controlBeans);
        } else {
            if (EmptyUtils.isEmpty(repay.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(repay.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            BeanUtils.copyProperties(fcAccbookDTO, accbook);
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);

            FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            BeanUtils.copyProperties(fcAccbookExtDTO, bookExt);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
        }
        return entity;
    }
    /**
     * 线上还款--确认已扣款 设置凭证信息
     */
    public FcAccbookPageVO onlineOKProcessTallyAccbook(CmsProvideLoanRepayDTO repay, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user, List<VoucherRenderControlVo> controlBeans) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookVO accBook = new FcAccbookVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getOnlineOKAccbookDtls(ruleDefine, repay, controlBeans);
        FcBusRaceVo fcbusrace = getFcBusRaceId(repay.getCorpId(), FinaBookConstant.SysRaceCode.NB_BANK_REPAYMENT_ONLINE_OK, user == null ? " " : user.getId());
        accBook.setBusRaceId(fcbusrace.getId());
        accBook.setRaceName(fcbusrace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：线上还款--确认已扣款 记账时的会计分录构造
     */
    public List<FcAccbookDtlVO> getOnlineOKAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsProvideLoanRepayDTO repay, List<VoucherRenderControlVo> controlBeans) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            CmsProvideSendDTO send = iCmsLoanService.getCmsProvideLoanSendByBillCode(repay.getSendCode());
            CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());
            /**
             * 借：其他应付款
             * 贷：银行活期
             */
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                FcAccDTO dto = fcAccService.getAccDTOByAccount(repay.getOnlineOtherAccCharge());//其他应付款帐号
                accbookDtl.setAccount(dto.getBlnctrAcc());
                accbookDtl.setDebitMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));//线上还款金额可修改，会导致借贷不平
                accbookDtl.setAccName(dto.getAccName());
            } else {
                FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getBlnctrAcc());//银行活期
                accbookDtl.setAccount(dto.getBlnctrAcc());
                //accbookDtl.setCreditMoney(repay.getOnlineMoney());//利息加本金
                accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));
                accbookDtl.setAccName(dto.getAccName());
            }
            accbookDtl.setCurCode(send.getCurCode());//使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setOrigVoucherId(repay.getId());
            accbookDtl.setSummaryContent(info.getPlPurpose());
            accbookDtl.setControlVo(bean);
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 头寸调拨
     **/
    protected void createInsAllocationReq(CmsProvideLoanRepayDTO repay, ICurrentUser user) {
        NisAllocationReqDTO dto = nisAllocationReqService.getNisAllocationReqByErpId(repay.getId());
        if (EmptyUtils.isEmpty(dto) || EmptyUtils.isEmpty(dto.getId())) {
            dto = new NisAllocationReqDTO();
        }
        CmsProvideSendDTO send = iCmsLoanService.getCmsProvideLoanSendByBillCode(repay.getSendCode());
        dto.setErpId(repay.getId());
        dto.setStatus(AllocationConstant.STATUE_TEMP);
        dto.setBillFrom(AllocationConstant.BILL_FROM_2);
        dto.setType(AllocationConstant.ALLOCATION_TYPE_POS);//头寸调拨
        dto.setIsQuick("0");//不加急
        dto.setCurId(send.getCurCode());//币别ID
        dto.setCorpId(repay.getCorpId());
        dto.setPayeeAccId(repay.getOnlinePaymentAccId());
        dto.setPayeeAcc(repay.getOnlinePaymentAcc());
        dto.setPayeeAccName(repay.getOnlineAccName());
        dto.setMoney(repay.getOnlineMoney());
        dto.setPurpose(repay.getOnlinePurpose());
        //获取还款单据的创建用户或修改用户信息
        if (EmptyUtils.isBlank(repay.getUpdateBy())) {
            user = userService.getByLoginName(repay.getCreateBy());
        } else {
            user = userService.getByLoginName(repay.getUpdateBy());
        }
        if (!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getId())) {
            nisAllocationReqService.updateNisAllocationReq(dto, user);
        } else {
            nisAllocationReqService.saveNisAllocationReq(dto, user);
        }
    }
    private void validata(String sendBillCode, String repayBillCode, String onlineSign) {
        if (EmptyUtils.isEmpty(onlineSign)) {
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                CmsProvideSendDTO sendInfo = iCmsLoanService.getCmsProvideLoanSendByBillCode(sendBillCode);
                if (sendInfo != null) {
                    if (sendInfo.getStatus() == 102) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                    }
                }
            }
        } else {
            if (EmptyUtils.isNotEmpty(repayBillCode)) {
                CmsProvideLoanRepayDTO repayInfo = iCmsLoanService.getCmsProvideLoanRepayByBillCode(repayBillCode, onlineSign);
                if (repayInfo != null) {
                    if (repayInfo.getStatus() == 102 || repayInfo.getStatus() == 12) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                    }
                }
            }
        }
    }
    private void validataBack(String sendBillCode, String repayBillCode, String onlineSign) {
        if (EmptyUtils.isEmpty(onlineSign)) {
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                CmsProvideSendDTO sendInfo = iCmsLoanService.getCmsProvideLoanSendByBillCode(sendBillCode);
                if (sendInfo != null) {
                    if (EmptyUtils.isNotEmpty(sendInfo.getTallySign()) && sendInfo.getTallySign().equals("4") || sendInfo.getTallySign().equals("0")) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                    }
                }
            }
        } else {
            if (EmptyUtils.isNotEmpty(repayBillCode)) {
                CmsProvideLoanRepayDTO repayInfo = iCmsLoanService.getCmsProvideLoanRepayByBillCode(repayBillCode, onlineSign);
                if (repayInfo != null) {
                    if (EmptyUtils.isNotEmpty(repayInfo.getTallySign()) && repayInfo.getTallySign().equals("4") || repayInfo.getTallySign().equals("0")) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                    }
                }
            }
        }
    }
    /**
     * 释放担保
     *
     * @param cmsProvideLoanInfoDTO
     */
    private void handleGua(CmsProvideLoanInfoDTO cmsProvideLoanInfoDTO) {
        if (CmsConstant.IS_TRUE.equals(cmsProvideLoanInfoDTO.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsProvideLoanInfoDTO.getId());
            if (cmsProvideLoanInfoDTO.getGuaContract().contains(";")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(cmsProvideLoanInfoDTO.getGuaContract().split(";")[0].split(",")));
            }
            guaContractOccupDTO.setSysCode(ConfigConstant.LCS);
            guaContractOccupDTO.setItemNo(cmsProvideLoanInfoDTO.getBillCode());
            guaContractOccupDTO.setUseDate(new Date());
            guaContractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
        }
    }
    /**
     * 发放生成履约合同信息
     *
     * @param entity
     * @param user
     */
    private void handleCon(CmsProvideSendDTO entity, ICurrentUser user) {
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();//是否对接合同管理
        String relateConType = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE).getValue();//对接合同管理管理模式
        String conSegment = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_SEGMENT).getValue();//履约环节
        if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES) &&//对接合同管理
//				null!=relateConType&&relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)&&
                conSegment.contains(CmsConstant.SEGMENT_LOAN)) {//履约环节包括发放履约
            CmsProvideLoanInfoDTO loanInfoDTO = iCmsLoanService.getLoanInfoVOByBillCode(entity.getBillCode());
            if (null != loanInfoDTO.getConId()) {
                ConInfoRecordDTO recordDTO = new ConInfoRecordDTO();
                recordDTO.setConId(loanInfoDTO.getConId());//合同ID 必填
                recordDTO.setOuterId(entity.getId());//履约业务挂接ID 必填
                recordDTO.setOpMsg(CmsConstant.PROVIDE_LOAN_SEND);//履约原因 必填
                recordDTO.setOpDate(DateUtils.getDate());//履约时间 必填
                recordDTO.setOpNo(entity.getBillCode());//履约申请编号 选填
                recordDTO.setOpMoney(entity.getMoney());//履约金额 选填
                UserDTO userDTO;
                if (StringUtils.isNotEmpty(entity.getCreateBy())) {
                    userDTO = userService.getByLoginName(entity.getCreateBy());
                } else {
                    userDTO = userService.getByLoginName(entity.getUpdateBy());
                }
                iConService.performanceConOp(recordDTO, userDTO);
            }
        }
    }
    /**
     * 还款生成履约合同信息
     */
    private void handleContract(CmsProvideLoanRepayDTO entity, ICurrentUser user) {
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();//是否对接合同管理
        String relateConType = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE).getValue();//对接合同管理管理模式
        String conSegment = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_SEGMENT).getValue();//履约环节
        if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES) &&//对接合同管理
//				null!=relateConType&&relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)&&
                conSegment.contains(CmsConstant.SEGMENT_REPAY)) {//履约环节包括还款履约
            ConInfoRecordDTO recordDTO = new ConInfoRecordDTO();
            recordDTO.setConId(entity.getConId());//合同ID 必填
            recordDTO.setOuterId(entity.getId());//履约业务挂接ID 必填
            recordDTO.setOpMsg(CmsConstant.PROVIDE_LOAN_REPAY);//履约原因 必填
            recordDTO.setOpDate(DateUtils.getDate());//履约时间 必填
            recordDTO.setOpNo(entity.getBillCode());//履约申请编号 选填
            recordDTO.setOpMoney(entity.getRepayMoney());//履约金额 选填
            UserDTO userDTO;
            if (StringUtils.isNotEmpty(entity.getCreateBy())) {
                userDTO = userService.getByLoginName(entity.getCreateBy());
            } else {
                userDTO = userService.getByLoginName(entity.getUpdateBy());
            }
            iConService.performanceConOp(recordDTO, userDTO);
        }
    }
    /**
     * 终止合同处理
     */
    private void endContract(CmsProvideLoanRepayDTO repay, ICurrentUser user) {
        CmsProvideLoanInfoDTO info = queryProvideInfo(repay.getSendCode());
        BigDecimal repaySum = getInfoRepayMoneySum(info.getBillCode()).add(repay.getRepayMoney());//累计已还金额+本次还款金额
        if (info.getPlMoney().compareTo(repaySum) == 0) {//借款金额等于还款总金额
            if (StringUtils.isNotEmpty(repay.getConId())) {
                ConInfoRecordDTO dto = new ConInfoRecordDTO();
                dto.setConId(repay.getConId());
                dto.setOuterId(repay.getId());//终止业务挂接ID
                dto.setOpMsg(CmsConstant.PROVIDE_LOAN_REPAYOVER);//终止原因：银行借款全部还清
                dto.setOpDate(repay.getRepayDate());
                dto.setOpNo(repay.getBillCode());
                UserDTO userDTO;
                if (StringUtils.isNotEmpty(repay.getCreateBy())) {
                    userDTO = userService.getByLoginName(repay.getCreateBy());
                } else {
                    userDTO = userService.getByLoginName(repay.getUpdateBy());
                }
                iConService.terminateConOp(dto, userDTO);
            }
        }
    }
    private void setAccountGrant(String accountId, String corpId, List<String> users) {
        users.removeAll(Collections.singleton(null));
        int i = 0;
        for (String user : users) {
            if (i != 0) {
                user = userService.getByLoginName(user).getId();
            }
            AccountGrantDTO ag = new AccountGrantDTO();
            ag.setAccountId(accountId);
            ag.setAccountType(1);
            ag.setUserId(user);
            ag.setCorpId(corpId);
            iAccountGrantService.saveAccountGrant(ag);
            i++;
        }
    }
    /**
     * 查询发放账务数据 --待记账
     */
    public List<CmsProvideSendDTO> queryTallyProvideSend() {
        return iCmsLoanService.queryTallyProvideSend();
    }
    /**
     * 查询还款账务数据 --记账
     */
    List<CmsProvideLoanRepayDTO> queryTallyProvideRepay(String onlineSign) {
        return iCmsLoanService.queryTallyProvideRepay(onlineSign);
    }
    /**
     * 释放抵质押物处理
     */
    private void releasePledge(CmsProvideLoanRepayDTO repay, ICurrentUser user) {
        CmsProvideLoanInfoDTO info = queryProvideInfo(repay.getSendCode());
        BigDecimal repaySum = getInfoRepayMoneySum(info.getBillCode()).add(repay.getRepayMoney());//累计已还金额+本次还款金额
        if (info.getPlMoney().compareTo(repaySum) == 0) {//借款金额等于还款总金额
            pledgeOccupyService.releasePledgeOccupy(info.getId(), info.getBillCode());
        }
    }
}
