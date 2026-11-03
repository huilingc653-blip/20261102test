package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.dao.AbBillPledgeApplyDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo;
import com.bytter.bill.domain.bill.vo.AbBillPledgeRelieveVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 票据质押申请
 * @author onlineGenerator
 * @date 2017-07-07
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGEAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_PLEDGEAPPLY)
@Service("abBillPledgeApplyService")
@Transactional
public class AbBillPledgeApplyService extends BaseService<AbBillPledgeApplyVo, String>
		implements IAccebillAccbookService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillPledgeApplyVo> {
	@Autowired
	private AbBillPledgeApplyDao abBillPledgeApplyDao;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
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
	@Autowired
	private AbVoucherinfoDao abVoucherinfoDao;
	@Autowired
	private AbVoucherinfoViceDao abVoucherinfoViceDao;
	@Autowired
	private AbAccrecordsDao abAccrecordsDao;
	@Autowired
	private BillTicketSplittingUtils billTicketSplittingUtils;
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	public BaseDao<AbBillPledgeApplyVo, String> getEntityDao() {
		return abBillPledgeApplyDao;
	}
	/**
	 * 列表显示应收票据质押申请信息
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillPledgeApply(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLEDGEAPPLY_QUERY,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_PLEDGEAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillPledgeApplyVo> dataTablesOutput = abBillPledgeApplyDao.listAbBillPledgeApply(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGEAPPLY_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeApplyVo bill : listData) {
				injectValues(bill, listDictMap);
			}
		});
		List<AbBillPledgeApplyVo> data = dataTablesOutput.getData();
		List<String> collect = data.stream().map(AbBillPledgeApplyVo::getBankAcc).collect(Collectors.toList());
		Map<String, BtBankAccDTO> billBankAccByBankAccList = abBtBankAccService.getBillBankAccByBankAccList(collect);
		Set<String> corpIdSet = new HashSet<>();
		for (AbBillPledgeApplyVo datum : data) {

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
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillPledgeApplyVo bill : listData){
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
		return dataTablesOutput;
	}
	private void injectValues(AbBillPledgeApplyVo bill,Map<String, Map<String, DictDTO>> listDictMap){
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_PLEDGEAPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 票据来源
		Optional.ofNullable(bill.getBillSource()).ifPresent(billSource -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SOURCE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billSource)))
					.ifPresent(dict -> bill.setBillSourceName(dict.getName()));
		});
		// 不得转让标记
		Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
					.ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
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

		//业务标记
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
	}
	/**
	 * 根据单位id查询银行账号信息
	 * 
	 * @param corpId
	 *            单位id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtBankAccDTO> getBankDataByCorpId(String corpId) {
		BtBankAccDTO params = new BtBankAccDTO();
		params.setCorpId(corpId);
		return abBtBankAccService.getBankAccList(params);
	}
	/**
	 * 批量质押
	 * 
	 * @return
	 */
	public boolean batchApply(AbBillPledgeApplyVo abBillPledgeApplyVo, String opType, ICurrentUser userVo) {
		boolean flag = true;
		String[] oldAccIdArr = abBillPledgeApplyVo.getAccId().split(",");
		//新一代票据拆票功能
		Map<String, String> stringStringMap = billTicketSplittingUtils.ticketSplitting(abBillPledgeApplyVo.getVouId(), abBillPledgeApplyVo.getViceId()
				, abBillPledgeApplyVo.getAccId() , abBillPledgeApplyVo.getTransMoneys(),abBillPledgeApplyVo.getSbbllRngStrtSnSplit(),abBillPledgeApplyVo.getSbbllRngEndSnSplit(),userVo);
		//这里处理的逻辑是：因为票据进行拆分，拆分后一张票变成两张票，所以以前的主表id、从表id、流水表id都已经发生了变化，需要重新赋值
		abBillPledgeApplyVo.setVouId(stringStringMap.get("infoIds"));
		abBillPledgeApplyVo.setViceId(stringStringMap.get("viceIds"));
		abBillPledgeApplyVo.setAccId(stringStringMap.get("accIds"));
		abBillPledgeApplyVo.setTransMoneys(stringStringMap.get("transMoneys"));
		String[] vouIds = abBillPledgeApplyVo.getVouId().split(",");//票据主表id
		String[] viceIds = abBillPledgeApplyVo.getViceId().split(",");//票据从表id
		String[] accIds = abBillPledgeApplyVo.getAccId().split(",");//流水表id
		// 批量修改票据从表信息(修改处理标记为处理中)
		int num = abVoucherinfoViceService.batchSubmit(viceIds, userVo);
		if (num != vouIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(abBillPledgeApplyVo,true,oldAccIdArr);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != accIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 挂票据账户余额
		for (AbAccrecords abs : abList) {
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
		}
		// 日志类型
		String logType = "";
		String msg = "";
		// 提交
		if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
			logType = AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT;
			msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
		}
		// 导入
		if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_IMPORTS).equals(opType)) {
			logType = AccebillConstant.ABLOG_LOGTYPE_IMPORT;
			msg = AccebillConstant.LANG_LOG_IMPORT_SUCC;
		}
		// 批量保存日志
		flag = abLogService.batchSaveAbLog(viceIds, accIds, AccebillConstant.BRIEF_STATUS_018, logType, msg, userVo);
		// 挂审批流程
		for (int i = 0; i < num; i++) {
			abBillNotificationService.completeMsg(abList.get(i), userVo);
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());
			AbAccrecordsVO variables = new AbAccrecordsVO();
			AbVoucherinfo vouInfo = abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo = abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGEAPPLY, abList.get(i).getId(), variables,
					vouViceInfo.getApplyCorpId(), userVo.getId());
		}
		return flag;
	}
	private String getValueForList(List<String> reqParam){
		StringBuilder strB = new StringBuilder();
		for (String s : reqParam) {
			strB.append(s).append(",");
		}
		String result = new String(strB);
		return result.substring(0, result.length() -1);
	}
	/**
	 * ERP质押申请
	 *
	 * @return
	 */
	public boolean pledgeApplyERP(AbBillPledgeApplyVo abBillPledgeApplyVo, ICurrentUser userVo) {
		String[] accIds = abBillPledgeApplyVo.getAccId().split(",");
		//新一代票据拆票功能
		Map<String, String> stringStringMap = billTicketSplittingUtils.ticketSplitting(abBillPledgeApplyVo.getVouId(), abBillPledgeApplyVo.getViceId()
				, abBillPledgeApplyVo.getAccId() , abBillPledgeApplyVo.getTransMoneys(), userVo);
		//这里处理的逻辑是：因为票据进行拆分，拆分后一张票变成两张票，所以以前的主表id、从表id、流水表id都已经发生了变化，需要重新赋值
		abBillPledgeApplyVo.setVouId(stringStringMap.get("infoIds"));
		abBillPledgeApplyVo.setViceId(stringStringMap.get("viceIds"));
		abBillPledgeApplyVo.setAccId(stringStringMap.get("accIds"));
		abBillPledgeApplyVo.setTransMoneys(stringStringMap.get("transMoneys"));
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(abBillPledgeApplyVo,false, accIds);
		accIds = abBillPledgeApplyVo.getAccId().split(",");
		int num  = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != accIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		for (AbAccrecords abs : abList) {
			//记账暂时不处理
			//内部质押处理
			batchStorageByAccId(abs.getId(), userVo);
			//修改上一笔流水状态为无效
			int oldVSNum = abAccrecordsService.updatAppPass(abs.getLastid(), true, userVo);
			if (oldVSNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			//修改票据从表信息为已完成
			int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(abs.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
					userVo);
			// 挂票据账户余额
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
			abLogService.saveAbLog(abs.getVvid(), abs.getId(), abs.getBriefId(),AccebillConstant.ABLOG_LOGTYPE_APP, AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
			abBillNotificationService.sendMsg(abs.getId());
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
		}
		return true;
	}
	/**
	 * 调用审批
	 * 
	 * @param buId
	 * @param isPassOK
	 * @param appMsg
	 */
	public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(buId, true);
		if (info == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
        if (isPassOK) {

			//线下票据
			if(AccebillConstant.AB_BILL_SEND_SIGN_N.equals(info.getSendSign())
					&& AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())
					&& !"2".equals(info.getCopyFlag())){
				//新票据拆分请求确认处理
				NewDraftDealMethod.newDraftSplitAck(billBankDao,info.getId());

				String billStatusOld = AccebillConstant.AB_BILL_STATUS_180006;
				String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
				String ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0304;
				//设置离线票据的票据状态和流通标志，并返回从票数据
				AbAccebillUtils.setOffLineVoucherViceBillStatus(abVoucherinfoService,info.getVid(),info.getVvid()
						,info.getSendSign(),abVoucherinfoViceService,billStatusOld, billStatusNew, ebillBlCrclIndc);
			}
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
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS95;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		
		batchStorageByAccId(info.getId(), userVo);
		int oldVSNum = abAccrecordsService.updatAppPass(info.getLastid(), true, userVo);
		if (oldVSNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}

		//批量拆分新业务批量处理
		if("2".equals(info.getCopyFlag())){
			billTicketSplittingUtils.splitConsentOp(info);
		}
		// 挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		
		int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		if (reNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 收票打回挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
		AbAccrecords oldAcc = abAccrecordsService.get(info.getId());
		if("1".equals(oldAcc.getSplitFlg())){
			//批量付票业务进行处理
			if("2".equals(info.getCopyFlag())){
				billTicketSplittingUtils.splitRepulseOp(info);
			}else {
				//新票据拆分后打回处理
				NewDraftDealMethod.draftMerge("1", info.getVid(), billBankDao, billAccrecordsDao);
			}
		}
	}
	
	/**
	 * 批量质押申请审批通过完成后对内部自开的票据做入库处理 票据来源为质押
	 * 
	 * @return
	 */
	public boolean batchStorageByAccId(String accId, ICurrentUser userVo) {
		AbVoucherinfo abVoucherinfo = null;
		AbAccrecords abAccrecords = null;
		AbVoucherinfoVice abVoucherinfoViceOld = null;// 票据从表信息
		AbVoucherinfoVice abVoucherinfoViceNew = null;// 待入库票据从表信息
		AbAccrecords abAccrecordsNew = null;// 待入库流水表信息
		AbBillPledgeApplyVo abBillPledgeApplyVo = null;
		BtBankAccDTO bankAccDTO = null;
		abAccrecords = abAccrecordsService.get(accId);// 票据流水
		abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());// 票据主信息
		// 当是内部质押时， 批量质押申请完成后做入库处理 票据来源为质押
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abAccrecords.getInsideSing()))) {
			abBillPledgeApplyVo = new AbBillPledgeApplyVo();
			// abAccrecordsOld =
			// abAccrecordsService.get(abAccrecords.getLastid());// 上一笔票据流水
			abVoucherinfoViceOld = abVoucherinfoViceService.get(abAccrecords.getVvid());// 票据从表信息
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))){
				bankAccDTO = getBtBankAccDTO(abAccrecords.getRecAcctId());
				if (bankAccDTO == null) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PLEDGE_BANKACC_ISNOTEXIST);
				}
			}
			if (bankAccDTO==null||!(BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccDTO.getCirculationMode())
					&& AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag())))) {
				if (bankAccDTO != null) {
					abBillPledgeApplyVo.setApplyCorpId(bankAccDTO.getCorpId());
					abBillPledgeApplyVo.setBusCorpId(bankAccDTO.getCorpId());
					abBillPledgeApplyVo.setCorpId(bankAccDTO.getCorpId());
					abBillPledgeApplyVo.setPledgeeName(bankAccDTO.getAccName());
					abBillPledgeApplyVo.setPledgeeBankName(bankAccDTO.getBankName());
				}else{
					CorpDTO corpInfo=null;
//					保证人地址 背书时候保存对内背书的单位
					if(StringUtils.isNotEmpty(abAccrecords.getAdr())){
						corpInfo=abCorpService.get(abAccrecords.getAdr());
					}
					if(corpInfo==null){
						corpInfo=abAccrecordsService.getCorpInfo(abAccrecords.getRecAcctId(), abAccrecords.getRecNm());
					}
					if (corpInfo != null) {
						abBillPledgeApplyVo.setApplyCorpId(corpInfo.getId());
						abBillPledgeApplyVo.setBusCorpId(corpInfo.getId());
						abBillPledgeApplyVo.setCorpId(corpInfo.getId());
						abBillPledgeApplyVo.setPledgeeName(corpInfo.getName());
					}
				}
				abBillPledgeApplyVo.setPledgeeAcc(abAccrecords.getRecAcctId());
				abBillPledgeApplyVo.setPledgeeBankAcc(abAccrecords.getRecAcctsvcr());
				abBillPledgeApplyVo.setInteriorApply(String.valueOf(abAccrecords.getInsideSing()));
				abBillPledgeApplyVo.setEnterStock(AccebillConstant.AB_ENTER_STOCK_CORP);
				abBillPledgeApplyVo.setBusinessDate(abAccrecords.getBusinessDate());
				abBillPledgeApplyVo.setRmk(abAccrecords.getRmk());
				abBillPledgeApplyVo.setCnAccrecords(abAccrecords.getReqNm());
				abVoucherinfoViceNew = getAbVoucherinfoVice(abBillPledgeApplyVo, abVoucherinfo);
				// 票据从表新增数据
				abVoucherinfoViceService.saveVoucherinfoVice(abVoucherinfoViceNew, userVo);
				// 票据流水新增
				abAccrecordsNew = getAbAccrecordsNew(abBillPledgeApplyVo, abVoucherinfo, abVoucherinfoViceNew,
						abVoucherinfoViceOld, abAccrecords);
				abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
				abBillNotificationService.sendMsg(abAccrecordsNew);
				abLogService.saveAbLog(abVoucherinfoViceNew.getId(), abAccrecordsNew.getId(),
						AccebillConstant.BRIEF_STATUS_6001, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
						AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
			}
		}
		return true;
	}
	/**
	 * 根据银行账号获取银行账户信息
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccDTO(String bankAcc) {
//		BtBankAccDTO bankAccDTO = null;
//		BtBankAccDTO params = new BtBankAccDTO();
//		params.setBankAcc(bankAcc);
//		List<BtBankAccDTO> bankaccInfoList = bankAccService.getBankAccList(params);
//		if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
//			bankAccDTO = bankaccInfoList.get(0);
//		} /*
//			 * else{ throw new BusinessException(AccebillMessageEnum.
//			 * ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL); }
//			 */
		return abAccrecordsService.getBtBankAccInfo(bankAcc);
	}
	/**
	 * 根据票据质押申请vo和票据主表生成从表入库数据
	 */
	protected AbVoucherinfoVice getAbVoucherinfoVice(AbBillPledgeApplyVo abBillPledgeApplyVo,
			AbVoucherinfo abVoucherinfo) {
		AbVoucherinfoVice abVoucherinfoVice = new AbVoucherinfoVice();
		abVoucherinfoVice.setVid(abVoucherinfo.getId());
		abVoucherinfoVice.setApplyCorpId(abBillPledgeApplyVo.getApplyCorpId());
		abVoucherinfoVice.setBusCorpId(abBillPledgeApplyVo.getBusCorpId());
		abVoucherinfoVice.setCorpId(abBillPledgeApplyVo.getCorpId());// 持票单位(质权人变为持票单位)
		abVoucherinfoVice.setBankAcc(abBillPledgeApplyVo.getPledgeeAcc());// 持票单位账号
		abVoucherinfoVice.setBankCode(abBillPledgeApplyVo.getPledgeeBankAcc());// 持票单位银行行号
		abVoucherinfoVice.setBankName(abBillPledgeApplyVo.getPledgeeBankName());// 持票单位银行名称
		abVoucherinfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);// 应收票据
		abVoucherinfoVice.setEnterStock(abBillPledgeApplyVo.getEnterStock());// 所属库存:10单位库存11中心代管12银行托管
		abVoucherinfoVice.setBillSource(AccebillConstant.AB_BILL_SOURCE_PLEDGE);// 票据来源：10
																				// 收票；11
																				// 背书；12
																				// 质押
		abVoucherinfoVice.setCnAccrecords(abBillPledgeApplyVo.getCnAccrecords());
		abVoucherinfoVice.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));// 内部业务标志：0
																							// 否；1
																							// 是
		abVoucherinfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		abVoucherinfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
		abVoucherinfoVice.setRmk(abBillPledgeApplyVo.getRmk());// 备注
		abVoucherinfoVice.setIsSetCreateBy(false);
		return abVoucherinfoVice;
	}
	/**
	 * 根据票据质押申请vo,票据主表信息和从表信息，流水信息生成流水入库数据
	 */
	protected AbAccrecords getAbAccrecordsNew(AbBillPledgeApplyVo abBillPledgeApplyVo, AbVoucherinfo abVoucherinfo,
			AbVoucherinfoVice abVoucherinfoViceNew, AbVoucherinfoVice abVoucherinfoViceOld, AbAccrecords abAccrecords) {
		AbAccrecords abAccrecordsNew = new AbAccrecords();
		abAccrecordsNew.setVid(abVoucherinfo.getId());// 票据主表ID
		abAccrecordsNew.setVvid(abVoucherinfoViceNew.getId());// 票据从表ID
		abAccrecordsNew.setLastid(abAccrecords.getId());// 保存上一笔流水ID
		abAccrecordsNew.setLastbillcode(abVoucherinfo.getBillStatus());// 保存上一笔业务类型
		abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6001);
		// abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		// abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
		abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
		abAccrecordsNew.setInsideSing(abVoucherinfoViceNew.getInsideSing());// 内部业务标志
		abAccrecordsNew.setCnAccrecords(abBillPledgeApplyVo.getCnAccrecords());// 前手
		abAccrecordsNew.setBankendrsmtmk(abVoucherinfo.getBanendrsmtmk());// 不得转让标记：EM00
																			// 可再转让；EM01
																			// 不得转让
		abAccrecordsNew.setApplyDate(new Date());// 申请日期
		abAccrecordsNew.setBusinessDate(abBillPledgeApplyVo.getBusinessDate());// 业务办理日期
		abAccrecordsNew.setRmk(abBillPledgeApplyVo.getRmk());
		abAccrecordsNew.setBillFlag(abVoucherinfo.getBillFlag());// 流通介质
		abAccrecordsNew.setReqNm(abAccrecords.getRecNm());// 业务申请人账号
		abAccrecordsNew.setReqAcctId(abAccrecords.getRecAcctId());// 业务申请人账号
		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());
		
		abAccrecordsNew.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
		abAccrecordsNew.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
//		abAccrecordsNew.setReqNm(abAccrecords.getReqNm());// 业务申请人
//		abAccrecordsNew.setReqAcctId(abAccrecords.getReqAcctId());// 业务申请人账号
//		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务申请人开户行号
//		
//		abAccrecordsNew.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
//		abAccrecordsNew.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
//		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
		
		abAccrecordsNew.setBillSource(AccebillConstant.AB_BILL_SOURCE_PLEDGE);// 票据来源：10
																				// 收票；11
																				// 背书；12
																				// 质押
		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);
		return abAccrecordsNew;
	}
	/**
	 * 新增流水赋值封装
	 * 
	 * @param abBillPledgeApplyVo
	 *            票据质押VO
	 * @param flag  true 未完成  false 已完成
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbBillPledgeApplyVo abBillPledgeApplyVo, boolean flag,String[] OldAccIdArr) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		String[] accIds = abBillPledgeApplyVo.getAccId().split(",");
		for (int i = 0; i < accIds.length; i++) {

			String id = accIds[i];

			// 获取流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(id,true);
			// 获取当前票据主，从表信息
			AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(),true);
			AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(),true);

			// 新的流水数据
			AbAccrecords entity = new AbAccrecords();

			if("2".equals(abAccrecords.getCopyFlag())){
				entity.setSplitFlg(abAccrecords.getSplitFlg());
				entity.setCopyFlag(abAccrecords.getCopyFlag());
				entity.setOldAccId(abAccrecords.getOldAccId());
			} else {
				//oldLastAccIdArr[i].equals(accIdArrays[i]) 为true，说明为不拆分的票据（可能为旧票或者不拆分新票）
				entity.setSplitFlg(OldAccIdArr[i].equals(id) ? "0" : "1");
				//拆分交易的话设置此字段为未拆分之前的id
				entity.setOldAccId("1".equals(entity.getSplitFlg()) ? OldAccIdArr[i] : null);
			}

			entity.setVid(ab.getId());// 票据主表ID
			entity.setVvid(abVice.getId());// 票据从表ID
			entity.setLastid(abAccrecords.getId());// 保存上一笔流水ID
			entity.setLastbillcode(ab.getBillStatus());// 保存上一笔业务类型
			if (flag) {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
				// 0未完成；1可用；2已结束
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态：-2已删除；-1打回；0
				// 暂存；10提交；11-94审批中；95
				// 已完成
			} else {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			}
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_018);// 流水业务类型018
																	// 应收质押申请
			entity.setInsideSing(Integer.parseInt(abBillPledgeApplyVo.getInteriorApply()));// 内部业务标志
			entity.setBankendrsmtmk(ab.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01
															// 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(abBillPledgeApplyVo.getBusinessDate());// 业务办理日期
			entity.setRmk(abBillPledgeApplyVo.getRmk());// 备注
			entity.setBillFlag(ab.getBillFlag());
			// .......业务申请人...，业务接收人....
			// entity.setReqRole(reqRole);//业务申请人类别
			// entity.setReqNm(abEmitBillVo.getPayer());//业务申请人
			// entity.setReqCmonid(abEmitBillVo.getPayer());//业务申请人组织机构代码
			entity.setReqAcctId(abVice.getBankAcc());// 业务申请人账号
			// entity.setReqAcctsvcr(abEmitBillVo.getPayerbankno());//业务申请人开户行号
			// entity.setReqAcctsvcrAg(abEmitBillVo.getPayerbankno());//业务申请人承接行号
			// entity.setRecRole(abEmitBillVo.getRmk());//业务接收人类别
			entity.setRecNm(abBillPledgeApplyVo.getPledgeeName());// 业务接收人名称
			// entity.setRecCmonid(abEmitBillVo.getPayee());//业务接收人组织机构代码
			entity.setRecAcctId(abBillPledgeApplyVo.getPledgeeAcc());// 业务接收人账号
			entity.setRecAcctsvcr(abBillPledgeApplyVo.getPledgeeBankAcc());// 业务接收人开户行号
			// entity.setRecAcctsvcrAg(abEmitBillVo.getPayee());//业务接收人承接行号
			if(AccebillConstant.AB_INSIDE_SING_Y.equals(abBillPledgeApplyVo.getInteriorApply())){
				entity.setAdr(abBillPledgeApplyVo.getPledgeeCorpid());
			}
			abList.add(entity);
		}
		return abList;
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGEAPPLY, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		Set<String> corpIdSet = new HashSet<>();
		DataTablesOutput<AbBillPledgeApplyVo> dp = abBillPledgeApplyDao.dataTableApprove(dataTablesInput, businessList);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeApplyVo bill : listData){
				injectValues(bill,listDictMap);
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
			return dp;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dp;
		}
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			for (AbBillPledgeApplyVo bill : listData){
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


		return workFlowBusiness.merge(dp);
	}
	/**
	 * 获票据信息
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillPledgeApplyVo getAbBillPledgeApplyVoById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbBillPledgeApplyVo reInfo = new AbBillPledgeApplyVo();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		
		// 根据银行账号获取单位信息
		CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
		if (corpInfo != null) {
			reInfo.setCorpName(corpInfo.getName());
		}
		// 根据银行账号获取银行信息
		BtBankAccDTO bankAccDTO = getBtBankAccDTO(accrecordsInfo.getReqAcctId());
		if (bankAccDTO != null) {
			reInfo.setAccName(bankAccDTO.getAccName());
			reInfo.setBankCode(bankAccDTO.getBankCode());
		}else{
			reInfo.setAccName(reInfo.getReqNm());
		}
		BtBankAccDTO recBankAcc = getBtBankAccDTO(accrecordsInfo.getRecAcctId());
		if (recBankAcc != null) {
			reInfo.setPledgeeBankName(recBankAcc.getBankName());
		}
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(accrecordsInfo.getInsideSing()))) {
			CorpDTO recCorpInfo=null;
//			保证人地址 背书时候保存对内背书的单位
			if(StringUtils.isNotEmpty(accrecordsInfo.getAdr())){
				recCorpInfo=abCorpService.get(accrecordsInfo.getAdr());
			}
			if(recCorpInfo==null){
				recCorpInfo=abAccrecordsService.getCorpInfo(accrecordsInfo.getRecAcctId(), accrecordsInfo.getRecNm());
			}
			if(recCorpInfo!=null){
				reInfo.setPledgeeCorpName("("+recCorpInfo.getCode()+")"+recCorpInfo.getName());
			}
		}
		if (recBankAcc == null) {
			reInfo.setPledgeeBankName(reInfo.getRecAcctsvcrName());
		}
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGEAPPLY_STATUS"
				, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM");
		injectValues(reInfo, listDictMap);
		reInfo.setBankTypeName(StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
		return reInfo;
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<AbBillPledgeApplyVo> list, ICurrentUser userVo) {
		if (list != null && !list.isEmpty()) {
			for (AbBillPledgeApplyVo vo : list) {
				/*
				 * vo.setIds(vo.getId()); vo.setVvids(vo.getVvid());
				 */
				if (vo.getTransMoneys() == null && vo.getTransMoney() != null && "1".equals(vo.getNewStmIssuofpprInd()) ){
					vo.setTransMoneys(String.valueOf(vo.getTransMoney()));
				}
				this.batchApply(vo, AccebillConstant.BILL_OPERATE_IMPORTS, userVo);
			}
		}
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillPledgeApplyVo obj,Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbBillPledgeApplyVo> list,Object... user) {
		if (list != null && !list.isEmpty()) {
			HashMap<String, CorpDTO> corpNameMap = new HashMap<>(8);
			HashMap<String, CorpDTO> corpCodeMap = new HashMap<>(8);
			for (AbBillPledgeApplyVo vo : list) {
				List<String> errorMsg = new ArrayList<String>();
				// 根据所属单位代码获取单位信息
				CorpDTO corpByCode = abCorpService.getCorpByCode(vo.getCorpId());
				CorpDTO corpInfo;
				if (StringUtils.isEmpty(vo.getApplyCorpCode()) && StringUtils.isEmpty(vo.getApplyCorpName())) {
					errorMsg.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL);
				} else {
					if (StringUtils.isNotEmpty(vo.getApplyCorpCode())) {
						if (corpCodeMap.containsKey(vo.getApplyCorpCode())) {
							corpInfo = corpCodeMap.get(vo.getApplyCorpCode());
						} else {
							corpInfo = abCorpService.getCorpByCode(vo.getApplyCorpCode());
						}
					} else {
						if (corpNameMap.containsKey(vo.getApplyCorpName())) {
							corpInfo = corpNameMap.get(vo.getApplyCorpName());
						} else {
							corpInfo = abCorpService.getCorpIdByName(vo.getApplyCorpName());
						}
					}
					if (corpInfo == null) {
						errorMsg.add(AccebillConstant.ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST);
					} else {
						vo.setApplyCorpId(corpInfo.getId());
						vo.setApplyCorpCode(corpInfo.getCode());
						vo.setApplyCorpName(corpInfo.getName());
						corpCodeMap.put(corpInfo.getCode(), corpInfo);
						corpNameMap.put(corpInfo.getName(), corpInfo);
					}
				}
				if (corpByCode != null) {
					if (StringUtils.isNotEmpty(vo.getCorpName())) {
						CorpDTO corpByName = abCorpService.getCorpIdByName(vo.getCorpName());
						// 判断所属单位代码与所填的所属单位名称匹配不匹配
						if (corpByName==null || !corpByCode.getId().equals(corpByName.getId())) {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPNAME_ISNOT);
						}
					}
					String userId="";
					if(user[0]!=null){
						ICurrentUser userInfo=(ICurrentUser)user[0];
						userId=userInfo.getId();
					}
					// 查询权限
					List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLEDGEAPPLY_QUERY,userId);
					// 根据单位名称、票据号码查询质押数据
					List<AbBillPledgeApplyVo> abBillPledgeApplyVoList = abBillPledgeApplyDao.excelListAbBillPledgeApply(vo,
							corpAuthList);
					if (abBillPledgeApplyVoList != null && !abBillPledgeApplyVoList.isEmpty()) {
						AbBillPledgeApplyVo abBillPledgeApplyVo = abBillPledgeApplyVoList.get(0);
						BeanUtils.copyNotNullProperties(abBillPledgeApplyVo, vo,"transMoney","transMoneys");
						vo.setTransMoneys(vo.getTransMoney().toString());
						vo.setCorpId(corpByCode.getCode());
						vo.setNewStmIssuofpprInd(abBillPledgeApplyVo.getNewStmIssuofpprInd());
						if (vo.getBusinessDate() != null) {// 判断业务办理日期
							// 业务办理日期必须大于等于出票日期,小于到期日期!
							if (vo.getBusinessDate().getTime() < abBillPledgeApplyVo.getStartDate().getTime()
									|| vo.getBusinessDate().getTime() >= abBillPledgeApplyVo.getEndDate().getTime()) {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CHECKBUSINESSDATE);
							}
						} else {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BUSINESSDATE_NULL);
						}

						//不允许拆票的票据交易金额需要与票据金额一样，允许拆分的票据交易金额不能大于票据金额
						if((!"1".equals(abBillPledgeApplyVo.getBlgrdbagcrclPrmtInd()) && (vo.getBillMoney().compareTo(vo.getTransMoney()) != 0) ) || (vo.getBillMoney().compareTo(vo.getTransMoney()) < 0)){
							errorMsg.add(AccebillConstant.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
						}

						// 判断是否内部质押值对不对
						if (!"是".equals(vo.getInteriorApplyName()) && !"否".equals(vo.getInteriorApplyName())) {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_ERROR);
						} else {
							if ("是".equals(vo.getInteriorApplyName())) {
								vo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);
							} else {
								vo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_N);
							}
						}
						// 判断所属库存是否可做对应的内外部质押，单位库存内外都可以，中心代管只能做内部质押，银行托管只能做外部质押
						if (AccebillConstant.AB_ENTER_STOCK_NET.equals(vo.getEnterStock())
								&& AccebillConstant.AB_INSIDE_SING_N.equals(vo.getInteriorApply())) {
							errorMsg.add(
									AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_ENTERSTOCK_NET_FALSE_INTERIORAPPLY);
						}
						if (AccebillConstant.AB_ENTER_STOCK_BANK.equals(vo.getEnterStock())
								&& AccebillConstant.AB_INSIDE_SING_Y.equals(vo.getInteriorApply())) {
							errorMsg.add(
									AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_ENTERSTOCK_BANK_TRUE_INTERIORAPPLY);
						}
						// 判断是否为内部质押
						if (AccebillConstant.AB_INSIDE_SING_Y.equals(vo.getInteriorApply())) {
							// 判断质权人单位代码
							if (StringUtils.isNotEmpty(vo.getPledgeeCorpCode())) {
								// 根据质权人单位名称获取单位信息
								CorpDTO corp = abCorpService.getCorpByCode(vo.getPledgeeCorpCode());
								if (corp != null) {
									// 内部质押时质权人不能跟选择的数据是同一单位
									if (vo.getCorpId().equals(vo.getPledgeeCorpCode())) {
										errorMsg.add(
												AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_CORP_NOTEQUAL);
									}
									if (StringUtils.isNotEmpty(vo.getPledgeeCorpName())) {
										CorpDTO corpByPName = abCorpService.getCorpIdByName(vo.getPledgeeCorpName());
										// 判断质权人单位代码与所填的质权人单位名称匹配不匹配
										if (corpByPName==null || !corp.getId().equals(corpByPName.getId())) {
											errorMsg.add(
													AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_CORPNAME_ISNOT);
										}
									}
									vo.setPledgeeCorpid(corp.getId());
									vo.setPledgeeCorpName(corp.getName());
									// 判断质权人账号
									if (StringUtils.isNotEmpty(vo.getPledgeeAcc())) {
										// 根据质权人账号获取账号信息
										BtBankAccDTO bankaccInfo = abAccrecordsService
												.getBtBankAccInfo(vo.getPledgeeAcc());
										if (bankaccInfo != null) {
											// 判断质权人账号信息的单位与所填的单位是否一致
											if (corp.getId().equals(bankaccInfo.getCorpId())) {
												vo.setPledgeeName(bankaccInfo.getAccName());
												vo.setPledgeeBankAcc(bankaccInfo.getBankCode());
												vo.setPledgeeBankName(bankaccInfo.getBankName());
											} else {
												errorMsg.add(
														AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPNAME_BANKACC_ISNOTEXIST);
											}
										} else {
											errorMsg.add(
													AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKACC_ISNOTEXIST);
										}
									}
								} else {
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPNAME_ISNOTEXIST);
								}
							} else {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPCODE_NULL);
							}
						} else if (AccebillConstant.AB_INSIDE_SING_N.equals(vo.getInteriorApply())) {
							// 外部质押时判断质权人开户行行号
							if (StringUtils.isNotEmpty(vo.getPledgeeBankAcc())) {
								// 根据银行行号得到银行账号信息
								InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(vo.getPledgeeBankAcc());
								if (bankInfo != null) {
									vo.setPledgeeBankName(bankInfo.getBankName());
								} else {
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKCODE_NULL);
								}
							} else {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKACC_NULL);
							}
							// 判断质权人名称
							if (StringUtils.isEmpty(vo.getPledgeeName())) {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PLEDGE_NAME_NULL);
							}
						}
					} else {
						errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_DATA_NULL);
					}
				} else {
					errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPCODE_ERROR);
				}
				// 添加错误信息到vo
				if (!errorMsg.isEmpty()) {
					vo.addErrorMsg(errorMsg);
				}
			}
		}
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillPledgeApplyVoById(aid);
	}
	/**
	 * 根据主表id 从表id查询质押申请数据
	 * @param vid 主表id
	 * @param vvid 从表id
	 * @return 返回列表数据
	 */
	public List<AbBillPledgeApplyVo> listAbBillPledgeApplyVoByVidAndVvid(String vid,String vvid) {
		return abBillPledgeApplyDao.listAbBillPledgeApplyVoByVidAndVvid(vid,vvid);
	}
}
