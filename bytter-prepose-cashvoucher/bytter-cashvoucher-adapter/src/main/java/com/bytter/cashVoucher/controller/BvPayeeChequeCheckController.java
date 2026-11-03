package com.bytter.cashVoucher.controller;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.cashVoucher.service.BvPayeeChequeCheckService;
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
 * @Title: BvPayeeChequeCheckController  
 * @Description: 支票收款复核控制器类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvpayeechequecheck")
public class BvPayeeChequeCheckController extends BaseController {
	 
	
	@Autowired
	private BvPayeeChequeCheckService bvPayeeChequeCheckService;
	
	@Autowired
	private BvPayeeChequeService bvPayeeChequeService;
	
	/**
	* 支票收款复核管理列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model){
		return "bill/blankvou/listBvPayeeChequeCheck";
	}
	
	/**
	 * 支票收款复核信息表 table列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "支票收款复核管理列表 ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		input.getFormdata().put("ini_status", "1");
		input.getFormdata().put("createBy", this.getCurrentUser().getLoginName());
		return bvPayeeChequeService.getDataTablesUnion(input,this.getCurrentUser(), PayeeChequeConstant.AB_BILL_RESOURCE_BVPAYEECHEQUECHECK_QUERY);
	}
	
	
	/**
	* 退票页面跳转
	* @return
	*/
	@GetMapping("/retreatPage/{id}")
	public String retreatPage(@PathVariable("id") String id,Model model) {
		model.addAttribute("id", id);
		model.addAttribute("submit", "retreat");
		return "bill/blankvou/retreatOrTovoibvPayeeChequeCheckDtl";
	}
	
	
	/**
	* 某一行打回
	* @param id
	* @return
	*/
	@LogMessage(content = "支票收款复核-某一行打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/retreat/{id}")
	@ResponseBody
	public AjaxJson retreat(@PathVariable("id") String id,String reason){
		boolean flag = bvPayeeChequeCheckService.operateUpdate(id,reason,PayeeChequeConstant.BV_CHEQUE_TYPE_BACK,
							this.getCurrentUser(), BlankvouConstant.BV_CHEQUE_STATUS_BACK);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;  
	}
	
	
	/**
	* 某一行通过
	* @param id
	* @return
	*/
	@LogMessage(content = "支票收款复核-某一行通过",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/check/{id}")
	@ResponseBody
	public AjaxJson check(@PathVariable("id") String id){
		boolean flag = bvPayeeChequeCheckService.operateUpdate(id,null,PayeeChequeConstant.BV_CHEQUE_TYPE_CHECK,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_INPUT);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;   
	}
	
	
	/**
	* 批量退回
	* @param ids,rmk
	* @return
	*/
	@LogMessage(content = "支票收款复核-批量退回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/bacthRetreat")
	@ResponseBody
	public AjaxJson bacthRetreat(String ids,String rmk){
		boolean flag = bvPayeeChequeCheckService.batchOperateUpdate(ids,rmk,PayeeChequeConstant.BV_CHEQUE_TYPE_BATHBACK,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_BACK);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;   
	}
	 
	
	/**
	* 批量通过
	* @param ids
	* @return
	*/
	@LogMessage(content = "支票收款复核-批量通过",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchCheck")
	@ResponseBody
	public AjaxJson batchCheck(String ids){
		boolean flag = bvPayeeChequeCheckService.batchOperateUpdate(ids,null,PayeeChequeConstant.BV_CHEQUE_TYPE_BATHCHECK,
				this.getCurrentUser(),BlankvouConstant.BV_CHEQUE_STATUS_INPUT);
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;   
	}
}
