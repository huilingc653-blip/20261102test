package com.bytter.poi.excel.entity.enmus;
/**
 * Excel列值类型枚举
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public enum CellValueType {
    /**
     * 字符串
     */
    String,
    /**
     * 数值
     */
    Number,
    /**
     * 布尔类型
     */
    Boolean,
    /**
     * 日期
     */
    Date,
    /**
     * Element
     */
    TElement,
    /**
     * null
     */
    Null,
    /**
     * none
     */
    None;
    private CellValueType() {
    }
}
