package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillDiscountCollectDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.domain.bill.vo.AbBillDiscountCollectVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据-贴现收回
 * @author onlineGenerator
 * @date 2017-09-05
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTCOLLECT)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTCOLLECT)
@Service("abBillDiscountCollectService")
@Transactional
public class AbBillDiscountCollectService extends BaseService<AbBillDiscountCollectVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillDiscountCollectDao abBillDiscountCollectDao;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private ICorpService corpService;
	@Override
	public BaseDao<AbBillDiscountCollectVo, String> getEntityDao() {
		return abBillDiscountCollectDao;
	}
	/**
	 * 列表显示应收票据贴现收回信息
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillDiscountCollect(DataTablesInput input) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTCOLLECT_QUERY, input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTCOLLECT_QUERY, corpIds);
		DataTablesOutput<AbBillDiscountCollectVo> dataTablesOutput = abBillDiscountCollectDao.listAbBillDiscountCollect(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
					, "ABBILL_DISCOUNTCOLLECT_STATUS", "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM"
					, "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillDiscountCollectVo bill : listData) {
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
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillDiscountCollectVo bill : listData){
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
	private void injectValues(AbBillDiscountCollectVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ABBILL_DISCOUNTCOLLECT_STATUS");
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
	}
	/**
	 * 批量贴现收回
	 * 
	 * @return
	 */
	public boolean batchSubmit(AbBillDiscountCollectVo abBillDiscountCollectVo, ICurrentUser userVo) {
		String[] vvids = abBillDiscountCollectVo.getVvids().split(",");
		String[] aids = abBillDiscountCollectVo.getAids().split(",");
		// 批量修改从表状态为处理中
		int num = abVoucherinfoViceService.batchSubmit(vvids, userVo);
		if (num != vvids.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 清除从表打回标记
		// for (String vid : vvids) {
		// num = abVoucherinfoViceService.updateBackBriefIdIsNull(vid);
		// if (num == 0) {
		// throw new
		// BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		// }
		// }
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(abBillDiscountCollectVo, userVo);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != aids.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量保存贴现收回利息
		List<AbDiscount> abDiscountList = getAbDiscountList(abList, abBillDiscountCollectVo);
		abDiscountService.batchSave(abDiscountList);
		int num2 = 0;
		for (int i = 0; i < num; i++) {
			// 票据当前流水改为作废
			num2 = abAccrecordsService.updatAppPass(abList.get(i).getLastid(), true, userVo);
			if (num2 != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 计算票据账户余额
			abBillAccBalService.setBankAccBillBal(abList.get(i).getId(), userVo);
			abBillNotificationService.completeMsg(abList.get(i),userVo);
			// 清除从表打回标记
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());
			// 启动工作流
			AbAccrecordsVO variables = new AbAccrecordsVO();
			AbVoucherinfo vouInfo = abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo = abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTCOLLECT, abList.get(i).getId(), variables,
					vouViceInfo.getApplyCorpId(),userVo.getId());
		}
		// 批量保存日志
		abLogService.batchSaveAbLog(vvids, aids, AccebillConstant.BRIEF_STATUS_6212,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		return true;
	}
	/**
	 * 根据请求参数生成贴现收回流水
	 * 
	 * @param abBillDiscountCollectVo
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbBillDiscountCollectVo abBillDiscountCollectVo, ICurrentUser userVo) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		String[] aids = abBillDiscountCollectVo.getAids().split(",");
		for (String aid : aids) {
			// 当前流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(aid);
			AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
			// 新增收回流水
			AbAccrecords entity = new AbAccrecords();
			entity.setVid(abVoucherinfo.getId());// 票据主表ID
			entity.setVvid(abVoucherinfoVice.getId());// 票据从表ID
			entity.setLastid(abAccrecords.getId());// 保存上一笔流水ID
			entity.setLastbillcode(abVoucherinfo.getBillStatus());// 保存上一笔业务类型
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
																		// 0未完成；1可用；2已结束
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态：-2已删除；-1打回；0
																	// 暂存；10提交；11-94审批中；95
																	// 已完成
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_6212);// 流水业务类型
			entity.setInsideSing(abAccrecords.getInsideSing());// 内部业务标志
			entity.setBankendrsmtmk(abVoucherinfo.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01
																		// 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(abBillDiscountCollectVo.getBusinessDate());// 业务办理日期
			entity.setBillFlag(abVoucherinfo.getBillFlag());
			// .......业务申请人...，业务接收人....
			entity.setReqRole(abAccrecords.getReqRole());// 业务申请人类别
			entity.setReqNm(abAccrecords.getReqNm());// 业务申请人
			entity.setReqCmonid(abAccrecords.getReqCmonid());// 业务申请人组织机构代码
			entity.setReqAcctId(abVoucherinfoVice.getBankAcc());// 业务申请人账号
			entity.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务申请人开户行号
			entity.setReqAcctsvcrAg(abAccrecords.getReqAcctsvcrAg());// 业务申请人承接行号
			entity.setRecRole(abAccrecords.getRecRole());// 业务接收人类别
			entity.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
			entity.setRecCmonid(abAccrecords.getRecCmonid());// 业务接收人组织机构代码
			entity.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
			entity.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
			entity.setRecAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());// 业务接收人承接行号
			entity.setCreateName(userVo.getUsername());// 创建人名称
			entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
			entity.setCreateDate(new Date());// 创建日期
			abList.add(entity);
		}
		return abList;
	}
	/**
	 * 根据请求参数生成贴现收回流水
	 * 
	 * @return
	 */
	protected List<AbDiscount> getAbDiscountList(List<AbAccrecords> abList,
			AbBillDiscountCollectVo abBillDiscountCollectVo) {
		List<AbDiscount> list = new ArrayList<AbDiscount>();
		String returnInterests = abBillDiscountCollectVo.getReturnInterests();
		String interestRatesList = abBillDiscountCollectVo.getInterestRatesList();
		if (abList != null && !abList.isEmpty() && StringUtils.isNotEmpty(returnInterests)) {
			String[] interestAry = returnInterests.split(",");
			String[] interestRatesAry = interestRatesList.split(",");
			if (abList.size() == interestAry.length) {
				AbDiscount abDiscount = null;
				for (int i = 0; i < interestAry.length; i++) {
					abDiscount = new AbDiscount();
					abDiscount.setRecordId(abList.get(i).getId());
					abDiscount.setInterest(new BigDecimal(interestAry[i]));// 贴现赎回返还利息
					abDiscount.setInterestRates(new BigDecimal(interestRatesAry[i]));// 贴现利率
					list.add(abDiscount);
				}
			}
		}
		return list;
	}
	/**
	 * 根据流水id获取贴现收回信息
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillDiscountCollectVo getAbBillDiscountCollectVoById(String aid,boolean isApprove) {
		AbBillDiscountCollectVo vo = null;
		if (StringUtils.isNotEmpty(aid)) {
			vo = abBillDiscountCollectDao.getAbBillDiscountCollectVoById(aid,isApprove);
			vo.setId(vo.getAid());
		}
		BeanUtils.copyNotNullProperties(abAccrecordsService.get(aid), vo);
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_DISCOUNTCOLLECT_STATUS"
				, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM");
		injectValues(vo, listDictMap);
		vo.setBankTypeName(StringUtils.isNotEmpty(vo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(vo.getVvid()):null);
		return vo;
	}
	/**
	 * 审批
	 * 
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void approve(String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		AbAccrecords info = abAccrecordsService.get(aid);
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
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords abAccrecords, String logType, String appMsg, ICurrentUser userVo){
		int num = 0;
		/** 审批通过 */
		// 票据收回流水表status改为95 validSing改为1
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
//		// 记账(中心贴现给银行的票据做贴现收回的时候记账)
//		AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid());// 贴现到账流水
//		if (abAccrecordsLast != null) {
//			AbAccrecords abAccrecords011 = abAccrecordsService.get(abAccrecordsLast.getLastid());// 贴现办理流水
//			if (abAccrecords011 != null) {
//				AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
//				int corpType = corpService.get(abVoucherinfoVice.getCorpId()).getType();
//				if (AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abAccrecords011.getInsideSing()))
//						&& corpType == CorpConstant.CORP_TYPE_CODE_NET) {
//					abAccbookService.accbookVoucher(abAccrecords, userVo);
//				}
//			}
//		}
		// 余额计算
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		// 从表box_status改为11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6212, logType, appMsg,
				userVo);
	}
	
	/**
	 * 票据业务打回操作
	 * @param abAccrecords 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords abAccrecords, String logType, String appMsg, ICurrentUser userVo){
		int num = 0;
		/** 打回 */
		// 票据收回流水表status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 上一条流水恢复为有效
		num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 删除贴现收回利息
		abDiscountService.deleteByRecordId(abAccrecords.getId());
		// 从表添加打回标记
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_6212,
				abAccrecords.getId());
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		
		// 从表box_status改为11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6212, logType, appMsg,
				userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTCOLLECT, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillDiscountCollectVo> dp = abBillDiscountCollectDao.dataTableApprove(dataTablesInput, businessList);
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
				, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_PAY_DATE", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_SEND_SIGN");
		List<AbBillDiscountCollectVo> list=dp.getData();
		Map<String, CorpDTO> corpMap = corpService.getCorpMap();
		for(AbBillDiscountCollectVo reInfo:list){
			injectValues(reInfo, listDictMap);
			if (StringUtils.isNotBlank(reInfo.getBusCorpId())){
				reInfo.setBusCorpName(corpMap.get(reInfo.getBusCorpId()) != null ? corpMap.get(reInfo.getBusCorpId()).getName(): "");
			}
		}

		return workFlowBusiness.merge(dp);
	}
	/**
	 * 工作流启动回调方法(无需关注)
	 */
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODOAuto-generated method stub
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
		approve(workFlowResult.getBusinessKey(), appMsg, workFlowResult.isPass(), workFlowResult.getUser());
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillDiscountCollectVoById(aid, false);
	}
}
