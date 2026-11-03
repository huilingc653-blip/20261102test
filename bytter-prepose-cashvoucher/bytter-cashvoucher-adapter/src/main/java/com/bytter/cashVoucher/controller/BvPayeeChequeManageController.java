package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.cashVoucher.service.BvPayeeChequeManageService;
import com.bytter.cashVoucher.service.BvPayeeChequeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: BvPayeeChequeManageController
 * @Description: 支票台账管理控制器类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvpayeechequemanage")
public class BvPayeeChequeManageController extends BaseController {
	@Autowired
	private BvPayeeChequeManageService bvPayeeChequeManageService;
	@Autowired
	private BvPayeeChequeService bvPayeeChequeService;
	/**
	 * 支票收款台账理列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model){
		return "bill/blankvou/listBvPayeeChequeManage";
	}
	/**
	 * "支票收款台账table列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "支票收款台账管理列表 ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		input.getFormdata().put("ini_status", "2,3,4,5,6");
		input.getFormdata().put("checkboxHide","1");
		return bvPayeeChequeService.getDataTablesUnion(input,this.getCurrentUser(),PayeeChequeConstant.AB_BILL_RESOURCE_BVPAYEECHEQUEMANAGE_QUERY);
	}
	/**
	 * 某一行退票
	 * @param id
	 * @return
	 */
	@LogMessage(content = "支票台账管理-某一行打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/retreat/{id}")
	@ResponseBody
	public AjaxJson retreat(@PathVariable("id") String id){
		boolean flag = bvPayeeChequeManageService.operateUpdate(id,PayeeChequeConstant.BV_CHEQUE_TYPE_RETREAT,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_BACKBILL);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 某一行作废
	 * @param id
	 * @return
	 */
	@LogMessage(content = "支票台账管理-某一行通过",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/tovoid/{id}")
	@ResponseBody
	public AjaxJson check(@PathVariable("id") String id){
		boolean flag = bvPayeeChequeManageService.operateUpdate(id,PayeeChequeConstant.BV_CHEQUE_TYPE_TOVOID,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_CANCELLATION);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 批量退票
	 * @return
	 */
	@LogMessage(content = "支票台账管理-批量退票",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchRetreat")
	@ResponseBody
	public AjaxJson batchRetreat(String ids){
		boolean flag = bvPayeeChequeManageService.batchOperateUpdate(ids,PayeeChequeConstant.BV_CHEQUE_TYPE_BATHRETREAT,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_BACKBILL);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 批量作废
	 * @return
	 */
	@LogMessage(content = "支票台账管理-批量作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchTovoid")
	@ResponseBody
	public AjaxJson batchDefeat(String ids){
		boolean flag = bvPayeeChequeManageService.batchOperateUpdate(ids,PayeeChequeConstant.BV_CHEQUE_TYPE_BATHTOVOID,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_CANCELLATION);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 托收申请
	 * @param id
	 * @return
	 */
	@LogMessage(content = "支票台账管理-托收申请",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/collection/{id}")
	@ResponseBody
	public AjaxJson collection(@PathVariable("id") String id){
		boolean flag = bvPayeeChequeManageService.operateUpdate(id,PayeeChequeConstant.BV_CHEQUE_TYPE_COLLECTION,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_COLLECTION);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 到账
	 * @param id
	 * @return
	 */
	@LogMessage(content = "支票台账管理-到账",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/arrivalAccount/{id}")
	@ResponseBody
	public AjaxJson arrivalAccount(@PathVariable("id") String id){
		boolean flag = bvPayeeChequeManageService.operateUpdate(id,PayeeChequeConstant.BV_CHEQUE_TYPE_ARRIVALACCOUNT,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_ARRIVALACCOUNT);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	/**
	 * 批量到账
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "支票台账管理-批量到账",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/batchArrivalAccount")
	@ResponseBody
	public AjaxJson batchArrivalAccount(String ids){
		boolean flag = bvPayeeChequeManageService.batchOperateUpdate(ids,PayeeChequeConstant.BV_CHEQUE_TYPE_BATHARRIVALACCOUNT,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_ARRIVALACCOUNT);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
}
