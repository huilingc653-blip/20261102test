package com.bytter.finance.web.inl.controller;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/inl/inlConfig")
public class InlConfigController {
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 内部从拆借配置页面
     *
     * @return
     */
    @GetMapping("/showConfig")
    public String showConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.INL));
        return "cms/inl/inlConfig";
    }
}
