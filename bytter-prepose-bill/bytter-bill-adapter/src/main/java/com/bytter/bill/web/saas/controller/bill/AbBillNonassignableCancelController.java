package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.domain.bill.vo.AbBillNonassignableCancelAppVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zouxs
 * @version 1.0
 * @Title: Controller
 * @Description: 不得转让撤销
 * @date 2023-02-24 14:28:10
 */
@Api(value = "/v1-preposebill/bill/accebill/nonassignableCancel", tags = "不得转让撤销")
@RestController("v1AbBillNonassignableCancelController")
@RequestMapping("/v1-preposebill/bill/accebill/nonassignableCancel")
public class AbBillNonassignableCancelController extends BaseController {
    //    @Autowired
//    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbBillNonassignableCancelService abBillNonassignableCancelService;
    @Autowired
    private AbReceiveBillService abReceiveBillService;
    @Autowired
    private AbDiscountService abDiscountService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;



    @PostMapping("/dataTable")
    @LogMessage(content = "不得转让撤销列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("不得转让撤销列表分页查询")
    public AjaxJson<DataTablesOutput> billReceDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillNonassignableCancelService.listAbVoucherinfoViceQuery(input);
        return AjaxJson.success(output);
    }

    /**
     * 查询不可转让票据详细信息
     * @param viceId
     * @param accId
     * @param isApprove
     * @param briefId
     * @param isShowPool
     * @return
     */
    @LogMessage(content = "查询不可转让票据详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    @ApiOperation("查询不可转让票据详细信息页面初始化")
    public AjaxJson<Map<String, Object>> view(String viceId, String accId, boolean isApprove, String briefId, boolean isShowPool) {
        ConcurrentModel model = new ConcurrentModel();
        String reVm = "bill/accebill/viewAbBillNonassignableCancel";
        if (StringUtils.isNotEmpty(accId)) {
            AbVoucherinfoVO billInfo = abReceiveBillService.getAbBillInfo(accId, briefId);
            if (!ObjectUtils.isEmpty(billInfo)) {
                if(StringUtils.isEmpty(viceId)){
                    viceId=billInfo.getVvid();
                }
                model.addAttribute("bankTypeName", StringUtils.isNotEmpty(viceId)?abBillNonassignableCancelService.findBankTypeNameWith(viceId):null);
                // 如果是签收业务跳转到签收页面详细页面
                if ((String.valueOf(billInfo.getBriefId()).startsWith("9") || String.valueOf(briefId).startsWith("9")
                        || AccebillConstant.BRIEF_STATUS_023.equals(billInfo.getBriefId())) && !AccebillConstant.BRIEF_STATUS_9004.equals(billInfo.getBriefId())) {
                    model.addAttribute("info", billInfo);
                    model.addAttribute("isShow", true);
                    // 系统参数
                    model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
                    model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
                    reVm = "bill/accebill/viewBusOpAbBillReceivebiz";
                } else {
                    model.addAttribute("billId", viceId);
                    model.addAttribute("abReceiveBillPage", billInfo);
                    AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(accId);
                    //判断是否挂接了银行贴现数据
                    if(null!=abDiscount && null!=abDiscount.getOccupancyRatio()){
                        model.addAttribute("isEnabled",true);
                    }else{
                        model.addAttribute("isEnabled",false);
                    }
                    //不启用贴现申请功能直接贴现办理
//                    boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
//                    model.addAttribute("isNotDiscountApply",isNotDiscountApply);
                }
//                // 挂预算系统参数
//                if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_6001)) {
//                    model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_6001));
//                    model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
//                    model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
//                    List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
//                            BudgetConstant.BUSINESS_NAME_REC, BudgetConstant.PROCESS_SYSNAME_AB, viceId);
//                    if (list != null && !list.isEmpty()) {
//                        // 显示预算
//                        model.addAttribute("budgetDisplay", "true");
//                    }
//                }
            }
            if (!isApprove) {
                List<AbLog> ablogList = abLogService.listAbLog(viceId, null);
                model.addAttribute("abLogPage", ablogList);
            }
            abBillAttachmentService.viewAttachment(accId, model);
        }
        model.addAttribute("isApprove", isApprove);
        model.addAttribute("isShowPool", isShowPool);
        model.addAttribute("forwardPath", reVm);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 不可转让撤销申请
     */
    @LogMessage(content = "不可转让撤销申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/nonassignableCancelApplication")
    @ApiOperation("不可转让撤销申请操作")
    public AjaxJson nonassignableCancelApplication(AbBillNonassignableCancelAppVo vo) {
        abBillNonassignableCancelService.nonassignableCancelApplication(vo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 不可转让撤销申请跳转至审核列表
     */
    @GetMapping("/listApprove")
    @ApiOperation("不可转让撤销审核列表页面初始化")
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
    @ApiOperation("不可转让撤销审核列表分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillNonassignableCancelService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 跳转至审核详情界面
     */
    @GetMapping("/approve")
    @ApiOperation("不可转让撤销审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillNonassignableCancelService.getAbBillEndorseVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abBillNonassignableCancelService.findBankTypeNameWith(info.getVvid()):null);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        model.addAttribute("forwardPath", "bill/accebill/approve/approveAbBillNonassignableCancel");
//        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
//        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return AjaxJson.success(model.asMap());
    }
}