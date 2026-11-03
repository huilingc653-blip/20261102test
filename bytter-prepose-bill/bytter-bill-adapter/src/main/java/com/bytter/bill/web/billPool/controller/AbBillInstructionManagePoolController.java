package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.service.AbBillInstructionManagePoolService;
import com.bytter.bill.domain.billPool.vo.AbBillInstructionManagePoolVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 票据池指令处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/pool/abbillInstructionManage")
public class AbBillInstructionManagePoolController extends BaseController {
	@Autowired
	private AbBillInstructionManagePoolService abBillInstructionManagePoolService;
	/**
	 * 测试指令处理列表 票据池保证金页面跳转
	 *
	 * @return
	 */
	@GetMapping("/listTestPoolBail")
	public String listTestPoolBail(Model model) {
		return "billPool/listAbBillInstructionManageTestPoolBail";
	}
	/**
	 * 测试指令处理列表 票据池额度申请金页面跳转
	 *
	 * @return
	 */
	@GetMapping("/listTestPoolLimit")
	public String listTestPoolLimit(Model model) {
		return "billPool/listAbBillInstructionManageTestPoolLimit";
	}
	/**
	 * 测试指令处理列表 票据池融资申请合同页面跳转
	 *
	 * @return
	 */
	@GetMapping("/listTestPoolCon")
	public String listTestPoolCon(Model model) {
		return "billPool/listAbBillInstructionManageTestPoolCon";
	}
	/**
	 * 批量成功指令操作
	 * 
	 * @return
	 */
	@PostMapping("/batchSucce")
	@ResponseBody
	@LogMessage(content = "批量成功指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
	public AjaxJson batchSucce(@RequestBody AbBillInstructionManagePoolVO AbBillInstructionManagePoolVO) {
		abBillInstructionManagePoolService.batchSucce(AbBillInstructionManagePoolVO, this.getCurrentUser());
		return success();
	}
	
	/**
	 * 指令测试处理 table列表 票据池保证金
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableTestPoolBail")
	@ResponseBody
	@LogMessage(content = "查询指令测试 票据池保证金处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTableTestPoolBail(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolBail(dataTablesInput);
	}
	
	/**
	 * 指令测试处理 table列表 票据池额度管理
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableTestPoolLimit")
	@ResponseBody
	@LogMessage(content = "查询指令测试票据池额度管理处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTableTestPoolLimit(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolLimit(dataTablesInput);
	}
	
	/**
	 * 指令测试处理 table列表 票据池合同管理
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableTestPoolCon")
	@ResponseBody
	@LogMessage(content = "查询指令测试处理票据池合同管理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTableTestPoolCon(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolCon(dataTablesInput);
	}
	@PostMapping("/operation")
	@ResponseBody
	public AjaxJson operation(@RequestParam("id") String id, @RequestParam("type") String type, @RequestParam("status") String status) {
		abBillInstructionManagePoolService.operation(type, id, status,this.getCurrentUser());
		return success();
	}
}
