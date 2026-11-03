package com.bytter.system.strategy;

import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.impl.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 根据配置文件生成附件上传使用的bean
 */
@Configuration
@Import({AttachmentHandleContext.class, ConfigService.class})
public class AttachmentStrategyConfig {
    /**
     * 根据配置文件生成不同附件上传处理类
     *
     * @param attachmentHandleContext 配置信息
     * @return 附件处理策略
     */
    @Bean
    public AttachmentStragegy attachmentStragegy(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        switch (attachmentHandleContext.getType()) {
            case LOCAL:
                return new LocalAttachmentStrategyImpl(attachmentHandleContext, configService);
            case MINIO:
                return new MinioAttachmentStrategyImpl(attachmentHandleContext, configService);
            case OSS:
                return new OssAttachmentStrategyImpl(attachmentHandleContext, configService);
            case FTP:
                return new FtpAttachmentStrategyImpl(attachmentHandleContext, configService);
            case SFTP:
                return new SftpAttachmentStrategyImpl(attachmentHandleContext, configService);
            default:
                return null;
        }
    }
}
