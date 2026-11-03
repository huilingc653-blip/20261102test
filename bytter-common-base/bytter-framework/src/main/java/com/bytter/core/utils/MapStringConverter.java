package com.bytter.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Map与String互转工具类
 * 支持格式：JSON、查询字符串(URL编码)、键值对字符串
 */
public class MapStringConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT); // 美化JSON输出

    // ========== Map 转 String ==========
    
    public enum Format {
        JSON,           // {"key":"value"}
        QUERY_STRING,   // key=value&arr=1,2,3
        KEY_VALUE_PAIRS // key1=value1, key2=value2
    }

    /**
     * 将Map转换为指定格式的字符串
     * @param map 输入Map
     * @param format 目标格式
     * @return 格式化后的字符串
     */
    public static String mapToString(Map<?, ?> map, Format format) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        switch (format) {
            case JSON:
                return toJsonString(map);
            case QUERY_STRING:
                return toQueryString(map);
            case KEY_VALUE_PAIRS:
                return toKeyValueString(map);
            default:
                throw new IllegalArgumentException("不支持的格式: " + format);
        }
    }

    // Map转JSON字符串
    private static String toJsonString(Map<?, ?> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转换失败", e);
        }
    }

    // Map转查询字符串
    private static String toQueryString(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(entry -> {
                    String key = encode(entry.getKey().toString());
                    Object value = entry.getValue();
                    
                    // 处理数组/集合类型
                    if (value instanceof Iterable) {
                        return ((Collection<?>) value).stream()
                                .map(Object::toString)
                                .map(MapStringConverter::encode)
                                .map(v -> key + "=" + v)
                                .collect(Collectors.joining("&"));
                    } else if (value instanceof Object[]) {
                        return Stream.of((Object[]) value)
                                .map(Object::toString)
                                .map(MapStringConverter::encode)
                                .map(v -> key + "=" + v)
                                .collect(Collectors.joining("&"));
                    }
                    return key + "=" + encode(value.toString());
                })
                .collect(Collectors.joining("&"));
    }

    // Map转键值对字符串
    private static String toKeyValueString(Map<?, ?> map) {
        return map.entrySet().stream()
                .map(entry -> {
                    Object value = entry.getValue();
                    
                    // 处理数组/集合类型
                    if (value instanceof Iterable) {
                        return entry.getKey() + "=" + 
                                ((Collection<?>) value).stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(","));
                    } else if (value instanceof Object[]) {
                        return entry.getKey() + "=" + 
                                Stream.of((Object[]) value)
                                        .map(Object::toString)
                                        .collect(Collectors.joining(","));
                    }
                    return entry.getKey() + "=" + value;
                })
                .collect(Collectors.joining(", "));
    }

    // ========== String 转 Map ==========
    
    /**
     * 字符串转Map（自动检测格式）
     * @param input 输入字符串
     * @return 解析后的Map
     */
    public static Map<String, Object> stringToMap(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        input = input.trim();

        try {
            // 1. 尝试解析为JSON
            if (input.startsWith("{") && input.endsWith("}")) {
                return parseJson(input);
            }
            
            // 2. 尝试解析为查询字符串
            if (input.contains("=") && input.contains("&")) {
                return parseQueryString(input);
            }
            
            // 3. 尝试解析为键值对字符串
            if (input.contains("=")) {
                return parseKeyValueString(input);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("字符串解析失败: " + e.getMessage(), e);
        }

        throw new IllegalArgumentException("无法识别的字符串格式: " + input);
    }

    // 解析JSON字符串
    private static Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<LinkedHashMap<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }

    // 解析查询字符串
    private static Map<String, Object> parseQueryString(String query) {
        // 移除URL中的查询参数标记
        if (query.contains("?")) {
            query = query.substring(query.indexOf('?') + 1);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length != 2) continue;

            String key = decode(parts[0]);
            String value = decode(parts[1]);
            
            // 处理数组值 (重复key)
            if (map.containsKey(key)) {
                Object existing = map.get(key);
                if (existing instanceof String) {
                    map.put(key, new String[]{(String) existing, value});
                } else if (existing instanceof String[]) {
                    String[] arr = (String[]) existing;
                    String[] newArr = new String[arr.length + 1];
                    System.arraycopy(arr, 0, newArr, 0, arr.length);
                    newArr[arr.length] = value;
                    map.put(key, newArr);
                }
            } else {
                map.put(key, value);
            }
        }
        
        return map;
    }

    // 解析键值对字符串
    private static Map<String, Object> parseKeyValueString(String input) {
        Map<String, Object> map = new LinkedHashMap<>();
        
        // 支持多种分隔符：逗号、分号、换行符
        String[] pairs = input.split("[,;\\n]");
        
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;
            
            String[] parts = pair.split("=", 2);
            if (parts.length != 2) continue;
            
            String key = parts[0].trim();
            String value = parts[1].trim();
            
            // 检测数组值 (逗号分隔)
            if (value.contains(",")) {
                map.put(key, value.split("\\s*,\\s*"));
            } else {
                map.put(key, convertValue(value));
            }
        }
        
        return map;
    }

    // ========== 辅助方法 ==========
    
    // 智能值类型转换
    private static Object convertValue(String value) {
        if ("null".equalsIgnoreCase(value)) return null;
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                return value; // 保留原始字符串
            }
        }
    }

    // URL编码
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL编码失败", e);
        }
    }

    // URL解码
    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL解码失败", e);
        }
    }

    // ========== 使用示例 ==========
    public static void main(String[] args) {
        // 创建测试Map
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);
        data.put("active", true);
        data.put("scores", new int[]{90, 85, 95});
        data.put("params", null);

        System.out.println("原始Map: " + data);

        // Map转各种格式
        String json = mapToString(data, Format.JSON);
        System.out.println("\nJSON格式:\n" + json);

        String query = mapToString(data, Format.QUERY_STRING);
        System.out.println("\n查询字符串格式:\n" + query);

        String kv = mapToString(data, Format.KEY_VALUE_PAIRS);
        System.out.println("\n键值对格式:\n" + kv);

        // 字符串转Map
        System.out.println("\nJSON转Map:");
        System.out.println(stringToMap(json));

        System.out.println("\n查询字符串转Map:");
        System.out.println(stringToMap(query));

        System.out.println("\n键值对转Map:");
        System.out.println(stringToMap(kv));

        // 复杂字符串解析测试
        String complexInput = "id=1001, name=Jane Smith, roles=admin,user, active=true, balance=1250.75";
        System.out.println("\n复杂键值对解析:");
        System.out.println(stringToMap(complexInput));
    }
}