package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbReceiveBillLockService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillLockVo;
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
/**
 * @Title: Controller
 * @Description: 票据锁定/解锁
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abReceiveBillLock")
public class AbReceiveBillLockController extends BaseController {
	@Autowired
	private AbReceiveBillLockService abReceiveBillLockService;
	/**
	 * 票据锁定/解锁页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "票据锁定/解锁", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbReceiveBillLock";
	}
	/**
	 * 票据锁定/解锁 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abReceiveBillLockService.listAbReceiveBillLock(dataTablesInput);
	}
	@LogMessage(content = "锁定", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/lock")
	@ResponseBody
	public AjaxJson lock(AbReceiveBillLockVo back) {
		abReceiveBillLockService.lock(back, this.getCurrentUser());
		return success();
	}
	@LogMessage(content = "解锁", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/unLock")
	@ResponseBody
	public AjaxJson unLock(AbReceiveBillLockVo back) {
		abReceiveBillLockService.unLock(back, this.getCurrentUser());
		return success();
	}
	@LogMessage(content = "批量锁定", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchLock")
	@ResponseBody
	public AjaxJson batchLock(AbReceiveBillLockVo back) {
		abReceiveBillLockService.batchLock(back, this.getCurrentUser());
		return success();
	}
	@LogMessage(content = "批量解锁", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchUnLock")
	@ResponseBody
	public AjaxJson batchUnLock(AbReceiveBillLockVo back) {
		abReceiveBillLockService.batchUnLock(back, this.getCurrentUser());
		return success();
	}
}
