package com.bytter.system.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lifh
 * @description
 * @createDate 2019/7/22
 */
@Configuration
@ConfigurationProperties(prefix = "bytter.bfs.files")
@Data
public class AttachmentHandleContext {
    /**
     * 存储类型
     */
    private Type type;
    private LocalProperties local;
    private FTPProperties ftp;
    private MinioProperties minio;
    private OssProperties oss;
    private  SFTPProperties  sftp;

    /**
     * ftp登录名
     */
    String ftpUsername;
    /**
     * ftp登录密码
     */
    String ftpPassword;

    /**
     * ftp登录超时
     */
    Integer ftpTimeout;

    /**
     * 存储地址
     */
    String storagePath;

    String ftpSplitTempPath;

    Integer ftpPort;

    String root;

    /**
     * 存储到本地配置
     */
    @Data
    public static class LocalProperties {
        private String path;
    }

    @Data
    public static class FTPProperties {
        private String username;
        private String password;
        private String path;
        private Integer port;
        private Integer timeout;
        private String splitTempPath;
        private String flag = "ftp://";

    }


    @Data
    public static class SFTPProperties {
        private String username;
        private String password;
        private String path;
        private Integer port;
        private Integer timeout;
        private String splitTempPath;
        private String flag = "sftp://";
        private  String root;

    }

    @Data
    public static class MinioProperties {
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String endpoint;
    }

    @Data
    public static class OssProperties {
        private String secretId;
        private String secretKey;
        private String bucketName;
        private String region;
        private String cdnDomian;
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        FTP,
        SFTP,
        LOCAL,
        MINIO,
        OSS;
    }
}
