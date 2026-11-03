package com.bytter.biz.receipt.controller;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.receipt.service.BisBankReceiptBlendingServer;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 银行电子回单勾兑
 */
@Controller
@RequestMapping("/receipt/bisbankreceiptblending")
public class BisBankReceiptBlendingController extends BaseController {
	@Autowired
	private BisBankReceiptBlendingServer bisBankReceiptBlendingServer;
	/**
	 * 银行电子回单列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询BIS_BANK_RECEIPT_DTL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list() {
		return "receipt/bankbill/listBisbankReceiptBlending";
	}
	/**
	 * 银行电子回单列表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询BIS_BANK_RECEIPT_DTL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{receiptId}")
	public String list(@PathVariable("receiptId") String receiptId,Model model) {
		model.addAttribute("receiptId", receiptId);
		return "receipt/bankbill/listBisbankReceiptBlending";
	}
	/**
	 * 银行电子回单列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return bisBankReceiptBlendingServer.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 银行流水列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dtlDatatable")
	@ResponseBody
	public DataTablesOutput dtlDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return bisBankReceiptBlendingServer.getDtlDataTablesUnion(dataTablesInput);
	}
	/**
	 * 自动勾兑
	 * @param ids
	 * @param bankAccTypeStr
	 * @return
	 */
	@RequestMapping(params = "auto")
	@ResponseBody
	public AjaxJson auto(String ids, String bankAccTypeStr){
		String msg = "";
		msg = bisBankReceiptBlendingServer.aotoBlending(ids,bankAccTypeStr);
		if(StringUtils.isEmpty(msg)){
			msg = "";
		}
		AjaxJson aj= new AjaxJson();
		if(StringUtils.isNotEmpty(msg)){
			aj.setSuccess(false);
		}else{
			aj.setSuccess(true);
		}
		aj.setMsg(msg);
		return  aj;
	}
	/**
	 * 手工勾兑
	 * @param dtlId 流水id
	 * @param receiptId 电子回单id
	 * @return
	 */
	@RequestMapping(params = "manual")
	@ResponseBody
	public AjaxJson manual(String dtlId, String receiptId){
		bisBankReceiptBlendingServer.manualBlending(dtlId,receiptId);
		AjaxJson aj= new AjaxJson();
		aj.setSuccess(true);
		aj.setMsg("bankacc.message.paymentblending.success");
		return  aj;
	}
	/**
	 * 取消勾兑
	 * @param receiptId 银行电子回单id
	 * @return
	 */
	@RequestMapping(params = "cancel")
	@ResponseBody
	public AjaxJson cancel(String receiptId){
		bisBankReceiptBlendingServer.cancelBlending(receiptId);
		AjaxJson aj= new AjaxJson();
		aj.setSuccess(true);
		aj.setMsg("bankacc.message.cancel.paymentblending.success");
		return  aj;
	}

	/**
	 * 无需勾对
	 * @param receiptId 银行电子回单id
	 * @return
	 */
	@RequestMapping(params = "needNot")
	@ResponseBody
	public AjaxJson needNot(String receiptId){
		if(EmptyUtils.isNotEmpty(receiptId)){
			String[] receiptIdStr = receiptId.split(",");
			if(!EmptyUtils.isEmpty(receiptIdStr) && receiptIdStr.length > 0 ) {
				for(int i=0;i<receiptIdStr.length;i++){
					bisBankReceiptBlendingServer.toUpdateBlendSign(receiptIdStr[i], BtBankAccConstant.BLENDSIGN_4);
				}
			}
		}
		AjaxJson aj= new AjaxJson();
		aj.setSuccess(true);
		return  aj;
	}
}
