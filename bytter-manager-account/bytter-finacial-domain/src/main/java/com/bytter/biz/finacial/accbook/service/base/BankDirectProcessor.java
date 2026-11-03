package com.bytter.biz.finacial.accbook.service.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.GenerateMode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.mq.message.service.ProducerMessageRecordService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
/**
 * 
 * @author liyh
 *
 */
@Component
@Service("bankDirectProcessor")
@Transactional
public class BankDirectProcessor extends BaseService implements Serializable {
	
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private INisBillheadService billheadService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IFcVoucherTypeService voucherTypeService;
	@DubboReference(check = false)
	private IConfigService configService;

	@Autowired
	private ProducerMessageRecordService producerMessageRecordService;

	@Value("${local.massage.transaction}")
	private String localMassageTransaction;
	
	public IBisExcService getBisService() {
		if (bisExcService == null) {
			bisExcService = ApplicationContextUtil.getContext().getBean(IBisExcService.class);
		}
		return bisExcService;
	}
	public IBtBankAccService getBtBankAccService() {
		if (btBankAccService == null) {
			btBankAccService = ApplicationContextUtil.getContext().getBean(IBtBankAccService.class);
		}
		return btBankAccService;
	}
	public INisBillheadService getNisBillheadService() {
		if (billheadService == null) {
			billheadService = ApplicationContextUtil.getContext().getBean(INisBillheadService.class);
		}
		return billheadService;
	}
	public IInnerAccountService getInnerAccountService() {
		if (innerAccountService == null) {
			innerAccountService = ApplicationContextUtil.getContext().getBean(IInnerAccountService.class);
		}
		return innerAccountService;
	}
	public IFcVoucherTypeService getVoucherTypeService() {
		if (voucherTypeService == null) {
			voucherTypeService = ApplicationContextUtil.getContext().getBean(IFcVoucherTypeService.class);
		}
		return voucherTypeService;
	}
	public IConfigService getConfigService() {
		if (configService == null) {
			configService = ApplicationContextUtil.getContext().getBean(IConfigService.class);
		}
		return configService;
	}
	
	/**
	 * 功能介绍。<br>
	 * 根据付款账号和外单位信息产生指令
	 * <b>日期：</b>May 11, 2018<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processResult(VoucherWrapper voucher) {
		//先付款后记账/先记账后付款 都走这个逻辑，主要根据这个判断是否产生指令
		if (!voucher.isCreateBisExc()) {//凭证不产生指令则退出
			return;
		}
    	for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {
    		FcAccbook accbook = bookVoucher.getAccbook();
        	if(GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())
        			|| GenerateMode.RUSH_BOOK.get().equals(accbook.getGenerateMode())){//利息、冲账凭证不产生指令
        		return ;
        	}
    	}
    	//根据付款账号和外单位信息产生指令
    	createPayBisExc(voucher);
	}
	
	/**
	 * 功能介绍。<br>
	 * 根据付款账号和外单位信息产生指令
	 * <b>日期：</b>May 11, 2018<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void checkProcessResult(VoucherWrapper voucher) {
		//先付款后记账/先记账后付款 都走这个逻辑，主要根据这个判断是否产生指令
		if (!voucher.isCreateBisExc()) {//凭证不产生指令则退出
			return;
		}
    	for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {
    		FcAccbook accbook = bookVoucher.getAccbook();
        	if(GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())
        			|| GenerateMode.RUSH_BOOK.get().equals(accbook.getGenerateMode())){//利息、冲账凭证不产生指令
        		return ;
        	}
    	}
    	//根据付款账号和外单位信息产生指令
    	checkCreatePayBisExc(voucher);
	}
	
	/**
	 * 功能介绍 : 付款指令生成。<br>
	 * <b>日期：</b>2018-05-11<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void createPayBisExc(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {
			String payerAcc = voucher.getPayerAcc();//付款账号
			if (!EmptyUtils.isEmpty(payerAcc)) {
				FcAccbookExt bookExt = bookVoucher.getBookExt();
				bookExt.setPayerAcc(payerAcc);
			}
			createBisExc(voucher.getIsForindividual(), bookVoucher, voucher.getUser());
		}
	}
	
	
	/**
	 * 功能介绍 : 付款指令生成。<br>
	 * <b>日期：</b>2018-05-11<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void checkCreatePayBisExc(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {
			String payerAcc = voucher.getPayerAcc();//付款账号
			if (!EmptyUtils.isEmpty(payerAcc)) {
				FcAccbookExt bookExt = bookVoucher.getBookExt();
				bookExt.setPayerAcc(payerAcc);
			}
			checkCreateBisExc(voucher.getIsForindividual(), bookVoucher, voucher.getUser());
		}
	}
	
	/**
	 * 功能介绍 : 计算凭证金额。<br>
	 * <b>日期：</b>2018-05-16<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private BigDecimal getVoucherMoney(BisExcDTO bisExc, FcAccbookVoucherVO bookVoucher) {
		BigDecimal money = BigDecimal.ZERO;
		List<FcAccbookDtl> accbookDtls = bookVoucher.getBookDtls();
		for (FcAccbookDtl bookDtl : accbookDtls) {
			money = money.add(bookDtl.getDebitMoney());
			bisExc.setCbsBillNo(bookDtl.getOrigVoucherNo());//原业务单号
			bisExc.setCbsBillId(bookDtl.getOrigVoucherId());//原业务ID
		}
		return money;
	}
	
	/**
	 * 功能介绍 : 生成付款指令。<br>
	 * <b>日期：</b>2018-05-16<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void createBisExc(String isForindividual, FcAccbookVoucherVO bookVoucher, ICurrentUser user) {
		FcAccbook accbook = bookVoucher.getAccbook();
		FcAccbookExt bookExt = bookVoucher.getBookExt();
		
		if (EmptyUtils.isEmpty(bookExt.getPayerAcc())) {
			throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_BANKACC_ISNULL);
		}
		//币别
		String curId = null;
		if(bookVoucher.getBookDtls().get(0).getAccDTO() != null) {
			curId = bookVoucher.getBookDtls().get(0).getAccDTO().getCurId();
		}else {
			curId = bookVoucher.getBookDtls().get(0).getCurCode();
		}
		//获取付款账号
//		
		BtBankAccDTO payerAcc = getBtBankAccService().getBankAccByIdAndCurId(bookExt.getPayerAcc(), curId);
		//离线账号不生成指令
		if (payerAcc == null || FinacialConstant.VALID_FALSE.equals(payerAcc.getIsOnline())) {
			return;
		}
		//生成付款指令
		BisExcDTO bisExc = new BisExcDTO();
//		bisExc.setVoucherNo(accbook.getVoucherNo()); //此处不用设置voucherNo，因为在结算模块有 新生成voucherNo的逻辑，使用voucherNo字段来存储 付款指令编码
		bisExc.setCbsBillId(accbook.getId());
		bisExc.setCbsBillNo(accbook.getVoucherNo());
		bisExc.setCorpId(payerAcc.getCorpId());
		bisExc.setReqDate(DateUtils.getDate());
		//付款方信息
		bisExc.setPayerAcc(payerAcc.getBankAcc());
		bisExc.setCounterAcc(payerAcc.getCounterAccId());//付款账号对应的内部账号
		bisExc.setPayerAccName(payerAcc.getAccName());
		bisExc.setPayerBank(payerAcc.getBankName());
		bisExc.setPayerProv(payerAcc.getProv());
		bisExc.setPayerCity(payerAcc.getCity());
		bisExc.setCurId(accbook.getCurCode());
		BigDecimal voucherMoney = getVoucherMoney(bisExc, bookVoucher);
		if (voucherMoney.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		bisExc.setPayMoney(voucherMoney);//付款金额
		bisExc.setVoucherTypeId(accbook.getVoucherType());
		bisExc.setBifCode(payerAcc.getBifCode());
		//收款方信息
		bisExc.setPayeeAcc(bookExt.getExternalAcc());
		bisExc.setPayeeAccName(bookExt.getExternalCorp());
		bisExc.setPayeeBank(bookExt.getExternalBank());
		bisExc.setPayeeBankCode(bookExt.getPaysysbankcode());//收款方联行号
        setProvAndCity(bisExc, accbook, bookExt);//收款方省市
		bisExc.setWishPayday(accbook.getTallyDate());
		//交易模式 3-对公 4-对私 默认4
		bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
		if (BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(accbook.getBusOriType())) {//内网付款
			bisExc.setTransMode(isForindividual);//对私交易
		}
		if (EmergencySign.EMERGENCY_SIGN_FAST.getValue().equals(accbook.getEmergencySign())) {
			bisExc.setIsQuick(NetbankConstant.ISQUICK_1);//加急
		} else {
			bisExc.setIsQuick(NetbankConstant.ISQUICK_0);//普通
		}
		setMidPayerAccInfo(bookVoucher, bisExc);//如果代付设置中间付款账号
		setBankArea(bisExc, payerAcc, bookExt);//设置同行异地标识
		bisExc.setPurpose(accbook.getRmk());
		bisExc.setAddWord(accbook.getPostscript());//附言
		bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_10);//柜台付款
		bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);//手工指令
		bisExc.setVoucherStat(BdlinkConstant.WAIT_FILL);//待发送
		setVoucherFrom(bisExc, accbook.getBusOriType(), user);//数据来源 里面有产生指令逻辑
	}
	
	/**
	 * 功能介绍 : 生成付款指令。<br>
	 * <b>日期：</b>2018-05-16<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void checkCreateBisExc(String isForindividual, FcAccbookVoucherVO bookVoucher, ICurrentUser user) {
		FcAccbookExt bookExt = bookVoucher.getBookExt();
		
		if (EmptyUtils.isEmpty(bookExt.getPayerAcc())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_COLLECT_MANUAL_BANKACC_ISNULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		//后续没有抛异常，省略校验
		
	}
	/**
	* <br>
	* <b>功能：</b> 设置收款方省市 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2022/9/23 0023 <br>
	*/
	public void setProvAndCity(BisExcDTO bisExc, FcAccbook accbook, FcAccbookExt bookExt) {
	    if (EmptyUtils.isEmpty(bookExt.getExternalProv()) || EmptyUtils.isEmpty(bookExt.getExternalCity())
            || EmptyUtils.isEmpty(bookExt.getExternalProv().trim()) || EmptyUtils.isEmpty(bookExt.getExternalCity().trim())) {
            Integer busOriType = accbook.getBusOriType();//单位申请付款
            if (FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(busOriType)) {
	            String billNo = accbook.getOrigVoucherNo();
                NisBillheadDTO nisBillhead = getNisBillheadService().getNisBillheadByBillNo(billNo);
                bisExc.setPayeeProv(nisBillhead.getProv());//收款方省市
                bisExc.setPayeeCity(nisBillhead.getCity());
                bookExt.setExternalProv(nisBillhead.getProv());
                bookExt.setExternalCity(nisBillhead.getCity());
                bookExt.setProv(nisBillhead.getProv());
                bookExt.setCity(nisBillhead.getCity());
            }
        } else {
            bisExc.setPayeeProv(bookExt.getExternalProv());//收款方省市
            bisExc.setPayeeCity(bookExt.getExternalCity());
        }
    }
	
	/**
	 * 功能介绍 : 设置指令来源, 发送付款指令。<br>
	 * <b>日期：</b>2018-06-07<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void setVoucherFrom(BisExcDTO bisExc, Integer busOriType, ICurrentUser user) {
		boolean flag = false;//资金调拨标识
		String voucherFrom = BdlinkConstant.FROM_COUNTER_TALLY;// 来源柜台记账
		if (FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(busOriType)) {
			voucherFrom = BdlinkConstant.FROM_NIS_PAY;// 来源单位申请付款
		} else if (FinaBookConstant.BusOriType.NB_UNIT_ASK_PAYMENT.get().equals(busOriType)) {
			voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_ASK;// 来源请款
			flag = true;
		} else if (FinaBookConstant.BusOriType.NB_CNT_DOWN_PAYMENT.get().equals(busOriType)) {
			voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN;// 来源下拨
			flag = true;
		} else if (FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(busOriType)) {
			voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_POS;// 来源头寸调拨
			flag = true;
		}
		bisExc.setVoucherFrom(voucherFrom);//数据来源
		// todo:数据转换
		BisExcDTO bisExcDTO = BeanUtils.copyNewInstanceProperties(bisExc, BisExcDTO.class);
		if (flag) {
			//如果来源资金调拨，则根据参数设置产生的指令是否是主动发送
			// 判断资金调拨-下拨/请款   是否自动生成指令
			String bisExcAutoAccount = getConfigService().getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
			if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {

				if (localMassageTransaction.equalsIgnoreCase("open") ){
					logger.error("参数 local.massage.transaction=open ，已开启本地消息事务方式！注意要开启本服务的 ProducerMessageRecordJob 定时任务！");
					producerMessageRecordService.saveProducerMsgRecord(BusinessType.AUTO_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
				}else {
					this.getBisService().autoSendBisExc(bisExcDTO, user);//自动指令；调用结算 ；
				}

//				if (localMassageTransaction.equalsIgnoreCase("close")){
//					this.getBisService().autoSendBisExc(bisExcDTO, user);//自动指令；调用结算 ；改为本地消息；这里的 businessId 选择 cbsBillId，因为 voucherNo 在结算模块又有逻辑自己生成 -- 不清楚原用意作用
//					return;
//				}
//				producerMessageRecordService.saveProducerMsgRecord(BusinessType.AUTO_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
			} else {
				if (localMassageTransaction.equalsIgnoreCase("open") ){
					logger.error("参数 local.massage.transaction=open|on ，已开启本地消息事务方式！注意要开启本服务的 ProducerMessageRecordJob 定时任务！");
					producerMessageRecordService.saveProducerMsgRecord(BusinessType.MANUAL_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
				}else {
					this.getBisService().addBisExc(bisExcDTO, user);//手工指令 ；调用结算 ；
				}

//				if (localMassageTransaction.equalsIgnoreCase("close")){
//					this.getBisService().addBisExc(bisExcDTO, user);//手工指令 ；调用结算 ；改为本地消息；这里的 businessId 选择 cbsBillId，因为 voucherNo 在结算模块又有逻辑自己生成 -- 不清楚原用意作用
//					return;
//				}
//				producerMessageRecordService.saveProducerMsgRecord(BusinessType.MANUAL_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
			}
		} else {
			//根据付款方式判断是否自动发送指令
			FcVoucherTypeDTO voucherType = voucherTypeService.getFcVoucherTypeById(bisExc.getVoucherTypeId());
			if(!EmptyUtils.isEmpty(voucherType) && CommonConstant.YES.equalsIgnoreCase(voucherType.getSendMode())){
				if (localMassageTransaction.equalsIgnoreCase("open") ){
					logger.error("参数 local.massage.transaction=open|on ，已开启本地消息事务方式！注意要开启本服务的 ProducerMessageRecordJob 定时任务！");
					producerMessageRecordService.saveProducerMsgRecord(BusinessType.AUTO_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
				}else {
					this.getBisService().autoSendBisExc(bisExcDTO, user);//自动指令 ；调用结算 ；
				}

//				if (localMassageTransaction.equalsIgnoreCase("close")){
//					this.getBisService().autoSendBisExc(bisExcDTO, user);//自动指令 ；调用结算 ；改为本地消息；这里的 businessId 选择 cbsBillId，因为 voucherNo 在结算模块又有逻辑自己生成 -- 不清楚原用意作用
//					return;
//				}
//				producerMessageRecordService.saveProducerMsgRecord(BusinessType.AUTO_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
			}else {
				if (localMassageTransaction.equalsIgnoreCase("open") ){
					logger.error("参数 local.massage.transaction=open|on ，已开启本地消息事务方式！注意要开启本服务的 ProducerMessageRecordJob 定时任务！");
					producerMessageRecordService.saveProducerMsgRecord(BusinessType.MANUAL_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
				}else {
					this.getBisService().addBisExc(bisExcDTO, user);//手工指令 ；调用结算 ；
				}

//				if (localMassageTransaction.equalsIgnoreCase("close")){
//					this.getBisService().addBisExc(bisExcDTO, user);//手工指令 ；调用结算 ；改为本地消息；这里的 businessId 选择 cbsBillId，因为 voucherNo 在结算模块又有逻辑自己生成 -- 不清楚原用意作用
//					return;
//				}
//				producerMessageRecordService.saveProducerMsgRecord(BusinessType.MANUAL_PAYMENT_INSTRUCTIONS, bisExcDTO.getCbsBillId(), bisExcDTO, user);
			}
		}
	}
	
	/**
	 * 功能介绍 : 设置代付类中间付款方信息。<br>
	 * <b>日期：</b>2018-05-25<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void setMidPayerAccInfo(FcAccbookVoucherVO bookVoucher, BisExcDTO bisExc){
		FcAccbook accbook = bookVoucher.getAccbook();
		String voucherType = accbook.getVoucherType();
		FcVoucherTypeDTO voucherTypeDTO = getVoucherTypeService().getFcVoucherTypeById(voucherType);
		if (!NetbankConstant.PAY_TYPE_33.equals(voucherTypeDTO.getSysVoucherCode())
				&& !NetbankConstant.PAY_TYPE_35.equals(voucherTypeDTO.getSysVoucherCode())) {//非代付/代理支付不保存中间付款方信息
			return;
		}
		
		List<FcAccbookDtl> accbookDtls = bookVoucher.getBookDtls();
		String debitAcc = "";
        for (FcAccbookDtl dtl : accbookDtls) {
            if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
            	debitAcc = dtl.getAccount();//取借方账号第一个
            	break;
            }
        }
        FcAccDTO fcAccDTO = getInnerAccountService().getByBlnctrAcc(debitAcc);
        bisExc.setMidPayerAccNo(debitAcc);//账号
        bisExc.setMidPayerAccName(fcAccDTO.getAccName());//户名
        bisExc.setMidPayerBankName(fcAccDTO.getCorpName());//单位名
    }
	
	/**
	 * 功能介绍 :设置同行异地标识。<br>
	 * <b>日期：</b>2018-05-16<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void setBankArea(BisExcDTO bisExc, BtBankAccDTO payerAcc, FcAccbookExt bookExt) {
		if (!EmptyUtils.isEmpty(bookExt) && !EmptyUtils.isEmpty(payerAcc)) {
			if (EmptyUtils.isNotEmpty(bookExt.getPaysysbankcode()) && EmptyUtils.isNotEmpty(payerAcc.getBankCode())
					&& bookExt.getPaysysbankcode().substring(0, 3).equalsIgnoreCase(payerAcc.getBankCode().substring(0, 3))) {
				bisExc.setDifBank(NetbankConstant.DIFBANK_0);
			} else {
				bisExc.setDifBank(NetbankConstant.DIFBANK_1);
			}
			String payerProveAndCity = payerAcc.getProv() + payerAcc.getCity();
			String payeeProveAndCity = bookExt.getExternalProv() + bookExt.getExternalCity();
			if (payerProveAndCity.equalsIgnoreCase(payeeProveAndCity)) {
				bisExc.setAreaSign(NetbankConstant.AREASIGN_0);
			} else {
				bisExc.setAreaSign(NetbankConstant.AREASIGN_1);
			}
		}
	}
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
