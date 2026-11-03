package com.bytter.system.saas.controller.web;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.service.RegionService;
import com.bytter.system.vo.RegionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 省市区域（SaaS）
 */
@Api(value = "/v1-system/sys/region", tags = "省市区域")
@RestController("v1RegionController")
@RequestMapping("/v1-system/sys/region")
public class RegionController extends BaseController {

    @Autowired
    private RegionService regionService;

    /**
     * 根据父级ID获取省市树，id 为空时返回顶级
     */
    @ApiOperation("根据父级ID获取省市树")
    @GetMapping("/json")
    public AjaxJson<List<RegionVO>> json(String id) {
        return AjaxJson.success(regionService.findByParentId(id));
    }
}
