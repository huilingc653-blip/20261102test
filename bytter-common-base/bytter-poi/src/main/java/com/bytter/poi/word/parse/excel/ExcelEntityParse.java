package com.bytter.poi.word.parse.excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.export.base.ExportBase;
import com.bytter.poi.exception.word.WordExportException;
import com.bytter.poi.exception.word.enmus.WordExportEnum;
import com.bytter.poi.util.PoiPublicUtil;
import com.bytter.poi.word.entity.params.ExcelListEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import java.lang.reflect.Field;
import java.util.*;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
public class ExcelEntityParse extends ExportBase {
    public ExcelEntityParse() {
    }
    private static void checkExcelParams(ExcelListEntity entity) {
        if (entity.getList() == null || entity.getClazz() == null) {
            throw new WordExportException(WordExportEnum.EXCEL_PARAMS_ERROR);
        }
    }
    private int createCells(int index, Object t, List<ExcelExportEntity> excelParams, XWPFTable table, short rowHeight)
            throws Exception {
        XWPFTableRow row = table.createRow();
        row.setHeight(rowHeight);
        int maxHeight = 1;
        int cellNum = 0;
        int k = 0;
        ExcelExportEntity entity;
        int paramSize;
        for (paramSize = excelParams.size(); k < paramSize; ++k) {
            entity = (ExcelExportEntity) excelParams.get(k);
            if (entity.getList() == null) {
                Object var16 = this.getCellValue(entity, t);
                if (entity.getType() == 1) {
                    this.setCellValue(row, var16, cellNum++);
                }
            } else {
                Collection value = (Collection) entity.getMethod().invoke(t, new Object[0]);
                int listC = 0;
                for (Iterator i$ = value.iterator(); i$.hasNext(); ++listC) {
                    Object obj = i$.next();
                    this.createListCells(index + listC, cellNum, obj, entity.getList(), table);
                }
                cellNum += entity.getList().size();
                if (value != null && value.size() > maxHeight) {
                    maxHeight = value.size();
                }
            }
        }
        cellNum = 0;
        k = 0;
        for (paramSize = excelParams.size(); k < paramSize; ++k) {
            entity = (ExcelExportEntity) excelParams.get(k);
            if (entity.getList() != null) {
                cellNum += entity.getList().size();
            } else if (entity.isNeedMerge()) {
                table.setCellMargins(index, index + maxHeight - 1, cellNum, cellNum);
                ++cellNum;
            }
        }
        return maxHeight;
    }
    public void createListCells(int index, int cellNum, Object obj, List<ExcelExportEntity> excelParams,
            XWPFTable table) throws Exception {
        XWPFTableRow row;
        if (table.getRow(index) == null) {
            row = table.createRow();
            row.setHeight(this.getRowHeight(excelParams));
        } else {
            row = table.getRow(index);
        }
        int k = 0;
        for (int paramSize = excelParams.size(); k < paramSize; ++k) {
            ExcelExportEntity entity = (ExcelExportEntity) excelParams.get(k);
            Object value = this.getCellValue(entity, obj);
            if (entity.getType() == 1) {
                this.setCellValue(row, value, cellNum++);
            }
        }
    }
    private Map<String, Integer> getTitleMap(XWPFTable table, int index, int headRows) {
        if (index < headRows) {
            throw new WordExportException(WordExportEnum.EXCEL_NO_HEAD);
        } else {
            HashMap map = new HashMap();
            for (int j = 0; j < headRows; ++j) {
                List cells = table.getRow(index - j - 1).getTableCells();
                for (int i = 0; i < cells.size(); ++i) {
                    String text = ((XWPFTableCell) cells.get(i)).getText();
                    if (text == null || text.length() == 0) {
                        throw new WordExportException(WordExportEnum.EXCEL_HEAD_HAVA_NULL);
                    }
                    map.put(text, Integer.valueOf(i));
                }
            }
            return map;
        }
    }
    public void parseNextRowAndAddRow(XWPFTable table, int index, ExcelListEntity entity) {
        checkExcelParams(entity);
        Map titlemap = this.getTitleMap(table, index, entity.getHeadRows());
        try {
            Field[] e = PoiPublicUtil.getClassFields(entity.getClazz());
            ExcelTarget etarget = (ExcelTarget) entity.getClazz().getAnnotation(ExcelTarget.class);
            String targetId = null;
            if (etarget != null) {
                targetId = etarget.value();
            }
            ArrayList excelParams = new ArrayList();
            this.getAllExcelField((String[]) null, targetId, e, excelParams, entity.getClazz(), (List) null);
            this.sortAndFilterExportField(excelParams, titlemap);
            short rowHeight = this.getRowHeight(excelParams);
            Object t;
            for (Iterator its = entity.getList().iterator(); its
                    .hasNext(); index += this.createCells(index, t, excelParams, table, rowHeight)) {
                t = its.next();
            }
        } catch (Exception var12) {
            log.error(var12.getMessage(), var12);
        }
    }
    private void setCellValue(XWPFTableRow row, Object value, int cellNum) {
        if (row.getCell(cellNum++) != null) {
            row.getCell(cellNum - 1).setText(value == null ? "" : value.toString());
        } else {
            row.createCell().setText(value == null ? "" : value.toString());
        }
    }
    private void sortAndFilterExportField(List<ExcelExportEntity> excelParams, Map<String, Integer> titlemap) {
        for (int i = excelParams.size() - 1; i >= 0; --i) {
            if (((ExcelExportEntity) excelParams.get(i)).getList() != null
                    && ((ExcelExportEntity) excelParams.get(i)).getList().size() > 0) {
                this.sortAndFilterExportField(((ExcelExportEntity) excelParams.get(i)).getList(), titlemap);
                if (((ExcelExportEntity) excelParams.get(i)).getList().size() == 0) {
                    excelParams.remove(i);
                } else {
                    ((ExcelExportEntity) excelParams.get(i)).setOrderNum(i);
                }
            } else if (titlemap.containsKey(((ExcelExportEntity) excelParams.get(i)).getName())) {
                ((ExcelExportEntity) excelParams.get(i)).setOrderNum(i);
            } else {
                excelParams.remove(i);
            }
        }
        this.sortAllParams(excelParams);
    }
}
