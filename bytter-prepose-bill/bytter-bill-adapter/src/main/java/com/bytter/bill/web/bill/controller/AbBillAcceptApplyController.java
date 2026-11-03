package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAcceptApplyService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillAcceptApplyVO;
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
 * @Description: 提示承兑处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillAcceptApply")
public class AbBillAcceptApplyController extends BaseController {
	
	@Autowired
	private AbBillAcceptApplyService abBillAcceptApplyService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	* 提示承兑处理列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/accebill/listAbBillAcceptApply";
	}
	
    /**
    * 提示承兑处理 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
	@LogMessage(content = "查询提示承兑处理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillAcceptApplyService.listAbBillAcceptApplyInfo(dataTablesInput);
    }
	
	/**
	 * 批量申请操作
	 * @return
	 */
	@PostMapping("/batchApply")
	@ResponseBody
	@LogMessage(content = "批量承兑申请操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHAPPLY)
	public AjaxJson batchApply(@RequestBody AbBillAcceptApplyVO abBillAcceptApplyVO) {
		abBillAcceptApplyService.batchApply(abBillAcceptApplyVO,this.getCurrentUser());
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
//		http://192.168.0.197:8088/v10/bill/accebill/abbillAcceptApply/listApprove
		return "bill/accebill/approve/listAbBillAcceptApplyApprove";
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
		return abBillAcceptApplyService.dataTableApprove(dataTablesInput);
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
			AbBillAcceptApplyVO info= abBillAcceptApplyService.getAbBillAcceptApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()):null);
			model.addAttribute("isApprove",true);
		}
		model.addAttribute("taskId", taskId);//必须要做此步骤
		return "bill/accebill/approve/approveAbBillAcceptApply";
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
			AbBillAcceptApplyVO info= abBillAcceptApplyService.getAbBillAcceptApplyVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove",true);
		}
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
