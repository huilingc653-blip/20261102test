package com.bytter.ras.controller;

import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.service.RasAuditStrategyService;
import com.bytter.ras.vo.RasAuditStrategyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 稽核策略维护
 * @author onlineGenerator
 * @date 2019-05-15 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/ras/auditStrategy")
public class RasAuditStrategyController extends BaseController {

	@Autowired
	private RasAuditStrategyService rasAuditStrategyService;


	/**
	 * 稽核策略维护列表页面
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	public String list(Model model) {
	    return "ras/listRasAuditStrategy";
	}

	/**
	 * 稽核策略维护列表数据获取
	 * @param input
	 * @return 列表数据
	 */
	@SuppressWarnings("rawtypes")
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@LogMessage(content = "查询稽核策略维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return rasAuditStrategyService.getDataTablesUnion(input);
	}

	/**
	 *
	* @Title: 稽核策略维护新增页面
	* @param  model
	* @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@GetMapping("/add")
	public String add(Model model) {
		return "ras/addRasAuditStrategy";
	}

	/**
	 *
	* @Title: 稽核策略维护修改页面
	* @param  id
	* @param  model
	* @return 页面地址
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@GetMapping("/update")
	public String update(String id,Model model) {
		if(StringUtils.isNotEmpty(id)){
			RasAuditStrategy info=rasAuditStrategyService.get(id, true);
			if(StringUtils.isNotEmpty(info.getRules())){
				info.setRules(info.getRules().replace(RasConstant.RAS_AUDIT_RULES_MONEY+",", ""));
			}
			model.addAttribute("info", info);
			if(RasConstant.RAS_PAYTYPE_001.equals(info.getPayType())){
				model.addAttribute("rulesDisplay", RasConstant.RAS_AUDIT_RULES_MONEY_COUNT);
			}else if(RasConstant.RAS_PAYTYPE_006.equals(info.getPayType())){
				model.addAttribute("rulesDisplay", RasConstant.RAS_AUDIT_RULES_OTHER_COUNT);
			}else{
				model.addAttribute("rulesFilter", RasConstant.RAS_AUDIT_RULES_MONEY
						+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS
						+RasConstant.RAS_AUDIT_RULES_MONEY_COUNT
						+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS
						+RasConstant.RAS_AUDIT_RULES_OTHER_COUNT);
			}
		}
		return "ras/updateRasAuditStrategy";
	}

	/**
	 *
	* @Title: 稽核策略维护新增或修改操作
	* @param  model
	* @return 处理结果
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@LogMessage(content = "稽核策略维护新增或修改操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(@RequestBody RasAuditStrategyVO info){
		AjaxJson ajaxJson = rasAuditStrategyService.saveOrUpdate(info,this.getCurrentUser());
		return ajaxJson;
	}


	/**
	 *
	* @Title: 通过ID设置策略启用和禁用
	* @param id 需要设置的ID
	* @param validSign 设置的类型 1启用0禁用
	* @return AjaxJson    返回类型
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@LogMessage(content = "批量启用/禁用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchValidSign")
	@ResponseBody
	public AjaxJson batchValidSign(String id,String validSign){
		return  rasAuditStrategyService.batchValidSign(id,validSign,this.getCurrentUser());
	}

	/**
	 *
	* @Title: 通过ID删除策略信息
	* @param id 需要删除的ID
	* @return AjaxJson    返回类型
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_AUDITSTRATEGY_OP)
	@LogMessage(content = "删除",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String id,String validSign){
		if(StringUtils.isNotEmpty(id)){
			rasAuditStrategyService.delete(id);
		}
		return new AjaxJson(true);
	}
}
