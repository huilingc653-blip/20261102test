package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 交易编码注入注解
 *
 * @author zhoul
 * @since 2017/5/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TranCode {
    /**
     * 填写的值是被注入属性名，该属性必须为String类型
     *
     * @return 被注入字段名称
     */
    String inject();
}
