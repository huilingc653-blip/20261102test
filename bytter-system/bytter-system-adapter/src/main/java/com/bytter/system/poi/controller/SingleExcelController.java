package com.bytter.system.poi.controller;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.system.poi.controller.base.BaseExcelView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/15.
 */
@Controller(NormalExcelConstants.EXCEL_VIEW)
public class SingleExcelController extends BaseExcelView {
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        Workbook workbook = null;
        if(model.containsKey("mapList")) {
            List out = (List)model.get("mapList");
            if(out.size() == 0) {
                throw new RuntimeException("MAP_LIST IS NULL");
            }
            workbook = ExcelExportUtil.exportExcel((ExportParams)((Map)out.get(0)).get("params"), (Class)((Map)out.get(0)).get("entity"), (Collection)((Map)out.get(0)).get("data"));
            for(int i = 1; i < out.size(); ++i) {
                (new ExcelExportServer()).createSheet(workbook, (ExportParams)((Map)out.get(i)).get("params"), (Class)((Map)out.get(i)).get("entity"), (Collection)((Map)out.get(i)).get("data"));
            }
        } else {
            workbook = ExcelExportUtil.exportExcel((ExportParams)model.get("params"), (Class)model.get("entity"), (Collection)model.get("data"));
        }
        if(model.containsKey("fileName")) {
            codedFileName = (String)model.get("fileName");
        }
        if(workbook instanceof HSSFWorkbook) {
            codedFileName = codedFileName + ".xls";
        } else {
            codedFileName = codedFileName + ".xlsx";
        }
        if(this.isIE(request)) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
