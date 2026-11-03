package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.finacial.api.service.IFcOutAccBalanceService;
import com.bytter.biz.finacial.api.vo.FcOutAccBalanceVO;
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
 * @Description: FC_OUT_ACC_BALANCE
 * @author onlineGenerator
 * @date 2017-06-07 10:24:29
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcoutaccbalance")
public class FcOutAccBalanceController extends BaseController {
	@DubboReference(check = false)
	private IFcOutAccBalanceService fcOutAccBalanceService;
	/**
	* FC_OUT_ACC_BALANCE列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询FC_OUT_ACC_BALANCE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcOutAccBalance";
	}
    /**
    * FC_OUT_ACC_BALANCE table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcOutAccBalanceService.getDataTables(dataTablesInput);
    }
	/**
	* FC_OUT_ACC_BALANCE新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(FcOutAccBalanceVO fcOutAccBalance, Model model) {
		if (StringUtils.isNotEmpty(fcOutAccBalance.getId())) {
			fcOutAccBalance = fcOutAccBalanceService.getFcOutAccBalanceById(fcOutAccBalance.getId());
			model.addAttribute("fcOutAccBalancePage", fcOutAccBalance);
		}
		return "inneraccount/addFcOutAccBalance";
	}
	/**
	* FC_OUT_ACC_BALANCE编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(FcOutAccBalanceVO fcOutAccBalance, Model model) {
		if (StringUtils.isNotEmpty(fcOutAccBalance.getId())) {
			fcOutAccBalance = fcOutAccBalanceService.getFcOutAccBalanceById(fcOutAccBalance.getId());
			model.addAttribute("fcOutAccBalancePage", fcOutAccBalance);
		}
		return "inneraccount/updateFcOutAccBalance";
	}
	/**
	* FC_OUT_ACC_BALANCE详情页
	*
	* @return
	*/
	@LogMessage(content = "查询FC_OUT_ACC_BALANCE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcOutAccBalanceVO fcOutAccBalance = fcOutAccBalanceService.getFcOutAccBalanceById(id);
		model.addAttribute("fcOutAccBalancePage", fcOutAccBalance);
		return "inneraccount/viewFcOutAccBalance";
	}
	/**
	 * 添加FC_OUT_ACC_BALANCE
	 * @param fcOutAccBalance
	 * @return
	 */
	@LogMessage(content = "添加FC_OUT_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcOutAccBalanceVO fcOutAccBalance){
		fcOutAccBalanceService.save(fcOutAccBalance);
		return success();
	}
	/**
	* 修改FC_OUT_ACC_BALANCE
	* @param fcOutAccBalance
	* @return
	*/
	@LogMessage(content = "修改FC_OUT_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcOutAccBalanceVO fcOutAccBalance){
		FcOutAccBalanceVO t = fcOutAccBalanceService.getFcOutAccBalanceById(fcOutAccBalance.getId());
		BeanUtils.copyNotNullProperties(fcOutAccBalance, t);
		fcOutAccBalanceService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_OUT_ACC_BALANCE
	* @param id
	* @return
	*/
	@LogMessage(content = "删除FC_OUT_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcOutAccBalanceVO fcOutAccBalance = fcOutAccBalanceService.getFcOutAccBalanceById(id);
		fcOutAccBalanceService.deleteEntity(fcOutAccBalance);
		return success();
	}
	/**
	* 批量删除FC_OUT_ACC_BALANCE
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除FC_OUT_ACC_BALANCE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FcOutAccBalanceVO fcOutAccBalance = fcOutAccBalanceService.getFcOutAccBalanceById(id);
			fcOutAccBalanceService.deleteEntity(fcOutAccBalance);
		}
		return success();
	}
}
