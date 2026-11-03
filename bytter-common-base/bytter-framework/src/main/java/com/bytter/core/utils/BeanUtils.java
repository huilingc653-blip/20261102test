package com.bytter.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.bytter.core.common.beancopy.BeanCopier;
import com.bytter.core.common.beancopy.BeanMap;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean转换工具
 *
 * @author 周亮
 * @since 0.0.5
 */
@Slf4j
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, BeanCopier> NONE_NULL_BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * MAP集合转成bean对象集合
     *
     * @param listResult 需要拷贝的集合
     * @param clazz      需要将map转换成的Class
     * @param <B>        泛型
     * @return 集合泛型
     */
    public static <B> List<B> copyMapListProperties(List listResult, Class<B> clazz) {
        List<B> sourceList = new ArrayList<>();
        Map<String, Object> cacheMap = new HashMap<>();
        // 遍历MAP结果集
        for (Object obj : listResult) {
            Map map = (Map) obj;
            B source;
            try {
                source = copyMapProperties(map, clazz, cacheMap);
            } catch (Exception ignore) {
                source = BeanUtil.toBean(map, clazz);
            }
            sourceList.add(source);
        }
        return sourceList;
    }

    /**
     * MAP转成bean对象集合
     *
     * @param map   集合
     * @param clazz 转换对象
     * @param <B>   泛型
     * @return bean对象
     */
    public static <B> B copyMapProperties(Map map, Class<B> clazz) {
        Map<String, Object> cacheMap = new HashMap<>();
        B res;
        try {
            res = copyMapProperties(map, clazz, cacheMap);
        } catch (Exception e) {
            res = BeanUtil.toBean(map, clazz);
        }
        return res;
    }

    /**
     * 递归获取当前class的所有父类中的属性
     *
     * @param clazz     需要获取的类
     * @param fieldList 字段集合
     */
    public static void getAllFields(Class<?> clazz, List<Field> fieldList) {
        Class superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            getAllFields(superClass, fieldList);
        }
        Field[] fields = clazz.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));
    }

    /**
     * 递归获取当前class的所有父类中的方法
     *
     * @param clazz      需要获取的类
     * @param methodList 方法集合
     */
    public static void getAllMethods(Class<?> clazz, List<Method> methodList) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            getAllMethods(superClass, methodList);
        }
        Method[] fields = clazz.getDeclaredMethods();
        methodList.addAll(Arrays.asList(fields));
    }

    /**
     * MAP转成bean对象集合
     *
     * @param map      集合
     * @param clazz    转换对象
     * @param cacheMap 缓存
     * @param <B>      泛型
     * @return bean对象
     * @author 周亮
     */
    @SuppressWarnings("unchecked")
    private static <B> B copyMapProperties(Map map, Class<B> clazz, Map<String, Object> cacheMap) {
        Assert.notNull(map, "map must not be null");
        Assert.notNull(clazz, "clazz must not be null");
        B instantiate = instantiate(clazz);
        BeanMap beanMap = BeanMap.create(instantiate, true);
        beanMap.putAll(map);
        return instantiate;
    }

    /**
     * 将MAP数据转换成Bean对象，处理CLOB数据。
     *
     * @param map     源数据。
     * @param beanMap 目标对象。
     * @return 转换后的数据。
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> convertMapData(Map map, BeanMap beanMap) {
        // 预处理 Map 中的值，将 CLOB 转换为 String
        Map<String, Object> convertedMap = new HashMap<>(map.size());
        for (Object entryObj : map.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj;
            String key = entry.getKey();
            Object value = entry.getValue();

            // 目标属性是 String，需要确保 value 能转为 String
            if (value != null) {
                value = convertValue(value);
            }
            convertedMap.put(key, value);
        }
        return convertedMap;
    }

    @Nullable
    private static Object convertValue(Object value) {
        // 1. 处理 JDBC Clob
        if (value instanceof Clob) {
            value = convertClobValue(value);
        }
        // 2. 处理 Hibernate 代理（如果项目中使用了 Hibernate）
        else if (value instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) value;
            Object real = proxy.getHibernateLazyInitializer().getImplementation();
            if (real instanceof Clob) {
                value = convertClobValue(real);
            } else {
                // 代理的不是 Clob，但目标是 String，可尝试 toString()
                value = real != null ? real.toString() : null;
            }
        }
        return value;
    }

    /**
     * 将MAP数据中的CLOB代理数据转换成String。
     *
     * @param dataMap 源数据。
     * @param key 键。
     * @return String。
     */
    public static String getMapClobToString(Map<String, Object> dataMap, String key) {
        if (dataMap.containsKey(key)) {
            Object value = dataMap.get(key);
            // 目标属性是 String，需要确保 value 能转为 String
            if (value != null) {
                // 处理 JDBC Clob
                value = convertValue(value);
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return null;
    }

    private static Object convertClobValue(Object value) {
        Clob clob = (Clob) value;
        try {
            long length = clob.length();
            if (length > 0 && length <= Integer.MAX_VALUE) {
                value = clob.getSubString(1, (int) length);
            } else {
                // 超大 CLOB，可改用流式读取（此处简化为 null 或抛出异常）
                value = null;
            }
        } catch (SQLException e) {
            // 记录日志并置 null
            log.error("读取 Clob 失败", e);
            value = null;
        }
        return value;
    }

    private static String getBeanCopierCacheKey(Class source, Class target) {
        return source.getName() + "|" + target.getName();
    }

    private static BeanCopier getBeanCopier(Class source, Class target) {
        String beanCopierCacheKey = getBeanCopierCacheKey(source, target);
        BeanCopier beanCopier = BEAN_COPIER_MAP.get(beanCopierCacheKey);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(source, target, false);
            BEAN_COPIER_MAP.put(beanCopierCacheKey, beanCopier);
        }
        return beanCopier;
    }

    private static BeanCopier getNoneNullBeanCopier(Class source, Class target) {
        String beanCopierCacheKey = getBeanCopierCacheKey(source, target);
        BeanCopier beanCopier = NONE_NULL_BEAN_COPIER_MAP.get(getBeanCopierCacheKey(source, target));
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(source, target, false, true);
            NONE_NULL_BEAN_COPIER_MAP.put(beanCopierCacheKey, beanCopier);
        }
        return beanCopier;
    }

    /**
     * 批量转换List
     *
     * @param source list集合
     * @param t      所成对象的class
     * @param <S>    泛型
     * @param <B>    泛型
     * @return 泛型集合
     * @throws BeansException 转换异常信息
     */
    public static <S, B> List<B> copyListProperties(List<S> source, Class<B> t) throws BeansException {
        List<B> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return list;
        }
        BeanCopier beanCopier = getBeanCopier(source.get(0).getClass(), t);
        for (Object o : source) {
            B instantiate = instantiate(t);
            beanCopier.copy(o, instantiate, null);
            list.add(instantiate);
        }
        return list;
    }

    /**
     * bean转map对象
     *
     * @param obj 对象
     * @return map集合
     */
    public static Map<String, Object> beanToMap(Object obj) {
        if (obj == null) {
            return MapUtil.empty();
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        BeanMap beanMap = BeanMap.create(obj);
        HashMap<String, Object> copy = new HashMap<>();
        for (Object key : beanMap.keySet()) {
            copy.put((String) key, beanMap.get(key));
        }
        return copy;
    }

    /**
     * 将两个对象集合合并，并且转换为Map集合
     *
     * @param listA 对象集合A
     * @param listB 对象集合B
     * @param key   根据对象中的哪个值进行合并
     * @return 合并后的Map集合
     */
    public static List<Map<String, Object>> mergeListBeanToMap(List<?> listA, List<?> listB, String key) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object objA : listA) {
            // 将对象转换为Map
            Map<String, Object> mapA = beanToMap(objA);
            for (Object objB : listB) {
                // 将对象转换为Map
                Map<String, Object> mapB = beanToMap(objB);
                // 如果对象A和对象B的值一直，则合并两个对象Map
                if (mapA.get(key).equals(mapB.get(key))) {
                    mapA.putAll(mapB);
                }
            }
            // 如果集合中不存在Map，则往List中追加合并后的Map
            if (!list.contains(mapA)) {
                list.add(mapA);
            }
        }
        return list;
    }

    /**
     * 将两个对象转换为map并做合并操作
     *
     * @param objA 对象A
     * @param objB 对象B
     * @param key  根据什么属性合并，属性名
     * @return 合并后的Map
     */
    public static Map<String, Object> mergeBeanToMap(Object objA, Object objB, String key) {
        if (objA == null) {
            return null;
        }
        Map<String, Object> mapA = beanToMap(objA);
        if (objB == null) {
            return mapA;
        }
        Map<String, Object> mapB = beanToMap(objB);
        if (mapA.get(key).equals(mapB.get(key))) {
            mapA.putAll(mapB);
        }
        return mapA;
    }

    /**
     * 把source和target相同属性的value复制到target中
     *
     * @param source 准备赋值对象
     * @param target 被赋值对象
     * @throws BeansException Bean转换异常错误
     */
    public static void copyNotNullProperties(Object source, Object target) throws BeansException {
        BeanCopier noneNullBeanCopier = getNoneNullBeanCopier(source.getClass(), target.getClass());
        noneNullBeanCopier.copy(source, target, null);
    }

    /**
     * 把source和target相同属性的value复制到target中 null值不复制
     *
     * @param source           准备赋值对象
     * @param target           被赋值对象
     * @param ignoreProperties 过滤属性字段
     * @throws BeansException Bean转换异常错误
     */
    public static void copyNotNullProperties(Object source, Object target, String... ignoreProperties)
            throws BeansException {
        copyProperties(source, target, null, ignoreProperties);
    }

    /**
     * 把source和target相同属性的value复制到target中
     *
     * @param source 准备赋值对象
     * @param t      被赋值对象
     * @param <B>    泛型
     * @return 泛型对象
     * @throws BeansException Bean转换异常错误
     */
    public static <B> B copyNewInstanceProperties(Object source, Class<B> t) throws BeansException {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), t);
        B instantiate = instantiateClass(t);
        beanCopier.copy(source, instantiate, null);
        return instantiate;
    }

    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName()
                        + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
                            readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (value != null) { // 只拷贝不为null的属性
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}
