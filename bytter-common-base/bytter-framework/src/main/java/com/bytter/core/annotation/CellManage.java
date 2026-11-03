package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 管理单元注入注解
 *
 * @author 郑刚
 * @since 2020/05/15.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellManage {
    /**
     * 填写的值是被注入属性名，该属性必须为String类型
     *
     * @return 被注入字段名称
     */
    String inject();
    /**
     * 实体中的属性
     *
     * @return 属性名称
     */
    String field() default "";
}
