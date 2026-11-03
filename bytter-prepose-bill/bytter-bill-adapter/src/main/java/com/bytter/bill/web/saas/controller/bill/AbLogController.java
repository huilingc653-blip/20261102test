package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据日志信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abLog", tags = "票据日志信息")
@RestController("v1AbLogController")
@RequestMapping("/v1-preposebill/bill/accebill/abLog")
public class AbLogController extends BaseController {

    @Autowired
    private AbLogService abLogService;

    /**
     * 查询票据日志信息
     */
    @GetMapping("/view")
    @ApiOperation("查询票据日志信息")
    public AjaxJson<Map<String, Object>> view(@RequestParam String ids) {
        Map<String, Object> dataMap =new HashMap<>();
        abLogService.initLogViewInfo(ids,dataMap);
        return AjaxJson.success(dataMap);
    }

}