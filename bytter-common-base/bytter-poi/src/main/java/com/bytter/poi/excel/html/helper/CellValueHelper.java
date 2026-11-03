package com.bytter.poi.excel.html.helper;
import com.google.common.xml.XmlEscapers;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/2.
 */
public class CellValueHelper {
    private boolean is07;
    private int cssRandom;
    private Map<String, String> fontCache = new HashMap();
    public CellValueHelper(Workbook wb, int cssRandom) {
        this.cssRandom = cssRandom;
        if (wb instanceof HSSFWorkbook) {
            this.is07 = false;
        } else {
            if (!(wb instanceof XSSFWorkbook)) {
                throw new IllegalArgumentException("unknown workbook type: " + wb.getClass().getSimpleName());
            }
            this.is07 = true;
            this.cacheFontInfo(wb);
        }
    }
    private void cacheFontInfo(Workbook wb) {
        short i = 0;
        for (short le = wb.getNumberOfFonts(); i < le; ++i) {
            Font font = wb.getFontAt(i);
            this.fontCache.put(font.getBoldweight() + "_" + font.getItalic() + "_" + font.getFontName() + "_"
                    + font.getFontHeightInPoints() + "_" + font.getColor(), font.getIndex() + "");
        }
    }
    public String getHtmlValue(Cell cell) {
        if (4 != cell.getCellType() && 0 != cell.getCellType()) {
            return 1 == cell.getCellType() ? (cell.getRichStringCellValue().numFormattingRuns() == 0
                    ? XmlEscapers.xmlContentEscaper().escape(cell.getStringCellValue())
                    : (this.is07 ? this.getXSSFRichString((XSSFRichTextString) cell.getRichStringCellValue())
                            : this.getHSSFRichString((HSSFRichTextString) cell.getRichStringCellValue())))
                    : "";
        } else {
            cell.setCellType(1);
            return cell.getStringCellValue();
        }
    }
    private String getHSSFRichString(HSSFRichTextString rich) {
        int nums = rich.numFormattingRuns();
        StringBuilder sb = new StringBuilder();
        String text = rich.toString();
        boolean currentIndex = false;
        sb.append(text.substring(0, rich.getIndexOfFormattingRun(0)));
        for (int i = 0; i < nums; ++i) {
            sb.append("<span ");
            sb.append("class=\'font_" + rich.getFontOfFormattingRun(i));
            sb.append("_");
            sb.append(this.cssRandom);
            sb.append("\'>");
            int var7 = rich.getIndexOfFormattingRun(i);
            if (i < nums - 1) {
                sb.append(XmlEscapers.xmlContentEscaper()
                        .escape(text.substring(var7, rich.getIndexOfFormattingRun(i + 1))));
            } else {
                sb.append(XmlEscapers.xmlContentEscaper().escape(text.substring(var7, text.length())));
            }
            sb.append("</span>");
        }
        return sb.toString();
    }
    private String getXSSFRichString(XSSFRichTextString rich) {
        int nums = rich.numFormattingRuns();
        StringBuilder sb = new StringBuilder();
        String text = rich.toString();
        boolean currentIndex = false;
        int lastIndex = 0;
        for (int i = 1; i <= nums; ++i) {
            sb.append("<span ");
            try {
                sb.append("class=\'font_" + this.getFontIndex(rich.getFontOfFormattingRun(i - 1)));
                sb.append("_");
                sb.append(this.cssRandom);
                sb.append("\'");
            } catch (Exception var9) {
                ;
            }
            sb.append(">");
            int var10 = rich.getIndexOfFormattingRun(i) == -1 ? text.length() : rich.getIndexOfFormattingRun(i);
            sb.append(XmlEscapers.xmlContentEscaper().escape(text.substring(lastIndex, var10)));
            sb.append("</span>");
            lastIndex = var10;
        }
        return sb.toString();
    }
    private String getFontIndex(XSSFFont font) {
        return (String) this.fontCache.get(font.getBoldweight() + "_" + font.getItalic() + "_" + font.getFontName()
                + "_" + font.getFontHeightInPoints() + "_" + font.getColor());
    }
}
