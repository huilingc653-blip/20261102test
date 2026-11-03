package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbClaimService;
import com.bytter.bill.domain.bill.vo.AbClaimMiddleVo;
import com.bytter.bill.domain.bill.vo.AbClaimVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 票据认领处理Controller
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/bill/accebill/abClaim", tags = "票据认领处理")
@RestController("v1AbClaimController")
@RequestMapping("/v1-preposebill/bill/accebill/abClaim")
public class AbClaimController extends BaseController {
    @Autowired
    private AbClaimService abClaimService;

    /**
     * 票据认领已确认列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableConfirmed")
    @ApiOperation("查询票据认领已确认列表")
    @LogMessage(content = "查询票据认领已确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTableConfirmed(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listAbClaimConfirmedInfo(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据认领已确认银行流水列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableConfirmedAcc")
    @ApiOperation("查询票据认领银行流水列表")
    @LogMessage(content = "查询票据认领银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> datatableConfirmedAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listAbClaimConfirmedInfoAcc(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据认领处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("查询票据认领处理列表")
    @LogMessage(content = "查询票据认领处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listAbClaimInfo(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据认领处理银行流水 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableAcc")
    @ApiOperation("查询票据认领处理银行流水列表")
    @LogMessage(content = "查询票据认领处理银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> datatableAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listAbClaimInfoAcc(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 待确认 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableWaitConfirm")
    @ApiOperation("查询待确认列表")
    @LogMessage(content = "查询待确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> datatableWaitConfirm(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listWaitConfirmInfo(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 待确认银行流水 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableWaitConfirmAcc")
    @ApiOperation("查询待确认银行流水列表")
    @LogMessage(content = "查询待确认银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> datatableWaitConfirmAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abClaimService.listAbClaimConfirmedInfoAcc(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 待人工认领页面初始化
     */
    @GetMapping("/listClaim")
    @ApiOperation("待人工认领页面初始化")
    public AjaxJson<Map<String, Object>> listClaim() {
        Map<String, Object> dataMap = new HashMap<>(6);
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", endDate);
        dataMap.put("forwardPath", "bill/accebill/listWaitManualClaim");
        return AjaxJson.success(dataMap);
    }

    /**
     * 待确认页面初始化
     */
    @GetMapping("/listWaitConfirm")
    @ApiOperation("待确认页面初始化")
    public AjaxJson<Map<String, Object>> listWaitConfirm() {
        Map<String, Object> dataMap = new HashMap<>(6);
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", endDate);
        dataMap.put("forwardPath", "bill/accebill/listWaitConfirm");
        return AjaxJson.success(dataMap);
    }

    /**
     * 已确认页面初始化
     */
    @GetMapping("/listConfirmed")
    @ApiOperation("已确认页面初始化")
    public AjaxJson<Map<String, Object>> listConfirmed() {
        Map<String, Object> dataMap = new HashMap<>(6);
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", endDate);
        dataMap.put("forwardPath", "bill/accebill/listConfirmedClaim");
        return AjaxJson.success(dataMap);
    }




    /**
     * 提交认领
     */
    @PostMapping("/submitClaim")
    @ApiOperation("提交认领")
    @LogMessage(content = "提交认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson batchApply(@RequestBody AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.submitClaim(abClaimMiddleVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 转认领
     */
    @PostMapping("/transferClaim")
    @ApiOperation("转认领")
    @LogMessage(content = "转认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson transferClaim(@RequestBody AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.transferClaim(abClaimMiddleVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 待确认转提交确认
     */
    @PostMapping("/submitConfirm")
    @ApiOperation("待确认转提交确认")
    @LogMessage(content = "待确认转提交确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson submitConfirm(@RequestBody AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.submitConfirm(abClaimMiddleVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 待确认转待认领
     */
    @PostMapping("/confirmToClaim")
    @ApiOperation("待确认转待认领")
    @LogMessage(content = "待确认转待认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson confirmToClaim(@RequestBody AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.confirmToClaim(abClaimMiddleVo);
        return AjaxJson.success();
    }

    /**
     * 获取银行交易明细查询配置信息
     */
    @PostMapping("/getBankAccDtlQueryConfig")
    @ApiOperation("获取银行交易明细查询配置信息")
    public AjaxJson getBankAccDtlQueryConfig(@RequestBody AbClaimVo vo) {
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(abClaimService.getBankAccDtlQueryConfig(vo));
        return ajaxJson;
    }
}
