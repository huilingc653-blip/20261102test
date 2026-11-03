package com.bytter.supply.controller;


import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.entity.ScfPayConfigVersion;
import com.bytter.supply.domain.payable.service.ScfPayConfigVersionService;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVersionVO;
import com.bytter.supply.domain.payable.vo.ScfPayTemplateProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Api(value = "/v1-supply/supplyChain/scfPayConfigVersion", tags = "应付债权凭证模版选择")
@RestController("v1ScfPayConfigVersionController")
@RequestMapping("/v1-supply/supplyChain/scfPayConfigVersion")
public class ScfPayConfigVersionController extends BaseController {

    @Autowired
    private ScfPayConfigVersionService scfPayConfigVersionService;

    @ApiOperation("应付债权凭证模版选择Table页")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ScfPayTemplateProductVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayConfigVersionService.getDataTablesUnion(tablesInput));
    }

}
