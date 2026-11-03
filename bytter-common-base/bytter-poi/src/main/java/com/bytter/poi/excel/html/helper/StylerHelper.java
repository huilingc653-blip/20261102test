package com.bytter.poi.excel.html.helper;
import com.bytter.poi.util.PoiPublicUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/2.
 */
public class StylerHelper {
    private static String DEFAULTS_CLASS_CSS = ".excelDefaults {background-color: white;color: black;text-decoration: none;direction: ltr;text-transform: none;text-indent: 0;letter-spacing: 0;word-spacing: 0;white-space: normal;unicode-bidi: normal;vertical-align: 0;text-shadow: none;padding: 0;margin: 0;border-collapse: collapse;white-space: pre-wrap;word-wrap: break-word;word-break: break-all;}.excelDefaults td {padding: 1px 5px;border: 1px solid silver;border-color: #000000;text-align: center;vertical-align: middle;font-size: 12pt;}.excelDefaults .colHeader {background-color: silver;font-weight: bold;border: 1px solid black;text-align: center;padding: 1px 5px;}.excelDefaults .rowHeader {background-color: silver;font-weight: bold;border: 1px solid black;text-align: right;padding: 1px 5px;}";
    private static final String DEFAULTS_CLASS = "excelDefaults";
    private static final Map<Short, String> ALIGN = PoiPublicUtil.mapFor(new Object[] { Short.valueOf((short) 1),
            "left", Short.valueOf((short) 2), "center", Short.valueOf((short) 3), "right", Short.valueOf((short) 4),
            "left", Short.valueOf((short) 5), "left", Short.valueOf((short) 6), "center" });
    private static final Map<Short, String> VERTICAL_ALIGN = PoiPublicUtil.mapFor(new Object[] {
            Short.valueOf((short) 2), "bottom", Short.valueOf((short) 1), "middle", Short.valueOf((short) 0), "top" });
    private Formatter out;
    private Sheet sheet;
    private StylerHelper.HtmlHelper helper;
    private int sheetNum;
    private int cssRandom;
    public StylerHelper(Workbook wb, Formatter out, int sheetNum, int cssRandom) {
        this.out = out;
        this.sheetNum = sheetNum;
        this.cssRandom = cssRandom;
        if (wb instanceof HSSFWorkbook) {
            this.helper = new StylerHelper.HSSFHtmlHelper((HSSFWorkbook) wb);
        } else {
            if (!(wb instanceof XSSFWorkbook)) {
                throw new IllegalArgumentException("unknown workbook type: " + wb.getClass().getSimpleName());
            }
            this.helper = new StylerHelper.XSSFHtmlHelper((XSSFWorkbook) wb);
        }
        this.printInlineStyle(wb);
    }
    private void printInlineStyle(Workbook wb) {
        this.out.format("<style type=\"text/css\">%n", new Object[0]);
        this.printStyles(wb);
        this.prontFonts(wb);
        this.out.format("</style>%n", new Object[0]);
    }
    private void prontFonts(Workbook wb) {
        short i = 0;
        for (short le = wb.getNumberOfFonts(); i <= le; ++i) {
            Font font = wb.getFontAt(i);
            this.out.format(".%s .%s {%n", new Object[] { "excelDefaults", "font_" + i + "_" + this.cssRandom });
            this.fontStyle(font);
            this.out.format("}%n", new Object[0]);
        }
    }
    public void printStyles(Workbook wb) {
        if (DEFAULTS_CLASS_CSS == null) {
            DEFAULTS_CLASS_CSS = this.getDefaultsClassCss();
        }
        this.out.format(DEFAULTS_CLASS_CSS, new Object[0]);
        HashSet seen = new HashSet();
        this.sheet = wb.getSheetAt(this.sheetNum);
        Iterator rows = this.sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = (Row) rows.next();
            Iterator i$ = row.iterator();
            while (i$.hasNext()) {
                Cell cell = (Cell) i$.next();
                CellStyle style = cell.getCellStyle();
                if (!seen.contains(style)) {
                    this.printStyle(style);
                    seen.add(style);
                }
            }
        }
    }
    private String getDefaultsClassCss() {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        try {
            in = new BufferedReader(new InputStreamReader(StylerHelper.class.getResourceAsStream("excelStyle.css")));
            String e;
            while ((e = in.readLine()) != null) {
                formatter.format("%s%n", new Object[] { e });
            }
            String var5 = formatter.toString();
            return var5;
        } catch (IOException var14) {
            throw new IllegalStateException("Reading standard css", var14);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var13) {
                    throw new IllegalStateException("Reading standard css", var13);
                }
            }
            formatter.close();
        }
    }
    private void printStyle(CellStyle style) {
        this.out.format(".%s .%s {%n", new Object[] { "excelDefaults", this.styleName(style) });
        this.styleContents(style);
        this.out.format("}%n", new Object[0]);
    }
    private void styleContents(CellStyle style) {
        if (style.getAlignment() != 2) {
            this.styleOut("text-align", Short.valueOf(style.getAlignment()), ALIGN);
            this.styleOut("vertical-align", Short.valueOf(style.getAlignment()), VERTICAL_ALIGN);
        }
        this.helper.colorStyles(style, this.out);
    }
    private void fontStyle(Font font) {
        if (font.getBoldweight() >= 700) {
            this.out.format("  font-weight: bold;%n", new Object[0]);
        }
        if (font.getItalic()) {
            this.out.format("  font-style: italic;%n", new Object[0]);
        }
        this.out.format("  font-family: %s;%n", new Object[] { font.getFontName() });
        short fontheight = font.getFontHeightInPoints();
        if (fontheight == 9) {
            fontheight = 10;
        }
        this.out.format("  font-size: %dpt;%n", new Object[] { Integer.valueOf(fontheight) });
        this.helper.styleColor(this.out, "color", this.getColor(font));
    }
    private Color getColor(Font font) {
        return (Color) (this.helper instanceof StylerHelper.HSSFHtmlHelper
                ? ((HSSFWorkbook) this.sheet.getWorkbook()).getCustomPalette().getColor(font.getColor())
                : ((XSSFFont) font).getXSSFColor());
    }
    private String styleName(CellStyle style) {
        return style == null ? "" : String.format("style_%02x_%s",
                new Object[] { Short.valueOf(style.getIndex()), Integer.valueOf(this.cssRandom) });
    }
    private <K> void styleOut(String attr, K key, Map<K, String> mapping) {
        String value = (String) mapping.get(key);
        if (value != null) {
            this.out.format("  %s: %s;%n", new Object[] { attr, value });
        }
    }
    private class XSSFHtmlHelper implements StylerHelper.HtmlHelper {
        public XSSFHtmlHelper(XSSFWorkbook wb) {
        }
        @Override
        public void colorStyles(CellStyle style, Formatter out) {
            XSSFCellStyle cs = (XSSFCellStyle) style;
            this.styleColor(out, "background-color", cs.getFillForegroundXSSFColor());
            this.styleColor(out, "color", cs.getFont().getXSSFColor());
        }
        @Override
        public void styleColor(Formatter out, String attr, Color color) {
            XSSFColor xSSFColor = (XSSFColor) color;
            if (color != null && !xSSFColor.isAuto()) {
                byte[] rgb = xSSFColor.getRGB();
                if (rgb != null) {
                    out.format("  %s: #%02x%02x%02x;%n",
                            new Object[] { attr, Byte.valueOf(rgb[0]), Byte.valueOf(rgb[1]), Byte.valueOf(rgb[2]) });
                }
            }
        }
    }
    private class HSSFHtmlHelper implements StylerHelper.HtmlHelper {
        private final HSSFWorkbook wb;
        private final HSSFPalette colors;
        private HSSFColor HSSF_AUTO = new HSSFColor.AUTOMATIC();
        public HSSFHtmlHelper(HSSFWorkbook wb) {
            this.wb = wb;
            this.colors = wb.getCustomPalette();
        }
        @Override
        public void colorStyles(CellStyle style, Formatter out) {
            HSSFCellStyle cs = (HSSFCellStyle) style;
            out.format("  /* fill pattern = %d */%n", new Object[] { Short.valueOf(cs.getFillPattern()) });
            this.styleColor(out, "background-color", cs.getFillForegroundColor());
            this.styleColor(out, "color", this.colors.getColor(cs.getFont(this.wb).getColor()));
        }
        private void styleColor(Formatter out, String attr, short index) {
            HSSFColor color = this.colors.getColor(index);
            if (index != this.HSSF_AUTO.getIndex() && color != null) {
                short[] rgb = color.getTriplet();
                out.format("  %s: #%02x%02x%02x; /* index = %d */%n", new Object[] { attr, Short.valueOf(rgb[0]),
                        Short.valueOf(rgb[1]), Short.valueOf(rgb[2]), Short.valueOf(index) });
            } else {
                out.format("  /* %s: index = %d */%n", new Object[] { attr, Short.valueOf(index) });
            }
        }
        @Override
        public void styleColor(Formatter out, String attr, Color color) {
            if (color != null) {
                HSSFColor hSSFColor = (HSSFColor) color;
                short[] rgb = hSSFColor.getTriplet();
                out.format("  %s: #%02x%02x%02x; %n",
                        new Object[] { attr, Short.valueOf(rgb[0]), Short.valueOf(rgb[1]), Short.valueOf(rgb[2]) });
            }
        }
    }
    private interface HtmlHelper {
        void colorStyles(CellStyle var1, Formatter var2);
        void styleColor(Formatter var1, String var2, Color var3);
    }
}
