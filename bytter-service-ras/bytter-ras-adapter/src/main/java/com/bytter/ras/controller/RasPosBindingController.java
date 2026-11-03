package com.bytter.ras.controller;

import com.bytter.ras.entity.RasLog;
import com.bytter.ras.service.RasLogService;
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

import java.util.List;

/**
 * @author zhenghj
 * @Title: Controller
 * @Description:营收稽核POS机绑定入口类
 * @date 2019-05-05
 */

@Controller
@RequestMapping("/ras/posBinding")
public class RasPosBindingController extends BaseController {

    @Autowired
    private RasPosBindingService rasPosBindingService;

    @Autowired
    private RasPosBorrowService rasPosBorrowService;

    @Autowired
    private RasLogService rasLogService;

    /**
     * 营收稽核POS机绑定列表页面跳转
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @GetMapping("/list")
    public String list(Model model) {
        return "ras/listRasPosBinding";
    }

    /**
     * 营收稽核POS机绑定列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "查询营收稽核POS机绑定列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return rasPosBindingService.listRasPosBinding(dataTablesInput);
    }

    /**
     * 营收稽核POS机绑定新增页面跳转
     *
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @GetMapping("/add")
    public String add(Model model) {
        return "ras/addRasPosBinding";
    }

    /**
     * 新增-营收稽核POS机绑定数据
     *
     * @param rasPosBindingVO 营收稽核POS机绑定VO类
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "新增-保存营收稽核POS机绑定数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RasPosBindingVO rasPosBindingVO) {
        rasPosBindingService.saveRasPosBinding(rasPosBindingVO, this.getCurrentUser(), RasConstant.RAS_BANK_OPERATE_STATUS_SAVE);
        return success();
    }

    /**
     * 营收稽核POS机绑定编辑页面跳转
     *
     * @param pid POS机绑定id
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @GetMapping("/update/{pid}")
    public String update(@PathVariable("pid") String pid, Model model) {
        RasPosBindingVO vo = rasPosBindingService.getRasPosBindingVOByPid(pid);
        model.addAttribute("rasPosBindingVO", vo);
        return "ras/updateRasPosBinding";
    }

    /**
     * 修改营收稽核POS机绑定信息
     *
     * @param rasPosBindingVO
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "修改营收稽核POS机绑定信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RasPosBindingVO rasPosBindingVO) {
        rasPosBindingService.updateRasPosBinding(rasPosBindingVO, this.getCurrentUser());
        return success();
    }

    /**
     * 营收稽核POS机绑定借出页面跳转
     *
     * @param pid POS机绑定id
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @GetMapping("/lend/{pid}")
    public String lend(@PathVariable("pid") String pid, Model model) {
        RasPosBindingVO vo = rasPosBindingService.getRasPosBindingVOByPid(pid);
        model.addAttribute("rasPosBindingVO", vo);
        return "ras/lendRasPosBinding";
    }

    /**
     * 添加营收稽核POS机借出信息
     *
     * @param rasPosBorrowVO
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "添加营收稽核POS机借出信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/lendSave")
    @ResponseBody
    public AjaxJson lendSave(RasPosBorrowVO rasPosBorrowVO) {
        rasPosBindingService.lendSave(rasPosBorrowVO, this.getCurrentUser());
        return success();
    }

    /**
     * 营收稽核POS机收回
     *
     * @param pid POS机绑定id
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "营收稽核POS机收回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/revokeSave")
    @ResponseBody
    public AjaxJson revokeSave(String pid) {
        rasPosBindingService.revokeSave(pid, this.getCurrentUser());
        return success();
    }


    /**
     * 营收稽核POS机绑定导入
     *
     * @param list
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<RasPosBindingVO> list) {
        rasPosBindingService.saveImport(list, this.getCurrentUser());
        return success();
    }


    /**
     * 营收稽核POS机绑定详情页
     *
     * @param pid
     * @param model
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_POSBINDING_OP)
    @LogMessage(content = "查询POS机绑定详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{pid}")
    public String view(@PathVariable("pid") String pid, Model model) {
        RasPosBindingVO rasPosBindingVO = rasPosBindingService.getRasPosBindingVOByPid(pid);
        List<RasPosBorrowVO> borrowList = rasPosBorrowService.getRasPosBorrowByBdId(pid, null);
        model.addAttribute("rasPosBindingVO", rasPosBindingVO);
        model.addAttribute("rasPosBorrowVOList", borrowList);
        //操作日志
        List<RasLog> rasLogs = rasLogService.listRasLog(pid);
        model.addAttribute("logList", rasLogs);
        return "ras/viewRasPosBinding";
    }
}
