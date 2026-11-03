package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.security.entity.CmsSecurityRepaySure;
import com.bytter.finance.domain.security.service.CmsSecurityRepaySureService;
import com.bytter.finance.domain.security.vo.CmsSecurityRepaySureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产证券化还款确认表
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/repaySure", tags = "资产证券化还款确认")
@RestController("v1CmsSecurityRepaySureController")
@RequestMapping("/v1-preposebill/cms/security/repaySure")
public class CmsSecurityRepaySureController extends BaseController {

    @Autowired
    private CmsSecurityRepaySureService cmsSecurityRepaySureService;

    /**
     * 资产证券化还款确认列表查询
     */
    @LogMessage(content = "查询资产证券化还款确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化还款确认分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_51);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        DataTablesOutput output = cmsSecurityRepaySureService.getDataTableInfo(dataInput, this.getCurrentUser(), bizStatuss);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化还款确认审核列表查询
     */
    @LogMessage(content = "查询资产证券化还款确认审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询资产证券化还款确认审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityRepaySureService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询还款确认列表数据
     */
    @LogMessage(content = "查询还款确认列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/list")
    @ApiOperation("查询还款确认列表数据")
    public AjaxJson<List<CmsSecurityRepaySure>> listRepaySure(@RequestBody CmsSecurityRepaySureVO vo) {
        List<CmsSecurityRepaySure> list = cmsSecurityRepaySureService.listRepaySure(vo);
        return AjaxJson.success(list);
    }

    /**
     * 查询已选择的还款确认列表数据
     */
    @LogMessage(content = "查询已选择的还款确认列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/listChose")
    @ApiOperation("查询已选择的还款确认列表数据")
    public AjaxJson<List<CmsSecurityRepaySure>> listRepaySureChose(@RequestBody CmsSecurityRepaySureVO vo) {
        vo.setIsChose("1");
        List<CmsSecurityRepaySure> list = cmsSecurityRepaySureService.listRepaySure(vo);
        return AjaxJson.success(list);
    }

    /**
     * 查询全部还款确认列表数据（含已还款）
     */
    @LogMessage(content = "查询全部还款确认列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/listView")
    @ApiOperation("查询全部还款确认列表数据")
    public AjaxJson<List<CmsSecurityRepaySure>> listRepaySureView(@RequestBody CmsSecurityRepaySureVO vo) {
        List<CmsSecurityRepaySure> list = cmsSecurityRepaySureService.listRepaySureView(vo);
        return AjaxJson.success(list);
    }

    /**
     * 查询还款确认详情
     */
    @LogMessage(content = "查询还款确认详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询还款确认详情")
    public AjaxJson<CmsSecurityRepaySure> getById(@PathVariable("id") String id) {
        CmsSecurityRepaySure repaySure = cmsSecurityRepaySureService.get(id);
        return AjaxJson.success(repaySure);
    }

    /**
     * 查询还款确认审核详情
     */
    @LogMessage(content = "查询还款确认审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询还款确认审核详情")
    public AjaxJson<Map<String, Object>> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);
        CmsSecurityRepaySure repaySure = cmsSecurityRepaySureService.get(businessId);
        dataMap.put("repaySure", repaySure);
        return AjaxJson.success(dataMap);
    }

    /**
     * 选择还款确认数据
     */
    @LogMessage(content = "选择还款确认数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/add")
    @ApiOperation("选择还款确认数据")
    public AjaxJson addPaySure(@RequestBody CmsSecurityRepaySureVO vo) {
        cmsSecurityRepaySureService.addPaySure(vo);
        return AjaxJson.success();
    }

    /**
     * 取消选择还款确认数据
     */
    @LogMessage(content = "取消选择还款确认数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/delete")
    @ApiOperation("取消选择还款确认数据")
    public AjaxJson deletePaySure(@RequestBody CmsSecurityRepaySureVO vo) {
        cmsSecurityRepaySureService.deletePaySure(vo);
        return AjaxJson.success();
    }

    /**
     * 批量提交还款确认
     */
    @LogMessage(content = "批量提交还款确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/save")
    @ApiOperation("批量提交还款确认")
    public AjaxJson save(@RequestParam String infoId) {
        // 获取已选择的数据
        CmsSecurityRepaySureVO vo = new CmsSecurityRepaySureVO();
        vo.setSecurityInfoId(infoId);
        vo.setIsChose(CmsConstant.IS_TRUE);
        List<CmsSecurityRepaySure> repaySureList = cmsSecurityRepaySureService.listRepaySure(vo);
        // 批量还款
        for (CmsSecurityRepaySure repaySure : repaySureList) {
            cmsSecurityRepaySureService.save(repaySure, this.getCurrentUser());
        }
        return AjaxJson.success();
    }

}