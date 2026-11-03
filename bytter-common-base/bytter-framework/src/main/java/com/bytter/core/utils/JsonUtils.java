package com.bytter.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
@Slf4j
/**
 * Created by zhouj on 2017/9/6.
 */
public final class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * 序列化包含null
     */
    private static final ObjectMapper mapperNew = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapperNew.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapperNew.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    public static <T> T parseObject(final String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 序列化包含null属性
     * @param json
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T parseObjectWithNull(final String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        }
        try {
            return mapperNew.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param content
     *            json字符串
     * @return JsonNode
     *         <p>
     *         zhoul
     */
    public static JsonNode getJsonNode(String content) {
        if (null == content || "".equals(content)) {
            return null;
        }
        return getJsonNode(content.getBytes());
    }
    /**
     * 使用UTF-8编码解析JSON字符串，
     *
     * @param content
     *            json字符串
     * @return JsonNode
     */
    public static JsonNode getJsonNodeWithUtf8(String content) {
        return getJsonNode(content, StandardCharsets.UTF_8.name());
    }
    /**
     * 解析JSON字符串
     *
     * @param content
     *            json字符串
     * @param charset
     *            编码格式
     * @return JsonNode
     */
    public static JsonNode getJsonNode(String content, String charset) {
        if (null == content || "".equals(content)) {
            return null;
        }
        try {
            return getJsonNode(content.getBytes(charset));
        } catch (IOException e) {
            log.error("JSON转化失败：" + e.getMessage(), e);
        }
        return null;
    }
    /**
     * @param content
     *            json字符串字节码
     * @return JsonNode
     *         <p>
     *         zhoul
     */
    public static JsonNode getJsonNode(byte[] content) {
        try {
            return mapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Map json2Map(final String json) {
        return parseObject(json, Map.class);
    }
    public static Map json2MapWithNull(final String json) {
        return parseObjectWithNull(json, Map.class);
    }
    public static <T> List<T> json2List(final String json, Class<T> cls) {
        if (null == json || "".equals(json)) {
            return null;
        }
        try {
            JavaType t = mapper.getTypeFactory().constructParametricType(List.class, cls);
            return mapper.readValue(json, t);
        } catch (IOException e) {
            log.error("json字符串转换成List失败", e);
        }
        return Collections.emptyList();
    }
    public static String toJSONString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json 格式化时间异常：" + e);
        }
        return "";
    }
    /**
     * 序列化包含null属性
     * @param obj
     * @return
     */
    public static String toJSONStringWithNull(final Object obj) {
        try {
            return mapperNew.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json 格式化时间异常：" + e);
        }
        return "";
    }
    /**
     * 创建空objectNode节点
     * @return ObjectNode
     */
    public static ObjectNode createObjNode() {
        return mapper.createObjectNode();
    }
}
