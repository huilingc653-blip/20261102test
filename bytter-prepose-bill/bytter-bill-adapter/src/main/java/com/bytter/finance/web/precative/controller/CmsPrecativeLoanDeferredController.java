package com.bytter.finance.web.precative.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanDeferred;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanDeferredService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanRepayService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanDeferredVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款展期申请表
 * @date 2017-08-03 10:48:22
 */
@Controller
@RequestMapping("/cms/precative/precativeloandeferred")
public class CmsPrecativeLoanDeferredController extends BaseController {
    @Autowired
    private CmsPrecativeLoanDeferredService cmsPrecativeLoanDeferredService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsPrecativeLoanRepayService cmsPrecativeLoanRepayService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 委托贷款展期申请跳转
     */
    @GetMapping("/listApply")
    public String list(Model model) {
        model.addAttribute("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return "cms/precative/loandeferred/listCmsPrecativeLoanDeferred";
    }
    /**
     * 委托贷款展期申请Table列表
     */
    @PostMapping("/dataTableApply")
    @ResponseBody
    public DataTablesOutput dataTableApply(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanDeferredService.queryApply(dataTablesInput,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_APPLY_QUERY, getUserId());
    }
    /**
     * 委托贷款展期申请页面
     */
    @GetMapping("Apply")
    public String Apply(String voucherNo, String sendNo, Model model) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(voucherNo);
        model.addAttribute("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(sendNo);
        model.addAttribute("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendNo);// 已还金额
        model.addAttribute("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendNo);// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        model.addAttribute("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return "cms/precative/loandeferred/addCmsPrecativeLoanDeferred";
    }
    /**
     * 委托贷款展期保存
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsPrecativeLoanDeferred pld) {
        cmsPrecativeLoanDeferredService.save(pld, getCurrentUser());
        return success();
    }
    /**
     * 委托贷款展期修改跳转
     */
    @GetMapping("/listupdate")
    public String listUpdate(Model model) {
        model.addAttribute("pageType", "listupdate");
        return "cms/precative/loandeferred/listDeferredUpdate";
    }
    /**
     * 委托贷款展期修改/打回Table列表
     */
    @PostMapping("/dataTableUpdate")
    @ResponseBody
    public DataTablesOutput dataTableUpdate(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        return cmsPrecativeLoanDeferredService.queryTable(dataTablesInput, pageType,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_UPDATE_QUERY, getUserId());
    }
    /**
     * 委托贷款展期修改/打回页面
     */
    @GetMapping("updatePage")
    public String updatePage(PrecativeLoanDeferredVo pre, Model model) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.addAttribute("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.addAttribute("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo()); // 已还金额
        model.addAttribute("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.addAttribute("deferred", deferred);
        model.addAttribute("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return "cms/precative/loandeferred/updateCmsPrecativeLoanDeferred";
    }
    /**
     * 委托贷款展期修改或保存
     */
    @PostMapping("/addUpdate")
    @ResponseBody
    public AjaxJson addUpdate(CmsPrecativeLoanDeferred pld) {
        cmsPrecativeLoanDeferredService.addUpdate(pld, getCurrentUser());
        return success();
    }
    /**
     * 委托贷款展期补录跳转
     */
    @GetMapping("/listAdditional")
    public String listAdditional(Model model) {
        model.addAttribute("pageType", "additional");
        return "cms/precative/loandeferred/listDeferredAdditional";
    }
    /**
     * 委托贷款展期补录Table表
     */
    @PostMapping("Additional")
    @ResponseBody
    public DataTablesOutput Additional(@RequestBody DataTablesInput input, String pageType) {
        return cmsPrecativeLoanDeferredService.queryTable(input, pageType,
                ResourceConstant.CMS_PRECATIVE_DEFERRED_ADDIT_QUERY, getUserId());
    }
    /**
     * 委托贷款展期补录提交
     */
    @GetMapping("queryAdditional")
    public String queryAdditional(PrecativeLoanDeferredVo pre, Model model) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.addAttribute("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.addAttribute("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo());// 已还金额
        model.addAttribute("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.addAttribute("deferred", deferred);
        model.addAttribute("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return "cms/precative/loandeferred/additionalCmsPrecativeLoanDeferred";
    }
    /**
     * 委托贷款展期详情页
     */
    // @LogMessage(content = "查询委托贷款展期申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String sendBillCode, String billCode, Model model) {
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(sendBillCode);
        model.addAttribute("send", send);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        model.addAttribute("info", info);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendBillCode);// 已还
        model.addAttribute("repayMoney", repayMoney);
        if (!EmptyUtils.isEmpty(billCode)) {
            CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(billCode);
            model.addAttribute("deferred", deferred);
        }
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendBillCode);// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        return "cms/precative/loandeferred/viewCmsPrecativeLoanDeferred";
    }
    /**
     * 委托贷款展期删除
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsPrecativeLoanDeferredService.deleteDef(id, getUserId());
        return success();
    }
    /**
     * 委贷展期处理跳转
     **/
    @GetMapping("/listHandle")
    public String listHandle(Model model) {
        return "cms/precative/loandeferred/listDeferredHandle";
    }
    /**
     * 委托贷款展期处理Table表
     */
    @PostMapping("queryHandle")
    @ResponseBody
    public DataTablesOutput queryHandle(@RequestBody DataTablesInput input) {
        return cmsPrecativeLoanDeferredService.queryHandle(input, ResourceConstant.CMS_PRECATIVE_DEFERRED_HANDLE_QUERY,
                getUserId());
    }
    /**
     * 委托贷款展期处理页面
     */
    @GetMapping("handle")
    public String handle(PrecativeLoanDeferredVo pre, Model model) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getVoucherNo());
        model.addAttribute("info", info);
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendNo());
        model.addAttribute("send", send);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(pre.getSendNo());// 已还金额
        model.addAttribute("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(pre.getSendNo());// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(pre.getBillCode());
        model.addAttribute("deferred", deferred);
        model.addAttribute("loanNum", configService.getConfig("cms", "LoanDfNum").getValue());
        return "cms/precative/loandeferred/handleCmsPrecativeLoanDeferred";
    }
    /**
     * 多次展期列表跳转
     */
    @GetMapping("/deferredInfolist")
    public String deferredInfolist(ProvideLoanDeferred provideLoanDeferred, Model model) {
        model.addAttribute("liCode", provideLoanDeferred.getLiCode());
        return "cms/precative/loandeferred/listPrecativeDeferredItem";
    }
    /**
     * 多次展期列表明细
     */
    @PostMapping("/deferredInfoItems")
    @ResponseBody
    public List<PrecativeLoanDeferredVo> deferredInfoItems(String liCode) {
        return cmsPrecativeLoanDeferredService.getPrecativeLoanSendListByBillCode(liCode);
    }
    /**
     * 委贷展期申请审核跳转
     **/
    @GetMapping("/listDeferredApprove")
    public String listDeferredApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/precative/loandeferred/listPrecativeDeferredApprove";
    }
    /**
     * 委贷展期申请审核列表
     **/
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableDeferredApprove")
    @ResponseBody
    public DataTablesOutput dataTableDeferredApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanDeferredService.dataTableApprove(dataTablesInput);
    }
    /**
     * 委贷展期补录审核跳转
     **/
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/precative/loandeferred/listPrecativeRepairApprove";
    }
    /**
     * 委贷展期补录审核列表
     **/
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableRepairApprove")
    @ResponseBody
    public DataTablesOutput dataTableRepairApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanDeferredService.dataTableRepairApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, String repairType, String billCode, Model model) {
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(businessId);
        model.addAttribute("send", send);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(send.getFatherCode());
        model.addAttribute("info", info);
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(businessId);// 已还
        model.addAttribute("repayMoney", repayMoney);
        BigDecimal passageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(businessId);// 在途金额
        model.addAttribute("passageMoney", passageMoney);
        if (!EmptyUtils.isEmpty(billCode)) {
            CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.getDeferredByBillCode(billCode);
            model.addAttribute("deferred", deferred);
        }
        model.addAttribute("type", "type");
        model.addAttribute("repairType", repairType);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/precative/loandeferred/approveViewPrecativeLoanDeferred";
    }
    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileDeferredApprove")
    public String mobileDeferredApprove(String businessId, String taskId, Model model) {
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.get(businessId);
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(deferred.getLiCode());
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendVo.getBillCode()); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendVo.getBillCode()); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.addAttribute("deferred", deferred);
        model.addAttribute("precativeSendPage", preVo);
        model.addAttribute("precativePage", preVo);
        model.addAttribute("PassageMoney", PassageMoney);
        model.addAttribute("repayMoney", repayMoney);
        model.addAttribute("loanMoney", loanMoney);
        model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.addAttribute("taskId", taskId);
        return "cms/precative/loandeferred/mobileDeferredApprove";
    }
    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileDeferredRepairApprove")
    public String mobileDeferredRepairApprove(String businessId, String taskId, Model model) {
        CmsPrecativeLoanDeferred deferred = cmsPrecativeLoanDeferredService.get(businessId);
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(deferred.getLiCode());
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendVo.getBillCode()); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendVo.getBillCode()); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.addAttribute("deferred", deferred);
        model.addAttribute("precativeSendPage", preVo);
        model.addAttribute("precativePage", preVo);
        model.addAttribute("PassageMoney", PassageMoney);
        model.addAttribute("repayMoney", repayMoney);
        model.addAttribute("loanMoney", loanMoney);
        model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.addAttribute("taskId", taskId);
        model.addAttribute("pageType", "repair");
        return "cms/precative/loandeferred/mobileDeferredApprove";
    }
}
