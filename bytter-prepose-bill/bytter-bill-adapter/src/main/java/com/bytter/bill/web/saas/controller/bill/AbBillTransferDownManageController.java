package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillTransferDownManageService;
import com.bytter.bill.domain.bill.service.AbBtCurrencyService;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownManageVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据下拨
 * @author onlineGenerator
 * @date 2017-07-19 09:43:45
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilltransferdownmanage", tags = "票据下拨")
@RestController("v1AbBillTransferDownManageController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilltransferdownmanage")
public class AbBillTransferDownManageController extends BaseController {
    @Autowired
    private AbBillTransferDownManageService abBillTransferDownManageService;
    @Autowired
    private AbBtCurrencyService abBtCurrencyService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;



    /**
     * 票据下拨单 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据下拨单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("查询票据下拨单分页列表")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownManageService.getDataTables(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据下拨单新增页面初始化
     */
    @GetMapping("/add")
    @ApiOperation("票据下拨单新增页面初始化")
    public AjaxJson<Map<String, Object>> add(AbBillTransfer abbilltransfer) {
        Map<String, Object> dataMap = new HashMap<>(6);
        String allocatedCode = SequenceSingleton.getSequence(SequenceEnum.BILL_TRANSFERDOWNMANAGE_NO, "XB");
        dataMap.put("allocatedCode", allocatedCode);
        dataMap.put("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dataMap.put("billCurrencyDefault", abBtCurrencyService.getRMBCurrency().getId());
        dataMap.put("forwardPath", "bill/accebill/addAbbilltransferdownmanage");
        return AjaxJson.success(dataMap);
    }

    /**
     * 票据下拨单编辑页面初始化
     */
    @GetMapping("/update/{aid}")
    @ApiOperation("票据下拨单编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(@PathVariable("aid") String aid) {
        Map<String, Object> dataMap = new HashMap<>(6);
        AbBillTransferDownManageVo vo = abBillTransferDownManageService.getDataById(aid);
        if (AccebillConstant.BRIEF_STATUS_6203.equals(vo.getBriefId())) {
            vo = abBillTransferDownManageService.toInit(vo);
        }
        dataMap.put("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dataMap.put("vo", vo);
        dataMap.put("forwardPath", "bill/accebill/updateAbbilltransferdownmanage");
        return AjaxJson.success(dataMap);
    }

    /**
     * 保存票据下拨单
     */
    @LogMessage(content = "保存票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save/{opt}")
    @ApiOperation("保存票据下拨单")
    public AjaxJson save(@PathVariable("opt") String opt, AbBillTransferDownManageVo abBillTransferDownManageVo) {
        abBillTransferDownManageService.save(abBillTransferDownManageVo, opt, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 提交票据下拨单
     */
    @LogMessage(content = "提交票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/submit/{opt}")
    @ApiOperation("提交票据下拨单")
    public AjaxJson submit(@PathVariable("opt") String opt, AbBillTransferDownManageVo abBillTransferDownManageVo) {
        abBillTransferDownManageService.submit(abBillTransferDownManageVo, opt, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量提交票据下拨单
     */
    @LogMessage(content = "批量提交票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量提交票据下拨单")
    public AjaxJson batchSubmit(AbBillTransferDownManageVo abBillTransferDownManageVo) {
        abBillTransferDownManageService.batchSubmit(abBillTransferDownManageVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除票据下拨单
     */
    @LogMessage(content = "删除票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{ids}")
    @ApiOperation("删除票据下拨单")
    public AjaxJson delete(@PathVariable("ids") String aid) {
        abBillTransferDownManageService.deleteAbBillTransfer(aid, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量删除票据下拨单
     */
    @LogMessage(content = "批量删除票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除票据下拨单")
    public AjaxJson batchDelete(String trids, String aids) {
        abBillTransferDownManageService.batchDelete(trids, aids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 票据下拨查看页面初始化
     */
    @GetMapping("/view")
    @ApiOperation("票据下拨查看页面初始化")
    public AjaxJson<Map<String, Object>> view(String businessId) {
        Map<String, Object> dataMap = new HashMap<>(6);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferDownManageVo vo = abBillTransferDownManageService.getAbBillTransferDownManageVoById(businessId);
            dataMap.put("info", vo);
        }
        dataMap.put("forwardPath", "bill/accebill/approve/viewApproveAbBill6204");
        return AjaxJson.success(dataMap);
    }



    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("票据下拨审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownManageService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("票据下拨审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferDownManageVo vo = abBillTransferDownManageService.getAbBillTransferDownManageVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillTransferDownManage");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("票据下拨移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferDownManageVo vo = abBillTransferDownManageService.getAbBillTransferDownManageVoById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
            AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(businessId);
            dataMap.put("attachmentInfo", attVo);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBill6204");
        return AjaxJson.success(dataMap);
    }

    /**
     * 根据提交的下拨申请单检查是否已经选择票据
     */
    @PostMapping("/checkSelectBill")
    @ApiOperation("检查下拨申请单是否已选择票据")
    public AjaxJson checkSelectBill(String trid) {
        boolean isSelectBill = abBillTransferDownManageService.checkSelectBill(trid);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("isSelectBill", isSelectBill);
        AjaxJson json = AjaxJson.success();
        json.setAttributes(attributes);
        return json;
    }
}