package com.bytter.system.strategy.impl;


import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.system.api.constant.AttachmentConfigConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.api.enums.AttachmentMessageEnum;
import com.bytter.system.entity.Config;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.AbstractAttachmentStrategy;
import com.bytter.system.strategy.AttachmentHandleContext;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project     : bt-parent
 * Copyright   : Copyright (c) 2020
 *
 * @author : GYQ
 * Classname   : SftpAttachmentStrategyImpl
 * Created     : 2021-05-17 09:41
 * Description :sftp文件上传
 */
public class SftpAttachmentStrategyImpl extends AbstractAttachmentStrategy {
    // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private static final String SessionStrictHostKeyChecking = "no";
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public InputStream previewAttachment(SysAttachment attachment) {
        logger.info("-------------- Sftp 附件预览！------------");
        FileInputStream fis = null;
        if (attachment != null) {
            String path = attachment.getPath();
            ChannelSftp sftp = new ChannelSftp();
            OutputStream outputStream = null;

            BufferedInputStream bis = null;
            try {
                sftp = this.createSftp();
                sftp.cd(attachmentHandleContext.getRoot());
                //文件的路径 /common/20241122/637a50ee67e8433b909c25d96c1ee388.png
//                File file = new File(path.substring(path.lastIndexOf("/") + 1));
                File file = new File(attachmentHandleContext.getFtpSplitTempPath() + path.substring(path.lastIndexOf("/") + 1));
                outputStream = new FileOutputStream(file);
                sftp.get(attachmentHandleContext.getRoot() + path, outputStream);

                fis = new FileInputStream(file);
            } catch (Exception e) {
                logger.error("预览附件失败{}", e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                if(fis != null){
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                if(bis != null){
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                disconnect(sftp);
            }
        }
        return fis;
    }


    public SftpAttachmentStrategyImpl(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        super.configService = configService;
        this.attachmentHandleContext = attachmentHandleContext;
    }

    @Override
    public InputStream getInputStream(String path) throws Exception {
        logger.info("-------------- Sftp 附件预览！------------");
        FileInputStream fis = null;
        if (StringUtils.isNotEmpty(path)) {

            ChannelSftp sftp = new ChannelSftp();
            OutputStream outputStream = null;

            BufferedInputStream bis = null;
            try {
                sftp = this.createSftp();
                sftp.cd(attachmentHandleContext.getSftp().getRoot());
                File file = new File(attachmentHandleContext.getSftp().getSplitTempPath() + path.substring(path.lastIndexOf("/") + 1));
                outputStream = new FileOutputStream(file);
                sftp.get(attachmentHandleContext.getSftp().getRoot() + path, outputStream);

                fis = new FileInputStream(file);
            } catch (Exception e) {
                logger.error("预览附件失败{}", e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                disconnect(sftp);
            }
        }
        return fis;
    }


    @Override
    public List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList, AttachmentCategoryEnum modeName, String groupId) throws Exception {
        ArrayList<SysAttachment> attachmentDTOS = new ArrayList<>(multipartFileList.size());
        ChannelSftp sftp = this.createSftp();
        for (MultipartFile file : multipartFileList) {
            String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
            if (file != null) {
                //先判断文件大小
                Config configDTO = configService.getByTypeAndName(ConfigConstant.SYS, AttachmentConfigConstant.FILE_MAX_SIZE);
                if (!EmptyUtils.isEmpty(configDTO) && NumberUtils.isNumber(configDTO.getValue())) {
                    if (file.getSize() > NumberUtils.toLong(configDTO.getValue()) * 1024 * 1024) {
                        throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_MAX_SIZE);
                    }
                }
                //取得当前上传文件的文件名称
                String originalFilename = StringUtils.isNotBlank(file.getOriginalFilename()) ? file.getOriginalFilename() : file.getName();
                String fileName = FileUtils.getFilePrefix(originalFilename);
                if (StringUtils.isNotBlank(originalFilename)) {
                    String suffix = FileUtils.getExtend(originalFilename);
                    configDTO = configService.getByTypeAndName(ConfigConstant.SYS, AttachmentConfigConstant.FILE_FORMAT);
                    if (!EmptyUtils.isEmpty(configDTO) && StringUtils.isNotBlank(configDTO.getValue())) {
                        if (!configDTO.getValue().contains(suffix)) {
                            throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_FORMAT);
                        }
                    }
                    //modeName
                    String categoryName = modeName.getName().replace(".", File.separator).replace(FileUtils.UNIX_FILE_SEPARATOR, File.separator);
                    //重命名上传后的文件名
                    String newFileName = StringUtils.getUUId();
                    String reFileName = newFileName + "." + suffix;
                    String relative = File.separator + categoryName + File.separator + timeName + File.separator;
                    String path = FileUtils.filtrationPath(relative).replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR, FileUtils.UNIX_FILE_SEPARATOR);
                    try {
                        InputStream input = file.getInputStream();
                        sftp.cd(attachmentHandleContext.getSftp().getRoot());
                        changeFtpWorkDirByFilePath(path, sftp);
                        sftp.put(input, reFileName);
                        path = path + reFileName;
                        ShiroUser user = new ShiroUser();
                        attachmentDTOS.add(SysAttachment.create(user.getId(), groupId, modeName)
                                .appendOri(file).appendDest(path));
                    } catch (Exception e) {
                        logger.error("上传附件异常：{}", e);
                        e.printStackTrace();
                        throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_UPLOAD);
                    } finally {
                        this.disconnect(sftp);
                    }
                }
            }
        }
        return attachmentDTOS;
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
                            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + fileName);
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


    @Override
    public void deleteAttachments(List<String> pathList) {
        ChannelSftp sftp = new ChannelSftp();
        try {
            sftp = this.createSftp();
            for (String path : pathList) {
                sftp.cd(attachmentHandleContext.getSftp().getRoot());
                String relativeFilePath = getPath(path);
                relativeFilePath = getFtpFilePathWithoutIpByWholePath(relativeFilePath);
                sftp.rm(relativeFilePath);
            }
        } catch (Exception e) {
            logger.error("删除ftp附件有误。{}", e);
        } finally {
            disconnect(sftp);
        }
    }

    @Override
    public String getStoragePath() {
        return "";
    }

    @Override
    public String getAccessPath() {
        return attachmentHandleContext.getSftp().getPath();
    }

    public String getPath(String relativePath) {
        String path = "";
        if (relativePath.indexOf(File.separator) == 0) {
            path = this.getStoragePath() + relativePath;
        } else {
            path = this.getStoragePath() + File.separator + relativePath;
        }
        return path;
    }

    /**
     * 关闭连接
     *
     * @param sftp
     */
    private void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    logger.info("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建SFTP连接
     *
     * @return
     * @throws Exception
     */
    private ChannelSftp createSftp() {
        JSch jsch = new JSch();
        Channel channel;
        try {
            Session session = createSession(jsch, attachmentHandleContext.getSftp().getPath(), attachmentHandleContext.getSftp().getUsername(), attachmentHandleContext.getSftp().getPort());
            session.setPassword(attachmentHandleContext.getSftp().getPassword());
            session.connect(attachmentHandleContext.getSftp().getTimeout());
            channel = session.openChannel("sftp");
            channel.connect(attachmentHandleContext.getSftp().getTimeout());
        } catch (UnknownHostException e1) {
            logger.error("sftp连接地址有误。ftp地址为[" + attachmentHandleContext.getSftp().getPath() + "]", e1);
            throw new ParameterException(AttachmentMessageEnum.SFTP_UNKNOW_HOST);
        } catch (SocketTimeoutException e2) {
            logger.error("sftp连接超时.", e2);
            throw new ParameterException(AttachmentMessageEnum.SFTP_CONNECT_TIMEOUT);
        } catch (IOException e3) {
            throw new ParameterException(AttachmentMessageEnum.SFTP_CONNECT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(AttachmentMessageEnum.SFTP_CONNECT_ERROR);
        }
        return (ChannelSftp) channel;
    }

    /**
     * 创建session
     *
     * @param jsch
     * @param host
     * @param username
     * @param port
     * @return
     * @throws Exception
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws Exception {
        Session session = null;
        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }
        if (session == null) {
            throw new Exception(host + " session is null");
        }
        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, SessionStrictHostKeyChecking);
        return session;
    }

    /**
     * 更改ftp工作目录到文件所在的目录
     *
     * @param path
     * @param sftp
     * @throws IOException
     */
    private void changeFtpWorkDirByFilePath(String path, ChannelSftp sftp) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String[] dirArray = path.split(FileUtils.UNIX_FILE_SEPARATOR);
        for (String dir : dirArray) {
            try {
                if (StringUtils.isNotEmpty(dir)) {
                    sftp.cd(dir);
                    logger.info("Change directory {}" + dir);
                }
            } catch (Exception e) {
                try {
                    sftp.mkdir(dir);
                    logger.info("Create directory {}" + dir);
                } catch (SftpException e1) {
                    logger.error("Create directory failure, directory:{}" + dir + e1);
                    e1.printStackTrace();
                }
                try {
                    sftp.cd(dir);
                    logger.info("Change directory {}" + dir);
                } catch (SftpException e1) {
                    logger.error("Change directory failure, directory:{}" + dir + e1);
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得ftp文件路径（不带ftp的IP地址）
     * eg：ftp://192.168.0.xxx/ftpFiles/bill/20190725/5552d97f995e48d9bbc0f6b66b3560b4.gif
     * ->   /ftpFiles/bill/20190725/5552d97f995e48d9bbc0f6b66b3560b4.gif
     *
     * @param ftpFilePath
     * @return
     */
    private String getFtpFilePathWithoutIpByWholePath(String ftpFilePath) {
        return ftpFilePath.substring(attachmentHandleContext.getSftp().getPath().length());
    }
}
