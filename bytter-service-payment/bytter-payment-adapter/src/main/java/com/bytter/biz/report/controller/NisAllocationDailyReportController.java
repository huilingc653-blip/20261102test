package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.NisAllocationDailyReportService;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金日报表
 * @date 2024-05-14
 */
@Controller
@RequestMapping("/report/allocationDaily")
public class NisAllocationDailyReportController extends BaseController {
	@Autowired
	private NisAllocationDailyReportService nisAllocationDailyReportService;

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("queryDate",DateUtils.getDate());
		model.addAttribute("beforeDate",DateUtils.addDay(DateUtils.getDate(),-1));
		return "report/listNisAllocationDailyReport";
	}

	/**
	 * 资金日报表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return nisAllocationDailyReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}


	/**
	 * 多级资金日报表
	 */
	@GetMapping("/multiLeveList")
	public String multiLeveList(Model model) {
		model.addAttribute("queryDate",DateUtils.getDate());
		model.addAttribute("beforeDate",DateUtils.addDay(DateUtils.getDate(),-1));
		return "report/listMultiLeveFundDailyReport";
	}

	@PostMapping("/multiLeveDatatable")
	@ResponseBody
	public DataTablesOutput multiLeveDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return nisAllocationDailyReportService.getMultiLeveDatatable(dataTablesInput, this.getCurrentUser());
	}

}
