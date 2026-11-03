package com.bytter.tally.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.api.constant.FinaBookConstant;
import com.bytter.tally.api.constant.FinacialConstant;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.api.constant.NbconsoleConstant;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("innerDepositService")
@Transactional
public class InnerDepositService {
    @DubboReference(check = false)
    private IChoInnerFinanceService iChoInnerFinanceService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    /*@Autowired
    private FcInterestService fcInterestService;*/
    @DubboReference(check = false)
    private IFcOnlineLedgerService iFcOnlineLedgerService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    @DubboReference(check = false)
    private IFcAccbookExtService iFcAccbookExtService;
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private PrecativeTallyService precativeTallyService;
    @Autowired
    private FinacialTallyRemindService finacialTallyRemindService;
    @DubboReference(check = false)
    private ICreditPeriodService creditPeriodService;

    @DubboReference(check = false)
    private IFcAccService fcAccService;

    public DataTablesOutput getDateTable(@RequestBody DataTablesInput input, String menuType, String opType, String userId) {
        // busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
        Integer busType = null;
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType)) {
            busType = 10;//活期转定期
        } else if (FinacialManualConstant.MANUAL_TYPE_REGULAR_TO_CURRENT.equals(menuType)) {
            busType = 11;//定期转活期
        } else if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            busType = 12;//活期转通知
        } else {
            busType = 13;//通知转活期
        }
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, userId);
        DataTablesOutput dataTablesOutput=iChoInnerFinanceService.listChoInnerFinanceByBusType(input, busType, "95", opType, userId, corpAuth);
        setExtendField(dataTablesOutput.getData());
        return dataTablesOutput;
    }

    private void setExtendField(List<ChoInnerFinanceDTO> choInnerFinanceDTOS) {
        List<String> rateTypes = choInnerFinanceDTOS.stream().map(ChoInnerFinanceDTO::getDepositRateType)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(rateTypes)) {
            return;
        }
        List<String> payerAccIds = choInnerFinanceDTOS.stream().map(ChoInnerFinanceDTO::getPayerAcc).collect(Collectors.toList());;
        List<FcAccDTO> accSet = fcAccService.getAccSet(payerAccIds);
        Map<String, String> collect = accSet.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
        choInnerFinanceDTOS.stream().forEach(vo->{
            vo.setPayerAccName(collect.get(vo.getPayerAcc()));
        });
        Map<String, TbCreditPeriodDTO> creditMap = creditPeriodService.listByIds(rateTypes);
        for (ChoInnerFinanceDTO choInnerFinanceDTO : choInnerFinanceDTOS) {
            choInnerFinanceDTO.setDepositRateTypeName(creditMap.get(choInnerFinanceDTO.getDepositRateType()).getPeriodName());
        }
    }

    /**
     * 构建凭证
     */
    public FcAccbookPageVO createAccbook(String id, String type, String menuType, String sysRaceCode, ICurrentUser user) {
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType)) {
            //活期转定期
            sysRaceCode = FinaBookConstant.SysRaceCode.DPS_ACTIVE_FIX.get();
        } else if (FinacialManualConstant.MANUAL_TYPE_REGULAR_TO_CURRENT.equals(menuType)) {
            //定期转活期
            sysRaceCode = FinaBookConstant.SysRaceCode.DPS_FIX_DRAW.get();
        } else if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            //活期转通知
            sysRaceCode = FinaBookConstant.SysRaceCode.DPS_ACTIVE_CALL.get();
        } else {
            //通知转活期
            sysRaceCode = FinaBookConstant.SysRaceCode.DPS_NOTIFY_DRAW.get();
        }
        return processAccbook(id, type, menuType, sysRaceCode, user);
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageVO processAccbook(String id, String opType, String menuType, String sysRaceCode, ICurrentUser user) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        ChoInnerFinanceDTO info = iChoInnerFinanceService.getInnerDepositById(id);
        List<VoucherRenderControlVo> controlBeans = new ArrayList<VoucherRenderControlVo>();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(sysRaceCode);
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            entity = processTallyAccbook(info, controlBeans, sysRaceCode, ruleDefine, user, menuType);//ok
        } else {
            if (EmptyUtils.isEmpty(info.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }

            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(info.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookVO accbook = new FcAccbookVO();
            if (!EmptyUtils.isEmpty(fcAccbookDTO)) {
                BeanUtils.copyNotNullProperties(fcAccbookDTO, accbook);
                accbook.setRaceName(fcAccbookDTO.getBusRaceName());

                List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
                List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);

                FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
                FcAccbookExtVO fcAccbookExtVO = new FcAccbookExtVO();
                BeanUtils.copyProperties(fcAccbookExtDTO, fcAccbookExtVO);
                if (bookDtls.isEmpty()) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
                }
                for (int i = 0; i < bookDtls.size(); i++) {
                    bookDtls.get(i).setControlVo(controlBeans.get(i));
                }
                entity.setAccbook(accbook);
                entity.setBookDtls(bookDtls);
                entity.setBookExt(fcAccbookExtVO);
            }
        }
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbook(ChoInnerFinanceDTO info, List<VoucherRenderControlVo> controlBeans, String sysRaceCode, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user, String menuType) {
        //凭证传输对象，用于页面显示
        FcAccbookPageVO entity = new FcAccbookPageVO();
        //账务凭证保存对象
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        List<FcAccbookDtlVO> accbookDtls = null;
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            //活期转定期  活期转通知
            accbookDtls = getAccbookDtls(info, ruleDefine, controlBeans, user, menuType);
        } else {
            //定期转活期  通知转活期
            BigDecimal interest = BigDecimal.ZERO;//利息金额
            BigDecimal accPreMoney = BigDecimal.ZERO;//冲预提金额
            FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(info.getPayerAcc());//账户余额
            BigDecimal checkBalance=BigDecimal.valueOf(0);
            if(onlineLedgerDTO!=null){
                checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
            }
            if (checkBalance.compareTo(info.getPayMoney()) > 0) {//部分取款
                interest = iFcInterestService.calTellerMoneyInterest(info.getPayerAcc(), info.getPayMoney(), null, info.getPayDate(), user);
                //accPreMoney = fcInterestService.getRushPreDrawMoney(info.getPayerAcc(), info.getPayMoney(), info.getId());//冲预提金额
                accPreMoney = iFcInterestService.getRushPreDrawMoney(info.getPayerAcc(), info.getPayMoney(), info.getId());//冲预提金额
            } else {//全部取款
                interest = iFcInterestService.calTellerMoneyInterest(info.getPayerAcc(), null, null, info.getPayDate(), user);
                //accPreMoney = fcInterestService.getRushPreDrawMoney(info.getPayerAcc(), null, info.getId());//冲预提金额
                accPreMoney = iFcInterestService.getRushPreDrawMoney(info.getPayerAcc(), null, info.getId());//冲预提金额
            }
            info.setRushMoney(accPreMoney);//预提金额
            info.setExpectInterest(interest);//利息金额
            accbookDtls = getInformAccbookDtls(info, ruleDefine, controlBeans, user, menuType);
        }
        accBook.setRmk(info.getAddWord());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(info.getCorpId(), sysRaceCode, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setNetId(info.getCorpId());
//		accBook.setTallyDate(accrecords.getBusinessDate());
        //记账日期约定就是当前系统日期
        accBook.setTallyDate(info.getCreateDate());
        accBook.setInterestStartDate(info.getInterestStartDate());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    private List<FcAccbookDtlVO> getAccbookDtls(ChoInnerFinanceDTO info, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, ICurrentUser userVo, String menuType) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
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
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                groupNum = entry.getGroupNum().intValue();
                VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
                /**
                 * 活期转定期
                 * 借：2120企业活期存款 申请单位
                 *贷：2130企业定期存款 申请单位
                 *
                 * 活期转通知
                 * 借：2120企业活期存款 申请单位
                 贷：2140企业通知存款 申请单位
                 */
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {//余额方向-借方
                    if (null != entry.getSubjectCode()) {
                        CorpDTO corp = iCorpService.get(info.getCorpId());
                        FcAccDTO dto = innerAccountService.getByBlnctrAcc(info.getPayerAcc());
                        if (dto == null) {
                            if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType)) {
                                throw new ParameterException(ChoProductMessageEnum.CHO_INNER_CURRENT_ACCOUNT_IS_NOT);//企业活期存款
                            } else {
                                throw new ParameterException(ChoProductMessageEnum.CHO_INNER_INFORM_ACCOUNT_IS_NOT);//企业活期存款
                            }
                        }
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setAccName(dto.getAccName());
                        accbookDtl.setDebitMoney(info.getPayMoney());
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                        bean.setAccountEditable(false);
                    }
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                    bean.setCreateAccountAble(true);
                    //todo 关联活期账号为活期转定期选择的活期账号，该定期账号的开户日期>=活转定的起息日期，定期账号的到期日期>=活转定的到期日期
                    List<FcAccDTO> dtos = innerAccountService.listAccountByParams(info.getCurId(), info.getCorpId(), info.getCorpId(), entry.getSubjectCode());
                    for (FcAccDTO dto : dtos) {
                        if (checkDate(dto, info.getInterestStartDate(), info.getDeadlineDate())) {
                            bean.setCreateAccountAble(false);
                            accbookDtl.setAccount(dto.getBlnctrAcc());
                            accbookDtl.setAccName(dto.getAccName());
                            break;
                        }
                    }
                    accbookDtl.setCreditMoney(info.getPayMoney());
                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                }
                accbookDtl.setCurCode(info.getCurId());
                accbookDtl.setOrigVoucherNo(info.getBillNo());
                accbookDtl.setOrigVoucherId(info.getId());
                accbookDtl.setSummaryContent(info.getPurpose());
                accbookDtls.add(accbookDtl);
                accbookDtl.setControlVo(bean);
            }
        }
        return accbookDtls;
    }

    private boolean checkDate(FcAccDTO dto,Date interestStartDate,Date deadlineDate ){
        if (dto.getAccStartDate() != null && dto.getDeadlineDate() != null) {
            return dto.getAccStartDate().compareTo(interestStartDate) >= 0 && dto.getDeadlineDate().compareTo(deadlineDate) >= 0;
        }
        return false;
    }

    /**
     * 通知转活期
     */
    private List<FcAccbookDtlVO> getInformAccbookDtls(ChoInnerFinanceDTO info, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, ICurrentUser userVo, String menuType) {
        //本金转入账号
        String capitalAcc = info.getCapitalAcc();
        //利息转入账号
        String interestAcc = info.getInterestAcc();
        String finAcc = info.getPayerAcc();//通知或定期账号
        FcAccDTO finAccDTO = iFcAccService.getAccDTOByAccount(finAcc);
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
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
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
            /**
             * 通知转活期
             * 借：521003利息支出—企业通知存款 冲预提   预提金额
             * 贷：201003应付利息-通知存款 冲预提  预提金额
             * 借：2140企业通知存款 申请单位  支取金额
             * 借：521003利息支出—企业通知存款 结算中心  结息金额
             * 贷：2120企业活期存款（本金转入账号） 申请单位  支取金额
             * 贷：2120企业活期存款 （利息转入账号）申请单位   结息金额
             *
             * 定期转活期
             * 借：521001利息支出—企业定期存款   冲预提
             * 贷：201002应付利息-企业定期存款    冲预提
             * 借：2130企业定期存款 申请单位
             * 借：521001利息支出—企业定期存款  结息
             * 贷：2120企业活期存款（本金转入账号） 申请单位  支取金额
             * 贷：2120企业活期存款 （利息转入账号）申请单位   结息金额
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == entry.getBalanceWay().intValue() && FinacialConstant.ENTRY_TYPE_10 == entry.getEntryType().intValue()) {
                accbookDtl.setAccount(info.getPayerAcc());
                accbookDtl.setAccName(info.getPayerAccName());
                accbookDtl.setDebitMoney(info.getPayMoney());
                bean.setAccountEditable(false);
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT) == entry.getBalanceWay().intValue() && FinacialConstant.ENTRY_TYPE_10 == entry.getEntryType().intValue()) {
                bean.setAccountEditable(false);
                FcAccDTO dto = null;
                //本金转入账号等于利息转入账号
                if (capitalAcc.equals(interestAcc)) {
                    accbookDtl.setCreditMoney(info.getPayMoney().add(info.getExpectInterest()));//支取金额+利息
                    dto = iFcAccService.getAccDTOByAccount(capitalAcc);//info.getPayeeAcc()
                    accbookDtl.setAccount(info.getPayeeAcc());
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    accbookDtl.setCreditMoney(info.getPayMoney());//本金转入金额
                    dto = iFcAccService.getAccDTOByAccount(capitalAcc);
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    accbookDtl.setCurCode(info.getCurId());
                    accbookDtl.setOrigVoucherNo(info.getBillNo());
                    accbookDtl.setOrigVoucherId(info.getId());
                    accbookDtl.setSummaryContent(info.getPurpose());
                    accbookDtls.add(accbookDtl);
                    accbookDtl.setControlVo(bean);
                    if (info.getExpectInterest().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    accbookDtl = new FcAccbookDtlVO();
                    accbookDtl.setDebitMoney(BigDecimal.ZERO);
                    accbookDtl.setCreditMoney(info.getExpectInterest());//本金转入金额
                    dto = iFcAccService.getAccDTOByAccount(interestAcc);
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                }
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT) == entry.getBalanceWay().intValue() && FinacialConstant.ENTRY_TYPE_30 == entry.getEntryType().intValue()) {//冲预提 应付利息
                if ((BigDecimal.ZERO.subtract(info.getRushMoney())).compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (EmptyUtils.isNotEmpty(finAccDTO.getInterestRecPay())) {//应付利息
                    FcAccDTO dto = iFcAccService.getAccDTOByAccount(finAccDTO.getInterestRecPay());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    bean.setAccountEditable(false);
                } else {
                    if (null != entry.getSubjectCode()) {
                        bean.setAccountEditable(true);
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                        if ((BigDecimal.ZERO.subtract(info.getRushMoney())).compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }
                    }
                }
                accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(info.getRushMoney()));
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT) == entry.getBalanceWay().intValue() && FinacialConstant.ENTRY_TYPE_30 == entry.getEntryType().intValue()) {//冲预提 利息支出
                if ((BigDecimal.ZERO.subtract(info.getRushMoney())).compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (EmptyUtils.isNotEmpty(finAccDTO.getInterestInExp())) {//利息支出
                    FcAccDTO dto = iFcAccService.getAccDTOByAccount(finAccDTO.getInterestInExp());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    bean.setAccountEditable(false);
                } else {
                    if (null != entry.getSubjectCode()) {
                        bean.setAccountEditable(true);
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                    }
                }
                accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(info.getRushMoney()));
            } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT) == entry.getBalanceWay().intValue() && FinacialConstant.ENTRY_TYPE_40 == entry.getEntryType().intValue()) {//处理利息分录 利息支出
                if (info.getExpectInterest().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (EmptyUtils.isNotEmpty(finAccDTO.getInterestInExp())) {//利息支出
                    FcAccDTO dto = iFcAccService.getAccDTOByAccount(finAccDTO.getInterestInExp());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    bean.setAccountEditable(false);
                } else {
                    if (null != entry.getSubjectCode()) {
                        bean.setAccountEditable(true);
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                    }
                }
                accbookDtl.setDebitMoney(info.getExpectInterest());
            }
            // 校验账户信息 //标准产品没有，暂注释
            /*if (ObjectUtil.isEmpty(accbookDtl.getAccount())){
                FcSubjectDTO subject = iFcSubjectService.findSubjectById(entry.getSubjectCode());
                throw new BusinessException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_ISNULL,info.getCorpName(),subject.getSubjectName());
            }*/
            accbookDtl.setCurCode(info.getCurId());
            accbookDtl.setOrigVoucherNo(info.getBillNo());
            accbookDtl.setOrigVoucherId(info.getId());
            accbookDtl.setSummaryContent(info.getPurpose());
            accbookDtls.add(accbookDtl);
            accbookDtl.setControlVo(bean);
        }
        return accbookDtls;
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, ICurrentUser user, String opType, String menuType, String id) {
        check(fcAccbook);
        validata(fcAccbook.getAccbook().getBusGlobalId());//核对有没有复核过
        BigDecimal itsRate = BigDecimal.ZERO;
        String payeeAcc = "";
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            if (fcAccbook.getBookDtls() != null && fcAccbook.getBookDtls().size() > 1 && fcAccbook.getBookDtls().get(1).getAccDTO() != null) {
                itsRate = fcAccbook.getBookDtls().get(1).getAccDTO().getInterestRate();
                payeeAcc = fcAccbook.getBookDtls().get(1).getAccDTO().getBlnctrAcc();
            }
        }
        ChoInnerFinanceDTO entity = iChoInnerFinanceService.getInnerDepositById(id);
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, entity.getCreateBy());
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        VoucherWrapperDTO voucherWrapperDTO = iFcAccbookService.saveVoucher(voucher);
        FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
//        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            FcAccbookDtlDTO bookDtl = voucher.getTarget().get(0).getBookDtls().get(1);
            entity.setPayeeAcc(bookDtl.getAccount());
            FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(bookDtl.getAccount());
            entity.setPayeeAccId(accDTO.getId());
        }
        entity.setStatus(InnerFinanceConstant.STATUE_TALLY_TEMP);
        entity.setOrderNo(accbook.getOrderNo());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            entity.setInterestRate(itsRate);
            entity.setPayeeAcc(payeeAcc);
        }
        iChoInnerFinanceService.updateInnerFinance(entity, user);
        notificationService.complete(NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, entity.getId(), user.getId());
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String menuType, ICurrentUser user, String pageType, BigDecimal interest) {
        check(fcAccbook);
        boolean isAutoCheck = this.isAutoCheck();
        String opType = fcAccbook.getOpType().name();
        //开户
        BigDecimal itsRate = BigDecimal.ZERO;
        String payeeAcc = "";
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            if (fcAccbook.getBookDtls() != null && fcAccbook.getBookDtls().size() > 1) {
                FcAccbookDtlDTO secondBookDtl = fcAccbook.getBookDtls().get(1);
                payeeAcc = secondBookDtl.getAccount();
                if (StringUtils.isNotEmpty(payeeAcc)) {
                    FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(payeeAcc);
                    if (null != accDTO) {
                        itsRate = accDTO.getInterestRate();
                    } else if (null != secondBookDtl.getAccDTO()) {
                        itsRate = secondBookDtl.getAccDTO().getInterestRate();
                    }
                } else {
                    if (null != secondBookDtl.getAccDTO()) {
                        itsRate = secondBookDtl.getAccDTO().getInterestRate();
                        payeeAcc = secondBookDtl.getAccDTO().getBlnctrAcc();
                    }
                }
            }
        }
        validata(fcAccbook.getAccbook().getBusGlobalId());
        ChoInnerFinanceDTO entity = iChoInnerFinanceService.getInnerDepositById(id);
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, entity.getCreateBy());
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            //voucher.setAutoCheck(true);
            if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
                String netId = corpService.getNetByCorpId(entity.getCorpId()).getNetId();
                List<String> users = precativeTallyService.getAutoCheckUser(netId);
                FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(1).getAccount());
                setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
            }
        }
        com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getPayerAcc());//账户余额
        VoucherWrapperDTO voucherWrapperDTO=iFcAccbookService.submitVoucher(voucher);
        FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
        if (isAutoCheck) {
            //记账自动复核流水号缺失
            voucherWrapperDTO.getBookVoucher().setAccbook(accbook);
            if (interest != null && interest.compareTo(BigDecimal.ZERO) != 0) {
                //将利息存入利息明细表
                //saveFcInterestDtl(voucher.getVoucherNo(),accbook.getOrderNo(), entity, interest, user.getId());
                processInterest(entity, voucherWrapperDTO, user, onlineLedgerDTO);
            }
            voucherWrapperDTO.setAutoCheck(true);
            iFcAccbookService.checkVoucher(voucherWrapperDTO);
        }
        entity.setOrderNo(accbook.getOrderNo());
        entity.setVoucherNo(accbook.getVoucherNo());
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            if (StringUtils.isNotEmpty(payeeAcc)) {
                entity.setPayeeAcc(payeeAcc);
                entity.setInterestRate(itsRate);
                FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(payeeAcc);
                entity.setPayeeAccId(accDTO.getId());
            }
        }
        entity.setTallyCode(accbook.getOperatorCode());
        if (isAutoCheck) {
            entity.setStatus(InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        } else {
            entity.setStatus(InnerFinanceConstant.STATUE_TALLY_WAIT_CHECK);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        }
        iChoInnerFinanceService.updateInnerFinance(entity, user);
        if (isAutoCheck) {
            //查询通知取款最近的通知预约记录，如有则更新为已取款
            if (FinacialManualConstant.MANUAL_TYPE_INFORM_TO_CURRENT.equals(menuType)) {
                iChoInnerFinanceService.updateLastN2BData(entity);
            }
        }
        // 如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucherWrapperDTO, user, pageType);
        completeCheckNotificationInfo(voucherWrapperDTO, user, pageType);
        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucherWrapperDTO, user, pageType);
        }
        notificationService.complete(NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, entity.getId(), user.getId());
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType, String pageType) {
        createVoucher(fcAccbook, menuType);
        String opType = fcAccbook.getOpType().name();
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getCorpId());
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user, pageType);
        completeCheckNotificationInfo(Voucher, user, pageType);
        FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
        ChoInnerFinanceDTO entity = iChoInnerFinanceService.getInnerDepositById(id);
        entity.setOrderNo(accbook.getOrderNo());
        //entity.setTallyCode(accbook.getOperatorCode());
        if ("MODIFY".equals(opType)) {
            validata(Voucher.getBusGlobalId());
            iFcAccbookService.deleteVoucher(Voucher);
            if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
                String account = fcAccbook.getBookDtls().get(1).getAccount();
                innerAccountService.cancelFcAcc(account, fcAccbook.getAccbook().getTallyDate(), user);//账号销户
            }
            entity.setStatus(InnerFinanceConstant.STATUE_TALLY_CHANG_BACK);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getPayMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, map, entity.getCorpId());
        } else if ("CHECK".equals(opType)) {
            validataBack(Voucher.getBusGlobalId());
            iFcAccbookService.backVoucher(Voucher);
            entity.setStatus(InnerFinanceConstant.STATUE_TALLY_BACK);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            sendBackNotificationInfo(Voucher, pageType);
        } else if ("BACK".equals(opType)) {//记账打回
            //String backReason=fcAccbook.getBookExt().getBackReason();
            entity.setStatus(InnerFinanceConstant.STATUE_BACK);
//            entity.setTallySign(""); // 数据库表字段为 char 不能为 空格
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            sendBackNotificationInfo(Voucher, pageType);
            iChoInnerFinanceService.tallyBackMessage(entity.getId(), user);
            finacialTallyRemindService.deleteByBusId(entity.getId());
            notificationService.complete(NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, entity.getId(), user.getId());
        }
        iChoInnerFinanceService.updateInnerFinance(entity, user);
    }
    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType, String pageType, BigDecimal interest) {
        createVoucher(fcAccbook, menuType);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user, menuType, pageType, interest);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String id, ICurrentUser user, String menuType, String pageType, BigDecimal interest) {
        validata(Voucher.getBusGlobalId());
        validataBack(Voucher.getBusGlobalId());
        ChoInnerFinanceDTO entity = iChoInnerFinanceService.getInnerDepositById(id);
        com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getPayerAcc());//账户余额
        //先处理利息
        if (interest != null && interest.compareTo(BigDecimal.ZERO) != 0) {
            //将利息存入利息明细表
            //saveFcInterestDtl(Voucher.getVoucherNo(),accbook.getOrderNo(), entity, interest, user.getId());
            processInterest(entity, Voucher, user, onlineLedgerDTO);
        }
        iFcAccbookService.checkVoucher(Voucher);
        FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
        entity.setOrderNo(accbook.getOrderNo());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setStatus(InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        iChoInnerFinanceService.updateInnerFinance(entity, user);
        //查询通知取款最近的通知预约记录，如有则更新为已取款
        if (FinacialManualConstant.MANUAL_TYPE_INFORM_TO_CURRENT.equals(menuType)) {
            iChoInnerFinanceService.updateLastN2BData(entity);
        }
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            List<String> users = new ArrayList<>();
            users.add(user.getId());
            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(Voucher.getBookVoucher().getBookDtls().get(1).getAccount());
            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
        }
        completeCheckNotificationInfo(Voucher, user, pageType);
        completeEditNotificationInfo(Voucher, user, pageType);
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
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount()) && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
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
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoAccounter(false);
        return voucher;
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
        return entity;
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, String sysRaceCode, String userId) {
        CorpDTO corp = corpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//        List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(sysRaceCode);
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceCode);
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        if (raceList.size() < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_BUSRACE_ISNULL);
        }
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
            tempRace.setRaceCode(raceList.get(0).getRaceCode());
            tempRace.setRaceName(raceList.get(0).getRaceName());
            tempRace.setSysRaceCode(raceList.get(0).getSysRaceCode());
            /*FcBusRace fcBusRace = new FcBusRace();
            BeanUtils.copyProperties(tempRace, fcBusRace);
            busRaceService.save(fcBusRace, new String[]{netCode}, userId);
            raceList = busRaceService.findBySysRaceCode(sysRaceCode);
            tempRace = raceList.get(0);*/
            FcBusRaceDTO fcBusRace = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRace);
            iFcBusRaceService.save(fcBusRace, new String[]{netCode}, userId);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysRaceCode);
            FcBusRaceDTO fcBusRaceDTO = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO, tempRace);
        }
        return tempRace;
    }
    private void validata(String busGlobalId) {
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
//            FcAccbook info = fcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            if (fcAccbookDTO != null) {
                if (fcAccbookDTO.getVoucherState().equals("N")) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                }
            } else {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
        }
    }
    private void validataBack(String busGlobalId) {
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
//            FcAccbook info = fcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            if (fcAccbookDTO != null) {
                if (fcAccbookDTO.getVoucherState().equals("F") || fcAccbookDTO.getVoucherType().equals("")) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                }
            } else {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
        }
    }
    private void createVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        voucher.getAccbook().setBusOriType(getOriType(menuType));
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String menuType) {
        ConstantBean<Integer> busOriType = FinacialManualConstant.MANUALTYPE_BUSORITYPE_MAP.get(menuType);
        return busOriType.getValue();
    }
    /**
     * 内部帐号开户
     */
    private void createFcAcc(List<FcAccbookDtlDTO> bookDtls, ICurrentUser user, String opType, String createBy) {
        createBy = userService.getByLoginName(createBy).getId();
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            for (FcAccbookDtlDTO dtl : bookDtls) {
                com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtl.getAccDTO();
                if (!EmptyUtils.isEmpty(accDTO)) {
                    if ("SUBMIT".equals(opType)) {
                        accDTO.setAutoCheck(true);
                        // todo:数据转换
                        FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);
                        iInnerAccountService.saveOrUpdate(fcAccDTO, user);
                        dtl.setAccDTO(null);
                        FcAccDTO newAcc = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        List<String> users = new ArrayList<>();
                        users.add(user.getId());
                        users.add(createBy);
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
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            @Override
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
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
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.INNER_DEPOSIT_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            notificationService.complete(NotificationConstant.FINACIAL.INNER_DEPOSIT_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
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
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.INNER_DEPOSIT_CHECK, notifyVO, user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher, String pageType) {
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
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.INNER_DEPOSIT_EDIT, notifyVO);
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
     * <br>
     * <b>功能：</b> 复核通过后利息处理 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    public void processInterest(ChoInnerFinanceDTO info, VoucherWrapperDTO voucher, ICurrentUser user, com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO onlineLedgerDTO) {
        //复核通过,处理利息及冲预提
        //FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(info.getPayerAcc());//账户余额
        BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
        if (checkBalance.compareTo(info.getPayMoney()) > 0) {//部分取款
            iFcInterestService.processTellerMoneyBalance(info.getPayerAcc(), info.getPayMoney(), voucher.getBookVoucher().getAccbook().getOrderNo(), user, info.getId(), info.getPayDate());
            //iFcInterestService.processRushPredraw(info.getPayerAcc(), user, info.getId(), info.getPayMoney(), voucher.getBookVoucher().getAccbook().getTallyDate());
        } else {//全部取款
            iFcInterestService.processTellerMoneyBalance(info.getPayerAcc(), null, voucher.getBookVoucher().getAccbook().getOrderNo(), user, info.getId(), info.getPayDate());
            //iFcInterestService.processRushPredraw(info.getPayerAcc(), user, null, info.getPayMoney(), voucher.getBookVoucher().getAccbook().getTallyDate());
        }
    }
    public void saveFcInterestDtl(String voucherNo, String orderNo, ChoInnerFinanceDTO choDto, BigDecimal interest, String userId) {
        List<FcInterestDtlDTO> fcInterestDtlList = new ArrayList<>();
        //String voucherNo=this.submitVoucher(vo, listDebitFcAcc.get(0));
        FcInterestDtlDTO fcInterestDtlDTO = new FcInterestDtlDTO();
        fcInterestDtlDTO.setInterestTime(DateUtils.getDateYMD());
        //获取定期/通知账号
        FcAccDTO fcAcc = innerAccountService.getByBlnctrAcc(choDto.getPayerAcc());
        fcInterestDtlDTO.setAccount(fcAcc.getBlnctrAcc());
        fcInterestDtlDTO.setCurCode(fcAcc.getCurId());
        fcInterestDtlDTO.setInterestType(InterestConstant.INSTRT_TYPE_NORMAL);
        fcInterestDtlDTO.setGenerateMode(InterestConstant.GENERATEMODE_INTEREST_PART);
        fcInterestDtlDTO.setInterestStartDate(choDto.getInterestStartDate());
        fcInterestDtlDTO.setBlnInstDate(DateUtils.addDay(choDto.getPayDate(), -1));
        //FcOnlineLedgerDTO bal = iFcOnlineLedgerService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
        /*
        if (bal != null) {
            fcInterestDtlDTO.setCumulation(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
        } else {
            fcInterestDtlDTO.setCumulation(BigDecimal.ZERO);
        }
        */
        fcInterestDtlDTO.setInterestRate(choDto.getInterestRate());
        //利息=存款金额*（到期日期-起息日期）*利率/360
        int dayLimit = DateUtils.dayDiffs(choDto.getPayDate(), choDto.getInterestStartDate());
        fcInterestDtlDTO.setCumulation(choDto.getPayMoney().multiply(new BigDecimal(dayLimit)));
        fcInterestDtlDTO.setDays(dayLimit);
        fcInterestDtlDTO.setInterest(interest);
        fcInterestDtlDTO.setRepayMoney(choDto.getPayMoney());
        fcInterestDtlDTO.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
        fcInterestDtlDTO.setTallyDate(DateUtils.getDateYMD());
        fcInterestDtlDTO.setCreateTime(DateUtils.getDate());
        fcInterestDtlDTO.setCreateBy(userId);
        fcInterestDtlDTO.setOperatorCode(userId);
        fcInterestDtlDTO.setVoucherNo(voucherNo);
        fcInterestDtlDTO.setOrderNo(orderNo);
        fcInterestDtlList.add(fcInterestDtlDTO);
        this.iFcInterestService.saveFcInterestDtl(fcInterestDtlList, userId);
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
     * 查询待记账数据
     */
    List<ChoInnerFinanceDTO> queryTallyByBusType(String menuType) {
        Integer busType = null;
        if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_REGULAR.equals(menuType)) {
            busType = 10;//活期转定期
        } else if (FinacialManualConstant.MANUAL_TYPE_REGULAR_TO_CURRENT.equals(menuType)) {
            busType = 11;//定期转活期
        } else if (FinacialManualConstant.MANUAL_TYPE_CURRENT_TO_INFORM.equals(menuType)) {
            busType = 12;//活期转通知
        } else {
            busType = 13;//通知转活期
        }
        return iChoInnerFinanceService.queryTallyByBusType(busType);
    }
}
