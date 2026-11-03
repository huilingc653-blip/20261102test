package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbReceiveBillBackService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillBackVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
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
 * @Description: 退票申请
 * @author onlineGenerator
 * @date 2017-06-29 16:53:43
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abreceivebillback")
public class AbReceiveBillBackController extends BaseController {
	@Autowired
	private AbReceiveBillBackService abReceiveBillBackService;
	/**
	 * 退票申请列表 页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "退票申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbReceiveBillBack";
	}
	/**
	 * 退票申请 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abReceiveBillBackService.listAbReceiveBillBack(dataTablesInput);
	}
	@LogMessage(content = "退票申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/returnTicket")
	@ResponseBody
	public AjaxJson returnTicket(AbReceiveBillBackVo back) {
		abReceiveBillBackService.returnTicket(back, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbReceiveBillBackApprove";
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
		return abReceiveBillBackService.dataTableApprove(dataTablesInput);
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
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbReceiveBillVO info = abReceiveBillBackService.getAbReceiveBillVoByAid(businessId);
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbReceiveBillBack";
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
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbReceiveBillVO info = abReceiveBillBackService.getAbReceiveBillVoByAid(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
