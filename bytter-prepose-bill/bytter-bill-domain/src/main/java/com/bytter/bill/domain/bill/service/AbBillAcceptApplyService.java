package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillAcceptApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAcceptApplyVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: 提示承兑处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ACCEPTAPPLY)
@Service("abBillAcceptApplyService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ACCEPTAPPLY)
@Transactional
public class AbBillAcceptApplyService extends BaseService<AbBillAcceptApplyVO, String>
		implements IAccebillBaseService, IWorkFlowListener, ITaskService {
	@Autowired
	private AbBillAcceptApplyDao abBillAcceptApply;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<AbBillAcceptApplyVO, String> getEntityDao() {
		return abBillAcceptApply;
	}
	/**
	 * 加载AbBillAcceptApply表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillAcceptApplyInfo(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_ACCEPTAPPLY_QUERY,dataTablesInput.getCurrentUserId()), "c.apply_corp_id", true);
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOACCEPT), "l.bif_code",
				true);
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_ACCEPTAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillAcceptApplyVO> data  = abBillAcceptApply.listAbBillAcceptApply(dataTablesInput, corpAuthSql, notInBifCodeSql);
		Optional.ofNullable(data.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY","AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_ACCEPT_STATUS","AB_BILL_OPENWAY","AB_BILL_BAIL_TYPE"
					,"AB_BRIEF_STATUS","ACCEBILL_EMIT_TYPE","sf_yn","AB_BILL_STATUS","AB_BAIL_TYPE","AB_BILL_INSBRIEFID","AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for(AbBillAcceptApplyVO bill : listData){
				injectValues(bill,listDictMap);
			}
		});
		return  data;
	}
	/**
	 * 提示承兑申请操作
	 *
	 * @param reqInfo
	 * @param userVo
	 */
	public void batchApply(AbBillAcceptApplyVO reqInfo, ICurrentUser userVo) {
		String id = reqInfo.getIds();
		if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			String[] ids = StringUtils.split(id, ",");
			String[] rmks = reqInfo.getRmks() != null ? String.valueOf(reqInfo.getRmks()).split(",") : new String[0];
			// 设置id对应的备注类型
			Map<String, String> rmkTMap = new HashMap<String, String>();
			for (int i = 0; i < ids.length && i < rmks.length; i++) {
				rmkTMap.put(ids[i], rmks[i]);
			}
			AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
			accrecordsVo.setIdArray(ids);
			accrecordsVo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(accrecordsVo);
			if (ids.length != accrecordsList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			if (accrecordsList != null && !accrecordsList.isEmpty()) {
				List<AbAccrecords> acceptAccrecordslist = new ArrayList<AbAccrecords>();
				int addAcceptAccrecordsSize = accrecordsToAcceptAccrecords(acceptAccrecordslist, accrecordsList,
						reqInfo, rmkTMap, userVo);
				if (accrecordsList.size() != addAcceptAccrecordsSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				String[] vvids = new String[addAcceptAccrecordsSize];
				for (int i = 0; i < addAcceptAccrecordsSize; i++) {
					vvids[i] = acceptAccrecordslist.get(i).getVvid();
					String msg = reqInfo.isTask() ? AccebillConstant.LANG_LOG_TASK_SUCC
							: AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					// 再次提交需要把打回从表的BackBriefId为空
					String logType = reqInfo.isTask() ? AccebillConstant.ABLOG_LOGTYPE_TASK
							: AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
					abLogService.saveAbLog(acceptAccrecordslist.get(i).getVvid(), acceptAccrecordslist.get(i).getId(),
							acceptAccrecordslist.get(i).getBriefId(), logType, msg, userVo);
				}
				int upBoxStatusNum = abVoucherinfoViceService.batchUpdateBoxStatus(vvids,
						AccebillConstant.AB_BOX_STATUS_N, userVo);
				if (addAcceptAccrecordsSize != upBoxStatusNum) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < addAcceptAccrecordsSize; i++) {
					AbAccrecords accrecords = acceptAccrecordslist.get(i);
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecords.getVvid());
					AbAccrecordsVO variables = new AbAccrecordsVO();
					BeanUtils.copyNotNullProperties(accrecords.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(accrecords, variables);
					abBillNotificationService.completeMsg(accrecords, userVo);
					// 是否定时任务处理
					String userId = userVo.getId();
					// 启动工作流
					getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ACCEPTAPPLY, accrecords.getId(), variables,
							accrecords.getVouViceInfo().getApplyCorpId(),userId);
				}
			}
		}
	}
	/**
	 * 审批完成处理
	 *
	 * @param buId
	 * @param isPassOK
	 * @param appMsg
	 * @param userVo
	 */
	public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(buId, true);
		if (info == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		if (isPassOK) {
			int oldVSNum = abAccrecordsService.updatAppPass(info.getLastid(), isPassOK, userVo);
			if (oldVSNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		} else {
			int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
			if (reNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		}
		String msg = appMsg;
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	/**
	 * 批量转换提示承兑申请数据
	 *
	 * @param bailAccrecordslist
	 * @param accrecordslist
	 * @param reqInfo
	 * @return
	 */
	public int accrecordsToAcceptAccrecords(List<AbAccrecords> bailAccrecordslist, List<AbAccrecords> accrecordslist,
											AbBillAcceptApplyVO reqInfo, Map<String, String> rmkTMap, ICurrentUser userVo) {
		for (int i = 0; i < accrecordslist.size(); i++) {
			AbAccrecords oldInfo = accrecordslist.get(i);
			AbVoucherinfo vouInfo = abVoucherinfoService.get(oldInfo.getVid(), true);
			AbVoucherinfoVice vice = abVoucherinfoViceService.get(oldInfo.getVvid(), true);
			AbAccrecords bailInfo = new AbAccrecords();
			bailInfo.setVid(oldInfo.getVid());
			bailInfo.setVvid(oldInfo.getVvid());
			bailInfo.setVouInfo(vouInfo);
			bailInfo.setVouViceInfo(vice);
			bailInfo.setApplyDate(new Date());
			bailInfo.setLastid(oldInfo.getId());
			bailInfo.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			bailInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			bailInfo.setBriefId(AccebillConstant.BRIEF_STATUS_002);
			bailInfo.setLastbillcode(vouInfo.getBillStatus());
			bailInfo.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			bailInfo.setBillFlag(vouInfo.getBillFlag());
			bailInfo.setRmk(rmkTMap.get(oldInfo.getId()));
			bailInfo.setReqAcctId(vice.getBankAcc());
			bailInfo.setRecNm(vouInfo.getAcceptor());
			bailInfo.setRecAcctId(vouInfo.getAcceptoracc());
			bailInfo.setRecAcctsvcr(vouInfo.getAcceptorbankno());
			bailAccrecordslist.add(bailInfo);
		}
		return abAccrecordsService.batchSaveAccrecords(bailAccrecordslist, userVo);
	}
	/**
	 * 审批回调方法
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ACCEPTAPPLY, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "l.id", true);
		DataTablesOutput<AbBillAcceptApplyVO> dp = abBillAcceptApply.dataTableApprove(dataTablesInput, businessSql);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY","AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_ACCEPT_STATUS"
					,"AB_BRIEF_STATUS","ACCEBILL_EMIT_TYPE","sf_yn","AB_BILL_STATUS","AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for(AbBillAcceptApplyVO bill : listData){
				injectValues(bill,listDictMap);
			}
		});
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 获票据信息
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAcceptApplyVO getAbBillAcceptApplyVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbBillAcceptApplyVO reInfo = new AbBillAcceptApplyVO();
		reInfo.setUcondlprmsmrk(AccebillConstant.UCONDLCONSGNMTMRK);
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid());// 票据从表信息
		reInfo.setBusPersonnel(abVoucherinfoVice.getBusPersonnel());
		reInfo.setVvid(abVoucherinfoVice.getId());
		// 根据单位id获取单位信息
		// 根据单位id获取单位信息
		if(StringUtils.isEmpty(reInfo.getCorpName())){
			CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
			if (corpInfo != null) {
				reInfo.setCorpName(corpInfo.getName());
			}
		}
		if(StringUtils.isEmpty(reInfo.getApplyCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getApplyCorpId())){
			CorpDTO applyCorpInfo = abCorpService.get(abVoucherinfoVice.getApplyCorpId());
			if (applyCorpInfo != null) {
				reInfo.setApplyCorpName(applyCorpInfo.getName());
			}
		}
		if(StringUtils.isEmpty(reInfo.getBusCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getBusCorpId())){
			CorpDTO busCorpInfo = abCorpService.get(abVoucherinfoVice.getBusCorpId());
			if (busCorpInfo != null) {
				reInfo.setBusCorpName(busCorpInfo.getName());
			}
		}
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
				,"AB_BILL_ACCEPT_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_UCONDLPRMSMRK");
		injectValues(reInfo,listDictMap);
		return reInfo;
	}
	private void injectValues(AbBillAcceptApplyVO bill,Map<String, Map<String, DictDTO>> listDictMap){
		// 票据类型
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag+"")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 开出方式
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing+"")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCEPT_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status+"")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 票据状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 提示承兑操作业务状态
		Optional.ofNullable(bill.getOptStatus()).ifPresent(optStatus -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus)))
					.ifPresent(dict -> bill.setOptStatusName(dict.getName()));
		});
		// 签收回复标志
		Optional.ofNullable(bill.getSignUpFlag()).ifPresent(signUpFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_SGNUPMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(signUpFlag)))
					.ifPresent(dict -> bill.setSignUpFlagName(dict.getName()));
		});
		// 保证类型
		Optional.ofNullable(bill.getBailType()).ifPresent(bailType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BAIL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bailType)))
					.ifPresent(dict -> bill.setBailTypeName(dict.getName()));
		});
		// 到期无条件支付承诺
		Optional.ofNullable(bill.getUcondlprmsmrk()).ifPresent(ucondlprmsmrk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_UCONDLPRMSMRK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(ucondlprmsmrk)))
					.ifPresent(dict -> bill.setUcondlprmsmrkName(dict.getName()));
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
		// 保证金类型
		Optional.ofNullable(bill.getBailType()).ifPresent(bailType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BAIL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bailType)))
					.ifPresent(dict -> bill.setBailTypeName(dict.getName()));
		});
	}
	/**
	 * 自动提示承兑定时任务触发执行
	 */
	@Override
	@XxlJob("AbBillAcceptApplyService")
	public void run(String arguments) {
		try {
			if (abVoucherinfoViceService.isEAutoAccept()) {
				ICurrentUser userVo=abUserService.getScheduler();
				this.logger.info("---启用自动提示承兑，开始执行定时任务---");
				autoAccept(userVo);
				this.logger.info("---启用自动提示承兑，结束执行定时任务---");
			} else {
				this.logger.info("---系统未启动自动提示承兑参数，不执行定时器---");
			}
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * 提示承兑自动业务处理
	 */
	private void autoAccept(ICurrentUser userVo) {
		List<AbBillAcceptApplyVO> autoAcceptList = listAutoAbBillAcceptApplyInfo();
		if (autoAcceptList == null || autoAcceptList.isEmpty()) {
			this.logger.info("---自动提示承兑，【0】条数据。");
			return;
		}
		this.logger.info("---自动提示承兑，【" + autoAcceptList.size() + "】条数据。");
		for (AbBillAcceptApplyVO reqInfo : autoAcceptList) {
			reqInfo.setIds(reqInfo.getId());
			reqInfo.setTask(true);
			batchApply(reqInfo, userVo);
			abBillMiddleService.saveBillMiddle(reqInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_002,userVo);
		}
	}
	/**
	 * 加载AbBillAcceptApply表数据
	 *
	 * @return 返回符合条件的数据
	 */
	@Transactional(readOnly = true)
	public List<AbBillAcceptApplyVO> listAutoAbBillAcceptApplyInfo() {
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOACCEPT), "l.bif_code",
				true);
		return abBillAcceptApply.listAutoAbBillAcceptApplyInfo(notInBifCodeSql);
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillAcceptApplyVOById(aid);
	}
}
