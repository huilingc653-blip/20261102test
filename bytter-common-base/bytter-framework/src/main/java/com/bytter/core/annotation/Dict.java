package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 基础字典、数据字典国际化
 *
 * @author zhoul
 * @since 2017-06-01
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 数据字典：需要国际化的表的名称
     *
     * @return 表名
     */
    String tableName() default "";
    /**
     * 数据字典：需要国际化的字段值的名称
     *
     * @return 字段值名称
     */
    String valueFieldName() default "";
    /**
     * 数据字典：要显示名称的字段名
     *
     * @return 显示字段名
     */
    String showFiledName() default "";
    /**
     * 基础字典组
     *
     * @return 字典组
     */
    String dictGroup() default "";
    /**
     * 是否多个值
     *
     * @return true是false否 默认false
     */
    boolean multi() default false;
    /**
     * 字符串多个值的分隔符
     *
     * @return 分隔符 默认英文","
     */
    String separator() default ",";
    /**
     * 填写的值是被注入国际化的属性，该属性必须为String类型
     *
     * @return 被注入字段名称
     */
    String inject();
}
