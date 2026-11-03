package com.bytter.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 国家信息注入
 *
 * @author qinl
 * @since 2019-07-19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Country {
    String inject();
    /**
     * 语言类型
     */
    LangEnum type() default LangEnum.ZH;
    /**
     * 语言类型枚举
     */
    enum LangEnum {
        /**
         * 中文
         */
        ZH,
        /**
         * 英文
         */
        EN
    }
}
