package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.security.entity.CmsSecurityInfo;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.entity.CmsSecurityPlan;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanDetailService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanService;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产证券化计划
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/plan", tags = "资产证券化计划")
@RestController("v1CmsSecurityPlanController")
@RequestMapping("/v1-preposebill/cms/security/plan")
public class CmsSecurityPlanController extends BaseController {

    @Autowired
    private CmsSecurityPlanService cmsSecurityPlanService;

    @Autowired
    private CmsSecurityPlanDetailService cmsSecurityPlanDetailService;

    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;

    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;

    /**
     * 资产证券化计划登记列表查询
     */
    @LogMessage(content = "查询资产证券化计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化计划登记分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_96);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_31);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__3);
        DataTablesOutput output = cmsSecurityPlanService.getDataTableInfo(dataInput, this.getCurrentUser(), bizStatuss);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化计划修改列表查询
     */
    @LogMessage(content = "查询资产证券化计划修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/update/datatable")
    @ApiOperation("查询资产证券化计划修改分页列表")
    public AjaxJson<DataTablesOutput> updateDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_41);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__4);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        DataTablesOutput output = cmsSecurityPlanService.getupDataTableInfo(dataInput, this.getCurrentUser(), bizStatuss);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化计划审核列表查询
     */
    @LogMessage(content = "查询资产证券化计划审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询资产证券化计划审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityPlanService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化计划修改审核列表查询
     */
    @LogMessage(content = "查询资产证券化计划修改审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/update/approve/datatable")
    @ApiOperation("查询资产证券化计划修改审核分页列表")
    public AjaxJson<DataTablesOutput> updateApproveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityPlanService.dataTableUpdateApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询发行列表数据（计划登记）
     */
    @LogMessage(content = "查询发行列表数据（计划登记）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/issueList")
    @ApiOperation("查询发行列表数据（计划登记）")
    public AjaxJson<List<CmsSecurityIssueVO>> getIssueList(@RequestParam String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_96);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_31);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__3);
        List<CmsSecurityIssueVO> list = cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
        return AjaxJson.success(list);
    }

    /**
     * 查询发行列表数据（计划修改）
     */
    @LogMessage(content = "查询发行列表数据（计划修改）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/update/issueList")
    @ApiOperation("查询发行列表数据（计划修改）")
    public AjaxJson<List<CmsSecurityIssueVO>> getUpdateIssueList(@RequestParam String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_97);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_41);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS__4);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_98);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_55);
        List<CmsSecurityIssueVO> list = cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
        return AjaxJson.success(list);
    }

    /**
     * 查询计划明细数据
     */
    @LogMessage(content = "查询计划明细数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/detail")
    @ApiOperation("查询计划明细数据")
    public AjaxJson<List<CmsSecurityPlanDetailVO>> getPlanDetail(@RequestParam String securityIssueCode) {
        List<CmsSecurityPlanDetailVO> list = cmsSecurityPlanDetailService.getPlanDetailByCode(securityIssueCode);
        return AjaxJson.success(list);
    }

    /**
     * 查询计划详情
     */
    @LogMessage(content = "查询计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询计划详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityPlan cmsSecurityPlan = cmsSecurityPlanService.get(id);
        dataMap.put("securityIssueCode", cmsSecurityPlan.getSecurityIssueCode());
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询计划审核详情
     */
    @LogMessage(content = "查询计划审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询计划审核详情")
    public AjaxJson<Map<String, Object>> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);
        CmsSecurityPlan cmsSecurityPlan = cmsSecurityPlanService.get(businessId);
        dataMap.put("securityIssueCode", cmsSecurityPlan.getSecurityIssueCode());
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询新增计划初始化数据
     */
    @LogMessage(content = "查询新增计划初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/add/{issueId}")
    @ApiOperation("查询新增计划初始化数据")
    public AjaxJson<Map<String, Object>> addInit(@PathVariable("issueId") String issueId) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityIssue issue = cmsSecurityIssueService.get(issueId);
        dataMap.put("issueMoney", issue.getThisIssueMoney());
        dataMap.put("securityIssueCode", issue.getSecurityIssueCode());
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            dataMap.put("corpId", info.getCorpId());
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询修改计划初始化数据
     */
    @LogMessage(content = "查询修改计划初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/update/{issueId}")
    @ApiOperation("查询修改计划初始化数据")
    public AjaxJson<Map<String, Object>> updateInit(@PathVariable("issueId") String issueId) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityIssue issue = cmsSecurityIssueService.get(issueId);
        CmsSecurityPlanVO planVO = cmsSecurityPlanService.getPlanBySecurityCode(issue.getSecurityIssueCode());
        if (!EmptyUtils.isEmpty(planVO)) {
            dataMap.put("planId", planVO.getId());
        }
        dataMap.put("issueMoney", issue.getThisIssueMoney());
        dataMap.put("securityIssueCode", issue.getSecurityIssueCode());
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            dataMap.put("corpId", info.getCorpId());
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 计划登记/修改保存
     */
    @LogMessage(content = "计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("计划登记保存")
    public AjaxJson save(@RequestBody CmsSecurityPlanVO vo, @RequestParam String isPass) {
        cmsSecurityPlanService.Save(vo, this.getCurrentUser(), isPass);
        return AjaxJson.success();
    }

}