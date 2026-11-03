package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbReceiveBillBackDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbReceiveBillBackVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
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
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: 退票申请
 * @author onlineGenerator
 * @date 2017-06-29 16:53:43
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABRECEIVEBILLBACK)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABRECEIVEBILLBACK)
@Service("abReceiveBillBackService")
@Transactional
public class AbReceiveBillBackService extends BaseService<AbVoucherinfo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbReceiveBillBackDao abReceiveBillBackDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<AbVoucherinfo, String> getEntityDao() {
		return abReceiveBillBackDao;
	}
	/**
	 * 加载退票申请数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbReceiveBillBack(DataTablesInput dataTablesInput) {		
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_REBILLBACK_QUERY,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_REBILLBACK_QUERY,corpIds);
		DataTablesOutput<AbReceiveBillBackVo> dataTablesOutput = abReceiveBillBackDao.listAbReceiveBillBack(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
					, "AB_BILL_AbReceiveBillBack_STATUS", "AB_ENTER_STOCK", "AB_BILL_OPENWAY", "AB_BILL_SEND_SIGN"
					, "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbReceiveBillBackVo bill : listData) {
				injectValues(bill, listDictMap);
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbReceiveBillBackVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbReceiveBillBack_STATUS");
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
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillPay_BRIEF_ID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
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
	 * 确认退票
	 * 
	 * @param back
	 */
	public void returnTicket(AbReceiveBillBackVo back, ICurrentUser userVo) {
		int len = back.getIds().split(",").length;
		// 批量修改主表box_status
		int num = abVoucherinfoViceService.batchSubmit(back.getVvids().split(","), userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(back, true);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
		// 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6120，流水：status=-2(已删除),这条兑付确认流水valid_sign=2(已删除)
		abLogService.batchSaveAbLog(back.getVvids().split(","), back.getIds().split(","),
				AccebillConstant.BRIEF_STATUS_6004, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
				AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		for (AbAccrecords abs : abList) {
			
			abBillNotificationService.completeMsg(abs, userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
			AbReceiveBillBackVo variables = new AbReceiveBillBackVo();
			BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
			BeanUtils.copyNotNullProperties(abs, variables);
			// 生成余额
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABRECEIVEBILLBACK, abs.getId(), variables,
					abs.getVouViceInfo().getApplyCorpId(),userVo.getId());
		}
	}
	/**
	 * 审批操作
	 * 
	 * @param buId
	 *            主表id数组
	 * @param isPassOK
	 *            通过标识
	 * @param appMsg
	 *            审批信息
	 */
	public void approvalPass(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(buId, true);
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
					abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6004, logType, appMsg, userVo);
				}else{
					busOverOp(info, logType, appMsg, userVo);
					if(isAccboo){
						// 记账
						abAccbookService.accbookVoucher(info, userVo);
						logType = AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_APP;
						abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6004, logType, AccebillConstant.LANG_LOG_AUTOACCBOOK_SUCC, userVo);
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
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo){
		// 批量修改票据主表信息(修改处理标记为处理完成)
		int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		//修改票据从表有效标识为无效
		int vvnum = abVoucherinfoViceService.updateValidSign(info.getVvid(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
		if (vvnum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS95;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 通过则上一流水valid_sign=2 失败修改BackBriefId
		String[] lastids = new String[] { info.getLastid() };
		num = abAccrecordsService.batchAppPass(lastids, true, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 释放预算
		abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
				BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
		// 生成余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6004, logType, appMsg,
				userVo);
	}
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo){
		// 批量修改票据主表信息(修改处理标记为处理完成)
		int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		if (reNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 生成余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6004, logType, appMsg,
				userVo);
	}
	
	/**
	 * 获取流水信息
	 * 
	 * @param back
	 * @param flag
	 *            true:审批中 false:审批通过
	 * @return
	 */
	@Transactional(readOnly = true)
	protected List<AbAccrecords> getAbAccrecords(AbReceiveBillBackVo back, boolean flag) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		// 流水表id数组
		String[] aids = back.getIds().split(",");
		for (String id : aids) {
			// 获取流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(id,true);
			// 获取当前票据主，从表信息
			AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(),true);
			AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(),true);
			// 新的流水数据
			AbAccrecords entity = new AbAccrecords();
			// 票据主表ID
			entity.setVid(ab.getId());
			// 票据从表ID
			entity.setVvid(abVice.getId());
			entity.setVouInfo(ab);
			entity.setVouViceInfo(abVice);
			// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
			if (flag) {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			} else {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			}
			// abVoucherinfoViceService.updateBackBriefId(abVice.getId(), "");//
			// 从表清除打回标记 挪到批量处理中
			// 业务类型
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_6004);
			entity.setLastid(id);
			entity.setLastbillcode(abVoucherinfoService.get(abAccrecords.getVid()).getBillStatus());
			entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
			// 申请日期
			entity.setBusinessDate(back.getDate());// 业务归属日期
			entity.setApplyDate(new Date());// 申请日期
			entity.setBillFlag(ab.getBillFlag());
			abList.add(entity);
		}
		return abList;
	}
	/**
	 * 根据流水查询票据信息
	 * 
	 * @param aid
	 *            流水ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbReceiveBillVO getAbReceiveBillVoByAid(String aid) {
		return abReceiveBillService.getAbReceiveBillVOById(aid);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABRECEIVEBILLBACK, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abReceiveBillBackDao.dataTableApprove(dataTablesInput, businessList);
		List<AbReceiveBillBackVo> results = dp.getData();
		Optional.ofNullable(results).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
					, "AB_BILL_AbReceiveBillBack_STATUS", "AB_ENTER_STOCK", "AB_BILL_OPENWAY", "AB_BILL_SEND_SIGN"
					, "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbReceiveBillBackVo bill : listData) {
				injectValues(bill, listDictMap);
			}
		});
		return workFlowBusiness.merge(dp);
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
	 * @param processDefinitionKey
	 *            工作流类型key
	 * @param businessKey
	 *            业务类型key
	 * @param result
	 *            审批结果:true审批通过，false审批打回
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		approvalPass(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbReceiveBillVoByAid(aid);
	}
}
