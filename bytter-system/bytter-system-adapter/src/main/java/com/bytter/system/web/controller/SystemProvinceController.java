package com.bytter.system.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.service.SystemProVinceService;
import com.bytter.system.vo.SystemProvinceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/systemProvince")
public class SystemProvinceController extends BaseController {

    @Autowired
    private SystemProVinceService systemProVinceService;

    @GetMapping("/list")
    public void list() {
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
       return systemProVinceService.queryByCodeNadName(dataTablesInput);
    }

    @GetMapping("/add")
    public String add( Model model) {
        return "sys/systemProvince/addSystemProvince";
    }

    @LogMessage(content = "省区维护保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(SystemProvince systemProvince) {
        systemProVinceService.saveSystemProvince(systemProvince, getUserLoginName());
        return success();
    }

    @LogMessage(content = "省区维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        SystemProvinceVo vo = systemProVinceService.getByUnion(id);
        model.addAttribute("SystemProvinceVo", vo);
        return "sys/systemProvince/viewSystemProvince";
    }

    @GetMapping("/update")
    public String update(SystemProvinceVo areaVo, Model model) {
        if (StringUtils.isNotBlank(areaVo.getId())){
            SystemProvinceVo vo = systemProVinceService.getByUnion(areaVo.getId());
            model.addAttribute("systemProvinceVo", vo);
        }
        return "sys/systemProvince/updateSystemProvince";
    }
    @LogMessage(content = "修改省区维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(SystemProvinceVo vo ) {
        if(vo == null || StringUtils.isBlank(vo.getId())){
            throw new BusinessException("请选择需要修改的数据");
        }
        systemProVinceService.modify(vo, getUserLoginName());
        return success();
    }

    @LogMessage(content = "删除省区", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        systemProVinceService.deleteById(id, getUserLoginName());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<SystemProvinceVo> json() {
        return systemProVinceService.getSystemProvince();
    }

    @PostMapping("/selectSystemProvince")
    @ResponseBody
    public DataTablesOutput selectSystemProvince(@RequestBody DataTablesInput dataTablesInput) {
        return systemProVinceService.selectSystemProvince(dataTablesInput);
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/selectPage")
    public String select(String id, Model model, String joinBankType, String bankTypeId, String layerType ) {
        model.addAttribute("layerType",layerType);
        return "sys/systemProvince/selectSystemProvince";
    }
    /**
     *
     *
     * @return
     */
    @GetMapping("/selectPage1/{layerType}")
    public String select1(@PathVariable("layerType") String layerType,Model model) {
        model.addAttribute("layerType",layerType);
        return "sys/systemProvince/selectSystemProvince";
    }
    /**
     * 营运区维护导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<SystemProvinceVo> list) {
//        abEmitBillService.saveImport(list, this.getCurrentUser());
        systemProVinceService.saveImport(list, getUserLoginName());
        return success();
    }
}
