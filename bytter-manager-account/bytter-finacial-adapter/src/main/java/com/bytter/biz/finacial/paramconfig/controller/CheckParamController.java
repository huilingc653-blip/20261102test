package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
	/**
	 * @Title Controller
	 * Description: 核算系统参数
	 * @author zhengxn
	 *
	 */
	@Controller
	@RequestMapping("/finacial/paramconfig/checkparam")
	public class CheckParamController extends BaseController {
		
		@DubboReference(check = false)
	private IConfigService configService;
		
	
		@GetMapping("/list")
		public String list(Model model) {
			model.addAttribute("config",configService.listByType(ConfigConstant.FCL));
	       
			return "finacial/paramconfig/checkparam/listCheckParam";
		}
	    
	
	 
}
