package com.bytter.finance.saas.controller.inl;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Api(tags = "基础数据配置")
@RestController("v1InlConfigController")
@RequestMapping("/v1-preposebill/inl/inlConfig")
public class InlConfigController {
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 内部从拆借配置页面
     *
     * @return
     */
    @GetMapping("/showConfig")
    public AjaxJson<Map<String, Object>> showConfig() {
        return AjaxJson.success(configService.listByType(ConfigConstant.INL));
    }
}
