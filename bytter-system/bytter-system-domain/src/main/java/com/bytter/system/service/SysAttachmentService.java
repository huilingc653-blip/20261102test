package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.dao.SysAttachmentDao;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.query.SysAttachmentForm;
import com.bytter.system.strategy.AttachmentStragegy;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: SYS_ATTACHMENT
 * @date 2017-05-26 12:20:21
 */
@Service("sysAttachmentService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class SysAttachmentService extends BaseService<SysAttachment, String> {
    @Autowired
    private SysAttachmentDao sysAttachmentDao;
    @Autowired
    private AttachmentStragegy attachmentStragegy;

    public List<SysAttachment> findAttachmentByIds(List<String> fileIds){
        return sysAttachmentDao.findAttachmentByIds(fileIds);
    }

    /**
     * 上传附件
     *
     * @param request  HttpServletRequest
     * @param modeName 模块名称 文件按模块分类
     * @return List<AttachmentDTO>
     */
    public List<AttachmentDTO> uploadAttachment(HttpServletRequest request, AttachmentCategoryEnum modeName) throws Exception {
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            String groupId = request.getParameter("pid");
            if (StringUtils.isBlank(groupId)) {
                groupId = StringUtils.getUUId();
            }
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            List<MultipartFile> multipartFileList = new ArrayList<>();
            while (iter.hasNext()) {
                multipartFileList.add(multiRequest.getFile(iter.next()));
            }
            List<SysAttachment> attachments = attachmentStragegy.uploadAttachments(multipartFileList, modeName, groupId);
            this.batchSave(attachments);
            return BeanUtils.copyListProperties(attachments, AttachmentDTO.class);
        }
        return Lists.newArrayList();
    }
    /**
     *
     * @param sysAttachmentForm  SysAttachmentForm
     * @return List<AttachmentDTO>
     */
    public List<AttachmentDTO> webUploader(SysAttachmentForm sysAttachmentForm) throws Exception {
        List<SysAttachment> sysAttachments = attachmentStragegy.uploadAttachments(sysAttachmentForm.getFile(),
                AttachmentCategoryEnum.CATEGORY_COMMON, sysAttachmentForm.getPid());
        this.batchSave(sysAttachments);
        return BeanUtils.copyListProperties(sysAttachments, AttachmentDTO.class);
    }
    public void downloadAttachment(String id, HttpServletRequest request, HttpServletResponse response) {
        SysAttachment attachment = this.get(id);
        if (Objects.isNull(attachment)) {
            return;
        }
        attachmentStragegy.downloadAttachment(attachment, request, response);
    }
    public void deleteAttachment(String id) {
        SysAttachment attachment = this.get(id);
        if (Objects.isNull(attachment)) {
            return;
        }
        String path = attachment.getPath();
        this.deleteEntity(attachment);
        attachmentStragegy.deleteAttachments(Lists.newArrayList(path));
    }
    public byte[] getAttachmentDataByid(String id) {
        SysAttachment attachment = this.get(id);
        if (Objects.isNull(attachment)) {
            return null;
        }
        return attachmentStragegy.getAttachmentData(attachment);
    }
    public List<AttachmentDTO> findAttachmentByGroupId(String groupId) {
        List<SysAttachment> attachmentList = this.getAttachmentByGroupId(groupId);
        return BeanUtils.copyListProperties(attachmentList, AttachmentDTO.class);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(SysAttachment entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(SysAttachment entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(SysAttachment entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(SysAttachment entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<SysAttachment, String> getEntityDao() {
        return sysAttachmentDao;
    }
    public List<SysAttachment> getAttachmentByGroupId(String groupId) {
        return sysAttachmentDao.getAttachmentByGroupId(groupId);
    }
    public void batchAttachmentSave(List<SysAttachment> list) {
        sysAttachmentDao.batchSave(list);
    }
}
