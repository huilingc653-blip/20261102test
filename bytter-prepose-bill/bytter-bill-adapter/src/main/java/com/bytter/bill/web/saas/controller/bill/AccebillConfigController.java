package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 承兑汇票系统参数配置
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/config", tags = "承兑汇票系统参数配置")
@RestController("v1AccebillConfigController")
@RequestMapping("/v1-preposebill/bill/accebill/config")
public class AccebillConfigController extends BaseController {

    @Autowired
    private AbConfigService abConfigService;

    /**
     * 查询承兑汇票系统参数配置
     */
    @GetMapping("/accebillConfig")
    @ApiOperation("查询承兑汇票系统参数配置")
    public AjaxJson<Map<String, Object>> accebillConfig() {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("config", abConfigService.listByType(ConfigConstant.ACCEBILL));
        return AjaxJson.success(dataMap);
    }

}