package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillEndorseRequestService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.domain.bill.vo.v1.ApproveVO;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 背书请求
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/bill/accebill/endorseRequest",tags = "背书请求")
@RestController("v1AbBillEndorseRequestController")
@RequestMapping("/v1-preposebill/bill/accebill/endorseRequest")
public class AbBillEndorseRequestController extends BaseController {
    @Autowired
    private AbBillEndorseRequestService abBillEndorseRequestService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 背书请求 table列表
     *
     * @param dataTablesInput 前端传参
     * @return 列表数据
     */
    @ApiOperation("背书请求 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<AbBillEndorseVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abBillEndorseRequestService.listAbBillEndorseRequest(input, this.getCurrentUser()));
    }
    /**
     * 背书请求
     *
     * @param eo vo
     * @return 是否请求成功
     */
    @ApiOperation("背书请求 ")
    @LogMessage(content = "背书请求", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/endorseRequest")
    @ResponseBody
    public AjaxJson endorseRequest(@RequestBody AbBillEndorseVo eo) {
        abBillEndorseRequestService.endorsementRequest(eo, this.getCurrentUser());
        return success();
    }


    @ApiOperation("背书请求（付款办理）")
    @LogMessage(content = "背书请求（付款办理）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/endorseRequestWithPay")
    @ResponseBody
    public AjaxJson endorseRequestWithPay(@RequestBody AbBillEndorseVo eo) {
        abBillEndorseRequestService.endorseRequestWithPay(eo, this.getCurrentUser());
        return success();
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 前端传参
     * @return 审批列表数据
     */
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abBillEndorseRequestService.dataTableApprove(input));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId id
     * @param taskId     审批id
     * @return 审批详情页
     */
    @ApiOperation("跳转至审核详情界面")
    @PostMapping("/approve")
    public AjaxJson<ApproveVO> approve(String businessId, String taskId) {
        ApproveVO model=new ApproveVO();
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillEndorseRequestService.getAbBillEndorseVoById(businessId);
            model.setInfo( info);
            model.setIsApprove(true);
            abBillAttachmentService.viewAttachmentV1(businessId, model);
            model.setBankTypeName( StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null);
        }
        // 必须要做此步骤
        model.setTaskId( taskId);
        // 背书挂接资金计划
        // 根据流水表预算科目是否为空来显示挂预算列表
        // 系统参数 背书挂接资金计划科目
        model.setEndorseItem( abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        model.setBusinessName(  BudgetConstant.BUSINESS_NAME_ENDORSE);
        model.setProcessSysname( BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model);
    }
}
