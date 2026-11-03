package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillInstructionManageService;
import com.bytter.bill.domain.bill.vo.AbBillInstructionManageVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
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
import java.lang.reflect.InvocationTargetException;
/**
 * @Title: Controller
 * @Description: 指令处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillInstructionManage")
public class AbBillInstructionManageController extends BaseController {
	@Autowired
	private AbBillInstructionManageService abBillInstructionManageService;
	/**
	 * 指令处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillInstructionManage";
	}
	
	/**
	 * 测试指令处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/listTest")
	public String listTest(Model model) {
		return "bill/accebill/listAbBillInstructionManageTest";
	}
	/**
	 * 测试指令处理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/listTestBill")
	public String listTestBill(Model model) {
		return "bill/accebill/listAbBillInstructionManageTestBill";
	}
	/**
	 * 指令处理 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询指令处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInstructionManageService.listAbBillInstructionManageInfo(dataTablesInput);
	}
	/**
	 * 指令测试处理 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableTest")
	@ResponseBody
	@LogMessage(content = "查询指令测试处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTableTest(@RequestBody DataTablesInput dataTablesInput) {
		return abBillInstructionManageService.listAbBillInstructionManageInfoTest(dataTablesInput);
	}
	/**
	 * 批量发送指令操作
	 * 
	 * @return
	 */
	@PostMapping("/batchSend")
	@ResponseBody
	@LogMessage(content = "批量发送指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSEND)
	public AjaxJson batchSend(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
		abBillInstructionManageService.batchSend(abBillInstructionManageVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量打回指令操作
	 * 
	 * @return
	 */
	@PostMapping("/batchRepulse")
	@ResponseBody
	@LogMessage(content = "批量打回指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
	public AjaxJson batchRepulse(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
		abBillInstructionManageService.batchRepulse(abBillInstructionManageVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量成功指令操作
	 * 
	 * @return
	 */
	@PostMapping("/batchSucce")
	@ResponseBody
	@LogMessage(content = "批量成功指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
	public AjaxJson batchSucce(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
		abBillInstructionManageService.batchSucce(abBillInstructionManageVO, this.getCurrentUser());
		return success();
	}
}
