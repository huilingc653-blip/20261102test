package com.bytter.biz.budget.saas.controller.budget;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangx
 * @Description 资金计划参数配置
 * @date 2019-3-26
 */
@Api(value = "/v1-plan/budget/config", tags = "资金计划参数配置")
@RestController("v1FbsConfigController")
@RequestMapping("/v1-plan/budget/config")
public class FbsConfigController extends BaseController {
	
	@DubboReference(check = false)
	private IConfigService configService;
	
	/**
	 * 账户管理参数配置
	 * @return
	 */
	@ApiOperation(value = "账户管理参数配置")
	@GetMapping("/fbsConfig")
	public AjaxJson<Map<String, Object>> fcsConfig() {
		Map<String, Object> map=new HashMap<>();
		map.put("config",configService.listByType(ConfigConstant.FBS));
	    return AjaxJson.success(map);
	}
	
}
