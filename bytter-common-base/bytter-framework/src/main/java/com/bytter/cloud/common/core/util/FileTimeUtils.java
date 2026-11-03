package com.bytter.cloud.common.core.util;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * 文件时间工具类
 *
 * @author limm 2022-06-27
 * @since 1.0.0
 */
public class FileTimeUtils {
    
    private static final String DEFAULT_FILE_PATH = FilePathUtils.getFileName(true, "app", "logs", "%s.log");
    
    private static final Map<String, FileTimeBuilder> FILE_TIME_BUILDER_MAP = new HashMap<>();
    
    private static long loginTimes = 0;
    
    private FileTimeUtils() {
    
    }
    
    /**
     * 创建构建对象
     *
     * @param key 键值
     * @return 返回构建对象
     */
    public static FileTimeBuilder build(String key) {
        FileTimeBuilder fileTimeBuilder = FileTimeBuilder.build(++loginTimes);
        key = getDefaultKey(key);
        FILE_TIME_BUILDER_MAP.put(key, fileTimeBuilder);
        return fileTimeBuilder;
    }
    
    /**
     * 添加日志打印
     *
     * @param key 键值
     * @param message 格式化消息
     * @param objects 格式化对象
     * @return 返回构建对象
     */
    public static FileTimeBuilder log(String key, String message, Object... objects) {
        key = getDefaultKey(key);
        FileTimeBuilder fileTimeBuilder = FILE_TIME_BUILDER_MAP.get(key);
        if (fileTimeBuilder != null) {
            fileTimeBuilder.log(message, objects);
        }
        return fileTimeBuilder;
    }
    
    /**
     * 关闭操作
     *
     * @param key 键值
     * @param fileName 文件名称
     * @param outTime 超时时间
     * @param message 格式化消息
     * @param objects 格式化对象
     */
    public static void close(String key, String fileName, Long outTime, String message, Object... objects) {
        key = getDefaultKey(key);
        FileTimeBuilder fileTimeBuilder = FILE_TIME_BUILDER_MAP.remove(key);
        if (fileTimeBuilder != null) {
            fileTimeBuilder.close(fileName, outTime, message, objects);
        }
    }
    
    /**
     * 关闭操作
     *
     * @param key 键值
     * @param outTime 超时时间
     * @param message 格式化消息
     * @param objects 格式化对象
     */
    public static void close(String key, Long outTime, String message, Object... objects) {
        close(key, getDefaultFilePath(key), outTime, message, objects);
    }
    
    /**
     * 关闭操作
     *
     * @param key 键值
     * @param fileName 文件名称
     * @param outTime 超出时间
     */
    public static void close(String key, String fileName, Long outTime) {
        key = getDefaultKey(key);
        FileTimeBuilder fileTimeBuilder = FILE_TIME_BUILDER_MAP.remove(key);
        if (fileTimeBuilder != null) {
            fileTimeBuilder.close(fileName, outTime);
        }
    }
    
    /**
     * 关闭操作
     *
     * @param key 键值
     * @param outTime 超出时间
     */
    public static void close(String key, Long outTime) {
        close(key, getDefaultFilePath(key), outTime);
    }
    
    /**
     * 关闭操作
     *
     * @param key 键值
     */
    public static void close(String key) {
        close(key, getDefaultFilePath(key), null);
    }
    
    private static String getDefaultFilePath(String key) {
        return String.format(Locale.CHINA, DEFAULT_FILE_PATH, key);
    }
    
    private static String getDefaultKey(String key) {
        return String.format(Locale.CHINA, "%s__%s", Thread.currentThread().getName(), key);
    }
}
