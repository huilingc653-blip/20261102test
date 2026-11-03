package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillDiscountReleaseService;
import com.bytter.bill.domain.bill.service.AbRatUrationService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
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
 * @Title: Controller
 * @Description:贴现额度释放Controller
 *
 */
@Controller
@RequestMapping("/bill/accebill/abBillDiscountRelease")
public class AbBillDiscountReleaseController extends BaseController {
	@Autowired
	private AbBillDiscountReleaseService abBillDiscountReleaseService;
	
	@Autowired
	private AbRatUrationService abRatUrationService;
	/**
	 * 贴现到账页面跳转
	 * 
	 * @return
	 */
	@LogMessage(content = "贴现额度释放", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
		Integer isCredit = abRatUrationService.isRelateRat()==false?0:1;
		model.addAttribute("isCredit",isCredit);
		model.addAttribute("isDisCountCredit",isDisCountCredit);
		return "bill/accebill/listAbBillDiscountRelease";
	}
	/**
	 * 贴现释放列表
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		dataTablesInput.getFormdata().put("checkboxHide","1");
		return abBillDiscountReleaseService.listAbBillDiscountAccount(dataTablesInput);
	}
	/**
	 * 批量确认释放额度
	 * 
	 * @param abBillDiscountAccountVO
	 * @return
	 */
	@LogMessage(content = "批量确认释放额度", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmitAbBillDiscountAccount(AbBillDiscountAccountVO abBillDiscountAccountVO) {
		AjaxJson j = success();
		boolean flag = abBillDiscountReleaseService.batchSubmitAbBillDiscountAccount(abBillDiscountAccountVO,
				this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	
	/**
	* 单笔确认
	* @param id
	* @return
	*/
	@LogMessage(content = "单笔确认",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/sure") 
	@ResponseBody
	public AjaxJson sure(String vvid,String id){
		AjaxJson ajaxJson = success();
		boolean flag = abBillDiscountReleaseService.signRelease(vvid,id,this.getCurrentUser());
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	
}
