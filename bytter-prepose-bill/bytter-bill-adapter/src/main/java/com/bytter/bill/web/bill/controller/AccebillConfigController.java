package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**   
 * @Title: Controller  
 * @Description: 承兑汇票系统参数配置
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/config")
public class AccebillConfigController extends BaseController {
	
	@Autowired
	private AbConfigService abConfigService;
	
	/**
	 *  承兑汇票系统参数配置
	 * @param model
	 * @return
	 */
	@GetMapping("/accebillConfig")
	public String fcsConfig(Model model) {
	    model.addAttribute("config",abConfigService.listByType(ConfigConstant.ACCEBILL));
	    return "bill/accebill/accebillConfig";
	}
	
}
