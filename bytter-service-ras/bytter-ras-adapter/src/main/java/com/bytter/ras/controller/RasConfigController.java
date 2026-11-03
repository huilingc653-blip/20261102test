package com.bytter.ras.controller;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bytter.core.base.BaseController;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.IConfigService;

/**   
 * @Title: Controller  
 * @Description: 营收稽核系统参数配置
 * @author onlineGenerator
 * @date 2019-04-29 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/ras/config")
public class RasConfigController extends BaseController {
	
	@Autowired(required=false)
	private IConfigService configService;
	
	/**
	 *  营收稽核系统参数配置
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_PARAM_OP)
	@GetMapping("/rasConfig")
	public String fcsConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.RAS));
	    return "ras/rasConfig";
	}
	
}
