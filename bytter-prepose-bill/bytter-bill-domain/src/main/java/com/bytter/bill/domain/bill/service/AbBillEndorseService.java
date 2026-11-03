package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.bill.domain.bill.dao.AbBillEndorseDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.payment.api.dto.NisBillDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ITaskService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 票据背书
 * @author onlineGenerator
 * @date 2017-07-05 17:53:54
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE)
@Service("abBillEndorseService")
@Transactional
public class AbBillEndorseService extends BaseService<AbVoucherinfo, String>
		implements IAccebillAccbookService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillEndorseVo>, ITaskService {
	@Autowired
	private AbBillEndorseDao abBillEndorseDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbAccbookService abAccbookService;
	@Autowired
	private AbInputBankInfoService abInputBankInfoService;
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbNisBillHeadService abNisBillHeadService;
	@Autowired
	private AbFbsItemService abFbsItemService;
	@Autowired
	private AbBillPayMiddleService abBillPayMiddleService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
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
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IDictService dictService;

	@Autowired
	private IAccebillService accebillService;

	@DubboReference(check = false)
	private INisBillService nisBillService;

	@Autowired
	private IAccebillPoolService accebillPoolService;

	@DubboReference
	private IBtBankService iBtBankService;
	public void update(AbVoucherinfo entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(AbVoucherinfo entity) {
		super.saveOrUpdate(entity);
	}
	/**
	 * 加载背书数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listabBillEndorse(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_ENDORSE_QUERY,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_ENDORSE_QUERY,corpIds);
		DataTablesOutput<AbBillEndorseVo> dataTablesOutput = abBillEndorseDao.listabBillEndorse(dataTablesInput, corpAuthList);
		addInject(dataTablesOutput);
		if(CollectionUtil.isEmpty(dataTablesOutput.getData())){
			return dataTablesOutput;
		}
		Set<String> corpIdSet = new HashSet<>();
		Set<String> banknameSet = new HashSet<>();
		for (AbBillEndorseVo datum : dataTablesOutput.getData()) {
			if(StrUtil.isNotBlank(datum.getApplyCorpId())){
				corpIdSet.add(datum.getApplyCorpId());
			}
			if(StrUtil.isNotBlank(datum.getCorpId())){
				corpIdSet.add(datum.getCorpId());
			}
			if(StrUtil.isNotBlank(datum.getBusCorpId())){
				corpIdSet.add(datum.getBusCorpId());
			}
			if(StrUtil.isNotBlank(datum.getBankName()) && StrUtil.isBlank(datum.getBankTypeName())){
				banknameSet.add(datum.getBankName());
			}
		}
		if(CollectionUtil.isNotEmpty(banknameSet)){
			List<BtBankDTO> btBankDTO = iBtBankService.getByBankNames(new ArrayList<>(banknameSet));
			if(CollectionUtil.isNotEmpty(btBankDTO)){
				Map<String, String> collect = btBankDTO.stream().collect(Collectors.toMap(BtBankDTO::getBankName, BtBankDTO::getBankTypeName));
				for (AbBillEndorseVo bill : dataTablesOutput.getData()){
					if(StrUtil.isNotBlank(bill.getBankTypeName())){
						continue;
					}
					bill.setBankTypeName(collect.get(bill.getBankName()));
				}
			}
		}
		if(corpIdSet.size() <= 0){
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillEndorseVo bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpIdName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}
		});
		return dataTablesOutput;
	}

	private void addInject(DataTablesOutput<AbBillEndorseVo> dataTablesOutput){
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BANEM","AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_AbBillEndorse_STATUS"
				,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillEndorseVo bill : listData){
				bill.injectValues(listDictMap);
			}
		});
	}

	/**
	 * 确认背书
	 *
	 * @return
	 * @throws ParseException
	 */
	public void EndorsementApplication(AbBillEndorseVo eo, String opType, ICurrentUser userVo) {
		// 获取需要修改的条数
		String[] accIds = eo.getIds().split(",");
		int len = accIds.length;
		//billTicketSplittingUtils.ticketSplitting(eo.getVids(),userVo);

		Map<String, String> resultMap = billTicketSplittingUtils.ticketSplitting(eo.getVids(),eo.getVvids(),eo.getIds(),eo.getTransMoneys(),eo.getSbbllRngStrtSnSplit(),eo.getSbbllRngEndSnSplit(),userVo);
		eo.setIds(resultMap.get("accIds"));
		eo.setVids(resultMap.get("infoIds"));
		eo.setVvids(resultMap.get("viceIds"));
		eo.setTransMoneys(resultMap.get("transMoneys"));
		// 批量修改主表box_status
		int num = abVoucherinfoViceService.batchSubmit(eo.getVvids().split(","), userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 当背书是单条数据时，则判断单位是否挂资金计划
		if (len == 1) {
			String[] vvid = eo.getVvids().split(",");
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(vvid[0]);// 票据从表信息
			if (abVoucherinfoViceService.isBudget(abVoucherinfoVice.getCorpId(), AccebillConstant.AB_BUSTYPE_RECITE)) {
				// 挂接资金计划 当系统参数为挂资金计划、背书计划科目不为空且业务为外部背书时
				// String budgetSel =
				// configService.getConfig(AccebillConstant.AB_CONFIG_TYPE,
				// AccebillConstant.AB_CONFIG_ISBUDGET).getValue();
				// String endorseItem =
				// configService.getConfig(AccebillConstant.AB_CONFIG_TYPE,
				// AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue();
				// 占用资金计划
				this.batchEngross(eo, eo.getItemCode(), eo.getItemName(), eo.getProcessMoney(), userVo);
				// 把选择的资金计划科目写入流水表
				if (!EmptyUtils.isEmptyArray(eo.getItemCode())) {
					String[] itemCodes = eo.getItemCode();
					String itemCode = "";
					for (int i = 0; i < itemCodes.length; i++) {
						itemCode += itemCodes[i];
						if (itemCodes.length > 1 && i + 1 < itemCodes.length) {
							itemCode += ",";
						}
					}
					eo.setEndorseItem(itemCode);
				}
			}
		}
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(eo, true,accIds);
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// // 内部背书为是时，如果票据类型为纸票则进行入库
		// if (eo.getInteriorApply().equals("1")) {
		// batchStorage(eo.getVids().split(","), eo.getVvids().split(","),
		// eo.getIds().split(","),eo);
		// }
		// 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
		// 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_004，流水：status=-2(已删除),这条未用退票流水valid_sign=2(已删除)
		// 日志类型
		String logType = "";
		String msg = "";
		// 提交
		if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
			logType = AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT;
			msg = AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
		}
		// 导入
		if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_IMPORTS).equals(opType)) {
			logType = AccebillConstant.ABLOG_LOGTYPE_IMPORT;
			msg = AccebillConstant.LANG_LOG_IMPORT_SUCC;
		}
		// 批量写日志
		abLogService.batchSaveAbLog(eo.getVvids().split(","), eo.getIds().split(","), AccebillConstant.BRIEF_STATUS_010,
				logType, msg, userVo);
		// 挂票据账户余额
		for (AbAccrecords abs : abList) {
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
		}
		// 挂审批流程
		for (AbAccrecords abs : abList) {
			abBillNotificationService.completeMsg(abs,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
			AbBillEndorseVo variables = new AbBillEndorseVo();
			BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
			BeanUtils.copyNotNullProperties(abs, variables);
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE, abs.getId(), variables,
					abs.getVouViceInfo().getApplyCorpId(),userVo.getId());
		}
	}
	/**
	 * Erp确认背书
	 *
	 * @param  eo
	 */
	public void endorsementApplicationErp(AbBillEndorseVo eo,ICurrentUser userVo, String[] oldAccIdArr) {
		// 获取需要修改的条数
		int len = eo.getIds().split(",").length;
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(eo, false,oldAccIdArr);
		int num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量审批通过完成以后修改当前流水的有效状态为2
		num = abAccrecordsService.batchAppPass(eo.getIds().split(","), true, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(eo.getInteriorApply())) {
			AbVoucherinfo abVoucherinfo = null;
			for (AbAccrecords accrecords : abList) {
				abVoucherinfo = abVoucherinfoService.get(accrecords.getVid());
				if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))) {
					// 入库
					batchStorage(accrecords.getId(), userVo,null);
				} else {
					// 修改从表的状态valid_sign=2
					num = abVoucherinfoViceService.updateValidSign(accrecords.getVvid(),
							AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
					if (num != 1) {
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
					}
				}
			}
		} else {
			for (AbAccrecords accrecords : abList) {
				// 修改从表的状态valid_sign=2
				abVoucherinfoViceService.batchUpdateValidSign(accrecords.getVvid().split(","),
						AccebillConstant.AB_BILL_VALID_SIGN_2, AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
			}
		}
		// 挂票据账户余额
		for (AbAccrecords abs : abList) {
			abBillNotificationService.sendMsg(abs.getId());
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());// 从表清除打回标记
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
			//记账处理，由于ERP接口过来的userVO为空，记账处理有用到，暂时不处理记账
			/*if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(abs);
				if(isAccboo){
					if(abAccbookService.isNotAutoAccbook()){
						abAccbookService.addAbAccbookInfo(abs,"0");
					}else{
						abAccbookService.accbookVoucher(abs, userVo);// 背书--付款接口调用票据记账处理
					}
				}
			}*/
			abLogService.saveAbLog(abs.getVvid(), abs.getId(), abs.getBriefId(),AccebillConstant.BRIEF_STATUS_010, AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
		}
	}
	/**
	 * 没有审批流程直接审批通过
	 */
	public void approvalNo(AbBillEndorseVo eo, ICurrentUser userVo, String[] oldAccArr) {
		abBillEndorseDao.flush();
		if(eo.getTransMoneys() != null){
			Map<String, String> resultMap = billTicketSplittingUtils.ticketSplitting(eo.getVids(),eo.getVvids(),eo.getIds(),eo.getTransMoneys(),null,null,userVo);
			eo.setIds(resultMap.get("accIds"));
			eo.setVids(resultMap.get("infoIds"));
			eo.setVvids(resultMap.get("viceIds"));
			eo.setTransMoneys(resultMap.get("transMoneys"));
		}


		// 获取需要修改的条数
		int len = eo.getIds().split(",").length;
		// 批量修改主表box_status（有些时候，原主表状态已经属于11，所以在此不判断有无更新到）
		// abVoucherinfoService.batchUpdateBoxStatus(eo.getVids().split(","),
		// AccebillConstant.AB_BOX_STATUS_Y);
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(eo, false,oldAccArr == null ? eo.getIds().split(",") : oldAccArr);
		int num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量审批通过完成以后修改当前流水的有效状态为2
		num = abAccrecordsService.batchAppPass(eo.getIds().split(","), true, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 都为内部背书进行处理
		for (AbAccrecords accrecords : abList) {
			batchStorage(accrecords.getId(), userVo,null);
		}
		// 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
		// 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_004，流水：status=-2(已删除),这条未用退票流水valid_sign=2(已删除)
		abLogService.batchSaveAbLog(eo.getVvids().split(","), eo.getIds().split(","), AccebillConstant.BRIEF_STATUS_010,
				AccebillConstant.ABLOG_LOGTYPE_UPDATE, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
		// 挂票据账户余额
		for (AbAccrecords abs : abList) {
			//批量拆分新业务批量处理
			if("2".equals(abs.getCopyFlag())){
				billTicketSplittingUtils.splitConsentOp(abs);
			}
			String billStatusOld = AccebillConstant.AB_BILL_STATUS_100006;
			String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
			String ebillBlCrclIndc = "EM00".equals(abs.getBankendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303;
			//设置离线票据的票据状态和流通标志，并返回从票数据
			AbAccebillUtils.setOffLineVoucherViceBillStatus(abVoucherinfoService,abs.getVid(),abs.getVvid()
					,abs.getSendSign(),abVoucherinfoViceService,billStatusOld, billStatusNew, ebillBlCrclIndc);

			abBillNotificationService.completeMsg(abs,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());// 从表清除打回标记
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);
		}
	}
	/**
	 * 占用预算
	 *
	 * @param entity
	 *            票据背书
	 * @param itemCode
	 *            资金计划科目
	 * @param itemName
	 *            资金计划科目名称
	 * @param processMoney
	 *            资金计划执行金额
	 */
	private void batchEngross(AbBillEndorseVo entity, String[] itemCode, String[] itemName, BigDecimal[] processMoney,
			ICurrentUser userVo) {
		if (!EmptyUtils.isEmptyArray(itemCode)) {
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(entity.getVvids());// 票据从表信息
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			for (int i = 0; i < itemCode.length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				//TODO 添加内转判断标识 innerSign  1-内转 0-非内转 开出方式对内，同一个网点下的成员单位 判定为内转
				if(AccebillConstant.AB_INSIDE_SING_Y.equals (entity.getInteriorApply())&&
						abCorpService.isTheSameNetByCorpId(abVoucherinfoVice.getCorpId(),entity.getPledgeeCorpid())){
					fbsBudgetProcessDTO.setInnerSign ("1");
				}else{
					fbsBudgetProcessDTO.setInnerSign ("0");
				}
				fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
				fbsBudgetProcessDTO.setBillId(entity.getVvids());// 业务单据号
				fbsBudgetProcessDTO.setBillNo(entity.getOrigNo());//票据号码
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);// 科目代码
				fbsBudgetProcessDTO.setItemName(itemName[i]);// 科目名称
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);// 执行金额
				//资金计划背书添加折合本位币金额与利率
				fbsBudgetProcessDTO.setProcessDate(entity.getBusinessDate());// 执行日期
				fbsBudgetProcessDTO.setCorpId(abVoucherinfoVice.getCorpId());// 单位id
				fbsBudgetProcessDTO.setProcessCurMoney(null!=entity && null!=entity.getProcessCurMoney()?entity.getProcessCurMoney()[i]:new BigDecimal(0.00));
				fbsBudgetProcessDTO.setExchangeRate(null!=entity && null!=entity.getExchangeRate()?entity.getExchangeRate()[i]:new BigDecimal(0.00));
//				BtBankAccDTO btBankDTO = this.abBtBankAccService.getBankAccInfo(abVoucherinfoVice.getBankAcc());
//				if (!EmptyUtils.isEmpty(btBankDTO)) {
//					if (!EmptyUtils.isEmptyList(btBankDTO.getCurList())) {
//						fbsBudgetProcessDTO.setCurId(btBankDTO.getCurList().get(0));// 币别
//					}
//				}
				fbsBudgetProcessDTO.setCurId(abVoucherinfoViceService.getCurIdByCNY());// 币别取人民币
				fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_ENDORSE);// 执行业务名称
				fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
				fbsBudgetProcessDTO.setCreateBy(userVo.getLoginName());
				fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
				list.add(fbsBudgetProcessDTO);
			}
			// 修改时，先删除之前的计划执行数据再保存
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_ENDORSE,
					BudgetConstant.PROCESS_SYSNAME_AB, entity.getVvids());
			this.abFbsBudgetProcessService.writeBudgetProcess(list,userVo);
		}
	}
	/**
	 * 占用预算 审批通过
	 *
	 * @param vvid
	 *            从表Id
	 * @param isPassOK
	 *            审批状态
	 */
	public void approvalBudget(String vvid, boolean isPassOK,ICurrentUser userVo) {
		if (isPassOK) {
			// 修改资金计划为占用状态
			this.abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_ENDORSE,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid, BudgetConstant.STATUE_CHECK,userVo);
		} else {
			// 删除资金计划
			this.abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_ENDORSE,
					BudgetConstant.PROCESS_SYSNAME_AB, vvid);
		}
	}
	/**
	 * 票据批量背书--付款接口调用
	 */
	public void batchEndorse(AbBillEndorseVo eo, boolean insideSing, ICurrentUser userVo) {
		// 获取需要修改的条数
		String[] oldAccIdArr = eo.getIds().split(",");
		int len = oldAccIdArr.length;

		Map<String, String> resultMap = billTicketSplittingUtils.ticketSplitting(eo.getIds(), eo.getTransMoneys(), eo.getPayId(), userVo);
		eo.setIds(resultMap.get("accIds"));
		eo.setVids(resultMap.get("infoIds"));
		eo.setVvids(resultMap.get("viceIds"));
		eo.setTransMoneys(resultMap.get("transMoneys"));

		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(eo, false,oldAccIdArr);
		int num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 批量审批通过完成以后修改当前流水的有效状态为2
		num = abAccrecordsService.batchAppPass(eo.getIds().split(","), true, userVo);

		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
//		if (insideSing) {
//			AbVoucherinfo abVoucherinfo = null;
//			for (AbAccrecords accrecords : abList) {
//				abVoucherinfo = abVoucherinfoService.get(accrecords.getVid());
//				if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))) {
//					// 入库
//					batchStorage(accrecords.getId(), userVo,null);
//				}
////				else {
////					// 修改从表的状态valid_sign=2
////					num = abVoucherinfoViceService.updateValidSign(accrecords.getVvid(),
////							AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
////					if (num != 1) {
////						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
////					}
////				}
//			}
//		}
//		else {
//			// 修改从表的状态valid_sign=2
//			num = abVoucherinfoViceService.batchUpdateValidSign(eo.getVvids().split(","),
//					AccebillConstant.AB_BILL_VALID_SIGN_2, AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
//			// if (num != len) {
//			// throw new
//			// BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//			// }
//		}
		StringBuilder billIds=new StringBuilder();
		AbVoucherinfo abVoucherinfo = null;
		for (AbAccrecords abs : abList) {
			//线下票据
			if(AccebillConstant.AB_BILL_SEND_SIGN_N.equals(abs.getSendSign())
					&& AccebillConstant.AB_BILL_OPT_STATUS19.equals(abs.getOptStatus())){
				//新票据拆分请求确认处理
				NewDraftDealMethod.newDraftSplitAck(billBankDao,abs.getId());
			}
			//批量拆分新业务批量处理
			if("2".equals(abs.getCopyFlag())){
				billTicketSplittingUtils.splitConsentOp(abs);
			}
			//子票入库处理
			if (insideSing) {
				abVoucherinfo = abVoucherinfoService.get(abs.getVid());
				if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))) {
					// 入库
					batchStorage(abs.getId(), userVo,null);
				}
			}
			if(billIds.length()>0){
				billIds.append(CommonConstant.COMMA_SPLIT);
			}
			billIds.append(abs.getId());
			if(!AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(abs.getSendSign())) {
				// 修改从表的状态valid_sign=2
				num = abVoucherinfoViceService.updateValidSign(abs.getVvid(),
						AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
			}
			//消息操作
			abBillNotificationService.completeMsg(abs,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());// 从表清除打回标记
			//记账操作
			if(abAccbookService.isAccbook()){
				boolean isAccboo=abAccbookService.checkBuIsAccbook(abs);
				if(isAccboo){
					if(abAccbookService.isNotAutoAccbook()){
						abAccbookService.addAbAccbookInfo(abs,"0");
					}else{
						abAccbookService.accbookVoucher(abs, userVo);// 背书--付款接口调用票据记账处理
					}
				}
			}
			// 挂票据账户余额
			abBillAccBalService.setBankAccBillBal(abs.getId(), userVo);//AB_BILL_ACC_BAL表
		}
		eo.setBillIds(billIds.toString());
		abLogService.batchSaveAbLog(eo.getVvids().split(","), eo.getIds().split(","), AccebillConstant.BRIEF_STATUS_010,
				AccebillConstant.ABLOG_LOGTYPE_UPDATE, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
	}
	/**
	 * 审批操作
	 *
	 * @param buId
	 *            主表id数组
	 * @param isPassOK
	 *            通过标识
	 * @param appMsg
	 *            审批信息
	 */
	public void approvalPass(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(buId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbAccrecords info = abAccrecordsService.get(buId, true);
		if (info == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		
        if (isPassOK) {
			String billStatusOld = AccebillConstant.AB_BILL_STATUS_100006;
			String billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
			String ebillBlCrclIndc = "EM00".equals(info.getBankendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303;
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
					abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_010, logType, appMsg, userVo);
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
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busOverOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo){
		// 批量修改票据主表信息(修改处理标记为处理完成)
		int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
		int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 通过则上一流水valid_sign=2 失败修改BackBriefId
		String[] lastids = new String[] { info.getLastid() };
		num = abAccrecordsService.batchAppPass(lastids, true, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}

		//批量拆分新业务批量处理
		if("2".equals(info.getCopyFlag())){
			billTicketSplittingUtils.splitConsentOp(info);
		}

		// 内部背书为是时，如果票据类型为纸票则进行入库
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(info.getInsideSing()))) {
			batchStorage(info.getId(), userVo,null);
		}
		// 判断流水的资金计划科目是否为空，空则没挂资金计划
		if (!EmptyUtils.isBlank(info.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(info.getVvid(), true,userVo);
		}
		// 挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_010, logType, appMsg, userVo);
	}
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
	public void busRepulseOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo){
		// 批量修改票据主表信息(修改处理标记为处理完成)
		int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 如果是电票，则修改指令状态为打回-2
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(info.getVid());
		if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))) {
			info.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS_2);
			abAccrecordsService.updateEntity(info);
		}
		// 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
		int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
		if (appNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 通过则上一流水valid_sign=2 失败修改BackBriefId
		int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
		if (reNum != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 判断流水的资金计划科目是否为空，空则没挂资金计划
		if (!EmptyUtils.isBlank(info.getBudgetCode())) {
			// 资金计划修改
			approvalBudget(info.getVvid(), false,userVo);
		}
		// 挂票据账户余额
		abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
		abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_010, logType, appMsg, userVo);

		AbAccrecords abAccrecords = abAccrecordsService.get(info.getId(),true);
		if("1".equals(abAccrecords.getSplitFlg())){
			//批量付票业务进行处理
			if("2".equals(abAccrecords.getCopyFlag())){
				billTicketSplittingUtils.splitRepulseOp(abAccrecords);
			}else {
				//新票据拆分后打回处理
				NewDraftDealMethod.draftMerge("1", info.getVid(), billBankDao, billAccrecordsDao);
			}
		}
	}

	protected List<AbAccrecords> getAbAccrecords(AbBillEndorseVo eo, boolean flag,String[] oldAccIdArr) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		// 流水表id数组
		String[] aids = eo.getIds().split(",");
		for (int i = 0; i < aids.length; i++) {
			String id = aids[i];
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
				entity.setSplitFlg(oldAccIdArr[i].equals(aids[i]) ? "0" : "1");
				//拆分交易的话设置此字段为未拆分之前的id
				entity.setOldAccId("1".equals(entity.getSplitFlg()) ? oldAccIdArr[i] : null);
			}

			// 设置数据来源信息
			if (StringUtils.isNotEmpty(eo.getBillSource())) {
				entity.setBillSource(eo.getBillSource());
			}
			// 票据主表ID
			entity.setVid(ab.getId());
			// 票据从表ID
			entity.setVvid(abVice.getId());
			entity.setVouInfo(ab);
			entity.setVouViceInfo(abVice);
			// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
			if (flag) {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			} else {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
				entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			}
			// 业务类型
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_010);
			entity.setLastid(id);
			entity.setLastbillcode(ab.getBillStatus());
			// 申请日期
			entity.setBusinessDate(eo.getBusinessDate());// 业务归属日期
			entity.setApplyDate(new Date());// 申请日期
			if(StringUtils.isEmpty(abVice.getBankAcc())){
				entity.setReqAcctId(abAccrecords.getReqAcctId());// 业务申请人账号
				entity.setReqNm(abAccrecords.getReqNm());// 业务申请人名称
			}else{
				entity.setReqAcctId(abVice.getBankAcc());// 业务申请人账号
				entity.setReqNm(abVice.getBankName());// 业务申请人名称
			}
			CorpDTO corpInfo=abCorpService.get(abVice.getCorpId());
			if(StringUtils.isEmpty(entity.getReqNm()) && null != corpInfo){
				entity.setReqNm(corpInfo.getName());// 业务申请人名称
			}
			entity.setRecNm(eo.getPledgeeName());// 业务接收人名称
			entity.setRecAcctId(eo.getPledgeeAcc());// 业务接收人账号
			entity.setRecAcctsvcr(eo.getPledgeeBankAcc());// 业务接收人开户行号
			entity.setRecDghType(eo.getRecDghType());// 业务接收人识别类型
			entity.setReqSocNo(abVice.getBlHoldUnnIdNo());// 业务申请人社会统一信用代码
			entity.setBillFlag(ab.getBillFlag());
			entity.setRmk(eo.getRmk());
			entity.setInsideSing(Integer.parseInt(eo.getInteriorApply()));
			// 是否内部背书
			if(AccebillConstant.AB_INSIDE_SING_Y.equals(eo.getInteriorApply())){
				// 如是，通过单位id就可以获取社会统一信用代码
				if(eo.getPledgeeCorpid() != null){
					entity.setAdr(eo.getPledgeeCorpid());
				}else {
					BtBankAccDTO btBankAccDTO = abBtBankAccService.getBankAccInfo(eo.getPledgeeAcc());
					if(btBankAccDTO != null && btBankAccDTO.getCorpId() != null){
						CorpDTO c = abCorpService.get(btBankAccDTO.getCorpId());
						if (null != c) {
							entity.setRecSocNo(c.getSocCode());// 业务接收人社会统一信用代码
						}
					}
				}
			} else {
				// 如否，则是客商账号
				entity.setRecSocNo(eo.getRecSocNo());
			}
			if (StringUtils.isEmpty(eo.getBankendrSmtmk())) {
				entity.setBankendrsmtmk(ab.getBanendrsmtmk());
			} else if ("1".equals(eo.getBankendrSmtmk())
					||AccebillConstant.AB_BANEM_Y.equals(eo.getBankendrSmtmk())) {
				entity.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);
			} else {
				entity.setBankendrsmtmk(AccebillConstant.AB_BANEM_N);
			}
			entity.setBudgetCode(eo.getEndorseItem());// 预算科目
			//领票人
			entity.setTrader(eo.getTrader());
			//保存收票id
			//
			entity.setRecid("");
			abList.add(entity);
		}
		return abList;
	}
	/**
	 * 批量入库 票据补录完成后对内部自开的票据做入库处理(新增应收票据暂存数据)
	 *
	 * @return
	 */
	protected boolean batchStorage(String aid, ICurrentUser userVo,String onlineTransferOfflineFlag) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid,true);// 票据流水
		if (abAccrecords == null) {
			return false;
		}
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid(),true);// 票据主信息
		if (abVoucherinfo == null) {
			return false;
		}
		BtBankAccDTO recAcc = null;
		BtBankAccDTO reqAcc = null;
		boolean flagElect=false;//是否为离线电票
		//电票
		if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag())) ){
			recAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getRecAcctId());
			reqAcc=abAccrecordsService.getBtBankAccInfo(abAccrecords.getReqAcctId());
			//业务申请人与接收人不能为空，为内部的离线电票
			if((recAcc!=null && !BtBankAccConstant.CIRCULATIONMODE_1.equals(recAcc.getCirculationMode()))
					&& (reqAcc!=null && !BtBankAccConstant.CIRCULATIONMODE_1.equals(reqAcc.getCirculationMode()))){
				flagElect =  true;
			}
		}
		//内部背书在线账户转给离线账户入库标记不为空则入库
		if(onlineTransferOfflineFlag != null && AccebillConstant.INSIDE_ENDORSE_AUTO_REG_SIGN_Y.equals(onlineTransferOfflineFlag)){
			flagElect =  true;
		}
		/** 对内部背书的纸票入库 或者业务申请人与接收人不能为空，为内部的离线电票入库*/
		if (abVoucherinfo != null
				&& (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))
				|| (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag())) && flagElect))) {

			//不得转让标记EM00可再转让EM01不得转让
			abVoucherinfo.setBanendrsmtmk(abAccrecords.getBankendrsmtmk());
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(),true);// 从表信息
			AbVoucherinfoVice abVoucherinfoViceNew = getAbVoucherinfoVice(abVoucherinfoVice, abAccrecords);
			AbAccrecords abAccrecordsNew = getAbAccrecordsNew(abVoucherinfo, abAccrecords);
			abAccrecordsNew.setCnAccrecords(abVoucherinfoViceNew.getCnAccrecords());// 前手
			// 修改旧的从表的状态valid_sign=2
			abVoucherinfoViceService.updateValidSign(abAccrecords.getVvid(), "2", userVo);
			AbReceiveBillVO abReceiveBillVO = new AbReceiveBillVO();
			abReceiveBillVO.setAbVoucherInfo(abVoucherinfo);
			abReceiveBillVO.setAbVoucherinfoVice(abVoucherinfoViceNew);
			abReceiveBillVO.setAbAccrecords(abAccrecordsNew);
			/** 票据划拨完毕后，票据状态改为有效状态，不再锁定票据从表 */
			boolean flag = false;
			if (StringUtils.isNotEmpty(abAccrecords.getLastid())) {
				AbAccrecords abAccrecordsLast = abAccrecordsService.get(abAccrecords.getLastid(),true);// 票据流水
				if (abAccrecordsLast == null || AccebillConstant.BRIEF_STATUS_6201.equals(abAccrecordsLast.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_6202.equals(abAccrecordsLast.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecordsLast.getBriefId())) {
					flag = true;
				}
			}
			if (flag) {
				abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, AccebillConstant.BILL_OPERATE_SUBMIT,
						userVo);
				// 背书流水变为作废状态
				abAccrecordsService.updatAppPass(abAccrecords.getId(), true, userVo);
			} else {
				abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, AccebillConstant.BILL_OPERATE_SAVE, userVo);
				abBillNotificationService.sendMsg(abReceiveBillVO.getId());
				// 批量修改从表box_status = 10 (入库完成后票据为暂存状态,需要到收票登记提交故需锁定)
				abVoucherinfoViceService.updateBoxStatus(abVoucherinfoViceNew.getId(), AccebillConstant.AB_BOX_STATUS_N,
						userVo);
			}
		}
		return true;
	}
	/**
	 * 根据票据主表和流水表生成从表入库数据
	 */
	protected AbVoucherinfoVice getAbVoucherinfoVice(AbVoucherinfoVice abVoucherinfoViceOld,
			AbAccrecords abAccrecords) {
		AbVoucherinfoVice abVoucherinfoVice = new AbVoucherinfoVice();
		// abVoucherinfoVice.setCorpId(abVoucherinfo.getPayee());//
		// 持票单位(收票人变为持票单位)
		BtBankAccDTO btBankAccDTO = abAccrecordsService.getBtBankAccInfo(abAccrecords.getRecAcctId());// 接收人账号
		if(btBankAccDTO!=null){
			abVoucherinfoVice.setCorpId(btBankAccDTO.getCorpId());// 持票单位(被背书人变为持票单位)--通过账号获取单位id
			abVoucherinfoVice.setBankAcc(btBankAccDTO.getBankAcc());// 持票单位账号
			abVoucherinfoVice.setBankCode(btBankAccDTO.getBankCode());// 持票单位行号
		}else{
			CorpDTO corpInfo=abAccrecordsService.getCorpInfo(abAccrecords.getRecAcctId(),abAccrecords.getRecNm());
			if(corpInfo!=null){
				abVoucherinfoVice.setCorpId(corpInfo.getId());
			}
		}
		//设置申请单位和业务单位
		if(StringUtils.isNotEmpty(abVoucherinfoVice.getCorpId())){
			abVoucherinfoVice.setApplyCorpId(abVoucherinfoVice.getCorpId());
		}
		abVoucherinfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);// 应收票据
		abVoucherinfoVice.setEnterStock(abVoucherinfoViceOld.getEnterStock());// 所属库存:10单位库存11中心代管12银行托管
		abVoucherinfoVice.setBillSource(AccebillConstant.AB_BILL_SOURCE_ENDORSE);// 11
		if(StringUtils.isEmpty(abAccrecords.getReqNm())){
			BtBankAccDTO btBankAccDTOOld = abAccrecordsService.getBtBankAccInfo(abAccrecords.getReqAcctId());// 申请人账号															// 背书
			if(btBankAccDTOOld!=null){
				abVoucherinfoVice.setCnAccrecords(btBankAccDTOOld.getAccName());// 前手:背书申请人账号名称
			}
		}else{
			abVoucherinfoVice.setCnAccrecords(abAccrecords.getReqNm());// 前手:背书申请人账号名称
		}
//		abVoucherinfoVice.setAbs(abAccrecords.getAbs());
//		abVoucherinfoVice.setRmk(abAccrecords.getRmk());
		abVoucherinfoVice.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_Y));// 内部业务标识
		abVoucherinfoVice.setIsSetCreateBy(false);
		return abVoucherinfoVice;
	}
//
//	/**
//	 * 根据账号获取账户信息
//	 *
//	 * @return
//	 */
//	public BtBankAccDTO getBtBankAccDTO(String bankAcc) {
//		BtBankAccDTO bankAccDTO = null;
//		BtBankAccDTO params = new BtBankAccDTO();
//		params.setBankAcc(bankAcc);
//		params.setElectricBill("1");
//		List<BtBankAccDTO> bankaccInfoList = bankAccService.getBankAccList(params);
//		if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
//			bankAccDTO = bankaccInfoList.get(0);
//		} else {
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
//		}
//		return bankAccDTO;
//	}
	/**
	 * 根据票据主表和流水表生成流水入库数据
	 */
	protected AbAccrecords getAbAccrecordsNew(AbVoucherinfo abVoucherinfo, AbAccrecords abAccrecords) {
		AbAccrecords abAccrecordsNew = new AbAccrecords();
//		Date tadyDate = DateUtils.getDateYMD();
		abAccrecordsNew.setBusinessDate(new Date());// 业务归属日期
		abAccrecordsNew.setBankendrsmtmk(abVoucherinfo.getBanendrsmtmk());// 不得转让标记：EM00
																			// 可再转让；EM01
																			// 不得转让
//		abAccrecordsNew.setAbs(abAccrecords.getAbs());
//		abAccrecordsNew.setRmk(abAccrecords.getRmk());
		abAccrecordsNew.setBillFlag(abAccrecords.getBillFlag());// 流通介质
		abAccrecordsNew.setLastid(abAccrecords.getId());// 上一笔流水
		abAccrecordsNew.setLastbillcode(abVoucherinfo.getBillStatus());// 上一笔业务类型
		// 设置票据来源
		if (StringUtils.isNotEmpty(abAccrecords.getBillSource())) {
			abAccrecordsNew.setBillSource(abAccrecords.getBillSource());
		} else {
			abAccrecordsNew.setBillSource(AccebillConstant.AB_BILL_SOURCE_ENDORSE);// 11
		}
		if(!(AccebillConstant.AB_BILL_SOURCE15.equals(abAccrecords.getBillSource())
				||AccebillConstant.AB_BILL_SOURCE16.equals(abAccrecords.getBillSource())
				||AccebillConstant.AB_BILL_SOURCE17.equals(abAccrecords.getBillSource()))){
			abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);
		}
		abAccrecordsNew.setInsideSing(abAccrecords.getInsideSing());
		abAccrecordsNew.setReqNm(abAccrecords.getRecNm());// 业务申请人账号
		abAccrecordsNew.setReqAcctId(abAccrecords.getRecAcctId());// 业务申请人账号
		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());
		abAccrecordsNew.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
		abAccrecordsNew.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
		return abAccrecordsNew;
	}
	@Override
	public BaseDao<AbVoucherinfo, String> getEntityDao() {
		return null;
	}
	/**
	 * 审批回调方法
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		this.approvalPass(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
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
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ENDORSE, dataTablesInput);
		// workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
		List<String> businessList=workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbBillEndorseVo> dp = abBillEndorseDao.dataTableApprove(dataTablesInput, businessList);
		addInject(dp);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 获票据信息
	 *
	 * @param id
	 * @return
	 */
	public AbBillEndorseVo getAbBillEndorseVoById(String id) {
		AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
		AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(accrecordsInfo.getVvid());// 票据从表信息
		AbBillEndorseVo reInfo = new AbBillEndorseVo();
		BeanUtils.copyNotNullProperties(vouInfo, reInfo);
		BeanUtils.copyNotNullProperties(abVoucherinfoVice, reInfo);
		BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
		reInfo.setBankendrSmtmk(accrecordsInfo.getBankendrsmtmk());
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
		// 根据银行账号获取银行信息
		/*
		 * BtBankAccDTO bankAccDTO =
		 * getBtBankAccDTO(accrecordsInfo.getReqAcctId()); if(bankAccDTO!=null){
		 * reInfo.setAccName(bankAccDTO.getAccName()); }
		 */
		// 根据银行行号得到银行账号信息
		InputBankInfoDTO bankInfo = abInputBankInfoService.getByBankCode(accrecordsInfo.getRecAcctsvcr());
		if (bankInfo != null) {
			reInfo.setPledgeeBankName(bankInfo.getBankName());
		}
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(
				accrecordsInfo.getInsideSing() != null ? String.valueOf(accrecordsInfo.getInsideSing()) : null)) {
			// 根据银行账号获取银行信息
			BtBankAccDTO recBankAcc = abAccrecordsService.getBtBankAccInfo(accrecordsInfo.getRecAcctId());
			if (recBankAcc != null) {
				if (StringUtils.isEmpty(reInfo.getPledgeeBankName())) {
					reInfo.setPledgeeBankName(recBankAcc.getBankName());
				}
				// 根据银行账号获取单位信息
				CorpDTO recCorpInfo = abCorpService.get(recBankAcc.getCorpId());
				if (recCorpInfo != null) {
					reInfo.setEndorseCorpName("("+recCorpInfo.getCode()+")"+recCorpInfo.getName());
				}
			}else if(StringUtils.isNotEmpty(accrecordsInfo.getAdr())){//背书业务时adr为内部单位id
				// 根据银行账号获取单位信息
				CorpDTO recCorpInfo = abCorpService.get(accrecordsInfo.getAdr());
				if (recCorpInfo != null) {
					reInfo.setEndorseCorpName("("+recCorpInfo.getCode()+")"+recCorpInfo.getName());
				}
			}else{
				reInfo.setEndorseCorpName(accrecordsInfo.getRecNm());
			}
		}
		inject(reInfo);
		return reInfo;
	}


	private void inject(AbBillEndorseVo bill){
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG","AB_BANEM","AB_BILL_TYPE","AB_BILL_FLAG","AB_BILL_AbBillEndorse_STATUS"
				,"AB_ENTER_STOCK","AB_BANEM","AB_BILL_SEND_SIGN","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
		bill.injectValues(listDictMap);
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<AbBillEndorseVo> list, ICurrentUser userVo) {
		if (list != null && !list.isEmpty()) {
			for (AbBillEndorseVo vo : list) {
				vo.setIds(vo.getId());
				vo.setVvids(vo.getVvid());
				this.EndorsementApplication(vo, AccebillConstant.BILL_OPERATE_IMPORTS, userVo);
			}
		}
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillEndorseVo obj,Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<AbBillEndorseVo> list,Object... user) {
		HashMap<String, CorpDTO> corpCodeMap = new HashMap<>();
		HashMap<String, CorpDTO> corpNameMap = new HashMap<>();
		String userId="";
		if(user[0]!=null){
			ICurrentUser userInfo=(ICurrentUser)user[0];
			userId=userInfo.getId();
		}
		// 查询权限:资源下的单位ID（合并所用账号单位）
		List<String> corpAuthList=abCorpService.getUserCorpIdsByUseAccount(AccebillConstant.AB_BILL_RESOURCE_ENDORSE_QUERY,userId);
		Map<String, BtBankAccDTO> bankAccInfoMap = new HashMap<>();
		Map<String, InputBankInfoDTO> inputBankInfoDTOMap = new HashMap<>();
		//同一批背书子票区间是否重叠判断
		Map<String, List<AbBillEndorseVo>> splitSbbllRngMap = new HashMap<>();
		//是否挂预算
		boolean budgetSel =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).toBoolean();
		//单位计划上报集合
		HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
		//计划科目
		HashMap<String, FbsItemDTO> fbsItemMap = new HashMap<>();
		if (list != null && !list.isEmpty()) {
			for (AbBillEndorseVo vo : list) {
				if(StrUtil.isEmpty(vo.getSbbllRngStrtSn())){
					vo.setSbbllRngStrtSn("0");
				}
				if(StrUtil.isEmpty(vo.getSbbllRngEndSn())){
					vo.setSbbllRngEndSn("0");
				}
				CorpDTO corpByCode = null;
				CorpDTO corpByName = null;
				List<String> errorMsg = new ArrayList<String>();
				if (corpCodeMap.containsKey(vo.getCorpId())) {
					corpByCode = corpCodeMap.get(vo.getCorpId());
				} else {
					// 根据所属单位代码获取单位信息
					corpByCode = abCorpService.getCorpByCode(vo.getCorpId());
					corpCodeMap.put(vo.getCorpId(), corpByCode);
				}
				if (corpByCode != null) {
					if (StringUtils.isNotEmpty(vo.getCorpIdName())) {
						if (corpNameMap.containsKey(vo.getCorpIdName())) {
							corpByName = corpNameMap.get(vo.getCorpIdName());
						} else {
							corpByName = abCorpService.getCorpIdByName(vo.getCorpIdName());
							corpNameMap.put(vo.getCorpIdName(), corpByName);
						}
						// 判断所属单位代码与所填的所属单位名称匹配不匹配
						if (corpByName==null || !corpByCode.getId().equals(corpByName.getId())) {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPNAME_ISNOT);
						}
					}else{
						vo.setCorpIdName(corpByCode.getName());
					}
					// 根据单位代码、票据号码查询背书数据
					List<AbBillEndorseVo> abBillEndorseVoList = abBillEndorseDao.excelListAbBillEndorse(vo,
							corpAuthList);
					if (abBillEndorseVoList != null && !abBillEndorseVoList.isEmpty()) {
						AbBillEndorseVo abBillEndorseVo = abBillEndorseVoList.get(0);
						BeanUtils.copyNotNullProperties(abBillEndorseVo, vo,"transMoney","transMoneys");
						vo.setVids(abBillEndorseVo.getVid());
						vo.setVvids(abBillEndorseVo.getVvid());
						vo.setIds(abBillEndorseVo.getId());
						vo.setTransMoneys(vo.getTransMoney().toString());
						vo.setCorpId(corpByCode.getCode());
						vo.setBillMoney(abBillEndorseVo.getBillMoney());
						vo.setBillType(abBillEndorseVo.getBillType());
						vo.setBillFlag(abBillEndorseVo.getBillFlag());
						vo.setStartDate(abBillEndorseVo.getStartDate());
						vo.setEndDate(abBillEndorseVo.getEndDate());
						if (vo.getBusinessDate() != null) {// 判断业务办理日期
							// 业务办理日期必须大于等于出票日期,小于到期日期!
							if (vo.getBusinessDate().getTime() < abBillEndorseVo.getStartDate().getTime()
									|| vo.getBusinessDate().getTime() >= abBillEndorseVo.getEndDate().getTime()) {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CHECKBUSINESSDATE);
							}
						} else {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BUSINESSDATE_NULL);
						}
						//不允许拆票的票据交易金额需要与票据金额一样，允许拆分的票据交易金额不能大于票据金额
						if((!"1".equals(abBillEndorseVo.getBlgrdbagcrclPrmtInd()) && (vo.getBillMoney().compareTo(vo.getTransMoney()) != 0) )
								|| (vo.getBillMoney().compareTo(vo.getTransMoney()) < 0)){
							errorMsg.add(AccebillConstant.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
						}
						// 验证是否可转让值正不正确
						if ("可再转让".equals(vo.getBankendrSmtmkName())) {
							vo.setBankendrSmtmk(AccebillConstant.AB_BANEM_Y);
						} else if ("不得转让".equals(vo.getBankendrSmtmkName())) {
							vo.setBankendrSmtmk(AccebillConstant.AB_BANEM_N);
						} else {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BANKENDRSMTMK_ERROR);
						}
						// 判断是否内部背书值对不对
						if (!"是".equals(vo.getInteriorApplyName()) && !"否".equals(vo.getInteriorApplyName())) {
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_ERROR);
						} else {
							if ("是".equals(vo.getInteriorApplyName())) {
								vo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_Y);
							} else {
								vo.setInteriorApply(AccebillConstant.AB_INSIDE_SING_N);
							}
							// 判断是否为内部背书
							if (AccebillConstant.AB_INSIDE_SING_Y.equals(vo.getInteriorApply())) {
								if (StringUtils.isNotEmpty(vo.getEndorseCorpCode())) {// 判断内部单位
									CorpDTO corp = null;
									if (corpCodeMap.containsKey(vo.getEndorseCorpCode())) {
										corp = corpCodeMap.get(vo.getEndorseCorpCode());
									} else {
										// 根据内部单位代码获取单位信息
										corp = abCorpService.getCorpByCode(vo.getEndorseCorpCode());
										corpCodeMap.put(vo.getEndorseCorpCode(),corp);
									}
									if (corp == null) {
										errorMsg.add(
												AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_CORPNAME_ISNOTEXIST);
									} else {
										vo.setPledgeeCorpid(corp.getId());
//										// 内部背书时被背书人单位不能跟选择的数据是同一单位 不需要了支持同名背书
//										if (vo.getCorpId().equals(vo.getEndorseCorpCode())) {
//											errorMsg.add(
//													AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_CORP_NOTEQUAL);
//										}
										if (StringUtils.isNotEmpty(vo.getEndorseCorpName())) {
											CorpDTO corpByEName = null;
											if (corpNameMap.containsKey(vo.getEndorseCorpName())) {
												corpByEName = corpNameMap.get(vo.getEndorseCorpName());
											} else {
												// 根据内部单位代码获取单位信息
												corpByEName = abCorpService.getCorpIdByName(vo.getEndorseCorpName());
												corpNameMap.put(vo.getEndorseCorpName(),corpByEName);
											}
											// 判断内部单位代码与所填的内部单位名称匹配不匹配
											if (corpByEName==null
													|| !corp.getId().equals(corpByEName.getId())) {
												errorMsg.add(
														AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_CORPNAME_ISNOT);
											}
										}
										vo.setEndorseCorpName(corp.getName());
										// 判断被背书人账号
										if (StringUtils.isNotEmpty(vo.getPledgeeAcc())) {
											BtBankAccDTO bankaccInfo = null;
											if (bankAccInfoMap.containsKey(vo.getPledgeeAcc())) {
												bankaccInfo = bankAccInfoMap.get(vo.getPledgeeAcc());
											} else {
												// 根据被背书人账号获取账号信息
												bankaccInfo = abAccrecordsService.getBtBankAccInfo(vo.getPledgeeAcc());
												bankAccInfoMap.put(vo.getPledgeeAcc(),bankaccInfo);
											}
											if (bankaccInfo != null) {
												// 判断被背书人账号信息的单位与所填的单位是否一致
												if (corp.getId().equals(bankaccInfo.getCorpId())) {
													vo.setPledgeeName(bankaccInfo.getAccName());
													vo.setPledgeeBankAcc(bankaccInfo.getBankCode());
													vo.setPledgeeBankName(bankaccInfo.getBankName());
												} else {
													errorMsg.add(
															AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_CORPNAME_BANKACC_ISNOTEXIST);
												}
											} else {
												errorMsg.add(
														AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKACC_ISNOTEXIST);
											}
										}
									}
								} else {
									errorMsg.add(
											AccebillConstant.AB_BILL_EXCEL_ERRORMSG_INTAPPLY_ENDORSE_CORPCODE_NULL);
								}
							} else if (AccebillConstant.AB_INSIDE_SING_N.equals(vo.getInteriorApply())) {
								// 外部背书 判断被背书人行号
								if (StringUtils.isNotEmpty(vo.getPledgeeBankAcc())) {
									InputBankInfoDTO bankInfo = null;
									if (inputBankInfoDTOMap.containsKey(vo.getPledgeeBankAcc())) {
										bankInfo = inputBankInfoDTOMap.get(vo.getPledgeeBankAcc());
									} else {
										// 根据银行行号得到银行账号信息
										bankInfo = abInputBankInfoService
												.getByBankCode(vo.getPledgeeBankAcc());
										inputBankInfoDTOMap.put(vo.getPledgeeAcc(),bankInfo);
									}
									if (bankInfo != null) {
										vo.setPledgeeBankName(bankInfo.getBankName());
									} else {
										errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKCODE_NULL);
									}
								} else if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abBillEndorseVo.getBillFlag()))) {
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKACC_NULL);
								}
							}
							// 判断被背书人名称
							if (StringUtils.isEmpty(vo.getPledgeeName())) {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_NAME_NULL);
							}
						}
						// 是否挂接资金计划
						if(budgetSel){
							Boolean reportBudget = reportBudgetMap.get(corpByCode.getId());
							if (reportBudget == null) {
								reportBudget = abVoucherinfoViceService.isBudget(corpByCode.getId(), AccebillConstant.BRIEF_STATUS_010);
								reportBudgetMap.put(corpByCode.getId(), reportBudget);
							}
							if (reportBudget && StringUtils.isEmpty(vo.getItemCodeStr())) {
								errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST);
							}else if(reportBudget && StringUtils.isNotEmpty(vo.getItemCodeStr())){
								FbsItemDTO fbsItemDTO = fbsItemMap.get(vo.getItemCodeStr());
								//判断计划科目是否存在
								if(fbsItemDTO==null){
									fbsItemDTO = abFbsItemService.getFbsItemByCode(vo.getItemCodeStr());
									fbsItemMap.put(vo.getItemCodeStr(), fbsItemDTO);
								}
								if (EmptyUtils.isEmpty(fbsItemDTO)) {
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNULL);
								}else{
									// 判断资金计划科目代码是否属于该业务
									if (abAccrecordsService.itemCodeIsMatchBusiness(AccebillConstant.AB_CONFIG_ENDORSEITEM, fbsItemDTO.getItemCode())) {
										errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISMATCH_BUSINESS);
									} else {
										vo.setItemCode(new String[]{vo.getItemCodeStr()});
										vo.setItemName(new String[]{fbsItemDTO.getItemName()});
										vo.setProcessMoney(new BigDecimal[]{vo.getBillMoney()});
										vo.setProcessCurMoney(new BigDecimal[]{vo.getBillMoney()});
										vo.setExchangeRate(new BigDecimal[]{new BigDecimal(1)});
									}
								}
							}
						}

						if(!"1".equals(abBillEndorseVo.getBlgrdbagcrclPrmtInd())
								&& (StrUtil.isNotEmpty(vo.getSbbllRngStrtSnSplit()) || StrUtil.isNotEmpty(vo.getSbbllRngEndSnSplit()))){
							//不可拆分票据不允许做子票区间拆分！
							errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_NOT_SPLIT);
						} else  {
							//设置了拆分子票区间处理
							if (StrUtil.isEmpty(vo.getSbbllRngEndSnSplit())
									&& StrUtil.isNotEmpty(vo.getSbbllRngStrtSnSplit())) {
//							结束子票区间为空开始子票区间有值自动根据结算结果设置结束子票区间
								String sbbllRngEndSnSplit=StrUtil.fill(vo.getTransMoney()
										.multiply(new BigDecimal("100"))
										.add(new BigDecimal(vo.getSbbllRngStrtSnSplit()))
										.subtract(new BigDecimal("1"))
										.setScale(0,BigDecimal.ROUND_HALF_UP).toString(),'0',12,true);
								vo.setSbbllRngEndSnSplit(sbbllRngEndSnSplit);
							}
//						开始拆分子票区间和结束子票区间 必须大于等于原票据子票区间 和 小于等于结束子票区间
							if (StrUtil.isNotEmpty(vo.getSbbllRngStrtSnSplit())
									&& StrUtil.isNotEmpty(vo.getSbbllRngEndSnSplit()) ) {
								long strtSnSplit=Long.parseLong(vo.getSbbllRngStrtSnSplit());
								long endSnSplit=Long.parseLong(vo.getSbbllRngEndSnSplit());
								if(strtSnSplit>=endSnSplit){
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_SPLIT_START_BE_END);
								}
								if(strtSnSplit<Long.parseLong(abBillEndorseVo.getSbbllRngStrtSn())
											||endSnSplit>Long.parseLong(abBillEndorseVo.getSbbllRngEndSn())){
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_OLD_NOT_INCLUDE_SPLIT);
								}
								BigDecimal transMoney= new BigDecimal(vo.getSbbllRngEndSnSplit())
										.subtract(new BigDecimal(vo.getSbbllRngStrtSnSplit()))
										.add(new BigDecimal("1"))
										.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
								//拆分金额和子票区间匹配
								if(vo.getTransMoney().compareTo(transMoney)!=0){
									errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_TRANSMONEY_NOT_SBBLLRNG);
								}

								//判断批量背书的子票区间是否有重叠
								String splitSbbllRngMapKey=StrUtil.builder(abBillEndorseVo.getOrigNo(),"-",abBillEndorseVo.getSbbllRngStrtSn(),"-",abBillEndorseVo.getSbbllRngEndSn()).toString();
								if(CollectionUtils.isEmpty(splitSbbllRngMap.get(splitSbbllRngMapKey))){
									splitSbbllRngMap.put(splitSbbllRngMapKey,new ArrayList<>());
								} else {
									for(AbBillEndorseVo splitVo : splitSbbllRngMap.get(splitSbbllRngMapKey)){
										if((strtSnSplit>=Long.parseLong(splitVo.getSbbllRngStrtSnSplit())
												&&strtSnSplit<=Long.parseLong(splitVo.getSbbllRngEndSnSplit()))
											||(endSnSplit>=Long.parseLong(splitVo.getSbbllRngStrtSnSplit())
												&&endSnSplit<=Long.parseLong(splitVo.getSbbllRngEndSnSplit()))){
											errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_SPLIT_SBBLLRNG_REP);
											break;
										}
									}
								}
								splitSbbllRngMap.get(splitSbbllRngMapKey).add(vo);
							}
						}

					} else {
						errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_DATA_NULL);
					}
				} else {
					errorMsg.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPCODE_ERROR);
				}
				// 添加错误信息到vo
				if (!errorMsg.isEmpty()) {
					vo.addErrorMsg(errorMsg);
				}
			}
		}
	}
	/**
	 * 校验重复背书方法
	 * @param ids
	 * @param origNos
	 * @param endorseeName
	 * @param repartMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object checkRepeatEndorse(String ids,String origNos,
			String endorseeName,String endorseeBankNo,String repartMsg){
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String,Object> returnAjaxJson = new HashMap<String,Object>();
		params.put("origNos", origNos);
		params.put("reqNm", endorseeName);
		params.put("recNm", endorseeName);
		List<String> resultListOrgin = abBillEndorseDao.checkRepeatEndorse(params);
		//背面信息存在重复的，
		if(ListUtils.isNotNullOrEmpty(resultListOrgin)){
			returnAjaxJson.put("flag", true);
			returnAjaxJson.put("origNos", resultListOrgin);
			return returnAjaxJson;
		}
		//票面要素
		if(StringUtils.isNotBlank(repartMsg)){
			String[] tpArray = repartMsg.split(",");
			List<String> sts=Arrays.asList(tpArray);
			resultListOrgin = (List<String>) (ListUtils.isNotNullOrEmpty(resultListOrgin)?resultListOrgin.addAll(sts):sts);
		}
		//票面要素重复
		returnAjaxJson.put("flag", ListUtils.isNotNullOrEmpty(resultListOrgin));
		returnAjaxJson.put("origNos", resultListOrgin);
		return returnAjaxJson;
	}
	/**
	 * 获取银行类型
	 * @param bankCode
	 * @param interiorApply
	 * @param pledgeeAcc
	 * @return
	 */
	public Object getBankTypeId(String bankCode,Integer interiorApply,String pledgeeAcc){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("bankCode", bankCode);
		params.put("pledgeeAcc", pledgeeAcc);
		Map<String,Object> returnAjaxJson = new HashMap<String,Object>();
		returnAjaxJson.put("flag", true);
		returnAjaxJson.put("bankTypeId", abBillEndorseDao.getBankTypeId(params,interiorApply));
		return returnAjaxJson;
	}
	/**
	 * 根据主表id 从表id查询背书数据
	 *
	 * @param vid 主表id
	 * @param vvid 从表id
	 * @return 返回列表数据
	 */
	public List<AbBillEndorseVo> listAbBillEndorseVobyVidAndVvid(String vid,String vvid) {
		return abBillEndorseDao.listAbBillEndorseVobyVidAndVvid(vid,vvid);
	}
	@Override
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbBillEndorseVoById(aid);
	}
	@Override
	@XxlJob("AbBillEndorseService")
	public void run(String arguments) {
		this.logger.info("---应收票据付款-背书失败结果返回结算系统，开始执行定时任务---");
//		 List<AbVoucherinfoDTO> infoList = abBillEndorseDao.queryBillEndorsePayErrorInfo();
		// 获取付款背书失败信息返回给结算系统
		List<String> listId = abBillEndorseDao.queryBillIdByStatus(false);
		List<String> listBillId = nisBillheadService.listBillId(listId);
		Map<String,String> billIdToPayIdMap=new HashMap<>();
		if(CollectionUtil.isNotEmpty(listBillId)) {
			listBillId.forEach(billId -> {
				String[] billIdToPayId = billId.split(",");
				billIdToPayIdMap.put(billIdToPayId[0], billIdToPayId[1]);
			});
			List<AbVoucherinfoDTO> infoList = abBillEndorseDao.queryBillEndorsePayInfo(ListUtil.toList(billIdToPayIdMap.keySet()));
			if (infoList == null || infoList.isEmpty()) {
				this.logger.info("---应收票据付款-背书失败结果返回结算系统，【0】条数据。");
			} else {
				infoList.forEach(info->{
					info.setPayId(billIdToPayIdMap.get(info.getId()));
				});
				Map<String, Map<String, List<AbVoucherinfoDTO>>> payReturnMap = new HashMap();
				//记录不需要返回数据，存在背书未完成信息不返回到付款（背书要么成功、拒签、打回、撤回）
				Map<String, String> notPayinfo = new HashMap<>();
				this.logger.info("---应收票据付款-背书失败结果返回结算系统，查询流水数据："+infoList);
				for (AbVoucherinfoDTO info : infoList) {
					if (notPayinfo.containsKey(info.getPayId())) {
						continue;
					}
					if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS_2.equals(info.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS_3.equals(info.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS_4.equals(info.getOptStatus())) {
						if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())) {
							if (!payReturnMap.containsKey(info.getPayId())) {
								Map<String, List<AbVoucherinfoDTO>> listMap = new HashMap<>();
								listMap.put("success", new ArrayList<AbVoucherinfoDTO>());
								payReturnMap.put(info.getPayId(), listMap);
							} else if (!payReturnMap.get(info.getPayId()).containsKey("success")) {
								payReturnMap.get(info.getPayId()).put("success", new ArrayList<AbVoucherinfoDTO>());
							}
							payReturnMap.get(info.getPayId()).get("success").add(info);
						} else {
							if (!payReturnMap.containsKey(info.getPayId())) {
								Map<String, List<AbVoucherinfoDTO>> listMap = new HashMap<>();
								listMap.put("error", new ArrayList<AbVoucherinfoDTO>());
								payReturnMap.put(info.getPayId(), listMap);
							} else if (!payReturnMap.get(info.getPayId()).containsKey("error")) {
								payReturnMap.get(info.getPayId()).put("error", new ArrayList<AbVoucherinfoDTO>());
							}
							payReturnMap.get(info.getPayId()).get("error").add(info);
						}
					} else {
						payReturnMap.remove(info.getPayId());
						notPayinfo.put(info.getPayId(), info.getPayId());
					}
				}
				if (payReturnMap.isEmpty()) {
					this.logger.info("---应收票据付款-背书失败结果返回结算系统，【0】条数据。");
				} else {
					ICurrentUser userVo = abUserService.getScheduler();
					this.logger.info("---应收票据付款-背书失败结果返回结算系统，【" + payReturnMap.size() + "】条付款数据。"+payReturnMap);
					for (String key : payReturnMap.keySet()) {
						abNisBillHeadService.backNisBillheadForBill(key, payReturnMap.get(key).get("error"), payReturnMap.get(key).get("success"));
						for (AbVoucherinfoDTO info : payReturnMap.get(key).get("error")) {
							abBillPayMiddleService.delBillPayMid(info.getVvid(), info.getPayId());
							abBillMiddleService.saveBillMiddle(info.getId(), AccebillConstant.BILL_MIDDLE_TYPE_010PAYERROR, userVo);
							AbVoucherinfoVice vice = abVoucherinfoViceDao.get(info.getVvid());
							AbVoucherinfo voucherinfo = abVoucherinfoDao.get(vice.getVid());
							if (vice.getPayLockMoney() == null || voucherinfo.getBillMoney().compareTo(vice.getPayLockMoney()) == 0) {
								vice.setPayLockMoney(BigDecimal.ZERO);
								vice.setUpdateBy(userVo == null ? vice.getUpdateBy() : userVo.getUsername());
								vice.setUpdateDate(new Date());
								abVoucherinfoViceDao.saveOrUpdate(vice);
							}
						}
					}
				}
			}
		}
		this.logger.info("---应收票据付款-背书失败结果返回结算系统，结束执行定时任务---");
		//获取付款背书成功信息返回给结算系统
		this.runEndorsePaySuccess();
	}
	/**
	 * 获取付款背书成功信息返回给结算系统
	 */
	public void runEndorsePaySuccess() {
		this.logger.info("---应收票据付款-背书成功结果返回结算系统，开始执行定时任务---");
		// List<AbVoucherinfoDTO> infoList = abBillEndorseDao.queryBillEndorsePaySuccessInfo();
		// 获取付款背书成功信息返回给结算系统
		List<String> listId = abBillEndorseDao.queryBillIdByStatus(true);
		List<String> listBillId = nisBillheadService.listBillId(listId);
		Map<String,String> billIdToPayIdMap=new HashMap<>();
		if(CollectionUtil.isNotEmpty(listBillId)) {
			listBillId.forEach(billId -> {
				String[] billIdToPayId = billId.split(",");
				billIdToPayIdMap.put(billIdToPayId[0], billIdToPayId[1]);
			});
			List<AbVoucherinfoDTO> infoList = abBillEndorseDao.queryBillEndorsePayInfo(ListUtil.toList(billIdToPayIdMap.keySet()));
			if (infoList == null || infoList.isEmpty()) {
				this.logger.info("---应收票据付款-背书成功结果返回结算系统，【0】条数据。");
			} else {
				infoList.forEach(info->{
					info.setPayId(billIdToPayIdMap.get(info.getId()));
				});
				Map<String, Map<String, List<AbVoucherinfoDTO>>> payReturnMap = new HashMap();
				//记录不需要返回数据，存在背书未完成信息不返回到付款（背书要么成功、拒签、打回、撤回）
				Map<String, String> notPayinfo = new HashMap<>();
				this.logger.info("---应收票据付款-背书成功结果返回结算系统，查询流水数据："+infoList);
				for (AbVoucherinfoDTO info : infoList) {
					if (notPayinfo.containsKey(info.getPayId())) {
						continue;
					}
					if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())) {
						if (!payReturnMap.containsKey(info.getPayId())) {
							Map<String, List<AbVoucherinfoDTO>> listMap = new HashMap<>();
							listMap.put("success", new ArrayList<AbVoucherinfoDTO>());
							payReturnMap.put(info.getPayId(), listMap);
						} else if (!payReturnMap.get(info.getPayId()).containsKey("success")) {
							payReturnMap.get(info.getPayId()).put("success", new ArrayList<AbVoucherinfoDTO>());
						}
						payReturnMap.get(info.getPayId()).get("success").add(info);
					} else {
						payReturnMap.remove(info.getPayId());
						notPayinfo.put(info.getPayId(), info.getPayId());
					}
				}
				if (payReturnMap.isEmpty()) {
					this.logger.info("---应收票据付款-背书成功结果返回结算系统，【0】条数据。");
				} else {
					ICurrentUser userVo = abUserService.getScheduler();
					this.logger.info("---应收票据付款-背书成功结果返回结算系统，【" + payReturnMap.size() + "】条付款数据。"+payReturnMap);
					for (String key : payReturnMap.keySet()) {
						abNisBillHeadService.successNisBillheadForBill(key, NetbankConstant.STATUE_BILL_COMPLETE);
						for (AbVoucherinfoDTO info : payReturnMap.get(key).get("success")) {
							abBillMiddleService.saveBillMiddle(info.getId(), AccebillConstant.BILL_MIDDLE_TYPE_010PAYERROR, userVo);
						}
					}
				}
			}
		}
		this.logger.info("---应收票据付款-背书成功结果返回结算系统，结束执行定时任务---");
	}

	public void paymentBillEndorsement(NisBillheadDTO nisBillhead,ICurrentUser user){
		//存在并发问题重新查询一次
		String billIds = nisBillService.getBillIds(nisBillhead.getId());
//		nisBillhead = nisBillheadService.getNisBillheadById(nisBillhead.getId());
		String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
		// 获取票据id一一对应的挂接付款金额
		String endorseBillBalances=nisBillService.getBillBalances(StrUtil.join(CommonConstant.COMMA_SPLIT, billIds), nisBillhead.getId());
		//解锁
		this.accebillService.batchUnLock(abAccrecordsIds, user);
		//普通票据流水id
		StringBuilder abAccIds = new StringBuilder();
		//票据池明细id
		StringBuilder poolIds=new StringBuilder();
		int index=0,poolIdsIndx=0,abAccIdsIndx=0;
		Map<Integer,Integer> poolIdsIndxMap=new HashMap<>();
		Map<Integer,Integer> abAccIdsIndxMap=new HashMap<>();
		for(String accId : abAccrecordsIds){
			//根据id判断是否是票据池明细数据,是则选择的是票据池已入池的数据
			if(this.accebillService.isPoolDtl(accId)){
				if(poolIds.length()>0){
					poolIds.append(CommonConstant.COMMA_SPLIT);
				}
				poolIds.append(accId);
				poolIdsIndxMap.put(poolIdsIndx,index);
				poolIdsIndx++;
			}else{
				if(abAccIds.length()>0){
					abAccIds.append(CommonConstant.COMMA_SPLIT);
				}
				abAccIds.append(accId);
				abAccIdsIndxMap.put(abAccIdsIndx,index);
				abAccIdsIndx++;
			}
			index++;
		}
		String[] abAccIdsArr = abAccIds.toString().split(CommonConstant.COMMA_SPLIT);
		String endorseBillAbAccIds=null;
		String endorseBillPoolIds=null;
		if(StringUtils.isNotEmpty(abAccIdsArr)){
			String billBalances=nisBillService.getBillBalances(StrUtil.join(CommonConstant.COMMA_SPLIT, abAccIdsArr), nisBillhead.getId());
			//普通票据背书
			endorseBillAbAccIds = this.accebillService.batchEndorse(abAccIdsArr, nisBillhead.getPayeeAccName(), nisBillhead.getPayeeAcc(), nisBillhead.getPayeeBankCode(), nisBillhead.getId(), billBalances,nisBillhead.getWishPayday(), false, user);
			AbAccrecordsVO vo = new AbAccrecordsVO();
			vo.setRmk(StringUtils.isNotBlank(vo.getRmk()) ? vo.getRmk() : nisBillhead.getPurpose());
			vo.setIdArray(endorseBillAbAccIds.split(","));
			abAccrecordsService.batchUpdate(vo,null);
		}
		if (!EmptyUtils.isBlank(poolIds.toString())) {
			//票据池背书
			endorseBillPoolIds = this.accebillPoolService.saveAbPoolEndorseInfoByNisPay(poolIds.toString(), nisBillhead.getPayeeAccName(), nisBillhead.getPayeeAcc(), nisBillhead.getPayeeBankCode(), nisBillhead.getWishPayday(), user);
		}

		//设置背书返回明细和挂接明细一致避免挂接余额顺序不准确
		String[] endorseBillIds=new String[abAccrecordsIds.length];
		if(StrUtil.isNotEmpty(endorseBillAbAccIds)){
			String[] endorseBillAbAccIdsArr=endorseBillAbAccIds.split(CommonConstant.COMMA_SPLIT);
			for(int i=0;i<endorseBillAbAccIdsArr.length;i++){
				endorseBillIds[abAccIdsIndxMap.get(i)]=endorseBillAbAccIdsArr[i];
			}
		}
		if(StrUtil.isNotEmpty(endorseBillPoolIds)){
			String[] endorseBillPoolIdsArr=endorseBillPoolIds.split(CommonConstant.COMMA_SPLIT);
			for(int i=0;i<endorseBillPoolIdsArr.length;i++){
				endorseBillIds[poolIdsIndxMap.get(i)]=endorseBillPoolIdsArr[i];
			}
		}
//		StringBuilder endorseBillIdsStr=new StringBuilder(ArrayUtil.join(endorseBillIds, ","));
//        return new StringBuilder(ArrayUtil.join(endorseBillIds, ","));
		//把票据id放到票据挂接中间表
		if (StringUtils.isNotEmpty(endorseBillIds)) {
			//票据进行开票登记完成操作
			String[] billIdAr = endorseBillIds;
			String[] billBalanceAr = endorseBillBalances.split(CommonConstant.COMMA_SPLIT);
			NisBillDTO bill=null;
			List<NisBillDTO> nisBillDTOList=new ArrayList<>();
			for (int a = 0; a < billIdAr.length; a++) {
				bill = new NisBillDTO();
				bill.setPayId(nisBillhead.getId());
				bill.setBillId(billIdAr[a]);
				bill.setBalance(new BigDecimal(billBalanceAr[a]));
				nisBillDTOList.add(bill);
			}
			nisBillService.batchNisBill(nisBillDTOList);
		}
	}
}
