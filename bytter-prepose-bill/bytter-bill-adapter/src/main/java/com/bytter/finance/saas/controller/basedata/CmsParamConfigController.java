package com.bytter.finance.saas.controller.basedata;
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

/***
 * @Title 信贷管理系统参数
 * @author zhengxn
 *
 */
@Api(value = "/v1-cms/cms/basedata/paramconfig",tags = "参数配置")
@RestController("v1CmsParamConfigController")
@RequestMapping("/v1-cms/cms/basedata/paramconfig")
public class CmsParamConfigController extends BaseController {
    @DubboReference(check = false)
    private IConfigService configService;
    @ApiOperation("信贷管理系统参数")
    @GetMapping("/list")
    public AjaxJson<Map<String,Object>> list() {
        return success(configService.listByType(ConfigConstant.CMS));
    }

    @ApiOperation("担保管理系统参数")
    @GetMapping("/gua")
    public AjaxJson<Map<String,Object>> guaConfig() {
        return success(configService.listByType(ConfigConstant.GUA));
    }

    @ApiOperation("债券参数")
    @GetMapping("/bond")
    public AjaxJson<Map<String,Object>> bondConfig() {
        return success(configService.listByType(ConfigConstant.BOND));
    }

    @ApiOperation("抵质押参数")
    @GetMapping("/pledge")
    public AjaxJson<Map<String,Object>> pledgeConfig() {
        return success(configService.listByType(ConfigConstant.PLEDGE));
    }
}
