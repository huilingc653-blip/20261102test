package com.bytter.ras.controller;

import java.util.List;

import com.bytter.ras.service.RasRevenueService;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.ras.api.service.IExcelCacheService;
import com.bytter.ras.api.constant.RasConstant;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核营收数据Controller
 * @date 2019/5/15 13:39
 */
@Controller
@RequestMapping("/ras/revenue")
public class RasRevenueController extends BaseController {

    @Autowired
    private RasRevenueService rasRevenueService;

	@Autowired(required=false)
    private IExcelCacheService excelCacheService;

    /**
     * 跳转营收数据查询页面
     * @param model
     * @return
     */
    @GetMapping("/queryList")
    public String list(Model model) {
        return "ras/listRevenueQuery";
    }

    /**
     * 营收数据查询列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "营收数据查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return rasRevenueService.getDataTablesUnion(dataTablesInput,RasConstant.GRANT_TYPE_QUERY, RasConstant.RAS_RESOURCE_REVENUEQUERY_OP);
    }
    
    
    /*****########营收数据导入块#########***********************/
    /**
     * 跳转营收数据导入查询页面
     * @param model
     * @return
     */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_REVENUEIMPORT_OP)
    @GetMapping("/list")
    public String importList(Model model) {
        return "ras/listImportRevenueQuery";
    }
    

    /**
     * 营收数据查询列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "营收导入数据查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/importDatatable")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_REVENUEIMPORT_OP)
    @ResponseBody
    public DataTablesOutput importDatatable(@RequestBody DataTablesInput dataTablesInput){
    	dataTablesInput.getFormdata().put("eqi_status", 1);
        return rasRevenueService.getDataTablesUnion(dataTablesInput,RasConstant.GRANT_TYPE_AUDIT, RasConstant.RAS_RESOURCE_REVENUEIMPORT_OP);
    }
    
    
    /**
	 * 
	* @Title: saveImport  
	* @Description: TODO(营收数据导入)  
	* @param @param list
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	@LogMessage(content = "导入营收数据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestParam("cacheCode") String cacheCode) {
		List<RasRevenueDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, RasRevenueDtlVO.class);
		return rasRevenueService.saveImportRasRevenueDtl(list,this.getCurrentUser());
	}

	
	@GetMapping("/importConf")
    public String importConf(Model model) {
        return "ras/importConf";
    }

	
	/**
	 * 
	* @Title: batchStorage  
	* @Description: TODO(营收作废)  
	* @param @param operate
	* @param @param ids
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_REVENUEIMPORT_OP)
	@LogMessage(content = "批量作废/单条作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchStorage/{operate}")
	@ResponseBody
	public AjaxJson batchStorage(@PathVariable("operate") String operate,String ids){
		return  rasRevenueService.batchOperation(ids,this.getCurrentUser());
	}
	
}
