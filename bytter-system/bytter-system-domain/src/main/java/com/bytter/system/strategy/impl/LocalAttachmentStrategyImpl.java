package com.bytter.system.strategy.impl;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.api.enums.AttachmentMessageEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.AbstractAttachmentStrategy;
import com.bytter.system.strategy.AttachmentHandleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
/**
 * @author lifh
 * @description
 * @createDate 2019/7/22
 */
public class LocalAttachmentStrategyImpl extends AbstractAttachmentStrategy {
    protected  final Logger logger = LoggerFactory.getLogger(getClass());
    public LocalAttachmentStrategyImpl(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        super.configService = configService;
        this.attachmentHandleContext = attachmentHandleContext;
    }

    public InputStream previewAttachment(SysAttachment attachment) {
        logger.info("-------------- locale 附件预览！------------");
        FileInputStream fis = null;
        if (attachment != null) {
            String path = attachment.getPath();
            try {
                File file = new File(path);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                }else {
                    logger.error(String.format("附件%s不存在", path));
                }
            } catch (FileNotFoundException e) {
                logger.error(String.format("附件%s不存在", path));
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return fis;
    }
    /**
     * 不对数据库进行操作,批量上传文件
     *
     * @param multipartFileList
     * @param modeName
     */
    @Override
    public List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList, AttachmentCategoryEnum modeName, String groupId) {
        ArrayList<SysAttachment> attachments = new ArrayList<>(multipartFileList.size());
        String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
        for (MultipartFile file : multipartFileList) {
            checkFile(file);
            //构建文件上传新名字
            String path = super.buildPath(file, timeName, modeName);
            if (StringUtils.isBlank(path)) {
                continue;
            }
            File localFile = new File(path);
            try {
                // 判断目录是否存在
                if (!localFile.getParentFile().exists()) {
                    // 如果文件所在的目录不存在，则创建父目录
                    localFile.getParentFile().mkdirs();
                }
                file.transferTo(localFile);
                ShiroUser user = new ShiroUser();
                attachments.add(SysAttachment.create(user.getId(), groupId, modeName)
                        .appendOri(file).appendDest(path));
            } catch (IOException e) {
                logger.error("上传附件异常：", e);
                throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_UPLOAD);
            }
        }
        return attachments;
    }
    @Override
    public InputStream getInputStream(String path) throws IOException {
        return FileUtils.openInputStream(new File(path));
    }
    /**
     * 不对数据库进行操作，删除文件
     *
     * @param pathList
     */
    @Override
    public void deleteAttachments(List<String> pathList) {
        for (String path : pathList) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    @Override
    public String getStoragePath() {
        return attachmentHandleContext.getLocal().getPath();
    }

    @Override
    public String getAccessPath() {
        return "";
    }

    @Override
    public void downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response) {
        //TODOD: 下载是否记录
        if (attachment != null) {
            String path = attachment.getPath();
            File file = new File(path);
            if (file.exists()) {
                String fileName =  attachment.getName() + "." + attachment.getSuffix();
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
                        response.setHeader("content-disposition",   "attachment;filename="+fileName);
                    }else {
                        response.setHeader("content-disposition", "attachment;filename=" + fileName);
                    }
                } catch (UnsupportedEncodingException e) {
                    response.setHeader("content-disposition",   "attachment;filename="+fileName);
                    logger.error("名字获取失败", e);
                }
                byte[] buffer = new byte[readLength];

                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {
                    OutputStream os = response.getOutputStream();
                    int length;
                    while ((length = bis.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                } catch (FileNotFoundException e) {
                    logger.error("要下载的文件不存在", e);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.error(String.format("附件%s不存在", path));
            }
        }
    }
}
