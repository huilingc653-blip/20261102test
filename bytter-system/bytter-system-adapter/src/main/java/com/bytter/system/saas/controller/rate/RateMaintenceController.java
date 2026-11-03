package com.bytter.system.saas.controller.rate;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.service.RateMaintenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/v1-system/rate/maintence", tags = "贷款利率维护")
@RestController("v1RateMaintenceController")
@RequestMapping("/v1-system/rate/maintence")
public class RateMaintenceController extends BaseController {
    @Autowired
    private RateMaintenceService rateMaintenceService;

    /**
     * 获取数据
     */
    @ApiOperation("查询贷款利率维护列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String loginName = getCurrentUser().getLoginName();
        return success(rateMaintenceService.listLoanRateMaintence(input, loginName));
    }
}
