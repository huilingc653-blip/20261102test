package com.bytter.ras.controller;

import com.bytter.ras.service.RasAuditService;
import com.bytter.ras.service.RasConfigService;
import com.bytter.ras.service.RasReceivedService;
import com.bytter.ras.service.RasRevenueService;
import com.bytter.ras.vo.RasAuditBackVO;
import com.bytter.ras.vo.RasAuditInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zhenghj
 * @Title: Controller
 * @Description:稽核处理入口类
 * @date 2019-05-16
 */

@Controller
@RequestMapping("/ras/audit")
public class RasAuditController extends BaseController {

    @Autowired
    private RasAuditService rasAuditService;

    @Autowired
    private RasReceivedService rasReceivedService;

    @Autowired
    private RasRevenueService rasRevenueService;

    @Autowired
    private RasConfigService rasConfigService;

    /**
     * 反稽核列表页面跳转
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKAUDIT_OP)
    @GetMapping("/back/list")
    public String list(Model model) {
        return "ras/listAuditBack";
    }

    /**
     * 反稽核列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKAUDIT_OP)
    @LogMessage(content = "查询稽核数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/back/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return rasAuditService.listRasAuditBack(dataTablesInput);
    }

    /**
     * 反稽核确认
     *
     * @param aids 稽核ids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKAUDIT_OP)
    @LogMessage(content = "营收稽核反稽核批量确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
    @PostMapping("/back/backAudit")
    @ResponseBody
    public AjaxJson backAudit(String aids) {
        AjaxJson ajaxJson = success();
        rasAuditService.backAudit(aids, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 根据查询条件反稽核自动确认全部
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKAUDIT_OP)
    @LogMessage(content = "营收稽核反稽核批量自动确认全部", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
    @PostMapping("/back/autoBackAudit")
    @ResponseBody
    public AjaxJson autoBackAudit(@RequestBody RasAuditBackVO rasAuditBackVO) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(rasAuditService.autoBackAudit(rasAuditBackVO, this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 稽核列表页面跳转
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_MANUALAUDIT_OP)
    @GetMapping("/manual/list")
    public String listManual(Model model) {
        Date today = DateUtils.getDateYMD();
        String beginRevenueDate = DateUtils.format(DateUtils.addDay(today, -1 * rasConfigService.getRevenueRollDay()), "yyyy-MM-dd");
        String beginReceivedDate = DateUtils.format(DateUtils.addDay(today, -1 * rasConfigService.getReceivedRollDay()), "yyyy-MM-dd");
        model.addAttribute("beginRevenueDate", beginRevenueDate);
        model.addAttribute("beginReceivedDate", beginReceivedDate);
        return "ras/listAuditManual";
    }

    /**
     * 营收稽核列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_MANUALAUDIT_OP)
    @LogMessage(content = "查询手工营收稽核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/manual/datatableRevenue")
    @ResponseBody
    public DataTablesOutput dataTableRevenue(@RequestBody DataTablesInput dataTablesInput) {
        return rasRevenueService.getDataTablesUnion(dataTablesInput, RasConstant.GRANT_TYPE_AUDIT, RasConstant.RAS_RESOURCE_MANUALAUDIT_OP);
    }

    /**
     * 实收稽核列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_MANUALAUDIT_OP)
    @LogMessage(content = "查询手工实收稽核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/manual/datatableReceived")
    @ResponseBody
    public DataTablesOutput dataTableReceived(@RequestBody DataTablesInput dataTablesInput) {
        return rasReceivedService.getDataTablesUnion(dataTablesInput, RasConstant.GRANT_TYPE_AUDIT, RasConstant.RAS_RESOURCE_MANUALAUDIT_OP);
    }


    /**
     * 手工稽核操作
     *
     * @param aids 稽核ids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_MANUALAUDIT_OP)
    @LogMessage(content = "稽核操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/manual/manualAudit")
    @ResponseBody
    public AjaxJson manualAudit(@RequestBody RasAuditInfoVO auditInfoVO) {
        AjaxJson ajaxJson = success();
        auditInfoVO.setUser(this.getCurrentUser());
        ajaxJson.setSuccess(rasAuditService.auditOp(auditInfoVO));
        return ajaxJson;
    }
}
