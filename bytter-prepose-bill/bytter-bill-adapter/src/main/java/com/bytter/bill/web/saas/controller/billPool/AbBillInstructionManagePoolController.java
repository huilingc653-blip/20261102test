package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.dto.AbBillInstructionManageOperateDTO;
import com.bytter.bill.domain.billPool.service.AbBillInstructionManagePoolService;
import com.bytter.bill.domain.billPool.vo.AbBillInstructionManagePoolVO;
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
 * @Description: 票据池指令处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/pool/abbillInstructionManage", tags = "票据池指令处理")
@RestController("v1AbBillInstructionManagePoolController")
@RequestMapping("/v1-preposebill/bill/pool/abbillInstructionManage")
public class AbBillInstructionManagePoolController extends BaseController {

    @Autowired
    private AbBillInstructionManagePoolService abBillInstructionManagePoolService;

    /**
     * 批量成功指令操作
     */
    @ApiOperation("批量成功指令操作")
    @LogMessage(content = "批量成功指令操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @PostMapping("/batchSucce")
    public AjaxJson batchSucce(@RequestBody AbBillInstructionManagePoolVO instructionVo) {
        abBillInstructionManagePoolService.batchSucce(instructionVo, this.getCurrentUser());
        return success();
    }

    /**
     * 指令测试处理 table列表 票据池保证金
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询指令测试 票据池保证金处理列表")
    @LogMessage(content = "查询指令测试 票据池保证金处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableTestPoolBail")
    public AjaxJson<DataTablesOutput> dataTableTestPoolBail(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolBail(input));
    }

    /**
     * 指令测试处理 table列表 票据池额度管理
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询指令测试票据池额度管理处理列表")
    @LogMessage(content = "查询指令测试票据池额度管理处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableTestPoolLimit")
    public AjaxJson<DataTablesOutput> dataTableTestPoolLimit(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolLimit(input));
    }

    /**
     * 指令测试处理 table列表 票据池合同管理
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询指令测试处理票据池合同管理列表")
    @LogMessage(content = "查询指令测试处理票据池合同管理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableTestPoolCon")
    public AjaxJson<DataTablesOutput> dataTableTestPoolCon(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abBillInstructionManagePoolService.listAbBillInstructionManageInfoTestPoolCon(input));
    }

    /**
     * 根据参数修改对应表状态
     */
    @ApiOperation("指令状态操作")
    @PostMapping("/operation")
    public AjaxJson operation(@RequestBody AbBillInstructionManageOperateDTO dto) {
        abBillInstructionManagePoolService.operation(dto.getType(), dto.getId(), dto.getStatus(), this.getCurrentUser());
        return success();
    }
}
