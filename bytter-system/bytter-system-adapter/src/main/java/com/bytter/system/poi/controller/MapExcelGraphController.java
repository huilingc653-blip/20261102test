package com.bytter.system.poi.controller;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.MapExcelGraphConstants;
import com.bytter.poi.excel.graph.builder.ExcelChartBuildService;
import com.bytter.poi.excel.graph.entity.ExcelGraph;
import com.bytter.system.poi.controller.base.BaseExcelView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/7/25.
 */
@Controller(MapExcelGraphConstants.MAP_GRAPH_EXCEL_VIEW)
public class MapExcelGraphController extends BaseExcelView {
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        ExportParams params=(ExportParams)model.get(MapExcelGraphConstants.PARAMS);
        List<ExcelExportEntity> entityList=(List<ExcelExportEntity>) model.get(MapExcelGraphConstants.ENTITY_LIST);
        List<Map<String, Object>> mapList=(List<Map<String, Object>>)model.get(MapExcelGraphConstants.MAP_LIST);
        List<ExcelGraph> graphDefinedList=(List<ExcelGraph>)model.get(MapExcelGraphConstants.GRAPH_DEFINED);
        //构建数据
        Workbook workbook = ExcelExportUtil.exportExcel(params,entityList,mapList);
        ExcelChartBuildService.createExcelChart(workbook,graphDefinedList, params.isDynamicData(), params.isAppendGraph());
        if (model.containsKey(MapExcelGraphConstants.FILE_NAME)) {
            codedFileName = (String) model.get(MapExcelGraphConstants.FILE_NAME);
        }
        if (workbook instanceof HSSFWorkbook) {
            codedFileName += HSSF;
        } else {
            codedFileName += XSSF;
        }
        if (isIE(request)) {
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
