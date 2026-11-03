package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.dataexchange.basic.entity.ErpDataType;
import com.bytter.biz.dataexchange.basic.service.ErpDataTypeService;
import com.bytter.biz.dataexchange.basic.vo.ErpDataTypeVO;
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
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 数据类型
 * @date 2017-12-13 15:29:54
 */
@Controller
@RequestMapping("/dataexchange2/basic/erpdatatype")
public class ErpDataTypeController extends BaseController {
    @Autowired
    private ErpDataTypeService erpDataTypeService;
    /**
     * 数据类型列表 页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_VIEW)
    @LogMessage(content = "查询数据类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "dataexchange/basic/listErpDataType";
    }
    /**
     * 数据类型 table列表
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return erpDataTypeService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 数据类型新增页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_ADD)
    @GetMapping("/add")
    public String add(ErpDataType erpDataType, Model model) {
        if (StringUtils.isNotEmpty(erpDataType.getId())) {
            erpDataType = erpDataTypeService.getErpTypeById(erpDataType.getId());
            model.addAttribute("erpDataTypePage", erpDataType);
        }
        return "dataexchange/basic/addErpDataType";
    }
    /**
     * 数据类型编辑页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_UPDATE)
    @GetMapping("/update")
    public String update(ErpDataType erpDataType, Model model) {
        if (StringUtils.isNotEmpty(erpDataType.getId())) {
            erpDataType = erpDataTypeService.getErpTypeById(erpDataType.getId());
            model.addAttribute("erpDataTypePage", erpDataType);
        }
        return "dataexchange/basic/updateErpDataType";
    }
    /**
     * 数据类型详情页
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_VIEW)
    @LogMessage(content = "查询数据类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        ErpDataTypeVO erpDataType = erpDataTypeService.getErpTypeVOById(id);
        model.addAttribute("erpDataTypePage", erpDataType);
        return "dataexchange/basic/viewErpDataType";
    }
    /**
     * 添加数据类型
     *
     * @param erpDataType
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_ADD)
    @LogMessage(content = "添加数据类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ErpDataTypeVO erpDataType) {
        erpDataTypeService.save(erpDataType, getCurrentUser());
        return success();
    }
    /**
     * 修改数据类型
     *
     * @param erpDataType
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_UPDATE)
    @LogMessage(content = "修改数据类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(ErpDataTypeVO erpDataType) {
        erpDataTypeService.update(erpDataType, getCurrentUser());
        return success();
    }
    /**
     * 删除数据类型
     *
     * @param id
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_DELETE)
    @LogMessage(content = "删除数据类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ErpDataType erpDataType = erpDataTypeService.getErpTypeById(id);
        erpDataTypeService.deleteEntity(erpDataType);
        return success();
    }
    /**
     * 批量删除数据类型
     *
     * @param ids
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDATATYPE_DELETE)
    @LogMessage(content = "批量删除数据类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            ErpDataType erpDataType = erpDataTypeService.getErpTypeById(id);
            erpDataTypeService.deleteEntity(erpDataType);
        }
        return success();
    }
    /**
     * 查询类型列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<ErpDataType> listByValidSign() {
        List<ErpDataType> list = erpDataTypeService.listErpDataType("1");
        return list;
    }
}
