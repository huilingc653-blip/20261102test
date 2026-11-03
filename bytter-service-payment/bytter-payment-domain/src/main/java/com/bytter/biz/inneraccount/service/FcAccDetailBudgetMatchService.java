package com.bytter.biz.inneraccount.service;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.service.IFcAccDetailBudgetMatchService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户流水
 * @date 2017-06-23 19:36:12
 */
@Service("fcAccDetailBudgetMatchService")
@Transactional
public class FcAccDetailBudgetMatchService extends AbstractService {
	@DubboReference(check = false)
	private IFcAccDetailBudgetMatchService fcAccDetailBudgetMatchService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
	/**
	 * 查询内部账号流水列表
	 *
	 * @param dataTablesInput
	 * @return
	 * 没有被调用，注释
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		//查询内部账户
		StringBuilder sql = new StringBuilder();
		List<String> corpIdS= corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY, user.getId());
		List<FcAccDTO> fcaccList = fcAccService.listFcAccInfo(corpIdS,null);
		Map<String, FcAccDTO> fcAccMap = fcaccList.stream()
				.collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		DataTablesOutput output = fcAccDetailBudgetMatchService.getDataTablesUnion(dataTablesInput,user);
		List<FcAccDetailBudgetMatchVO> fcAccDetailBudgetMatchVOList = output.getData();
		List<FcAccDetailBudgetMatchVO> voList = output.getData();
		for(FcAccDetailBudgetMatchVO vo:voList){
			FcAccDTO fcAcc = fcAccMap.get(vo.getBlnctrAcc());
			if(null != fcAcc &&null!=fcAccMap.get(vo.getAccount())){
				if("30".equals(vo.getBusOriType())){
					if("01".equals(fcAcc.getAccType()) ){
						fcAccDetailBudgetMatchVOList.add(vo);
					}
				}else {
					fcAccDetailBudgetMatchVOList.add(vo);
				}
			}
			if(StringUtils.isEmpty(vo.getFbsStatus())){
				vo.setFbsStatus("0");
				vo.setFbsStatusName("未确认");
			}
		}
		output.setData(fcAccDetailBudgetMatchVOList);
		this.setItem(output);
		return output;
	}
	/**
	 * 设置计划科目
	 *
	 * @param output
	 */
	public void setItem(DataTablesOutput output) {
		if (null != output.getData()) {
			for (int i = 0; i < output.getData().size(); i++) {
				FcAccDetailBudgetMatchVO vo = (FcAccDetailBudgetMatchVO) output.getData().get(i);
				String billNo = vo.getVoucherNo();
				if (EmptyUtils.isBlank(billNo)) {
					billNo = vo.getId();
				}
				billNo = billNo + "_" + vo.getCdSign();
				List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
				if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
					vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
				}
				this.setItemInfo(vo, fbsBudgetProcessDTOList);
				BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
				vo.setCurExrate(curExrate);
			}
		}
	}


	public void setItem(FcAccDetailBudgetMatchVO vo) {
		if(vo!=null){
			String billNo = vo.getVoucherNo();
			if (EmptyUtils.isBlank(billNo)) {
				billNo = vo.getId();
			}
			billNo = billNo + "_" + vo.getCdSign();
			List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
			if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
				vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
			}
			this.setItemInfo(vo, fbsBudgetProcessDTOList);
			BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
			vo.setCurExrate(curExrate);
		}
	}
	/**
	 * 设置计划科目详情
	 *
	 * @param fbsBudgetProcessDTOList
	 */
	public void setItemInfo(FcAccDetailBudgetMatchVO vo, List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList) {
		StringBuilder item = new StringBuilder();
		StringBuilder itemInfoText = new StringBuilder();
		if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
			for (int j = 0; j < fbsBudgetProcessDTOList.size(); j++) {
				if (j == 0) {
					item.append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
					item.append(fbsBudgetProcessDTOList.get(j).getItemName()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
					item.append(fbsBudgetProcessDTOList.get(j).getProcessMoney().toString());
					itemInfoText.append("(").append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(")")
							.append(fbsBudgetProcessDTOList.get(j).getItemName());
				} else {
					item.append(CommonConstant.COMMA_SPLIT).append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
					item.append(fbsBudgetProcessDTOList.get(j).getItemName()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
					item.append(fbsBudgetProcessDTOList.get(j).getProcessMoney().toString());
					itemInfoText.append(CommonConstant.COMMA_SPLIT).append("(").append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(")")
							.append(fbsBudgetProcessDTOList.get(j).getItemName());
				}
			}
		}
		vo.setItemInfo(item.toString());
		vo.setItemInfoText(itemInfoText.toString());
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
	 * 预算信息占用
	 *
	 * @param vo
	 * @param user
	 */
	public void saveFcAccBookBudgetProcess(FcAccDetailBudgetMatchVO vo, ICurrentUser user) {
//		if (this.isBudget(vo.getCorpId())) {
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
//		}
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
	public void batchProcess(FcAccDetailBudgetMatchVO entity, String[] itemCode, String[] itemName, BigDecimal[] processMoney, ICurrentUser user) {
		if (!EmptyUtils.isEmptyArray(itemCode)) {
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			String billNo = entity.getVoucherNo();
			if (EmptyUtils.isBlank(billNo)) {
				billNo = entity.getId();
			}
			billNo = billNo + "_" + entity.getCdSign();
			for (int i = 0; i < itemCode.length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);//状态:11在途，95占用
				fbsBudgetProcessDTO.setBillNo(billNo);//业务单据号
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);//科目代码
				fbsBudgetProcessDTO.setItemName(itemName[i]);//科目名称
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);//执行金额
				fbsBudgetProcessDTO.setProcessDate(DateUtils.str2Date(entity.getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));//执行日期
				fbsBudgetProcessDTO.setCorpId(entity.getCorpId());//单位id
				fbsBudgetProcessDTO.setCurId(entity.getCurId());//币别
				fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL);//执行业务名称  内网收款" 外部系统付款";
				fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);//执行系统名称
				fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
				fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
				if (BisAccBalConstant.CDSIGN_IN.equals(entity.getCdSign())) {//收入 收款账号是本方账号 付款账号是对方账号
					fbsBudgetProcessDTO.setPayerAcc(entity.getOppAccNo());
					fbsBudgetProcessDTO.setPayeeAcc(entity.getBlnctrAcc());
					fbsBudgetProcessDTO.setPayeeAccName(entity.getAccName());
				} else {//支出 收款账号是对方账号 付款账号是本方账号
					fbsBudgetProcessDTO.setPayerAcc(entity.getBlnctrAcc());
					fbsBudgetProcessDTO.setPayeeAcc(entity.getOppAccNo());
					fbsBudgetProcessDTO.setPayeeAccName(entity.getOppAccName());
				}
				if (fcAccService.isTheSameNetByFcAcc(entity.getBlnctrAcc(), entity.getOppAccNo())) {//目前对方账号为空，前提需为同一网点下的成员单位）
					fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_1);
				}
				list.add(fbsBudgetProcessDTO);
			}
			// 修改时，先删除之前的计划执行数据再保存
			this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
			this.fbsBudgetProcessService.writeBudgetProcess(list, user);
		}
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
	public void batchBusiness(String ids, String itemCode, String itemName, String processDate, ICurrentUser user, String type) {
		if (StringUtils.isNotBlank(type) && BisAccBalConstant.TYPE_D.equals(type)) {
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {//删除
				FcAccDetailBudgetMatchVO vo = this.getInfoById(id);
				this.business(id, user, BisAccBalConstant.FBS_STATUS_D, this.isDayCloseStatus(vo.getDayCloseSign()));
			}
		} else {
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {//确认
				FcAccDetailBudgetMatchVO vo = fcAccDetailBudgetMatchService.getInfoById(id);
				fcAccbookService.updateFcAccFbsStatus(id, "1", this.isDayCloseStatus(vo.getDayCloseSign()));
				if (BisAccBalConstant.CDSIGN_IN.equals(vo.getCdSign())) {//0 收 挂贷方 1 支 挂借方
					vo.setBudget(itemCode + BudgetConstant.BUDGET_PROCESS_SPLIT + itemName + BudgetConstant.BUDGET_PROCESS_SPLIT + vo.getCreditMoney());
				} else {
					vo.setBudget(itemCode + BudgetConstant.BUDGET_PROCESS_SPLIT + itemName + BudgetConstant.BUDGET_PROCESS_SPLIT + vo.getDebitMoney());
				}
				vo.setProcessDate(processDate);
				this.saveFcAccBookBudgetProcess(vo, user);
			}
		}
	}
	/**
	 * 逻辑业务
	 *
	 * @param id
	 * @param user
	 * @param type 0 恢复 1还原 2删除
	 */
	public void business(String id, ICurrentUser user, String type, boolean dayCloseSign) {
		if (StringUtils.isNotBlank(type)) {
			if (BisAccBalConstant.TYPE_H.equals(type)) {//恢复
				fcAccbookService.updateFcAccFbsStatus(id, BisAccBalConstant.FBS_STATUS_N, dayCloseSign);
			} else if (BisAccBalConstant.TYPE_R.equals(type)) {//还原
				fcAccbookService.updateFcAccFbsStatus(id, BisAccBalConstant.FBS_STATUS_N, dayCloseSign);
			} else {//删除
				fcAccbookService.updateFcAccFbsStatus(id, BisAccBalConstant.FBS_STATUS_D, dayCloseSign);
			}
			FcAccDetailBudgetMatchVO vo = this.getInfoById(id);
			String billNo = vo.getVoucherNo();
			if (EmptyUtils.isBlank(billNo)) {
				billNo = vo.getId();
			}
			billNo = billNo + "_" + vo.getCdSign();
			List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
			if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {//如果没有预算执行信息，不处理，只修改流水计划状态
				fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo, -2, user);
			}
		}
	}
	/**
	 * 是否已日结
	 *
	 * @param sign 1 未日结 2 已日结
	 * @return
	 */
	public boolean isDayCloseStatus(String sign) {
		if ("1".equals(sign)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 根据id查找明细信息
	 *
	 * @param id
	 * @return
	 */
	public FcAccDetailBudgetMatchVO getInfoById(String id) {
		return fcAccDetailBudgetMatchService.getInfoById(id);
	}

	public FcAccDetailBudgetMatchVO getItemInfoById(String id) {
		FcAccDetailBudgetMatchVO info = fcAccDetailBudgetMatchService.getInfoById(id);
		this.setItem(info);
		return info;
	}
}
