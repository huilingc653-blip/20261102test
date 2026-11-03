package com.bytter.biz.bdlinks.service;


import com.bytter.biz.bdlinks.entity.BisForeignExc;
import com.bytter.biz.bdlinks.vo.BisForeignExcVO;
import com.bytter.biz.bdlinks.dao.BisForeignExcDao;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.biz.foreign.utils.DistortUtil;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.service.IBisForeignExcBackService;
import com.bytter.biz.payment.api.service.IFexMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.*;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:
 * @date 2017-07-04 15:57:19
 */
@Service("bisForeignExcService")
@Transactional
public class BisForeignExcService extends BaseService<BisForeignExc, String> implements ITaskService {
	@Autowired
	private BisForeignExcDao bisForeignExcDao;

	@Autowired
	private FexMessageService fexMessageService;

	@DubboReference(check = false)
	private INotificationService notificationService;

	@DubboReference(check = false)
	private ICorpService corpService;

	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private IBankTypeService iBankTypeService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
//	@Autowired
//	private BisMQService bisMQService;

	@Autowired
	private BisExcStatusChangeHandler bisExcStatusChangeHandler;

	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BisForeignExc entity) {
		super.deleteEntity(entity);
	}

	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}

	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BisForeignExc entity) {
		super.saveEntity(entity);
	}

	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BisForeignExc entity) {
		super.updateEntity(entity);
	}

	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BisForeignExc entity) {
		super.saveOrUpdate(entity);
	}


	@Override
	public BaseDao<BisForeignExc, String> getEntityDao() {
		return bisForeignExcDao;
	}

	/**
	 * 指令列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource = "";
		String opType = (String) dataTablesInput.getFormdata().get("opType");
		if ("listSend".equals(opType)) {
			//指令发送:p-审批通过 6-等待补充数据
			sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.PASS + "','" + BdlinkConstant.WAIT_FILL + "')");
			resource = ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDQUERY;
		} else if ("listTrace".equals(opType)) {
			//指令追踪 2-银行处理失败  9-接口处理失败 1-等待结果 Y-异常交易 Z-数据被篡改
			resource = ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEQUERY;
			sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.WAIT_RESULT
					+ "','" + BdlinkConstant.EXCEPTION + "','" + BdlinkConstant.DATA_TAMPER + "')");
		} else if ("listConfirm".equals(opType)) {
			//可疑指令发送确认 T-可疑状态
			resource = ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_CONFIRMQUERY;
			sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.SUSPECTED + "')");
		} else {
			resource = ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_QUERY;
		}
		if (!StringUtils.isBlank(resource)) {
			String corpAuthSql = SqlUtil.getInSql(
					corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id",
					true);
			sql.append(" and ").append(corpAuthSql);
		}
		DataTablesOutput dataTablesOutput = bisForeignExcDao.getDataTablesUnion(dataTablesInput, sql.toString());
		List<BisForeignExcVO> bisForeignExcVOList = dataTablesOutput.getData();
		for (BisForeignExcVO bisForeignExcVO : bisForeignExcVOList) {
			BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(bisForeignExcVO.getBifCode());
			if(!EmptyUtils.isEmpty(bisBifInitDTO)){
				bisForeignExcVO.setBifName(bisBifInitDTO.getName());
			}
			CorpDTO corpDTO = corpService.get(bisForeignExcVO.getCorpId());
			if(!EmptyUtils.isEmpty(corpDTO)){
				bisForeignExcVO.setCorpName(corpDTO.getName());
			}
			BankTypeDTO bankTypeDTO = iBankTypeService.getObjectById(bisForeignExcVO.getBankTypeId());
			if(!EmptyUtils.isEmpty(bankTypeDTO)){
				bisForeignExcVO.setBankTypeName(bankTypeDTO.getTypeName());
			}
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(bisForeignExcVO.getCurId());
			if(!EmptyUtils.isEmpty(btCurrencyDTO)){
				bisForeignExcVO.setCurName(btCurrencyDTO.getCurName());
				bisForeignExcVO.setEnglishCode(btCurrencyDTO.getEnglishCode());
			}
        }
		return dataTablesOutput;
	}

	/**
	 * 根据ID获取指令
	 *
	 * @param id
	 * @return
	 */
	public BisForeignExcVO getByIdUnion(String id) {
		BisForeignExcVO bisForeignExcVO = bisForeignExcDao.getByIdUnion(id);
		injectValue(bisForeignExcVO);
		return bisForeignExcVO;
	}

	/**
	 * 根据业务单号查询指令
	 *
	 * @param cbsBillId
	 * @return
	 */
	@Transactional(readOnly = true)
	public BisForeignExc getByCbsBillId(String cbsBillId) {
		return bisForeignExcDao.getUniqueBy("cbsBillId", cbsBillId);
	}

	/**
	 * 根据指令单号查询付款指令
	 *
	 * @param voucherNo
	 * @return
	 */
	public BisForeignExcVO getByVoucherNo(String voucherNo) {
		return bisForeignExcDao.getByVoucherNo(voucherNo);
	}

	/**
	 * 生成加密信息
	 * @param entity
	 * @return
	 */
	public String setVerifyCode(BisForeignExc entity){
		//金额格式转换，不足两位小数，自动补零(制单跳过审批时金额未自动补零，导致生成指令和发送时加密串不一致)
		String payMoney = NumberUtil.formatDoubles(entity.getPayMoney(), 2);
		//加密规则：汇款账号+汇款金额+申请日期+收款人名称+收款账号
		String sourceData = entity.getPayerAcc()+payMoney+DateUtils.formatDate(entity.getReqDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE)+entity.getPayeeAccName()+entity.getPayeeAcc();
		return BytterCypherUtil.encodeToString(sourceData);
	}

	/**
	 * 指令发送
	 *
	 * @param bisForeignExc
	 */
	public void sendBisForeignExc(BisForeignExc bisForeignExc, ICurrentUser user) {
		//数据防篡改校验
		if(!bisForeignExc.getVerifyCode().equals(this.setVerifyCode(bisForeignExc))){
			throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisForeignExc.getVoucherNo());
		}
		int result = 0;
		if(StringUtils.isBlank(bisForeignExc.getId())) {
			//指令自动发送，生成状态为3-准备发送
			bisForeignExc.setVoucherStat(BdlinkConstant.PRE_SEND);
			bisForeignExc.setUpdateBy(user.getLoginName());
			bisForeignExc.setUpdateDate(DateUtils.getDate());
			//签名
			bisForeignExc.setSignatureCode(this.getSignatureCode(bisForeignExc));
			this.saveOrUpdate(bisForeignExc);
			result = 1;
		}else {
			//指令手工发送，更新原指令6-待发送状态为3-准备发送
			bisForeignExc.setReqDate(DateUtils.getDate());
			bisForeignExc.setVerifyCode(this.setVerifyCode(bisForeignExc));
			//签名
			bisForeignExc.setSignatureCode(this.getSignatureCode(bisForeignExc));
			result = this.bisForeignExcDao.updateBisForeignExcVoucherStat(bisForeignExc, BdlinkConstant.PRE_SEND);
		}
		if(result>0) {
			// 发送指令消息
//			bisMQService.sendBisForeignExcChangeMsg(bisForeignExc, BdlinkConstant.PRE_SEND);
			bisExcStatusChangeHandler.processBisForeignExcChangeMsg(bisForeignExc.getCbsBillId(), BdlinkConstant.PRE_SEND);
			//生成日志
			fexMessageService.saveMessage(bisForeignExc.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_SEND, "", FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
			//完成消息通知
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_SEND, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
		} else {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
	}

	/**
	 * 指令退回
	 *
	 * @param bisForeignExc
	 */
	public void backBisForeignExc(BisForeignExc bisForeignExc, String backMsg, ICurrentUser user) {
		//指令退回，更新原指令状态为B-指令退回
		int result = this.bisForeignExcDao.updateBisForeignExcVoucherStat(bisForeignExc, BdlinkConstant.BACK);
		if(result>0) {
			//更新业务单
			String serviceName = ForeignConstant.getServiceNameByVoucherFrom(bisForeignExc.getVoucherFrom());
			if (StringUtils.isNotEmpty(serviceName)) {
				IBisForeignExcBackService bisForeignExcBackService = (IBisForeignExcBackService)this.getBean(serviceName);
				BisForeignExcDTO dto = new BisForeignExcDTO();
				BeanUtils.copyNotNullProperties(bisForeignExc, dto);
				dto.setBackMsg(backMsg);
				bisForeignExcBackService.backBisForeignExc(dto, user);
			}
			// 发送指令消息
//			bisMQService.sendBisForeignExcChangeMsg(bisForeignExc, BdlinkConstant.BACK);
			bisExcStatusChangeHandler.processBisForeignExcChangeMsg(bisForeignExc.getCbsBillId(), BdlinkConstant.BACK);
			//生成日志
			fexMessageService.saveMessage(bisForeignExc.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_BACK, backMsg, FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
			//完成消息通知
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_SEND, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
		} else {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
	}

	/**
	 * 指令退回
	 *
	 * @param id
	 */
	public void backBisForeignExcForCashFlow(String id, ICurrentUser user) {
		BisForeignExc bisForeignExc = this.get(id, true);
		//更新状态为B-指令退回
		bisForeignExc.setVoucherStat(BdlinkConstant.BACK);
		bisForeignExc.setCbsBillId(bisForeignExc.getCbsBillId());
		bisForeignExc.setUpdateBy(user.getLoginName());
		bisForeignExc.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(bisForeignExc);
		// 发送指令消息
//		bisMQService.sendBisForeignExcChangeMsg(bisForeignExc, BdlinkConstant.BACK);
		bisExcStatusChangeHandler.processBisForeignExcChangeMsg(bisForeignExc.getCbsBillId(), BdlinkConstant.BACK);
		//生成日志
		fexMessageService.saveMessage(bisForeignExc.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_BACK, "", FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
		//完成消息通知
		notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_SEND, bisForeignExc.getId(), user.getId());
		notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL, bisForeignExc.getId(), user.getId());
		notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
		notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
	}

	/**
	 * 指令重发
	 *
	 * @param bisForeignExc
	 */
	public void resendBisForeignExc(BisForeignExc bisForeignExc, ICurrentUser user) {
		//数据防篡改校验
		if(!bisForeignExc.getVerifyCode().equals(this.setVerifyCode(bisForeignExc))){
			throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisForeignExc.getVoucherNo());
		}
		//指令重发，更新原指令状态为D-指令删除
		int result = this.bisForeignExcDao.updateBisForeignExcVoucherStat(bisForeignExc, BdlinkConstant.DELETE);
		if(result>0) {
			//生成新的付款指令
			BisForeignExc entity = new BisForeignExc();
			BeanUtils.copyNotNullProperties(bisForeignExc, entity);
			entity.setId(null);
			entity.setReqDate(DateUtils.getDate());
			//调整了申请日期，需同步调整加密信息
			entity.setVerifyCode(this.setVerifyCode(entity));
			entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_FOREIGN_EXC));
			entity.setVoucherStat(BdlinkConstant.PRE_SEND);
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
			entity.setUpdateBy(user.getLoginName());
			entity.setUpdateDate(DateUtils.getDate());
			entity.setReturnTime(null);
			entity.setFirstReturnMsg(null);
			entity.setNextReturnMsg(null);
			//指令重发标志：1-重发指令
			entity.setResendSign(CommonConstant.SEND_SIGN_YES);
			//写入原指令编号
			entity.setOldVoucherNo(bisForeignExc.getVoucherNo());
			//签名
			entity.setSignatureCode(this.getSignatureCode(entity));
			this.saveOrUpdate(entity);
			// 发送指令消息
//			bisMQService.sendBisForeignExcChangeMsg(bisForeignExc, BdlinkConstant.PRE_SEND);
			bisExcStatusChangeHandler.processBisForeignExcChangeMsg(bisForeignExc.getCbsBillId(), BdlinkConstant.PRE_SEND);
			//生成日志
			fexMessageService.saveMessage(bisForeignExc.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_RESEND, "", FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
			//完成消息通知
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
		} else {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
	}

	/**
	 * 指令作废
	 *
	 * @param bisForeignExc
	 */
	public void defeatBisForeignExc(BisForeignExc bisForeignExc, ICurrentUser user) {
		//指令作废，更新原指令状态为7-指令无效
		int result = this.bisForeignExcDao.updateBisForeignExcVoucherStat(bisForeignExc, BdlinkConstant.INVALIDATE);
		if(result>0) {
			//更新业务单
			String serviceName = ForeignConstant.getServiceNameByVoucherFrom(bisForeignExc.getVoucherFrom());
			if (StringUtils.isNotEmpty(serviceName)) {
				IBisForeignExcBackService bisForeignExcBackService = (IBisForeignExcBackService)this.getBean(serviceName);
				BisForeignExcDTO dto = new BisForeignExcDTO();
				BeanUtils.copyNotNullProperties(bisForeignExc, dto);
				bisForeignExcBackService.defeatBisForeignExc(dto, user);
			}
			// 发送指令消息
//			bisMQService.sendBisForeignExcChangeMsg(bisForeignExc, BdlinkConstant.INVALIDATE);
			bisExcStatusChangeHandler.processBisForeignExcChangeMsg(bisForeignExc.getCbsBillId(), BdlinkConstant.INVALIDATE);
			//生成日志
			fexMessageService.saveMessage(bisForeignExc.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
			//完成消息通知
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
		} else {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
	}

	/**
	 * 指令交易成功/失败消息提醒
	 */
	@Override
	@XxlJob("BisForeignExcService")
	public void run(String arguments) {
		arguments = XxlJobHelper.getJobParam();
		logger.info("定时任务接收参数：【{}】", arguments);
		List<BisForeignExcVO> list = null;
		//取未生成消息提醒的指令
		StringBuilder sql = new StringBuilder(" and (b.notice_sign <> '1' OR b.notice_sign is null or b.notice_sign='')");
		if (NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL.equals(arguments)) {
			//交易失败提醒
			list = this.bisForeignExcDao.listByVoucherStat(BdlinkConstant.BANK_FAILED, sql.toString());
		} else if (NotificationConstant.FOREIGN.BISFOREIGNEXC_SUCCESS.equals(arguments)) {
			//交易成功提醒
			list = this.bisForeignExcDao.listByVoucherStat(BdlinkConstant.SUCCESS, sql.toString());
		} else if (NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME.equals(arguments)) {
			String payOverTime = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_PAYOVERTIME).getValue();
			Integer payOverTimeDays = -1;
			if (StringUtils.isNotBlank(payOverTime)) {
				payOverTimeDays = Integer.parseInt(payOverTime)*payOverTimeDays;
			}
			Date reqDate = DateUtils.addDay(DateUtils.getDateYMD(), payOverTimeDays);
			//sql.append(" and b.req_Date <= bt_to_date('").append(DateUtils.formatDate(reqDate)).append("','yyyy-MM-dd') ");
			//付款支付超时预警
			list = this.bisForeignExcDao.listByVoucherStat(BdlinkConstant.PRE_SEND, sql.toString());
		} else if (NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME.equals(arguments)) {
			//付款可疑指令发送确认提醒
			list = this.bisForeignExcDao.listByVoucherStat(BdlinkConstant.SUSPECTED, sql.toString());
		}
		if (!EmptyUtils.isEmptyList(list)) {
			List<String> ids = new ArrayList<>();
			for (BisForeignExcVO exc : list) {
				ids.add(exc.getId());
				//消息提醒
				notificationService.send(exc.getId(), exc.getCorpId(), arguments, exc);
			}
			//更新消息提醒标志
			this.bisForeignExcDao.updateNoticeSign(ids);
		}
	}

	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param bisForeignExc
	 * @return
	 */
	public int updateBisForeignExcCbsBillNo(BisForeignExc bisForeignExc){
		return bisForeignExcDao.updateBisForeignExcCbsBillNo(bisForeignExc);
	}

	/**
	 * 设为支付成功/失败
	 *
	 * @param bisForeignExcVO
	 */
	public void setPayResultBisExc(BisForeignExcVO bisForeignExcVO, ICurrentUser user) {
		BisForeignExc bisForeignExc = this.get(bisForeignExcVO.getId(), true);
		//等待结果状态才可以提交
		if (bisForeignExc != null && !BdlinkConstant.WAIT_RESULT.equals(bisForeignExc.getVoucherStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
		//指令状态重设，更新原指令状态为0-交易成功/2-银行处理失败
		int result = this.bisForeignExcDao.updateBisForeignExcVoucherStat(bisForeignExc, bisForeignExcVO.getVoucherStat());
		if(result>0) {
			//生成日志
			Integer messageType = BdlinkConstant.SUCCESS.equals(bisForeignExcVO.getVoucherStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
			fexMessageService.saveMessage(bisForeignExc.getId(), messageType, bisForeignExcVO.getHandleOpinion(), FexMessageConstant.BUS_TYPE_EXC, user.getLoginName(), user.getUsername());
			//完成消息通知
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_SEND, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_FAIL, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, bisForeignExc.getId(), user.getId());
			notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, bisForeignExc.getId(), user.getId());
		} else {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
	}

	public void injectValue(BisForeignExcVO bisForeignExcVO){
		//单位信息
		CorpDTO corpDTO = corpService.get(bisForeignExcVO.getCorpId());
		if(!EmptyUtils.isEmpty(corpDTO)){
			bisForeignExcVO.setCorpName(corpDTO.getName());
		}
		//币种信息
		BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(bisForeignExcVO.getCurId());
		if(!EmptyUtils.isEmpty(btCurrencyDTO)){
			bisForeignExcVO.setCurName(btCurrencyDTO.getCurName());
			bisForeignExcVO.setEnglishCode(btCurrencyDTO.getEnglishCode());
		}

		//银行类别信息
		BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bisForeignExcVO.getBankTypeId());
		if(!EmptyUtils.isEmpty(bankTypeDTO)){
			bisForeignExcVO.setBankTypeName(bankTypeDTO.getTypeName());
		}
		//接口信息
		BisBifInitDTO bisBifInitDTO = bisBifInitService.getByBifCode(bisForeignExcVO.getBifCode());
		if(!EmptyUtils.isEmpty(bisBifInitDTO)){
			bisForeignExcVO.setBifName(bisBifInitDTO.getName());
		}
	}

	/**
	 * 签名
	 * @param bisForeignExc
	 * @return
	 */
	public String getSignatureCode(BisForeignExc bisForeignExc) {
		BisForeignExcDTO bisForeignExcDTO = new BisForeignExcDTO();
		BeanUtils.copyNotNullProperties(bisForeignExc, bisForeignExcDTO);
		return DistortUtil.signBisForeignExc(bisForeignExcDTO);
	}

	/**
	 * 更新银行流水号
	 * @param cbsBillId
	 * @param bankSerialNo
	 * @return
	 */
	public int updateBankSerialNoByCbsBillId(String cbsBillId, String bankSerialNo, String refundFlag){
		return bisForeignExcDao.updateBankSerialNoByCbsBillId(cbsBillId, bankSerialNo, refundFlag);
	}

	/**
	 * 通过指令状态过滤交易表id
	 * @param voucherStat
	 * @param tranBillIdList
	 * @return
	 */
	public List<String> listFexTranReqIdsByStatus(String voucherStat,List<String> tranBillIdList){
		return bisForeignExcDao.listFexTranReqIdsByStatus(voucherStat,tranBillIdList);
	}
}
