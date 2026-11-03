package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 银行联行号注入
 *
 * @author zhoul
 * @since 2017/5/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InputBankInfos {
    String valField() default "bankCode";
    /**
     * 数据字典：需要国际化的表的名称
     *
     * @return InputBankInfo注解数组
     */
    InputBankInfo[] value();
}
