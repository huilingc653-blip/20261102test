package com.bytter.poi.excel.export;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.enmus.ExcelType;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.export.styler.IExcelExportStyler;
import com.bytter.poi.exception.excel.ExcelExportException;
import com.bytter.poi.exception.excel.enums.ExcelExportEnum;
import com.bytter.poi.util.PoiPublicUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import java.lang.reflect.Field;
import java.util.*;
/**
 * 提供批次插入服务
 */
@Slf4j
public class ExcelBatchExportServer extends ExcelExportServer {
    private static ThreadLocal<ExcelBatchExportServer> THREAD_LOCAL = new ThreadLocal<ExcelBatchExportServer>();
    private Workbook workbook;
    private Sheet sheet;
    private List<ExcelExportEntity> excelParams;
    private ExportParams entity;
    private int titleHeight;
    private Drawing patriarch;
    private short rowHeight;
    private int index;
    public void init(ExportParams entity, Class<?> pojoClass) {
        log.debug("ExcelBatchExportServer only support SXSSFWorkbook");
        entity.setType(ExcelType.XSSF);
        workbook = new SXSSFWorkbook();
        this.entity = entity;
        super.type = entity.getType();
        createSheet(workbook, entity, pojoClass);
        if (entity.getMaxNum() == 0) {
            entity.setMaxNum(1000000);
        }
        insertDataToSheet(workbook, entity, excelParams, null, sheet);
    }
    public void createSheet(Workbook workbook, ExportParams entity, Class<?> pojoClass) {
        if (workbook == null || entity == null || pojoClass == null) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        try {
            excelParams = new ArrayList<ExcelExportEntity>();
            if (entity.isAddIndex()) {
                excelParams.add(indexExcelEntity(entity));
            }
            // 得到所有字段
            Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            String targetId = etarget == null ? null : etarget.value();
            getAllExcelField(entity.getExclusions(), targetId, fileds, excelParams, pojoClass, null);
            sortAllParams(excelParams);
            try {
                sheet = workbook.createSheet(entity.getSheetName());
            } catch (Exception e) {
                // 重复遍历,出现了重名现象,创建非指定的名称Sheet
                sheet = workbook.createSheet();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }
    public Workbook appendData(Collection<?> dataSet) {
        if (sheet.getLastRowNum() + dataSet.size() > entity.getMaxNum()) {
            sheet = workbook.createSheet();
            index = 0;
        }
        Iterator<?> its = dataSet.iterator();
        while (its.hasNext()) {
            Object t = its.next();
            try {
                index += createCells(patriarch, index, t, excelParams, sheet, workbook, rowHeight);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e);
            }
        }
        return workbook;
    }
    @Override
    protected void insertDataToSheet(Workbook workbook, ExportParams entity, List<ExcelExportEntity> entityList,
            Collection<?> dataSet, Sheet sheet) {
        try {
            dataHanlder = entity.getDataHanlder();
            if (dataHanlder != null && dataHanlder.getNeedHandlerFields() != null) {
                needHanlderList = Arrays.asList(dataHanlder.getNeedHandlerFields());
            }
            // 创建表格样式
            setExcelExportStyler(
                    (IExcelExportStyler) entity.getStyle().getConstructor(Workbook.class).newInstance(workbook));
            patriarch = sheet.createDrawingPatriarch();
            List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
            if (entity.isAddIndex()) {
                excelParams.add(indexExcelEntity(entity));
            }
            excelParams.addAll(entityList);
            sortAllParams(excelParams);
            this.index = entity.isCreateHeadRows() ? createHeaderAndTitle(entity, sheet, workbook, excelParams) : 0;
            titleHeight = index;
            setCellWith(excelParams, sheet);
            rowHeight = getRowHeight(excelParams);
            setCurrentIndex(1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }
    public static ExcelBatchExportServer getExcelBatchExportServer(ExportParams entity, Class<?> pojoClass) {
        if (THREAD_LOCAL.get() == null) {
            ExcelBatchExportServer batchServer = new ExcelBatchExportServer();
            batchServer.init(entity, pojoClass);
            THREAD_LOCAL.set(batchServer);
        }
        return THREAD_LOCAL.get();
    }
    public void closeExportBigExcel() {
        if (entity.getFreezeCol() != 0) {
            sheet.createFreezePane(entity.getFreezeCol(), 0, entity.getFreezeCol(), 0);
        }
        mergeCells(sheet, excelParams, titleHeight);
        // 创建合计信息
        addStatisticsRow(getExcelExportStyler().getStyles(true, null), sheet);
        THREAD_LOCAL.remove();
    }
}
