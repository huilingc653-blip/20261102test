package com.bytter.poi.excel.entity.enmus;
import com.bytter.poi.excel.export.styler.ExcelExportStylerBorderImpl;
import com.bytter.poi.excel.export.styler.ExcelExportStylerColorImpl;
import com.bytter.poi.excel.export.styler.ExcelExportStylerDefaultImpl;
/**
 * Excel样式枚举
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public enum ExcelStyleType {
    /**
     * 默认样式
     */
    NONE("默认样式", ExcelExportStylerDefaultImpl.class),
    /**
     * 边框样式
     */
    BORDER("边框样式", ExcelExportStylerBorderImpl.class),
    /**
     * 间隔行样式
     */
    COLOR("间隔行样式", ExcelExportStylerColorImpl.class);
    private String name;
    private Class<?> clazz;
    private ExcelStyleType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
    public Class<?> getClazz() {
        return this.clazz;
    }
    public String getName() {
        return this.name;
    }
}
