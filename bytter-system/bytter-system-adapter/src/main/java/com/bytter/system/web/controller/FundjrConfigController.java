package com.bytter.system.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
 * @Title: Controller  
 * @Description: 企e金服系统参数配置
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/fundjr")
public class FundjrConfigController extends BaseController {
	
	@Autowired
	private ConfigService configService;
	
	/**
	 *  企e金服系统参数配置
	 * @param model
	 * @return
	 */
	@GetMapping("/fundjrConfig")
	public String fcsConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.FUNDJR));
	    return "sys/fundjr/fundjrConfig";
	}
	
}
