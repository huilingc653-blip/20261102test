package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillDiscountCollectService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountCollectVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
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
/**
 * @Title: Controller
 * @Description: 票据-贴现收回
 * @author luop
 * @date 2017-09-05
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilldiscountcollect")
public class AbBillDiscountCollectController extends BaseController {
	@Autowired
	private AbBillDiscountCollectService abBillDiscountCollectService;
	/**
	 * 票据贴现收回列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询票据贴现收回列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillDiscountCollect";
	}
	/**
	 * 票据贴现收回 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillDiscountCollectService.listAbBillDiscountCollect(dataTablesInput);
	}
	/**
	 * 批量贴现收回
	 * 
	 * @return
	 */
	@LogMessage(content = "贴现收回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(AbBillDiscountCollectVo abBillDiscountCollectVo) {
		abBillDiscountCollectService.batchSubmit(abBillDiscountCollectVo, this.getCurrentUser());
		return success();
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
		return "bill/accebill/approve/listAbBillDiscountCollectApprove";
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
		return abBillDiscountCollectService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountCollectVo info = abBillDiscountCollectService.getAbBillDiscountCollectVoById(businessId, true);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillDiscountCollect";
	}
	/**
	 * 跳转至手机审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountCollectVo info = abBillDiscountCollectService.getAbBillDiscountCollectVoById(businessId, true);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
