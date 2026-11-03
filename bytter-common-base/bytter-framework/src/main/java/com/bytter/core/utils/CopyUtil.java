package com.bytter.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;

/**
 * 复制工具
 *
 * @author loveCamille
 */
public class CopyUtil {

    private CopyUtil() {}

    /**
     *
     * 比较字段值，如果不一样则替换（忽略空值，如果数据来源字段为 null 或 empty，则跳过）
     * @author loveCamille
     * @param source 源对象（数据来源）
     * @param target 目标对象（需要被修改）
     * @param ignoreNull 忽略空值
     * @return {@link T}
    */
    public static <T> T replaceIfDiff(T source, T target, boolean ignoreNull) {
        if (source == null) {
            return target;
        }
        if (target == null) {
            return source;
        }
        if (source.getClass() != target.getClass()) throw new IllegalStateException("source and target are not same");
        Class<?> clazz = source.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            Object sourceValue = ReflectUtil.getFieldValue(source, field.getName());
            if (ignoreNull && (ObjectUtil.isEmpty(sourceValue) || ObjectUtil.isNull(sourceValue))) continue;
            Object targetValue = ReflectUtil.getFieldValue(target, field.getName());
            if (ObjectUtil.equals(sourceValue, targetValue)) continue;
            ReflectUtil.setFieldValue(target, field.getName(), sourceValue);
        }
        return target;
    }

    /**
     * 比较字段值，如果不一样则替换
     * @author loveCamille
     * @param source 源对象（数据来源）
     * @param target 目标对象（需要被修改）
     * @return {@link T} 字段值被替换后的对象
     */
    public static <T> T replaceIfDiff(T source, T target) {
        return replaceIfDiff(source, target, true);
    }

}
