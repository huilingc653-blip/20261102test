package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description:贴现办理
 * @author onlineGenerator
 * @date 2017-07-05 15:47:44
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abBillDiscountManager", tags = "贴现办理")
@RestController("v1AbBillDiscountManagerController")
@RequestMapping("/v1-preposebill/bill/accebill/abBillDiscountManager")
public class AbBillDiscountManagerController extends BaseController {
    @Autowired
    private AbBillDiscountManagerService abBillDiscountManagerService;
    @Autowired
    private AbCalendarService abCalendarService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;

    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private AbDiscountService abDiscountService;
    @Autowired
    private AbConfigService abConfigService;

    /**
     * 贴现办理页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("贴现办理列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
        Integer isDisThirdBillCredit = abRatUrationService.isDisThirdBillCredit();
        Integer isCredit = abRatUrationService.isRelateRat() == false ? 0 : 1;
        model.addAttribute("isCredit", isCredit);
        model.addAttribute("isDisCountCredit", isDisCountCredit);
        model.addAttribute("isDisThirdBillCredit", isDisThirdBillCredit);
        //不启用贴现申请功能直接贴现办理
        model.addAttribute("isNotDiscountApply", abVoucherinfoViceService.isNotDiscountApply());
        if (isDisCountCredit == 1) {
            model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
        }
        // 系统参数 是否挂资金计划
        String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
                .getValue();
        model.addAttribute("budgetSel", budgetSel);
        if ("1".equals(budgetSel)) {
            model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
            // 系统参数 贴现挂接资金计划科目
            model.addAttribute("discountItem", abConfigService
                    .getBillConfig(AccebillConstant.AB_CONFIG_DISCOUNTITEM).getValue());
        }
        return AjaxJson.success(model.asMap());
    }

    /**
     * 贴现办理列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("贴现办理分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountManagerService.listAbBillDiscountManager(input);
        return AjaxJson.success(output);
    }

    /**
     * 贴现办理提交
     */
    @LogMessage(content = "批量提交贴现办理信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量提交贴现办理信息")
    public AjaxJson batchSubmitAbBillDiscountManager(@RequestBody AbBillDiscountManagerVO abBillDiscountManagerVO) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillDiscountManagerService.batchSubmitAbBillDiscountManager(abBillDiscountManagerVO,
                this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 贴现申请打回
     */
    @LogMessage(content = "贴现申请打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchBack")
    @ApiOperation("贴现申请批量打回")
    public AjaxJson backAbBillDiscountManager(@RequestBody AbBillDiscountManagerVO abBillDiscountManagerVO) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillDiscountManagerService.batchBackAbBillDiscountManager(abBillDiscountManagerVO,
                this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 导入
     */
    @LogMessage(content = "导入应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ApiOperation("导入应收票据贴现办理信息")
    public AjaxJson saveImport(@RequestBody List<AbBillDiscountManagerVO> list) {
        AjaxJson json = AjaxJson.success();
        boolean flag = abBillDiscountManagerService.saveImport(list, this.getCurrentUser());
        json.setSuccess(flag);
        return json;
    }

    /**
     * 贴现办理审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("贴现办理审核列表页面初始化")
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
    @ApiOperation("贴现办理审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountManagerService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("贴现办理审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountManagerVO info = abBillDiscountManagerService.getAbBillDiscountManagerVOById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.addAttribute("billId", info.getVvid());
            AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(info.getRecordId());
            if (null != abDiscount && null != abDiscount.getOccupancyRatio()) {
                model.addAttribute("isEnabled", true);
            } else {
                model.addAttribute("isEnabled", false);
            }
        }
        model.addAttribute("taskId", taskId);
        // 贴现挂接资金计划
        // 根据流水表预算科目是否为空来显示挂预算列表
        // 系统参数 贴现挂接资金计划科目
        model.addAttribute("discountItem", abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_DISCOUNTITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_DISCOUNT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("贴现办理手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountManagerVO info = abBillDiscountManagerService.getAbBillDiscountManagerVOById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 获取顺延工作日
     */
    @PostMapping("/getDeferdate")
    @ApiOperation("获取顺延工作日")
    public AjaxJson getDeferdate(String endTime) {
        AjaxJson json = AjaxJson.success();
        Date date = abCalendarService.checkWorkDay(endTime, true);
        if (date == null) {
            json.setSuccess(false);
        } else {
            json.setSuccess(true);
            json.setObj(date);
        }
        return json;
    }
}