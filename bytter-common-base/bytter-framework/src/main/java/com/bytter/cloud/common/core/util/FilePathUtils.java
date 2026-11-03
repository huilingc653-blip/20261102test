package com.bytter.cloud.common.core.util;
import java.io.File;
/**
 * 文件路径工具
 *
 * @author limm 2022-06-27
 * @since 1.0.0
 */
public final class FilePathUtils {
    
    private FilePathUtils() {
    
    }
    
    /**
     * 获取文件路径打印信息
     *
     * @param message 信息
     * @return 返回文件路径打印信息
     */
    public static String getFilePathPrint(String message) {
        if (message == null || message.equalsIgnoreCase("")) {
            return message;
        }
        return message.replaceAll(String.format("%s%s", File.separator, File.separator),
                String.format("%s%s%s%s", File.separator, File.separator, File.separator, File.separator));
    }
    
    /**
     * 获取文件路径
     *
     * @param havePrefix 是否有前缀
     * @param names 目录名称
     * @return 返回文件路径
     */
    public static String getFileName(boolean havePrefix, String... names) {
        String fileNameStr = getFileNameSuffix(havePrefix, names);
        return fileNameStr.substring(0, fileNameStr.lastIndexOf(File.separator));
    }
    
    /**
     * 获取文件路径
     *
     * @param names 目录名称
     * @return 返回文件路径
     */
    public static String getFileName(String... names) {
        return getFileName(true, names);
    }
    
    /**
     * 获取有后缀的文件路径
     *
     * @param havePrefix 是否有前缀
     * @param names 目录名称
     * @return 返回文件路径
     */
    public static String getFileNameSuffix(boolean havePrefix, String... names) {
        StringBuilder fileName = new StringBuilder();
        if (havePrefix) {
            fileName.append(File.separator);
        }
        if (names != null && names.length > 0) {
            for (String name: names) {
                if (name == null || name.equalsIgnoreCase("")) {
                    continue;
                }
                fileName.append(name).append(File.separator);
            }
        }
        return fileName.toString();
    }
    
    /**
     * 获取有后缀的文件路径
     *
     * @param names 目录名称
     * @return 返回文件路径
     */
    public static String getFileNameSuffix(String... names) {
        return getFileName(true, names);
    }
}
