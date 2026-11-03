package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillInvenTransferDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillInvenTransferVO;
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
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据移存
 * @author onlineGenerator
 * @date 2017-06-29 10:34:22
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_INVENTRANSFER)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_INVENTRANSFER)
@Service("abBillInvenTransferService")
@Transactional
public class AbBillInvenTransferService extends BaseService<AbBillInvenTransferVO, String>
		implements IAccebillAccbookService, IWorkFlowListener {
	@Autowired
	private AbBillInvenTransferDao abBillInvenTransferDao;
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
	private AbAccbookService abAccbookService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<AbBillInvenTransferVO, String> getEntityDao() {
		return abBillInvenTransferDao;
	}
	/**
	 * 列表显示应收票据移存信息
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillInvenTransfer(DataTablesInput input) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_TRANSFER_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_TRANSFER_QUERY,corpIds);
		DataTablesOutput<AbBillInvenTransferVO> dataTablesOutput = abBillInvenTransferDao.listAbBillInvenTransfer(input, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_TRANSFER_STATUS"
					,"AB_BRIEF_STATUS","AB_ENTER_STOCK", "AB_BILL_SOURCE","AB_BILL_POOL_STATUS","AB_BANEM");
			for (AbBillInvenTransferVO bill : listData) {
				injectValues(bill, listDictMap);
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbBillInvenTransferVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_TRANSFER_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 变更库存
		Optional.ofNullable(bill.getEnterStockNew()).ifPresent(enterStockNew -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStockNew)))
					.ifPresent(dict -> bill.setEnterStockNewName(dict.getName()));
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
		// 票据状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 票据池状态
		Optional.ofNullable(bill.getPoolStatus()).ifPresent(poolStatus -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_POOL_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(poolStatus)))
					.ifPresent(dict -> bill.setPoolStatusName(dict.getName()));
		});
	}
	/**
	 * 批量移存
	 * 
	 * @return
	 */
	public boolean batchChangeEnterStock(AbBillInvenTransferVO abBillInvenTransferVO, ICurrentUser userVo) {
		boolean flag = true;
		String[] vouIds = abBillInvenTransferVO.getVouId().split(",");
		String[] viceIds = abBillInvenTransferVO.getViceId().split(",");
		String[] accIds = abBillInvenTransferVO.getAccId().split(",");
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
		List<AbAccrecords> abList = getAbAccrecords(accIds, abBillInvenTransferVO.getEnterStock(),
				abBillInvenTransferVO.getBusinessdate());
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != accIds.length) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		flag = abLogService.batchSaveAbLog(viceIds, accIds, AccebillConstant.BRIEF_STATUS_6005,
				AccebillConstant.ABLOG_LOGTYPE_TRANSFER, AccebillConstant.LANG_LOG_BATCHTRANSF_SUCC, userVo);
		// 挂审批流程
		for (int i = 0; i < num; i++) {
			abBillNotificationService.completeMsg(abList.get(i),userVo);
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abList.get(i).getVvid());
			AbAccrecordsVO variables = new AbAccrecordsVO();
			AbVoucherinfo vouInfo = abVoucherinfoService.get(abList.get(i).getVid(), true);
			BeanUtils.copyNotNullProperties(vouInfo, variables);
			BeanUtils.copyNotNullProperties(abList.get(i), variables);
			AbVoucherinfoVice vouViceInfo = abVoucherinfoViceService.get(variables.getVvid(), true);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_INVENTRANSFER, abList.get(i).getId(), variables,
					vouViceInfo.getApplyCorpId(),userVo.getId());
		}
		return flag;
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
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(info.getVvid());
			
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
		int numES = 0 ;
		//对入池在途数据进行确认入池操作
		AbVoucherinfoVice vice=abVoucherinfoViceService.get(info.getVvid(), true);
		if(AccebillConstant.POOL_STATUS_2.equals(vice.getPoolStatus())){
			numES=abVoucherinfoViceService.updateEnterStock(info.getVvid(), info.getEnterStockNew(),AccebillConstant.POOL_STATUS_1, userVo);
		}else{
			numES=abVoucherinfoViceService.updateEnterStock(info.getVvid(), info.getEnterStockNew(), userVo);
		}
		if (numES == 0) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		int newVSNum = abAccrecordsService.updatAppPass(info.getId(), true, userVo);
		if (newVSNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		
		int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		if (reNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
				userVo);
		if (appNum != 1 || appNum != upBoxStatusNum) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
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
		abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
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
	 * 新增流水赋值封装
	 * 
	 * @param accIds
	 *            流水表ids
	 * @param abEnterStock
	 *            变更库存类型
	 * @param businessdate
	 *            业务办理日期
	 * @return
	 */
	protected List<AbAccrecords> getAbAccrecords(String[] accIds, String abEnterStock, String businessdate) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
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
			entity.setLastbillcode(ab.getBillStatus());// 保存上一笔业务类型
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
																		// 0未完成；1可用；2已结束
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态：-2已删除；-1打回；0
																	// 暂存；10提交；11-94审批中；95
																	// 已完成
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_6005);// 流水业务类型6005应收移存
			entity.setInsideSing(abVice.getInsideSing());// 内部业务标志
			entity.setEnterStockNew(abEnterStock);// 变更库存
			entity.setEnterStockOld(abVice.getEnterStock());// 把上一条所属库存保存到旧的所属库存
			entity.setBankendrsmtmk(ab.getBanendrsmtmk());// 不得转让标记：EM00可再转让；EM01
															// 不得转让
			entity.setApplyDate(new Date());// 申请日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				// 业务办理日期
				entity.setBusinessDate(sdf.parse(businessdate));
			} catch (ParseException e) {
				this.logger.error(e.toString());
				ExceptionStackUtils.getStack(e);
			}
			entity.setAbs(abVice.getAbs());// 摘要
			entity.setRmk(abVice.getRmk());// 备注
			entity.setBillFlag(ab.getBillFlag());
			abList.add(entity);
		}
		return abList;
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
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_INVENTRANSFER, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "s.id", true);
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillInvenTransferVO> dp = abBillInvenTransferDao.dataTableApprove(dataTablesInput, businessList);
		Optional.ofNullable(dp.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_TRANSFER_STATUS"
					, "AB_ENTER_STOCK", "AB_BILL_SOURCE", "AB_BANEM","AB_BILL_POOL_STATUS");
			for (AbBillInvenTransferVO bill : listData) {
				injectValues(bill, listDictMap);
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
	public AbBillInvenTransferVO getAbBillInvenTransferVOById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
		AbBillInvenTransferVO reInfo = new AbBillInvenTransferVO();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		reInfo.setViceId(abVoucherinfoVice.getId());
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_TRANSFER_STATUS"
				, "AB_ENTER_STOCK", "AB_BILL_SOURCE", "AB_BANEM");
		injectValues(reInfo, listDictMap);
		return reInfo;
	}
	@Override
	@Transactional(readOnly = true)
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return getAbBillInvenTransferVOById(aid);
	}
}
