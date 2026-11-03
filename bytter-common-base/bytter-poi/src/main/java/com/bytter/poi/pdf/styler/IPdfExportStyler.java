package com.bytter.poi.pdf.styler;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
/**
 * PDF导出样式设置
 */
public interface IPdfExportStyler {
    /**
     * 获取文档格式
     * 
     * @return
     */
    public Document getDocument();
    /**
     * 设置Cell的样式
     * 
     * @param entity
     * @param text
     */
    public void setCellStyler(PdfPCell iCell, ExcelExportEntity entity, String text);
    /**
     * 获取字体
     * 
     * @param entity
     * @param text
     */
    public Font getFont(ExcelExportEntity entity, String text);
}
