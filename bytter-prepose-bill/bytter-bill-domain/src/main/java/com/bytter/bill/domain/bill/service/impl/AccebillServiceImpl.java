package com.bytter.bill.domain.bill.service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.*;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.billPool.dto.AbPoolDtlDTO;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.bill.domain.bill.dao.AbBillBankAutoDtlDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.payment.api.service.INisBillService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
/**
 * 票据公用接口实现
 * @author luop
 * 2017-08-25  17:14:11
 */
@Service("accebillServiceImpl")
@Transactional
@DubboService
public class AccebillServiceImpl implements IAccebillService {
	@Autowired
	private AbReceiveBillLockService abReceiveBillLockService;
	@Autowired
	private AbBillEndorseService abBillEndorseService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbEmitBillPayService abEmitBillPayService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
	@Autowired
	private AbBillPledgeApplyService abBillPledgeApplyService;
	@Autowired
	private AbBillDiscountApplyService abBillDiscountApplyService;
	@Autowired
	private AbBillCollectionApplyService abBillcollectionApplyService;
	@Autowired
	private AbBillBalCompareQueryService abBillBalCompareQueryService;
	@Autowired
	private AbLogService abLogService;
	@DubboReference(check = false)
	private IAccebillPoolService accebillPoolService;
	@Autowired
	private AbBillPayMiddleService abBillPayMiddleService;
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@DubboReference(check = false)
	INisBillService iNisBillService;
	@Autowired
	private BillTicketSplittingUtils billTicketSplittingUtils;
	@Autowired
	private AbBillBankAutoDtlDao abBillBankAutoDtlDao;

	/**
	 * 应收票据批量锁定
	 * @param abAccrecordsIds 票据流水IDs
	 */
	@Override
	public void batchLock(String[] abAccrecordsIds,ICurrentUser userVo) {
		batchLockOrUnLock(abAccrecordsIds,true,userVo);
	}
	/**
	 * 应收票据批量解锁
	 * @param abAccrecordsIds 票据流水IDs
	 */
	@Override
	public void batchUnLock(String[] abAccrecordsIds,ICurrentUser userVo) {
		batchLockOrUnLock(abAccrecordsIds,false,userVo);
	}
	@Override
	public void batchHoldEndorse(String[] abAccrecordsIds, ICurrentUser userVo) {
		batchHoldOrUnHoldEndorse(abAccrecordsIds,true,userVo);
	}
	@Override
	public void batchUnHoldEndorse(String[] abAccrecordsIds, ICurrentUser userVo) {
		batchHoldOrUnHoldEndorse(abAccrecordsIds,false,userVo);
	}
	/**
	 * 票据背书流水占用或者解除占用
	 * @param abAccrecordsIds 票据流水IDs
	 * isHold true:占用，flase:取消占用
	 * userVo 操作用户
	 */
	private void batchHoldOrUnHoldEndorse(String[] abAccrecordsIds, boolean isHold, ICurrentUser userVo){
		if(abAccrecordsIds==null||abAccrecordsIds.length==0){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		String logType=AccebillConstant.ABLOG_LOGTYPE_ADD;
		String msg=AccebillConstant.LANG_LOG_ENDORSE_HOLD;
		String validSign=AccebillConstant.AB_BILL_VALID_SIGN_2;
		if(!isHold){
			logType=AccebillConstant.ABLOG_LOGTYPE_DELETE;
			msg=AccebillConstant.LANG_LOG_ENDORSE_UNHOLD;
			validSign=AccebillConstant.AB_BILL_VALID_SIGN_1;
		}
		int size=abAccrecordsService.batchUpdate(abAccrecordsIds, validSign, userVo);
		if(size!=abAccrecordsIds.length && isHold){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ADDBILL_CONFLICT);
		}
		abLogService.batchSaveAbLog(null,abAccrecordsIds,null,logType,msg,userVo);
	}
	/**
	 * 票据锁定或解锁处理
	 * @param abAccrecordsIds 票据流水IDs
	 * flag true:锁定，flase:解锁
	 */
	private void batchLockOrUnLock(String[] abAccrecordsIds,boolean flag,ICurrentUser userVo) {
		if(abAccrecordsIds==null||abAccrecordsIds.length==0){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbReceiveBillLockVo abReceiveBillLockVo = new AbReceiveBillLockVo();
		String ids = "",vvids="";
		AbAccrecords abAccrecords = null;
		for(int i=0;i<abAccrecordsIds.length;i++){
			if(StringUtils.isNotEmpty (abAccrecordsIds[i]) && !"null".equals(abAccrecordsIds[i])){
				abAccrecords = abAccrecordsService.get(abAccrecordsIds[i],true);
				//为空时可能选择的是票据池明细流水，则查票据池明细流水信息
				if(abAccrecords==null){
					AbPoolDtlDTO poolDtlold = accebillPoolService.getAbPoolDtlDtoByParams(abAccrecordsIds[i]);
					if(poolDtlold!=null){
						//根据票据从表id和票据池交易明细id查询最新的流水信息
						abAccrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtlold.getVvId(), poolDtlold.getId());
					}
				}
				if(abAccrecords==null){
					if(!flag){
						continue;
					}
					//票据信息不存在
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
				}
				//付款锁定的时候才需要此判断，要不然付款单删除的时候会报错
				if(flag && AccebillConstant.BRIEF_STATUS_010.equals(abAccrecords.getBriefId())){
					AbVoucherinfo voucherinfo=abVoucherinfoService.get(abAccrecords.getVid(),true);
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BOXSTATUSERROR,voucherinfo.getOrigNo());
				}
				if(StrUtil.isEmpty(ids)){
					ids = abAccrecords.getId();
					vvids = abAccrecords.getVvid();
				}else{
					ids = ids+","+abAccrecords.getId();
					vvids = vvids+","+abAccrecords.getVvid();
				}
			}
		}
		if(StringUtils.isNotEmpty (ids) && StringUtils.isNotEmpty (vvids)){
			abReceiveBillLockVo.setIds(ids);
			abReceiveBillLockVo.setVvids(vvids);
			if(flag){
				abReceiveBillLockVo.setReason(AccebillConstant.AB_ACCEBILL_LOCK);//付款锁定
				abReceiveBillLockService.batchLock(abReceiveBillLockVo,userVo);
			}else{
				abReceiveBillLockService.batchUnLock(abReceiveBillLockVo, userVo);
			}
		}
	}
	/**
	 * 根据id判断是否是票据池明细数据
	 * @param id 流水id
	 * @return 是否是票据池数据
	 */
	@Override
	public boolean isPoolDtl(String id){
		boolean isPool = false;
		if(StringUtils.isNotEmpty (id)) {
			AbAccrecords abAccrecords = abAccrecordsService.get(id, true);
			//为空时可能选择的是票据池明细流水，则查票据池明细流水信息
			if (abAccrecords == null) {
				AbPoolDtlDTO poolDtlold = accebillPoolService.getAbPoolDtlDtoByParams(id);
				if (poolDtlold != null) {
					//根据票据从表id和票据池交易明细id查询最新的流水信息
					//abAccrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtlold.getVvId(), poolDtlold.getId());
					isPool = true;
				}
			}
		}
		return isPool;
	}
	@Override
	public String batchEndorse(String[] abAccrecordsIds, String pledgeeName, String pledgeeAcc, String pledgeeBankCode, String payId, String billBalances, Date businessDate, boolean insideSing,ICurrentUser userVo){
		if(abAccrecordsIds==null||abAccrecordsIds.length==0||StringUtils.isEmpty(pledgeeName)||StringUtils.isEmpty(pledgeeAcc)
				||StringUtils.isEmpty(pledgeeBankCode)){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbBillEndorseVo abBillEndorseVo = getAbBillEndorseVo(abAccrecordsIds,pledgeeName,pledgeeAcc,pledgeeBankCode,payId,billBalances,insideSing);
		if(businessDate==null){
			abBillEndorseVo.setBusinessDate(new Date());//业务归属日期
		}else{
			abBillEndorseVo.setBusinessDate(businessDate);//业务归属日期
		}
		abBillEndorseService.batchEndorse(abBillEndorseVo,insideSing,userVo);
		return abBillEndorseVo.getBillIds();
	}
	/**
	 * 修改从表付款状态和付款单id
	 * @param billAccerIds 流水ids
	 * @param isPay 付款状态: 1付过款   0或空则没付过款
	 * @param voucherId 付款单id
	 * @param userVo 当前用户
	 * @param  isBudget 是否挂预算（true：则释放票据的原有的预算 false:则不预算票据原有的预算）
	 */
	@Override
	public void updateBillPayInfo(String[] billAccerIds, String isPay, String voucherId, ICurrentUser userVo,boolean isBudget) {
		if(billAccerIds!=null||billAccerIds.length!=0){
			int num = abVoucherinfoViceService.batchUpdateBillIsPayAndVoucherId(billAccerIds,isPay,voucherId,userVo,isBudget);
		}
	}
	/**
	 * 根据参数获取背书参数对象
	 * @return
	 */
	@Transactional(readOnly = true)
	protected AbBillEndorseVo getAbBillEndorseVo(String[] abAccrecordsIds, String pledgeeName, String pledgeeAcc, String pledgeeBankCode, String payId, String billBalances, boolean insideSing){
		AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
		String ids = "",vids="",vvids="";
		AbAccrecords abAccrecords = null;
		for(int i=0;i<abAccrecordsIds.length;i++){
			abAccrecords = abAccrecordsService.get(abAccrecordsIds[i],true);
			if(i==0){
				ids = abAccrecordsIds[i];
				vids = abAccrecords.getVid();
				vvids = abAccrecords.getVvid();
			}else{
				ids = ids+","+abAccrecordsIds[i];
				vids = vids+","+abAccrecords.getVid();
				vvids = vvids+","+abAccrecords.getVvid();
			}
		}
		abBillEndorseVo.setIds(ids);//流水表id
		abBillEndorseVo.setVids(vids);//主表id
		abBillEndorseVo.setVvids(vvids);//从表id
		abBillEndorseVo.setPayId(payId);//付款表ID
		abBillEndorseVo.setTransMoneys(billBalances);//挂接付款金额
		abBillEndorseVo.setPledgeeName(pledgeeName);//背入人
		abBillEndorseVo.setPledgeeAcc(pledgeeAcc);//背入人账号
		abBillEndorseVo.setPledgeeBankAcc(pledgeeBankCode);//背入人开户行号
		abBillEndorseVo.setInteriorApply(insideSing?AccebillConstant.AB_INSIDE_SING_Y:AccebillConstant.AB_INSIDE_SING_N);//内部业务标志
		return abBillEndorseVo;
	}
	/**
	 * 应付开票暂存
	 * @param abEmitBillDTO 应付开票暂存参数
	 */
	@Override
	public void saveAbEmitBill(AbEmitBillDTO abEmitBillDTO, ICurrentUser userVo) {
		if(abEmitBillDTO==null){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		//开票单位(ID)
		if(StringUtils.isEmpty(abEmitBillDTO.getCorpId())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CORPID_ISNOTNULL);
		}
		//票据金额
		if(abEmitBillDTO.getBillMoney()==null||abEmitBillDTO.getBillMoney().compareTo(BigDecimal.ZERO)!=1){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLMONEY_ISNOTNULL);
		}
//		//出票人
//		if(StringUtils.isEmpty(abEmitBillDTO.getPayer())){
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYER_ISNOTNULL);
//		}
		BtBankAccDTO btBankAccDTO = null;
		//出票人账号
		if(StringUtils.isNotEmpty (abEmitBillDTO.getPayeracc())){
			//出票人开户银行名称
			//出票人开户银行行号
			btBankAccDTO = abEmitBillService.getBtBankAccDTO(abEmitBillDTO.getPayeracc());
			abEmitBillDTO.setPayerBankName(btBankAccDTO.getBankName());
			abEmitBillDTO.setPayerbankno(btBankAccDTO.getBankCode());
			abEmitBillDTO.setPayer(btBankAccDTO.getAccName());
		}
		//收票人
		if(StringUtils.isEmpty(abEmitBillDTO.getPayee())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEE_ISNOTNULL);
		}
		//收票人账号
		if(StringUtils.isEmpty(abEmitBillDTO.getPayeeacc())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTNULL);
		}
		//收票人开户银行名称
		if(StringUtils.isEmpty(abEmitBillDTO.getPayeeBankName())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEBANKNAME_ISNOTNULL);
		}
		//收票人开户银行行号
		if(StringUtils.isEmpty(abEmitBillDTO.getPayeebankno())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEBANKNO_ISNOTNULL);
		}
		//外部系统id
		if(StringUtils.isEmpty(abEmitBillDTO.getVoucherId())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VOUCHERID_ISNOTNULL);
		}
		AbEmitBillVo abEmitBillVo = new AbEmitBillVo();
		BeanUtils.copyNotNullProperties(abEmitBillDTO, abEmitBillVo);
		//开票数据来源--应付付款
		abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE10);
		//承兑人默认为出票人
		//modify 2017/10/10 承兑人在编辑时默认为空，票据类型选择为银票时默认为空，选择为商票时默认为出票人
		/*abEmitBillVo.setAcceptor(abEmitBillVo.getPayer());
		abEmitBillVo.setAcceptoracc (abEmitBillVo.getPayeracc ());
		abEmitBillVo.setAcceptorBankName(abEmitBillVo.getPayerBankName());
		abEmitBillVo.setAcceptorbankno(abEmitBillVo.getPayerbankno());*/
		abEmitBillVo.setInterfaceCall(true);//接口调用标志
		String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
		abEmitBillVo.setAppNo (appNo);//开票申请单号
		//radio选择框设置默认值
		/*abEmitBillVo.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));//流通介质
		abEmitBillVo.setBillType(Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK));//票据类型
		abEmitBillVo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));//内部标志:对外*/
		//付款开票登记如果无值，开出方式默认为对外
		if(abEmitBillVo.getInsideSing()==null){
			abEmitBillVo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));
		}
		abEmitBillVo.setIsNisPayAdd(AccebillConstant.BILL_ISNISPAYADD);
		abEmitBillService.saveOrUpdate(abEmitBillVo,AccebillConstant.BILL_OPERATE_ADD,userVo);
		abBillNotificationService.sendMsg(abEmitBillVo.getId());
		abEmitBillDTO.setBillId(abEmitBillVo.getId());
	}

	/**
	 * 应付开票暂存
	 * @param voDTO 应付开票暂存参数
	 */
	@Override
	@Transactional
	public AbEmitBillVoDTO saveAbEmitBillNew(AbEmitBillVoDTO voDTO, ICurrentUser userVo) {

		if(voDTO==null){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		//开票单位(ID)
		if(StringUtils.isEmpty(voDTO.getCorpId())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CORPID_ISNOTNULL);
		}
		//票据金额
		if(voDTO.getBillMoney()==null||voDTO.getBillMoney().compareTo(BigDecimal.ZERO)!=1){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLMONEY_ISNOTNULL);
		}
//		//出票人
//		if(StringUtils.isEmpty(abEmitBillDTO.getPayer())){
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYER_ISNOTNULL);
//		}
		BtBankAccDTO btBankAccDTO = null;
		//出票人账号
		if(StringUtils.isNotEmpty (voDTO.getPayeracc())){
			//出票人开户银行名称
			//出票人开户银行行号
			btBankAccDTO = abEmitBillService.getBtBankAccDTO(voDTO.getPayeracc());
			voDTO.setPayerBankName(btBankAccDTO.getBankName());
			voDTO.setPayerbankno(btBankAccDTO.getBankCode());
			voDTO.setPayer(btBankAccDTO.getAccName());
		}
		//收票人
		if(StringUtils.isEmpty(voDTO.getPayee())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEE_ISNOTNULL);
		}
		//收票人账号
		if(StringUtils.isEmpty(voDTO.getPayeeacc())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTNULL);
		}
		//收票人开户银行名称
		if(StringUtils.isEmpty(voDTO.getPayeeBankName())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEBANKNAME_ISNOTNULL);
		}
		//收票人开户银行行号
		if(StringUtils.isEmpty(voDTO.getPayeebankno())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEBANKNO_ISNOTNULL);
		}
		//外部系统id
		if(StringUtils.isEmpty(voDTO.getVoucherId())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VOUCHERID_ISNOTNULL);
		}
		AbEmitBillVo abEmitBillVo = new AbEmitBillVo();
		if(StrUtil.isNotEmpty(voDTO.getOpenBillInfo())) {
			Map json2Map = JsonUtils.json2Map(voDTO.getOpenBillInfo());
			RatUrationDTO ratUrationDTO = BeanUtil.copyProperties(json2Map, RatUrationDTO.class);
			RatUrationDtlDTO dtl = new RatUrationDtlDTO();
			dtl.setItemCode(json2Map.get("applyDtls[0].itemCode") == null ? null : json2Map.get("applyDtls[0].itemCode").toString());
			dtl.setItemMoney((json2Map.get("applyDtls[0].itemMoney") == null || StringUtils.isBlank(json2Map.get("applyDtls[0].itemMoney").toString())) ? null : new BigDecimal(json2Map.get("applyDtls[0].itemMoney").toString()));
			if(StringUtils.isNotBlank(dtl.getItemCode()) && dtl.getItemMoney() != null){
				List<RatUrationDtlDTO> applyDtls = new ArrayList<>();
				applyDtls.add(dtl);
				ratUrationDTO.setApplyDtls(applyDtls);
			}
			voDTO.setCreditCode(ratUrationDTO.getRatItemCode());
			abEmitBillVo.setUrationDTO(ratUrationDTO);
		}
		BeanUtils.copyNotNullProperties(voDTO, abEmitBillVo);
		//开票数据来源--应付付款
		abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE10);
		//承兑人默认为出票人
		//modify 2017/10/10 承兑人在编辑时默认为空，票据类型选择为银票时默认为空，选择为商票时默认为出票人
		/*abEmitBillVo.setAcceptor(abEmitBillVo.getPayer());
		abEmitBillVo.setAcceptoracc (abEmitBillVo.getPayeracc ());
		abEmitBillVo.setAcceptorBankName(abEmitBillVo.getPayerBankName());
		abEmitBillVo.setAcceptorbankno(abEmitBillVo.getPayerbankno());*/
		abEmitBillVo.setInterfaceCall(true);//接口调用标志
		//radio选择框设置默认值
		/*abEmitBillVo.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));//流通介质
		abEmitBillVo.setBillType(Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK));//票据类型
		abEmitBillVo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));//内部标志:对外*/
		//付款开票登记如果无值，开出方式默认为对外
		if(abEmitBillVo.getInsideSing()==null){
			abEmitBillVo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));
		}
		abEmitBillVo.setIsNisPayAdd(AccebillConstant.BILL_ISNISPAYADD);
		abEmitBillVo.setIsPayFlg("2"); //付款办理-弹窗开票
		abEmitBillService.saveOrUpdate(abEmitBillVo,AccebillConstant.BILL_OPERATE_ADD,userVo);
		abBillNotificationService.sendMsg(abEmitBillVo.getId());
		voDTO.setBillId(abEmitBillVo.getId());
		voDTO.setId(abEmitBillVo.getId());
		return voDTO;
	}

	/**
	 * 付款办理开票
	 * @param vids
	 * @param vvids
	 * @param aids
	 * @param userVo
	 */
	public void payOpenBillBatchSubmit(String vids, String vvids, String aids, ICurrentUser userVo){
		abEmitBillService.payOpenBillBatchSubmit(vids,vvids,aids,userVo);
	};

	/**
	 * ERP应付开票暂存
	 *
	 * @param abEmitBillDTO 应付开票暂存参数
	 */
	@Override
	public void saveAbEmitBillERP(AbEmitBillDTO abEmitBillDTO) {
		if (abEmitBillDTO == null) {
			//传递的参数值不能为空
			throw new RuntimeException("传递参数值不允许为空！");
		}
		//开票单位(ID)
		if (StringUtils.isEmpty(abEmitBillDTO.getCorpId())) {
			throw new RuntimeException("开票单位参数值不允许为空！");
		}
		// 校验单位
		if (!EmptyUtils.isBlank(abEmitBillDTO.getCorpId())) {
			CorpDTO corp = abCorpService.get(abEmitBillDTO.getCorpId());
			if (EmptyUtils.isEmpty(corp)) {
				throw new RuntimeException(String.format("单位Id为[%s],不存在!", abEmitBillDTO.getCorpId()));
			}
		}
		//流通介质
		if (abEmitBillDTO.getBillFlag() == null) {
			throw new RuntimeException("流通介质不允许为空！");
		} else {
			if (!AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillDTO.getBillFlag()))
					&& !AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillDTO.getBillFlag()))) {
				throw new RuntimeException("流通介质参数值错误,纸票=10,电票=11");
			}
		}
		//票据金额
		if (abEmitBillDTO.getBillMoney() == null || abEmitBillDTO.getBillMoney().compareTo(BigDecimal.ZERO) != 1) {
			throw new RuntimeException("票据金额不能为空！");
		}else{
			// 金额最小为0.01
			if (abEmitBillDTO.getBillMoney().compareTo(BigDecimal.valueOf(0.01)) < 0) {
				throw new RuntimeException("票据金额最小值为0.01");
			}
			// 电票不能超过10亿
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillDTO.getBillFlag()))
					&& (abEmitBillDTO.getBillMoney().compareTo(BigDecimal.valueOf(1000000000.00)) > 0)) {
				throw new RuntimeException("电票金额不能大于1000000000");
			}
			// 判断纸票金额不能超过设置的额度
			if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillDTO.getBillFlag()))
					&& (abEmitBillDTO.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
				throw new RuntimeException("纸票金额不能大于"+abVoucherinfoViceService.getMaxBillMoney());
			}
		}
//		//出票人
//		if(StringUtils.isEmpty(abEmitBillDTO.getPayer())){
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYER_ISNOTNULL);
//		}
		//票据类型
		if (abEmitBillDTO.getBillType() == null) {
			throw new RuntimeException("票据类型不允许为空！");
		} else {
			if (!AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abEmitBillDTO.getBillType()))
					&& !AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abEmitBillDTO.getBillType()))) {
				throw new RuntimeException("票据类型参数值错误！");
			}
		}
		//不得转让标记
		if (StringUtils.isEmpty(abEmitBillDTO.getBanendrsmtmk())) {
			throw new RuntimeException("不得转让标记不允许为空！");
		} else {
			if (!AccebillConstant.AB_BANEM_Y.equals(abEmitBillDTO.getBanendrsmtmk())
					&& !AccebillConstant.AB_BANEM_N.equals(abEmitBillDTO.getBanendrsmtmk())) {
				throw new RuntimeException("不得转让标记参数值错误！");
			}
		}
		//出票日期
		if (abEmitBillDTO.getStartDate() == null) {
			throw new RuntimeException("出票日期不允许为空！");
		}
		//到期日期
		if (abEmitBillDTO.getEndDate() == null) {
			throw new RuntimeException("到期日期不允许为空！");
		}
		//开出方式
		if (abEmitBillDTO.getInsideSing() == null) {
			throw new RuntimeException("开出方式不允许为空！");
		} else {
			if (!AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillDTO.getInsideSing()))
					&& !AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abEmitBillDTO.getInsideSing()))) {
				throw new RuntimeException("开出方式参数值错误！");
			}
		}
		//业务办理日期
		if (abEmitBillDTO.getBusinessDate() == null) {
			throw new RuntimeException("业务办理日期不允许为空！");
		}
		BtBankAccDTO btBankAccDTO = null;
		//出票人账号
		if (StringUtils.isNotEmpty(abEmitBillDTO.getPayeracc())) {
			//出票人开户银行名称
			//出票人开户银行行号
			btBankAccDTO = abEmitBillService.getBtBankAccDTO(abEmitBillDTO.getPayeracc());
			abEmitBillDTO.setPayerBankName(btBankAccDTO.getBankName());
			abEmitBillDTO.setPayerbankno(btBankAccDTO.getBankCode());
			abEmitBillDTO.setPayer(btBankAccDTO.getAccName());
		}
		//收票人
		if (StringUtils.isEmpty(abEmitBillDTO.getPayee())) {
			throw new RuntimeException("收票人不能为空！");
		}
		//收票人账号
		if (StringUtils.isEmpty(abEmitBillDTO.getPayeeacc())) {
			throw new RuntimeException("收票人账号不能为空！");
		}
		//收票人开户银行名称
		if (StringUtils.isEmpty(abEmitBillDTO.getPayeeBankName())) {
			throw new RuntimeException("收票人开户银行名称不能为空！");
		}
		//收票人开户银行行号
		if (StringUtils.isEmpty(abEmitBillDTO.getPayeebankno())) {
			throw new RuntimeException("收票人开户银行行号不能为空！");
		}
		//外部系统id
		/*if (StringUtils.isEmpty(abEmitBillDTO.getVoucherId())) {
			throw new RuntimeException("外部系统id不能为空!");
		}*/
		if (StringUtils.isNotEmpty(abEmitBillDTO.getOrigNo())) {
			// 新增
			if (abVoucherinfoService.checkOrigNo(abEmitBillDTO.getCorpId(), abEmitBillDTO.getOrigNo(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
					abEmitBillDTO.getSbbllRngStrtSn(),abEmitBillDTO.getSbbllRngEndSn())) {
				// 该票据号已经存在,请检查!
				throw new RuntimeException(String.format("票据号[%s]已经存在,请检查!", abEmitBillDTO.getOrigNo()));
			}
		}
		AbEmitBillVo abEmitBillVo = new AbEmitBillVo();
		BeanUtils.copyNotNullProperties(abEmitBillDTO, abEmitBillVo);
		//开票数据来源
		abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE15);
		abEmitBillVo.setInterfaceCall(true);//接口调用标志
		String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
		abEmitBillVo.setAppNo(appNo);//开票申请单号
		if (abEmitBillDTO.getInsideSing() == null) {
			abEmitBillVo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));
		}else{
			abEmitBillVo.setInsideSing(Integer.parseInt(abEmitBillDTO.getInsideSing()));
		}
		abEmitBillService.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, null);
		if (StringUtils.isNotEmpty(abEmitBillVo.getId())) {
			abBillNotificationService.sendMsg(abEmitBillVo.getId());
		}
	}
	/**
	 * ERP应付票据线下兑付确认
	 *
	 * @param abEmitBillDTOList
	 */
	@Override
	public void cashingConfirmationERP(List<AbEmitBillDTO> abEmitBillDTOList) {
		if (abEmitBillDTOList != null && !abEmitBillDTOList.isEmpty()) {
			for (AbEmitBillDTO abEmitBillDTO : abEmitBillDTOList) {
				if (abEmitBillDTO == null) {
					//传递的参数值不能为空
					throw new RuntimeException("传递的参数值不能为空!");
				}
				//开票单位(ID)
				if (StringUtils.isEmpty(abEmitBillDTO.getCorpId())) {
					throw new RuntimeException("所属单位不能为空!");
				}
				// 校验单位
				if (!EmptyUtils.isBlank(abEmitBillDTO.getCorpId())) {
					CorpDTO corp = abCorpService.get(abEmitBillDTO.getCorpId());
					if (EmptyUtils.isEmpty(corp)) {
						throw new RuntimeException(String.format("单位Id为[%s],不存在!", abEmitBillDTO.getCorpId()));
					}
				}
				//票据号(origNO)
				if (StringUtils.isEmpty(abEmitBillDTO.getOrigNo())) {
					throw new RuntimeException("票据号码不能为空!");
				}
				//校验票据号是否存在
				AbVoucherinfoVO reInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abEmitBillDTO.getOrigNo(),abEmitBillDTO.getSbbllRngStrtSn(),abEmitBillDTO.getSbbllRngEndSn());
				if (reInfo == null) {
					throw new RuntimeException(String.format("票据号码为[%s],不存在该票据!", abEmitBillDTO.getOrigNo()));
				}
				//根据票据主信息ID和单位ID获取从表数据
				AbVoucherinfoVice reviceInfo = abVoucherinfoViceService.getVoucherinfoViceByVidAndCorpId(reInfo.getId(), abEmitBillDTO.getCorpId(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE0, null);
				if (reviceInfo == null) {
					throw new RuntimeException("根据单位和票据号码查无票据信息!");
				}
				//根据票据主表id和从表id获取可兑付确认的票据信息
				List<AbEmitBillPayVo> emitPayList = abEmitBillPayService.listAbEmitBillbyVidAndVvid(reviceInfo.getVid(), reviceInfo.getId());
				if (emitPayList != null && !emitPayList.isEmpty()) {
					AbEmitBillPayVo emitBillPayVo = emitPayList.get(0);
					AbEmitBillPayVo abEmitBillPayVo = new AbEmitBillPayVo();
					BeanUtils.copyNotNullProperties(abEmitBillDTO, abEmitBillPayVo);
					abEmitBillPayVo.setIds(emitBillPayVo.getId());
					abEmitBillPayVo.setVid(reviceInfo.getVid());
					abEmitBillPayVo.setVvid(reviceInfo.getId());
					abEmitBillPayVo.setDate(abEmitBillDTO.getBusinessDate());
					//线下兑付
					abEmitBillPayVo.setEmitSttlmmk(AccebillConstant.AB_BILL_PAYEMITSTTLMMK0);
					abEmitBillPayService.cashingConfirmationERP(abEmitBillPayVo, null);
				} else {
					throw new RuntimeException("根据单位和票据号码查无可兑付确认票据信息!");
				}
			}
		} else {
			//传递的参数值不能为空
			throw new RuntimeException("传递的参数值不能为空!");
		}
	}
	/**
	 * ERP应收票据登记暂存
	 * @param abReceiveBillDTO 应收票据登记暂存参数
	 */
	@Override
	public void saveAbReceiveBillERP(AbReceiveBillDTO abReceiveBillDTO) {
		//校验参数
		checkAbReceiveBill(abReceiveBillDTO);
		AbReceiveBillVO abReceiveBillVO = new AbReceiveBillVO();
		AbVoucherinfo vouInfo = new AbVoucherinfo();// 主表信息
		AbVoucherinfoVice viceInfo = new AbVoucherinfoVice();// 从表信息
		AbAccrecords accRecords = new AbAccrecords(); // 流水信息
		// 写入主信息表
		BeanUtils.copyNotNullProperties(abReceiveBillDTO, vouInfo);
		// 写入从表信息
		BeanUtils.copyNotNullProperties(abReceiveBillDTO, viceInfo);
		// 写入流水信息
		BeanUtils.copyNotNullProperties(abReceiveBillDTO, accRecords);
		abReceiveBillVO.setAbVoucherInfo(vouInfo);
		abReceiveBillVO.setAbVoucherinfoVice(viceInfo);
		abReceiveBillVO.setAbAccrecords(accRecords);
		abReceiveBillService.registerAbReceiveBill(abReceiveBillVO,AccebillConstant.BILL_OPERATE_SAVE,null);
	}
	public void checkAbReceiveBill(AbReceiveBillDTO abReceiveBillDTO){
		if (abReceiveBillDTO == null) {
			//传递的参数值不能为空
			throw new RuntimeException("传递参数值不允许为空！");
		}
		//所属单位(ID)
		if (StringUtils.isEmpty(abReceiveBillDTO.getCorpId())) {
			throw new RuntimeException("所属单位参数值不允许为空！");
		}
		// 校验单位
		if (!EmptyUtils.isBlank(abReceiveBillDTO.getCorpId())) {
			CorpDTO corp = abCorpService.get(abReceiveBillDTO.getCorpId());
			if (EmptyUtils.isEmpty(corp)) {
				throw new RuntimeException(String.format("单位Id为[%s]不存在!", abReceiveBillDTO.getCorpId()));
			}
		}
		//票据号码
		if (StringUtils.isEmpty(abReceiveBillDTO.getOrigNo())) {
			throw new RuntimeException("票据号码不允许为空！");
		}
		//判断票据号码是否存在
		if (StringUtils.isNotEmpty(abReceiveBillDTO.getOrigNo())) {
			// 新增
			if (abVoucherinfoService.checkOrigNo(abReceiveBillDTO.getCorpId(), abReceiveBillDTO.getOrigNo(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1,
					abReceiveBillDTO.getSbbllRngStrtSn(),abReceiveBillDTO.getSbbllRngEndSn())) {
				// 该票据号已经存在,请检查!
				throw new RuntimeException(String.format("票据号[%s]已经存在,请检查!", abReceiveBillDTO.getOrigNo()));
			}
		}
		//流通介质
		if (abReceiveBillDTO.getBillFlag() == null) {
			throw new RuntimeException("流通介质不允许为空！");
		} else {
			if (!AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abReceiveBillDTO.getBillFlag())) && !AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abReceiveBillDTO.getBillFlag()))) {
				throw new RuntimeException("流通介质参数值错误,纸票=10,电票=11");
			}
		}
		//所属库存
		if (StringUtils.isEmpty(abReceiveBillDTO.getEnterStock())) {
			throw new RuntimeException("所属库存不允许为空！");
		} else {
			if (!AccebillConstant.AB_ENTER_STOCK_CORP.equals(abReceiveBillDTO.getEnterStock())
					&& !AccebillConstant.AB_ENTER_STOCK_NET.equals(abReceiveBillDTO.getEnterStock())
					&& !AccebillConstant.AB_ENTER_STOCK_BANK.equals(abReceiveBillDTO.getEnterStock())) {
				throw new RuntimeException("所属库存参数值错误！");
			}
		}
		//票据类型
		if (abReceiveBillDTO.getBillType() == null) {
			throw new RuntimeException("票据类型不允许为空！");
		} else {
			if (!AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abReceiveBillDTO.getBillType()))
					&& !AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abReceiveBillDTO.getBillType()))) {
				throw new RuntimeException("票据类型参数值错误！");
			}
		}
		//业务办理日期
		if (abReceiveBillDTO.getBusinessDate() == null) {
			throw new RuntimeException("业务办理日期不允许为空！");
		}
		//收票方式
		if (abReceiveBillDTO.getInsideSing() == null) {
			throw new RuntimeException("收票方式不允许为空！");
		} else {
			if (!AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abReceiveBillDTO.getInsideSing()))
					&& !AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abReceiveBillDTO.getInsideSing()))) {
				throw new RuntimeException("收票方式参数值错误！");
			}
		}
		//不得转让标记
		if (StringUtils.isEmpty(abReceiveBillDTO.getBanendrsmtmk())) {
			throw new RuntimeException("不得转让标记不允许为空！");
		} else {
			if (!AccebillConstant.AB_BANEM_Y.equals(abReceiveBillDTO.getBanendrsmtmk())
					&& !AccebillConstant.AB_BANEM_N.equals(abReceiveBillDTO.getBanendrsmtmk())) {
				throw new RuntimeException("不得转让标记参数值错误！");
			}
		}
		//出票日期
		if (abReceiveBillDTO.getStartDate() == null) {
			throw new RuntimeException("出票日期不允许为空！");
		}
		//到期日期
		if (abReceiveBillDTO.getEndDate() == null) {
			throw new RuntimeException("到期日期不允许为空！");
		}
		//票据金额
		if (abReceiveBillDTO.getBillMoney()==null||abReceiveBillDTO.getBillMoney().compareTo(BigDecimal.ZERO)!=1) {
			throw new RuntimeException("票据金额不能为空！");
		}else{
			// 金额最小为0.01
			if (abReceiveBillDTO.getBillMoney().compareTo(BigDecimal.valueOf(0.01)) < 0) {
				throw new RuntimeException("票据金额最小值为0.01");
			}
			// 电票不能超过10亿
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abReceiveBillDTO.getBillFlag()))
					&& (abReceiveBillDTO.getBillMoney().compareTo(BigDecimal.valueOf(1000000000.00)) > 0)) {
				throw new RuntimeException("电票金额不能大于1000000000");
			}
			// 判断纸票金额不能超过设置的额度
			if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abReceiveBillDTO.getBillFlag()))
					&& (abReceiveBillDTO.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
				throw new RuntimeException("纸票金额不能大于"+abVoucherinfoViceService.getMaxBillMoney());
			}
		}
		//前手
		if (StringUtils.isEmpty(abReceiveBillDTO.getCnAccrecords())) {
			throw new RuntimeException("前手不能为空！");
		}
		//票据来源
		if (StringUtils.isEmpty(abReceiveBillDTO.getBillSource())) {
			throw new RuntimeException("票据来源参数不能为空!");
		}else if (AccebillConstant.AB_BILL_SOURCE_REC.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE_SIGN.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE_DISCOUNT.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE15.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE16.equals(abReceiveBillDTO.getBillSource())
				|| AccebillConstant.AB_BILL_SOURCE17.equals(abReceiveBillDTO.getBillSource())) {
			abReceiveBillDTO.setBillSource(abReceiveBillDTO.getBillSource());
		}else{
			throw new RuntimeException("票据来源参数值错误,票据来源值只能为:10(收票)、11(背书)、12(质押)、13(签收)、14(贴现)、15(归集)、16(上划 )、17(下拨)。");
		}
		//出票人
		if (StringUtils.isEmpty(abReceiveBillDTO.getPayer())) {
			throw new RuntimeException("出票人不能为空！");
		}
		//收票人
		if (StringUtils.isEmpty(abReceiveBillDTO.getPayee())) {
			throw new RuntimeException("收票人不能为空！");
		}
		//承兑人
		if (StringUtils.isEmpty(abReceiveBillDTO.getAcceptor())) {
			throw new RuntimeException("承兑人不能为空！");
		}
		if (abReceiveBillDTO.getStartDate() != null && abReceiveBillDTO.getEndDate() != null) {
			if (abReceiveBillDTO.getStartDate().getTime() >= abReceiveBillDTO.getEndDate().getTime()) {// 到期日期必须大于出票日期
				throw new RuntimeException("到期日期必须大于出票日期!");
			}
			// 业务办理日期必须大于等于出票日期,小于到期日期!
			if (abReceiveBillDTO.getBusinessDate() != null
					&& !(abReceiveBillDTO.getBusinessDate().getTime() >= abReceiveBillDTO.getStartDate().getTime()
					&& abReceiveBillDTO.getBusinessDate().getTime() < abReceiveBillDTO.getEndDate().getTime())) {
				throw new RuntimeException("业务办理日期必须大于等于出票日期,小于到期日期! ");
			}
			// 电票不能超过一年
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abReceiveBillDTO.getBillFlag()))) {
				Calendar calendarElect = Calendar.getInstance();
				calendarElect.setTime(abReceiveBillDTO.getStartDate());
				calendarElect.add(Calendar.YEAR, 1);
				if (calendarElect.getTime().getTime() < abReceiveBillDTO.getEndDate().getTime()) {
					throw new RuntimeException("电票到期日不能超过开票日一年!");
				}
			}
			// 纸票不能超过半年
			if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abReceiveBillDTO.getBillFlag()))) {
				Calendar calendarPaper = Calendar.getInstance();
				calendarPaper.setTime(abReceiveBillDTO.getStartDate());
				calendarPaper.add(Calendar.MONTH, 6);
				if (calendarPaper.getTime().getTime() < abReceiveBillDTO.getEndDate().getTime()) {
					throw new RuntimeException("纸票到期日不能超过开票日半年!");
				}
			}
		}
	}
	/**
	 * ERP应收票据批量背书
	 *
	 * @param abBillEndorseDTOList
	 */
	@Override
	public void endorseERP(List<AbBillEndorseDTO> abBillEndorseDTOList) {
		if (abBillEndorseDTOList != null && !abBillEndorseDTOList.isEmpty()) {
			for (AbBillEndorseDTO abBillEndorseDTO : abBillEndorseDTOList) {
				if (abBillEndorseDTO == null) {
					//传递的参数值不能为空
					throw new RuntimeException("传递的参数值不能为空!");
				}
				//所属单位(ID)
				if (StringUtils.isEmpty(abBillEndorseDTO.getCorpId())) {
					throw new RuntimeException("所属单位不能为空!");
				}
				// 校验单位
				if (!EmptyUtils.isBlank(abBillEndorseDTO.getCorpId())) {
					CorpDTO corp = abCorpService.get(abBillEndorseDTO.getCorpId());
					if (EmptyUtils.isEmpty(corp)) {
						throw new RuntimeException(String.format("单位Id为[%s],不存在!", abBillEndorseDTO.getCorpId()));
					}
				}
				//票据号(origNO)
				if (StringUtils.isEmpty(abBillEndorseDTO.getOrigNo())) {
					throw new RuntimeException("票据号码不能为空!");
				}
				//校验票据号是否存在
				AbVoucherinfoVO reInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abBillEndorseDTO.getOrigNo(),abBillEndorseDTO.getSbbllRngStrtSn(),abBillEndorseDTO.getSbbllRngEndSn());
				if (reInfo == null) {
					throw new RuntimeException(String.format("票据号码为[%s],不存在该票据!", abBillEndorseDTO.getOrigNo()));
				}
				//根据票据主信息ID和单位ID获取从表数据
				AbVoucherinfoVice reviceInfo = abVoucherinfoViceService.getVoucherinfoViceByVidAndCorpId(reInfo.getId(), abBillEndorseDTO.getCorpId(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, null);
				if (reviceInfo == null) {
					throw new RuntimeException("根据单位和票据号码查无票据信息!");
				}
				//是否内部背书(interiorApply)
				if (StringUtils.isEmpty(abBillEndorseDTO.getInteriorApply())) {
					throw new RuntimeException("是否内部背书值不能为空!");
				} else {
					// 验证是否内部背书值正不正确
					if (!AccebillConstant.AB_INSIDE_SING_Y.equals(abBillEndorseDTO.getInteriorApply())
							&& !AccebillConstant.AB_INSIDE_SING_N.equals(abBillEndorseDTO.getInteriorApply())) {
						throw new RuntimeException("是否内部背书值错误!");
					}
					//为内部背书时
					if (AccebillConstant.AB_INSIDE_SING_Y.equals(abBillEndorseDTO.getInteriorApply())) {
						//被背书人单位ID不能为空
						if (StringUtils.isEmpty(abBillEndorseDTO.getPledgeeCorpid())) {
							throw new RuntimeException("被背书人单位ID不能为空!");
						}
						//被背书人开户行行名
						if (StringUtils.isEmpty(abBillEndorseDTO.getPledgeeBankName())) {
							throw new RuntimeException("被背书人开户行行名不能为空!");
						}
					}
				}
				//被背书人账号
				if (StringUtils.isEmpty(abBillEndorseDTO.getPledgeeAcc())) {
					throw new RuntimeException("被背书人账号不能为空!");
				}
				//被背书人名称
				if (StringUtils.isEmpty(abBillEndorseDTO.getPledgeeName())) {
					throw new RuntimeException("被背书人名称不能为空!");
				}
				//被背书人开户行行号
				if (StringUtils.isEmpty(abBillEndorseDTO.getPledgeeBankAcc())) {
					throw new RuntimeException("被背书人开户行行号不能为空!");
				}
				//验证是否可转让值是否为空
				if (StringUtils.isEmpty(abBillEndorseDTO.getBanendrsmtmk())) {
					throw new RuntimeException("是否可转让值不能为空!");
				} else {
					// 验证是否可转让值正不正确
					if (!AccebillConstant.AB_BANEM_Y.equals(abBillEndorseDTO.getBanendrsmtmk())
							&& !AccebillConstant.AB_BANEM_N.equals(abBillEndorseDTO.getBanendrsmtmk())) {
						throw new RuntimeException("是否可转让值错误!");
					}
				}
				//根据票据主表id和从表id获取可背书的票据信息
				List<AbBillEndorseVo> endorseList = abBillEndorseService.listAbBillEndorseVobyVidAndVvid(reviceInfo.getVid(), reviceInfo.getId());
				if (endorseList != null && !endorseList.isEmpty()) {
					AbBillEndorseVo abBillEndorseVo = endorseList.get(0);
					// 判断业务办理日期
					if (abBillEndorseDTO.getBusinessDate() != null) {
						// 业务办理日期必须大于等于出票日期,小于到期日期!
						if (abBillEndorseDTO.getBusinessDate().getTime() < abBillEndorseVo.getStartDate().getTime()
								|| abBillEndorseDTO.getBusinessDate().getTime() >= abBillEndorseVo.getEndDate().getTime()) {
							throw new RuntimeException("业务办理日期必须大于等于出票日期,小于到期日期!");
						}
					}
					Map<String,String> map = billTicketSplittingUtils.ticketSplitting(abBillEndorseVo.getVid(),abBillEndorseVo.getVvid(),abBillEndorseVo.getId(),abBillEndorseDTO.getTransMoney().toString(),null);
					//设值
					abBillEndorseVo.setIds(map.get("accIds"));
					BeanUtils.copyNotNullProperties(abBillEndorseDTO, abBillEndorseVo);
					abBillEndorseVo.setVids(map.get("infoIds"));
					abBillEndorseVo.setVvids(map.get("viceIds"));
					abBillEndorseService.endorsementApplicationErp(abBillEndorseVo, null,new String[]{abBillEndorseVo.getId()});
				} else {
					throw new RuntimeException("根据单位和票据号码查无可背书的票据信息!");
				}
			}
		} else {
			//传递的参数值不能为空
			throw new RuntimeException("传递的参数值不能为空!");
		}
	}
	/**
	 * ERP应收票据批量质押申请
	 *
	 * @param
	 */
	@Override
	public void pledgeApplyERP(List<AbBillPledgeApplyDTO> abBillPledgeApplyDTOList) {
		if (abBillPledgeApplyDTOList != null && !abBillPledgeApplyDTOList.isEmpty()) {
			for (AbBillPledgeApplyDTO abBillPledgeApplyDTO : abBillPledgeApplyDTOList) {
				if (abBillPledgeApplyDTO == null) {
					//传递的参数值不能为空
					throw new RuntimeException("传递的参数值不能为空!");
				}
				//所属单位(ID)
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getCorpId())) {
					throw new RuntimeException("所属单位不能为空!");
				}
				// 校验单位
				if (!EmptyUtils.isBlank(abBillPledgeApplyDTO.getCorpId())) {
					CorpDTO corp = abCorpService.get(abBillPledgeApplyDTO.getCorpId());
					if (EmptyUtils.isEmpty(corp)) {
						throw new RuntimeException(String.format("单位Id为[%s],不存在!", abBillPledgeApplyDTO.getCorpId()));
					}
				}
				//票据号(origNO)
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getOrigNo())) {
					throw new RuntimeException("票据号码不能为空!");
				}
				//校验票据号是否存在
				AbVoucherinfoVO reInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abBillPledgeApplyDTO.getOrigNo(),abBillPledgeApplyDTO.getSbbllRngStrtSn(),abBillPledgeApplyDTO.getSbbllRngEndSn());
				if (reInfo == null) {
					throw new RuntimeException(String.format("票据号码为[%s],不存在该票据!", abBillPledgeApplyDTO.getOrigNo()));
				}
				//根据票据主信息ID和单位ID获取从表数据
				AbVoucherinfoVice reviceInfo = abVoucherinfoViceService.getVoucherinfoViceByVidAndCorpId(reInfo.getId(), abBillPledgeApplyDTO.getCorpId(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, null);
				if (reviceInfo == null) {
					throw new RuntimeException("根据单位和票据号码查无票据信息!");
				}
				//是否内部质押(interiorApply)
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getInteriorApply())) {
					throw new RuntimeException("是否内部质押值不能为空!");
				} else {
					// 验证是否内部质押值正不正确
					if (!AccebillConstant.AB_INSIDE_SING_Y.equals(abBillPledgeApplyDTO.getInteriorApply())
							&& !AccebillConstant.AB_INSIDE_SING_N.equals(abBillPledgeApplyDTO.getInteriorApply())) {
						throw new RuntimeException("是否内部质押值错误!");
					}
					//为内部质押时
					if (AccebillConstant.AB_INSIDE_SING_Y.equals(abBillPledgeApplyDTO.getInteriorApply())) {
						//质权人单位ID不能为空
						if (StringUtils.isEmpty(abBillPledgeApplyDTO.getPledgeeCorpid())) {
							throw new RuntimeException("质权人单位ID不能为空!");
						}
						//质权人开户行行名
						if (StringUtils.isEmpty(abBillPledgeApplyDTO.getPledgeeBankName())) {
							throw new RuntimeException("质权人开户行行名不能为空!");
						}
					}
				}
				//质权人账号
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getPledgeeAcc())) {
					throw new RuntimeException("质权人账号不能为空!");
				}
				//质权人名称
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getPledgeeName())) {
					throw new RuntimeException("质权人名称不能为空!");
				}
				//质权人开户行行号
				if (StringUtils.isEmpty(abBillPledgeApplyDTO.getPledgeeBankAcc())) {
					throw new RuntimeException("质权人开户行行号不能为空!");
				}
				//根据票据主表id和从表id获取可质押的票据信息
				List<AbBillPledgeApplyVo> pledgeApplyList = abBillPledgeApplyService.listAbBillPledgeApplyVoByVidAndVvid(reviceInfo.getVid(), reviceInfo.getId());
				if (pledgeApplyList != null && !pledgeApplyList.isEmpty()) {
					AbBillPledgeApplyVo abBillPledgeApplyVo = pledgeApplyList.get(0);
					// 判断业务办理日期
					if (abBillPledgeApplyDTO.getBusinessDate() != null) {
						// 业务办理日期必须大于等于出票日期,小于到期日期!
						/*if (abBillPledgeApplyDTO.getBusinessDate().getTime() < abBillPledgeApplyVo.getStartDate().getTime()
								|| abBillPledgeApplyDTO.getBusinessDate().getTime() >= abBillPledgeApplyVo.getEndDate().getTime()) {
							throw new RuntimeException("业务办理日期必须大于等于出票日期,小于到期日期!");
						}*/
					}
					BeanUtils.copyNotNullProperties(abBillPledgeApplyDTO, abBillPledgeApplyVo);
					abBillPledgeApplyService.pledgeApplyERP(abBillPledgeApplyVo, null);
				} else {
					throw new RuntimeException("根据单位和票据号码查无可质押的票据信息!");
				}
			}
		} else {
			//传递的参数值不能为空
			throw new RuntimeException("传递的参数值不能为空!");
		}
	}
	/**
	 * ERP应收票据批量贴现申请
	 *
	 * @param
	 */
	@Override
	public void discountApplyERP(List<AbBillDiscountApplyDTO> abBillDiscountApplyDTOList) {
		if (abBillDiscountApplyDTOList != null && !abBillDiscountApplyDTOList.isEmpty()) {
			for (AbBillDiscountApplyDTO abBillDiscountApplyDTO : abBillDiscountApplyDTOList) {
				if (abBillDiscountApplyDTO == null) {
					//传递的参数值不能为空
					throw new RuntimeException("传递的参数值不能为空!");
				}
				//所属单位(ID)
				if (StringUtils.isEmpty(abBillDiscountApplyDTO.getCorpId())) {
					throw new RuntimeException("所属单位不能为空!");
				}
				// 校验单位
				if (!EmptyUtils.isBlank(abBillDiscountApplyDTO.getCorpId())) {
					CorpDTO corp = abCorpService.get(abBillDiscountApplyDTO.getCorpId());
					if (EmptyUtils.isEmpty(corp)) {
						throw new RuntimeException(String.format("单位Id为[%s],不存在!", abBillDiscountApplyDTO.getCorpId()));
					}
				}
				//票据号(origNO)
				if (StringUtils.isEmpty(abBillDiscountApplyDTO.getOrigNo())) {
					throw new RuntimeException("票据号码不能为空!");
				}
				//校验票据号是否存在
				AbVoucherinfoVO reInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abBillDiscountApplyDTO.getOrigNo(),null,null);
				if (reInfo == null) {
					throw new RuntimeException(String.format("票据号码为[%s],不存在该票据!", abBillDiscountApplyDTO.getOrigNo()));
				}
				//根据票据主信息ID和单位ID获取从表数据
				AbVoucherinfoVice reviceInfo = abVoucherinfoViceService.getVoucherinfoViceByVidAndCorpId(reInfo.getId(), abBillDiscountApplyDTO.getCorpId(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, null);
				if (reviceInfo == null) {
					throw new RuntimeException("根据单位和票据号码查无票据信息!");
				}
				//是否结算中心贴现
				if (abBillDiscountApplyDTO.getInsideSing() == null) {
					throw new RuntimeException("是否结算中心贴现值不能为空!");
				} else {
					// 验证是否结算中心贴现值正不正确
					if (!AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abBillDiscountApplyDTO.getInsideSing()))
							&& !AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abBillDiscountApplyDTO.getInsideSing()))) {
						throw new RuntimeException("是否结算中心贴现值错误!");
					}
					//为银行贴现时且是电票时
					if (AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abBillDiscountApplyDTO.getInsideSing()))
							&& AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(reInfo.getBillFlag()))) {
						//贴入银行账号不能为空
						if (StringUtils.isEmpty(abBillDiscountApplyDTO.getRecAcctId())) {
							throw new RuntimeException("贴入银行账号不能为空!");
						}
						//贴入银行行号
						if (StringUtils.isEmpty(abBillDiscountApplyDTO.getRecAcctsvcr())) {
							throw new RuntimeException("贴入银行行号不能为空!");
						}
					}
				}
				//贴入银行名称
				if (StringUtils.isEmpty(abBillDiscountApplyDTO.getRecNm())) {
					throw new RuntimeException("贴入银行名称不能为空!");
				}

				//根据单位id,票据号获取可贴现的票据信息
				Map<String, Object> params = new HashMap<>();
				params.put("origNo", abBillDiscountApplyDTO.getOrigNo());
				params.put("sbbllRngEndSn", abBillDiscountApplyDTO.getSbbllRngStrtSn());
				params.put("sbbllRngEndSn", abBillDiscountApplyDTO.getSbbllRngEndSn());
				params.put("corpId", abBillDiscountApplyDTO.getCorpId());
				AbBillDiscountApplyVO billInfo = abBillDiscountApplyService.getAbBillDiscountApplyVOByParams(params, null);
				if (billInfo != null) {
					// 判断业务办理日期
					if (abBillDiscountApplyDTO.getBusinessDate() == null) {
						throw new RuntimeException("业务办理日期不能为空!");
					}
					BeanUtils.copyNotNullProperties(abBillDiscountApplyDTO, billInfo);
					billInfo.setAccIds(billInfo.getAccId());
					abBillDiscountApplyService.batchSubmitAbBillDiscountApplyERP(billInfo, null);
				} else {
					throw new RuntimeException("根据单位和票据号码查无可贴现的票据信息!");
				}
			}
		} else {
			//传递的参数值不能为空
			throw new RuntimeException("传递的参数值不能为空!");
		}
	}
	/**
	 * ERP应收票据批量托收申请
	 *
	 * @param abBillCollectionApplyDTOList
	 */
	@Override
	public void collectionApplyERP(List<AbBillCollectionApplyDTO> abBillCollectionApplyDTOList) {
		if (abBillCollectionApplyDTOList != null && !abBillCollectionApplyDTOList.isEmpty()) {
			for (AbBillCollectionApplyDTO abBillCollectionApplyDTO : abBillCollectionApplyDTOList) {
				if (abBillCollectionApplyDTO == null) {
					//传递的参数值不能为空
					throw new RuntimeException("传递的参数值不能为空!");
				}
				//所属单位(ID)
				if (StringUtils.isEmpty(abBillCollectionApplyDTO.getCorpId())) {
					throw new RuntimeException("所属单位不能为空!");
				}
				// 校验单位
				if (!EmptyUtils.isBlank(abBillCollectionApplyDTO.getCorpId())) {
					CorpDTO corp = abCorpService.get(abBillCollectionApplyDTO.getCorpId());
					if (EmptyUtils.isEmpty(corp)) {
						throw new RuntimeException(String.format("单位Id为[%s],不存在!", abBillCollectionApplyDTO.getCorpId()));
					}
				}
				//票据号(origNO)
				if (StringUtils.isEmpty(abBillCollectionApplyDTO.getOrigNo())) {
					throw new RuntimeException("票据号码不能为空!");
				}
				//校验票据号是否存在
				AbVoucherinfoVO reInfo = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abBillCollectionApplyDTO.getOrigNo(),null,null);
				if (reInfo == null) {
					throw new RuntimeException(String.format("票据号码为[%s],不存在该票据!", abBillCollectionApplyDTO.getOrigNo()));
				}
				//清算模式
				if (StringUtils.isEmpty(abBillCollectionApplyDTO.getSttlmmk())) {
					throw new RuntimeException("清算模式不能为空!");
				} else {
					// 验证清算模式值正不正确
					if (!AccebillConstant.AB_STTLMMK_Y.equals(abBillCollectionApplyDTO.getSttlmmk())
							&& !AccebillConstant.AB_STTLMMK_N.equals(abBillCollectionApplyDTO.getSttlmmk())) {
						throw new RuntimeException("清算模式值错误!");
					}
				}
				//根据票据主信息ID和单位ID获取从表数据
				AbVoucherinfoVice reviceInfo = abVoucherinfoViceService.getVoucherinfoViceByVidAndCorpId(reInfo.getId(), abBillCollectionApplyDTO.getCorpId(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, null);
				if (reviceInfo == null) {
					throw new RuntimeException("根据单位和票据号码查无票据信息!");
				}
				//根据票据号和单位id获取可托收的票据信息
				List<AbBillCollectionApplyVo> collectionApplyList = abBillcollectionApplyService.getAbBillCollectionApplyVoDTOList(abBillCollectionApplyDTO.getCorpId(), abBillCollectionApplyDTO.getOrigNo());
				if (collectionApplyList != null && !collectionApplyList.isEmpty()) {
					AbBillCollectionApplyVo abBillCollectionApplyVo = collectionApplyList.get(0);
					if ("1".equals(abBillCollectionApplyVo.getIsOverdue()) && StringUtils.isEmpty(abBillCollectionApplyDTO.getIsOverdueRmk())) {
						throw new RuntimeException("提交票据中包含逾期票据，请填写逾期托收说明!");
					}
					BeanUtils.copyNotNullProperties(abBillCollectionApplyDTO, abBillCollectionApplyVo);
					abBillCollectionApplyVo.setAids(abBillCollectionApplyVo.getAid());
					abBillcollectionApplyService.collectionApplyERP(abBillCollectionApplyVo, null);
				} else {
					throw new RuntimeException("根据单位和票据号码查无可托收的票据信息!");
				}
			}
		} else {
			//传递的参数值不能为空
			throw new RuntimeException("传递的参数值不能为空!");
		}
	}
/**
 * 获取票据到期可以发起托收申请或逾期托收申请的票据
 */
	/*@Override
	public List<AbBillCollectionApplyVoDTO> getAbBillCollectionApplyVoDTOList() {
		return abBillCollectionApplyService.getAbBillCollectionApplyVoDTOList();
	}*/
	/**
	 * 发起托收申请接口
	 //* @param aid 票据流水id
	 */
	/*@Override
	public void saveAbBillCollectionApply(String aid) {
		abBillCollectionApplyService.saveAbBillCollectionApply(aid);
	}*/
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public DataTablesOutput getBillAccbookDataTables(DataTablesInput input, String accbookType,String accbookStatus,ICurrentUser user) {
		ConstantBean<Integer> busOriType= FinacialManualConstant.MANUALTYPE_BUSORITYPE_MAP.get(accbookType);
		if(busOriType==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_BUSORITYPE_ISNULL);
		}
		input.getFormdata().put("eqi_busOriType", busOriType.getValue());
		if(AccebillConstant.BILLACCBOOK_STATUS_UPDATE.equals(accbookStatus)){
			String[] voucherStates={FinaBookConstant.VoucherStatus.STATUS_BACK.getValue(),FinaBookConstant.VoucherStatus.STATUS_TEMP.getValue()};
			input.getFormdata().put("ins_voucherState", StringUtils.join(voucherStates, ","));
//			input.getFormdata().put("ins_voucherState", voucherStates);
		}else if(AccebillConstant.BILLACCBOOK_STATUS_CHECK.equals(accbookStatus)){
			String[] voucherStates={FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue()};
			input.getFormdata().put("ins_voucherState", StringUtils.join(voucherStates, ","));
//			input.getFormdata().put("ins_voucherState", voucherStates);
		}
		return abAccbookService.getBillAccbookDataTables(input, user);
	}

	@Override
	public void billAccbookOp(String buId, String accbookType, String opType,String busGlobalId, String opMsg,ICurrentUser userVo) {
		if(StringUtils.isEmpty(buId)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_AID_ISNULL);
		}
		AbAccrecords info=abAccrecordsService.get(buId,true);
		AbAccbookInfo bookInfo=abAccbookService.getAbAccbookInfoByAid(info.getId());
		if(info==null||bookInfo==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_AID_ISNULL);
		}
		IAccebillAccbookService accbookService=AbAccebillUtils.getAbAccebillAccbookService(info.getBriefId());
		if(accbookService==null&&!"0".equals(bookInfo.getIsBack())&&!AccebillConstant.BILLACCBOOK_OPTYPE_BACK.equals(opType)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_SERVICE_ISNULL);
		}
		if(AccebillConstant.BILLACCBOOK_OPTYPE_REPULSE.equals(opType)){//记账打回
			if("0".equals(bookInfo.getIsBack())){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_ISNOTBACK);
			}
			busGlobalId="null";
			if(StringUtils.isEmpty(opMsg)){
				opMsg=AccebillConstant.LANG_LOG_ACCBOOK_BACK;
			}
			accbookService.busRepulseOp(info, AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_REPULSE, opMsg, userVo);
		}else if(AccebillConstant.BILLACCBOOK_OPTYPE_OK.equals(opType)&&!"0".equals(bookInfo.getIsBack())){//记账通过
			busGlobalId=null;
			if(StringUtils.isEmpty(opMsg)){
				opMsg=AccebillConstant.LANG_LOG_ACCBOOK_SUCC;
			}
			accbookService.busOverOp(info, AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_APP, opMsg, userVo);
		}else if(AccebillConstant.BILLACCBOOK_OPTYPE_CHECK.equals(opType)&&StringUtils.isEmpty(busGlobalId)){//暂存或者复核中
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_BUSGLOBALID_ISNULL);
		}else if(AccebillConstant.BILLACCBOOK_OPTYPE_BACK.equals(opType)){//改账打回
			busGlobalId="null";
		}
		if(StringUtils.isNotEmpty(busGlobalId)){
			int size=abAccrecordsService.setBusGlobalIdById(buId,busGlobalId,userVo);
			if(size!=1){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_ACCBOOK_MESSAGE_BUSGLOBALID_ERROR);
			}
		}
//			设置操作人
		if (AccebillConstant.BILLACCBOOK_OPTYPE_CHECK.equals(opType)) {
			bookInfo.setOperatorCode(userVo.getId());
			abAccbookService.updateEntity(bookInfo);
		}
	}
	@Override
	public AbBillAccbookInfoDTO getAbVoucherinfoVOById(String id) {
		return abAccbookService.getAbBillInfoById(id);
	}
	@Override
	public AbDiscountDTO getAbDiscountByRecordId(String recordId){
		AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(recordId);
		AbDiscountDTO dto = new AbDiscountDTO();
		if(abDiscount == null){
			return null;
		}
		BeanUtils.copyNotNullProperties(abDiscount, dto);
		return dto;
	}
	@Override
	public AbAccrecordsDTO getAbAccrebyLastid(String id) {
		AbAccrecords abAccrecords = abAccrecordsService.getAbAccrebyLastid(id);
		AbAccrecordsDTO dto = new AbAccrecordsDTO();
		if(abAccrecords != null){
			BeanUtils.copyNotNullProperties(abAccrecords, dto);
		}
		return dto;
	}
	@Override
	public String getFcAccCorpId(String netId, String corpId, String otherCorpId, String sysRaceCode, FcVoucherEntryDefineDTO voucherDtl){
		// todo:数据转换
		com.bytter.biz.finacial.api.dto.FcVoucherEntryDefineDTO fcVoucherEntryDefineDTO = BeanUtils.copyNewInstanceProperties(voucherDtl, com.bytter.biz.finacial.api.dto.FcVoucherEntryDefineDTO.class);
		return abAccbookService.getFcAccCorpId(netId,corpId,otherCorpId,sysRaceCode,fcVoucherEntryDefineDTO);
	}
	/**
	 * 根据流水IDS查看票据信息
	 *
	 * @param
	 * @return
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public List<AbVoucherinfoViceDTO> listAccebillInfoByIds(String aids,String payId){
		AbVoucherinfoViceVO viceVo=new AbVoucherinfoViceVO();
		if(StrUtil.isEmpty(payId)){
			String[] arr = aids.trim().split("&");
			if(aids.contains("&")){
				viceVo.setPayId(arr[1]);
			}
			viceVo.setId(arr[0]);
		}else{
			viceVo.setId(aids);
			viceVo.setPayId(payId);
		}
		List<AbVoucherinfoViceVO> list=abVoucherinfoViceService.listAccebillInfoView(viceVo);
		//查询票据池
		List<AbVoucherinfoViceVO> list2=abVoucherinfoViceService.listAccebillInfoViewByPoolId(viceVo);
		if (!CollectionUtils.isEmpty(list2)) {
			list.addAll(list2);
		}
		List<AbVoucherinfoViceVO> relist=new ArrayList<>();
		String[] ids=StringUtils.split(viceVo.getId(),",");
		for(String id:ids){
			if(StrUtil.isNotEmpty(id)){
				list.forEach(dtl->{
					if(id.equals(dtl.getId())){
						relist.add(dtl);
					}
				});
			}
		}
		return BeanUtils.copyListProperties(relist, AbVoucherinfoViceDTO.class);
	}
	/**
	 * 修改从表信息为不挂接担保合同
	 * @param id 票据从表id
	 * @param isBearModel 是否挂接担保合同
	 * @param user
	 * @return
	 */
	@Override
	public void updateGuarantee(String id, String isBearModel, ICurrentUser user) {
		abVoucherinfoViceService.updateGuarantee(id, isBearModel, user);
	}
	/**
	 * 获取挂接授信的票据主表ID集合
	 * @return
	 */
	@Override
	public List<String> getBillIdListByRat() {
		return abVoucherinfoViceService.getBillIdListByRat();
	}
	@Override
	public void backBillBrief(String aids, ICurrentUser userVo) {
		if(StringUtils.isNotEmpty(aids)){
			abAccrecordsService.backBillBrief(aids, userVo);
		}
	}
	@Override
	public void completeAccrecord(String aids, ICurrentUser userVo) {
		if(StringUtils.isNotEmpty(aids)){
			abAccrecordsService.completeAccrecord(aids, userVo);
		}
	}
	/**
	 * 根据单位id查询票据待记账数据
	 * @param corpId 单位id
	 * @return true 有待记账数据  false 无待记账数据
	 */
	@Override
	public boolean checkToBeBookByCorpId(String corpId){
		return abAccbookService.checkToBeBookByCorpId(corpId);
	}
	/**
	 * 指定日期到期金额
	 * @param userId 用户ID(必填)
	 * @param resource 资源(必填)
	 * @param date 日期 (选填)
	 * @return
	 */
	@Override
	public BigDecimal appointeDayMoney(String userId, String resource, Date date){
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(resource)){
			return new BigDecimal(0.00);
		}
		if(null==date){
			date = new Date();
		}
		return abBillBalCompareQueryService.appointeDayMoney(userId,resource,date);
	}
	/**
	 * 根据流水查询票据信息
	 * 用于收款流水认领
	 * @param aid 流水Id(必填)
	 * @return
	 */
	@Override
	public AbEmitBillDTO selectBillInfo(String aid) {
		return abAccrecordsService.selectBillInfo(aid);
	}
	/**待记账查询*/
	@Override
	public List<AbBillAccbookInfoDTO> queryTallyAbAccbook(String accbookType){
		ConstantBean<Integer> busOriType=FinacialManualConstant.MANUALTYPE_BUSORITYPE_MAP.get(accbookType);
		return abAccbookService.queryTallyAbAccbook(busOriType.getValue());
	}
	@Override
	public AbAccrecordsDTO abAccBookInfo(String aid,String briefId) {
		AbVoucherinfoVO billInfo = abReceiveBillService.getAbBillInfo(aid, briefId);
		AbAccrecordsDTO refVo = new AbAccrecordsDTO();
		if(null!=billInfo){
			BeanUtils.copyNotNullProperties(billInfo,refVo);
		}
		return refVo;
	}
	/**
	 * 电票签收接口
	 * origNo 票据号码
	 * signmsgType  签收类型（002承兑签收、003收票签收、010背书签收、012回购式贴现赎回签收、
	 *              018质押签收、019质押解除签收、020提示付款签收、021逾期提示付款签收）
	 * signUpFlag 签收标识 SU00同意签收、SU01拒收
	 * bankAcc签收人账号
	 * dshnrcd 拒付理由
	 * 			DC00与自己有直接债权债务关系的持票人未履行约定义务；
	 * 			DC01持票人以欺诈、偷盗或者胁迫等手段取得票据；
	 * 			DC02持票人明知有欺诈、偷盗或者胁迫等情形，出于恶意取得票据；
	 * 			DC03持票人明知债务人与出票人或者持票人的前手之间存在抗辩事由而取得票据；
	 * 			DC04持票人因重大过失取得不符合《票据法》规定的票据；
	 * 			DC05超过提示付款期；
	 * 			DC06被法院冻结或收到法院止付通知书；
	 * 			DC07票据未到期；
	 * 			DC08商业承兑汇票承兑人账户余额不足。
	 * 			DC09其他（必须注明）
	 * dshnrrsn 拒付理由备注
	 */
	@Override
	public void billSignOp(AbReceivebizDTO abReceivebiz) {
//		//票据号
//		if (StringUtils.isEmpty(abReceivebiz.getOrigNo())) {
//			throw new RuntimeException("OrigNo票据号码不能为空!");
//		}
		//签收表
		if (StringUtils.isEmpty(abReceivebiz.getBillRecId())) {
			throw new RuntimeException("签收表ID不能为空!");
		}
		/*
		 * 签收类型（002承兑签收、003收票签收、010背书签收、012回购式贴现赎回签收、
		 * 018质押签收、019质押解除签收、020提示付款签收、021逾期提示付款签收）
		 * 签收状态（SU00-同意  SU01-拒绝）
		 * 必填的业务类型：签收申请
		 */
		if (StringUtils.isEmpty(abReceivebiz.getSignmsgType())) {
			throw new RuntimeException("SignmsgType签收类型必填!");
		} else if (!(AccebillConstant.BRIEF_STATUS_002.equals(abReceivebiz.getSignmsgType()) || AccebillConstant.BRIEF_STATUS_003.equals(abReceivebiz.getSignmsgType())
				|| AccebillConstant.BRIEF_STATUS_010.equals(abReceivebiz.getSignmsgType()) || AccebillConstant.BRIEF_STATUS_012.equals(abReceivebiz.getSignmsgType())
				|| AccebillConstant.BRIEF_STATUS_018.equals(abReceivebiz.getSignmsgType()) || AccebillConstant.BRIEF_STATUS_019.equals(abReceivebiz.getSignmsgType())
				|| AccebillConstant.BRIEF_STATUS_020.equals(abReceivebiz.getSignmsgType()) || AccebillConstant.BRIEF_STATUS_021.equals(abReceivebiz.getSignmsgType()))) {
			throw new RuntimeException("SignmsgType签收类型值错误!");
		}
		//签收状态（SU00-同意  SU01-拒绝）
		if (StringUtils.isEmpty(abReceivebiz.getSignUpFlag())) {
			throw new RuntimeException("SignUpFlag签收状态必填!");
		} else if (!(AccebillConstant.AB_SGNUPMK_Y.equals(abReceivebiz.getSignUpFlag()) || AccebillConstant.AB_SGNUPMK_N.equals(abReceivebiz.getSignUpFlag()))) {
			throw new RuntimeException("SignUpFlag签收状态值错误!");
		}
		//签收人账号
		if (StringUtils.isEmpty(abReceivebiz.getBankAcc())){
			throw new RuntimeException("BankAcc签收人账号必填!");
		}
		//签收类型为020提示付款签收/021逾期提示付款 SU01-拒绝签收时必填
		if ((AccebillConstant.BRIEF_STATUS_020.equals(abReceivebiz.getSignmsgType()) || AccebillConstant.BRIEF_STATUS_021.equals(abReceivebiz.getSignmsgType()))
				&& AccebillConstant.AB_SGNUPMK_N.equals(abReceivebiz.getSignUpFlag())) {
			if (StringUtils.isEmpty(abReceivebiz.getDshnrcd())) {
				throw new RuntimeException("拒付理由代码必填!");
				//还有新一的票据的拒签理由代码 此次不做强控
//			} else if (!("DC01".equals(abReceivebiz.getDshnrcd()) || "DC02".equals(abReceivebiz.getDshnrcd())
//					|| "DC03".equals(abReceivebiz.getDshnrcd()) || "DC04".equals(abReceivebiz.getDshnrcd())
//					|| "DC05".equals(abReceivebiz.getDshnrcd()) || "DC06".equals(abReceivebiz.getDshnrcd())
//					|| "DC07".equals(abReceivebiz.getDshnrcd()) || "DC08".equals(abReceivebiz.getDshnrcd())
//					|| "DC09".equals(abReceivebiz.getDshnrcd()))) {
//				throw new RuntimeException("Dshnrcd拒付理由代码值错误!");
			} else if (("DC09".equals(abReceivebiz.getDshnrcd())||"CP06".equals(abReceivebiz.getDshnrcd())) && StringUtils.isEmpty(abReceivebiz.getDshnrrsn())) {
				throw new RuntimeException("拒付理由代码为CP06或DC09其他时Dshnrrsn拒付理由备注必填!");
			}
		}
//		//根据签收表ID,账号,签收类型查询待签收数据
//		List<AbBillReceivebiz> receivebizs = abBillReceivebizService.queryReceiveBizByParames(abReceivebiz.getOrigNo(), abReceivebiz.getSignmsgType(), abReceivebiz.getBankAcc());
		List<AbBillReceivebiz> receivebizs = abBillReceivebizService.queryReceiveBizByParames(abReceivebiz.getBillRecId(), abReceivebiz.getSignmsgType(), abReceivebiz.getBankAcc());
		if (receivebizs == null || receivebizs.isEmpty()) {
			throw new RuntimeException("没有待签收数据可以操作!");
		}else{
			AbBillReceivebizVO  receivebizVO =  new AbBillReceivebizVO();
			receivebizVO.setId(receivebizs.get(0).getId());
			receivebizVO.setOrigNo(abReceivebiz.getOrigNo());
			receivebizVO.setMsgtype(abReceivebiz.getSignmsgType());
			receivebizVO.setSgnupmk(abReceivebiz.getSignUpFlag());
			receivebizVO.setDshnrcd(abReceivebiz.getDshnrcd());
			receivebizVO.setDshnrrsn(abReceivebiz.getDshnrrsn());
			//进行票据签收操作
			abBillReceivebizService.billSignOpErp(receivebizVO, null);
		}
	}
	@Override
	public void saveBillPayMiddle(AbBillPayMiddleDTO abBillPayMiddleDTO) {
		abBillPayMiddleService.saveEntity(BeanUtils.copyNewInstanceProperties(abBillPayMiddleDTO, AbBillPayMiddle.class));
	}
	@Override
	public List<AbBillPayMiddleDTO> getAbBillPayMiddleList(String vvid, String payId) {
		return abBillPayMiddleService.getAbBillPayMiddleList(vvid, payId);
	}
	@Override
	public void updateAbBillPayMiddleStatus(String payId, Integer status) {
		abBillPayMiddleService.updateAbBillPayMiddleStatus(payId, status);
	}
	@Override
	public List<AbBillPayMiddleDTO> getAbBillPayMiddleListByPayId(String payId) {
		return abBillPayMiddleService.getAbBillPayMiddleListByPayId(payId);
	}
	@Override
	public void updateAbBillPayMiddleBalance(String id, double balance) {
		abBillPayMiddleService.updateAbBillPayMiddleBalance(id, balance);
	}
	@Override
	public void deleteAbBillPayMiddleByPayId(String payId) {
		abBillPayMiddleService.deleteAbBillPayMiddleByPayId(payId);
	}
	@Override
	public void delBillPayMid(String billId, String payId) {
		abBillPayMiddleService.delBillPayMid(billId, payId);
	}
	@Override
	public List<AbAccrecordsDTO> getAccrecordsById(String id) {
		List<AbAccrecords> accrecordsList = abAccrecordsService.getAccrecordsInfoByIds(id.split(","));
		if (!CollectionUtils.isEmpty(accrecordsList)) {
			ArrayList<AbAccrecordsDTO> abAccrecordsDTOS = new ArrayList<>();
			for (AbAccrecords abAccrecords : accrecordsList) {
				abAccrecordsDTOS.add(BeanUtils.copyNewInstanceProperties(abAccrecords, AbAccrecordsDTO.class));
			}
			return abAccrecordsDTOS;
		}
		return null;
	}
	/**
	 * 新增中间表数据
	 * @param buId 业务id
	 * @param middleType 中间表类型
	 */
	@Override
	public void saveBillMiddle(String buId,String middleType,ICurrentUser userVo){
		abBillMiddleService.saveBillMiddle(buId,middleType,userVo);
	}
	/**
	 * 应收票据并且未付款时，查询挂接的所有票据是否存在为新系统出票并且分包
	 *
	 * @param vids 票据主表主键id字符串
	 * @return 1：是，0：否：
	 */
	@Override
	public String getNewBillAndSplitTicketFlag(String vids) {
		String[] vidAr = vids.split(CommonConstant.COMMA_SPLIT);
		for (String vid : vidAr) {
			AbVoucherinfo entity = abVoucherinfoService.get(vid, true);
			if (!ObjectUtils.isEmpty(entity) && "1".equals(entity.getNewStmIssuofpprInd()) && "1".equals(entity.getBlgrdbagcrclPrmtInd())) {
				return CommonConstant.YES;
			}
		}
		return CommonConstant.NO;
	}

	/**
	 * 定时任务查询待生成需求单的承兑到期
	 * @return
	 */
	@Override
	public List<AbAccrecordsDTO> listTaskBillForNisDemand() {
		return abAccrecordsService.listTaskBillForNisDemand();
	}

	public int batchPayLockMoneyAdd(List<NisBillDTO> nisBillDtoList) {
		if(CollectionUtil.isEmpty(nisBillDtoList)){
			return 0;
		}
		int size=0;
		for(NisBillDTO nisBillDTO:nisBillDtoList) {
			size += payLockMoneyAdd(nisBillDTO.getBillId(),nisBillDTO.getBalance());
		}
		if(nisBillDtoList.size()!=size){
			throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		return size;
	}

	@Override
	public int batchPayLockMoneySubtract(List<NisBillDTO> nisBillDtoList) {
		if(CollectionUtil.isEmpty(nisBillDtoList)){
			return 0;
		}
		int size=0;
		for(NisBillDTO nisBillDTO:nisBillDtoList) {
			size += payLockMoneySubtract(nisBillDTO.getBillId(),nisBillDTO.getBalance());
		}
		return size;
	}

	public int payLockMoneyAdd(String aid, BigDecimal addMoney) {
		if(StrUtil.isEmpty(aid)){
			return 0;
		}else if(addMoney==null){
			return 1;
		}
		Map<String,Object> params = new HashMap<>();
		params.put("aid", aid);
		params.put("addMoney", addMoney);
		return abVoucherinfoViceService.updatePayLockMoney(params);
	}

	public int payLockMoneySubtract(String aid, BigDecimal subtractMoney) {
		if(StrUtil.isEmpty(aid)){
			return 0;
		} if(subtractMoney==null){
			return 1;
		}
		Map<String,Object> params = new HashMap<>();
		params.put("aid", aid);
		params.put("subtractMoney", subtractMoney);
		return abVoucherinfoViceService.updatePayLockMoney(params);
	}

	public void updateBillAccByid(AbAccrecordsDTO dto, String ids,ICurrentUser user){
		if(StringUtils.isBlank(ids) || dto == null){
			return;
		}
		//批次号不为空，只更新票据从表批次号
		if(StringUtils.isNotBlank(dto.getBatchNo())){
			AbAccrecords abAccrecords = abAccrecordsService.get(ids);
			AbVoucherinfoVODTO voucherinfoVODTO = new AbVoucherinfoVODTO();
			voucherinfoVODTO.setId(abAccrecords.getVvid());
			voucherinfoVODTO.setBatchNo(dto.getBatchNo());
			abVoucherinfoViceService.updateFieldById(voucherinfoVODTO);
			//设置批次开票明细顺序号
			if(StringUtil.isNotEmpty(dto.getBatchDtlNo())){
				abAccrecords.setBatchDtlNo(dto.getBatchDtlNo());
			}
			abAccrecordsService.updateEntity(abAccrecords);
			return;
		}
		AbAccrecordsVO vo = new AbAccrecordsVO();
		BeanUtils.copyNotNullProperties(dto,vo);
		vo.setIdArray(ids.split(","));
		abAccrecordsService.batchUpdate(vo,null);
	}

	public void deleteOpenBillInfo(String aid,String reason, ICurrentUser userVo){
		abEmitBillService.delete(aid,reason,userVo);
	}
	public AbEmitBillVoDTO getAbEmitBillVoByAid(String aid){
		AbEmitBillVo abEmitBillVoByAid = abEmitBillService.getAbEmitBillVoByAid(aid, false);
		AbEmitBillVoDTO voDTO = new AbEmitBillVoDTO();
		BeanUtils.copyNotNullProperties(abEmitBillVoByAid,voDTO);
		return voDTO;
	}
	public Map dealBillInfo(String aid){
		return abEmitBillService.dealBillInfo(aid,null,null);
	}

	public List<AbBillBankAutoDtlDTO> listAbBillBankAutoDtlByParams(Map<String,Object> params){
		List<AbBillBankAutoDtl> abBillBankAutoDtls = abBillBankAutoDtlDao.listAbBillBankAutoDtlByParams(params);
		if(CollectionUtil.isEmpty(abBillBankAutoDtls)){
			return null;
		}
		List<AbBillBankAutoDtlDTO> dtos = new ArrayList<>();
		for(AbBillBankAutoDtl dtl : abBillBankAutoDtls){
			AbBillBankAutoDtlDTO dto = new AbBillBankAutoDtlDTO();
			BeanUtils.copyNotNullProperties(dtl,dto);
			dtos.add(dto);
		}
		return dtos;
	}
}
