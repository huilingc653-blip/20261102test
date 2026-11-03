package com.bytter.biz.bdlinks.service.impl;


import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.entity.BisExcMidAcc;
import com.bytter.biz.bdlinks.service.BisExcMidAccService;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.service.BisExcStatusChangeHandler;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifRuleService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BisBifRuleDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
/**
 * @Title: Service
 * @Description: 付款指令
 * @author huangx
 * @date 2017-08-23 11:08:59
 * @version 1.0
 *
 */
@Slf4j
@Service("bisExcServiceImpl")
@Transactional
@DubboService
public class BisExcServiceImpl implements IBisExcService {
	@Autowired
	private BisExcService bisExcService;
	@DubboReference(check = false)
	private INisMessageService nisMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@Autowired
	private BisExcMidAccService bisExcMidAccService;
	@DubboReference(check = false)
	private IBisBifRuleService bisBifRuleService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IConfigService configService;
//	@Autowired
//	private BisMQService bisMQService;

	@Autowired
	private BisExcStatusChangeHandler bisExcStatusChangeHandler;

	public List<BisExcDTO> listBisExc(BisExcDTO dto){
		List<BisExcVO> bisExcVOS = bisExcService.listBisExc(dto);
		if(CollectionUtils.isEmpty(bisExcVOS)){
			return null;
		}
		List<BisExcDTO> bisExcDTOS = BeanUtils.copyListProperties(bisExcVOS, BisExcDTO.class);
		return bisExcDTOS;
	}

	/**
	 * 新增付款指令
	 */
	@Override
	public void addBisExc(BisExcDTO bisExc, ICurrentUser user) {
		this.updateOldBisExc(bisExc.getCbsBillId(), user);
		BisExc entity = new BisExc();
		BeanUtils.copyNotNullProperties(bisExc, entity);
		if (StringUtils.isBlank(entity.getCmdType())) {
			entity.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
		}
		entity.setStatus(NetbankConstant.STATUE_CHECK);
		entity.setVoucherStat(BdlinkConstant.WAIT_FILL);
		entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));  //手动的地方又直接 生成?
		//接口代码业务规则
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
		if(!ObjectUtils.isEmpty(bisBifRule)) 
		{
			if(CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())){
				if(StringUtils.isNotBlank(entity.getAddWord())){
					entity.setAddWord(entity.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}else {
					entity.setAddWord(BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}
			}
		}
		entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
		//加密信息
		entity.setVerifyCode(bisExcService.setVerifyCode(entity));
		//区分对私付款：0-付款办理 1-费用报销
		if(BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())
				&& BdlinkConstant.TRANS_MODE_PRIVATE.equals(entity.getTransMode())
				&& StringUtils.isBlank(entity.getVoucherFromPrivate())){
			NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(entity.getCbsBillId());
			entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_0);
			if (nisBillheadDTO != null && NetbankConstant.BILLFROM_2.equals(nisBillheadDTO.getBillFrom())) {
				//费用报销
				entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_1);
			}
		}
		//跨行转付在下拨指令成功后延迟支付时间
		if (BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())) {
			String delayTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DELAYTIME).getValue();
			if (StringUtils.isNotBlank(delayTime)) {
				entity.setDelayTime(new Integer(delayTime));
			}
		}
		//指令编号重复校验
		if(bisExcService.getByVoucherNo(entity.getVoucherNo()) != null){
			throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_VOUCHERNO_REPAY, entity.getVoucherNo());
		}else {
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
			bisExcService.save(entity);
			//获取系统结算方式
			FcVoucherTypeDTO voucherType = fcVoucherTypeService.getFcVoucherTypeById(entity.getVoucherTypeId());
			//代付/代理支付保存中心账号
			if ((NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode()) || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode()))
					&& NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
				BisExcMidAcc bisExcMidAcc = new BisExcMidAcc();
				bisExcMidAcc.setBisExcId(entity.getId());
				bisExcMidAcc.setMidPayerAccName(bisExc.getMidPayerAccName());
				bisExcMidAcc.setMidPayerAccNo(bisExc.getMidPayerAccNo());
				bisExcMidAcc.setMidPayerBankName(bisExc.getMidPayerBankName());
				bisExcMidAccService.saveOrUpdate(bisExcMidAcc);
			}
//			bisMQService.sendBisExcChangeMsg(entity, entity.getVoucherStat());
			bisExcStatusChangeHandler.processBisExcChangeMsg(entity.getVoucherFrom(), entity.getCbsBillId(), entity.getVoucherStat());
			//日志记录
			nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_PAY, NisMessageConstant.NIS_LOG_AUTO_PAY, NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
			//消息提醒
			notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.NETBANK.BISEXC_SEND, entity);
		}
	}

	/**
	 * 新增付款指令
	 */
	@Override
	public BisExcDTO addBisExcReturn(BisExcDTO bisExc, ICurrentUser user) {
		BisExcDTO result = new BisExcDTO();
		this.updateOldBisExc(bisExc.getCbsBillId(), user);
		BisExc entity = new BisExc();
		BeanUtils.copyNotNullProperties(bisExc, entity);
		if (StringUtils.isBlank(entity.getCmdType())) {
			entity.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
		}
		entity.setStatus(NetbankConstant.STATUE_CHECK);
		if(EmptyUtils.isEmpty(entity.getVoucherStat())) {
			entity.setVoucherStat(BdlinkConstant.WAIT_FILL);
		}
		entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
		//接口代码业务规则
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
		if(!ObjectUtils.isEmpty(bisBifRule)) 
		{
			if(CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())){
				if(StringUtils.isNotBlank(entity.getAddWord())){
					entity.setAddWord(entity.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}else {
					entity.setAddWord(BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}
			}
		}
		entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
		//加密信息
		entity.setVerifyCode(bisExcService.setVerifyCode(entity));
		//区分对私付款：0-付款办理 1-费用报销
		if(BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())
				&& BdlinkConstant.TRANS_MODE_PRIVATE.equals(entity.getTransMode())
				&& StringUtils.isBlank(entity.getVoucherFromPrivate())){
			NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(entity.getCbsBillId());
			entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_0);
			if (nisBillheadDTO != null && NetbankConstant.BILLFROM_2.equals(nisBillheadDTO.getBillFrom())) {
				//费用报销
				entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_1);
			}
		}
		//跨行转付在下拨指令成功后延迟支付时间
		if (BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())) {
			String delayTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DELAYTIME).getValue();
			if (StringUtils.isNotBlank(delayTime)) {
				entity.setDelayTime(new Integer(delayTime));
			}
		}
		//指令编号重复校验
		if(bisExcService.getByVoucherNo(entity.getVoucherNo()) != null){
			throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_VOUCHERNO_REPAY, entity.getVoucherNo());
		}else {
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
			bisExcService.save(entity);
			//获取系统结算方式
			FcVoucherTypeDTO voucherType = fcVoucherTypeService.getFcVoucherTypeById(entity.getVoucherTypeId());
			//代付/代理支付保存中心账号
			if ((NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode()) || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode()))
					&& NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
				BisExcMidAcc bisExcMidAcc = new BisExcMidAcc();
				bisExcMidAcc.setBisExcId(entity.getId());
				bisExcMidAcc.setMidPayerAccName(bisExc.getMidPayerAccName());
				bisExcMidAcc.setMidPayerAccNo(bisExc.getMidPayerAccNo());
				bisExcMidAcc.setMidPayerBankName(bisExc.getMidPayerBankName());
				bisExcMidAccService.saveOrUpdate(bisExcMidAcc);
			}
//			bisMQService.sendBisExcChangeMsg(entity, entity.getVoucherStat());
			bisExcStatusChangeHandler.processBisExcChangeMsg(entity.getVoucherFrom(), entity.getCbsBillId(), entity.getVoucherStat());
			//日志记录
			nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_PAY, NisMessageConstant.NIS_LOG_AUTO_PAY, NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
			//消息提醒
			notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.NETBANK.BISEXC_SEND, entity);
		}
		BeanUtils.copyNotNullProperties(entity,result);
		return result;
	}
	/**
	 * 根据单号更新旧指令
	 *
	 * @param cbsBillId
	 * @param user
	 * @author zuoy 2018-08-07
	 */
	private void updateOldBisExc(String cbsBillId, ICurrentUser user) {
		//根据业务Id获取指令
		BisExc oldExc = bisExcService.getByCbsBillId(cbsBillId);
		if (!EmptyUtils.isEmpty(oldExc)) {
			//指令状态为B-指令退回或者7-指令无效
			if (BdlinkConstant.BACK.equals(oldExc.getVoucherStat()) || BdlinkConstant.INVALIDATE.equals(oldExc.getVoucherStat())) {
				//指令退回后提交，更新原指令状态的业务单号
				int result = bisExcService.updateBisExcCbsBillNo(oldExc);
				if(result<1) {
					throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
				}
			} else {
				throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
			}
		}
	}
	/**
	 * 根据指令单号获取指令信息
	 *
	 * @param voucherNo
	 * @return
	 */
	@Override
	public BisExcDTO getBisExcByVoucherNo(String voucherNo) {
		BisExcVO entity = this.bisExcService.getByVoucherNo(voucherNo);
		BisExcDTO bisExcDTO = new BisExcDTO();
		BeanUtils.copyNotNullProperties(entity, bisExcDTO);
		return bisExcDTO;
	}
	/**
	 * 根据业务单号获取指令信息
	 *
	 * @param cbsBillNo
	 * @param voucherFrom
	 * @return
	 */
	@Override
	public BisExcDTO getBisExcByCbsBillNo(String cbsBillNo, String voucherFrom) {
		BisExcVO entity = this.bisExcService.getByCbsBillNo(cbsBillNo, voucherFrom);
		if (!EmptyUtils.isEmpty(entity)) {
			BisExcDTO bisExcDTO = new BisExcDTO();
			BeanUtils.copyNotNullProperties(entity, bisExcDTO);
			return bisExcDTO;
		}
		return null;
	}

	/**
	 * 根据业务id获取指令信息
	 *
	 * @param cbsBillId
	 * @return
	 */
	@Override
	public BisExcDTO getBisExcBycbsBillId(String cbsBillId) {
		BisExcVO entity = this.bisExcService.getByCbsBillIdUnion(cbsBillId);
		if (!EmptyUtils.isEmpty(entity)) {
			BisExcDTO bisExcDTO = new BisExcDTO();
			BeanUtils.copyNotNullProperties(entity, bisExcDTO);
			return bisExcDTO;
		}
		return null;
	}

	/**
	 * 指令查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@Override
	public DataTablesOutput getBisExcDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
		return this.bisExcService.getDataTablesUnion(dataTablesInput, user);
	}
	/**
	 * 修改付款指令
	 */
	@Override
	public void updateBisExc(BisExcDTO bisExc, ICurrentUser user) {
		BisExc entity = new BisExc();
		BeanUtils.copyNotNullProperties(bisExc, entity);
		//加密信息
		entity.setVerifyCode(bisExcService.setVerifyCode(entity));
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		bisExcService.update(entity);
	}
	/**
	 * 新增付款指令（代发工资）
	 *
	 * @param bisExc
	 */
	@Override
	public String addBisExcForNisWage(BisExcDTO bisExc, ICurrentUser user) {
		log.info("新增付款指令（代发工资）开始");
		BisExc entity = new BisExc();
		BeanUtils.copyNotNullProperties(bisExc, entity);
		entity.setStatus(NetbankConstant.STATUE_CHECK);
		entity.setVoucherStat(BdlinkConstant.PRE_SEND);
		entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
		//接口代码业务规则
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
		if(!ObjectUtils.isEmpty(bisBifRule)) 
		{
			if(CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())){
				if(StringUtils.isNotBlank(entity.getAddWord())){
					entity.setAddWord(entity.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}else {
					entity.setAddWord(BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
				}
			}
		}
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		//加密信息
		entity.setVerifyCode(bisExcService.setVerifyCode(entity));
		log.info("新增付款指令（代发工资），付款单号：【{}】，指令编号【{}】", bisExc.getCbsBillNo(), entity.getVoucherNo());
		bisExcService.save(entity);
		//日志记录
		nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_ADD, "", NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
		//消息提醒
		return entity.getVoucherNo();
	}
	@Override
	public List<BisExcDTO> getBisExcByCbsBillNoForNisWage(String cbsBillNo, String voucherFrom) {
		List<BisExcVO> list = this.bisExcService.getBisExcByCbsBillNoForNisWage(cbsBillNo, voucherFrom);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, BisExcDTO.class);
	}
	@Override
	public void autoSendBisExc(BisExcDTO bisExc, ICurrentUser user) {
		BisExc entity = autoSendBisCommon(bisExc, user); //里面有生成 付款指令编码,存到了 voucherNo 字段
		this.bisExcService.sendBisExc(entity, user);
	}

	@Override
	public void autoSendBisExcNoAccounting(BisExcDTO bisExc, ICurrentUser user) {
		BisExc entity = autoSendBisCommon(bisExc, user); //里面有生成 付款指令编码,存到了 voucherNo 字段
		this.bisExcService.sendBisExcNoAccounting(entity, user);
	}

	private BisExc autoSendBisCommon(BisExcDTO bisExc, ICurrentUser user) {
		this.updateOldBisExc(bisExc.getCbsBillId(), user);
		BisExc entity = new BisExc();
		BeanUtils.copyNotNullProperties(bisExc, entity);
		if (EmptyUtils.isBlank(entity.getVoucherNo())) { //怎么这里又生成一个？ 这里其实生成的是 付款指令编码；而 voucherNo 字段 其实是 记账凭证号
			entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_EXC));
		}
		//接口代码业务规则
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(bisExc.getBifCode());
		if(ObjectUtils.isEmpty(bisBifRule)) {
			bisBifRule = bisBifRuleService.getByIdUnion(bisExc.getBifCode());
		}
		if(ObjectUtils.isEmpty(bisBifRule)) {
			throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BISBIFINIT_NULL);
		}
		if(CommonConstant.YES.equals(bisBifRule.getIsAddBillNo())){
			if(StringUtils.isNotBlank(entity.getAddWord())){
				entity.setAddWord(entity.getAddWord() + BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
			}else {
				entity.setAddWord(BdlinkConstant.ADDWORD_SPLIT + entity.getVoucherNo());
			}
		}
		entity.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
		//加密信息
		entity.setVerifyCode(bisExcService.setVerifyCode(entity));
		//区分对私付款：0-付款办理 1-费用报销
		if(BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())
				&& BdlinkConstant.TRANS_MODE_PRIVATE.equals(entity.getTransMode())
				&& StringUtils.isBlank(entity.getVoucherFromPrivate())){
			NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(entity.getCbsBillId());
			entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_0);
			if (nisBillheadDTO != null && NetbankConstant.BILLFROM_2.equals(nisBillheadDTO.getBillFrom())) {
				//费用报销
				entity.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_1);
			}
		}
		//跨行转付在下拨指令成功后延迟支付时间
		if (BdlinkConstant.FROM_NIS_PAY.equals(entity.getVoucherFrom())) {
			String delayTime = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DELAYTIME).getValue();
			if (StringUtils.isNotBlank(delayTime)) {
				entity.setDelayTime(new Integer(delayTime));
			}
		}
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		return entity;
	}

	/**
	 * 更新付款指令银行流水号（付款明细勾兑返写银行流水号）
	 * @param cbsBillId
	 * @param bankSerialNo
	 */
	@Override
	public void updateBisExcForBankSerialNo(String cbsBillId, String bankSerialNo) {
		BisExc bisExc = bisExcService.getByCbsBillId(cbsBillId);
		if(StringUtils.isNotEmpty(bisExc)){
			bisExc.setBankSerialNo(bankSerialNo);
			bisExcService.saveOrUpdate(bisExc);
		}
	}
	/**
	 * 查询待记账数据
	 * */
	@Override
	public List<BisExcDTO> queryTallyBisEx(){
		return  bisExcService.queryTallyBisEx();
	}
	/**
	 *  退款勾对打回原单 更新指令为无效
	 * @param id
	 * @param user
	 */
	@Override
	public void updateBisExc(String id, ICurrentUser user) {
		BisExc bisExc = bisExcService.get(id,true);
		if(StringUtils.isNotEmpty(bisExc)){
			//指令无效
			bisExc.setVoucherStat(BdlinkConstant.INVALIDATE);
			bisExc.setStatus(BdlinkConstant.STATUE_DEL);
			bisExc.setUpdateBy(user.getLoginName());
			bisExc.setUpdateDate(DateUtils.getDate());
			bisExcService.saveOrUpdate(bisExc);
		}
	}
    @Override
    public List<BisExcDTO> getBisExcByCbsId(List<String> billIds) {
        return bisExcService.getBisExcByCbsId(billIds);
    }

	public List<BisExcDTO> getAllBisExcBankSerialNo() {
		return bisExcService.getAllBisExcBankSerialNo();
	}
    
    @Override
    public List<BisExcDTO> getBisExcByCbsIdsWithCondition(List<String> billIds) {
        return bisExcService.getBisExcByCbsIdsWithCondition(billIds);
    }

	@Override
	public BisExcDTO getByVoucherNo(String voucherNo) {
		BisExcVO vo = bisExcService.getByVoucherNo(voucherNo);
		BisExcDTO bisExcDTO = new BisExcDTO();
		BeanUtils.copyProperties(vo,bisExcDTO);
		return bisExcDTO;
	}

	/**
	 * 根据业务单号ID集合和指令来源，获取指令Map
	 * @param billIds
	 * @param voucherFrom
	 * @return
	 */
	@Override
	public Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom) {
		return bisExcService.getBisExcMapByCbsBillIds(billIds, voucherFrom);
	}
	@Override
	public Map<String, BisExcDTO> getBisExcMapByCbsBillIds(List<String> billIds, String voucherFrom,List<String> voucherStat) {
		return bisExcService.getBisExcMapByCbsBillIds(billIds, voucherFrom,voucherStat);
	}
	/**
	 * 根据业务单号集合和指令来源，获取指令Map
	 * @param billNos
	 * @param voucherFrom
	 * @return
	 */
	@Override
	public Map<String, BisExcDTO> getBisExcMapByCbsBillNos(List<String> billNos, String voucherFrom) {
		return bisExcService.getBisExcMapByCbsBillNos(billNos, voucherFrom);
	}
	
	@Override
	public List<String> getBisExcCbsBillIdsByVoucherstat(){
		return bisExcService.getBisExcCbsBillIdsByVoucherstat();
	}
}
