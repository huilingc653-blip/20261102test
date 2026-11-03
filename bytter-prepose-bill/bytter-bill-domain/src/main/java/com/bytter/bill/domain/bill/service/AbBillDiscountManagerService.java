package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillDiscountManagerDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
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
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;

import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:贴现办理Service
 * @date 2017-07-03 11:42:27
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTMANAGER)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTMANAGER)
@Service("abBillDiscountManagerService")
@Transactional
public class AbBillDiscountManagerService extends BaseService<AbBillDiscountManagerVO, String>
		implements IAccebillAccbookService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillDiscountManagerVO> {
	@Autowired
	private AbBillDiscountManagerDao abBillDiscountManagerDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;

	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbFbsItemService abFbsItemService;
	@DubboReference(check = false)
	private IDictService dictService;

	@Autowired
	private AbVoucherinfoDao abVoucherinfoDao;
	@Autowired
	private AbVoucherinfoViceDao abVoucherinfoViceDao;
	@Autowired
	private BillTicketSplittingUtils billTicketSplittingUtils;
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Override
	public BaseDao<AbBillDiscountManagerVO, String> getEntityDao() {
		return abBillDiscountManagerDao;
	}

	/**
	 * 列表显示贴现申请信息
	 *
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings({ "rawtypes"})
	public DataTablesOutput listAbBillDiscountManager(DataTablesInput input) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_QUERY,corpIds);
		DataTablesOutput<AbBillDiscountManagerVO> dataTablesOutput = abBillDiscountManagerDao.listAbBillDiscountManager(input, corpAuthList, abVoucherinfoViceService.isNotDiscountApply());
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
					, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_PAY_DATE", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_SEND_SIGN"
					, "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillDiscountManagerVO bill : listData) {
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

		List<AbBillDiscountManagerVO> data = dataTablesOutput.getData();
		List<String> collect = data.stream().map(AbBillDiscountManagerVO::getBankAcc).collect(Collectors.toList());

		List<String> cnAccrecords = dataTablesOutput.getData().stream().map(AbBillDiscountManagerVO::getCnAccrecords).collect(Collectors.toList());
		Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(cnAccrecords);

		if(CollectionUtils.isEmpty(data)) {
			return new DataTablesOutput<AbBillDiscountManagerVO>();
		}
		Map<String, BtBankAccDTO> billBankAccByBankAccList = abBtBankAccService.getBillBankAccByBankAccList(collect);
		if(corpIdSet.size() <= 0){
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));

		for (AbBillDiscountManagerVO datum : data) {

			if(CollectionUtil.isNotEmpty(stringCorpDTOMap)){
				CorpDTO corpDTO = stringCorpDTOMap.get(datum.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(datum.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(datum.getBusCorpId());
				datum.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				datum.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				datum.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				datum.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				datum.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}

			if (billBankAccByBankAccList.containsKey(datum.getBankAcc()) ) {
				BtBankAccDTO btBankAccDTO = billBankAccByBankAccList.get(datum.getBankAcc());
				if(ObjectUtils.isNotEmpty(btBankAccDTO)) {
					datum.setBankTypeName(btBankAccDTO.getAccTypeName());
				}
			}
			if (StringUtils.isNotBlank(datum.getCnAccrecords()) && MapUtils.isNotEmpty(corpDTOMap)){
				if (corpDTOMap.containsKey(datum.getCnAccrecords())){
					CorpDTO corpDTO = corpDTOMap.get(datum.getCnAccrecords());
					if(ObjectUtils.isNotEmpty(corpDTO)) {
						datum.setCnAccrecords(corpDTO.getName());
					}
				}
			}

		}
		dataTablesOutput.setData(data);
		return dataTablesOutput;
	}
	private void injectValues(AbBillDiscountManagerVO bill,Map<String, Map<String, DictDTO>> listDictMap){
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
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 票据业务状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABBILLDISCOUNT_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 不得转让标记
		Optional.ofNullable(bill.getBankendrsmtmk()).ifPresent(bankendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bankendrsmtmk)))
					.ifPresent(dict -> bill.setBankendrsmtmkName(dict.getName()));
		});
		// 划款天数
		Optional.ofNullable(bill.getPaydate()).ifPresent(paydate -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_PAY_DATE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(paydate + "")))
					.ifPresent(dict -> bill.setPaydateName(dict.getName()));
		});
		// 贴现种类：RM00 买断式、RM01 回购式买断式
		Optional.ofNullable(bill.getRpdmk()).ifPresent(rpdmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_DISCOUNT_RPDMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rpdmk)))
					.ifPresent(dict -> bill.setRpdmkName(dict.getName()));
		});
		// 清算方式 SM00线上清算、SMO1线下清算
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
	 * 批量提交贴现办理
	 *
	 * @param abBillDiscountManagerVO
	 * @return
	 */
	public boolean batchSubmitAbBillDiscountManager(AbBillDiscountManagerVO abBillDiscountManagerVO,
			ICurrentUser userVo) {
		//不启用贴现申请功能直接贴现办理
		boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
		String accIds = abBillDiscountManagerVO.getAccIds();
		String[] oldLastAccIdArr = abBillDiscountManagerVO.getAccIds().split(",");
		// 新一代票据处理 start
		String vouIds = abBillDiscountManagerVO.getVouIds();
		String viceIds = abBillDiscountManagerVO.getViceIds();
		Map<String, String> stringStringMap = billTicketSplittingUtils.ticketSplitting(vouIds, viceIds, accIds, abBillDiscountManagerVO.getTransMoneys(),
				abBillDiscountManagerVO.getSbbllRngStrtSnSplit(),abBillDiscountManagerVO.getSbbllRngEndSnSplit(), userVo);
		abBillDiscountManagerVO.setVouIds(stringStringMap.get("infoIds"));
		abBillDiscountManagerVO.setViceIds(stringStringMap.get("viceIds"));
		abBillDiscountManagerVO.setAccIds(stringStringMap.get("accIds"));
		accIds = abBillDiscountManagerVO.getAccIds();
		String[] accIdArrays =accIds.split(",");
		//end
		List<AbAccrecords> newList = abBillDiscountManagerVO.getAbAccrecordsList();
		List<AbDiscount> abDiscountList = abBillDiscountManagerVO.getAbDiscountList();
		if (StringUtils.isEmpty(accIds) || abDiscountList.isEmpty() || newList.isEmpty()) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		String[] viceIdArrays = new String[accIdArrays.length];
		if (accIdArrays.length != newList.size() || accIdArrays.length != abDiscountList.size()) { // 判断上一笔流水与新增流水,贴现信息是否匹配
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		for (int i = 0; i < accIdArrays.length; i++) {
			// 上一笔流水信息
			AbAccrecords accRecordsLast = abAccrecordsService.get(accIdArrays[i],true);
			// 主信息表
			AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecordsLast.getVid(),true);
			// 从表
			AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecordsLast.getVvid(),true);
			viceIdArrays[i] = accRecordsLast.getVvid();

			// 生成贴现办理流水
			AbAccrecords accRecordsNew = newList.get(i);
			if("2".equals(accRecordsLast.getCopyFlag())) {
				accRecordsNew.setSplitFlg(accRecordsLast.getSplitFlg());
				accRecordsNew.setCopyFlag(accRecordsLast.getCopyFlag());
				accRecordsNew.setOldAccId(accRecordsLast.getOldAccId());
			} else {
				//oldLastAccIdArr[i].equals(accIdArrays[i]) 为true，说明为不拆分的票据（可能为旧票或者不拆分新票）
				accRecordsNew.setSplitFlg(oldLastAccIdArr[i].equals(accIdArrays[i]) ? "0" : "1");
				//拆分交易的话设置此字段为未拆分之前的id
				accRecordsNew.setOldAccId("1".equals(accRecordsNew.getSplitFlg()) ? oldLastAccIdArr[i] : null);
			}

			accRecordsNew.setVid(vouInfo.getId());
			accRecordsNew.setVvid(viceInfo.getId());
			accRecordsNew.setLastid(accRecordsLast.getId());// 上一笔流水id
			accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
			accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
			accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态
			accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_011);// 业务类型
			accRecordsNew.setApplyDate(new Date());
			accRecordsNew.setBillFlag(vouInfo.getBillFlag());
			//不启用贴现申请功能直接贴现办理
			if (isNotDiscountApply) {
				accRecordsNew.setRecNm(abBillDiscountManagerVO.getRecNm());// 贴入人名称
				accRecordsNew.setRecAcctsvcr(abBillDiscountManagerVO.getRecAcctsvcr());// 贴入人行号
				accRecordsNew.setInsideSing(abBillDiscountManagerVO.getInsideSing());// 是否内部业务标识
				accRecordsNew.setRecAcctId(abBillDiscountManagerVO.getRecAcctId());// 贴入人账号
			} else {
				accRecordsNew.setInsideSing(accRecordsLast.getInsideSing());// 是否内部业务标识
				// 隐藏贴现申请流水
				accRecordsLast.setIsHide(AccebillConstant.AB_IS_HIDE_Y);
				abAccrecordsService.update(accRecordsLast, userVo);
			}
			// 当贴现是单条数据时，则判断单位是否挂资金计划
			if (accIdArrays.length == 1) {
				if (abVoucherinfoViceService.isBudget(viceInfo.getCorpId(), AccebillConstant.AB_BUSTYPE_DISCOUNT)) {
					abBillDiscountManagerVO.setInsideSing(accRecordsNew.getInsideSing());
					abBillDiscountManagerVO.setOrigNo(vouInfo.getOrigNo());
					abBillDiscountManagerVO.setBusinessDate(accRecordsNew.getBusinessDate());
					this.batchEngross(abBillDiscountManagerVO, viceInfo, userVo);
					// 预算科目
					accRecordsNew.setBudgetCode(abBillDiscountManagerVO.getDiscountItem());
				}
			}
		}
		// 批量新增流水
		int accNum = abAccrecordsService.batchSaveAccrecords(newList, userVo);
		// 修改box_status=10
		int viceNum = abVoucherinfoViceService.batchUpdateBoxStatus(viceIdArrays, AccebillConstant.AB_BOX_STATUS_N,
				userVo);
		if (accNum != accIdArrays.length || viceNum != viceIdArrays.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 取新增流水id
		List<String> accIdNewList = new ArrayList<String>();
		for (int i = 0; i < newList.size(); i++) {
			AbAccrecords accRecordsNew = newList.get(i);
			AbDiscount abDis = abDiscountList.get(i);
			//获取旧的贴现明细信息id
			//String disOldId = abDiscountService.getDiscountIdByRecordId(accRecordsNew.getLastid());
			abDis.setRecordId(accRecordsNew.getId());
			if(BigDecimal.ZERO.compareTo(abDis.getBuyerinterestRatio()) == 0){
				abDis.setBuyerinterestSing(AccebillConstant.AB_BUYERINTERESTSING_N);
				abDis.setIntpymtprtWay("2");
			}else if(BigDecimal.ONE.compareTo(abDis.getBuyerinterestRatio()) == 0){
				abDis.setBuyerinterestSing(AccebillConstant.AB_BUYERINTERESTSING_Y);
				abDis.setIntpymtprtWay("1");
			}else {
				abDis.setBuyerinterestSing(AccebillConstant.AB_BUYERINTERESTSING_N);
				abDis.setIntpymtprtWay("3");
			}
			abDiscountService.save(abDis);
			accIdNewList.add(accRecordsNew.getId());
			//判断是否开启授信，是否选择授信编号
			if(abRatUrationService.isRelateRat()==true
					&& StringUtils.isNotEmpty(abBillDiscountManagerVO.getUrationDTO())
					&& (abRatUrationService.isDisCountCredit()==1 || abRatUrationService.isDisThirdBillCredit()==1)
					&&!StringUtils.isBlank(abBillDiscountManagerVO.getUrationDTO().getRatCode())){
				AbAccrecords abAccrecords = newList.get(i);
				//修改从表数据
				AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
				abVoucherinfoVice.setRatmodel("1");
				//判断银行挂授信类型是否为承兑人
				if(AccebillConstant.AB_RATMODEL_TYPE_ACCEPTOR.equals(abBillDiscountManagerVO.getRatBankCorpType())){
					abVoucherinfoVice.setRatBankCorpId(abBillDiscountManagerVO.getRatBankCorpId());//承兑人CorpId
				}else{
					abVoucherinfoVice.setRatBankCorpId(abBillDiscountManagerVO.getUrationDTO().getCorpId());//银行授信单位贴现申请人CorpId
				}
				abVoucherinfoViceService.update(abVoucherinfoVice);
				//商票或第三方票据贴现占用额度
				AbDiscount abDiscount = abDiscountList.get(i);
				abDiscount.setOccupancyRatio(abBillDiscountManagerVO.getUrationDTO().getMoney());
				abDiscountService.update(abDiscount);
				// 增加商票或第三方票据贴现银行授信
				abAccrecords.setVouViceInfo(abVoucherinfoVice);
				abAccrecords.setRatUrationDTO(abBillDiscountManagerVO.getUrationDTO());
				abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_SUBMIT,userVo);
				abLogService.saveAbLog(abVoucherinfoVice.getId(), abAccrecords.getId(),
						AccebillConstant.BRIEF_STATUS_011, AccebillConstant.ABLOG_LOGTYPE_ISDISCOUNTCREDIT,
						AccebillConstant.AB_ADD_BANK_ACCEBILL_ISDISCOUNTCREDIT, userVo);
			}
		}
		String[] accIdNewArrs = new String[accIdNewList.size()];
		accIdNewList.toArray(accIdNewArrs);
		abLogService.batchSaveAbLog(viceIdArrays, accIdNewArrs, AccebillConstant.BRIEF_STATUS_011,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		// 提交审批
		for (int i = 0; i < accIdNewArrs.length; i++) {
			AbBillDiscountManagerVO variables = new AbBillDiscountManagerVO();
			AbAccrecords accRecords = abAccrecordsService.get(accIdNewArrs[i], true);
			abBillNotificationService.completeMsg(accRecords,userVo);
			// 修改从表back_brief_id为""
			abVoucherinfoViceService.updateBackBriefIdIsNull(accRecords.getVvid());
			AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecords.getVid(), true);
			AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid(), true);
			AbDiscount discount = abDiscountService.getAbDiscountByRecordId(accRecords.getId());
			BeanUtils.copyProperties(vouInfo, variables);
			BeanUtils.copyProperties(viceInfo, variables);
			BeanUtils.copyProperties(accRecords, variables);
			BeanUtils.copyProperties(discount, variables);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTMANAGER, accRecords.getId(), vouInfo,
					viceInfo.getApplyCorpId(),userVo.getId());
		}
		return true;
	}
	/**
	 * 贴现申请打回
	 *
	 * @param abBillDiscountManagerVO
	 */
	public boolean batchBackAbBillDiscountManager(AbBillDiscountManagerVO abBillDiscountManagerVO,
			ICurrentUser userVo) {
		String accIds = abBillDiscountManagerVO.getAccIds();
		if (StringUtils.isEmpty(accIds)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		String[] accIdArrays = accIds.split(",");
		for (int i = 0; i < accIdArrays.length; i++) {
			AbAccrecords info = abAccrecordsService.get(accIdArrays[i]);
			String[] accIdArray = accIdArrays[i].split(",");
			// 贴现申请流水变为 status=-2
			int statusNum = abAccrecordsService.batchUpdateStatus(accIdArray, AccebillConstant.AB_BILL_BUS_STATUS_2,
					info.getStatus(), userVo);
			if (statusNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			if("1".equals(info.getCopyFlag())){
				AbAccrecords upd = abAccrecordsService.get(info.getLastid());
				upd.setId(info.getLastid());
				upd.setUpdateDate(new Date());
				upd.setUpdateBy(userVo.getLoginName());
				upd.setValidSign("1");
				upd.setStatus(95);
				List<AbAccrecords> updList = new ArrayList<>();
				updList.add(upd);
				abAccrecordsService.batchSaveOrUpdate(updList);
				AbVoucherinfoVice updVice = abVoucherinfoViceService.get(upd.getVvid());
				updVice.setValidSign("1");
				abVoucherinfoViceService.update(updVice);
			}else {
				// 上一流水valid_Sign=1
				int upLastValidSignNum = abAccrecordsService.updateValidSign(info.getLastid(),
						AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
				if (upLastValidSignNum != 1) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				if("2".equals(info.getCopyFlag())){
					//批量拆分业务进行打回处理
					billTicketSplittingUtils.splitRepulseOp(info);
				}
			}
			//int ii = 1/0;
			abBillNotificationService.completeMsg(info, AccebillConstant.BRIEF_STATUS_011,userVo);
			abBillNotificationService.excRepulseSendMsg(info, info.getCreateBy());
			// 修改back_brief_id为6011
			int backNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), AccebillConstant.BRIEF_STATUS_6011,
					info.getId());
			if (backNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(),
					AccebillConstant.ABLOG_LOGTYPE_REPULSE, AccebillConstant.LANG_LOG_DISCOUNT_BACK, userVo);
			// 余额计算
			abBillAccBalService.setBankAccBillBal(accIdArrays[i], userVo);
		}
		return true;
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
	// 审批
	public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		// 通过:主表：box_status=11，流水：status=95,上一条流水valid_sign=2,可做业务票据流入下一个可做业务中。
		// 打回:主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_011，这条贴现申请流水status=-2(已删除)
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
				String billStatusOld = AccebillConstant.AB_BILL_STATUS_110106;
				String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
				String ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0301;
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
			//挂接授信通过
			abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_APPOK,userVo);
			// 修改最终审批人
			abAccrecordsService.updateApprover(buId, userVo);
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
		int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);// 修改status=95,-2
		int boxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);// 修改boxStatus=11
		if (statusNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		if (boxStatusNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}

		int upLastValidSignNum = abAccrecordsService.updateValidSign(info.getLastid(),
				AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);// 修改上一流水validSign=2
		if (upLastValidSignNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}

		//批量拆分新业务批量处理
		if("2".equals(info.getCopyFlag())){
			billTicketSplittingUtils.splitConsentOp(info);
		}

		if ((AccebillConstant.AB_INSIDE_SING_Y).equals(String.valueOf(info.getInsideSing()))) {
			registerAbDiscount(info.getId(), userVo);
		}
		// 判断流水的资金计划科目是否为空，空则没挂资金计划
		if (!EmptyUtils.isBlank(info.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(info.getVvid(), true,userVo);
		}
		// 生成余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info	.getId(), info.getBriefId(), logType, msg, userVo);
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
		// 上一流水
		AbAccrecords lastInfo = abAccrecordsService.get(info.getLastid(), true);
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
		int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);// 修改status=95,-2
		int boxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);// 修改boxStatus=11
		if (statusNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		if (boxStatusNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}

		int backNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), AccebillConstant.BRIEF_STATUS_011,
				info.getId());// 修改back_brief_id为011
		lastInfo.setIsHide(AccebillConstant.AB_IS_HIDE_N);
		abAccrecordsService.update(lastInfo, userVo);
		if (backNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 挂接授信打回释放
		abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_BACK,userVo);
		abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_DELETE,userVo);
		//不启用贴现申请功能直接贴现办理
		/*boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
		if(!isNotDiscountApply){
			//贴现明细流水id修改为贴现申请流水id
			String disId = abDiscountService.getDiscountIdByRecordId(info.getId());
			abDiscountService.updateDiscountRecordIdById(disId,lastInfo.getId());
		}*/
		// 判断流水的资金计划科目是否为空，空则没挂资金计划，不为空则删除计划
		if (!EmptyUtils.isBlank(info.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(info.getVvid(), false,userVo);
		}
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);

		AbAccrecords oldAcc = abAccrecordsService.get(info.getId());
		if("1".equals(oldAcc.getSplitFlg())){
			//批量付票业务进行处理
			if("2".equals(info.getCopyFlag())){
				billTicketSplittingUtils.splitRepulseOp(info);
			}else {
				//新票据拆分后打回处理
				NewDraftDealMethod.draftMerge("1",info.getVid(),billBankDao,billAccrecordsDao);
			}
		}

	}

	/**
	 * 占用预算
	 *
	 * @param entity 票据贴现
	 * @param abVoucherinfoVice 从表数据
	 * @param userVo 操作用户
	 */
	private void batchEngross(AbBillDiscountManagerVO entity,AbVoucherinfoVice abVoucherinfoVice, ICurrentUser userVo) {
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
				budget.setBusinessName(BudgetConstant.BUSINESS_NAME_DISCOUNT);// 执行业务名称
				budget.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
				budget.setCreateBy(userVo.getLoginName());
				budget.setCreateDate(DateUtils.getDate());
			}
			// 把选择的资金计划科目写入流水表
			entity.setDiscountItem(itemCodeStr);
			// 修改时，先删除之前的计划执行数据再保存
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_DISCOUNT,
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
			this.abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_DISCOUNT,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid, BudgetConstant.STATUE_CHECK,userVo);
		} else {
			// 删除资金计划
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_DISCOUNT,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid);
		}
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTMANAGER, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abBillDiscountManagerDao.dataTableApprove(dataTablesInput, businessList);
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
				, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_PAY_DATE", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_SEND_SIGN");
		List<AbBillDiscountManagerVO> list=dp.getData();
		Map<String, CorpDTO> corpMap = corpService.getCorpMap();
		for(AbBillDiscountManagerVO reInfo:list){
			injectValues(reInfo, listDictMap);
			if (StringUtils.isNotBlank(reInfo.getBusCorpId())){
				reInfo.setBusCorpName(corpMap.get(reInfo.getBusCorpId()) != null ? corpMap.get(reInfo.getBusCorpId()).getName(): "");
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 加载详细审核数据
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillDiscountManagerVO getAbBillDiscountManagerVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbDiscount disInfo = abDiscountService.getAbDiscountByRecordId(accrecordsInfo.getId());
		AbBillDiscountManagerVO reInfo = new AbBillDiscountManagerVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(viceInfo, reInfo);
		BeanUtils.copyNotNullProperties(disInfo, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
				, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_PAY_DATE", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_SEND_SIGN");
		injectValues(reInfo, listDictMap);
		return reInfo;
	}
	/**
	 * 登记内部贴现票据
	 *
	 * @param accId
	 */
	public void registerAbDiscount(String accId, ICurrentUser userVo) {
		// 贴现流水信息
		AbAccrecords lastRecInfo = abAccrecordsService.get(accId, true);
		// 主表信息
		AbVoucherinfo lastVouInfo = abVoucherinfoService.get(lastRecInfo.getVid(), true);
		AbVoucherinfo vouInfo = new AbVoucherinfo();
		BeanUtils.copyNotNullProperties(lastVouInfo, vouInfo);
		vouInfo.setId(null);
		// 从表信息
		AbVoucherinfoVice viceInfo = new AbVoucherinfoVice();
//		// 获取账户信息
//		BtBankAccDTO recAccInfo = abAccrecordsService.getBtBankAccInfo(lastRecInfo.getRecAcctId());
//
//		BtBankAccDTO reqAccInfo = abAccrecordsService.getBtBankAccInfo(lastRecInfo.getReqAcctId());
//		if (reqAccInfo == null&&acc) {
//			//贴出人账户不能为空
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DISCUNTREQBANKACC_ISNOTNULL,lastRecInfo.getReqAcctId());
//		}
//		if (recAccInfo == null) {
//			//贴入人账户不能为空
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DISCOUNTRECBANKACC_ISNOTNULL,lastRecInfo.getRecAcctId());
//		}

//		CorpDTO reqDTO = corpServiceImpl.get(reqAccInfo.getCorpId());
		CorpDTO recDTO = abAccrecordsService.getCorpInfo(lastRecInfo.getRecAcctId(), lastRecInfo.getRecNm());
		if(recDTO==null){
			CorpDTO reqDTO =  abAccrecordsService.getCorpInfo(lastRecInfo.getReqAcctId(), lastRecInfo.getReqNm());
			if(reqDTO!=null){
				recDTO=abCorpService.get(reqDTO.getNetId());
			}
		}
		if(recDTO==null){
			//内部贴现贴入单位获取异常！
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DISCOUNTRECCORPINFO_ISNOTNULL);
		}
		// 票据单位
		viceInfo.setApplyCorpId(recDTO.getId());
		// 业务单位
		viceInfo.setBusCorpId(recDTO.getId());
		viceInfo.setCorpId(recDTO.getId());// 持票单位
		viceInfo.setBankAcc(lastRecInfo.getRecAcctId());// 持票账号
		viceInfo.setBankCode(lastRecInfo.getRecAcctsvcr());// 持票单位行号
		viceInfo.setCnAccrecords(lastRecInfo.getReqNm());// 前手
		viceInfo.setEnterStock(AccebillConstant.AB_ENTER_STOCK_CORP);// 所属库存
		viceInfo.setBillSource(AccebillConstant.AB_BILL_SOURCE_DISCOUNT);// 贴现来源
		viceInfo.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_Y));
		viceInfo.setIsSetCreateBy(false);//
		// 贴现信息
		AbDiscount lastDisInfo = abDiscountService.getAbDiscountByRecordId(lastRecInfo.getId());
		AbDiscount disInfo = new AbDiscount();
		BeanUtils.copyNotNullProperties(lastDisInfo, disInfo);
		disInfo.setId(null);
		disInfo.setRecordId(null);
		// 贴入登记流水信息
		AbAccrecords recInfo = new AbAccrecords();
		recInfo.setBusinessDate(disInfo.getDiscountDate());// 业务归属日期
		recInfo.setCnAccrecords(lastRecInfo.getReqNm());// 前手
		recInfo.setLastid(lastRecInfo.getId());// 上一笔流水
		recInfo.setBankendrsmtmk(vouInfo.getBanendrsmtmk());// 不得转让标记
		recInfo.setReqAcctId(lastRecInfo.getReqAcctId());// 业务发起人账号
		recInfo.setRecAcctId(lastRecInfo.getRecAcctId());// 业务接收人账号
		recInfo.setRecRole(lastRecInfo.getRecRole());// 业务接收人角色
		recInfo.setRecNm(lastRecInfo.getRecNm());// 业务接收人名称
		recInfo.setRecAcctsvcr(lastRecInfo.getRecAcctsvcr());// 业务接收人行号
		recInfo.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);// 创建人是否设置为空
		AbReceiveBillVO reInfo = new AbReceiveBillVO();
		reInfo.setAbAccrecords(recInfo);
		reInfo.setAbVoucherInfo(vouInfo);
		reInfo.setAbVoucherinfoVice(viceInfo);
		reInfo.setAbDiscount(disInfo);
		reInfo.setBillSource(AccebillConstant.AB_BILL_SOURCE_DISCOUNT);// 贴现来源
		// 登记票据
		abReceiveBillService.registerAbReceiveBill(reInfo, AccebillConstant.BILL_OPERATE_SAVE, userVo);
		abBillNotificationService.sendMsg(reInfo.getId());
	}
	/**
	 * 导入
	 *
	 * @param list
	 * @return
	 */
	public boolean saveImport(List<AbBillDiscountManagerVO> list, ICurrentUser userVo) {
		if (list != null && !list.isEmpty()) {
			// 返回校验后的数据信息
			for (AbBillDiscountManagerVO managerVo : list) {
				// 新增流水信息
				AbAccrecords accInfo = new AbAccrecords();
				List<AbAccrecords> abAccrecordsList = new ArrayList<>();
				accInfo.setBankendrsmtmk(managerVo.getBankendrsmtmk());
				accInfo.setReqAcctId(managerVo.getReqAcctId());
				accInfo.setRecAcctId(managerVo.getRecAcctId());
				accInfo.setRecAcctsvcr(managerVo.getRecAcctsvcr());
				accInfo.setRecNm(managerVo.getRecNm());
				accInfo.setBusinessDate(managerVo.getBusinessDate());
				accInfo.setRmk(managerVo.getRmk());
				abAccrecordsList.add(accInfo);
				managerVo.setAbAccrecordsList(abAccrecordsList);
				// 新增贴现信息
				AbDiscount disInfo = new AbDiscount();
				List<AbDiscount> abAbDiscountList = new ArrayList<>();
				disInfo.setDiscountDate(managerVo.getDiscountDate());
				disInfo.setPaydate(managerVo.getPaydate());
				disInfo.setDeferdate(managerVo.getDeferdate());
				disInfo.setDaynum(managerVo.getDaynum());
				disInfo.setInterestRates(managerVo.getInterestRates().divide(new BigDecimal(100)));
				disInfo.setInterest(managerVo.getInterest());
				disInfo.setBuyerinterestSing(managerVo.getBuyerinterestSing());
				disInfo.setBuyerinterestRatio(managerVo.getBuyerinterestRatio().divide(new BigDecimal(100)));
				disInfo.setBuyerinterestMoney(managerVo.getBuyerinterestMoney());
				disInfo.setRealMoney(managerVo.getRealMoney());
				disInfo.setInvcNb(managerVo.getInvcNb());
				disInfo.setTxlCtrctNb(managerVo.getTxlCtrctNb());
				disInfo.setRpdmk(managerVo.getRpdmk());
				disInfo.setRpdduedt(managerVo.getRpdduedt());
				disInfo.setRpdopendt(managerVo.getRpdopendt());
				disInfo.setAoaccninfAc(managerVo.getAoaccninfAc());
				disInfo.setAoaccninfName(managerVo.getAoaccninfName());
				disInfo.setAoaccninfId(managerVo.getAoaccninfId());
				disInfo.setSttlmmk(managerVo.getSttlmmk());
				if (managerVo.getRpdintrstRate() != null) {
					disInfo.setRpdintrstRate(managerVo.getRpdintrstRate().divide(new BigDecimal(100)));
				}
				abAbDiscountList.add(disInfo);
				managerVo.setAbDiscountList(abAbDiscountList);
				batchSubmitAbBillDiscountManager(managerVo, userVo);
			}
		}
		return true;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillDiscountManagerVO obj,Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbBillDiscountManagerVO> list,Object... user) {
		if (list != null && !list.isEmpty()) {
			//不启用贴现申请功能直接贴现办理
			boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
			Map<String, String> origNoMap = new HashMap<>();
			HashMap<String, CorpDTO> corpNameMap = new HashMap<>();
			HashMap<String, CorpDTO> corpCodeMap = new HashMap<>();
			Map<String, BtBankAccDTO> bankAccDTOMap = new HashMap<>();
			Map<String, InputBankInfoDTO> bankNameInfoDTOMap = new HashMap<>();
			Map<String, InputBankInfoDTO> bankCodeInfoDTOMap = new HashMap<>();
			//是否挂预算
			boolean budgetSel =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).toBoolean();
			//单位计划上报集合
			HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
			//计划科目
			HashMap<String, FbsItemDTO> fbsItemMap = new HashMap<>();
			String userId="";
			if(user[0]!=null){
				ICurrentUser userInfo=(ICurrentUser)user[0];
				userId=userInfo.getId();
			}
			String corpAuthSql = SqlUtil.getInSql(abCorpService.getUserCorpIds(
					AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_QUERY,userId), "p.APPLY_CORP_ID", true);
			for (AbBillDiscountManagerVO managerVo : list) {
				List<String> errorList = new ArrayList<>();
				CorpDTO corpInfo = null;
				InputBankInfoDTO bankInfo = null;
				BtBankAccDTO bankAccInfo = null;
//				if (origNoMap.containsKey(managerVo.getOrigNo()+managerVo.getSbbllRngStrtSn()+managerVo.getSbbllRngEndSn())) {
//					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
//					managerVo.addErrorMsg(errorList);
//					continue;
//				} else {
				origNoMap.put(managerVo.getOrigNo()+managerVo.getSbbllRngStrtSn()+managerVo.getSbbllRngEndSn(), managerVo.getOrigNo()+managerVo.getSbbllRngStrtSn()+managerVo.getSbbllRngEndSn());
//				}
				if (StringUtils.isEmpty(managerVo.getApplyCorpCode()) && StringUtils.isEmpty(managerVo.getApplyCorpName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL);
				} else {
					if (StringUtils.isNotEmpty(managerVo.getApplyCorpCode())) {
						if (corpCodeMap.containsKey(managerVo.getApplyCorpCode())) {
							corpInfo = corpCodeMap.get(managerVo.getApplyCorpCode());
						} else {
							corpInfo = abCorpService.getCorpByCode(managerVo.getApplyCorpCode());
						}
					} else {
						if (corpNameMap.containsKey(managerVo.getApplyCorpName())) {
							corpInfo = corpNameMap.get(managerVo.getApplyCorpName());
						} else {
							corpInfo = abCorpService.getCorpIdByName(managerVo.getApplyCorpName());
						}
					}
					if (corpInfo == null) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST);
					} else {
						managerVo.setApplyCorpId(corpInfo.getId());
						managerVo.setApplyCorpCode(corpInfo.getCode());
						managerVo.setApplyCorpName(corpInfo.getName());
						corpCodeMap.put(corpInfo.getCode(), corpInfo);
						corpNameMap.put(corpInfo.getName(), corpInfo);
					}
				}
				if (StringUtils.isEmpty(managerVo.getCorpCode()) && StringUtils.isEmpty(managerVo.getCorpName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
					managerVo.addErrorMsg(errorList);
					continue;
				} else {
					if (StringUtils.isNotEmpty(managerVo.getCorpCode())) {
						if (corpCodeMap.containsKey(managerVo.getCorpCode())) {
							corpInfo = corpCodeMap.get(managerVo.getCorpCode());
						} else {
							corpInfo = abCorpService.getCorpByCode(managerVo.getCorpCode());
						}
					} else {
						if (corpNameMap.containsKey(managerVo.getCorpName())) {
							corpInfo = corpNameMap.get(managerVo.getCorpName());
						} else {
							corpInfo = abCorpService.getCorpIdByName(managerVo.getCorpName());
						}
					}
				}
				if (corpInfo == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORPID_ISNOTEXIST);
					managerVo.addErrorMsg(errorList);
					continue;
				} else {
					managerVo.setCorpId(corpInfo.getId());
					managerVo.setCorpCode(corpInfo.getCode());
					managerVo.setCorpName(corpInfo.getName());
					corpCodeMap.put(corpInfo.getCode(), corpInfo);
					corpNameMap.put(corpInfo.getName(), corpInfo);
				}
				//业务单位
				if (StringUtils.isNotEmpty(managerVo.getBusCorpName())) {
					CorpDTO busCorpDTO;
					if (corpNameMap.containsKey(managerVo.getBusCorpName())) {
						busCorpDTO = corpNameMap.get(managerVo.getBusCorpName());
					} else {
						busCorpDTO = abCorpService.getCorpIdByName(managerVo.getBusCorpName());
					}
					if (busCorpDTO == null) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUSCORPID_ISNOTEXIST);
					}else{
						managerVo.setBusCorpId(busCorpDTO.getId());
					}
				}
				Map<String, Object> params = new HashMap<>();
				params.put("origNo", managerVo.getOrigNo());
				params.put("corpId", managerVo.getCorpId());
				params.put("sbbllRngStrtSn",managerVo.getSbbllRngStrtSn());
				params.put("sbbllRngEndSn",managerVo.getSbbllRngEndSn());


				AbBillDiscountManagerVO billInfo = abBillDiscountManagerDao.getAbBillDiscountManagerVoByParams(params,
						corpAuthSql, abVoucherinfoViceService.isNotDiscountApply());
				if (billInfo == null) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOOP);
					managerVo.addErrorMsg(errorList);
					continue;
				} else if (billInfo.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_APPROVEOP);
					managerVo.addErrorMsg(errorList);
					continue;
				} else {
					managerVo.setAccIds(billInfo.getAccId());
					managerVo.setVouIds(billInfo.getVouId());
					managerVo.setViceIds(billInfo.getViceId());
					managerVo.setBillFlag(billInfo.getBillFlag());
					managerVo.setBillType(billInfo.getBillType());
					managerVo.setBillMoney(billInfo.getBillMoney());
					managerVo.setTransMoneys(managerVo.getTransMoney().toString());
					managerVo.setStartDate(billInfo.getStartDate());
					managerVo.setEndDate(billInfo.getEndDate());
					if(StringUtils.isEmpty(managerVo.getRecAcctId())){
						managerVo.setRecAcctId(billInfo.getRecAcctId());
					}
					if(StringUtils.isEmpty(managerVo.getRecNm())){
						managerVo.setRecNm(billInfo.getRecNm());
					}
					if(StringUtils.isEmpty(managerVo.getRecAcctsvcr())){
						managerVo.setRecAcctsvcr(billInfo.getRecAcctsvcr());
					}
					managerVo.setReqAcctId(billInfo.getBankAcc());
				}
				//不允许拆票的票据交易金额需要与票据金额一样，允许拆分的票据交易金额不能大于票据金额
				if((!"1".equals(billInfo.getBlgrdbagcrclPrmtInd()) && (managerVo.getBillMoney().compareTo(managerVo.getTransMoney()) != 0) ) || (managerVo.getBillMoney().compareTo(managerVo.getTransMoney()) < 0)){
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
				}
				//不启用贴现申请功能直接贴现办理
				if (isNotDiscountApply){
					if(StringUtils.isEmpty(managerVo.getInsideSingName())){
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTAPPLY_INSIDESING_ISNOTNULL);
						managerVo.addErrorMsg(errorList);
						continue;
					}else{
						if (AccebillConstant.AB_INSIDE_SING_Y.equals(managerVo.getInsideSingName())
								|| AccebillConstant.AB_INSIDE_SING_N.equals(managerVo.getInsideSingName())) {
							managerVo.setInsideSing(Integer.parseInt(managerVo.getInsideSingName()));
						} else {
							errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTAPPLY_INSIDESING_ERROR);
							managerVo.addErrorMsg(errorList);
							continue;
						}
					}
					// 中心代管的纸票,只能进行内部贴现
					if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(billInfo.getBillFlag()))
							&& AccebillConstant.AB_ENTER_STOCK_NET.equals(String.valueOf(billInfo.getEnterStock()))
							&& AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(managerVo.getInsideSing()))) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTAPPLY_BILLFLAG10ANDNET_TOINNER);
						managerVo.addErrorMsg(errorList);
						continue;
					}
					// 银行托管的纸票,只能贴到银行
					if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(billInfo.getBillFlag()))
							&& AccebillConstant.AB_ENTER_STOCK_BANK.equals(String.valueOf(billInfo.getEnterStock()))
								&& AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(managerVo.getInsideSing()))) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG10ANDBANK_TOOUTER);
						managerVo.addErrorMsg(errorList);
						continue;
					}
					// 单位库存的电票,只能贴到银行
					if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(billInfo.getBillFlag()))
							&& AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(managerVo.getInsideSing()))) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG11_TOOUTER);
						managerVo.addErrorMsg(errorList);
						continue;
					}
					// 贴入到结算中心
					if (managerVo.getInsideSing() != null
							&& AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(managerVo.getInsideSing()))) {
						if (StringUtils.isEmpty(managerVo.getRecAcctId())) {
							managerVo.setRecAcctsvcr(null);
							if (StringUtils.isEmpty(managerVo.getRecNm())) {
								errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTNULL);
								managerVo.addErrorMsg(errorList);
								continue;
							}
						} else {
							if (bankAccDTOMap.containsKey(managerVo.getRecAcctId())) {
								bankAccInfo = bankAccDTOMap.get(managerVo.getRecAcctId());
							} else {
								bankAccInfo = abBtBankAccService.getBankAccInfo(managerVo.getRecAcctId());
							}
							if (bankAccInfo == null) {
								errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTEXIST);
							} else {
								bankAccDTOMap.put(bankAccInfo.getBankAcc(), bankAccInfo);
								managerVo.setRecNm(bankAccInfo.getAccName());
								managerVo.setRecAcctsvcr(bankAccInfo.getBankCode());
							}
						}
					}
					// 贴入到银行
					if (managerVo.getInsideSing() != null
							&& AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(managerVo.getInsideSing()))) {
						if (StringUtils.isEmpty(managerVo.getRecAcctsvcr()) && StringUtils.isEmpty(managerVo.getRecNm())) {
							errorList.add(AccebillConstant.LANG_ERROR_DISCOUNTREC_CODEANDNAME_NONULL);
						} else {
							if (StringUtils.isNotEmpty(managerVo.getRecAcctsvcr())) {
								if (bankCodeInfoDTOMap.containsKey(managerVo.getRecAcctsvcr())) {
									bankInfo = bankCodeInfoDTOMap.get(managerVo.getRecAcctsvcr());
								} else {
									bankInfo = abInputBankInfoService.getByBankCode(managerVo.getRecAcctsvcr());
									if (bankInfo != null) {
										bankNameInfoDTOMap.put(bankInfo.getBankName(), bankInfo);
										bankCodeInfoDTOMap.put(bankInfo.getBankCode(), bankInfo);
									}
								}
							}
							if (bankInfo == null && StringUtils.isNotEmpty(managerVo.getRecNm())) {
								if (bankNameInfoDTOMap.containsKey(managerVo.getRecNm())) {
									bankInfo = bankNameInfoDTOMap.get(managerVo.getRecNm());
								} else {
									bankInfo = abInputBankInfoService.getByBankName(managerVo.getRecNm());
									if (bankInfo != null) {
										bankNameInfoDTOMap.put(bankInfo.getBankName(), bankInfo);
										bankCodeInfoDTOMap.put(bankInfo.getBankCode(), bankInfo);
									}
								}
							}
							// 贴入人银行数据校验
							if (bankInfo == null) {
								errorList.add(AccebillConstant.LANG_ERROR_DISCOUNTREC_CODEORNAME);
							} else {
								managerVo.setRecAcctsvcr(bankInfo.getBankCode());
								managerVo.setRecNm(bankInfo.getBankName());
							}
						}
					}
				}
				// 贴现种类
				if (StringUtils.isEmpty(managerVo.getRpdmkName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RPDMK_ISNOTNULL);
				} else {
					if (AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT.equals(managerVo.getRpdmkName())
							|| AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(managerVo.getRpdmkName())) {
						managerVo.setRpdmk(managerVo.getRpdmkName());
					} else {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RPDMK_ERROR);
					}
				}
				// 清算方式
				if (StringUtils.isEmpty(managerVo.getSttlmmkName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_STTLMMK_ISNOTNULL);
				} else {
					if (AccebillConstant.AB_STTLMMK_Y.equals(managerVo.getSttlmmkName())
							|| AccebillConstant.AB_STTLMMK_N.equals(managerVo.getSttlmmkName())) {
						managerVo.setSttlmmk(managerVo.getSttlmmkName());
					} else {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_STTLMMK_ERROR);
					}
				}
				// 贴现日期
				if (managerVo.getDiscountDate() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTDATE_ISNOTNULL);
				} else {
					if (managerVo.getDiscountDate().getTime() - managerVo.getStartDate().getTime() < 0
							|| managerVo.getDiscountDate().getTime() - managerVo.getEndDate().getTime() >= 0) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTDATE_COMPARESTARTANDEND);// 贴现申请日期必须大于等于出票日期,小于到期日期!
					}
				}
				// 赎回开放日,截止日
				if (AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(managerVo.getRpdmkName())) {
					if (managerVo.getRpdopendt() == null) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RM01_RPDOPENDT_ISNOTNULL);
					}
					if (managerVo.getRpdduedt() == null) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RM01_RPDDUEDT_ISNOTNULL);
					}
					if (managerVo.getRpdopendt() != null && managerVo.getRpdduedt() != null) {
						if (managerVo.getRpdopendt().getTime() - managerVo.getRpdduedt().getTime() >= 0) {
							errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMDUETOOPENMSG);// 赎回截止日必须大于赎回开放日
						}
						if (managerVo.getDiscountDate() != null) {
							if (managerVo.getDiscountDate().getTime() - managerVo.getRpdopendt().getTime() > 0) {
								errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDOPENDTMSG);// 赎回开放日不能小于贴现日期!
							}
							if (managerVo.getDiscountDate().getTime() - managerVo.getRpdopendt().getTime() >= 0) {
								errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDDUEDTMSG);// 赎回截止日必须大于贴现日期!
							}
						}
						if (!(managerVo.getRpdopendt().getTime() - managerVo.getStartDate().getTime() >= 0
								&& managerVo.getRpdopendt().getTime() - managerVo.getEndDate().getTime() < 0)) {// 赎回开放日必须大于等于出票日期小于到期日期!
							errorList.add(AccebillConstant.BILL_ACCEBILL_RPDOPENDT_COMPARESTARTANDEND);
						}
						if (!(managerVo.getRpdduedt().getTime() - managerVo.getStartDate().getTime() >= 0
								&& managerVo.getRpdduedt().getTime() - managerVo.getEndDate().getTime() < 0)) {// 赎回截止日必须大于出票日期小于到期日期
							errorList.add(AccebillConstant.BILL_ACCEBILL_RPDDUEDT_COMPARESTARTANDEND);
						}
					}
				}
				// 划款天数
				if (StringUtils.isEmpty(managerVo.getPaydateName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYDATE_ISNOTNULL);
				} else {
					if (AccebillConstant.AB_PAYDATE_HERE.equals(managerVo.getPaydateName())
							|| AccebillConstant.AB_PAYDATE_THERE.equals(managerVo.getPaydateName())) {
						managerVo.setPaydate(Integer.parseInt(managerVo.getPaydateName()));
					} else {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYDATE_ERROR);
					}
				}
				// 假期顺延天数
				if (managerVo.getDeferdate() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DEFERDATE_ISNOTNULL);
				}
				// 贴现天数
				if (managerVo.getDaynum() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DAYNUM_ISNOTNULL);
				}
				// 是否买方付息
				if (StringUtils.isEmpty(managerVo.getBuyerinterestSingName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTSING_ISNOTNULL);
				} else {
					if (AccebillConstant.AB_BUYERINTERESTSING_Y.equals(managerVo.getBuyerinterestSingName())
							|| AccebillConstant.AB_BUYERINTERESTSING_N.equals(managerVo.getBuyerinterestSingName())) {
						managerVo.setBuyerinterestSing(managerVo.getBuyerinterestSingName());
					} else {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTSING_ISNOTNULL);
					}
				}
				if (managerVo.getBuyerinterestRatio() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTRATIO_ISNOTNULL);
				} else {
					if (!(managerVo.getBuyerinterestRatio().compareTo(BigDecimal.valueOf(0.0000)) >= 0
							&& managerVo.getBuyerinterestRatio().compareTo(BigDecimal.valueOf(100.0000)) <= 0)) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTRATIO_ERROR);//
					} else {
						if (StringUtils.isNotEmpty(managerVo.getBuyerinterestSing()) && // 是否买方付息为是时,买方付息比率必须为100%
								AccebillConstant.AB_BUYERINTERESTSING_Y.equals(managerVo.getBuyerinterestSing())) {
							if (managerVo.getBuyerinterestRatio().compareTo(BigDecimal.valueOf(100.0000)) != 0) {
								errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTRATIO_100);
							}
						}
					}
				}
				// 贴现利率
				if (managerVo.getInterestRates() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTERESTRATES_ISNOTNULL);
				} else {
					if (!(managerVo.getInterestRates().compareTo(BigDecimal.valueOf(0.0000)) >= 0
							&& managerVo.getInterestRates().compareTo(BigDecimal.valueOf(100.0000)) <= 0)) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTERESTRATES_ERROR);//
					}
				}
				// 贴现利息
				if (managerVo.getInterest() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTEREST_ISNOTNULL);
				} else {
					if (managerVo.getBillMoney() != null
							&& managerVo.getInterest().compareTo(managerVo.getBillMoney()) > 0) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTEREST_ERROR); // 贴现利息不能大于票据金额
					}
				}
				// 买方付息金额
				if (managerVo.getBuyerinterestMoney() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTMONEY_ISNOTNULL);
				} else {
					if (managerVo.getInterest() != null
							&& managerVo.getBuyerinterestMoney().compareTo(managerVo.getInterest()) > 0) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUYERINTERESTMONEY_ERROR); // 买方付息金额不能大于贴现利息
					}
				}
				// 实付金额
				if (managerVo.getRealMoney() == null) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_REALMONEY_ISNOTNULL);
				} else {
					if (managerVo.getBillMoney() != null
							&& managerVo.getRealMoney().compareTo(managerVo.getBillMoney()) > 0) {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_REALMONEY_ERROR); // 实付金额不能大于票据金额
					}
				}
				// 不得转让标记
				if (StringUtils.isEmpty(managerVo.getBankendrsmtmkName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ISNOTNULL);
				} else {
					if (AccebillConstant.AB_BANEM_Y.equals(managerVo.getBankendrsmtmkName())
							|| AccebillConstant.AB_BANEM_N.equals(managerVo.getBankendrsmtmkName())) {
						managerVo.setBankendrsmtmk(managerVo.getBankendrsmtmkName());
					} else {
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ERROR);
					}
				}
				// 入账账户,行号
				if (StringUtils.isNotEmpty(String.valueOf(managerVo.getBillFlag()))
						&& AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(managerVo.getBillFlag()))) {
					if (StringUtils.isEmpty(managerVo.getAoaccninfAc())) {// 入账行号
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_AOACCNINFAC_BILLFLAG11_ISNOTNULL);
					}
					if (StringUtils.isEmpty(managerVo.getAoaccninfId())) {// 入账账户
						errorList.add(AccebillConstant.ACCEBILL_MESSAGE_AOACCNINFID_BILLFLAG11_ISNOTNULL);
					}
				}
				// 是否挂接资金计划
				if(budgetSel){
					Boolean reportBudget = reportBudgetMap.get(corpInfo.getId());
					if (reportBudget == null) {
						reportBudget = abVoucherinfoViceService.isBudget(corpInfo.getId(), AccebillConstant.BRIEF_STATUS_011);
						reportBudgetMap.put(corpInfo.getId(), reportBudget);
					}
					if (reportBudget && StringUtils.isEmpty(managerVo.getItemCodeStr())) {
						errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST);
					}else if(reportBudget && StringUtils.isNotEmpty(managerVo.getItemCodeStr())){
						FbsItemDTO fbsItemDTO = fbsItemMap.get(managerVo.getItemCodeStr());
						//判断计划科目是否存在
						if(fbsItemDTO==null){
							fbsItemDTO = abFbsItemService.getFbsItemByCode(managerVo.getItemCodeStr());
							fbsItemMap.put(managerVo.getItemCodeStr(), fbsItemDTO);
						}
						if (EmptyUtils.isEmpty(fbsItemDTO)) {
							errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNULL);
						}else{
							// 判断资金计划科目代码是否属于该业务
							if (abAccrecordsService.itemCodeIsMatchBusiness(AccebillConstant.AB_CONFIG_DISCOUNTITEM, fbsItemDTO.getItemCode())) {
								errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISMATCH_BUSINESS);
							} else {
								FbsBudgetProcessDTO budgetProcessDTO = new FbsBudgetProcessDTO();
								budgetProcessDTO.setItemCode(managerVo.getItemCodeStr());
								budgetProcessDTO.setItemName(fbsItemDTO.getItemName());
								budgetProcessDTO.setProcessMoney(managerVo.getBillMoney());
								budgetProcessDTO.setProcessCurMoney(managerVo.getBillMoney());
								budgetProcessDTO.setExchangeRate(new BigDecimal(1));
								List<FbsBudgetProcessDTO> budgetList = new ArrayList<>();
								budgetList.add(budgetProcessDTO);
								managerVo.setBudgetList(budgetList);
							}
						}
					}
				}
				managerVo.addErrorMsg(errorList);
			}
		}
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillDiscountManagerVOById(aid);
	}
}
