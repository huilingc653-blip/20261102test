package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillSearchforApplyService;
import com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO;
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
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 追索通知处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillSearchforApply", tags = "追索通知处理")
@RestController("v1AbBillSearchforApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillSearchforApply")
public class AbBillSearchforApplyController extends BaseController {
    @Autowired
    private AbBillSearchforApplyService abBillSearchforApplyService;

    /**
     * 追索通知处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询追索通知处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("追索通知处理列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillSearchforApplyService.listAbBillSearchforApplyInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量申请操作
     */
    @LogMessage(content = "批量追索通知申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchApply")
    @ApiOperation("批量追索通知申请操作")
    public AjaxJson batchApply(@RequestBody AbBillSearchforApplyVO abBillSearchforApplyVO) {
        AjaxJson j = AjaxJson.success();
        abBillSearchforApplyService.batchApply(abBillSearchforApplyVO, this.getCurrentUser());
        return j;
    }

    /**
     * 导入追索通知申请
     */
    @LogMessage(content = "导入追索通知申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ApiOperation("导入追索通知申请")
    public AjaxJson saveImport(@RequestBody List<AbBillSearchforApplyVO> list) {
        AjaxJson json = AjaxJson.success();
        boolean flag = abBillSearchforApplyService.saveImport(list, this.getCurrentUser());
        json.setSuccess(flag);
        return json;
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("追索通知审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillSearchforApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("追索通知审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillSearchforApplyVO info = abBillSearchforApplyService.getAbBillSearchforApplyVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("bankTypeName", info.getBankTypeName());
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillSearchforApply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机端审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端追索通知审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillSearchforApplyVO info = abBillSearchforApplyService.getAbBillSearchforApplyVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(dataMap);
    }
}