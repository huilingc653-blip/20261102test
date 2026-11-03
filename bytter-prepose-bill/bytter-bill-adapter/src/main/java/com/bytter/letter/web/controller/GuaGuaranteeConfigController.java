package com.bytter.letter.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author luxy
 * @version 1.0
 * @Description: 保函参数设置控制层
 * @date: 2019年1月7日 上午9:51:22
 */
@Controller
@RequestMapping("/letter/guarantee/guaranteeConfig")
public class GuaGuaranteeConfigController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    // 保函管理参数设置
    @GetMapping("guaGuaranteeConfig")
    public String guaGuaranteeConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.GUA));
        return "cms/guarantee/guaGuaranteeConfig";
    }
    // 担保管理系统参数
    @GetMapping("gua")
    public String guaConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.GUA));
        return "cms/guarantee/guaranteeConfig";
    }
    /**
     * 抵质押参数
     *
     * @param model 模型
     * @return 页面
     */
    @GetMapping("/pledge")
    public String pledgeConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.PLEDGE));
        return "cms/guarantee/pledgeConfig";
    }
}
