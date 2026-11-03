package com.bytter.poi.handler.inter;
/**
 * Excel读取类
 *
 * @param <T>
 *            导入对象
 * @author zhouj
 * @since 2017/6/2.
 */
public interface IExcelReadRowHanlder<T> {
    /**
     * 处理
     * 
     * @param var1
     *            对象
     */
    void hanlder(T var1);
}
