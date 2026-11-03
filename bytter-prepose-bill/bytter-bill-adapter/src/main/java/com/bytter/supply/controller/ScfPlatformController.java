package com.bytter.supply.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.platform.service.ScfPlatformService;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "/v1-supply/supplyChain/platform",tags = "供应链金融平台/产品管理")
@RestController("v1ScfPlatformController")
@RequestMapping("/v1-supply/supplyChain/platform")
public class ScfPlatformController extends BaseController {

    @Autowired
    private ScfPlatformService scfPlatformService;


    @DubboReference(check = false)
    private IConfigService configService;

    @ApiOperation(value = "供应链金融平台参数配置")
    @ResponseBody
    @GetMapping("/getScfConfig")
    public AjaxJson<Map<String, Object>> fcsConfig() {
        return AjaxJson.success(configService.listByType("scf"));
    }

    @ApiOperation(value = "查询所有平台列表")
    @ResponseBody
    @GetMapping("/getPlatformJson")
    public AjaxJson<List<ScfPlatformVo>> getPlatformJson(){
        return success(scfPlatformService.getPlatformJson());
    }

    @ApiOperation(value = "查询平台下所有产品列表")
    @ResponseBody
    @GetMapping("/getProductJson/{platformId}")
    public AjaxJson<List<ScfProductVo>> getProductJson(@PathVariable String platformId){
        return success(scfPlatformService.getProductJson(platformId));
    }

    @ApiOperation(value = "查询平台下所有产品列表")
    @ResponseBody
    @GetMapping("/getProductJson")
    public AjaxJson<List<ScfProductVo>> getProductJsonById(String platformId){
        if(StringUtils.isEmpty(platformId)){
            return success(scfPlatformService.getAllProductJson());
        }
        return success(scfPlatformService.getProductJson(platformId));
    }

    @ApiOperation(value = "查询所有产品列表")
    @ResponseBody
    @GetMapping("/getProductJson/allProduct")
    public AjaxJson<List<ScfProductVo>> getAllProductJson(){
        return success(scfPlatformService.getAllProductJson());
    }
}
