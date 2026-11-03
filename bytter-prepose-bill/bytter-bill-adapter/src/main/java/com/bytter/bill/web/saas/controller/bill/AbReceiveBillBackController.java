package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbReceiveBillBackService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillBackVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.v1.AbReceiveBillBackApprovePageVO;
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

/**
 * @Title: Controller
 * @Description: 退票申请
 * @author onlineGenerator
 * @date 2017-06-29 16:53:43
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abreceivebillback", tags = "退票申请")
@RestController("v1AbReceiveBillBackController")
@RequestMapping("/v1-preposebill/bill/accebill/abreceivebillback")
public class AbReceiveBillBackController extends BaseController {

    @Autowired
    private AbReceiveBillBackService abReceiveBillBackService;

    /**
     * 退票申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询退票申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询退票申请分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abReceiveBillBackService.listAbReceiveBillBack(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 退票申请
     */
    @LogMessage(content = "退票申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/returnTicket")
    @ApiOperation("退票申请")
    public AjaxJson returnTicket(@RequestBody AbReceiveBillBackVo back) {
        abReceiveBillBackService.returnTicket(back, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 退票申请审核列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询退票申请审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询退票申请审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abReceiveBillBackService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 退票申请审核详情查询
     */
    @LogMessage(content = "查询退票申请审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/approve/{businessId}")
    @ApiOperation("查询退票申请审核详情")
    public AjaxJson<AbReceiveBillBackApprovePageVO> approve(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        AbReceiveBillBackApprovePageVO pageVO = new AbReceiveBillBackApprovePageVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbReceiveBillVO info = abReceiveBillBackService.getAbReceiveBillVoByAid(businessId);
            pageVO.setInfo(info);
            pageVO.setIsApprove(true);
        }
        pageVO.setTaskId(taskId);
        return AjaxJson.success(pageVO);
    }

    /**
     * 退票申请手机端审核详情查询
     */
    @LogMessage(content = "查询退票申请手机端审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/mobileApprove/{businessId}")
    @ApiOperation("查询退票申请手机端审核详情")
    public AjaxJson<AbReceiveBillBackApprovePageVO> mobileApprove(@PathVariable("businessId") String businessId, @RequestParam(required = false) String taskId) {
        AbReceiveBillBackApprovePageVO pageVO = new AbReceiveBillBackApprovePageVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbReceiveBillVO info = abReceiveBillBackService.getAbReceiveBillVoByAid(businessId);
            pageVO.setInfo(info);
            pageVO.setIsApprove(true);
        }
        pageVO.setTaskId(taskId);
        return AjaxJson.success(pageVO);
    }

}