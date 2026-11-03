package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillPledgeApplyService;
import com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 应收票据质押申请
 * @author zhenghj
 * @date 2017-07-07
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillpledgeapply", tags = "应收票据质押申请")
@RestController("v1AbBillPledgeApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillpledgeapply")
public class AbBillPledgeApplyController extends BaseController {
    @Autowired
    private AbBillPledgeApplyService abBillPledgeApplyService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;

    /**
     * 票据质押申请列表 页面初始化
     */
    @LogMessage(content = "查询质押申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("票据质押申请列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        return AjaxJson.success(model.asMap());
    }

    /**
     * 票据质押申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("票据质押申请列表分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeApplyService.listAbBillPledgeApply(input);
        return AjaxJson.success(output);
    }

    /**
     * 批量质押申请
     */
    @LogMessage(content = "批量质押申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchApply")
    @ApiOperation("批量质押申请")
    public AjaxJson batchApply(@RequestBody AbBillPledgeApplyVo abBillPledgeApplyVo) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillPledgeApplyService.batchApply(abBillPledgeApplyVo, AccebillConstant.BILL_OPERATE_SUBMIT,
                this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 根据单位id查询银行账号信息
     */
    @PostMapping("/getBankDataByCorpId")
    @ApiOperation("根据单位id查询银行账号信息")
    public AjaxJson<List<BtBankAccDTO>> getBankDataByCorpId(String corpId) {
        List<BtBankAccDTO> resultList = abBillPledgeApplyService.getBankDataByCorpId(corpId);
        return AjaxJson.success(resultList);
    }

    /**
     * 跳转至审核列表
     */
    @GetMapping("/listApprove")
    @ApiOperation("质押申请审核列表页面初始化")
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
    @ApiOperation("质押申请审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillPledgeApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("质押申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeApplyVo info = abBillPledgeApplyService.getAbBillPledgeApplyVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("bankTypeName", info.getBankTypeName());
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/approve/approveAbBillPledgeApply");
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机端审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("手机端质押申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillPledgeApplyVo info = abBillPledgeApplyService.getAbBillPledgeApplyVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/mobile/viewApproveAbBillBase");
        return AjaxJson.success(model.asMap());
    }

    /**
     * 保存导入数据
     */
    @PostMapping("/saveImport")
    @ApiOperation("质押申请导入")
    public AjaxJson saveImport(@RequestBody List<AbBillPledgeApplyVo> list) {
        this.abBillPledgeApplyService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }
}