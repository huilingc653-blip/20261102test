package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillDiscountApplyService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
// import com.bytter.system.api.dto.CorpDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description:贴现申请Controller
 * @author onlineGenerator
 * @date 2017-07-03 11:25:01
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abBillDiscountApply")
public class AbBillDiscountApplyController extends BaseController {
	@Autowired
	private AbBillDiscountApplyService abBillDiscountApplyService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 贴现申请页面跳转
	 * 
	 * @return
	 */
	// @LogMessage(content = "贴现申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		//不启用贴现申请功能直接贴现办理
		model.addAttribute("isNotDiscountApply",abVoucherinfoViceService.isNotDiscountApply());
		return "bill/accebill/listAbBillDiscountApply";
	}
	/**
	 * 贴现申请列表
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillDiscountApplyService.listAbBillDiscountApply(dataTablesInput);
	}
	/**
	 * 贴现申请提交
	 * 
	 * @param abBillDiscountApplyVO
	 * @return
	 */
	@LogMessage(content = "批量提交应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmitAbBillDiscountApply(@RequestBody AbBillDiscountApplyVO abBillDiscountApplyVO) {
		AjaxJson j = success();
		boolean flag = abBillDiscountApplyService.batchSubmitAbBillDiscountApply(abBillDiscountApplyVO,
				this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 导入
	 * 
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入应收票据贴现申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillDiscountApplyVO> list) {
		AjaxJson json = success();
		boolean flag = abBillDiscountApplyService.saveImport(list, this.getCurrentUser());
		json.setSuccess(flag);
		return json;
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
		return "bill/accebill/approve/listAbBillDiscountApplyApprove";
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
		// http://192.168.0.198:8888/newBTFP/bill/accebill/abBillDiscountApply/listApprove
		return abBillDiscountApplyService.dataTableApprove(dataTablesInput);
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
			AbBillDiscountApplyVO info = abBillDiscountApplyService.getAbBillDiscountApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/approve/approveAbBillDiscountApply";
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
			AbBillDiscountApplyVO info = abBillDiscountApplyService.getAbBillDiscountApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
	/**
	 * 通过单位id获取单位信息
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getSysCorpById")
	@ResponseBody
	public CorpDTO getSysCorpById(String id) {
		return abCorpService.get(id);
	}
	/**
	 * 通过单位id串获取单位信息集合
	 * 
	 * @param corpIds
	 * @return
	 */
	@PostMapping("/listSysCorp")
	@ResponseBody
	public AjaxJson listSysCorp(String corpIds, String corpNames) {
		AjaxJson json = success();
		List<CorpDTO> list = abBillDiscountApplyService.listSysCorp(corpIds, corpNames);
		if (list == null || list.isEmpty()) {
			json.setSuccess(false);
		} else {
			json.setSuccess(true);
			json.setObj(list);
		}
		return json;
	}
}
