package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.MonthFundAllocationCollectReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 月度资金划拨汇总表
 * @date 2024-05-16
 */
@Controller
@RequestMapping("/report/monthFundAllocationCollect")
public class MonthFundAllocationCollectReportController extends BaseController {
	@Autowired
	private MonthFundAllocationCollectReportService monthFundAllocationCollectReportService;

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("queryDate",DateUtils.getDate());
		return "report/listMonthFundAllocationCollectReport";
	}

	/**
	 * 月度资金划拨汇总列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return monthFundAllocationCollectReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}

}
