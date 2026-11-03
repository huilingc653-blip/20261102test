package com.bytter.ras.controller;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bytter.ras.service.RasAuditTaskService;
import com.bytter.ras.vo.RasAuditTaskVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;

/**   
 * @Title: Controller  
 * @Description: 稽核任务维护
 * @author onlineGenerator
 * @date 2019-05-15 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/ras/auditTask")
public class RasAuditTaskController extends BaseController {

	@Autowired
	private RasAuditTaskService rasAuditTaskService;
	
	/**
	 * 稽核任务维护列表页面
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	public String list(Model model) {
	    return "ras/listRasAuditTask";
	}

	/**
	 * 稽核任务维护列表数据获取
	 * @param input
	 * @return 列表数据
	 */
	@SuppressWarnings("rawtypes")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@LogMessage(content = "查询稽核任务维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return rasAuditTaskService.getDataTablesUnion(input);
	}
	
	/**
	 * 
	* @Title: 稽核任务维护新增页面  
	* @param  model
	* @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@GetMapping("/add")
	public String add(Model model) {
		return "ras/addRasAuditTask";
	}
	
	/**
	 * 
	 * @Title: 稽核任务维护修改页面  
	 * @param  id
	 * @param  model
	 * @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@GetMapping("/update")
	public String update(String id,Model model) {
		if(StringUtils.isNotEmpty(id)){
			RasAuditTaskVO info=rasAuditTaskService.getRasAuditTaskVO(id);
			info.setOpType(RasConstant.RAS_TASK_OPTYPE_UPDATEOP);
			model.addAttribute("info", info);
		}
		return "ras/updateRasAuditTask";
	}
	
	/**
	 * 
	 * @Title: 稽核任务维护扫描参数设置  
	 * @param  id
	 * @param  model
	 * @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@GetMapping("/scanparameter")
	public String scanparameter(String id,Model model) {
		if(StringUtils.isNotEmpty(id)){
			RasAuditTaskVO info=rasAuditTaskService.getRasAuditTaskVO(id);
			info.setOpType(RasConstant.RAS_TASK_OPTYPE_TASKOP);
			model.addAttribute("info", info);
		}
		return "ras/taskRasAuditTask";
	}

	/**
	 * 
	* @Title: 稽核任务维护复制页面  
	* @param  id
	* @param  model
	* @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@GetMapping("/clone")
	public String clone(String id,Model model) {
		if(StringUtils.isNotEmpty(id)){
			RasAuditTaskVO info=rasAuditTaskService.getRasAuditTaskVO(id);
			info.setOpType(RasConstant.RAS_TASK_OPTYPE_CLONEOP);
			info.setLastId(info.getId());
			info.setId(null);
			info.setCode(null);
			model.addAttribute("info", info);
		}
		return "ras/updateRasAuditTask";
	}
	
	/**
	 * 
	* @Title: 稽核任务维护新增或修改操作  
	* @param  model
	* @return 处理结果
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@LogMessage(content = "稽核任务维护新增或修改操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(@RequestBody RasAuditTaskVO info){
		AjaxJson ajaxJson = rasAuditTaskService.saveOrUpdate(info,this.getCurrentUser());
		return ajaxJson;
	}
	
	/**
	* 判断相同单位下，是否code也相同
	* @param id
	* @return
	*/
	@PostMapping("/codeCheck")
	@ResponseBody
    public AjaxJson codeCheck(String code,String id)
    {
		AjaxJson reInfo=success();
		reInfo.setSuccess(rasAuditTaskService.codeCheck(code,id));
		return reInfo;
    }
	

	/**
	 * 
	* @Title: 稽核任务手工执行  
	* @param  model
	* @return 处理结果
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITTASK_OP)
	@LogMessage(content = "稽核任务手工执行操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/executeScan")
	@ResponseBody
	public AjaxJson executeScan(@RequestBody RasAuditTaskVO info){
		AjaxJson reInfo=success();
		reInfo.setSuccess(rasAuditTaskService.executeScan(info,this.getCurrentUser()));
		return reInfo;
	}
	
	/**
	 * 
	* @Title: 通过ID删除稽核任务
	* @param id 需要删除的ID
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@LogMessage(content = "删除",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String id,String validSign){
		rasAuditTaskService.delete(id);
		return new AjaxJson(true);
	}
}
