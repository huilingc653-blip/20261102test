package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillPledgeRelieveDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo;
import com.bytter.bill.domain.bill.vo.AbBillPledgeRelieveVo;
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
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
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
 * @Title: Service
 * @Description: 票据-解除质押
 * @author onlineGenerator
 * @date 2017-07-14
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGERELIEVE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_PLEDGERELIEVE)
@Service("abBillPledgeRelieveService")
@Transactional
public class AbBillPledgeRelieveService extends BaseService<AbBillPledgeRelieveVo, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillPledgeRelieveDao abBillPledgeRelieveDao;
	@Autowired
	private AbAccbookService abAccbookService;
	
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
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillPledgeCollectService abBillPledgeCollectService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	@Autowired
	private BillTicketSplittingUtils billTicketSplittingUtils;
	@Autowired
	private AbVoucherinfoDao abVoucherinfoDao;
	@Autowired
	private AbVoucherinfoViceDao abVoucherinfoViceDao;
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	@Override
	public BaseDao<AbBillPledgeRelieveVo, String> getEntityDao() {
		return abBillPledgeRelieveDao;
	}
	/**
	 * 列表显示应收票据解除质押信息
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillPledgeRelieve(DataTablesInput input) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PLEDGERELIEVE_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_PLEDGERELIEVE_QUERY,corpIds);
		DataTablesOutput<AbBillPledgeRelieveVo> dataTablesOutput = abBillPledgeRelieveDao.listAbBillPledgeRelieve(input, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeRelieveVo bill : listData){
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
			for (AbBillPledgeRelieveVo bill : listData){
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
	private void injectValues(AbBillPledgeRelieveVo bill,Map<String, Map<String, DictDTO>> listDictMap){
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS");
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
	 * 批量解除质押
	 * 
	 * @return
	 */
	public boolean batchRelieve(AbBillPledgeRelieveVo abBillPledgeRelieveVo, ICurrentUser userVo) {
		boolean flag = true;
		if(abBillPledgeRelieveVo.getVouId() == null && abBillPledgeRelieveVo.getTransMoneys() == null){
			throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		String[] vouIdarr = abBillPledgeRelieveVo.getVouId().split(",");
		//String[] moneysArr = StringUtils.isBlank(abBillPledgeRelieveVo.getTransMoneys()) ? new String[vouIdarr.length] : abBillPledgeRelieveVo.getTransMoneys().split(",");
		List<String> moneysList = StringUtils.isBlank(abBillPledgeRelieveVo.getTransMoneys()) ? new ArrayList<String>() : Arrays.asList(abBillPledgeRelieveVo.getTransMoneys());
		List<String> newMons = new ArrayList<>();
		newMons.addAll(moneysList);
		//int num = 0;
		if(vouIdarr.length != newMons.size()){
			for(int i = 0; i < vouIdarr.length; i++){
				String vd = vouIdarr[i];
				AbVoucherinfo info = abVoucherinfoService.get(vd);
				if("0".equals(info.getNewStmIssuofpprInd())){
					newMons.add(i,info.getBillMoney().toString());
					//num++;
				}
			}
		}
		String moneysStr = org.apache.commons.lang.StringUtils.join(newMons,",");
		abBillPledgeRelieveVo.setTransMoneys(moneysStr);
		String[] oldAccIdArr = abBillPledgeRelieveVo.getAccId().split(",");
		//解除质押  新一代票据功能处理之拆票处理
		Map<String, String> resultMap = billTicketSplittingUtils.ticketSplitting(abBillPledgeRelieveVo.getVouId(), abBillPledgeRelieveVo.getViceId()
				, abBillPledgeRelieveVo.getAccId() , abBillPledgeRelieveVo.getTransMoneys(),abBillPledgeRelieveVo.getSbbllRngStrtSnSplit(),abBillPledgeRelieveVo.getSbbllRngEndSnSplit(),userVo);
		abBillPledgeRelieveVo.setVouId(resultMap.get("infoIds"));
		abBillPledgeRelieveVo.setViceId(resultMap.get("viceIds"));
		abBillPledgeRelieveVo.setAccId(resultMap.get("accIds"));
		abBillPledgeRelieveVo.setTransMoneys(resultMap.get("transMoneys"));
		String[] vouIds = abBillPledgeRelieveVo.getVouId().split(",");
		String[] viceIds = abBillPledgeRelieveVo.getViceId().split(",");
		String[] accIds = abBillPledgeRelieveVo.getAccId().split(",");
		// 批量修改票据主表信息(修改处理标记为处理中)
		int num = abVoucherinfoViceService.batchSubmit(viceIds, userVo);
		if (num != vouIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 修改从表的BACK_BRIEF_ID为空
		// for (String id : viceIds) {
		// num = abVoucherinfoViceService.updateBackBriefIdIsNull(id);
		// if (num == 0) {
		// throw new
		// BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		// }
		// }
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(abBillPledgeRelieveVo,oldAccIdArr);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != accIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量保存日志
		flag = abLogService.batchSaveAbLog(viceIds, accIds, AccebillConstant.BRIEF_STATUS_019,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
		
		// 挂审批流程
		for (int i = 0; i < num; i++) {
			// 挂票据账户余额
			abBillAccBalService.setBankAccBillBal(abList.get(i).getId(), userVo);
			abBillNotificationService.completeMsg(abList.get(i),userVo);
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());
			AbAccrecordsVO variables = new AbAccrecordsVO();
			AbVoucherinfo vouInfo = abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo = abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGERELIEVE, abList.get(i).getId(), variables,
					vouViceInfo.getApplyCorpId(),userVo.getId());
		}
		return flag;
	}
	/**
	 * ListToStr
	 */
	private String list2Str(List<String> tranParam){
		StringBuilder stringBuilder = new StringBuilder();
		for (String s : tranParam) {
			stringBuilder.append(s).append(",");
		}
		String result = new String(stringBuilder);
		return result.substring(0, result.length()-1);
	}
	/**
	 * 调用审批
	 * 
	 * @param buId
	 * @param isPassOK
	 * @param appMsg
	 */
	public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(buId, true);
		if (info == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
        if (isPassOK) {
			String billStatusOld = AccebillConstant.AB_BILL_STATUS_190006;
			String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
			String ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0301;
			//设置离线票据的票据状态和流通标志，并返回从票数据
			AbAccebillUtils.setOffLineVoucherViceBillStatus(abVoucherinfoService,info.getVid(),info.getVvid()
					,info.getSendSign(),abVoucherinfoViceService,billStatusOld, billStatusNew, ebillBlCrclIndc);

			//线下票据
			if(AccebillConstant.AB_BILL_SEND_SIGN_N.equals(info.getSendSign())
					&& AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())
					&& !"2".equals(info.getCopyFlag())){
				//新票据拆分请求确认处理
				NewDraftDealMethod.newDraftSplitAck(billBankDao,info.getId());
			}
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
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS95;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		// 解除质押审批通过时，当票据为纸票时，以质出人为主的从表信息写质押收回流水数据
		abBillPledgeCollectService.saveAbAccrecordsNew(info.getId(), userVo);
		int oldVSNum = abAccrecordsService.updatAppPass(info.getLastid(), true, userVo);
		if (oldVSNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		//批量拆分新业务批量处理
		if("2".equals(info.getCopyFlag())){
			billTicketSplittingUtils.splitConsentOp(info);
		}
		// 挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
		int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
		int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		if (reNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);

		AbAccrecords oldAcc = abAccrecordsService.get(info.getId());
		if("1".equals(oldAcc.getSplitFlg())){
			//批量付票业务进行处理
			if("2".equals(info.getCopyFlag())){
				billTicketSplittingUtils.splitRepulseOp(info);
			}else {
				//新票据拆分后打回处理
				NewDraftDealMethod.draftMerge("1", info.getVid(), billBankDao, billAccrecordsDao);
			}
		}
	}

	/**
	 * 新增流水赋值封装
	 * 
	 * @param abBillPledgeRelieveVo
	 *            票据质押VO
	 * @param abBillPledgeRelieveVo
	 *            是否挂审批
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(AbBillPledgeRelieveVo abBillPledgeRelieveVo, String[] oldAccIdArr) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		String[] accIds = abBillPledgeRelieveVo.getAccId().split(",");
		for (int i = 0; i < accIds.length; i++) {
			String id = accIds[i];

			// 获取流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(id,true);
			// 获取当前票据主，从表信息
			AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(),true);
			AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(),true);
			// 新的流水数据
			AbAccrecords entity = new AbAccrecords();

			if("2".equals(abAccrecords.getCopyFlag())){
				entity.setSplitFlg(abAccrecords.getSplitFlg());
				entity.setCopyFlag(abAccrecords.getCopyFlag());
				entity.setOldAccId(abAccrecords.getOldAccId());
			} else {
				//oldLastAccIdArr[i].equals(accIdArrays[i]) 为true，说明为不拆分的票据（可能为旧票或者不拆分新票）
				entity.setSplitFlg(oldAccIdArr[i].equals(id) ? "0" : "1");
				//拆分交易的话设置此字段为未拆分之前的id
				entity.setOldAccId("1".equals(entity.getSplitFlg()) ? oldAccIdArr[i] : null);
			}

			entity.setVid(ab.getId());// 票据主表ID
			entity.setVvid(abVice.getId());// 票据从表ID
			entity.setLastid(abAccrecords.getId());// 保存上一笔流水ID
			entity.setLastbillcode(abVoucherinfoService.get(abAccrecords.getVid()).getBillStatus());// 保存上一笔业务类型
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
																		// 0未完成；1可用；2已结束
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态：-2已删除；-1打回；0
																	// 暂存；10提交；11-94审批中；95
																	// 已完成
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_019);// 流水业务类型019
																	// 应收质押解除
			entity.setInsideSing(abAccrecords.getInsideSing());// 内部业务标志
			entity.setBankendrsmtmk(ab.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01
															// 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(abBillPledgeRelieveVo.getBusinessDate());// 业务办理日期
			entity.setBillFlag(ab.getBillFlag());
			entity.setReqAcctId(abVice.getBankAcc());// 业务申请人账号
			// .......业务申请人...
			// entity.setReqRole(abAccrecords.getRecRole());//业务申请人类别
			// bankAccDTO = getBtBankAccDTO(abVice.getBankAcc());
			entity.setReqNm(abAccrecords.getReqNm());// 业务申请人
			entity.setReqCmonid(abAccrecords.getReqCmonid());// 业务申请人组织机构代码
			entity.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务申请人开户行号
			entity.setReqAcctsvcrAg(abAccrecords.getReqAcctsvcrAg());//业务申请人承接行号
			// 业务接收人
			entity.setRecCmonid(abAccrecords.getRecCmonid());// 业务接收人组织机构代码
			entity.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
			entity.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
			entity.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
			entity.setRecAcctsvcrAg(abAccrecords.getRecAcctsvcrAg());// 业务接收人承接行号
			abList.add(entity);
		}
		return abList;
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_PLEDGERELIEVE, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillPledgeRelieveVo> dp = abBillPledgeRelieveDao.dataTableApprove(dataTablesInput, businessList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS"
					, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BRIEF_STATUS");
			for (AbBillPledgeRelieveVo bill : listData){
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
			return dp;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dp;
		}
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			for (AbBillPledgeRelieveVo bill : listData){
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

		return workFlowBusiness.merge(dp);
	}
	/**
	 * 获票据信息
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillPledgeRelieveVo getAbBillPledgeRelieveVoById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbBillPledgeRelieveVo reInfo = new AbBillPledgeRelieveVo();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		// 根据银行账号获取单位信息
		CorpDTO corpInfo = abCorpService.get(abVoucherinfoVice.getCorpId());
		if (corpInfo != null) {
			reInfo.setCorpName(corpInfo.getName());
		}
		// 根据银行账号获取银行信息
		BtBankAccDTO bankAccDTO = abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getReqAcctId());
		if (bankAccDTO != null) {
			reInfo.setAccName(bankAccDTO.getAccName());
			reInfo.setBankCode(bankAccDTO.getBankCode());
		}
		if (StringUtils.isNotEmpty(accrecordsInfo.getRecAcctId())||StringUtils.isNotEmpty(accrecordsInfo.getRecNm())) {
			// 根据银行账号获取银行信息
			BtBankAccDTO recBankAcc = abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getRecAcctId());
			if (recBankAcc != null) {
				if (StringUtils.isEmpty(reInfo.getPledgeeBankName())) {
					reInfo.setPledgeeBankName(recBankAcc.getBankName());
				}
				// 根据银行账号获取单位信息
				CorpDTO recCorpInfo = abCorpService.get(recBankAcc.getCorpId());
				if (recCorpInfo != null) {
					reInfo.setPledgeeCorpName(recCorpInfo.getName());
				}
				if(reInfo.getInsideSing()==null){
					reInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));
				}
			}else{
				CorpDTO recCorpInfo = abAccrecordsService.getCorpInfo(accrecordsInfo.getRecAcctId(), accrecordsInfo.getRecNm());
				if (recCorpInfo != null) {
					reInfo.setPledgeeCorpName(recCorpInfo.getName());
					if(reInfo.getInsideSing()==null){
						reInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_Y));
					}
				}
			}
		}
		if(reInfo.getInsideSing()==null){
			reInfo.setInsideSing(Integer.valueOf(AccebillConstant.AB_INSIDE_SING_N));
		}
		if (StringUtils.isEmpty(reInfo.getPledgeeBankName())) {
			reInfo.setPledgeeBankName(reInfo.getRecAcctsvcrName());
		}
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS"
				, "AB_BILL_SOURCE", "AB_ENTER_STOCK", "AB_BANEM");
		injectValues(reInfo, listDictMap);
		reInfo.setBankTypeName(abVoucherinfoViceService.findBankTypeNameWith(abVoucherinfoVice.getId()));
		return reInfo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillPledgeRelieveVoById(aid);
	}
}
