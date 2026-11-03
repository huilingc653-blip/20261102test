package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.credential.api.constant.LcConfigConstant;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-01-11 18:20
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/config/creditConfig")
public class CreditConfigController extends BaseController {
    @DubboReference(check = false)
	private IConfigService configService;
    /**
     * 新增跳转
     * @param model
     * @return
     */
    @LogMessage(content = "信用证开证参数配置条状", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCreditConfig")
    public String addCredit(Model model){
        model.addAttribute("config",configService.listByType(LcConfigConstant.LC_CONFIG_TYPE));
        return "international/config/creditConfig";
    }
    /**
     * 新增跳转
     * @param model
     * @return
     */
    @LogMessage(content = "信用证收证参数配置条状", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewReceiveConfig")
    public String addReceive(Model model){
        model.addAttribute("config",configService.listByType(LcConfigConstant.LC_CONFIG_RECEIVE));
        return "international/config/receiveConfig";
    }
}
