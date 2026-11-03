package com.bytter.poi.base;
import com.bytter.poi.excel.ExcelToHtmlUtil;
import com.bytter.poi.excel.entity.vo.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.AbstractView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/15.
 */
public abstract class BaseExcelView extends AbstractView {
    public static final String CONTENT_TYPE = "application/vnd.ms-excel";
    protected static final String HSSF = ".xls";
    protected static final String XSSF = ".xlsx";
    public BaseExcelView() {
        this.setContentType("application/vnd.ms-excel");
    }
    protected boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
    public static void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, String viewName) {
        BaseExcelView view = null;
        if (BigExcelConstants.BIG_EXCEL_VIEW.equals(viewName)) {
            //view = new BigExcelController();
        } else if (MapExcelConstants.MAP_EXCEL_VIEW.equals(viewName)) {
            //view = new MapExcelController();
        } else if (NormalExcelConstants.EXCEL_VIEW.equals(viewName)) {
            //view = new SingleExcelController();
        } else if (TemplateExcelConstants.TEMPLATE_EXCEL_VIEW.equals(viewName)) {
            //view = new TemplateExcelController();
        } else if(TemplateWordConstants.TEMPLATE_WORD_VIEW.equals(viewName)){
            //view = new TemplateWordController();
        } else if (MapExcelGraphConstants.MAP_GRAPH_EXCEL_VIEW.equals(viewName)) {
            //view = new MapExcelGraphController();
        } else if (PDFTemplateConstants.PDF_TEMPLATE_VIEW.equals(viewName)) {
            //view = new PdfTemplateController();
        }
        try {
            view.renderMergedOutputModel(model, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void previewExcel(Workbook workbook, HttpServletResponse response){
        String html = ExcelToHtmlUtil.toAllHtml(workbook);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(html);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }
}
