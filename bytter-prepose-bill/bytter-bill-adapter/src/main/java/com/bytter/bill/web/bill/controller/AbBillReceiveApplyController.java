package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillReceiveApplyService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillReceiveApplyVO;
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
 * @Description: 提示收票处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillReceiveApply")
public class AbBillReceiveApplyController extends BaseController {
	@Autowired
	private AbBillReceiveApplyService abBillReceiveApplyService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 提示收票处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillReceiveApply";
	}
	/**
	 * 提示收票处理 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询提示收票处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillReceiveApplyService.listAbBillReceiveApplyInfo(dataTablesInput);
	}
	/**
	 * 批量申请操作
	 * 
	 * @return
	 */
	@PostMapping("/batchApply")
	@ResponseBody
	@LogMessage(content = "批量收票申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
	public AjaxJson batchApply(@RequestBody AbBillReceiveApplyVO abBillReceiveApplyVO) {
		abBillReceiveApplyService.batchApply(abBillReceiveApplyVO, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbBillReceiveApplyApprove";
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
		return abBillReceiveApplyService.dataTableApprove(dataTablesInput);
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
			AbBillReceiveApplyVO info = abBillReceiveApplyService.getAbBillReceiveApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
			AbAccrecords accrecordsInfo=abAccrecordsService.get(businessId, true);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillReceiveApply";
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
			AbBillReceiveApplyVO info = abBillReceiveApplyService.getAbBillReceiveApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
