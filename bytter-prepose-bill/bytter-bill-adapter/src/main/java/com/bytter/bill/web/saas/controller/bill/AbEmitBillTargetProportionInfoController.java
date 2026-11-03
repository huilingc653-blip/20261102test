package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbEmitBillTargetProportionInfo;
import com.bytter.bill.domain.bill.service.AbEmitBillTargetProportionInfoService;
import com.bytter.bill.domain.bill.vo.AbEmitBillTargetProportionInfoVO;
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
import java.util.List;
import java.util.Map;

/**
 * @Description 银承开票占比目标维护
 * @author zhous
 * @date 2025/4/22 10:30
 */
@Api(value = "/v1-preposebill/bill/accebill/billTargetProportion", tags = "银承开票占比目标维护")
@RestController("v1AbEmitBillTargetProportionInfoController")
@RequestMapping("/v1-preposebill/bill/accebill/billTargetProportion")
public class AbEmitBillTargetProportionInfoController extends BaseController {

    @Autowired
    private AbEmitBillTargetProportionInfoService abEmitBillTargetProportionInfoService;

    /**
     * 银承开票占比目标维护列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询银承开票占比目标维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询银承开票占比目标维护分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillTargetProportionInfoService.dataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询银承开票占比目标维护详情
     */
    @LogMessage(content = "查询银承开票占比目标维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询银承开票占比目标维护详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>(2);
        if (StringUtils.isNotBlank(id)) {
            AbEmitBillTargetProportionInfo info = abEmitBillTargetProportionInfoService.get(id, true);
            dataMap.put("info", info);
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 银承开票占比目标维护新增或修改操作
     */
    @LogMessage(content = "银承开票占比目标维护新增或修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ApiOperation("银承开票占比目标维护新增或修改")
    public AjaxJson saveOrUpdate(@RequestBody AbEmitBillTargetProportionInfoVO info ) {
        abEmitBillTargetProportionInfoService.saveOrUpdate(info, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除记录
     */
    @LogMessage(content = "删除银承开票占比目标维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除银承开票占比目标维护")
    public AjaxJson delete(@PathVariable("id") String id) {
        abEmitBillTargetProportionInfoService.deleteById(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 导入银承开票占比目标维护
     */
    @LogMessage(content = "导入银承开票占比目标维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ApiOperation("导入银承开票占比目标维护")
    public AjaxJson saveImport(@RequestBody List<AbEmitBillTargetProportionInfoVO> list) {
        abEmitBillTargetProportionInfoService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }

}