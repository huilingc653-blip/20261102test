package com.bytter.supply.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.supply.domain.common.service.ScfInstructionService;
import com.bytter.supply.domain.common.vo.ScfInstructionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Title: ScfInstructionController
 * @Description: 指令管理
 */
@Api(value = "/v1-supply/supplyChain/instruction", tags = "指令管理")
@RestController("v1ScfInstructionController")
@RequestMapping("/v1-supply/supplyChain/instruction")
public class ScfInstructionController extends BaseController {

    @Autowired
    private ScfInstructionService scfInstructionService;

    /**
     * 应付指令列表（sign = 0）
     */
    @ApiOperation("应付指令列表")
    @PostMapping("/payableList")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public AjaxJson<DataTablesOutput<ScfInstructionVO>> payableList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(scfInstructionService.listPayableInstruction(input));
    }

    /**
     * 应收指令列表（sign = 1）
     */
    @ApiOperation("应收指令列表")
    @PostMapping("/receivableList")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public AjaxJson<DataTablesOutput<ScfInstructionVO>> receivableList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(scfInstructionService.listReceivableInstruction(input));
    }
}
