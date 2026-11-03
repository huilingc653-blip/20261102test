package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 银行类型
 * 
 * @author zhoul
 * @since 2017/5/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BankType {
    /**
     * 填写的值是被注入，该属性必须为String类型
     * 
     * @return 注入字段
     */
    String inject();
    /**
     * 实体中的属性
     *
     * @return 属性名称
     */
    String field() default "";
    boolean prefix() default false;
}
