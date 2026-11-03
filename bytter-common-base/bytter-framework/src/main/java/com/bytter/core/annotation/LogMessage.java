package com.bytter.core.annotation;
import java.lang.annotation.*;
/**
 * 控制器日志记录注解
 *
 * @author zhouj
 * @since 2017-10-14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface LogMessage {
    /**
     * 日志属性映射名称
     **/
    String content() default "";
    /**
     * 日志级别<br>
     * info 1 warring 2 error 3
     *
     * @return 日志级别
     */
    short level() default 1;//
    /**
     * 日志类型<br>
     * 0 其他 1 登陆 2 退出 3 插入 4 删除 5 更新 6 上传 7 查询
     *
     * @return 日志类型
     */
    short type() default 0;
}
