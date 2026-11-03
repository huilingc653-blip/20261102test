package com.bytter.biz.finacial.paramconfig.controller;
import java.util.List;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.paramconfig.entity.BtSummary;
import com.bytter.biz.finacial.paramconfig.service.BtSummaryService;
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
 * @Description: 摘要词组摘要词组维护
 * @author Lixi
 * @date 2017-05-16 15:36:55
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/finacial/paramconfig/btsummary")
public class BtSummaryController extends BaseController {
	@Autowired
	private BtSummaryService btSummaryService;
	/**
	* 摘要词组列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@LogMessage(content = "查询摘要词组列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/btsummary/listBtSummary";
	}
    /**
    * 摘要词组 table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return btSummaryService.dataTable(dataTablesInput);
    }
    
	/**
	* 摘要词组新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@GetMapping("/add")
	public String add(BtSummary btSummary, Model model) {
		if (StringUtils.isNotEmpty(btSummary.getId())) {
			btSummary = btSummaryService.get(btSummary.getId());
			model.addAttribute("btSummaryPage", btSummary);
		}
		return "finacial/paramconfig/btsummary/addBtSummary";
	}
	/**
	* 摘要词组编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@GetMapping("/update")
	public String update(BtSummary btSummary, Model model) {
		if (StringUtils.isNotEmpty(btSummary.getId())) {
			btSummary = btSummaryService.get(btSummary.getId());
			model.addAttribute("btSummaryPage", btSummary);
		}
		return "finacial/paramconfig/btsummary/updateBtSummary";
	}
	/**
	* 摘要词组详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@LogMessage(content = "查询摘要词组详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtSummary btSummary = btSummaryService.get(id);
		model.addAttribute("btSummaryPage", btSummary);
		return "finacial/paramconfig/btsummary/viewBtSummary";
	}
	/**
	 * 添加摘要词组
	 * @param btSummary
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@LogMessage(content = "添加摘要词组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtSummary btSummary){
		btSummaryService.save(btSummary, getUserId());
		return success();
	}
	/**
	* 修改摘要词组
	* @param btSummary
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@LogMessage(content = "修改摘要词组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtSummary btSummary){
		BtSummary t = btSummaryService.get(btSummary.getId());
		BeanUtils.copyNotNullProperties(btSummary, t);
		btSummaryService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除摘要词组
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_BTSUMMARY_APPLY)
	@LogMessage(content = "删除摘要词组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		BtSummary btSummary = btSummaryService.get(id);
		btSummaryService.deleteEntity(btSummary);
		return success();
	}
	
	/**
	* 查询摘要词组
	* @param id
	* @return
	*/
	@LogMessage(content = "查询摘要词组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/query/{id}")
	@ResponseBody
	public AjaxJson query(@PathVariable("id") String id){
		btSummaryService.listById(id);
		return success();
	}
	
	/**
	* 批量删除摘要词组
	* @param ids
	* @return
	*/
	////@RequiresPermissions("btsummary:btsummary:delete")
	@LogMessage(content = "批量删除摘要词组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids")String ids){
		for(String id : ids.split(",")){
			BtSummary btSummary = btSummaryService.get(id);
			btSummaryService.deleteEntity(btSummary);
		}
		return success();
	}
	/**
	 * 摘要词组
	 *
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<BtSummary> getListByValidSign() {
		return btSummaryService.getListByValidSign();
	}
	@PostMapping("/jsonPost")
	@ResponseBody
	public List<BtSummary> getListByValidSignPost() {
		return btSummaryService.getListByValidSign();
	}
}
