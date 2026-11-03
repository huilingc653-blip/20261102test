package com.bytter.system.strategy.impl;

import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.AbstractAttachmentStrategy;
import com.bytter.system.strategy.AttachmentHandleContext;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class MinioAttachmentStrategyImpl extends AbstractAttachmentStrategy {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final MinioClient minioClient;
    private final String buckName;

    public InputStream previewAttachment(SysAttachment attachment) {
        String path = attachment.getPath();
        return getFileInputStream(path);
    }

    public InputStream getFileInputStream(String relativePath) {
        try {
            GetObjectArgs build = GetObjectArgs.builder().bucket(attachmentHandleContext.getMinio().getBucketName()).object(relativePath).build();
            return minioClient.getObject(build);
        } catch (Exception e) {
            logger.error("下载附件失败{}", e);
        }
        return null;
    }

    public MinioAttachmentStrategyImpl(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        super.configService = configService;
        this.attachmentHandleContext = attachmentHandleContext;
        AttachmentHandleContext.MinioProperties minio = attachmentHandleContext.getMinio();
        this.minioClient = MinioClient.builder()
                .endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(), minio.getSecretKey())
                .build();
        this.buckName = attachmentHandleContext.getMinio().getBucketName();
    }

    private void createBucket() throws Exception {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(buckName).build();
        if (!minioClient.bucketExists(bucketExistsArgs)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(buckName).build());
        }
    }

    @Override
    public List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList,
                                                 AttachmentCategoryEnum modeName, String groupId) throws Exception {
        List<SysAttachment> attachments = new ArrayList<>();
        createBucket();
        String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
        for (MultipartFile multipartFile : multipartFileList) {
            checkFile(multipartFile);
            String path = super.buildPath(multipartFile, timeName, modeName);
            if (StringUtils.isBlank(path)) {
                continue;
            }

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(buckName)
                    .contentType(multipartFile.getContentType())
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .object(path).build();
            minioClient.putObject(putObjectArgs);
            ShiroUser user = new ShiroUser();
            attachments.add(SysAttachment.create(user.getId(), groupId, modeName)
                    .appendOri(multipartFile).appendDest(path));
        }
        return attachments;
    }

    /**
     *
     * @param path 文件路径
     */
    @Override
    public InputStream getInputStream(String path) throws Exception {
        GetObjectArgs build = GetObjectArgs.builder().bucket(buckName).object(path).build();
        return minioClient.getObject(build);
    }


    @Override
    public void deleteAttachments(List<String> pathList) {
        List<DeleteObject> deleteObjects = pathList.stream().map(DeleteObject::new).collect(Collectors.toList());
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(buckName)
                .objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(deleteErrorResult -> {
            try {
                DeleteError deleteError = deleteErrorResult.get();
                log.info("info in deleting object " + deleteError.objectName() + "; " + deleteError.message());
            } catch (Exception exception) {
                log.error("文件删除失败", exception);
            }
        });
    }

    @Override
    public String getStoragePath() {
        return "";
    }

    @Override
    public String getAccessPath() {
        return attachmentHandleContext.getMinio().getEndpoint();
    }

    @Override
    public String getBuckName() {
        return this.buckName;
    }


    @Override
    public void downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (attachment != null) {
                String path = attachment.getPath();
                InputStream inputStream = this.getInputStream(path);
                if (inputStream != null) {
                    String fileName = attachment.getName() + "." + attachment.getSuffix();
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/force-download");
                    try {
                        fileName = URLEncoder.encode(fileName, "UTF8");
                        if (ServletUtils.isIE(request)) {
                            //判断后缀代码，避免在IE的情况下显示不正确，by zhoul 2018-02-01 17:36
                            String suffix = attachment.getSuffix();
                            if ("jpg".equals(suffix)) {
                                response.setContentType("image/jpeg");
                            } else if ("png".equals(suffix)) {
                                response.setContentType("image/png");
                            } else if ("gif".equals(suffix)) {
                                response.setContentType("image/gif");
                            } else if ("bmp".equals(suffix)) {
                                response.setContentType("image/bmp");
                            }
                            response.setHeader("content-disposition", "attachment;filename=" + fileName);
                        } else {
                            response.setHeader("content-disposition", "attachment;filename=" + fileName);
                        }
                    } catch (UnsupportedEncodingException e) {
                        response.setHeader("content-disposition", "attachment;filename=" + fileName);
                        logger.error("名字获取失败", e);
                    }
                    byte[] buffer = new byte[readLength];
                    OutputStream os = response.getOutputStream();
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                } else {
                    logger.error(String.format("附件%s不存在", path));
                }

            }
        } catch (Exception e) {
            logger.error(String.format("文件下载失败%s", e));
        }
    }

}
