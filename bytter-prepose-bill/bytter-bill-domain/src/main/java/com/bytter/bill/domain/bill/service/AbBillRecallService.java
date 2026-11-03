package com.bytter.bill.domain.bill.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillRecallDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillRecallVO;
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
import com.bytter.core.utils.SqlUtil;
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
import java.util.*;
/**
 * @Title: Service
 * @Description: 通用撤回处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_RECALL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_RECALL)
@Service("abBillRecallService")
@Transactional
public class AbBillRecallService extends BaseService<AbBillRecallVO, String>
		implements IAccebillBaseService, IWorkFlowListener {
	@Autowired
	private AbBillRecallDao abBillRecall;
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
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	public BaseDao<AbBillRecallVO, String> getEntityDao() {
		return abBillRecall;
	}
	/**
	 * 加载AbBillRecall表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillRecallInfo(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECALL_QUERY,dataTablesInput.getCurrentUserId()), "c.apply_corp_id", true);
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_RECALL_QUERY,corpIds);
		DataTablesOutput<AbBillRecallVO> dataTablesOutput = abBillRecall.listAbBillRecall(dataTablesInput, corpAuthSql);
		
		if(dataTablesOutput == null || CollectionUtils.isEmpty(dataTablesOutput.getData())){
			return dataTablesOutput;
		}
		for(AbBillRecallVO vo : dataTablesOutput.getData()){
			if(StringUtils.isNotEmpty(vo.getCnAccrecords())){
				vo.setCnAccrecords(vo.getCnAccrecords());
			}
		}
		
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECALL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_RECALL_TYPE", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbBillRecallVO bill:listData){
				bill.injectValues(listDictMap);
			}
		});
		return dataTablesOutput;
	}
	/**
	 * 撤票批量申请操作
	 * 
	 * @param reqInfo
	 * @param userVo
	 */
	public void batchApply(AbBillRecallVO reqInfo, ICurrentUser userVo) {
		String id = reqInfo.getIds();
		if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			String[] ids = StringUtils.split(id, ",");
			String[] recallTypes = StringUtils.split(reqInfo.getRecallTypes(), ",");
			// 设置id对应的保证类型
			Map<String, String> recallTMap = new HashMap<String, String>();
			for (int i = 0; i < ids.length && i < recallTypes.length; i++) {
				recallTMap.put(ids[i], recallTypes[i]);
			}
			AbAccrecordsVO accrecordsVo = new AbAccrecordsVO();
			accrecordsVo.setIdArray(ids);
			accrecordsVo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(accrecordsVo);
			if (ids.length != accrecordsList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			if (accrecordsList != null && !accrecordsList.isEmpty()) {
				List<AbAccrecords> acceptAccrecordslist = new ArrayList<AbAccrecords>();
				int addRecallAccrecordsSize = accrecordsToRecallAccrecords(acceptAccrecordslist, accrecordsList,
						reqInfo, recallTMap, userVo);
				if (accrecordsList.size() != addRecallAccrecordsSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				String[] vvids = new String[addRecallAccrecordsSize];
				for (int i = 0; i < addRecallAccrecordsSize; i++) {
					vvids[i] = acceptAccrecordslist.get(i).getVvid();
					String msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					abLogService.saveAbLog(acceptAccrecordslist.get(i).getVvid(), acceptAccrecordslist.get(i).getId(),
							acceptAccrecordslist.get(i).getBriefId(), AccebillConstant.ABLOG_LOGTYPE_SUBMIT, msg,
							userVo);
				}
				int upBoxStatusNum = abVoucherinfoViceService.batchUpdateBoxStatus(vvids,
						AccebillConstant.AB_BOX_STATUS_N, userVo);
				if (addRecallAccrecordsSize != upBoxStatusNum) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < addRecallAccrecordsSize; i++) {
					AbAccrecords accrecords = acceptAccrecordslist.get(i);
					abBillNotificationService.completeMsg(accrecords,userVo);
					// 再次提交需要把打回从表的BackBriefId为空
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecords.getVvid());
					AbAccrecordsVO variables = new AbBillRecallVO();
					BeanUtils.copyNotNullProperties(accrecords.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(accrecords, variables);
					 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_RECALL, accrecords.getId(), variables,
							accrecords.getVouViceInfo().getApplyCorpId(),userVo.getId());
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
	 * 批量转换通用撤回申请数据
	 * 
	 * @param bailAccrecordslist
	 * @param accrecordslist
	 * @param reqInfo
	 * @return
	 */
	public int accrecordsToRecallAccrecords(List<AbAccrecords> bailAccrecordslist, List<AbAccrecords> accrecordslist,
			AbBillRecallVO reqInfo, Map<String, String> recallTMap, ICurrentUser userVo) {
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
			bailInfo.setLastid(oldInfo.getId());
			bailInfo.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			bailInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			bailInfo.setBriefId(AccebillConstant.BRIEF_STATUS_032);
			bailInfo.setLastbillcode(vouInfo.getBillStatus());
			bailInfo.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			bailInfo.setBillFlag(vouInfo.getBillFlag());
			bailInfo.setReqNm(oldInfo.getReqNm());
			bailInfo.setReqAcctId(oldInfo.getReqAcctId());
			bailInfo.setReqAcctsvcr(oldInfo.getReqAcctsvcr());
			bailInfo.setRecNm(oldInfo.getRecNm());
			bailInfo.setRecAcctId(oldInfo.getRecAcctId());
			bailInfo.setRecAcctsvcr(oldInfo.getRecAcctsvcr());
			bailInfo.setRecallType(recallTMap.get(oldInfo.getId()));
			bailAccrecordslist.add(bailInfo);
		}
		return abAccrecordsService.batchSaveAccrecords(bailAccrecordslist, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_RECALL, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "l.id", true);
		DataTablesOutput<AbBillRecallVO> dp = abBillRecall.dataTableApprove(dataTablesInput, businessSql);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECALL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_RECALL_TYPE");
			for (AbBillRecallVO bill:listData){
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
	public AbBillRecallVO getAbBillRecallVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbBillRecallVO reInfo = new AbBillRecallVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid());// 票据从表信息
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		reInfo.setVvid(abVoucherinfoVice.getId());
		reInfo.setBankTypeName(abVoucherinfoViceService.findBankTypeNameWith(abVoucherinfoVice.getId()).toString());
		// 根据单位id获取单位信息
		// 根据单位id获取单位信息
		if(StringUtils.isEmpty(reInfo.getCorpName())){
			CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
			if (corpInfo != null) {
				reInfo.setCorpName(corpInfo.getName());
			}
		}
		if(StringUtils.isEmpty(reInfo.getApplyCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getApplyCorpId())){
			CorpDTO applyCorpInfo = abCorpService.get(abVoucherinfoVice.getApplyCorpId());
			if (applyCorpInfo != null) {
				reInfo.setApplyCorpName(applyCorpInfo.getName());
			}
		}
		if(StringUtils.isEmpty(reInfo.getBusCorpName()) && StringUtils.isNotEmpty(abVoucherinfoVice.getBusCorpId())){
			CorpDTO busCorpInfo = abCorpService.get(abVoucherinfoVice.getBusCorpId());
			if (busCorpInfo != null) {
				reInfo.setBusCorpName(busCorpInfo.getName());
			}
		}
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
				,"AB_BILL_RECALL_STATUS","AB_BILL_INSBRIEFID","AB_BILL_INSOPTSTATUS","AB_SGNUPMK","AB_RECALL_TYPE");
		reInfo.injectValues(listDictMap);
		return reInfo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return getAbBillRecallVOById(aid);
	}
}
