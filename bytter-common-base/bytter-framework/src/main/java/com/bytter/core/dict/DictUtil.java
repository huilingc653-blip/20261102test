package com.bytter.core.dict;

import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DictUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DictUtil.class);

    public static  void handleList(List<?> list,  Map<String, Map<String, DictInDTO>> dictMap) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }

        // 遍历处理每个对象
        for (Object obj : list) {
            if (obj != null) {
                // 处理当前对象的字典注入
                injectDictValues(obj, dictMap);
            }
        }
    }
    /**
     * 为对象注入字典值
     */
    public static void injectDictValues(Object obj, Map<String, Map<String, DictInDTO>> dictMap) {
        if (obj == null || dictMap.isEmpty()) {
            return;
        }
        List<FieldInfo> fieldInfos = scanDictFields(obj.getClass());
        for (FieldInfo fieldInfo : fieldInfos) {
            injectDictValue(obj, fieldInfo, dictMap);
        }
    }
    /**
     * 注入单个字典值
     */
    private static void injectDictValue(Object obj, FieldInfo fieldInfo, Map<String, Map<String, DictInDTO>> dictMap) {
        try {
            Object fieldValue = fieldInfo.getField().get(obj);
            if (fieldValue == null) {
                return;
            }
            String dictKey = fieldValue.toString();
            Map<String, DictInDTO> groupDict = dictMap.get(fieldInfo.getDictGroup());
            if (groupDict == null) {
                return;
            }
            DictInDTO DictInDTO = groupDict.get(dictKey);
            if (DictInDTO == null) {
                return;
            }
            fieldInfo.getInjectField().setAccessible(true);
            fieldInfo.getInjectField().set(obj, DictInDTO.getName());
        } catch (Exception e) {
            LOG.warn("字典值注入失败: 类={}, 字段={}, 字典组={}",
                    obj.getClass().getSimpleName(), fieldInfo.getField().getName(), fieldInfo.getDictGroup(), e);
        }
    }
    /**
     * 扫描类的字典字段
     */
    private static List<FieldInfo> scanDictFields(Class<?> clazz) {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        List<Field> allFields = getAllFields(clazz);
        for (Field field : allFields) {
            Dict dictAnnotation = field.getAnnotation(Dict.class);
            if (dictAnnotation == null) {
                continue;
            }
            String dictGroup = dictAnnotation.dictGroup();
            String injectFieldName = dictAnnotation.inject();
            if (EmptyUtils.isEmpty(dictGroup) || EmptyUtils.isEmpty(injectFieldName)) {
                LOG.warn("字典注解配置不完整: 类={}, 字段={}", clazz.getSimpleName(), field.getName());
                continue;
            }
            try {
                Field injectField = findField(clazz, injectFieldName);
                if (injectField == null) {
                    LOG.warn("找不到注入字段: 类={}, 字段={}", clazz.getSimpleName(), injectFieldName);
                    continue;
                }
                field.setAccessible(true);
                fieldInfos.add(new FieldInfo(field, injectField, dictGroup, injectFieldName, field.getName()));
            } catch (Exception e) {
                LOG.warn("扫描字典字段失败: 类={}, 字段={}", clazz.getSimpleName(), field.getName(), e);
            }
        }

        return fieldInfos;
    }
    public static List<String> getDictGroups(Class<?> clazz) {
        List<String> fieldInfos = new ArrayList<>();
        List<Field> allFields = getAllFields(clazz);
        for (Field field : allFields) {
            Dict dictAnnotation = field.getAnnotation(Dict.class);
            if (dictAnnotation == null) {
                continue;
            }
            String dictGroup = dictAnnotation.dictGroup();
            String injectFieldName = dictAnnotation.inject();
            if (EmptyUtils.isEmpty(dictGroup) || EmptyUtils.isEmpty(injectFieldName)) {
                LOG.warn("字典注解配置不完整: 类={}, 字段={}", clazz.getSimpleName(), field.getName());
                continue;
            }
            try {
                Field injectField = findField(clazz, injectFieldName);
                if (injectField == null) {
                    LOG.warn("找不到注入字段: 类={}, 字段={}", clazz.getSimpleName(), injectFieldName);
                    continue;
                }
                field.setAccessible(true);
                fieldInfos.add(dictGroup);
            } catch (Exception e) {
                LOG.warn("扫描字典字段失败: 类={}, 字段={}", clazz.getSimpleName(), field.getName(), e);
            }
        }
        return fieldInfos;
    }
    /**
     * 获取所有字段（包括父类）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 查找字段（包括父类）
     */
    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }


}
