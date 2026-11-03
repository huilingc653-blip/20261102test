package com.bytter.biz.finacial.interest.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/interest/config")
public class IstConfigController extends BaseController {
	@DubboReference(check = false)
	private IConfigService configService;
	
	/**
	 * <b>功能介绍:</b>利息管理参数<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_INTEREST_APPLY)
	@GetMapping("/itsConfig")
	public String itsConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.IST));
	    return "finacial/interest/itsConfig";
	}
}
