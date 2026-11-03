package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillPledgeCollectService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeCollectVo;
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
 * @Description: 票据-质押收回
 * @author zhenghj
 * @date 2017-07-17
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillpledgecollect")
public class AbBillPledgeCollectController extends BaseController {
	@Autowired
	private AbBillPledgeCollectService abBillPledgeCollectService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 票据质押收回列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询票据质押收回列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillPledgeCollect";
	}
	/**
	 * 票据质押收回 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillPledgeCollectService.listAbBillPledgeCollect(dataTablesInput);
	}
	/**
	 * 批量质押收回
	 * 
	 * @return
	 */
	@LogMessage(content = "解除质押", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchCollect")
	@ResponseBody
	public AjaxJson batchCollect(@RequestBody AbBillPledgeCollectVo abBillPledgeCollectVo) {
		AjaxJson j = success();
		boolean flag = abBillPledgeCollectService.batchCollect(abBillPledgeCollectVo, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbBillPledgeCollectApprove";
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
		return abBillPledgeCollectService.dataTableApprove(dataTablesInput);
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
			AbBillPledgeCollectVo info = abBillPledgeCollectService.getAbBillPledgeCollectVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);

			AbAccrecords accrecordsInfo=abAccrecordsService.get(businessId, true);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillPledgeCollect";
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
			AbBillPledgeCollectVo info = abBillPledgeCollectService.getAbBillPledgeCollectVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
