package com.bytter.biz.finacial.paramconfig.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcInstrateService;
import com.bytter.system.pricing.dto.FcInstrateDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: 科目利率表
 * @author onlineGenerator
 * @date 2017-05-18 15:45:32
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcinstrate")
public class FcInstrateController extends BaseController {
//	@Autowired
//	private FcInstrateService fcInstrateService;

	@DubboReference(check = false)
	private IFcInstrateService iFcInstrateService;
	/**
	* 科目利率表列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@LogMessage(content = "查询科目利率表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/instrate/listFcInstrate";
	}
    /**
    * 科目利率表 table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return iFcInstrateService.fcInstrateList(dataTablesInput);
		//return iFcInstrateService.getDataTables(dataTablesInput);
    }
	/**
	* 科目利率表新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@GetMapping("/add")
	public String add(FcInstrateDTO fcInstrate, Model model) {
		if (StringUtils.isNotEmpty(fcInstrate.getId())) {
			fcInstrate = iFcInstrateService.getfcInstrate(fcInstrate.getId());
			model.addAttribute("fcInstratePage", fcInstrate);
		}
		return "finacial/paramconfig/instrate/addFcInstrate";
	}
	/**
	* 科目利率表编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@GetMapping("/update")
	public String update(FcInstrateDTO fcInstrate, Model model) {
		if (StringUtils.isNotEmpty(fcInstrate.getId())) {
			fcInstrate = iFcInstrateService.getfcInstrate(fcInstrate.getId());
			model.addAttribute("fcInstratePage", fcInstrate);
		}
		return "finacial/paramconfig/instrate/updateFcInstrate";
	}
	/**
	* 科目利率表详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@LogMessage(content = "查询科目利率表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcInstrate fcInstrate = iFcInstrateService.get(id);
//		FcInstrateVo fcInstrate = iFcInstrateService.getfcInstrate(id);
		FcInstrateDTO fcInstrate = iFcInstrateService.getfcInstrate(id);
		model.addAttribute("fcInstratePage", fcInstrate);
		return "finacial/paramconfig/instrate/viewFcInstrate";
	}
	/**
	 * 添加科目利率表
	 * @param fcInstrate
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@LogMessage(content = "添加科目利率表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcInstrateDTO fcInstrate){
		iFcInstrateService.save(fcInstrate,this.getCurrentUser().getLoginName());
		return success();
	}
	/**
	* 修改科目利率表
	* @param fcInstrate
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@LogMessage(content = "修改科目利率表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcInstrateDTO fcInstrate){
		FcInstrateDTO t = iFcInstrateService.getfcInstrate(fcInstrate.getId());
		BeanUtils.copyNotNullProperties(fcInstrate, t);
		iFcInstrateService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除科目利率表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCINSTRATE_APPLY)
	@LogMessage(content = "删除科目利率表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		iFcInstrateService.deleteFcInstrate(id);
		return success();
	}
	/**
	* 批量删除科目利率表
	* @param ids
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcinstrate:delete")
	@LogMessage(content = "批量删除科目利率表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcInstrateDTO fcInstrate = iFcInstrateService.getfcInstrate(id);
			iFcInstrateService.deleteEntity(fcInstrate);
		}
		return success();
	}
}
