package com.bytter.system.api;
import java.io.InputStream;
/**
 * 电子回单文件操作接口
 *
 * @author zuoy
 * @since 2019-12-04
 */
public interface IBillFileService {
    /**
     * 上传文件
     *
     * @param relativePath 文件相对路径
     * @param input        文件流
     */
    void uploadFile(String relativePath, InputStream input);
    /**
     * 上传文件
     *
     * @param relativePath 文件相对路径
     * @param bytes        文件流
     */
    void uploadFile(String relativePath, byte[] bytes);
    /**
     * 下载文件
     *
     * @param relativePath 文件相对路径
     * @return 电子回单
     */
    InputStream getFileInputStream(String relativePath)throws Exception;
    /**
     * 下载文件
     *
     * @param relativePath 文件相对路径
     * @return 电子回单
     */
    byte[] getFileByte(String relativePath);
    
    /**
     * 下载文件
     *
     * @param relativePath 文件相对路径
     * @return 电子回单
     */
    byte[] getFileByteOss(String relativePath);
    /**
     * 删除文件
     *
     * @param relativePath 文件相对路径
     */
    void deleteFile(String relativePath);
}
