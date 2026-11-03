package com.bytter.accountReceivable.guide.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: Controller
 * @Description: 基础设置
 * @author zhenghy
 * @date 2025-03-20
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/accountReceivable/guide")
public class GuideController extends BaseController {


	/**
	 * 应收款管理使用指引 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "应收款管理使用指引页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/index")
	public String list(Model model) {
		System.err.println("是否进入");
		return "guide/index";
	}

}
