package com.bytter.system.mobile.controller;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by chenlw on 2018/12/28.
 */
@Controller
@RequestMapping("/app/test")
public class TestAppController extends BaseController {
    @Value("${push.customerPrefix}")
    private String customerPrefix;
    /**
     * 进入APP测试页面（消息推送测试，人脸识别测试）
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("customerPrefix", customerPrefix);
        return "sys/mobile/test/index";
    }
    @RequestMapping("/appH5JsBridgeExample")
    public String appH5JsBridgeExample(Model model) {
        return "sys/mobile/test/appH5JsBridgeExample";
    }
}
