package com.bytter.supply.controller;


import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.service.ScfPayConfigService;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "/v1-supply/supplyChain/scfPayConfig", tags = "应付债权凭证模版相关字段配置")
@RestController("v1ScfPayConfigController")
@RequestMapping("/v1-supply/supplyChain/scfPayConfig")
public class ScfPayConfigController extends BaseController {

    @Autowired
    private ScfPayConfigService scfPayConfigService;

    /**
     *
     * 查询模板配置
     *
     * @param templateId
     * @return
     */
    @ApiOperation(value = "查询模板配置", notes = "查询模板配置")
    @GetMapping("/getPayTemplateFieldConfig/{templateId}")
    @ResponseBody
    public AjaxJson<ScfPayConfigVO> getPayTemplateFieldConfig(@PathVariable("templateId") String templateId){
        List<ScfPayConfigVO> templateConfigs = scfPayConfigService.getPayTemplateFieldConfig(templateId);
        AjaxJson ajaxJson = success();
        ajaxJson.setObj(templateConfigs);
        return ajaxJson;
    }

    @ApiOperation(value = "根据平台ID查询模板配置", notes = "查询模板配置")
    @GetMapping("/getPayTemplateFieldConfigByPlatformId/{platformId}")
    @ResponseBody
    public AjaxJson<ScfPayConfigVO> getPayTemplateFieldConfigByPlatformId(@PathVariable("platformId") String platformId){
        List<ScfPayConfigVO> templateConfigs = scfPayConfigService.getPayTemplateFieldConfigByPlatformId(platformId);
        AjaxJson ajaxJson = success();
        ajaxJson.setObj(templateConfigs);
        return ajaxJson;
    }

    @ApiOperation(value = "查询默认模板配置", notes = "查询默认模板配置")
    @GetMapping("/getDefaultPayTemplateFieldConfig")
    @ResponseBody
    public AjaxJson<ScfPayConfigVO> getDefaultPayTemplateFieldConfig(){
        List<ScfPayConfigVO> templateConfigs = scfPayConfigService.getDefaultPayTemplateFieldConfig();
        AjaxJson ajaxJson = success();
        ajaxJson.setObj(templateConfigs);
        return ajaxJson;
    }


}
