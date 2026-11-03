package com.bytter.finance.saas.controller.directFinance;

import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.DfConstant;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepay;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceInfoService;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceRepayService;
import com.bytter.finance.domain.directFinance.vo.*;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-13 15:05
 * @Version 1.0
 */
@Api(value = "/v1-preposebill/cms/dfDirectFinanceRepay", tags = "直融还款")
@RestController("v1DfDirectFinanceRepayController")
@RequestMapping("/v1-preposebill/cms/dfDirectFinanceRepay")
public class DfDirectFinanceRepayController extends BaseController {

    @Autowired
    private DfDirectFinanceRepayService dfDirectFinanceRepayService;

    @Autowired
    private DfDirectFinanceInfoService dfDirectFinanceInfoService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    /**
     * 还款列表查询
     */
    @LogMessage(content = "查询直融还款列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询直融还款分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceRepayService.listDfDirectFinanceRepay(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 编辑打回列表查询
     */
    @LogMessage(content = "查询直融还款编辑打回列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/tempOrBack/datatable")
    @ApiOperation("查询直融还款编辑打回分页列表")
    public AjaxJson<DataTablesOutput> tempOrBackDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceRepayService.listTempOrback(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 还款审核列表查询
     */
    @LogMessage(content = "查询直融还款审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询直融还款审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceRepayService.listApprove(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 查询直融还款详情
     */
    @LogMessage(content = "查询直融还款详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询直融还款详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceRepayVO repayVO = dfDirectFinanceRepayService.getById(id);
        DfDirectFinanceInfoVO infoVO = dfDirectFinanceInfoService.getById(repayVO.getDirectFinanceId());
        dataMap.put("dfDirectFinanceRepayVO", repayVO);
        dataMap.put("dfDirectFinanceInfoVO", infoVO);
        dataMap.put("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(infoVO.getId()));
        // 查询关联数据
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", infoVO.getId());
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        // 资金计划系统参数
        dataMap.put("isBudget", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_DF_BUDGET).getValue());
        dataMap.put("dfRepayItem", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.DIRECT_FINANCE_REPAY_BUDGET).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_DF_REPAY);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询新增还款页面初始化数据
     */
    @LogMessage(content = "查询新增还款初始化数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/add/{directFinanceId}")
    @ApiOperation("查询新增还款初始化数据")
    public AjaxJson<Map<String, Object>> addInit(@PathVariable("directFinanceId") String directFinanceId) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceInfoVO infoVO = dfDirectFinanceInfoService.getById(directFinanceId);
        dataMap.put("dfDirectFinanceInfoVO", infoVO);
        dataMap.put("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(directFinanceId));
        // 查询关联数据
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", directFinanceId);
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        // 资金计划系统参数
        dataMap.put("isBudget", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_DF_BUDGET).getValue());
        dataMap.put("dfRepayItem", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.DIRECT_FINANCE_REPAY_BUDGET).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_DF_REPAY);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询已还金额
     */
    @LogMessage(content = "查询已还金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/repaidMoney/{directFinanceId}")
    @ApiOperation("查询已还金额")
    public AjaxJson<List<DfDirectFinanceRepayVO>> getRepaidMoney(@PathVariable("directFinanceId") String directFinanceId) {
        List<DfDirectFinanceRepayVO> list = dfDirectFinanceRepayService.listRepaidMoney(directFinanceId);
        return AjaxJson.success(list);
    }

    /**
     * 保存还款
     */
    @LogMessage(content = "保存直融还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存直融还款")
    public AjaxJson save(@RequestBody DfDirectFinanceRepayVO dfDirectFinanceRepayVO) {
        dfDirectFinanceRepayService.saveDfDirectFinanceRepay(dfDirectFinanceRepayVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除还款（逻辑删除）
     */
    @LogMessage(content = "删除直融还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除直融还款")
    public AjaxJson delete(@PathVariable("id") String id) {
        DfDirectFinanceRepay dfDirectFinanceRepay = dfDirectFinanceRepayService.get(id);
        dfDirectFinanceRepay.setStatus(DfConstant.DELETED);
        dfDirectFinanceRepayService.updateEntity(dfDirectFinanceRepay);
        // 删除资金计划
        dfDirectFinanceRepayService.deleteBudget(dfDirectFinanceRepay.getBillCode());
        return AjaxJson.success();
    }

    /**
     * 新增校验
     */
    @LogMessage(content = "直融还款新增校验", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/addCheck")
    @ApiOperation("直融还款新增校验")
    public AjaxJson addCheck(@RequestParam String directFinanceId) {
        dfDirectFinanceRepayService.addCheck(directFinanceId);
        return AjaxJson.success();
    }

    /**
     * 手机端审核详情查询
     */
    @LogMessage(content = "查询直融还款手机端审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/mobileApprove/{businessId}")
    @ApiOperation("查询直融还款手机端审核详情")
    public AjaxJson<Map<String, Object>> mobileApprove(@PathVariable("businessId") String businessId) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceRepayVO repayVO = dfDirectFinanceRepayService.getById(businessId);
        DfDirectFinanceInfoVO infoVO = dfDirectFinanceInfoService.getById(repayVO.getDirectFinanceId());
        dataMap.put("dfDirectFinanceRepayVO", repayVO);
        dataMap.put("dfDirectFinanceInfoVO", infoVO);
        dataMap.put("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(infoVO.getId()));
        // 查询关联数据
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", infoVO.getId());
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        return AjaxJson.success(dataMap);
    }

}