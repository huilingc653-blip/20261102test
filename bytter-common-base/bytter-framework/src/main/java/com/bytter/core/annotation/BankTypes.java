package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 多银行类别注入
 *
 * @author zuoy
 * @since 2020/02/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BankTypes {
    /**
     * 多个银行类别组合
     *
     * @return BankType注解数组
     */
    BankType[] value();
}
