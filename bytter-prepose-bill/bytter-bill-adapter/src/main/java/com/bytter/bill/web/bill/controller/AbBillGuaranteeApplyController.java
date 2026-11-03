package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillGuaranteeApplyService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillGuaranteeApplyVO;
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
 * @Description: 提示保证处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillGuaranteeApply")
public class AbBillGuaranteeApplyController extends BaseController {
	
	@Autowired
	private AbBillGuaranteeApplyService abBillGuaranteeApplyService;

	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;

	/**
	* 提示保证处理列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/accebill/listAbBillGuaranteeApply";
	}
	
    /**
    * 提示保证处理 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
	@LogMessage(content = "查询提示保证处理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillGuaranteeApplyService.listAbBillGuaranteeApplyInfo(dataTablesInput);
    }
	
	/**
	 * 批量申请操作
	 * @return
	 */
	@PostMapping("/batchApply")
	@ResponseBody
	@LogMessage(content = "批量保证申请操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHAPPLY)
	public AjaxJson batchApply(@RequestBody AbBillGuaranteeApplyVO abBillGuaranteeApplyVO) {
		abBillGuaranteeApplyService.batchApply(abBillGuaranteeApplyVO,this.getCurrentUser());
		return success();
	}
	
	/**
	 * 导入保证申请
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入保证申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillGuaranteeApplyVO> list) {
		AjaxJson json=success();
		boolean flag=abBillGuaranteeApplyService.saveImport(list,this.getCurrentUser());
		json.setSuccess(flag);
		return success();
	}
	
	/**
	 * 跳转至审核列表
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime,Model model){
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bill/accebill/approve/listAbBillGuaranteeApplyApprove";
	}
	
	/**
	 * 加载审核列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody  DataTablesInput dataTablesInput){
		return abBillGuaranteeApplyService.dataTableApprove(dataTablesInput);
	}
	
	/**
	 * 跳转至审核详情界面
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
//		model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillGuaranteeApplyVO info= abBillGuaranteeApplyService.getAbBillGuaranteeApplyVOById(businessId);
			model.addAttribute("info", info);
			AbAccrecords accrecordsInfo=abAccrecordsService.get(businessId, true);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);//必须要做此步骤
		return "bill/accebill/approve/approveAbBillGuaranteeApply";
	}
	/**
	 * 跳转至手机审核详情界面
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
//		model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillGuaranteeApplyVO info= abBillGuaranteeApplyService.getAbBillGuaranteeApplyVOById(businessId);
			model.addAttribute("info", info);
		}
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
