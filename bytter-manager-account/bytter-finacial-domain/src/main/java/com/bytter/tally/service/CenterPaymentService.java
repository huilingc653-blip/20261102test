package com.bytter.tally.service;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccPlusVO;
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
 * @Description: BisExcDTO
 * @date
 */
@Service("centerPaymentService")
@Transactional
public class CenterPaymentService extends BaseService<BisExcDTO, String> {
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private ITallyService tallyService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    /*@Autowired
    @Qualifier("fcAccbookService")
    private FcAccbookService fcAccbookService;*/
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    /*@Autowired
    private FcBusRaceService busRaceService;*/
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private BtNetChalkService btNetChalkService;
    @DubboReference(check = false)
    private INisMessageService iNisMessageService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    public DataTablesOutput getBisExcDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisExcService.getBisExcDataTables(dataTablesInput, user);
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageVO processAccbook(String voucherNo, String opType, String userId) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        BisExcDTO bis = bisExcService.getBisExcByVoucherNo(voucherNo);
        // todo:数据转换
        BisExcDTO bisExcDTO = BeanUtils.copyNewInstanceProperties(bis, BisExcDTO.class);
        List<VoucherRenderControlVo> controlBeans = getControlBeanEdit(2);
        if ("listTally".equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_CNT_POS_CENTPAY.get());
            entity = processTallyAccbook(bisExcDTO, ruleDefine, controlBeans, userId);
            ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, FinacialConstant.INTEREST_START_DATE_RULE);
            if (!EmptyUtils.isEmpty(configDTO)) {
                Boolean dateRule = CmsConstant.IS_TRUE.equals(configDTO.getValue());//起息日期规则
                entity.setDateRule(dateRule);
            }
        } else {
            if (EmptyUtils.isEmpty(bis.getCbsBillId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(bis.getCbsBillId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            FcAccbookDTO accbook = fcAccbookService.getAccBookVoById(bis.getCbsBillId());
            accbook.setBusGlobalId(bis.getBusGlobalId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            List<FcAccbookDtlVO> bookDtlList = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlVO.class);
            this.setEditBookDtlControlBean(bookDtlList, bisExcDTO, controlBeans, opType);
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            this.setExtData(bookExt, bisExcDTO);
            entity.setBookDtls(bookDtlList);
            entity.setBookExt(bookExt);
            FcAccbookVO bookVo = new FcAccbookVO();
            BeanUtils.copyNotNullProperties(accbook, bookVo);
            entity.setAccbook(bookVo);
        }
        return entity;
    }
    /**
     * 设置改账时会计分录的编辑状态
     */
    private void setEditBookDtlControlBean(List<FcAccbookDtlVO> accBookDtls, BisExcDTO bis, List<VoucherRenderControlVo> controlBeans, String opType) {
        VoucherRenderControlVo creditBean = controlBeans.get(0);
        creditBean.setAccountEditable(false);//默认账户不可编辑
        VoucherRenderControlVo debitBean = controlBeans.get(1);
        FcAccbookDtlVO accbookDtl = null;
        BtBankAccDTO payer = bankAccService.getBankAccInfo(bis.getPayerAcc());
        BtBankAccDTO payee = null;
        FcVoucherTypeDTO voucher = null;
        if ("listTallyCheck".equals(opType)) {
            for (FcAccbookDtlVO dtl : accBookDtls) {
                accbookDtl = dtl;
                if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                    debitBean.setSummaryEditable(false);
                    debitBean.setAccountEditable(false);
                    accbookDtl.setControlVo(debitBean);
                } else {
                    creditBean.setSummaryEditable(false);
                    accbookDtl.setControlVo(creditBean);
                }
            }
        } else { // listTallyChange
            if (!EmptyUtils.isEmpty(bis.getPolicyId())) {
                payee = bankAccService.getBankAccInfo(bis.getPayeeAcc());
            } //else { //这个 else有逻辑问题
                if (!EmptyUtils.isEmpty(bis.getVoucherTypeId())) {
                    voucher = fcVoucherTypeService.getFcVoucherTypeById(bis.getVoucherTypeId());
                    if ("10".equals(voucher.getSysVoucherCode())) {
                        //|| "33".equals(voucher.getVoucherType())){
                        payee = bankAccService.getBankAccInfo(bis.getPayeeAcc());
                    } else {
                        if ((NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) || NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) && BdlinkConstant.BUSORITYPE_48.equals(bis.getBusOriType()) && BdlinkConstant.FROM_BIS_EXC_PAY.equals(bis.getVoucherFrom())) { //
                            payee = payer;
                        } else {
                            //取付款帐号对应的父账户
                            payee = bankAccService.getParentAccByAccId(payer.getId(), bis.getCurId());
                        }
                    }
                }
            //}
            int index = isParentSign(bis, payee);
            for (FcAccbookDtlVO dtl : accBookDtls) {
                accbookDtl = dtl;
                if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                    switch (index) {
                        case 0:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        case 1:
                            //代付业务 可选择账号
                            if (NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) || NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) {
                                creditBean.setAccountEditable(true);
                            }
                            accbookDtl.setControlVo(creditBean);
                            break;
                        case 2:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        case 3:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        default:
                            accbookDtl.setControlVo(creditBean);
                            break;
                    }
                } else {
                    switch (index) {
                        case 0:
                            accbookDtl.setControlVo(creditBean);
                            break;
                        case 1:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        case 2:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        case 3:
                            accbookDtl.setControlVo(debitBean);
                            break;
                        default:
                            accbookDtl.setControlVo(creditBean);
                            break;
                    }
                }
            }
        }
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbook(BisExcDTO bis, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, String userId) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getAccbookDtls(entity, ruleDefine, bis, controlBeans, bookExt);
        this.setExtData(bookExt, bis);
        if (NetbankConstant.ISQUICK_0.equals(bis.getIsQuick())) {
            accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        } else {
            accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        }
        accBook.setRmk(bis.getPurpose());
        FcBusRaceVo race = getFcBusRaceId(bis.getCorpId(), userId);
        accBook.setBusRaceId(race.getId());
        accBook.setRaceName(race.getRaceName());
        accBook.setVoucherType(EmptyUtils.isEmpty(bis.getVoucherTypeId()) ? ruleDefine.getVoucherType() : bis.getVoucherTypeId());
        entity.setAccbook(accBook);
        entity.setBookExt(bookExt);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 设置外单位信息
     */
    public void setExtData(FcAccbookExtVO bookExt, BisExcDTO bis) {
        bookExt.setExternalCorp(bis.getPayeeAccName());//外单位户名
        bookExt.setExternalAcc(bis.getPayeeAcc());//外单位户名
        bookExt.setExternalBank(bis.getPayeeBank());//外单位开户行
        bookExt.setProv(bis.getPayeeProv());
        bookExt.setCity(bis.getPayeeCity());
        bookExt.setRmk(bis.getPurpose());
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, String userId) {
        CorpDTO corp = corpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//		List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_CENTPAY.get());
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(SysRaceCode.NB_CNT_POS_CENTPAY.get());
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
            tempRace.setRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_CENTPAY.get());
            tempRace.setRaceName(FinaBookConstant.SysRaceCode.NB_CNT_POS_CENTPAY.getName());
            tempRace.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_CENTPAY.get());
			/*FcBusRace fcBusRace = new FcBusRace();
			BeanUtils.copyProperties(tempRace,fcBusRace);*/
//			busRaceService.save(fcBusRace, new String[]{netCode},userId);
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
//			raceList = busRaceService.findBySysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_CENTPAY.get());
//			tempRace = raceList.get(0);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(SysRaceCode.NB_CNT_POS_CENTPAY.get());
            FcBusRaceDTO fcBusRaceDTO1 = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO1, tempRace);
        }
        return tempRace;
    }
    /**
     * 构造会计分录
     */
    public List<FcAccbookDtlVO> getAccbookDtls(FcAccbookPageVO entity, FcBusinessRuleDefineDTO ruleDefine, BisExcDTO bis, List<VoucherRenderControlVo> controlBeans, FcAccbookExtVO bookExt) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        VoucherRenderControlVo creditBean = controlBeans.get(0);
        creditBean.setAccountEditable(true);
        VoucherRenderControlVo debitBean = controlBeans.get(1);
        BtBankAccDTO payer = bankAccService.getBankAccInfo(bis.getPayerAcc());
        BtBankAccDTO payee = null;
        String payeeAcc = null;
        FcVoucherTypeDTO voucher = null;
        voucher = fcVoucherTypeService.getFcVoucherTypeById(bis.getVoucherTypeId());
        if (!EmptyUtils.isEmpty(bis.getPolicyId())) {
            payee = bankAccService.getBankAccInfo(bis.getPayeeAcc());
        } else {
            //收款方帐号
            if (!EmptyUtils.isEmpty(bis.getVoucherTypeId())) {
                if (NbconsoleConstant.PayType.PAY_TYPE_10.get().equals(voucher.getSysVoucherCode())) {
                    // || NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getVoucherType())){
                    payee = bankAccService.getBankAccInfo(bis.getPayeeAcc());
                } else {
                    if ((NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) || NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) && BdlinkConstant.BUSORITYPE_48.equals(bis.getBusOriType()) && BdlinkConstant.FROM_BIS_EXC_PAY.equals(bis.getVoucherFrom())) { //
                        //	payee = payer;
                        //}else{
                        //取付款帐号对应的父账户
                        //payee = bankAccService.getParentAccByAccId(payer.getId(),bis.getCurId());
                    } else if (NbconsoleConstant.PayType.PAY_TYPE_02.get().equals(voucher.getSysVoucherCode())) {
                        payee = payer;
                        //获取父账号
                        payer = bankAccService.getParentAccByAccId(payer.getId(), bis.getCurId());
                        if (payer == null) {
                            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_PARENTACCOUNT_ISNULL, payee.getBankAcc());
                        }
                    }
                }
            }
        }
        int index = 1;
        if (!NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) && !NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) {
            index = isParentSign(bis, payee);
        }
        //代付需要选择付款账号的子帐号对应的内部账号
        if (!NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) && !NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) {
            String payerCorpCode = payer.getCorpId();
            String payeeCorpCode = payee.getCorpId();
            CorpDTO payeeCorp = corpService.get(payeeCorpCode);
            Integer type = payeeCorp.getType();
            String blnctrAcc = bankAccService.getBlnctrAcc(payee.getBankAcc(), bis.getCurId());
            //判断跨网点调拨策略方式记账
            if (!payeeCorp.getId().equals(payerCorpCode) && CorpConstant.CORP_TYPE_CODE_NET == type) {//!EmptyUtils.isEmpty(bis.getPolicyId())
                Integer payeeNetSign = payeeCorp.getType();
                //if (!payeeCorp.getId().equals(payerCorpCode) && (payeeNetSign != null && payeeNetSign == 0)) {//跨网点调拨
                String payeeSuperNetCode = payeeCorp.getParentId();
                if (payeeSuperNetCode.equals(payerCorpCode)) {//说明是跨网点调拨
                    String superCurrentAcc = bankAccService.getCountertAcc(payee.getBankAcc()).get(0).getSuperCounterAcc();//关联上级内部账号
                    if (StringUtils.isEmpty(superCurrentAcc)) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL, payee.getBankAcc());
                    }
                    payeeAcc = superCurrentAcc;
                    bookExt.setStartBank(payee.getBankName());
                    bookExt.setStartCorp(payeeCorp.getName());
                    bookExt.setStartCorpAcc(blnctrAcc);//第三方账号,子账号银行活期
                } //else {
//						//企业活期
//						if (StringUtils.isEmpty(blnctrAcc)) {
//							throw new ParameterException("请先维护银行账号[" + payee.getBankAcc() + "]与内部账号的关系");
//						}
//						payeeAcc = blnctrAcc;
//					}
//				}
            } else {
                //企业活期
                payeeAcc = bankAccService.getBlnctrAcc(payee.getBankAcc(), bis.getCurId());
                if (EmptyUtils.isEmpty(payeeAcc)) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, payee.getBankAcc());
                }
            }
        }
        for (FcVoucherEntryDefineDTO entry : entrys) {
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            FcAccDTO fcAccDTO = null;
            String account = "";
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                switch (index) {
                    case 0:
                        account = bankAccService.getBlnctrAcc(bis.getPayerAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    case 1:
                        if (NbconsoleConstant.PayType.PAY_TYPE_33.get().equals(voucher.getSysVoucherCode()) || NbconsoleConstant.PayType.PAY_TYPE_35.get().equals(voucher.getSysVoucherCode())) {
                            creditBean.setAccountEditable(true);
                        } else {
                            account = payeeAcc;
                            creditBean.setAccountEditable(false);
                        }
                        accbookDtl.setControlVo(creditBean);
                        break;
                    case 2:
                        account = bankAccService.getBlnctrAcc(bis.getPayerAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    case 3:
                        account = bankAccService.getBlnctrAcc(payee.getBankAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    default:
                        accbookDtl.setControlVo(creditBean);
                        break;
                }
                accbookDtl.setDebitMoney(bis.getPayMoney());
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                switch (index) {
                    case 0:
                        accbookDtl.setControlVo(creditBean);
                        break;
                    case 1:
                        account = bankAccService.getBlnctrAcc(bis.getPayerAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    case 2:
                        account = bankAccService.getBlnctrAcc(payee.getBankAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    case 3:
                        account = bankAccService.getBlnctrAcc(payer.getBankAcc(), bis.getCurId());
                        accbookDtl.setControlVo(debitBean);
                        break;
                    default:
                        accbookDtl.setControlVo(creditBean);
                        break;
                }
                accbookDtl.setCreditMoney(bis.getPayMoney());
            }
            if (!EmptyUtils.isEmpty(account)) {
                fcAccDTO = fcAccService.getAccDTOByAccount(account);
                if (EmptyUtils.isEmpty(fcAccDTO)) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_FCACC_DATE_NOT_EXIST, account);
                }
                accbookDtl.setAccount(fcAccDTO.getBlnctrAcc());
                accbookDtl.setAccName(fcAccDTO.getAccName());
            }
            accbookDtl.setSummaryContent(bis.getPurpose());
            accbookDtl.setOrigVoucherNo(bis.getVoucherNo());
            accbookDtl.setOrigVoucherId(bis.getId());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 详细的功能描述
     */
    public int isParentSign(BisExcDTO bis, BtBankAccDTO payee) {
        if (EmptyUtils.isEmpty(payee)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_BANKACC_ISNULL);
        }
        //付款方
        BtBankAccDTO payer = bankAccService.getBankAccInfo(bis.getPayerAcc());
        String payerAccType = "";
        String payerFcAcc = bankAccService.getBlnctrAcc(bis.getPayerAcc(), bis.getCurId());
        String payeeFcAcc = "";
        payeeFcAcc = bankAccService.getBlnctrAcc(payee.getBankAcc(), bis.getCurId());
        FcAccDTO payerAccDTO = null;
        if (!EmptyUtils.isEmpty(payerFcAcc)) {
            payerAccDTO = fcAccService.getAccDTOByAccount(payerFcAcc);
            if (payerFcAcc != null) {
                payerAccType = payerAccDTO.getAccType();
            }
        }
        if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(payerAccType)) {
            //付款账号为银行活期,付款账号是父账号:借为空,贷付款账号的中心账号.否则借为付款账号的中心账号，贷为空
            if ("1".equals(payer.getParentSign())) {
                return 1;
            }
            return 0;
        } else {
            if (EmptyUtils.isEmpty(payee)) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_DATA_ERROR);
            }
            if (EmptyUtils.isEmpty(payeeFcAcc)) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, payer.getBankAcc());
            }
            FcAccDTO payeeAccDTO = fcAccService.getAccDTOByAccount(payeeFcAcc);
            if (payeeAccDTO == null) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_RELATION_BANKACC_FCACC, payee.getBankAcc());
            }
            if (payee != null && InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(payerAccType) && InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(payeeAccDTO.getAccType())) {
                //付和收都是企业活期：
                //借收款账号的中心账号，贷付款账号的中心账号
                //修改借贷方中心帐号的帐号
                return 3;
            } else if (payee != null && InnerAccountConstant.FCACC_BANK_DEMAND.equals(payeeAccDTO.getAccType())) {
                //如果收款为银行活期：
                //借付款账号的中心账号，贷收款账号的中心账号
                //修改借贷方中心帐号的帐号
                return 2;
            }
            return 4;
        }
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user) {
        BisExcDTO entity = bisExcService.getBisExcByVoucherNo(key);
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
//		fcAccbookService.submitFcAccbook(voucher);
//        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
//        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);
        voucher.getTarget().get(0).setAccbook(accbook);
        voucher.setBusGlobalId(accbook.getBusGlobalId());
        entity.setCbsBillId(accbook.getId());
        entity.setCbsBillNo(accbook.getVoucherNo());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setStatus(NetbankConstant.STATUE_TALLY_WAIT_CHECK);
        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        entity.setTallyCode(user.getLoginName());
        boolean isAutoCheck = btNetChalkService.isAutoCheck();
        iNisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT, "记账提交", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
        completeEditNotificationInfo(voucher, user);
        completeCheckNotificationInfo(voucher, user);
        if (isAutoCheck) {
            //获取自动复核员用户
            btNetChalkService.setCheckUser(voucher);
            checkFcAccbook(voucher, key, user);
        } else {
            sendCheckNotificationInfo(voucher, user);
            bisExcService.updateBisExc(entity, user);
        }
        notificationService.complete(NotificationConstant.FINACIAL.BISEXC_TALLY, entity.getId(), user.getId());
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user) {
        BisExcDTO entity = bisExcService.getBisExcByVoucherNo(key);
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
//		fcAccbookService.saveFcAccbook(voucher);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucherWrapperDTO);
        entity.setCbsBillId(accbook.getId());
        entity.setCbsBillNo(accbook.getVoucherNo());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setStatus(NetbankConstant.STATUE_TALLY_TEMP);
        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
        entity.setTallyCode(user.getLoginName());
        bisExcService.updateBisExc(entity, user);
        iNisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_TEMP, "记账暂存", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
        notificationService.complete(NotificationConstant.FINACIAL.BISEXC_TALLY, entity.getId(), user.getId());
    }
    //凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);
        voucher.setAutoAccounter(false);
        return voucher;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher) {
        createVoucher(voucher);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<FcAccbookDtlDTO>();
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount()) && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        Collections.sort(bookDtls, new Comparator<FcAccbookDtlDTO>() {
            public int compare(FcAccbookDtlDTO o1, FcAccbookDtlDTO o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
    }
    private void createVoucher(FcAccbookVoucherDTO voucher) {
        voucher.getAccbook().setBusOriType(FinaBookConstant.BusOriType.NB_CNT_POS_CENTPAY.get());
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user) {
        BisExcDTO entity = bisExcService.getBisExcByVoucherNo(key);
        String opType = fcAccbook.getOpType().name();
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        Voucher.getTarget().get(0).getAccbook().setId(entity.getCbsBillId());
        if ("MODIFY".equals(opType)) {
//			fcAccbookService.deleteFcAccbook(Voucher);
            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.deleteFcAccbook(voucherWrapperDTO);
            entity.setCbsBillId(null);
            entity.setCbsBillNo(null);
            entity.setBusGlobalId(null);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
            iNisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_BACK, "记账改账打回", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            completeEditNotificationInfo(Voucher, user);
            completeCheckNotificationInfo(Voucher, user);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getPayMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY, NotificationConstant.FINACIAL.BISEXC_TALLY, map, entity.getCorpId());
        } else if ("CHECK".equals(opType)) {
//			fcAccbookService.backFcAccbook(Voucher);
            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.backFcAccbook(voucherWrapperDTO);
            entity.setStatus(NetbankConstant.STATUE_TALLY_BACK);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            iNisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_BACK, "记账复核打回", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            completeCheckNotificationInfo(Voucher, user);
            sendBackNotificationInfo(Voucher);
        }
        bisExcService.updateBisExc(entity, user);
    }
    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user) {
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String key, ICurrentUser user) {
        BisExcDTO entity = bisExcService.getBisExcByVoucherNo(key);
		/*createVoucher(fcAccbook);
		VoucherWrapper Voucher = setVoucherWrapper(fcAccbook);*/
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getCbsBillId());
//		fcAccbookService.checkFcAccbook(Voucher);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
        iFcAccbookService.checkFcAccbook(voucherWrapperDTO);
        FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
        entity.setCbsBillNo(accbook.getVoucherNo());
        entity.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
        entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        bisExcService.updateBisExc(entity, user);
        iNisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_PASS, "记账复核通过", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
        completeCheckNotificationInfo(Voucher, user);
        completeEditNotificationInfo(Voucher, user);
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
            controBeans.add(controBean);
        }
        return controBeans;
    }
    @Override
    public BaseDao<BisExcDTO, String> getEntityDao() {
        return null;
    }
    public List<FcAccPlusDTO> getFcAcc(String voucherNo) {
        BisExcDTO bis = bisExcService.getBisExcByVoucherNo(voucherNo);
        BtBankAccDTO parentBankAcc = bankAccService.getBankAccInfo(bis.getPayerAcc());
        List<FcAccPlusVO> list = fcAccService.getSubAccFcAccByParentBankAcc(parentBankAcc.getId(), bis.getCurId(), null);
        List<FcAccPlusDTO> listDto = BeanUtils.copyListProperties(list, FcAccPlusDTO.class);
        return listDto;
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.BISEXC_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            //打回改账处，通知提醒记账人
            notificationService.complete(NotificationConstant.FINACIAL.BISEXC_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
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
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.BISEXC_CHECK, notifyVO, user.getId());
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
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.BISEXC_EDIT, notifyVO);
        }
    }
    public List<BisExcDTO> queryTallyBisEx() {
        // todo:数据转换
        List<BisExcDTO> bisExcDTOS = bisExcService.queryTallyBisEx();
        return BeanUtils.copyListProperties(bisExcDTOS, BisExcDTO.class);
    }
}
