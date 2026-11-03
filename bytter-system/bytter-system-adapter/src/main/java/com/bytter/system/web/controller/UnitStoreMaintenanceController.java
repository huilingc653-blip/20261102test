package com.bytter.system.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.UnitStoreMaintenance;
import com.bytter.system.service.UnitStoreMaintenanceService;
import com.bytter.system.vo.UnitStoreMaintenanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/unitStore")
public class UnitStoreMaintenanceController extends BaseController {

    @Autowired
    private UnitStoreMaintenanceService unitStoreMaintenanceService;

    @GetMapping("/list")
    public void list() {
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = unitStoreMaintenanceService.queryByCodeNadName(dataTablesInput);
        List<UnitStoreMaintenance> data = dataTablesOutput.getData();
        data.stream().forEach(vo->{
            vo.setCorpName("("+vo.getCorpCode()+")"+vo.getCorpName());
        });
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }

    @GetMapping("/add")
    public String add( Model model) {
        return "sys/unitStore/addUnitStore";
    }

    @LogMessage(content = "单位仓库维护保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(UnitStoreMaintenance unitStoreMaintenance) {
        unitStoreMaintenanceService.saveEquityMaintenance(unitStoreMaintenance, getUserLoginName());
        return success();
    }

    @LogMessage(content = "单位仓库维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        UnitStoreMaintenanceVo vo = unitStoreMaintenanceService.getByUnion(id);
        vo.setCorpName("("+vo.getCorpCode()+")"+vo.getCorpName());
        model.addAttribute("unitStoreVo", vo);
        return "sys/unitStore/viewUnitStore";
    }

    @GetMapping("/update")
    public String update(UnitStoreMaintenanceVo unitStore, Model model) {
        if (StringUtils.isNotBlank(unitStore.getId())){
            UnitStoreMaintenanceVo vo = unitStoreMaintenanceService.getByUnion(unitStore.getId());
            model.addAttribute("unitStoreVo", vo);
        }
        return "sys/unitStore/updateUnitStore";
    }
    @LogMessage(content = "修改单位仓库维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(UnitStoreMaintenanceVo unitStore ) {
        unitStoreMaintenanceService.modify(unitStore, getUserLoginName());
        return success();
    }

    @LogMessage(content = "删除单位仓库维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        unitStoreMaintenanceService.deleteById(id, getUserLoginName());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<UnitStoreMaintenanceVo> json() {
        return unitStoreMaintenanceService.json();
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/selectPage")
    public String selectPage(String id, Model model, String joinBankType, String bankTypeId, String layerType) {
        model.addAttribute("layerType",layerType);
        return "sys/unitStore/selectUnitStore";
    }

    @PostMapping("/selectUnitStore")
    @ResponseBody
    public DataTablesOutput selectUnitStore(@RequestBody DataTablesInput dataTablesInput) {
        return unitStoreMaintenanceService.selectUnitStore(dataTablesInput);
    }

    /**
     * 单位仓库维护导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<UnitStoreMaintenanceVo> list) {
        unitStoreMaintenanceService.saveImport(list, getUserLoginName());
        return success();
    }
}
