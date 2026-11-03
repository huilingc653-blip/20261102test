package com.bytter.biz.transfer.swap.controller;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 支付参数配置
 * Created by shenc on 2018/7/6.
 */
@Controller
@RequestMapping("/erp/swap/disable")
public class ErpDisableConfigController extends BaseController {
    @Autowired
    private IConfigService configService;
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_DISABLE_APPLY)
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.ERP));
        return "transfer/swap/config_disable";
    }
}
