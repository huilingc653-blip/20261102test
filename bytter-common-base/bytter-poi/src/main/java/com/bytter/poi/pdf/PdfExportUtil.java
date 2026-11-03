package com.bytter.poi.pdf;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.pdf.entity.PdfExportParams;
import com.bytter.poi.pdf.export.PdfExportServer;
import com.itextpdf.text.Document;
/**
 * PDF 导出工具类
 */
public class PdfExportUtil {
    /**
     * 根据注解导出数据
     * 
     * @param entity
     *            表格标题属性
     * @param pojoClass
     *            PDF对象Class
     * @param dataSet
     *            PDF对象数据List
     */
    public static Document exportPdf(PdfExportParams entity, Class<?> pojoClass, Collection<?> dataSet,
            OutputStream outStream) {
        return new PdfExportServer(outStream, entity).createPdf(entity, pojoClass, dataSet);
    }
    /**
     * 根据Map创建对应的PDF
     * 
     * @param entity
     *            表格标题属性
     * @param entityList
     *            PDF对象Class
     * @param dataSet
     *            PDF对象数据List
     */
    public static Document exportPdf(PdfExportParams entity, List<ExcelExportEntity> entityList,
            Collection<? extends Map<?, ?>> dataSet, OutputStream outStream) {
        return new PdfExportServer(outStream, entity).createPdfByExportEntity(entity, entityList, dataSet);
    }
}
