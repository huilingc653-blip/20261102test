package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.MonthFundAllocationReportService;
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
 * @Description: 月度资金划拨明细表
 * @date 2024-05-16
 */
@Controller
@RequestMapping("/report/monthFundAllocation")
public class MonthFundAllocationReportController extends BaseController {
	@Autowired
	private MonthFundAllocationReportService monthFundAllocationReportService;

	@GetMapping("/list")
	public String list(Model model) {
		return "report/listMonthFundAllocationReport";
	}

	/**
	 * 月度资金划拨明细表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return monthFundAllocationReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}

}
