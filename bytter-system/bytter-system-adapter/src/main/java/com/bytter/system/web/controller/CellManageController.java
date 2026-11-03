package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.system.service.CellManageService;
import com.bytter.system.vo.CellManageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author wangzy
 * @Title: CellManageController
 * @Description: 管理单元维护
 * @date 2020/5/11 18:13
 */
@Controller
@RequestMapping("/sys/cell")
public class CellManageController extends BaseController {
    @Autowired
    private CellManageService cellManageService;
    /**
     * 管理单元页面跳转
     *
     * @param cellManageVO
     * @return
     */
    @GetMapping("/list")
    public String listCellManage(CellManageVO cellManageVO, Model model) {
        model.addAttribute("listInfo", cellManageService.listCellManageVOTree(cellManageVO));
        model.addAttribute("cellManageVO", cellManageVO);
        return "sys/cellManage/listCellManage";
    }
    @PostMapping("/listCell")
    public String listCellManageVO(CellManageVO cellManageVO, Model model) {
        model.addAttribute("listInfo", cellManageService.listCellManageVOTree(cellManageVO));
        model.addAttribute("cellManageVO", cellManageVO);
        return "sys/cellManage/listCellManage";
    }
    /**
     * 管理单元新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String addCellManage(Model model) {
        return "sys/cellManage/addCellManage";
    }
    /**
     * 管理单元编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String updateCellManage(String id, Model model) {
        model.addAttribute("cellManageVO", cellManageService.getCellManageVOInfo(id));
        model.addAttribute("id", id);
        return "sys/cellManage/addCellManage";
    }
    /**
     * 管理单元新增或修改
     *
     * @param infoVO 信息
     * @return 处理结果
     */
    @LogMessage(content = "管理单元新增或修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(CellManageVO infoVO) {
        AjaxJson ajaxJson = cellManageService.saveOrUpdate(infoVO, getCurrentUser());
        return ajaxJson;
    }
    /**
     * 管理单元编辑页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String viewCellManage(String id, Model model) {
        model.addAttribute("cellManageVO", cellManageService.getCellManageVOInfo(id));
        return "sys/cellManage/viewCellManage";
    }
    /**
     * 管理单元删除
     *
     * @param id    管理单元id
     * @param model
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson deleteCell(String id, Model model) {
        return cellManageService.deleteCell(id, getCurrentUser());
    }
    /**
     * 获取树形结构单元信息
     *
     * @return
     */
    @GetMapping("/treeData")
    @ResponseBody
    public Object treeCellData() {
        return cellManageService.getCellTreeMapList();
    }
    /**
     * 管理单元数据导出
     *
     * @param columns  所有的列头
     * @param fileName 导出文件名
     * @param infoVO   查询条件
     * @param modelMap
     * @param request
     * @param response
     */
    @PostMapping("/excelCell")
    public void excelCell(String columns, String fileName, CellManageVO infoVO, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String[] conlumnArray = columns.split(CommonConstant.COMMA_SPLIT);
        map.put("list", cellManageService.listExcelCellManageVOTree(infoVO));
        map.put("column0", conlumnArray[0]);
        map.put("column1", conlumnArray[1]);
        map.put("column2", conlumnArray[2]);
        TemplateExportParams temmp = new TemplateExportParams(ResourceTemplateEnum.EXPORT_SYS_CELL_XLSX.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, fileName);
        modelMap.put(TemplateExcelConstants.PARAMS, temmp);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        //todo
        //BaseExcelView.render(modelMap, request, response, TemplateExcelConstants.TEMPLATE_EXCEL_VIEW);
    }
}
