package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbIntellectElementService;
import com.bytter.bill.domain.bill.vo.AbIntellectElementVO;
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

import java.util.List;

/**
 * author: liusy
 * date: 2022/10/20 10:30
 * description: 选票要素控制器
 */
@Api(value = "/v1-preposebill/bill/intellect/element", tags = "选票要素")
@RestController("v1AbIntellectElementController")
@RequestMapping("/v1-preposebill/bill/intellect/element")
public class AbIntellectElementController extends BaseController {

    @Autowired
    private AbIntellectElementService abIntellectElementService;

    /**
     * 选票要素列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询选票要素列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询选票要素分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abIntellectElementService.datatable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询选票要素详情
     */
    @LogMessage(content = "查询选票要素详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询选票要素详情")
    public AjaxJson<AbIntellectElementVO> getById(@PathVariable("id") String id) {
        AbIntellectElementVO vo = abIntellectElementService.getAbIntellectElementVOById(id);
        return AjaxJson.success(vo);
    }

    /**
     * 选票要素保存操作
     */
    @LogMessage(content = "保存选票要素", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存选票要素")
    public AjaxJson save(@RequestBody AbIntellectElementVO abIntellectElementVO) {
        abIntellectElementService.saveElement(abIntellectElementVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票要素编辑操作
     */
    @LogMessage(content = "修改选票要素", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ApiOperation("修改选票要素")
    public AjaxJson update(@RequestBody AbIntellectElementVO abIntellectElementVO) {
        abIntellectElementService.updateElement(abIntellectElementVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票要素删除操作
     */
    @LogMessage(content = "删除选票要素", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/del")
    @ApiOperation("删除选票要素")
    public AjaxJson del(@RequestParam String id) {
        abIntellectElementService.deleteElement(id);
        return AjaxJson.success();
    }

    /**
     * 获取所有选票要素
     */
    @LogMessage(content = "查询所有选票要素", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取所有选票要素")
    public AjaxJson<List<AbIntellectElementVO>> list() {
        List<AbIntellectElementVO> list = abIntellectElementService.listElement();
        return AjaxJson.success(list);
    }

}