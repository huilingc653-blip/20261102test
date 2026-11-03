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
public @interface Corps {
    /**
     * 多个单位组合
     *
     * @return Corp注解数组
     */
    Corp[] value();
}
