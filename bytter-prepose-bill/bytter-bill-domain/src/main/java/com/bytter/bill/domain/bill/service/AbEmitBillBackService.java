package com.bytter.bill.domain.bill.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbEmitBillBackDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbEmitBillBackVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
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
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @Title: Service
 * @Description: 未用退票
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLBACK)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLBACK)
@Service("abEmitBillBackService")
@Transactional
public class AbEmitBillBackService extends BaseService<AbVoucherinfo, String>
		implements IAccebillBaseService, IWorkFlowListener, ITaskService {
	@Autowired
	private AbEmitBillBackDao abEmitBillBackDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbNisBillHeadService abNisBillHeadService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbFcBondHookService abFcBondHookService;
	@Autowired
	private AbGuaranteeService abGuaranteeService;
	@Autowired
	private AbRatCpUrationService abRatCpUrationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	public BaseDao<AbVoucherinfo, String> getEntityDao() {
		return abEmitBillBackDao;
	}
	/**
	 * 加载未用退票数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbEmitBillBack(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RETURNTICKET_QUERY,dataTablesInput.getCurrentUserId());
		dataTablesInput.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, abVoucherinfoViceService.isEmitMake());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_RETURNTICKET_QUERY,corpIds);
		DataTablesOutput<AbEmitBillBackVo> dataTablesOutput = abEmitBillBackDao.listAbEmitBillBack(dataTablesInput, corpAuthList);
		if (ObjectUtil.isNotEmpty(dataTablesOutput.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_EMITBILL_SOURCE", "AB_BILL_FLAG"
					, "AB_BILL_OPENWAY", "AB_BILL_AbEmitBillBack_STATUS", "AB_EMITBILLBACK_BRIEF", "AB_BILL_SEND_SIGN"
					, "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbEmitBillBackVo bill : dataTablesOutput.getData()) {
				injectValues(bill, listDictMap);
			}
		}
		return dataTablesOutput;
	}
	public void injectValues(AbEmitBillBackVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 开票数据来源
		Optional.ofNullable(bill.getSource()).ifPresent(source -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMITBILL_SOURCE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(source)))
					.ifPresent(dict -> bill.setSourceName(dict.getName()));
		});
		// 流通介质：10 纸票、11 电票
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 开出方式：0  对外开出；1 对内自开
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
//		// 业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成
//		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
//			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_EMITBILL_STATUS");
//			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
//					.ifPresent(dict -> bill.setStatusName(dict.getName()));
//		});
		// 业务状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMITBILLBACK_BRIEF");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是；
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
		// 状态：
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillBack_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(status))))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
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
	public void returnTicket(AbEmitBillBackVo back, ICurrentUser userVo) {
		// 获取需要修改的条数
		int len = back.getIds().split(",").length;
		// 批量修改主表box_status
		int num = abVoucherinfoViceService.batchSubmit(back.getVvids().split(","), userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(back, true, userVo);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		//批量修改--补录后生成收票登记(6001)暂存状态的票据--修改流水表status=-2
		String[] aids = back.getIds().split(",");
		for (String id : aids) {
			abAccrecordsService.updateStatusByLastId(id,userVo,AccebillConstant.AB_BILL_BUS_STATUS_2);
		}
		// 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
		// 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_004，流水：status=-2(已删除),这条未用退票流水valid_sign=2(已删除)
		abLogService.batchSaveAbLog(back.getVvids().split(","), back.getIds().split(","),
				AccebillConstant.BRIEF_STATUS_004, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
				AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		for (AbAccrecords abs : abList) {
			abBillNotificationService.completeMsg(abs, userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
			AbEmitBillBackVo variables = new AbEmitBillBackVo();
			BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
			BeanUtils.copyNotNullProperties(abs, variables);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLBACK, abs.getId(), variables,
					abs.getVouViceInfo().getApplyCorpId(), userVo.getId());
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
		// 批量修改票据主表信息(修改处理标记为处理完成)
		int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
		Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 通过则上一流水valid_sign=2 失败修改BackBriefId
		if (isPassOK) {
			String[] lastids = new String[] { info.getLastid() };
			num = abAccrecordsService.batchAppPass(lastids, isPassOK, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get (info.getVvid (),true);
			if(StringUtils.isNotBlank (abVoucherinfoVice.getVoucherId ())){
				//释放付款单预算
				List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
						BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,abVoucherinfoVice.getVoucherId ());
				if(list!=null && !list.isEmpty ()){
					String billNo = list.get (0).getBillNo();
					if(StringUtils.isNotBlank (billNo) ){
						abFbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
								BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
					}
				}
			}else{
				// 释放预算
				abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
						BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
			}
			if (StringUtils.isNotEmpty(info.getLastid())) {
				AbAccrecords lastInfo = abAccrecordsService.get(info.getLastid(), true);
				// 是否补录
				if(abVoucherinfoViceService.isEmitMake()){
					// 挂接授信
					if (AccebillConstant.BRIEF_STATUS_6101.equals(lastInfo.getBriefId())) {
//					abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_REPULSE,userVo);
						//票据华南城授信挂接新需求
						abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_OPTBACK,userVo);
						// 内部授信
						abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_REPULSE, info, userVo);
					} else {
						abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_BACK,userVo);
						abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_DELETE,userVo);
						// 内部授信
						abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_BACK, info, userVo);
						abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_DELETE, info, userVo);
//					abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_DELETE,abEmitBillVo,userVo);
					}
				}else {
					//票据华南城授信挂接新需求
					abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_OPTBACK,userVo);
					// 内部授信
					abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_REPULSE, info, userVo);
				}
				// 挂接担保
				AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBearVoByAid(info.getId());
				abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_REPULSE,abEmitBillVo,userVo);
			}
			// 针对线下电票和纸票进行退票时付款生成的票据打回
			String voucherId = abVoucherinfoViceService.get(info.getVvid()).getVoucherId();
			String source = abVoucherinfoViceService.get(info.getVvid()).getSource();
			if (StringUtils.isNotEmpty(voucherId) && AccebillConstant.AB_ACCEBILL_SOURCE10.equals(source)
					&& AccebillConstant.AB_BILL_SEND_SIGN_N.equals(info.getSendSign())) {
				abNisBillHeadService.updateNisBillhead(voucherId, AccebillConstant.AB_BILL_BUS_STATUS_1,
						info.getRmk(), userVo);
			}
			//挂接保证金  释放保证金
			abFcBondHookService.freedFcBondApply(abVoucherinfoVice,userVo);
//			针对线下电票和纸票进行退票时候对从表数据进行作废处理
			if(!AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())){
				//通过流水的发送标记判断是否线上电票 非在线标记为是的则进行从表设置无线操作
				if(StringUtils.isNotEmpty(info.getVvid())){
					abVoucherinfoViceService.updateValidSign(info.getVvid(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
				}
			}
			
//			//退票确认修改此笔票状态为2，不可用（线上电票除外）
//			AbVoucherinfo vice =abVoucherinfoService.get(info.getVid(),true);
//			BtBankAccDTO recAcc = null;
//			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(vice.getBillFlag())) ){
//				recAcc=abAccrecordsService.getBtBankAccInfo(abVoucherinfoVice.getBankAcc());
//			}
//			if(null!=vice && !vice.getBillFlag().equals(AccebillConstant.AB_BILL_FLAG_ELECT)
//					&& (recAcc!=null && !BtBankAccConstant.CIRCULATIONMODE_1.equals(recAcc.getCirculationMode()))){
//				abEmitBillBackDao.updateViceValidSign(abVoucherinfoVice.getVid());
//			}
		} else {
			int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
			if (reNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			//打回--批量修改--补录后生成收票登记暂存状态的票据--修改流水表status=0暂存
			abAccrecordsService.updateStatusById(info.getId(),userVo,AccebillConstant.AB_BILL_BUS_STATUS0);
		}
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_004, logType, appMsg,
				userVo);
	}
	/**
	 * 获取流水信息
	 *
	 * @param back
	 * @param flag
	 *            true:审批通过 false:审批中
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbEmitBillBackVo back, boolean flag, ICurrentUser userVo) {
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
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			} else {
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			}
			// abVoucherinfoViceService.updateBackBriefId(abVice.getId(), "");//
			// 从表清除打回标记
			// 业务类型
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_004);
			entity.setLastid(id);
			entity.setLastbillcode(ab.getBillStatus());
			// 申请日期
			entity.setBusinessDate(back.getDate());// 业务归属日期
			entity.setApplyDate(new Date());// 申请日期
			if (ab.getBillFlag() == 11) {
//				entity.setRecAcctId(userVo.getLoginName());
//				entity.setRecNm(userVo.getUsername());
				entity.setReqAcctId(abVice.getBankAcc());
			}
			entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
			entity.setBillFlag(ab.getBillFlag());
			entity.setRmk(back.getRmk());// 退票原因
			abList.add(entity);
		}
		return abList;
	}
	/**
	 * 根据流水查询开票信息
	 *
	 * @param aid
	 *            流水ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbEmitBillVo getAbEmitBillVoByAid(String aid) {
		return abEmitBillService.getAbEmitBillVoByAid(aid,false);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLBACK, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abEmitBillBackDao.dataTableApprove(dataTablesInput, businessList);
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
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		approvalPass(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbEmitBillVoByAid(aid);
	}
	/**
	 * 未用退票指令成功打回付款单处理
	 * @param arguments
	 */
	@Override
	@XxlJob("AbEmitBillBackService")
	public void run(String arguments) {
		this.logger.info("---未用退票付款-指令成功把付款单修改为打回开始执行定时任务---");
		List<AbVoucherinfoDTO> infoList = abEmitBillBackDao.queryBillBackSuccessInfo();
		if (infoList == null || infoList.isEmpty()) {
			this.logger.info("---未用退票付款-指令成功把付款单修改为打回，【0】条数据。");
		}else {
			// 数据过滤
			List<String> listId = infoList.stream().map(AbVoucherinfoDTO::getPayId).collect(Collectors.toList());
			List<String> payIdList = nisBillheadService.listPayIdByBill(listId);
			// 过滤掉不符合条件的数据
			infoList = infoList.stream().filter(info -> payIdList.contains(info.getPayId())).collect(Collectors.toList());
			Map<String, Map<String, List<AbVoucherinfoDTO>>> payReturnMap = new HashMap<>();
			//记录不需要返回数据，存在背书未完成信息不返回到付款（背书要么成功、拒签、打回、撤回）
			Map<String,String> notPayinfo=new HashMap<>();
			for(AbVoucherinfoDTO info:infoList){
				if(notPayinfo.containsKey(info.getPayId())){
					continue;
				}
				if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())){
					if(!payReturnMap.containsKey(info.getPayId())){
						Map<String, List<AbVoucherinfoDTO>> listMap=new HashMap<>();
						listMap.put("success",new ArrayList<AbVoucherinfoDTO>());
						payReturnMap.put(info.getPayId(),listMap);
					}else if(!payReturnMap.get(info.getPayId()).containsKey("success")){
						payReturnMap.get(info.getPayId()).put("success",new ArrayList<AbVoucherinfoDTO>());
					}
					payReturnMap.get(info.getPayId()).get("success").add(info);
				}else{
					payReturnMap.remove(info.getPayId());
					notPayinfo.put(info.getPayId(),info.getPayId());
				}
			}
			if (payReturnMap.isEmpty()) {
				this.logger.info("---未用退票付款-指令成功把付款单修改为打回，【0】条数据。");
			} else {
				ICurrentUser userVo = abUserService.getScheduler();
				this.logger.info("---未用退票付款-指令成功把付款单修改为打回，【" + payReturnMap.size() + "】条付款数据。");
				for (String key : payReturnMap.keySet()) {
					for(AbVoucherinfoDTO info:payReturnMap.get(key).get("success")){
						abBillMiddleService.saveBillMiddle(info.getId(), AccebillConstant.BILL_MIDDLE_TYPE_004PAY_BACKSUCCESS, userVo);
						abNisBillHeadService.updateNisBillhead(info.getPayId(), AccebillConstant.AB_BILL_BUS_STATUS_1,
								"未用退票付款-指令成功把付款单修改为打回", userVo);
					}
				}
			}
		}
		this.logger.info("---未用退票付款-指令成功把付款单修改为打回，结束执行定时任务---");
	}
}
