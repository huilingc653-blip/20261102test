package com.bytter.poi.util;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import java.lang.annotation.ElementType;
/**
 * 解决hibernate的Validator在weblogic环境下失效报错的问题
 *
 * @author zhouj
 * @since 2019/4/3.
 */
public class JPAIgnoreTraversableResolver implements TraversableResolver {
    @Override
    public boolean isReachable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
        return true;
    }
    @Override
    public boolean isCascadable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
        return true;
    }
}
