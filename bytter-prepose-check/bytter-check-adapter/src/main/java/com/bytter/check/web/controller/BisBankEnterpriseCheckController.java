package com.bytter.check.web.controller;

import com.bytter.check.reconcile.service.BisBankEnterpriseCheckService;
import com.bytter.check.reconcile.vo.BisBankEnterpriseCheckVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 银企对账
 *
 */
@Controller
@RequestMapping("/bis/bankEnterprise")
public class BisBankEnterpriseCheckController extends BaseController{
	@Autowired
   private BisBankEnterpriseCheckService bisBankEnterpriseCheckService;
   @DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	//////@RequiresPermissions(ReceiptConstant.BIS_BANK_ENTERPRISE_CHECK_QUERY)
   @GetMapping("/list")
   public String list(Model model) {
	   //获取人民币
	   BtCurrencyDTO rmbCur = iBtCurrencyService.getRMBCurrency();
	   //人民币id
	   model.addAttribute("curId",rmbCur.getId());
	   model.addAttribute("dateEnd", DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
	   // 默认上个月 yyyy-MM
	   String dateStart = DateUtils.formatDate(DateUtils.addMonth(DateUtils.getDateYMD(), -1)).substring(0,7);
	   model.addAttribute("dateStart", dateStart);
	   return "reconcile/listBisBankEnterpriseCheck";
   }
   @SuppressWarnings("rawtypes")
   @PostMapping("/datatable")
   @ResponseBody
   //////@RequiresPermissions(ReceiptConstant.BIS_BANK_ENTERPRISE_CHECK_QUERY)
   public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
       return bisBankEnterpriseCheckService.getDataTablesUnion(dataTablesInput);
   }
   @PostMapping("/manualExc")
   @ResponseBody
   //////@RequiresPermissions(ReceiptConstant.BIS_BANK_ENTERPRISE_CHECK_QUERY)
   public AjaxJson manualExc(Model model, BisBankEnterpriseCheckVo bean){
		AjaxJson ajaxJson = success();
		try {
			bisBankEnterpriseCheckService.manualSynchronize(bean,this.getCurrentUser());
			ajaxJson.setSuccess(true);
		} catch (RuntimeException e) {
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
   }
	/**
	* @return
	*/
	@GetMapping("/optContrast")
	public String retreatPage(String opt,String ids,String bankTypeId,Model model) {
		model.addAttribute("ids", ids);
		model.addAttribute("opt", opt);
		model.addAttribute("bankTypeId", bankTypeId);
		return "reconcile/optContrastRecord";
	}
	@PostMapping("/optSure")
	@ResponseBody
	public AjaxJson optSure(BisBankEnterpriseCheckVo bean) {
		return bisBankEnterpriseCheckService.opSure(bean, this.getCurrentUser());
	}
}
