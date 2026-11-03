package com.bytter.bill.web.bill.controller;
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
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author zouxs
 * @version 1.0
 * @Title: Controller
 * @Description: 不得转让撤销
 * @date 2023-02-24 14:28:10
 */
@Controller
@RequestMapping("/bill/accebill/nonassignableCancel")
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
    /**
     * 不得转让撤销 页面跳转
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listNonassignableCancel";
    }
    @PostMapping("/dataTable")
    @ResponseBody
    @LogMessage(content = "不得转让撤销列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput billReceDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillNonassignableCancelService.listAbVoucherinfoViceQuery(dataTablesInput);
    }
    /**
     * 查询不可转让票据详细信息
     * @param viceId
     * @param model
     * @return
     */
    @LogMessage(content = "查询不可转让票据详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String viceId, String accId, boolean isApprove, String briefId, Model model, boolean isShowPool) {
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
        return reVm;
    }
    /**
     * 不可转让撤销申请
     */
    @LogMessage(content = "不可转让撤销申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/nonassignableCancelApplication")
    @ResponseBody
    public AjaxJson nonassignableCancelApplication(AbBillNonassignableCancelAppVo vo) {
        abBillNonassignableCancelService.nonassignableCancelApplication(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 不可转让撤销申请跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bill/accebill/approve/listAbBillNonassignableCancelApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return abBillNonassignableCancelService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        // model.addAttribute("entity", 根据businessId查询到需要展示的数据);
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillNonassignableCancelService.getAbBillEndorseVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abBillNonassignableCancelService.findBankTypeNameWith(info.getVvid()):null);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
//        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
//        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return "bill/accebill/approve/approveAbBillNonassignableCancel";
    }
}
