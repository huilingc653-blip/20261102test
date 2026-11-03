package com.bytter.poi.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 导入实体对象
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ExcelTarget {
    /**
     * 定义excel导出ID 来限定导出字段
     */
    String value();
    /**
     * 导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符 全局设置,优先使用
     */
    double height() default 10;
    /**
     * 导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符 全局设置,优先使用
     */
    short fontSize() default 11;
}
