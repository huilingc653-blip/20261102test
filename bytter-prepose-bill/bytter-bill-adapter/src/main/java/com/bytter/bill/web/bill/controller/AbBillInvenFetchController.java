package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillInvenFetchService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillInvenFetchVo;
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
 * @Description: 应收票据取票
 * @author onlineGenerator
 * @date 2017-07-05 18:17:47
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillinvenfetch")
public class AbBillInvenFetchController extends BaseController {
	@Autowired
	private AbBillInvenFetchService abBillInvenFetchService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 应收票据取票列表 页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "查询应收票据取票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillInvenFetch";
	}
	/**
	 * 应收票据取票 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInvenFetchService.listAbBillInvenFetch(dataTablesInput);
	}
	/**
	 * 批量取票
	 * 
	 * @return
	 */
	@LogMessage(content = "批量取票", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchChangeEnterStock")
	@ResponseBody
	public AjaxJson batchChangeEnterStock(@RequestBody AbBillInvenFetchVo abBillInvenFetchVo) {
		AjaxJson j = success();
		boolean flag = abBillInvenFetchService.batchChangeEnterStock(abBillInvenFetchVo, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
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
		return "bill/accebill/approve/listAbBillInvenFetchApprove";
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
		return abBillInvenFetchService.dataTableApprove(dataTablesInput);
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
			AbBillInvenFetchVo info = abBillInvenFetchService.getAbBillInvenFetchVoById(businessId);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getViceId())?abVoucherinfoViceService.findBankTypeNameWith(info.getViceId()):null);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillInvenFetch";
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
			AbBillInvenFetchVo info = abBillInvenFetchService.getAbBillInvenFetchVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
