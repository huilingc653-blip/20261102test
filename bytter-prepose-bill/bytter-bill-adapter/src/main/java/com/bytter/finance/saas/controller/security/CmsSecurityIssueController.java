package com.bytter.finance.saas.controller.security;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.service.CmsSecurityInfoService;
import com.bytter.finance.domain.security.service.CmsSecurityIssueService;
import com.bytter.finance.domain.security.service.CmsSecurityPlanService;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 资产证券化发行
 *
 * @author tongqr
 */
@Api(value = "/v1-preposebill/cms/security/issue", tags = "资产证券化发行")
@RestController("v1CmsSecurityIssueController")
@RequestMapping("/v1-preposebill/cms/security/issue")
public class CmsSecurityIssueController extends BaseController {

    @Autowired
    private CmsSecurityInfoService cmsSecurityInfoService;

    @Autowired
    private CmsSecurityIssueService cmsSecurityIssueService;

    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;

    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;

    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;

    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;

    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;

    @Autowired
    private CmsSecurityPlanService cmsSecurityPlanService;

    /**
     * 资产证券化发行登记列表查询
     */
    @LogMessage(content = "查询资产证券化发行登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询资产证券化发行登记分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityInfoService.getDataTableForissue(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化发行查询列表查询
     */
    @LogMessage(content = "查询资产证券化发行查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/query/datatable")
    @ApiOperation("查询资产证券化发行查询分页列表")
    public AjaxJson<DataTablesOutput> queryDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityInfoService.getDataTable(dataInput, this.getCurrentUser(), "query");
        return AjaxJson.success(output);
    }

    /**
     * 资产证券化发行审核列表查询
     */
    @LogMessage(content = "查询资产证券化发行审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询资产证券化发行审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = cmsSecurityIssueService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询资产证券化发行详情
     */
    @LogMessage(content = "查询资产证券化发行详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询资产证券化发行详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityIssue cmsSecurityIssue = cmsSecurityIssueService.get(id, true);
        dataMap.put("issue", cmsSecurityIssue);
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(cmsSecurityIssue.getSecurityInfoId());
        dataMap.put("info", info);
        // 利率相关信息
        Optional.ofNullable(cmsSecurityIssue).ifPresent(issue -> {
            if (StringUtils.isNotEmpty(issue.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(issue.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> dataMap.put("tbBaseRate", rateDTO));
            }
            if (StringUtils.isNotEmpty(issue.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(issue.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> dataMap.put("tbCreditPeriod", periodDTO));
            }
        });
        // 第三方费率信息
        List<DfIntermediateCostVO> costList = cmsSecurityIssueService.listIntermediateCostByDfId(cmsSecurityIssue.getId());
        dataMap.put("costList", costList);
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询资产证券化发行审核详情
     */
    @LogMessage(content = "查询资产证券化发行审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询资产证券化发行审核详情")
    public AjaxJson<Map<String, Object>> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);
        CmsSecurityIssue cmsSecurityIssue = cmsSecurityIssueService.get(businessId, true);
        dataMap.put("issue", cmsSecurityIssue);
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(cmsSecurityIssue.getSecurityInfoId());
        dataMap.put("info", info);
        // 利率相关信息
        Optional.ofNullable(cmsSecurityIssue).ifPresent(issue -> {
            if (StringUtils.isNotEmpty(issue.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(issue.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rateDTO -> dataMap.put("tbBaseRate", rateDTO));
            }
            if (StringUtils.isNotEmpty(issue.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(issue.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(periodDTO -> dataMap.put("tbCreditPeriod", periodDTO));
            }
        });
        // 第三方费率信息
        List<DfIntermediateCostVO> costList = cmsSecurityIssueService.listIntermediateCostByDfId(cmsSecurityIssue.getId());
        dataMap.put("costList", costList);
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询资产层级信息
     */
    @LogMessage(content = "查询资产层级信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/level/{infoId}")
    @ApiOperation("查询资产层级信息")
    public AjaxJson<List<CmsSecurityLevelInfoVO>> getLevelList(@PathVariable("infoId") String infoId) {
        List<CmsSecurityLevelInfoVO> levelInfoVOS = cmsSecurityInfoService.listLevelInfo(infoId);
        return AjaxJson.success(levelInfoVOS);
    }

    /**
     * 查询发行列表数据（查询模式）
     */
    @LogMessage(content = "查询发行列表数据（查询模式）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/query/issueList")
    @ApiOperation("查询发行列表数据（查询模式）")
    public AjaxJson<List<CmsSecurityIssueVO>> getIssueQueryList(@RequestParam String infoId) {
        List<CmsSecurityIssueVO> list = cmsSecurityIssueService.getIssueForInfoId(infoId, "query");
        return AjaxJson.success(list);
    }

    /**
     * 查询发行列表数据
     */
    @LogMessage(content = "查询发行列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/issueList")
    @ApiOperation("查询发行列表数据")
    public AjaxJson<List<CmsSecurityIssueVO>> getIssueList(@RequestParam String infoId) {
        List<CmsSecurityIssueVO> list = cmsSecurityIssueService.getIssueForInfoId(infoId, null);
        return AjaxJson.success(list);
    }

    /**
     * 查询新增发行初始化数据
     */
    @LogMessage(content = "查询新增发行初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/add/{infoId}")
    @ApiOperation("查询新增发行初始化数据")
    public AjaxJson<Map<String, Object>> addInit(@PathVariable("infoId") String infoId) {
        Map<String, Object> dataMap = new HashMap<>();
        CmsSecurityInfoVO info = cmsSecurityInfoService.getInfoById(infoId);
        dataMap.put("info", info);
        dataMap.put("isHookBond", "1");
        dataMap.put("pledgeConfig", "1");
        dataMap.put("isGuarantee", "1");
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询新增发行计划初始化数据
     */
    @LogMessage(content = "查询新增发行计划初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/addPlan")
    @ApiOperation("查询新增发行计划初始化数据")
    public AjaxJson<Map<String, Object>> addPlanInit(@RequestParam String securityIssueCode, @RequestParam String issueMoney, @RequestParam String corpId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("issueMoney", issueMoney);
        dataMap.put("securityIssueCode", securityIssueCode);
        dataMap.put("corpId", corpId);
        dataMap.put("isPass", "isPass");
        CmsSecurityPlanVO planVO = cmsSecurityPlanService.getPlanBySecurityCode(securityIssueCode);
        if (!EmptyUtils.isEmpty(planVO)) {
            dataMap.put("planId", planVO.getId());
        }
        return AjaxJson.success(dataMap);
    }

    /**
     * 新增资产证券化发行信息
     */
    @LogMessage(content = "新增资产证券化发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("新增资产证券化发行信息")
    public AjaxJson save(@RequestBody CmsSecurityIssueVO cmsSecurityIssueVO) {
        cmsSecurityIssueService.saveSecurityIssue(cmsSecurityIssueVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改资产证券化发行信息
     */
    @LogMessage(content = "修改资产证券化发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ApiOperation("修改资产证券化发行信息")
    public AjaxJson modify(@RequestBody CmsSecurityIssueVO cmsSecurityIssueVO) {
        cmsSecurityIssueService.modifySecurityIssue(cmsSecurityIssueVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除发行信息
     */
    @LogMessage(content = "删除发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除发行信息")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsSecurityIssueService.deleteIssueInfo(id, this.getCurrentUser());
        return AjaxJson.success();
    }

}