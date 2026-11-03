package com.bytter.system.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.EquityMaintenance;
import com.bytter.system.service.EquityMaintenanceService;
import com.bytter.system.vo.EquityMaintenanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/equityMaintenance")
public class EquityMaintenanceController extends BaseController {

    @Autowired
    private EquityMaintenanceService equityMaintenanceService;

    @GetMapping("/list")
    public void list() {
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return equityMaintenanceService.queryByCodeNadName(dataTablesInput);
    }

    @GetMapping("/add")
    public String add( Model model) {
        return "sys/equityMaintenance/addEquityMaintenance";
    }

    @LogMessage(content = "股权维护保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EquityMaintenance equityMaintenance) {
        equityMaintenanceService.saveEquityMaintenance(equityMaintenance, getUserLoginName());
        return success();
    }

    ///view
    @LogMessage(content = "股权维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        EquityMaintenanceVo vo = equityMaintenanceService.getByUnion(id);
        model.addAttribute("EquityMaintenanceVo", vo);
        return "sys/equityMaintenance/viewEquityMaintenance";
    }

    @GetMapping("/update")
    public String update(EquityMaintenanceVo areaVo, Model model) {
        if (StringUtils.isNotBlank(areaVo.getId())){
            EquityMaintenanceVo vo = equityMaintenanceService.getByUnion(areaVo.getId());
            model.addAttribute("equityMaintenanceVo", vo);
        }
        return "sys/equityMaintenance/updateEquityMaintenance";
    }
    @LogMessage(content = "修改股权维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(EquityMaintenanceVo vo ) {

        equityMaintenanceService.modify(vo, getUserLoginName());
        return success();
    }

    @LogMessage(content = "删除股权维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        equityMaintenanceService.deleteById(id, getUserLoginName());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<EquityMaintenanceVo> json() {
        return equityMaintenanceService.json();
    }

    /**
     * 股权维护导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<EquityMaintenanceVo> list) {

        equityMaintenanceService.saveImport(list, getUserLoginName());
        return success();
    }
}
