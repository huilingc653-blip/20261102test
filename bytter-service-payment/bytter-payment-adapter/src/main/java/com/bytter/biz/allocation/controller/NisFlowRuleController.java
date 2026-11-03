package com.bytter.biz.allocation.controller;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.service.NisFlowRuleService;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisFlowRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: Controller
 * @Description: 资金调拨流水规则
 * @author onlineGenerator
 * @date 2024-05-08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/nisFlowRule")
public class NisFlowRuleController extends BaseController {
	@Autowired
	private NisFlowRuleService nisFlowRuleService;

	/**
	* 资金调拨流水规则列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询资金调拨流水规则列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listNisFlowRule";
	}

	/**
    * 资金调拨流水规则列表 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return nisFlowRuleService.findListBysql(dataTablesInput);
    }

	@GetMapping("/add")
	public String add(Model model) {
		return "netbank/addNisFlowRule";
	}

	/**
	 * 添加资金调拨流水规则
	 * @param nisFlowRuleVO
	 * @return
	 */
	@LogMessage(content = "添加资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisFlowRuleVO nisFlowRuleVO, HttpServletRequest request){
		ICurrentUser currentUser = this.getCurrentUser();
		nisFlowRuleService.save(nisFlowRuleVO, request, currentUser);
		return success();
	}

	/**
	 * 资金调拨流水规则编辑页面跳转
	 * @param vo
	 * @return
	 */
	@LogMessage(content = "资金调拨流水规则编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@GetMapping("/update")
	public String update(NisFlowRuleVO vo, Model model){
		NisFlowRuleVO nisFlowRuleVO = nisFlowRuleService.getNisFlowRule(vo.getId());
		model.addAttribute("nisFlowRuleVO",nisFlowRuleVO);
		model.addAttribute("nisFlowRulePurpose", nisFlowRuleVO.getNisFlowRulePurposes().get(0));
		return "netbank/updateNisFlowRule";
	}

	/**
	 * 修改资金调拨流水规则
	 * @param nisFlowRuleVO
	 * @return
	 */
	@LogMessage(content = "修改资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(NisFlowRuleVO nisFlowRuleVO, HttpServletRequest request){
		ICurrentUser currentUser = this.getCurrentUser();
		nisFlowRuleService.update(nisFlowRuleVO, request, currentUser);
		return success();
	}

	/**
	 * 删除资金调拨流水规则
	 * @param id
	 * @return
	 */
	@LogMessage(content = "删除资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		nisFlowRuleService.delete(id);
		return success();
	}

	/**
	 * 资金调拨流水规则详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询资金调拨流水规则详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		NisFlowRuleVO nisFlowRuleVO = nisFlowRuleService.getNisFlowRule(id);
		model.addAttribute("nisFlowRuleVO",nisFlowRuleVO);
		return "netbank/viewNisFlowRule";
	}
}
