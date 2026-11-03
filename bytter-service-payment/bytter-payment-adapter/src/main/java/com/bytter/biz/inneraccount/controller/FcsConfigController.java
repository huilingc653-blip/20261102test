package com.bytter.biz.inneraccount.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author huangx
 * @date 2019/8/7.
 */
@Controller
@RequestMapping("/inneraccount/config")
public class FcsConfigController extends BaseController {
	
	@DubboReference(check = false)
	private IConfigService configService;
	/**
	 * 内部账户管理参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions("netbank:config:fcsConfig:apply")
	@GetMapping("/fcsConfig")
	public String fcsConfig(Model model) {
		model.addAttribute("config",configService.listByType(ConfigConstant.FCS));
		return "inneraccount/fcsConfig";
	}
}
