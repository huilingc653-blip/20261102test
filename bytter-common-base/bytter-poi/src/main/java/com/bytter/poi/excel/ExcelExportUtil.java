package com.bytter.poi.excel;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.enmus.ExcelType;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.export.ExcelBatchExportServer;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.poi.excel.export.template.ExcelExportOfTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 导出工具类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public final class ExcelExportUtil {
    private ExcelExportUtil() {
    }
    /**
     * 导出excel
     *
     * @param entity
     *            表格标题属性
     * @param pojoClass
     *            Excel对象Class
     * @param dataSet
     *            Excel对象数据List
     * @return {@link Workbook}
     */
    public static Workbook exportBigExcel(ExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
        ExcelBatchExportServer batachServer = ExcelBatchExportServer.getExcelBatchExportServer(entity, pojoClass);
        return batachServer.appendData(dataSet);
    }
    /**
     * 关闭excel
     */
    public static void closeExportBigExcel() {
        ExcelBatchExportServer batachServer = ExcelBatchExportServer.getExcelBatchExportServer(null, null);
        batachServer.closeExportBigExcel();
    }
    /**
     * 设置导出Workbook
     *
     * @param entity
     *            参数
     * @param pojoClass
     *            导出对象
     * @param dataSet
     *            导出数据
     * @return {@link Workbook}
     */
    public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
        Object workbook;
        if (ExcelType.HSSF.equals(entity.getType())) {
            workbook = new HSSFWorkbook();
        } else if (dataSet.size() < 1000) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook();
        }
        (new ExcelExportServer()).createSheet((Workbook) workbook, entity, pojoClass, dataSet);
        return (Workbook) workbook;
    }
    /**
     * 设置导出Workbook
     *
     * @param entity
     *            参数
     * @param entityList
     *            导出对象集合
     * @param dataSet
     *            导出数据
     * @return {@link Workbook}
     */
    public static Workbook exportExcel(ExportParams entity, List<ExcelExportEntity> entityList,
            Collection<? extends Map<?, ?>> dataSet) {
        Object workbook;
        if (ExcelType.HSSF.equals(entity.getType())) {
            workbook = new HSSFWorkbook();
        } else if (dataSet.size() < 1000) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook();
        }
        (new ExcelExportServer()).createSheetForMap((Workbook) workbook, entity, entityList, dataSet);
        return (Workbook) workbook;
    }
    /**
     * 设置导出Workbook
     *
     * @param list
     *            导出MAP集合
     * @param type
     *            Excel类型
     * @return {@link Workbook}
     */
    public static Workbook exportExcel(List<Map<String, Object>> list, String type) {
        Object workbook;
        if (ExcelType.HSSF.equals(type)) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        Iterator i$ = list.iterator();
        while (i$.hasNext()) {
            Map map = (Map) i$.next();
            ExcelExportServer server = new ExcelExportServer();
            server.createSheet((Workbook) workbook, (ExportParams) map.get("title"), (Class) map.get("entity"),
                    (Collection) map.get("data"));
        }
        return (Workbook) workbook;
    }
    /**
     * 设置导出Workbook
     *
     * @param params
     *            导出模板参数
     * @param pojoClass
     *            导出对象
     * @param dataSet
     *            导出数据
     * @param map
     *            导出Map对照对象
     * @return {@link Workbook}
     */
    public static Workbook exportExcel(TemplateExportParams params, Class<?> pojoClass, Collection<?> dataSet,
            Map<String, Object> map) {
        return (new ExcelExportOfTemplateUtil()).createExcleByTemplate(params, pojoClass, dataSet, map);
    }
    /**
     * 设置导出Workbook
     *
     * @param params
     *            导出模板参数
     * @param map
     *            导出Map对照对象
     * @return {@link Workbook}
     */
    public static Workbook exportExcel(TemplateExportParams params, Map<String, Object> map) {
        return (new ExcelExportOfTemplateUtil()).createExcleByTemplate(params, (Class) null, (Collection) null, map);
    }
    /**
     * 根据Map创建对应的Excel
     * 
     * @param entity
     *            表格标题属性
     * @param entityList
     *            Map对象列表
     * @param dataSet
     *            Excel对象数据List
     */
    public static Workbook exportExcelByList(ExportParams entity, List<ExcelExportEntity> entityList,
            Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
        ;
        (new ExcelExportServer()).createSheetForMap(workbook, entity, entityList, dataSet);
        return workbook;
    }
    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }
}
