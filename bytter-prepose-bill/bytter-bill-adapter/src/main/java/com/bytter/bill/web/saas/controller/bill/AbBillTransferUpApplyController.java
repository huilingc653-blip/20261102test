package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillTransferUpApplyService;
import com.bytter.bill.domain.bill.vo.AbBillTransferUpApplyVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilltransferupapply", tags = "应收-票据划拨-票据上划")
@RestController("v1AbBillTransferUpApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilltransferupapply")
public class AbBillTransferUpApplyController extends BaseController {
    @Autowired
    private AbBillTransferUpApplyService abBillTransferUpApplyService;



    /**
     * 应收-票据划拨-票据上划 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询可票据上划的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("查询可票据上划的票据分页数据")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abBillTransferUpApplyService.getDataTablesUnion(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据上划
     */
    @LogMessage(content = "票据上划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/transferup")
    @ApiOperation("执行票据上划操作")
    public AjaxJson transferup(AbBillTransferUpApplyVo abBillTransferUpApplyVo) {
        boolean result = abBillTransferUpApplyService.transferup(abBillTransferUpApplyVo, this.getCurrentUser());
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setSuccess(result);
        return ajaxJson;
    }



    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据上划审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferUpApplyService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据上划审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferUpApplyVo vo = abBillTransferUpApplyService.getAbBillTransferUpApplyVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillTransferUpApply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("票据上划移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferUpApplyVo vo = abBillTransferUpApplyService.getAbBillTransferUpApplyVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(dataMap);
    }
}