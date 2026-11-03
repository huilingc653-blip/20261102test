package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description:贴现办理
 * @author onlineGenerator
 * @date 2017-07-05 15:47:44
 * @version 1.0
 */
@Controller
@RequestMapping("/bill/accebill/abBillDiscountManager")
public class AbBillDiscountManagerController extends BaseController {
	@Autowired
	private AbBillDiscountManagerService abBillDiscountManagerService;
	@Autowired
	private AbCalendarService abCalendarService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbConfigService abConfigService;
	/**
	 * 贴现办理页面跳转
	 * 
	 * @return
	 */
	// @LogMessage(content = "贴现办理页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
		Integer isDisThirdBillCredit = abRatUrationService.isDisThirdBillCredit();
		Integer isCredit = abRatUrationService.isRelateRat()==false?0:1;
		model.addAttribute("isCredit",isCredit);
		model.addAttribute("isDisCountCredit",isDisCountCredit);
		model.addAttribute("isDisThirdBillCredit",isDisThirdBillCredit);
		//不启用贴现申请功能直接贴现办理
		model.addAttribute("isNotDiscountApply",abVoucherinfoViceService.isNotDiscountApply());
		if (isDisCountCredit==1) {
			model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		}
		// 系统参数 是否挂资金计划
		String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
				.getValue();
		model.addAttribute("budgetSel", budgetSel);
		if ("1".equals(budgetSel)) {
			model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
			// 系统参数 贴现挂接资金计划科目
			model.addAttribute("discountItem", abConfigService
					.getBillConfig(AccebillConstant.AB_CONFIG_DISCOUNTITEM).getValue());
		}
		return "bill/accebill/listAbBillDiscountManager";
	}
	/**
	 * 贴现办理列表
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillDiscountManagerService.listAbBillDiscountManager(dataTablesInput);
	}
	/**
	 * 贴现办理提交
	 * 
	 * @param abBillDiscountManagerVO
	 */
	@LogMessage(content = "批量提交贴现办理信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmitAbBillDiscountManager(@RequestBody AbBillDiscountManagerVO abBillDiscountManagerVO) { 
		AjaxJson j = success();
		boolean flag = abBillDiscountManagerService.batchSubmitAbBillDiscountManager(abBillDiscountManagerVO,
				this.getCurrentUser()); 
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 贴现申请打回
	 * 
	 * @param abBillDiscountManagerVO
	 */
	@LogMessage(content = "贴现申请打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson backAbBillDiscountManager(@RequestBody AbBillDiscountManagerVO abBillDiscountManagerVO) {
		AjaxJson j = success();
		boolean flag = abBillDiscountManagerService.batchBackAbBillDiscountManager(abBillDiscountManagerVO,
				this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 导入
	 * 
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillDiscountManagerVO> list) {
		AjaxJson json = success();
		boolean flag = abBillDiscountManagerService.saveImport(list, this.getCurrentUser());
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 跳转至审核列表
	 * 
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bill/accebill/approve/listAbBillDiscountManagerApprove";
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
		// http://192.168.0.198:8888/newBTFP/bill/accebill/abBillDiscountManager/listApprove
		return abBillDiscountManagerService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountManagerVO info = abBillDiscountManagerService.getAbBillDiscountManagerVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
			model.addAttribute("billId", info.getVvid());
			AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(info.getRecordId());
			if(null!=abDiscount && null!=abDiscount.getOccupancyRatio()){
				model.addAttribute("isEnabled",true);
			}else{
				model.addAttribute("isEnabled",false);
			}
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		// 贴现挂接资金计划
		// 根据流水表预算科目是否为空来显示挂预算列表
		// 系统参数 贴现挂接资金计划科目
		model.addAttribute("discountItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_DISCOUNTITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_DISCOUNT);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/approve/approveAbBillDiscountManager";
	}
	/**
	 * 跳转至手机审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountManagerVO info = abBillDiscountManagerService.getAbBillDiscountManagerVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
	@PostMapping("/getDeferdate")
	@ResponseBody
	public AjaxJson getDeferdate(String endTime) {
		AjaxJson json = success();
		Date date = abCalendarService.checkWorkDay(endTime, true);
		if (date == null) {
			json.setSuccess(false);
		} else {
			json.setSuccess(true);
			json.setObj(date);
		}
		return json;
	}
}
