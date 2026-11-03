package com.bytter.biz.expense.controller;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.enums.ExpenseMessageEnum;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 费用报销表
 * @date 2017-05-31 16:04:01
 */
@Controller
@RequestMapping("/expense/nisexpensedetail")
public class NisExpenseDetailController extends BaseController {
	@Autowired
	private NisExpenseDetailService nisExpenseDetailService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IConfigService configService;
	/**
	 * 费用报销表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHQUERY)
	@LogMessage(content = "查询费用报销表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "expense/listNisExpenseDetail";
	}
	/**
	 * 费用报销表 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHQUERY,
//			ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTQUERY,
//			ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_QUERY},
//			logical = Logical.OR)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return nisExpenseDetailService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 费用报销表新增页面跳转
	 *
	 * @return String
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@GetMapping("/add")
	public String add(NisExpenseDetail nisExpenseDetail, Model model) {
		if (StringUtils.isNotEmpty(nisExpenseDetail.getId())) {
			nisExpenseDetail = nisExpenseDetailService.get(nisExpenseDetail.getId());
			model.addAttribute("nisExpenseDetailPage", nisExpenseDetail);
		}
		//初始化系统参数
		this.initConfig(model);
		return "expense/addNisExpenseDetail";
	}
	/**
	 * 费用报销表编辑页面跳转
	 *
	 * @return String
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@GetMapping("/update")
	public String update(NisExpenseDetail nisExpenseDetail, Model model) {
		if (StringUtils.isNotEmpty(nisExpenseDetail.getId())) {
			nisExpenseDetail = nisExpenseDetailService.get(nisExpenseDetail.getId(), true);
			//暂存和编辑状态才可以进行该操作
			if (nisExpenseDetail != null && !NetbankConstant.STATUE_TEMP.equals(nisExpenseDetail.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisExpenseDetail.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
			}
			model.addAttribute("nisExpenseDetailPage", nisExpenseDetail);
		}
		//资金计划
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EXPENSE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
		//初始化系统参数
		this.initConfig(model);
		return "expense/updateNisExpenseDetail";
	}
	/**
	 * 初始化系统参数
	 * @param model
	 */
	private void initConfig(Model model){
		model.addAttribute("budgetSel", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue());
		model.addAttribute("payItem", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_PAYITEM).getValue());
		model.addAttribute("expFbsin", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue());
		model.addAttribute("accountOver", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_ACCOUNTOVER).getValue());
		model.addAttribute("bankaccOver", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BANKACCOVER).getValue());
		model.addAttribute("showIsPayed", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
		//是否启用限额管理  1-是 0-否
		model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS,SysConfigConstant.ISOPENLIMIT).getValue());
	}
	/**
	 * 费用报销表详情页
	 *
	 * @return String
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHQUERY)
	@LogMessage(content = "查询费用报销表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
		model.addAttribute("nisExpenseDetailPage", nisExpenseDetail);
		//初始化系统参数
		this.initConfig(model);
		//资金计划
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EXPENSE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_EXPENSE);
		return "expense/viewNisExpenseDetail";
	}
	/**
	 * 添加费用报销表
	 * 费用报销
	 * @param nisExpenseDetail
	 * @return AjaxJson
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@LogMessage(content = "添加费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisExpenseDetailVO nisExpenseDetail) {
		if (dataCheck(nisExpenseDetail)) {
			nisExpenseDetailService.saveExpenseDetail(nisExpenseDetail, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 修改费用报销表
	 *
	 * @param nisExpenseDetail
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@LogMessage(content = "修改费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(NisExpenseDetailVO nisExpenseDetail) {
		if (dataCheck(nisExpenseDetail)) {
			nisExpenseDetailService.updateExpenseDetail(nisExpenseDetail, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 数据校验
	 *
	 * @param nisExpenseDetail
	 * @return boolean
	 */
	private boolean dataCheck(NisExpenseDetailVO nisExpenseDetail) {
		//账号去空格
		if (StringUtils.isNotBlank(nisExpenseDetail.getBankAcc())) {
			nisExpenseDetail.setBankAcc(nisExpenseDetail.getBankAcc().trim());
		}
		//单位不能为空
		if (StringUtils.isEmpty(nisExpenseDetail.getCorpId())) {
			throw new ParameterException(ExpenseMessageEnum.NIS_EXPENSE_MESSAGE_CORP_NULL);
		}
		//银行卡类型不能为空
		if (StringUtils.isEmpty(nisExpenseDetail.getCardType())) {
			throw new ParameterException(ExpenseMessageEnum.NIS_EXPENSE_MESSAGE_CARDTYPE_NULL);
		}
		//状态校验
		if(StringUtils.isNotEmpty(nisExpenseDetail.getId())) {
			NisExpenseDetail entity = nisExpenseDetailService.get(nisExpenseDetail.getId(), true);
			//暂存和编辑状态才可以提交
			if (entity != null &&  !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
			}
		}
		return true;
	}
	/**
	 * 删除费用报销表
	 *
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@LogMessage(content = "删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		NisExpenseDetail entity = nisExpenseDetailService.get(id);
		//暂存和编辑状态才可以提交
		if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
		}
		nisExpenseDetailService.delExpenseDetail(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除费用报销表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@LogMessage(content = "批量删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetail entity = nisExpenseDetailService.get(id);
			//暂存和编辑状态才可以提交
			if (entity != null &&  !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
			}
			nisExpenseDetailService.delExpenseDetail(id, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 批量提交费用报销表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHAPPLY)
	@LogMessage(content = "批量提交费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
		boolean flag = false;
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetailVO nisExpenseDetailVO = nisExpenseDetailService.getByIdUnion(id);
			flag = dataCheck(nisExpenseDetailVO);
		}
		if (flag) {
			nisExpenseDetailService.batchSubmitExpenseDetail(ids, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 费用报销表汇总列表 页面跳转
	 *
	 * @return String
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTQUERY)
	@LogMessage(content = "查询费用报销表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listCollect")
	public String listCollect(Model model) {
		return "expense/listNisExpenseDetailCollect";
	}
	/**
	 * 费用报销表汇总页面跳转
	 *
	 * @return String
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTAPPLY)
	@GetMapping("/collect")
	public String collect(@RequestParam("ids") String ids, Model model) {
		List<NisExpenseDetailVO> listNisExpenseDetail = new ArrayList<NisExpenseDetailVO>();
		BigDecimal payMoney = BigDecimal.ZERO;
		String corpId = "";
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
			//审批通过状态才可以提交
			if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
			}
			listNisExpenseDetail.add(nisExpenseDetail);
			payMoney = payMoney.add(nisExpenseDetail.getExpenseMoney());
			corpId = nisExpenseDetail.getCorpId();
		}
		//汇总明细列表
		model.addAttribute("listNisExpenseDetail", listNisExpenseDetail);
		//汇总金额
		model.addAttribute("payMoney", payMoney);
		model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(),"yyyy-MM-dd"));
		//所用账号单位代码
		CorpDTO corp = corpService.get(corpId);
		if (StringUtils.isEmpty(corp.getUserAccountCode())) {
			corp.setUserAccountCode(corp.getId());
		}
		//汇总单位
		model.addAttribute("corp", corp);
		//初始化系统参数
		this.initConfig(model);
		return "expense/collectNisExpenseDetail";
	}
	/**
	 * 批量打回费用报销表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTAPPLY)
	@LogMessage(content = "批量修改费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(@RequestParam("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
			//审批通过状态才可以打回
			if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
			}
			nisExpenseDetailService.backExpenseDetail(id, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 费用报销表处理列表 页面跳转
	 *
	 * @return
	 */
    //////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_HANDLEQUERY)
	@LogMessage(content = "查询费用报销表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listDeal")
	public String listDeal(Model model) {
		model.addAttribute("showIsPayed", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
		//是否启用限额管理  1-是 0-否
		model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS,SysConfigConstant.ISOPENLIMIT).getValue());
		return "expense/listNisExpenseDetailDeal";
	}
	/**
	 * 费用报销表查询列表 页面跳转
	 *
	 * @return
	 */
    //////@RequiresPermissions(ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_QUERY)
	@LogMessage(content = "查询费用报销表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listQuery")
	public String listQuery(Model model) {
		return "expense/listNisExpenseDetailQuery";
	}
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime,Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "expense/listNisExpenseDetailApprove";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		return this.nisExpenseDetailService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
			model.addAttribute("nisExpenseDetailPage", nisExpenseDetail);
			model.addAttribute("taskId", taskId);
			//初始化系统参数
			this.initConfig(model);
			//资金计划
			model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EXPENSE);
			model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
			model.addAttribute("busType", NisMessageConstant.BUS_TYPE_EXPENSE);
		}
		return "expense/approveNisExpenseDetail";
	}
	/**
	 * 跳转至手机审核详情界面
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			NisExpenseDetailVO nisExpenseDetailVO = this.nisExpenseDetailService.getByUnion(businessId);
			model.addAttribute("nisExpenseDetailPage", nisExpenseDetailVO);
			model.addAttribute("taskId", taskId);
			//初始化系统参数
			this.initConfig(model);
			//资金计划
			model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EXPENSE);
			model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
			model.addAttribute("busType", NisMessageConstant.BUS_TYPE_EXPENSE);
		}
		return "expense/viewApproveNisExpenseDetail";
	}
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<NisExpenseDetailVO> list) {
		this.nisExpenseDetailService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 费用报销表汇总页面跳转
	 *
	 * @return String
	 */
	@GetMapping("/collectTests")
	public String collectTests(Model model) {
		//初始化系统参数
		this.initConfig(model);
		return "expense/collectNisExpenseDetail";
	}
	/**
	 * 根据明细ID获取汇总数据信息
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "collectData")
	@ResponseBody
	public AjaxJson collectData(String ids) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		List<NisExpenseDetailVO> listNisExpenseDetail = new ArrayList<NisExpenseDetailVO>();
		BigDecimal payMoney = BigDecimal.ZERO;
		String corpId = "";
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
			//审批通过状态才可以提交
			if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
			}
			listNisExpenseDetail.add(nisExpenseDetail);
			payMoney = payMoney.add(nisExpenseDetail.getExpenseMoney());
			corpId = nisExpenseDetail.getCorpId();
		}
		//汇总明细列表
		HashMap<String, Object> map = new HashMap<>();
		map.put("listNisExpenseDetail",listNisExpenseDetail);
		//汇总金额
		map.put("payMoney",payMoney);
		map.put("wishPayday",DateUtils.formatDate(DateUtils.getDate(),"yyyy-MM-dd"));
		//所用账号单位代码
		CorpDTO corp = corpService.get(corpId);
		if (StringUtils.isEmpty(corp.getUserAccountCode())) {
			corp.setUserAccountCode(corp.getId());
		}
		//汇总单位
		map.put("corp",corp);
		j.setObj(map);
		return j;
	}
	/**
	 * 汇总数据信息 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableCollect")
	@ResponseBody
	public DataTablesOutput dataTableCollect(@RequestBody DataTablesInput dataTablesInput) {
		return nisExpenseDetailService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
}
