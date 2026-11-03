package com.bytter.core.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * @author zhoul
 * @since 2017/5/20.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BtEntityTitle {
    /**
     * 名称
     *
     * @return 名称
     */
    String name();
}
