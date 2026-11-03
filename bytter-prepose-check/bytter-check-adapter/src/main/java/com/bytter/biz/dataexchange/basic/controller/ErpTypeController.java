package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.dataexchange.basic.entity.ErpType;
import com.bytter.biz.dataexchange.basic.service.ErpTypeService;
import com.bytter.biz.dataexchange.basic.vo.ErpTypeVO;
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
 * @Description: 接口类型
 * @date 2017-12-13 14:53:07
 */
@Controller
@RequestMapping("/dataexchange2/basic/erptype")
public class ErpTypeController extends BaseController {
    @Autowired
    private ErpTypeService erpTypeService;
    /**
     * 接口类型列表 页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_VIEW)
    @LogMessage(content = "查询接口类型列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "dataexchange/basic/listErpType";
    }
    /**
     * 接口类型 table列表
     *
     * @return
     */
//    @RequiresPermissions("dataexchange:basic:erptype:view")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return erpTypeService.getDataTables(dataTablesInput);
    }
    /**
     * 接口类型新增页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_ADD)
    @GetMapping("/add")
    public String add(ErpType erpType, Model model) {
        if (StringUtils.isNotEmpty(erpType.getId())) {
            erpType = erpTypeService.get(erpType.getId());
            model.addAttribute("erpTypePage", erpType);
        }
        return "dataexchange/basic/addErpType";
    }
    /**
     * 接口类型编辑页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_UPDATE)
    @GetMapping("/update")
    public String update(ErpType erpType, Model model) {
        if (StringUtils.isNotEmpty(erpType.getId())) {
            erpType = erpTypeService.getErpTypeById(erpType.getId());
            model.addAttribute("erpTypePage", erpType);
        }
        return "dataexchange/basic/updateErpType";
    }
    /**
     * 接口类型详情页
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_VIEW)
    @LogMessage(content = "查询接口类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        ErpTypeVO erpType = erpTypeService.getErpTypeVOById(id);
        model.addAttribute("erpTypePage", erpType);
        return "dataexchange/basic/viewErpType";
    }
    /**
     * 添加接口类型
     *
     * @param erpType
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_ADD)
    @LogMessage(content = "添加接口类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ErpTypeVO erpType) {
        erpTypeService.save(erpType, getCurrentUser());
        return success();
    }
    /**
     * 修改接口类型
     *
     * @param erpType
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_UPDATE)
    @LogMessage(content = "修改接口类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(ErpTypeVO erpType) {
        erpTypeService.update(erpType, getCurrentUser());
        return success();
    }
    /**
     * 删除接口类型
     *
     * @param id
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_DELETE)
    @LogMessage(content = "删除接口类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ErpType erpType = erpTypeService.getErpTypeById(id);
        erpTypeService.delete(id);
        return success();
    }
    /**
     * 批量删除接口类型
     *
     * @param ids
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPTYPE_DELETE)
    @LogMessage(content = "批量删除接口类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            ErpType erpType = erpTypeService.get(id);
            erpTypeService.deleteEntity(erpType);
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
    public List<ErpTypeVO> listByValidSign() {
        List<ErpTypeVO> list = erpTypeService.listErpType();
        return list;
    }
}
