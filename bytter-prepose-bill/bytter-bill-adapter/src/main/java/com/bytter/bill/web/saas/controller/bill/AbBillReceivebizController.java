package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillReceivebizService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbDraftExcService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据签收信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillreceivebiz", tags = "票据签收信息")
@RestController("v1AbBillReceivebizController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillreceivebiz")
public class AbBillReceivebizController extends BaseController {
    @Autowired
    private AbBillReceivebizService abBillReceivebizService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbDraftExcService abDraftExcService;

    /**
     * 票据签收信息 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据签收信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("票据签收信息列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillReceivebizService.listAbBillReceivebizInfo(input);
        return AjaxJson.success(output);
    }

    /**
     * 票据签收详细信息页面初始化
     */
    @GetMapping("/businessDtlView")
    @ApiOperation("票据签收详细信息页面初始化")
    public AjaxJson<Map<String, Object>> businessDtlView(AbBillReceivebizVO abBillReceivebizVO) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(abBillReceivebizVO.getId())) {
            AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(abBillReceivebizVO.getId());
            dataMap.put("info", info);
            String bankTypeName = StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null;
            dataMap.put("bankTypeName", bankTypeName);
        }
        // 系统参数
        dataMap.put("recItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_REC);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("curId", abVoucherinfoViceService.getCurIdByCNY());
        dataMap.put("isSign", "1");
        dataMap.put("forwardPath", "bill/accebill/busOpAbBillReceivebiz");
        return AjaxJson.success(dataMap);
    }

    /**
     * 签收同意操作
     */
    @LogMessage(content = "签收同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/consent")
    @ApiOperation("签收同意操作")
    public AjaxJson consent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
        AjaxJson j = AjaxJson.success();
        abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
        abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
        return j;
    }

    /**
     * 签收不同意操作
     */
    @LogMessage(content = "签收不同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/noConsent")
    @ApiOperation("签收不同意操作")
    public AjaxJson noConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
        AjaxJson j = AjaxJson.success();
        abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_N);
        abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
        return j;
    }

    /**
     * 签收批量同意操作
     */
    @LogMessage(content = "签收批量同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchConsent")
    @ApiOperation("签收批量同意操作")
    public AjaxJson batchConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
        AjaxJson j = AjaxJson.success();
        abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
        abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
        return j;
    }

    /**
     * 签收批量不同意操作
     */
    @LogMessage(content = "签收批量不同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchNoConsent")
    @ApiOperation("签收批量不同意操作")
    public AjaxJson batchNoConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
        AjaxJson j = AjaxJson.success();
        abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_N);
        abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
        return j;
    }

    /**
     * 批量已撤回操作
     */
    @LogMessage(content = "批量已撤回操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchRecall")
    @ApiOperation("批量已撤回操作")
    public AjaxJson batchRecall(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
        AjaxJson j = AjaxJson.success();
        abBillReceivebizService.billRecall(abBillReceivebizVO, this.getCurrentUser());
        return j;
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据签收审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillReceivebizService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据签收审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("isApprove", true);
            String bankTypeName = StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null;
            dataMap.put("bankTypeName", bankTypeName);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_REC);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillReceivebiz");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机端审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端票据签收审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(businessId);
            dataMap.put("info", info);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_REC);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("forwardPath", "bill/accebill/mobile/approveAbBillReceivebiz");
        return AjaxJson.success(dataMap);
    }

    /**
     * 判断单位是否为同一网点下
     */
    @PostMapping("/isTheSameNetByCorpId")
    @ApiOperation("判断单位是否为同一网点下")
    public AjaxJson isTheSameNetByCorpId(String corpId1, String corpId2) {
        AjaxJson json = AjaxJson.success();
        boolean flag = abBillReceivebizService.isTheSameNetByCorpId(corpId1, corpId2);
        json.setSuccess(flag);
        return json;
    }

    /**
     * 支票收款信用详情页初始化
     */
    @GetMapping("/playPage")
    @ApiOperation("支票收款信用详情页初始化")
    public AjaxJson<Map<String, Object>> playPage(String billCode, String acctId, String recId, String msgType) {
        Map<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("billCode", billCode);
        dataMap.put("acctId", acctId);
        dataMap.put("recId", recId);
        dataMap.put("msgType", msgType);
        dataMap.put("paymentCredit", abDraftExcService.getAbPaymentCreditVoByOrigNo(billCode));
        dataMap.put("forwardPath", "bill/accebill/waitSignRequest");
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询信用信息
     */
    @PostMapping("/fastCredit")
    @ApiOperation("查询信用信息")
    public AjaxJson fastCredit(String billCode, String acctId, String acId, String msgType, String recId) {
        if (StringUtils.isNotEmpty(billCode)) {
            billCode = billCode.replace(" ", "");
        }
        if (StringUtils.isNotEmpty(recId)) {
            return abDraftExcService.creditInformationSign(billCode, acctId, acId, recId, msgType);
        } else if (StringUtils.isNotEmpty(acId)
                && (acId.indexOf(",") != -1
                || (StringUtils.isEmpty(billCode) && StringUtils.isEmpty(acctId)))) {
            String[] acIds = acId.split(",");
            for (String id : acIds) {
                if (StringUtil.isNotEmpty(id)) {
                    abDraftExcService.creditInformation(billCode, acctId, id, msgType);
                }
            }
            AjaxJson json = new AjaxJson();
            json.setSuccess(true);
            Map<String, Object> messMsg = new HashMap<>();
            messMsg.put("MSG_INFO", AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG);
            json.setAttributes(messMsg);
            return json;
        } else {
            return abDraftExcService.creditInformation(billCode, acctId, acId, msgType);
        }
    }

    /**
     * 批量查询正背面信息
     */
    @PostMapping("/batchBankSyncroBillInfo")
    @ApiOperation("批量查询正背面信息")
    public AjaxJson batchBankSyncroBillInfo(String acId, String type) {
        String[] acIds = acId.split(",");
        if (AccebillConstant.MSG_TYPE_BILLINFO.equals(type)) {
            for (String id : acIds) {
                if (StringUtil.isNotEmpty(id)) {
                    abDraftExcService.creditInformation(null, null, id, AccebillConstant.MSG_TYPE_BILLINFO);
                }
            }
        } else if (AccebillConstant.MSG_TYPE_BACKINFO.equals(type)) {
            for (String id : acIds) {
                if (StringUtil.isNotEmpty(id)) {
                    abDraftExcService.creditInformation(null, null, id, AccebillConstant.MSG_TYPE_BACKINFO);
                }
            }
        }
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        Map<String, Object> messMsg = new HashMap<>();
        messMsg.put("MSG_INFO", AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG);
        json.setAttributes(messMsg);
        return json;
    }

    /**
     * 批量发送指令
     */
    @PostMapping("/sendCommand")
    @ApiOperation("批量发送指令")
    public AjaxJson sendCommand(String payerAcc, String msgType, String billType, String signType) {
        return abBillReceivebizService.sendCommand(payerAcc, msgType, billType, signType);
    }
}