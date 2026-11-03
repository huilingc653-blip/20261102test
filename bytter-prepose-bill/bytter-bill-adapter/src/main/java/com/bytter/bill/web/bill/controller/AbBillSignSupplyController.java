package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillSignSupplyService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillSignSupplyVo;
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
 * 签收补录Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/bill/accebill/abbillSignSupply")
public class AbBillSignSupplyController extends BaseController {
    @Autowired
    private AbBillSignSupplyService abBillSignSupplyService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    /**
     * 签收补录信息列表 页面跳转
     *
     * @return 业务路径
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillSignSupply";
    }
    /**
     * 签收补录信息 table列表
     *
     * @return table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    @LogMessage(content = "查询签收补录信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillSignSupplyService.listAbBillSignSupplyDaoInfo(dataTablesInput);
    }
    /**
     * 签收补录操作
     *
     * @return 是否成功
     */
    @PostMapping("/signSupply")
    @ResponseBody
    @LogMessage(content = "签收补录操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_CONSENT)
    public AjaxJson signSupply(@RequestBody AbBillSignSupplyVo abBillSignSupplyVo) {
        abBillSignSupplyService.signSupply(abBillSignSupplyVo, this.getCurrentUser());
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
        return "bill/accebill/approve/listAbBillSignSupplyApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return abBillSignSupplyService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId 业务id
     * @param taskId     taskId
     * @param model      模型
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        // model.addAttribute("entity", 根据businessId查询到需要展示的数据);
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillSignSupplyVo info = abBillSignSupplyService.getAbBillSignSupplyVoById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()) : null);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 系统参数
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return "bill/accebill/approve/approveAbBillReceivebiz";
    }
}
