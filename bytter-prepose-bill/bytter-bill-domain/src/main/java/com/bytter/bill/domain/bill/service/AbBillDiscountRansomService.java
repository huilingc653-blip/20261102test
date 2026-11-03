package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillDiscountRansomDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillDiscountRansomVo;
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
import com.bytter.core.utils.DateUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据-贴现赎回申请
 * @author onlineGenerator
 * @date 2017-09-07
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTRANSOM)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTRANSOM)
@Service("abBillDiscountRansomService")
@Transactional
public class AbBillDiscountRansomService extends BaseService<AbBillDiscountRansomVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillDiscountRansomDao abBillDiscountRansomDao;
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
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<AbBillDiscountRansomVo, String> getEntityDao() {
		return abBillDiscountRansomDao;
	}
	/**
	 * 列表显示应收票据贴现赎回申请信息
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillDiscountRansom(DataTablesInput input) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTRANSOM_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTRANSOM_QUERY,corpIds);
		DataTablesOutput<AbBillDiscountRansomVo> dataTablesOutput = abBillDiscountRansomDao.listAbBillDiscountRansom(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_DISCOUNTRANSOM_STATUS"
					, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillDiscountRansomVo bill : listData){
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
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillDiscountRansomVo bill : listData){
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
	private void injectValues(AbBillDiscountRansomVo bill,Map<String, Map<String, DictDTO>> listDictMap){
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ABBILL_DISCOUNTRANSOM_STATUS");
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
	 * 批量贴现赎回申请
	 * 
	 * @return
	 */
	public boolean batchSubmit(AbBillDiscountRansomVo AbBillDiscountRansomVo, ICurrentUser userVo) {
		String[] vvids = AbBillDiscountRansomVo.getVvids().split(",");
		String[] aids = AbBillDiscountRansomVo.getAids().split(",");
		// 内部贴现，贴现到账校验
		checkDiscountAccount(AbBillDiscountRansomVo);
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
		List<AbAccrecords> abList = getAbAccrecords(AbBillDiscountRansomVo, userVo);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != aids.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量保存贴现赎回申请返还利息
		List<AbDiscount> abDiscountList = getAbDiscountList(abList, AbBillDiscountRansomVo);
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
			// 批量插入流水表数据
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());
			// 启动工作流
			AbAccrecordsVO variables = new AbAccrecordsVO();
			AbVoucherinfo vouInfo = abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo = abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTRANSOM, abList.get(i).getId(), variables,
					vouViceInfo.getApplyCorpId(),userVo.getId());
		}
		// 批量保存日志
		abLogService.batchSaveAbLog(vvids, aids, AccebillConstant.BRIEF_STATUS_012, AccebillConstant.ABLOG_LOGTYPE_ADD,
				AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		return true;
	}
	/**
	 * 根据请求参数生成贴现赎回申请流水，并做业务校验
	 * 
	 * @param AbBillDiscountRansomVo
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbBillDiscountRansomVo AbBillDiscountRansomVo, ICurrentUser userVo) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		String[] aids = AbBillDiscountRansomVo.getAids().split(",");
		for (String aid : aids) {
			// 当前流水表信息(收票登记流水)
			AbAccrecords abAccrecords = abAccrecordsService.get(aid);
			AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
			// 新增赎回申请流水
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
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_012);// 流水业务类型
			entity.setInsideSing(abAccrecords.getInsideSing());// 内部业务标志
			entity.setBankendrsmtmk(abVoucherinfo.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01
																		// 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(AbBillDiscountRansomVo.getBusinessDate());// 业务办理日期
			entity.setBillFlag(abVoucherinfo.getBillFlag());
			// .......业务申请人...，业务接收人....
			entity.setReqRole(abAccrecords.getRecRole());// 业务申请人类别
			entity.setReqNm(abAccrecords.getRecNm());// 业务申请人
			entity.setReqCmonid(abAccrecords.getRecCmonid());// 业务申请人组织机构代码
			entity.setReqAcctId(abAccrecords.getRecAcctId());// 业务申请人账号
			entity.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务申请人开户行号
			entity.setReqAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());// 业务申请人承接行号
			entity.setRecRole(abAccrecords.getReqRole());// 业务接收人类别
			entity.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
			entity.setRecCmonid(abAccrecords.getReqCmonid());// 业务接收人组织机构代码
			entity.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
			entity.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
			entity.setRecAcctsvcrAg(abAccrecords.getReqAcctsvcrAg());// 业务接收人承接行号
			entity.setCreateName(userVo.getUsername());// 创建人名称
			entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
			entity.setCreateDate(new Date());// 创建日期
			abList.add(entity);
		}
		return abList;
	}
	/**
	 * 判断是否已做贴现到账 业务校验:内部票据贴现到中心时，中心在做贴现赎回申请前，贴出单位需要先做贴现到账，没有做则提示
	 */
	protected void checkDiscountAccount(AbBillDiscountRansomVo AbBillDiscountRansomVo) {
		String[] aids = AbBillDiscountRansomVo.getAids().split(",");
		// int count = 0;
		boolean flag = false;
		for (String aid : aids) {
			AbAccrecords abAccrecords = abAccrecordsService.get(aid);
			// 内部贴现校验(收票登记的lastid不为空，则为内部业务,此处的abAccrecords.getLastid
			// ()为贴现办理流水id)
			if (StringUtils.isNotEmpty(abAccrecords.getLastid())) {
				List<AbAccrecords> list = abAccrecordsService.getAbAccrecordsbyLastid(abAccrecords.getLastid());
				if (list != null && !list.isEmpty()) {
					for (AbAccrecords lis : list) {
						if (lis.getBriefId().equals("6012")
								&&lis.getStatus()==AccebillConstant.AB_BILL_BUS_STATUS95) {
							// 已做贴现到账
							flag = true;
						}
					}
				}
			}
			if (!flag) {
				// 内部贴现没有贴现到账，不能发起贴现申请赎回
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CHECKDISCOUNTACCOUNT);
			}
			// count++;
		}
	}
	/**
	 * 根据请求参数生成贴现赎回申请返还利息信息
	 * 
	 * @return
	 */
	protected List<AbDiscount> getAbDiscountList(List<AbAccrecords> abList,
			AbBillDiscountRansomVo abBillDiscountRansomVo) {
		List<AbDiscount> list = new ArrayList<AbDiscount>();
		String returnInterests = abBillDiscountRansomVo.getReturnInterests();
		String interestRatesList = abBillDiscountRansomVo.getInterestRatesList();
		if (abList != null && abList.size() > 0 && StringUtils.isNotEmpty(returnInterests)) {
			String[] interestAry = returnInterests.split(",");
			String[] interestRatesAry = interestRatesList.split(",");
			String[] rpdopendtAry = abBillDiscountRansomVo.getRpdopendtAry().split(",");
			String[] rpdduedtAry = abBillDiscountRansomVo.getRpdduedtAry().split(",");
			if (abList.size() == interestAry.length) {
				AbDiscount abDiscount = null;
				for (int i = 0; i < interestAry.length; i++) {
					abDiscount = new AbDiscount();
					abDiscount.setRecordId(abList.get(i).getId());
					abDiscount.setInterest(new BigDecimal(interestAry[i]));// 贴现赎回返还利息
					if (StringUtils.isEmpty(interestRatesAry[i])) {
						interestRatesAry[i] = "0.0";
					}
					abDiscount.setInterestRates(new BigDecimal(interestRatesAry[i]));// 贴现利率
					abDiscount.setRpdopendt(DateUtils.str2Date(rpdopendtAry[i], new SimpleDateFormat("yyyy-MM-dd")));
					abDiscount.setRpdduedt(DateUtils.str2Date(rpdduedtAry[i], new SimpleDateFormat("yyyy-MM-dd")));
					list.add(abDiscount);
				}
			}
		}
		return list;
	}
	/**
	 * 生成贴现赎回申请流水
	 * 
	 * @return
	 */
	protected AbAccrecords getAbAccrecords6212(String aid, String lastid, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		AbAccrecords abAccrecordsNow = abAccrecordsService.get(aid, true);// 当前流水--赎回申请流水
		AbAccrecords abAccrecordsOld = abAccrecordsService.get(lastid, true);// 内部贴现办理的流水
		BeanUtils.copyNotNullProperties(abAccrecordsOld, entity);
		entity.setId(null);
		// 根据贴现办理流水id获取贴现到账流水
		List<AbAccrecords> list = abAccrecordsService.getAbAccrecordsbyLastid(lastid);
		for (AbAccrecords lis : list) {
			if (AccebillConstant.BRIEF_STATUS_6012.equals(lis.getBriefId())) {
				entity.setLastid(lis.getId());
				entity.setLastbillcode(abVoucherinfoService.get(lis.getVid()).getBillStatus());
				break;
			}
		}
		// entity.setLastid(aid);
		// entity.setLastbillcode(abAccrecordsNow.getBriefId());
		entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志
																	// 0未完成；1可用；2已结束
		entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态：-2已删除；-1打回；0
																// 暂存；10提交；11-94审批中；95
																// 已完成
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_6212);// 流水业务类型
		entity.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_Y));// 内部业务标志
		entity.setBusinessDate(new Date());// 业务办理日期
		// .......业务申请人...，业务接收人....
		entity.setReqRole(abAccrecordsNow.getReqRole());// 业务申请人类别
		entity.setReqNm(abAccrecordsNow.getReqNm());// 业务申请人
		entity.setReqCmonid(abAccrecordsNow.getReqCmonid());// 业务申请人组织机构代码
		entity.setReqAcctId(abAccrecordsNow.getReqAcctId());// 业务申请人账号
		entity.setReqAcctsvcr(abAccrecordsNow.getReqAcctsvcr());// 业务申请人开户行号
		entity.setReqAcctsvcrAg(abAccrecordsNow.getReqAcctsvcrAg());// 业务申请人承接行号
		entity.setRecRole(abAccrecordsNow.getRecRole());// 业务接收人类别
		entity.setRecNm(abAccrecordsNow.getRecNm());// 业务接收人名称
		entity.setRecCmonid(abAccrecordsNow.getRecCmonid());// 业务接收人组织机构代码
		entity.setRecAcctId(abAccrecordsNow.getRecAcctId());// 业务接收人账号
		entity.setRecAcctsvcr(abAccrecordsNow.getRecAcctsvcr());// 业务接收人开户行号
		entity.setRecAcctsvcrAg(abAccrecordsNow.getRecAcctsvcrAg());// 业务接收人承接行号
		entity.setCreateName(userVo.getUsername());// 创建人名称
		entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		entity.setCreateDate(new Date());// 创建日期
		entity.setUpdateName(null);// 修改人名称
		entity.setUpdateBy(null);// 修改人登录账号
		entity.setUpdateDate(null);// 修改日期
		return entity;
	}
	/**
	 * 根据条件查询开票登记信息，用于详细页面
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillDiscountRansomVo getAbBillDiscountRansomVoById(String aid) {
		AbBillDiscountRansomVo vo = null;
		if (StringUtils.isNotEmpty(aid)) {
			vo = abBillDiscountRansomDao.getAbBillDiscountRansomVoById(aid);
		}
		BeanUtils.copyNotNullProperties(abAccrecordsService.get(aid), vo);
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_DISCOUNTRANSOM_STATUS"
				, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND");
		injectValues(vo, listDictMap);
		Set<String> corpIdSet = new HashSet<>();

		if(StrUtil.isNotBlank(vo.getApplyCorpId())){
			corpIdSet.add(vo.getApplyCorpId());
		}
		if(StrUtil.isNotBlank(vo.getCorpId())){
			corpIdSet.add(vo.getCorpId());
		}
		if(StrUtil.isNotBlank(vo.getBusCorpId())){
			corpIdSet.add(vo.getBusCorpId());
		}

		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));

		CorpDTO corpDTO = stringCorpDTOMap.get(vo.getApplyCorpId());
		CorpDTO corpDTO2 = stringCorpDTOMap.get(vo.getCorpId());
		CorpDTO corpDTO3 = stringCorpDTOMap.get(vo.getBusCorpId());
		vo.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
		vo.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
		vo.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
		vo.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
		vo.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());

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
		AbAccrecords info = abAccrecordsService.get(aid, true);
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
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords abAccrecords, String logType, String appMsg, ICurrentUser userVo){
		int num = 0;
		String collectId = null;
		/** 审批通过 */
		// 票据赎回申请流水表status改为95 validSing改为1
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 对内的贴现赎回申请，自动办理贴现收回
		AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid());
		if (StringUtils.isNotEmpty(abAccrecordsLast.getLastid())) {
			// 写一笔贴现收回流水
			AbAccrecords abAccrecordsNew = getAbAccrecords6212(abAccrecords.getId(), abAccrecordsLast.getLastid(), userVo);
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			// 贴现到账的流水变为结束
			num = abAccrecordsService.updateValidSign(abAccrecordsNew.getLastid(),
					AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			collectId = abAccrecordsNew.getId();
		}
		// 从表box_status改为11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		if (StringUtils.isNotEmpty(collectId)) {
			// 票据自动收回计算票据账户余额
			abBillAccBalService.setBankAccBillBal(collectId, userVo);
		}
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_012, logType, appMsg, userVo);
	}
	
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords abAccrecords, String logType, String appMsg, ICurrentUser userVo){
		int num = 0;
		/** 打回 */
		// 票据收赎回申请流水表status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 上一条流水恢复为有效
		num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 删除贴现赎回申请利息
		abDiscountService.deleteByRecordId(abAccrecords.getId());
		// 从表添加打回标记
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_012,
				abAccrecords.getId());
		
		// 从表box_status改为11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_012, logType, appMsg, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTRANSOM, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillDiscountRansomVo> dataTablesOutput = abBillDiscountRansomDao.dataTableApprove(dataTablesInput, businessList);

		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_DISCOUNTRANSOM_STATUS"
					, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillDiscountRansomVo bill : listData){
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
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillDiscountRansomVo bill : listData){
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

		return workFlowBusiness.merge(dataTablesOutput);
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
		return this.getAbBillDiscountRansomVoById(aid);
	}
}
