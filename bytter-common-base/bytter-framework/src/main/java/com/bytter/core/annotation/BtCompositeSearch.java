package com.bytter.core.annotation;
import java.lang.annotation.*;
/**
 * where查询处理注解
 *
 * @author zhouj
 * @since 2017/5/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
@Documented
public @interface BtCompositeSearch {
    /**
     * 字段名
     *
     * @return 字段名
     */
    String[] fieldName() default "";
    /**
     * 操作代码
     *
     * @return 操作代码
     */
    String[] operCode() default "";
    /**
     * 排序值
     *
     * @return 排序值
     */
    String orderBy() default "";
}
