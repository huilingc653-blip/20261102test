package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityAssetType;
import com.bytter.finance.domain.security.service.CmsSecurityAssetTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityAssetTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础资产类型表
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/assetType", tags = "基础资产类型")
@RestController("v1CmsSecurityAssetTypeController")
@RequestMapping("/v1-preposebill/cms/security/assetType")
public class CmsSecurityAssetTypeController extends BaseController {

    @Autowired
    private CmsSecurityAssetTypeService cmsSecurityAssetTypeService;

    /**
     * 基础资产类型列表查询
     */
    @LogMessage(content = "查询基础资产类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询基础资产类型分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityAssetTypeService.getDataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询基础资产类型详情
     */
    @LogMessage(content = "查询基础资产类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询基础资产类型详情")
    public AjaxJson<CmsSecurityAssetType> getById(@PathVariable("id") String id) {
        CmsSecurityAssetType entity = cmsSecurityAssetTypeService.get(id, true);
        return AjaxJson.success(entity);
    }

    /**
     * 新增基础资产类型
     */
    @LogMessage(content = "新增基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增基础资产类型")
    public AjaxJson save(@RequestBody CmsSecurityAssetType cmsSecurityAssetType) {
        cmsSecurityAssetTypeService.saveSecurityAssetType(cmsSecurityAssetType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改基础资产类型
     */
    @LogMessage(content = "修改基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改基础资产类型")
    public AjaxJson update(@RequestBody CmsSecurityAssetType cmsSecurityAssetType) {
        cmsSecurityAssetTypeService.updateSecurityAssetType(cmsSecurityAssetType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除基础资产类型
     */
    @LogMessage(content = "删除基础资产类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除基础资产类型")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityAssetTypeService.deleteSecurityAssetType(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取有效的基础资产类型列表
     */
    @LogMessage(content = "查询有效的基础资产类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的基础资产类型列表")
    public AjaxJson<List<CmsSecurityAssetTypeVO>> list() {
        List<CmsSecurityAssetTypeVO> list = cmsSecurityAssetTypeService.listSecurityAssetType();
        return AjaxJson.success(list);
    }

}