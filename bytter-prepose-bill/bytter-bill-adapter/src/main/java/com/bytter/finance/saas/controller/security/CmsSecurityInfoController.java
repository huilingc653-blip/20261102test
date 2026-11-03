package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.service.CmsSecurityAssetTypeService;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityTypeService;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产证券化登记主信息表
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/info", tags = "资产证券化登记")
@RestController("v1CmsSecurityInfoController")
@RequestMapping("/v1-preposebill/cms/security/info")
public class CmsSecurityInfoController extends BaseController {

    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;

    @Autowired
    private CmsSecurityTypeService cmsSecurityTypeService;

    @Autowired
    private CmsSecurityAssetTypeService cmsSecurityAssetTypeService;

    /**
     * 资产证券化登记列表查询
     */
    @LogMessage(content = "查询资产证券化登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化登记分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityInfoService.getDataTable(dataInput, getCurrentUser(), null);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化登记审核列表查询
     */
    @LogMessage(content = "查询资产证券化登记审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询资产证券化登记审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityInfoService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询资产证券化登记详情
     */
    @LogMessage(content = "查询资产证券化登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询资产证券化登记详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityInfoVO infoVO = cmsSecurityInfoService.getInfoById(id);
        // 设置类型名称
        infoVO.setSecurityTypeName(cmsSecurityTypeService.getTypeById(infoVO.getSecurityTypeId()).getTypeName());
        infoVO.setAssetTypeName(cmsSecurityAssetTypeService.getAssetTypeByCode(infoVO.getAssetTypeCode()).getAssetTypeName());
        dataMap.put("info", infoVO);
        // 查询层级信息
        List<CmsSecurityLevelInfoVO> levelInfoVOList = cmsSecurityInfoService.listLevelInfo(id);
        dataMap.put("levelInfoList", levelInfoVOList);
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询资产证券化登记审核详情
     */
    @LogMessage(content = "查询资产证券化登记审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询资产证券化登记审核详情")
    public AjaxJson<Map<String, Object>> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);
        CmsSecurityInfoVO infoVO = cmsSecurityInfoService.getInfoById(businessId);
        // 设置类型名称
        infoVO.setSecurityTypeName(cmsSecurityTypeService.getTypeById(infoVO.getSecurityTypeId()).getTypeName());
        infoVO.setAssetTypeName(cmsSecurityAssetTypeService.getAssetTypeByCode(infoVO.getAssetTypeCode()).getAssetTypeName());
        dataMap.put("info", infoVO);
        // 查询层级信息
        List<CmsSecurityLevelInfoVO> levelInfoVOList = cmsSecurityInfoService.listLevelInfo(businessId);
        dataMap.put("levelInfoList", levelInfoVOList);
        return AjaxJson.success(dataMap);
    }

    /**
     * 新增资产证券化登记
     */
    @LogMessage(content = "新增资产证券化登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增资产证券化登记")
    public AjaxJson save(@RequestBody CmsSecurityInfoVO cmsSecurityInfoVO) {
        cmsSecurityInfoService.save(cmsSecurityInfoVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改资产证券化登记
     */
    @LogMessage(content = "修改资产证券化登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改资产证券化登记")
    public AjaxJson update(@RequestBody CmsSecurityInfoVO cmsSecurityInfoVO) {
        cmsSecurityInfoService.modify(cmsSecurityInfoVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除资产证券化登记
     */
    @LogMessage(content = "删除资产证券化登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除资产证券化登记")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityInfoService.delete(id, this.getCurrentUser());
        return AjaxJson.success();
    }

}