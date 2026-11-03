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
import com.bytter.finance.domain.security.entity.CmsSecurityToAccount;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityToAccountService;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityToAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产证券化到账
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/toAccount", tags = "资产证券化到账")
@RestController("v1CmsSecurityToAccountController")
@RequestMapping("/v1-preposebill/cms/security/toAccount")
public class CmsSecurityToAccountController extends BaseController {

    @Autowired
    private CmsSecurityToAccountService cmsSecurityToAccountService;

    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;

    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;

    /**
     * 资产证券化到账列表查询
     */
    @LogMessage(content = "查询资产证券化到账列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化到账分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_95);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_21);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_25);
        DataTablesOutput output = cmsSecurityToAccountService.getDataTableInfo(dataInput, getCurrentUser(), bizStatuss);
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化到账审核列表查询
     */
    @LogMessage(content = "查询资产证券化到账审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询资产证券化到账审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityToAccountService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询发行列表数据
     */
    @LogMessage(content = "查询发行列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/issueList")
    @ApiOperation("查询发行列表数据")
    public AjaxJson<List<CmsSecurityIssueVO>> getIssueList(@RequestParam String infoId) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_95);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_21);
        bizStatuss.add(CmsConstant.SECURITY_BIZ_STATUS_25);
        List<CmsSecurityIssueVO> list = cmsSecurityIssueService.getSuccessIssueForInfoId(infoId, bizStatuss);
        return AjaxJson.success(list);
    }

    /**
     * 查询到账确认详情列表
     */
    @LogMessage(content = "查询到账确认详情列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/detail/{issueId}")
    @ApiOperation("查询到账确认详情列表")
    public AjaxJson<List<CmsSecurityToAccountVO>> getDetailList(@PathVariable("issueId") String issueId) {
        CmsSecurityIssue issue = cmsSecurityIssueService.get(issueId);
        List<CmsSecurityToAccountVO> toAccountVOS = cmsSecurityToAccountService.getIssueByCode(issue.getSecurityIssueCode());
        return AjaxJson.success(toAccountVOS);
    }

    /**
     * 查询新增到账确认初始化数据
     */
    @LogMessage(content = "查询新增到账确认初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/add/{issueId}")
    @ApiOperation("查询新增到账确认初始化数据")
    public AjaxJson<Map<String, Object>> addInit(@PathVariable("issueId") String issueId) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityIssue issue = cmsSecurityIssueService.get(issueId);
        dataMap.put("issue", issue);
        if (!EmptyUtils.isEmpty(issue)) {
            CmsSecurityInfo info = cmsSecurityInfoService.get(issue.getSecurityInfoId());
            dataMap.put("info", info);
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询到账确认审核详情
     */
    @LogMessage(content = "查询到账确认审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询到账确认审核详情")
    public AjaxJson<Map<String, Object>> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);
        CmsSecurityToAccount toAccount = cmsSecurityToAccountService.get(businessId);
        dataMap.put("toAccount", toAccount);
        List<CmsSecurityIssue> issueList = cmsSecurityIssueService.getSecurityIssueByCode(toAccount.getSecurityIssueCode(), null);
        if (!EmptyUtils.isEmptyList(issueList)) {
            dataMap.put("issue", issueList.get(0));
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 新增到账确认信息
     */
    @LogMessage(content = "新增到账确认信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增到账确认信息")
    public AjaxJson save(@RequestBody CmsSecurityToAccount cmsSecurityToAccount) {
        cmsSecurityToAccountService.save(cmsSecurityToAccount, this.getCurrentUser());
        return AjaxJson.success();
    }

}