package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillTransferDownManageDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
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
 * @Title: 票据下拨Service
 * @Description: 票据下拨
 * @author onlineGenerator
 * @date 2017-07-19 09:43:45
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE)
@Service("abBillTransferDownManageService")
@Transactional
public class AbBillTransferDownManageService extends BaseService<AbBillTransfer, String>
		implements IAccebillBaseService, IWorkFlowListener {
	@Autowired
	private AbBillTransferDownManageDao abBillTransferDownManageDao;
	@Autowired
	private AbBillTransferService abBillTransferService;
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
	private AbBillEndorseService abBillEndorseService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillTransferDtlService abBillTransferDtlService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;

	@Autowired
	private BtCurrencyService btCurrencyService;
	/**
	 * 票据票据下拨列表查询
	 * 
	 * @param input
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTables(DataTablesInput input) {
		// 中心单位条件corpCenterId不为空，则根据中心单位下面的单位来查询
		String netId = String.valueOf(input.getFormdata().get("eqs_netId"));
		if (StringUtils.isEmpty(netId)) {// 重置没有值时从select组件中取值
			input.getFormdata().put("eqs_netId", netId);
			netId = String.valueOf(input.getFormdata().get("snetId"));
		}
		input.getFormdata().remove("snetId");// 没有用单位组件提交，屏蔽控制台的报错
		List<String> corpCenterIds = new ArrayList<> ();
		if (StringUtils.isNotEmpty(netId)) {
			// 获取网点下面的单位
			List<CorpDTO> list = abCorpService.listUnderCorpByNetId(netId);
			for (int i = 0; i < list.size(); i++) {
				corpCenterIds.add (list.get(i).getId());
			}
		}
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_TRANSFERDOWNMANAGE_QUERY,corpIds);
		DataTablesOutput<AbBillTransferDownManageVo> dataTablesUnion = abBillTransferDownManageDao.getDataTablesUnion(input, corpCenterIds);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERDOWNAPPLY_STATUS", "AB_BILL_BUSTYPE_NAME");
			for (AbBillTransferDownManageVo bill : listData) {
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
			return dataTablesUnion;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesUnion;
		}
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			for (AbBillTransferDownManageVo bill : listData){
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
	private void injectValues(AbBillTransferDownManageVo bill,Map<String, Map<String, DictDTO>> listDictMap){
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TRANSFERDOWNAPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 业务类型
		Optional.ofNullable(bill.getBusType()).ifPresent(busType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSTYPE_NAME");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType + "")))
					.ifPresent(dict -> bill.setBusTypeName(dict.getName()));
		});
	}
	/**
	 * 下拨单提交或保存时选票校验
	 */
	public void billVerification(String[] aidArray) {
		if (aidArray != null && aidArray.length > 0) {
			for (int i = 0; i < aidArray.length; i++) {
				AbAccrecords abAccrecords = abAccrecordsService.get(aidArray[i]);
				AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
				AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
				if (abVoucherinfoVice != null && abVoucherinfo != null) {
					if (AccebillConstant.AB_BILL_VALID_SIGN_2.equals(abVoucherinfoVice.getValidSign())) {
						// 票据号:{0}已不属于下拨中心,请编辑重新选票！
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALIDSINGERROR,
								abVoucherinfo.getOrigNo());
					}
					if (AccebillConstant.AB_BOX_STATUS_N.equals(abVoucherinfoVice.getBoxStatus())) {
						// 票据号:{0}已做业务或被锁定状态，请编辑重新选票！
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BOXSTATUSERROR,
								abVoucherinfo.getOrigNo());
					}
				}
			}
		}
	}
	/**
	 * 批量锁定票据 billAids 要锁定的票据流水id
	 */
	protected void batchUpdateBoxStatus(String[] billAids, ICurrentUser userVo) {
		String[] vvids = new String[billAids.length];
		for (int i = 0; i < billAids.length; i++) {
			vvids[i] = abAccrecordsService.get(billAids[i]).getVvid();
		}
		// 从表box_status改为10
		int num = abVoucherinfoViceService.batchUpdate(vvids, AccebillConstant.AB_BOX_STATUS_N,
				AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != billAids.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
	}
	/**
	 * 申请单编辑界面初始化
	 */
//	@Transactional(readOnly = true) 不能标识为只读，在mysql环境下会报错
	public AbBillTransferDownManageVo toInit(AbBillTransferDownManageVo vo) {
		String allocatedCode = SequenceSingleton.getSequence(SequenceEnum.BILL_TRANSFERDOWNMANAGE_NO, "XB");
		vo.setAllocatedCode(allocatedCode);
		// 根据申请单位id获取上级中心信息
		CorpDTO corpDTO = abCorpService.getNetByCorpId(vo.getRecevieCorpId());
		vo.setRecordCorpId(corpDTO.getId());
		vo.setRecordCorpName(corpDTO.getName());
		return vo;
	}
	/**
	 * 保存票据下拨
	 * 
	 * @param opt
	 * @param abBillTransferDownManageVo
	 */
	public void save(AbBillTransferDownManageVo abBillTransferDownManageVo, String opt, ICurrentUser userVo) {
		saveOrSubmit(abBillTransferDownManageVo, opt, AccebillConstant.BILL_OPERATE_SAVE, userVo);
	}
	/**
	 * 提交票据下拨
	 * 
	 * @param opt
	 * @param abBillTransferDownManageVo
	 *            审批通过后才会新增选票票据的下拨流水
	 */
	public void submit(AbBillTransferDownManageVo abBillTransferDownManageVo, String opt, ICurrentUser userVo) {
		saveOrSubmit(abBillTransferDownManageVo, opt, AccebillConstant.BILL_OPERATE_SUBMIT, userVo);
	}
	/**
	 * 新增修改 保存提交 票据下拨业务处理
	 * 
	 * @param abBillTransferDownManageVo
	 * @param opt
	 *            新增或修改标志 add or update
	 * @param operater
	 *            保存或提交标志 save or submit 审批通过后才会新增选票票据的下拨流水信息
	 */
	public void saveOrSubmit(AbBillTransferDownManageVo abBillTransferDownManageVo, String opt, String operater,
			ICurrentUser userVo) {
		AbBillTransfer abBillTransfer = getAbBillTransfer(abBillTransferDownManageVo, opt, userVo);// 票据下拨单
		abBillTransferService.saveOrUpdate(abBillTransfer);
		// 选票票据状态校验
		if (StringUtils.isNotEmpty(abBillTransferDownManageVo.getBillAid())) {
			billVerification(abBillTransferDownManageVo.getBillAid().split(","));
		}
		// 选票票据锁定
		if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operater)) {
			String[] billAids = abBillTransferDownManageVo.getBillAid().split(",");// 选票票据流水id
			batchUpdateBoxStatus(billAids, userVo);
		}
		/** 下拨操作流水处理 */
		String msg = null;
		AbAccrecords abAccrecordsOpt = getAbAccrecordsOpt(abBillTransferDownManageVo, userVo);// 下拨单操作流水
		abAccrecordsOpt.setTrid(abBillTransfer.getId());
		abAccrecordsOpt.setBusinessDate(abBillTransferDownManageVo.getBusinessDate());
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
			/** 新增 */
			if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operater)) {
				abAccrecordsOpt.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
				msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
			}
			abAccrecordsService.save(abAccrecordsOpt);
		} else {
			/** 基于下拨申请单第一次编辑的情况 需要新增下拨单操作流水 */
			int num = 0;
			if (AccebillConstant.BRIEF_STATUS_6203.equals(abBillTransferDownManageVo.getBriefId())) {
				if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operater)) {
					// 提交设置流水信息
					msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
					abAccrecordsOpt.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
					abAccrecordsOpt.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
				}
				// 下拨申请操作流水作废status=-2
				num = abAccrecordsService.updatAppPass(abBillTransferDownManageVo.getAid(), true, userVo);
				if (num != 1) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				// 新增下拨单操作流水-暂存状态
				abAccrecordsOpt.setLastid(abBillTransferDownManageVo.getAid());
				abAccrecordsService.save(abAccrecordsOpt);
			} else {
				/** 修改下拨单操作流水 */
				abAccrecordsOpt = abAccrecordsService.get(abBillTransferDownManageVo.getAid());
				if (AccebillConstant.BILL_OPERATE_SAVE.equals(operater)) {
					// 保存设置流水信息
					abAccrecordsOpt.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
				} else {
					// 提交设置流水信息
					msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
					abAccrecordsOpt.setTrid(abBillTransfer.getId());
					abAccrecordsOpt.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
					abAccrecordsOpt.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
				}
				abAccrecordsOpt.setApplyDate(new Date());// 下拨日期
				abAccrecordsOpt.setUpdateName(userVo.getUsername());// 修改人名称
				abAccrecordsOpt.setUpdateBy(userVo.getLoginName());// 修改人登录名称
				abAccrecordsOpt.setUpdateDate(new Date());// 修改日期
				abAccrecordsService.update(abAccrecordsOpt, userVo);
			}
		}
		// 保存下拨单与票据流水关联关系
		if (StringUtils.isNotEmpty(abBillTransferDownManageVo.getBillAid())) {
			saveAbBillTransferDtl(abBillTransfer.getId(), abBillTransferDownManageVo.getBillAid());
		}
		abBillNotificationService.completeMsg(abAccrecordsOpt,userVo);
		if (AccebillConstant.BILL_OPERATE_SAVE.equals(operater)) {
			if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
				// 新增保存日志
				abLogService.saveAbLog(abBillTransfer.getId(), abAccrecordsOpt.getId(),
						AccebillConstant.BRIEF_STATUS_6204, AccebillConstant.ABLOG_LOGTYPE_ADD,
						AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
			} else {
				// 修改保存日志
				abLogService.saveAbLog(abBillTransfer.getId(), abAccrecordsOpt.getId(),
						AccebillConstant.BRIEF_STATUS_6204, AccebillConstant.ABLOG_LOGTYPE_UPDATE,
						AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
			}
		} else {
			// 提交处理
			abLogService.saveAbLog(abBillTransfer.getId(), abAccrecordsOpt.getId(), AccebillConstant.BRIEF_STATUS_6204,
					AccebillConstant.ABLOG_LOGTYPE_SUBMIT, msg, userVo);
			AbBillTransferDownApplyVo variables = new AbBillTransferDownApplyVo();// 审批数据
			BeanUtils.copyNotNullProperties(abBillTransfer, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecordsOpt, variables);// 票据流水信息填充到审批对象
			// 计算票据账户余额
			abBillAccBalService.setTransferDownBillBal(abBillTransfer.getId(), userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE, abAccrecordsOpt.getId(),
					variables, abBillTransfer.getRecordCorpId(),userVo.getId());
		}
	}
	/**
	 * 批量提交票据下拨单
	 * 
	 * @param abBillTransferDownManageVo
	 */
	public void batchSubmit(AbBillTransferDownManageVo abBillTransferDownManageVo, ICurrentUser userVo) {
		String[] trids = abBillTransferDownManageVo.getTrids().split(",");// 下拨单id
		String[] aids = abBillTransferDownManageVo.getAids().split(",");// 下拨单操作流水id
		AbBillTransfer abBillTransfer = null;// 下拨单
		AbAccrecords abAccrecordsOpt = null;// 下拨单操作流水
		// 循环处理下拨单
		for (int i = 0; i < trids.length; i++) {
			abBillTransfer = abBillTransferService.get(trids[i]);
			String[] billAid = abBillTransferDtlService.getBillAidArrayByTransferId(abBillTransfer.getId());
			// 选票票据状态校验
			billVerification(billAid);
			// 修改下拨单操作流水
			abAccrecordsOpt = abAccrecordsService.get(aids[i]);
			abAccrecordsOpt.setBusinessDate(abBillTransferDownManageVo.getBusinessDate());
			abAccrecordsOpt.setApplyDate(new Date());// 下拨日期
			abAccrecordsOpt.setBusinessDate(abBillTransferDownManageVo.getBusinessDate());// 业务归属日期
			abAccrecordsOpt.setUpdateName(userVo.getUsername());// 创建人名称
			abAccrecordsOpt.setUpdateBy(userVo.getLoginName());// 创建人登录名称
			abAccrecordsOpt.setUpdateDate(new Date());// 创建日期
			abAccrecordsOpt.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsOpt.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecordsService.update(abAccrecordsOpt, userVo);
			// 选票票据锁定
			batchUpdateBoxStatus(billAid, userVo);
			AbBillTransferDownApplyVo variables = new AbBillTransferDownApplyVo();// 审批数据
			BeanUtils.copyNotNullProperties(abBillTransfer, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecordsOpt, variables);// 票据流水信息填充到审批对象
			variables.setAid(abAccrecordsOpt.getId());
			abBillNotificationService.completeMsg(abAccrecordsOpt,userVo);
			// 计算票据账户余额
			abBillAccBalService.setTransferDownBillBal(trids[i], userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE, abAccrecordsOpt.getId(),
					variables, abBillTransfer.getRecordCorpId(),userVo.getId());
		}
		abLogService.batchSaveAbLog(trids, aids, AccebillConstant.BRIEF_STATUS_6204,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.ABLOG_LOGTYPE_BATCHSUCCE, userVo);
	}
	/**
	 * 删除票据下拨
	 * 
	 * @param aid
	 */
	public void deleteAbBillTransfer(String aid, ICurrentUser userVo) {
		String trid = abAccrecordsService.get(aid).getTrid();
		batchDelete(trid, aid, userVo);
	}
	/**
	 * 批量删除 暂存或打回状态的下拨单做逻辑删除 如果是下拨单: 1、下拨操作流水status=-2 2、选票关联关系信息物理删除
	 * 如果是下拨申请单:操作流水改为作废状态
	 * 
	 * @param trids
	 */
	public void batchDelete(String trids, String aids, ICurrentUser userVo) {
		String[] aidsArray = aids.split(",");
		String[] tridsArray = trids.split(",");
		// 从流水中获取status，解决打回的下拨单不能删除问题
		Integer status;
		int num = 0;
		AbAccrecords abAccrecords = null;
		for (int i = 0; i < aidsArray.length; i++) {
			abAccrecords = abAccrecordsService.get(aidsArray[i]);
			if (AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())) {
				/** 下拨单操作流水status=-2 */
				status = abAccrecords.getStatus();
				if (AccebillConstant.AB_BILL_BUS_STATUS0.equals(status)
						|| AccebillConstant.AB_BILL_BUS_STATUS_1.equals(status)) {
					num = abAccrecordsService.batchDelete(aidsArray, status, userVo);
					if (num == 0) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
					/** 选票关联关系信息物理删除 */
					abBillTransferDtlService.deleteByTransferId(tridsArray[i]);
				}
			} else {
				/** 下拨申请单:操作流水改为作废状态 */
				// 修改下拨单操作流水 status=11改为-1
				num = abAccrecordsService.batchDelete(aidsArray, AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
				if (num != 1) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
			}
			abLogService.batchSaveAbLog(tridsArray, aidsArray, AccebillConstant.BRIEF_STATUS_6204,
					AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE, AccebillConstant.LANG_LOG_BATCHDELETE_SUCC, userVo);
			abBillNotificationService.completeMsg(abAccrecords,userVo);
		}
	}
	/**
	 * 生成下拨单操作流水
	 */
	protected AbAccrecords getAbAccrecordsOpt(AbBillTransferDownManageVo abBillTransferDownManageVo,
			ICurrentUser userVo) {
		AbAccrecords abAccrecords = new AbAccrecords();
		abAccrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
		abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		abAccrecords.setBriefId(AccebillConstant.BRIEF_STATUS_6204);
		abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
		abAccrecords.setApplyDate(new Date());// 下拨日期
		abAccrecords.setCreateName(userVo.getUsername());// 创建人名称
		abAccrecords.setCreateBy(userVo.getLoginName());// 创建人登录名称
		abAccrecords.setCreateDate(new Date());// 创建日期
		return abAccrecords;
	}
	/**
	 * 根据请求参数生成票据下拨流水
	 */
	protected List<AbAccrecords> getAccrecordsList(String[] aids, String trid, Date businessDate, ICurrentUser userVo) {
		List<AbAccrecords> accrecordslist = new ArrayList<AbAccrecords>();
		AbAccrecords abAccrecordsOld = null;
		AbAccrecords abAccrecordsNew = null;
		BtBankAccDTO btBankAccDTO = null;
		AbBillTransfer abBillTransfer = abBillTransferService.get(trid);
		for (int i = 0; i < aids.length; i++) {
			abAccrecordsOld = abAccrecordsService.get(aids[i]);
			abAccrecordsNew = new AbAccrecords();
			abAccrecordsNew.setVid(abAccrecordsOld.getVid());
			abAccrecordsNew.setVvid(abAccrecordsOld.getVvid());
			abAccrecordsNew.setLastid(abAccrecordsOld.getId());
			abAccrecordsNew.setLastbillcode(abVoucherinfoService.get(abAccrecordsOld.getVid()).getBillStatus());
			abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6204);
			abAccrecordsNew.setApplyDate(new Date());// 下拨日期
			abAccrecordsNew.setBusinessDate(businessDate);// 业务归属日期
			abAccrecordsNew.setReqAcctId(abBillTransfer.getRecordBankacc());// 业务申请人账号--下拨中心账号
			abAccrecordsNew.setRecAcctId(abBillTransfer.getRecevieBankacc());// 业务接收人账号
			abAccrecordsNew.setCreateName(userVo.getUsername());// 创建人名称
			abAccrecordsNew.setCreateBy(userVo.getLoginName());// 创建人登录名称
			abAccrecordsNew.setCreateDate(new Date());// 创建日期
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			if(StringUtils.isBlank (abBillTransfer.getRecevieBankacc())){
				abAccrecordsNew.setRecNm(abBillTransfer.getRecevieCorpName ());
			}else{
				btBankAccDTO = abBtBankAccService.getBankAccInfo(abBillTransfer.getRecevieBankacc());// 收票单位账号信息
				if (btBankAccDTO != null) {
					abAccrecordsNew.setRecNm(btBankAccDTO.getAccName());// 业务接收人名称--用于查看后手
				}
			}
			accrecordslist.add(abAccrecordsNew);
		}
		return accrecordslist;
	}
	/**
	 * 根据请求参数生成AbBillTransfer下拨下拨单对象
	 * 
	 * @param abBillTransferDownManageVo
	 * @param opt
	 * @return
	 */
	protected AbBillTransfer getAbBillTransfer(AbBillTransferDownManageVo abBillTransferDownManageVo, String opt,
			ICurrentUser userVo) {
		AbBillTransfer entity = new AbBillTransfer();
		if (StringUtils.isNotEmpty(abBillTransferDownManageVo.getTrid())
				&& AccebillConstant.BRIEF_STATUS_6204.equals(abBillTransferDownManageVo.getBriefId())) {
			entity = abBillTransferService.get(abBillTransferDownManageVo.getTrid());
		}
		// entity.setBillAid(abBillTransferDownManageVo.getBillAid());//票据流水表id，多个用逗号隔开
		// entity.setBillNo(abBillTransferDownManageVo.getBillNo());//选票票号
		entity.setAllocatedCode(abBillTransferDownManageVo.getAllocatedCode());// 下拨单号
		entity.setAllocationMoney(abBillTransferDownManageVo.getAllocationMoney());// 下拨金额
		entity.setAllocationReason(abBillTransferDownManageVo.getAllocationReason());// 下拨原因
		entity.setRecordDate(abBillTransferDownManageVo.getRecordDate());// 录入日期
		entity.setRecordCorpId(abBillTransferDownManageVo.getRecordCorpId());// 下拨单位id
		// entity.setRecordCorpName(abBillTransferDownManageVo.getRecordCorpName());//下拨单位名称
		CorpDTO corpDTO = abCorpService.getNetByCorpId(abBillTransferDownManageVo.getRecevieCorpId());
		if (corpDTO != null) {
			entity.setRecordCorpName(corpDTO.getName());// 下拨单位名称
			entity.setRecordCorpCode(corpDTO.getCode());// 下拨单位code
		}
		entity.setRecevieCorpId(abBillTransferDownManageVo.getRecevieCorpId());// 收票单位id
		entity.setRecevieCorpName(abBillTransferDownManageVo.getRecevieCorpName());// 收票单位名称
		entity.setRecevieBankacc(abBillTransferDownManageVo.getRecevieBankacc());// 收票单位账号

		CorpDTO netInfo = abCorpService.get(entity.getRecevieCorpId());
		if ( netInfo != null){
			entity.setRecevieCorpCode(netInfo.getCode());
		}
		entity.setRecordBankacc(abBillTransferDownManageVo.getRecordBankacc());// 中心下拨账号
		entity.setBillCurrency(abBillTransferDownManageVo.getBillCurrency());// 票据币别
		if (StringUtils.isNotBlank(entity.getBillCurrency())){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.get(entity.getBillCurrency());
			entity.setBillCurName(btCurrencyDTO.getCurName());
		}
		entity.setEnclosure(abBillTransferDownManageVo.getEnclosure());// 附件
		if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)
				|| AccebillConstant.BRIEF_STATUS_6203.equals(abBillTransferDownManageVo.getBriefId())) {
			entity.setCreateBy(userVo.getLoginName());
			entity.setCreateName(userVo.getUsername());
			entity.setCreateDate(new Date());
		} else {// 修改
			entity.setId(abBillTransferDownManageVo.getTrid());
			entity.setUpdateBy(userVo.getLoginName());
			entity.setUpdateName(userVo.getUsername());
			entity.setUpdateDate(new Date());
		}
		return entity;
	}
	/**
	 * 根据条件查询票据下拨单，用于修改
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownManageVo getDataById(String aid) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		List<AbBillTransferDownManageVo> list = abBillTransferDownManageDao.getDataById(abAccrecords.getTrid(), aid);
		if (list != null && !list.isEmpty()) {
			AbBillTransferDownManageVo vo = list.get(0);
			String[] billAidArray = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
			vo.setAids(StringUtils.join(billAidArray, ","));
			vo.setBillAid(StringUtils.join(billAidArray, ","));
			vo.setBillNo(getBillNoByAid(billAidArray));
			return vo;
		}
		return null;
	}
	@Transactional(readOnly = true)
	protected String getBillNoByAid(String[] billAidArray) {
		StringBuilder str = new StringBuilder();
		if (StringUtils.isNotEmpty(billAidArray)) {
			for (int i = 0; i < billAidArray.length; i++) {
				if (i == 0) {
					str.append(abVoucherinfoService.get(abAccrecordsService.get(billAidArray[i]).getVid()).getOrigNo());
				} else {
					str.append(","
							+ abVoucherinfoService.get(abAccrecordsService.get(billAidArray[i]).getVid()).getOrigNo());
				}
			}
		}
		return str.toString();
	}
	/**
	 * 根据参数获取背书参数对象
	 * 
	 * @return
	 */
	protected AbBillEndorseVo getAbBillEndorseVo(AbAccrecords abAccrecords) {
		AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
		AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
		String[] billAidAry = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
		AbAccrecords abAccrecordsNew = null;
		String vids = "", vvids = "";
		for (int i = 0; i < billAidAry.length; i++) {
			abAccrecordsNew = abAccrecordsService.get(billAidAry[i]);// 下拨票据流水
			if (billAidAry.length == 1) {
				vids = abAccrecordsNew.getVid();
				vvids = abAccrecordsNew.getVvid();
			} else {
				vids = vids + "," + abAccrecordsNew.getVid();
				vvids = vvids + "," + abAccrecordsNew.getVvid();
			}
		}
		abBillEndorseVo.setVids(vids);// 主表id
		abBillEndorseVo.setVvids(vvids);// 从表id
		abBillEndorseVo.setBusinessDate(abAccrecords.getBusinessDate());// 业务归属日期
		abBillEndorseVo.setPledgeeAcc(abBillTransfer.getRecevieBankacc());// 业务接收人账号
		if(StringUtils.isEmpty(abBillTransfer.getRecevieBankacc())){
			abBillEndorseVo.setPledgeeBankAcc(abBillTransfer.getRecevieBankacc());// 业务接收人开户行号
			abBillEndorseVo.setPledgeeName(abBillTransfer.getRecevieCorpName());// 业务接收人 -- 后手
		}else{
			BtBankAccDTO btBankAccDTO = abBtBankAccService.getBankAccInfo(abBillTransfer.getRecevieBankacc());// 收票单位账号信息
			abBillEndorseVo.setPledgeeBankAcc(btBankAccDTO.getBankCode());// 业务接收人开户行号
			abBillEndorseVo.setPledgeeName(btBankAccDTO.getAccName());// 业务接收人 -- 后手
		}
		// abBillEndorseVo.setRmk("");//备注
		abBillEndorseVo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);// 内部业务标志
		abBillEndorseVo.setPledgeeCorpid(abBillTransfer.getRecevieCorpId());//被背书人单位ID
//		abBillEndorseVo.setIsHide(AccebillConstant.AB_IS_HIDE_Y);// 是否隐藏流水 1 是
		return abBillEndorseVo;
	}
	/**
	 * 根据条件查询下拨申请单，用于修改
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownManageVo getAbBillTransferDownManageVoById(String aid, Model model) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		AbBillTransferDownManageVo vo = null;
		List<AbEmitBillVo> abEmitBillVoList = null;
		if (abAccrecords != null) {
			vo = new AbBillTransferDownManageVo();
			if (AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())) {
				AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
				BeanUtils.copyNotNullProperties(abBillTransfer, vo);
			}
			AbBillTransfer abBillTransferApply = null;
			Date applyDate = null;
			AbBillAttachmentVO attVo = null;
			if (AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())
					&& StringUtils.isNotEmpty(abAccrecords.getLastid())) {
				/** 基于下拨申请编辑后的下拨单通过流水lastId关联获取下拨申请信息 */
				AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid(), true);
				abBillTransferApply = abBillTransferService.get(abAccrecordsLast.getTrid());
				applyDate = abAccrecordsLast.getBusinessDate();
				attVo = abBillAttachmentService.viewAttachment(abAccrecords.getLastid());
			} else if (AccebillConstant.BRIEF_STATUS_6203.equals(abAccrecords.getBriefId())) {
				abBillTransferApply = abBillTransferService.get(abAccrecords.getTrid(), true);
				applyDate = abAccrecords.getBusinessDate();
				attVo = abBillAttachmentService.viewAttachment(abAccrecords.getId());
			}
			if (abBillTransferApply != null) {
				model.addAttribute("applyInfo", abBillTransferApply);
				model.addAttribute("applyDate", applyDate);
			}
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setId(aid);
			String[] billAidArray = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
			vo.setAids(StringUtils.join(billAidArray, ","));
			vo.setBillNo(getBillNoByAid(billAidArray));
			// this.inject(vo);
			String[] billAid = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
			if (StringUtils.isNotEmpty(billAid)) {
				// 选票票据信息
				abEmitBillVoList = abEmitBillService.getAbEmitBillVoByAid(billAid);
			}
			if(StringUtils.isNotEmpty(vo.getRecevieBankacc())){
				vo.setRecevieBankaccName(abEmitBillService.getBtBankAccDTO(vo.getRecevieBankacc()).getAccName());// 后手
			}
			model.addAttribute("abEmitBillVoList", abEmitBillVoList);
			// 日志查询
			List<AbLog> abLogList = abLogService.listAbLog(abAccrecords.getTrid(), null);
			model.addAttribute("abLogList", abLogList);
			// 获取附件信息
			model.addAttribute("attachmentInfo", attVo);
		}
		return vo;
	}
	/**
	 * 根据条件查询下拨申请单，用于修改（前后端分离专用）
	 *
	 * 前后端分离场景下 Controller 没有 Model，原方法中通过 Model 携带的
	 * applyInfo/applyDate/abEmitBillVoList/abLogList/attachmentInfo 直接放入 VO 返回。
	 *
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownManageVo getAbBillTransferDownManageVoById(String aid) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		AbBillTransferDownManageVo vo = null;
		if (abAccrecords != null) {
			vo = new AbBillTransferDownManageVo();
			if (AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())) {
				AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
				BeanUtils.copyNotNullProperties(abBillTransfer, vo);
			}
			AbBillTransfer abBillTransferApply = null;
			Date applyDate = null;
			AbBillAttachmentVO attVo = null;
			if (AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())
					&& StringUtils.isNotEmpty(abAccrecords.getLastid())) {
				/** 基于下拨申请编辑后的下拨单通过流水lastId关联获取下拨申请信息 */
				AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid(), true);
				abBillTransferApply = abBillTransferService.get(abAccrecordsLast.getTrid());
				applyDate = abAccrecordsLast.getBusinessDate();
				attVo = abBillAttachmentService.viewAttachment(abAccrecords.getLastid());
			} else if (AccebillConstant.BRIEF_STATUS_6203.equals(abAccrecords.getBriefId())) {
				abBillTransferApply = abBillTransferService.get(abAccrecords.getTrid(), true);
				applyDate = abAccrecords.getBusinessDate();
				attVo = abBillAttachmentService.viewAttachment(abAccrecords.getId());
			}
			if (abBillTransferApply != null) {
				vo.setApplyInfo(abBillTransferApply);
				vo.setApplyDate(applyDate);
			}
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setId(aid);
			String[] billAidArray = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
			vo.setAids(StringUtils.join(billAidArray, ","));
			vo.setBillNo(getBillNoByAid(billAidArray));
			// this.inject(vo);
			String[] billAid = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());
			if (StringUtils.isNotEmpty(billAid)) {
				// 选票票据信息
				vo.setAbEmitBillVoList(abEmitBillService.getAbEmitBillVoByAid(billAid));
			}
			if (StringUtils.isNotEmpty(vo.getRecevieBankacc())) {
				vo.setRecevieBankaccName(abEmitBillService.getBtBankAccDTO(vo.getRecevieBankacc()).getAccName());// 后手
			}
			// 日志查询
			vo.setAbLogList(abLogService.listAbLog(abAccrecords.getTrid(), null));
			// 获取附件信息
			vo.setAttachmentInfo(attVo);
		}
		return vo;
	}
	/**
	 * 根据票据流水id，查看业务详细信息
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferDownManageVo getAbBillTransferDownManageVoByAid(String aid) {
		AbBillTransferDownManageVo vo = null;
		if (StringUtils.isNotEmpty(aid)) {
			vo = new AbBillTransferDownManageVo();
			AbAccrecords abAccrecords = abAccrecordsService.get(aid);
			AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid(), true);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			// 票据流水Id方式查看,选票关系表里存储的是下拨业务之前的流水id，故要通过lstId来获取关联的下拨操作单
			AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid());
			AbBillTransferDtl abBillTransferDtl = abBillTransferDtlService
					.getAbBillTransferDtlByAid(abAccrecordsLast.getId());
			AbBillTransfer abBillTransfer = abBillTransferService.get(abBillTransferDtl.getTransferId());
			BeanUtils.copyNotNullProperties(abBillTransfer, vo);
			BeanUtils.copyNotNullProperties(abVoucherinfo, vo);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setId(aid);
			String[] billAidArray = abBillTransferDtlService.getBillAidArrayByTransferId(abBillTransfer.getId());
			// vo.setAids (StringUtils.join(billAidArray, ","));
			vo.setBillNo(getBillNoByAid(billAidArray));
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERDOWNAPPLY_STATUS", "AB_BILL_BUSTYPE_NAME");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	/**
	 * 保存下拨单选票对应关系
	 */
	public void saveAbBillTransferDtl(String transferId, String billAids) {
		if (StringUtils.isNotEmpty(transferId)) {
			// 删除旧的对应关系
			abBillTransferDtlService.deleteByTransferId(transferId);
			// 添加新的选票关系
			if (StringUtils.isNotEmpty(billAids)) {
				String[] billAidArray = billAids.split(",");
				AbBillTransferDtl abBillTransferDtl = null;
				List<AbBillTransferDtl> list = new ArrayList<AbBillTransferDtl>();
				for (int i = 0; i < billAidArray.length; i++) {
					abBillTransferDtl = new AbBillTransferDtl();
					abBillTransferDtl.setTransferId(transferId);
					abBillTransferDtl.setBillAid(billAidArray[i]);
					list.add(abBillTransferDtl);
				}
				if (list != null && !list.isEmpty()) {
					abBillTransferDtlService.batchSave(list);
				}
			}
		}
	}
	/**
	 * 根据提交的下拨申请单检查是否已经选择票据,是isSelectBill=true 否isSelectBill=false
	 * 
	 * @param trid
	 * @return
	 */
	public boolean checkSelectBill(String trid) {
		if (StringUtils.isNotEmpty(trid)) {
			String[] billAidArray = abBillTransferDtlService.getBillAidArrayByTransferId(trid);
			return StringUtils.isNotEmpty(billAidArray);
		}
		return false;
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
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		AbBillTransfer abBillTransfer = abBillTransferService.get(abAccrecords.getTrid());
		List<AbAccrecords> accrecordsNewList = new ArrayList<AbAccrecords>();// 票据下拨流水集合
		String[] billAids = abBillTransferDtlService.getBillAidArrayByTransferId(abAccrecords.getTrid());// 选票票据流水id
		String aids = "";// 选票票据的下拨流水id
		AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
		accrecordsNewList = getAccrecordsList(billAids, abBillTransfer.getId(), abAccrecords.getBusinessDate(), userVo);// 选票票据下拨流水集合
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		int num = 0;
		abBillEndorseVo = getAbBillEndorseVo(abAccrecords);// 票据背书参数
		if (isPassOK) {
			// 修改下拨单操作流水 status=11改为95 valid_sign=0改为1
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 新增票据下拨流水
			num = abAccrecordsService.batchSaveAccrecords(accrecordsNewList, userVo);
			if (num != accrecordsNewList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 票据当前流水作废
			num = abAccrecordsService.batchAppPass(billAids, true, userVo);
			if (num != accrecordsNewList.size()) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			/** 背书 */
			for (AbAccrecords abAccrecordsNew : accrecordsNewList) {
				if ("".equals(aids)) {
					aids = abAccrecordsNew.getId();
				} else {
					aids = aids + "," + abAccrecordsNew.getId();
				}
			}
			abBillEndorseVo.setIds(aids);
			abBillEndorseVo.setBillSource(AccebillConstant.AB_BILL_SOURCE17);
			abBillEndorseService.approvalNo(abBillEndorseVo, userVo,null);
			/** 背书结束 */
			// 记账
			for (AbAccrecords abAccrecordsNew : accrecordsNewList) {
				if(abAccbookService.isAccbook()){
					boolean isAccboo=abAccbookService.checkBuIsAccbook(abAccrecordsNew);
					if(isAccboo){
						if(abAccbookService.isNotAutoAccbook()){
							abAccbookService.addAbAccbookInfo(abAccrecordsNew,"0");
						}else{
							abAccbookService.accbookVoucher(abAccrecordsNew, userVo);
						}
					}
				}
			}
			// 票据下拨操作日志
			abLogService.batchSaveAbLog(abBillEndorseVo.getVvids().split(","), aids.split(","),
					AccebillConstant.BRIEF_STATUS_6204, logType, appMsg, userVo);
		} else {
			// 修改下拨单操作流水 status=11改为-1
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS_1, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 计算票据账户余额
			abBillAccBalService.setTransferDownBillBal(abAccrecords.getTrid(), userVo);
		}
		// 票据锁定释放 从表box_status改为11
		num = abVoucherinfoViceService.batchUpdateBoxStatus(abBillEndorseVo.getVvids().split(","),
				AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != accrecordsNewList.size()) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 下拨单日期
		abLogService.saveAbLog(abAccrecordsService.get(aid).getTrid(), aid, AccebillConstant.BRIEF_STATUS_6204, logType,
				appMsg, userVo);
	}
	@Override
	public BaseDao<AbBillTransfer, String> getEntityDao() {
		return abBillTransferDownManageDao;
	}
	/**
	 * 票据业务完成操作
	 * @param abAccrecords 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	public void busOverOp(AbAccrecords abAccrecords,String logType, String appMsg, ICurrentUser userVo){
		
	}
	
	/**
	 * 票据业务打回操作
	 * @param abAccrecords 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	public void busRepulseOp(AbAccrecords abAccrecords,String logType, String appMsg, ICurrentUser userVo){
		
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput dp = abBillTransferDownManageDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillTransferDownManageVoByAid(aid);
	}
}
