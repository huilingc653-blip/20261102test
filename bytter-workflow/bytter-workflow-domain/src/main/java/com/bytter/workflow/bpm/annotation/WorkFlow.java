package com.bytter.workflow.bpm.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 工作流注解
 *
 * @author zhoul
 * @since 2018/1/15.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkFlow {
    /**
     * 需要和“流程分类中所一定的key值一致”
     *
     * @return 流程分类Key
     */
    String[] value();
}
