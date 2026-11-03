package com.bytter.bill.web.bill.controller;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbEmitBillBackService;
import com.bytter.bill.domain.bill.service.AbFcBondHookService;
import com.bytter.bill.domain.bill.vo.AbEmitBillBackVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 未用退票
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abemitbillback")
public class AbEmitBillBackController extends BaseController {
	@Autowired
	private AbEmitBillBackService abEmitBillBackService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbFcBondHookService abFcBondHookService;
	/**
	 * 未用退票列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询未用退票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbEmitBillBack";
	}
	/**
	 * 未用退票 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abEmitBillBackService.listAbEmitBillBack(dataTablesInput);
	}
	@LogMessage(content = "确认退票", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/returnTicket")
	@ResponseBody
	public AjaxJson returnTicket(AbEmitBillBackVo back) {
		abEmitBillBackService.returnTicket(back, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbEmitBillBackApprove";
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
		return abEmitBillBackService.dataTableApprove(dataTablesInput);
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
			AbEmitBillVo info = abEmitBillBackService.getAbEmitBillVoByAid(businessId);
			model.addAttribute("abEmitBillVo", info);
			model.addAttribute("isApprove", true);
			//判断是否有已挂保证金数据
			FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(info.getVvid(),info.getAppNo());
			String bailmodel = FcBondConstant.IS_HOOK_N;
			if(fcBondApplyDTO != null){
				bailmodel = FcBondConstant.IS_HOOK_Y;
			}
			model.addAttribute("bailmodel",bailmodel);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		// 票据挂预算初始化参数
		model.addAttribute("budgetSel",
				abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).getValue());
		model.addAttribute("payItem",
				abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/approve/approveAbEmitBillBack";
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
			AbEmitBillVo info = abEmitBillBackService.getAbEmitBillVoByAid(businessId);
			model.addAttribute("abEmitBillVo", info);
			model.addAttribute("isApprove", true);
		}
		return "bill/accebill/mobile/viewApproveAbBillEmit";
	}
}
