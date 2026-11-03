package com.bytter.credit.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 授信管理系统参数Controller
 * 
 * @author liyh
 *
 */
@Controller
@RequestMapping("cms/rat/config")
public class RatConfigController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 承兑汇票系统参数配置
     * 
     * @param model
     * @return
     */
    @GetMapping("/ratConfig")
    public String ratConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.RAT));
        return "cms/rat/ratConfig";
    }
}
