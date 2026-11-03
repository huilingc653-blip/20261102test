package com.bytter.ras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bytter.ras.service.RasLogService;
import com.bytter.core.base.BaseController;

/**   
 * @Title: Controller  
 * @Description: 营收稽核日志信息
 * @author onlineGenerator
 * @date 2019-04-29 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/ras/rasLog")
public class RasLogController extends BaseController {
	
	@Autowired
	private RasLogService rasLogService;

	/**
	* 营收稽核日志信息查看
	* @return
	*/
	@GetMapping("/rasLogView")
	public String rasLogView(String ids, Model model) {
		rasLogService.initLogViewInfo(ids, model);
		return "ras/viewRasLog";
	}

}
