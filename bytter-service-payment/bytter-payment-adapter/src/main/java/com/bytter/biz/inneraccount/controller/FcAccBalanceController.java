package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.vo.FcAccBalance;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
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
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: FC_ACC_BALANCE
 * @author onlineGenerator
 * @date 2017-06-07 10:04:02
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccbalance")
public class FcAccBalanceController extends BaseController {
	@DubboReference(check = false)
	private IFcAccBalanceService fcAccBalanceService;
	/**
	* FC_ACC_BALANCE列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY)
	@LogMessage(content = "查询FC_ACC_BALANCE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcAccBalance";
	}
    /**
    * FC_ACC_BALANCE table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccBalanceService.getDataTables(dataTablesInput);
    }
	/**
	* FC_ACC_BALANCE新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(FcAccBalanceVO fcAccBalance, Model model) {
		if (StringUtils.isNotEmpty(fcAccBalance.getId())) {
			fcAccBalance = fcAccBalanceService.getFcAccBalanceById(fcAccBalance.getId());
			model.addAttribute("fcAccBalancePage", fcAccBalance);
		}
		return "inneraccount/addFcAccBalance";
	}
	/**
	* FC_ACC_BALANCE编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(FcAccBalanceVO fcAccBalance, Model model) {
		if (StringUtils.isNotEmpty(fcAccBalance.getId())) {
			fcAccBalance = fcAccBalanceService.getFcAccBalanceById(fcAccBalance.getId());
			model.addAttribute("fcAccBalancePage", fcAccBalance);
		}
		return "inneraccount/updateFcAccBalance";
	}
	/**
	* FC_ACC_BALANCE详情页
	*
	* @return
	*/
	@LogMessage(content = "查询FC_ACC_BALANCE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcAccBalanceVO fcAccBalance = fcAccBalanceService.getFcAccBalanceById(id);
		model.addAttribute("fcAccBalancePage", fcAccBalance);
		return "inneraccount/viewFcAccBalance";
	}
	/**
	 * 添加FC_ACC_BALANCE
	 * @param fcAccBalance
	 * @return
	 */
	@LogMessage(content = "添加FC_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcAccBalanceVO fcAccBalance){
		fcAccBalanceService.save(fcAccBalance);
		return success();
	}
	/**
	* 修改FC_ACC_BALANCE
	* @param fcAccBalance
	* @return
	*/
	@LogMessage(content = "修改FC_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcAccBalanceVO fcAccBalance){
		FcAccBalanceVO t = fcAccBalanceService.getFcAccBalanceById(fcAccBalance.getId());
		BeanUtils.copyNotNullProperties(fcAccBalance, t);
		fcAccBalanceService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_ACC_BALANCE
	* @param id
	* @return
	*/
	@LogMessage(content = "删除FC_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcAccBalanceVO fcAccBalance = fcAccBalanceService.getFcAccBalanceById(id);
		fcAccBalanceService.deleteEntity(fcAccBalance);
		return success();
	}
	/**
	* 批量删除FC_ACC_BALANCE
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除FC_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FcAccBalanceVO fcAccBalance = fcAccBalanceService.getFcAccBalanceById(id);
			fcAccBalanceService.deleteEntity(fcAccBalance);
		}
		return success();
	}
}
