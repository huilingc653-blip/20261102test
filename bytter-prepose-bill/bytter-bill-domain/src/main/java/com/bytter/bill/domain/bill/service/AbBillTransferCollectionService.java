package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillTransferCollectionDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
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
import com.bytter.core.utils.SqlUtil;
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
 * @Title: 票据归集Service
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERCOLLECTION)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERCOLLECTION)
@Service("abBillTransferCollectionService")
@Transactional
public class AbBillTransferCollectionService extends BaseService<AbBillTransferCollectionVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillTransferCollectionDao abBillTransferCollectionDao;
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
	 * 票据归集列表查询
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		// 中心过滤后不必考虑单位权限
		/*
		 * String corpAuthSql = SqlUtil.getInSql(
		 * corpService.getUserCorpIds(AccebillConstant.
		 * AB_BILL_RESOURCE_TRANSFERUPAPPLY_QUERY), "s.id", true);
		 */
		// 中心单位条件corpCenterId不为空，则根据中心单位下面的单位来查询
		String netId = String.valueOf(input.getFormdata().get("eqs_netId"));
		if (StringUtils.isEmpty(netId)) {// 重置没有值时从select组件中取值
			input.getFormdata().put("eqs_netId", netId);
			netId = String.valueOf(input.getFormdata().get("snetId"));
		}
		input.getFormdata().remove("snetId");// 没有用单位组件提交，屏蔽控制台的报错
		List<String> corpCenterIds = new ArrayList<>();
		if (StringUtils.isNotEmpty(netId)) {
			// 获取网点下面的单位
			List<CorpDTO> list = abCorpService.listUnderCorpByNetId(netId);
			for (int i = 0; i < list.size(); i++) {
				corpCenterIds.add ( list.get(i).getId());
			}
		}
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_TRANSFERCOLLECTION_QUERY,input.getCurrentUserId()), "b.APPLY_CORP_ID",
				true);
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_TRANSFERCOLLECTION_QUERY,corpIds);
		DataTablesOutput<AbBillTransferCollectionVo> dataTablesUnion = abBillTransferCollectionDao.getDataTablesUnion(input, corpCenterIds, corpAuthSql);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ABBILLTRANSFERCOLLECTION_STATUS"
					, "AB_ENTER_STOCK");
			for (AbBillTransferCollectionVo bill : listData){
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
			for (AbBillTransferCollectionVo bill : listData){
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
	private void injectValues (AbBillTransferCollectionVo bill,Map<String, Map<String, DictDTO>> listDictMap){
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
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ABBILLTRANSFERCOLLECTION_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
	}
	/**
	 * 票据归集 把页面选中的直属下级单位持有的票据以背书的方式归集到中心 页面需选择要归集的中心账号(审批通过后才背书)
	 */
	@SuppressWarnings("unused")
	public void transfercollection(AbBillTransferCollectionVo abBillTransferCollectionVo, ICurrentUser userVo) {
		String[] abVoucherinfoIds = abBillTransferCollectionVo.getVids().split(",");
		String[] abVoucherinfoViceIds = abBillTransferCollectionVo.getVvids().split(",");
		String[] abAccrecordsIds = abBillTransferCollectionVo.getAids().split(",");
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		AbBillEndorseVo abBillEndorseVo = null;
		/** 归集处理 */
		BtBankAccDTO params = new BtBankAccDTO();
		params.setBankAcc(abBillTransferCollectionVo.getNetBankAcc());// 归集中心账号
		BtBankAccDTO btBankAccDTO = abBtBankAccService.getBankAccList(params).get(0);// 归集中心银行账号信息
		for (int i = 0; i < abVoucherinfoViceIds.length; i++) {
			// 流水处理
			AbAccrecords abAccrecordsNew = getAbAccrecords(abAccrecordsService.get(abAccrecordsIds[i]),
					abBillTransferCollectionVo, abVoucherinfoViceIds[i], userVo);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i]);
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i],true);
			abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			if (btBankAccDTO != null) {
				abAccrecordsNew.setRecNm(btBankAccDTO.getAccName());// 业务接收人名称--用于查看后手
				abAccrecordsNew.setRecAcctId(btBankAccDTO.getBankAcc());// 业务接收人账号--中心账号
				abAccrecordsNew.setRecAcctsvcr(btBankAccDTO.getBankCode());// 业务接收人行号--中心行号
			}
			abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
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
			// 启动工作流 (由归集中心对应的审批人进行审批)
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERCOLLECTION, abAccrecordsNew.getId(),
					variables, abCorpService.get (abVoucherinfoVice.getApplyCorpId()).getNetId (),userVo.getId());
		}
		abLogService.batchSaveAbLog(abVoucherinfoViceIds, abAccrecordsIds, AccebillConstant.BRIEF_STATUS_6202,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
	}
	@Override
	public BaseDao<AbBillTransferCollectionVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 根据网点id获取网点的直属下级单位
	 */
	@Transactional(readOnly = true)
	public List<CorpDTO> getCorpIdByNetId(String netId) {
		return abCorpService.listUnderCorpByNetId(netId);
	}
	/**
	 * 根据网点id获取网点直属下级单位对应的银行账号
	 */
	@Transactional(readOnly = true)
	public List<BtBankAccDTO> getBankAccByNetId(String netId) {
		// 先获取单位
		List<CorpDTO> corpList = getCorpIdByNetId(netId);
		BtBankAccDTO params = null;
		List<BtBankAccDTO> bankAccList = new ArrayList<BtBankAccDTO>();
		for (int i = 0; i < corpList.size(); i++) {
			params = new BtBankAccDTO();
			params.setCorpId(corpList.get(i).getId());
			params.setElectricBill("1");
			bankAccList.addAll(abBtBankAccService.getBankAccList(params));
		}
		return bankAccList;
	}
	/**
	 * 根据网点id获取网点对应的银行账号
	 */
	@Transactional(readOnly = true)
	public List<BtBankAccDTO> getNetBankAccByNetId(String netId) {
		BtBankAccDTO params = new BtBankAccDTO();
		params.setCorpId(netId);
		params.setElectricBill("1");
		return abBtBankAccService.getBankAccList(params);
	}
	/**
	 * 根据单位id获取银行账户信息
	 * 
	 * @param corpId
	 *            单位id
	 * @return BtBankAccDTO 银行接口对象
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccByCorpId(String corpId) {
		BtBankAccDTO bankAccDTO = null;
		BtBankAccDTO params = new BtBankAccDTO();
		params.setCorpId(corpId);
		params.setElectricBill("1");// 票据专户
		List<BtBankAccDTO> bankaccInfoList = abBtBankAccService.getBankAccList(params);
		if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
			bankAccDTO = bankaccInfoList.get(0);
		} else {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
		}
		return bankAccDTO;
	}
	/**
	 * 根据请求参数生成归集流水
	 * 
	 * @param abAccrecordsOld
	 * @return
	 */
	protected AbAccrecords getAbAccrecords(AbAccrecords abAccrecordsOld,
			AbBillTransferCollectionVo abBillTransferCollectionVo, String vvid, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(vvid);
		// 先把旧流水复制过来
		BeanUtils.copyNotNullProperties(abAccrecordsOld, entity);
		entity.setBusGlobalId(null);// 造成记账失败,设置全局流水失败的问题。
		entity.setId(null);
		entity.setLastid(abAccrecordsOld.getId());
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_6202);// 业务类型
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abBillTransferCollectionVo.getBusinessDate());// 业务归属日期
		entity.setCreateName(userVo.getUsername());// 创建人名称
		entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		entity.setReqAcctId(abVoucherinfoVice.getBankAcc());// 业务申请人账号--持票人账号
		entity.setReqAcctsvcr(abVoucherinfoVice.getBankCode());// 业务申请人行号--持票人行号
		entity.setCreateDate(new Date());// 创建日期
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
	@Transactional(readOnly = true)
	public AbBillEndorseVo getAbBillEndorseVo(String aid) {
		AbBillEndorseVo abBillEndorseVo = new AbBillEndorseVo();
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		abBillEndorseVo.setIds(aid);// 流水表id
		abBillEndorseVo.setVids(abAccrecords.getVid());// 主表id
		abBillEndorseVo.setVvids(abAccrecords.getVvid());// 从表id
		abBillEndorseVo.setBusinessDate(abAccrecords.getBusinessDate());// 业务归属日期
		BtBankAccDTO reqAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getReqAcctId());
		// 获取当前票据主，从表信息
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
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
		abBillEndorseVo.setPledgeeAcc(btBankAccDTO.getBankAcc());// 业务接收人账号
		abBillEndorseVo.setPledgeeBankAcc(btBankAccDTO.getBankCode());// 业务接收人开户行号
		abBillEndorseVo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);// 内部业务标志
//		abBillEndorseVo.setIsHide(AccebillConstant.AB_IS_HIDE_Y);// 是否隐藏流水 1 是
		abBillEndorseVo.setBillSource(AccebillConstant.AB_BILL_SOURCE15);
		return abBillEndorseVo;
	}
	/**
	 * 批量审批
	 * 
	 * @param aid
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
			if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(info);
				if(abAccbookService.isNotAutoAccbook()&&isAccboo){
					abAccbookService.addAbAccbookInfo(info);
					int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS94, userVo);
					if (appNum != 1) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
					abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6202, logType, appMsg, userVo);
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
		// 票据归集流水status11改为95
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		/** 背书 */
		abBillEndorseVo = getAbBillEndorseVo(abAccrecords.getId());
		// 调用背书接口，变更持票人,归集到上级中心的票据，持票人修改成上级中心
		abBillEndorseService.approvalNo(abBillEndorseVo, userVo,new String[]{abAccrecords.getId()});
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
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6202, logType, appMsg,
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
		// 1归集流水删除status改为-2
		num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 2上一条流水VALID_SIGN=1 恢复为有效
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
		abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_6202,
				abAccrecords.getId());
		// 计算票据账户余额
		abBillAccBalService.setBankAccBillBal(abAccrecords.getId(), userVo);
		abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6202, logType, appMsg,
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
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLTRANSFERCOLLECTION, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillTransferCollectionVo> dp = abBillTransferCollectionDao.dataTableApprove(dataTablesInput, businessList);
		if (ObjectUtil.isNotEmpty(dp.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_ENTER_STOCK","AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_ABEMITBILLSUPPLY_STATUS", "AB_BILL_SEND_SIGN");
			for (AbBillTransferCollectionVo billSupplyVo : dp.getData()) {
				injectValues(billSupplyVo, listDictMap);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 票据归集审批详情页
	 * 
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillTransferCollectionVo getAbBillTransferCollectionVoById(String aid) {
		AbBillTransferCollectionVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid);
		if (abAccrecords != null) {
			vo = new AbBillTransferCollectionVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setAid(aid);
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getCorpId())) {
				vo.setCorpName(abCorpService.get(abVoucherinfoVice.getCorpId()).getName());
				// 获取归集单位银行信息
				BtBankAccDTO btBankAccDTO = null;
				if(StringUtils.isNotBlank(abVoucherinfoVice.getBankAcc())){
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
					vo.setBankTypeName(StringUtils.isNotEmpty(abAccrecords.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(abAccrecords.getVvid()):null);
				}
				// model.addAttribute("corpBankInfo", btBankAccDTO);
				// 获取中心银行信息
				// 中心单位开户银行、中心银行账号=rec_acct_id、中心银行行号=rec_acctsvcr
				String prantId = abCorpService.get(abVoucherinfoVice.getCorpId()).getNetId();// 中心单位id
				btBankAccDTO = getBtBankAccByCorpId(prantId);// 中心单位银行信息
				vo.setNetBankName(btBankAccDTO.getBankName());
				vo.setNetCorpName(abCorpService.get(prantId).getName());
				// model.addAttribute("centerCorpBankInfo", btBankAccDTO);
				// model.addAttribute("centerCorpName",corpService.get(prantId).getName());//中心单位名称
			}
		}
		if (ObjectUtil.isNotEmpty(vo)){
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ABBILLTRANSFERCOLLECTION_STATUS"
					, "AB_ENTER_STOCK");
			injectValues(vo,listDictMap);
		}
		return vo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillTransferCollectionVoById(aid);
	}
}
