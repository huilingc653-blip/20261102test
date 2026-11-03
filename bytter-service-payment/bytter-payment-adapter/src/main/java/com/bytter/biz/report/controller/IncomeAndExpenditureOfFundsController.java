package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.IncomeAndExpenditureOfFundsService;
import com.bytter.cloud.common.core.util.DateUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;


/**
 * @author zhoup
 * @version 1.0
 * @Title: Controller
 * @Description: 资金收支表
 * @date 2024-05-09
 */
@Controller
@RequestMapping("/report/incomeAndExpenditureOfFunds")
public class IncomeAndExpenditureOfFundsController extends BaseController {
	@Autowired
	private IncomeAndExpenditureOfFundsService incomeAndExpenditureOfFundsService;

	//////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_ALLOCATION_QUERY)
	@GetMapping("/list")
	public String list(String corpIdStr, java.util.Date startDate,java.util.Date endDate, Model model) {
		// 判断当前用户是否为单位用户
		//默认查询时间的默认值(开始时间为本周一，结束时间为本周日)
		String monday = DateUtil.getCurrentWeekDay(1);
		String sunday = DateUtil.getCurrentWeekDay(7);
		if (EmptyUtils.isEmpty(startDate)){
			model.addAttribute("startDate",DateUtil.parse(monday,"yyyy-MM-dd"));
		}
		if (EmptyUtils.isEmpty(endDate)){
			model.addAttribute("endDate",DateUtil.parse(sunday,"yyyy-MM-dd"));
		}
		return "report/incomeAndExpenditureOfFunds";
	}

	@PostMapping("/dataTable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return incomeAndExpenditureOfFundsService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}

	/**
	 * 多级资金收支表
	 */
	@GetMapping("/multiLeveList")
	public String multiLeveList(Model model) {
		Calendar calendar = Calendar.getInstance();
		// 设置到本周一
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date monday = calendar.getTime();
		// 上周日
		Date sunday = DateUtils.addDay(monday, -1);
		// 上周一
		Date lastMonday = DateUtils.addDay(sunday, -6);
		model.addAttribute("startDate",lastMonday);
		model.addAttribute("endDate", sunday);
		return "report/listMultiLeveFundIncomeExpenditure";
	}

	@PostMapping("/multiLeveDatatable")
	@ResponseBody
	public DataTablesOutput multiLeveDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return incomeAndExpenditureOfFundsService.getMultiLeveDatatable(dataTablesInput, this.getCurrentUser());
	}

}
