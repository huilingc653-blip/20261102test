package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpPayBillDtlService;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 商户信息对账单明细Controller
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
@Controller
@RequestMapping("/ttpay/payBillDtl")
public class TpPayBillDtlController extends BaseController {
	@Autowired
	private TpPayBillDtlService tpPayBillDtlService;
	/**
	 * 商户信息流水查询列表页面
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP)
	public String list(String billDate,String payType,Model model) {
		model.addAttribute("trandTimeStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		model.addAttribute("trandTimeEnd", DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return "ttpay/listTpPayBillDtl";
	}
	/**
	 * 商户信息流水查询列表数据获取
	 * @param input
	 * @return 列表数据
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP)
	@LogMessage(content = "查询商户信息流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
		return tpPayBillDtlService.getDataTablesUnion(input);
	}
	/**
	 * 商户信息对账单明细列表页面
	 * @param model
	 * @return
	 */
	@GetMapping("/dtlView")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP)
	public String dtlView(String billDate,String payType,String appId,Model model) {
		model.addAttribute("billDate", billDate);
		model.addAttribute("payType", payType);
		model.addAttribute("appId", appId);
		model.addAttribute("fieldList", TtPayUtil.getTableListField(payType));
	    return "ttpay/listTpPayBillDtlView";
	}
	/**
	 * 商户信息对账单明细列表数据获取
	 * @param input
	 * @return 列表数据
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP)
	@LogMessage(content = "查询商户信息对账单明细列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/dtlViewDatatable")
	@ResponseBody
	public DataTablesOutput dtlViewDatatable(@RequestBody DataTablesInput input) {
		return tpPayBillDtlService.getDtlViewDataTablesUnion(input);
	}
	/**
	 * 商户信息交易流水明细页面
	 * @param model
	 * @return
	 */
	@GetMapping("/view")
	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP)
	public String view(String id,String payType,Model model) {
		List<TpPayBillViewDtlVO> list=TtPayUtil.getTableViewField(payType);
		if(EmptyUtils.isNotEmpty(list)){
			model.addAttribute("payTypeTitle", list.get(0).getPayShowName());
		}
		List<TpRefundOrderVO> listRefund = tpPayBillDtlService.listRefundInfo(id);
		if(EmptyUtils.isNotEmpty(listRefund)){
			model.addAttribute("refundInfoList", listRefund);
			model.addAttribute("isRefund", true);
		}
		model.addAttribute("fieldList", list);
		model.addAttribute("dataInfo", tpPayBillDtlService.getPayBillDtlInfoByIdOrPayType(id,payType));
	    return "ttpay/viewTpPayBillDtl";
	}
}
