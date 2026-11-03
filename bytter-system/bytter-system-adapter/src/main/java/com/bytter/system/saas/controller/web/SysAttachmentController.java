package com.bytter.system.saas.controller.web;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.Base64;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.query.SysAttachmentForm;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.SysAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: SYS_ATTACHMENT
 * @date 2017-05-26 12:20:21
 */
@Slf4j
@Api(value = "/v1-system/attachment/sysattachment", tags = "附件相关内容")
@Controller("v1SysAttachmentController")
@RequestMapping("/v1-system/attachment/sysattachment")
public class SysAttachmentController extends BaseController {

    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ConfigService configService;

    @Value("${bytter.bfs.files.kkfileview.url:http://192.168.0.114:8012}")
    private String onlinePreview;

    @Value("${bytter.gateway.url:http://192.168.0.114:8080}")
    String currentAddress;

    @ApiOperation("上传附件")
    @PostMapping("/upload")
    @ResponseBody
    public AjaxJson<List<AttachmentDTO>> upload(HttpServletRequest request) throws Exception {
        return success(sysAttachmentService.uploadAttachment(request, AttachmentCategoryEnum.CATEGORY_BILL));
    }

    /**
     * 预留印鉴上传接口
     */
    @ApiOperation("预留印鉴上传接口")
    @PostMapping("/netBankUpload")
    @ResponseBody
    public AjaxJson<List<AttachmentDTO>> netBankUpload(HttpServletRequest request) throws Exception {
        return success(sysAttachmentService.uploadAttachment(request, AttachmentCategoryEnum.CATEGORY_NETBANK));

    }

    @ApiOperation("下载附件")
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        sysAttachmentService.downloadAttachment(id, request, response);
    }

    /**
     * 文件分片上传
     * 不支持断点续传,分片传递完自动合并
     */
    @ApiOperation("文件分片上传")
    @PostMapping("/webUploader")
    @ResponseBody
    public AjaxJson<List<AttachmentDTO>> webUploader(@CookieValue(value = "Access-Token", required = false) String token,@ModelAttribute SysAttachmentForm attachmentForm) throws Exception {
        List<AttachmentDTO> list= sysAttachmentService.webUploader(attachmentForm);
        list.forEach(e->{
            // 构建文件流 URL
            String apiUrl = String.format("%s/v1-system/attachment/sysattachment/preview?id=%s&access_token=%s&fullfilename=%s",currentAddress,
                    e.getId(),token,e.getName()+"."+e.getSuffix());
            // Base64 编码
            String base64 = Base64Util.encodeToString(apiUrl.getBytes(StandardCharsets.UTF_8));
            // 构建预览 URL
            String previewPath = String.format("%s/onlinePreview?url=%s&officePreviewType=html",onlinePreview,base64);
            e.setPreviewPath(previewPath);
        });

        return success(list);
    }

    //删除附件
    @ApiOperation("删除附件")
    @PostMapping("/deleteFile")
    @ResponseBody
    public AjaxJson deleteFile(String id) throws IOException {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isNotBlank(id)) {
            sysAttachmentService.deleteAttachment(id);
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }

    //获取附件列表
    @ApiOperation("获取附件列表")
    @GetMapping("/getFileList")
    @ResponseBody
    public AjaxJson<List<AttachmentDTO>> getFileList(@CookieValue(value = "Access-Token", required = false) String token,String id) {
        List<AttachmentDTO>  result=  sysAttachmentService.findAttachmentByGroupId(id);
        result.forEach(e -> {
            // 构建文件流 URL
            String apiUrl = String.format("%s/v1-system/attachment/sysattachment/preview?id=%s&access_token=%s&fullfilename=%s",currentAddress,
                    e.getId(),token,e.getName()+"."+e.getSuffix());
            // Base64 编码
            String base64 = Base64Util.encodeToString(apiUrl.getBytes(StandardCharsets.UTF_8));
            // 构建预览 URL
            String previewPath = String.format("%s/onlinePreview?url=%s&officePreviewType=html",onlinePreview,base64);
            e.setPreviewPath(previewPath);
        });
        return success(result);
    }

    //获取最大上传数量
    @ApiOperation("获取最大上传数量")
    @GetMapping("/getLimitNum")
    @ResponseBody
    public AjaxJson<ConfigDTO> getLimitNum(String type, String name) {
        return success(configService.getDTOByTypeAndName(type, name));
    }

    /**
     * 获取文件流接口（用于 kkFileView 预览）
     *
     * @param id       附件ID
     * @param response HttpServletResponse
     */
    @GetMapping("/preview")
    public void previewFile(String id, HttpServletResponse response) {
        OutputStream os = null;
        try {
            // 1. 获取附件信息
            SysAttachment attachment = sysAttachmentService.get(id);
            if (attachment == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File not found");
                return;
            }
            // 2. 获取文件流数据
            byte[] fileData = sysAttachmentService.getAttachmentDataByid(id);
            if (fileData == null || fileData.length == 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File data is empty");
                return;
            }
            // 3. 设置响应头，方便 kkFileView 识别文件类型和名称
            String fileName = attachment.getName();
            // 设置 Content-Disposition，告诉 kkFileView 文件名和类型
            // 使用 inline 方式，让浏览器直接预览而不是下载
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition",
                    "inline; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            // 设置 Content-Type
            response.setContentType(attachment.getType());
            // 设置文件大小
            if (attachment.getFileSize() != null) {
                response.setContentLengthLong(attachment.getFileSize().longValue());
            }
            // 设置缓存控制，允许 kkFileView 缓存文件
            response.setHeader("Cache-Control", "max-age=3600");
            // 4. 输出文件流
            os = response.getOutputStream();
            os.write(fileData);
            os.flush();
            log.info("文件预览成功: id={}, fileName={}, size={}", id, fileName, fileData.length);
        } catch (Exception e) {
            log.error("文件预览失败: id={}", id, e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to get file stream: " + e.getMessage());
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败", e);
                }
            }
        }
    }

}
