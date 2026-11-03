package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillPledgeRelieveService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeRelieveVo;
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
 * @Description: 票据-解除质押
 * @author zhenghj
 * @date 2017-07-14
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillpledgerelieve")
public class AbBillPledgeRelieveController extends BaseController {
	@Autowired
	private AbBillPledgeRelieveService abBillPledgeRelieveService;
	/**
	 * 票据解除质押列表 页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "查询票据解除质押列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillPledgeRelieve";
	}
	/**
	 * 票据解除质押 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillPledgeRelieveService.listAbBillPledgeRelieve(dataTablesInput);
	}
	/**
	 * 批量解除质押
	 * 
	 * @return
	 */
	@LogMessage(content = "解除质押", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchRelieve")
	@ResponseBody
	public AjaxJson batchRelieve(@RequestBody AbBillPledgeRelieveVo abBillPledgeRelieveVo) {
		AjaxJson j = success();
		boolean flag = abBillPledgeRelieveService.batchRelieve(abBillPledgeRelieveVo, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
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
		return "bill/accebill/approve/listAbBillPledgeRelieveApprove";
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
		return abBillPledgeRelieveService.dataTableApprove(dataTablesInput);
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
			AbBillPledgeRelieveVo info = abBillPledgeRelieveService.getAbBillPledgeRelieveVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillPledgeRelieve";
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
			AbBillPledgeRelieveVo info = abBillPledgeRelieveService.getAbBillPledgeRelieveVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
