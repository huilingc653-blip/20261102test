package com.bytter.biz.finacial.accbook.controller;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.core.constant.CommonConstant;
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
import com.bytter.biz.finacial.accbook.entity.FcChargeAccrue;
import com.bytter.biz.finacial.accbook.service.FcChargeAccrueService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
/**   
 * @Title: 手续费计提
 * @Description: FC_CHARGE_ACCRUE
 * @author onlineGenerator
 * @date 2017-07-04 15:14:11
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/accbook/accrue/fcchargeaccrue")
public class FcChargeAccrueController extends BaseController {
	@Autowired
	private FcChargeAccrueService fcChargeAccrueService;
	/**
	* FC_CHARGE_ACCRUE列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:index")
	@LogMessage(content = "查询FC_CHARGE_ACCRUE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/accbook/accrue/listFcChargeAccrue";
	}
    /**
    * FC_CHARGE_ACCRUE table列表
    *
    * @return
    */
    ////@RequiresPermissions("accrue:fcchargeaccrue:index")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcChargeAccrueService.queryChargeAccrue(dataTablesInput, ResourceConstant.FCL_ACCBOOK_COMMON_QUERY,getUserId());
    }
	/**
	* FC_CHARGE_ACCRUE新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:save")
	@GetMapping("/add")
	public String add(FcChargeAccrue fcChargeAccrue, Model model) {
		if (StringUtils.isNotEmpty(fcChargeAccrue.getId())) {
			fcChargeAccrue = fcChargeAccrueService.get(fcChargeAccrue.getId());
			model.addAttribute("fcChargeAccruePage", fcChargeAccrue);
		}
		return "finacial/accbook/accrue/addFcChargeAccrue";
	}
	/**
	* FC_CHARGE_ACCRUE编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:update")
	@GetMapping("/update")
	public String update(FcChargeAccrue fcChargeAccrue, Model model) {
		if (StringUtils.isNotEmpty(fcChargeAccrue.getId())) {
			fcChargeAccrue = fcChargeAccrueService.get(fcChargeAccrue.getId());
			model.addAttribute("fcChargeAccruePage", fcChargeAccrue);
		}
		return "finacial/accbook/accrue/updateFcChargeAccrue";
	}
	/**
	* FC_CHARGE_ACCRUE详情页
	*
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:view")
	@LogMessage(content = "查询FC_CHARGE_ACCRUE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcChargeAccrue fcChargeAccrue = fcChargeAccrueService.get(id);
		model.addAttribute("fcChargeAccruePage", fcChargeAccrue);
		return "finacial/accbook/accrue/viewFcChargeAccrue";
	}
	/**
	 * 添加FC_CHARGE_ACCRUE
	 * @param fcChargeAccrue
	 * @return
	 */
	////@RequiresPermissions("accrue:fcchargeaccrue:save")
	@LogMessage(content = "添加FC_CHARGE_ACCRUE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/doChargeAccrue")
	@ResponseBody
	public AjaxJson doChargeAccrue(String id){
		if (StringUtils.isEmpty(id)) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_CHOOSEONE);
		}
		fcChargeAccrueService.doChargeAccrue(id, getUserId());
		return success();
	}
	
	/**
	* 修改FC_CHARGE_ACCRUE
	* @param fcChargeAccrue
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:update")
	@LogMessage(content = "修改FC_CHARGE_ACCRUE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcChargeAccrue fcChargeAccrue){
		FcChargeAccrue t = fcChargeAccrueService.get(fcChargeAccrue.getId());
		BeanUtils.copyNotNullProperties(fcChargeAccrue, t);
		fcChargeAccrueService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_CHARGE_ACCRUE
	* @param id
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:delete")
	@LogMessage(content = "删除FC_CHARGE_ACCRUE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcChargeAccrue fcChargeAccrue = fcChargeAccrueService.get(id);
		fcChargeAccrueService.deleteEntity(fcChargeAccrue);
		return success();
	}
	/**
	* 批量删除FC_CHARGE_ACCRUE
	* @param ids
	* @return
	*/
	////@RequiresPermissions("accrue:fcchargeaccrue:delete")
	@LogMessage(content = "批量删除FC_CHARGE_ACCRUE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FcChargeAccrue fcChargeAccrue = fcChargeAccrueService.get(id);
			fcChargeAccrueService.deleteEntity(fcChargeAccrue);
		}
		return success();
	}
}
