package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillSearchforApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO;
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
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
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
 * @Description: 追索通知处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORAPPLY)
@Service("abBillSearchforApplyService")
@Transactional
public class AbBillSearchforApplyService extends BaseService<AbBillSearchforApplyVO, String>
		implements IAccebillBaseService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillSearchforApplyVO> {
	@Autowired
	private AbBillSearchforApplyDao abBillSearchforApply;
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
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;

	@DubboReference(check = false)
	private IBtBankService iBtBankService;
	public BaseDao<AbBillSearchforApplyVO, String> getEntityDao() {
		return abBillSearchforApply;
	}
	/**
	 * 加载AbBillSearchforApply表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillSearchforApplyInfo(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_SEARCHFORAPPLY_QUERY,dataTablesInput.getCurrentUserId()), "c.apply_corp_id",
				true);
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_SEARCHFORAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillSearchforApplyVO> dataTablesOutput = abBillSearchforApply.listAbBillSearchforApply(dataTablesInput, corpAuthSql);
		Set<String> corpIdSet = new HashSet<>();
		//Set<String> applyCorpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_SEARCHFOR_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_RCRS_TP","AB_RCRSRSNCD");
			for (AbBillSearchforApplyVO bill : listData){
				bill.injectValues(listDictMap);
				if(StrUtil.isNotBlank(bill.getApplyCorpId())){
					corpIdSet.add(bill.getApplyCorpId());
				}
				if(StrUtil.isNotBlank(bill.getCorpId())){
					corpIdSet.add(bill.getCorpId());
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
			for (AbBillSearchforApplyVO bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
			}
		});
		return dataTablesOutput;
	}
	/**
	 * 导入追索通知申请
	 * 
	 * @param list
	 *            导入vo信息
	 * @return 处理结果
	 */
	public boolean saveImport(List<AbBillSearchforApplyVO> list, ICurrentUser userVo) {
		for (AbBillSearchforApplyVO reqInfo : list) {
			batchApply(reqInfo, userVo);
		}
		return true;
	}
	/**
	 * 追索通知批量申请操作
	 * 
	 * @param reqInfo
	 * @param userVo
	 */
	public void batchApply(AbBillSearchforApplyVO reqInfo, ICurrentUser userVo) {
		String id = reqInfo.getIds();
		if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			String[] ids = StringUtils.split(id, ",");
			String[] rcrsTps = StringUtils.split(reqInfo.getRcrsTps(), ",");
			String[] searchForMoneys = StringUtils.split(reqInfo.getSearchForMoneys(), ";");
			// 设置id对应的追索通知类型
			Map<String, String> searchForMap = new HashMap<String, String>();
			for (int i = 0; i < ids.length && i < rcrsTps.length && i < searchForMoneys.length; i++) {
				searchForMap.put(ids[i], rcrsTps[i] + ";" + searchForMoneys[i]);
			}
			AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
			accrecordsVo.setIdArray(ids);
			accrecordsVo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(accrecordsVo);
			if (ids.length != accrecordsList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			if (accrecordsList != null && !accrecordsList.isEmpty()) {
				List<AbAccrecords> searchforAccrecordslist = new ArrayList<AbAccrecords>();
				int addSearchforAccrecordsSize = accrecordsToSearchforAccrecords(searchforAccrecordslist,
						accrecordsList, reqInfo, searchForMap, userVo);
				if (accrecordsList.size() != addSearchforAccrecordsSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				String[] vvids = new String[addSearchforAccrecordsSize];
				for (int i = 0; i < addSearchforAccrecordsSize; i++) {
					vvids[i] = searchforAccrecordslist.get(i).getVvid();
					String msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					String logType = reqInfo.isImport() ? AccebillConstant.ABLOG_LOGTYPE_IMPORT
							: AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
					abLogService.saveAbLog(searchforAccrecordslist.get(i).getVvid(),
							searchforAccrecordslist.get(i).getId(), searchforAccrecordslist.get(i).getBriefId(),
							logType, msg, userVo);
				}
				int upBoxStatusNum = abVoucherinfoViceService.batchUpdateBoxStatus(vvids,
						AccebillConstant.AB_BOX_STATUS_N, userVo);
				if (addSearchforAccrecordsSize != upBoxStatusNum) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < addSearchforAccrecordsSize; i++) {
					AbAccrecords accrecords = searchforAccrecordslist.get(i);
					abBillNotificationService.completeMsg(accrecords,userVo);
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecords.getVvid());
					AbAccrecordsVO variables = new AbAccrecordsVO();
					BeanUtils.copyNotNullProperties(accrecords.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(accrecords, variables);
					 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORAPPLY, accrecords.getId(),
							variables, accrecords.getVouViceInfo().getApplyCorpId(),userVo.getId());
					// /**
					// * 默认审批通过
					// */
					// approveOver(bailAccrecordslist.get(i).getId(), true,
					// "审批通过！");
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
	 * 批量转换追索通知申请数据
	 * 
	 * @param bailAccrecordslist
	 * @param accrecordslist
	 * @param reqInfo
	 * @return
	 */
	public int accrecordsToSearchforAccrecords(List<AbAccrecords> searchforAccrecordslist,
			List<AbAccrecords> accrecordslist, AbBillSearchforApplyVO reqInfo, Map<String, String> searchForMap,
			ICurrentUser userVo) {
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
			bailInfo.setBusinessDate(DateUtils.getDateYMD());
			bailInfo.setLastid(oldInfo.getId());
			bailInfo.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			bailInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			bailInfo.setBriefId(AccebillConstant.BRIEF_STATUS_022);
			bailInfo.setLastbillcode(vouInfo.getBillStatus());
			bailInfo.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			bailInfo.setBillFlag(vouInfo.getBillFlag());
			if (searchForMap.containsKey(oldInfo.getId())) {
				String[] searchForInfors = searchForMap.get(oldInfo.getId()).split(";");
				String rcrsTp = searchForInfors[0];
				String rcrsokmoney = searchForInfors[1];
				bailInfo.setRcrsTp(rcrsTp);
				bailInfo.setRcrsokmoney(new BigDecimal(rcrsokmoney));
				// if(AccebillConstant.AB_RCRS_TP_PROTESTN.equals(rcrsTp)){
				bailInfo.setRcrsrsncd(reqInfo.getRcrsrsncd());
				// }
			}
			bailInfo.setRmk(reqInfo.getRmk());
			bailInfo.setReqAcctId(vice.getBankAcc());
			bailInfo.setRecNm(reqInfo.getRecNm());
			bailInfo.setRecCmonid(reqInfo.getRecCmonid());
			bailInfo.setRecAcctId(reqInfo.getRecAcctId());
			bailInfo.setRecAcctsvcr(reqInfo.getRecAcctsvcr());
			searchforAccrecordslist.add(bailInfo);
		}
		return abAccrecordsService.batchSaveAccrecords(searchforAccrecordslist, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORAPPLY, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "l.id", true);
		DataTablesOutput<AbBillSearchforApplyVO> dp = abBillSearchforApply.dataTableApprove(dataTablesInput, businessSql);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_SEARCHFOR_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_RCRS_TP","AB_RCRSRSNCD");
			for (AbBillSearchforApplyVO bill : listData){
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
	public AbBillSearchforApplyVO getAbBillSearchforApplyVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbAccrecordsVO accVo = new AbAccrecordsVO();
		BeanUtils.copyNotNullProperties(viceInfo, accVo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, accVo);
		AbBillSearchforApplyVO reInfo = new AbBillSearchforApplyVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(accVo, reInfo);
		reInfo.setBankTypeName(abVoucherinfoViceService.findBankTypeNameWith(viceInfo.getId()).toString());
		// BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
				,"AB_BILL_SEARCHFOR_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_RCRS_TP","AB_RCRSRSNCD");
		reInfo.injectValues(listDictMap);
		if(StrUtil.isBlank(accVo.getReqAcctsvcrName()) && StrUtil.isNotBlank(accVo.getReqAcctsvcr())){
			BtBankDTO byBankCode = iBtBankService.getByBankCode(accVo.getReqAcctsvcr());
			if(byBankCode != null){
				reInfo.setReqAcctsvcrName(byBankCode.getBankName());
			}
		}
		return reInfo;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillSearchforApplyVO obj,Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbBillSearchforApplyVO> list,Object... user) {
		if (list != null && !list.isEmpty()) {
			Map<String, String> origNoMap = new HashMap<>();
			Map<String, CorpDTO> corpMap = new HashMap<>();
			Map<String, InputBankInfoDTO> bankCodeKeyMap = new HashMap<>();
			Map<String, InputBankInfoDTO> bankNameKeyMap = new HashMap<>();
			String userId="";
			if(user[0]!=null){
				ICurrentUser userInfo=(ICurrentUser)user[0];
				userId=userInfo.getId();
			}
			String corpAuthSql = SqlUtil.getInSql(
					abCorpService.getUserCorpIdsByUseAccount(AccebillConstant.AB_BILL_RESOURCE_GUARANTEEAPPLY_QUERY,userId), "c.apply_corp_id",
					true);
			for (AbBillSearchforApplyVO info : list) {
				List<String> errorList = new ArrayList<>();
				if (origNoMap.containsKey(info.getOrigNo() + "&" + info.getSbbllRngStrtSn() + "&" + info.getSbbllRngEndSn())) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
					info.addErrorMsg(errorList);
					continue;
				} else {
					origNoMap.put(info.getOrigNo() + "&" + info.getSbbllRngStrtSn() + "&" + info.getSbbllRngEndSn(), info.getOrigNo() + "&" + info.getSbbllRngStrtSn() + "&" + info.getSbbllRngEndSn());
				}
				if (StringUtils.isEmpty(info.getCorpCode()) || StringUtils.isEmpty(info.getOrigNo())
						|| StringUtils.isEmpty(info.getRecNm()) || StringUtils.isEmpty(info.getRecAcctId())
						|| StringUtils.isEmpty(info.getRecCmonid())) {
					continue;
				}
				if (info.getRecCmonid().length() != 18
						&& (info.getRecCmonid().length() == 10 && info.getRecCmonid().indexOf("-") != 8)) {
					errorList.add(AccebillConstant.LANG_BSEARCHFORPERSONCMONID_FORMAT_ERROR);
				}
				CorpDTO corpInfo = null;
				InputBankInfoDTO bankInfo = null;
				if (corpMap.containsKey(info.getCorpCode())) {
					corpInfo = corpMap.get(info.getCorpCode());
				} else {
					corpInfo = abCorpService.getCorpByCode(info.getCorpCode());
					if (corpInfo != null) {
						corpMap.put(corpInfo.getCode(), corpInfo);
					}
				}
				// 单位数据校验
				if (corpInfo == null) {
					errorList.add(AccebillConstant.LANG_ERROR_CORP_NONENTITY);
					info.addErrorMsg(errorList);
					continue;
				} else {
					info.setCorpId(corpInfo.getId());
					info.setCorpName(corpInfo.getName());
				}
				Map<String, Object> params = new HashMap<>();
				params.put("origNo", info.getOrigNo());
				params.put("sbbllRngStrtSn", info.getSbbllRngStrtSn());
				params.put("sbbllRngEndSn", info.getSbbllRngEndSn());
				params.put("corpId", info.getCorpId());
				AbBillSearchforApplyVO billInfo = abBillSearchforApply.getAbBillSearchforApplyVOByParams(params,
						corpAuthSql);
				if (billInfo == null) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOOP);
				} else if (billInfo.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_APPROVEOP);
				} else if (AccebillConstant.AB_RCRS_TP_PROTESTN.equals(billInfo.getRcrsTp())
						&& StringUtils.isEmpty(info.getRcrsrsncdName())) {
					info.setRcrsTp(billInfo.getRcrsTp());
					errorList.add(AccebillConstant.LANG_ERROR_SEARCHFORPERSONBRCRSRSNCD_ISNOTNULL);
				} else {
					info.setIds(billInfo.getId());
					info.setBillMoney(billInfo.getBillMoney());
					if (info.getSearchForMoney() == null || info.getSearchForMoney().doubleValue() < 0.01) {
						info.setSearchForMoney(billInfo.getBillMoney());
						info.setSearchForMoneys(NumberUtil.formatDouble(billInfo.getBillMoney()));
					} else {
						info.setSearchForMoneys(NumberUtil.formatDouble(info.getSearchForMoney()));
					}
					info.setRcrsTp(billInfo.getRcrsTp());
					info.setRcrsTps(billInfo.getRcrsTp());
					info.setRcrsrsncd(info.getRcrsrsncdName());
				}
				if (StringUtils.isEmpty(info.getRecAcctsvcr()) && StringUtils.isEmpty(info.getRecAcctsvcrName())) {
					errorList.add(AccebillConstant.LANG_ERROR_BSEARCHFORPERSONBANK_CODEANDNAME_NONULL);
				} else {
					if (StringUtils.isNotEmpty(info.getRecAcctsvcr())) {
						if (bankCodeKeyMap.containsKey(info.getRecAcctsvcr())) {
							bankInfo = bankCodeKeyMap.get(info.getRecAcctsvcr());
						} else {
							bankInfo = abInputBankInfoService.getByBankCode(info.getRecAcctsvcr());
							if (bankInfo != null) {
								bankCodeKeyMap.put(bankInfo.getBankCode(), bankInfo);
								bankNameKeyMap.put(bankInfo.getBankName(), bankInfo);
							}
						}
					}
					if (bankInfo == null && StringUtils.isNotEmpty(info.getRecAcctsvcrName())) {
						if (bankNameKeyMap.containsKey(info.getRecAcctsvcrName())) {
							bankInfo = bankNameKeyMap.get(info.getRecAcctsvcrName());
						} else {
							bankInfo = abInputBankInfoService.getByBankName(info.getRecAcctsvcrName());
							if (bankInfo != null) {
								bankCodeKeyMap.put(bankInfo.getBankCode(), bankInfo);
								bankNameKeyMap.put(bankInfo.getBankName(), bankInfo);
							}
						}
					}
				}
				// 保证人银行数据校验
				if (bankInfo == null) {
					errorList.add(AccebillConstant.LANG_ERROR_BSEARCHFORPERSONBANK_CODEORNAME);
				} else {
					info.setRecAcctsvcr(bankInfo.getBankCode());
					info.setRecAcctsvcrName(bankInfo.getBankName());
				}
				if (errorList != null && !errorList.isEmpty()) {
					info.addErrorMsg(errorList);
				}
				info.setImport(true);
			}
		}
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillSearchforApplyVOById(aid);
	}
}
