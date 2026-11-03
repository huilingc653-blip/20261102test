package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.dto.AbBillCollectionApplyVoDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillCollectionApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillCollectionApplyVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
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
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @Title: Service
 * @Description: 托收申请
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONAPPLY)
@Service("abBillCollectionApplyService")
@Transactional
public class AbBillCollectionApplyService extends BaseService<AbBillCollectionApplyVo, String>
		implements IAccebillAccbookService, IWorkFlowListener, ITaskService {
	@Autowired
	private AbBillCollectionApplyDao abBillCollectionApplyDao;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	@Autowired
	private AbCalendarService abCalendarService;
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbFbsItemService abFbsItemService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	/**
	 * 托收申请列表查询
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		// run("test");//定时器测试调用
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_COLLECTIONAPPLY_QUERY,input.getCurrentUserId());
		// 银票到期逾期天数
		String day10 = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM).getValue();
		// 商票到期逾期天数
		String day11 = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM).getValue();
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOPAY), "c.bif_code",
				true);
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_COLLECTIONAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillCollectionApplyVo> output = abBillCollectionApplyDao.getDataTablesUnion(input, day10, day11, corpAuthList, notInBifCodeSql);
		List<AbBillCollectionApplyVo> list = output.getData();
		if (!EmptyUtils.isEmptyList(list)) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_COLLECTIONAPPLY_STATUS"
					, "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillCollectionApplyVo collectionApplyVo : list) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(collectionApplyVo.getEndDate());
				if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(collectionApplyVo.getBillType()))) {
					cal.add(Calendar.DATE, Integer.valueOf(day10));// 银票逾期日
				} else {
					cal.add(Calendar.DATE, Integer.valueOf(day11));// 商票逾期日
				}
				Date enddate = cal.getTime();
				if (enddate.getTime() < Calendar.getInstance().getTime().getTime()) {
					// 票据逾期
					collectionApplyVo.setIsOverdue("1");
				} else {
					// 票据未逾期
					collectionApplyVo.setIsOverdue("0");
				}
				// 注入冗余数据
				injectValues(collectionApplyVo, listDictMap);
			}
		}
		List<AbBillCollectionApplyVo> data = output.getData();
		if(CollectionUtil.isEmpty(data)){
			return output;
		}
		List<String> collect = data.stream().map(AbBillCollectionApplyVo::getBankAcc).collect(Collectors.toList());
		Map<String, BtBankAccDTO> billBankAccByBankAccList = abBtBankAccService.getBillBankAccByBankAccList(collect);
		Set<String> corpIdSet = new HashSet<>();
		for (AbBillCollectionApplyVo datum : data) {

			if (billBankAccByBankAccList.containsKey(datum.getBankAcc()) ) {
				BtBankAccDTO btBankAccDTO = billBankAccByBankAccList.get(datum.getBankAcc());
				datum.setBankTypeName(btBankAccDTO.getAccTypeName());
			}
			if(StrUtil.isNotBlank(datum.getApplyCorpId())){
				corpIdSet.add(datum.getApplyCorpId());
			}
			if(StrUtil.isNotBlank(datum.getCorpId())){
				corpIdSet.add(datum.getCorpId());
			}
			if(StrUtil.isNotBlank(datum.getBusCorpId())){
				corpIdSet.add(datum.getBusCorpId());
			}
		}
		if(corpIdSet.size() <= 0){
			return output;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return output;
		}
		Optional.ofNullable(data).ifPresent(listData->{
			for (AbBillCollectionApplyVo bill : listData){
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
		return output;
	}


	/**
	 * 托收申请
	 */
	public void collectionApplyForAotoTask(AbBillCollectionApplyVo abBillCollectionApplyVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillCollectionApplyVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillCollectionApplyVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillCollectionApplyVo.getAids().split(",");
		/**
		 * 新一代票据 sttlmmk
		 * 后台需要校验，若托收申请中的票据含有新一代票据的：需要判断清算方式是否为线上清算，
		 * 若不是，需要终止该笔交易，并给出相应的提示；清算类型后台默认为CT01 全额清算
		 */
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		AbAccrecords abAccrecordsOld = null;// 当前流水
		AbAccrecords abAccrecordsNew = null;// 托收申请流水
		List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
		for (int i = 0; i < abVoucherinfoIds.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i],true);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i],true);
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i],true);

			// 新增托收流水--(逾期)提示付款指令待发送
			abAccrecordsNew = getAccrecords(abBillCollectionApplyVo, abVoucherinfo, abVoucherinfoVice, abAccrecordsOld,
					userVo);
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			abAccrecordsNew.setSttlmmk(AccebillConstant.AB_STTLMMK_Y);
			abAccrecordsNew.setRmk("已结清在线新一代票据到期日当天自动生成提示付款流水");

			// 当托收是单条数据时，则判断单位是否挂资金计划
			if (abVoucherinfoIds.length == 1) {
				if (abVoucherinfoViceService.isBudget(abVoucherinfoVice.getCorpId(), AccebillConstant.AB_BUSTYPE_HINT_PAYER)) {
					abBillCollectionApplyVo.setInsideSing(abAccrecordsNew.getInsideSing());
					abBillCollectionApplyVo.setOrigNo(abVoucherinfo.getOrigNo());
					abBillCollectionApplyVo.setBusinessDate(abAccrecordsNew.getBusinessDate());

					abBillCollectionApplyVo.setRecAcctId(abVoucherinfo.getAcceptoracc());
					abBillCollectionApplyVo.setRecNm(abVoucherinfo.getAcceptor());
					abBillCollectionApplyVo.setRecAcctsvcrName(abVoucherinfo.getAcceptorBankName());
					this.batchEngross(abBillCollectionApplyVo, abVoucherinfoVice, userVo);
					// 预算科目
					abAccrecordsNew.setBudgetCode(abBillCollectionApplyVo.getCollectionItem());
				}
			}

			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			abAccrecordsNew.setOptStatus("19");
			accrecordslist.add(abAccrecordsNew);
			abAccrecordsService.batchUpdate(accrecordslist);

			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			// 票据当前流水改为作废
			int num = abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
		}
	}

	private void injectValues(AbBillCollectionApplyVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
		// 票据状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ABBILL_COLLECTIONAPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 清算模式:SM00：线上清算，SM01：线下清算
		Optional.ofNullable(bill.getSttlmmk()).ifPresent(sttlmmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_STTLMMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sttlmmk)))
					.ifPresent(dict -> bill.setSttlmmkName(dict.getName()));
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
	 * 托收申请
	 */
	public void collectionApply(AbBillCollectionApplyVo abBillCollectionApplyVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillCollectionApplyVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillCollectionApplyVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillCollectionApplyVo.getAids().split(",");
		/**
		 * 新一代票据 sttlmmk
		 * 后台需要校验，若托收申请中的票据含有新一代票据的：需要判断清算方式是否为线上清算，
		 * 若不是，需要终止该笔交易，并给出相应的提示；清算类型后台默认为CT01 全额清算
		 */
		String sttlmmk = abBillCollectionApplyVo.getSttlmmk();
		AbVoucherinfo abVoucherinfo = null;
//		if ( AccebillConstant.AB_STTLMMK_N.equals(sttlmmk)){
//			for (int i = 0; i < abVoucherinfoIds.length; i++) {
//				abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i]);
//				if(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abVoucherinfo.getNewStmIssuofpprInd())){
//					throw new ParameterException(AccebillMessageEnum.NEW_GEN_BILL_NOT_OFFLINE_PAY);
//				}
//			}
//		}
//		abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		AbAccrecords abAccrecordsOld = null;// 当前流水
		AbAccrecords abAccrecordsNew = null;// 托收申请流水
		List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
		boolean isTask = abBillCollectionApplyVo.getTask();// 是否定时任务处理
		for (int i = 0; i < abVoucherinfoIds.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i]);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i]);
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i]);
			// 新增托收流水--(逾期)提示付款指令待发送
			abAccrecordsNew = getAccrecords(abBillCollectionApplyVo, abVoucherinfo, abVoucherinfoVice, abAccrecordsOld,
					userVo);
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			// 当托收是单条数据时，则判断单位是否挂资金计划
			if (abVoucherinfoIds.length == 1) {
				if (abVoucherinfoViceService.isBudget(abVoucherinfoVice.getCorpId(), AccebillConstant.AB_BUSTYPE_HINT_PAYER)) {
					abBillCollectionApplyVo.setInsideSing(abAccrecordsNew.getInsideSing());
					abBillCollectionApplyVo.setOrigNo(abVoucherinfo.getOrigNo());
					abBillCollectionApplyVo.setBusinessDate(abAccrecordsNew.getBusinessDate());
					this.batchEngross(abBillCollectionApplyVo, abVoucherinfoVice, userVo);
					// 预算科目
					abAccrecordsNew.setBudgetCode(abBillCollectionApplyVo.getCollectionItem());
				}
			}
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			accrecordslist.add(abAccrecordsNew);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecordsNew, variables);// 票据流水信息填充到审批对象
			// 票据当前流水改为作废
			int num = abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为10
			num = abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			String corpId = abVoucherinfoVice.getApplyCorpId();
			// 计算票据账户余额
			abBillAccBalService.setBankAccBillBal(abAccrecordsNew.getId(), userVo);
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
			String userId = userVo.getId();
			if (isTask) {
//				abBillNotificationService.sendMsg(abAccrecordsNew, abAccrecordsNew.getBriefId());
				abLogService.saveAbLog(abVoucherinfoViceIds[i], abAccrecordsIds[i], abAccrecordsNew.getBriefId(),
						AccebillConstant.ABLOG_LOGTYPE_TASK, AccebillConstant.LANG_LOG_TASK_SUCC, userVo);
			} else {
				abLogService.saveAbLog(abVoucherinfoViceIds[i], abAccrecordsIds[i],
						AccebillConstant.BRIEF_STATUS_020.equals(abAccrecordsNew.getBriefId())
								? AccebillConstant.BRIEF_STATUS_020 : AccebillConstant.BRIEF_STATUS_021,
						AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
			}
			// 启动工作流
			getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONAPPLY, abAccrecordsNew.getId(),
					variables, corpId,userId);
		}
	}
	/**
	 * ERP托收申请
	 */
	public void collectionApplyERP(AbBillCollectionApplyVo abBillCollectionApplyVo, ICurrentUser userVo) {
		String[] abAccrecordsIds = abBillCollectionApplyVo.getAids().split(",");
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		AbAccrecords abAccrecordsOld = null;// 当前流水
		AbAccrecords abAccrecordsNew = null;// 托收申请流水
		List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
		for (int i = 0; i < abAccrecordsIds.length; i++) {
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i]);
			abVoucherinfo = abVoucherinfoService.get(abAccrecordsOld.getVid());
			abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecordsOld.getVvid());
			// 新增托收流水--(逾期)提示付款指令待发送
			abAccrecordsNew = getAccrecords(abBillCollectionApplyVo, abVoucherinfo, abVoucherinfoVice, abAccrecordsOld,
					userVo);
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			accrecordslist.add(abAccrecordsNew);
			// 票据当前流水改为作废
			int num = abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为11
			num = abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, null, userVo);
//			abBillNotificationService.sendMsg(abAccrecordsNew, abAccrecordsNew.getBriefId());
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoVice.getId());// 从表清除打回标记
			// 计算票据账户余额
			abBillAccBalService.setBankAccBillBal(abAccrecordsNew.getId(), userVo);
			abLogService.saveAbLog(abAccrecordsNew.getVvid(), abAccrecordsNew.getId(), abAccrecordsNew.getBriefId(), AccebillConstant.ABLOG_LOGTYPE_APP,
					AccebillConstant.LANG_LOG_APP_SUCC, userVo);
		}
	}
	/**
	 * 根据参数获取托收申请流水
	 *
	 */
	protected AbAccrecords getAccrecords(AbBillCollectionApplyVo abBillCollectionApplyVo, AbVoucherinfo abVoucherinfo,
										 AbVoucherinfoVice abVoucherinfoVice, AbAccrecords abAccrecordsOld, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		entity.setVid(abAccrecordsOld.getVid());// 票据主表ID
		entity.setVvid(abAccrecordsOld.getVvid());// 票据从表ID
		entity.setLastid(abAccrecordsOld.getId());// 上条流水ID
		if (isOverdue(abVoucherinfo)) {
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_020);// 业务类型
		} else {// 逾期
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_021);// 业务类型
			entity.setRsn(abBillCollectionApplyVo.getIsOverdueRmk());// 逾期原因
		}
		entity.setRmk(abBillCollectionApplyVo.getRmk());// 备注
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abBillCollectionApplyVo.getBusinessDate());// 业务归属日期
		entity.setBillFlag(abVoucherinfo.getBillFlag());
		if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(abVoucherinfo.getBillFlag())) {
			// 纸票设置为线下清算
			entity.setSttlmmk(AccebillConstant.AB_STTLMMK_N);
		} else {
			// 清算方式,为空则设置为线下
			entity.setSttlmmk(AccebillConstant.AB_STTLMMK_Y.equals(abBillCollectionApplyVo.getSttlmmk())
					? AccebillConstant.AB_STTLMMK_Y : AccebillConstant.AB_STTLMMK_N);
		}
		entity.setReqRole(abAccrecordsService.getRole(abVoucherinfoVice.getCorpId(), abVoucherinfoVice.getBankAcc(),
				abVoucherinfoVice.getBankName()));// 业务申请人类别
		String corpName =  abCorpService.get(abVoucherinfoVice.getCorpId()).getName();
		entity.setReqNm(corpName);// 业务申请人
		// entity.setReqCmonid(abBillCollectionApplyVo.getPayer());//业务申请人组织机构代码
		entity.setReqAcctId(abVoucherinfoVice.getBankAcc());// 业务申请人账号
		entity.setReqAcctsvcr(abVoucherinfoVice.getBankCode());// 业务申请人开户行号
		// entity.setReqAcctsvcrAg(abBillCollectionApplyVo.getPayerbankno());//业务申请人承接行号
		entity.setRecRole(
				abAccrecordsService.getRole(abVoucherinfo.getAcceptoracc(), abVoucherinfo.getAcceptoracc(), null));// 业务接收人类别--(承兑人)
		entity.setRecNm(abVoucherinfo.getAcceptor());// 业务接收人名称
		// entity.setRecCmonid(abVoucherinfo.getA);//业务接收人组织机构代码
		entity.setRecAcctId(abVoucherinfo.getAcceptoracc());// 业务接收人账号
		entity.setRecAcctsvcr(abVoucherinfo.getAcceptorbankno());// 业务接收人开户行号
		// entity.setRecAcctsvcrAg(abBillCollectionApplyVo.getPayee());//业务接收人承接行号
		if (userVo != null) {
			entity.setCreateName(userVo.getUsername());// 创建人名称
			entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		}
		entity.setCreateDate(new Date());// 创建日期
		return entity;
	}
	/**
	 * 判断票据是否逾期 return boolean
	 */
	protected boolean isOverdue(AbVoucherinfo abVoucherinfo) {
		// 银票到期逾期天数
		String day10 = abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM).getValue();
		// 商票到期逾期天数
		String day11 = abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM).getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(abVoucherinfo.getEndDate());
		if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abVoucherinfo.getBillType()))) {
			cal.add(Calendar.DATE, Integer.valueOf(day10));// 银票逾期日
		} else {
			cal.add(Calendar.DATE, Integer.valueOf(day11));// 商票逾期日
		}
		Date enddate = cal.getTime();
		return enddate.getTime() >= Calendar.getInstance().getTime().getTime();
	}
	@Override
	public BaseDao<AbBillCollectionApplyVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 批量审批
	 *
	 * @param aid
	 * @param appMsg
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
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), abAccrecords.getBriefId(), logType,
				msg, userVo);
		//给对线下票据进行票据状态已结清标记
		if(abAccrecords!=null
				&& AccebillConstant.AB_BILL_OPT_STATUS19.equals(abAccrecords.getOptStatus())) {
			AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
			if(abVoucherinfo == null){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
			}
			//新票
			if("1".equals(abVoucherinfo.getNewStmIssuofpprInd())){
				abVoucherinfoService.updateBillStatus(abAccrecords.getVid(),AccebillConstant.NEWBILL_STATUS_CS06,userVo,AccebillConstant.CIRCULATE_TF0501);
			}else {
				abVoucherinfoService.updateBillStatus(abAccrecords.getVid(),AccebillConstant.AB_BILL_STATUS_000000,userVo,null);
			}
		}
		// 判断流水的资金计划科目是否为空，空则没挂资金计划
		if (!EmptyUtils.isBlank(abAccrecords.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(abAccrecords.getVvid(), true,userVo);
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
		// 托收申请流水删除 status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 上一条流水VALID_SIGN=1
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
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(),
				AccebillConstant.BRIEF_STATUS_020.equals(abAccrecords.getBriefId())
						? AccebillConstant.BRIEF_STATUS_020 : AccebillConstant.BRIEF_STATUS_021,
				abAccrecords.getId());
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		// 判断流水的资金计划科目是否为空，空则没挂资金计划，不为空则删除计划
		if (!EmptyUtils.isBlank(abAccrecords.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(abAccrecords.getVvid(), false,userVo);
		}
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), abAccrecords.getBriefId(), logType,
				msg, userVo);
	}
	/**
	 * 占用预算
	 *
	 * @param entity 票据托收
	 * @param abVoucherinfoVice 从表数据
	 * @param userVo 操作用户
	 */
	private void batchEngross(AbBillCollectionApplyVo entity, AbVoucherinfoVice abVoucherinfoVice, ICurrentUser userVo) {
		String itemCodeStr = "";
		// 币别取人民币
		String curId = abVoucherinfoViceService.getCurIdByCNY();
		List<FbsBudgetProcessDTO> budgetlist = entity.getBudgetList();
		if (budgetlist != null && !budgetlist.isEmpty()) {
			for (FbsBudgetProcessDTO budget : budgetlist) {
				if (!StringUtils.isEmpty(itemCodeStr)) {
					itemCodeStr += ",";
				}
				itemCodeStr += budget.getItemCode();
				if(AccebillConstant.AB_INSIDE_SING_Y.equals(entity.getInsideSing())){
					budget.setInnerSign ("1");
				}else{
					budget.setInnerSign ("0");
				}
				budget.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
				budget.setBillId(abVoucherinfoVice.getId());// 业务单据号
				budget.setBillNo(entity.getOrigNo());//票据号码
				//资金计划背书添加折合本位币金额与利率
				budget.setProcessDate(entity.getBusinessDate());// 执行日期
				budget.setCorpId(abVoucherinfoVice.getCorpId());// 单位id
				budget.setCurId(curId);
				budget.setBusinessName(BudgetConstant.BUSINESS_NAME_COLLECTION);// 执行业务名称
				budget.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
				budget.setCreateBy(userVo.getLoginName());
				budget.setCreateDate(DateUtils.getDate());
			}
			// 把选择的资金计划科目写入流水表
			entity.setCollectionItem(itemCodeStr);
			// 修改时，先删除之前的计划执行数据再保存
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_COLLECTION,
					BudgetConstant.PROCESS_SYSNAME_AB, abVoucherinfoVice.getId());
			// 写入资金计划信息
			if (budgetlist != null && !budgetlist.isEmpty()) {
				this.abFbsBudgetProcessService.writeBudgetProcess(budgetlist,userVo);
			}
		}
	}
	/**
	 * 占用预算 审批通过
	 *
	 * @param vvid
	 *            从表Id
	 * @param isPassOK
	 *            审批状态
	 */
	public void approvalBudget(String vvid, boolean isPassOK,ICurrentUser userVo) {
		if (isPassOK) {
			// 修改资金计划为占用状态
			this.abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_COLLECTION,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid, BudgetConstant.STATUE_CHECK,userVo);
		} else {
			// 删除资金计划
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_COLLECTION,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid);
		}
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
		WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLCOLLECTIONAPPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		// 银票到期逾期天数
		String day10 = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM).getValue();
		// 商票到期逾期天数
		String day11 = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM).getValue();
		DataTablesOutput<AbBillCollectionApplyVo> dp = abBillCollectionApplyDao.dataTableApprove(dataTablesInput, day10, day11, businessList);
		List<AbBillCollectionApplyVo> list = dp.getData();
		if (!EmptyUtils.isEmptyList(list)) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_COLLECTIONAPPLY_STATUS"
					, "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillCollectionApplyVo collectionApplyVo : list) {
				injectValues(collectionApplyVo, listDictMap);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 票据托收申请审批详情页
	 *
	 * @param aid
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillCollectionApplyVo getAbBillCollectionApplyVoById(String aid, Model model) {
		AbBillCollectionApplyVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		if (abAccrecords != null) {
			vo = new AbBillCollectionApplyVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setAid(aid);
			/** 判断是否逾期 */
			int day = 0;
			if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(vo.getBillType()))) {
				day = Integer.parseInt(abConfigService
						.getBillConfig(AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM)
						.getValue());// 银票到期逾期天数
			} else {
				day = Integer.parseInt(abConfigService
						.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM)
						.getValue());// 商票到期逾期天数
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vo.getEndDate());// 票据到期日
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);// 票据到期日+票据到期付款天数
			// =
			// 票据逾期日
			if (calendar.getTime().getTime() < Calendar.getInstance().getTime().getTime()) {
				vo.setIsOverdue("1");// 逾期
				vo.setBriefId(AccebillConstant.AB_BUSTYPE_HINT_PAYER);
			} else {
				vo.setIsOverdue("0");// 未逾期
			}
			/** 判断是否逾期结束 */
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
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_COLLECTIONAPPLY_STATUS"
					, "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	/**
	 * 获取票据到期可以发起托收申请或逾期托收申请的票据
	 */
	@Transactional(readOnly = true)
	public List<AbBillCollectionApplyVoDTO> getAbBillCollectionApplyVoDTOList() {
		// 银票到期逾期天数
		String day10 = abConfigService
				.getBillConfig( AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM).getValue();
		// 商票到期逾期天数
		String day11 = abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM).getValue();
		// 电票自动托收申请提前天数
		int autoPayDayNum = Integer.valueOf(abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOPAYDAYNUM).getValue());
		// 第三方票据自动托收申请
		String isThirdBillAutoCollect = abConfigService.getConfig(SysConfigConstant.Module.ACCEBILL, SysConfigConstant.IS_THIRD_BILL_AUTO_COLLECT).getValue();
		if (StringUtils.isEmpty(isThirdBillAutoCollect)) {
			isThirdBillAutoCollect = "1";
		}
		//提前天数根据工作日来判断
		//1、先获取当前时间加上提前天数获取对应的工作日
		Date today=DateUtils.getDateYMD();
		Date lastWorkDay=abCalendarService.getWorkDay(today,autoPayDayNum);
		//2、根据对应提前几天的工作日减去当前时间得出具体提前多少天的天数
		if(lastWorkDay!=null && lastWorkDay.getTime()>today.getTime() && autoPayDayNum>0){
			autoPayDayNum=DateUtils.getOffsetDays(today,lastWorkDay);
		}
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOPAY), "c.bif_code",
				true);
		List<AbBillCollectionApplyVoDTO> dtoList = abBillCollectionApplyDao.getAbBillCollectionApplyVoDTOList(day10, day11, autoPayDayNum, notInBifCodeSql, isThirdBillAutoCollect);
		if (ObjectUtil.isNotEmpty(dtoList)){
			List<String> bankAccList = dtoList.stream().map(AbBillCollectionApplyVoDTO::getBankAcc).collect(Collectors.toList());
			// 过滤掉 electric_bill='1' and circulation_mode='1'的账户数据
			List<Map<String, Object>> bankModelList = abBtBankAccService.getBankModelByBankAcc(bankAccList);
			dtoList = dtoList.stream().filter(source -> bankModelList.stream()
							.anyMatch(target -> ObjectUtil.equal(source.getBankAcc(), Convert.toStr(target.get("bankAcc")))))
					.collect(Collectors.toList());
		}
		return dtoList;
	}
	/**
	 * 根据单位id和票据号查询托收申请
	 */
	public List<AbBillCollectionApplyVo> getAbBillCollectionApplyVoDTOList(String corpId, String origNo) {
		// 银票到期逾期天数
		String day10 = abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_EBANKOVERDAYNUM).getValue();
		// 商票到期逾期天数
		String day11 = abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ETRADEOVERDAYNUM).getValue();
		String notInBifCodeSql = SqlUtil.getNotinSql(
				abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISEAUTOPAY), "c.bif_code",
				true);
		return abBillCollectionApplyDao.getAbBillCollectionApplyVoDTOList(corpId, origNo, day10, day11, notInBifCodeSql);
	}
	/**
	 * 发起托收申请接口(定时任务调用)
	 *
	 * @param aid
	 *            票据流水id
	 */
	public void saveAbBillCollectionApply(String aid, ICurrentUser userVo) {
		AbBillCollectionApplyVo abBillCollectionApplyVo = new AbBillCollectionApplyVo();
		if (StringUtils.isNotEmpty(aid)) {
			AbAccrecords abAccrecords = abAccrecordsService.get(aid);
			if (abAccrecords != null && StringUtils.isNotEmpty(abAccrecords.getVid())
					&& StringUtils.isNotEmpty(abAccrecords.getVvid())) {
				abBillCollectionApplyVo.setRmk(AccebillConstant.AB_ACCEBILL_STTLMMK);// 托收说明
				abBillCollectionApplyVo.setIsOverdueRmk(AccebillConstant.AB_ACCEBILL_STTLMMK);// 逾期托收说明
				abBillCollectionApplyVo.setBusinessDate(new Date());// 业务归属日期
				abBillCollectionApplyVo.setSttlmmk(abVoucherinfoViceService.isEAutoPaySttlmmk(abAccrecords.getVid()));// 清算方式
				// SM00线上清算
				// SM01线下清算
				abBillCollectionApplyVo.setVids(abAccrecords.getVid());
				abBillCollectionApplyVo.setVvids(abAccrecords.getVvid());
				abBillCollectionApplyVo.setAids(aid);
				abBillCollectionApplyVo.setTask(true);
				this.collectionApply(abBillCollectionApplyVo, userVo);
			} else {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
			}
		} else {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
	}
	/**
	 * 托收申请自动业务处理
	 */
	private void autoBillCollectionApply(ICurrentUser userVo) {
		List<AbBillCollectionApplyVoDTO> autoInfoList = getAbBillCollectionApplyVoDTOList();
		if (autoInfoList == null || autoInfoList.isEmpty()) {
			this.logger.info("---自动托收申请，【0】条数据。");
			return;
		}
		this.logger.info("---自动托收申请，【" + autoInfoList.size() + "】条数据。");
		for (AbBillCollectionApplyVoDTO collectionApplyInfo : autoInfoList) {
			saveAbBillCollectionApply(collectionApplyInfo.getAid(), userVo);
			abBillMiddleService.saveBillMiddle(collectionApplyInfo.getAid(), AccebillConstant.BILL_MIDDLE_TYPE_020,userVo);
		}
	}
	/**
	 * 电票自动托收申请定时任务触发执行
	 */
	@Override
	@XxlJob("AbBillCollectionApplyService")
	public void run(String arguments) {
		try {
			if (abVoucherinfoViceService.isEAutoPay()) {
				ICurrentUser userVo= abUserService.getScheduler();
				this.logger.info("---启用自动托收申请，开始执行定时任务---");
				autoBillCollectionApply(userVo);
				this.logger.info("---启用自动托收申请，结束执行定时任务---");
			} else {
				this.logger.info("---系统未启动自动托收申请参数，不执行定时器---");
			}
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillCollectionApplyVoById(aid, null);
	}
}
