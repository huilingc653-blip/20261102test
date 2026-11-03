package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillReceiveApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillReceiveApplyVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
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
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: 提示收票处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEAPPLY)
@Service("abBillReceiveApplyService")
@Transactional
public class AbBillReceiveApplyService extends BaseService<AbBillReceiveApplyVO, String>
		implements IAccebillBaseService, IWorkFlowListener, ITaskService {
	@Autowired
	private AbBillReceiveApplyDao abBillReceiveApply;
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
	public BaseDao<AbBillReceiveApplyVO, String> getEntityDao() {
		return abBillReceiveApply;
	}
	/**
	 * 加载AbBillReceiveApply表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillReceiveApplyInfo(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECEIVEAPPLY_QUERY,dataTablesInput.getCurrentUserId()), "c.apply_corp_id", true);
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOINVOICE), "l.bif_code",
				true);
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_RECEIVEAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillReceiveApplyVO> dataTablesOutput = abBillReceiveApply.listAbBillReceiveApply(dataTablesInput, corpAuthSql, notInBifCodeSql);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECEIVE_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND" );
			for (AbBillReceiveApplyVO bill : listData){
				bill.injectValues(listDictMap);
			}
		});
		return dataTablesOutput;
	}
	/**
	 * 提示收票批量申请操作
	 *
	 * @param reqInfo
	 * @param userVo
	 */
	public void batchApply(AbBillReceiveApplyVO reqInfo, ICurrentUser userVo) {
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
				List<AbAccrecords> receiveAccrecordslist = new ArrayList<AbAccrecords>();
				int addReceiveAccrecordsSize = accrecordsToReceiveAccrecords(receiveAccrecordslist, accrecordsList,
						reqInfo, rmkTMap, userVo);
				if (accrecordsList.size() != addReceiveAccrecordsSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				String[] vvids = new String[addReceiveAccrecordsSize];
				for (int i = 0; i < addReceiveAccrecordsSize; i++) {
					vvids[i] = receiveAccrecordslist.get(i).getVvid();
					String msg = reqInfo.isTask() ? AccebillConstant.LANG_LOG_TASK_SUCC
							: AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					String logType = reqInfo.isTask() ? AccebillConstant.ABLOG_LOGTYPE_TASK
							: AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
					abLogService.saveAbLog(receiveAccrecordslist.get(i).getVvid(), receiveAccrecordslist.get(i).getId(),
							receiveAccrecordslist.get(i).getBriefId(), logType, msg, userVo);
				}
				int upBoxStatusNum = abVoucherinfoViceService.batchUpdateBoxStatus(vvids,
						AccebillConstant.AB_BOX_STATUS_N, userVo);
				if (addReceiveAccrecordsSize != upBoxStatusNum) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < addReceiveAccrecordsSize; i++) {
					AbAccrecords accrecords = receiveAccrecordslist.get(i);
					// 再次提交需要把打回从表的BackBriefId为空
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecords.getVvid());
					AbAccrecordsVO variables = new AbAccrecordsVO();
					BeanUtils.copyNotNullProperties(accrecords.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(accrecords, variables);
					abBillNotificationService.completeMsg(accrecords, userVo);
					// 是否定时任务处理
					String userId = userVo.getId();
					// 启动工作流
					getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEAPPLY, accrecords.getId(),
							variables, accrecords.getVouViceInfo().getApplyCorpId(),userId);
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
		// int oldVSNum = abAccrecordsService.updatAppPass(info.getLastid(),
		// isPassOK);
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
	 * 批量转换提示收票申请数据
	 *
	 * @param bailAccrecordslist
	 * @param accrecordslist
	 * @param reqInfo
	 * @return
	 */
	public int accrecordsToReceiveAccrecords(List<AbAccrecords> bailAccrecordslist, List<AbAccrecords> accrecordslist,
											 AbBillReceiveApplyVO reqInfo, Map<String, String> rmkTMap, ICurrentUser userVo) {
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
			bailInfo.setBriefId(AccebillConstant.BRIEF_STATUS_003);
			bailInfo.setLastbillcode(vouInfo.getBillStatus());
			bailInfo.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			bailInfo.setBillFlag(vouInfo.getBillFlag());
			bailInfo.setRmk(rmkTMap.get(oldInfo.getId()));
			bailInfo.setReqAcctId(vice.getBankAcc());
			bailInfo.setRecNm(vouInfo.getPayee());
			bailInfo.setRecAcctId(vouInfo.getPayeeacc());
			bailInfo.setRecAcctsvcr(vouInfo.getPayeebankno());
			bailAccrecordslist.add(bailInfo);
		}
		return abAccrecordsService.batchSaveAccrecords(bailAccrecordslist, userVo);
	}
	/**
	 * 审批回调方法
	 */
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
	}
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEAPPLY, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "l.id", true);
		DataTablesOutput<AbBillReceiveApplyVO> dp = abBillReceiveApply.dataTableApprove(dataTablesInput, businessSql);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECEIVE_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE");
			for (AbBillReceiveApplyVO bill : listData){
				bill.injectValues(listDictMap);
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
	public AbBillReceiveApplyVO getAbBillReceiveApplyVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbBillReceiveApplyVO reInfo = new AbBillReceiveApplyVO();
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
				,"AB_BILL_RECEIVE_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE");
		reInfo.injectValues(listDictMap);
		return reInfo;
	}
	/**
	 * 自动提示收票定时任务触发执行
	 */
	@Override
	@XxlJob("AbBillReceiveApplyService")
	public void run(String arguments) {
		try {
			if (abVoucherinfoViceService.isEAutoInvoice()) {
				ICurrentUser userVo= abUserService.getScheduler();
				this.logger.info("---启用自动提示收票，开始执行定时任务---");
				//提示收票延迟推送时间设置（部分银行不支持承兑完成马上发起提示收票要隔几分钟默认3分钟）
				if(StringUtils.isEmpty(arguments)){
					arguments="3";
				}
				autoReceive(userVo,arguments);
				this.logger.info("---启用自动提示收票，结束执行定时任务---");
			} else {
				this.logger.info("---系统未启动自动提示收票参数，不执行定时器---");
			}
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	/**
	 * 提示收票自动业务处理
	 */
	private void autoReceive(ICurrentUser userVo,String arguments) {
		List<AbBillReceiveApplyVO> autoInfoList = listAutoAbBillReceiveApplyInfo(arguments);
		if (autoInfoList == null || autoInfoList.isEmpty()) {
			this.logger.info("---自动提示收票，【0】条数据。");
			return;
		}
		this.logger.info("---自动提示收票，【" + autoInfoList.size() + "】条数据。");
		for (AbBillReceiveApplyVO reqInfo : autoInfoList) {
			reqInfo.setIds(reqInfo.getId());
			reqInfo.setTask(true);
			batchApply(reqInfo, userVo);
			abBillMiddleService.saveBillMiddle(reqInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_003,userVo);
		}
	}
	/**
	 * 加载可以做自动提示的数据
	 *
	 * @return 返回符合条件的数据
	 */
	@Transactional(readOnly = true)
	public List<AbBillReceiveApplyVO> listAutoAbBillReceiveApplyInfo(String arguments) {
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOINVOICE), "l.bif_code",
				true);
		return abBillReceiveApply.listAutoAbBillReceiveApplyInfo(notInBifCodeSql,arguments);
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillReceiveApplyVOById(aid);
	}
}
