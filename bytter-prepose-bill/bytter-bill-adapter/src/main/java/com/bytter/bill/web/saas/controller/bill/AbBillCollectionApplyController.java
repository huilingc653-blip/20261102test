package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillCollectionApplyService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionApplyVo;
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
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 托收申请
 * @author onlineGenerator
 * @date 2017-07-26 15:30:56
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillcollectionapply", tags = "托收申请")
@RestController("v1AbBillCollectionApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillcollectionapply")
public class AbBillCollectionApplyController extends BaseController {
    @Autowired
    private AbBillCollectionApplyService abBillCollectionApplyService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 应收-票据托收-托收申请列表 页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("托收申请列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        Map<String,Object> model = new HashMap<>();
        // 系统参数 是否挂资金计划
        String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).getValue();
        model.put("budgetSel", budgetSel);
        if ("1".equals(budgetSel)) {
            model.put("curId", abVoucherinfoViceService.getCurIdByCNY());
            // 系统参数 托收挂接资金计划科目
            model.put("collectionItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_COLLECTIONITEM).getValue());
        }
        return AjaxJson.success(model );
    }

    /**
     * 应收-票据托收-托收申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询可托收申请的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询可托收申请的票据分页列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abBillCollectionApplyService.getDataTablesUnion(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 托收申请提交
     *
     * @param abBillCollectionApplyVo 托收申请参数
     */
    @LogMessage(content = "提交托收申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("提交托收申请")
    @PostMapping("/collectionApply")
    public AjaxJson collectionApply(@RequestBody AbBillCollectionApplyVo abBillCollectionApplyVo) {
        abBillCollectionApplyService.collectionApply(abBillCollectionApplyVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("托收申请审核分页列表查询")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillCollectionApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化数据
     */
    @GetMapping("/approve")
    @ApiOperation("托收申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillCollectionApplyVo vo = abBillCollectionApplyService.getAbBillCollectionApplyVoById(businessId, model);
            model.put("info", vo);
            model.put("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
        }
        model.put("taskId", taskId);
        // 托收挂接资金计划 系统参数 根据流水表预算科目是否为空来显示挂预算列表
        model.put("collectionItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_COLLECTIONITEM).getValue());
        model.put("businessName", BudgetConstant.BUSINESS_NAME_COLLECTION);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化数据
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("托收申请手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillCollectionApplyVo vo = abBillCollectionApplyService.getAbBillCollectionApplyVoById(businessId, model);
            model.put("info", vo);
            model.put("isApprove", true);
        }
        model.put("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }
}