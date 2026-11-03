package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillDeleteService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoService;
import com.bytter.bill.domain.bill.vo.AbBillDeleteVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
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
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 票据删除
 * @date 2020-06-01
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilldelete", tags = "票据删除")
@RestController("v1AbBillDeleteController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilldelete")
public class AbBillDeleteController extends BaseController {
    @Autowired
    private AbBillDeleteService abBillDeleteService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;



    /**
     * 票据删除分页列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据删除分页数据查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDeleteService.listAbBillDelete(input);
        return AjaxJson.success(output);
    }

    /**
     * 票据删除提交
     */
    @LogMessage(content = "票据删除提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteConfirm")
    @ApiOperation("执行票据删除操作")
    public AjaxJson deleteConfirm(@RequestBody AbBillDeleteVO deleteVO) {
        abBillDeleteService.deleteConfirm(deleteVO, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 票据删除审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("票据删除审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        ConcurrentModel model = new ConcurrentModel();
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 票据删除审核分页列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据删除审核分页数据查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDeleteService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 票据删除审核详情页面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据删除审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(vouInfo.getSourceType())) {
                AbReceiveBillVO info = abBillDeleteService.getAbReceiveBillVoByAid(businessId);
                model.addAttribute("abReceiveBillPage", info);
                abBillAttachmentService.viewAttachment(businessId, model);
            } else {
                AbEmitBillVo info = abBillDeleteService.getAbEmitBillVoByAid(businessId);
                model.addAttribute("abEmitBillVo", info);
            }
            model.addAttribute("type", vouInfo.getSourceType());
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 票据删除手机端审核详情页面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("票据删除手机审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(vouInfo.getSourceType())) {
                AbReceiveBillVO info = abBillDeleteService.getAbReceiveBillVoByAid(businessId);
                model.addAttribute("info", info);
            } else {
                AbEmitBillVo info = abBillDeleteService.getAbEmitBillVoByAid(businessId);
                model.addAttribute("abEmitBillVo", info);
            }
            model.addAttribute("type", vouInfo.getSourceType());
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}