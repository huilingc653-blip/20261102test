package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityType;
import com.bytter.finance.domain.security.service.CmsSecurityTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产证券化类型表
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/type", tags = "资产证券化类型")
@RestController("v1CmsSecurityTypeController")
@RequestMapping("/v1-preposebill/cms/security/type")
public class CmsSecurityTypeController extends BaseController {

    @Autowired
    private CmsSecurityTypeService cmsSecurityTypeService;

    /**
     * 资产证券化类型列表查询
     */
    @LogMessage(content = "查询资产证券化类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化类型分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityTypeService.getDataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询资产证券化类型详情
     */
    @LogMessage(content = "查询资产证券化类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询资产证券化类型详情")
    public AjaxJson<CmsSecurityType> getById(@PathVariable("id") String id) {
        CmsSecurityType entity = cmsSecurityTypeService.get(id, true);
        return AjaxJson.success(entity);
    }

    /**
     * 新增资产证券化类型
     */
    @LogMessage(content = "新增资产证券化类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增资产证券化类型")
    public AjaxJson save(@RequestBody CmsSecurityType cmsSecurityType) {
        cmsSecurityTypeService.saveSecurityType(cmsSecurityType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改资产证券化类型
     */
    @LogMessage(content = "修改资产证券化类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改资产证券化类型")
    public AjaxJson update(@RequestBody CmsSecurityType cmsSecurityType) {
        cmsSecurityTypeService.updateSecurityType(cmsSecurityType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除资产证券化类型
     */
    @LogMessage(content = "删除资产证券化类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除资产证券化类型")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityTypeService.deleteSecurityType(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取有效的资产证券化类型列表
     */
    @LogMessage(content = "查询有效的资产证券化类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的资产证券化类型列表")
    public AjaxJson<List<CmsSecurityTypeVO>> list() {
        List<CmsSecurityTypeVO> list = cmsSecurityTypeService.listSecurityType();
        return AjaxJson.success(list);
    }

}