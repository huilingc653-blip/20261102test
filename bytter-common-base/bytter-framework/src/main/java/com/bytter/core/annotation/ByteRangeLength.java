package com.bytter.core.annotation;
import com.bytter.core.annotation.impl.ByteRangeLengthImpl;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @author zhouj
 * @since 2017/11/23.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ByteRangeLengthImpl.class)
public @interface ByteRangeLength {
    /**
     * 字节长度值
     *
     * @return 字节长度值
     */
    int value() default 0;
    /**
     * 消息内容
     *
     * @return 消息内容
     */
    String message() default "length is to long";
    /**
     * 分组
     *
     * @return 分组
     */
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ByteRangeLength[] value();
    }
}
