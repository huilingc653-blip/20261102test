package com.bytter.tally.service;

import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant.PayType;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SysSubject;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.biz.finacial.api.service.IFcAccbookExtService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.util.FinacialManualUtil;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * 付款单手工账
 */
@Service("NisBillheadManualService")
@Transactional
public class NisBillheadManualService extends FinacialManualService {
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IBisExcService iBisExcService;
	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;
	@DubboReference(check = false)
	private INisBillheadService billHeadService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@Autowired
	private FinacialManualService finacialManualService;
	@DubboReference(check = false)
	private IInnerAccountService iInnerAccountService;
	@DubboReference(check = false)
	private IFcAccbookDtlService iFcAccbookDtlService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference
	private IFcAccbookExtService iFcAccbookExtService;
    @DubboReference(check = false)
	private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private INisMessageService iNisMessageService;
	// 查询数据
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDateTable(DataTablesInput dataTablesInput, ICurrentUser user) {
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		return billHeadService.getDataTablesUnion(dataTablesInput, user);
	}
	/**
	 * 一般付款构造accbook
	 */
	public FcAccbookPageVO processAccbook(String key, String type, ICurrentUser user) {
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		if(EmptyUtils.isEmpty(finacialManualService)){
			finacialManualService = (FinacialManualService) ApplicationContextUtil.getContext().getBean("finacialManualService");
		}
		if(EmptyUtils.isEmpty(iFcVoucherTypeService)){
			iFcVoucherTypeService = (IFcVoucherTypeService) ApplicationContextUtil.getContext().getBean("fcVoucherTypeService");
		}
		if(EmptyUtils.isEmpty(iFcAccbookService)){
			iFcAccbookService = (IFcAccbookService) ApplicationContextUtil.getContext().getBean("fcAccbookServiceI");
		}
		if(EmptyUtils.isEmpty(iFcAccbookDtlService)){
			iFcAccbookDtlService = (IFcAccbookDtlService) ApplicationContextUtil.getContext().getBean("fcAccbookDtlServiceI");
		}
		if(EmptyUtils.isEmpty(iFcAccbookExtService)){
			iFcAccbookExtService = (IFcAccbookExtService) ApplicationContextUtil.getContext().getBean("fcAccbookExtServiceI");
		}
		FcAccbookPageVO entity = new FcAccbookPageVO();
		NisBillheadDTO nisBillhead = billHeadService.getNisBillheadByBillNo(key);
		List<VoucherRenderControlVo> controlBeans = finacialManualService.getControlBeanEdit(2);
        //bookDtls = buildUpVoucher(listDefine, nisBillhead, sysVoucherCode);//组装后的自定义会计分录
		FcVoucherTypeDTO voucherType = iFcVoucherTypeService.getFcVoucherTypeById(nisBillhead.getVoucherTypeId());
		String sysVoucherCode = voucherType.getSysVoucherCode();
        if (NbconsoleConstant.OpType.INIT.get().equals(type)) {
        	FcBusinessRuleDefineDTO ruleDefine = finacialManualService.getBusinessRuleExp(getSysRaceCode(sysVoucherCode,nisBillhead).getValue());
        	entity = processTallyAccbook(nisBillhead, controlBeans, ruleDefine, sysVoucherCode, user, type);
        }else{
        	if(EmptyUtils.isEmpty(nisBillhead.getOrderNo())){
        		throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
        	}
			FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(nisBillhead.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
			FcAccbookVO accbook = new FcAccbookVO();
        	if(!EmptyUtils.isEmpty(fcAccbookDTO)){
        		BeanUtils.copyNotNullProperties(fcAccbookDTO, accbook);
        		accbook.setRaceName(fcAccbookDTO.getRaceName());
        	}
			List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
			List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
			FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
			FcAccbookExtVO bookExt = new FcAccbookExtVO();
			BeanUtils.copyProperties(fcAccbookExtDTO,bookExt);
			if(null == bookDtls || bookDtls.size() == 0){
            	throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
			}
        	if (type.equals("Edit")) {
                editableData(bookDtls, sysVoucherCode,nisBillhead);
            }else{
            	for (int i = 0; i < bookDtls.size(); i++) {
            		bookDtls.get(i).setControlVo(controlBeans.get(i));
				}
            }
        	if(NetbankConstant.ISQUICK_0.equals(nisBillhead.getIsQuick())){
        		accbook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
            }else{
            	accbook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
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
    public FcAccbookPageVO processTallyAccbook(NisBillheadDTO nisBillhead, List<VoucherRenderControlVo> controlBeans,
											   FcBusinessRuleDefineDTO ruleDefine, String sysRaceCode, ICurrentUser user, String type) {
    	FcAccbookPageVO entity = new FcAccbookPageVO();
        List<FcAccbookDtlVO> accbookDtls = getAccbookDtls(nisBillhead, ruleDefine, controlBeans, sysRaceCode);
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
		bookExt.setProv(nisBillhead.getProv());
		bookExt.setCity(nisBillhead.getCity());
        bookExt.setExternalProv(nisBillhead.getProv());
        bookExt.setExternalCity(nisBillhead.getCity());
        bookExt.setExternalAcc(nisBillhead.getPayeeAcc());//外单位账号
        bookExt.setExternalCorp(nisBillhead.getPayeeAccName());//外单位名
        bookExt.setExternalBank(nisBillhead.getPayeeBank());
        bookExt.setPaysysbankcode(nisBillhead.getPayeeBankCode());//收款方联行号
        bookExt.setRmk(nisBillhead.getPurpose());
        bookExt.setPayerAcc("");//FIXME 付款账号
        FcAccbookVO accBook = new FcAccbookVO();
        FcBusRaceVo fcbusRace = finacialManualService.getFcBusRaceId(nisBillhead.getCorpId(), getSysRaceCode(sysRaceCode,nisBillhead) ,user.getId());
        String voucher = EmptyUtils.isEmpty(nisBillhead.getVoucherTypeId()) ? ruleDefine.getVoucherType() : nisBillhead.getVoucherTypeId();
        if(NetbankConstant.ISQUICK_0.equals(nisBillhead.getIsQuick())){//加急标志
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        }else{
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        }
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(voucher);
        accBook.setBusOriType(getOriType());
        accBook.setRmk(nisBillhead.getPurpose());
        accBook.setPostscript(nisBillhead.getAddWord());//附言
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
	 * 复核逻辑
     * 功能介绍 : 设置复核参数，是否产生指令等。<br>
     * <b>日期：</b>2018-05-16<br>
     * @version 1.0
     * @author liyh
     */
	public void setCheckVoucherInfo(VoucherWrapperDTO wrapperDTO, ICurrentUser user, String billNo) {
		if(EmptyUtils.isEmpty(configService)){
    		configService = (IConfigService) ApplicationContextUtil.getContext().getBean("configServiceI");
		}
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		if(EmptyUtils.isEmpty(iFcVoucherTypeService)){
			iFcVoucherTypeService = (IFcVoucherTypeService) ApplicationContextUtil.getContext().getBean("fcVoucherTypeServiceI");
		}
		if(EmptyUtils.isEmpty(iBtBankAccService)){
			iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccServiceImpl");
		}
		boolean paymentIsAccount = CmsConstant.IS_FALSE.equals(configService
				.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue());
		if (paymentIsAccount) {// 先记账后付款
			if (!EmptyUtils.isEmpty(billNo)) {
				NisBillheadDTO billheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
				if (!CommonConstant.VALID_SIGN_YES.equals(billheadDTO.getIsPayed())) {// 未付款
					//内转不产生指令
					// 产生指令
					wrapperDTO.setCreateBisExc(!PayType.PAY_TYPE_05.getValue().equals(billheadDTO.getSysVoucherCode()));// 不产生指令
					wrapperDTO.setUser(user);
					if (NetbankConstant.FORINDIVIDUAL_1.equals(billheadDTO.getIsForindividual())) {
						wrapperDTO.setIsForindividual(BdlinkConstant.TRANS_MODE_PRIVATE);//对私
					}
					FcVoucherTypeDTO voucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(billheadDTO.getVoucherTypeId());
					if (NetbankConstant.PAY_TYPE_02.equals(voucherTypeDTO.getSysVoucherCode())) {
						wrapperDTO.setPayerAcc(billheadDTO.getDownAccId());//转付取下拨账号
					} else {
						//取贷方银行活期对应的银行账号
						FcAccbookVoucherDTO voucherDTO = wrapperDTO.getBookVoucher();
						if (null == voucherDTO) {
							if (EmptyUtils.isEmpty(billheadDTO.getOrderNo())) {
								return;
							}
							voucherDTO = transVoucher(billheadDTO.getOrderNo());//付款凭证
						}
						List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
						String creditAcc = "";
						for (FcAccbookDtlDTO dtlDTO : bookDtls) {
							if (dtlDTO.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
								creditAcc = dtlDTO.getAccount();//取贷方账号
							}
						}
						List<BtBankAccDTO> accList = iBtBankAccService.getBankAccByCountAcc(creditAcc, billheadDTO.getCurId());
						if (EmptyUtils.isEmptyList(accList)) {
							wrapperDTO.setCreateBisExc(false); //是否产生指令
							return;
						}
						wrapperDTO.setPayerAcc(accList.get(0).getId());//付款账号ID
					}
					//对私付款根据付款账号设置付款单的bifCode和对私明细的bifCode及跨行标识
					if (NetbankConstant.FORINDIVIDUAL_1.equals(billheadDTO.getIsForindividual())) {//对私付款
						if (EmptyUtils.isNotEmpty(wrapperDTO.getPayerAcc())) {
							//更新接口代码及同行跨行标识
							billHeadService.updateNisBillheadForindividual(billNo, wrapperDTO.getPayerAcc());
						}
					}
				}
			}
		}
	}
    /**
     * 设置外单位信息
     * @param bookExt
     * @param nisBillhead
     */
    @SuppressWarnings("unused")
	private void setBookExt(FcAccbookExtVO bookExt, NisBillheadDTO nisBillhead, String type){
    	bookExt.setProv(nisBillhead.getProv());
        bookExt.setCity(nisBillhead.getCity());
		bookExt.setExternalProv(nisBillhead.getProv());
		bookExt.setExternalCity(nisBillhead.getCity());
        bookExt.setExternalAcc(nisBillhead.getPayeeAcc());//外单位账号
        bookExt.setExternalCorp(nisBillhead.getPayeeAccName());//外单位名
        bookExt.setExternalBank(nisBillhead.getPayeeBank());
        bookExt.setPaysysbankcode(nisBillhead.getPayeeBankCode());//收款方联行号
        bookExt.setRmk(nisBillhead.getPurpose());
        String payType = "";
        //自动复核或复核
        /**
         * 付款方式不为 内转  资金调拨 活转定 活转通知 需要外单位的联行号或者银行类型
         */
        if(isAutoCheck() || "".equals(type)){
        	if ((!PayType.PAY_TYPE_05.get().equals(payType)
                            && !PayType.PAY_TYPE_37.get().equals(payType)&& !PayType.PAY_TYPE_30.get().equals(payType)
                            && !PayType.PAY_TYPE_31.get().equals(payType)) && !PayType.PAY_TYPE_13.get().equals(payType)) {
            	if(!NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())){
            		String payeeAcc = nisBillhead.getPayeeAcc();
            		String bankCodeParam = "";//付款是否必须选择联行号
            		if(PayType.PAY_TYPE_10.get().equals(payType)){
            			BtBankDTO bankAccInfo = null;
            			if(bankAccInfo == null){
            				//throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护，请先维护账号信息!");
            			}
            			/*if(MustEnterBankCodeMode.YES.get().equals(bankCodeParam)){
            				if(StringUtil.isBlank_new(bankAccInfo.getPaysysbankcode())){
            					throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护联行号，请先维护联行信息!");
            				}
            				accbook.setPaysysbankcode(bankAccInfo.getPaysysbankcode());
            			}else{
            				if(StringUtil.isBlank_new(bankAccInfo.getBankTypeCode())){
            					throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护银行类型，请先维护信息!");
            				}
            				accbook.setBankType(bankAccInfo.getBankTypeCode());
            			}*/
            		}else{
            			/*CustomerBankAccService customerBankAccService = SpringContextUtil
            					.getServiceByClass(CustomerBankAccService.class);
            			String[] extBankAcc = customerBankAccService.getCustomerBankSourceCode(payeeAcc,nisBillhead.getCorpId());
            			if(null == extBankAcc){
            				throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护，请先维护账号信息!");
            			}
            			if(MustEnterBankCodeMode.YES.get().equals(bankCodeParam)){
            				if(StringUtil.isBlank_new(extBankAcc[0])){
            					throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护联行号，请先维护联行信息!");
            				}
            				accbook.setPaysysbankcode(extBankAcc[0]);
            				accbook.setBankType(extBankAcc[1]);
            			}else{
            				if (StringUtil.isBlank_new(extBankAcc[1])) {
            					throw new BusinessException("外单位帐号[" + payeeAcc + "]未维护银行类型，请先维护信息!");
            				}
            				accbook.setPaysysbankcode(extBankAcc[0]);
            				accbook.setBankType(extBankAcc[1]);
            			}*/
            		}
            		/*if(StringUtil.isNotEmpty(nisBillhead.getPayChannel())){
            			accbook.setPayChannel(nisBillhead.getPayChannel());//付款通道接口
            		}*/
                } else {
                	/*String bankType = getExpenseDetailBankType(nisBillhead.getBillNo());
                	accbook.setBankType(bankType);*/
                }
        	}
        }
    }
    /**
	 * 判断是否自动复核
	 */
	@Override
    public boolean isAutoCheck(){
		boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
		return isAutoCheck;
	}
	/**
     * 组装自定义会计分录
     */
    public List<FcAccbookDtlVO> getAccbookDtls(NisBillheadDTO nisBillhead, FcBusinessRuleDefineDTO ruleDefine,
    		List<VoucherRenderControlVo> controlBeans, String voucherType) {
    	if(EmptyUtils.isEmpty(iFcAccService)){
    		iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccService");
		}
    	if(EmptyUtils.isEmpty(iBtBankAccService)){
    		iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccService");
		}
    	if(EmptyUtils.isEmpty(configService)){
    		configService = (IConfigService) ApplicationContextUtil.getContext().getBean("configService");
		}
		if(EmptyUtils.isEmpty(iBisExcService)){
			iBisExcService = (IBisExcService) ApplicationContextUtil.getContext().getBean("bisExcService");
		}
    	List<FcVoucherEntryDefineDTO> list = ruleDefine.getVouchers();
        FcVoucherTypeDTO voucher = iFcVoucherTypeService.getFcVoucherTypeById(nisBillhead.getVoucherTypeId());
    	String payType = voucher.getSysVoucherCode();
        List<FcAccbookDtlVO> listDtl = new ArrayList<FcAccbookDtlVO>();
        int balanceWay;
        for (FcVoucherEntryDefineDTO entry : list) {
        	FcAccbookDtlVO dtl = new FcAccbookDtlVO();
            dtl.setSummaryContent(EmptyUtils.isEmpty(nisBillhead.getPurpose()) ? entry.getSummary() : nisBillhead.getPurpose());
            balanceWay = entry.getBalanceWay().intValue();
			if (balanceWay == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() && entry.getEntryType().intValue() == 10) {
                dtl.setDebitMoney(nisBillhead.getPayMoney());
                dtl.setCreditMoney(BigDecimal.ZERO);
                dtl.setAccount(nisBillhead.getPayerAcc());
                FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(nisBillhead.getPayerAcc());
                dtl.setAccount(nisBillhead.getPayerAcc());
                dtl.setAccName(fcAcc.getAccName());
                dtl.setOrigVoucherNo(nisBillhead.getBillNo());
                dtl.setOrigVoucherId(nisBillhead.getId());
			} else if (balanceWay == Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT) && entry.getEntryType().intValue() == 10) {
                dtl.setDebitMoney(BigDecimal.ZERO);
                dtl.setCreditMoney(nisBillhead.getPayMoney());
                if (PayType.PAY_TYPE_05.get().equals(payType) && getDefinitionType(voucherType,nisBillhead) == 0) {//内转
                    dtl.setAccount(nisBillhead.getPayeeAcc());
                    FcAccDTO fcAccPayee = iFcAccService.getAccDTOByAccount(nisBillhead.getPayeeAcc());
                    dtl.setAccName(fcAccPayee.getAccName());
                }else{
                    /**
                     * 记账参数先付款后记账
                     */
                	boolean paymentIsAccount = CmsConstant.IS_TRUE.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue());
                    if (paymentIsAccount && !NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {//TODO "expense".equals(nisBillhead.getIsFrom())老产品代码
                        if (!PayType.PAY_TYPE_13.get().equals(payType) && !PayType.PAY_TYPE_30.get().equals(payType)
                                && !PayType.PAY_TYPE_31.get().equals(payType)&& !PayType.PAY_TYPE_32.get().equals(payType)
                                && !PayType.PAY_TYPE_04.get().equals(payType)) {
                        	//NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(nisBillhead.getBillNo());//获取付款单信息
							String payerAccNo = null;
							String curId = null;
							String voucherTypeId = null;
                            //已付款情况下，转付取下拨账号的父账号对应的内部账号作为贷方账号，其他付款方式需要手工选择贷方账号
                            if("1".equals(nisBillhead.getIsPayed())){
                                if(PayType.PAY_TYPE_02.get().equals(payType)){
                                    BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccByIdAndCurId(nisBillhead.getDownAccId(), nisBillhead.getCurId());
                                    payerAccNo = btBankAccDTO.getBankAcc();//付款账号
                                }
                                curId = nisBillhead.getCurId();
                                voucherTypeId = nisBillhead.getVoucherTypeId();
							}else {
								BisExcDTO bisExc = iBisExcService.getBisExcByCbsBillNo(nisBillhead.getBillNo(), "1");//获取付款指令
								if (null == bisExc) {
									throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_BILLNO_ISNULL, nisBillhead.getBillNo());
								}
								payerAccNo = bisExc.getPayerAcc();//付款账号
								curId = bisExc.getCurId();
								voucherTypeId = bisExc.getVoucherTypeId();
							}
                            //获取生成付款指令的付款账号对应的内部账号
                            if(EmptyUtils.isNotEmpty(payerAccNo)) {
                                BtBankAccDTO bankAccInfo = iBtBankAccService.getBankAccByBankAccAndCurId(payerAccNo, curId);//
                                BtBankAccDTO parentAcc = iBtBankAccService.getParentAccByAccId(bankAccInfo.getId(),bankAccInfo.getCurId());
                                String counterAcc = "";
                                FcVoucherTypeDTO voucherDTO= iFcVoucherTypeService.getFcVoucherTypeById(voucherTypeId);//
                                if (voucherDTO.getSysVoucherCode().equals(PayType.PAY_TYPE_02.get())) {
                                    //付款账号的父账号对应的内部账号
                                    if (EmptyUtils.isEmpty(parentAcc)) {
                                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_PARENTACCOUNT_ISNULL);
                                    } else {
                                        counterAcc = iBtBankAccService.getBlnctrAcc(parentAcc.getBankAcc(), curId);
                                    }
                                } else {
                                    counterAcc = iBtBankAccService.getBlnctrAcc(bankAccInfo.getBankAcc(), nisBillhead.getCurId());//付款账号对应的内部账号
                                }
                                if (!EmptyUtils.isEmpty(counterAcc)) {//内部账号为空,则不能记账
                                    dtl.setAccount(counterAcc);
                                    FcAccDTO fcAccPayee = iFcAccService.getAccDTOByAccount(counterAcc);
                                    dtl.setAccount(fcAccPayee.getBlnctrAcc());
                                    dtl.setAccName(fcAccPayee.getAccName());
                                } else {
                                    //不满足记账条件，不能记账
                                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_PAYACCOUNT_ISNULL);
                                }
                            } else {
                                dtl.setAccount(null);
                            }
                        } else {
                            dtl.setAccount(null);
                        }
                    } else {
                        dtl.setAccount(null);
                    }
					//根据下拨账号获取贷方账号
					if(StringUtils.isNotBlank(nisBillhead.getDownAccId())) {
						BtBankAccDTO downAcc = iBtBankAccService.getBankAccById(nisBillhead.getDownAccId());
						if (downAcc != null && StringUtils.isNotBlank(downAcc.getPrentAccId())) {
							BtBankAccDTO parentAcc = iBtBankAccService.getBankAccById(downAcc.getPrentAccId());
							if (parentAcc != null && StringUtils.isNotBlank(parentAcc.getCounterAccId())) {
								FcAccDTO countAcc = iFcAccService.getAccDTOByAccount(parentAcc.getCounterAccId());
								if (countAcc != null) {
									dtl.setAccount(countAcc.getBlnctrAcc());
									dtl.setAccName(countAcc.getAccName());
								}
							}
						}
					}
                }
                dtl.setOrigVoucherNo(nisBillhead.getBillNo());
                dtl.setOrigVoucherId(nisBillhead.getId());
            }
            VoucherRenderControlVo dtlControBean = new VoucherRenderControlVo();
            dtlControBean.setSummaryEditable(true);
            //转付&代付&代理支付 可修改 借方账户信息
			if(NetbankConstant.PAY_TYPE_02.equals(nisBillhead.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_33.equals(nisBillhead.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(nisBillhead.getSysVoucherCode())){
				dtlControBean.setAccountEditable(true);
				if(dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0){//是借方
					dtlControBean.setDebitAccountLoadModle(true);
				}
			} else {
				dtlControBean.setDebitAccountLoadModle(false);
			}
            if (dtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0) {//是贷方，且不是内转的时候
                if(getDefinitionType(voucherType, nisBillhead) == 0){
                    if(PayType.PAY_TYPE_30.get().equals(payType) || PayType.PAY_TYPE_31.get().equals(payType)){
                        //活转定或者活转通
                        dtlControBean.setCreateAccountAble(true);
                    } else {
                        dtlControBean.setAccountEditable(true);
                    }
                } else {
                    dtlControBean.setAccountEditable(true);
                }
            }
            dtl.setControlVo(dtlControBean);
            listDtl.add(dtl);
        }
        return listDtl;
    }
    /**
     * 根据支付方式得到系统品种
     */
    public ConstantBean<String> getSysRaceCode(String sysVoucherCode, NisBillheadDTO nisBillhead) {
        switch (getDefinitionType(sysVoucherCode,nisBillhead)) {
        case 0:
            return SysRaceCode.NB_UNIT_COMMON_PAYMENT_SAME;
        case 1:
            return SysRaceCode.NB_UNIT_COMMON_PAYMENT_DIFF;
        case 2:
            return SysRaceCode.NB_UNIT_COMMON_PAYMENT_OUT;
        default:
            return SysRaceCode.NB_UNIT_COMMON_PAYMENT_OUT;
        }
    }
    /**
     * 得到会计分录类型
     */
    public int getDefinitionType(String sysVoucherCode, NisBillheadDTO nisBillhead) {
        if (sysVoucherCode.equals("32")) {//同业转账，不同跨点间单位的转账相当于内转中的不同网点
            return 1;//内转，不同网点
        }
        if (PayType.PAY_TYPE_05.get().equals(sysVoucherCode)) {//内转
            boolean flag = this.isSameNet(nisBillhead, sysVoucherCode);
            if (flag) {
                return 0;//内转，同网点
            } else {
                return 1;//内转，不同网点
            }
        } else if (PayType.PAY_TYPE_30.get().equals(sysVoucherCode) || PayType.PAY_TYPE_31.get().equals(sysVoucherCode)) {
            return 0;//内转，同网点
        } else {
            return 2;//对外付款
        }
    }
    /**
     * 改账时可编辑数据控制
     */
    public List<FcAccbookDtlVO> editableData(List<FcAccbookDtlVO>dtls, String voucherType, NisBillheadDTO nisBillhead) {
        for (FcAccbookDtlVO dtl : dtls) {
            VoucherRenderControlVo dtlControBean = new VoucherRenderControlVo();
            dtlControBean.setSummaryEditable(true);
            if (dtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 && getDefinitionType(voucherType,nisBillhead) != 0) {//是贷方，且不是内转的时候
                if(PayType.PAY_TYPE_30.get().equals(voucherType) || PayType.PAY_TYPE_31.get().equals(voucherType)){
                    //活转定或者活转通
                    dtlControBean.setCreateAccountAble(true);
                } else {
                    dtlControBean.setAccountEditable(true);
                }
            }
            dtl.setControlVo(dtlControBean);
        }
        return dtls;
    }
    /**
	 * 查询有权限的内部帐号
	 */
	public List<FcAccDTO> getFcAcc(String key, ICurrentUser user) {
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		if(EmptyUtils.isEmpty(iBtBankAccService)){
			iBtBankAccService = (IBtBankAccService) ApplicationContextUtil.getContext().getBean("btBankAccServiceImpl");
		}
		if(EmptyUtils.isEmpty(iInnerAccountService)){
			iInnerAccountService = (IInnerAccountService) ApplicationContextUtil.getContext().getBean("innerAccountService");
		}
		if(EmptyUtils.isEmpty(iFcVoucherTypeService)){
			iFcVoucherTypeService = (IFcVoucherTypeService) ApplicationContextUtil.getContext().getBean("fcVoucherTypeService");
		}
		if(EmptyUtils.isEmpty(configService)){
			configService = (IConfigService) ApplicationContextUtil.getContext().getBean("configService");
		}
		if(EmptyUtils.isEmpty(finacialManualService)){
			finacialManualService = (FinacialManualService) ApplicationContextUtil.getContext().getBean("finacialManualService");
		}
		if(EmptyUtils.isEmpty(iCorpService)){
			iCorpService = (ICorpService) ApplicationContextUtil.getContext().getBean("corpService");
		}
		if(EmptyUtils.isEmpty(iFcAccService)){
			iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccService");
		}

		NisBillheadDTO nisBillhead = billHeadService.getNisBillheadByBillNo(key);
		List<FcAccDTO> list = new ArrayList<FcAccDTO>();
		FcVoucherTypeDTO voucherType = iFcVoucherTypeService.getFcVoucherTypeById(nisBillhead.getVoucherTypeId());
		String payType = voucherType.getSysVoucherCode();
		if (isSameNet(nisBillhead, payType)) {
        	FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(nisBillhead.getPayeeAcc());
            list.add(fcAcc);
            return list;
        }
		String corpCondition = "";
		CorpDTO net = iCorpService.get(nisBillhead.getCorpId());
		if(CorpConstant.CORP_TYPE_CODE_NET == net.getType() && net.getId() == net.getNetId()){
			corpCondition += " and t.corp_id = '" + net.getId() + "' ";
		}else{
			corpCondition += " and t.corp_id = '" + net.getNetId() + "' ";
		}
        corpCondition += " and t.cur_id = '" + nisBillhead.getCurId() + "' ";
        corpCondition += " and t.acc_state <> 'D' and t.acc_state <> 'R' ";
		//参数是否允许中心修改下拨账号
		String value = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ALLOWEDITDOWNACC).getValue();
		if("0".equals(value) && PayType.PAY_TYPE_02.get().equals(voucherType.getSysVoucherCode())){//不允许
			//直接改为id关联，省去重复查询
//			String downBankAcc = nisBillhead.getDownAcc();
			String downBankAccId = nisBillhead.getDownAccId();
			corpCondition += " and t.blnctr_acc in (select bac.counter_acc  from bt_bank_acc_cur bac where bac.bank_acc_id =  "
    				+ "(select cur.prent_acc_id  from bt_bank_acc acc  left join bt_bank_acc_cur cur  on acc.id = cur.bank_acc_id " +
//					" where acc.bank_acc = '"+ downBankAcc +"')) ";
					" where acc.id = '"+ downBankAccId +"')) ";
		} else{
			if (!PayType.PAY_TYPE_32.get().equals(payType) && !PayType.PAY_TYPE_33.get().equals(payType)
        			&& !PayType.PAY_TYPE_35.get().equals(payType) && !PayType.PAY_TYPE_13.get().equals(payType)) {
				corpCondition += " and (f.sys_subject_code = '" + SysSubject.SUBJECT_CASH.get() + "' or exists " +
    					"  (select 1 from bt_bank_acc_cur bac where bac.bank_acc_id in " +
						"  (select bcur.prent_acc_id from bt_bank_acc acc left join bt_bank_acc_cur bcur on acc.id = bcur.bank_acc_id " +
						"	 where bcur.counter_acc = '" + nisBillhead.getPayerAcc() + "' ) " +
						" and bac.counter_acc = t.blnctr_Acc" +
    					" and (substr(ba.attribute_des, 2, 1) in ('2', '3') or substr(ba.attribute_des, 1, 1) in ('2', '3')))) ";//之前结尾加的and t.acc_type='02'))";
			}else{
				//代付取网点银行活期存款// 现金科目 '1900','1020','1001'
        		corpCondition += " and (f.sys_Subject_Code = '" + SysSubject.SUBJECT_CASH.get() + "' or t.acc_Type = '02' )";
			}
		}
		return finacialManualService.queryBySomeCondPgd(corpCondition);
	}
	/**
	 * 查询有权限的内部帐号
	 */
	public List<FcAccDTO> getFcAccDebit(String key,ICurrentUser user) {
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		if(EmptyUtils.isEmpty(finacialManualService)){
			finacialManualService = (FinacialManualService) ApplicationContextUtil.getContext().getBean("finacialManualService");
		}
		NisBillheadDTO nisBillhead = billHeadService.getNisBillheadByBillNo(key);
		String corpCondition = " and t.corp_id = '" + nisBillhead.getCorpId() + "' ";
		corpCondition += " and t.cur_id = '" + nisBillhead.getCurId() + "' ";
		corpCondition += " and t.acc_state <> 'D' and t.acc_state <> 'R' ";
		List<FcAccDTO> list = finacialManualService.queryBySomeCondPgd(corpCondition);
		return list;
	}
	/**
	 * 判断两个账号是否判断是不是同一结算中心
	 */
	public boolean isSameNet(NisBillheadDTO nisBillhead,String voucherType){
		if(EmptyUtils.isEmpty(iFcAccService)){
			iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
		}
		/**
         * 如果非内转
         */
		if(!NbconsoleConstant.PayType.PAY_TYPE_05.getValue().equals(voucherType)){
			return false;
		}
		/*FcAccDTO payeeAcc = iFcAccService.getAccDTOByAccount(nisBillhead.getPayeeAcc());
		FcAccDTO payerAcc = iFcAccService.getAccDTOByAccount(nisBillhead.getPayerAcc());
		return payeeAcc.getNetId().equals(payerAcc.getNetId());*/
		return iFcAccService.isTheSameNetByFcAcc(nisBillhead.getPayeeAcc(), nisBillhead.getPayerAcc());
	}
	/**
     * 返回记账的业务种类
     */
    public Integer getOriType() {
		return BusOriType.NB_UNIT_COMMON_PAYMENT.get();
    }
	/**
	 * 记账前校验数据状态
	 * @param opType
	 * @param key
	 */
	public void validBefore(String opType, String key) {
		if(EmptyUtils.isEmpty(billHeadService)){
			billHeadService = (INisBillheadService) ApplicationContextUtil.getContext().getBean("nisBillheadServiceI");
		}
		NisBillheadDTO nisBillhead = billHeadService.getNisBillheadByBillNo(key);
		if (FinacialManualConstant.MANUAL_STATUS_INIT.equals(opType)) {//待记账
			if(EmptyUtils.isNotEmpty(nisBillhead.getTallySign())){
				FinacialManualUtil.validBillStatus(nisBillhead.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_WAIT_TALLY});
			}
		} else {
			if(EmptyUtils.isEmpty(nisBillhead.getTallySign())){
				throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
			}
			if (FinacialManualConstant.MANUAL_STATUS_EDIT.equals(opType)) {//改账中
				FinacialManualUtil.validBillStatus(nisBillhead.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_TALLY_TEMP,
						FinacialManualConstant.TALLY_SIGN_TALLY_BACK, FinacialManualConstant.TALLY_SIGN_TALLY_CHECK});
			} else if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)) {//待复核
				FinacialManualUtil.validBillStatus(nisBillhead.getTallySign(), new String[]{FinacialManualConstant.TALLY_SIGN_TALLY_CHECK});
			}
		}
	}
	/**
	 * 待记账查询
	 */
	public List<NisBillheadDTO> queryTallyUnions(){
		return billHeadService.queryTallyUnions();
	}
    @Override
    public FcAccbookPageVO processAccbook(String key, String type, String menuType, ICurrentUser user) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return super.processAccbook(key, type, menuType, user);
    }
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoAccounter(false);//设置是否自动记账  否
        return voucher;
    }
    public void setVoucherDtlInfo(FcAccbookPageVO accbookPageVO,FcAccbookVoucherDTO fcAccbook, Date tallyDate, Date interestStartDate, String creditAcc) {
        FcAccbookVO accbook = accbookPageVO.getAccbook();
        FcAccbookDTO accbookDTO = fcAccbook.getAccbook();
        BeanUtils.copyProperties(accbook, accbookDTO);
        accbookDTO.setTallyDate(tallyDate);
        accbookDTO.setInterestStartDate(interestStartDate);
        fcAccbook.setAccbook(accbookDTO);
        FcAccbookExtVO bookExtVO = accbookPageVO.getBookExt();
        if(null != bookExtVO){
            FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
            BeanUtils.copyProperties(bookExtVO, bookExt);
            fcAccbook.setBookExt(bookExt);
        }
        List<FcAccbookDtlVO> bookDtls = accbookPageVO.getBookDtls();
        List<FcAccbookDtlDTO> bookDtlDTOs = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
        if (EmptyUtils.isNotEmpty(creditAcc)) {
            for(FcAccbookDtlDTO accbookDtlDTO : bookDtlDTOs) {
                if (accbookDtlDTO.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
					if(EmptyUtils.isNotEmpty(creditAcc)){
						accbookDtlDTO.setAccount(creditAcc);
						FcAccDTO fcAccDTO=	iFcAccService.getAccDTOByAccount(creditAcc);
						if(fcAccDTO!=null){
							accbookDtlDTO.setAccName(fcAccDTO.getAccName());
						}
					}
                }
            }
        }
        fcAccbook.setBookDtls(bookDtlDTOs);
    }
    /**
    * <br>
    * <b>功能：</b> 提交付款单信息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2022/3/5 0005 <br>
    */
    public void submitNisBillheadAccbook(String billNo, Date tallyDate, Date interestStartDate, String creditAcc,
                                         ICurrentUser user, String menuType) {
        FcAccbookVoucherDTO fcAccbook = new FcAccbookVoucherDTO();
        NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
        String businessId = nisBillheadDTO.getId();
        FcAccbookPageVO accbookPageVO = processAccbook(billNo, NbconsoleConstant.OpType.INIT.get(), user);
        setVoucherDtlInfo(accbookPageVO, fcAccbook, tallyDate, interestStartDate, creditAcc);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        boolean autoCheck = this.isAutoCheck();//判断是否自动复核
        if (autoCheck) {
            setCheckVoucherInfo(voucher, user, billNo);
            voucher.setAutoCheck(true);
        }
		VoucherWrapperDTO voucherWrapperDTO = iFcAccbookService.submitVoucher(voucher);
		FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
		//更新付款单记账信息
        int count = 0;
        if(autoCheck) {
            //更新付款单复核信息
            count = billHeadService.updateAutoCheckSign(nisBillheadDTO.getId(), accbook.getOrderNo(),
                    user.getId(), accbook.getVoucherNo(), NetbankConstant.STATUE_TALLY_CHECKPASS);
        } else {
            //发送消息
            count = billHeadService.updateTallySign(nisBillheadDTO.getId(), accbook.getOrderNo(), user.getId());
        }
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT,"记账提交",NisMessageConstant.BUS_TYPE_PAY,user.getLoginName(),user.getUsername());
        notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, businessId, user.getId());
        completeEditNotificationInfo(voucher, user, menuType, businessId);
        completeCheckNotificationInfo(voucher, user, menuType, businessId);
        if(!autoCheck){
            sendCheckNotificationInfo(voucher, user, menuType, businessId);
        }
    }
    /**
     * 功能介绍 : 记账复核凭证对象
     */
    public void passNisBillheadAccbook(String billNo, ICurrentUser user, String menuType) {
        FcAccbookVoucherDTO fcAccbook = new FcAccbookVoucherDTO();
        NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
        String businessId = nisBillheadDTO.getId();
        String orderNo = nisBillheadDTO.getOrderNo();
        FcAccbookDTO accbookDTO = iFcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
        fcAccbook.getAccbook().setBusGlobalId(accbookDTO.getBusGlobalId());
		VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(BusOriType.NB_UNIT_COMMON_PAYMENT.get(), accbookDTO.getBusGlobalId());
		voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setUserCode(user.getId());// 设置用户
		voucher.setAutoAccounter(false);//设置是否自动记账  否
		voucher.setUser(user);
		setCheckVoucherInfo(voucher, user, billNo);
        voucher=  iFcAccbookService.checkVoucher(voucher);
        /*FcAccbook fa = new FcAccbook();
        BeanUtils.copyNotNullProperties(voucher.getTarget().get(0).getAccbook(), fa);*/
        //更新付款单记账信息
        int count = billHeadService.updateCheckSign(nisBillheadDTO.getId(),
                voucher.getTarget().get(0).getAccbook().getVoucherNo(), NetbankConstant.STATUE_TALLY_CHECKPASS);
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_PASS,"记账复核通过",NisMessageConstant.BUS_TYPE_PAY,user.getLoginName(),user.getUsername());
        completeCheckNotificationInfo(voucher,user,menuType, businessId);
        completeEditNotificationInfo(voucher,user,menuType, businessId);
    }
    /**
     * 功能介绍 : 复核打回凭证对象
     */
	@Transactional(rollbackFor = Exception.class)
    public void backNisBillheadAccbook(String billNo, ICurrentUser user, String menuType, String backReason) {
        FcAccbookVoucherDTO fcAccbook = new FcAccbookVoucherDTO();
        NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
        String businessId = nisBillheadDTO.getId();
        String orderNo = nisBillheadDTO.getOrderNo();
        FcAccbookDTO accbookDTO = iFcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
        fcAccbook.getAccbook().setBusGlobalId(accbookDTO.getBusGlobalId());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        voucher.setUser(user);
        iFcAccbookService.backVoucher(voucher);
        //更新付款单记账信息
        int count = billHeadService.updateCheckBackSign(nisBillheadDTO.getId(),
                voucher.getTarget().get(0).getAccbook().getVoucherNo());
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        iFcAccbookService.backVoucher(voucher);
        iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_BACK, backReason,NisMessageConstant.BUS_TYPE_PAY,user.getLoginName(),user.getUsername());
        completeCheckNotificationInfo(voucher,user,menuType, businessId);
        sendBackNotificationInfo(voucher, menuType, businessId);
    }
    /**
     * 功能介绍 : 记账复核凭证对象
     */
    public void passEditNisBillheadAccbook(String billNo, ICurrentUser user, String menuType) {
        NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
        String businessId = nisBillheadDTO.getId();
        String orderNo = nisBillheadDTO.getOrderNo();
        FcAccbookDTO accbookDTO = iFcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(BusOriType.NB_UNIT_COMMON_PAYMENT.get(), accbookDTO.getBusGlobalId());
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(user.getId());// 设置用户
        voucher.setAutoAccounter(false);//设置是否自动记账  否
        voucher.setUser(user);
        iFcAccbookService.submitFcAccbook(voucher);
        //更新付款单记账信息
        int count = billHeadService.updateEditPassSign(businessId, nisBillheadDTO.getTallySign());
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT,"记账提交",NisMessageConstant.BUS_TYPE_PAY,user.getLoginName(),user.getUsername());
        notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, businessId, user.getId());
        completeEditNotificationInfo(voucher, user, menuType, businessId);
        completeCheckNotificationInfo(voucher, user, menuType, businessId);
        sendCheckNotificationInfo(voucher, user, menuType, businessId);
    }
    /**
     * 功能介绍 : 复核打回凭证对象
     */
    public void backEditNisBillheadAccbook(String billNo, ICurrentUser user, String menuType) {
        FcAccbookVoucherDTO fcAccbook = new FcAccbookVoucherDTO();
        NisBillheadDTO nisBillheadDTO = billHeadService.getNisBillheadByBillNo(billNo);
        String businessId = nisBillheadDTO.getId();
        String orderNo = nisBillheadDTO.getOrderNo();
        FcAccbookDTO accbookDTO = iFcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
        fcAccbook.getAccbook().setBusGlobalId(accbookDTO.getBusGlobalId());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        voucher.setUser(user);
        iFcAccbookService.deleteVoucher(voucher);
        //更新付款单记账信息
        int count = billHeadService.updateEditBackSign(businessId, nisBillheadDTO.getTallySign());
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_CHANGE_BACK,fcAccbook.getBookExt().getInvalidReason(),NisMessageConstant.BUS_TYPE_PAY,user.getLoginName(),user.getUsername());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("money", NumberUtil.formatNumeric(nisBillheadDTO.getPayMoney(), "#,##0.00"));
        completeCheckNotificationInfo(voucher, user, menuType, nisBillheadDTO.getId());
        completeEditNotificationInfo(voucher, user, menuType, nisBillheadDTO.getId());
        notificationService.sendToUserByResPermission(nisBillheadDTO.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, map, nisBillheadDTO.getCorpId());
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user,String menuType, String businessId) {
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
            if("5".equals(menuType)){
                notificationService.sendIgnoreMySelf(businessId, accbook.getNetId(),
                        NotificationConstant.FINACIAL.CORPPAYMENT_CHECK, notifyVO, user.getId());
            }else{
                notificationService.sendIgnoreMySelf(businessId, accbook.getNetId(),
                        NotificationConstant.FINACIAL.MONEYTRANSFERS_CHECK, notifyVO, user.getId());
            }
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher,String menuType, String businessId) {
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
            if("5".equals(menuType)){
                notificationService.sendToUser(businessId,
                        accbook.getOperatorCode(), NotificationConstant.FINACIAL.CORPPAYMENT_EDIT, notifyVO);
            }else{
                notificationService.sendToUser(businessId,
                        accbook.getOperatorCode(), NotificationConstant.FINACIAL.MONEYTRANSFERS_EDIT, notifyVO);
            }
        }
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user,String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            if("5".equals(menuType)){
                notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_EDIT, businessId, user.getId());
            }else{
                notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_EDIT, businessId, user.getId());
            }
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user,String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            if("5".equals(menuType)){
                notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_CHECK, businessId, user.getId());
            }else{
                notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_CHECK, businessId, user.getId());
            }
        }
    }
}
