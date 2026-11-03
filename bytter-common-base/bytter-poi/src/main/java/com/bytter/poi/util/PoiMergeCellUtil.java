package com.bytter.poi.util;
import com.bytter.poi.excel.entity.params.MergeEntity;
import com.bytter.poi.exception.excel.ExcelExportException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import java.util.*;
/**
 * Created by zhouj on 2017/6/2.
 */
public final class PoiMergeCellUtil {
    private PoiMergeCellUtil() {
    }
    public static void mergeCells(Sheet sheet, int startRow, Integer... columns) {
        if (columns == null) {
            throw new ExcelExportException("至少需要处理1列");
        } else {
            HashMap mergeMap = new HashMap();
            for (int i = 0; i < columns.length; ++i) {
                mergeMap.put(columns[i], (Object) null);
            }
            mergeCells(sheet, mergeMap, startRow, sheet.getLastRowNum());
        }
    }
    public static void mergeCells(Sheet sheet, Map<Integer, int[]> mergeMap, int startRow) {
        mergeCells(sheet, mergeMap, startRow, sheet.getLastRowNum());
    }
    public static void mergeCells(Sheet sheet, Map<Integer, int[]> mergeMap, int startRow, int endRow) {
        HashMap mergeDataMap = new HashMap();
        if (mergeMap.size() != 0) {
            Set sets = mergeMap.keySet();
            label52: for (int i$ = startRow; i$ <= endRow; ++i$) {
                Row row = sheet.getRow(i$);
                Iterator index = sets.iterator();
                while (true) {
                    while (true) {
                        if (!index.hasNext()) {
                            continue label52;
                        }
                        Integer index1 = (Integer) index.next();
                        if (row != null && row.getCell(index1.intValue()) != null) {
                            String text = row.getCell(index1.intValue()).getStringCellValue();
                            if (StringPoiUtils.isNotEmpty(text)) {
                                hanlderMergeCells(index1, i$, text, mergeDataMap, sheet, row.getCell(index1.intValue()),
                                        (int[]) mergeMap.get(index1));
                            } else {
                                mergeCellOrContinue(index1, mergeDataMap, sheet);
                            }
                        } else if (mergeDataMap.get(index1) != null
                                && ((MergeEntity) mergeDataMap.get(index1)).getEndRow() == 0) {
                            ((MergeEntity) mergeDataMap.get(index1)).setEndRow(i$ - 1);
                        }
                    }
                }
            }
            if (mergeDataMap.size() > 0) {
                Iterator var11 = mergeDataMap.keySet().iterator();
                while (var11.hasNext()) {
                    Integer var12 = (Integer) var11.next();
                    sheet.addMergedRegion(new CellRangeAddress(((MergeEntity) mergeDataMap.get(var12)).getStartRow(),
                            ((MergeEntity) mergeDataMap.get(var12)).getEndRow(), var12.intValue(), var12.intValue()));
                }
            }
        }
    }
    private static void hanlderMergeCells(Integer index, int rowNum, String text,
            Map<Integer, MergeEntity> mergeDataMap, Sheet sheet, Cell cell, int[] delys) {
        if (mergeDataMap.containsKey(index)) {
            if (checkIsEqualByCellContents((MergeEntity) mergeDataMap.get(index), text, cell, delys, rowNum)) {
                ((MergeEntity) mergeDataMap.get(index)).setEndRow(rowNum);
            } else {
                sheet.addMergedRegion(new CellRangeAddress(((MergeEntity) mergeDataMap.get(index)).getStartRow(),
                        ((MergeEntity) mergeDataMap.get(index)).getEndRow(), index.intValue(), index.intValue()));
                mergeDataMap.put(index, createMergeEntity(text, rowNum, cell, delys));
            }
        } else {
            mergeDataMap.put(index, createMergeEntity(text, rowNum, cell, delys));
        }
    }
    private static void mergeCellOrContinue(Integer index, Map<Integer, MergeEntity> mergeDataMap, Sheet sheet) {
        if (mergeDataMap.containsKey(index) && ((MergeEntity) mergeDataMap.get(index))
                .getEndRow() != ((MergeEntity) mergeDataMap.get(index)).getStartRow()) {
            sheet.addMergedRegion(new CellRangeAddress(((MergeEntity) mergeDataMap.get(index)).getStartRow(),
                    ((MergeEntity) mergeDataMap.get(index)).getEndRow(), index.intValue(), index.intValue()));
            mergeDataMap.remove(index);
        }
    }
    private static MergeEntity createMergeEntity(String text, int rowNum, Cell cell, int[] delys) {
        MergeEntity mergeEntity = new MergeEntity(text, rowNum, rowNum);
        if (delys != null && delys.length != 0) {
            ArrayList list = new ArrayList(delys.length);
            mergeEntity.setRelyList(list);
            for (int i = 0; i < delys.length; ++i) {
                list.add(getCellNotNullText(cell, delys[i], rowNum));
            }
        }
        return mergeEntity;
    }
    private static boolean checkIsEqualByCellContents(MergeEntity mergeEntity, String text, Cell cell, int[] delys,
            int rowNum) {
        if (delys != null && delys.length != 0) {
            if (mergeEntity.getText().equals(text)) {
                for (int i = 0; i < delys.length; ++i) {
                    if (!getCellNotNullText(cell, delys[i], rowNum).equals(mergeEntity.getRelyList().get(i))) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return mergeEntity.getText().equals(text);
        }
    }
    private static String getCellNotNullText(Cell cell, int index, int rowNum) {
        String temp;
        Sheet var10000;
        for (temp = cell.getRow().getCell(index).getStringCellValue(); StringPoiUtils
                .isEmpty(temp); temp = var10000.getRow(rowNum).getCell(index).getStringCellValue()) {
            var10000 = cell.getRow().getSheet();
            --rowNum;
        }
        return temp;
    }
}
