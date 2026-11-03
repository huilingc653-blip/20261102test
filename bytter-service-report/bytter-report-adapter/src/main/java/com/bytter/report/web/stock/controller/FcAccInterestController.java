package com.bytter.report.web.stock.controller;
import com.bytter.report.domain.stock.service.FcAccInterestService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/bi/fcAccInterest")
public class FcAccInterestController extends BaseController{
	@Autowired
	private FcAccInterestService fcAccInterestService;

	@GetMapping("/listFcAccInterest")
	public String listFcAccInterest(Model model){
		String date=DateUtils.formatDate(DateUtils.getMonthStartTime());
		model.addAttribute("monthFirstDay", date);
		return "stock/listFcAccInterest";
	}
		@SuppressWarnings("rawtypes")
	@PostMapping("/getFcAccInterest")
	@ResponseBody
	public DataTablesOutput getFcAccInterest(@RequestBody DataTablesInput input )  {
		DataTablesOutput output=fcAccInterestService.getFcAccInterest(input,this.getCurrentUser());
		return output;
	}
	@GetMapping("/view")
	public String view(String beginDate,String endDate,String curId,String corpId,String accType,String status,Model model){
		model.addAttribute("curId", curId);
		model.addAttribute("corpId",corpId);
		model.addAttribute("status",status);
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		model.addAttribute("accType",accType);
		return "stock/viewReceivableDetail";
	}
	@SuppressWarnings("rawtypes")
	@PostMapping("/getDetail")
	@ResponseBody
	public DataTablesOutput getDetail(@RequestBody DataTablesInput input ){
		DataTablesOutput output=fcAccInterestService.getDetail(input);
		return output;
	}
}
