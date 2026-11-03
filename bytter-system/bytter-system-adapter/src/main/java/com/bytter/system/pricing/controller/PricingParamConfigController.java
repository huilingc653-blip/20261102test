package com.bytter.system.pricing.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author luxy
 * @Title: PricingParamConfigController
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/22 11:29
 */
@Controller
@RequestMapping("/pricing/basedata/paramconfig")
public class PricingParamConfigController extends BaseController {
    @Autowired
	private IConfigService configServiceI;;
    @RequestMapping("pricingconfig")
    public String pricingConfig(Model model){
        model.addAttribute("config",configServiceI.listByType(ConfigConstant.PRICING));
        return "pricing/basedata/PricingConfig";
    }
}
