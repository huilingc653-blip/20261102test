package com.bytter.tally.service;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.api.constant.BudgetConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.biz.finacial.api.service.IFcAccbookExtService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.util.FinacialManualUtil;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金调拨记账类
 */
@Service("nisAllocationReqManualService")
@Transactional
public class NisAllocationReqManualService extends BaseService {
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @DubboReference(check = false)
    private INisAllocationReqService nisAllocationReqServiceImpl;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    @Autowired
    private FinacialManualService finacialManualService;
    /*@Autowired
    private FcAccbookDtlService fcAccbookDtlService;*/
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    /*@Autowired
    private FcAccbookService fcAccbookService;*/
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    /*@Autowired
    private FcAccbookExtService fcAccbookExtService;*/
    @DubboReference(check = false)
    private IFcAccbookExtService iFcAccbookExtService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 下拨和请款构造accbook
     */
    public FcAccbookPageVO processAccbook(String key, String type, ICurrentUser user) {
        if (EmptyUtils.isEmpty(nisAllocationReqServiceImpl)) {
            nisAllocationReqServiceImpl = (INisAllocationReqService) ApplicationContextUtil.getContext().getBean("nisAllocationReqServiceI");
        }
        if (EmptyUtils.isEmpty(iFcAccbookExtService)) {
            iFcAccbookExtService = (IFcAccbookExtService) ApplicationContextUtil.getContext().getBean("fcAccbookExtServiceI");
        }
        NisAllocationReqDTO fund = nisAllocationReqServiceImpl.getNisAllocationReqByBillNo(key);
        FcAccbookPageVO entity = new FcAccbookPageVO();
        List<VoucherRenderControlVo> controlBeans = getControlBeanEdit(2, fund);
        if (NbconsoleConstant.OpType.INIT.get().equals(type)) {
            //判断数据来源是否是下拨，修改 网银控制台 >> 查询 >> 单位申请付款查询问题
            if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(fund.getType())) {
                entity = this.processAccBook(fund, controlBeans, SysRaceCode.NB_CNT_DOWN_PAYMENT.get(), user);
            } else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(fund.getType())) {
                entity = this.processAccBook(fund, controlBeans, SysRaceCode.NB_CNT_POS_PAYMENT.get(), user);
            } else {
                entity = this.processAccBook(fund, controlBeans, SysRaceCode.NB_UNIT_ASK_PAYMENT.get(), user);
            }
        } else {
            if (EmptyUtils.isEmpty(fund.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            if (EmptyUtils.isEmpty(iFcAccbookService)) {
                iFcAccbookService = (IFcAccbookService) ApplicationContextUtil.getContext().getBean("fcAccbookServiceI");
            }
            if (EmptyUtils.isEmpty(iFcAccbookDtlService)) {
                iFcAccbookDtlService = (IFcAccbookDtlService) ApplicationContextUtil.getContext().getBean("fcAccbookDtlServiceI");
            }
//			FcAccbookVO books = fcAccbookService.getBookVOByOrderNo(fund.getOrderNo(),FinacialConstant.BOOLEAN_FALSE);
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(fund.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            if (!EmptyUtils.isEmpty(fcAccbookDTO)) {
                BeanUtils.copyNotNullProperties(fcAccbookDTO, accbook);
                accbook.setRaceName(fcAccbookDTO.getRaceName());
            }
            accbook.setRmk(fund.getPurpose());
            accbook.setPostscript(fund.getPurpose());//附言
//        	List<FcAccbookDtlVO> bookDtls = fcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
//			FcAccbookExtVO bookExt = fcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
            FcAccbookExtVO fcAccbookExtVO = new FcAccbookExtVO();
            BeanUtils.copyProperties(fcAccbookExtDTO, fcAccbookExtVO);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(fcAccbookExtVO);
        }
        return entity;
    }

    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlVo> getControlBeanEdit(int size, NisAllocationReqDTO fund) {
        List<VoucherRenderControlVo> controBeans = new ArrayList<VoucherRenderControlVo>();
        VoucherRenderControlVo controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlVo();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setDebitAccountLoadModle(false);
            if (i == 0) {
                if (AllocationConstant.ALLOCATION_TYPE_DOWN.toString().equals(fund.getType().toString())
                        || AllocationConstant.ALLOCATION_TYPE_ASK.toString().equals(fund.getType().toString())) {
                    //下拨&请款 可修改 借方账户信息
                    controBean.setAccountEditable(true);
                    controBean.setDebitAccountLoadModle(true);
                } else {
                    controBean.setAccountEditable(false);
                }
            } else {
                controBean.setAccountEditable(false);
            }
            controBeans.add(controBean);
        }
        return controBeans;
    }

    // 查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput getDateTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        if (EmptyUtils.isEmpty(nisAllocationReqServiceImpl)) {
            nisAllocationReqServiceImpl = (INisAllocationReqService) ApplicationContextUtil.getContext().getBean("nisAllocationReqServiceI");
        }
        return nisAllocationReqServiceImpl.getDataTables(dataTablesInput, user);
    }

    /**
     * 构造凭证明细
     *
     * @param fund 下拨详情
     * @param fund payerAcc+";"+payeeAcc+";"+purpose+";"+billNo+";"+money;
     * @return
     */
    public FcAccbookPageVO processAccBook(NisAllocationReqDTO fund, List<VoucherRenderControlVo> controlBeans, String sysRaceCode, ICurrentUser user) {
        if (EmptyUtils.isEmpty(finacialManualService)) {
            finacialManualService = (FinacialManualService) ApplicationContextUtil.getContext().getBean("finacialManualService");
        }
        if (EmptyUtils.isEmpty(iFcAccService)) {
            iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
        }
        if (EmptyUtils.isEmpty(iCorpService)) {
            iCorpService = (ICorpService) ApplicationContextUtil.getContext().getBean("corpServiceImpl");
        }
        if (EmptyUtils.isEmpty(iBtBankAccService)) {
            iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccServiceImpl");
        }
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(sysRaceCode);
        List<FcAccbookDtlVO> accbookDtls = getAccbookDtls(ruleDefine, controlBeans, fund, sysRaceCode, bookExt);
        entity.setBookDtls(accbookDtls);
        //构建账务账务凭证主档表记录
        FcAccbookVO accBook = new FcAccbookVO();
        accBook.setRmk(fund.getPurpose());//备注
        accBook.setPostscript(fund.getPurpose());//附言
        //获取记账规则ID
        ConstantBean<String> sysRaceBean = null;
        if (SysRaceCode.NB_CNT_POS_PAYMENT.get().equals(sysRaceCode)) {
            sysRaceBean = SysRaceCode.NB_CNT_POS_PAYMENT;
        } else if (SysRaceCode.NB_CNT_DOWN_PAYMENT.get().equals(sysRaceCode)) {
            sysRaceBean = SysRaceCode.NB_CNT_DOWN_PAYMENT;
        } else {
            sysRaceBean = SysRaceCode.NB_UNIT_ASK_PAYMENT;
        }
        FcBusRaceVo fcBusRace = finacialManualService.getFcBusRaceId(fund.getCorpId(), sysRaceBean, user.getId());
        accBook.setBusRaceId(fcBusRace.getId());
        accBook.setRaceName(fcBusRace.getRaceName());
        accBook.setTallyDate(DateUtils.getDate());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setInterestStartDate(DateUtils.getDateYMD());
        if (NetbankConstant.ISQUICK_0.equals(fund.getIsQuick())) {
            accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        } else {
            accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        }
        accBook.setBusOriType(getOriType(sysRaceCode));
        entity.setBookDtls(accbookDtls);
        //外单位信息
        BtBankAccDTO PayeeAcc = iBtBankAccService.getBankAccInfo(fund.getPayeeAcc());
        bookExt.setExternalCorp(fund.getPayeeAccName());//外单位户名
        bookExt.setExternalAcc(fund.getPayeeAcc());//外单位户名
        bookExt.setExternalBank(PayeeAcc.getBankName());//外单位开户行
        bookExt.setPaysysbankcode(PayeeAcc.getBankCode());//收款方联行号
        bookExt.setExternalProv(PayeeAcc.getProv());
        bookExt.setExternalCity(PayeeAcc.getCity());
        bookExt.setProv(PayeeAcc.getProv());
        bookExt.setCity(PayeeAcc.getCity());
        bookExt.setRmk(fund.getPurpose());
        bookExt.setPayerAcc(fund.getPayerAcc());//付款账号
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        return entity;
    }

    /**
     * 构造会计分录（请款 下拨 头寸调拨）
     */
    public List<FcAccbookDtlVO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, NisAllocationReqDTO fund,
                                               String sysRaceCode, FcAccbookExtVO bookExtVo) {
        if (EmptyUtils.isEmpty(iBtBankAccService)) {
            iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccServiceImpl");
        }
        if (EmptyUtils.isEmpty(iFcAccService)) {
            iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
        }
        if (EmptyUtils.isEmpty(iInnerAccountService)) {
            iInnerAccountService = (IInnerAccountService) ApplicationContextUtil.getContext().getBean("innerAccountServiceI");
        }
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        //FcVoucherTypeDTO voucherType = null;
        //获取收款方
        BtBankAccDTO payeeAcc = iBtBankAccService.getBankAccByIdAndCurId(fund.getPayeeAccId(), fund.getCurId());
        //获取付款方
        BtBankAccDTO payerAcc = iBtBankAccService.getBankAccByIdAndCurId(fund.getPayerAccId(), fund.getCurId());
        CorpDTO payerAccDTO = null;
        CorpDTO payeeAccDTO = null;
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            accbookDtl = new FcAccbookDtlVO();
            FcAccDTO fcAcc = new FcAccDTO();
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
            fcAcc.setBlnctrAcc(entry.getAccount());
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (balanceWay == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() && entry.getEntryType().intValue() == 10) {
                String blnctrAcc = iBtBankAccService.getBlnctrAcc(payeeAcc.getBankAcc(), fund.getCurId());//取银行账号对应的内部帐号
                if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(fund.getType())
                        || AllocationConstant.ALLOCATION_TYPE_ASK.equals(fund.getType())) {
                    //以制单选择的中心账号为准
                    if (StringUtils.isNotBlank(fund.getPayeeCounterAcc())) {
                        blnctrAcc = fund.getPayeeCounterAcc();
                    }
                    payeeAccDTO = iCorpService.get(payeeAcc.getCorpId());
                    payerAccDTO = iCorpService.get(payerAcc.getCorpId());
                    //if (!payeeAccDTO.getNetId().equals(payerAcc.getCorpId())) {//如果取反这个方法就进不去
                    Integer payeeNetSign = payeeAccDTO.getType();
                    if (!EmptyUtils.isEmpty(payeeNetSign) && payeeNetSign == 0) {
                        String payeeSuperNetId = StringUtils.isBlank(payeeAccDTO.getParentId()) ? "" : payeeAccDTO.getParentId();
                        String payeeBlnctrAcc = blnctrAcc;
                        if (payeeSuperNetId.equals(payerAccDTO.getId())) {// TODO 说明是跨网点调拨
                            /*开始*/
                            String superCurrentAcc = iBtBankAccService.getCountertAcc(payeeAcc.getBankAcc()).get(0).getSuperCounterAcc();
                            if (EmptyUtils.isEmpty(superCurrentAcc)) {//请先维护银行账号["+ payeeAccNo.getBankAcc() + "]与上级内部账号的关系"
                                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL, payeeAcc.getBankAcc());
                            }
                            blnctrAcc = superCurrentAcc;
                            bookExtVo.setStartBank(payeeAcc.getBankName());
                            bookExtVo.setStartCorp(payeeAcc.getCorpName());
                            bookExtVo.setStartCorpAcc(payeeBlnctrAcc);// 第三方账号,子账号银行活期
                            /*结束*/
//								List<BtBankAccCurDTO> btBankAccList = payeeAcc.getBtBankAccCurDTOList();
//								String account = null;
//								for (BtBankAccCurDTO bt : btBankAccList) {
//									if (!EmptyUtils.isEmpty(bt.getSuperCounterAcc())) {
//										account = bt.getSuperCounterAcc();
//										break;
//									}
//								}
////								if (EmptyUtils.isEmpty(account)) {//请先维护银行账号["+ payeeAccNo.getBankAcc() + "]与上级内部账号的关系"
////									throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
////								}
//								blnctrAcc = account;
                        }
                    }
                    //}
                }
                if (EmptyUtils.isEmpty(blnctrAcc)) {// TODO
                    if (!AllocationConstant.ALLOCATION_TYPE_DOWN.equals(fund.getType())
                            && !AllocationConstant.ALLOCATION_TYPE_ASK.equals(fund.getType())) {
                        //下拨、请款无需校验，可在记账页面选择
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, payeeAcc.getBankAcc());
                    }
                } else {
                    FcAccDTO byBlnctrAcc = iInnerAccountService.getByBlnctrAcc(blnctrAcc);
                    if (byBlnctrAcc != null) {
                        accbookDtl.setAccName(byBlnctrAcc.getAccName());
                        accbookDtl.setAccount(byBlnctrAcc.getBlnctrAcc());
                    }
                }
                fcAcc.setBlnctrAcc(blnctrAcc);
                accbookDtl.setDebitMoney(fund.getMoney());//设置借方发生额
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                bean.setCreateAccountAble(false);
            } else if (balanceWay == Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT) && entry.getEntryType().intValue() == 10) {
                String creditAccount = iBtBankAccService.getBlnctrAcc(payerAcc.getBankAcc(), fund.getCurId());//取银行账号对应的内部帐号
                if (EmptyUtils.isEmpty(creditAccount)) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, payerAcc.getBankAcc());
                }
                FcAccDTO fcAccCredit = iInnerAccountService.getByBlnctrAcc(creditAccount);
                fcAcc.setBlnctrAcc(creditAccount);
                accbookDtl.setAccName(fcAccCredit.getAccName());
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(fund.getMoney());//设置贷方发生额
                accbookDtl.setAccount(fcAccCredit.getBlnctrAcc());
                bean.setCreateAccountAble(false);
            }
            if (!EmptyUtils.isEmpty(fcAcc.getBlnctrAcc())) {
                FcAccDTO fc = iFcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                if (null != fc) {
                    fcAcc.setAccName(fc.getAccName());
                }
                fcAcc.setNetId(fc.getNetId());
            }
            if (SysRaceCode.NB_CNT_DOWN_PAYMENT.get().equals(sysRaceCode)) {
                //下拨摘要区分
                accbookDtl.setSummaryContent(fund.getPurpose());//"下拨-" +
            } else {
                accbookDtl.setSummaryContent(fund.getPurpose());
            }
            accbookDtl.setOrigVoucherNo(fund.getBillNo());
            accbookDtl.setOrigVoucherId(fund.getId());
            accbookDtl.setCurCode(fund.getCurId());
            accbookDtl.setControlVo(bean);
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }

    /**
     * 功能介绍 : 设置复核参数，是否产生指令等。<br>
     * <b>日期：</b>2018-05-16<br>
     *
     * @version 1.0
     * @author liyh
     * FinacialManualService类的setCheckVoucherInfo方法 反射调用此方法
     */
    public void setCheckVoucherInfo(VoucherWrapperDTO wrapperDTO, ICurrentUser user, String nisId) {
        if (EmptyUtils.isEmpty(configServiceImpl)) {
            configServiceImpl = (IConfigService) ApplicationContextUtil.getContext().getBean("configServiceI");
        }
        if (EmptyUtils.isEmpty(nisAllocationReqServiceImpl)) {
            nisAllocationReqServiceImpl = (INisAllocationReqService) ApplicationContextUtil.getContext().getBean("nisAllocationReqServiceImpl");
        }
        //判断头寸调拨是否需要记账
        String isAccountPos = configServiceImpl.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
        NisAllocationReqDTO nis = nisAllocationReqServiceImpl.getNisAllocationReqByBillNo(nisId);
        //判断资金调拨是否需要记账(头寸调拨单据来源是银行借款的话，是否记账是根据银行借款，借款管理是否记账参数来确定)
        String isAccount = "";
        if (AllocationConstant.BILL_FROM_2.equals(nis.getBillFrom())) {
            isAccount = configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_TALLY).getValue();
        } else {
            isAccount = configServiceImpl.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
        }
        //不记账跳过
        if ((CommonConstant.NO.equals(isAccount) && !AllocationConstant.ALLOCATION_TYPE_POS.equals(nis.getType()))
                || (CommonConstant.NO.equals(isAccountPos) && AllocationConstant.ALLOCATION_TYPE_POS.equals(nis.getType()))) {
            return;
        }
        //先记账后付款
        String payAccount = configServiceImpl.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_PAYACCOUNT).getValue();
        if (CommonConstant.NO.equalsIgnoreCase(payAccount)) {// 先记账后付款
            wrapperDTO.setCreateBisExc(true);// 产生指令
            wrapperDTO.setUser(user);
            NisAllocationReqDTO reqDTO = nisAllocationReqServiceImpl.getNisAllocationReqByBillNo(nisId);
            wrapperDTO.setPayerAcc(reqDTO.getPayerAccId());//付款账号
        }
    }

    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        if (EmptyUtils.isEmpty(fcBusinessRuleDefineService)) {
            fcBusinessRuleDefineService = (IFcBusinessRuleDefineService) ApplicationContextUtil.getContext().getBean("fcBusinessRuleDefineServiceI");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }

    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String sysRaceCode) {
        if (SysRaceCode.NB_CNT_POS_PAYMENT.get().equals(sysRaceCode)) {
            return BusOriType.NB_CNT_POS_PAYMENT.get();
        } else if (SysRaceCode.NB_CNT_DOWN_PAYMENT.get().equals(sysRaceCode)) {
            return BusOriType.NB_CNT_DOWN_PAYMENT.get();
        } else {
            return BusOriType.NB_UNIT_ASK_PAYMENT.get();
        }
    }

    /**
     * 记账前校验数据状态
     *
     * @param opType
     * @param key
     */
    public void validBefore(String opType, String key) {
        if (EmptyUtils.isEmpty(nisAllocationReqServiceImpl)) {
            nisAllocationReqServiceImpl = (INisAllocationReqService) ApplicationContextUtil.getContext().getBean("nisAllocationReqServiceI");
        }
        NisAllocationReqDTO fund = nisAllocationReqServiceImpl.getNisAllocationReqByBillNo(key);
        if (FinacialManualConstant.MANUAL_STATUS_INIT.equals(opType)) {//待记账
            if (EmptyUtils.isNotEmpty(fund.getTallySign())) {
                FinacialManualUtil.validBillStatus(fund.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_WAIT_TALLY});
            }
        } else {
            if (EmptyUtils.isEmpty(fund.getTallySign())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            if (FinacialManualConstant.MANUAL_STATUS_EDIT.equals(opType)) {//改账中
                FinacialManualUtil.validBillStatus(fund.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_TALLY_TEMP,
                        FinacialManualConstant.TALLY_SIGN_TALLY_BACK, FinacialManualConstant.TALLY_SIGN_TALLY_CHECK});
            } else if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)) {//待复核
                FinacialManualUtil.validBillStatus(fund.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_TALLY_CHECK});
            }
        }
    }

    /**
     * 待记账查询
     */
    List<NisAllocationReqDTO> queryTallyNisAllReq(Integer menuType) {
        return nisAllocationReqServiceImpl.queryTallyNisAllReq(menuType);
    }

    /**
     * 查询有权限的内部帐号
     */
    public List<FcAccDTO> getFcAccDebit(String key, ICurrentUser user) {
        if (EmptyUtils.isEmpty(nisAllocationReqServiceImpl)) {
            nisAllocationReqServiceImpl = (INisAllocationReqService) ApplicationContextUtil.getContext().getBean("nisAllocationReqServiceI");
        }
        if (EmptyUtils.isEmpty(finacialManualService)) {
            finacialManualService = (FinacialManualService) ApplicationContextUtil.getContext().getBean("finacialManualService");
        }
        if (EmptyUtils.isEmpty(iBtBankAccService)) {
            iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccServiceImpl");
        }
        NisAllocationReqDTO fund = nisAllocationReqServiceImpl.getNisAllocationReqByBillNo(key);
        String corpId = fund.getCorpId();
        if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(fund.getType())) {
            BtBankAccDTO payeeAcc = iBtBankAccService.getBankAccInfo(fund.getPayeeAcc());
            corpId = payeeAcc.getCorpId();
        }
        String corpCondition = " and t.corp_id = '" + corpId + "' ";
        corpCondition += " and t.cur_id = '" + fund.getCurId() + "' ";
        corpCondition += " and t.acc_state <> 'D' and t.acc_state <> 'R' ";
        List<FcAccDTO> list = finacialManualService.queryBySomeCondPgd(corpCondition);
        return list;
    }
}
