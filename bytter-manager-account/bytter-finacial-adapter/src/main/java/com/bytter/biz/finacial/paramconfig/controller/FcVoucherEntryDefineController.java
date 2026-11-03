package com.bytter.biz.finacial.paramconfig.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherEntryDefineService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
/**   
 * @Title: Controller  
 * @Description: FC_VOUCHER_ENTRY_DEFINE
 * @author onlineGenerator
 * @date 2017-05-22 16:44:06
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcvoucherentrydefine")
public class FcVoucherEntryDefineController extends BaseController {
	@Autowired
	private FcVoucherEntryDefineService fcVoucherEntryDefineService;
	/**
	* FC_VOUCHER_ENTRY_DEFINE列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:index")
	@LogMessage(content = "查询FC_VOUCHER_ENTRY_DEFINE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/paramconfig/listFcVoucherEntryDefine";
	}
    /**
    * FC_VOUCHER_ENTRY_DEFINE table列表
    *
    * @return
    */
    ////@RequiresPermissions("paramconfig:fcvoucherentrydefine:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcVoucherEntryDefineService.getDataTables(dataTablesInput);
    }
	/**
	* FC_VOUCHER_ENTRY_DEFINE新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:save")
	@GetMapping("/add")
	public String add(FcVoucherEntryDefine fcVoucherEntryDefine, Model model) {
		if (StringUtils.isNotEmpty(fcVoucherEntryDefine.getId())) {
			fcVoucherEntryDefine = fcVoucherEntryDefineService.get(fcVoucherEntryDefine.getId());
			model.addAttribute("fcVoucherEntryDefinePage", fcVoucherEntryDefine);
		}
		return "finacial/paramconfig/paramconfig/addFcVoucherEntryDefine";
	}
	/**
	* FC_VOUCHER_ENTRY_DEFINE编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:update")
	@GetMapping("/update")
	public String update(FcVoucherEntryDefine fcVoucherEntryDefine, Model model) {
		if (StringUtils.isNotEmpty(fcVoucherEntryDefine.getId())) {
			fcVoucherEntryDefine = fcVoucherEntryDefineService.get(fcVoucherEntryDefine.getId());
			model.addAttribute("fcVoucherEntryDefinePage", fcVoucherEntryDefine);
		}
		return "finacial/paramconfig/paramconfig/updateFcVoucherEntryDefine";
	}
	/**
	* FC_VOUCHER_ENTRY_DEFINE详情页
	*
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:view")
	@LogMessage(content = "查询FC_VOUCHER_ENTRY_DEFINE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcVoucherEntryDefine fcVoucherEntryDefine = fcVoucherEntryDefineService.get(id);
		model.addAttribute("fcVoucherEntryDefinePage", fcVoucherEntryDefine);
		return "finacial/paramconfig/paramconfig/viewFcVoucherEntryDefine";
	}
	/**
	 * 添加FC_VOUCHER_ENTRY_DEFINE
	 * @param fcVoucherEntryDefine
	 * @return
	 */
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:save")
	@LogMessage(content = "添加FC_VOUCHER_ENTRY_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcVoucherEntryDefine fcVoucherEntryDefine){
		fcVoucherEntryDefineService.save(fcVoucherEntryDefine);
		return success();
	}
	/**
	* 修改FC_VOUCHER_ENTRY_DEFINE
	* @param fcVoucherEntryDefine
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:update")
	@LogMessage(content = "修改FC_VOUCHER_ENTRY_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcVoucherEntryDefine fcVoucherEntryDefine){
		FcVoucherEntryDefine t = fcVoucherEntryDefineService.get(fcVoucherEntryDefine.getId());
		BeanUtils.copyNotNullProperties(fcVoucherEntryDefine, t);
		fcVoucherEntryDefineService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_VOUCHER_ENTRY_DEFINE
	* @param id
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:delete")
	@LogMessage(content = "删除FC_VOUCHER_ENTRY_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcVoucherEntryDefine fcVoucherEntryDefine = fcVoucherEntryDefineService.get(id);
		fcVoucherEntryDefineService.deleteEntity(fcVoucherEntryDefine);
		return success();
	}
	/**
	* 批量删除FC_VOUCHER_ENTRY_DEFINE
	* @param ids
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcvoucherentrydefine:delete")
	@LogMessage(content = "批量删除FC_VOUCHER_ENTRY_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcVoucherEntryDefine fcVoucherEntryDefine = fcVoucherEntryDefineService.get(id);
			fcVoucherEntryDefineService.deleteEntity(fcVoucherEntryDefine);
		}
		return success();
	}
}
