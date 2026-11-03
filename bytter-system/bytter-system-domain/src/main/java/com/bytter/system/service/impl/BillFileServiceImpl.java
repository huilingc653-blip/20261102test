package com.bytter.system.service.impl;


import com.bytter.core.common.exception.BusinessException;
import com.bytter.system.api.IBillFileService;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.strategy.impl.FtpAttachmentStrategyImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author zuoy
 * @since 2019-12-04
 */
@Slf4j
@Service
@DubboService
public class BillFileServiceImpl implements IBillFileService {

    // 直接注入 FTP 实现，而不是接口
    @Autowired
    @Qualifier("ftpAttachmentStrategyImpl")  // 指定具体的 bean 名称
    private FtpAttachmentStrategyImpl attachmentStragegy;  // 改为具体类型

    @Override
    public void uploadFile(String relativePath, InputStream input) {
        throw new BusinessException("暂时不需要实现");
    }

    @Override
    public void uploadFile(String relativePath, byte[] bytes) {
        throw new BusinessException("暂时不需要实现");
    }

    @Override
    public InputStream getFileInputStream(String relativePath) throws Exception {
        String path = attachmentStragegy.getStoragePath() + relativePath;
        return attachmentStragegy.getInputStream(path);
    }

    @Override
    public byte[] getFileByte(String relativePath) {
        SysAttachment sysAttachment = new SysAttachment();
        sysAttachment.setPath(attachmentStragegy.getStoragePath()+relativePath);
        return attachmentStragegy.getAttachmentData(sysAttachment);
    }

    /**
     *  //ftp 获取文件数据
     * @param relativePath 文件相对路径
     * @return
     */
    @Override
    public byte[] getFileByteOss(String relativePath) {
        SysAttachment sysAttachment = new SysAttachment();
        sysAttachment.setPath(attachmentStragegy.getStoragePath()+relativePath);
        return attachmentStragegy.getAttachmentData(sysAttachment);
    }
    @Override
    public void deleteFile(String relativePath) {
        throw new BusinessException("暂时不需要实现");
    }


}
