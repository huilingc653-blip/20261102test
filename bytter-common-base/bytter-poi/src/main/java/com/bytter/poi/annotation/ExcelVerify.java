package com.bytter.poi.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 导入校验
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelVerify {
    boolean interHandler() default false;
    boolean isEmail() default false;
    boolean isMobile() default false;
    boolean isTel() default false;
    int maxLength() default -1;
    int minLength() default -1;
    boolean notNull() default false;
    String regex() default "";
    String regexTip() default "数据不符合规范";
}
