package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolDtlBookService;
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
 * @Description: 票据池明细账簿信息控制器
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/billPool/dtlBook")
public class AbPoolDtlBookController extends BaseController {
	@Autowired
	private AbPoolDtlBookService abPoolDtlBookService;
	/**
	 *跳转提示付款查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listPaymentQuery")
	public String listPaymentQuery(Model model) {
		return "billPool/listAbPoolBillPaymentBook";
	}
	/**
	 * 跳转提示付款明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewPaymentInfo")
	public String viewPaymentInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolBillPaymentBook";
	}
	/**
	 * "查询提示付款票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatablePaymentQuery")
	@ResponseBody
	@LogMessage(content = "查询提示付款票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatablePaymentQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_PAY_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
	
	/**
	 *跳转出票申请查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listEmitQuery")
	public String listEmitQuery(Model model) {
		return "billPool/listAbPoolBillEmitBook";
	}
	/**
	 * 跳转出票申请明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewEmitInfo")
	public String viewEmitInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolBillEmitBook";
	}
	/**
	 * "查询提示付款票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableEmitQuery")
	@ResponseBody
	@LogMessage(content = "查询出票申请票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableEmitQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_EMIT_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
	
	/**
	 *跳转贴现查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listDiscountQuery")
	public String listDiscountQuery(Model model) {
		return "billPool/listAbPoolBillDiscountBook";
	}
	/**
	 * 跳转贴现明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewDiscountInfo")
	public String viewDiscountInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolBillDiscountBook";
	}
	/**
	 * "查询贴现票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableDiscountQuery")
	@ResponseBody
	@LogMessage(content = "查询贴现票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableDiscountQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_DISCOUNT_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
	
	/**
	 *跳转背书查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listEndorseQuery")
	public String listEndorseQuery(Model model) {
		return "billPool/listAbPoolBillEndorseBook";
	}
	/**
	 * 跳转背书明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewEndorseInfo")
	public String viewEndorseInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolBillEndorseBook";
	}
	/**
	 * "查询背书票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableEndorseQuery")
	@ResponseBody
	@LogMessage(content = "查询背书票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableEndorseQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_ENDORSE_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
	
	/**
	 *跳转出入池查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listInoutQuery")
	public String listInoutQuery(Model model) {
		return "billPool/listAbPoolInoutBook";
	}
	/**
	 * 跳转出入池明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewInoutInfo")
	public String viewInoutInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolInoutBook";
	}
	/**
	 * "查询出入池票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatableInoutQuery")
	@ResponseBody
	@LogMessage(content = "查询出入池票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatableInoutQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_INOUT_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
	
	/**
	 *跳转质押托管转换查询列表页面
	 *
	 * @return
	 */
	@GetMapping("/listPdTstQuery")
	public String listPdTstQuery(Model model) {
		return "billPool/listAbPoolPdTstBook";
	}
	/**
	 * 跳转质押托管转换明细页面
	 *
	 * @return
	 */
	@GetMapping("/viewPdTstInfo")
	public String viewPdTstInfo(String id,Model model) {
		model.addAttribute("info",abPoolDtlBookService.getAbPoolDtlBookVOById(id));
		return "billPool/viewAbPoolPdTstBook";
	}
	/**
	 * "查询背书票据池明细账簿信息列表数据
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatablePdTstQuery")
	@ResponseBody
	@LogMessage(content = "查询质押托管转换票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput datatablePdTstQuery(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.setResource(AbPoolConstant.AB_POLL_QUERY_PDTST_OP);
		return abPoolDtlBookService.datatableQuery(dataTablesInput);
	}
}
