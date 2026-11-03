package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillCollectionAccountDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

/**
 * @Title: Service
 * @Description: 托收到账
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONACCOUNT)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONACCOUNT)
@Service("abBillCollectionAccountService")
@Transactional
public class AbBillCollectionAccountService extends BaseService<AbBillCollectionAccountVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillCollectionAccountDao abBillCollectionAccountDao;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * 托收到账列表查询
	 * 
	 * @param input
	 * @param isApply 是否审批中
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput<AbBillCollectionAccountVo> getDataTablesUnion(DataTablesInput input, String isApply) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_COLLECTIONACCOUNT_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_COLLECTIONACCOUNT_QUERY,corpIds);
		DataTablesOutput<AbBillCollectionAccountVo> dataTablesUnion = abBillCollectionAccountDao.getDataTablesUnion(input, corpAuthList, isApply);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
					, "ABBILL_COLLECTIONACCOUNT_STATUS", "AB_ENTER_STOCK", "AB_BILL_OPENWAY", "AB_BILL_SEND_SIGN"
					, "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillCollectionAccountVo bill : listData) {
				injectValues(bill, listDictMap);
				if(StrUtil.isNotBlank(bill.getApplyCorpId())){
					corpIdSet.add(bill.getApplyCorpId());
				}
				if(StrUtil.isNotBlank(bill.getCorpId())){
					corpIdSet.add(bill.getCorpId());
				}
				if(StrUtil.isNotBlank(bill.getBusCorpId())){
					corpIdSet.add(bill.getBusCorpId());
				}
			}
		});
		if(corpIdSet.size() <= 0){
			return dataTablesUnion;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesUnion;
		}
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			for (AbBillCollectionAccountVo bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}
		});
		return dataTablesUnion;
	}
	private void injectValues(AbBillCollectionAccountVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质：10 纸票、11 电票
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ABBILL_COLLECTIONACCOUNT_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 开出方式：0  对外开出；1 对内自开
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
		//出票标志: 1 新系统出票 、0 老系统出票
		Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
					.ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
		});
		//是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
		Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
					.ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
		});
	}
	/**
	 * 托收到账
	 */
	public void collectionApply(AbBillCollectionAccountVo abBillCollectionAccountVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillCollectionAccountVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillCollectionAccountVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillCollectionAccountVo.getAids().split(",");
		String[] signeroks = abBillCollectionAccountVo.getSignerok().split(",");// 收款人
		AbAccrecords abAccrecordsOld = null;// 当前流水
		AbAccrecords abAccrecordsNew = null;// 托收到账流水
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		for (int i = 0; i < abVoucherinfoIds.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i],true);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i],true);
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i],true);
			abAccrecordsNew = getAccrecords(abBillCollectionAccountVo, abAccrecordsOld, userVo);
			abAccrecordsNew.setSignerok(signeroks[i]);// 收款人
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecordsNew, variables);// 票据流水信息填充到审批对象
			// 票据当前流水改为作废
			abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			// 从表box_status改为10
			abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONACCOUNT, abAccrecordsNew.getId(),
					variables, abVoucherinfoVice.getApplyCorpId(),userVo.getId());
		}
		abLogService.batchSaveAbLog(abVoucherinfoViceIds, abAccrecordsIds, AccebillConstant.BRIEF_STATUS_6020,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
	}
	/**
	 * 自动审批通过托收到账（不使用工作流审批）
	 */
	public void autoApprovalCollectionApply(AbBillCollectionAccountVo abBillCollectionAccountVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillCollectionAccountVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillCollectionAccountVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillCollectionAccountVo.getAids().split(",");
		String[] signeroks = abBillCollectionAccountVo.getSignerok().split(",");// 收款人
		AbAccrecords abAccrecordsOld;// 当前流水
		AbAccrecords abAccrecordsNew;// 托收到账流水
		for (int i = 0; i < abVoucherinfoIds.length; i++) {
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i],true);
			abAccrecordsNew = getAccrecords(abBillCollectionAccountVo, abAccrecordsOld, userVo);
			abAccrecordsNew.setSignerok(signeroks[i]);// 收款人
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			// 票据当前流水改为作废
			abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			// 从表box_status改为10
			abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
			abLogService.saveAbLog(abVoucherinfoViceIds[i], abAccrecordsIds[i], AccebillConstant.BRIEF_STATUS_6020,
					AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
			String logType = AccebillConstant.ABLOG_LOGTYPE_APP;
			String appMsg = AccebillConstant.LANG_LOG_APP_SUCC;
			if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(abAccrecordsNew);
				if(abAccbookService.isNotAutoAccbook()&&isAccboo){
					abAccbookService.addAbAccbookInfo(abAccrecordsNew);
					int appNum = abAccrecordsService.approve(abAccrecordsNew.getId(), AccebillConstant.AB_BILL_BUS_STATUS94, userVo);
					if (appNum != 1) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
					abLogService.saveAbLog(abAccrecordsNew.getVvid(), abAccrecordsNew.getId(), abAccrecordsNew.getBriefId(), logType, appMsg, userVo);
				}else{
					busOverOp(abAccrecordsNew, logType, appMsg, userVo);
					if(isAccboo){
						// 记账
						abAccbookService.accbookVoucher(abAccrecordsNew, userVo);
						logType = AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_APP;
						abLogService.saveAbLog(abAccrecordsNew.getVvid(), abAccrecordsNew.getId(), abAccrecordsNew.getBriefId(),
								logType, AccebillConstant.LANG_LOG_AUTOACCBOOK_SUCC, userVo);
					}
				}
			}else{
				busOverOp(abAccrecordsNew, logType, appMsg, userVo);
			}
		}
	}
	/**
	 * 根据参数获取托收到账流水
	 * 
	 */
	protected AbAccrecords getAccrecords(AbBillCollectionAccountVo abBillCollectionAccountVo,
			AbAccrecords abAccrecordsOld, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		entity.setVid(abAccrecordsOld.getVid());// 票据主表ID
		entity.setVvid(abAccrecordsOld.getVvid());// 票据从表ID
		entity.setLastid(abAccrecordsOld.getId());// 上条流水ID
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_6020);
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abBillCollectionAccountVo.getBusinessDate());// 业务归属日期
		// entity.setBillFlag(abVoucherinfo.getBillFlag());
		entity.setCreateName(userVo.getUsername());// 创建人名称
		entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		entity.setCreateDate(new Date());// 创建日期
		return entity;
	}
	@Override
	public BaseDao<AbBillCollectionAccountVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 批量审批
	 * 
	 * @param aid
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void batchApprove(String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		if (StringUtils.isEmpty(aid)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(aid,true);
		if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
		
        if (isPassOK) {
			String logType = AccebillConstant.ABLOG_LOGTYPE_APP;
			if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(info);
				if(abAccbookService.isNotAutoAccbook()&&isAccboo){
					abAccbookService.addAbAccbookInfo(info);
					int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS94, userVo);
					if (appNum != 1) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
					abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, appMsg, userVo);
				}else{
					busOverOp(info, logType, appMsg, userVo);
					if(isAccboo){
						// 记账
						abAccbookService.accbookVoucher(info, userVo);
						logType = AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_APP;
						abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, AccebillConstant.LANG_LOG_AUTOACCBOOK_SUCC, userVo);
					}
				}
			}else{
				busOverOp(info, logType, appMsg, userVo);
			}
		} else {
			String logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
			busRepulseOp(info, logType, appMsg, userVo);
		}
	}
	/**
	 * 票据业务完成操作
	 * @param abAccrecords 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busOverOp(AbAccrecords abAccrecords, String logType, String msg, ICurrentUser userVo){
		int num = 0;
		/** 审批通过 */
		// 1从表box_status=11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 2流水表status11改为95
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), abAccrecords.getBriefId(), logType,
				msg, userVo);
	}
	/**
	 * 票据业务打回操作
	 * @param abAccrecords 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords abAccrecords, String logType, String msg, ICurrentUser userVo){
		int num = 0;
		/** 打回 */
		// 托收到账流水删除 status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 2上一条流水VALID_SIGN=1
		num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 3从表box_status=11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 4从表添加打回标记
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_6020,
				abAccrecords.getId());
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), abAccrecords.getBriefId(), logType,
				msg, userVo);
	}
	
	/**
	 * 工作流启动回调方法(无需关注)
	 */
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// Auto-generated method stub
	}
	/**
	 * 审批回调方法
	 * 
	 * @param workFlowResult
	 * processDefinitionKey
	 *            工作流类型key
	 * businessKey
	 *            业务类型key
	 * result
	 *            审批结果:true审批通过，false审批打回
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		batchApprove(workFlowResult.getBusinessKey(), appMsg, workFlowResult.isPass(), workFlowResult.getUser());
	}
	/**
	 * 加载审核列表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONACCOUNT, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abBillCollectionAccountDao.dataTableApprove(dataTablesInput, businessList);
		// 冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
				, "AB_ENTER_STOCK", "AB_BILL_OPENWAY");
		List<AbBillCollectionAccountVo> data = dp.getData();
		Map<String, CorpDTO> corpMap = corpService.getCorpNetMap();
		for (AbBillCollectionAccountVo bill : data) {
			if (StringUtils.isNotBlank(bill.getBusCorpId())){
				bill.setBusCorpName(corpMap.get(bill.getBusCorpId()).getName());
			}
			injectValues(bill, listDictMap);
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 票据托收申请审批详情页
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillCollectionAccountVo getAbBillCollectionAccountVoById(String aid, Model model) {
		AbBillCollectionAccountVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		if (abAccrecords != null) {
			vo = new AbBillCollectionAccountVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setAid(aid);
			// 获取持票单位名称
			if (StringUtils.isNotEmpty(vo.getCorpId())) {
				CorpDTO corp = abCorpService.get(vo.getCorpId());
				if (corp != null) {
					vo.setCorpName(corp.getName());
				}
			}
			// 获取持票单位银行信息
			if (StringUtils.isNotEmpty(vo.getBankAcc())) {
				try {
					BtBankAccDTO bankInfo = abAccrecordsService.getBtBankAccDTO(vo.getBankAcc());
					if (model != null) {
						model.addAttribute("bankInfo", bankInfo);
					}
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 持票人账号不存在!
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTEXIST);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			}
		}
		if (ObjectUtil.isNotEmpty(vo)){
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_COLLECTIONACCOUNT_STATUS"
					, "AB_ENTER_STOCK", "AB_BILL_OPENWAY", "AB_BILL_SEND_SIGN");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillCollectionAccountVoById(aid, null);
	}
}
