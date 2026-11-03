package com.bytter.poi.pdf.styler;
import java.io.IOException;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
/**
 * 默认的PDFstyler 实现
 */
public class PdfExportStylerDefaultImpl implements IPdfExportStyler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfExportStylerDefaultImpl.class);
    @Override
    public Document getDocument() {
        return new Document(PageSize.A4, 36, 36, 24, 36);
    }
    @Override
    public void setCellStyler(PdfPCell iCell, ExcelExportEntity entity, String text) {
        iCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        iCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }
    @Override
    public Font getFont(ExcelExportEntity entity, String text) {
        try {
            // 用以支持中文
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            return font;
        } catch (DocumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        Font font = new Font(FontFamily.UNDEFINED);
        return font;
    }
}
