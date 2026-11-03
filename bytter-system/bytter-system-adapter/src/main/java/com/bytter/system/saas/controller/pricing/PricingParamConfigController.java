package com.bytter.system.saas.controller.pricing;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luxy
 * @Title: PricingParamConfigController
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/22 11:29
 */
@Api(value = "/v1-system/pricing/basedata/paramconfig", tags = "定价管理参数设置")
@RestController("v1PricingParamConfigController")
@RequestMapping("/v1-system/pricing/basedata/paramconfig")
public class PricingParamConfigController extends BaseController {
    @Autowired
    private IConfigService configServiceI;
    ;

    @RequestMapping("pricingconfig")
    public AjaxJson<Map<String, Object>> pricingConfig() {
        Map<String, Object> model = new HashMap<>();
        model.put("config", configServiceI.listByType(ConfigConstant.PRICING));
        model.put("forwardUrl", "pricing/basedata/PricingConfig");
        return AjaxJson.success(model);
    }
}
