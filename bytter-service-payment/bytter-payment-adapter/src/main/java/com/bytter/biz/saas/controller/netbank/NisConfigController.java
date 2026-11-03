package com.bytter.biz.saas.controller.netbank;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author huangx
 * @date 2017/5/25.
 */
@Api(value = "/v1-payment/netbank/config",tags = "资金调拨参数配置")
@RestController("v1NisConfigController")
@RequestMapping("/v1-payment/netbank/config")
public class NisConfigController extends BaseController {

	@DubboReference(check = false)
	private IConfigService configService;

	@GetMapping("/allNisConfig")
	public AjaxJson<Map<String,Object>> allNisConfig() {
		Map<String,Object> ajaxJsonMap = new HashMap<>();
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.NIS));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.EXP));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.ALLOT));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.WAGE));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.POOL));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.ELE));
		ajaxJsonMap.putAll(configService.listByType(ConfigConstant.RECEIVABLE));
		return AjaxJson.success(ajaxJsonMap);
	}

	/**
	 * 支付结算参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_NISCONFIG_APPLY)
	@GetMapping("/nisConfig")
	public AjaxJson<Object> nisConfig() {
	    return AjaxJson.success(configService.listByType(ConfigConstant.NIS));
	}

	/**
	 * 费用报销参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_EXPCONFIG_APPLY)
	@GetMapping("/expConfig")
	public AjaxJson<Object> expConfig() {
	    return AjaxJson.success(configService.listByType(ConfigConstant.EXP));
	}

	/**
	 * 资金调拨参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_ALLOTCONFIG_APPLY)
	@GetMapping("/allotConfig")
	public AjaxJson<Object> allotConfig() {
	    return AjaxJson.success(configService.listByType(ConfigConstant.ALLOT));
	}

	/**
	 * 代发工资参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_WAGECONFIG_APPLY)
	@GetMapping("/wageConfig")
	public AjaxJson<Object> wageConfig() {
	    return AjaxJson.success(configService.listByType(ConfigConstant.WAGE));
	}
	
	/**
	 * 资金池参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_POOLCONFIG_APPLY)
	@GetMapping("/poolConfig")
	public AjaxJson<Object> poolConfig() {
		return AjaxJson.success(configService.listByType(ConfigConstant.POOL));
	}
	
	/**
	 * 电子回单参数配置
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_ELECONFIG_APPLY)
	@GetMapping("/eleConfig")
	public AjaxJson<Object> eleConfig() {
		return AjaxJson.success(configService.listByType(ConfigConstant.ELE));
	}
	
	/**
	 * 收款认领管理参数
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_CONFIG_RECEIVABLECONFIG_APPLY)
	@GetMapping("/receivableConfig")
	public AjaxJson<Object> receivableConfig() {
		return AjaxJson.success(configService.listByType(ConfigConstant.RECEIVABLE));
	}

}
