package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.finacial.api.service.IFcAccuBalanceService;
import com.bytter.biz.finacial.api.vo.FcAccuBalanceVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: FC_ACCU_BALANCE
 * @author onlineGenerator
 * @date 2017-06-07 10:03:48
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccubalance")
public class FcAccuBalanceController extends BaseController {
	@DubboReference(check = false)
	private IFcAccuBalanceService fcAccuBalanceService;
	/**
	* FC_ACCU_BALANCE列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询FC_ACCU_BALANCE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcAccuBalance";
	}
    /**
    * FC_ACCU_BALANCE table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccuBalanceService.getDataTables(dataTablesInput);
    }
	/**
	* FC_ACCU_BALANCE新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(FcAccuBalanceVO fcAccuBalance, Model model) {
		if (StringUtils.isNotEmpty(fcAccuBalance.getId())) {
			fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(fcAccuBalance.getId());
			model.addAttribute("fcAccuBalancePage", fcAccuBalance);
		}
		return "inneraccount/addFcAccuBalance";
	}
	/**
	* FC_ACCU_BALANCE编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(FcAccuBalanceVO fcAccuBalance, Model model) {
		if (StringUtils.isNotEmpty(fcAccuBalance.getId())) {
			fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(fcAccuBalance.getId());
			model.addAttribute("fcAccuBalancePage", fcAccuBalance);
		}
		return "inneraccount/updateFcAccuBalance";
	}
	/**
	* FC_ACCU_BALANCE详情页
	*
	* @return
	*/
	@LogMessage(content = "查询FC_ACCU_BALANCE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcAccuBalanceVO fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(id);
		model.addAttribute("fcAccuBalancePage", fcAccuBalance);
		return "inneraccount/viewFcAccuBalance";
	}
	/**
	 * 添加FC_ACCU_BALANCE
	 * @param fcAccuBalance
	 * @return
	 */
	@LogMessage(content = "添加FC_ACCU_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcAccuBalanceVO fcAccuBalance){
		fcAccuBalanceService.save(fcAccuBalance);
		return success();
	}
	/**
	* 修改FC_ACCU_BALANCE
	* @param fcAccuBalance
	* @return
	*/
	@LogMessage(content = "修改FC_ACCU_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcAccuBalanceVO fcAccuBalance){
		FcAccuBalanceVO t = fcAccuBalanceService.getFcAccuBalanceById(fcAccuBalance.getId());
		BeanUtils.copyNotNullProperties(fcAccuBalance, t);
		fcAccuBalanceService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_ACCU_BALANCE
	* @param id
	* @return
	*/
	@LogMessage(content = "删除FC_ACCU_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcAccuBalanceVO fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(id);
		fcAccuBalanceService.deleteEntity(fcAccuBalance);
		return success();
	}
	/**
	* 批量删除FC_ACCU_BALANCE
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除FC_ACCU_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FcAccuBalanceVO fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(id);
			fcAccuBalanceService.deleteEntity(fcAccuBalance);
		}
		return success();
	}
}
