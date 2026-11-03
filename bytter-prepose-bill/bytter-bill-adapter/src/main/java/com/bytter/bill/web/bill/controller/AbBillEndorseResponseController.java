package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillEndorseResponseService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
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
 * 背书请求应答
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/bill/accebill/endorseResponse")
public class AbBillEndorseResponseController extends BaseController {
    @Autowired
    private AbBillEndorseResponseService abBillEndorseResponseService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    /**
     * 背书请求应答列表 页面跳转
     *
     * @param model 模型
     * @return 前端页面路径
     */
    @LogMessage(content = "查询背书请求应答列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillEndorseResponse";
    }
    /**
     * 背书请求应答 table列表
     *
     * @param dataTablesInput 前端传参
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillEndorseResponseService.listAbBillEndorseResponse(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 背书请求应答
     *
     * @param eo vo
     * @return 是否请求成功
     */
    @LogMessage(content = "背书请求应答", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/endorseResponse")
    @ResponseBody
    public AjaxJson endorseResponse(AbBillEndorseVo eo) {
        abBillEndorseResponseService.endorseResponse(eo, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @param flowStatus 审批状态
     * @param cycleTime  周期
     * @param model      模型
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bill/accebill/approve/listAbBillEndorseResponseApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 前端传参
     * @return 审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return abBillEndorseResponseService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId id
     * @param taskId     审批id
     * @param model      模型
     * @return 审批详情页
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        AbBillEndorseVo info = new AbBillEndorseVo();
        if (StringUtils.isNotEmpty(businessId)) {
            info = abBillEndorseResponseService.getAbBillEndorseVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null);
        }
        // 必须要做此步骤
        model.addAttribute("taskId", taskId);
        // 背书挂接资金计划
        // 根据流水表预算科目是否为空来显示挂预算列表
        // 系统参数 背书挂接资金计划科目
        model.addAttribute("endorseItem", abConfigService
                .getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return "bill/accebill/approve/approveAbBillEndorse";
    }
}
