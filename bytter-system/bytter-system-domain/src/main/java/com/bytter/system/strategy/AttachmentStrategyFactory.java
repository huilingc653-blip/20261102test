package com.bytter.system.strategy;

import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.impl.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lifh
 * @description 附件策略工厂，用于创建附件策略
 * @createDate 2019/7/22
 */
@Configuration
@Import({AttachmentHandleContext.class, ConfigService.class})
public class AttachmentStrategyFactory {

    public static AbstractAttachmentStrategy createAttachmentStrategy(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        //设置context的属性
        if ("ftp".equalsIgnoreCase(attachmentHandleContext.getType().toString())) {
            return new FtpAttachmentStrategyImpl(attachmentHandleContext,configService);
        }else if("sftp".equalsIgnoreCase(attachmentHandleContext.getType().toString())){
            return new SftpAttachmentStrategyImpl(attachmentHandleContext,configService);
        }else if("minio".equalsIgnoreCase(attachmentHandleContext.getType().toString())){
            return new MinioAttachmentStrategyImpl(attachmentHandleContext,configService);
        }
        return new LocalAttachmentStrategyImpl(attachmentHandleContext,configService);
    }

    public static AttachmentStragegy attachmentStragegy(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        //设置context的属性
        if (AttachmentHandleContext.Type.FTP.equals(attachmentHandleContext.getType())) {
            return new FtpAttachmentStrategyImpl(attachmentHandleContext, configService);
        } else if (AttachmentHandleContext.Type.SFTP.equals(attachmentHandleContext.getType())) {
            return new SftpAttachmentStrategyImpl(attachmentHandleContext, configService);
        } else if (AttachmentHandleContext.Type.MINIO.equals(attachmentHandleContext.getType())) {
            return new MinioAttachmentStrategyImpl(attachmentHandleContext, configService);
        } else if (AttachmentHandleContext.Type.OSS.equals(attachmentHandleContext.getType())) {
            return new OssAttachmentStrategyImpl(attachmentHandleContext, configService);
        }
        return new LocalAttachmentStrategyImpl(attachmentHandleContext, configService);
    }
}
