package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillInvenFetchService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillInvenFetchVo;
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
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Title: Controller
 * @Description: 应收票据取票
 * @author onlineGenerator
 * @date 2017-07-05 18:17:47
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillinvenfetch", tags = "应收票据取票")
@RestController("v1AbBillInvenFetchController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillinvenfetch")
public class AbBillInvenFetchController extends BaseController {
    @Autowired
    private AbBillInvenFetchService abBillInvenFetchService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;



    /**
     * 应收票据取票 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("应收票据取票分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInvenFetchService.listAbBillInvenFetch(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量取票
     */
    @LogMessage(content = "批量取票", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchChangeEnterStock")
    @ApiOperation("批量取票操作")
    public AjaxJson batchChangeEnterStock(@RequestBody AbBillInvenFetchVo abBillInvenFetchVo) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillInvenFetchService.batchChangeEnterStock(abBillInvenFetchVo, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("应收票据取票审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        ConcurrentModel model = new ConcurrentModel();
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("应收票据取票审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInvenFetchService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("应收票据取票审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillInvenFetchVo info = abBillInvenFetchService.getAbBillInvenFetchVoById(businessId);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getViceId()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getViceId()) : null);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("应收票据取票手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillInvenFetchVo info = abBillInvenFetchService.getAbBillInvenFetchVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}