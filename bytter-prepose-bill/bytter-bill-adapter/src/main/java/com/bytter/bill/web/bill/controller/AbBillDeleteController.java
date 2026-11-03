package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillDeleteService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoService;
import com.bytter.bill.domain.bill.vo.AbBillDeleteVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 票据删除
 * @date 2020-06-01
 */
@Controller
@RequestMapping("/bill/accebill/abbilldelete")
public class AbBillDeleteController extends BaseController {
    @Autowired
    private AbBillDeleteService abBillDeleteService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    /**
     * 票据删除列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询票据删除列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillDelete";
    }
    /**
     * 票据删除 able列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillDeleteService.listAbBillDelete(dataTablesInput);
    }
    @LogMessage(content = "票据删除提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteConfirm")
    @ResponseBody
    public AjaxJson deleteConfirm(AbBillDeleteVO deleteVO) {
        abBillDeleteService.deleteConfirm(deleteVO, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bill/accebill/approve/listAbBillDeleteApprove";
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
        return abBillDeleteService.dataTableApprove(dataTablesInput);
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
        if (StringUtils.isNotEmpty(businessId)) {
            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(vouInfo.getSourceType())) {
                AbReceiveBillVO info = abBillDeleteService.getAbReceiveBillVoByAid(businessId);
                model.addAttribute("abReceiveBillPage", info);
                abBillAttachmentService.viewAttachment(businessId, model);
            } else {
                AbEmitBillVo info = abBillDeleteService.getAbEmitBillVoByAid(businessId);
                model.addAttribute("abEmitBillVo", info);
            }
            model.addAttribute("type", vouInfo.getSourceType());
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "bill/accebill/approve/approveAbBillDelete";
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
        String returnVm = "";
        if (StringUtils.isNotEmpty(businessId)) {
            AbAccrecords accrecordsInfo = abAccrecordsService.get(businessId, true);
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(vouInfo.getSourceType())) {
                AbReceiveBillVO info = abBillDeleteService.getAbReceiveBillVoByAid(businessId);
                model.addAttribute("info", info);
                returnVm = "bill/accebill/mobile/viewApproveAbBillBase";
            } else {
                AbEmitBillVo info = abBillDeleteService.getAbEmitBillVoByAid(businessId);
                model.addAttribute("abEmitBillVo", info);
                returnVm = "bill/accebill/mobile/viewApproveAbBillEmit";
            }
            model.addAttribute("type", vouInfo.getSourceType());
            model.addAttribute("isApprove", true);
        }
        return returnVm;
    }
}
