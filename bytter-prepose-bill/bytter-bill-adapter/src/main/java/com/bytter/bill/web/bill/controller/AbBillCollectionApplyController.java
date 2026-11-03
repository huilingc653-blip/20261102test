package com.bytter.bill.web.bill.controller;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillCollectionApplyService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionApplyVo;
import com.bytter.core.constant.BudgetConstant;
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
 * @Description: 托收申请
 * @author onlineGenerator
 * @date 2017-07-26 15:30:56
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillcollectionapply")
public class AbBillCollectionApplyController extends BaseController {
	@Autowired
	private AbBillCollectionApplyService abBillCollectionApplyService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 应收-票据托收-托收申请列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		// 系统参数 是否挂资金计划
		String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
				.getValue();
		model.addAttribute("budgetSel", budgetSel);
		if ("1".equals(budgetSel)) {
			model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
			// 系统参数 托收挂接资金计划科目
			model.addAttribute("collectionItem", abConfigService
					.getBillConfig(AccebillConstant.AB_CONFIG_COLLECTIONITEM).getValue());
		}
		return "bill/accebill/listAbbillcollectionapply";
	}
	/**
	 * 应收-票据托收-托收申请 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询可托收申请的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abBillCollectionApplyService.getDataTablesUnion(input);
	}
	/**
	 * 托收申请
	 * 
	 * @param abBillCollectionApplyVo
	 * @return
	 */
	@LogMessage(content = "提交托收申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/collectionApply")
	@ResponseBody
	public AjaxJson collectionApply(@RequestBody AbBillCollectionApplyVo abBillCollectionApplyVo) {
		abBillCollectionApplyService.collectionApply(abBillCollectionApplyVo, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbbillcollectionapplyApprove";
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
		return abBillCollectionApplyService.dataTableApprove(dataTablesInput);
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
			AbBillCollectionApplyVo vo = abBillCollectionApplyService.getAbBillCollectionApplyVoById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
		}
		model.addAttribute("taskId", taskId);
		// 托收挂接资金计划 系统参数 根据流水表预算科目是否为空来显示挂预算列表
		model.addAttribute("collectionItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_COLLECTIONITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_COLLECTION);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/approve/approveAbBillCollectionApply";
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
			AbBillCollectionApplyVo vo = abBillCollectionApplyService.getAbBillCollectionApplyVoById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
