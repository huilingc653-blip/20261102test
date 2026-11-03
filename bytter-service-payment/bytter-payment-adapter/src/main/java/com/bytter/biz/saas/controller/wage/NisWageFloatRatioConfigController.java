package com.bytter.biz.saas.controller.wage;

import com.bytter.biz.wage.entity.NisWageFloatRatioConfig;
import com.bytter.biz.wage.service.NisWageFloatRatioConfigService;
import com.bytter.biz.wage.vo.NisWageFloatRatioConfigVO;
import com.bytter.biz.wage.vo.NisWageFloatRatioMessageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 代发工资浮动比率配置控制层
 *
 * @author fangcf
 * @date 2025-11-20
 */
@Api(value = "/v1-payment/wage/wageFloatRatio", tags = "代发工资浮动比率配置")
@RestController("vqNisWageFloatRatioConfigController")
@RequestMapping("/v1-payment/wage/wageFloatRatio")
public class NisWageFloatRatioConfigController extends BaseController {

    @Autowired
    private NisWageFloatRatioConfigService nisWageFloatRatioConfigService;

    @Autowired
    private IDictService dictService;


    /**
     * 代发工资浮动比率配置 table列表
     *
     * @param dataTablesInput 入参
     * @return 出参
     */
    @ApiOperation("代发工资浮动比率配置 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<NisWageFloatRatioConfigVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<NisWageFloatRatioConfigVO> dataTable = nisWageFloatRatioConfigService.dataTable(input);
        Map<String, DictDTO> dictDTOMap = dictService.listMap("btBankAccDtl.months");
        dataTable.getData().forEach(item -> {
            item.setMonthName(dictDTOMap.getOrDefault(item.getMonth(),DictDTO.EMPTY).getName());
        });
        return AjaxJson.success(dataTable);
    }


    /**
     * 代发工资浮动比率配置编辑页面跳转
     *
     * @return 代发工资浮动比率配置新增跳转地址
     */
    @ApiOperation("代发工资浮动比率配置编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisWageFloatRatioConfig> update(@PathVariable("id") String id) {
        return AjaxJson.success(nisWageFloatRatioConfigService.get(id, true));
    }


    /**
     * 操作记录查询 table列表
     *
     * @param input 入参
     * @return 出参
     */
    @ApiOperation("操作记录查询 table列表")
    @PostMapping("/datatableMessage")
    @ResponseBody
    public AjaxJson<DataTablesOutput<NisWageFloatRatioMessageVO>> datatableMessage(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        DataTablesOutput<NisWageFloatRatioMessageVO> output = nisWageFloatRatioConfigService.datatableMessage(dataTablesInput);
        Map<String, DictDTO> monthNameMap = dictService.listMap("btBankAccDtl.months");
        Map<String, DictDTO> messageTypeMap = dictService.listMap("nisMessage.messageType");
        output.getData().forEach(item -> {
            item.setMonthName(monthNameMap.getOrDefault(item.getMonth(),DictDTO.EMPTY).getName());
            item.setMessageTypeName(messageTypeMap.getOrDefault(String.valueOf(item.getMessageType()),DictDTO.EMPTY).getName());
        });
        return AjaxJson.success(output);
    }

    /**
     * 保存代发工资浮动比率配置
     *
     * @param form 表单数据
     * @return 操作结果
     */
    @ApiOperation("保存代发工资浮动比率配置")
    @LogMessage(content = "保存代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisWageFloatRatioConfig form) {
        nisWageFloatRatioConfigService.saveWageFloatRatio(form, this.getCurrentUser());
        return success();
    }

    /**
     * 修改代发工资浮动比率配置
     *
     * @param form 表单数据
     * @return 操作结果
     */
    @ApiOperation("修改代发工资浮动比率配置")
    @LogMessage(content = "修改代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody NisWageFloatRatioConfig form) {
        nisWageFloatRatioConfigService.updateWageFloatRatio(form, this.getCurrentUser());
        return success();
    }

    /**
     * 删除代发工资浮动比率配置
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除代发工资浮动比率配置")
    @LogMessage(content = "删除代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisWageFloatRatioConfigService.delWageFloatRatio(id, this.getCurrentUser());
        return success();
    }

}
