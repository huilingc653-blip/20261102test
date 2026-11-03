package com.bytter.credential.web.international.controller;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcDeposit;
import com.bytter.credential.domain.international.entity.LcDepositDelay;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.credential.domain.international.service.LcDepositDelayService;
import com.bytter.credential.domain.international.service.LcDepositService;
import com.bytter.credential.api.constant.InternationalResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-25 14:38
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/lcDepositDelay")
public class LcDepositDelayController extends BaseController {
    @Autowired
    private LcDepositDelayService lcDepositDelayService;
    @Autowired
    private BillArrivalService billArrivalService;
    @Autowired
    private CreatedCreditService creditService;
    @Autowired
    private LcDepositService lcDepositService;
    /**
     * 申请展期列表页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "展期列表页跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDepositDelayApply")
    public String listDeposit(Model model) {
        return "international/credit/lcDepositDelay/listLcDepositDelayApply";
    }
    /**
     *展期列表页面数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "展期列表页面数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLcDepositDelayApply")
    @ResponseBody
    public DataTablesOutput queryLcDepositDelayApply(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositDelayService.listlcDepositDelayApply(dataTablesInput, user);
    }
    /**
     * 展期申请页面跳转
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "展期申请页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addLcDepositDelayApply")
    public String addDepositDelay(@RequestParam String id, Model model) {
        LcDeposit lcDeposit = lcDepositService.get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcDeposit.getLcId());
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcBillArrival",lcBillArrival);
        model.addAttribute("lcEnrol",lcEnrol);
        return "international/credit/lcDepositDelay/addLcDepositDelay";
    }
    /**
     * 保存数据进入审批
     * @param lcDepositDelay
     * @param model
     * @return
     */
    @LogMessage(content = "展期申请保存并进入审批", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveLcDepositDelayApply")
    @ResponseBody
    public AjaxJson saveLcDepositDelay(LcDepositDelay lcDepositDelay, Model model) {
        ICurrentUser user = getCurrentUser();
        lcDepositDelayService.saveLcdepositDelay(lcDepositDelay, user);
        return success();
    }
    /**
     *展期审批列表页面数据查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "展期审批列表页面数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLcDepositDelayApprove")
    @ResponseBody
    public DataTablesOutput queryLcDepositDelayApprove(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositDelayService.listlcDepositDelayApprove(dataTablesInput, user);
    }
    /**
     * 展期审批列表页面数据查询
     * @param flowStatus
     * @param model
     * @return
     */
    @LogMessage(content = "展期审批列表页面数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listLcDepositDelayApprove")
    public String listLcDepositDelayApprove(@RequestParam String flowStatus,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/lcDepositDelay/approve/listLcDepositDelayApprove";
    }
    /**
     * 展期审批列表页面数据查询
     * @param id 主键id
     * @param taskId
     * @param model
     * @return
     */
    @LogMessage(content = "展期审批列表页面数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewLcDepositApprove")
    public String viewLcDepositApprove(@RequestParam String id, @RequestParam String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "international/credit/lcDepositDelay/approve/viewDepositDelayApprove";
    }
    /**
     *展期审批显示页面
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "展期审批显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDepositDelay")
    public String viewDepositDelay(@RequestParam String id,String flag, Model model) {
        LcDepositDelay lcDepositDelay = lcDepositDelayService.get(id);
        LcDeposit lcDeposit = lcDepositService.get(lcDepositDelay.getDepositId());
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcDeposit.getLcId());
        model.addAttribute("lcDepositDelay", lcDepositDelay);
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcBillArrival",lcBillArrival);
        model.addAttribute("lcEnrol",lcEnrol);
        model.addAttribute("flag",flag);
        return "international/credit/lcDepositDelay/viewLcDepositDelay";
    }
    @LogMessage(content = "展期列表页面数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLcDepositDelay")
    @ResponseBody
    public DataTablesOutput queryLcDepositDelay(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = getCurrentUser();
        return lcDepositDelayService.queryLcDepositDelay(dataTablesInput, user);
    }
    /**
     * 申请展期列表页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "展期列表页跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDepositDelay")
    public String listDepositDelay(Model model) {
        return "international/credit/lcDepositDelay/listLcDepositDelay";
    }
    /**
     * 展期申请编辑
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "展期申请编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editDepositDelay")
    public String editDepositDelay(@RequestParam String id, Model model) {
        LcDepositDelay lcDepositDelay = lcDepositDelayService.get(id);
        LcDeposit lcDeposit = lcDepositService.get(lcDepositDelay.getDepositId());
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcDeposit.getLcId());
        model.addAttribute("lcDepositDelay", lcDepositDelay);
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcBillArrival",lcBillArrival);
        model.addAttribute("lcEnrol",lcEnrol);
        return "international/credit/lcDepositDelay/editLcDepositDelay";
    }
    /**
     *获取该押汇id最大展期日期
     * @return
     */
    @LogMessage(content = "获取该押汇id最大展期日期", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getMaxDateByDepositId")
    @ResponseBody
    public Map<String, Object> getMaxDate(@RequestParam String depositId, Model model) {
        Map<String, Object> params = new HashedMap();
        params.put("depositId", depositId);
        return lcDepositDelayService.getMaxDate(params);
    }
    /**
     * 取消展期
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "取消展期", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/deleteLcDepositDelay")
    @ResponseBody
    public AjaxJson deleteLcDepositDelay(@RequestParam String id, Model model) {
        lcDepositDelayService.DeletLcDepositDelayt(id);
        return success();
    }
    /**
     * 展期审批手机显示页面
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "展期审批手机显示页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewDepositDelay")
    public String MobileViewDepositDelay(@RequestParam String id, @RequestParam String businessId, Model model) {
        LcDepositDelay lcDepositDelay = lcDepositDelayService.get(businessId);
        LcDeposit lcDeposit = lcDepositService.get(lcDepositDelay.getDepositId());
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcDeposit.getLcId());
        model.addAttribute("lcDepositDelay", lcDepositDelay);
        model.addAttribute("lcDeposit", lcDeposit);
        model.addAttribute("lcBillArrival",lcBillArrival);
        model.addAttribute("lcEnrol",lcEnrol);
        return "international/credit/lcDepositDelay/approve/mobileViewLcDepositDelay";
    }
}
