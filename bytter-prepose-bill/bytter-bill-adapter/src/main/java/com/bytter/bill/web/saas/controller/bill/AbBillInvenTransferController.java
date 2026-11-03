package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillInvenTransferService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillInvenTransferVO;
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
 * @Description: 票据移存
 * @author zhenghj
 * @date 2017-06-29 10:34:22
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillinventransfer", tags = "票据移存")
@RestController("v1AbBillInvenTransferController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillinventransfer")
public class AbBillInvenTransferController extends BaseController {
    @Autowired
    private AbBillInvenTransferService abBillInvenTransferService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 票据移存列表 页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("票据移存列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        return AjaxJson.success(model.asMap());
    }

    /**
     * 票据移存 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据移存分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInvenTransferService.listAbBillInvenTransfer(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量移存
     */
    @LogMessage(content = "批量移存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchChangeEnterStock")
    @ApiOperation("批量移存操作")
    public AjaxJson batchChangeEnterStock(@RequestBody AbBillInvenTransferVO abBillInvenTransferVO) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillInvenTransferService.batchChangeEnterStock(abBillInvenTransferVO, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("票据移存审核列表页面初始化")
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
    @ApiOperation("票据移存审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillInvenTransferService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据移存审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillInvenTransferVO info = abBillInvenTransferService.getAbBillInvenTransferVOById(businessId);
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
    @ApiOperation("票据移存手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillInvenTransferVO info = abBillInvenTransferService.getAbBillInvenTransferVOById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}