package com.bytter.poi.excel;
import com.bytter.poi.cache.HtmlCache;
import com.bytter.poi.excel.entity.ExcelToHtmlParams;
import com.bytter.poi.excel.entity.enmus.ExcelType;
import com.bytter.poi.excel.html.HtmlToExcelServer;
import com.bytter.poi.exception.excel.ExcelExportException;
import com.bytter.poi.exception.excel.enums.ExcelExportEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;
/**
 * Excel转html工具类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public final class ExcelToHtmlUtil {
    private ExcelToHtmlUtil() {
    }
    /**
     * Workbook转换成为Table
     *
     * @param wb
     *            Excel
     * @return html
     */
    public static String toTableHtml(Workbook wb) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, false, 0, null));
    }
    /**
     * Workbook转换成为Table
     *
     * @param wb
     *            Excel
     * @param sheetNum
     *            工作表位置
     * @return html
     */
    public static String toTableHtml(Workbook wb, int sheetNum) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, false, sheetNum, null));
    }
    /**
     * 转换成为完整界面,显示图片
     *
     * @param wb
     *            Excel
     * @return html
     */
    public static String toAllHtml(Workbook wb) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, true, 0, null));
    }
    /**
     * 转换成为完整界面,显示图片
     *
     * @param wb
     *            Excel
     * @param sheetNum
     *            工作表位置
     * @return html
     */
    public static String toAllHtml(Workbook wb, int sheetNum) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, true, sheetNum, null));
    }
    /**
     * 转换成为Table
     *
     * @param params
     *            参数
     * @return html
     */
    public static String excelToHtml(ExcelToHtmlParams params) {
        return HtmlCache.getHtml(params);
    }
    /**
     * Html 读取Excel
     *
     * @param html
     *            html
     * @param type
     *            excel类型
     * @return {@link Workbook}
     */
    public static Workbook htmlToExcel(String html, ExcelType type) {
        Workbook workbook = null;
        if (ExcelType.HSSF.equals(type)) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        new HtmlToExcelServer().createSheet(html, workbook);
        return workbook;
    }
    /**
     * Html 读取Excel
     *
     * @param is
     *            excel流
     * @param type
     *            excel类型
     * @return {@link Workbook}
     */
    public static Workbook htmlToExcel(InputStream is, ExcelType type) {
        try {
            return htmlToExcel(new String(IOUtils.toByteArray(is)), type);
        } catch (IOException e) {
            throw new ExcelExportException(ExcelExportEnum.HTML_ERROR, e);
        }
    }
}
