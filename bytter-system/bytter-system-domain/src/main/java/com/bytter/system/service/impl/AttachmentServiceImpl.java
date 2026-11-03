package com.bytter.system.service.impl;

import com.bytter.cloud.common.core.util.OfdUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAttachmentService;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.SysAttachmentService;
import com.bytter.system.strategy.AbstractAttachmentStrategy;
import com.bytter.system.strategy.AttachmentHandleContext;
import com.bytter.system.strategy.AttachmentStrategyFactory;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上传附件接口实现
 * Created by  zhouj on 2017/5/31.
 */
@Service("attachmentServiceI")
@DubboService
@Transactional
public class AttachmentServiceImpl implements IAttachmentService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private ConfigService configService;

    private AttachmentHandleContext attachmentHandleContext = new AttachmentHandleContext();

    private final String previewPathKey = "previewPath";

    private final ConcurrentHashMap<String, String> cacheMap = new ConcurrentHashMap<>();

    public BufferedImage ofdToPng(InputStream inputStream ) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096]; // 推荐4KB缓冲区:ml-citation{ref="4" data="citationList"}
        int bytesRead;
        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead); // 分块写入缓冲区:ml-citation{ref="3" data="citationList"}
        }
        BufferedImage bufferedImage = getBufferedImageByOfd(new ByteArrayInputStream(buffer.toByteArray()));
        return bufferedImage;
//        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }

    public BufferedImage getBufferedImageByOfd(ByteArrayInputStream byteArrayInputStream) throws Exception {
        int maxWidth = 0;
        int height = 0;
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = byteArrayInputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } finally {
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    throw new Exception("输入流关闭异常");
                }
            }
        }
        String ofdBase64 = Base64.getEncoder().encodeToString(data);
        List<BufferedImage> list = OfdUtils.ofdToImage(ofdBase64);
        for (int i = 0; i < list.size(); i++) {
            BufferedImage image = list.get(i);
            maxWidth = Math.max(maxWidth, image.getWidth());
            height += image.getHeight();
        }
        return list.get(0);
    }

    public AttachmentDTO previewAttachmentFromPath(String id,String realPath,StringBuffer requestURL) {
        logger.info("------------ 执行预览附件 -------------");
        SysAttachment attachment = sysAttachmentService.get(id);
        String oldPath = attachment.getPath();
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        BeanUtils.copyProperties(attachment, attachmentDTO);
        if (!attachment.getPath().contains(attachmentHandleContext.getStoragePath()) && !"sftp".equals(attachmentHandleContext.getType())) {
            attachmentDTO.setPath(attachmentHandleContext.getStoragePath() + attachment.getPath());
            attachment.setPath(attachmentHandleContext.getStoragePath() + attachment.getPath());
        }
        if (StringUtils.isNotBlank(attachment.getName())) {
            attachmentDTO.setName(attachment.getName().replaceAll(CommonConstant.COMMA_SPLIT, ""));
            attachment.setName(attachment.getName().replaceAll(CommonConstant.COMMA_SPLIT, ""));
        }
        AbstractAttachmentStrategy attachmentStrategy = AttachmentStrategyFactory.createAttachmentStrategy(attachmentHandleContext,configService);

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = attachmentStrategy.previewAttachment(attachment);
            attachment.setPath(oldPath);
            String stat = "/static";
            String tempFile = "/tempFile";
            String previewPath = new File(realPath.concat(stat).concat(tempFile)).getAbsolutePath();
            cacheMap.putIfAbsent(previewPathKey, previewPath);
            File file = new File(previewPath);
            if (file.exists()) {
                System.out.println(previewPath + " 路径存在");
            } else {
                logger.info(previewPath + " 不存在，创建");
                file.mkdirs();
            }

            String url = requestURL.toString().substring(0, requestURL.indexOf("attachment") - 1);
            String targetPath = previewPath + File.separator + attachmentDTO.getReName() + "." + attachmentDTO.getSuffix();
            File file1 = new File(targetPath);

            if ("ofd".equals(attachment.getType())){
                targetPath= previewPath + File.separator + attachmentDTO.getReName() + ".png";
                fileOutputStream = new FileOutputStream(targetPath);
                BufferedImage image=ofdToPng(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                baos.writeTo(fileOutputStream);
                attachmentDTO.setUrl(url.concat(stat).concat(tempFile).concat("/") + attachmentDTO.getReName() + ".png");
                attachmentDTO.setType("png");
//                BufferedImage  image=ofdToPng(inputStream);
//                String targetPathPng = previewPath + File.separator + attachmentDTO.getReName() + ".png";
//                fileOutputStream2 = new FileOutputStream(targetPathPng);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(image, "png", baos);
//                // 4. 写入输出流
//                baos.writeTo(fileOutputStream2);
            }else if (!file1.exists()) {
                fileOutputStream = new FileOutputStream(targetPath);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, read);
                }
                attachmentDTO.setUrl(url.concat(stat).concat(tempFile).concat("/") + attachmentDTO.getReName() + "." + attachmentDTO.getSuffix());
            }else{
                attachmentDTO.setUrl(url.concat(stat).concat(tempFile).concat("/") + attachmentDTO.getReName() + "." + attachmentDTO.getSuffix());
            }

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return attachmentDTO;
    }

    public List<AttachmentDTO> findAttachmentByIds(List<String> fileIds) {
        List<SysAttachment> attachment = sysAttachmentService.findAttachmentByIds(fileIds);
        if (attachment != null && attachment.size()!=0) {
            List<AttachmentDTO> list = BeanUtils.copyListProperties(attachment, AttachmentDTO.class);
            return list;
        }
        return null;
    }
    /**
     * 根据组id查询附件列表
     *
     * @param groupId
     * @return
     */
    @Override
    public List<AttachmentDTO> findAttachmentByGroupId(String groupId) {
        return sysAttachmentService.findAttachmentByGroupId(groupId);
    }
    @Override
    public String mergeGroupIds(List<String> ids) {
        String groupId = StringUtils.getUUId();
        List<SysAttachment> result = new ArrayList<SysAttachment>();
        try {
            if (!EmptyUtils.isEmptyList(ids)) {
                for (String id : ids) {
                    List<SysAttachment> attachmentList = sysAttachmentService.getAttachmentByGroupId(id);
                    if (!EmptyUtils.isEmptyList(attachmentList)) {
                        for (SysAttachment attachment : attachmentList) {
                            SysAttachment sys = new SysAttachment();
                            BeanUtils.copyNotNullProperties(attachment, sys);
                            sys.setId("");
                            sys.setGroupId(groupId);
                            result.add(sys);
                        }
                    }
                }
            }
            if (!EmptyUtils.isEmptyList(result)) {
                sysAttachmentService.batchAttachmentSave(result);
            } else {
                groupId = "";
            }
        } catch (Exception e) {
            groupId = "";
        }
        return groupId;
    }
    @Override
    public byte[] getAttachmentDataByid(String id) {
        return sysAttachmentService.getAttachmentDataByid(id);
    }
    @Override
    public AttachmentDTO findAttachmentById(String fileId) {
        SysAttachment attachment = sysAttachmentService.get(fileId);
        if (attachment != null) {
            AttachmentDTO dto = new AttachmentDTO();
            BeanUtils.copyNotNullProperties(attachment, dto);
            return dto;
        }
        return null;
    }
}
