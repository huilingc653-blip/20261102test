package com.bytter.system.strategy.impl;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lifh
 * @description
 * @createDate 2019/7/22
 */
@Component
public class FtpAttachmentStrategyImpl extends AbstractAttachmentStrategy {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public FtpAttachmentStrategyImpl(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        super.configService = configService;
        this.attachmentHandleContext = attachmentHandleContext;
    }

    //跟换糊涂的下载方式，原先下载方式兼容存在问题
    public InputStream previewAttachment(SysAttachment attachment) {
        logger.info("-------------- Ftp 附件预览！------------");
        InputStream inputStream = null;
        if (attachment != null) {
            String path = attachment.getPath();
            path=path.substring(attachmentHandleContext.getStoragePath().length()+1);
            byte[] dataByte=getFileDatas(path);
            return new ByteArrayInputStream(dataByte);
//            String path = attachment.getPath();
//            FTPClient ftp = new FTPClient();
//            try {
//                connectFtp(ftp);
//                //文件的路径
//                String relativeFilePath = getFtpFilePathWithoutIpByWholePath(path);
//                FTPFile[] ftpFiles = null;
//                ftpFiles = ftp.listFiles(relativeFilePath);
//                if (EmptyUtils.isEmpty(ftpFiles)) {
//                    logger.error(String.format("附件%s不存在", path));
//                }
//                if (ftpFiles != null && ftpFiles.length > 0) {
//                    changeFtpWorkDirByFilePath(path, ftp);
//                    inputStream = ftp.retrieveFileStream(ftpFiles[0].getName());
//                }
//            } catch (Exception e) {
//                logger.error("预览附件失败{}", e);
//            } finally {
//                closeFtpClient(ftp, "预览附件异常：{}");
//            }
        }

        return inputStream;
    }


    @Override
    public InputStream getInputStream(String path) throws Exception {
        logger.info("-------------- Ftp 附件预览！------------");
        InputStream inputStream = null;
        if (StringUtils.isNotEmpty(path)) {
            path = path.substring(this.getStoragePath().length() + 1);
            byte[] dataByte = getFileDatas(path);
            return new ByteArrayInputStream(dataByte);
        }
        return inputStream;
    }

    public byte[] getFileDatas(String relativePath) {
        Ftp ftp = null;
        byte[] fileData = null;
        ByteArrayOutputStream outputStream = null;
        try {
            //文件的路径
            String uploadPath = this.getStoragePath();
            if (relativePath.indexOf(File.separator) != 0) {
                relativePath = File.separator + relativePath;
            }
            String path = FileUtils.filtrationPath(uploadPath + relativePath).replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR, FileUtils.UNIX_FILE_SEPARATOR);
            String relativeFilePath = getFtpFilePathWithoutIpByWholePath(path);
            String fileName = relativeFilePath.substring(relativeFilePath.lastIndexOf(FileUtils.UNIX_FILE_SEPARATOR) + 1);
            relativeFilePath = relativeFilePath.substring(0, relativeFilePath.lastIndexOf(FileUtils.UNIX_FILE_SEPARATOR));
            outputStream = new ByteArrayOutputStream();
            ftp = new Ftp(attachmentHandleContext.getFtp().getPath(), attachmentHandleContext.getFtp().getPort(), attachmentHandleContext.getFtp().getUsername(), attachmentHandleContext.getFtp().getPassword());
            //设置为被动模式，主动模式客户端需要开放端口，本地化部署不好控制
            ftp.setMode(FtpMode.Passive);
            ftp.download(relativeFilePath, fileName, outputStream);
            fileData = outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("下载附件失败{}", e);
        } finally {
            try {
                if (ftp != null) {
                    ftp.close();
                }
            } catch (Exception e) {
                logger.error("关闭异常", e);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                logger.error("关闭异常", e);
            }
        }
        return fileData;
    }


    @Override
    public List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList, AttachmentCategoryEnum modeName, String groupId) throws Exception {
        ArrayList<SysAttachment> attachmentDTOS = new ArrayList<>(multipartFileList.size());
        FTPClient ftp = new FTPClient();
        createFtpClient(ftp);
        for (MultipartFile file : multipartFileList) {
            String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
            String upload_path = attachmentHandleContext.getFtp().getSplitTempPath();
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
                    //定义上传路径。eg：\bill\20190726\52b177be73e84944939b0bde08be480f.gif
                    String relative = File.separator + categoryName + File.separator + timeName + File.separator + reFileName;
                    String path = FileUtils.filtrationPath(upload_path + relative).replaceAll(FileUtils.WINDOWS_FILE_SEPARATOR, FileUtils.UNIX_FILE_SEPARATOR);
                    ftp.setControlEncoding("UTF-8");
                    try {
                        InputStream input = file.getInputStream();
                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        changeFtpWorkDirByFilePath(path, ftp);
                        ftp.storeFile(reFileName, input);
                        input.close();
                        ftp.logout();
                        ShiroUser user = new ShiroUser();
                        SysAttachment attachment = new SysAttachment(groupId, fileName, newFileName, suffix, suffix, new BigDecimal(file.getSize()), path, categoryName, user);
                        //转换赋值
                        attachment.setId(StringUtils.getUUId());
                        attachmentDTOS.add(attachment);
                    } catch (IOException e) {
                        logger.error("上传附件异常：{}", e);
                        throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_UPLOAD);
                    } finally {
                        closeFtpClient(ftp);
                    }
                }
            }
        }
        return attachmentDTOS;
    }

    @Override
    public void downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream os = null;
        try {
            if (attachment != null) {
                String path = attachment.getPath();
                  inputStream = this.getInputStream(path);
                if (inputStream != null) {
                    String fileName = attachment.getName() + "." + attachment.getSuffix();
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/octet-stream");
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
                      os = response.getOutputStream();
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
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("关闭输出流失败", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("关闭输入流失败", e);
                }
            }
        }
    }

    private void closeFtpClient(FTPClient ftp) {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException ioe) {
                throw new BusinessException(AttachmentMessageEnum.ATTACHMENT_MESSAGE_ERROR_UPLOAD);
            }
        }
    }

    private void createFtpClient(FTPClient ftp) {
        try {
            ftp.setConnectTimeout(attachmentHandleContext.getFtp().getTimeout());
            // 取消服务器获取自身Ip地址和提交的host进行匹配
            ftp.setRemoteVerificationEnabled(false);
            // 连接FTP服务器
            ftp.connect(attachmentHandleContext.getFtp().getPath(), attachmentHandleContext.getFtp().getPort());
            // 登陆FTP服务器
            ftp.login(attachmentHandleContext.getFtp().getUsername(), attachmentHandleContext.getFtp().getPassword());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
        } catch (UnknownHostException e1) {
            logger.error("ftp连接地址有误。ftp地址为[" + attachmentHandleContext.getFtp().getPath() + "]", e1);
            throw new ParameterException(AttachmentMessageEnum.FTP_UNKNOW_HOST);
        } catch (SocketTimeoutException e2) {
            logger.error("ftp连接超时.", e2);
            throw new ParameterException(AttachmentMessageEnum.FTP_CONNECT_TIMEOUT);
        } catch (IOException e3) {
            throw new ParameterException(AttachmentMessageEnum.FTP_CONNECT_ERROR);
        }
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            logger.error("未连接到FTP，用户名或密码错误.");
            throw new ParameterException(AttachmentMessageEnum.FTP_USERNAME_PASSWORD_ERROR);
        }
    }

    /**
     * 更改ftp工作目录到文件所在的目录
     *
     * @param path
     * @param ftp
     * @throws IOException
     */
    private void changeFtpWorkDirByFilePath(String path, FTPClient ftp) throws IOException {
        String dirPath = getFtpFileDirPathWithoutIpByWholePath(path);
        String[] dirArray = dirPath.split(FileUtils.UNIX_FILE_SEPARATOR);
        for (String dir : dirArray) {
            ftp.makeDirectory(dir);
            ftp.changeWorkingDirectory(dir);
        }
    }

    /**
     * 获得ftp文件所在的目录的路径（不包括ip）
     * eg：ftp://192.168.0.xxx/ftpFiles/bill/20190725/5552d97f995e48d9bbc0f6b66b3560b4.gif
     * ->   /ftpFiles/bill/20190725
     *
     * @param ftpFilePath
     * @return
     */
    private String getFtpFileDirPathWithoutIpByWholePath(String ftpFilePath) {
        String ftpFilePathWithoutIP = getFtpFilePathWithoutIpByWholePath(ftpFilePath);
        return ftpFilePathWithoutIP.substring(0, ftpFilePathWithoutIP.lastIndexOf(FileUtils.UNIX_FILE_SEPARATOR));
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
        return ftpFilePath.substring(attachmentHandleContext.getFtp().getFlag().length() + attachmentHandleContext.getFtp().getPath().length() + 1);
    }

    @Override
    public void deleteAttachments(List<String> pathList) {
        FTPClient ftp = new FTPClient();
        try {
            for (String path : pathList) {
                createFtpClient(ftp);
                String relativeFilePath = getPath(path);
                relativeFilePath = getFtpFilePathWithoutIpByWholePath(relativeFilePath);
                ftp.deleteFile(relativeFilePath);
            }
        } catch (Exception e) {
            logger.error("删除ftp附件有误。{}", e);
        } finally {
            closeFtpClient(ftp);
        }
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

    @Override
    public String getStoragePath() {
        return attachmentHandleContext.getFtp().getSplitTempPath();
    }

    @Override
    public String getAccessPath() {
        return "";
    }
}
