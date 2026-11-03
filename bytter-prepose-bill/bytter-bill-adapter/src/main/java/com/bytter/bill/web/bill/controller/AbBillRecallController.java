package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillRecallService;
import com.bytter.bill.domain.bill.vo.AbBillRecallVO;
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
 * @Description: 通用撤回处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillRecall")
public class AbBillRecallController extends BaseController {
	@Autowired
	private AbBillRecallService abBillRecallService;
	/**
	 * 通用撤回处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillRecall";
	}
	/**
	 * 通用撤回处理 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询通用撤回处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillRecallService.listAbBillRecallInfo(dataTablesInput);
	}
	/**
	 * 批量申请操作
	 * 
	 * @return
	 */
	@PostMapping("/batchApply")
	@ResponseBody
	@LogMessage(content = "批量通用撤回申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
	public AjaxJson batchApply(@RequestBody AbBillRecallVO abBillRecallVO) {
		abBillRecallService.batchApply(abBillRecallVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至审核列表
	 * 
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus,String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bill/accebill/approve/listAbBillRecallApprove";
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
		return abBillRecallService.dataTableApprove(dataTablesInput);
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
	public String approve(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillRecallVO info = abBillRecallService.getAbBillRecallVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillRecall";
	}
	/**
	 * 跳转至手机审核详情界面
	 * 
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillRecallVO info = abBillRecallService.getAbBillRecallVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
