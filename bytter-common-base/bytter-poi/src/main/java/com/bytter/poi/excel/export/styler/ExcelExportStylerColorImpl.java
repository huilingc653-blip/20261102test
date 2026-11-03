package com.bytter.poi.excel.export.styler;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * Created by zhouj on 2017/6/2.
 */
public class ExcelExportStylerColorImpl extends AbstractExcelExportStyler implements IExcelExportStyler {
    public ExcelExportStylerColorImpl(Workbook workbook) {
        super.createStyles(workbook);
    }
    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle titleStyle = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short) 24);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(headerColor);
        titleStyle.setAlignment((short) 2);
        titleStyle.setVerticalAlignment((short) 1);
        return titleStyle;
    }
    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setAlignment((short) 2);
        style.setVerticalAlignment((short) 1);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }
    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = this.workbook.createCellStyle();
        titleStyle.setFillForegroundColor(color);
        titleStyle.setAlignment((short) 2);
        titleStyle.setVerticalAlignment((short) 1);
        titleStyle.setFillPattern((short) 1);
        titleStyle.setWrapText(true);
        return titleStyle;
    }
    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setFillForegroundColor((short) 41);
        style.setFillPattern((short) 1);
        style.setAlignment((short) 2);
        style.setVerticalAlignment((short) 1);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }
}
