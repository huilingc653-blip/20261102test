package com.bytter.poi.handler.inter;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import java.util.Map;
/**
 * 导出处理接口
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public interface IExcelDataHandler {
    /**
     * 导出处理方法
     *
     * @param obj
     *            当前对象
     * @param name
     *            当前字段名称
     * @param value
     *            当前值
     * @return Object
     */
    Object exportHandler(Object obj, String name, Object value);
    /**
     * 获取需要处理的字段,导入和导出统一处理了, 减少书写的字段
     *
     * @return 需要处理的字段
     */
    String[] getNeedHandlerFields();
    /**
     * 导入处理方法 当前对象,当前字段名称,当前值
     *
     * @param obj
     *            当前对象
     * @param name
     *            当前字段名称
     * @param value
     *            当前值
     * @return Object
     */
    Object importHandler(Object obj, String name, Object value);
    /**
     * 设置需要处理的属性列表
     *
     * @param fields
     *            属性列表
     */
    void setNeedHandlerFields(String[] fields);
    /**
     * 设置Map导入,自定义 put
     *
     * @param map
     *            导入集合
     * @param originKey
     *            导入字段key值
     * @param value
     *            导入字段值
     */
    void setMapValue(Map<String, Object> map, String originKey, Object value);
    /**
     * 获取这个字段的 Hyperlink ,07版本需要,03版本不需要
     *
     * @param creationHelper
     *            creationHelper
     * @param obj
     *            导入对象
     * @param name
     *            导入字段name值
     * @param value
     *            导入字段value值
     * @return {@link Hyperlink}
     */
    Hyperlink getHyperlink(CreationHelper creationHelper, Object obj, String name, Object value);
}
