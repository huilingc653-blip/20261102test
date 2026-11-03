package com.bytter.system.strategy;

import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.AttachmentConfigConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.api.enums.AttachmentMessageEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.query.SysAttachmentForm;
import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.impl.FtpAttachmentStrategyImpl;
import com.bytter.system.strategy.impl.LocalAttachmentStrategyImpl;
import com.bytter.system.strategy.impl.MinioAttachmentStrategyImpl;
import com.bytter.system.strategy.impl.SftpAttachmentStrategyImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lifh
 * @description
 * @createDate 2019/7/22
 */
@Slf4j
public abstract class AbstractAttachmentStrategy implements AttachmentStragegy {
    protected AttachmentHandleContext attachmentHandleContext;
    protected ConfigService configService;
    protected final Integer readLength = 1024;
    private static final List<String> imageTag = Lists.newArrayList("jpg", "png", "gif", "bmp");

    public AbstractAttachmentStrategy() {
    }

    //todo 配置一次性查询
    protected void checkFile(MultipartFile file) {
        //判断文件大小
        ConfigDTO configDTO = configService.getDTOByTypeAndName(ConfigConstant.SYS, AttachmentConfigConstant.FILE_MAX_SIZE);
        if (StringUtils.isNotBlank(configDTO.getValue())) {
            if (file.getSize() > NumberUtils.toLong(configDTO.getValue()) * 1024 * 1024) {
                throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_MAX_SIZE);
            }
        }
        String originalFilename = StringUtils.isNotBlank(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName();
        String suffix = FileUtils.getExtend(originalFilename);
        if (StringUtils.isNotBlank(originalFilename)) {
            configDTO = configService.getDTOByTypeAndName(ConfigConstant.SYS, AttachmentConfigConstant.FILE_FORMAT);
            if (StringUtils.isNotBlank(configDTO.getValue())) {
                if (!configDTO.getValue().contains(suffix)) {
                    throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_FORMAT);
                }
            }
        }
    }


    /**
     * 构建上传目录
     *
     * @param timeName
     * @param modeName
     * @return
     */
    public String buildPath(MultipartFile file, String timeName, AttachmentCategoryEnum modeName) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) return null;
        String suffix = FileUtils.getExtend(originalFilename);
        //modeName
        String categoryName = modeName.getName();
        //重命名上传后的文件名
        String newFileName = StringUtils.getUUId();
        String reFileName = newFileName + CommonConstant.DOT + suffix;
        //定义上传路径
        String relative = File.separator + categoryName + File.separator + timeName +
                File.separator + reFileName;
        return FileUtils.filtrationPath(this.getStoragePath() + relative)
                .replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR, FileUtils.UNIX_FILE_SEPARATOR);
    }




    @Override
    public byte[] getAttachmentData(SysAttachment attachment) {
        byte[] buffer = new byte[readLength];
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream fis = getInputStream(attachment.getPath());
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            int length;
            while ((length = bis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception exception) {
            log.error("path=={}文件流获取错误", attachment.getPath(), exception);
        }
        return null;
    }



    /**
     * 存在分片上传时合并文件
     */
    protected void mergeFile(SysAttachmentForm attachmentForm, AttachmentCategoryEnum modeName) throws IOException {
        List<SysAttachment> sysAttachments = new ArrayList<>();
        ShiroUser user = new ShiroUser();
        Date date = DateUtils.getDate();
        String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
        String id = attachmentForm.getId();
        String groupId = attachmentForm.getPid();
        if (StringUtils.isBlank(groupId)) {
            groupId = StringUtils.getUUId();
        }
        int chunks = attachmentForm.getChunks();//如果大于1说明是分片处理
        int chunk = attachmentForm.getChunk();
        List<MultipartFile> files = attachmentForm.getFile();
        String fileName = attachmentForm.getFileName();
        String suffix = attachmentForm.getSuffix();
        for (MultipartFile file : files) {
            String categoryName = modeName.getName();
            String relative = File.separator + categoryName + File.separator + timeName + File.separator;
            String path = FileUtils.filtrationPath(this.getStoragePath() + relative)
                    .replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR, FileUtils.UNIX_FILE_SEPARATOR);
            String tempFileDir = path + fileName + id;
            File parentFileDir = new File(tempFileDir);
            if (!parentFileDir.exists()) {
                parentFileDir.mkdirs();
            }
            //分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
            File tempPartFile = new File(parentFileDir, fileName + "_" + chunk + ".part");
            file.transferTo(tempPartFile);
            //是否全部上传完成 所有分片都存在才说明整个文件上传完成
            boolean uploadDone = true;
            for (int i = 0; i < chunks; i++) {
                File partFile = new File(parentFileDir, fileName + "_" + i + ".part");
                if (!partFile.exists()) {
                    uploadDone = false;
                }
            }
            //所有分片文件都上传完成 将所有分片文件合并到一个文件中
            if (uploadDone) {
                //重命名上传后的文件名
                String newFileName = StringUtils.getUUId();
                String reFileName = newFileName + "." + suffix;
                //得到destFile 就是最终的文件
                File destFile = new File(path, reFileName);
                int i = 0;
                do {
                    File partFile = new File(parentFileDir, fileName + "_" + i + ".part");
                    try (FileOutputStream destOut = new FileOutputStream(destFile, true)) {
                        FileUtils.copyFile(partFile, destOut);
                    }
                    i++;
                } while (i < chunks);
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);
                //保存到数据库
                String relativePath = (relative + reFileName).replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR,
                        FileUtils.UNIX_FILE_SEPARATOR);
                SysAttachment attachment = new SysAttachment();
                attachment.setId(StringUtils.getUUId());
                attachment.setGroupId(groupId);
                attachment.setFileSize(BigDecimal.valueOf(destFile.length()));
                attachment.setName(fileName);
                attachment.setReName(newFileName);
                attachment.setSuffix(suffix);
                attachment.setType(file.getContentType());
                attachment.setPath(relativePath);
                attachment.setCategory(categoryName);
                attachment.setUserId(user.getId());
                attachment.setCreateDate(date);
                attachment.setCreateBy(user.getId());
                sysAttachments.add(attachment);
            } else {
                //临时文件创建失败
                if (chunk == chunks - 1) {
                    FileUtils.deleteDirectory(parentFileDir);
                }
            }
        }
    }
}
