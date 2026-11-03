package com.bytter.biz.finacial.closedeal.controller;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.closedeal.service.FcAccountBalanceService;
import com.bytter.biz.finacial.closedeal.vo.FcAccountBalanceVo;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 账户批量余额表
 * @author onlineGenerator
 * @date 2017-06-23 14:22:37
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/closedeal/fcaccbalance")
public class FcAccountBalanceController extends BaseController {
	@Autowired
	private FcAccountBalanceService fcAccountBalanceService;
	/**
	* 账户批量余额表列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("closedeal:fcaccbalance:index")
	@LogMessage(content = "查询账户批量余额表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/closedeal/fcaccbalance/listFcAccBalance";
	}
    /**
    * 账户批量余额表 table列表
    */
    ////@RequiresPermissions("closedeal:fcaccbalance:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccountBalanceService.getFcAccBalance(dataTablesInput,FinacialResourceConstant.FINACIAL_FCACCOUNTBALANCE_QUERY, getUserId());
    }
    @RequestMapping("/excel")
	public String export(ModelMap map,String eqs_corpId,String eqs_netId,Date ged_balanceDate,Date led_balanceDate,String eqs_curId,String eqs_subjectid){
    	List<FcAccountBalanceVo>  list = fcAccountBalanceService.export(eqs_corpId,eqs_netId,ged_balanceDate,led_balanceDate,eqs_curId , eqs_subjectid);
    	map.put(NormalExcelConstants.FILE_NAME, "银行每日余额表");
    	map.put(NormalExcelConstants.CLASS, FcAccountBalanceVo.class);
    	map.put(NormalExcelConstants.PARAMS, new ExportParams("银行每日余额表","导出人：admin","账单信息"));
    	map.put(NormalExcelConstants.DATA_LIST, list);
    	return NormalExcelConstants.EXCEL_VIEW;
	}
    
    @PostMapping("/queryAccount")
	@ResponseBody
	public List<FcAccDTO> queryBank(){
		return fcAccountBalanceService.getAccount(getUserId());
	}
}
