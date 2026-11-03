package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillTransferUpApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.bill.domain.billbank.util.StringUtil;
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
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
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
 * @Title: 票据上划Service
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERUPAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERUPAPPLY)
@Service("abBillTransferUpApplyService")
@Transactional
public class AbBillTransferUpApplyService extends BaseService<AbBillTransferUpApplyVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillTransferUpApplyDao AbBillTransferUpApplyDao;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBillEndorseService abBillEndorseService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 票据上划列表查询
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_TRANSFERUPAPPLY_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_TRANSFERUPAPPLY_QUERY,corpIds);
		DataTablesOutput<AbBillTransferUpApplyVo> dataTablesUnion = AbBillTransferUpApplyDao.getDataTablesUnion(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ENTER_STOCK"
					, "AB_BILL_TRANSFERUPAPPLY_STATUS");
			for (AbBillTransferUpApplyVo bill : listData){
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
			for (AbBillTransferUpApplyVo bill : listData){
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
	private void injectValues(AbBillTransferUpApplyVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 票据状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TRANSFERUPAPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
	}
	/**
	 * 票据上划批量提交 通过持票人银行账号对应的单位找到上级单位id 通过上级单位id，找到上级单位的票据专户信息并把票据背书到该账户(审批通过后背书)
	 */
	public boolean transferup(AbBillTransferUpApplyVo abBillTransferUpApplyVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillTransferUpApplyVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillTransferUpApplyVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillTransferUpApplyVo.getAids().split(",");
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		BtBankAccDTO btBankAccDTO = null;// 上级中心归集银行账号信息
		List<AbAccrecords> abAccrecordsList = new ArrayList<AbAccrecords>();
		AbAccrecords abAccrecordsNew = null;// 票据上划流水
		/** 上划处理 */
		for (int i = 0; i < abVoucherinfoIds.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i], true);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i]);
			BtBankAccDTO bankAccInfo = null;
			if(StringUtils.isNotBlank (abVoucherinfoVice.getBankAcc())){
				try {
					bankAccInfo = abEmitBillService.getBtBankAccDTO(abVoucherinfoVice.getBankAcc());
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 持票人账号不存在!
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTEXIST);
					} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
							.equals(e.getIMessageEnum())) {
						// 持票人账号不是票据专户!
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTELECTRICBILL);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			}
			// 上划账号取值：该账号为票据账户，取持票单位银行对应的中心账号，
			String prantId = abCorpService.get(abVoucherinfoVice.getCorpId()).getNetId();
			if (StringUtils.isEmpty(prantId)) {
				// 没有上级中心不能进行上划操作
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_NETIDISNULL);
			}
			//根据corpId与票据专户取第一条数据(此处有问题，多个账号有的是离线的有的是在线的，不能选择)
			btBankAccDTO = getBtBankAccByCorpId(prantId);// 中心单位银行信息
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))
					&& bankAccInfo != null
					&& String.valueOf(BtBankAccConstant.CIRCULATIONMODE_1).equals(bankAccInfo.getCirculationMode())) {
				if (btBankAccDTO == null || StringUtils.isEmpty(btBankAccDTO.getBankAcc())) {
					// 中心没有维护票据专户，电票不能上划!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_NETBANKACCISNULL);
				}
			}
			// 流水处理
			abAccrecordsNew = getAbAccrecords(abAccrecordsService.get(abAccrecordsIds[i]), abBillTransferUpApplyVo,
					abVoucherinfoVice, userVo);
			if(btBankAccDTO!=null){
				abAccrecordsNew.setRecNm(btBankAccDTO.getAccName());// 业务接收人名称--用于查看后手
				abAccrecordsNew.setRecAcctId(btBankAccDTO.getBankAcc());// 业务接收人账号--中心账号
				abAccrecordsNew.setRecAcctsvcr(btBankAccDTO.getBankCode());// 业务接收人行号--中心行号
			}
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			abAccrecordsList.add(abAccrecordsNew);
			abBillNotificationService.completeMsg(abAccrecordsNew, userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecordsNew, variables);// 票据流水信息填充到审批对象
			// 票据当前流水改为作废
			int num = abAccrecordsService.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为10
			num = abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 计算票据账户余额
			abBillAccBalService.setBankAccBillBal(abAccrecordsNew.getId(), userVo);
			// 启动工作流
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERUPAPPLY, abAccrecordsNew.getId(),
					variables, abVoucherinfoVice.getApplyCorpId(), userVo.getId());
		}
		abLogService.batchSaveAbLog(abVoucherinfoViceIds, abAccrecordsIds, AccebillConstant.BRIEF_STATUS_6201,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		return true;
	}
	@Override
	public BaseDao<AbBillTransferUpApplyVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 根据单位id获取银行账户信息
	 * 根据corpId与票据专户取第一条数据(此处有问题，多个账号有的是离线的有的是在线的，不能选择)
	 * @param corpId
	 *            单位id
	 * @return BtBankAccDTO 银行接口对象
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccByCorpId(String corpId) {
		BtBankAccDTO bankAccDTO = null;
		BtBankAccDTO params = new BtBankAccDTO();
		params.setCorpId(corpId);
		params.setElectricBill("1");// 电票专户
		List<BtBankAccDTO> bankaccInfoList = abBtBankAccService.getBankAccList(params);
		if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
			bankAccDTO = bankaccInfoList.get(0);
		}
		return bankAccDTO;
	}
	/**
	 * 根据请求参数生成上划流水
	 * 
	 * @param abAccrecordsOld
	 * @param abBillTransferUpApplyVo
	 * @return
	 */
	protected AbAccrecords getAbAccrecords(AbAccrecords abAccrecordsOld,
			AbBillTransferUpApplyVo abBillTransferUpApplyVo, AbVoucherinfoVice abVoucherinfoVice, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		// 先把旧流水复制过来
		BeanUtils.copyNotNullProperties(abAccrecordsOld, entity);
		entity.setBusGlobalId(null);// 造成记账失败,设置全局流水失败的问题。
		entity.setId(null);
		entity.setLastid(abAccrecordsOld.getId());
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_6201);// 业务类型
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abBillTransferUpApplyVo.getBusinessDate());// 业务归属日期
		entity.setReqAcctId(abVoucherinfoVice.getBankAcc());// 业务申请人账号--持票人账号
		entity.setReqAcctsvcr(abVoucherinfoVice.getBankCode());// 业务申请人行号--持票人行号
		entity.setCreateName(userVo.getUsername());// 创建人名称
		entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		entity.setCreateDate(new Date());// 创建日期
		entity.setUpdateBy(null);
		entity.setUpdateName(null);
		entity.setUpdateDate(null);
		return entity;
	}
	/**
	 * 根据参数获取背书参数对象
	 * 
	 * @return
	 */
	protected AbBillEndorseVo getAbBillEndorseVo(String aid) {
		AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		abBillEndorseVo.setIds(aid);// 流水表id
		abBillEndorseVo.setVids(abAccrecords.getVid());// 主表id
		abBillEndorseVo.setVvids(abAccrecords.getVvid());// 从表id
		abBillEndorseVo.setBusinessDate(abAccrecords.getBusinessDate());// 业务归属日期
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
		// 获取当前票据主，从表信息
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
		BtBankAccDTO reqAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getReqAcctId());
		String prantId = abCorpService.get(abVoucherinfoVice.getCorpId()).getNetId();
		BtBankAccDTO btBankAccDTO = getBtBankAccByCorpId(prantId);// 中心单位银行信息
		if(btBankAccDTO!=null){
			abBillEndorseVo.setPledgeeAcc(btBankAccDTO.getBankAcc());// 业务接收人账号
			abBillEndorseVo.setPledgeeBankAcc(btBankAccDTO.getBankCode());// 业务接收人开户行号
			//电票
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))
				&& BtBankAccConstant.CIRCULATIONMODE_1.equals(reqAcc.getCirculationMode())
				&& BtBankAccConstant.CIRCULATIONMODE_1.equals(btBankAccDTO.getCirculationMode())){
				abBillEndorseVo.setPledgeeName(btBankAccDTO.getAccName());// 业务接收人(账号户名)
			}
		}
		if(StringUtil.isEmpty(abBillEndorseVo.getPledgeeName())){
			CorpDTO corpDTO = abCorpService.get(prantId);
			abBillEndorseVo.setPledgeeName(corpDTO.getName());// 业务接收人(上级中心)
			abBillEndorseVo.setPledgeeCorpid(corpDTO.getId());
		}
		abBillEndorseVo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);// 内部业务标志
//		abBillEndorseVo.setIsHide(AccebillConstant.AB_IS_HIDE_Y);// 是否隐藏流水 1 是
		abBillEndorseVo.setBillSource(AccebillConstant.AB_BILL_SOURCE16);
		return abBillEndorseVo;
	}
	/**
	 * 批量审批
	 * 
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void batchApprove(String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		AbAccrecords info = abAccrecordsService.get(aid, true);
		if (info == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}

		if (isPassOK) {
			String logType = AccebillConstant.ABLOG_LOGTYPE_APP;

			String billStatusOld = AccebillConstant.AB_BILL_STATUS_100006;
			String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
			String ebillBlCrclIndc = "EM00".equals(info.getBankendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303;
			//设置离线票据的票据状态和流通标志，并返回从票数据
			AbAccebillUtils.setOffLineVoucherViceBillStatus(abVoucherinfoService,info.getVid(),info.getVvid()
					,info.getSendSign(),abVoucherinfoViceService,billStatusOld, billStatusNew, ebillBlCrclIndc);
			if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(info);
				if(abAccbookService.isNotAutoAccbook()&&isAccboo){
					abAccbookService.addAbAccbookInfo(info);
					int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS94, userVo);
					if (appNum != 1) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
					abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6201, logType, appMsg, userVo);
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
		AbBillEndorseVo abBillEndorseVo = null;
		int num = 0;
		// 票据上划流水status11改为95 vliad_sing=0改为1
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		/** 背书 */
		// abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
		// 调用背书接口，变更持票人,上划到上级中心的票据，持票人修改成上级中心
		abBillEndorseVo = getAbBillEndorseVo(abAccrecords.getId());
		abBillEndorseService.approvalNo(abBillEndorseVo, userVo,null);
		/*
		 * //纸票 内部业务会入库，应收票据为暂存状态，故box_status=10 if
		 * (abBillEndorseVo.getInteriorApply().equals("1")&&AccebillConstant
		 * .AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.
		 * getBillFlag()))) { //从表box_status改为10
		 * abVoucherinfoViceService.update(abAccrecords.getVvid(),
		 * AccebillConstant.AB_BOX_STATUS_N,
		 * AccebillConstant.AB_BOX_STATUS_Y); }else{ //从表box_status改为11
		 * abVoucherinfoViceService.update(abAccrecords.getVvid(),
		 * AccebillConstant.AB_BOX_STATUS_Y,
		 * AccebillConstant.AB_BOX_STATUS_N); }
		 */
		// modify by luop 2017/9/28 入库后票据改为有效状态
		// 从表box_status改为11
		abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		/** 背书结束 */
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6201, logType, appMsg,
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
		// 上划流水删除 status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 上一条流水VALID_SIGN=1恢复为有效
		num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 从表box_status改为11
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				AccebillConstant.AB_BOX_STATUS_N, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 从表添加打回标记
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_6201,
				abAccrecords.getId());
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6201, logType, appMsg,
				userVo);
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERUPAPPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillTransferUpApplyVo> dp = AbBillTransferUpApplyDao.dataTableApprove(dataTablesInput, businessList);
		if (ObjectUtil.isNotEmpty(dp.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_ENTER_STOCK","AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_ABEMITBILLSUPPLY_STATUS", "AB_BILL_SEND_SIGN");
			for (AbBillTransferUpApplyVo billSupplyVo : dp.getData()) {
				injectValues(billSupplyVo, listDictMap);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 票据上划审批详情页
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferUpApplyVo getAbBillTransferUpApplyVoById(String aid) {
		AbBillTransferUpApplyVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		if (abAccrecords != null) {
			vo = new AbBillTransferUpApplyVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setAid(aid);
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getCorpId())) {
				vo.setCorpName(abCorpService.get(abVoucherinfoVice.getCorpId()).getName());
				// 获取上划单位银行信息
				BtBankAccDTO btBankAccDTO = null;
				if(StringUtils.isNotEmpty(abVoucherinfoVice.getBankAcc())){
					try {
						btBankAccDTO = abEmitBillService.getBtBankAccDTO(abVoucherinfoVice.getBankAcc());
					} catch (BusinessException e) {
						if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
							// 持票人账号不存在!
							throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTEXIST);
						} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
								.equals(e.getIMessageEnum())) {
							// 持票人账号不是票据专户!
							throw new BusinessException(
									AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTELECTRICBILL);
						} else {
							throw new BusinessException(e.getIMessageEnum());
						}
					}
					// 改造为属性 上划单位开户银行、上划银行账号=req_acct_id、上划银行行号=req_acctsvcr
					vo.setCorpBankName(btBankAccDTO.getBankName());
				}
				// model.addAttribute("corpBankInfo", btBankAccDTO);
				// 获取中心银行信息
				// 中心单位开户银行 、中心银行账号=rec_acct_id、中心银行行号=rec_acctsvcr
				String prantId = abCorpService.get(abVoucherinfoVice.getCorpId()).getNetId();// 中心单位id
				btBankAccDTO = getBtBankAccByCorpId(prantId);// 中心单位银行信息
				if(btBankAccDTO!=null){
					vo.setNetBankName(btBankAccDTO.getBankName());
				}
				vo.setNetCorpName(abCorpService.get(prantId).getName());
				vo.setBankTypeName(StringUtils.isNotEmpty(abAccrecords.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(abAccrecords.getVvid()):null);
				// model.addAttribute("centerCorpBankInfo", btBankAccDTO);
				// model.addAttribute("centerCorpName",corpService.get(prantId).getName());//中心单位名称
			}
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ENTER_STOCK"
					, "AB_BILL_TRANSFERUPAPPLY_STATUS");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillTransferUpApplyVoById(aid);
	}
}
