package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 网点，单位注入
 *
 * @author zhoul
 * @since 2017/5/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Corp {
    String value() default "";
    /**
     * 填写的值是被注入，该属性必须为String类型
     *
     * @return 被注入字段名称
     */
    String inject() default "";
    /**
     * 实体中的属性
     *
     * @return 属性名称
     */
    String field() default "";
}
