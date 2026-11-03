package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.bond.entity.CmsBondType;
import com.bytter.finance.domain.bond.service.CmsBondTypeService;
import com.bytter.finance.domain.bond.vo.CmsBondTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债券类型Controller
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/cms/bond/type", tags = "债券类型管理")
@RestController("v1CmsBondTypeController")
@RequestMapping("/v1-preposebill/cms/bond/type")
public class CmsBondTypeController extends BaseController {
    @Autowired
    private CmsBondTypeService cmsBondTypeService;



    /**
     * 获取债券类型编辑页初始化数据
     *
     * @param id 债券类型id
     */
    @ApiOperation("债券类型编辑页面初始化数据")
    @GetMapping("/update/{id}")
    public AjaxJson<Map<String, Object>> update(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cmsBondType", cmsBondTypeService.get(id, true));
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取债券类型列表数据
     */
    @ApiOperation("债券类型分页列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsBondTypeService.getDataTable(input);
        return AjaxJson.success(output);
    }

    /**
     * 新增债券类型
     *
     * @param cmsBondType 债券类型
     */
    @LogMessage(content = "新增债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增债券类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsBondType cmsBondType) {
        cmsBondTypeService.saveBondType(cmsBondType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改债券类型
     *
     * @param cmsBondType 债券类型
     */
    @LogMessage(content = "修改债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("修改债券类型")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody CmsBondType cmsBondType) {
        cmsBondTypeService.updateBondType(cmsBondType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除债券类型
     *
     * @param id 债券类型id
     */
    @LogMessage(content = "删除债券类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除债券类型")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondTypeService.deleteBondType(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取有效的债券类型集合
     */
    @ApiOperation("查询有效债券类型下拉列表")
    @GetMapping("/listValidBondType")
    public AjaxJson<List<CmsBondTypeVo>> listValidBondType() {
        List<CmsBondTypeVo> list = cmsBondTypeService.listValidBondType();
        return AjaxJson.success(list);
    }
}