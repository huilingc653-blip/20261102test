package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillCollectionAccountReportService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountReportVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
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
 * 到期托收到账统计
 *
 * @author songkk
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillCollectionAccountReport")
public class AbBillCollectionAccountReportController extends BaseController {
	@Autowired
	private AbBillCollectionAccountReportService abBillCollectionAccountReportService;
	/**
	 * 统计报表-承兑汇票-到期托收到账统计 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model, AbBillCollectionAccountReportVo accountReportVo) {
		if ("1".equals(accountReportVo.getQuerySign())) {
			abBillCollectionAccountReportService.getSumCollectionAccountReportList(model, accountReportVo);
		}
		return "bill/accebill/listAbBillCollectionAccountReport";
	}
	/**
	 *
	 * 到期托收到账统计 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询可托收到账的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input, String countType) {
		return abBillCollectionAccountReportService.getDataTablesUnion(input, countType);
	}
}
