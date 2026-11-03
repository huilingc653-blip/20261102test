package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillPledgeCollectDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillPledgeCollectVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
* @Title: Service
* @Description: 票据-质押收回
* @author onlineGenerator
* @date 2017-07-17
* @version 1.0
*
*/
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGECOLLECT)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_PLEDGECOLLECT)
@Service("abBillPledgeCollectService")
@Transactional
public class AbBillPledgeCollectService extends BaseService<AbBillPledgeCollectVo,String> implements IAccebillBaseService,IWorkFlowListener {
	@Autowired
	private AbBillPledgeCollectDao abBillPledgeCollectDao;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	
    @Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	@Override
	public BaseDao<AbBillPledgeCollectVo, String> getEntityDao() {
		return abBillPledgeCollectDao;
	}
	
	/**
	 *列表显示应收票据质押收回信息
	 *@param input 查询入参
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillPledgeCollect(DataTablesInput input){
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLEDGECOLLECT_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_PLEDGECOLLECT_QUERY,corpIds);
		DataTablesOutput<AbBillPledgeCollectVo> dataTablesOutput = abBillPledgeCollectDao.listAbBillPledgeCollect(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGECOLLECT_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeCollectVo bill : listData) {
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
			for (AbBillPledgeCollectVo bill : listData){
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
	private void injectValues(AbBillPledgeCollectVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_PLEDGECOLLECT_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 票据来源
		Optional.ofNullable(bill.getBillSource()).ifPresent(billSource -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SOURCE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billSource)))
					.ifPresent(dict -> bill.setBillSourceName(dict.getName()));
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
		//业务标记
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
	}
	/**
	 *批量质押收回
	 *@return
	 */
	public boolean batchCollect(AbBillPledgeCollectVo abBillPledgeCollectVo, ICurrentUser userVo) {
		boolean flag = true;
		String[] vouIds = abBillPledgeCollectVo.getVouId().split(",");
		String[] viceIds = abBillPledgeCollectVo.getViceId().split(",");
		String[] accIds = abBillPledgeCollectVo.getAccId().split(",");
		// 批量修改票据主表信息(修改处理标记为处理中)
		int num = abVoucherinfoViceService.batchSubmit(viceIds,userVo);
		if (num != vouIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		//修改从表的BACK_BRIEF_ID为空
//		for (String id : viceIds) {
//			num = abVoucherinfoViceService.updateBackBriefIdIsNull(id);	
//			if (num == 0) {
//				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//			}
//		}
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(abBillPledgeCollectVo);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != accIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		 //批量保存日志
		flag = abLogService.batchSaveAbLog(viceIds, accIds,
				AccebillConstant.BRIEF_STATUS_6019, AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT,
				AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC,userVo);
		
		//挂审批流程
		for (int i = 0; i < num; i++) {
			abBillNotificationService.completeMsg(abList.get(i),userVo);
			//修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());	
			AbAccrecordsVO variables=new AbAccrecordsVO();
			AbVoucherinfo vouInfo=abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo=abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGECOLLECT, abList.get(i).getId(), variables,vouViceInfo.getApplyCorpId(),userVo.getId());
		}
		
		return flag;
	}
	
	/**
	 * 调用审批
	 * @param buId
	 * @param isPassOK
	 * @param appMsg
	 */
	public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info=abAccrecordsService.get(buId, true);
		if (info==null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		if(isPassOK){
			//线下票据
			if(AccebillConstant.AB_BILL_SEND_SIGN_N.equals(info.getSendSign()) && AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())){
				//新票据拆分请求确认处理
				NewDraftDealMethod.newDraftSplitAck(billBankDao,info.getId());
			}
			int oldVSNum = abAccrecordsService.updatAppPass(info.getLastid(), isPassOK, userVo);
			if (oldVSNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			//挂票据账户余额
			abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		}else{
			int reNum=abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
			if(reNum!=1){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			AbAccrecords oldAcc = abAccrecordsService.get(info.getId());
			if("1".equals(oldAcc.getSplitFlg())){
				//新票据拆分后打回处理
				NewDraftDealMethod.draftMerge("1",info.getVid(),billBankDao,billAccrecordsDao);
			}
		}
		String msg = appMsg;
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	
	/**
	 * 审批回调方法
	 */
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg=workFlowResult.isPass()?AccebillConstant.LANG_LOG_APP_SUCC:AccebillConstant.LANG_LOG_APP_BACK;
		this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
	}
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	
	/**
	 * 加载审核列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput){
		WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGECOLLECT, dataTablesInput);
		//workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillPledgeCollectVo> dp = abBillPledgeCollectDao.dataTableApprove(dataTablesInput, businessList);
		Optional.ofNullable(dp.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGECOLLECT_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeCollectVo bill : listData) {
				injectValues(bill, listDictMap);
			}
		});
		return workFlowBusiness.merge(dp);
	}
	
	/**
	 * 获票据信息
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillPledgeCollectVo getAbBillPledgeCollectVoById(String id){
		AbAccrecords accrecordsInfo=abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo=abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid(),true);
		AbBillPledgeCollectVo reInfo=new AbBillPledgeCollectVo();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		//根据银行账号获取单位信息
		CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
		if(corpInfo!=null){
			reInfo.setCorpName(corpInfo.getName());
		}
		//根据银行账号获取银行信息
		BtBankAccDTO bankAccDTO = abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getReqAcctId());
		if(bankAccDTO!=null){
			reInfo.setAccName(bankAccDTO.getAccName());
		}
		
		// 根据银行行号得到银行账号信息
		if(StringUtils.isNotEmpty(accrecordsInfo.getRecAcctsvcr())){
			InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(accrecordsInfo.getRecAcctsvcr());
			if (bankInfo != null) {
				reInfo.setPledgeeBankName(bankInfo.getBankName());
			}
			if(AccebillConstant.AB_INSIDE_SING_Y.equals(accrecordsInfo.getInsideSing()!=null?String.valueOf(accrecordsInfo.getInsideSing()):null)){
				//根据银行账号获取银行信息
				BtBankAccDTO recBankAcc= abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getRecAcctId());
				if(recBankAcc!=null){
					//根据银行账号获取单位信息
					CorpDTO recCorpInfo = abCorpService.get(recBankAcc.getCorpId());
					if(recCorpInfo!=null){
						reInfo.setPledgeeCorpName(recCorpInfo.getName());
					}
				}
			}
		}
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGECOLLECT_STATUS"
				, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM");
		injectValues(reInfo, listDictMap);
		return reInfo;
	}
	
	/**
	 * 新增流水赋值封装
	 *@param abBillPledgeCollectVo 票据-质押收回VO
	 *@return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbBillPledgeCollectVo abBillPledgeCollectVo) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		String[] accIds = abBillPledgeCollectVo.getAccId().split(",");
		for (String id : accIds) {
			// 获取流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(id);
			// 获取当前票据主，从表信息
			AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid());
			AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
			// 新的流水数据
			AbAccrecords entity = new AbAccrecords();
			entity.setVid(ab.getId());// 票据主表ID
			entity.setVvid(abVice.getId());// 票据从表ID
			entity.setLastid(abAccrecords.getId());// 保存上一笔流水ID
			entity.setLastbillcode(ab.getBillStatus ());// 保存上一笔业务类型
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志 0未完成；1可用；2已结束
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_6019);// 流水业务类型6019 应收质押收回 
			entity.setInsideSing(abAccrecords.getInsideSing());// 内部业务标志
			entity.setBankendrsmtmk(ab.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(abBillPledgeCollectVo.getBusinessDate());// 业务办理日期
			entity.setBillFlag(ab.getBillFlag());
			
			// .......业务申请人...，业务接收人....
			entity.setReqRole(abAccrecords.getReqRole());//业务申请人类别
			entity.setReqNm(abAccrecords.getReqNm());//业务申请人
			entity.setReqCmonid(abAccrecords.getReqCmonid());//业务申请人组织机构代码
			entity.setReqAcctId(abVice.getBankAcc());//业务申请人账号
			entity.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());//业务申请人开户行号
			entity.setReqAcctsvcrAg(abAccrecords.getReqAcctsvcrAg());//业务申请人承接行号
			
			entity.setRecRole(abAccrecords.getRecRole());//业务接收人类别
			entity.setRecNm(abAccrecords.getRecNm());//业务接收人名称
			entity.setRecCmonid(abAccrecords.getRecCmonid());//业务接收人组织机构代码
			entity.setRecAcctId(abAccrecords.getRecAcctId());//业务接收人账号
			entity.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());//业务接收人开户行号
			entity.setRecAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());//业务接收人承接行号
			abList.add(entity);
		}
		return abList;
	}
	/**
	 *  解除质押审批通过时，当票据为纸票和线下电票时，以质出人为主的从表信息写质押收回流水数据
	 */
	public boolean saveAbAccrecordsNew(String id, ICurrentUser userVo){
		// 获取质押解除流水表信息
		AbAccrecords abAccrecords = abAccrecordsService.get(id);
		// 获取质押解除票据主表信息
		AbVoucherinfo abVin = abVoucherinfoService.get(abAccrecords.getVid());
//		if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVin.getBillFlag())) && AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abAccrecords.getInsideSing()))) {
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abAccrecords.getInsideSing()))) {
			//判断电票时必须质押发起人和质押接收人都为离线账户
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVin.getBillFlag())) ){
				BtBankAccDTO recAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getRecAcctId());
				BtBankAccDTO reqAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getReqAcctId());
				if((recAcc!=null&&BtBankAccConstant.CIRCULATIONMODE_1.equals(recAcc.getCirculationMode()))
						||(reqAcc!=null&&BtBankAccConstant.CIRCULATIONMODE_1.equals(reqAcc.getCirculationMode()))){
					return false;
				}
			}
			// 获取质押来源票据登记流水表信息
			AbAccrecords lastAccrecords = abAccrecordsService.get(abAccrecords.getLastid());
			// 新的流水数据
			AbAccrecords abAccrecordsNew = new AbAccrecords();
			abAccrecordsNew.setVid(abVin.getId());// 票据主表ID
			
			if (StringUtils.isNotEmpty(lastAccrecords.getLastid())) {
				// 获取质押申请流水表信息
				AbAccrecords lastLastAccrecords = abAccrecordsService.get(lastAccrecords.getLastid());
				// 获取质押申请从表信息
				AbVoucherinfoVice lastLastVice = abVoucherinfoViceService.get(lastLastAccrecords.getVvid());
				abAccrecordsNew.setVvid(lastLastVice.getId());// 票据从表ID
				abAccrecordsNew.setLastid(abAccrecords.getId());// 保存上一笔流水ID
				abAccrecordsNew.setLastbillcode(abVoucherinfoService.get (abAccrecords.getVid ()).getBillStatus ());// 保存上一笔业务类型
				abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6019);
				abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
				abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
				abAccrecordsNew.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_Y));// 内部业务标志
				//abAccrecordsNew.setCnAccrecords(lastAccrecords.getReqNm());// 前手
				abAccrecordsNew.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);// 不得转让标记：EM00
				// 可再转让；EM01
				// 不得转让
				abAccrecordsNew.setApplyDate(new Date());// 申请日期
				abAccrecordsNew.setBusinessDate(abAccrecords.getBusinessDate());// 业务办理日期
				abAccrecordsNew.setRmk(abAccrecords.getRmk());
				abAccrecordsNew.setBillFlag(abVin.getBillFlag());// 流通介质
				abAccrecordsNew.setReqAcctId(abAccrecords.getRecAcctId());// 业务申请人账号
				abAccrecordsNew.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
				abAccrecordsNew.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
				abAccrecordsNew.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
				abAccrecordsService.saveAccrecords(abAccrecordsNew, userVo);
//				// 记账
//				abAccbookService.accbookVoucher(abAccrecordsNew, userVo);
				//挂票据账户余额
				abBillAccBalService.setBankAccBillBal(abAccrecordsNew.getId(), userVo);
				abLogService.saveAbLog(lastLastVice.getId(), abAccrecordsNew.getId(),
						AccebillConstant.BRIEF_STATUS_6019, AccebillConstant.ABLOG_LOGTYPE_AUTO,
						AccebillConstant.LANG_LOG_SUBMIT_SUCC,userVo);
			}
		}
		return true;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillPledgeCollectVoById(aid);
	}
}
