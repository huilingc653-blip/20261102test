package com.bytter.check.web.controller;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.service.FcBankBillService;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: FC_BANK_BILL
 * @author onlineGenerator
 * @date 2017-06-02 10:04:16
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fcbankbill")
public class FcBankBillController extends BaseController {
	@Autowired
	private FcBankBillService fcBankBillService;
	/**
	* FC_BANK_BILL列表 页面跳转
	*/
	@LogMessage(content = "查询FC_BANK_BILL列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "check/fcbankbill/listFcBankBill";
	}
	
    /**
    * FC_BANK_BILL table列表
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return fcBankBillService.getBankBill(dataTablesInput,getUserId());
    }
	
	/**
	* FC_BANK_BILL新增页面跳转
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_EDIT)
	@GetMapping("/add")
	public String add(FcBankBill fcBankBill, Model model) {
		if (StringUtils.isNotEmpty(fcBankBill.getId())) {
			fcBankBill = fcBankBillService.get(fcBankBill.getId());
			model.addAttribute("fcBankBillPage", fcBankBill);
		}
		return "check/fcbankbill/addFcBankBill";
	}
	
	/**
	* FC_BANK_BILL编辑页面跳转
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_EDIT)
	@GetMapping("/update")
	public String update(FcBankBillVo fcBankBillVo, Model model) {
		if (StringUtils.isNotEmpty(fcBankBillVo.getId())) {
			FcBankBill fc= fcBankBillService.get(fcBankBillVo.getId());
			model.addAttribute("fcBankBillPage", fc);
		}
		return "check/fcbankbill/updateFcBankBill";
	}
	/**
	 * 添加FC_BANK_BILL
	 * 银行对账单录入
	 */
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBankBillVo fcBankBills){
		fcBankBillService.save(fcBankBills,null, getUserId());
		return success();
	}
	
	/**
	* 修改FC_BANK_BILL
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_EDIT)
	@LogMessage(content = "修改FC_BANK_BILL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBankBill fcBankBill){
		fcBankBillService.updateFcbankBill(fcBankBill, getUserId());
		return  success();
	}
	
	/**
	* 删除FC_BANK_BILL
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_DELETE)
	@LogMessage(content = "删除FC_BANK_BILL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		fcBankBillService.deleteFcbankBill(id);
		return success();
	}
	
	/**
	* 批量删除FC_BANK_BILL
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_DELETE)
	@LogMessage(content = "批量删除FC_BANK_BILL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids")String ids){
        String[] idStr = ids.split(",");
        for(String id : idStr){
			fcBankBillService.deleteFcbankBill(id);
		}
		return success();
	}
	
	@PostMapping("/queryBank")
	@ResponseBody
	public List<FcBankBillVo> queryBank(){
		return fcBankBillService.getIsCheckBankAcc(getUserId());
	}
	/**
	 * 判断是否初始化余额
	 */
	@PostMapping("/validateAccInit")
	@ResponseBody
	public AjaxJson validateAccInit(FcBankBillVo fc, Model model){
		fcBankBillService.validateAccInit(fc);
		return success();
	}
	
	/**
	* 银行接口导入列表 页面跳转
	*/
	@LogMessage(content = "查询FC_BANK_BILL列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/interfaceList")
	public String interfaceList(Model model) {
        return "check/fcbankbill/listFcBankBillInterface";
	}
	
	/**
    * 银行接口导入 table列表
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_QUERY)
    @PostMapping("/interfaceDatatable")
    @ResponseBody
    public DataTablesOutput interfaceDataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return fcBankBillService.getInterfaceBankBill(dataTablesInput,getUserId());
    	//return fcBankBillService.getBankBill(dataTablesInput);
    }
	   
    /**
    * 银行接口导入保存列表
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_SAVE)
	@PostMapping("/interfaceAdd")
	@ResponseBody
	public AjaxJson interfaceAdd(FcBankBillVo fc){
		fcBankBillService.saveFcBankBills(fc);
		return success();
    }
	
	/**
    * 银行接口导入提取
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_EXTRACT)
	@PostMapping("/doBatchBillSave")
	@ResponseBody
	public AjaxJson doBatchBillSave(FcBankBillVo fc){
		fcBankBillService.batchSaveFcBankBill(fc, getCurrentUser());
		return success();
    }
}
