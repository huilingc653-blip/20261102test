package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeChargeRuleRel;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeRule;
import com.bytter.biz.finacial.paramconfig.service.FcChargeRuleService;
import com.bytter.biz.finacial.paramconfig.service.FcChargeTypeService;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeRuleVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 费用计提维护表
 * @author onlineGenerator
 * @date 2017-05-16 09:06:35
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/paramconfig/fcchargerule")
public class FcChargeRuleController extends BaseController {
	@Autowired
	private FcChargeRuleService fcChargeRuleService;
	@Autowired
	private FcChargeTypeService fcChargeTypeService;
	/**
	* 费用计提维护表列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:index")
	@LogMessage(content = "查询费用计提维护表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/fcchargerule/listFcChargeRule";
	}
    
    /**
     * 费用计提维护表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
	@PostMapping("/datatablelist")
    @ResponseBody
    public DataTablesOutput toList(@RequestBody  DataTablesInput dataTablesInput){
    	return fcChargeRuleService.listFcChargeRule(dataTablesInput);
    }
    /**
     * <b>功能介绍：</b>获取收费账号<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param subCode
     * @param netId
     * @return
     */
    @GetMapping("/getChargeAcc")
    @ResponseBody
    public List<FcAccDTO> getChargeAcc(String subCode, String netId){
    	List<FcAccDTO> acc = new ArrayList<FcAccDTO>();
    	FcAccDTO  initAcc = new FcAccDTO();
    	initAcc.setBlnctrAcc("");
    	if(EmptyUtils.isEmpty(netId)&&EmptyUtils.isEmpty(subCode)){
    		acc.add(initAcc);
    		return acc;
    	}
    	acc=fcChargeRuleService.getChargeAcc(subCode,netId);
    	if(EmptyUtils.isEmptyList(acc)){
    		acc = new ArrayList<FcAccDTO>();
    		acc.add(initAcc);
    	}
    	return acc;
    }
    
	/**
	* 费用计提维护表新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:save")
	@GetMapping("/add")
	public String add(FcChargeRule fcChargeRule, Model model) {
		if (StringUtils.isNotEmpty(fcChargeRule.getId())) {
			fcChargeRule = fcChargeRuleService.get(fcChargeRule.getId());
			model.addAttribute("fcChargeRulePage", fcChargeRule);
		}
		model.addAttribute("chargeType",fcChargeTypeService.listAll());
		model.addAttribute("initNetId",fcChargeRuleService.getInitNetId());
		return "finacial/paramconfig/fcchargerule/addFcChargeRule";
	}
	/**
	* 费用计提维护表编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:update")
	@GetMapping("/update")
	public String update(FcChargeRule fcChargeRule, Model model) {
		if (StringUtils.isNotEmpty(fcChargeRule.getId())) {
			fcChargeRule = fcChargeRuleService.get(fcChargeRule.getId(),true);
			model.addAttribute("fcChargeRulePage", fcChargeRule);
			model.addAttribute("voucherEntryDefines", fcChargeRuleService.listFcVoucherEntryDefine(fcChargeRule.getId()));
			
			List<ChargeTypeChargeRuleRel> types = fcChargeRuleService.listChargeTypeChargeRules(fcChargeRule.getId());
			StringBuilder fcChargeTypes = new StringBuilder();
			for (ChargeTypeChargeRuleRel fct : types) {
				fcChargeTypes.append(fct.getChargeTypeId()+",");
			}
			model.addAttribute("ChargeRuleChargeType", fcChargeTypes);
		}
		model.addAttribute("chargeType",fcChargeTypeService.listAll());
		return "finacial/paramconfig/fcchargerule/updateFcChargeRule";
	}
	/**
	* 费用计提维护表详情页
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:view")
	@LogMessage(content = "查询费用计提维护表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcChargeRule fcChargeRule = fcChargeRuleService.get(id);
		FcChargeRuleVo fcChargeRule = fcChargeRuleService.getFcChargeRuleById(id);
		model.addAttribute("fcChargeRulePage", fcChargeRule);
		List<ChargeTypeChargeRuleRel> types = fcChargeRuleService.listChargeTypeChargeRules(fcChargeRule.getId());
		StringBuilder fcChargeTypes = new StringBuilder();
		for (ChargeTypeChargeRuleRel fct : types) {
			fcChargeTypes.append(fcChargeTypeService.get(fct.getChargeTypeId()).getChargeTypeName()+", ");
		}
		model.addAttribute("ChargeRuleChargeType", fcChargeTypes.substring(0, fcChargeTypes.length()-2));
		model.addAttribute("voucherEntryDefines", fcChargeRuleService.listFcVoucherEntryDefine(fcChargeRule.getId()));
		return "finacial/paramconfig/fcchargerule/viewFcChargeRule";
	}
	/**
	 * 添加费用计提维护表
	 * @param fcChargeRule
	 * @return
	 */
	////@RequiresPermissions("paramconfig:fcchargerule:save")
	@LogMessage(content = "添加费用计提维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcChargeRule fcChargeRule,@RequestParam("chargeTypeIds[]") List<String> chargeTypeIds){
		fcChargeRuleService.save(fcChargeRule, chargeTypeIds, getUserId());
		return success();
	}
	/**
	* 修改费用计提维护表
	* @param fcChargeRule
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:update")
	@LogMessage(content = "修改费用计提维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcChargeRule fcChargeRule,@RequestParam("chargeTypeIds[]") List<String> chargeTypeIds){
		FcChargeRule t = fcChargeRuleService.get(fcChargeRule.getId());
		BeanUtils.copyNotNullProperties(fcChargeRule, t);
		t.setSummary(fcChargeRule.getSummary());
		t.setRaceCode(fcChargeRule.getRaceCode());
		t.setVoucherType(fcChargeRule.getVoucherType());
		t.setCurCode(fcChargeRule.getCurCode());
		fcChargeRuleService.saveOrUpdate(t, chargeTypeIds, getUserId());
		return  success();
	}
	/**
	* 删除费用计提维护表
	* @param id
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:delete")
	@LogMessage(content = "删除费用计提维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcChargeRule fcChargeRule = fcChargeRuleService.get(id);
		fcChargeRuleService.deleteEntity(fcChargeRule);
		return success();
	}
	/**
	* 批量删除费用计提维护表
	* @param ids
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargerule:delete")
	@LogMessage(content = "批量删除费用计提维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcChargeRule fcChargeRule = fcChargeRuleService.get(id);
			fcChargeRuleService.deleteEntity(fcChargeRule);
		}
		return success();
	}
	
	@GetMapping("/json")
    @ResponseBody
    public List<FcVoucherTypeDTO> json(){
        return fcChargeRuleService.getFcVoucherType();
    }
}
