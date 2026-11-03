package com.bytter.bill.web.bill.controller;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillPledgeApplyService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo;
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
 * @Description: 应收票据质押申请
 * @author zhenghj
 * @date 2017-07-07
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillpledgeapply")
public class AbBillPledgeApplyController extends BaseController {
	@Autowired
	private AbBillPledgeApplyService abBillPledgeApplyService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	/**
	 * 票据质押申请列表 页面跳转
	 * 
	 * @return
	 */
	@LogMessage(content = "查询质押申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillPledgeApply";
	}
	/**
	 * 票据质押申请 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillPledgeApplyService.listAbBillPledgeApply(dataTablesInput);
	}
	/**
	 * 批量质押申请
	 * 
	 * @return
	 */
	@LogMessage(content = "批量质押申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchApply")
	@ResponseBody
	public AjaxJson batchApply(@RequestBody AbBillPledgeApplyVo abBillPledgeApplyVo) {
		AjaxJson j = success();
		boolean flag = abBillPledgeApplyService.batchApply(abBillPledgeApplyVo, AccebillConstant.BILL_OPERATE_SUBMIT,
				this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 根据单位id查询银行账号信息
	 * 
	 * @param corpId
	 *            单位id
	 * @return
	 */
	@PostMapping("/getBankDataByCorpId")
	@ResponseBody
	public List<BtBankAccDTO> getBankDataByCorpId(String corpId) {
		return abBillPledgeApplyService.getBankDataByCorpId(corpId);
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
		return "bill/accebill/approve/listAbBillPledgeApplyApprove";
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
		return abBillPledgeApplyService.dataTableApprove(dataTablesInput);
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
			AbBillPledgeApplyVo info = abBillPledgeApplyService.getAbBillPledgeApplyVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillPledgeApply";
	}
	/**
	 * 跳转至审核详情界面
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
			AbBillPledgeApplyVo info = abBillPledgeApplyService.getAbBillPledgeApplyVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
	/**
	 * 保存导入数据
	 * 
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillPledgeApplyVo> list) {
		this.abBillPledgeApplyService.saveImport(list, this.getCurrentUser());
		return success();
	}
}
