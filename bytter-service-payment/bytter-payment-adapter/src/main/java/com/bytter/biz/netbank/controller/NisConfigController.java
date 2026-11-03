package com.bytter.biz.netbank.controller;
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
 * @date 2017/5/25.
 */
@Controller
@RequestMapping("/netbank/config")
public class NisConfigController extends BaseController {
	
	@DubboReference(check = false)
	private IConfigService configService;
	
	/**
	 * 支付结算参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_NISCONFIG_APPLY)
	@GetMapping("/nisConfig")
	public String nisConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.NIS));
	    return "netbank/nisConfig";
	}
	
	/**
	 * 费用报销参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_EXPCONFIG_APPLY)
	@GetMapping("/expConfig")
	public String expConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.EXP));
	    return "netbank/expConfig";
	}
	
	/**
	 * 资金调拨参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_ALLOTCONFIG_APPLY)
	@GetMapping("/allotConfig")
	public String allotConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.ALLOT));
	    return "netbank/allotConfig";
	}
	
	/**
	 * 代发工资参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_WAGECONFIG_APPLY)
	@GetMapping("/wageConfig")
	public String wageConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.WAGE));
	    return "netbank/wageConfig";
	}
	/**
	 * 资金池参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_POOLCONFIG_APPLY)
	@GetMapping("/poolConfig")
	public String poolConfig(Model model) {
		model.addAttribute("config",configService.listByType(ConfigConstant.POOL));
		return "netbank/poolConfig";
	}
	/**
	 * 电子回单参数配置
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_ELECONFIG_APPLY)
	@GetMapping("/eleConfig")
	public String eleConfig(Model model) {
		model.addAttribute("config",configService.listByType(ConfigConstant.ELE));
		return "netbank/eleConfig";
	}
	/**
	 * 收款认领管理参数
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_RECEIVABLECONFIG_APPLY)
	@GetMapping("/receivableConfig")
	public String receivableConfig(Model model) {
		model.addAttribute("config",configService.listByType(ConfigConstant.RECEIVABLE));
		return "netbank/receivableConfig";
	}
}
