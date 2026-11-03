package com.bytter.poi.util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
/**
 * 获取单元格的值
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class PoiCellUtil {
    /**
     * 读取单元格的值
     *
     * @param sheet
     *            工作表
     * @param row
     *            读取行
     * @param column
     *            读取列
     * @return 单元格值
     */
    public static String getCellValue(Sheet sheet, int row, int column) {
        String value = null;
        if (isMergedRegion(sheet, row, column)) {
            value = getMergedRegionValue(sheet, row, column);
        } else {
            Row rowData = sheet.getRow(row);
            Cell cell = rowData.getCell(column);
            value = getCellValue(cell);
        }
        return value;
    }
    /**
     * 获取合并单元格的值
     *
     * @param sheet
     *            工作表
     * @param row
     *            读取行
     * @param column
     *            读取列
     * @return 单元格值
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }
    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     *            工作表
     * @param row
     *            读取行
     * @param column
     *            读取列
     * @return 是合并单元格
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 获取单元格的值，并去掉左右空格
     *
     * @param cell
     *            单元格
     * @return 单元格值
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue()).trim();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula().trim();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()).trim();
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue().trim();
        }
    }
}
