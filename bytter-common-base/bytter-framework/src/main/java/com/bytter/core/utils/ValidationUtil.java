package com.bytter.core.utils;
import javax.validation.*;
import java.lang.annotation.ElementType;
import java.util.Set;
/**
 * Created by zhouj on 2018/5/19.
 */
public class ValidationUtil {
    private final static Validator validator;
    static {
        // ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        ValidatorFactory factory = Validation.byDefaultProvider().configure()
                .traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
        validator = factory.getValidator();
    }
    public static String validation(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        if (set.size() > 0) {
            return getValidateErrMsg(set);
        }
        return null;
    }
    private static String getValidateErrMsg(Set<ConstraintViolation<Object>> set) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            builder.append(constraintViolation.getMessage()).append(",");
        }
        return builder.substring(0, builder.length() - 1).toString();
    }
    /**
     * 解决hibernate的Validator在weblogic环境下失效报错的问题 zhouj
     */
    public static class JPAIgnoreTraversableResolver implements TraversableResolver {
        @Override
        public boolean isReachable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
            return true;
        }
        @Override
        public boolean isCascadable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
            return true;
        }
    }
}
