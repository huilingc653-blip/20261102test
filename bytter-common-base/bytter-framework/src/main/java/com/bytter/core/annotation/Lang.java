package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 字典国际化注解类
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lang {
    /**
     * 指向需要国际化的实体字典对象
     *
     * @return 国际化实体类
     */
    Class<?> entity() default Object.class;
    /**
     * 属性名称
     *
     * @return 属性名称
     */
    String attrName() default "";
    /**
     * 该注解写在国际化的值上，填写的值是被注入国际化的属性
     *
     * @return 被注入字段名称
     */
    String inject();
}
