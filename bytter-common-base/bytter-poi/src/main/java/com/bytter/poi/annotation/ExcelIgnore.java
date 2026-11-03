package com.bytter.poi.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 标记为excel 创建实体忽略,放置死循环的造成
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelIgnore {
}
