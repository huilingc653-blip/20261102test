package com.bytter.biz.budget.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.bytter.biz.budget.entity.FbsBudgetPeriod;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.vo.FbsBudgetPeriodTransmitVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
/**   
 * @Title: Controller  
 * @Description: FBS_BUDGET_PERIOD
 * @author onlineGenerator
 * @date 2017-07-07 16:09:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/budget/fbsbudgetperiod")
public class FbsBudgetPeriodController extends BaseController {
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	/**
	* FBS_BUDGET_PERIOD列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@LogMessage(content = "查询FBS_BUDGET_PERIOD列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		int defaultYear = DateUtils.getYear();
		model.addAttribute("defaultYear", defaultYear);
        return "budget/listFbsBudgetPeriod";
	}
    /**
    * FBS_BUDGET_PERIOD table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fbsBudgetPeriodService.getDataTables(dataTablesInput);
    }
	/**
	* FBS_BUDGET_PERIOD新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@GetMapping("/add")
	public String add(FbsBudgetPeriod fbsBudgetPeriod, Model model) {
		if (StringUtils.isNotEmpty(fbsBudgetPeriod.getId())) {
			fbsBudgetPeriod = fbsBudgetPeriodService.get(fbsBudgetPeriod.getId());
			model.addAttribute("fbsBudgetPeriodPage", fbsBudgetPeriod);
		}
		return "budget/addFbsBudgetPeriod";
	}
	/**
	* FBS_BUDGET_PERIOD编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@GetMapping("/update")
	public String update(FbsBudgetPeriod fbsBudgetPeriod, Model model) {
		if (StringUtils.isNotEmpty(fbsBudgetPeriod.getId())) {
			fbsBudgetPeriod = fbsBudgetPeriodService.get(fbsBudgetPeriod.getId());
			model.addAttribute("fbsBudgetPeriodPage", fbsBudgetPeriod);
		}
		return "budget/updateFbsBudgetPeriod";
	}
	/**
	* FBS_BUDGET_PERIOD详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@LogMessage(content = "查询FBS_BUDGET_PERIOD详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodService.get(id);
		model.addAttribute("fbsBudgetPeriodPage", fbsBudgetPeriod);
		return "budget/viewFbsBudgetPeriod";
	}
	/**
	 * 添加FBS_BUDGET_PERIOD
	 * @param fbsBudgetPeriodBean
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@LogMessage(content = "添加FBS_BUDGET_PERIOD",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FbsBudgetPeriodTransmitVO fbsBudgetPeriodBean){
		fbsBudgetPeriodService.toSave(fbsBudgetPeriodBean, this.getCurrentUser());
		return success();
	}
	/**
	* 修改FBS_BUDGET_PERIOD
	* @param fbsBudgetPeriod
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@LogMessage(content = "修改FBS_BUDGET_PERIOD",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FbsBudgetPeriod fbsBudgetPeriod){
		FbsBudgetPeriod t = fbsBudgetPeriodService.get(fbsBudgetPeriod.getId());
		BeanUtils.copyNotNullProperties(fbsBudgetPeriod, t);
		fbsBudgetPeriodService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FBS_BUDGET_PERIOD
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSBUDGETPERIOD_QUERY)
	@LogMessage(content = "删除FBS_BUDGET_PERIOD",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodService.get(id);
		fbsBudgetPeriodService.deleteEntity(fbsBudgetPeriod);
		return success();
	}
	/**
	* 批量删除FBS_BUDGET_PERIOD
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除FBS_BUDGET_PERIOD",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodService.get(id);
			fbsBudgetPeriodService.deleteEntity(fbsBudgetPeriod);
		}
		return success();
	}
	
	/**
	 * 得到可选的年份
	 * @return
	 */
	@GetMapping("/getChooseYear")
	@ResponseBody
	public List<Map<String,Object>> getChooseYear(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		int defaultYear = DateUtils.getYear();
		int startYear = defaultYear - 2;
		for (int i = 0; i < 13; i++,startYear++) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("year", startYear);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 得到表格的值
	 * @return
	 */
	@PostMapping("/getBtTable")
	@ResponseBody
	public List<FbsBudgetPeriodVO> getBtTable(String year){
		return fbsBudgetPeriodService.getBtTable(year);
	}
	
	/**
	 * 自动计算当年的周期自然周
	 * @param year
	 * @return
	 */
	@PostMapping("/autoAenerate")
	@ResponseBody
	public List<FbsBudgetPeriodVO> autoAenerate(String year){
		return fbsBudgetPeriodService.autoAenerate(year);
	}
	/**
	 * 自动计算当年的周期标准周
	 * @param year
	 * @return
	 */
	@PostMapping("/autoStandard")
	@ResponseBody
	public List<FbsBudgetPeriodVO> autoStandard(String year){
		return fbsBudgetPeriodService.autoStandard(year);
	}
	
	/**
	 * 判断是否可以自动计算
	 * @param year
	 * @return
	 */
	@PostMapping("/isAutoAenerate")
	@ResponseBody
	public AjaxJson isAutoAenerate(String year){
		return success();
	}
	/**
	 * 得到时间段的工作日
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@PostMapping("/getWorkDays")
	@ResponseBody
	public Integer getWorkDays(String beginDate,String endDate){
		return fbsBudgetPeriodService.getWorkDays(beginDate,endDate);
	}
}
