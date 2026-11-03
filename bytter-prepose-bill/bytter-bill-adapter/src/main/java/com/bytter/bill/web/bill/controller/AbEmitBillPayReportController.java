package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbEmitBillPayReportService;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayReportVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
/**
 * @Title: Controller
 * @Description: 到期兑付确认统计
 * @author onlineGenerator
 * @date 2018-01-15 16:35:25
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abemitbillpayReport")
public class AbEmitBillPayReportController extends BaseController {
	@Autowired
	private AbEmitBillPayReportService abEmitBillPayReportService;
	/**
	 * 到期兑付确认统计报表 页面跳转
	 * @param statistical 统计方式
	 * @param startDate 统计开始日期
	 * @param endDate 统计结束日期
	 * @param billType 票据类型
	 * @param billFlag 流通介质
	 * @param corpId 单位id
	 * @param acceptor 承兑人
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model,String statistical,String startDate,String endDate,
					   String billType,String billFlag,String corpId,String acceptor) {
		abEmitBillPayReportService.tableInit(model,statistical,startDate,endDate);
		model.addAttribute ("statistical",statistical);
		model.addAttribute ("startDate",startDate);
		model.addAttribute ("endDate",endDate);
		model.addAttribute ("billType",billType);
		model.addAttribute ("billFlag",billFlag);
		model.addAttribute ("corpId",corpId);
		if(StringUtils.isNotEmpty(acceptor)){
			try {
				model.addAttribute ("acceptor",URLDecoder.decode(acceptor, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return "bill/accebill/listAbEmitBillPayReport";
	}
	/**
	 * 到期兑付确认统计报表 table列表
	 *
	 * @return
	 */
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询到期兑付确认统计报表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput<AbEmitBillPayReportVO> dataTable(@RequestBody DataTablesInput dataTablesInput
			,String statistical,String startDate,String endDate,String corpId) {
		return abEmitBillPayReportService.listAbEmitBillPayReport(dataTablesInput,statistical,startDate,endDate,corpId);
	}
}
