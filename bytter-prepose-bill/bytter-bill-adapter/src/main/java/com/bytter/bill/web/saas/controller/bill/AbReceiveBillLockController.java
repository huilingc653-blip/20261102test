package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbReceiveBillLockService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillLockVo;
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
 * @Description: 票据锁定/解锁
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abReceiveBillLock", tags = "票据锁定/解锁")
@RestController("v1AbReceiveBillLockController")
@RequestMapping("/v1-preposebill/bill/accebill/abReceiveBillLock")
public class AbReceiveBillLockController extends BaseController {

    @Autowired
    private AbReceiveBillLockService abReceiveBillLockService;

    /**
     * 票据锁定/解锁 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据锁定/解锁列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询票据锁定/解锁分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abReceiveBillLockService.listAbReceiveBillLock(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 锁定
     */
    @LogMessage(content = "锁定", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/lock")
    @ApiOperation("锁定")
    public AjaxJson lock(@RequestBody AbReceiveBillLockVo back) {
        abReceiveBillLockService.lock(back, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 解锁
     */
    @LogMessage(content = "解锁", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/unLock")
    @ApiOperation("解锁")
    public AjaxJson unLock(@RequestBody AbReceiveBillLockVo back) {
        abReceiveBillLockService.unLock(back, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量锁定
     */
    @LogMessage(content = "批量锁定", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchLock")
    @ApiOperation("批量锁定")
    public AjaxJson batchLock(@RequestBody AbReceiveBillLockVo back) {
        abReceiveBillLockService.batchLock(back, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量解锁
     */
    @LogMessage(content = "批量解锁", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchUnLock")
    @ApiOperation("批量解锁")
    public AjaxJson batchUnLock(@RequestBody AbReceiveBillLockVo back) {
        abReceiveBillLockService.batchUnLock(back, this.getCurrentUser());
        return AjaxJson.success();
    }

}