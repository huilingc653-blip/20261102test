package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillTransferUpApplyService;
import com.bytter.bill.domain.bill.vo.AbBillTransferUpApplyVo;
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
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilltransferupapply")
public class AbBillTransferUpApplyController extends BaseController {
	@Autowired
	private AbBillTransferUpApplyService abBillTransferUpApplyService;
	/**
	 * 应收-票据划拨-票据上划列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listabbilltransferupapply";
	}
	/**
	 * 应收-票据划拨-票据上划 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询可票据上划的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abBillTransferUpApplyService.getDataTablesUnion(input);
	}
	/**
	 * 票据上划
	 * 
	 * @param abBillTransferUpApplyVo
	 * @return
	 */
	@LogMessage(content = "票据上划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/transferup")
	@ResponseBody
	public AjaxJson transferup(AbBillTransferUpApplyVo abBillTransferUpApplyVo) {
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(abBillTransferUpApplyService.transferup(abBillTransferUpApplyVo, this.getCurrentUser()));
		return ajaxJson;
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
		return "bill/accebill/approve/listAbBillTransferUpApplyApprove";
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
		return abBillTransferUpApplyService.dataTableApprove(dataTablesInput);
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
			AbBillTransferUpApplyVo vo = abBillTransferUpApplyService.getAbBillTransferUpApplyVoById(businessId);
			model.addAttribute("info", vo);
			model.addAttribute("bankTypeName", vo.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillTransferUpApply";
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
			AbBillTransferUpApplyVo vo = abBillTransferUpApplyService.getAbBillTransferUpApplyVoById(businessId);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
