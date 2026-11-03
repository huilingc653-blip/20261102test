package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityLevel;
import com.bytter.finance.domain.security.service.CmsSecurityLevelService;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产证券化层级表
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/level", tags = "资产证券化层级")
@RestController("v1CmsSecurityLevelController")
@RequestMapping("/v1-preposebill/cms/security/level")
public class CmsSecurityLevelController extends BaseController {

    @Autowired
    private CmsSecurityLevelService cmsSecurityLevelService;

    /**
     * 资产证券化层级列表查询
     */
    @LogMessage(content = "查询资产证券化层级列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化层级分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityLevelService.getDataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询资产证券化层级详情
     */
    @LogMessage(content = "查询资产证券化层级详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询资产证券化层级详情")
    public AjaxJson<CmsSecurityLevel> getById(@PathVariable("id") String id) {
        CmsSecurityLevel entity = cmsSecurityLevelService.get(id, true);
        return AjaxJson.success(entity);
    }

    /**
     * 新增资产证券化层级
     */
    @LogMessage(content = "新增资产证券化层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增资产证券化层级")
    public AjaxJson save(@RequestBody CmsSecurityLevel cmsSecurityLevel) {
        cmsSecurityLevelService.saveSecurityLevel(cmsSecurityLevel, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改资产证券化层级
     */
    @LogMessage(content = "修改资产证券化层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改资产证券化层级")
    public AjaxJson update(@RequestBody CmsSecurityLevel cmsSecurityLevel) {
        cmsSecurityLevelService.updateSecurityLevel(cmsSecurityLevel, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除资产证券化层级
     */
    @LogMessage(content = "删除资产证券化层级", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除资产证券化层级")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityLevelService.deleteSecurityLevel(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 获取有效的资产证券化层级列表
     */
    @LogMessage(content = "查询有效的资产证券化层级列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的资产证券化层级列表")
    public AjaxJson<List<CmsSecurityLevelVO>> list() {
        List<CmsSecurityLevelVO> list = cmsSecurityLevelService.listSecurityLevel();
        return AjaxJson.success(list);
    }

}