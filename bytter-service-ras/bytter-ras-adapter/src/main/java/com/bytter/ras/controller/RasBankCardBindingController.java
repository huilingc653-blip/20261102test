package com.bytter.ras.controller;

import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bytter.ras.entity.RasBankCardBinding;
import com.bytter.ras.entity.RasLog;
import com.bytter.ras.service.RasBankCardBindingService;
import com.bytter.ras.service.RasLogService;
import com.bytter.ras.vo.RasBankCardBindingVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;


/**   
 * @Title: RasBankCardBindingController  
 * @Description: 营收稽核银行卡绑定入口类
 * @author Jinyong.Cao
 * @date 2019-05-05 20:06:40
 *
 */
@Controller
@RequestMapping("/ras/bankCardBinding")
public class RasBankCardBindingController extends BaseController{

	@Autowired
	private RasBankCardBindingService  rasBankCardBindingService;
	
	@Autowired
	private RasLogService rasLogService;	
	/**
	 * 
	* @Title: list  
	* @Description: TODO(跳转营收稽核银行卡VM)  
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@GetMapping("/list")
	public String list(Model model){
		 return "ras/listRasBankCardBinding";
	}
	
	
	@SuppressWarnings("rawtypes")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@LogMessage(content = "查询营收稽核银行卡信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return rasBankCardBindingService.getDataTablesUnion(input,RasConstant.GRANT_TYPE_AUDIT,RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP);
	}
	
	
	/**
	 * 
	* @Title: add  
	* @Description: TODO(营收稽核银行卡信息新增页面跳转)  
	* @param @param rasBankCardBinding
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@GetMapping("/add")
	public String add(RasBankCardBinding rasBankCardBinding, Model model) {
		if (StringUtils.isNotEmpty(rasBankCardBinding.getId())) {
			rasBankCardBinding = rasBankCardBindingService.get(rasBankCardBinding.getId());
			model.addAttribute("rasBankCardBinding", rasBankCardBinding);
		}
		return "ras/addRasBankCardBinding";
	}
	
	
	/**
	 * 
	* @Title: saveOrstorage  
	* @Description: TODO(保存营收稽核银行卡信息)  
	* @param @param operate
	* @param @param bvPayeeCheque
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@LogMessage(content = "保存营收稽核银行卡信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/saveOrstorage/{operate}")
	@ResponseBody
	public AjaxJson saveOrstorage(@PathVariable("operate") String operate,RasBankCardBindingVO  rasBankCardBindingVo){
		AjaxJson ajaxJson = rasBankCardBindingService.baseSaveOrUpdate(rasBankCardBindingVo,operate,this.getCurrentUser());
		return ajaxJson;
	}
	
	
	@LogMessage(content = "稽核银行卡详细信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{rsId}")
	public String view(@PathVariable("rsId") String rsId, Model model) {
		RasBankCardBindingVO rasBankCardBindingVo = 
				rasBankCardBindingService.getRasBankCardBindingSign(rsId);
		List<RasLog> raslogList = rasLogService.listRasLog(rasBankCardBindingVo.getId());
		model.addAttribute("raslogList", raslogList);
		model.addAttribute("rasBankCardBindingVo", rasBankCardBindingVo);
		return "ras/viewRasBankBinding";
	}
	
	/**
	 * 
	* @Title: update  
	* @Description: TODO(营收稽核银行卡信息修改页面跳转)  
	* @param @param id
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@GetMapping("/update/{rsId}")
	public String update(@PathVariable("rsId") String rsId ,Model model) {
		RasBankCardBindingVO rasBankCardBindingVo = 
				rasBankCardBindingService.getRasBankCardBindingSign(rsId);		
		model.addAttribute("rasBankCardBindingVo", rasBankCardBindingVo);
		return "ras/updateRasBankCardBinding";
	}
	
	/**
	 * 
	* @Title: saveImport  
	* @Description: TODO(导入营收稽核银行卡信息)  
	* @param @param list
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	@LogMessage(content = "导入营收稽核银行卡信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<RasBankCardBindingVO> list) {
		AjaxJson json=success();
		boolean flag=rasBankCardBindingService.saveImport(list,this.getCurrentUser());
		json.setSuccess(flag);
		return success();
	}
	
	/**
	 * 
	* @Title: batchStorage  
	* @Description: TODO(批量启用/禁用)  
	* @param @param operate
	* @param @param ids
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_BANKCARDBINDING_OP)
	@LogMessage(content = "批量启用/禁用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchStorage/{operate}")
	@ResponseBody
	public AjaxJson batchStorage(@PathVariable("operate") String operate,String ids){
		return  rasBankCardBindingService.batchOperation(ids,operate,this.getCurrentUser());
	}
}
