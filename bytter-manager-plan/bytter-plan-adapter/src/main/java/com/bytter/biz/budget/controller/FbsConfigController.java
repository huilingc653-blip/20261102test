package com.bytter.biz.budget.controller;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author huangx
 * @Description 资金计划参数配置
 * @date 2019-3-26
 */
@Controller
@RequestMapping("/budget/config")
public class FbsConfigController extends BaseController {
	
	@DubboReference(check = false)
	private IConfigService configService;
	
	/**
	 * 账户管理参数配置
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_FBSCONFIG_QUERY)
	@GetMapping("/fbsConfig")
	public String fcsConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.FBS));
	    return "budget/fbsConfig";
	}
	
}
