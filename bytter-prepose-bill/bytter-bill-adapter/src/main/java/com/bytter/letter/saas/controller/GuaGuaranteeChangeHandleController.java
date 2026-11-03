package com.bytter.letter.saas.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.letter.domain.service.GuaGuaranteeChangeHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeHandleController
 * @Description:保函更改办理控制层
 * @date 2019年1月14日 上午11:49:17
 */
@Api(value = "/v1-guarantee/letter/guarantee/guaGuaranteeChangeHandle",tags = "保函更改办理控制层")
@RestController("v1GuaGuaranteeChangeHandleController")
@RequestMapping("/v1-guarantee/letter/guarantee/guaGuaranteeChangeHandle")
public class GuaGuaranteeChangeHandleController extends BaseController {
    @Autowired
    private GuaGuaranteeChangeHandleService LgChangeHandleService;

    /**
     * 查询保函申请更改可办理数据列表
     */
    @ApiOperation(value = "查询保函申请更改可办理数据列表")
    @LogMessage(content = "查询保函申请更改可办理数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput tablesInput = input.convertAndInitColumns();
        return success(LgChangeHandleService.datatableList(tablesInput, this.getCurrentUser()));
    }
    /**
     * 完成更改
     */
    @ApiOperation("完成更改")
    @LogMessage(content = "执行保函申请更改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/submit/{id}")
    @ResponseBody
    public AjaxJson submit(@PathVariable String id) {
        LgChangeHandleService.submit(id, this.getCurrentUser());
        return success();
    }
    /**
     * 拒绝更改
     */
    @ApiOperation("拒绝更改")
    @LogMessage(content = "执行拒绝保函申请更改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/refuse/{id}")
    @ResponseBody
    public AjaxJson refuse(@PathVariable String id) {
        LgChangeHandleService.refuse(id, this.getCurrentUser());
        return success();
    }
}
