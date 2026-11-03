package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolHoldBookService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 票据池持有账簿信息控制器
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/billPool/holdBook")
public class AbPoolHoldBookController extends BaseController {
	@Autowired
	private AbPoolHoldBookService abPoolHoldBookService;
	/**
	 *跳转查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listQuery")
	public String listQuery(Model model) {
		return "billPool/listAbPoolHoldBook";
	}
	/**
	 * 跳转查询明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewInfo")
	public String viewInfo(String id,Model model) {
		model.addAttribute("info",abPoolHoldBookService.getAbPoolHoldBookVOById(id));
		return "billPool/viewAbPoolHoldBook";
	}
	/**
	 * "查询票据池持有账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableQuery")
	@ResponseBody
	@LogMessage(content = "查询票据池持有账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableQuery(@RequestBody DataTablesInput dataTablesInput) {
		return abPoolHoldBookService.datatableQuery(dataTablesInput);
	}
}
