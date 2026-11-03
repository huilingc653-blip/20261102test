package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillTransferDownApplyService;
import com.bytter.bill.domain.bill.service.AbBtCurrencyService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownApplyVo;
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
import com.bytter.system.api.dto.CorpDTO;
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
 * @Description: 下拨申请单
 * @author onlineGenerator
 * @date 2017-07-17 14:43:45
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbilltransferdownapply", tags = "下拨申请单")
@RestController("v1AbBillTransferDownApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbilltransferdownapply")
public class AbBillTransferDownApplyController extends BaseController {
    @Autowired
    private AbBillTransferDownApplyService abBillTransferDownApplyService;
    @Autowired
    private AbBtCurrencyService abBtCurrencyService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbCorpService abCorpService;



    /**
     * 下拨申请单 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询下拨申请单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("查询下拨申请单分页列表")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownApplyService.getDataTables(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 下拨申请单新增页面初始化
     */
    @GetMapping("/add")
    @ApiOperation("下拨申请单新增页面初始化")
    public AjaxJson<Map<String, Object>> add() {
        Map<String, Object> dataMap = new HashMap<>(6);
        String allocatedCode = SequenceSingleton.getSequence(SequenceEnum.BILL_TRANSFERDOWNAPPLY_NO, "SQ");
        dataMap.put("allocatedCode", allocatedCode);
        dataMap.put("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dataMap.put("billCurrencyDefault", abBtCurrencyService.getRMBCurrency().getId());
        dataMap.put("forwardPath", "bill/accebill/addAbbilltransferdownapply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 下拨申请单编辑页面初始化
     */
    @GetMapping("/update/{aid}")
    @ApiOperation("下拨申请单编辑页面初始化")
    public AjaxJson<Map<String, Object>> update(@PathVariable("aid") String aid) {
        Map<String, Object> dataMap = new HashMap<>(6);
        AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(aid);
        dataMap.put("abBillTransferDownApplyVo", vo);
        dataMap.put("forwardPath", "bill/accebill/updateAbbilltransferdownapply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 保存下拨申请单
     */
    @LogMessage(content = "保存下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save/{opt}")
    @ApiOperation("保存下拨申请单")
    public AjaxJson save(@PathVariable("opt") String opt, AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
        abBillTransferDownApplyService.save(abBillTransferDownApplyVo, opt, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 提交下拨申请单
     */
    @LogMessage(content = "提交下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/submit/{opt}")
    @ApiOperation("提交下拨申请单")
    public AjaxJson submit(@PathVariable("opt") String opt, AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
        abBillTransferDownApplyService.submit(abBillTransferDownApplyVo, opt, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量提交下拨申请单
     */
    @LogMessage(content = "批量提交下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量提交下拨申请单")
    public AjaxJson batchSubmit(@RequestBody AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
        abBillTransferDownApplyService.batchSubmit(abBillTransferDownApplyVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除下拨申请单--物理删除
     */
    @LogMessage(content = "删除下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{ids}")
    @ApiOperation("删除下拨申请单")
    public AjaxJson delete(@PathVariable("ids") String ids) {
        abBillTransferDownApplyService.deleteAbBillTransfer(ids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量删除下拨申请单
     */
    @LogMessage(content = "批量删除下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除下拨申请单")
    public AjaxJson batchDelete(String trids, String aids) {
        abBillTransferDownApplyService.batchDelete(trids, aids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("下拨申请单审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> dataMap = new HashMap<>(6);
        dataMap.put("flowStatus", flowStatus);
        dataMap.put("cycleTime", cycleTime);
        dataMap.put("forwardPath", "bill/accebill/approve/listAbbilltransferdownapplyApprove");
        return AjaxJson.success(dataMap);
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("下拨申请单审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownApplyService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 跳转查看详情界面
     */
    @GetMapping("/view")
    @ApiOperation("下拨申请单查看页面初始化")
    public AjaxJson<Map<String, Object>> view(String aid) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(aid)) {
            AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(aid);
            dataMap.put("info", vo);
            AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(aid);
            dataMap.put("attachmentInfo", attVo);
        }
        dataMap.put("forwardPath", "bill/accebill/approve/viewApproveAbBill6203");
        return AjaxJson.success(dataMap);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("下拨申请单审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(10);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
            AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(businessId);
            dataMap.put("attachmentInfo", attVo);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbBillTransferDownApply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("下拨申请单移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(businessId);
            dataMap.put("info", vo);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBill6203");
        return AjaxJson.success(dataMap);
    }

    /**
     * 通过单位id获取单位信息
     */
    @PostMapping("/getSysCorpById/{id}")
    @ApiOperation("根据单位ID获取单位信息")
    public AjaxJson getSysCorpById(@PathVariable("id") String id) {
        CorpDTO corpDTO = abCorpService.get(id);
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(corpDTO);
        return ajaxJson;
    }
}