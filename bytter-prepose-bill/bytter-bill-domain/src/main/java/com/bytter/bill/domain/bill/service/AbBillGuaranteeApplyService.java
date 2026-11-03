package com.bytter.bill.domain.bill.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.dao.AbBillGuaranteeApplyDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillGuaranteeApplyVO;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.*;
/**
 * @Title: Service
 * @Description: 提示保证处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_GUARANTEEAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_GUARANTEEAPPLY)
@Service("abBillGuaranteeApplyService")
@Transactional
public class AbBillGuaranteeApplyService extends BaseService<AbBillGuaranteeApplyVO, String>
		implements IAccebillBaseService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillGuaranteeApplyVO> {
	@Autowired
	private AbBillGuaranteeApplyDao abBillGuaranteeApply;
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
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
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
	private BillTicketSplittingUtils billTicketSplittingUtils;
	@Autowired
	private AbAccrecordsDao abAccrecordsDao;
	@Override
	public BaseDao<AbBillGuaranteeApplyVO, String> getEntityDao() {
		return abBillGuaranteeApply;
	}
	/**
	 * 加载AbBillGuaranteeApply表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillGuaranteeApplyInfo(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_GUARANTEEAPPLY_QUERY,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_GUARANTEEAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillGuaranteeApplyVO> dataTablesOutput = abBillGuaranteeApply.listAbBillGuaranteeApply(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_BAIL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillGuaranteeApplyVO bill : listData){
				bill.injectValues(listDictMap);
			}
		});
		return dataTablesOutput;
	}
	/**
	 * 导入保证申请
	 * 
	 * @param list
	 *            导入vo信息
	 * @return 处理结果
	 */
	public boolean saveImport(List<AbBillGuaranteeApplyVO> list, ICurrentUser userVo) {
		for (AbBillGuaranteeApplyVO reqInfo : list) {
			batchApply(reqInfo, userVo);
		}
		return true;
	}
	/**
	 * 保证批量申请操作
	 * 
	 * @param reqInfo
	 * @param userVo
	 */
	public void batchApply(AbBillGuaranteeApplyVO reqInfo, ICurrentUser userVo) {
		String infoIds = null;
		String viceIds = null;
		String accIds = null;
		String transMoneysTi = null;
		String id = reqInfo.getIds();
		if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			String[] ids = StringUtils.split(id, ",");//流水表id
			String[] tailTypes = StringUtils.split(reqInfo.getBailTypes(), ",");
			String[] transMoneys = StringUtils.split(reqInfo.getTransMoneys(), ",");
			//新一代票据拆票处理start
			List<String> infoList = Lists.newArrayList();
			List<String> viceList = Lists.newArrayList();
			List<String> recordsList = Lists.newArrayList();
			List<String> recordsNoTi = Lists.newArrayList();
			List<String> transMoneyList = Lists.newArrayList();
			List<String> bailTypeList = Lists.newArrayList();
			for (int i = 0; i < ids.length; i++) {
				String s = ids[i];
				//保证顺序只能循环查询
				AbAccrecords cordsDb = abAccrecordsService.get(s);
				AbVoucherinfo infoDb = abVoucherinfoDao.get(cordsDb.getVid());
				AbVoucherinfoVice viceDb = abVoucherinfoViceDao.get(cordsDb.getVvid());
				if (ObjectUtils.isEmpty(infoDb) || ObjectUtils.isEmpty(viceDb)){
					//TODO 证明数据有问题不再进行下面操作
					continue;
				}
				if (StringUtils.isNotBlank(cordsDb.getBailType()) ){
					if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(cordsDb.getBailType())
							&& AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(infoDb.getNewStmIssuofpprInd())
							&& AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(infoDb.getBlgrdbagcrclPrmtInd())){
						//可以拆票， 拿到拆票主表id 从表id和 流水表id
						infoList.add(infoDb.getId());
						viceList.add(viceDb.getId());
						recordsList.add(s);
						transMoneyList.add(transMoneys[i]);
					}else {
						//不可以拆票L
						recordsNoTi.add(s);
						bailTypeList.add(tailTypes[i]);
					}
				}else {
					//不能这样写 按照查询记录语句得知，当业务类型不为 AccebillConstant.BRIEF_STATUS_001，AccebillConstant.BRIEF_STATUS_9002 时，其他都是 AccebillConstant.AB_BAIL_TYPE_RECITE背书保证
					if (!(AccebillConstant.BRIEF_STATUS_001.equals(cordsDb.getBriefId()) || AccebillConstant.BRIEF_STATUS_9002.equals(cordsDb.getBriefId()) )
							&& AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(infoDb.getNewStmIssuofpprInd())
							&& AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(infoDb.getBlgrdbagcrclPrmtInd())){
						//可以拆票
						//拿到拆票主表id 从表id和 流水表id
						infoList.add(infoDb.getId());
						viceList.add(viceDb.getId());
						recordsList.add(s);
						transMoneyList.add(transMoneys[i]);
					}else {
						//不可以拆票L
						recordsNoTi.add(s);
						bailTypeList.add(tailTypes[i]);
					}
				}
			}
			//调用拆票方法
			if (CollectionUtils.isNotEmpty(infoList) && CollectionUtils.isNotEmpty(viceList)
					&& CollectionUtils.isNotEmpty(recordsList) && CollectionUtils.isNotEmpty(transMoneyList) ){
				Map<String, String> resultMap = billTicketSplittingUtils.ticketSplitting(StringUtils.listTtoString(infoList)
						, StringUtils.listTtoString(viceList), StringUtils.listTtoString(recordsList), StringUtils.listTtoString(transMoneyList), userVo);
				infoIds = resultMap.get("infoIds");
				viceIds = resultMap.get("viceIds");
				accIds = resultMap.get("accIds");
				transMoneysTi = resultMap.get("transMoneys");
			}
			//TODO 合并流水表id
			if (StringUtils.isNotBlank(infoIds) && StringUtils.isNotBlank(viceIds) && StringUtils.isNotBlank(accIds) ){
				List<String> accIdListt = StringUtils.splitToList(",", accIds);
				List<AbAccrecords> list = abAccrecordsDao.list(accIdListt);
				if (CollectionUtils.isNotEmpty(list)){
					for (AbAccrecords accrecords : list) {
						recordsNoTi.add(accrecords.getId());
						if(StringUtils.isNotBlank(accrecords.getBailType())){
							bailTypeList.add(accrecords.getBailType());
						}
						//只有背书保证才可以拆票,所以拆票回来的数据中的 保证类型 全都是 背书保证
						if (StringUtils.isBlank(accrecords.getBailType())){
							bailTypeList.add(AccebillConstant.AB_BAIL_TYPE_RECITE);
						}
					}
				}
			}
			ids = StringUtils.listTtoString(recordsNoTi).split(",");
			tailTypes = StringUtils.listTtoString(bailTypeList).split(",");
			//新一代票据拆票处理end
			// 设置id对应的保证类型
			Map<String, String> tailTMap = new HashMap<String, String>();
			for (int i = 0; i < ids.length; i++) {
				tailTMap.put(ids[i], tailTypes[i]);
			}
			AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
			accrecordsVo.setIdArray(ids);
			accrecordsVo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(accrecordsVo);
			if (ids.length != accrecordsList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			if (accrecordsList != null && !accrecordsList.isEmpty()) {
				List<AbAccrecords> bailAccrecordslist = new ArrayList<AbAccrecords>();
				int addBailAccrecordsSize = accrecordsToBailAccrecords(bailAccrecordslist, accrecordsList, reqInfo,
						tailTMap, userVo, ids);
				if (accrecordsList.size() != addBailAccrecordsSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				String[] vvids = new String[addBailAccrecordsSize];
				for (int i = 0; i < addBailAccrecordsSize; i++) {
					vvids[i] = bailAccrecordslist.get(i).getVvid();
					String msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					String logType = reqInfo.isImport() ? AccebillConstant.ABLOG_LOGTYPE_IMPORT
							: AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
					abLogService.saveAbLog(bailAccrecordslist.get(i).getVvid(), bailAccrecordslist.get(i).getId(),
							bailAccrecordslist.get(i).getBriefId(), logType, msg, userVo);
				}
				int upBoxStatusNum = abVoucherinfoViceService.batchUpdateBoxStatus(vvids,
						AccebillConstant.AB_BOX_STATUS_N, userVo);
				if (addBailAccrecordsSize != upBoxStatusNum) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < addBailAccrecordsSize; i++) {
					AbAccrecords accrecords = bailAccrecordslist.get(i);
					abBillNotificationService.completeMsg(accrecords,userVo);
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecords.getVvid());
					AbAccrecordsVO variables = new AbAccrecordsVO();
					BeanUtils.copyNotNullProperties(accrecords.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(accrecords, variables);
					 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_GUARANTEEAPPLY, accrecords.getId(),
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
	 * 批量转换保证申请数据
	 * 
	 * @param bailAccrecordslist
	 * @param accrecordslist
	 * @param reqInfo
	 * @return
	 */
	public int accrecordsToBailAccrecords(List<AbAccrecords> bailAccrecordslist, List<AbAccrecords> accrecordslist,
			AbBillGuaranteeApplyVO reqInfo, Map<String, String> tailTMap, ICurrentUser userVo, String[] oldAccIdArr) {
		for (int i = 0; i < accrecordslist.size(); i++) {
			AbAccrecords oldInfo = accrecordslist.get(i);
			AbVoucherinfo vouInfo = abVoucherinfoService.get(oldInfo.getVid(), true);
			AbVoucherinfoVice vice = abVoucherinfoViceService.get(oldInfo.getVvid(), true);
			AbAccrecords bailInfo = new AbAccrecords();
			//oldLastAccIdArr[i].equals(accIdArrays[i]) 为true，说明为不拆分的票据（可能为旧票或者不拆分新票）
			bailInfo.setSplitFlg(oldAccIdArr[i].equals(oldInfo) ? "0" : "1");
			//拆分交易的话设置此字段为未拆分之前的id
			bailInfo.setOldAccId("1".equals(bailInfo.getSplitFlg()) ? oldAccIdArr[i] : null);
			bailInfo.setVid(oldInfo.getVid());
			bailInfo.setVvid(oldInfo.getVvid());
			bailInfo.setVouInfo(vouInfo);
			bailInfo.setVouViceInfo(vice);
			bailInfo.setApplyDate(new Date());
			bailInfo.setLastid(oldInfo.getId());
			bailInfo.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			bailInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			bailInfo.setBriefId(AccebillConstant.BRIEF_STATUS_017);
			bailInfo.setLastbillcode(vouInfo.getBillStatus());
			bailInfo.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			bailInfo.setBillFlag(vouInfo.getBillFlag());
			bailInfo.setBailType(tailTMap.get(oldInfo.getId()));
			bailInfo.setAdr(reqInfo.getAdr());
			bailInfo.setRmk(reqInfo.getRmk());
			bailInfo.setReqAcctId(vice.getBankAcc());
			bailInfo.setRecNm(reqInfo.getRecNm());
			bailInfo.setRecAcctId(reqInfo.getRecAcctId());
			bailInfo.setRecAcctsvcr(reqInfo.getRecAcctsvcr());
			bailInfo.setRecDghType(oldInfo.getRecDghType());
			bailAccrecordslist.add(bailInfo);
		}
		return abAccrecordsService.batchSaveAccrecords(bailAccrecordslist, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_GUARANTEEAPPLY, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillGuaranteeApplyVO> dp = abBillGuaranteeApply.dataTableApprove(dataTablesInput, businessList);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_BAIL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillGuaranteeApplyVO bill : listData){
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
	public AbBillGuaranteeApplyVO getAbBillGuaranteeApplyVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
		BeanUtils.copyNotNullProperties(accrecordsInfo, accrecordsVo);
		// this.inject(accrecordsVo);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbBillGuaranteeApplyVO reInfo = new AbBillGuaranteeApplyVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(viceInfo, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsVo, reInfo);
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY","AB_BILL_ROLE"
				,"AB_BILL_BAIL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_BAIL_TYPE","AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
		reInfo.injectValues(listDictMap);
		return reInfo;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillGuaranteeApplyVO obj,Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbBillGuaranteeApplyVO> list,Object... user) {
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
			List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_GUARANTEEAPPLY_QUERY,userId);
			
			for (AbBillGuaranteeApplyVO info : list) {
				List<String> errorList = new ArrayList<>();
				if (origNoMap.containsKey(info.getOrigNo()+"&"+info.getSbbllRngStrtSn()+"&"+info.getSbbllRngEndSn())) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
					info.addErrorMsg(errorList);
					continue;
				} else {
					origNoMap.put(info.getOrigNo()+"&"+info.getSbbllRngStrtSn()+"&"+info.getSbbllRngEndSn(), info.getOrigNo()+"&"+info.getSbbllRngStrtSn()+"&"+info.getSbbllRngEndSn());
				}
				if (StringUtils.isEmpty(info.getCorpCode()) || StringUtils.isEmpty(info.getOrigNo())
						|| StringUtils.isEmpty(info.getRecNm()) || StringUtils.isEmpty(info.getRecAcctId())) {
					continue;
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
				params.put("origNo", info.getOrigNo()+"&"+info.getSbbllRngStrtSn()+"&"+info.getSbbllRngEndSn());
				params.put("corpId", info.getCorpId());
				AbBillGuaranteeApplyVO billInfo = abBillGuaranteeApply.getAbBillGuaranteeApplyVOByParams(params,
						corpAuthList);
				if (billInfo == null) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOOP);
				} else if (billInfo.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)) {
					errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_APPROVEOP);
				} else {
					info.setIds(billInfo.getId());
					info.setBillMoney(billInfo.getBillMoney());
					info.setBailTypes(billInfo.getBailType());
				}
				if (StringUtils.isEmpty(info.getRecAcctsvcr()) && StringUtils.isEmpty(info.getRecAcctsvcrName())) {
					errorList.add(AccebillConstant.LANG_ERROR_GUARANTEEBANK_CODEANDNAME_NONULL);
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
					errorList.add(AccebillConstant.LANG_ERROR_GUARANTEEBANK_CODEORNAME);
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
		return this.getAbBillGuaranteeApplyVOById(aid);
	}
}
