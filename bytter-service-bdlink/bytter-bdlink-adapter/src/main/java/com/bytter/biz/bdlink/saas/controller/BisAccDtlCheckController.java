package com.bytter.biz.bdlink.saas.controller;

import com.bytter.biz.bdlink.service.BisAccDtlCheckService;
import com.bytter.biz.bdlink.vo.BisAccDtlCheckVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
@Api(value = "余额流水异常核查表",tags = "余额流水异常核查表")
@RestController("v1BisAccDtlCheckController")
@RequestMapping("/v1-bdlink/bankacc/bisaccdtlcheck")
public class BisAccDtlCheckController extends BaseController {

    @Autowired
    private BisAccDtlCheckService bisAccDtlCheckService;


    /**
     * 余额流水异常核查 table列表
     *
     * @return
     */
    @ApiOperation("余额流水异常核查")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlCheckVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisAccDtlCheckService.getDataTablesUnion(input, this.getCurrentUser()));
    }


    @ApiOperation("核查账户设置 table列表")
    @PostMapping("/queryBankAccDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> queryBankAccDatatable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisAccDtlCheckService.queryBankAccDatatable(input, this.getCurrentUser()));
    }

    /**
     * 批量设置已处理（保存）
     *
     * @return
     */
    @ApiOperation(value = "批量设置已处理（保存）")
    @LogMessage(content = "批量设置已处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/setCheckBankAccSave")
    @ResponseBody
    public AjaxJson setCheckBankAccSave(@RequestParam("ids") String ids, @RequestParam("checkSign") String checkSign) {
        this.bisAccDtlCheckService.setCheckBankAccSave(ids, checkSign);
        return success();
    }

    /**
     * 批量设置已处理
     *
     * @return
     */
    @ApiOperation("批量设置已处理")
    @LogMessage(content = "批量设置已处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchHandled")
    @ResponseBody
    public AjaxJson batchHandled(@RequestParam("ids") String ids) {
        this.bisAccDtlCheckService.batchUpdateStatus(ids, CommonConstant.YES, this.getCurrentUser());
        return success();
    }

    /**
     * 批量设置未处理
     *
     * @return
     */
    @ApiOperation("批量设置未处理")
    @LogMessage(content = "批量设置未处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchUnHandled")
    @ResponseBody
    public AjaxJson batchUnHandled(@RequestParam("ids") String ids) {
        this.bisAccDtlCheckService.batchUpdateStatus(ids, CommonConstant.NO, this.getCurrentUser());
        return success();
    }
}
