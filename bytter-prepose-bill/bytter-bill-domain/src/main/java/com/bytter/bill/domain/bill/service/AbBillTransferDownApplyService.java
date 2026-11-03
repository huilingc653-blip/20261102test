package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillTransferDownApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownApplyVo;
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
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.*;
/**
 * @Title: Service
 * @Description: 下拨申请单
 * @author onlineGenerator
 * @date 2017-07-17 14:43:45
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY)
@Service("abBillTransferDownApplyService")
@Transactional
public class AbBillTransferDownApplyService extends BaseService<AbBillTransfer, String>
		implements IAccebillBaseService, IWorkFlowListener {
	@Autowired
	private AbBillTransferDownApplyDao abBillTransferDownApplyDao;
	@Autowired
	private AbBillTransferService abBillTransferService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbAccountGrantService accountGrantService;

	@Autowired
	private BtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 票据下拨申请列表查询
	 * 
	 * @param input
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTables(DataTablesInput input) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_TRANSFERDOWNAPPLY_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_TRANSFERDOWNAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillTransferDownApplyVo> dataTablesUnion = abBillTransferDownApplyDao.getDataTablesUnion(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData -> {
			Map<String, DictDTO> listMap = dictService.listMap("AB_BILL_TRANSFERDOWNAPPLY_STATUS");
			for (AbBillTransferDownApplyVo bill : listData) {
				injectValues(bill, listMap);
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
			return dataTablesUnion;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesUnion;
		}
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			for (AbBillTransferDownApplyVo bill : listData){
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
		return dataTablesUnion;
	}
	private void injectValues(AbBillTransferDownApplyVo bill, Map<String, DictDTO> listMap) {
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).flatMap(status ->
				Optional.ofNullable(listMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))).
				ifPresent(dict -> bill.setStatusName(dict.getName()));
	}
	/**
	 * 保存下拨申请
	 * 
	 * @param opt
	 *            新增或修改标志 add or update
	 * @param abBillTransferDownApplyVo
	 */
	public void save(AbBillTransferDownApplyVo abBillTransferDownApplyVo, String opt, ICurrentUser userVo) {
		saveOrSubmit(abBillTransferDownApplyVo, opt, AccebillConstant.BILL_OPERATE_SAVE, userVo);
	}
	/**
	 * 提交下拨申请
	 * 
	 * @param opt
	 *            新增或修改标志 add or update
	 * @param abBillTransferDownApplyVo
	 */
	public void submit(AbBillTransferDownApplyVo abBillTransferDownApplyVo, String opt, ICurrentUser userVo) {
		saveOrSubmit(abBillTransferDownApplyVo, opt, AccebillConstant.BILL_OPERATE_SUBMIT, userVo);
	}
	/**
	 * 新增修改，保存提交 下拨申请业务处理
	 * 
	 * @param abBillTransferDownApplyVo
	 * @param opt
	 *            新增或修改标志 add or update
	 * @param operater
	 *            保存或提交 save or submit
	 */
	protected void saveOrSubmit(AbBillTransferDownApplyVo abBillTransferDownApplyVo, String opt, String operater,
			ICurrentUser userVo) {
		AbBillTransfer abBillTransfer = getAbBillTransfer(abBillTransferDownApplyVo, opt, userVo);
		abBillTransferService.saveOrUpdate(abBillTransfer);
		AbAccrecords abAccrecords = null;
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			abAccrecords = new AbAccrecords();
			abAccrecords.setTrid(abBillTransfer.getId());
			abAccrecords.setBriefId(AccebillConstant.BRIEF_STATUS_6203);
			abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		} else {
			// 修改下拨申请流水-暂存状态
			abAccrecords = abAccrecordsService.get(abBillTransferDownApplyVo.getAid());
		}
		abAccrecords.setApplyDate(new Date());// 申请日期
		abAccrecords.setCreateName(userVo.getUsername());// 创建人名称
		abAccrecords.setCreateBy(userVo.getLoginName());// 创建人登录名称
		abAccrecords.setCreateDate(new Date());// 创建日期
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(operater)) {
			// 保存设置状态
			abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
		} else {
			// 提交设置状态
			abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecords.setBusinessDate(new Date());// 业务归属日期
		}
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			// 新增数据
			abAccrecordsService.save(abAccrecords);
		} else {
			// 修改数据
			abAccrecordsService.update(abAccrecords, userVo);
			abBillNotificationService.completeMsg(abAccrecords,userVo);
		}
		/** 附件、日志、工作流处理 */
		if (abBillTransferDownApplyVo != null && abAccrecords != null) {
			abBillAttachmentService.opAttachmentInfo(abBillTransferDownApplyVo, abAccrecords, userVo);
		}
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(operater)) {
			if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
				// 新增保存日志
				abLogService.saveAbLog(abAccrecords.getTrid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6203,
						AccebillConstant.ABLOG_LOGTYPE_ADD, AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
			} else {
				// 修改保存日志
				abLogService.saveAbLog(abAccrecords.getTrid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6203,
						AccebillConstant.ABLOG_LOGTYPE_UPDATE, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
			}
		} else {
			abLogService.saveAbLog(abAccrecords.getTrid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6203,
					AccebillConstant.ABLOG_LOGTYPE_SUBMIT, AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
			AbBillTransferDownApplyVo variables = new AbBillTransferDownApplyVo();// 审批数据
			BeanUtils.copyNotNullProperties(abBillTransfer, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
			variables.setAid(abAccrecords.getId());
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY, abAccrecords.getId(),
					variables, abBillTransfer.getRecordCorpId(),userVo.getId());
		}
	}
	/**
	 * 批量提交下拨申请
	 * 
	 * @param abBillTransferDownApplyVo
	 */
	public void batchSubmit(AbBillTransferDownApplyVo abBillTransferDownApplyVo, ICurrentUser userVo) {
		String[] trids = abBillTransferDownApplyVo.getTrids().split(",");
		String[] aids = abBillTransferDownApplyVo.getAids().split(",");
		AbAccrecords abAccrecords = null;
		AbBillTransfer abBillTransfer = null;
		for (int i = 0; i < aids.length; i++) {
			abAccrecords = abAccrecordsService.get(aids[i]);
			abBillTransfer = abBillTransferService.get(trids[i]);
			abAccrecords.setBusinessDate(abBillTransferDownApplyVo.getBusinessDate());// 业务归属日期
			// 新增下拨申请流水-暂存状态
			abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecordsService.update(abAccrecords, userVo);
			AbBillTransferDownApplyVo variables = new AbBillTransferDownApplyVo();// 审批数据
			BeanUtils.copyNotNullProperties(abBillTransfer, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
			variables.setAid(abAccrecords.getId());
			abBillNotificationService.completeMsg(abAccrecords,userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY, abAccrecords.getId(),
					variables, abBillTransfer.getRecordCorpId(),userVo.getId());
		}
		abLogService.batchSaveAbLog(trids, aids, AccebillConstant.BRIEF_STATUS_6203,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.ABLOG_LOGTYPE_BATCHSUCCE, userVo);
	}
	/**
	 * 删除下拨申请--对暂存状态的 下拨申请单 进行物理删除操作
	 * 
	 * @param ids
	 */
	public void deleteAbBillTransfer(String ids, ICurrentUser userVo) {
		String trid = ids.split(",")[0];
		String aid = ids.split(",")[1];
		batchDelete(trid, aid, userVo);
	}
	/**
	 * 批量删除 删除下拨申请--对暂存状态的 下拨申请单 进行物理删除操作
	 * 
	 * @param trids
	 */
	public boolean batchDelete(String trids, String aids, ICurrentUser userVo) {
		String[] tridsArray = trids.split(",");
		String[] aidsArray = aids.split(",");
		Map<String, Object> params = new HashMap<>();
		params.put("tridsArray", tridsArray);
		params.put("aidsArray", aidsArray);
		int num = abBillTransferDownApplyDao.batchDelete(params);
		if (num != tridsArray.length) {
			return false;
		}
		abLogService.batchSaveAbLog(tridsArray, aidsArray, AccebillConstant.BRIEF_STATUS_6203,
				AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE, AccebillConstant.LANG_LOG_BATCHDELETE_SUCC, userVo);
		return true;
	}
	/**
	 * 根据请求参数生成AbBillTransfer申请下拨单对象
	 * 
	 * @param abBillTransferDownApplyVo
	 * @param opt
	 * @return
	 */
	protected AbBillTransfer getAbBillTransfer(AbBillTransferDownApplyVo abBillTransferDownApplyVo, String opt,
			ICurrentUser userVo) {
		AbBillTransfer entity = new AbBillTransfer();
		if (!AccebillConstant.BILL_OPERATE_ADD.equals(opt)
				&& StringUtils.isNotEmpty(abBillTransferDownApplyVo.getTrid())) {
			// 修改
			entity = abBillTransferService.get(abBillTransferDownApplyVo.getTrid());
		}
		entity.setAllocatedCode(abBillTransferDownApplyVo.getAllocatedCode());// 申请单号
		entity.setAllocationMoney(abBillTransferDownApplyVo.getAllocationMoney());// 申请金额
		entity.setAllocationReason(abBillTransferDownApplyVo.getAllocationReason());// 申请原因
		entity.setRecordDate(abBillTransferDownApplyVo.getRecordDate());// 录入日期
		entity.setRecordCorpId(abBillTransferDownApplyVo.getRecordCorpId());// 申请单位id
		entity.setRecordCorpName(abBillTransferDownApplyVo.getRecordCorpName());// 申请单位名称
		entity.setRecevieBankacc(abBillTransferDownApplyVo.getRecevieBankacc());// 收票单位账号
		entity.setBillCurrency(abBillTransferDownApplyVo.getBillCurrency());// 票据币别
		entity.setEnclosure(abBillTransferDownApplyVo.getAttachmentId());// 附件
		CorpDTO netInfo = abCorpService.get(entity.getRecordCorpId());
		if (netInfo != null) {
			entity.setRecordCorpCode(netInfo.getCode());
			String netId = netInfo.getNetId();
			String netCorpName = abCorpService.get(netId).getName();
			String netCorpCode = abCorpService.get(netId).getCode();
			entity.setRecevieCorpId(netId);
			entity.setRecevieCorpName(netCorpName);
			entity.setRecevieCorpCode(netCorpCode);
		}
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			entity.setCreateBy(userVo.getLoginName());
			entity.setCreateName(userVo.getUsername());
			entity.setCreateDate(new Date());
		} else {// 修改
			entity.setUpdateBy(userVo.getLoginName());
			entity.setUpdateName(userVo.getUsername());
			entity.setUpdateDate(new Date());
		}
		if (StringUtils.isNotBlank(entity.getBillCurrency())){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.get(entity.getBillCurrency());
			entity.setBillCurName(btCurrencyDTO.getCurName());
		}
		return entity;
	}
	/**
	 * 根据条件查询下拨申请单，用于修改
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownApplyVo getDataById(String aid, Model model) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		AbBillTransferDownApplyVo vo = null;
		if (abAccrecords != null) {
			vo = new AbBillTransferDownApplyVo();
			AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
			BeanUtils.copyNotNullProperties(abBillTransfer, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			model.addAttribute("netCorpName", abBillTransfer.getRecevieCorpName());
			vo.setAid(aid);
			// 日志查询
			List<AbLog> abLogList = abLogService.listAbLog(abAccrecords.getTrid(), null);
			model.addAttribute("abLogList", abLogList);
		}
		return vo;
	}
	/**
	 * 根据条件查询下拨申请单，用于修改（前后端分离，无需Model，netCorpName与abLogList直接返回在VO中）
	 *
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownApplyVo getDataById(String aid) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		AbBillTransferDownApplyVo vo = null;
		if (abAccrecords != null) {
			vo = new AbBillTransferDownApplyVo();
			AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
			BeanUtils.copyNotNullProperties(abBillTransfer, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setNetCorpName(abBillTransfer.getRecevieCorpName());
			vo.setAid(aid);
			// 日志查询
			vo.setAbLogList(abLogService.listAbLog(abAccrecords.getTrid(), null));
		}
		return vo;
	}
	/**
	 * 批量审批
	 * 
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void batchApprove(String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		if (StringUtils.isEmpty(aid)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		int num = 0;
		if (isPassOK) {
			/** 审批通过 */
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
			abBillNotificationService.sendMsg(aid, AccebillConstant.BRIEF_STATUS_6204);
		} else {
			/** 打回 */
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS_1, userVo);
		}
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		abLogService.saveAbLog(abAccrecordsService.get(aid).getTrid(), aid, AccebillConstant.BRIEF_STATUS_6203, logType,
				appMsg, userVo);
	}
	public BaseDao<AbBillTransfer, String> getEntityDao() {
		return abBillTransferDownApplyDao;
	}
	/**
	 * 工作流启动回调方法
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
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abBillTransferDownApplyDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		// todo:未实现
		return null;
	}
}
