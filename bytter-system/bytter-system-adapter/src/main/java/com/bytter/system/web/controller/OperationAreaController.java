package com.bytter.system.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.service.OperationAreaService;
import com.bytter.system.vo.OperationAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/operationArea")
public class OperationAreaController extends BaseController {

    @Autowired
    private OperationAreaService operationAreaService;

    @GetMapping("/list")
    public void list() {
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
       return operationAreaService.queryByCodeNadName(dataTablesInput);
    }

    @GetMapping("/add")
    public String add( Model model) {
        return "sys/operationArea/addAperationArea";
    }

    @LogMessage(content = "营运区维护保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(OperationArea area) {
        operationAreaService.saveOperationArea(area, getUserLoginName());
        return success();
    }

    @LogMessage(content = "营运区维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        OperationAreaVo vo = operationAreaService.getByUnion(id);
        model.addAttribute("OperationAreaVO", vo);
        return "sys/operationArea/viewOperationArea";
    }

    @GetMapping("/update")
    public String update(OperationAreaVo areaVo, Model model) {
        if (StringUtils.isNotBlank(areaVo.getId())){
            OperationAreaVo vo = operationAreaService.getByUnion(areaVo.getId());
            model.addAttribute("operationAreaVO", vo);
        }
        return "sys/operationArea/updateOperationArea";
    }
    @LogMessage(content = "修改营运区维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(OperationAreaVo vo ) {
        operationAreaService.modify(vo, getUserLoginName());
        return success();
    }

    @LogMessage(content = "删除营运区", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        operationAreaService.deleteById(id, getUserLoginName());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<OperationAreaVo> json() {
        return operationAreaService.getOperationArea();
    }

    @PostMapping("/selectOperationArea")
    @ResponseBody
    public DataTablesOutput selectOperationArea(@RequestBody DataTablesInput dataTablesInput) {
        dataTablesInput.getFormdata().put("eqs_status","1");
        return operationAreaService.queryByCodeNadName(dataTablesInput);
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/selectPage")
    public String select(String id, Model model, String joinBankType, String bankTypeId, String layerType ) {
        model.addAttribute("layerType",layerType);
        return "sys/operationArea/selectOperationArea";
    }
    /**
     *
     *
     * @return
     */
    @GetMapping("/selectPage1/{layerType}")
    public String select1(@PathVariable("layerType") String layerType,Model model) {
        model.addAttribute("layerType",layerType);
        return "sys/operationArea/selectOperationArea";
    }
    /**
     * 营运区维护导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<OperationAreaVo> list) {
//        abEmitBillService.saveImport(list, this.getCurrentUser());
        operationAreaService.saveImport(list, getUserLoginName());
        return success();
    }
}
