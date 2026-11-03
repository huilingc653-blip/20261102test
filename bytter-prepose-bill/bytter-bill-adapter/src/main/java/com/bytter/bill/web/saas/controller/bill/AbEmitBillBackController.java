package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbEmitBillBackService;
import com.bytter.bill.domain.bill.service.AbFcBondHookService;
import com.bytter.bill.domain.bill.vo.AbEmitBillBackVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 未用退票
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abemitbillback", tags = "未用退票")
@RestController("v1AbEmitBillBackController")
@RequestMapping("/v1-preposebill/bill/accebill/abemitbillback")
public class AbEmitBillBackController extends BaseController {
    @Autowired
    private AbEmitBillBackService abEmitBillBackService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;

    /**
     * 未用退票 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询未用退票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("未用退票分页列表")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillBackService.listAbEmitBillBack(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 确认退票
     */
    @LogMessage(content = "确认退票", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/returnTicket")
    @ApiOperation("确认退票操作")
    public AjaxJson returnTicket(@RequestBody AbEmitBillBackVo back) {
        abEmitBillBackService.returnTicket(back, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("未用退票审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillBackService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("未用退票审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(12);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillBackService.getAbEmitBillVoByAid(businessId);
            dataMap.put("abEmitBillVo", info);
            dataMap.put("isApprove", true);
            // 判断是否有已挂保证金数据
            String bailmodel = FcBondConstant.IS_HOOK_N;
            if (info != null) {
                FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(info.getVvid(), info.getAppNo());
                if (fcBondApplyDTO != null) {
                    bailmodel = FcBondConstant.IS_HOOK_Y;
                }
            }
            dataMap.put("bailmodel", bailmodel);
        }
        dataMap.put("taskId", taskId);
        // 票据挂预算初始化参数
        dataMap.put("budgetSel", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).getValue());
        dataMap.put("payItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITITEM).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbEmitBillBack");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机端审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("未用退票移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(6);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillBackService.getAbEmitBillVoByAid(businessId);
            dataMap.put("abEmitBillVo", info);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillEmit");
        return AjaxJson.success(dataMap);
    }
}