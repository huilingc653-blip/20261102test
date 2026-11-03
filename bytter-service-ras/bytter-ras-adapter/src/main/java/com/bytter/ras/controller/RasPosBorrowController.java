package com.bytter.ras.controller;

import com.bytter.ras.service.RasPosBindingService;
import com.bytter.ras.service.RasPosBorrowService;
import com.bytter.ras.vo.RasPosBindingVO;
import com.bytter.ras.vo.RasPosBorrowVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhenghj
 * @Title: Controller
 * @Description:营收稽核POS机借入数据入口类
 * @date 2019-05-07
 */

@Controller
@RequestMapping("/ras/posBorrow")
public class RasPosBorrowController extends BaseController {

    @Autowired
    private RasPosBorrowService rasPosBorrowService;

    @Autowired
    private RasPosBindingService rasPosBindingService;

    /**
     * 营收稽核POS机借入列表页面跳转
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @GetMapping("/list")
    public String list(Model model) {
        return "ras/listRasPosBorrow";
    }

    /**
     * 营收稽核POS机借入列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "查询营收稽核POS机借入列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return rasPosBorrowService.listRasPosBorrow(dataTablesInput);
    }

    /**
     * 营收稽核POS机借入确认
     *
     * @param pid pos机借入id
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "营收稽核POS机借入确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_CONSENT)
    @PostMapping("/sure")
    @ResponseBody
    public AjaxJson sure(String pid) {
        rasPosBorrowService.sure(pid, this.getCurrentUser());
        return success();
    }

    /**
     * 营收稽核POS机借入批量确认
     *
     * @param pids pos机借入ids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "营收稽核POS机借入批量确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHCONSENT)
    @PostMapping("/batchSure")
    @ResponseBody
    public AjaxJson batchSure(String pids) {
        AjaxJson ajaxJson = success();
        rasPosBorrowService.batchSure(pids, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 营收稽核POS机借入打回
     *
     * @param pid pos机借入id
     * @param msg 打回原因
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "营收稽核POS机借入打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_REPULSE)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(String pid, String msg) {
        rasPosBorrowService.back(pid, msg, this.getCurrentUser());
        return success();
    }

    /**
     * 营收稽核POS机借入批量打回
     *
     * @param pids pos机借入ids
     * @param msg  打回原因
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "营收稽核POS机借入批量打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(String pids, String msg) {
        AjaxJson ajaxJson = success();
        rasPosBorrowService.batchBack(pids, msg, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 营收稽核POS机借入详情页
     *
     * @param pid
     * @param model
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBORROW_OP)
    @LogMessage(content = "查询POS机借入详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{pid}")
    public String view(@PathVariable("pid") String pid, Model model) {
        RasPosBorrowVO rasPosBorrowVO = rasPosBorrowService.getRasPosBorrowById(pid);
        RasPosBindingVO rasPosBindingVO = null;
        if (rasPosBorrowVO != null) {
            rasPosBindingVO = rasPosBindingService.getRasPosBindingVOByPid(rasPosBorrowVO.getBdId());
        }
        model.addAttribute("rasPosBorrowVO", rasPosBorrowVO);
        model.addAttribute("rasPosBindingVO", rasPosBindingVO);
        return "ras/viewRasPosBorrow";
    }
}
