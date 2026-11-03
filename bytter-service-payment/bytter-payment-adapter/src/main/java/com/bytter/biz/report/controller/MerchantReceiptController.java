package com.bytter.biz.report.controller;
import org.springframework.ui.Model;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.report.service.MerchantReceiptService;
import com.bytter.biz.report.vo.NisBillheadVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;

/**
 * @author zhuop
 * @Description 客商收入资金量统计情况
 * @date 2018.8.15
 */
@Controller
@RequestMapping("/report/merchantreceipt")
public class MerchantReceiptController extends BaseController{
	@Autowired
	private MerchantReceiptService merchantReceiptService;
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY)
	@LogMessage(content = "客商收入资金量查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String listMerchantReceipt(Model model){
		return "report/listMerchantReceipt";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY)
	@GetMapping("/view")
	public String listDealData(NisBillheadVO vo, Model model){
		model.addAttribute("nisBillheadVO", vo);
		return "report/viewDealData";
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput getMerchantData(@RequestBody DataTablesInput input){
		return merchantReceiptService.getMerchantData(input,getCurrentUser());
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY)
	@PostMapping("/viewDatatable")
	@ResponseBody
	public DataTablesOutput getViewData(@RequestBody DataTablesInput input){
		return merchantReceiptService.getViewData(input,getCurrentUser());
	}
}
