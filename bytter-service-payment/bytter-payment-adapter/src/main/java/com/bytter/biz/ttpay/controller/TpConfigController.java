package com.bytter.biz.ttpay.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/ttpay/config")
public class TpConfigController extends BaseController {
    @DubboReference(check = false)
	private IConfigService configService;
    /**
     * 参数配置列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_CONFIG_OPERATION)
    public String list(Model model) {
        model.addAttribute("config",configService.listByType(ConfigConstant.TP));
        return "ttpay/listTpConfig";
    }
}
