package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolLimitService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
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
 * @Description: 票据池额度信息控制器
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/billPool/limitInfo")
public class AbPoolLimitController extends BaseController {
	@Autowired
	private AbPoolLimitService abPoolLimitService;
	/**
	 *跳转查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listQuery")
	public String listQuery(Model model) {
		return "billPool/listAbPoolLimit";
	}
	/**
	 * 跳转查询明细页面
	 *
	 * @return
	 */
	@GetMapping("/view")
	public String view(String id,Model model) {
		model.addAttribute("info",abPoolLimitService.getAbPoolLimitVOById(id));
		return "billPool/viewAbPoolLimit";
	}
	/**
	 * "查询票据池额度信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableQuery")
	@ResponseBody
	@LogMessage(content = "查询票据池额度信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableQuery(@RequestBody DataTablesInput dataTablesInput) {
		return abPoolLimitService.datatableQuery(dataTablesInput);
	}
	/**
	 * 根据单位ID和票据池ID或者账号查询额度信息
	 *
	 * @param corpId 单位ID
	 * @param bpId   票据池ID
	 * @param bankAcc 银行账号
	 * @return 额度信息
	 */
	@PostMapping("/getLimitInfo")
	@ResponseBody
	public AbPoolLimitVO getLimitInfo(String corpId, String bpId, String bankAcc) {
		return abPoolLimitService.getLimitInfo(corpId, bpId, bankAcc);
	}
}
