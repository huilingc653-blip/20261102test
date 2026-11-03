package com.bytter.biz.netbank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bytter.biz.payment.api.enums.BusinessType;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlinks.service.FcAccbookCommonUtil;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookExtDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.util.FinacialUtils;
import com.bytter.biz.mq.message.service.ProducerMessageRecordService;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.vo.NisAccbookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
/**
 * 结算记账挂接服务类
 * @author huangx
 *
 */
@Service("nisAccbookService")
@Transactional
public class NisAccbookService extends BaseService<NisAccbookVO, String> {
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;

	@Autowired
	private FcVoucherTypeService fcVoucherTypeService;

	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;

	@DubboReference(check = false)
	private IFcBusRaceService fcBusRaceService;

	@Autowired
	private FcAccbookCommonUtil fcAccbookCommonUtil;

	@Autowired
	private ProducerMessageRecordService producerMessageRecordService;

	@Value("${local.massage.transaction}")
	private String localMassageTransaction;

	@Override
	public BaseDao<NisAccbookVO, String> getEntityDao() {
		return null;
	}

	/**
	 * 自动记账
	 * @param dto
	 * @return voucher_no
	 */
	public String submitVoucher(NisAccbookVO dto, ICurrentUser user) {
		logger.error("进入nisAccbookService.submitVoucher方法！");
		VoucherWrapperDTO voucher = new VoucherWrapperDTO();
		FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();

        //构建账务凭证分录明细记录
        List<FcAccbookDtlDTO> accbookDtls = getAccbookDtls(dto);
        //构建账务账务凭证主档表记录
    	FcAccbookDTO accBook = new FcAccbookDTO();
		//备注
    	accBook.setRmk(dto.getPurpose());
    	accBook.setNetId(dto.getFcAcc().getNetId()); //这里有网点 ; 如果为空，则是 没有设置 结算中心账号 //TODO 需要抛异常
        accBook.setBusOriType(dto.getBusOriType());
        //获取记账规则ID
        accBook.setBusRaceId(fcBusRaceService.getNetBusRace(dto.getSysRaceCode(), accBook.getNetId(),user.getId()));
        accBook.setVoucherType(dto.getVoucherTypeId());
        accBook.setTallyDate(DateUtils.getDate()); //这里成生成记账时间
        accBook.setInterestStartDate(DateUtils.getDate());
        if(NetbankConstant.ISQUICK_0.equals(dto.getIsQuick())){
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        }else{
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        }
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls); //里面有 币别id，可以查询 curCode
		entity.setBookExt(dto.getBookExt());

        //外单位信息
        if(!StringUtils.isBlank(dto.getExternalCorp())){
	        FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
	        bookExt.setExternalId(dto.getExternalId());
	        bookExt.setExternalCorp(dto.getExternalCorp());
	        bookExt.setExternalAcc(dto.getPayeeAcc());
	        bookExt.setExternalBank(dto.getBank());
	        bookExt.setPaysysbankcode(dto.getSysBankCode());
	        bookExt.setProv(dto.getProv());
	        bookExt.setCity(dto.getCity());
	    	entity.setBookExt(bookExt);
        }
		//内转资金计划内转标识
        voucher.setInnerSign(dto.getInnerSign());
        voucher.setBusGlobalId(entity.getAccbook().getBusGlobalId());
		voucher.addTarget(entity);
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		//设置用户
		voucher.setUserCode(user.getId());
		voucher.setAutoCheck(true);
		voucher.setAutoAccounter(true);

		logger.error(localMassageTransaction);
		if (localMassageTransaction.equalsIgnoreCase("open") ){
			//改为插入本地消息表 -- 调用核算模块；但是需要注意，这里面原有 voucherNo 的生成并返回，先前置生成 voucherNo
//			分布式改造 前置设置 voucherNo 注意 bisExc的voucherNo 和 entity的voucherNo 不是同一个意思
			logger.error("参数 local.massage.transaction=open ，已开启本地消息事务方式！注意要开启本服务的 ProducerMessageRecordJob 定时任务！");
			String voucherNo = fcAccbookCommonUtil.getVoucherNo(accBook.getNetId(), accBook.getTallyDate(), accbookDtls.get(0).getCurCode());
			accBook.setVoucherNo(voucherNo);
			voucher.setVoucherNo(voucherNo);
			producerMessageRecordService.saveProducerMsgRecord(BusinessType.FUND_ALLOCATION, dto.getBillId(), voucher, user); //改为插入本地消息表; dto.getBillId() 为 nis_allocation_req 或 nis_billhead 表id
			return voucher.getVoucherNo();
		}else {
			logger.error("执行原有逻辑，进入this.fcAccbookService.submitVoucher方法前");
			VoucherWrapperDTO voucherWrapperDTO = this.fcAccbookService.submitVoucher(voucher);
			return voucherWrapperDTO.getVoucherNo();
		}

//		if (localMassageTransaction.equalsIgnoreCase("close")){
//			logger.error("进入this.fcAccbookService.submitVoucher方法前");
//			VoucherWrapperDTO voucherWrapperDTO = this.fcAccbookService.submitVoucher(voucher);
//			return voucherWrapperDTO.getVoucherNo();
//		}else {
//			改为插入本地消息表 -- 调用核算模块；但是需要注意，这里面原有 voucherNo 的生成并返回，先前置生成 voucherNo
//			分布式改造 前置设置 voucherNo  注意 bisExc的voucherNo 和 entity的voucherNo 不是同一个意思
//			String voucherNo = fcAccbookCommonUtil.getVoucherNo(accBook.getNetId(), accBook.getTallyDate(), accbookDtls.get(0).getCurCode());
//			accBook.setVoucherNo(voucherNo);
//			voucher.setVoucherNo(voucherNo);
//			producerMessageRecordService.saveProducerMsgRecord(BusinessType.FUND_ALLOCATION, dto.getBillId(), voucher, user); // 改为插入本地消息表; dto.getBillId() 为 nis_allocation_req 或 nis_billhead 表id
//		}
		//返回记账凭证号
//		return voucher.getVoucherNo();
	}
	
	/**
	 * 自动记账前置校验
	 * @param dto
	 * @return voucher_no
	 */
	public String checkSubmitVoucher(NisAccbookVO dto, ICurrentUser user) {
		logger.error("进入nisAccbookService.submitVoucher方法！");
		VoucherWrapperDTO voucher = new VoucherWrapperDTO();
		FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();

        //构建账务凭证分录明细记录
        List<FcAccbookDtlDTO> accbookDtls = getCheckAccbookDtls(dto);//TODO 校验抛异常
        //构建账务账务凭证主档表记录
    	FcAccbookDTO accBook = new FcAccbookDTO();
		//备注
    	accBook.setBusGlobalId(FinacialUtils.createGlobalId());
    	accBook.setRmk(dto.getPurpose());
    	accBook.setNetId(dto.getFcAcc().getNetId()); //这里有网点 ; 如果为空，则是 没有设置 结算中心账号 //TODO 需要抛异常
        accBook.setBusOriType(dto.getBusOriType());
        //获取记账规则ID
        accBook.setBusRaceId(fcBusRaceService.getNetBusRace(dto.getSysRaceCode(), accBook.getNetId(),user.getId()));
        accBook.setVoucherType(dto.getVoucherTypeId());
        accBook.setTallyDate(DateUtils.getDate()); //这里成生成记账时间
        accBook.setInterestStartDate(DateUtils.getDate());
        if(NetbankConstant.ISQUICK_0.equals(dto.getIsQuick())){
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        }else{
        	accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        }
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls); //里面有 币别id，可以查询 curCode
		entity.setBookExt(dto.getBookExt());

        //外单位信息
        if(!StringUtils.isBlank(dto.getExternalCorp())){
	        FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
	        bookExt.setExternalId(dto.getExternalId());
	        bookExt.setExternalCorp(dto.getExternalCorp());
	        bookExt.setExternalAcc(dto.getPayeeAcc());
	        bookExt.setExternalBank(dto.getBank());
	        bookExt.setPaysysbankcode(dto.getSysBankCode());
	        bookExt.setProv(dto.getProv());
	        bookExt.setCity(dto.getCity());
	    	entity.setBookExt(bookExt);
        }
		//内转资金计划内转标识
        voucher.setInnerSign(dto.getInnerSign());
        voucher.setBusGlobalId(entity.getAccbook().getBusGlobalId());
		voucher.addTarget(entity);
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		//设置用户
		voucher.setUserCode(user.getId());
		voucher.setAutoCheck(true);

		logger.error(localMassageTransaction);
		logger.error("进入this.fcAccbookService.checkSubmitVoucher方法前");
		VoucherWrapperDTO voucherWrapperDTO = this.fcAccbookService.checkSubmitVoucher(voucher);
		return voucherWrapperDTO.getVoucherNo();
	}

	/**
	 * 构建账务凭证分录明细记录
	 * @param dto
	 * @return
	 */
    public List<FcAccbookDtlDTO> getAccbookDtls(NisAccbookVO dto) {
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        //根据业务判断构建分录
        FcAccDTO fcAcc = new FcAccDTO();
        if(FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(dto.getBusOriType())){
        	//一般付款
	        //获取系统结算方式
	  		FcVoucherType voucherType = fcVoucherTypeService.get(dto.getVoucherTypeId(), true);
	        if(NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode())){
	        	//内转(借：2120企业活期  贷：2120企业活期)
	        	fcAcc = innerAccountService.getByBlnctrAcc(dto.getPayeeAcc());
	        }else if(NetbankConstant.PAY_TYPE_04.equals(voucherType.getSysVoucherCode())){
	        	//现付(借：2120企业活期  贷：1010现金)
	        	List<FcAccDTO> list = innerAccountService.listAccountByMoreParams(dto.getFcAcc().getCurId(), null,
	        			dto.getFcAcc().getNetId(), null, SubjectConstant.SysSubject.SUBJECT_CASH.get());
				if(EmptyUtils.isEmptyList(list)){
					//未维护现金账号
					throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_1010_NOTEXISTS);
				}
				fcAcc = list.get(0);
	        }else if(NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
	        		|| NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())){
	        	//转付、代付(借：2120企业活期  贷：1020银行活期)
	    		fcAcc = innerAccountService.getByBlnctrAcc(dto.getBankAcc().getCounterAccId());
	    		if(EmptyUtils.isEmpty(fcAcc)){
	    			//未维护银行活期账号
					throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, dto.getBankAcc().getBankAcc());
				}
	        }else{
	        	throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_VOUCHERTYPE_NOTEXISTS);
	        }
        }
		else if(FinaBookConstant.BusOriType.NB_UNIT_ASK_PAYMENT.get().equals(dto.getBusOriType())
        		|| FinaBookConstant.BusOriType.NB_CNT_DOWN_PAYMENT.get().equals(dto.getBusOriType())
        		|| FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(dto.getBusOriType())){
        	//单位申请付款-请款(借：2120企业活期（收方）  贷：1020银行活期（付方）)
        	//中心申请付款-下拨(借：2120企业活期（收方） 贷：1020银行活期（付方）)
        	//中心申请付款-头寸调拨(借：1020银行活期（收方）  贷：1020银行活期（付方）)
        	fcAcc = innerAccountService.getByBlnctrAcc(dto.getBankAcc().getCounterAccId());
        	if(EmptyUtils.isEmpty(fcAcc)){
        		//未维护银行活期账号
				throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, dto.getBankAcc().getBankAcc());
			}
        }
		if(EmptyUtils.isEmpty(dto.getFcAcc())){
        	//未维护企业活期账号
			if(FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(dto.getBusOriType())){
				throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, dto.getPayeeAcc());
			}else{
				//跨网点记账
				if(dto.getBookExt()!=null){
					throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL, dto.getPayeeAcc());
				}else {
					throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_2120_NOTEXISTS);
				}
			}
		}
        //借方
        FcAccbookDtlDTO debitAccbookDtl = new FcAccbookDtlDTO();
    	debitAccbookDtl.setAccount(dto.getFcAcc().getBlnctrAcc());
    	debitAccbookDtl.setCurCode(dto.getFcAcc().getCurId());
		//设置借方发生额
    	debitAccbookDtl.setDebitMoney(dto.getPayMoney());
    	debitAccbookDtl.setCreditMoney(BigDecimal.ZERO);
    	debitAccbookDtl.setAccName(dto.getFcAcc().getAccName());
    	debitAccbookDtl.setOrigVoucherNo(dto.getBillNo());
    	debitAccbookDtl.setOrigVoucherId(dto.getBillId());
    	debitAccbookDtl.setSummaryContent(dto.getPurpose());
    	accbookDtls.add(debitAccbookDtl);
    	//贷方
        FcAccbookDtlDTO creditAccbookDtl = new FcAccbookDtlDTO();
        creditAccbookDtl.setAccount(fcAcc.getBlnctrAcc());
    	creditAccbookDtl.setCurCode(fcAcc.getCurId());
		//设置贷方发生额
    	creditAccbookDtl.setCreditMoney(dto.getPayMoney());
    	creditAccbookDtl.setDebitMoney(BigDecimal.ZERO);
    	creditAccbookDtl.setAccName(fcAcc.getAccName());
    	creditAccbookDtl.setOrigVoucherNo(dto.getBillNo());
    	creditAccbookDtl.setOrigVoucherId(dto.getBillId());
    	creditAccbookDtl.setSummaryContent(dto.getPurpose());
        accbookDtls.add(creditAccbookDtl);
    	return accbookDtls;
    }
    
    /**
	 * 构建账务凭证分录明细记录
	 * @param dto
	 * @return
	 */
    public List<FcAccbookDtlDTO> getCheckAccbookDtls(NisAccbookVO dto) {
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        //根据业务判断构建分录
        FcAccDTO fcAcc = new FcAccDTO();
        if(FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(dto.getBusOriType())){
        	//一般付款
	        //获取系统结算方式
	  		FcVoucherType voucherType = fcVoucherTypeService.get(dto.getVoucherTypeId(), true);
	        if(NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode())){
	        	//内转(借：2120企业活期  贷：2120企业活期)
	        	fcAcc = innerAccountService.getByBlnctrAcc(dto.getPayeeAcc());
	        }else if(NetbankConstant.PAY_TYPE_04.equals(voucherType.getSysVoucherCode())){
	        	//现付(借：2120企业活期  贷：1010现金)
	        	List<FcAccDTO> list = innerAccountService.listAccountByMoreParams(dto.getFcAcc().getCurId(), null,
	        			dto.getFcAcc().getNetId(), null, SubjectConstant.SysSubject.SUBJECT_CASH.get());
				if(EmptyUtils.isEmptyList(list)){
					//未维护现金账号
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_1010_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
				}
				fcAcc = list.get(0);
	        }else if(NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
	        		|| NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())){
	        	//转付、代付(借：2120企业活期  贷：1020银行活期)
	    		fcAcc = innerAccountService.getByBlnctrAcc(dto.getBankAcc().getCounterAccId());
	    		if(EmptyUtils.isEmpty(fcAcc)){
	    			//未维护银行活期账号
	    			String[] params = {StringUtils.isEmpty(dto.getBankAcc().getAccName()) ? "" :  dto.getBankAcc().getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
				}
	        }else{
	        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_VOUCHERTYPE_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
	        }
        }
		else if(FinaBookConstant.BusOriType.NB_UNIT_ASK_PAYMENT.get().equals(dto.getBusOriType())
        		|| FinaBookConstant.BusOriType.NB_CNT_DOWN_PAYMENT.get().equals(dto.getBusOriType())
        		|| FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(dto.getBusOriType())){
        	//单位申请付款-请款(借：2120企业活期（收方）  贷：1020银行活期（付方）)
        	//中心申请付款-下拨(借：2120企业活期（收方） 贷：1020银行活期（付方）)
        	//中心申请付款-头寸调拨(借：1020银行活期（收方）  贷：1020银行活期（付方）)
        	fcAcc = innerAccountService.getByBlnctrAcc(dto.getBankAcc().getCounterAccId());
        	if(EmptyUtils.isEmpty(fcAcc)){
        		//未维护银行活期账号
        		String[] params = {StringUtils.isEmpty(dto.getBankAcc().getAccName()) ? "" :  dto.getBankAcc().getAccName()};
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
			}
        }
		if(EmptyUtils.isEmpty(dto.getFcAcc())){
        	//未维护企业活期账号
			if(FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(dto.getBusOriType())){
				String[] params = {StringUtils.isEmpty(fcAcc.getAccName())?"":fcAcc.getAccName()};
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
			}else{
				//跨网点记账
				if(dto.getBookExt()!=null){
					String[] params = {StringUtils.isEmpty(fcAcc.getAccName())?"":fcAcc.getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
				}else {
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_2120_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
				}
			}
		}
        //借方
        FcAccbookDtlDTO debitAccbookDtl = new FcAccbookDtlDTO();
    	debitAccbookDtl.setAccount(dto.getFcAcc().getBlnctrAcc());
    	debitAccbookDtl.setCurCode(dto.getFcAcc().getCurId());
		//设置借方发生额
    	debitAccbookDtl.setDebitMoney(dto.getPayMoney());
    	debitAccbookDtl.setCreditMoney(BigDecimal.ZERO);
    	debitAccbookDtl.setAccName(dto.getFcAcc().getAccName());
    	debitAccbookDtl.setOrigVoucherNo(dto.getBillNo());
    	debitAccbookDtl.setOrigVoucherId(dto.getBillId());
    	debitAccbookDtl.setSummaryContent(dto.getPurpose());
    	accbookDtls.add(debitAccbookDtl);
    	//贷方
        FcAccbookDtlDTO creditAccbookDtl = new FcAccbookDtlDTO();
        creditAccbookDtl.setAccount(fcAcc.getBlnctrAcc());
    	creditAccbookDtl.setCurCode(fcAcc.getCurId());
		//设置贷方发生额
    	creditAccbookDtl.setCreditMoney(dto.getPayMoney());
    	creditAccbookDtl.setDebitMoney(BigDecimal.ZERO);
    	creditAccbookDtl.setAccName(fcAcc.getAccName());
    	creditAccbookDtl.setOrigVoucherNo(dto.getBillNo());
    	creditAccbookDtl.setOrigVoucherId(dto.getBillId());
    	creditAccbookDtl.setSummaryContent(dto.getPurpose());
        accbookDtls.add(creditAccbookDtl);
    	return accbookDtls;
    }
}
