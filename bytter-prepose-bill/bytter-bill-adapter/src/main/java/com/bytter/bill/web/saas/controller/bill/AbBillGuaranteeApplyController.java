package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillGuaranteeApplyService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillGuaranteeApplyVO;
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

import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 提示保证处理
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillGuaranteeApply", tags = "提示保证处理")
@RestController("v1AbBillGuaranteeApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillGuaranteeApply")
public class AbBillGuaranteeApplyController extends BaseController {

    @Autowired
    private AbBillGuaranteeApplyService abBillGuaranteeApplyService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 提示保证处理 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @LogMessage(content = "查询提示保证处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("提示保证处理分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillGuaranteeApplyService.listAbBillGuaranteeApplyInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量申请操作
     */
    @PostMapping("/batchApply")
    @LogMessage(content = "批量保证申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    @ApiOperation("批量保证申请操作")
    public AjaxJson batchApply(@RequestBody AbBillGuaranteeApplyVO abBillGuaranteeApplyVO) {
        abBillGuaranteeApplyService.batchApply(abBillGuaranteeApplyVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 导入保证申请
     */
    @LogMessage(content = "导入保证申请操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ApiOperation("导入保证申请")
    public AjaxJson saveImport(@RequestBody List<AbBillGuaranteeApplyVO> list) {
        AjaxJson json = AjaxJson.success();
        boolean flag = abBillGuaranteeApplyService.saveImport(list, this.getCurrentUser());
        json.setSuccess(flag);
        return json;
    }

    /**
     * 审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("提示保证处理审核列表页面初始化")
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
    @ApiOperation("提示保证处理审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillGuaranteeApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("提示保证处理审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillGuaranteeApplyVO info = abBillGuaranteeApplyService.getAbBillGuaranteeApplyVOById(businessId);
            model.addAttribute("info", info);
            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()) : null);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("提示保证处理手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId ) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillGuaranteeApplyVO info = abBillGuaranteeApplyService.getAbBillGuaranteeApplyVOById(businessId);
            model.addAttribute("info", info);
        }
        return AjaxJson.success(model.asMap());
    }
}