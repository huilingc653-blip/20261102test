package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.GroupMonthFundAllocationReportService;
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
 * @Description: 集团月度资金划拨分析表
 * @date 2024-05-16
 */
@Controller
@RequestMapping("/report/groupMonthFundaAllocation")
public class GroupMonthFundAllocationReportController extends BaseController {
	@Autowired
	private GroupMonthFundAllocationReportService groupMonthFundAllocationReportService;

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("queryDate",DateUtils.getDate());
		return "report/listGroupMonthFundAllocationReport";
	}

	/**
	 * 集团月度资金划拨分析列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return groupMonthFundAllocationReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}

}
