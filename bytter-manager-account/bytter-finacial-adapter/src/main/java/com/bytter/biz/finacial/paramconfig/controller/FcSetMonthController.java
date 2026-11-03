package com.bytter.biz.finacial.paramconfig.controller;
import java.util.List;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.paramconfig.entity.FcSetMonth;
import com.bytter.biz.finacial.paramconfig.service.FcSetMonthService;
import com.bytter.biz.finacial.paramconfig.vo.FcSetMonthVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
/**   
 * @Title: Controller  
 * @Description: 月结区间
 * @author onlineGenerator
 * @date 2017-05-24 09:54:22
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcsetmonth")
public class FcSetMonthController extends BaseController {
	@Autowired
	private FcSetMonthService fcSetMonthService;
	/**
	* 月结区间列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCSETMONTH_APPLY)
	@LogMessage(content = "查询月结区间列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(FcSetMonthVO fsmv,Model model) {
		FcSetMonth fsm = new FcSetMonth();
		BeanUtils.copyNotNullProperties(fsmv, fsm);
	    FcSetMonth fcset = fcSetMonthService.setInitdata(fsm, getCurrentUser());
		List<FcSetMonthVO> listFcSetMonth = fcSetMonthService.listFcSetMonth(fcset, getCurrentUser());
		model.addAttribute("yearItems", fcSetMonthService.getYears());
		model.addAttribute("listFcSetMonth", listFcSetMonth);
		model.addAttribute("FcSetMonthVO", fcset);
        return "finacial/paramconfig/fcsetmonth/listFcSetMonth";
	}
	
	/**
	* 新增月结区间
	* @param fcSetMonth
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCSETMONTH_APPLY)
	@LogMessage(content = "新增月结区间",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcSetMonthVO vo){
		fcSetMonthService.saveSetMonth(vo);
		return  success();
	}
	
}
