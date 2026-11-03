package com.bytter.increment.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 理财管理参数
 * @date 2019-03-26 14:03:30
 */
@Controller
@RequestMapping("/chowmatistic/config")
public class ChoConfigController extends BaseController {
    @DubboReference(check = false)
	private IConfigService configService;
    /**
     * 账户管理参数配置
     *
     * @param model
     * @return
     */
    @GetMapping("/choBankConfig")
    public String fcsConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.CHOBANK));
        return "chowmatistic/choBankConfig";
    }
    /**
     * @param model 视图
     * @return 内部存款参数界面
     */
    @GetMapping("/choInnerConfig")
    public String nisConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.CHOINNER));
        return "chowmatistic/choInnerConfig";
    }
    @GetMapping("/choProductConfig")
    public String productConfig(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.CHOPRODUCT));
        return "chowmatistic/choProductConfig";
    }
}
