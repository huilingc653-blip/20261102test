package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpLogService;
import com.bytter.biz.ttpay.service.TpRefundOrderApplyService;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillDtlVO;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: tpRefundOrderApplyController
 * @Description: 退款申请
 * @author wangzy
 * @date 2019/10/10 10:26
 *
 */
@Controller
@RequestMapping("/ttpay/refundOrderApply")
public class TpRefundOrderApplyController extends BaseController {
    @Autowired
    private TpRefundOrderApplyService tpRefundOrderApplyService;
    @Autowired
    private TpLogService tpLogService;
    /**
     * 退款申请页面跳转
     * @param model
     * @return
     */
    @GetMapping("/refundApply")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDAPPLY_OP)
    public String listRefundApply(Model model) {
        model.addAttribute("trandTimeStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("trandTimeEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "ttpay/listTpRefundOrderApply";
    }
    /**
     * 退款申请列表数据获取
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDAPPLY_OP)
    @LogMessage(content = "查询退款申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/applyDatatable")
    @ResponseBody
    public DataTablesOutput applyDatatable(@RequestBody DataTablesInput input) {
        return tpRefundOrderApplyService.getApplyDataTablesUnion(input);
    }
    /**
     * 退款申请金额选择页面跳转
     * @param tpPayBillDtlVO 交易流水
     * @param model
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDAPPLY_OP)
    @GetMapping("/refundApplyMoney")
    public String listRefundApplyMoney(TpPayBillDtlVO tpPayBillDtlVO, Model model) {
        model.addAttribute("tpPayBillDtlVO", tpPayBillDtlVO);
        return "ttpay/addTpRefundMoney";
    }
    /**
     * 退款申请提交
     * @param tpPayBillDtlVO 退款申请Id
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDAPPLY_OP)
    @PostMapping("/submit")
    @ResponseBody
    @LogMessage(content = "提交退款申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    public AjaxJson refundApplySubmit(TpPayBillDtlVO tpPayBillDtlVO){
        tpRefundOrderApplyService.refundApplySubmit(tpPayBillDtlVO, getCurrentUser());
        return success();
    }
    /**
     * 跳转至审批界面
     * @param flowStatus
     * @param model
     * @return
     */
    @LogMessage(content = "跳转至审批界面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/approve")
    public String approve(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "ttpay/approve/listTpRefundApplyApprove";
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return tpRefundOrderApplyService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审批详情界面
     * @param taskId
     * @param payType 付款方式
     * @param model
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String taskId,String dtlId, String payType, Model model) {
        // 显示标题栏信息
        List<TpPayBillViewDtlVO> list= TtPayUtil.getTableViewField(payType);
        List<TpRefundOrderVO> listRefund = null;
        if (StringUtils.isNotEmpty(dtlId) && StringUtils.isNotEmpty(payType)) {
            if(EmptyUtils.isNotEmpty(list)){
                model.addAttribute("payTypeTitle", list.get(0).getPayShowName());
            }
            model.addAttribute("dataInfo", tpRefundOrderApplyService.getPayBillDtlInfoByIdOrPayType(dtlId,payType));
        }
        listRefund = tpRefundOrderApplyService.listRefundInfo(dtlId, true);
        model.addAttribute("refundInfoList", listRefund);
        model.addAttribute("isRefund", true);
        model.addAttribute("fieldList", list);
        model.addAttribute("taskId", taskId);
        return "ttpay/approve/viewTpRefundAppleApprove";
    }
    /**
     * 退款申请明细页面
     * @param id 退款数据id
     * @param payType 付款方式
     * @param refundId 退款Id
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String view(String id,String payType,String refundId,Model model) {
        // 显示标题栏信息
        List<TpPayBillViewDtlVO> list= TtPayUtil.getTableViewField(payType);
        if (EmptyUtils.isNotEmpty(list)){
            model.addAttribute("payTypeTitle", list.get(0).getPayShowName());
        }
        // 交易数据信息
        model.addAttribute("dataInfo", tpRefundOrderApplyService.getPayBillDtlInfoByIdOrPayType(id,payType));
        // 日志信息，当退款Id不为空时(即退款审批状态为审批中)则显示日志
        List<TpRefundOrderVO> listRefund = null;
        if (StringUtils.isNotEmpty(refundId)) {
            model.addAttribute("tpLogList",tpLogService.listTpLog(refundId));
            model.addAttribute("isLog", true);
            listRefund = tpRefundOrderApplyService.listRefundInfo(id, true);
        }else {
            // 未申请状态时则退款信息显示所有信息
            listRefund = tpRefundOrderApplyService.listRefundInfo(id, false);
        }
        // 退款信息不为空时显示退款信息
        if(EmptyUtils.isNotEmpty(listRefund)){
            model.addAttribute("isRefund", true);
            model.addAttribute("refundInfoList",listRefund);
        }
        model.addAttribute("fieldList", list);
        return "ttpay/viewTpRefundApplyDtl";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            TpRefundOrderVO tpRefundOrderVO = tpRefundOrderApplyService.getRefundInfo(businessId);
            model.addAttribute("tpRefundOrderVO", tpRefundOrderVO);
        }
        return "ttpay/approve/mobile/mobileTpRefundAppleApprove";
    }
}
