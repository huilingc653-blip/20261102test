package com.bytter.bill.web.bill.controller;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
/**
 * @Title: Controller
 * @Description: 票据账户余额
 * @author zhenghj
 * @date 2017-10-10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillaccbal")
public class AbBillAccBalController extends BaseController {
	@Autowired
	private AbBillAccBalService abBillAccBalService;
	/**
	 * 票据账户余额列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询票据账户余额列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillAccBal";
	}
	/**
	 * 票据账户余额 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : dataTablesInput.getFormdata().get("ins_corpId").toString();
		String balDate = (dataTablesInput.getFormdata().get("ged_balDate") == null || "".equals(dataTablesInput.getFormdata().get("ged_balDate")))
				? "2010-01-01" : dataTablesInput.getFormdata().get("ged_balDate").toString();
		//不通过余额表查数据
		//abBillAccBalService.resetAbBillAccBal(corpIds,"2010-01-01",this.getCurrentUser(),false);
		DataTablesOutput out = abBillAccBalService.searchBalance(corpIds, this.getCurrentUser(), dataTablesInput, AccebillConstant.AB_BILL_ACCBAL_QUERY_CORPOP);
		//DataTablesOutput out1 = abBillAccBalService.listAbBillAccBalDao(dataTablesInput);
		return out;
	}
	/**
	 * 票据账户余额重新生成页面 页面跳转
	 * @return
	 */
	@LogMessage(content = "跳转票据账户余额重新生成页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/balReset")
	public String toAbBillAccBalReset(Model model) {
		model.addAttribute("dataToday", DateUtils.format(new Date(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return "bill/accebill/resetAbBillAccBal";
	}
	
	/**
	 * 票据账户余额重新生成操作
	 * @param corpIds 需要重新生成的单位
	 * @param beginDate 重新生成余额的开始日期
	 * @return
	 */
	@PostMapping("/resetAbBillAccBal")
	@ResponseBody
	public AjaxJson resetAbBillAccBal(String corpIds,String beginDate) {
		abBillAccBalService.resetAbBillAccBal(corpIds, beginDate, this.getCurrentUser(),false);
		return success();
	}
	/**
	 * 票据账户余额指定日期重新生成操作
	 * @param corpIds 需要重新生成的单位
	 * @param beginDate 重新生成余额的开始日期
	 * @return
	 */
	@PostMapping("/resetAbBillAccBalByBalDate")
	@ResponseBody
	public AjaxJson resetAbBillAccBalByBalDate(String corpIds,String beginDate) {
		abBillAccBalService.resetAbBillAccBal(corpIds, beginDate, this.getCurrentUser(),true);
		return success();
	}
}
