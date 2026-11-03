package com.bytter.biz.ttpay.controller;
import java.util.Date;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.ttpay.service.TpBillJobService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
/**
 * @Title: Controller
 * @Description: 商户信息对账单Controller
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
@Controller
@RequestMapping("/ttpay/billJob")
public class TpBillJobController extends BaseController {
	@Autowired
	private TpBillJobService tpBillJobService;
	
	/**
	 * 商户信息对账单列表页面
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP)
	public String list(Model model) {
		model.addAttribute("billDateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		model.addAttribute("billDateEnd", DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
	    return "ttpay/listTpBillJob";
	}
	/**
	 * 商户信息对账单列表数据获取
	 * @param input
	 * @return 列表数据
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP)
	@LogMessage(content = "查询商户信息对账单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return tpBillJobService.getDataTablesUnion(input);
	}
}
