package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Api(tags = "担保管理系统参数", value = "/v1-guarantee/cms/guarantee/config")
@RestController("v1GuaGuaranteeConfigController")
@RequestMapping("/v1-guarantee/cms/guarantee/config")
public class GuaGuaranteeConfigController extends BaseController {

    @DubboReference(check = false)
    private IConfigService configService;

    // 保函管理参数设置
    @ApiOperation("保函管理参数设置")
    @GetMapping("/guaGuaranteeConfig")
    public AjaxJson<Map<String,Object>> guaGuaranteeConfig() {
        return success(configService.listByType(ConfigConstant.GUA));
    }
    // 担保管理系统参数
    @ApiOperation("担保管理系统参数")
    @GetMapping("/gua")
    public AjaxJson<Map<String,Object>> guaConfig() {
        return success(configService.listByType(ConfigConstant.GUA));
    }
    /**
     * 抵质押参数
     * @return 页面
     */
    @ApiOperation("抵质押参数")
    @GetMapping("/pledge")
    public AjaxJson<Map<String,Object>> pledgeConfig() {
        return success(configService.listByType(ConfigConstant.PLEDGE));
    }

}
