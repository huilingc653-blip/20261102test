package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillRecallService;
import com.bytter.bill.domain.bill.vo.AbBillRecallVO;
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
 * @Description: 通用撤回处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillRecall", tags = "通用撤回处理")
@RestController("v1AbBillRecallController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillRecall")
public class AbBillRecallController extends BaseController {
    @Autowired
    private AbBillRecallService abBillRecallService;

    /**
     * 通用撤回处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询通用撤回处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("通用撤回处理列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillRecallService.listAbBillRecallInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量申请操作
     */
    @LogMessage(content = "批量通用撤回申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchApply")
    @ApiOperation("批量通用撤回申请操作")
    public AjaxJson batchApply(@RequestBody AbBillRecallVO abBillRecallVO) {
        AjaxJson j = AjaxJson.success();
        abBillRecallService.batchApply(abBillRecallVO, this.getCurrentUser());
        return j;
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("撤回申请审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillRecallService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("撤回申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillRecallVO info = abBillRecallService.getAbBillRecallVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("bankTypeName", info.getBankTypeName());
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);// 必须要做此步骤
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillRecall");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机端审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端撤回申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillRecallVO info = abBillRecallService.getAbBillRecallVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);// 必须要做此步骤
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(dataMap);
    }
}