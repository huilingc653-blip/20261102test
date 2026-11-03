package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillAcceptApplyService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillAcceptApplyVO;
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
 * @Description: 提示承兑处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillAcceptApply", tags = "提示承兑处理")
@RestController("v1AbBillAcceptApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillAcceptApply")
public class AbBillAcceptApplyController extends BaseController {

    @Autowired
    private AbBillAcceptApplyService abBillAcceptApplyService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 提示承兑处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询提示承兑处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询提示承兑处理分页列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillAcceptApplyService.listAbBillAcceptApplyInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量申请操作
     */
    @LogMessage(content = "批量承兑申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @ApiOperation("批量承兑申请操作")
    @PostMapping("/batchApply")
    public AjaxJson batchApply(@RequestBody AbBillAcceptApplyVO abBillAcceptApplyVO) {
        abBillAcceptApplyService.batchApply(abBillAcceptApplyVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 审核列表页面初始化
     */
    @ApiOperation("审核列表页面初始化参数")
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("flowStatus", flowStatus);
        resultMap.put("cycleTime", cycleTime);
        return AjaxJson.success(resultMap);
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("审核分页列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillAcceptApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化数据
     */
    @ApiOperation("审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillAcceptApplyVO info = abBillAcceptApplyService.getAbBillAcceptApplyVOById(businessId);
            resultMap.put("info", info);
            String bankTypeName = StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null;
            resultMap.put("bankTypeName", bankTypeName);
            resultMap.put("isApprove", true);
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }

    /**
     * 手机审核详情界面初始化数据
     */
    @ApiOperation("手机端审核详情初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillAcceptApplyVO info = abBillAcceptApplyService.getAbBillAcceptApplyVOById(businessId);
            resultMap.put("info", info);
            resultMap.put("isApprove", true);
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }
}