package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolQueryExcService;
import com.bytter.bill.domain.billPool.vo.AbPoolExcVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 票据池指令
 */
@Api(value = "/v1-preposebill/billPool/queryExc", tags = "票据池指令")
@RestController("v1AbPoolQueryExcController")
@RequestMapping("/v1-preposebill/billPool/queryExc")
public class AbPoolQueryExcController extends BaseController {

    @Autowired
    private AbPoolQueryExcService abPoolQueryExcService;

    /**
     * 手工同步指令
     */
    @ApiOperation("手工同步指令")
    @PostMapping("/manualExc")
    public AjaxJson manualExc(@RequestBody AbPoolExcVO bean) {
        AjaxJson ajaxJson = success();
        try {
            abPoolQueryExcService.manualSynchronize(bean, getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
}
