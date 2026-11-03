package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.dataexchange.basic.service.ErpDynamicDataService;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataListVO;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataTypeVO;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.poi.enums.ResourceTemplateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Controller
 * @Description: 自定义数据映射
 * @date 2018-04-19 11:24:27
 */
@Controller
@RequestMapping("/dataexchange2/basic/erpdynamicdata")
public class ErpDynamicDataController extends BaseController {
    @Autowired
    private ErpDynamicDataService erpDynamicDataService;
    /**
     * 自定义数据映射列表 页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_VIEW)
    @LogMessage(content = "查询自定义数据映射列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "dataexchange/basic/listErpDynamicData";
    }
    /**
     * 自定义数据映射 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return erpDynamicDataService.getDataTables(dataTablesInput);
    }
    /**
     * 接口类型新增页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_SAVE_UPDATE)
    @GetMapping("/add")
    public String add(String typeCode, String dataTypeId, Model model) {
        model.addAttribute("typeCode", typeCode);
        model.addAttribute("dataTypeId", dataTypeId);
        return "dataexchange/basic/addErpDynamicData";
    }
    /**
     * 自定义数据保存
     *
     * @param erpDynamicDataList
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_SAVE_UPDATE)
    @LogMessage(content = "保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson save(@RequestBody ErpDynamicDataListVO erpDynamicDataList) {
        erpDynamicDataService.saveOrUpdateErpDynamicData(erpDynamicDataList, getCurrentUser());
        return success();
    }
    /**
     * 查询类型列表
     *
     * @return
     */
    @GetMapping("/dataType")
    @ResponseBody
    public List<ErpDynamicDataTypeVO> listByValidSign() {
        List<ErpDynamicDataTypeVO> list = erpDynamicDataService.getAllDynamicDataType();
        return list;
    }
    /**
     * 导入自定义数据映射信息
     *
     * @param list
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_IMPORT)
    @LogMessage(content = "导入自定义数据对应信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ErpDynamicDataVO> list) {
        AjaxJson json = success();
        boolean flag = erpDynamicDataService.saveImport(list, this.getCurrentUser());
        json.setSuccess(flag);
        return success();
    }
    /**
     * 导出excel 使模板
     */
    @GetMapping("/exportXls")
    public void exportXls(String typeCode, String dataTypeId, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<ErpDynamicDataVO> listInfo = erpDynamicDataService.getDynamicDataByMap(typeCode, dataTypeId);
        Map<String, Object> map = new HashMap<>();
        map.put("list", listInfo);
        TemplateExportParams temmp = new TemplateExportParams(ResourceTemplateEnum.EXPORT_DATAEXCHANGE_DYNAMIC_EXP_XLSX.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "dynamicDataExp");
        modelMap.put(TemplateExcelConstants.PARAMS, temmp);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
//        BaseExcelView.render(modelMap, request, response, TemplateExcelConstants.TEMPLATE_EXCEL_VIEW);
    }
    /**
     * 删除自定义数据映射
     *
     * @param id
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_DELETE)
    @LogMessage(content = "删除自定义数据映射", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        boolean reBoo = erpDynamicDataService.batchDelete(id, getCurrentUser());
        AjaxJson json = new AjaxJson();
        json.setSuccess(reBoo);
        return json;
    }
    /**
     * 批量删除自定义数据映射
     *
     * @param ids
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPDYNAMICDATA_DELETE)
    @LogMessage(content = "批量删除自定义数据映射", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHDEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(String ids) {
        boolean reBoo = erpDynamicDataService.batchDelete(ids, this.getCurrentUser());
        AjaxJson json = new AjaxJson();
        json.setSuccess(reBoo);
        return json;
    }
}
