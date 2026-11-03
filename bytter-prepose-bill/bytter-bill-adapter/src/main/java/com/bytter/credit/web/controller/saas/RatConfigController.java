package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 授信管理系统参数Controller
 *
 * @author liyh
 *
 */

@Api(tags = "授信管理系统参数Controller", value = "/v1-credit/cms/rat/config")
@RestController("v1RatConfigController")
@RequestMapping("/v1-credit/cms/rat/config")
public class RatConfigController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 承兑汇票系统参数配置
     *
     * @return
     */
    @GetMapping("/ratConfig")
    public AjaxJson<HashMap<String, Object>> ratConfig() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("config", configService.listByType(ConfigConstant.RAT));
        return AjaxJson.success(model);
    }
}
