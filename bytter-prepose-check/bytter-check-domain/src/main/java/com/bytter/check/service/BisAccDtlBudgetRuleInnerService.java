package com.bytter.check.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlBudgetRuleVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeRuleDTO;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.IFexForeignReqService;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.*;
import com.bytter.core.utils.*;
import com.bytter.credential.api.dto.LcBillArrivalDTO;
import com.bytter.credential.api.service.ILcEnrolService;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
import com.bytter.increment.api.dto.ChoProductRedeemDTO;
import com.bytter.increment.api.service.IChoBankFinanceService;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行流水明细查询
 * @date 2017-07-05 11:20:29
 */
@Service("bisAccDtlBudgetRuleInnerService")
@Transactional
public class BisAccDtlBudgetRuleInnerService implements ITaskService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BisAccDtlBudgetRuleService bisAccDtlBudgetRuleService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
	@DubboReference(check = false)
	private INisReceivableService nisReceivableService;
	@DubboReference(check = false)
	private INisBusTypeService nisBusTypeService;
	@DubboReference(check = false)
	private IAccebillService accebillService;
	@DubboReference(check = false)
	private ICmsLoanService cmsLoanService;
	@DubboReference(check = false)
	private ILcEnrolService lcEnrolService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IChoBankFinanceService choBankFinanceService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFexForeignReqService fexForeignReqService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;

	@DubboReference(check = false)
	private IDictService dictService;
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
//		String corpAuthSql = SqlUtil.getInSql(
//				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, user.getId()), "t.corpId",
//				true);
        String corpAuthSql = SqlUtil.getInSql(
                btBankAccService.getBankAccsByCorpIdList(corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, user.getId()), "", new HashMap<>()), "t.bankAcc",true);
		sql.append(" and ").append(corpAuthSql);
		//权限控制类型  1-单位级 2-账号级
		String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (!CommonConstant.YES.equals(controlType)) {
			//设置账号授权控制
			/*List<String> accountGrantIdList = accountGrantService.listByMultiValAccIds(dataTablesInput.getCurrentUserId(), ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, null,
					AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
					AccountGrantConstant.QUERY_DETAIL);*/
			List<BtBankAccDTO> accountGrantList = accountGrantService.listDTONotUnionByMultiVal(dataTablesInput.getCurrentUserId(), null, ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, null,
					AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
					AccountGrantConstant.QUERY_DETAIL);
			List<String> bankAccGrantList = null;
			//List<BtBankAccDTO> accountGrantList = bankAccService.getBankAccNotUnionByIdList(accountGrantIdList);
			if (ListUtils.isNotNullOrEmpty(accountGrantList)) {
				bankAccGrantList = new ArrayList<>();
				for (int i = 0; i < accountGrantList.size(); i++) {
					BtBankAccDTO accDTO = accountGrantList.get(i);
					bankAccGrantList.add(accDTO.getBankAcc());
				}
			}
			String accAuthSql = SqlUtil.getInSql(bankAccGrantList, "t.bankAcc",
					true);
			sql.append(" and ").append(accAuthSql);
		}
		List<String> bankAccList = Lists.newArrayList();

		Object insCorpId = dataTablesInput.getFormdata().get("ins_corpId");
		if (ObjectUtil.isNotEmpty(insCorpId)){
			String[] split = String.valueOf(insCorpId).split(",");
			List<String> corpIdList = Lists.newArrayList();
			for (String s : split) {
				corpIdList.add(s);
			}
			List<BtBankAccDTO> bankAccDTO = btBankAccService.getListByCorpIds(corpIdList);
			bankAccList = bankAccDTO.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
		}
		Object eqBankTypeId = dataTablesInput.getFormdata().get("eqs_bankTypeId");
		if (ObjectUtil.isNotEmpty(eqBankTypeId)){
			String[] split = String.valueOf(eqBankTypeId).split(",");
			List<String> bankTypeIds = Lists.newArrayList();
			for (String s : split) {
				bankTypeIds.add(s);
			}
			List<BtBankAccDTO> bankAccDTO = btBankAccService.queryListByBankTypeIds(bankTypeIds);
			bankAccList = bankAccDTO.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
			if(ListUtils.isNullOrEmpty(bankAccList)){
				return new DataTablesOutput(1, new ArrayList(0), 0, 0);
			}
		}
		if (!CollectionUtil.isEmpty(bankAccList)){
			dataTablesInput.getFormdata().put("bankAccList",bankAccList);
		}
		DataTablesOutput output = bisAccDtlBudgetRuleService.getDataTablesUnion(dataTablesInput, sql.toString());
		String cdSign = (String) dataTablesInput.getFormdata().get("eqs_cdSign");
		//付款-设置计划科目
		this.setItem(output);
		if(CommonConstant.NO.equals(cdSign)) {
			//收款-设置收款单号
			this.setBillNo(output);
		}
		return output;
	}
	/**
	 * 设置计划科目
	 *
	 * @param output
	 */
	public void setItem(DataTablesOutput output) {
		if (null != output.getData()) {
			HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
			HashMap<String, Boolean> controlMap = new HashMap<>();
			for (int i = 0; i < output.getData().size(); i++) {
				BisAccDtlBudgetRuleDTO vo = (BisAccDtlBudgetRuleDTO) output.getData().get(i);
				//设置状态
				if(StringUtils.isBlank(vo.getFbsStatus())){
					vo.setFbsStatus(CommonConstant.NO);
					//inject(vo);
				}
				//设置结算类型
				if(StringUtils.isNotEmpty(vo.getSettlType())){
					vo.setSettlTypeName(dictService.get("settlType",vo.getSettlType()));
				}
				if(vo.getProcessDate() == null){
					continue;
				}
				if(CommonConstant.YES.equals(vo.getCdSign())) {
					List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, vo.getId());
					String item = this.setItemInfo(fbsBudgetProcessDTOList);
					if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
						vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
					}
					vo.setItemInfo(item);
					BigDecimal curExrate = curExrateMap.get(vo.getCurId());
					if (curExrate == null) {
						curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
						curExrateMap.put(vo.getCurId(), curExrate);
					}
					vo.setCurExrate(curExrate);
					//单位是否受计划控制
					Boolean control = controlMap.get(vo.getCorpId());
					if (control == null) {
						control = fbsCorpBudgetService.controlBudgetByCorpId(vo.getCorpId());
						controlMap.put(vo.getCorpId(), control);
					}
					vo.setControl(control);
				}

			}
		}
	}
	/**
	 * 设置收款单号
	 * @param output
	 */
	public void setBillNo(DataTablesOutput output){
		if (null != output.getData()) {
			for (int i = 0; i < output.getData().size(); i++) {
				BisAccDtlBudgetRuleDTO vo = (BisAccDtlBudgetRuleDTO) output.getData().get(i);
				String bankSerialNo = vo.getId();
				String billNo = "";
				List<NisReceivableDTO> nisReceivableDTOList = nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
				if(!EmptyUtils.isEmptyList(nisReceivableDTOList)) {
					for (NisReceivableDTO dto : nisReceivableDTOList) {
						billNo += "," + dto.getBillNo();
					}
					billNo = billNo.substring(1);
					vo.setBillNo(billNo);
				}
			}
		}
	}
	/**
	 * 设置计划科目详情
	 *
	 * @param fbsBudgetProcessDTOList
	 */
	public String setItemInfo(List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList) {
		String item = "";
		if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
			for (int j = 0; j < fbsBudgetProcessDTOList.size(); j++) {
				if (j == 0) {
					item = fbsBudgetProcessDTOList.get(j).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT
							+ fbsBudgetProcessDTOList.get(j).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT
							+ fbsBudgetProcessDTOList.get(j).getProcessMoney().toString();
				} else {
					item = item + "," + fbsBudgetProcessDTOList.get(j).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT
							+ fbsBudgetProcessDTOList.get(j).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT
							+ fbsBudgetProcessDTOList.get(j).getProcessMoney().toString();
				}
			}
		}
		return item;
	}
	/**
	 * 是否挂接预算系统
	 */
	public boolean isBudget(String corpId) {
		if (StringUtils.isNotEmpty(corpId)
				&& fbsBudgetProcessService.corpCtlBudget(corpId)) {
			return true;
		}
		return false;
	}
	/**
	 * 逻辑业务
	 *
	 * @param id
	 * @param user
	 * @param type 0 恢复 1还原 2删除
	 */
	public void business(String id, ICurrentUser user, String type) {
		if (StringUtils.isNotBlank(type)) {
			if (BisAccBalConstant.TYPE_H.equals(type)) {
				//恢复
				bisAccDtlService.updateDtlFbsStatus(id, BisAccBalConstant.FBS_STATUS_N);
			} else if (BisAccBalConstant.TYPE_R.equals(type)) {
				//还原
				bisAccDtlService.updateDtlFbsStatus(id, BisAccBalConstant.FBS_STATUS_N);
			} else {
				if(this.isMergeBill(id)){
					throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_DELETE);
				}
				//删除
				bisAccDtlService.updateDtlFbsStatus(id, BisAccBalConstant.FBS_STATUS_D);
			}
			BisAccDtlBudgetRuleDTO bisAccDtl = this.getBisAccDtlByIdUnion(id);
			List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, bisAccDtl.getId());
			if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
				this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, bisAccDtl.getId());
			}
			com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
			String bankSerialNo = bisAccDtlDTO.getId();
			List<NisReceivableDTO> nisReceivableDTOList =  nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
			String msg = "恢复收款流水确认";
			if(!EmptyUtils.isEmptyList(nisReceivableDTOList)){
				for(NisReceivableDTO nisReceivableDTO :nisReceivableDTOList){
					if(NetbankConstant.RECEIVABLE_STATUE_ISREC.equals(nisReceivableDTO.getStatus())) {
						if (BisAccBalConstant.TYPE_H.equals(type)) {
							//获取业务类别
							NisBusTypeDTO nisBusTypeDTO = this.nisBusTypeService.getById(nisReceivableDTO.getBusType());
							if(!EmptyUtils.isEmpty(nisBusTypeDTO) && !EmptyUtils.isEmpty(nisBusTypeDTO.getSysBusType())
									&& !EmptyUtils.isEmpty(bisAccDtl.getBillId()) && !bisAccDtl.getBillId().equals("null")){
								//贴现到账,托收到账回调 业务回滚
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_7) ||
										nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_8)){
									this.accebillService.backBillBrief(bisAccDtl.getBillId(),user);
								}
								//银行借款放款到账回调
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_9)){
									this.cmsLoanService.cancelBankFlow(bisAccDtl.getBillId());
								}
								//信用证到期收款回调
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_10)){
									this.lcEnrolService.deletBillArrivalMark(bisAccDtl.getBillId());
								}
								//定转活、通转活
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_4) ||
										nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_5)){
									this.choBankFinanceService.collectionConfirmBankFinance("0",bisAccDtl.getBillId());
								}
								//理财赎回到账
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_6)){
									this.choBankFinanceService.collectionConfirmProductRedeem("0",bisAccDtl.getBillId());
								}
								//外汇取消
								if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_11) ||
										nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_12) ||
										nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_13) ||
										nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_14)){
									bisAccDtlDTO.setBillId(bisAccDtl.getBillId());
									// todo:数据转换
									BisAccDtlDTO bisAccDtlNew = BeanUtils.copyNewInstanceProperties(bisAccDtlDTO, BisAccDtlDTO.class);
									this.fexForeignReqService.updateFexCashFlowForCancel(bisAccDtlNew);
								}
							}
						}
						//如果是流水认领生成的收款单，恢复时做逻辑删除
						if (NetbankConstant.RECEIVABLE_BILLFROM_2.equals(nisReceivableDTO.getBillFrom())) {
							nisReceivableService.updateNisReceivable(nisReceivableDTO.getBillNo(), bankSerialNo, NetbankConstant.STATUE_DEL, msg, nisReceivableDTO.getBusType(),user,"","","");
						} else {
							nisReceivableService.updateNisReceivable(nisReceivableDTO.getBillNo(), bankSerialNo, NetbankConstant.STATUE_CHECK, msg,nisReceivableDTO.getBusType(), user,"","","");
						}
					}
				}
			}
		}
	}
	/**
	 * 判断是否已确认资金计划
	 *
	 * @param id
	 * @return
	 */
	public boolean isSure(String id) {
		List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, id);
		if (!EmptyUtils.isEmptyList(list) && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 根据id查到流水明细
	 *
	 * @param id
	 * @return
	 */
	public BisAccDtlBudgetRuleDTO getBisAccDtlByIdUnion(String id) {
		BisAccDtlBudgetRuleDTO vo = bisAccDtlBudgetRuleService.getBisAccDtlByIdUnion(id);
		String bankSerialNo = vo.getId();
		String billNo = "";
		List<NisReceivableDTO> nisReceivableDTOList = nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
		if(!EmptyUtils.isEmptyList(nisReceivableDTOList)) {
			for (NisReceivableDTO dto : nisReceivableDTOList) {
				billNo += "," + dto.getBillNo();
			}
			billNo = billNo.substring(1);
			vo.setBillNo(billNo);
		}
		List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, vo.getId());
		String item = this.setItemInfo(fbsBudgetProcessDTOList);
		if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
			vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
		}
		vo.setItemInfo(item);
		vo.setBudget(item);
		BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
		vo.setCurExrate(curExrate);
		//设置状态
		if(StringUtils.isBlank(vo.getFbsStatus())){
			vo.setFbsStatus(CommonConstant.NO);
			//inject(vo);
		}
		return vo;
	}
	/**
	 * 批量确认计划
	 *
	 * @param ids
	 * @param type        Y 确认 D 删除
	 * @param itemCode    科目代码
	 * @param itemName    科目名称
	 * @param processDate 执行时间
	 * @return
	 */
	public void batchBusiness(String ids, String itemCode, String itemName, String processDate, String settlType, ICurrentUser user, String type,
                              String billNos, String batchBusType, String rmks) {
		if (StringUtils.isNotBlank(type) && BisAccBalConstant.TYPE_D.equals(type)) {
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
				this.business(id, user, BisAccBalConstant.RBSIGN_BLUE);
			}
		} else {
			String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
//			String[] billNoStr = billNos.split(CommonConstant.SEMI_SPLIT);

			String[] rmkStr = {};
			if (rmks != null){
				rmkStr = rmks.split(CommonConstant.COMMA_SPLIT);
			}
			for (int i=0;i<idStr.length;i++) {
				String id = idStr[i];
				String billNo = "";
//				if (!EmptyUtils.isEmpty(billNos)){
//					if (!EmptyUtils.isEmpty(billNoStr) && billNoStr.length>i) {
//						billNo = billNoStr[i];
//					}
//				}
				String rmk = "";
				if (!EmptyUtils.isEmpty(rmks)) {
					if (!EmptyUtils.isEmpty(rmkStr) && rmkStr.length>i) {
						rmk = rmkStr[i];
					}
				}
				com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
				bisAccDtlDTO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
				if(!EmptyUtils.isBlank(settlType)){
					bisAccDtlDTO.setSettlType(settlType);
				}
				if(!EmptyUtils.isBlank(rmk)){
					bisAccDtlDTO.setRmk(rmk);
				}
				if(!EmptyUtils.isBlank(batchBusType)){
					bisAccDtlDTO.setBusType(batchBusType);
				}
				bisAccDtlService.updateDtlByDto(bisAccDtlDTO);
				BisAccDtlBudgetRuleDTO bisAccDtl = this.getBisAccDtlByIdUnion(id);
				bisAccDtl.setBudget(itemCode + BudgetConstant.BUDGET_PROCESS_SPLIT + itemName
						+ BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtl.getAmt());
				bisAccDtl.setProcessDate(processDate);
				bisAccDtl.setBillNo(billNo);
				bisAccDtl.setBusType(batchBusType);
				bisAccDtl.setRmk(rmk);
				this.saveBisAccDtlBudgetProcess(bisAccDtl, user);
			}
		}
	}
	/**
	 * 确认计划
	 * @param bisAccDtlBudgetRuleVO
	 * @param user
	 */
	public void sure(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO, ICurrentUser user){
        //是否挂资金计划
        String budgetSel = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
		if (bisAccDtlBudgetRuleVO != null) {
			com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(bisAccDtlBudgetRuleVO.getId());
			if(CommonConstant.YES.equals(budgetSel) && EmptyUtils.isNotEmpty(bisAccDtlBudgetRuleVO.getBudget())) {
				bisAccDtlDTO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
			}else if(CommonConstant.NO.equals(budgetSel)){
                bisAccDtlDTO.setFbsStatus(BisAccBalConstant.FBS_STATUS_Y);
            }
			if(!EmptyUtils.isBlank(bisAccDtlBudgetRuleVO.getSettlType())) {
				bisAccDtlDTO.setSettlType(bisAccDtlBudgetRuleVO.getSettlType());
			}
			if(!EmptyUtils.isBlank(bisAccDtlBudgetRuleVO.getRmk())) {
				bisAccDtlDTO.setRmk(bisAccDtlBudgetRuleVO.getRmk());
			}
			if(!EmptyUtils.isBlank(bisAccDtlBudgetRuleVO.getBusType())) {
				bisAccDtlDTO.setBusType(bisAccDtlBudgetRuleVO.getBusType());
			}
			if(NisBusTypeConstant.CD_SIGN_PAYEE.equals(bisAccDtlDTO.getCdSign())) {
				//保存业务id
				if (!EmptyUtils.isBlank(bisAccDtlBudgetRuleVO.getBillId())) {
					bisAccDtlDTO.setBillId(bisAccDtlBudgetRuleVO.getBillId());
					//获取业务类别
					NisBusTypeDTO nisBusTypeDTO = this.nisBusTypeService.getById(bisAccDtlBudgetRuleVO.getBusType());
					if (!EmptyUtils.isEmpty(nisBusTypeDTO) && !EmptyUtils.isEmpty(nisBusTypeDTO.getSysBusType())
							&& !EmptyUtils.isEmpty(bisAccDtlBudgetRuleVO.getBillId()) && !bisAccDtlBudgetRuleVO.getBillId().equals("null")) {
						//贴现到账,托收到账回调
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_7) ||
								nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_8)) {
							this.accebillService.completeAccrecord(bisAccDtlBudgetRuleVO.getBillId(), user);
						}
						//银行借款放款到账回调
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_9)) {
							this.cmsLoanService.relateBankFlow(bisAccDtlBudgetRuleVO.getBillId());
						}
						//信用证到期收款回调
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_10)) {
							this.lcEnrolService.addBillArrivalMark(bisAccDtlBudgetRuleVO.getBillId());
						}
						//定转活、通转活
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_4) ||
								nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_5)) {
							this.choBankFinanceService.collectionConfirmBankFinance("1", bisAccDtlBudgetRuleVO.getBillId());
						}
						//理财赎回到账
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_6)) {
							this.choBankFinanceService.collectionConfirmProductRedeem("1", bisAccDtlBudgetRuleVO.getBillId());
						}
						//外汇
						if (nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_11) ||
								nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_12) ||
								nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_13) ||
								nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_14)) {
							// todo:数据转换
							BisAccDtlDTO bisAccDtlNew = BeanUtils.copyNewInstanceProperties(bisAccDtlDTO, BisAccDtlDTO.class);
							this.fexForeignReqService.updateFexCashFlowForSure(bisAccDtlNew);
						}
					}
				}
			}
			bisAccDtlService.updateDtlByDto(bisAccDtlDTO);
			this.saveBisAccDtlBudgetProcess(bisAccDtlBudgetRuleVO, user);
		}
	}
	/**
	 * 确认计划
	 * @param bisAccDtlBudgetRuleVO
	 * @param user
	 */
	public void sureNotUpdateStatus(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO, ICurrentUser user){
		if (bisAccDtlBudgetRuleVO != null) {
			com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(bisAccDtlBudgetRuleVO.getId());
			bisAccDtlDTO.setSettlType(bisAccDtlBudgetRuleVO.getSettlType());
			if(!EmptyUtils.isBlank(bisAccDtlBudgetRuleVO.getRmk())) {
				bisAccDtlDTO.setRmk(bisAccDtlBudgetRuleVO.getRmk());
			}
			bisAccDtlDTO.setBusType(bisAccDtlBudgetRuleVO.getBusType());
			//更新流水认领标志为已匹配
			bisAccDtlDTO.setFbsStatus(BisAccBalConstant.FBS_STATUS_M);
			bisAccDtlService.updateDtlByDto(bisAccDtlDTO);
			//计划不为空时
			if(EmptyUtils.isNotEmpty(bisAccDtlBudgetRuleVO.getBudget())) {
				this.saveBisAccDtlBudgetProcessForAuto(bisAccDtlBudgetRuleVO, user);
			}
		}
	}
	/**
	 * 资金调拨没有匹配上则直接改成已匹配
	 */
	public void sureUpdateStatusM(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO){
		if (bisAccDtlBudgetRuleVO != null) {
			com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(bisAccDtlBudgetRuleVO.getId());
			//更新流水认领标志为已匹配
			bisAccDtlDTO.setFbsStatus(BisAccBalConstant.FBS_STATUS_M);
			bisAccDtlService.updateDtlByDto(bisAccDtlDTO);
		}
	}
	/**
	 * 操作收款单
	 * @param vo
	 * @param user
	 */
	public void toSaveNisReceivable(BisAccDtlBudgetRuleDTO vo, ICurrentUser user){
		String bankSerialNo = vo.getId();
		NisBusTypeDTO nisBusTypeDTO = new NisBusTypeDTO();
		if(!EmptyUtils.isEmpty(vo.getBusType())) {
			//获取业务类别
			nisBusTypeDTO = this.nisBusTypeService.getById(vo.getBusType());
		}
		String sysBusType = "";
		String billId = "";
		String busBillNo = "";
		if(!EmptyUtils.isEmpty(nisBusTypeDTO) && EmptyUtils.isNotEmpty(nisBusTypeDTO.getSysBusType())) {
			sysBusType = nisBusTypeDTO.getSysBusType();
			if(EmptyUtils.isNotEmpty(vo.getBillId()) && !vo.getBillId().equals("null") ) {
				billId = vo.getBillId();
				//贴现到账,托收到账
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_7) ||
						nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_8)){
					AbEmitBillDTO abEmitBillDTO  = this.accebillService.selectBillInfo(billId);
					if(!EmptyUtils.isEmpty(abEmitBillDTO)){
						busBillNo =abEmitBillDTO.getOrigNo();
					}
				}
				//银行借款放款到账
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_9)){
					CmsProvideSendDTO cmsProvideSendDTO = cmsLoanService.getCmsProvideLoanSendById(billId);
					if(!EmptyUtils.isEmpty(cmsProvideSendDTO)){
						busBillNo = cmsProvideSendDTO.getBillCode();
					}
				}
				//信用证到期收款回调
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_10)){
					LcBillArrivalDTO lcBillArrivalDTO = lcEnrolService.getBillArrivalById(billId);
					if(!EmptyUtils.isEmpty(lcBillArrivalDTO)){
						busBillNo = lcBillArrivalDTO.getArrivalCode();
					}
				}
				//定转活、通转活
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_4) ||
						nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_5)){
					ChoBankFinanceDTO choBankFinanceDTO = choBankFinanceService.getBankFinanceEntity(billId);
					if(!EmptyUtils.isEmpty(choBankFinanceDTO)) {
						busBillNo = choBankFinanceDTO.getBillNo();
					}
				}
				//理财赎回到账
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_6)){
					ChoProductRedeemDTO choProductRedeemDTO = choBankFinanceService.getProductRedeemEntity(billId);
					if(!EmptyUtils.isEmpty(choProductRedeemDTO)) {
						busBillNo = choProductRedeemDTO.getBillNo();
					}
				}
				//外汇
				if(nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_11) ||
						nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_12) ||
						nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_13) ||
						nisBusTypeDTO.getSysBusType().equals(NisBusTypeConstant.SYS_BUS_TYPE_14)){
					   busBillNo = fexForeignReqService.getFexCashFlowBillNo(billId);
				}
			}
		}
		//收款单操作
		List<NisReceivableDTO> list = nisReceivableService.getNisReceivableByBankSerialNo(vo.getId());
		if(EmptyUtils.isEmpty(vo.getBillNo()) && EmptyUtils.isEmpty(list)) {
			NisReceivableDTO dto = new NisReceivableDTO();
			BeanUtils.copyNotNullProperties(vo,dto);
			dto.setBillFrom(NetbankConstant.RECEIVABLE_BILLFROM_2);
			dto.setBankSerialNo(bankSerialNo);
			dto.setId("");
			dto.setStatus(NetbankConstant.RECEIVABLE_STATUE_ISREC);
			dto.setSysBusType(sysBusType);
			dto.setBillId(billId);
			dto.setBusBillNo(busBillNo);
			nisReceivableService.saveNisReceivable(dto, user);
		}else if(!EmptyUtils.isEmpty(vo.getBillNo())){
			String billNo = vo.getBillNo();
			String[] billNos = billNo.split(",");
			String msg = "流水认领确认";
			String busType = vo.getBusType();
			for (int i =0;i<billNos.length;i++) {
				nisReceivableService.updateNisReceivable(billNos[i],bankSerialNo, NetbankConstant.RECEIVABLE_STATUE_ISREC,msg,busType,user,sysBusType,billId,busBillNo);
			}
		}
	}
	/**
	 * 预算信息占用
	 *
	 * @param vo
	 * @param user
	 */
	public void saveBisAccDtlBudgetProcess(BisAccDtlBudgetRuleDTO vo, ICurrentUser user) {
		if(NisBusTypeConstant.CD_SIGN_PAYEE.equals(vo.getCdSign())) {
			this.toSaveNisReceivable(vo, user);
		}
		if (EmptyUtils.isNotEmpty(vo.getProcessDate())) {
			List<String[]> itemCodeList = new ArrayList();
			List<String[]> itemNameList = new ArrayList();
			List<BigDecimal[]> processMoneyList = new ArrayList();
			if (StringUtils.isNotBlank(vo.getBudget())) {
				String[] budgets = String.valueOf(vo.getBudget()).split(",");
				if (!EmptyUtils.isEmptyArray(budgets)) {
					String[] itemCode = new String[budgets.length];
					String[] itemName = new String[budgets.length];
					BigDecimal[] processMoney = new BigDecimal[budgets.length];
					for (int i = 0; i < budgets.length; i++) {
						String[] budget = budgets[i].split(BudgetConstant.BUDGET_PROCESS_SPLIT);
						itemCode[i] = budget[0];
						itemName[i] = budget[1];
						processMoney[i] = new BigDecimal(budget[2]);
					}
					itemCodeList.add(itemCode);
					itemNameList.add(itemName);
					processMoneyList.add(processMoney);
				}
			}
			for (int i = 0; i < itemCodeList.size(); i++) {
				this.batchProcess(vo, itemCodeList.get(i), itemNameList.get(i), processMoneyList.get(i), user);
			}
		}
	}
	/**
	 * 预算信息占用 应用于自动匹配功能，不自动生成收款信息
	 * @param vo
	 * @param user
	 */
	public void saveBisAccDtlBudgetProcessForAuto(BisAccDtlBudgetRuleDTO vo, ICurrentUser user) {
		List<String[]> itemCodeList = new ArrayList();
		List<String[]> itemNameList = new ArrayList();
		List<BigDecimal[]> processMoneyList = new ArrayList();
		if (StringUtils.isNotBlank(vo.getBudget())) {
			String[] budgets = String.valueOf(vo.getBudget()).split(",");
			if (!EmptyUtils.isEmptyArray(budgets)) {
				String[] itemCode = new String[budgets.length];
				String[] itemName = new String[budgets.length];
				BigDecimal[] processMoney = new BigDecimal[budgets.length];
				for (int i = 0; i < budgets.length; i++) {
					String[] budget = budgets[i].split(BudgetConstant.BUDGET_PROCESS_SPLIT);
					itemCode[i] = budget[0];
					itemName[i] = budget[1];
					processMoney[i] = new BigDecimal(budget[2]);
				}
				itemCodeList.add(itemCode);
				itemNameList.add(itemName);
				processMoneyList.add(processMoney);
			}
		}
		for (int i = 0; i < itemCodeList.size(); i++) {
			this.batchProcess(vo, itemCodeList.get(i), itemNameList.get(i), processMoneyList.get(i), user);
		}
	}
	/**
	 * 预算信息写入
	 *
	 * @param entity
	 * @param itemCode
	 * @param itemName
	 * @param processMoney
	 * @param user
	 */
	public void batchProcess(BisAccDtlBudgetRuleDTO entity, String[] itemCode, String[] itemName, BigDecimal[] processMoney, ICurrentUser user) {
		if (!EmptyUtils.isEmptyArray(itemCode)) {
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			for (int i = 0; i < itemCode.length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);//状态:11在途，95占用
				fbsBudgetProcessDTO.setBillNo(entity.getAccNo());//业务单据号
				fbsBudgetProcessDTO.setBillId(entity.getId());//保存流水ID
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);//科目代码
				fbsBudgetProcessDTO.setItemName(itemName[i]);//科目名称
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);//执行金额
				fbsBudgetProcessDTO.setProcessDate(DateUtils.str2Date(entity.getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));//执行日期
				fbsBudgetProcessDTO.setCorpId(entity.getCorpId());//单位id
				fbsBudgetProcessDTO.setCurId(entity.getCurId());//币别
				fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);//执行业务名称  内网收款" 外部系统付款";
				fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);//执行系统名称
				fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
				fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
				if(NisBusTypeConstant.CD_SIGN_PAYER.equals(entity.getCdSign())) {
					fbsBudgetProcessDTO.setPayerAcc(entity.getBankAcc());
					fbsBudgetProcessDTO.setPayeeAcc(entity.getOppAccNo());
					fbsBudgetProcessDTO.setPayeeAccName(entity.getOppAccName());
					fbsBudgetProcessDTO.setPayeeBank(entity.getOppAccBank());
				}else{
					fbsBudgetProcessDTO.setPayerAcc(entity.getOppAccNo());
					fbsBudgetProcessDTO.setPayeeAcc(entity.getBankAcc());
					fbsBudgetProcessDTO.setPayeeAccName(entity.getAccName());
					fbsBudgetProcessDTO.setPayeeBank(entity.getBankName());
				}
				fbsBudgetProcessDTO.setSettlType(entity.getSettlType());
				BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(entity.getBankAcc(), entity.getCurId());
				if (btBankAccDTO != null) {
					//校验是否对内付款
					// 1.对方账号是否为内部单位账号，且跟本方账户同网点
					BtBankAccDTO oppAcc = btBankAccService.getBankAccInfo(entity.getOppAccNo());
					boolean accIsTheSameNet = false;
					if(oppAcc != null){
						accIsTheSameNet = this.corpService.isTheSameNetByCorpId(btBankAccDTO.getCorpId(), oppAcc.getCorpId());
					}
					// 2.收款户名是否为系统单位，且跟付款方同网点
					CorpDTO corpDTO = this.corpService.getCorpIdByName(entity.getOppAccName());
					boolean corpIsTheSameNet = false;
					if (!EmptyUtils.isEmpty(corpDTO)) {
						corpIsTheSameNet = this.corpService.isTheSameNetByCorpId(entity.getCorpId(), corpDTO.getId());
					}
					if (accIsTheSameNet || corpIsTheSameNet) {
						fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_1);
					} else {
						//收付款账号或户名均不为内部成员单位信息,执行表数据里的【内转业务】字段为0，代表非内转业务的数据。
						fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_0);
					}
				} else {
					fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_0);
				}
				list.add(fbsBudgetProcessDTO);
			}
			// 修改时，先删除之前的计划执行数据再保存
			this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getId());
			this.fbsBudgetProcessService.writeBudgetProcess(list, user);
		}
	}
	/**
	 * 自动确认
	 * @param bisAccDtlBudgetRuleDTO
	 * @param user
	 * @param type 1.手工操作，2定时任务
	 * @throws Exception
	 */
	public void autoRule(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleDTO, ICurrentUser user,String type) throws Exception {
		String billNo = "";
		String bankSerialNo = bisAccDtlBudgetRuleDTO.getBankSerialNo();
		if (EmptyUtils.isBlank(bankSerialNo)) {
			bankSerialNo = bisAccDtlBudgetRuleDTO.getId();
		}
		List<NisReceivableDTO> nisReceivableDTOList = nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
		if(!EmptyUtils.isEmptyList(nisReceivableDTOList)) {
			for (NisReceivableDTO dto : nisReceivableDTOList) {
				billNo += "," + dto.getBillNo();
			}
			billNo = billNo.substring(1);
			bisAccDtlBudgetRuleDTO.setBillNo(billNo);
		}
		//得到所有的匹配规则
		//获取有校的业务类别
		List<NisBusTypeDTO> nisBusTypeDTOList = nisBusTypeService.getNisBusTypeAll(bisAccDtlBudgetRuleDTO.getCorpId(),bisAccDtlBudgetRuleDTO.getCdSign());
		//是否挂资金计划
		String budgetSel = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
		//先判断是否是资金调拨
		BtBankAccDTO bankacc = this.btBankAccService.getBankAccByBankAccAndCurId(bisAccDtlBudgetRuleDTO.getBankAcc(),bisAccDtlBudgetRuleDTO.getCurId());
		//对方账号信息
		BtBankAccDTO oppBankAcc = this.btBankAccService.getBankAccByBankAccAndCurId(bisAccDtlBudgetRuleDTO.getOppAccNo(),bisAccDtlBudgetRuleDTO.getCurId());
		//如果本方账号的父账号等于对方账号则是下拨
		if(!EmptyUtils.isEmpty(bankacc)&&StringUtils.isNotEmpty(bankacc.getPrentAccId()) &&
				!EmptyUtils.isEmpty(oppBankAcc) && bankacc.getPrentAccId().equals(oppBankAcc.getId())){
			List<NisBusTypeDTO> nisBusTypeDTOList1 = nisBusTypeService.getBySysBusType(bisAccDtlBudgetRuleDTO.getCorpId(),bisAccDtlBudgetRuleDTO.getCdSign(),"1");
			if(!EmptyUtils.isEmpty(nisBusTypeDTOList1) && nisBusTypeDTOList1.size()>0){
				String budget = "";
				bisAccDtlBudgetRuleDTO.setBusType(nisBusTypeDTOList1.get(0).getId());
				bisAccDtlBudgetRuleDTO.setSettlType(nisBusTypeDTOList1.get(0).getSettleType());
				if(EmptyUtils.isNotEmpty(nisBusTypeDTOList1.get(0).getItemCode()) && budgetSel.equals("1")) {
					if (this.fbsBudgetProcessService.isCorpHaveItemCode(bisAccDtlBudgetRuleDTO.getCorpId(), nisBusTypeDTOList1.get(0).getItemCode(), bisAccDtlBudgetRuleDTO.getTransTime())){
						budget = nisBusTypeDTOList1.get(0).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT + nisBusTypeDTOList1.get(0).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtlBudgetRuleDTO.getAmt();
						bisAccDtlBudgetRuleDTO.setBudget(budget);
					}
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}else if(budgetSel.equals("1") && (EmptyUtils.isEmpty(nisBusTypeDTOList1.get(0).getItemCode()))){
					if("1".equals(type)) {
						throw new ParameterException(NetbankMessageEnum.DTL_BUDGET_RULE_BUSTYPE_NO_ITEMCODE, nisBusTypeDTOList1.get(0).getBusType());
					}else{
						return;
					}
				}else if(budgetSel.equals("0")){
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}
			}else{
				//没有匹配上资金调拨的修改流水为已匹配以防总是扫描这些数据卡死
				this.sureUpdateStatusM(bisAccDtlBudgetRuleDTO);
			}
			return;
		}
		//如果对方账号是我们系统账号并且是本方账号的子账号则是上划
		if(!EmptyUtils.isEmpty(oppBankAcc) && bankacc.getId().equals(oppBankAcc.getPrentAccId())){
			List<NisBusTypeDTO> nisBusTypeDTOList1 = nisBusTypeService.getBySysBusType(bisAccDtlBudgetRuleDTO.getCorpId(),bisAccDtlBudgetRuleDTO.getCdSign(),"2");
			if(!EmptyUtils.isEmpty(nisBusTypeDTOList1) && nisBusTypeDTOList1.size()>0){
				String budget = "";
				bisAccDtlBudgetRuleDTO.setBusType(nisBusTypeDTOList1.get(0).getId());
				bisAccDtlBudgetRuleDTO.setSettlType(nisBusTypeDTOList1.get(0).getSettleType());
				if(EmptyUtils.isNotEmpty(nisBusTypeDTOList1.get(0).getItemCode())&& budgetSel.equals("1")) {
					if (this.fbsBudgetProcessService.isCorpHaveItemCode(bisAccDtlBudgetRuleDTO.getCorpId(), nisBusTypeDTOList1.get(0).getItemCode(), bisAccDtlBudgetRuleDTO.getTransTime())) {
						budget = nisBusTypeDTOList1.get(0).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT + nisBusTypeDTOList1.get(0).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtlBudgetRuleDTO.getAmt();
						bisAccDtlBudgetRuleDTO.setBudget(budget);
					}
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}else if(budgetSel.equals("1") && (EmptyUtils.isEmpty(nisBusTypeDTOList1.get(0).getItemCode()))){
					if("1".equals(type)) {
						throw new ParameterException(NetbankMessageEnum.DTL_BUDGET_RULE_BUSTYPE_NO_ITEMCODE, nisBusTypeDTOList1.get(0).getBusType());
					}else{
						return;
					}
				}else if(budgetSel.equals("0")){
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}
			}else{
				//没有匹配上资金调拨的修改流水为已匹配以防总是扫描这些数据卡死
				this.sureUpdateStatusM(bisAccDtlBudgetRuleDTO);
			}
			return;
		}
		//如果对方账号是我们系统账号并且是同单位的账号则是头调拨
		if(!EmptyUtils.isEmpty(oppBankAcc) && oppBankAcc.getCorpId().equals(bankacc.getCorpId())){
			List<NisBusTypeDTO> nisBusTypeDTOList1 = nisBusTypeService.getBySysBusType(bisAccDtlBudgetRuleDTO.getCorpId(),bisAccDtlBudgetRuleDTO.getCdSign(),"3");
			if(!EmptyUtils.isEmpty(nisBusTypeDTOList1) && nisBusTypeDTOList1.size()>0){
				String budget = "";
				bisAccDtlBudgetRuleDTO.setBusType(nisBusTypeDTOList1.get(0).getId());
				bisAccDtlBudgetRuleDTO.setSettlType(nisBusTypeDTOList1.get(0).getSettleType());
				if(EmptyUtils.isNotEmpty(nisBusTypeDTOList1.get(0).getItemCode())&& budgetSel.equals("1")) {
					if (this.fbsBudgetProcessService.isCorpHaveItemCode(bisAccDtlBudgetRuleDTO.getCorpId(), nisBusTypeDTOList1.get(0).getItemCode(), bisAccDtlBudgetRuleDTO.getTransTime())) {
						budget = nisBusTypeDTOList1.get(0).getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT + nisBusTypeDTOList1.get(0).getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtlBudgetRuleDTO.getAmt();
						bisAccDtlBudgetRuleDTO.setBudget(budget);
					}
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}else if(budgetSel.equals("1") && (EmptyUtils.isEmpty(nisBusTypeDTOList1.get(0).getItemCode()))){
					if("1".equals(type)) {
						throw new ParameterException(NetbankMessageEnum.DTL_BUDGET_RULE_BUSTYPE_NO_ITEMCODE, nisBusTypeDTOList1.get(0).getBusType());
					}else{
						return;
					}
				}else if(budgetSel.equals("0")){
					//自动确认
					this.sure(bisAccDtlBudgetRuleDTO, user);
				}
			}else{
				//没有匹配上资金调拨的修改流水为已匹配以防总是扫描这些数据卡死
				this.sureUpdateStatusM(bisAccDtlBudgetRuleDTO);
			}
			return;
		}
		//是否匹配到业务规则
		boolean isHas = false;
		for (NisBusTypeDTO nisBusTypeDTO : nisBusTypeDTOList){
			//是否满足匹配规则
			Boolean allCheck = false;
			List<Boolean> bList = new ArrayList<>();
			//同级间的关系匹配规则  01-或者  02-并且
			List<String> matchRuleList = new ArrayList<>();
			//取收款的业务类型
			if(NisBusTypeConstant.CD_SIGN_PAYEE.equals(nisBusTypeDTO.getCdSign())){
				List<List<NisBusTypeRuleDTO>>  nisBusTypeRuleDTOList =  nisBusTypeService.getNisBusTypeRuleDTOByBusId(nisBusTypeDTO.getId());
				for(List<NisBusTypeRuleDTO> list : nisBusTypeRuleDTOList) {
					Boolean isCheck = false;
					String matchRuleTemp = "";
					//判断是否是第一个
					int count =0;
					for (NisBusTypeRuleDTO nisBusTypeRuleDTO : list) {
						if (StringUtils.isNotEmpty(nisBusTypeRuleDTO.getMatchElements())) {
							String matchRule = nisBusTypeRuleDTO.getMatchRule();
							//取第一个的
							if(count ==0){
								matchRuleTemp = matchRule;
							}
							if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
								if(count>0 && !isCheck){
									continue;
								}
								count ++;
								/**
								 *  将属性的首字符大写，方便构造get，set方法
								 */
								String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
								if (upName.equals("CorpId")) {
									upName = "CorpName";
								}
								if (upName.equals("CurId")) {
									upName = "CurName";
								}
								Method m = bisAccDtlBudgetRuleDTO.getClass().getMethod("get" + upName, new Class[0]);
								if (!"Amt".equals(upName)) {
									// 调用getter方法获取属性值
									String value = (String) m.invoke(bisAccDtlBudgetRuleDTO);
									String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
									String matchContent = nisBusTypeRuleDTO.getMatchContent();
									if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(matchContent)) {
										//包含
										if ("1".equals(matchRelations)) {
											isCheck = value.contains(matchContent);
											//不包含
										} else if ("2".equals(matchRelations)) {
											isCheck = !value.contains(matchContent);
											//等于
										} else if ("3".equals(matchRelations)) {
											isCheck = value.equals(matchContent);
											//不等于
										} else if ("4".equals(matchRelations)) {
											isCheck = !value.equals(matchContent);
										}
									} else {
										isCheck = false;
									}
								} else {
									BigDecimal value = (BigDecimal) m.invoke(bisAccDtlBudgetRuleDTO);
									String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
									String matchContent = nisBusTypeRuleDTO.getMatchContent();
									if ("1".equals(matchRelations) || "2".equals(matchRelations)
											|| "3".equals(matchRelations) || "4".equals(matchRelations)) {
										//判断金额为整数
										if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
											int b = value.intValue();
											//判断是否为奇数
											if ("奇数".equals(matchContent)) {
												if (b % 2 != 0) {
													isCheck = true;
												} else {
													isCheck = false;
												}
												//判断是否为偶数
											} else if ("偶数".equals(matchContent)) {
												if (b % 2 == 0) {
													isCheck = true;
												} else {
													isCheck = false;
												}
											} else {
												isCheck = false;
											}
										} else {
											isCheck = false;
										}
									} else {
										BigDecimal matchAmt = BigDecimal.ZERO;
										try{
											matchAmt = new BigDecimal(matchContent);
										}catch (Exception e) {
											matchContent = null;
										}
										if (StringUtils.isBlank(matchContent)) {
											isCheck = false;
										} else {
											if ("5".equals(matchRelations)) {
												//大于
												isCheck = value.compareTo(matchAmt)>0 ? true:false;
											} else if ("6".equals(matchRelations)) {
												//大于等于
												isCheck = value.compareTo(matchAmt)>=0 ? true:false;
											} else if ("7".equals(matchRelations)) {
												//小于
												isCheck = value.compareTo(matchAmt)<0 ? true:false;
												//小于等于
											} else if ("8".equals(matchRelations)) {
												isCheck = value.compareTo(matchAmt)<=0 ? true:false;
											} else {
												isCheck = false;
											}
										}
									}
								}
							} else if (matchRule.equals("01")) {
								if (isCheck) {
								} else {
									/**
									 *  将属性的首字符大写，方便构造get，set方法
									 */
									String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
									if (upName.equals("CorpId")) {
										upName = "CorpName";
									}
									if (upName.equals("CurId")) {
										upName = "CurName";
									}
									Method m = bisAccDtlBudgetRuleDTO.getClass().getMethod("get" + upName, new Class[0]);
									if (!"Amt".equals(upName)) {
										// 调用getter方法获取属性值
										String value = (String) m.invoke(bisAccDtlBudgetRuleDTO);
										String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
										String matchContent = nisBusTypeRuleDTO.getMatchContent().trim();
										if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(matchContent)) {
											value = value.trim();
											//包含
											if ("1".equals(matchRelations)) {
												isCheck = value.contains(matchContent);
												//不包含
											} else if ("2".equals(matchRelations)) {
												isCheck = !value.contains(matchContent);
												//等于
											} else if ("3".equals(matchRelations)) {
												isCheck = value.equals(matchContent);
												//不等于
											} else if ("4".equals(matchRelations)) {
												isCheck = !value.equals(matchContent);
											}
										}
									} else {
										BigDecimal value = (BigDecimal) m.invoke(bisAccDtlBudgetRuleDTO);
										String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
										String matchContent = nisBusTypeRuleDTO.getMatchContent();
										if ("1".equals(matchRelations) || "2".equals(matchRelations)
												|| "3".equals(matchRelations) || "4".equals(matchRelations)) {
											//判断金额为整数
											if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
												int b = value.intValue();
												//判断是否为奇数
												if ("奇数".equals(matchContent)) {
													if (b % 2 != 0) {
														isCheck = true;
													}
													//判断是否为偶数
												} else if ("偶数".equals(matchContent)) {
													if (b % 2 == 0) {
														isCheck = true;
													}
												}
											}
										} else {
											BigDecimal matchAmt = BigDecimal.ZERO;
											try{
												matchAmt = new BigDecimal(matchContent);
											}catch (Exception e) {
												matchContent = null;
											}
											if (StringUtils.isBlank(matchContent)) {
												isCheck = false;
											} else {
												if ("5".equals(matchRelations)) {
													//大于
													isCheck = value.compareTo(matchAmt)>0 ? true:false;
												} else if ("6".equals(matchRelations)) {
													//大于等于
													isCheck = value.compareTo(matchAmt)>=0 ? true:false;
												} else if ("7".equals(matchRelations)) {
													//小于
													isCheck = value.compareTo(matchAmt)<0 ? true:false;
													//小于等于
												} else if ("8".equals(matchRelations)) {
													isCheck = value.compareTo(matchAmt)<=0 ? true:false;
												} else {
													isCheck = false;
												}
											}
										}
									}
								}
							}
						}
					}
					bList.add(isCheck);
					matchRuleList.add(matchRuleTemp);
				}
				for(int i =0;i<bList.size();i++){
					Boolean b = bList.get(i);
					String matchRule = matchRuleList.get(i);
					if(i == 0){
						allCheck = b;
					}else{
						if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
							if(allCheck && b){
								allCheck = true;
							}else{
								allCheck = false;
							}
						}else{
							if(b || allCheck){
								allCheck = true;
							}else{
								allCheck = false;
							}
						}
					}
				}
				if(allCheck){
					String budget = "";
					if(StringUtils.isNotEmpty(nisBusTypeDTO.getItemCode())&& budgetSel.equals("1")){
						if (this.fbsBudgetProcessService.isCorpHaveItemCode(bisAccDtlBudgetRuleDTO.getCorpId(), nisBusTypeDTO.getItemCode(), bisAccDtlBudgetRuleDTO.getTransTime())) {
							budget = nisBusTypeDTO.getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT + nisBusTypeDTO.getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtlBudgetRuleDTO.getAmt();
							bisAccDtlBudgetRuleDTO.setBudget(budget);
						}
					}
					bisAccDtlBudgetRuleDTO.setBusType(nisBusTypeDTO.getId());
					bisAccDtlBudgetRuleDTO.setSettlType(nisBusTypeDTO.getSettleType());
					if(EmptyUtils.isNotEmpty(budget)){
						this.sure(bisAccDtlBudgetRuleDTO,user);
					}else {
						//自动确认
						this.sureNotUpdateStatus(bisAccDtlBudgetRuleDTO, user);
					}
					isHas = true;
					break;
				}
			}
		}
		//如果没有匹配上规则就清空业务类型
		if(!isHas){
			bisAccDtlBudgetRuleDTO.setBusType("");
			bisAccDtlBudgetRuleDTO.setSettlType("");
			//删除计划
            this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, bisAccDtlBudgetRuleDTO.getId());
			//自动确认
			this.sureNotUpdateStatus(bisAccDtlBudgetRuleDTO,user);
		}
	}
	/**
	 * 判断是否是合并单据
	 *
	 * @param id
	 * @return
	 */
	public boolean isMergeBill(String id) {
		return  this.bisAccDtlService.isMergeBill(id);
	}
	/**
	 * 拆分保存
	 * @param
	 * @param amts
	 * @param user
	 */
	public void split(com.bytter.biz.bdlink.api.dto.BisAccDtlDTO t , List<BigDecimal> amts, ICurrentUser user){
		//生成拆分单据
		for (int i = 0; i < amts.size(); i++) {
			com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = new com.bytter.biz.bdlink.api.dto.BisAccDtlDTO();
			BeanUtils.copyNotNullProperties(t, bisAccDtlDTO);
			bisAccDtlDTO.setId(null);
			bisAccDtlDTO.setAmt(amts.get(i));
			//保存拆分前单号
			bisAccDtlDTO.setSplitId(t.getId());
			bisAccDtlDTO.setCreateBy(user.getLoginName());
			bisAccDtlDTO.setCreateDate(DateUtils.getDate());
			bisAccDtlService.saveDtlByDto(bisAccDtlDTO);
		}
		//更新原单状态为删除
		t.setStatus(NetbankConstant.STATUE_DEL);
		t.setUpdateBy(user.getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		bisAccDtlService.updateDtlByDto(t);
		// 释放原单挂接的计划
		fbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, t.getId());
	}
	/**
	 * 定时任务 自动认领收款流水定时任务
	 * @param arguments
	 */
	@Override
	@XxlJob("BisAccDtlBudgetRuleService")
	public void run(String arguments) {
		try {
			this.logger.info("---启用自动认领收款流水定时任务---");
			this.logger.info("启动参数arguments：{}",arguments);
			DataTablesInput dataTablesInput = new DataTablesInput();
			//一次默认600条
			int size = 600;
			//后台任务每批次执行笔数
			String autoCount = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_COLLECTIONAUTOCOUNT).getValue();
			if(EmptyUtils.isNotEmpty(autoCount)){
				size = Integer.parseInt(autoCount);
			}
			dataTablesInput.setLength(size);

			String dayCountStr = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_DAYCOUNT).getValue();
			//默认查询3天的流水
			int dayCount = 3;
			if(EmptyUtils.isNotEmpty(dayCountStr)){
				dayCount = Integer.parseInt(dayCountStr);
			}

			//获取当前时间 获取开始时间和结束时间，开始时间为当天时间减少一天，结束时间为当前时间
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -dayCount);
			String satausDate=DateUtils.formatDate(calendar.getTime(),DateUtils.PATTERN_CLASSICAL_SIMPLE);
			String endDate=DateUtils.formatDate(new Date(),DateUtils.PATTERN_CLASSICAL_SIMPLE);

			//如果传递有参数值,只用传递的值查询时间范围格式yyyy-MM-dd HH:mm:ss
			/*if(StringUtils.isNotEmpty(arguments)){
				String[] parts = arguments.split(",");
				 satausDate=parts[0];
				 endDate=parts[1];
			}*/

			Map<String, Object> map = new HashMap<>();
			map.put("eqs_cdSign","0");
			map.put("eqs_fbsStatus","0");
			map.put("ged_transTime",satausDate);
			map.put("led_transTime",endDate);
			dataTablesInput.setFormdata(map);
			DataTablesOutput output = bisAccDtlService.getDataTablesUnion(dataTablesInput, "");
			//默认管理员执行
			UserDTO userDTO = this.userService.getByLoginName("admin");
			ShiroUser user = new ShiroUser(userDTO.getId(),userDTO.getLoginName(),userDTO.getUsername());
			if (null != output.getData()) {
				for (int i = 0; i < output.getData().size(); i++) {
					this.logger.info("执行认领数据数量：{}，正在执行第：{}笔",output.getData().size(),i+1);
					BisAccDtlBudgetRuleDTO vo = (BisAccDtlBudgetRuleDTO) output.getData().get(i);
					vo.setProcessDateTime(vo.getTransTime());
					vo.setMessageType(BtBankAccMessageConstant.MESSAGE_TYPE_TASK);
					this.autoRule(vo,user,"2");
				}
			}
			this.logger.info("---结束自动认领收款流水定时任务---");
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@XxlJob("BisAccDtlBudgetRulePayService")
	public void bisAccDtlBudgetRulePayService(String arguments) {
		try {
			this.logger.info("---启用自动认领其他付款流水定时任务---");
			DataTablesInput dataTablesInput = new DataTablesInput();
			//一次只查300条
			dataTablesInput.setLength(600);
			Map<String, Object> map = new HashMap<>();
			map.put("eqs_cdSign","1");
			map.put("eqs_fbsStatus","0");
			dataTablesInput.setFormdata(map);
			DataTablesOutput output = bisAccDtlService.getDataTablesUnion(dataTablesInput, "");
			//默认管理员执行
			UserDTO userDTO = this.userService.getByLoginName("admin");
			if(null == userDTO){
				userDTO = new UserDTO();
				userDTO.setId("402880425b6b0a58015b6b0abeb10000");
				userDTO.setLoginName("admin");
				userDTO.setUsername("admin");
			}
			ShiroUser user = new ShiroUser(userDTO.getId(),userDTO.getLoginName(),userDTO.getUsername());
			if (null != output.getData()) {
				for (int i = 0; i < output.getData().size(); i++) {
					BisAccDtlBudgetRuleDTO vo = (BisAccDtlBudgetRuleDTO) output.getData().get(i);
					vo.setProcessDateTime(vo.getTransTime());
					vo.setMessageType(BtBankAccMessageConstant.MESSAGE_TYPE_TASK);
					autoRuleOther(vo,user);
				}
			}
			this.logger.info("---结束自动认领其他付款流水定时任务---");
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据合并id查到流水明细
	 * @param mergeId
	 * @return
	 */
	public List<BisAccDtlBudgetRuleDTO> getBisAccDtlByMergeId(String mergeId){
		return this.bisAccDtlBudgetRuleService.getBisAccDtlByMergeId(mergeId);
	}
	/**
	 * 自动确认
	 * @param bisAccDtlBudgetRuleVO
	 * @param user
	 * @throws Exception
	 */
	public void autoRuleOther(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO, ICurrentUser user) throws Exception {
		//获取有校的业务类别
		List<NisBusTypeDTO> nisBusTypeDTOList = nisBusTypeService.getNisBusTypeAll(bisAccDtlBudgetRuleVO.getCorpId(),bisAccDtlBudgetRuleVO.getCdSign());
		//是否挂资金计划
		String budgetSel = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
		//是否匹配到业务规则
		boolean isHas = false;
		for (NisBusTypeDTO nisBusTypeDTO : nisBusTypeDTOList){
			//是否满足匹配规则
			Boolean allCheck = false;
			List<Boolean> bList = new ArrayList<>();
			//同级间的关系匹配规则  01-或者  02-并且
			List<String> matchRuleList = new ArrayList<>();
			//取收款的业务类型
			if(nisBusTypeDTO.getCdSign().equals(NisBusTypeConstant.CD_SIGN_PAYER)){
				List<List<NisBusTypeRuleDTO>>  nisBusTypeRuleDTOList =  nisBusTypeService.getNisBusTypeRuleDTOByBusId(nisBusTypeDTO.getId());
				for(List<NisBusTypeRuleDTO> list : nisBusTypeRuleDTOList) {
					Boolean isCheck = false;
					String matchRuleTemp = "";
					int count = 0;
					for (NisBusTypeRuleDTO nisBusTypeRuleDTO : list) {
						if (StringUtils.isNotEmpty(nisBusTypeRuleDTO.getMatchElements())) {
							String matchRule = nisBusTypeRuleDTO.getMatchRule();
							//取第一个的
							if(count ==0){
								matchRuleTemp = matchRule;
							}
							if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
								if(count>0 && !isCheck){
									continue;
								}
								count ++;
								/**
								 *  将属性的首字符大写，方便构造get，set方法
								 */
								String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
								if (upName.equals("CorpId")) {
									upName = "CorpName";
								}
								if (upName.equals("CurId")) {
									upName = "CurName";
								}
								Method m = bisAccDtlBudgetRuleVO.getClass().getMethod("get" + upName, new Class[0]);
								if (!"Amt".equals(upName)) {
									// 调用getter方法获取属性值
									String value = (String) m.invoke(bisAccDtlBudgetRuleVO);
									String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
									String matchContent = nisBusTypeRuleDTO.getMatchContent();
									if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(matchContent)) {
										//包含
										if ("1".equals(matchRelations)) {
											isCheck = value.contains(matchContent);
											//不包含
										} else if ("2".equals(matchRelations)) {
											isCheck = !value.contains(matchContent);
											//等于
										} else if ("3".equals(matchRelations)) {
											isCheck = value.equals(matchContent);
											//不等于
										} else if ("4".equals(matchRelations)) {
											isCheck = !value.equals(matchContent);
										}
									} else {
										isCheck = false;
									}
								}else{
									BigDecimal value = (BigDecimal) m.invoke(bisAccDtlBudgetRuleVO);
									String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
									String matchContent = nisBusTypeRuleDTO.getMatchContent();
									if ("1".equals(matchRelations) || "2".equals(matchRelations)
											|| "3".equals(matchRelations) || "4".equals(matchRelations)) {
										//判断金额为整数
										if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
											int b = value.intValue();
											//判断是否为奇数
											if ("奇数".equals(matchContent)) {
												if (b % 2 != 0) {
													isCheck = true;
												} else {
													isCheck = false;
												}
												//判断是否为偶数
											} else if ("偶数".equals(matchContent)) {
												if (b % 2 == 0) {
													isCheck = true;
												} else {
													isCheck = false;
												}
											} else {
												isCheck = false;
											}
										} else {
											isCheck = false;
										}
									} else {
										BigDecimal matchAmt = BigDecimal.ZERO;
										try{
											matchAmt = new BigDecimal(matchContent);
										}catch (Exception e) {
											matchContent = null;
										}
										if (StringUtils.isBlank(matchContent)) {
											isCheck = false;
										} else {
											if ("5".equals(matchRelations)) {
												//大于
												isCheck = value.compareTo(matchAmt)>0 ? true:false;
											} else if ("6".equals(matchRelations)) {
												//大于等于
												isCheck = value.compareTo(matchAmt)>=0 ? true:false;
											} else if ("7".equals(matchRelations)) {
												//小于
												isCheck = value.compareTo(matchAmt)<0 ? true:false;
												//小于等于
											} else if ("8".equals(matchRelations)) {
												isCheck = value.compareTo(matchAmt)<=0 ? true:false;
											} else {
												isCheck = false;
											}
										}
									}
								}
							} else if (matchRule.equals("01")) {
								if (isCheck) {
								} else {
									/**
									 *  将属性的首字符大写，方便构造get，set方法
									 */
									String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
									if (upName.equals("CorpId")) {
										upName = "CorpName";
									}
									if (upName.equals("CurId")) {
										upName = "CurName";
									}
									Method m = bisAccDtlBudgetRuleVO.getClass().getMethod("get" + upName, new Class[0]);
									if (!"Amt".equals(upName)) {
										// 调用getter方法获取属性值
										String value = (String) m.invoke(bisAccDtlBudgetRuleVO);
										String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
										String matchContent = nisBusTypeRuleDTO.getMatchContent().trim();
										if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(matchContent)) {
											value = value.trim();
											//包含
											if ("1".equals(matchRelations)) {
												isCheck = value.contains(matchContent);
												//不包含
											} else if ("2".equals(matchRelations)) {
												isCheck = !value.contains(matchContent);
												//等于
											} else if ("3".equals(matchRelations)) {
												isCheck = value.equals(matchContent);
												//不等于
											} else if ("4".equals(matchRelations)) {
												isCheck = !value.equals(matchContent);
											}
										}
									}else{
										BigDecimal value = (BigDecimal) m.invoke(bisAccDtlBudgetRuleVO);
										String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
										String matchContent = nisBusTypeRuleDTO.getMatchContent();
										if ("1".equals(matchRelations) || "2".equals(matchRelations)
												|| "3".equals(matchRelations) || "4".equals(matchRelations)) {
											//判断金额为整数
											if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
												int b = value.intValue();
												//判断是否为奇数
												if ("奇数".equals(matchContent)) {
													if (b % 2 != 0) {
														isCheck = true;
													}
													//判断是否为偶数
												} else if ("偶数".equals(matchContent)) {
													if (b % 2 == 0) {
														isCheck = true;
													}
												}
											}
										} else {
											BigDecimal matchAmt = BigDecimal.ZERO;
											try{
												matchAmt = new BigDecimal(matchContent);
											}catch (Exception e) {
												matchContent = null;
											}
											if (StringUtils.isBlank(matchContent)) {
												isCheck = false;
											} else {
												if ("5".equals(matchRelations)) {
													//大于
													isCheck = value.compareTo(matchAmt)>0 ? true:false;
												} else if ("6".equals(matchRelations)) {
													//大于等于
													isCheck = value.compareTo(matchAmt)>=0 ? true:false;
												} else if ("7".equals(matchRelations)) {
													//小于
													isCheck = value.compareTo(matchAmt)<0 ? true:false;
													//小于等于
												} else if ("8".equals(matchRelations)) {
													isCheck = value.compareTo(matchAmt)<=0 ? true:false;
												} else {
													isCheck = false;
												}
											}
										}
									}
								}
							}
						}
					}
					bList.add(isCheck);
					matchRuleList.add(matchRuleTemp);
				}
				for(int i =0;i<bList.size();i++){
					Boolean b = bList.get(i);
					String matchRule = matchRuleList.get(i);
					if(i == 0){
						allCheck = b;
					}else{
						if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
							if(allCheck && b){
								allCheck = true;
							}else{
								allCheck = false;
							}
						}else{
							if(b || allCheck){
								allCheck = true;
							}else{
								allCheck = false;
							}
						}
					}
				}
				if(allCheck){
					String budget = "";
					if(StringUtils.isNotEmpty(nisBusTypeDTO.getItemCode())&& budgetSel.equals("1")){
						if (this.fbsBudgetProcessService.isCorpHaveItemCode(bisAccDtlBudgetRuleVO.getCorpId(), nisBusTypeDTO.getItemCode(), bisAccDtlBudgetRuleVO.getTransTime())) {
							budget = nisBusTypeDTO.getItemCode() + BudgetConstant.BUDGET_PROCESS_SPLIT + nisBusTypeDTO.getItemName() + BudgetConstant.BUDGET_PROCESS_SPLIT + bisAccDtlBudgetRuleVO.getAmt();
							bisAccDtlBudgetRuleVO.setBudget(budget);
						}
					}
					bisAccDtlBudgetRuleVO.setBusType(nisBusTypeDTO.getId());
					bisAccDtlBudgetRuleVO.setSettlType(nisBusTypeDTO.getSettleType());
					//自动确认
					if(StringUtils.isNotEmpty(budget)){
						this.sure(bisAccDtlBudgetRuleVO,user);
					}else {
						this.sureNotUpdateStatus(bisAccDtlBudgetRuleVO, user);
					}
					isHas = true;
					break;
				}
			}
		}
		//如果没有匹配上规则就清空业务类型
		if(!isHas){
			bisAccDtlBudgetRuleVO.setBusType("");
			bisAccDtlBudgetRuleVO.setSettlType("");
			//删除计划
			this.fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_BISACCDTL, BudgetConstant.PROCESS_SYSNAME_NIS, bisAccDtlBudgetRuleVO.getId());
			//自动确认
			this.sureNotUpdateStatus(bisAccDtlBudgetRuleVO,user);
		}
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput exportAll(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, user.getId()), "t.corpId",
				true);
		sql.append(" and ").append(corpAuthSql);
		//权限控制类型  1-单位级 2-账号级
		String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (!CommonConstant.YES.equals(controlType)) {
			//设置账号授权控制
			List<String> accountGrantIdList = accountGrantService.listByMultiValAccIds(dataTablesInput.getCurrentUserId(), ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY, null,
					AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
					AccountGrantConstant.QUERY_DETAIL);
			List<String> bankAccGrantList = null;
			List<BtBankAccDTO> accountGrantList = btBankAccService.getBankAccNotUnionByIdList(accountGrantIdList);
			if (ListUtils.isNotNullOrEmpty(accountGrantList)) {
				bankAccGrantList = new ArrayList<>();
				for (int i = 0; i < accountGrantList.size(); i++) {
					BtBankAccDTO accDTO = accountGrantList.get(i);
					bankAccGrantList.add(accDTO.getBankAcc());
				}
			}
			String accAuthSql = SqlUtil.getInSql(bankAccGrantList, "t.bankAcc",
					true);
			sql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput output = bisAccDtlBudgetRuleService.getDataTablesUnion(dataTablesInput, sql.toString());
		return output;
	}
	public void splitCancel(String id, ICurrentUser user){
		bisAccDtlBudgetRuleService.splitCancel(id, user);
	}
	public void merge(String ids, ICurrentUser user){
		bisAccDtlBudgetRuleService.merge(ids, user);
	}
	public void mergeCancel(String ids, ICurrentUser user){
		bisAccDtlBudgetRuleService.mergeCancel(ids, user);
	}
}
