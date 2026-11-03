package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.service.CheckQueryService;
import com.bytter.check.vo.FcCompareAccTempVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @Title: 对账中心--ERP对账单、中心对账单
 * @Description: FC_COMPARE_ACC
 * @author zhengxn
 * @date 2017-06-02 17:21:47
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/erpquery")
public class ErpQueryController extends BaseController {
	@Autowired
	private CheckQueryService checkQueryService;
	/**erp对账单**/
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_QUERY)
	@LogMessage(content = "查询FC_COMPARE_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("type",2);
		return "check/query/listErpQuery";
	}
	/**erp对账单 Table表**/
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput,String type) {
		return checkQueryService.listByTable(dataTablesInput,FinacialResourceConstant.FINACIAL_CHECK_ERP_QUERY,getUserId(),type);
	}
	
	/**导出excel**/
	@PostMapping("/excel")
	public String excel(ModelMap map,String eqs_compAcc,Date ged_tallyDate,Date led_tallyDate,String eqs_compareSign){
		List<FcCompareAcc> list = null;
		if(StringUtils.isNoneBlank(eqs_compAcc) 
				|| ged_tallyDate!=null 
				|| led_tallyDate!=null
				|| StringUtils.isNoneBlank(eqs_compareSign)){
			 list = checkQueryService.findListBySql(eqs_compAcc, led_tallyDate, led_tallyDate, eqs_compareSign);
		}else{	
		
			 list = checkQueryService.listQueryAll();
		}
		map.put(NormalExcelConstants.FILE_NAME, "ERP对账单");
        map.put(NormalExcelConstants.CLASS,FcCompareAcc.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("ERP对账单", "导出人:admin", "账单信息"));
        map.put(NormalExcelConstants.DATA_LIST,list);
        
		return  NormalExcelConstants.EXCEL_VIEW;
	}
	
	
	/**中心对账单**/
	@LogMessage(content = "查询FC_COMPARE_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list1")
	public String list1(Model model) {
		model.addAttribute("type",1);
		return "check/query/listCheckQuery";
	}
	
    
	/**中心对账单 table列表* */
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_QUERY)
    @PostMapping("/datatable1")
    @ResponseBody
    public DataTablesOutput dataTable1(@RequestBody  DataTablesInput dataTablesInput,String type){
    	 DataTablesOutput<FcCompareAccTempVo> dto = checkQueryService.listByTable(dataTablesInput,FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_QUERY,getUserId(),type);
    	 List<FcCompareAccTempVo> list = dto.getData();
    	 FcCompareAccTempVo acc = new FcCompareAccTempVo();
    	 BigDecimal sumDebitMoney = new BigDecimal(0); 
    	 BigDecimal sumCreditMoney = new BigDecimal(0); 
    	 BigDecimal sumBalance = new BigDecimal(0); 
    	 for (FcCompareAccTempVo fcCompareAcc : list) {
    		
    		 sumDebitMoney = sumDebitMoney.add(fcCompareAcc.getDebitMoney()==null?new BigDecimal(0):fcCompareAcc.getDebitMoney());
    		 sumCreditMoney =sumCreditMoney.add(fcCompareAcc.getCreditMoney()==null?new BigDecimal(0):fcCompareAcc.getCreditMoney());
    		 sumBalance=sumBalance.add(fcCompareAcc.getBalance()==null?new BigDecimal(0):fcCompareAcc.getBalance());
		}
    	acc.setTallyDate(null);
    	acc.setCompAcc("合计");
    	acc.setDebitMoney(sumDebitMoney);
    	acc.setCreditMoney(sumCreditMoney);
    	acc.setBalance(sumBalance);
    	list.add(acc);
    	return  dto;
    }
 
       
   
}
