package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillDiscountRansomService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountRansomVo;
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
 * @Description: 票据-贴现赎回申请
 * @author luop
 * @date 2017-09-07
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilldiscountransom")
public class AbBillDiscountRansomController extends BaseController {
	@Autowired
	private AbBillDiscountRansomService abBillDiscountRansomService;
	/**
	 * 票据贴现赎回申请列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询票据贴现赎回申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillDiscountRansom";
	}
	/**
	 * 票据贴现赎回申请 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillDiscountRansomService.listAbBillDiscountRansom(dataTablesInput);
	}
	/**
	 * 批量贴现赎回申请批量提交
	 * 
	 * @return
	 */
	@LogMessage(content = "贴现赎回申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(AbBillDiscountRansomVo abBillDiscountRansomVo) {
		abBillDiscountRansomService.batchSubmit(abBillDiscountRansomVo, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbBillDiscountRansomApprove";
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
		return abBillDiscountRansomService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountRansomVo info = abBillDiscountRansomService.getAbBillDiscountRansomVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillDiscountRansom";
	}
	/**
	 * 跳转至手机审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountRansomVo info = abBillDiscountRansomService.getAbBillDiscountRansomVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
