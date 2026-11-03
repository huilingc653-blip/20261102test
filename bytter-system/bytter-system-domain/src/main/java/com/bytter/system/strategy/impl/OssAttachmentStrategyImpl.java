package com.bytter.system.strategy.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ConfigService;
import com.bytter.system.strategy.AbstractAttachmentStrategy;
import com.bytter.system.strategy.AttachmentHandleContext;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import io.minio.GetObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云存储实现类
 *
 * @author wade
 *
 */
@Slf4j
public class OssAttachmentStrategyImpl extends AbstractAttachmentStrategy {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String bucketName;

    private AttachmentHandleContext.OssProperties ossProperties = null;




    public OssAttachmentStrategyImpl(AttachmentHandleContext attachmentHandleContext, ConfigService configService) {
        super.configService = configService;
        this.attachmentHandleContext = attachmentHandleContext;
        ossProperties = attachmentHandleContext.getOss();
        this.bucketName = ossProperties.getBucketName();
        createCosClient();
    }

    /**
     * 创建 COSClient客户端
     *
     * @return
     */
    private COSClient createCosClient() {
        //1.1 初始化用户身份信息
        BasicCOSCredentials credentials = new BasicCOSCredentials(ossProperties.getSecretId(), ossProperties.getSecretKey());
        //1.2 设置bucket的地域
        Region region = new Region(ossProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        //1.3 生成cos客户端
        COSClient cosClient = new COSClient(credentials, clientConfig);
        return cosClient;
    }

    /**
     * 在指定账号下创建一个存储桶。同一用户账号下，可以创建多个存储桶，数量上限是200个（不区分地域），存储桶中的对象数量没有限制。
     * 创建存储桶是低频操作，一般建议在控制台创建 Bucket，在 SDK 进行 Object 的操作。
     *
     * @return
     */
    public Bucket createBucket() {
        Bucket bucketResult = null;
        boolean bucketExistFlag = false;
        COSClient cosClient = createCosClient();
        try {

            //存储桶名称，格式：BucketName-APPID
            String bucket = ossProperties.getBucketName();
            bucketExistFlag = cosClient.doesBucketExist(bucket);
            if (!bucketExistFlag) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
                // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
                createBucketRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
                bucketResult = cosClient.createBucket(createBucketRequest);
            }
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return bucketResult;
    }


    @Override
    public List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList, AttachmentCategoryEnum modeName,
                                                 String groupId) throws Exception {
        List<SysAttachment> attachments = new ArrayList<SysAttachment>();
        createBucket();
        String timeName = DateUtils.date2Str(DateUtils.yyyyMMdd());
        ShiroUser user = (ShiroUser) UserContextHelper.helper().getCurrentUser();//当前用户
        COSClient cosClient = createCosClient();
        for (MultipartFile multipartFile : multipartFileList) {
            checkFile(multipartFile);
            String path = super.buildPath(multipartFile, timeName, modeName);
            if (StringUtils.isBlank(path)) {
                continue;
            }
            //2.上传文件
            File localFile = null;
            String bucket = ossProperties.getBucketName();
            try {
                String suffix = FileUtils.getExtend(path);
                localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), suffix);
                multipartFile.transferTo(localFile);
                //指定文件上传到 COS 上的路径
                log.info("===============================bucket" + bucket);
                log.info("===============================path" + path);
                log.info("===============================localFile" + localFile.getName());
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, localFile);
                PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                //测试 尝试多线程上传
                log.info("上传" + path + "文件成功");
                String uploadPath = path;//大参林文件上传cdn域名+文件路径
                log.info("图片路径" + uploadPath);
                attachments.add(SysAttachment.create(ObjectUtils.isNotEmpty(user) ? user.getId() : "", groupId, modeName).appendOri(multipartFile).appendDest(uploadPath));
            } catch (IOException e) {
                log.info("上传" + path + "文件失败");
            } finally {
                cosClient.shutdown();
                localFile.delete();
            }
        }
        return attachments;
    }

    public InputStream previewAttachment(SysAttachment attachment) {
        String path = attachment.getPath();
        return getFileInputStream(path);
    }

    public InputStream getFileInputStream(String relativePath) {
        try {
            InputStream inputStream = this.getInputStream(relativePath);
            return inputStream;
        } catch (Exception e) {
            logger.error("下载附件失败{}", e);
        }
        return null;
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

    @Override
    public void deleteAttachments(List<String> fileList) {
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }
        COSClient cosClient = createCosClient();
        String bucket = ossProperties.getBucketName();
        for (String path : fileList) {
            //TODO
            String key = path;//组装文件路径
            try {
                cosClient.deleteObject(bucket, key);
                log.info(bucket + "删除文件" + key + "成功");
            } catch (CosClientException e) {
                log.error(bucket + "删除文件" + key + "异常" + e.getMessage());
            } finally {
                cosClient.shutdown();
            }
        }
    }


    /**
     * 默认用父类的download方法，这个是下载到某个文件路径, 系统不用这个方法
     *
     * @param fileName
     * @return
     */
    public String download(String fileName) {
        TransferManager transferManager = createTransferManager();
        String bucket = ossProperties.getBucketName();
        String key = fileName;
        //下载到工程目录下的imgs文件夹中
        File downloadFile = new File(System.getProperty("user.dir") + "/imgs/" + fileName);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
        try {
            //返回一个异步结果 Donload, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
            Download download = transferManager.download(getObjectRequest, downloadFile);
            download.waitForCompletion();
            return "下载文件成功";
        } catch (InterruptedException e) {
            return "下载文件失败";
        } finally {
            shutdownTransferManager(transferManager);
            downloadFile.delete();
        }
    }

    @Override
    public String getStoragePath() {
        return "";
    }

    @Override
    public String getAccessPath() {
        return attachmentHandleContext.getOss().getCdnDomian();
    }

    @Override
    public String getBuckName() {
        return this.bucketName;
    }

    @Override
    public InputStream getInputStream(String path) throws Exception {
        try {
            COSObject ossObject = createCosClient().getObject(bucketName,  path);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            logger.error(String.format("文件下载失败%s", e));
        }
        return null;
    }


    private void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }

    private TransferManager createTransferManager() {
        //初始化客户端
        COSClient cosClient = createCosClient();
        //创建线程池
        ExecutorService threadPool = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(10));//视CPU定
        //创建 TransferManager
        return new TransferManager(cosClient, threadPool);
    }
}
