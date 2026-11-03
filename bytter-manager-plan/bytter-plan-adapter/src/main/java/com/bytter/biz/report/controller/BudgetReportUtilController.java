package com.bytter.biz.report.controller;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.report.service.BudgetUtilService;
import com.bytter.core.base.BaseController;
/**
 * 资金计划上报辅助类
 *
 * @author zuoy 2018-03-05
 */
@Controller
@RequestMapping("/budget/budgetreportutil")
public class BudgetReportUtilController extends BaseController {
	@Autowired
	private BudgetUtilService budgetUtilService;
	/**
	 * 获取有效预算类型信息
	 *
	 * @return
	 */
	@GetMapping("/getTimeType")
	@ResponseBody
	public List<Map<String, Object>> json() {
		return budgetUtilService.getTimeType();
	}
}
