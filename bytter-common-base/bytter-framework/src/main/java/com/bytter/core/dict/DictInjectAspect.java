package com.bytter.core.dict;

import com.bytter.core.annotation.Dict;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 字典字段字面转换切面
 * 用于自动将字典编码转换为字典名称并注入到指定字段
 * 支持嵌套对象、嵌套集合、嵌套Map的处理
 */
@Slf4j
@Aspect
@Component
public class DictInjectAspect {
    private static final Map<Class<?>, List<FieldInfo>> FIELD_CACHE = new ConcurrentHashMap<>();
    // 用于防止循环引用的集合
    private static final ThreadLocal<Set<Object>> PROCESSED_OBJECTS = ThreadLocal.withInitial(HashSet::new);

    @Autowired(required = false)
    private DictInjectProvider dictInjectProvider;

    /**
     * 定义切点：拦截所有Controller类的方法
     */
    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void controllerMethods() {
    }

    /**
     * 环绕通知：处理Controller方法返回值
     */
    @Around("controllerMethods()")
    public Object aroundControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 清理线程局部变量
        PROCESSED_OBJECTS.get().clear();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取完整泛型类型
        Type genericReturnType = signature.getMethod().getGenericReturnType();
        // 判断返回类型是否包含 DataTablesOutput
        boolean isDataTablesReturn = isDataTablesReturnType(genericReturnType);
        // 解析嵌套泛型
        Class<?> innermostType = extractInnermostGenericType(genericReturnType);
        Object[] args = joinPoint.getArgs();
        // 只有返回类型包含 DataTablesOutput 时才处理请求参数
        if (isDataTablesReturn && innermostType != null) {
            List<FieldInfo> fieldInfos = getFieldInfos(innermostType);
            if (EmptyUtils.isNotEmpty(fieldInfos)) {
                // 处理请求参数，转换 orderColumns 中的 field 值
                args = processRequestArgs(args, fieldInfos);
            }
        }
        // 执行原方法，获取返回值
        Object result = joinPoint.proceed(args);
        try {
            if (result == null) {
                return null;
            }
            // 处理返回值
            handleObject(result);
        } catch (Exception e) {
            log.error("字典值注入处理异常", e);
        } finally {
            // 清理线程局部变量
            PROCESSED_OBJECTS.remove();
        }
        return result;
    }
    /**
     * 判断返回类型是否包含 DataTablesOutput
     */
    private boolean isDataTablesReturnType(Type type) {
        if (type == null) {
            return false;
        }
        String typeName = type.getTypeName();
        return typeName.contains("DataTablesOutput");
    }

    /**
     * 处理请求参数，转换 orderColumns 中的 field 值
     */
    private Object[] processRequestArgs(Object[] args, List<FieldInfo> fieldInfos) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] processedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            processedArgs[i] = processSingleArg(args[i], fieldInfos);
        }
        return processedArgs;
    }

    /**
     * 处理单个参数
     */
    private Object processSingleArg(Object arg, List<FieldInfo> fieldInfos) {
        if (arg == null) {
            return null;
        }
        try {
            // 使用 Jackson 或 Gson 将对象转为 JSON 树进行处理
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.valueToTree(arg);
            // 处理 orderColumns 字段
            processOrderColumns(rootNode, fieldInfos);
            // 将处理后的 JSON 树转回原对象
            return objectMapper.treeToValue(rootNode, arg.getClass());
        } catch (Exception e) {
            log.warn("处理请求参数失败，使用原参数", e);
            return arg;
        }
    }

    /**
     * 处理 orderColumns 数组中的 field 值
     */
    private void processOrderColumns(JsonNode rootNode, List<FieldInfo> fieldInfos) {
        JsonNode orderColumnsNode = rootNode.get("orderColumns");
        if (orderColumnsNode == null || !orderColumnsNode.isArray()) {
            return;
        }
        for (JsonNode orderColumn : orderColumnsNode) {
            JsonNode fieldNode = orderColumn.get("field");
            if (fieldNode != null && fieldNode.isTextual()) {
                String originalField = fieldNode.asText();
                List<FieldInfo> list = fieldInfos.stream().filter(e -> e.getInjectFieldName().equals(originalField)).collect(Collectors.toList());
                if (list.isEmpty()) {
                    continue;
                }
                // 由于 JsonNode 是不可变的，需要使用 ObjectNode
                if (orderColumn.isObject()) {
                    ((ObjectNode) orderColumn).put("field", list.get(0).getOrgField());
                }
            }
        }
    }


    /**
     * 递归解析最内层的泛型类型
     */
    private Class<?> extractInnermostGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] actualTypeArguments = paramType.getActualTypeArguments();

            // 获取第一个泛型参数
            Type firstArg = actualTypeArguments[0];

            // 递归解析
            return extractInnermostGenericType(firstArg);
        } else if (type instanceof Class) {
            // 如果是普通类（非泛型），直接返回
            return (Class<?>) type;
        }

        return null;
    }

    /**
     * 统一处理对象
     */
    private void handleObject(Object obj) {
        if (obj == null) {
            return;
        }
        // 防止循环引用
        if (PROCESSED_OBJECTS.get().contains(obj)) {
            return;
        }
        PROCESSED_OBJECTS.get().add(obj);

        try {
            // 根据不同类型处理
            if (obj instanceof AjaxJson) {
                handleAjaxJson((AjaxJson) obj);
            } else if (obj instanceof DataTablesOutput) {
                handleDataTablesOutput((DataTablesOutput<?>) obj);
            } else if (obj instanceof List) {
                handleList((List<?>) obj);
            } else if (obj instanceof Map) {
                // Map类型不处理
            } else if (obj instanceof Collection) {
                handleCollection((Collection<?>) obj);
            } else if (isCustomObject(obj)) {
                handleSingleObject(obj);
            }
            // 基础类型和String等不处理
        } catch (Exception e) {
            log.error("处理对象异常: {}", e);
        }
    }

    /**
     * 处理AjaxJson响应
     */
    private void handleAjaxJson(AjaxJson ajaxJson) {
        Object data = ajaxJson.getObj();
        if (data != null) {
            handleObject(data);
        }
    }

    /**
     * 处理DataTablesOutput响应
     */
    private <T> void handleDataTablesOutput(DataTablesOutput<T> dataTablesOutput) {
        List<T> dataList = dataTablesOutput.getData();
        if (EmptyUtils.isNotEmpty(dataList)) {
            handleList(dataList);
        }
    }

    /**
     * 处理List响应
     */
    private void handleList(List<?> list) {
        if (EmptyUtils.isEmpty(list)) {
            return;
        }
        // 先收集所有对象的字典映射
        Map<String, Map<String, DictInDTO>> dictMap = collectDictMapFromList(list);
        // 遍历处理每个对象
        for (Object obj : list) {
            if (obj != null) {
                // 处理当前对象的字典注入
                injectDictValues(obj, dictMap);
                // 递归处理嵌套对象
                processNestedFields(obj);
            }
        }
    }

    /**
     * 处理Collection
     */
    private void handleCollection(Collection<?> collection) {
        if (EmptyUtils.isEmpty(collection)) {
            return;
        }
        for (Object item : collection) {
            if (item != null) {
                handleObject(item);
            }
        }
    }

    /**
     * 处理单个对象
     */
    private void handleSingleObject(Object obj) {
        if (obj == null) {
            return;
        }
        // 获取字典映射并注入
        Map<String, Map<String, DictInDTO>> dictMap = getDictMap(obj);
        if (!dictMap.isEmpty()) {
            injectDictValues(obj, dictMap);
        }
        // 递归处理嵌套字段
        processNestedFields(obj);
    }

    /**
     * 递归处理嵌套字段
     */
    private void processNestedFields(Object obj) {
        if (obj == null || isPrimitiveOrWrapperOrString(obj.getClass())) {
            return;
        }

        Class<?> clazz = obj.getClass();
        // 获取所有字段（包括父类）
        List<Field> allFields = getAllFields(clazz);

        for (Field field : allFields) {
            try {
                // 跳过静态字段和final字段
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (fieldValue == null) {
                    continue;
                }

                // 根据字段类型递归处理
                if (isCustomObject(fieldValue)) {
                    // 自定义对象
                    handleObject(fieldValue);
                } else if (fieldValue instanceof List) {
                    // List类型
                    handleList((List<?>) fieldValue);
                } else if (fieldValue instanceof Collection) {
                    // Collection类型
                    handleCollection((Collection<?>) fieldValue);
                } else if (fieldValue instanceof Map) {
                    // Map类型不处理
                } else if (fieldValue.getClass().isArray() && !fieldValue.getClass().getComponentType().isPrimitive()) {
                    // 对象数组
                    Object[] array = (Object[]) fieldValue;
                    handleList(Arrays.asList(array));
                }
            } catch (Exception e) {
                log.error("处理嵌套字段失败: 类={}, 字段={}", clazz.getSimpleName(), field.getName());
            }
        }
    }

    /**
     * 从对象列表中收集字典映射
     */
    private Map<String, Map<String, DictInDTO>> collectDictMapFromList(List<?> list) {
        if (EmptyUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Set<String> dictGroups = new HashSet<>();
        Set<Class<?>> processedClasses = new HashSet<>();

        // 收集所有对象的字典组
        for (Object obj : list) {
            if (obj == null) {
                continue;
            }
            Class<?> clazz = obj.getClass();
            if (processedClasses.contains(clazz)) {
                continue;
            }
            List<FieldInfo> fieldInfos = getFieldInfos(clazz);
            for (FieldInfo fieldInfo : fieldInfos) {
                dictGroups.add(fieldInfo.getDictGroup());
            }
            processedClasses.add(clazz);
        }

        if (dictGroups.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            return dictInjectProvider.listDictMap(new ArrayList<>(dictGroups));
        } catch (Exception e) {
            log.error("获取字典数据失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 获取字典映射
     */
    private Map<String, Map<String, DictInDTO>> getDictMap(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }

        List<FieldInfo> fieldInfos = getFieldInfos(obj.getClass());
        if (fieldInfos.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<String> dictGroups = new HashSet<>();
        for (FieldInfo fieldInfo : fieldInfos) {
            dictGroups.add(fieldInfo.getDictGroup());
        }

        try {
            return dictInjectProvider.listDictMap(new ArrayList<>(dictGroups));
        } catch (Exception e) {
            log.error("获取字典数据失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 为对象注入字典值
     */
    private void injectDictValues(Object obj, Map<String, Map<String, DictInDTO>> dictMap) {
        if (obj == null || dictMap.isEmpty()) {
            return;
        }
        List<FieldInfo> fieldInfos = getFieldInfos(obj.getClass());
        for (FieldInfo fieldInfo : fieldInfos) {
            injectDictValue(obj, fieldInfo, dictMap);
        }
    }

    /**
     * 注入单个字典值
     */
    private void injectDictValue(Object obj, FieldInfo fieldInfo, Map<String, Map<String, DictInDTO>> dictMap) {
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
            DictInDTO dictDTO = groupDict.get(dictKey);
            if (dictDTO == null) {
                return;
            }
            fieldInfo.getInjectField().setAccessible(true);
            fieldInfo.getInjectField().set(obj, dictDTO.getName());
        } catch (Exception e) {
            log.warn("字典值注入失败: 类={}, 字段={}, 字典组={}",
                    obj.getClass().getSimpleName(), fieldInfo.getField().getName(), fieldInfo.getDictGroup(), e);
        }
    }

    /**
     * 获取类的字段信息（带缓存）
     */
    private List<FieldInfo> getFieldInfos(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, this::scanDictFields);
    }

    /**
     * 扫描类的字典字段
     */
    private List<FieldInfo> scanDictFields(Class<?> clazz) {
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
                log.warn("字典注解配置不完整: 类={}, 字段={}", clazz.getSimpleName(), field.getName());
                continue;
            }

            try {
                Field injectField = findField(clazz, injectFieldName);
                if (injectField == null) {
                    log.warn("找不到注入字段: 类={}, 字段={}", clazz.getSimpleName(), injectFieldName);
                    continue;
                }
                field.setAccessible(true);
                fieldInfos.add(new FieldInfo(field, injectField, dictGroup, injectFieldName, field.getName()));
            } catch (Exception e) {
                log.warn("扫描字典字段失败: 类={}, 字段={}", clazz.getSimpleName(), field.getName(), e);
            }
        }

        return fieldInfos;
    }

    /**
     * 查找字段（包括父类）
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取所有字段（包括父类）
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 判断是否为自定义对象
     */
    private boolean isCustomObject(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return !(clazz.isPrimitive() ||
                clazz.isEnum() ||
                isPrimitiveOrWrapperOrString(clazz) ||
                clazz.getName().startsWith("java.") ||
                clazz.getName().startsWith("javax."));
    }

    /**
     * 判断是否为基本类型、包装类型或String
     */
    private boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Boolean.class ||
                clazz == Byte.class ||
                clazz == Character.class ||
                clazz == Short.class;
    }


}
