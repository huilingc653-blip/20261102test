package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillSearchforApplyService;
import com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO;
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
import java.util.List;
/**
 * @Title: Controller
 * @Description: 追索通知处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillSearchforApply")
public class AbBillSearchforApplyController extends BaseController {
	@Autowired
	private AbBillSearchforApplyService abBillSearchforApplyService;
	/**
	 * 追索通知处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillSearchforApply";
	}
	/**
	 * 追索通知处理 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询追索通知处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillSearchforApplyService.listAbBillSearchforApplyInfo(dataTablesInput);
	}
	/**
	 * 批量申请操作
	 * 
	 * @return
	 */
	@PostMapping("/batchApply")
	@ResponseBody
	@LogMessage(content = "批量追索通知申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
	public AjaxJson batchApply(@RequestBody AbBillSearchforApplyVO abBillSearchforApplyVO) {
		abBillSearchforApplyService.batchApply(abBillSearchforApplyVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 导入追索通知申请
	 * 
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入追索通知申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillSearchforApplyVO> list) {
		AjaxJson json = success();
		boolean flag = abBillSearchforApplyService.saveImport(list, this.getCurrentUser());
		json.setSuccess(flag);
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
		return "bill/accebill/approve/listAbBillSearchforApplyApprove";
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
		return abBillSearchforApplyService.dataTableApprove(dataTablesInput);
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
			AbBillSearchforApplyVO info = abBillSearchforApplyService.getAbBillSearchforApplyVOById(businessId);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillSearchforApply";
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
			AbBillSearchforApplyVO info = abBillSearchforApplyService.getAbBillSearchforApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
