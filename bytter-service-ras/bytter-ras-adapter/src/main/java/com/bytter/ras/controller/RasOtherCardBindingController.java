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

import com.bytter.ras.entity.RasLog;
import com.bytter.ras.entity.RasOtherCardBinding;
import com.bytter.ras.service.RasLogService;
import com.bytter.ras.service.RasOtherCardBindingService;
import com.bytter.ras.vo.RasOtherCardBindingVO;
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
 * @Description: 营收稽核第三方账号绑定入口类
 * @author Jinyong.Cao
 * @date 2019-05-06 21:06:40
 *
 */
@Controller
@RequestMapping("/ras/otherCardBinding")
public class RasOtherCardBindingController extends BaseController {
	
	@Autowired
	private RasOtherCardBindingService  rasOtherCardBindingService;
	
	@Autowired
	private RasLogService rasLogService;	
	
	
	/**
	 * 
	* @Title: list  
	* @Description: TODO(跳转 营收稽核第三方账号绑定VM)  
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@GetMapping("/list")
	public String list(Model model){
		 return "ras/listRasOtherCardBinding";
	}
	
	
	@SuppressWarnings("rawtypes")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@LogMessage(content = "查询营收稽核第三方账号绑定信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return rasOtherCardBindingService.getDataTablesUnion(input,RasConstant.GRANT_TYPE_AUDIT,RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP);
	}
	
	
	/**
	 * 
	* @Title: add  
	* @Description: TODO(营收稽核第三方账号绑定信息跳转)  
	* @param @param rasBankCardBinding
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@GetMapping("/add")
	public String add(RasOtherCardBinding rasOtherCardBinding, Model model) {
		if (StringUtils.isNotEmpty(rasOtherCardBinding.getId())) {
			rasOtherCardBinding = rasOtherCardBindingService.get(rasOtherCardBinding.getId());
			model.addAttribute("rasOtherCardBinding", rasOtherCardBinding);
		}
		return "ras/addOtherCardBinding";
	}
	
	/**
	 * 
	* @Title: saveOrstorage  
	* @Description: TODO(保存营收稽核第三方账号绑定保存)  
	* @param @param operate
	* @param @param bvPayeeCheque
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@LogMessage(content = "营收稽核第三方账号绑定保存",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/saveOrstorage/{operate}")
	@ResponseBody
	public AjaxJson saveOrstorage(@PathVariable("operate") String operate,RasOtherCardBindingVO  rasOtherCardBindingVO){
		AjaxJson ajaxJson = rasOtherCardBindingService.baseSaveOrUpdate(rasOtherCardBindingVO,operate,this.getCurrentUser());
		return ajaxJson;
	}
	
	
	@LogMessage(content = "营收稽核第三方账号绑定详细信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{rsId}")
	public String view(@PathVariable("rsId") String rsId, Model model) {
		RasOtherCardBindingVO rasOtherCardBindingVO = 
			rasOtherCardBindingService.getRasOtherCardBindingVOSign(rsId);
		List<RasLog> raslogList = rasLogService.listRasLog(rasOtherCardBindingVO.getId());
		model.addAttribute("raslogList", raslogList);
		model.addAttribute("rasOtherCardBindingVo", rasOtherCardBindingVO);
		return "ras/viewRasOtherCardBinding";
	}
	
	
	/**
	 * 
	* @Title: update  
	* @Description: TODO(营收稽核第三方账号绑定修改页面跳转)  
	* @param @param id
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@GetMapping("/update/{rsId}")
	public String update(@PathVariable("rsId") String rsId ,Model model) {
		RasOtherCardBindingVO rasOtherCardBindingVO =
			rasOtherCardBindingService.getRasOtherCardBindingVOSign(rsId);
		model.addAttribute("rasOtherCardBindingVo", rasOtherCardBindingVO);
		return "ras/updateRasOtherCardBinding";
	}
	
	/**
	 * 
	* @Title: saveImport  
	* @Description: TODO(导入营收稽核第三方账号绑定信息)  
	* @param @param list
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	@LogMessage(content = "导入营收稽核第三方账号绑定信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<RasOtherCardBindingVO> list) {
		AjaxJson json=success();
		boolean flag=rasOtherCardBindingService.saveImport(list,this.getCurrentUser());
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
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_OTHERCARDBINDING_OP)
	@LogMessage(content = "批量启用/禁用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchStorage/{operate}")
	@ResponseBody
	public AjaxJson batchStorage(@PathVariable("operate") String operate,String ids){
		return  rasOtherCardBindingService.batchOperation(ids,operate,this.getCurrentUser());
	}
	
}
