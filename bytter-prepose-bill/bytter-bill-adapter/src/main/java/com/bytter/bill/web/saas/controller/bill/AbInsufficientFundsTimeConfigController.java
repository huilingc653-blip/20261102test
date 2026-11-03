package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbInsufficientFundsTimeConfig;
import com.bytter.bill.domain.bill.service.AbInsufficientFundsTimeConfigService;
import com.bytter.bill.domain.bill.vo.AbInsufficientFundsTimeConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 票据银行余额不足时间配置
 * @author zhous
 * @date 2025/6/24 10:30
 */
@Api(value = "/v1-preposebill/bill/accebill/bankInsufficientFundsTimeConfig", tags = "票据银行余额不足时间配置")
@RestController("v1AbInsufficientFundsTimeConfigController")
@RequestMapping("/v1-preposebill/bill/accebill/bankInsufficientFundsTimeConfig")
public class AbInsufficientFundsTimeConfigController extends BaseController {

    @Autowired
    private AbInsufficientFundsTimeConfigService abInsufficientFundsTimeConfigService;

    /**
     * 票据银行余额不足时间配置查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据银行余额不足时间配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询票据银行余额不足时间配置分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abInsufficientFundsTimeConfigService.dataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询票据银行余额不足时间配置详情
     */
    @LogMessage(content = "查询票据银行余额不足时间配置详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询票据银行余额不足时间配置详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>(2);
        if (StringUtils.isNotBlank(id)) {
            AbInsufficientFundsTimeConfig config = abInsufficientFundsTimeConfigService.get(id, true);
            dataMap.put("info", config);
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 票据银行余额不足时间配置新增或修改操作
     */
    @LogMessage(content = "票据银行余额不足时间配置新增或修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ApiOperation("票据银行余额不足时间配置新增或修改")
    public AjaxJson saveOrUpdate(@RequestBody AbInsufficientFundsTimeConfigVO info ) {
        abInsufficientFundsTimeConfigService.saveOrUpdate(info, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除配置
     */
    @LogMessage(content = "删除票据银行余额不足时间配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除票据银行余额不足时间配置")
    public AjaxJson delete(@PathVariable("id") String id) {
        abInsufficientFundsTimeConfigService.deleteConfig(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量删除票据银行余额不足时间配置
     */
    @LogMessage(content = "批量删除票据银行余额不足时间配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除票据银行余额不足时间配置")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        abInsufficientFundsTimeConfigService.batchDelete(ids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量禁用
     */
    @LogMessage(content = "批量禁用票据银行余额不足时间配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchDisable")
    @ApiOperation("批量禁用票据银行余额不足时间配置")
    public AjaxJson batchDisable(@RequestParam("ids") String ids) {
        abInsufficientFundsTimeConfigService.batchDisable(ids, this.getCurrentUser());
        return AjaxJson.success();
    }

}