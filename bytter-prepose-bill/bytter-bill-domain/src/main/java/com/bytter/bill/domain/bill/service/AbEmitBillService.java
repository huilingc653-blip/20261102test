package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.domain.bill.dao.AbBillReceivebizDao;
import com.bytter.bill.domain.bill.dao.AbEmitBillDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatCpUrationDTO;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILL)
@Service("abEmitBillService")
@Transactional
public class AbEmitBillService extends BaseService<AbEmitBillVo, String>
		implements IAccebillBaseService, IWorkFlowListener, IExcelVerifyHandler<AbEmitBillVo>, ICorpDeleteVerifyService, ICorpDisableVerifyService {
	@Autowired
	private AbEmitBillDao abEmitBillDao;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbEmitBillSupplyService abEmitBillSupplyService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbNisBillHeadService abNisBillHeadService;
	@DubboReference(check = false)
	INisBillService iNisBillService;
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbExternalCorpService abExternalCorpService;
	@Autowired
	private AbFcBondHookService abFcBondHookService;
	@Autowired
	private AbGuaranteeService abGuaranteeService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Autowired(required=false)
	private AbRatCpUrationService abRatCpUrationService;
	@Autowired
	private AbDictService abDictService;
	@Autowired
	private AbFieldIsmdfService abFieldIsmdfService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@Autowired(required = false)
	private AbBillReceivebizDao abBillReceivebizDao;
	@DubboReference(check = false)
	private IAccebillService accebillService;
	@Resource
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbAccountGrantService abAccountGrantService;
	@Autowired
	private AbFbsItemService abFbsItemService;
	@Autowired
	private AbConfigService abConfigService;
	@DubboReference(check = false)
	private IDictService dictService;

	@DubboReference(check = false)
	private IRegionService regionService;

	@Autowired
	private IBisBifInitService iBisBifInitService;

	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;

//	@DubboReference(check = false)
//	private INisBillService iNisBillService;

	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;

	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IFcVoucherTypeService voucherTypeService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;

	@DubboReference(check = false)
	private IInputBankInfoService btInputBankInfoService;

	/**
	 * 开票登记列表查询
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		List<String> bursarBillNoList = new ArrayList<>();
		//如果会计凭证号是从excel复制出来的，格式是通过空格隔开的，做一个批量查询
		if (!EmptyUtils.isEmpty(input.getFormdata().get("likes_bursarBillNo"))){
			String bursarBillNo = input.getFormdata().get("likes_bursarBillNo").toString();
			bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
			if (bursarBillNoList.size()>=2){
				input.getFormdata().remove("likes_bursarBillNo");
				input.getFormdata().put("bursarBillNoList",bursarBillNoList);
			}
		}

		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_ABEMITBILL_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		abAccountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_ABEMITBILL_QUERY,corpIds);
		String userName = UserContextHelper.helper().getCurrentUser().getLoginName();
		DataTablesOutput<AbEmitBillVo> dataTablesUnion = abEmitBillDao.getDataTablesUnion(input, corpAuthList, userName);
		if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_EMITBILL_STATUS", "sf_yn","AB_BILL_INSBRIEFID","AB_BILL_ABACCRECORDS_EMITSTTLMMK", "AB_BILL_SEND_SIGN"
					, "ACCEBILL_EMIT_TYPE", "AB_BILL_BAIL_TYPE","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbEmitBillVo bill : dataTablesUnion.getData()) {

				injectValues(bill, listDictMap);
			}
		}

		if (bursarBillNoList.size()>=2){
			List<AbEmitBillVo> list = dataTablesUnion.getData();
			List<AbEmitBillVo> list1 = new ArrayList<>();
			for (String bursarBillNo : bursarBillNoList) {
				for (AbEmitBillVo abEmitBillVo : list) {
					if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())){
						list1.add(abEmitBillVo);
					}
				}
			}
			dataTablesUnion.setData(list1);
		}
		return dataTablesUnion;
	}
	private void injectValues(AbEmitBillVo bill,Map<String, Map<String, DictDTO>> listDictMap){
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
		// 开出方式：0  对外开出；1 对内自开
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
		// 开票数据来源
		Optional.ofNullable(bill.getSource()).ifPresent(source -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMITBILL_SOURCE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(source)))
					.ifPresent(dict -> bill.setSourceName(dict.getName()));
		});
		// 不得转让标记：EM00可再转让EM01不得转让
		Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
					.ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
		});
		// 业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_EMITBILL_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 兑付方式:0或空线下兑付,1线上兑付'
		Optional.ofNullable(bill.getEmitSttlmmk()).ifPresent(emitSttlmmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ABACCRECORDS_EMITSTTLMMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitSttlmmk)))
					.ifPresent(dict -> bill.setEmitSttlmmkName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是；
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是；
		Optional.ofNullable(bill.getEmitType()).ifPresent(emitType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_EMIT_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitType)))
					.ifPresent(dict -> bill.setEmitTypeName(dict.getName()));
		});
		// 保证金类型
		Optional.ofNullable(bill.getBailType()).ifPresent(bailType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BAIL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bailType)))
					.ifPresent(dict -> bill.setBailTypeName(dict.getName()));
		});
		// 是否开票保贴
		Optional.ofNullable(bill.getEmitGuandisSign()).ifPresent(emitGuandisSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitGuandisSign)))
					.ifPresent(dict -> bill.setEmitGuandisSignName(dict.getName()));
		});
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 出票标志
		Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
					.ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
		});

		// 是否允许分包
		Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtInd -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtInd)))
					.ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
		});

		// 是否敞口 isOpenRat
		Optional.ofNullable(bill.getIsOpenRat()).ifPresent(isOpenRat -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isOpenRat)))
					.ifPresent(dict -> bill.setIsOpenRatName(dict.getName()));
		});
	}
	/**
	 * 根据id删除开票登记
	 * @param aid 流水表id
	 * @param reason 付款票据删除原因
	 * @param userVo 操作用户
	 */
	public void delete(String aid,String reason, ICurrentUser userVo) {
		if (StringUtils.isNotEmpty(aid)) {
			batchDelete(aid,reason, userVo);
		}
	}
	/**
	 * 批量删除开票登记信息
	 *
	 * @param aids 流水表id
	 * @param reason 付款票据删除原因
	 * @param userVo 操作用户
	 */
	public boolean batchDelete(String aids,String reason, ICurrentUser userVo) {
		if (StringUtils.isNotEmpty(aids)) {
			String[] aidArray = aids.split(",");
			String vvid = null;
			String[] vvids = new String[aidArray.length];
			AbVoucherinfoVice abVoucherinfoVice = null;
			int errDataNum = 0;
			for (int i = 0; i < aids.split(",").length; i++) {
				AbAccrecords abAccrecords = abAccrecordsService.get(aidArray[i]);
				try {
					vvid = abAccrecords.getVvid();
				}catch (Exception e){
					errDataNum++;
					logger.error("数据异常，票据流水不存在！");
					continue;
				}
				abVoucherinfoVice = abVoucherinfoViceService.get(vvid);
				vvids[i] = vvid;
				if (StringUtils.isNotEmpty(vvid)) {
					// 释放预算
					abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
							BudgetConstant.PROCESS_SYSNAME_AB, vvid);
					// 付款打回
					if (abVoucherinfoVice != null && StringUtils.isNotEmpty(abVoucherinfoVice.getVoucherId())
							&& StringUtils.isNotEmpty(reason) && !"付款开票删除".equals(reason) &&!"事务异常删除".equals(reason)) {
						abNisBillHeadService.updateNisBillhead(abVoucherinfoVice.getVoucherId(),
								NetbankConstant.STATUE_BACK, reason,userVo);
					}
				}
				abBillNotificationService.completeMsg(aidArray[i],userVo);
				abVoucherinfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
				abEmitBillDao.update(abVoucherinfoVice);
				//删除保证金，修改状态为释放
				abFcBondHookService.updateFcBondApply(abVoucherinfoVice,userVo, FcBondConstant.STATUE_RELEASE);
				//删除授信
				abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_DELETE, userVo);
			}
			int num = abAccrecordsService.batchDeleteByBriefIdIs001(aids.split(","), userVo);
			if (num == aids.split(",").length) {
				abLogService.batchSaveAbLog(vvids, aids.split(","), AccebillConstant.BRIEF_STATUS_001,
						AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE, AccebillConstant.LANG_LOG_BATCHDELETE_SUCC, userVo);
				return true;
			} else {
				if(errDataNum == aids.split(",").length){
					return true;
				}
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		}
		return false;
	}
	/**
	 * 批量提交开票登记信息
	 *
	 * @param vids
	 *            票据主表ids
	 * @param vvids
	 *            票据从表ids
	 * @param aids
	 *            流水表ids 2017/10/12 添加业务校验:
	 *            1、批量提交时，系统开启挂预算，通过流水查询是否有预算信息，没有则提示要挂预算
	 *            2、批量提交时，系统开启挂授信，则判断RAT_MODEL是否挂授信,如果为空或者要挂授信，则CREDIT_CODE
	 *            授信代码不能为空(为空提示要挂授信)
	 * @param isBatchNoSubmit 是否是批次提交
	 */
	public boolean batchSubmit(String vids, String vvids, String aids, ICurrentUser userVo, boolean isBatchNoSubmit) {
		AbAccrecords abAccrecords = null;
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		String[] vidsList = vids.split(",");
		String[] vvidsList = vvids.split(",");
		String[] aidsList = aids.split(",");
		String batchNo = null;
		Map<String,Object> params=new HashMap<>();
		if(isBatchNoSubmit){
			batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
			params.put("batchNoNew",batchNo);
		}
		params.put("ids",vvids);
		params.put("boxStatusAll",AccebillConstant.AB_BOX_STATUS_Y);
		abVoucherinfoViceService.batchUpdate(params);
//		BtBankAccDTO bankAccInfo = null;
		boolean isBudget = abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001);// 系统是否开启资金计划
		boolean isRelateRat = abRatUrationService.isRelateRat();// 系统是否开启挂授信
		for (int i = 0; i < vidsList.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(vidsList[i], true);
			abVoucherinfoVice = abVoucherinfoViceService.get(vvidsList[i],true);
			abAccrecords = abAccrecordsService.get(aidsList[i], true);
//			bankAccInfo = getBtBankAccDTO(abVoucherinfo.getPayeracc());
			// 资金计划校验 (付款生成的开票登记不用校验)
			if(isBudget && abFbsBudgetProcessService.corpCtlBudget(abVoucherinfoVice.getCorpId ())
					&& StringUtils.isBlank (abVoucherinfoVice.getVoucherId ()) ){
				List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = abFbsBudgetProcessService
						.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_EMIT,
								BudgetConstant.PROCESS_SYSNAME_AB, vvidsList[i]);
				if (fbsBudgetProcessDTOList == null || fbsBudgetProcessDTOList.isEmpty()) {
					// {0}单号的票据没有选择资金计划，请编辑后再提交!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ISBUDGET_ISNOTEXIST,
							abVoucherinfoVice.getAppNo());
				}
			}
			// 授信校验 (根据前端控制，系统目前只有银票支持挂授信)
			if (isRelateRat && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf (abVoucherinfo.getBillType ()))
					&& !AccebillConstant.AB_RATMODEL_SING_N.equals(abVoucherinfoVice.getRatmodel())
					&& StringUtils.isEmpty(abVoucherinfoVice.getCreditCode())) {
				// {0}单号的票据没有编辑授信信息，请编辑后再提交!
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ISRELATERAT_ISNOTEXIST,
						abVoucherinfoVice.getAppNo());
			}
			if (AccebillConstant.AB_BILL_SEND_SIGN_N.equals(abAccrecords.getSendSign())) {// 电票离线模式
				// 离线电票添加票据状态码
				if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))
						&& !AbAccebillUtils.isBillTypeTP(abVoucherinfo.getBillType())) {
                    abVoucherinfo.setBillStatus("1".equals(abVoucherinfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.AB_BILL_STATUS_030006);// 提示收票已签收
					abVoucherinfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC03);
					abVoucherinfo.setEbillBlCrclIndc(AccebillConstant.AB_BANEM_Y.equals(abVoucherinfo.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303);
				}
				abAccrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
			}
			abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
			abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, variables);// 票据从水信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
			// 修改票据主表信息
			abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
					userVo);
			//批次提交时设置从表批次号
			if(isBatchNoSubmit){
				abVoucherinfoVice.setBatchNo(batchNo);
				abVoucherinfoViceService.update(abVoucherinfoVice);
			}
			// 修改票据流水信息
			abAccrecordsService.update(abAccrecords, userVo);
			// 更新资金计划
			if(StringUtils.isNotBlank (abVoucherinfoVice.getVoucherId ())){
				List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_PAY,
						BudgetConstant.PROCESS_SYSNAME_NIS, abVoucherinfoVice.getId ());
				if(list!=null && !list.isEmpty ()){
					abFbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
							BudgetConstant.PROCESS_SYSNAME_NIS, list.get(0).getBillNo(), BudgetConstant.STATUE_WAIT_CHECK, userVo);
				}
			}else{
				abFbsBudgetProcessService.updateBudgetProcessForBill (BudgetConstant.BUSINESS_NAME_EMIT,
						BudgetConstant.PROCESS_SYSNAME_AB, abVoucherinfoVice.getId (), BudgetConstant.STATUE_WAIT_CHECK, userVo);
			}
			// 挂银行接授信
			abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_BATCHSUBMIT, userVo);
			// 挂接内部授信
			abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_BATCHSUBMIT, abAccrecords,  userVo);
			// 挂接担保
			AbEmitBillVo abEmitBillVo = this.getAbEmitBearVoByAid(abAccrecords.getId());
			abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_BATCHSUBMIT,abEmitBillVo,  userVo);
			abBillNotificationService.completeMsg(abAccrecords,userVo);
			//付款办理开票不需要票据单独审批
			if(!(AccebillConstant.PAY_MONEY_OPEN_BILL_FLG.equals(abAccrecords.getExcId()) || "3".equals(abAccrecords.getExcId()))){
				// 启动工作流
				getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILL, abAccrecords.getId(), variables,
						abVoucherinfoVice.getApplyCorpId(),userVo.getId());
			}
		}
		abLogService.batchSaveAbLog(vvids.split(","), aids.split(","), AccebillConstant.BRIEF_STATUS_001,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		return true;
	}

	/**
	 * 付款办理开票提交
	 */
	public void payOpenBillBatchSubmit(String vids, String vvids, String aids, ICurrentUser userVo){
		batchSubmit(vids, vvids, aids, userVo, false);
		//付款办理开票提交 直接审批通过
		approve(vids, vvids, aids, null, true, userVo);
	}

	/**
	 * 根据从表id获取已经存在的资金计划并组装为 AbEmitBillVo
	 *
	 * @param vvid
	 *            从表id
	 */
	public AbEmitBillVo getFbsBudgetProcessByVvid(String vvid) {
		if (StringUtils.isNotEmpty(vvid)) {
			AbEmitBillVo abEmitBillVo = null;
			List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
					BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vvid);
			if (fbsBudgetProcessDTOList != null && !fbsBudgetProcessDTOList.isEmpty()) {
				abEmitBillVo = new AbEmitBillVo();
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				int size = fbsBudgetProcessDTOList.size();
				String[] itemCode = new String[size];
				String[] itemName = new String[size];
				BigDecimal[] processMoney = new BigDecimal[size];
				for (int i = 0; i < size; i++) {
					fbsBudgetProcessDTO = fbsBudgetProcessDTOList.get(i);
					itemCode[i] = fbsBudgetProcessDTO.getItemCode();
					itemName[i] = fbsBudgetProcessDTO.getItemName();
					processMoney[i] = fbsBudgetProcessDTO.getProcessMoney();
				}
				abEmitBillVo.setItemCode(itemCode);
				abEmitBillVo.setItemName(itemName);
				abEmitBillVo.setProcessMoney(processMoney);
				fbsBudgetProcessDTO = fbsBudgetProcessDTOList.get(0);
				abEmitBillVo.setCorpId(fbsBudgetProcessDTO.getCorpId());// 单位id
				abEmitBillVo.setBusinessDate(fbsBudgetProcessDTO.getProcessDate());// 执行日期
				abEmitBillVo.setVvid(vvid);// 业务单据号(流水表id)
				abEmitBillVo.setAppNo(fbsBudgetProcessDTO.getBillNo());
				abEmitBillVo.setPayeracc(fbsBudgetProcessDTO.getPayerAcc());
			}
			return abEmitBillVo;
		}
		return null;
	}
	/**
	 * 保存开票登记
	 *
	 * @param abEmitBillVo
	 * @param operate
	 *            新增或修改操作
	 */
	public void saveOrUpdate(AbEmitBillVo abEmitBillVo, String operate, ICurrentUser userVo) {
		//付款办理批量开票使用
		if(StringUtils.isNotBlank(abEmitBillVo.getVoucherId()) && StringUtils.isBlank(abEmitBillVo.getPayeeacc())){
//			abEmitBillVo.setBatchNo(SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO));
			payOrderOpenBillDeal(abEmitBillVo, operate, userVo);
			return;
		}
		saveOrUpdateOrSubmit(abEmitBillVo, operate, AccebillConstant.BILL_OPERATE_SAVE, userVo);
	}

	private void payOrderOpenBillDeal(AbEmitBillVo abEmitBillVo, String operate, ICurrentUser userVo) {
		Map<String, String> payIdAndBillIdsMap = iNisBillService.getBillIdsByPayIds(abEmitBillVo.getVoucherId());
		String billIdStr = "";
		if(!CollectionUtil.isEmpty(payIdAndBillIdsMap)){
			for(String s : payIdAndBillIdsMap.keySet()){
				if("".equals(billIdStr)){
					billIdStr = payIdAndBillIdsMap.get(s);
				}else {
					billIdStr = payIdAndBillIdsMap.get(s) + "," + billIdStr;
				}
			}
			batchDelete(billIdStr,"付款办理批量开票删除旧票", userVo);
		}
//		Map<String,Integer> voucherIdBatchDtlNoMap=new HashMap<>();
//		//设置批次开票明细顺序号
//		if(StrUtil.isNotEmpty(abEmitBillVo.getVoucherId())) {
//			List<String> voucherids=StrUtil.split(abEmitBillVo.getVoucherId(),',');
//			int i=0;
//			for (String voucherid:voucherids) {
//				voucherIdBatchDtlNoMap.put(voucherid,++i);
//			}
//		}
		List<NisBillheadDTO> nisBillheadDTOS = nisBillheadService.listNisBillheadByPayIds(abEmitBillVo.getVoucherId());

		String rmk = abEmitBillVo.getRmk();
		for(NisBillheadDTO billheadDTO : nisBillheadDTOS){
			String payId = billheadDTO.getId();
//			if(voucherIdBatchDtlNoMap.containsKey(payId)) {
//				abEmitBillVo.setBatchDtlNo(voucherIdBatchDtlNoMap.get(payId));
//			}
			//开票数据来源--应付付款
			abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE10);
			abEmitBillVo.setInterfaceCall(true);//接口调用标志
			abEmitBillVo.setIsNisPayAdd(AccebillConstant.BILL_ISNISPAYADD);
			abEmitBillVo.setIsPayFlg("2");
			abEmitBillVo.setVoucherId(payId);
			abEmitBillVo.setPayee(billheadDTO.getPayeeAccName());
			abEmitBillVo.setPayeeacc(billheadDTO.getPayeeAcc());
			abEmitBillVo.setPayeebankno(billheadDTO.getPayeeBankCode());
			abEmitBillVo.setPayeeBankName(billheadDTO.getPayeeBank());
			abEmitBillVo.setBillMoney(billheadDTO.getPayMoney());
			abEmitBillVo.setInsideSing(abEmitBillVo.getInsideSing() == null ? 0:abEmitBillVo.getInsideSing());
			abEmitBillVo.setVid(null);
			abEmitBillVo.setVvid(null);
			abEmitBillVo.setAid(null);
			abEmitBillVo.setId(null);
			abEmitBillVo.setRmk(StringUtils.isBlank(rmk) ? billheadDTO.getPurpose() : abEmitBillVo.getRmk());
			if(abEmitBillVo.getBailpercent() != null){
				abEmitBillVo.setBailmoney((abEmitBillVo.getBillMoney().multiply(abEmitBillVo.getBailpercent())).setScale(2,RoundingMode.CEILING));
			}
			saveOrUpdateOrSubmit(abEmitBillVo, operate, AccebillConstant.BILL_OPERATE_SAVE, userVo);
			iNisBillService.saveOrUpdateSync(payId, abEmitBillVo.getId()); //更新 nis_bill 中间表
			nisBillheadService.updateNisBillheadPyaerInfo(payId,abEmitBillVo.getPayeracc(),abEmitBillVo.getPayer(),abEmitBillVo.getPayerBankName(),userVo); // 更新付款通知表
		}
	}

	/**
	 * 提交开票登记
	 *
	 * @param abEmitBillVo
	 * @param operate
	 *            新增或修改操作
	 */
	public void submit(AbEmitBillVo abEmitBillVo, String operate, ICurrentUser userVo) {
		if (AccebillConstant.BILL_OPERATE_ADD.equals(operate)) {
			// 新增提交
			saveOrUpdateOrSubmit(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, AccebillConstant.BILL_OPERATE_SUBMIT,
					userVo);
		} else {
			// 修改提交
			saveOrUpdateOrSubmit(abEmitBillVo, AccebillConstant.BILL_OPERATE_UPDATE,
					AccebillConstant.BILL_OPERATE_SUBMIT, userVo);
		}
	}
	/**
	 * 新增修改业务处理
	 *
	 * @param abEmitBillVo
	 * @param opt
	 *            新增或修改标志 add or update
	 * @param operate
	 *            保存或提交标志 save or submit
	 */
	public void saveOrUpdateOrSubmit(AbEmitBillVo abEmitBillVo, String opt, String operate, ICurrentUser userVo) {
		// 判断用户是否有账号权限
		boolean isPermission = accountGrantService.checkPermissionByUserAndAccount(userVo.getId(), abEmitBillVo.getPayeracc(),
				AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT, null);
		if (!isPermission) {
			throw new BusinessException(AccebillMessageEnum.FIN_BOOK_TALLY_ACCOUNT_IS_NOT_PERMISSION);
		}
		//票据金额不能为空
		if(abEmitBillVo.getBillMoney() == null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLMONEY_ISNOTNULL);
		}
		// 判断金额大于0.01
		if (abEmitBillVo.getBillMoney().doubleValue() < AccebillConstant.AB_ACCBILL_MINMONEY) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_BILLMONEYMIX);
		}
		// 判断纸票金额不能超过设置的额度
		if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillVo.getBillFlag()))
				&& (abEmitBillVo.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
			throw new BusinessException(AccebillMessageEnum.TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY);
		}
		// 判断电票金额小于等于10亿
		if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
				&& abEmitBillVo.getBillMoney().doubleValue() > AccebillConstant.AB_ACCBILL_MONEY_BILLFLAG11) {
			throw new BusinessException(AccebillMessageEnum.TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11);
		}
		//执行保存或提交
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		AbAccrecords abAccrecords = null;
		BtBankAccDTO bankAccInfo = null;
		if (StringUtils.isEmpty(abEmitBillVo.getVid())) { //票据ID为空
			abVoucherinfo = getAbVoucherinfo(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, operate, userVo);
			abVoucherinfoVice = getAbVoucherinfoVice(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, operate, userVo);
			abAccrecords = getAbAccrecords(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, operate, userVo);
		} else {
			abVoucherinfo = abVoucherinfoService.get(abEmitBillVo.getVid());
			abVoucherinfo.setCooperativeBankNo(abEmitBillVo.getCooperativeBankNo());//合作银行号
			abVoucherinfoVice = abVoucherinfoViceService.get(abEmitBillVo.getVvid());
			abAccrecords = abAccrecordsService.get(abEmitBillVo.getAid()); //根据票据流水ID查？
		}
		if(abEmitBillVo.getBatchDtlNo()!=null){
			abAccrecords.setBatchDtlNo(abEmitBillVo.getBatchDtlNo());
		}
		abAccrecords.setOptStatus(null);
		/** 数据校验 */
		// 票据号重复校验
		checkOrigNo(abEmitBillVo.getCorpId(), abEmitBillVo.getOrigNo(), abEmitBillVo.getVvid(), opt, abVoucherinfo.getSbbllRngStrtSn(),abVoucherinfo.getSbbllRngEndSn());
		if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
				&&!AccebillConstant.BILL_ISNISPAYADD.equals(abEmitBillVo.getIsNisPayAdd())){
			if (!AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
				try {
					bankAccInfo = getBtBankAccDTO(abEmitBillVo.getPayeracc());
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 收票人账号不存在
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST);
					} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST.equals(e.getIMessageEnum())) {
						// 收票人账号不是票据专户
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			}else {
				bankAccInfo = abAccrecordsService.getBtBankAccInfo(abVoucherinfo.getPayeracc());
			}
		}
		// 提交时设置业务状态
		if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operate)) {
            /* 电票要根据账号判断是否离线模式，如果为离线模式或为第三方票据类型则以纸票规则处理 */
			if (AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType()) || bankAccInfo == null
					|| BtBankAccConstant.CIRCULATIONMODE_0.equals(bankAccInfo.getCirculationMode()) || StringUtils.isNotEmpty(abEmitBillVo.getOrigNo())) {// 电票离线模式
				// 离线电票且非第三方票据添加票据状态码
				if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
						&& !AbAccebillUtils.isBillTypeTP(abVoucherinfo.getBillType())
						&& (BtBankAccConstant.CIRCULATIONMODE_0.equals(bankAccInfo.getCirculationMode()) || StringUtils.isNotEmpty(abEmitBillVo.getOrigNo()))) {
					abVoucherinfo.setBillStatus("1".equals(abEmitBillVo.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.AB_BILL_STATUS_030006);// 提示收票已签收
					abVoucherinfo.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC03);
					abVoucherinfo.setEbillBlCrclIndc("EM00".equals(abEmitBillVo.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303);
				}
				abAccrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
			}
			abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
		}
		//修改编辑时判断是否已经提交
		if(AccebillConstant.BILL_OPERATE_UPDATE.equals(opt) && abAccrecords.getStatus() > 10 ){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		/** 新增处理 */
		if (StringUtils.isEmpty(abEmitBillVo.getVid())) {
			if(StringUtils.isNotEmpty(abEmitBillVo.getOrigNo())){
				AbVoucherinfoVO abVoucherinfoVO= abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abEmitBillVo.getOrigNo(),abVoucherinfo.getSbbllRngStrtSn(),abVoucherinfo.getSbbllRngEndSn());
				if(StringUtils.isNotEmpty(abVoucherinfoVO)){
					abVoucherinfo.setId(abVoucherinfoVO.getId());
				}
			}
			abVoucherinfo.setBlStdAmt(BigDecimal.valueOf(0.01));
			/** 新增票据主表信息 */
			abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
					userVo);
			/** 新增票据从表信息 */
			abEmitBillVo.setVid(abVoucherinfo.getId());
			abVoucherinfoVice.setVid(abVoucherinfo.getId());
			abVoucherinfoVice.setBatchNo(abEmitBillVo.getBatchNo());
			abVoucherinfoViceService.saveVoucherinfoVice(abVoucherinfoVice, userVo);
			/** 新增票据流水信息 */
			abEmitBillVo.setVvid(abVoucherinfoVice.getId());
			abAccrecords.setVid(abVoucherinfo.getId());
			abAccrecords.setVvid(abVoucherinfoVice.getId());
			abAccrecords.setBillFlag(abVoucherinfo.getBillFlag());
			if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operate)) {
				abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
				abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			}
			//针对结算新增票据暂存数据 创建人设置为空
			if(AccebillConstant.BILL_ISNISPAYADD.equals(abEmitBillVo.getIsNisPayAdd())){
				abAccrecordsService.saveAccrecords(abAccrecords, null);
			}else{
				abAccrecordsService.saveAccrecords(abAccrecords, userVo);
			}
			// 对付款来的数据进行处理，带有流通介质和票据类型时不允许修改
			if(AccebillConstant.AB_ACCEBILL_SOURCE10.equals(abEmitBillVo.getSource())){
				if(StringUtils.isNotEmpty(abEmitBillVo.getBillFlag())){
					abFieldIsmdfService.saveAbFieldIsmdf(abAccrecords.getId(), AccebillConstant.BILL_TYPE, userVo);
				}
				if(StringUtils.isNotEmpty(abEmitBillVo.getBillType())){
					abFieldIsmdfService.saveAbFieldIsmdf(abAccrecords.getId(), AccebillConstant.BILL_FLAG, userVo);
				}
			}
			abEmitBillVo.setVvid(abAccrecords.getVvid());
			abEmitBillVo.setVid(abAccrecords.getVid());
			abEmitBillVo.setId(abAccrecords.getId());
			abEmitBillVo.setAid(abAccrecords.getId());
			/** 修改处理 */
		} else {
			/** 修改票据主表信息 */
			Integer billFlag = abVoucherinfo.getBillFlag ();
			AbVoucherinfo abVoucherinfoNew = getAbVoucherinfo(abEmitBillVo, AccebillConstant.BILL_OPERATE_UPDATE,
					operate, userVo);
			BeanUtils.copyNotNullProperties(abVoucherinfoNew, abVoucherinfo);
			if(StringUtils.isNotEmpty(abEmitBillVo.getOrigNo())){
				//abVoucherinfo.setOrigNo(null);
				AbVoucherinfoVO abVoucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abEmitBillVo.getOrigNo(),abVoucherinfo.getSbbllRngStrtSn(),abVoucherinfo.getSbbllRngEndSn());
				if(abVoucherinfoVO != null && abVoucherinfoVO.getId() != null){
					abEmitBillVo.setVid(abVoucherinfoVO.getId());
					abVoucherinfoNew.setId(abVoucherinfoVO.getId());
					abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfoNew, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
							userVo);
				}else {
					abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
							userVo);
				}
			}else {
				abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
						userVo);
			}

			/** 修改票据从表信息 */
			AbVoucherinfoVice abVoucherinfoViceNew = getAbVoucherinfoVice(abEmitBillVo,
					AccebillConstant.BILL_OPERATE_UPDATE, operate, userVo);
			BeanUtils.copyNotNullProperties(abVoucherinfoViceNew, abVoucherinfoVice);
			System.out.println("abVoucherinfoVice.getUseType():"+abVoucherinfoVice.getUseType()+"===="+abVoucherinfoVice);
			if(!abRatCpUrationService.isRelateCpRat()){
				abVoucherinfoVice.setInsideRatModel(AccebillConstant.AB_BILL_RATMODEL_N);
			}
			if(abRatUrationService.isNotRelateRat()){
				abVoucherinfoVice.setRatmodel(AccebillConstant.AB_BILL_RATMODEL_N);
			}
			// 开票保贴
			abVoucherinfoVice.setEmitGuandisSign(abEmitBillVo.getEmitGuandisSign());
			abVoucherinfoVice.setVid(abEmitBillVo.getVid());
			abVoucherinfoViceService.update(abVoucherinfoVice);
			/** 修改票据流水信息 */
			AbAccrecords abAccrecordsNew = getAbAccrecords(abEmitBillVo, AccebillConstant.BILL_OPERATE_UPDATE, operate,
					userVo);
			BeanUtils.copyNotNullProperties(abAccrecordsNew, abAccrecords);

			abAccrecords.setSendSign(null);
//			if(billFlag != abVoucherinfoNew.getBillFlag () || AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(abEmitBillVo.getIsOnline())){
//				//如果是电票修改为纸票则 发送标志改为0反之改为1
//				abAccrecords.setSendSign ((AccebillConstant.AB_BILL_FLAG_PAPER.equals(
//						String.valueOf (abVoucherinfoNew.getBillFlag ()))
//						|| AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType()))?
//						AccebillConstant.AB_BILL_SEND_SIGN_N:AccebillConstant.AB_BILL_SEND_SIGN_Y);
//			}
			if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operate)) {
				abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
				abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			}
			abAccrecords.setVid(abEmitBillVo.getVid());
			abAccrecordsService.update(abAccrecords, userVo);
		}
		//保存客商信息,省市信息为空表明没有修改过客商信息，则不需要更新客商信息 是否选择了要保存客商信息
		if(AccebillConstant.AB_BILL_TYPE_MERCH.equals (String.valueOf (abEmitBillVo.getBillType ()))
				&&StringUtils.isNotEmpty (abEmitBillVo.getAcceptProvince ()) && !EmptyUtils.isEmpty(abEmitBillVo.getIsExternalCorp())){
			saveExternalCorpDTO(abEmitBillVo);
		}
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			abBillAttachmentService.opAttachmentInfo(abEmitBillVo, abAccrecords, userVo);
		}
		//新增挂保证金授信业务处理流程
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())
				&& null!=abEmitBillVo.getUrationDTO()
				&& StringUtils.isNotEmpty(abEmitBillVo.getUrationDTO().getRatCode())
				&& !" ".equals(abEmitBillVo.getUrationDTO().getRatCode())
				&& StringUtils.isNotBlank(abEmitBillVo.getUrationDTO().getIsOpenRat())){
			abEmitBillVo.getUrationDTO().setBondMoney(abEmitBillVo.getBailmoney());//设置保证金金额
			abEmitBillVo.getUrationDTO().setBailRate(abEmitBillVo.getBailpercent());//占用保证金比例
			abEmitBillVo.getUrationDTO().setBondCurId(abEmitBillVo.getUrationDTO().getCurCode());//占用币别
			abEmitBillVo.getUrationDTO().setCurCode(abEmitBillVo.getUrationDTO().getCurCode());//占用币别
			abEmitBillVo.getUrationDTO().setBillMoney(abEmitBillVo.getBillMoney());//业务金额
		}
		//新增挂接内部授信处理流程
		if(null!=abEmitBillVo.getIsHangCredit()){
			abEmitBillVo.setRatCpUrationDTO(new RatCpUrationDTO());
			if(!StringUtils.isBlank(abEmitBillVo.getCpUrationId())){
				abEmitBillVo.getRatCpUrationDTO().setCpUrationId(abEmitBillVo.getCpUrationId());
			}
			abEmitBillVo.getRatCpUrationDTO().setCpRatCode(abEmitBillVo.getCpRatCode());
			abEmitBillVo.getRatCpUrationDTO().setCpRatItemName(abEmitBillVo.getCpUrationId());
			abEmitBillVo.getRatCpUrationDTO().setCpRatItemCode(abEmitBillVo.getCpRatItemCode());
			abEmitBillVo.getRatCpUrationDTO().setCurCode(abEmitBillVo.getUrationDTO().getCurCode());
		}
		// 挂接银行授信
		RatUrationDTO newDto = abEmitBillVo.getUrationDTO() == null ?
				new RatUrationDTO() : abEmitBillVo.getUrationDTO();
		newDto.setInitCurCode(iBtCurrencyService.getRMBCurrency().getId());
		abAccrecords.setRatUrationDTO(newDto);
		StopWatch stopWatch = new StopWatch();
		/** 挂授信，资金计划，工作流，日志 处理 */
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(operate)) {
			/** 暂存操作*/
			//新增
			if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
				abEmitBillVo.setId(abAccrecords.getId());
				if(!abEmitBillVo.isInterfaceCall()){
					// 执行资金计划
					stopWatch.start("开票调用资金计划接口耗时:");
					fbsBudgetProcess(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, AccebillConstant.BILL_OPERATE_SAVE, userVo);
					stopWatch.stop();
				}
//				if(!"1".equals(abEmitBillVo.getIsImport())) {
					stopWatch.start("开票调用授信接口耗时:");
					abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_ADD, userVo);
					stopWatch.stop();
					//挂接保证金
					stopWatch.start("开票调用保证金接口耗时:");
					abFcBondHookService.addBailByRegister(abEmitBillVo, userVo, FcBondConstant.STATUE_APPLY);//开票登记提交审批，保证金的状态为占用
					stopWatch.stop();
					//挂接担保
					stopWatch.start("开票挂接担保接口耗时:");
					abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_ADD, abEmitBillVo, userVo);//开票登记担保为暂存占用，补录完成为正式占用
					stopWatch.stop();

					abAccrecords.setRatCpUrationDTO(abEmitBillVo.getRatCpUrationDTO());
					//挂接内部授信
					stopWatch.start("开票挂接内部授信耗时:");
					abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_ADD, abAccrecords, userVo);
					stopWatch.stop();
//				}

				abLogService.saveAbLog(abVoucherinfoVice.getId(), abAccrecords.getId(),
						AccebillConstant.BRIEF_STATUS_001, AccebillConstant.ABLOG_LOGTYPE_ADD,
						AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
				//修改
			} else {
				// 执行资金计划
				stopWatch.start("开票资金计划耗时2:");
				fbsBudgetProcess(abEmitBillVo, AccebillConstant.BILL_OPERATE_EDIT, AccebillConstant.BILL_OPERATE_SAVE,
						userVo);
				stopWatch.stop();
				// 挂接授信
				stopWatch.start("开票挂接授信耗时2:");
				abAccrecords.setRatUrationDTO(abEmitBillVo.getUrationDTO());
				abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_EDIT,userVo);
				stopWatch.stop();
				//挂接保证金
				stopWatch.start("开票挂接保证金2:");
				abFcBondHookService.addBailByRegister(abEmitBillVo,userVo, FcBondConstant.STATUE_APPLY);//开票登记暂存，保证金的状态为占用
				stopWatch.stop();
				//挂接担保
				stopWatch.start("开票挂接担保2:");
				abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_EDIT,abEmitBillVo,userVo);//开票登记修改
				stopWatch.stop();
				//挂接内部授信
				stopWatch.start("开票挂接内部授信2:");
				abAccrecords.setRatCpUrationDTO(abEmitBillVo.getRatCpUrationDTO());
				abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_EDIT,abAccrecords,userVo);
				stopWatch.stop();
				abLogService.saveAbLog(abVoucherinfoVice.getId(), abAccrecords.getId(),
						AccebillConstant.BRIEF_STATUS_001, AccebillConstant.ABLOG_LOGTYPE_UPDATE,
						AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
				abBillNotificationService.completeMsg(abAccrecords,userVo);
			}
		} else {
			/** 提交操作 */
			abEmitBillVo.setAid(abAccrecords.getId());
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, variables);// 票据从水信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
			// 执行资金计划
			stopWatch.start("开票资金计划3:");
			fbsBudgetProcess(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD.equals(operate)
							? AccebillConstant.BILL_OPERATE_ADD : AccebillConstant.BILL_OPERATE_EDIT,
					AccebillConstant.BILL_OPERATE_SUBMIT, userVo);
			stopWatch.stop();
			// 挂接授信
			stopWatch.start("开票挂接授信3:");
			abAccrecords.setRatUrationDTO(abEmitBillVo.getUrationDTO());
			abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_SUBMIT,userVo);
			stopWatch.stop();
			//挂接保证金  TODO
			stopWatch.start("开票挂接保证金3:");
			abFcBondHookService.addBailByRegister(abEmitBillVo,userVo, FcBondConstant.STATUE_APPLY);//开票登记提交审批，保证金的状态为占用
			stopWatch.stop();
			//挂接担保
			stopWatch.start("开票挂接担保3:");
			abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_SUBMIT,abEmitBillVo,userVo);//开票登记提交
			stopWatch.stop();
			//内部授信挂接
			stopWatch.start("开票内部授信挂接3:");
			abAccrecords.setRatCpUrationDTO(abEmitBillVo.getRatCpUrationDTO());
			abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_SUBMIT,abAccrecords,userVo);
			stopWatch.stop();
			abBillNotificationService.completeMsg(abAccrecords,userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILL, abAccrecords.getId(), variables,
					abVoucherinfoVice.getApplyCorpId(),userVo.getId());
			abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfo.getId(), AccebillConstant.BRIEF_STATUS_001,
							AccebillConstant.ABLOG_LOGTYPE_SUBMIT, AccebillConstant.BILL_OPERATE_ADD.equals(operate)
									? AccebillConstant.LANG_LOG_SAVE_SUCC : AccebillConstant.LANG_LOG_UPDATE_SUCC,
							userVo);
		}
		logger.info("开票调用外部接口耗时："+stopWatch.prettyPrint());
	}
	/**
	 * 付款开票打回
	 *
	 * @param abEmitBillVo
	 */
	public void repulse(AbEmitBillVo abEmitBillVo, ICurrentUser userVo) {
		if (abEmitBillVo != null && StringUtils.isNotEmpty(abEmitBillVo.getVoucherId())
				&& StringUtils.isNotEmpty(abEmitBillVo.getVvid())) {
			// 付款接口调用
			abNisBillHeadService.updateNisBillhead(abEmitBillVo.getVoucherId(), NetbankConstant.STATUE_BACK,
					abEmitBillVo.getReason(),userVo);
			// 删除票据
			abAccrecordsService.deleteByBriefIdIs001(abEmitBillVo.getAid(), userVo);
			abBillNotificationService.completeMsg(abEmitBillVo.getAid(),userVo);
			abLogService.saveAbLog(abEmitBillVo.getVvid(), abEmitBillVo.getAid(), AccebillConstant.BRIEF_STATUS_001,
					AccebillConstant.ABLOG_LOGTYPE_REPULSE, AccebillConstant.LANG_LOG_APP_BACK, userVo);
		}
	}

	public Map<String,Object> dealBillInfo(String aid,String isBatchBill,String batchNo){
		Map<String,Object>  model = new HashMap<>();
		AbEmitBillVo vo = getAbEmitBillVoByAid(aid,false);
		//针对应付票据有区间控制的对按钮进行控制
		if(StringUtils.isNotEmpty(vo.getVoucherId())){
			NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(vo.getVoucherId());
            if(nisBillheadDTO!=null){
                FcVoucherTypeDTO fcVoucherType = voucherTypeService.getFcVoucherTypeById(nisBillheadDTO.getVoucherTypeId());
                if(fcVoucherType!=null){
                    if(NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode()) ){
                        if(StringUtils.isNotBlank(fcVoucherType.getBillSectionControl())){
                            if(NetbankConstant.BILL_SECTION_THREE.equals(fcVoucherType.getBillSectionControl())){
                                model.put("billSectionThreeButton",1);
                            }else{
                                model.put("billSectionSixButton",1);
                            }
                        }else{
                            model.put("billSectionThreeButton",1);
                            model.put("billSectionSixButton",1);
                        }
                    }
                }
            }
		}else{
			model.put("billSectionThreeButton",1);
			model.put("billSectionSixButton",1);
		}
		if(AccebillConstant.BILL_ISBATCHBILL.equals(isBatchBill)){
			model.put("batchNo", batchNo);
			model.put("isBatchBill", AccebillConstant.BILL_ISBATCHBILL);
			String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
			model.put("appNo", appNo);
		}
		// 来自付款的票据设置账户的线上线下标识
		if(vo.getBillFlag() == null && StringUtils.isNotEmpty(vo.getPayeracc())){
			BtBankAccDTO bankAccInfo=abAccrecordsService.getBtBankAccInfo(vo.getPayeracc());
			if(bankAccInfo!=null){
				vo.setSendSign(bankAccInfo.getCirculationMode());
			}
		}
		//银行授信单位 ratBankCorpType: 0开票人，1承兑人
		if (ObjectUtil.isEmpty(vo.getRatBankCorpType())){
			vo.setRatBankCorpType(CommonConstant.NO);
		}
		//授信银行 ratBankType：0承兑人开户银行，1出票人开户银行
		if (ObjectUtil.isEmpty(vo.getRatBankType())){
			vo.setRatBankType(CommonConstant.NO);
		}
		model.put("abEmitBillVo", vo);
		// 挂预算系统参数
		if(StringUtils.isNotBlank (vo.getVoucherId ())){
			//付款单应付票据预算
			List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
					(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, vo.getVoucherId ());
			if(list!=null && !list.isEmpty ()){
				model.put("payItem",
						abConfigService.getConfig(ConfigConstant.NIS, NetbankConstant.NIS_CONFIG_PAYITEM).toString());
				model.put("businessName", BudgetConstant.BUSINESS_NAME_PAY);
				model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
			}else{
				//付款单没有预算信息
				model.put("nisBillheadNoBudget", true);
			}
		}else if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)){
			model.put("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
			model.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
			model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		}
		// 挂授信
		boolean isRelateRat = abRatUrationService.isRelateRat();
		model.put("curId", abVoucherinfoViceService.getCurIdByCNY());
		model.put("isRelateRat", isRelateRat);
		String isOpenRat = FcBondConstant.IS_HOOK_N;
		String ratmodel = FcBondConstant.IS_HOOK_N;
		if (isRelateRat) {
			RatUrationDTO urationDTO = new RatUrationDTO();
			if(!StringUtils.isBlank(vo.getRatmodel())
					&& vo.getRatmodel().equals("1")){
				ratmodel = vo.getRatmodel();
				urationDTO = abRatUrationService.getRatUrationDTOByBillId(vo.getVvid());
			}
			model.put("urationDTO", urationDTO);
			if(!StringUtils.isBlank(vo.getIsOpenRat())
					&& vo.getIsOpenRat().equals("1")){
				isOpenRat =vo.getIsOpenRat();
			}
		}
		//是否挂保证金
		boolean isBail = abFcBondHookService.isBail();
		//判断是否有已挂保证金数据
		FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(),vo.getAppNo());
		String bailmodel = FcBondConstant.IS_HOOK_N;
		String bondId = "";
		BigDecimal availMoney = new BigDecimal(0);
		if(fcBondApplyDTO != null){
			bondId = fcBondApplyDTO.getBondId();
			bailmodel = FcBondConstant.IS_HOOK_Y;
			availMoney = fcBondApplyDTO.getAvailMoney().add(fcBondApplyDTO.getApplyMoney());
		}
		//保存的保证金可用余额
		model.put("availMoney",availMoney);
		//保证金id
		model.put("bondId",bondId);
		//是否有挂保证金记录
		model.put("bailmodel",bailmodel);
		//是否挂保证金
		model.put("isBail",isBail);
		//挂保证金银行类型
		model.put("bailBankControlType",abFcBondHookService.bailBankControlType());
		//是否启用授信敞口
		model.put("mouthRat",abRatUrationService.isNotOpenRat());
		model.put("attachmentInfo", abBillAttachmentService.editAttachment(aid));
		model.put("ratmodel", ratmodel);
		model.put("isOpenRat",isOpenRat);
		//担保参数公共参数
		model.put("isGuarantee",abGuaranteeService.isHangGuarantee());
		//担保业务参数
		String isHangGuarantee = vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee();
		model.put("isHangGuarantee",isHangGuarantee);
		//过滤查询参数操作
		if(abGuaranteeService.isHangGuarantee().equals(1)
				&& isHangGuarantee.equals(FcBondConstant.IS_HOOK_Y)){
			GuaContractOccupDTO bearVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
			if(null!=bearVo && bearVo.getContractIds()!=null && bearVo.getContractIds().size()>0){
				List<String> idList=bearVo.getContractIds();
				String contractIds="";
				for (String id:idList) {
					contractIds+=id+",";
				}
				model.put("contractIds",contractIds);
				model.put("contractCode",bearVo.getContractCode());
				model.put("idList",bearVo==null?null:bearVo.getContractIds());
				model.put("guaWayNames",bearVo.getGuaWayName());
				model.put("guaWay",bearVo.getGuaWay());
				model.put("guaranteeRmk",bearVo.getGuaRmk());
			}
		}
		model.put("isEmitMake",abVoucherinfoViceService.isEmitMake());
		model.put("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
		//获取纸票票据金额最大额度
		model.put("maxBillMoney",abVoucherinfoViceService.getMaxBillMoney());
		//内部授信大参数
		model.put("isOpenCpRat",abRatCpUrationService.isRelateCpRat());
		//当前配置的内部参数
		model.put("curOpenCpRat",vo.getInsideRatModel()==null?"0":vo.getInsideRatModel());
		RatUrationDTO ratDto = abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid());
		//当前内部授信信息
		model.put("cpURationDTO",ratDto);
		model.put("externalCorp", abConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
		model.put("isHangGuarantee",vo.getIsHangGuarantee());
		BisBifInitDTO bisBifInitDTO = iBisBifInitService.getByBifCode(vo.getBifCode());
		if(bisBifInitDTO != null){
			AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.getDataByBifCode(bisBifInitDTO.getId());
			if(abBillBankAutoDtl == null){
				model.put("newBillFlg",null);
			}else {
				model.put("newBillFlg",abBillBankAutoDtl.getNewBillFlg());
			}
		}
		// 添加不能修改信息
		abFieldIsmdfService.initFieldIsmdfInfo(vo.getAid(), model);
		InputBankInfoDTO inputBankInfoDTO = btInputBankInfoService.getByBankCode(vo.getAcceptorbankno());
		model.put("acceptorBankType",inputBankInfoDTO == null ? null : inputBankInfoDTO.getBankTypeName());
		return model;
	}

	/**
	 * 根据流水ID查询开票信息
	 * @param aid 流水ID
	 * @param isPay 是否为线上兑付查看
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbEmitBillVo getAbEmitBillVoByAid(String aid,boolean isPay) {
		AbEmitBillVo vo = null;
		if (StringUtils.isNotEmpty(aid)) {
			if(!isPay){
				AbAccrecords accInfo=abAccrecordsService.get(aid, true);
				if(accInfo!=null && AccebillConstant.AB_BILL_PAYEMITSTTLMMK1.equals(accInfo.getEmitSttlmmk())){
					isPay=true;
				}
			}
			List<AbEmitBillVo> list = isPay?abEmitBillDao.getAbEmitBillVoByIdForPay(aid):abEmitBillDao.getAbEmitBillVoById(aid);
			if (isPay){
				List<String> citys = list.stream().distinct().map(AbEmitBillVo::getCity).collect(Collectors.toList());
				Map<String, RegionDTO> regionDTOMap = regionService.listByIds(citys);
				if (regionDTOMap != null && !regionDTOMap.isEmpty()){
					list.stream().forEach(item -> {
						if (regionDTOMap.containsKey(item.getCity())){
							RegionDTO regionDTO = regionDTOMap.get(item.getCity());
							item.setProv(regionDTO.getParentId());
							item.setProvName(regionDTO.getProvName());
							item.setCity(regionDTO.getId());
							item.setCityName(regionDTO.getName());
						}
					});
				}

			}
			if (list != null && !list.isEmpty()) {
				vo = list.get(0);
				if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(vo.getInsideSing()))) {
					// 对内自开时，获取收票人单位id用于编辑回显
					if(StringUtils.isBlank (vo.getPayeeacc())){
						CorpDTO corp =abCorpService.getCorpIdByName(vo.getPayee());
						if(corp!=null){
							vo.setPayeeCorpId(corp.getId ());
						}
					}else{
						try {
							BtBankAccDTO btBankAccDTO = getBtBankAccDTO(vo.getPayeeacc());
							if (btBankAccDTO != null) {
								vo.setPayeeCorpId(btBankAccDTO.getCorpId());
							}
						} catch (BusinessException e) {
							// 此处查看页面不处理账号的异常
							if (!(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL
									.equals(e.getIMessageEnum())
									|| AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
									.equals(e.getIMessageEnum()))) {
								throw new BusinessException(e.getIMessageEnum());
							}
						}
					}
				}
			}
		}
		if (ObjectUtil.isNotEmpty(vo)){
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_EMITBILL_STATUS", "sf_yn","AB_BILL_INSBRIEFID","AB_BILL_ABACCRECORDS_EMITSTTLMMK", "AB_BILL_SEND_SIGN"
					, "ACCEBILL_EMIT_TYPE", "AB_BILL_BAIL_TYPE", "AB_BRIEF_STATUS");
			injectValues(vo, listDictMap);
            Set<String> strings = new HashSet<>();
            strings.add(vo.getBusCorpId() == null ? "" : vo.getBusCorpId());
            strings.add(vo.getApplyCorpId() == null ? "" : vo.getApplyCorpId());
            strings.add(vo.getCorpId() == null ? "" : vo.getCorpId());
            Map<String, CorpDTO> stringCorpDTOMap = new HashMap<>();
            if(strings.size() > 0){
                Optional.ofNullable(abCorpService.listCorpDtoByCorpIds(new ArrayList<>(strings))).ifPresent(stringCorpDTOMap::putAll);
            }
            CorpDTO corpDTO = stringCorpDTOMap.get(vo.getBusCorpId());
            CorpDTO corpDTO1 = stringCorpDTOMap.get(vo.getApplyCorpId());
            CorpDTO corpDTO2 = stringCorpDTOMap.get(vo.getCorpId());
            vo.setBusCorpName(corpDTO == null ? "" : corpDTO.getName());
            vo.setApplyCorpCode(corpDTO1 == null ? "" : corpDTO1.getCode());
            vo.setApplyCorpName(corpDTO1 == null ? "" : corpDTO1.getName());
            vo.setCorpCode(corpDTO2 == null ? "" : corpDTO2.getCode());
            vo.setCorpName(corpDTO2 == null ? "" : corpDTO2.getName());
            vo.setCorpCodeAndName("("+vo.getCorpCode()+")"+vo.getCorpName());
		}
		return vo;
	}
	/**
	 * 根据流水IDS查询开票信息
	 *
	 * @param aids
	 *            流水ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbEmitBillVo> getAbEmitBillVoByAid(String[] aids) {
		List<AbEmitBillVo> voList = new ArrayList<AbEmitBillVo>();
		AbEmitBillVo abEmitBillVo = null;
        for (String aid : aids) {
            abEmitBillVo = getAbEmitBillVoByAid(aid, false);
            voList.add(abEmitBillVo);
        }
		return voList;
	}
	@Override
	public BaseDao<AbEmitBillVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 商票补录时，如果客商信息有修改则保存客商信息
	 *
	 * @param abEmitBillVo
	 * @return AbVoucherinfo
	 */
	protected void saveExternalCorpDTO(AbEmitBillVo abEmitBillVo){
		ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
		externalCorpDTO.setName (abEmitBillVo.getAcceptor ());//客商名称
		externalCorpDTO.setCorpId (abEmitBillVo.getCorpId ());//客商所属单位id
		externalCorpDTO.setProvince (abEmitBillVo.getAcceptProvince ());//所在省
		externalCorpDTO.setCity (abEmitBillVo.getAcceptCity ());//所在市
		externalCorpDTO.setSourceSystem("BFS");//数据来源
		//是否共享
		externalCorpDTO.setIsCommon(abEmitBillVo.getIsCommon());
		//是否有效
		externalCorpDTO.setStatus(1);
		//客商账号
		List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
		ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
		externalCorpBankAccDTO.setExternalAcc(abEmitBillVo.getAcceptoracc ());//客商银行账号
		externalCorpBankAccDTO.setBankType(abEmitBillVo.getAcceptBankTypeId ());//银行类别
		externalCorpBankAccDTO.setBank(abEmitBillVo.getAcceptorBankName ());//银行名称
		externalCorpBankAccDTO.setBankSourceCode(abEmitBillVo.getAcceptorbankno ());//联行号
		externalCorpBankAccDTO.setAccProvince(abEmitBillVo.getAcceptProvince ());//省份
		externalCorpBankAccDTO.setAccCity(abEmitBillVo.getAcceptCity ());//城市
		externalCorpBankAccDTO.setIsDefaultAcc("0");//首选银行：1是 0否
		externalCorpBankAccList.add(externalCorpBankAccDTO);
		externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
		abExternalCorpService.saveIfNotExist(externalCorpDTO);
	}
	/**
	 * 开票登记执行资金计划 挂预算:1开启挂资金计划；2、维护了对应的预算科目；3、对外业务才执行资金计划
	 *
	 * @param opt
	 *            新增或修改操作标记
	 * @param operate
	 *            保存或提交业务标记
	 */
	protected void fbsBudgetProcess(AbEmitBillVo abEmitBillVo, String opt, String operate, ICurrentUser userVo) {
		String[] itemCode = abEmitBillVo.getItemCode();
		String[] itemName = abEmitBillVo.getItemName();
		BigDecimal[] processMoney = abEmitBillVo.getProcessMoney();
		FbsBudgetProcessDTO fbsBudgetProcessDTO = null;
		FbsBudgetProcessDTO fbsBudgetProcessOld = null;
		List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();// 预算占用
		/**判断是否挂资金计划*/
		// 判断付款方式是否挂资金计划 1、票据模块配置开启挂接资金计划(控制层判断) 2、预算科目维护不为空 3、当前单位是否要挂预算
		boolean isBudget = abVoucherinfoViceService.isBudget(abEmitBillVo.getCorpId(), AccebillConstant.BRIEF_STATUS_001)
				&& StringUtils.isNotEmpty(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001))
				&& itemCode != null;
		if( StringUtils.isBlank (abEmitBillVo.getVoucherId ()) && !isBudget ) {
			return;//非付款生成的票据且开票登记不挂预算
		}
		/**删除原来的资金计划*/
		if (AccebillConstant.BILL_OPERATE_EDIT.equals(opt)) {
			if(StringUtils.isNotBlank (abEmitBillVo.getVoucherId ())){
				//付款应付票据的资金计划
				List<FbsBudgetProcessDTO> oldList = this.abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
						(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,abEmitBillVo.getVoucherId ());
				if(oldList!=null && !oldList.isEmpty ()){
					fbsBudgetProcessOld = oldList.get (0);
					// 修改时，先删除之前的计划执行数据
					this.abFbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
							BudgetConstant.PROCESS_SYSNAME_NIS, fbsBudgetProcessOld.getBillNo ());
				}
			}else{
				// 修改时，先删除之前的计划执行数据
				this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
						BudgetConstant.PROCESS_SYSNAME_AB, abEmitBillVo.getVvid());
			}
		}
		/**写入资金计划*/
		if(StringUtils.isNotEmpty(itemCode)){
			for (int i = 0; i < itemCode.length; i++) {
				fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				if(fbsBudgetProcessOld != null){
					BeanUtils.copyNotNullProperties(fbsBudgetProcessOld, fbsBudgetProcessDTO);
					fbsBudgetProcessDTO.setId (null);
				}else{
					fbsBudgetProcessDTO.setCorpId(abEmitBillVo.getCorpId());// 单位id
					fbsBudgetProcessDTO.setProcessDate(abEmitBillVo.getBusinessDate());// 执行日期
					fbsBudgetProcessDTO.setBillNo(abEmitBillVo.getAppNo());// 业务单据号(流水表id) ；开票登记号
					fbsBudgetProcessDTO.setBillId(abEmitBillVo.getVvid());// 业务单据号(流水表id)
//					BtBankAccDTO bankAccInfo = getBtBankAccDTO(abEmitBillVo.getPayeracc());
					fbsBudgetProcessDTO.setCurId(abVoucherinfoViceService.getCurIdByCNY());// 币别
					fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_EMIT);// 执行业务名称
					fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
				}
				if (AccebillConstant.BILL_OPERATE_SAVE.equals(operate)) {
					// 暂存
					fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:0暂存(暂存,打回),11在途(审批中)，95占用(审批通过)
				} else {
					// 提交
					fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0暂存(暂存,打回),11在途(审批中)，95占用(审批通过)
				}
				//添加内转判断标识 innerSign  1-内转 0-非内转 开出方式对内，同一个网点下的成员单位 判定为内转
				if(AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (abEmitBillVo.getInsideSing ()))
						&& abCorpService.isTheSameNetByCorpId(abEmitBillVo.getCorpId (),abEmitBillVo.getPayeeCorpId ())){
					fbsBudgetProcessDTO.setInnerSign ("1");
				}else{
					fbsBudgetProcessDTO.setInnerSign ("0");
				}
				fbsBudgetProcessDTO.setProcessCurMoney(null!=abEmitBillVo && null!=abEmitBillVo.getProcessCurMoney()?abEmitBillVo.getProcessCurMoney()[i]:new BigDecimal(0.00));
				fbsBudgetProcessDTO.setExchangeRate(null!=abEmitBillVo && null!=abEmitBillVo.getExchangeRate()?abEmitBillVo.getExchangeRate()[i]:new BigDecimal(0.00));
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);
				fbsBudgetProcessDTO.setItemName(itemName[i]);
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);
				if (userVo != null) {
					fbsBudgetProcessDTO.setCreateBy(userVo.getLoginName());// 创建人登录名称
				}
				fbsBudgetProcessDTO.setCreateDate(new Date());// 创建日期
				if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
					fbsBudgetProcessDTO.setPayeeAcc(abEmitBillVo.getPayeeacc1());
				} else {
					fbsBudgetProcessDTO.setPayeeAcc(abEmitBillVo.getPayeeacc());
				}
				fbsBudgetProcessDTO.setPayeeAccName(abEmitBillVo.getPayee());
				list.add(fbsBudgetProcessDTO);
			}
		}
		if (list != null && !list.isEmpty()) {
			abFbsBudgetProcessService.writeBudgetProcess(list,userVo);
		}
	}
	/**
	 * 根据提交参数生成AbVoucherinfo 票据主表对象
	 *
	 * @param abEmitBillVo
	 *            页面传递票据信息
	 * @param opt
	 *            新增或修改标志
	 * @param optrate
	 *            保存或提交标志
	 * @return AbVoucherinfo
	 */
	protected AbVoucherinfo getAbVoucherinfo(AbEmitBillVo abEmitBillVo, String opt, String optrate,
											 ICurrentUser userVo) {
		AbVoucherinfo entity = new AbVoucherinfo();
		BeanUtils.copyNotNullProperties(abEmitBillVo,entity);
		//entity.setOrigNo(abEmitBillVo.getOrigNo());// 票据号码
		//entity.setCooperativeBankNo(abEmitBillVo.getCooperativeBankNo());//合作银行号
		//entity.setBillType(abEmitBillVo.getBillType());// 票据类型
		//entity.setBanendrsmtmk(abEmitBillVo.getBanendrsmtmk());// 是否可转让标记
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(optrate)) {// 保存
			entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);// 处理标记
			// 10正在处理11处理完成
		} else {// 提交
			entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);// 处理标记
			// 10正在处理11处理完成
		}
		//entity.setBillFlag(abEmitBillVo.getBillFlag());// 流通介质
		//entity.setBillMoney(abEmitBillVo.getBillMoney());// 票据金额
		if("1".equals(abEmitBillVo.getNewStmIssuofpprInd()) && "1".equals(abEmitBillVo.getBlgrdbagcrclPrmtInd())){
			//设置票据区间
			entity.setSbbllRngStrtSn(AccebillConstant.DRAFT_RANG_START);
			Long endRangLong = abEmitBillVo.getBillMoney().multiply(new BigDecimal(100)).longValue();
			String initEndRangStr = "000000000000";
			String subEndRang = initEndRangStr.substring(0,12-endRangLong.toString().length());
			entity.setSbbllRngEndSn(subEndRang + endRangLong);
		}
		//entity.setStartDate(abEmitBillVo.getStartDate());// 出票日期
		//entity.setEndDate(abEmitBillVo.getEndDate());// 到期日期
		//entity.setPayer(abEmitBillVo.getPayer());// 出票人
		//entity.setPayeracc(abEmitBillVo.getPayeracc());// 出票人账号
		//entity.setPayerBankName(abEmitBillVo.getPayerBankName());// 出票人开户银行名称
		//entity.setPayerbankno(abEmitBillVo.getPayerbankno());// 出票人开户银行行号
		//entity.setPayee(abEmitBillVo.getPayee());// 收票人
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
			entity.setPayeeacc(abEmitBillVo.getPayeeacc1());// 收票人账号
		} else {
			entity.setPayeeacc(abEmitBillVo.getPayeeacc());// 收票人账号
		}
		//entity.setPayeeBankName(abEmitBillVo.getPayeeBankName());// 收票人开户银行名称
		//entity.setPayeebankno(abEmitBillVo.getPayeebankno());// 收票人开户行行号
		//entity.setAcceptor(abEmitBillVo.getAcceptor());// 承兑人
		//entity.setAcceptoracc(abEmitBillVo.getAcceptoracc());// 承兑人账号
		//entity.setAcceptorbankno(abEmitBillVo.getAcceptorbankno());// 承兑人开户行行号
		//entity.setAcceptorBankName(abEmitBillVo.getAcceptorBankName());// 承兑人开户行名称
		//entity.setAcceptorbankaddress(abEmitBillVo.getAcceptorbankaddress());// 承兑人开户行地址
		//entity.setAccptncagrmtnb(abEmitBillVo.getAccptncagrmtnb());// 承兑协议编号
		// entity.setArXclx();//瑕疵类型
		//entity.setAcontractNo(abEmitBillVo.getAcontractNo());// 承兑合同号
		//entity.setAinvcnb(abEmitBillVo.getAinvcnb());// 发票号
		//entity.setPayerCdtRatgs(abEmitBillVo.getPayerCdtRatgs());// 开票信用等级
		//entity.setPayerCdtRatgGgcy(abEmitBillVo.getPayerCdtRatgGgcy());// 开票评级机构
		//entity.setPayerCdtRatgDuedt(abEmitBillVo.getPayerCdtRatgDuedt());// 开票评级到期日
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			if (userVo!=null) {
				entity.setCreateName(userVo.getUsername());// 创建人名称
				entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
			}
			entity.setCreateDate(new Date());// 创建日期
		} else {
			if (userVo!=null) {
				entity.setUpdateName(userVo.getUsername());// 修改人名称
				entity.setUpdateBy(userVo.getLoginName());// 修改人登录名称
			}
			entity.setUpdateDate(new Date());// 修改日期
		}
		return entity;
	}
	/**
	 * 根据提交参数生成AbVoucherinfoVice 票据从表对象
	 *
	 * @param abEmitBillVo
	 * @param opt
	 *            新增或修改标志
	 * @return AbVoucherinfoVice
	 */
	protected AbVoucherinfoVice getAbVoucherinfoVice(AbEmitBillVo abEmitBillVo, String opt, String optrate,
													 ICurrentUser userVo) {
		AbVoucherinfoVice entity = new AbVoucherinfoVice();
		entity.setUseType(abEmitBillVo.getUseType());
		entity.setVid(abEmitBillVo.getVid());// 票据主表id
		entity.setCorpId(abEmitBillVo.getCorpId());// 票据单位
		entity.setApplyCorpId(abEmitBillVo.getApplyCorpId());// 申请单位
		entity.setBusCorpId(abEmitBillVo.getBusCorpId());// 业务单位
		entity.setBusPersonnel(abEmitBillVo.getBusPersonnel());// 业务人员
		entity.setBankAcc(abEmitBillVo.getPayeracc());// 持票单位账号
		entity.setBankName(abEmitBillVo.getPayerBankName());// 持票单位银行名称
		entity.setBankCode(abEmitBillVo.getPayerbankno());// 持票单位银行行号
		// entity.setBifCode();银行接口代码
		// entity.setBillSource();//票据来源 收票，背书，质押
		// entity.setCnAccrecords();//前手
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
			// 对内开票时，将收票人单位id保存到前手里面(开票登记无前手)
			entity.setCnAccrecords(abEmitBillVo.getPayee1());// 业务接收人单位id
		}
		entity.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);// 应付票据
		entity.setInsideSing(abEmitBillVo.getInsideSing());// 内部业务标志
		entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志
		entity.setCreditCode(abEmitBillVo.getCreditCode());// 授信代码
		// entity.setBudgetCode();//预算科目
		entity.setBailAcc(abEmitBillVo.getBailAcc());// 保证金账户
		entity.setBailMoney(abEmitBillVo.getBailmoney());// 保证金
		entity.setBailPercent(abEmitBillVo.getBailpercent());// 保证金比例
		entity.setBailRate(abEmitBillVo.getBailrate());// 保证金比率
		entity.setFeeMoney(abEmitBillVo.getFeemoney());// 手续费
		entity.setBailContract(abEmitBillVo.getBailContract());//保证金合同
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(optrate)) {// 保存
			entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);// 处理标记
			// 10正在处理11处理完成
		} else {// 提交
			entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);// 处理标记
			// 10正在处理11处理完成
		}
		// entity.setEnterStock();//所属库存
		entity.setAbs(abEmitBillVo.getAbs());// 摘要
		entity.setRmk(abEmitBillVo.getRmk());// 备注
		entity.setVoucherId(abEmitBillVo.getVoucherId());// 外部系统id
		entity.setSource(abEmitBillVo.getSource());// 开票数据来源
		if(StringUtils.isEmpty(entity.getSource())){
			entity.setSource(AccebillConstant.AB_ACCEBILL_SOURCE11);
		}
		entity.setAttachmentId(abEmitBillVo.getAttachmentId());
		entity.setRatmodel(abEmitBillVo.getRatmodel());
		//判断挂银行授信
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())){
			entity.setRatBankCorpType(abEmitBillVo.getRatBankCorpType());//银行授信单位类型
			entity.setRatBankCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
			entity.setRatBankType(abEmitBillVo.getRatBankType());//授信银行类型
		}else{
			entity.setRatBankCorpType(null);//银行授信单位类型
			entity.setRatBankCorpId(null);//银行授信单位CorpId
			entity.setRatBankType(null);//授信银行类型
		}
		entity.setAppNo(abEmitBillVo.getAppNo());// 开票申请单号
		entity.setBatchNo(abEmitBillVo.getBatchNo());//票据批次号
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			if (abEmitBillVo.isInterfaceCall()) {
				// 接口生成数据时，不设置创建人
				entity.setIsSetCreateBy(false);
			} /*
			 * else{ entity.setCreateName(getUserVo().getUsername());//创建人名称
			 * entity.setCreateBy(getUserVo().getLoginName());//创建人登录名称 }
			 * entity.setCreateDate(new Date());//创建日期
			 */
		} else {
			if (userVo != null) {
				entity.setUpdateName(userVo.getUsername());// 修改人名称
				entity.setUpdateBy(userVo.getLoginName());// 修改人登录名称
			}
			entity.setUpdateDate(new Date());// 修改日期
		}
		entity.setIsOpenRat(abEmitBillVo.getUrationDTO()==null?(StringUtils.isNotBlank(abEmitBillVo.getIsOpenRat()) && "0".equals(abEmitBillVo.getIsOpenRat()))?abEmitBillVo.getIsOpenRat():null : abEmitBillVo.getUrationDTO().getIsOpenRat());
		entity.setBailModel(abEmitBillVo.getBailmodel());
		entity.setBearModel(abEmitBillVo.getIsHangGuarantee());//是否挂接担保
		entity.setInsideRatModel(abEmitBillVo.getIsHangCredit());//是否挂接内部授信
		entity.setInsideCreditCode(abEmitBillVo.getInsideCreditCode());//内部授信代码
		entity.setEmitType(abEmitBillVo.getEmitType());//开票方式
		entity.setEmitGuandisSign(abEmitBillVo.getEmitGuandisSign());//是否开票保贴
		entity.setIsEnsure(abEmitBillVo.getIsEnsure());
		entity.setBailType(abEmitBillVo.getBailType());
		// 是否授信提用
		entity.setIsCreditDraw(abEmitBillVo.getIsCreditDraw());
		entity.setPayLockMoney(abEmitBillVo.getPayLockMoney());
		return entity;
	}
	/**
	 * 根据提交参数生成AbAccrecords 票据流水表对象
	 *
	 * @param abEmitBillVo
	 * @param opt
	 *            新增或修改标志
	 * @param optrate
	 *            保存或提交标志
	 * @return AbAccrecords
	 */
	protected AbAccrecords getAbAccrecords(AbEmitBillVo abEmitBillVo, String opt, String optrate, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		entity.setVid(abEmitBillVo.getVid());// 票据主表ID
		entity.setVvid(abEmitBillVo.getVvid());// 票据从表ID
		entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(optrate)) {// 保存
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);// 业务处理状态：-2已删除；-1打回；0
			// 暂存；11-94审批中；95
			// 已完成
		} else {
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 95 已完成
		}
		// entity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);//是否发送指令标志：0否；
		// 1是 ；
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_001);// 业务类型
		entity.setInsideSing(abEmitBillVo.getInsideSing());// 内部业务标志
		entity.setBankendrsmtmk(abEmitBillVo.getBanendrsmtmk());// 是否可转让
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abEmitBillVo.getBusinessDate());// 业务归属日期
		// 开票登记的摘要和备注存储在票据从表中
		/*
		 * entity.setAbs(abEmitBillVo.getAbs());//摘要
		 * entity.setRmk(abEmitBillVo.getRmk());//备注
		 */
		entity.setCreditCode(abEmitBillVo.getCreditCode());// 授信代码
		entity.setBailAcc(abEmitBillVo.getBailAcc());// 保证金账户
		entity.setBailmoney(abEmitBillVo.getBailmoney());// 保证金
		entity.setBailpercent(abEmitBillVo.getBailpercent());// 保证金比例
		entity.setBailrate(abEmitBillVo.getBailrate());// 保证金利率
		entity.setFeemoney(abEmitBillVo.getFeemoney());// 手续费
		entity.setFeemoneyScale(abEmitBillVo.getFeemoneyScale());//手续费比例
		// entity.setAdr(abEmitBillVo.getAdr());//保证人地址
		entity.setCdtRatgs(abEmitBillVo.getPayerCdtRatgs());// 开票信用等级
		entity.setCdtRatgGgcy(abEmitBillVo.getPayerCdtRatgGgcy());// 开票评级机构
		entity.setCdtRatgDuedt(abEmitBillVo.getPayerCdtRatgDuedt());// 开票评级到期日
		entity.setBillFlag(abEmitBillVo.getBillFlag());// 流通介质
		// entity.setReqRole(reqRole);//业务申请人类别
		entity.setReqNm(abEmitBillVo.getPayer());// 业务申请人
		// entity.setReqCmonid(abEmitBillVo.getPayer());//业务申请人组织机构代码
		entity.setReqAcctId(abEmitBillVo.getPayeracc());// 业务申请人账号
		entity.setReqAcctsvcr(abEmitBillVo.getPayerbankno());// 业务申请人开户行号
		// entity.setReqAcctsvcrAg(abEmitBillVo.getPayerbankno());//业务申请人承接行号
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
			entity.setRecAcctId(abEmitBillVo.getPayeeacc1());// 业务接收人账号
			if (StringUtils.isNotBlank(abEmitBillVo.getPayeeacc1())) {
				try {
					BtBankAccDTO btBankAccDTO = getBtBankAccDTO(abEmitBillVo.getPayeeacc1());
					if (btBankAccDTO != null) {
						entity.setRecNm(btBankAccDTO.getAccName());// 业务接收人名称
					}
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 收票人账号不存在
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTEXIST);
					} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
							.equals(e.getIMessageEnum())) {
						// 收票人账号不是票据专户
						throw new BusinessException(
								AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTELECTRICBILL);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			}else{
				entity.setRecNm(abEmitBillVo.getPayee());// 业务接收人名称
			}
		} else {
			entity.setRecNm(abEmitBillVo.getPayee());// 业务接收人名称
			entity.setRecAcctId(abEmitBillVo.getPayeeacc());// 业务接收人账号
		}
		// entity.setRecRole(abEmitBillVo.getRmk());//业务接收人类别
		// entity.setRecCmonid(abEmitBillVo.getPayee());//业务接收人组织机构代码
		entity.setRecAcctsvcr(abEmitBillVo.getPayeebankno());// 业务接收人开户行号
		// entity.setRecAcctsvcrAg(abEmitBillVo.getPayee());//业务接收人承接行号
		if (abEmitBillVo.getItemCode() != null && abEmitBillVo.getItemCode().length > 0) {
			// 添加预算科目代码，多个代码以","号隔开
			entity.setBudgetCode(StringUtils.join(abEmitBillVo.getItemCode(), ","));
		}
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			if (abEmitBillVo.isInterfaceCall()) {
				// 接口生成数据时，不设置创建人
				entity.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);
			}
			/*
			 * entity.setCreateName(getUserVo().getUsername());//创建人名称
			 * entity.setCreateBy(getUserVo().getLoginName());//创建人登录名称
			 * entity.setCreateDate(new Date());//创建日期
			 */
		} else {
			if (userVo != null) {
				entity.setUpdateName(userVo.getUsername());// 修改人名称
				entity.setUpdateBy(userVo.getLoginName());// 修改人登录名称
			}
			entity.setUpdateDate(new Date());// 修改日期
		}
		//付款办理开票标志
		if(AccebillConstant.PAY_MONEY_OPEN_BILL_FLG.equals(abEmitBillVo.getIsPayFlg())){
			entity.setExcId(AccebillConstant.PAY_MONEY_OPEN_BILL_FLG);
		}
		return entity;
	}
	/**
	 * 根据账号获取账户信息--票据专户
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccDTO(String bankAcc) {
		BtBankAccDTO bankAccDTO = null;
		BtBankAccDTO params = new BtBankAccDTO();
		List<BtBankAccDTO> bankaccInfoList =null;
		if (StringUtils.isBlank(bankAcc)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
		}
		/*
		modify zouxs ，没必要重复查询
		if(StringUtils.isNotEmpty(bankAcc)){
			params.setBankAcc(bankAcc);
			bankaccInfoList=abBtBankAccService.getBankAccList(params);
		}
		if (bankaccInfoList == null || bankaccInfoList.isEmpty()) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
		}*/
		params.setBankAcc(bankAcc);
		params.setElectricBill("1");
		List<BtBankAccDTO> bankaccElectricbill = abBtBankAccService.getBankAccList(params);
		if (bankaccElectricbill != null && !bankaccElectricbill.isEmpty()) {
			bankAccDTO = bankaccElectricbill.get(0);
		} else {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST);
		}
		return bankAccDTO;
	}
	/**
	 * 根据账号获取账户信息,用于EXCEL导入校验--票据专户
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccDTOExcel(String bankAcc) {
		BtBankAccDTO bankAccDTO = null;
		BtBankAccDTO params = new BtBankAccDTO();
		params.setBankAcc(bankAcc);
		params.setElectricBill("1");
		List<BtBankAccDTO> bankaccInfoList = abBtBankAccService.getBankAccList(params);
		if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
			bankAccDTO = bankaccInfoList.get(0);
		}
		return bankAccDTO;
	}
	/**
	 * 根据银行账号获取银行账号信息--票据专户 用于付款生成的开票登记，编辑时选择对内时校验收票人账号是否存在，
	 *
	 * @param bankAcc
	 *            银行账号
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean getBankDataByBankAcc(String bankAcc) {
		BtBankAccDTO params = new BtBankAccDTO();
		params.setBankAcc(bankAcc);
		params.setElectricBill("1");
		List<BtBankAccDTO> btBankList = abBtBankAccService.getBankAccList(params);
		return btBankList != null && !btBankList.isEmpty();
	}
	/**
	 * 票据号重复校验
	 *
	 * @param origNo
	 *            票据号
	 * @param opreate
	 *            新增修改标志
	 */
	@Transactional(readOnly = true)
	protected void checkOrigNo(String corpId, String origNo, String vid, String opreate, String sbbllRngStrtSn, String sbbllRngEndSn) {
		if (StringUtils.isNotEmpty(origNo)) {
			if (AccebillConstant.BILL_OPERATE_ADD.equals(opreate)) {
				// 新增
				if (abVoucherinfoService.checkOrigNo(corpId,origNo,AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,sbbllRngStrtSn,sbbllRngEndSn)) {
					// 该票据号已经存在,请检查!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ORIGNO_REPEAT);
				}
			} else {
				// 修改校验
				if (abVoucherinfoService.checkOrigNo(corpId,origNo, vid,AccebillConstant.AB_VOUCHERINFO_OP_TYPE0)) {
					// 该票据号已经存在,请检查!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ORIGNO_REPEAT);
				}
			}
		}
	}
	/**
	 * 审批
	 *
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void approve(String vid, String vvid, String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		int num = 0;
		abEmitBillDao.flush();
		abEmitBillDao.clear();
		AbAccrecords accRecords = abAccrecordsService.get(aid, true);
		// 从表box_status改为11
		abVoucherinfoViceService.update(vvid, AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BOX_STATUS_N,
				userVo);
		if (isPassOK) {
			/** 审批通过 */
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
			if(!abVoucherinfoViceService.isEmitMake()){//不需要补录功能则开票通过就占用预算和占用授信
				// 入库
//				B20200400346 在线电票出票申请不写入库存
				if(!AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(accRecords.getSendSign())){
					abEmitBillSupplyService.batchStorage(accRecords.getVid(), accRecords.getVvid(), aid, userVo);
				}
				/** 占用预算 11改为95 */
				fbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
						BudgetConstant.PROCESS_SYSNAME_AB, accRecords.getVvid(), BudgetConstant.STATUE_CHECK,userVo);
				//设置离线票据的票据状态和流通标志，并返回从票数据
				AbVoucherinfoVice abVoucherinfoVice = AbAccebillUtils.setOffLineVoucherViceBillStatus
						(abVoucherinfoService,accRecords.getVid(),accRecords.getVvid()
						,accRecords.getSendSign(),abVoucherinfoViceService,AccebillConstant.AB_BILL_STATUS_010004, AccebillConstant.NEWBILL_STATUS_CS01, AccebillConstant.CIRCULATE_TF0101);

				// 付款单付款完成
				if (abVoucherinfoViceService.get(accRecords.getVvid()) != null) {
					String voucherId = abVoucherinfoViceService.get(accRecords.getVvid()).getVoucherId();
					//付款开票不在这里调用付款完成方法，因为会导致死锁，在付款办理审批方法调用
					if (StringUtils.isNotEmpty(voucherId) && !AccebillConstant.PAY_MONEY_OPEN_BILL_FLG.equals(accRecords.getExcId())) {
						// 99 付款完成
						abNisBillHeadService.updateNisBillhead(voucherId, NetbankConstant.STATUE_BILL_COMPLETE, null,userVo);
					}
				}

				if(StringUtils.isNotBlank(abVoucherinfoVice.getVoucherId())){
					List<FbsBudgetProcessDTO> list = this.abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
							(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,abVoucherinfoVice.getVoucherId());
					if(list!=null && !list.isEmpty ()){
						abFbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,
								list.get(0).getBillNo(), BudgetConstant.STATUE_CHECK,userVo);
					}
				}
				// 挂接授信
				abRatUrationService.opRatUration(accRecords, AccebillConstant.BILL_OPERATE_APPOK,userVo);
				//挂接保证金 直接占用保证金
				//abFcBondHookService.updateFcBondApply(abVoucherinfoVice,userVo,FcBondConstant.STATUE_APPLY);
				// 挂接担保
				AbEmitBillVo abEmitBillVo = this.getAbEmitBearVoByAid(accRecords.getId());
				abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_APPOK,abEmitBillVo,userVo);
				//挂接内部担保
				abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_APPOK, accRecords,userVo);
			}else{
				abBillNotificationService.sendMsg(aid, AccebillConstant.BRIEF_STATUS_6101);
			}
			//开票登记完成，票据离线处理设置票据状态
			if(!AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(accRecords.getSendSign())){
				AbVoucherinfo voucherinfo = abVoucherinfoService.get(vid,true);
				Map<String,Object> params=new HashMap<>();
				params.put("ids",new String[]{vvid});
				params.put("billStatusNew",AccebillConstant.AB_BILL_STATUS_030006);
				if("1".equals(voucherinfo.getNewStmIssuofpprInd())){
					params.put("billStatusNew",AccebillConstant.NEWBILL_STATUS_CS03);
					if(AccebillConstant.AB_BANEM_Y.equals(voucherinfo.getBanendrsmtmk())) {
						params.put("ebillBlCrclIndcNew", AccebillConstant.CIRCULATE_TF0301);
					}else{
						params.put("ebillBlCrclIndcNew", AccebillConstant.CIRCULATE_TF0303);
					}
				}
				abVoucherinfoViceService.batchUpdate(params);
			}
		} else {
			/** 打回 */
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS_1, userVo);
			// 从表添加打回标记--开票登记无需标记
			// 资金计划在途改为暂存
			abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid, BudgetConstant.STATUE_BACK,userVo);
			// 打回授信
			abRatUrationService.opRatUration(accRecords, AccebillConstant.BILL_OPERATE_BACK,userVo);
			// 打回担保
			AbEmitBillVo abEmitBillVo = this.getAbEmitBearVoByAid(accRecords.getId());
			abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_BACK,abEmitBillVo,userVo);
			//打回内部授信
			abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_BACK, accRecords,userVo);
		}
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		abLogService.saveAbLog(vvid, aid, AccebillConstant.BRIEF_STATUS_001, logType, appMsg, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILL, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbEmitBillVo> dp = abEmitBillDao.dataTableApprove(dataTablesInput, businessList);
		if (ObjectUtil.isNotEmpty(dp.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_ABEMITBILLSUPPLY_STATUS", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbEmitBillVo abEmitBillVo : dp.getData()) {
				injectValues(abEmitBillVo, listDictMap);
			}
		}
		return workFlowBusiness.merge(dp);
	}

	/**
	 * 招行合作银行号查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput<CmbBnkbilnegVo> dataTableCooperativeBankNumber(DataTablesInput dataTablesInput) {
		return abEmitBillDao.dataTableCooperativeBankNumber(dataTablesInput);
	}

	/**
	 * 添加招行风险合同信息表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput<CmbCrlconVo> dataTableCMBCRLCONInfo(DataTablesInput dataTablesInput) {
		return abEmitBillDao.dataTableCMBCRLCONInfo(dataTablesInput);
	}

	/**
	 * 招行协议文本信息查询
	 *
	 * @param cvmNO
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public CmbCrlinfoVo selectCmbCrlinfoByCVMNo(String cvmNO) {
		return abEmitBillDao.selectCmbCrlinfoByCVMNo(cvmNO);
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
	 *  processDefinitionKey
	 *            工作流类型key
	 *  businessKey
	 *            业务类型key
	 *  result
	 *            审批结果:true审批通过，false审批打回
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		AbAccrecords abAccrecords = abAccrecordsService.get(workFlowResult.getBusinessKey());
		approve(abAccrecords.getVid(), abAccrecords.getVvid(), workFlowResult.getBusinessKey(), appMsg, workFlowResult.isPass(), workFlowResult.getUser());
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbEmitBillVo obj) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbEmitBillVo> list) {
		if (list != null && !list.isEmpty()) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND",
					"AB_BILL_FLAG","AB_BILL_OPENWAY","AB_BANEM","sf_yn");

			List<String> errorMsgList = null;
			BtBankAccDTO btBankAccDTO = null;
			CorpDTO corpDTO = null;
			Calendar calendar = null;
			boolean isEmitMake=abVoucherinfoViceService.isEmitMake();
			Map<String, DictDTO> billTypeDictMap=abDictService.listMap(AccebillConstant.AB_BILL_TYPE_DICT);
			Map<String, DictDTO> emitTypeDictMap=abDictService.listMap(AccebillConstant.AB_EMIT_TYPE_DICT);
			//是否挂预算
			boolean budgetSel =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).toBoolean();
			//单位计划上报集合
			HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
			//计划科目
			HashMap<String, FbsItemDTO> fbsItemMap = new HashMap<>();
			Set<String> origNo=new HashSet<>();
			List<String> bursarBillNoList = new ArrayList<>();
			for (AbEmitBillVo abEmitBillVo : list) {
				if(StrUtil.isNotEmpty(abEmitBillVo.getOrigNoNew())
						&&StrUtil.isEmpty(abEmitBillVo.getOrigNo())){
					abEmitBillVo.setOrigNo(abEmitBillVo.getOrigNoNew());
				}

				//保证金信息初始化
				if (abEmitBillVo.getBailpercent() != null && abEmitBillVo.getBillMoney() != null) {
					//如果保证金为空
					if (abEmitBillVo.getBailmoney() == null){
						BigDecimal num = new BigDecimal(100);
						//保证金为：票据金额*保证金比例
						BigDecimal bailpercent = abEmitBillVo.getBailpercent().divide(num, 6, BigDecimal.ROUND_HALF_UP);
						BigDecimal billMoney = abEmitBillVo.getBillMoney();
						BigDecimal multiply = billMoney.multiply(bailpercent).setScale(2 , RoundingMode.CEILING);
						abEmitBillVo.setBailmoney(multiply);
					}
				}
				//手续费初始化
				if ((abEmitBillVo.getFeemoney() != null||abEmitBillVo.getFeemoneyScale() != null)
						&& abEmitBillVo.getBillMoney() != null) {
					//如果手续费为空
					if (abEmitBillVo.getFeemoney() == null){
						BigDecimal num = new BigDecimal(100);
						//手续费为：票据金额*手续费比例
						BigDecimal feemoney = abEmitBillVo.getFeemoneyScale().divide(num, 6, BigDecimal.ROUND_HALF_UP);
						BigDecimal billMoney = abEmitBillVo.getBillMoney();
						feemoney = billMoney.multiply(feemoney).setScale(2 , BigDecimal.ROUND_HALF_UP);
						abEmitBillVo.setFeemoney(feemoney);
					}
					//如果手续费比例为空
					if (abEmitBillVo.getFeemoneyScale() == null){
						BigDecimal num = new BigDecimal(100);
						//为：手续费比例=手续费/票据金额*100
						BigDecimal feemoneyScale = abEmitBillVo.getFeemoney().multiply(num).setScale(6 , BigDecimal.ROUND_HALF_UP);
						feemoneyScale = feemoneyScale.divide(abEmitBillVo.getBillMoney()).setScale(2 , BigDecimal.ROUND_HALF_UP);
						abEmitBillVo.setFeemoneyScale(feemoneyScale);
					}
				}

				errorMsgList = new ArrayList<>();
				String isOnline="0";
//				if(StrUtil.isEmpty(abEmitBillVo.getBursarBillNo())){
//					errorMsgList.add("共享结算单号不允许为空！");
//				}
				if(StrUtil.isNotEmpty(abEmitBillVo.getBursarBillNo())){
					if(bursarBillNoList.contains(abEmitBillVo.getBursarBillNo())){
						errorMsgList.add("共享结算单号不允许重复！");
					}else {
						bursarBillNoList.add(abEmitBillVo.getBursarBillNo());
					}
				}

				/** 申请单位代码和申请单位名称校验 */
				if (StringUtils.isEmpty(abEmitBillVo.getApplyCorpCode())
						&& StringUtils.isEmpty(abEmitBillVo.getApplyCorpName())) {
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL);
				} else {
					if (StringUtils.isEmpty(abEmitBillVo.getNewStmIssuofpprIndName())) {
						errorMsgList.add("出票标志不能为空");
					}else {
						abEmitBillVo.setNewStmIssuofpprInd(abEmitBillVo.getNewStmIssuofpprIndName());
						Optional.ofNullable(abEmitBillVo.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
							Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
							Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd + "")))
									.ifPresent(dict -> abEmitBillVo.setNewStmIssuofpprIndName(dict.getName()));
						});
					}
					if (StringUtils.isEmpty(abEmitBillVo.getBlgrdbagcrclPrmtIndName())) {
						errorMsgList.add("是否允许分包标志不能为空");
					}else {
						abEmitBillVo.setBlgrdbagcrclPrmtInd(abEmitBillVo.getBlgrdbagcrclPrmtIndName());
						// 是否允许分包
						Optional.ofNullable(abEmitBillVo.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtInd -> {
							Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
							Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtInd)))
									.ifPresent(dict -> abEmitBillVo.setBlgrdbagcrclPrmtIndName(dict.getName()));
						});
					}
					if (StringUtils.isNotEmpty(abEmitBillVo.getApplyCorpCode())) {
						// 根据申请单位代码获取单位信息
						corpDTO = abCorpService.getCorpByCode(abEmitBillVo.getApplyCorpCode());
						if (corpDTO != null) {
							if (StringUtils.isNotEmpty(abEmitBillVo.getApplyCorpName())
									&& !abEmitBillVo.getApplyCorpName().equals(corpDTO.getName())) {
								errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_APPLYCORPNAME_ISNOT);
							} else {
								abEmitBillVo.setApplyCorpName(corpDTO.getName());
							}
						} else {
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_APPLYCORPCODE_ERROR);
						}
					} else {
						// 只填单位名称
						corpDTO = abCorpService.getCorpIdByName(abEmitBillVo.getApplyCorpName());
						if (corpDTO == null) {
							errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST);
						}else{
							abEmitBillVo.setApplyCorpCode(corpDTO.getCode());
						}
					}
					/** 根据corpName获取corpId */
					if (corpDTO!=null) {
						abEmitBillVo.setApplyCorpId(corpDTO.getId());
					}
				}
				corpDTO=null;
				/** 票据单位代码和票据单位名称校验 */
				if (StringUtils.isEmpty(abEmitBillVo.getCorpCode())
						&& StringUtils.isEmpty(abEmitBillVo.getCorpName())) {
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
				} else {
					if (StringUtils.isNotEmpty(abEmitBillVo.getCorpCode())) {
						// 根据所属单位代码获取单位信息
						corpDTO = abCorpService.getCorpByCode(abEmitBillVo.getCorpCode());
						if (corpDTO != null) {
							if (StringUtils.isNotEmpty(abEmitBillVo.getCorpName())
									&& !abEmitBillVo.getCorpName().equals(corpDTO.getName())) {
								errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPNAME_ISNOT);
							} else {
								abEmitBillVo.setCorpName(corpDTO.getName());
							}
						} else {
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPCODE_ERROR);
						}
					} else {
						// 只填单位名称
						corpDTO = abCorpService.getCorpIdByName(abEmitBillVo.getCorpName());
						if (corpDTO == null) {
							errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_CORPID_ISNOTEXIST);
						}else{
							abEmitBillVo.setCorpCode(corpDTO.getCode());
						}
					}
					/** 根据corpName获取corpId */
					if (corpDTO!=null) {
						abEmitBillVo.setCorpId(corpDTO.getId());
					}
				}
				//业务单位
				if (StringUtils.isNotEmpty(abEmitBillVo.getBusCorpName())) {
					CorpDTO busCorpDTO = abCorpService.getCorpIdByName(abEmitBillVo.getBusCorpName());
					if (busCorpDTO == null) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_BUSCORPID_ISNOTEXIST);
					}else{
						abEmitBillVo.setBusCorpId(busCorpDTO.getId());
					}
				}
				/** 流通介质校验 */
				if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(abEmitBillVo.getBillFlagName())
						|| AccebillConstant.AB_BILL_FLAG_ELECT.equals(abEmitBillVo.getBillFlagName())) {
					abEmitBillVo.setBillFlag(Integer.parseInt(abEmitBillVo.getBillFlagName()));

					// 流通介质：10 纸票、11 电票
					Optional.ofNullable(abEmitBillVo.getBillFlag()).ifPresent(billFlag -> {
						Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
						Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
								.ifPresent(dict -> abEmitBillVo.setBillFlagName(dict.getName()));
					});
				} else {
					errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BILLFLAG_ISNOTEXIST);
				}
				/** 日期校验:开票日期，到期日期，业务办理日期校验 */
				if (abEmitBillVo.getStartDate() != null && abEmitBillVo.getEndDate() != null) {
					if (abEmitBillVo.getStartDate().getTime() >= abEmitBillVo.getEndDate().getTime()) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_COMSTARTTOEND);
					}
					if (abEmitBillVo.getBusinessDate() != null && (abEmitBillVo.getBusinessDate()
							.getTime() < abEmitBillVo.getStartDate().getTime()
							|| abEmitBillVo.getBusinessDate().getTime() >= abEmitBillVo.getEndDate().getTime())) {
						errorMsgList.add(AccebillConstant.BILL_ACCEBILL_ABRECEIVEBILL_CHECKBUSINESSDATE);
					}
					//去掉日期有效期校验
				}
				/** 票据类型校验 */
				/*if (AccebillConstant.AB_BILL_TYPE_BANK.equals(abEmitBillVo.getBillTypeName())
						|| AccebillConstant.AB_BILL_TYPE_MERCH.equals(abEmitBillVo.getBillTypeName())) {
					abEmitBillVo.setBillType(Integer.parseInt(abEmitBillVo.getBillTypeName()));
				} else {
					errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BILLTYPE_ISNOTEXIST);
				}*/
				// 票据类型不存在进行异常提醒
				if (StringUtils.isEmpty(abEmitBillVo.getBillTypeName())) { // 票据类型
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ISNOTNULL);
				} else if (!billTypeDictMap.containsKey(abEmitBillVo.getBillTypeName())) {
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ERROR);
				} else {
					abEmitBillVo.setBillType(Integer.parseInt(billTypeDictMap.get(abEmitBillVo.getBillTypeName()).getCode()));
					abEmitBillVo.setBillTypeName(billTypeDictMap.get(abEmitBillVo.getBillTypeName()).getName());
				}
				// 开票方式不存在进行异常提醒
				if (StringUtils.isNotEmpty(abEmitBillVo.getEmitTypeName())
						&&!emitTypeDictMap.containsKey(abEmitBillVo.getEmitTypeName())) {
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_EMITTYPE_ERROR);
				} else if (emitTypeDictMap.containsKey(abEmitBillVo.getEmitTypeName())){
					abEmitBillVo.setEmitType(emitTypeDictMap.get(abEmitBillVo.getEmitTypeName()).getCode());
					abEmitBillVo.setEmitTypeName(emitTypeDictMap.get(abEmitBillVo.getEmitTypeName()).getName());
				}
				//默认开出方式为信用和质押开票不需要挂接银行授信
				if(AccebillConstant.AB_EMIT_TYPE_10.equals(abEmitBillVo.getEmitType())||AccebillConstant.AB_EMIT_TYPE_30.equals(abEmitBillVo.getEmitType())){
					abEmitBillVo.setRatmodel(AccebillConstant.AB_RATMODEL_SING_N);
				}
				/** 开出方式校验 */
				if (AccebillConstant.AB_INSIDE_SING_Y.equals(abEmitBillVo.getInsideSingName())
						|| AccebillConstant.AB_INSIDE_SING_N.equals(abEmitBillVo.getInsideSingName())) {
					abEmitBillVo.setInsideSing(Integer.parseInt(abEmitBillVo.getInsideSingName()));
					// 开出方式：0  对外开出；1 对内自开
					Optional.ofNullable(abEmitBillVo.getInsideSing()).ifPresent(insideSing -> {
						Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
						Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
								.ifPresent(dict -> abEmitBillVo.setInsideSingName(dict.getName()));
					});
				} else {
					errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_INSIDESING_ISNOTEXIST);
				}
				/** 金额校验 */
				if (abEmitBillVo.getBillMoney() != null) {
					// 金额最小为0.01
					if (abEmitBillVo.getBillMoney().compareTo(BigDecimal.valueOf(0.01)) < 0) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLMONEYMIX);
					}
					// 电票不能超过10亿
					if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
							&& (abEmitBillVo.getBillMoney().compareTo(BigDecimal.valueOf(1000000000.00)) > 0)) {
						errorMsgList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11);
					}
					// 纸票金额不能超过设置的额度
					if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillVo.getBillFlag()))
							&& (abEmitBillVo.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
						errorMsgList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY);
					}
				}
				/** 出票人账号校验 */
				if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
						&&StringUtils.isEmpty(abEmitBillVo.getPayeracc())
						&& !AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYERACC_BILLFLAG11_ISNOTNULL);
				}
				if (StringUtils.isNotEmpty(abEmitBillVo.getCorpId())
						&& StringUtils.isNotEmpty(abEmitBillVo.getPayeracc())) {
					btBankAccDTO = getBtBankAccDTOExcel(abEmitBillVo.getPayeracc());
					if (btBankAccDTO != null) {
						if (abEmitBillVo.getCorpId().equals(btBankAccDTO.getCorpId())) {
							abEmitBillVo.setPayer(btBankAccDTO.getAccName());
							abEmitBillVo.setPayerBankName(btBankAccDTO.getBankName());
							abEmitBillVo.setPayerbankno(btBankAccDTO.getBankCode());
						} else {
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERCORP_MATCHERROR);
						}
						isOnline=btBankAccDTO.getCirculationMode();
					} else {
						errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERACC_ISNOTEXIST);
					}
				}
				if(StringUtils.isEmpty(abEmitBillVo.getPayer())){
					abEmitBillVo.setPayer(abEmitBillVo.getCorpName());
				}
				/** 收票人账号校验 */
				if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
						&&StringUtils.isEmpty(abEmitBillVo.getPayeeacc())
						&& !AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYEEACC_BILLFLAG11_ISNOTNULL);
				}
				if (StringUtils.isNotEmpty(abEmitBillVo.getPayeeacc())
						&&AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
					// 对内
					btBankAccDTO = getBtBankAccDTOExcel(abEmitBillVo.getPayeeacc());
					if (btBankAccDTO != null) {
						abEmitBillVo.setPayee(btBankAccDTO.getAccName());
						abEmitBillVo.setPayeeBankName(btBankAccDTO.getBankName());
						abEmitBillVo.setPayeebankno(btBankAccDTO.getBankCode());
						abEmitBillVo.setPayeeCorpId(btBankAccDTO.getCorpId());
					} else {
						errorMsgList.add("收票人账户不存在或者不是票据专户");
					}
				}
				if (StringUtils.isEmpty(abEmitBillVo.getPayee())) {
					errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYEE_ISNOTNULL);
				}
				if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
						&&AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abEmitBillVo.getInsideSing()))
						&& !AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
					// 电票且非第三方票据类型时对外
					if (StringUtils.isEmpty(abEmitBillVo.getPayeeBankName())) {
						errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYEEBANKNAME_ISNOTEXIST);
					}
					//当收票人开户行号未填写时根据收票人开户行名称查找银行开户行号
					if (!StringUtils.isEmpty(abEmitBillVo.getPayeeBankName()) && StringUtils.isEmpty(abEmitBillVo.getPayeebankno())) {
						InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getByBankName(abEmitBillVo.getPayeeBankName().trim());
						//若未查出则导入失败
						if(bankInfoDTO == null){
							errorMsgList.add("根据收票人开户行名称未查询到开户行号");
						}else{
							abEmitBillVo.setPayeebankno(bankInfoDTO.getBankCode());
						}
					}
					if (StringUtils.isEmpty(abEmitBillVo.getPayeebankno())) {
						errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYEEBANKCODE_ISNOTEXIST);
					}
				}
				/** 承兑人校验 */
				if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
						&& !AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
					// 电票且非第三方票据类型时承兑人不能为空
					if(AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abEmitBillVo.getBillType()))){
						if(StringUtils.isEmpty(abEmitBillVo.getAcceptor())){
							abEmitBillVo.setAcceptor(abEmitBillVo.getPayer());
							abEmitBillVo.setAcceptoracc(abEmitBillVo.getPayeracc());
							abEmitBillVo.setAcceptorbankno(abEmitBillVo.getPayerbankno());
							abEmitBillVo.setAcceptorBankName(abEmitBillVo.getPayerBankName());
						}
					}
					// 电票、银票，且承兑行名称不为空
					if(!StringUtils.isEmpty(abEmitBillVo.getAcceptorBankName())
							&& AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abEmitBillVo.getBillType()))) {
						InputBankInfoDTO inputBankInfoDTO = abInputBankInfoService.getByBankName(abEmitBillVo.getAcceptorBankName().trim());
						if (inputBankInfoDTO == null) {
							errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNULL);
						} else {
							abEmitBillVo.setAcceptorbankno(inputBankInfoDTO.getBankCode());
							abEmitBillVo.setAcceptor(inputBankInfoDTO.getBankName());
							if(StringUtils.isEmpty(abEmitBillVo.getAcceptoracc())){
								abEmitBillVo.setAcceptoracc("0");
							}
						}
					}
					// 为银票时，承兑人信息为出票人银行信息，承兑人账户为0。
					if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abEmitBillVo.getBillType()))) {
						if (StringUtils.isEmpty(abEmitBillVo.getAcceptor())) {
							abEmitBillVo.setAcceptor(abEmitBillVo.getPayerBankName());
							abEmitBillVo.setAcceptoracc("0");
							abEmitBillVo.setAcceptorbankno(abEmitBillVo.getPayerbankno());
							abEmitBillVo.setAcceptorBankName(abEmitBillVo.getPayerBankName());
						}
					}

					if (StringUtils.isEmpty(abEmitBillVo.getAcceptoracc())) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL);
					}
					if (StringUtils.isEmpty(abEmitBillVo.getAcceptor())) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL);
					}
					if (StringUtils.isEmpty(abEmitBillVo.getAcceptorbankno())) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNO_BILLFLAG11_ISNOTNULL);
					}
					if (StringUtils.isEmpty(abEmitBillVo.getAcceptorBankName())) {
						errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNAME_BILLFLAG11_ISNOTNULL);
					}
				}
				/** 不得转让标记校验 */
				if (AccebillConstant.AB_BANEM_Y.equals(abEmitBillVo.getBanendrsmtmkName())
						|| AccebillConstant.AB_BANEM_N.equals(abEmitBillVo.getBanendrsmtmkName())) {
					abEmitBillVo.setBanendrsmtmk(abEmitBillVo.getBanendrsmtmkName());
					// 不得转让标记：EM00可再转让EM01不得转让
					Optional.ofNullable(abEmitBillVo.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
						Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
						Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
								.ifPresent(dict -> abEmitBillVo.setBanendrsmtmkName(dict.getName()));
					});
				} else {
					errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BANENDRSMTMK_ISNOTEXIST);
				}
				if(!isEmitMake){
					if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abEmitBillVo.getBillFlag()))
							&& !AbAccebillUtils.isBillTypeTP(abEmitBillVo.getBillType())){
						if(!"1".equals(isOnline)&&StringUtils.isEmpty(abEmitBillVo.getOrigNo())){
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ORIGNO_NULL);
						}
					}else{
						if (StringUtils.isEmpty(abEmitBillVo.getAcceptor())) {
							errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL);
						}
						if (StringUtils.isEmpty(abEmitBillVo.getOrigNo())) {
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ORIGNO_NULL);
						}
					}
				}
				/** 票据号重复校验 */
				if (StringUtils.isNotEmpty(abEmitBillVo.getOrigNo())) {
					if(origNo.contains(abEmitBillVo.getOrigNo())){
						errorMsgList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
					}else if (abVoucherinfoService.checkOrigNo(abEmitBillVo.getCorpId(), abEmitBillVo.getOrigNo(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE0,
							abEmitBillVo.getSbbllRngStrtSn(),abEmitBillVo.getSbbllRngEndSn())) {
						errorMsgList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
					}else{
						origNo.add(abEmitBillVo.getOrigNo());
					}
				}
				//当票据为银票时，且承兑人信息为空，则默认把出票人开户银行信息填入承兑人信息中
				if(AccebillConstant.AB_BILL_TYPE_BANK.equals(abEmitBillVo.getBillTypeName())
						&& StringUtils.isEmpty(abEmitBillVo.getAcceptor())
						&& StringUtils.isEmpty(abEmitBillVo.getAcceptoracc())
						&& StringUtils.isEmpty(abEmitBillVo.getAcceptorbankno())
						&& StringUtils.isEmpty(abEmitBillVo.getAcceptorBankName())){
					abEmitBillVo.setAcceptor(abEmitBillVo.getPayerBankName());
					abEmitBillVo.setAcceptoracc("0");
					abEmitBillVo.setAcceptorbankno(abEmitBillVo.getPayerbankno());
					abEmitBillVo.setAcceptorBankName(abEmitBillVo.getPayerBankName());
				}
				//添加是否保证信息
				if(StringUtils.isNotEmpty(abEmitBillVo.getIsEnsureName())
						&&("是".equals(abEmitBillVo.getIsEnsureName())
						||"1".equals(abEmitBillVo.getIsEnsureName()))){
					abEmitBillVo.setIsEnsure("1");
				}else{
					abEmitBillVo.setIsEnsure("0");
				}
				//添加是否授信提用信息
				if (StringUtils.isNotEmpty(abEmitBillVo.getIsCreditDraw())
						&& ("是".equals(abEmitBillVo.getIsCreditDraw())
						|| "1".equals(abEmitBillVo.getIsCreditDraw()))) {
					abEmitBillVo.setIsCreditDraw("是");
				} else {
					abEmitBillVo.setIsCreditDraw("否");
				}
				//添加是否敞口信息
				if (StringUtils.isNotEmpty(abEmitBillVo.getIsOpenRatName())
						&& ("1".equals(abEmitBillVo.getIsOpenRatName())
						|| "0".equals(abEmitBillVo.getIsOpenRatName()))) {
					abEmitBillVo.setIsOpenRat(abEmitBillVo.getIsOpenRatName());
					Optional.ofNullable(abEmitBillVo.getIsOpenRat()).ifPresent(isOpenRat -> {
						Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
						Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isOpenRat)))
								.ifPresent(dict -> abEmitBillVo.setIsOpenRatName(dict.getName()));
					});
				}
				// 是否挂接资金计划
				if(budgetSel && StringUtils.isNotEmpty(abEmitBillVo.getCorpId())){
					Boolean reportBudget = reportBudgetMap.get(abEmitBillVo.getCorpId());
					if (reportBudget == null) {
						reportBudget = abVoucherinfoViceService.isBudget(abEmitBillVo.getCorpId(), AccebillConstant.BRIEF_STATUS_001);
						reportBudgetMap.put(abEmitBillVo.getCorpId(), reportBudget);
					}
					if (reportBudget && StringUtils.isEmpty(abEmitBillVo.getItemCodeStr())) {
						errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST);
					}else if(reportBudget && StringUtils.isNotEmpty(abEmitBillVo.getItemCodeStr())){
						FbsItemDTO fbsItemDTO = fbsItemMap.get(abEmitBillVo.getItemCodeStr());
						//判断计划科目是否存在
						if(fbsItemDTO==null){
							fbsItemDTO = abFbsItemService.getFbsItemByCode(abEmitBillVo.getItemCodeStr());
							fbsItemMap.put(abEmitBillVo.getItemCodeStr(), fbsItemDTO);
						}
						if (EmptyUtils.isEmpty(fbsItemDTO)) {
							errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNULL);
						}else{
							// 判断资金计划科目代码是否属于该业务
							if (abAccrecordsService.itemCodeIsMatchBusiness(AccebillConstant.AB_CONFIG_EMITITEM, fbsItemDTO.getItemCode())) {
								errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISMATCH_BUSINESS);
							} else {
								abEmitBillVo.setItemCode(new String[]{abEmitBillVo.getItemCodeStr()});
								abEmitBillVo.setItemName(new String[]{fbsItemDTO.getItemName()});
								abEmitBillVo.setProcessMoney(new BigDecimal[]{abEmitBillVo.getBillMoney()});
								abEmitBillVo.setProcessCurMoney(new BigDecimal[]{abEmitBillVo.getBillMoney()});
								abEmitBillVo.setExchangeRate(new BigDecimal[]{new BigDecimal(1)});
							}
						}
					}
				}
				abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE11);

				//保证金校验
				if(StrUtil.isNotEmpty(abEmitBillVo.getBailAcc())){
					abEmitBillVo.setBailmodel(FcBondConstant.IS_HOOK_Y);
					if (abEmitBillVo.getBailpercent() != null) {
//                            abEmitBillVo.setBailpercent(abEmitBillVo.getBailpercent().divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP));// 保证金比例
						//如果保证金为空
						if (abEmitBillVo.getBailmoney() == null){
							//保证金为：票据金额*保证金比例
							BigDecimal bailpercent = abEmitBillVo.getBailpercent().divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP);
							BigDecimal billMoney = abEmitBillVo.getBillMoney();
							BigDecimal multiply = billMoney.multiply(bailpercent).setScale(2,RoundingMode.CEILING);
							abEmitBillVo.setBailmoney(multiply);
						}
					}
					if(abEmitBillVo.getBailmoney()!=null){

						FcBondDTO fcBondDTO = abFcBondHookService.getFcBondMsgByBondBankAcc(abEmitBillVo.getBailAcc());
						if(fcBondDTO!=null){
//								errorMsgList.add(abEmitBillVo.getBailAcc()+"不存在存入的保证金信息！");
////							}else{
							abEmitBillVo.setBondId(fcBondDTO.getId());
						}else{
							//查询是否为有效的账户类别为保证金的银行账户
							BtBankAccDTO bankAccDTO = abBtBankAccService.getBankAccInfo(abEmitBillVo.getBailAcc());
							if(!EmptyUtils.isEmpty(bankAccDTO)){
								String accType = bankAccDTO.getAccType();
								if(EmptyUtils.isEmpty(accType) || !accType.equals(BtBankAccConstant.ACCTYPE_04)){
									errorMsgList.add("该保证金账号已存在其他类型的有效银行账户！");
								}
							}
						}
					}else{
						errorMsgList.add("挂接保证金保证金金额不允许为空！");
					}
				}
				//银行授信校验
				if(StrUtil.isNotEmpty(abEmitBillVo.getCreditCode()) && StrUtil.isNotEmpty(abEmitBillVo.getApplyCorpId())){
					if(StrUtil.isEmpty(abEmitBillVo.getRatItemName())){
						errorMsgList.add("挂接银行授信授信品种不允许为空！");
					}else {
						RatUrationDisDTO ratUrationDisDTO = abRatUrationService.getRatUrationDTOByParams(abEmitBillVo.getApplyCorpId(), abEmitBillVo.getCreditCode(), abEmitBillVo.getRatItemName());
						if (ratUrationDisDTO == null
								|| CollectionUtil.isEmpty(ratUrationDisDTO.getRationDlts())) {
							errorMsgList.add("单位不存在此授信合同编码【" + abEmitBillVo.getCreditCode() + "】的【"+abEmitBillVo.getRatItemName()+"】银行授信信息！");
						} else {
							RatUrationDTO ratUrationDTO = new RatUrationDTO();
							abEmitBillVo.setRatBankCorpId(abEmitBillVo.getApplyCorpId());
							ratUrationDTO.setRatCode(ratUrationDisDTO.getRatCode());
							ratUrationDTO.setBillMoney(abEmitBillVo.getBillMoney());
							ratUrationDTO.setCorpId(abEmitBillVo.getApplyCorpId());
							ratUrationDTO.setRatItemCode(ratUrationDisDTO.getRationDlts().get(0).getItemCode());
							ratUrationDTO.setInitCurCode(abVoucherinfoViceService.getCurIdByCNY());
							ratUrationDTO.setBondCurId(ratUrationDTO.getInitCurCode());
							ratUrationDTO.setContactNo(abEmitBillVo.getCreditCode());
							ratUrationDTO.setIsOpenRat(abEmitBillVo.getIsOpenRat());
							if ("1".equals(ratUrationDisDTO.getRationDlts().get(0).getItemFlag())) {
								List<RatUrationDtlDTO> applyDtls = new ArrayList<>();
								RatUrationDtlDTO applyDtl = new RatUrationDtlDTO();
								applyDtl.setItemCode(ratUrationDisDTO.getRationDlts().get(0).getItemCode());
								applyDtl.setItemMoney(abEmitBillVo.getBillMoney());
								applyDtls.add(applyDtl);
								ratUrationDTO.setApplyDtls(applyDtls);
							}
							abEmitBillVo.setUrationDTO(ratUrationDTO);
							abEmitBillVo.setRatmodel(AccebillConstant.AB_BILL_RATMODEL_Y);
						}
					}
				}
				if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())
						&&"1".equals(abEmitBillVo.getIsOpenRat())
						&&(abEmitBillVo.getBailmoney()==null||abEmitBillVo.getBailmoney().compareTo(BigDecimal.ONE)<=0)){
					errorMsgList.add("是否敞口为是必须挂接银行授信且保证金大于0！");
				}

				abEmitBillVo.addErrorMsg(errorMsgList);
			}

			//对共享结算单号进行处理
			if(CollectionUtil.isNotEmpty(bursarBillNoList)){
				List<NisBillheadDTO> nisBillheadDTOS = abNisBillHeadService.listNisBillheadByBursarBillNos(bursarBillNoList);
				Map<String,NisBillheadDTO> bursarBillNoMap = new HashMap<>();
				if(CollectionUtil.isNotEmpty(nisBillheadDTOS)){
					nisBillheadDTOS.forEach(info->{
						bursarBillNoMap.put(info.getBursarBillNo(),info);
					});
				}
				NisBillheadDTO nisBillheadDTO = null;
				//对未设置共享结算单号进行处理
				for (AbEmitBillVo abEmitBillVo : list) {
					errorMsgList = new ArrayList<>();
					nisBillheadDTO = bursarBillNoMap.get(abEmitBillVo.getBursarBillNo());
					if(StrUtil.isEmpty(abEmitBillVo.getOrigNo())){
						errorMsgList.add("付款批量导入，票据号不允许为空！");
					}
					if(StrUtil.isEmpty(abEmitBillVo.getBursarBillNo())){
						errorMsgList.add("共享结算单号不允许为空！");
					}else if(nisBillheadDTO==null){
						errorMsgList.add("共享结算单号付款单不存在！");
					}else if(nisBillheadDTO.getStatus()!=null && !(nisBillheadDTO.getStatus()==0 || nisBillheadDTO.getStatus()==-1)){
						errorMsgList.add("共享结算单号付款单必须为暂存或者打回状态！");
					}
					if(nisBillheadDTO != null) {
						if (abEmitBillVo.getBillMoney() != null
								&& nisBillheadDTO.getPayMoney().compareTo(abEmitBillVo.getBillMoney()) != 0) {
							errorMsgList.add("开票金额必须和付款金额相等！");
						}
						if(!NetbankConstant.PAY_TYPE_49.equals(nisBillheadDTO.getSysVoucherCode())){
							errorMsgList.add("付款单付款方式必须为应付票据！");
						}
						if(!nisBillheadDTO.getCorpId().equals(abEmitBillVo.getCorpId())
							&&!nisBillheadDTO.getCorpId().equals(abEmitBillVo.getApplyCorpId())){
							errorMsgList.add("开票单位或者申请单位必须同付款单位相同！");
						}
						abEmitBillVo.setVoucherId(nisBillheadDTO.getId());
						abEmitBillVo.setSource(AccebillConstant.AB_ACCEBILL_SOURCE10);
					}
					abEmitBillVo.addErrorMsg(errorMsgList);
				}
			}
		}
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<AbEmitBillVo> list, ICurrentUser userVo) {
		if (list != null && !list.isEmpty()) {
			BigDecimal num = new BigDecimal(100);
			Map<String,AbEmitBillVo> payIdsMap=new HashMap<>();
			for (AbEmitBillVo abEmitBillVo : list) {
				abEmitBillVo.setIsImport("1");
				if(StrUtil.isNotEmpty(abEmitBillVo.getVoucherId())){
					payIdsMap.put(abEmitBillVo.getVoucherId(),abEmitBillVo);
					abEmitBillVo.setPayLockMoney(abEmitBillVo.getBillMoney());
					abEmitBillVo.setIsImport("0");
				}
				if (abEmitBillVo.getBailpercent() != null) {
					abEmitBillVo.setBailpercent(abEmitBillVo.getBailpercent().divide(num, 6, BigDecimal.ROUND_HALF_UP));// 保证金比例
					//如果保证金为空
					if (abEmitBillVo.getBailmoney() == null){
						//保证金为：票据金额*保证金比例
						BigDecimal bailpercent = abEmitBillVo.getBailpercent();
						BigDecimal billMoney = abEmitBillVo.getBillMoney();
						BigDecimal multiply = billMoney.multiply(bailpercent).setScale(2,RoundingMode.CEILING);
						abEmitBillVo.setBailmoney(multiply);
					}
				}
				if (abEmitBillVo.getBailrate() != null) {
					abEmitBillVo.setBailrate(abEmitBillVo.getBailrate().divide(num, 6, BigDecimal.ROUND_HALF_UP));// 保证金比率
				}
				String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
				abEmitBillVo.setAppNo(appNo);// 设置开票登记单号
				// 解决对内导入时，收票人账号为空问题
				if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abEmitBillVo.getInsideSing()))) {
					abEmitBillVo.setPayee1(abEmitBillVo.getPayee());
					abEmitBillVo.setPayeeacc1(abEmitBillVo.getPayeeacc());
				}
				if ("是".equals(abEmitBillVo.getIsCreditDraw())) {
					abEmitBillVo.setIsCreditDraw(CommonConstant.YES);
					abEmitBillVo.setRatmodel(CommonConstant.YES);
				} else {
					abEmitBillVo.setIsCreditDraw(CommonConstant.NO);
					if(StrUtil.isEmpty(abEmitBillVo.getRatmodel())) {
						abEmitBillVo.setRatmodel(CommonConstant.NO);
					}
				}
				// this.save(abEmitBillVo);
//				this.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, userVo);
				this.saveOrUpdateOrSubmit(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, AccebillConstant.BILL_OPERATE_SAVE, userVo);
			}

			//对付款批量导入的票据进行业务完成操作
			if(CollectionUtil.isNotEmpty(payIdsMap)){
				List<NisBillDTO> nisBillDTOList =new ArrayList<>(payIdsMap.size());
				//票据进行开票登记完成操作
				payIdsMap.values().forEach(abEmitBillVo->{
					//不需要审批完成
//					approve(abEmitBillVo.getVid(), abEmitBillVo.getVvid(), abEmitBillVo.getId(), "付款导入自动完成！" ,true,userVo);
					NisBillDTO nisBillDTO = new NisBillDTO();
					nisBillDTO.setBalance(abEmitBillVo.getBillMoney());
					nisBillDTO.setPayId(abEmitBillVo.getVoucherId());
					nisBillDTO.setBillId(abEmitBillVo.getId());
					nisBillDTOList.add(nisBillDTO);
				});
				//对付款和付款中间表进行挂接
				iNisBillService.batchNisBill(nisBillDTOList);
//				//对付款单进行业务完成操作
				String payIds=StrUtil.join(",",payIdsMap.keySet());
				abNisBillHeadService.successNisBillheadForBill(payIds,NetbankConstant.STATUE_CHECK);
			}

		}
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbEmitBillVoByAid(aid,false);
	}
	/**
	 * 设置业务上自己需要显示的内容
	 *
	 * @param vo
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbVoucherinfoVO viewBusInfo(AbEmitBillVo vo, Model model) {
		AbVoucherinfoVO billInfo = null;
		if (vo != null && AccebillConstant.BRIEF_STATUS_017.equals(vo.getBriefId())) {
			IAccebillBaseService baseService = AbAccebillUtils.getAbAccebillService(vo.getBriefId());
			if (baseService != null) {
				billInfo = baseService.getAbBillInfoById(vo.getId());
				model.addAttribute("busInfo", billInfo);
			}
		}
		return billInfo;
	}
	@Transactional(readOnly = true)
	public AbEmitBillVo getAbEmitBearVoByAid(String aid) {
		AbEmitBillVo vo = null;
		if (StringUtils.isNotEmpty(aid)) {
			List<AbEmitBillVo> list = abEmitBillDao.getAbEmitBillVoById(aid);
			if (list != null && !list.isEmpty()) {
				vo = list.get(0);
				GuaContractOccupDTO bearVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
				if(null!=bearVo && bearVo.getContractIds()!=null && bearVo.getContractIds().size()>0){
					vo.setContractIds(bearVo.getContractIds());
					vo.setGuaContract(bearVo.getContractCode());
					vo.setGuaWay(bearVo.getGuaWay());
					vo.setGuaWayNames(bearVo.getGuaWay());
					vo.setGuaranteeRmk(bearVo.getGuaRmk());
				}
			}
		}
		return vo;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<CorpVerifyErrorDTO>();
			// 单位有未结清的应付票据，不允许禁用！
			int num=abEmitBillDao.getValidBillDisbleNumByCorpId(corpId,AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_EMIT,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILLEMIT_ERROR)));
			}
			// 单位存在暂存、待审批、审批打回的应付票据，不允许禁用！
			num=abEmitBillDao.getApproveBillDisbleNumByCorpId(corpId, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_EMIT,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_APPROVE_DISABLE_BILLEMIT_ERROR)));
			}
			// 单位有库存中的应收票据，不允许禁用！
			num=abEmitBillDao.getValidBillDisbleNumByCorpId(corpId,AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_RECEIVE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILLRECEIVE_ERROR)));
			}
			// 单位存在暂存、待审批、审批打回的应收票据，不允许禁用！
			num=abEmitBillDao.getApproveBillDisbleNumByCorpId(corpId, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_RECEIVE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_APPROVE_DISABLE_BILLRECEIVE_ERROR)));
			}
			// 单位有电票业务指令未结束，不允许禁用！
			num=abEmitBillDao.getElectBillDisbleNumByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_ELECTRIC,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILL_ELECTRIC_ERROR)));
			}
			// 单位有待签收票据信息，不允许禁用！
			num=abBillReceivebizDao.getValidBizApproveNumByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_ELECTRIC,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILLELECTRIC_DISB_ERROR)));
			}
			// 单位有贴现在途的票据数据，不允许禁用！
			num = abEmitBillDao.getDiscuntNumByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_RECEIVE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILL_DISCOUNT_ERROR)));
			}
			// 单位存在记账未完成的票据数据，不允许禁用！
			boolean isCount = accebillService.checkToBeBookByCorpId(corpId);
			if(isCount){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_RECEIVE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DISABLE_BILLEMIT_NOTOVER_ERROR)));
			}
			if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
				return verifyErrorDTOS;
			}
		}
		return null;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		//禁用划分细则
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<CorpVerifyErrorDTO>();
			//单位有登记的应付票据，不允许删除！
			int num=abVoucherinfoViceService.getValidBillNumByCorpId(corpId,AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_EMIT,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DEL_BILLEMIT_ERROR)));
			}
			//单位有登记的应收票据，不允许删除！
			num=abVoucherinfoViceService.getValidBillNumByCorpId(corpId,AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_RECEIVE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DEL_BILLRECEIVE_ERROR)));
			}
			//单位有票据签收信息，不允许删除！待签收和已签收的都判断
			num=abBillReceivebizDao.getValidReceivebizNumByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.ACCEBILL,
						SysConfigConstant.BusType.ACCEBILL_ELECTRIC,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.ACCEBILL_VALID_DEL_BILLELECTRIC_ERROR)));
			}
			if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
				return verifyErrorDTOS;
			}
		}
		return null;
	}

	/**
	 * 招行保证金信息查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput<CmbBailAccountInfoVo> getZhBondAccDataTables(DataTablesInput dataTablesInput) {
		DataTablesOutput<CmbBailAccountInfoVo> dataTablesUnion = abEmitBillDao.getZhBondAccDataTables(dataTablesInput);
		if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_BAIL_TYPE");
			for (CmbBailAccountInfoVo vo : dataTablesUnion.getData()) {
				// 保证金类型 0-活期 1- 定期
				Optional.ofNullable(vo.getHmgtyp()).ifPresent(hmgTyp -> {
					Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BAIL_TYPE");
					Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(hmgTyp + "")))
							.ifPresent(dict -> vo.setHmgtypStr(dict.getName()));
				});
			}
		}
		return dataTablesUnion;
	}

	/**
	 * 根据帐号查询招行保证金信息
	 *
	 * @param acc
	 * @return
	 */
	public CmbBailAccountInfoVo getZhBondByAcc(String acc) {
		return abEmitBillDao.getZhBondByAcc(acc);
	}

	/**
	 * 根据帐号查询民生保证金信息
	 *
	 * @param acc
	 * @return
	 */
	public CmbcBailAccountInfoVo getMsBondByAcc(String acc) {
		return abEmitBillDao.getMsBondByAcc(acc);
	}

	public DataTablesOutput<CmbcBailAccountInfoVo> getMsBondAccDataTables(DataTablesInput dataTablesInput) {
		DataTablesOutput<CmbcBailAccountInfoVo> dataTablesUnion = abEmitBillDao.getMsBondAccDataTables(dataTablesInput);
		if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
			for (CmbcBailAccountInfoVo vo : dataTablesUnion.getData()) {
				// 保证金类型 0-活期 1- 定期
				if(AccebillConstant.TIMTYPE_HQ.equals(vo.getTimType())){
					vo.setTimTypeValue("0");
				}else{
					vo.setTimTypeValue("1");
				}
			}
		}
		return dataTablesUnion;
	}
}
