package com.bytter.biz.foreign.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.constant.ResourceConstant;

import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangx
 * @date 2019/7/15.
 */
@Controller
@RequestMapping("/foreign/config")
public class FexConfigController extends BaseController {

    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 外汇管理参数配置
     * @param model
     * @return
     */
    //@RequiresPermissions(value = ResourceConstant.FOREIGN_FFEXCONFIG_QUERY)
    @GetMapping("/fexConfig")
    public String fexConfig(Model model) {
        model.addAttribute("config",configService.listByType(ConfigConstant.FEX));
        return "foreign/fexConfig";
    }

}
