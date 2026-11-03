package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillInvenTransferService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillInvenTransferVO;
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
 * @Description: 票据移存
 * @author zhenghj
 * @date 2017-06-29 10:34:22
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillinventransfer")
public class AbBillInvenTransferController extends BaseController {
	@Autowired
	private AbBillInvenTransferService abBillInvenTransferService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 票据移存列表 页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "查询票据移存列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillInvenTransfer";
	}
	/**
	 * 票据移存 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInvenTransferService.listAbBillInvenTransfer(dataTablesInput);
	}
	/**
	 * 批量移存
	 * 
	 * @return
	 */
	@LogMessage(content = "批量移存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchChangeEnterStock")
	@ResponseBody
	public AjaxJson batchChangeEnterStock(@RequestBody AbBillInvenTransferVO abBillInvenTransferVO) {
		AjaxJson j = success();
		boolean flag = abBillInvenTransferService.batchChangeEnterStock(abBillInvenTransferVO, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbBillInvenTransferApprove";
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
		return abBillInvenTransferService.dataTableApprove(dataTablesInput);
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
			AbBillInvenTransferVO info = abBillInvenTransferService.getAbBillInvenTransferVOById(businessId);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getViceId())?abVoucherinfoViceService.findBankTypeNameWith(info.getViceId()):null);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillInvenTransfer";
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
			AbBillInvenTransferVO info = abBillInvenTransferService.getAbBillInvenTransferVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
