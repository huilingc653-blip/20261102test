package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillInstructionManageService;
import com.bytter.bill.domain.bill.vo.AbBillInstructionManageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 指令处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillInstructionManage", tags = "指令处理")
@RestController("v1AbBillInstructionManageController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillInstructionManage")
public class AbBillInstructionManageController extends BaseController {
    @Autowired
    private AbBillInstructionManageService abBillInstructionManageService;

    /**
     * 指令处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @LogMessage(content = "查询指令处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("指令处理分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInstructionManageService.listAbBillInstructionManageInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 指令测试处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableTest")
    @LogMessage(content = "查询指令测试处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("指令测试处理分页查询")
    public AjaxJson<DataTablesOutput> dataTableTest(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInstructionManageService.listAbBillInstructionManageInfoTest(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量发送指令操作
     */
    @PostMapping("/batchSend")
    @LogMessage(content = "批量发送指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSEND)
    @ApiOperation("批量发送指令操作")
    public AjaxJson batchSend(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
        abBillInstructionManageService.batchSend(abBillInstructionManageVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量打回指令操作
     */
    @PostMapping("/batchRepulse")
    @LogMessage(content = "批量打回指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
    @ApiOperation("批量打回指令操作")
    public AjaxJson batchRepulse(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
        abBillInstructionManageService.batchRepulse(abBillInstructionManageVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量成功指令操作
     */
    @PostMapping("/batchSucce")
    @LogMessage(content = "批量成功指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @ApiOperation("批量成功指令操作")
    public AjaxJson batchSucce(@RequestBody AbBillInstructionManageVO abBillInstructionManageVO) {
        abBillInstructionManageService.batchSucce(abBillInstructionManageVO, this.getCurrentUser());
        return AjaxJson.success();
    }
}