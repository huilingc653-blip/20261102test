package com.bytter.accountReceivable.businessConfig.controller;

import com.bytter.accountReceivable.businessConfig.service.impl.BaseConfigService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 基础设置
 * @author zhenghy
 * @date 2025-03-20
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/accountReceivable/businessConfig/baseConfig")
public class BaseConfigController extends BaseController {

	@DubboReference(check = false)
	private IConfigService configService;

	@Autowired
	private BaseConfigService baseConfigService;

	/**
	 * 基础设置 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询基础设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/index")
	public String list(Model model) {
		Map<String, Object> stringObjectMap = configService.listByType(ConfigConstant.BASIC);
		model.addAttribute("config",stringObjectMap);

		Map<String, String> basic = (Map<String, String>) stringObjectMap.get("basic");
		String defaultBudgetSel = basic.get("defaultBudgetSel");
		String defaultBudgetSelStr = baseConfigService.getDefaultBudgetSelStr(defaultBudgetSel, getCurrentUser().getTenantId());
		model.addAttribute("defaultBudgetSelStr",defaultBudgetSelStr);

		String remindStr = ((Map<String, String>) stringObjectMap.get("basic")).get("remind");
		if (StringUtils.isNotBlank(remindStr)) {
			List<String> remind = Arrays.asList(remindStr.split(","));
			model.addAttribute("remind",remind);
		}

		return "businessConfig/baseConfig/index";
	}

	/**
	 * 恢复初始化basic参数
	 */
	@LogMessage(content = "恢复初始化basic参数", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/goReset")
	@ResponseBody
	public AjaxJson goReset(){
		configService.goAccountReceivableReset();
		return success();
	}

}
