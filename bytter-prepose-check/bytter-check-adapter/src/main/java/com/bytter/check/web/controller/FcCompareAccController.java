package com.bytter.check.web.controller;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.service.FcCompareAccService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: FC_COMPARE_ACC
 * @author onlineGenerator
 * @date 2017-06-02 17:21:47
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fccompareacc")
public class FcCompareAccController extends BaseController {
	@Autowired
	private FcCompareAccService fcCompareAccService;
	/**
	* FC_COMPARE_ACC列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:index")
	@LogMessage(content = "查询FC_COMPARE_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "check/query/listFcCompareAcc";
	}
    /**
    * FC_COMPARE_ACC table列表
    *
    * @return
    */
    ////@RequiresPermissions("query:fccompareacc:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcCompareAccService.getDataTables(dataTablesInput);
    }
	/**
	* FC_COMPARE_ACC新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:save")
	@GetMapping("/add")
	public String add(FcCompareAcc fcCompareAcc, Model model) {
		if (StringUtils.isNotEmpty(fcCompareAcc.getId())) {
			fcCompareAcc = fcCompareAccService.get(fcCompareAcc.getId());
			model.addAttribute("fcCompareAccPage", fcCompareAcc);
		}
		return "check/query/addFcCompareAcc";
	}
	/**
	* FC_COMPARE_ACC编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:update")
	@GetMapping("/update")
	public String update(FcCompareAcc fcCompareAcc, Model model) {
		if (StringUtils.isNotEmpty(fcCompareAcc.getId())) {
			fcCompareAcc = fcCompareAccService.get(fcCompareAcc.getId());
			model.addAttribute("fcCompareAccPage", fcCompareAcc);
		}
		return "check/query/updateFcCompareAcc";
	}
	/**
	* FC_COMPARE_ACC详情页
	*
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:view")
	@LogMessage(content = "查询FC_COMPARE_ACC详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcCompareAcc fcCompareAcc = fcCompareAccService.get(id);
		model.addAttribute("fcCompareAccPage", fcCompareAcc);
		return "check/query/viewFcCompareAcc";
	}
	/**
	 * 添加FC_COMPARE_ACC
	 * @param fcCompareAcc
	 * @return
	 */
	////@RequiresPermissions("query:fccompareacc:save")
	@LogMessage(content = "添加FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcCompareAcc fcCompareAcc){
		fcCompareAccService.save(fcCompareAcc);
		return success();
	}
	/**
	* 修改FC_COMPARE_ACC
	* @param fcCompareAcc
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:update")
	@LogMessage(content = "修改FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcCompareAcc fcCompareAcc){
		FcCompareAcc t = fcCompareAccService.get(fcCompareAcc.getId());
		BeanUtils.copyNotNullProperties(fcCompareAcc, t);
		fcCompareAccService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_COMPARE_ACC
	* @param id
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:delete")
	@LogMessage(content = "删除FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcCompareAccService.deleteCompareDtl(id);
		return success();
	}
	/**
	* 批量删除FC_COMPARE_ACC
	* @param ids
	* @return
	*/
	////@RequiresPermissions("query:fccompareacc:delete")
	@LogMessage(content = "批量删除FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids) {
	    String[] idStr = ids.split(",");
		for(String id : idStr){
			fcCompareAccService.deleteCompareDtl(id);
		}
		return success();
	}
}
