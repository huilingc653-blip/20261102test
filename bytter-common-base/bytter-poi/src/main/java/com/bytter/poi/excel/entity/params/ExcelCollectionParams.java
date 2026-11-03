package com.bytter.poi.excel.entity.params;
import java.util.Map;
/**
 * Excel导入集合
 * 
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelCollectionParams {
    private String name;
    private String excelName;
    private Class<?> type;
    private Map<String, ExcelImportEntity> excelParams;
    public ExcelCollectionParams() {
    }
    public Map<String, ExcelImportEntity> getExcelParams() {
        return this.excelParams;
    }
    public String getName() {
        return this.name;
    }
    public Class<?> getType() {
        return this.type;
    }
    public void setExcelParams(Map<String, ExcelImportEntity> excelParams) {
        this.excelParams = excelParams;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(Class<?> type) {
        this.type = type;
    }
    public String getExcelName() {
        return this.excelName;
    }
    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }
}
