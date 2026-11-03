package com.bytter.biz.erp.bizStrategy;


import com.bytter.erp.exception.BtVerificationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;
import java.util.Map;

/**
 * Description: 业务策略接口
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
public interface BizStrategy  {
    /**
     * 服务代码
     * @return 服务代码
     */
    String getServiceCode();

    /**
     * 版本号
     * @return 版本号
     */
    default String getVersion(){
        return "1.0";
    };

    /**
     * 检查策略是否匹配
     * @param serviceCode 服务代码
     * @param version 版本号
     * @return 是否匹配
     */
    default boolean matches(String serviceCode, String version) {
        return this.getServiceCode().equals(serviceCode) && this.getVersion().equals(version);
    }

    /**
     * 业务实现接口
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String,String> doBiz(Map<String,String> params) throws Exception;

    default void appendMsg(StringBuilder result, String msg) {
        if (result.length() > 0){
            result.append(";");
        }
        result.append(msg);
    }

    /**
     * 安全解析JSON到指定类型
     * @param json JSON字符串
     * @param clazz 目标类型
     * @return 转换结果或错误信息
     */
    default <T> T safeParseJson(String json, Class<T> clazz) throws Exception {
        try {
            ObjectMapper strictMapper = new ObjectMapper();
            strictMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, false);
            strictMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
            strictMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);

            T obj = strictMapper.readValue(json, clazz);
            if (obj == null) {
                throw new IllegalArgumentException("报文为空");
            }
            return obj;
        } catch (Exception e) {
            throw new BtVerificationException("报文解析失败: 请求报文格式不正确: "+e.getMessage());
        }
    }

    /**
     * 安全解析JSON数组到指定类型的列表
     * @param json JSON字符串
     * @param clazz 目标类型
     * @return 转换结果或错误信息
     */
    default <T> List<T> safeParseJsonList(String json, Class<T> clazz) throws Exception {
        try {
            ObjectMapper strictMapper = new ObjectMapper();
            strictMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, false);
            strictMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
            strictMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);

            // 使用 CollectionType 构建参数化类型
            CollectionType listType = strictMapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz);

            List<T> list = strictMapper.readValue(json, listType);
            if (list == null) {
                throw new IllegalArgumentException("报文为空");
            }
            // 检查列表中的空对象
            for (int i = 0; i < list.size(); i++) {
                T item = list.get(i);
                if (item == null) {
                    throw new IllegalArgumentException("列表中第 " + (i + 1) + " 个对象为空");
                }
            }
            return list;
        } catch (Exception e) {
            throw new BtVerificationException("报文解析失败: 请求报文格式不正确: "+e.getMessage());
        }
    }

}
