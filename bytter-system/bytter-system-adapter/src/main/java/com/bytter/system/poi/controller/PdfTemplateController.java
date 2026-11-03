package com.bytter.system.poi.controller;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.PDFTemplateConstants;
import com.bytter.poi.pdf.PdfExportUtil;
import com.bytter.poi.pdf.entity.PdfExportParams;
import com.bytter.system.poi.controller.base.BaseExcelView;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/7/25.
 */
@Controller(PDFTemplateConstants.PDF_TEMPLATE_VIEW)
public class PdfTemplateController extends BaseExcelView {
    public PdfTemplateController() {
        setContentType("application/pdf");
    }
    protected boolean generatesDownloadContent() {
        return true;
    }
    protected final void renderMergedOutputModel(Map<String, Object> model,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        String fileName = "临时文件";
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        PdfExportParams entity = (PdfExportParams) model.get(PDFTemplateConstants.PARAMS);
        Class<?> pojoClass = (Class<?>) model.get(PDFTemplateConstants.CLASS);
        Collection<?> dataSet = (Collection<?>) model.get(PDFTemplateConstants.DATA_LIST);
        List<ExcelExportEntity> entityList = (List<ExcelExportEntity>) model.get(PDFTemplateConstants.ENTITY_LIST);
        if (entityList == null) {
            PdfExportUtil.exportPdf(entity, pojoClass, dataSet, baos);
        } else {
            PdfExportUtil.exportPdf(entity, entityList, (Collection<? extends Map<?, ?>>) dataSet,baos);
        }
        String userFileName = (String) model.get(PDFTemplateConstants.FILE_NAME);
        if (StringUtils.isNoneBlank(userFileName)) {
            fileName = userFileName;
        }
        if (isIE(request)) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName + ".pdf");
        writeToResponse(response, baos);
    }
}
