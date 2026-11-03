package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillEndorseResponseService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 背书请求应答
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/bill/accebill/endorseResponse", tags = "背书请求应答")
@RestController("v1AbBillEndorseResponseController")
@RequestMapping("/v1-preposebill/bill/accebill/endorseResponse")
public class AbBillEndorseResponseController extends BaseController {
    @Autowired
    private AbBillEndorseResponseService abBillEndorseResponseService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;


    /**
     * 背书请求应答 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("背书请求应答分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillEndorseResponseService.listAbBillEndorseResponse(input, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 背书请求应答
     */
    @LogMessage(content = "背书请求应答", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/endorseResponse")
    @ApiOperation("执行背书请求应答")
    public AjaxJson endorseResponse(@RequestBody AbBillEndorseVo eo) {
        abBillEndorseResponseService.endorseResponse(eo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("背书请求应答审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String,Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("背书请求应答审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillEndorseResponseService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("背书请求应答审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillEndorseResponseService.getAbBillEndorseVoById(businessId);
            model.put("info", info);
            model.put("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.put("bankTypeName", StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null);
        }
        model.put("taskId", taskId);
        // 背书挂接资金计划
        model.put("endorseItem", abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        model.put("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }
}