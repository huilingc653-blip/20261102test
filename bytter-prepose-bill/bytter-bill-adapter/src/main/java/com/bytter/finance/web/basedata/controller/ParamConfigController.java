package com.bytter.finance.web.basedata.controller;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/***
 * @Title 信贷管理系统参数
 * @author zhengxn
 *
 */
@Controller
@RequestMapping("/cms/basedata/paramconfig")
public class ParamConfigController {
    @DubboReference(check = false)
    private IConfigService configService;
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.CMS));
        return "cms/param/paramConfig";
    }
    @GetMapping("/gua")
    public String guaConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.GUA));
        return "cms/param/guaranteeConfig";
    }
    @GetMapping("/bond")
    public String bondConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.BOND));
        return "cms/param/bondConfig";
    }
    @GetMapping("/pledge")
    public String pledgeConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.PLEDGE));
        return "cms/param/pledgeConfig";
    }
}
