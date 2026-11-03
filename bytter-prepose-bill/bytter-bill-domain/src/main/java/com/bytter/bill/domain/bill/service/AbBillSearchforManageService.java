package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillSearchforManageDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillReceivebiz;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.domain.bill.vo.AbBillSearchforManageVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
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
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 追索清偿处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORMANAGE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORMANAGE)
@Service("abBillSearchforManageService")
@Transactional
public class AbBillSearchforManageService extends BaseService<AbBillSearchforManageVO, String>
		implements IAccebillBaseService, IWorkFlowListener {
	@Autowired
	private AbBillSearchforManageDao abBillSearchforManage;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
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
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private BillAccrecordsDao billAccrecordsDao;
	public BaseDao<AbBillSearchforManageVO, String> getEntityDao() {
		return abBillSearchforManage;
	}
	/**
	 * 加载AbBillSearchforManage表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillSearchforManageInfo(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_SEARCHFORMANAGE_QUERY,dataTablesInput.getCurrentUserId()), "p.REC_CORP_ID",
				true);
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_SEARCHFORMANAGE_QUERY,corpIds);
		DataTablesOutput<AbBillSearchforManageVO> dataTablesOutput = abBillSearchforManage.listAbBillSearchforManage(dataTablesInput, corpAuthSql);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECEIVEBIZ_STATUS","AB_BILL_INSBRIEFID","AB_SGNUPMK","AB_RCRS_TP","AB_RCRSRSNCD");
			for (AbBillSearchforManageVO bill : listData){
				bill.injectValues(listDictMap);
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
			for (AbBillSearchforManageVO bill : listData){
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
	/**
	 * 追索清偿批量申请操作
	 * 
	 * @param guaranteeApplyVo
	 */
	public void batchApply(AbBillSearchforManageVO reqInfo, ICurrentUser userVo) {
		String id = reqInfo.getIds();
		if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			reqInfo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
			String[] ids = StringUtils.split(id, ",");
			String[] rcrsokmoneys = StringUtils.split(reqInfo.getRcrsokmoneys(), ";");
			// 设置id对应的追索清偿类型
			Map<String, String> rcrsokmoneyMap = new HashMap<String, String>();
			for (int i = 0; i < ids.length && i < rcrsokmoneys.length; i++) {
				rcrsokmoneyMap.put(ids[i], rcrsokmoneys[i]);
			}
			AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
			billReceivebizVo.setStatus(reqInfo.getStatus());
			List<AbBillReceivebiz> billReceivebizList = abBillReceivebizService.listAbBillReceivebizByIds(ids,
					billReceivebizVo);
			if (ids.length != billReceivebizList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
            //新票据拆分处理
            for (int i = 0; i < billReceivebizList.size(); i++) {
                AbBillReceivebiz abBillReceivebiz = billReceivebizList.get(i);
                if (!("1".equals(abBillReceivebiz.getNewStmIssuofpprInd()) && "1".equals(abBillReceivebiz.getBlgrdbagcrclPrmtInd()))
                        || !(abBillReceivebiz.getBillMoney().compareTo(new BigDecimal(rcrsokmoneyMap.get(abBillReceivebiz.getId()))) > 0
                        && new BigDecimal(rcrsokmoneyMap.get(abBillReceivebiz.getId())).compareTo(BigDecimal.ZERO) > 0)) {
                    continue;
                }
                if (StringUtils.isBlank(abBillReceivebiz.getSbbllRngStrtSn()) || "0".equals(abBillReceivebiz.getSbbllRngStrtSn())
                        || StringUtils.isBlank(abBillReceivebiz.getSbbllRngEndSn()) || "0".equals(abBillReceivebiz.getSbbllRngEndSn())) {
                    continue;
                }
                BigDecimal startB = new BigDecimal(abBillReceivebiz.getSbbllRngStrtSn());
				BigDecimal endB = new BigDecimal(rcrsokmoneyMap.get(abBillReceivebiz.getId())).multiply(BigDecimal.valueOf(100));
				endB = startB.add(endB).subtract(BigDecimal.ONE);
                String endStr = Long.valueOf(endB.toString()).toString();
                String zeroStr = "000000000000";
                endStr = zeroStr.substring(0, zeroStr.length() - endStr.length()) + endStr;
                abBillReceivebiz.setUpdateDate(new Date());
                abBillReceivebiz.setUpdateBy(userVo.getId());
                abBillReceivebiz.setUpdateName(userVo.getUsername());
                AbBillReceivebiz newBillReceivebiz = new AbBillReceivebiz();
                BeanUtils.copyNotNullProperties(abBillReceivebiz, newBillReceivebiz, "id");
                newBillReceivebiz.setSbbllRngEndSn(endStr);
                newBillReceivebiz.setBillMoney(new BigDecimal(rcrsokmoneyMap.get(abBillReceivebiz.getId())));
                List<AbBillReceivebiz> addList = new ArrayList<>();
                addList.add(newBillReceivebiz);
                //插入一条新的签收数据替换原来的旧的签收数据
                abBillReceivebizService.batchSaveOrUpdate(addList);
                //更新旧的签收数据状态为已拆分-100
                abBillReceivebiz.setStatus(AccebillConstant.RECIVE_STATUE_100F);
                List<AbBillReceivebiz> updList = new ArrayList<>();
                updList.add(abBillReceivebiz);
                abBillReceivebizService.batchSaveOrUpdate(updList);
                //替换
                billReceivebizList.remove(i);
                billReceivebizList.add(i, newBillReceivebiz);
				rcrsokmoneyMap.put(newBillReceivebiz.getId(),rcrsokmoneyMap.get(abBillReceivebiz.getId()));
                ids[i] = newBillReceivebiz.getId();
                //abBillReceivebiz = newBillReceivebiz;
            }
            //int num = 1/0;
			if (billReceivebizList != null && !billReceivebizList.isEmpty()) {
				List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
				abBillReceivebizService.batchSaveVoucherInfo(billReceivebizList, billReceivebizVo, userVo);
				int addAccrecordsSize = billReceivebizToAccrecords(accrecordslist, billReceivebizList, reqInfo,
						rcrsokmoneyMap, userVo);
				billReceivebizVo.setReplyDt(new Date());
				int updateBillReceivebizSize = abBillReceivebizService.batchUpdateAbBillReceivebiz(ids,
						billReceivebizVo, userVo);
				if (billReceivebizList.size() != addAccrecordsSize || addAccrecordsSize != updateBillReceivebizSize) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				for (int i = 0; i < updateBillReceivebizSize; i++) {
					AbAccrecords accrecInfo = accrecordslist.get(i);
					String msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
					abLogService.saveAbLog(accrecordslist.get(i).getVvid(), accrecordslist.get(i).getId(),
							accrecordslist.get(i).getBriefId(), AccebillConstant.ABLOG_LOGTYPE_SUBMIT, msg, userVo);
					abBillNotificationService.completeMsg(accrecInfo,userVo);
					abVoucherinfoViceService.updateBackBriefIdIsNull(accrecInfo.getVvid());
					AbBillReceivebiz recInfo = billReceivebizList.get(i);
					AbBillSearchforManageVO variables = new AbBillSearchforManageVO();
					BeanUtils.copyNotNullProperties(recInfo.getVouInfo(), variables);
					BeanUtils.copyNotNullProperties(recInfo, variables);
					BeanUtils.copyNotNullProperties(accrecInfo, variables);
					 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORMANAGE, accrecInfo.getId(),
							variables, recInfo.getRecCorpId(),userVo.getId());
				}
			}
		}
	}
	/**
	 * 审批完成处理
	 * 
	 * @param accrecordslist
	 * @param isPassOK
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
		if (!isPassOK) {
            //新票处理
            String recId = NewDraftDealMethod.agreeSettleRepulse(billSignDao,info.getRecid(),18,buId,billAccrecordsDao);
            info.setRecid(recId);
			AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
			billReceivebizVo.setId(info.getRecid());
			billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE);
			int reNum = abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo, userVo);
			if (reNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		}
		String msg = appMsg;
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		// 签收业务不需要做票据主表状态处理
		// int upBoxStatusNum =
		// abVoucherinfoService.updateBoxStatus(info.getVid(),
		// AccebillConstant.AB_BOX_STATUS_Y);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	/**
	 * 签收表转流水表业务处理处理
	 * 
	 * @param billReceivebizList
	 * @return
	 */
	public int billReceivebizToAccrecords(List<AbAccrecords> accrecordslist, List<AbBillReceivebiz> billReceivebizList,
			AbBillSearchforManageVO reqInfo, Map<String, String> rcrsokmoneyMap, ICurrentUser userVo) {
		for (AbBillReceivebiz info : billReceivebizList) {
			AbAccrecords accrecords = new AbAccrecords();
			accrecords.setRecid(info.getId());
			accrecords.setVid(info.getVid());
			accrecords.setVvid(info.getVvid());
			accrecords.setValidSign("0");
			accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			accrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			accrecords.setBriefId(AccebillConstant.BRIEF_STATUS_023);
			// accrecords.setApplyDate(info.getReplyDt());
			accrecords.setApplyDate(new Date());
			accrecords.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
			accrecords.setRmk(info.getRmk());
			if (rcrsokmoneyMap.containsKey(info.getId())) {
				accrecords.setRcrsokmoney(new BigDecimal(rcrsokmoneyMap.get(info.getId())));
			}
			accrecords.setRcrsTp(info.getRcrsTp());
			accrecords.setRcrsrsncd(info.getRcrsrsncd());
			accrecords.setReqRole(info.getRecRole());
			accrecords.setReqNm(info.getRecNm());
			accrecords.setReqCmonid(info.getRecCmonid());
			accrecords.setReqAcctId(info.getRecAcctId());
			accrecords.setReqAcctsvcr(info.getRecAcctsvcr());
			accrecords.setReqAcctsvcrAg(info.getRecAcctsvcrAg());
			accrecords.setRecRole(info.getReqRole());
			accrecords.setRecNm(info.getReqNm());
			accrecords.setRecCmonid(info.getReqCmonid());
			accrecords.setRecAcctId(info.getReqAcctId());
			accrecords.setRecAcctsvcr(info.getReqAcctsvcr());
			accrecords.setRecAcctsvcrAg(info.getReqAcctsvcrAg());
			accrecords.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
			accrecordslist.add(accrecords);
		}
		return abAccrecordsService.batchSaveAccrecords(accrecordslist, userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_SEARCHFORMANAGE, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "l.id", true);
		DataTablesOutput<AbBillSearchforManageVO> dp = abBillSearchforManage.dataTableApprove(dataTablesInput, businessSql);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
					,"AB_BILL_RECEIVEBIZ_STATUS","AB_BILL_INSBRIEFID","AB_SGNUPMK","AB_RCRS_TP","AB_RCRSRSNCD");
			for (AbBillSearchforManageVO bill : listData){
				bill.injectValues(listDictMap);
			}
		});
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 获取业务信息
	 * 
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillSearchforManageVO getAbBillSearchforManageVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbBillReceivebiz recInfo = abBillReceivebizService.get(accrecordsInfo.getRecid(), true);
		AbBillSearchforManageVO reInfo = new AbBillSearchforManageVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(recInfo, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid());// 票据从表信息
		reInfo.setBusPersonnel(abVoucherinfoVice.getBusPersonnel());
		reInfo.setVvid(abVoucherinfoVice.getId());
		reInfo.setBankTypeName(abVoucherinfoViceService.findBankTypeNameWith(abVoucherinfoVice.getId()).toString());
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
		reInfo.setAcid(accrecordsInfo.getId());
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_OPENWAY"
				,"AB_BILL_RECEIVEBIZ_STATUS","AB_BILL_INSBRIEFID","AB_SGNUPMK","AB_RCRS_TP","AB_RCRSRSNCD");
		reInfo.injectValues(listDictMap);
		return reInfo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillSearchforManageVOById(aid);
	}
}
