package com.bytter.system.poi.controller;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.BigExcelConstants;
import com.bytter.poi.handler.inter.IExcelExportServer;
import com.bytter.system.poi.controller.base.BaseExcelView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/7/25.
 */
@Controller(BigExcelConstants.BIG_EXCEL_VIEW)
public class BigExcelController extends BaseExcelView {
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        Workbook workbook = ExcelExportUtil.exportBigExcel((ExportParams) model.get(BigExcelConstants.PARAMS),(Class<?>) model.get(BigExcelConstants.CLASS), Collections.EMPTY_LIST);
        IExcelExportServer server = (IExcelExportServer) model.get(BigExcelConstants.DATA_INTER);
        int page = 1;
        List<Object> list = server
                .selectListForExcelExport(model.get(BigExcelConstants.DATA_PARAMS), page++);
        while (list != null && list.size() > 0) {
            workbook = ExcelExportUtil.exportBigExcel((ExportParams) model.get(BigExcelConstants.PARAMS),(Class<?>) model.get(BigExcelConstants.CLASS), list);
            list = server.selectListForExcelExport(model.get(BigExcelConstants.DATA_PARAMS),page++);
        }
        ExcelExportUtil.closeExportBigExcel();
        if (model.containsKey(BigExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(BigExcelConstants.FILE_NAME);
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
