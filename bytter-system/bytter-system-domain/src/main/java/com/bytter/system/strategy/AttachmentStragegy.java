package com.bytter.system.strategy;

import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.entity.SysAttachment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * @author lifh
 * @description
 * @createDate 2019/7/22
 */
public interface AttachmentStragegy {

    List<SysAttachment> uploadAttachments(List<MultipartFile> multipartFileList,
                                          AttachmentCategoryEnum modeName, String groupId) throws Exception;

    void downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response);

    void deleteAttachments(List<String> pathList);

    byte[] getAttachmentData(SysAttachment attachment);

    InputStream previewAttachment(SysAttachment attachment);

    /**
     * 不同文件下载文件流不同 分别实现
     *
     * @param path 文件路径
     * @return 文件流
     * @throws Exception 获取文件流异常
     */
     InputStream getInputStream(String path) throws Exception;

    /**
     * 获取存储路径
     *
     * @return
     */
    String getStoragePath();

    /**
     * 获取访问路径
     */
    String getAccessPath();

    /**
     * 获取存储桶名称
     */
    default String getBuckName() {
        return CommonConstant.EMPTY_STR;
    }


}
