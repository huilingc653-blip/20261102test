package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 币别注入
 *
 * @author zuoy
 * @since 2020/02/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Currencys {
    /**
     * 多个单位组合
     *
     * @return Corp注解数组
     */
    Currency[] value();
}
