package com.bytter.core.utils;
import com.bytter.core.common.exception.AbstractBytterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
@Slf4j
/**
 * 文件操作工具类
 *
 * @author 张代浩
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    public final static String UNIX_FILE_SEPARATOR = "/";
    public final static String WINDOWS_FILE_SEPARATOR = "\\\\";
    public static final String SEPARATOR = ".";
    /**
     * 获取文件扩展名
     *
     * @param filename
     *            文件名
     * @return 文件扩展名
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }
    /**
     * 获取文件扩展名
     *
     * @param filename
     *            文件名
     * @param defExt
     *            默认扩展名
     * @return 文件扩展名
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }
    /**
     * 获取文件名称[不含后缀名]
     *
     * @param fileName
     *            文件名
     * @return String
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }
    /**
     * 文件复制 方法摘要：这里一句话描述方法的用途
     *
     * @param inputFile
     *            准备复制路径
     * @param outputFile
     *            被复制路径
     * @throws FileNotFoundException
     *             文件找不到异常
     */
    public static void copyFile(String inputFile, String outputFile) throws FileNotFoundException {
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while ((temp = fis.read(buf)) != -1) {
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 删除指定的文件
     *
     * @param strFileName
     *            指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);
        if (!fileDelete.exists() || !fileDelete.isFile()) {
            return false;
        }
        return fileDelete.delete();
    }
    /**
     * 防止文件名中文乱码含有空格时%20
     *
     * @param fileName
     *            文件名
     * @return 设定文件
     */
    public static String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnFileName;
    }
    /**
     * 根据url获取文件
     *
     * @param url
     *            文件路径
     * @return byte[]
     */
    public static byte[] getFileByte(String url) {
        InputStream fileis = null;
        ByteArrayOutputStream baos = null;
        try {
            try {
                ClassPathResource classPathResource = new ClassPathResource(url);
                fileis=classPathResource.getInputStream();
            } catch (FileNotFoundException var19) {
                String weburl = null;
                try {
                    weburl = FileUtils.class.getClassLoader().getResource("").toURI().getPath();
                    weburl = weburl.replace("WEB-INF/classes/", "");
                    weburl = weburl.replace("file:/", "");
                    String root = weburl + url;
                    fileis = new FileInputStream(root);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                    throw new AbstractBytterException(e1);
                }
            }
            baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fileis.read(bytes)) > -1) {
                baos.write(bytes, 0, length);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        } finally {
            try {
                if (fileis != null) {
                    fileis.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.error(fileis + "这个路径文件没有找到,请查询");
        return null;
    }
    /**
     * 过滤 \b \t \n \f \r 避免因为路径中保存相关字符而被转义
     *
     * @param path
     *            文件路径
     * @return 路径
     */
    public static String filtrationPath(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        return path.replaceAll("\b", "/b").replaceAll("\t", "/t").replaceAll("\n", "/n").replaceAll("\f", "/f")
                .replaceAll("\r", "/r");
    }
}
