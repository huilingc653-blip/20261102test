package com.bytter.system.vo;
import com.bytter.core.annotation.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * 文章VO
 *
 * @author zhoul
 * @since 2020-05-15
 */
public class ArticleVO {
    private String id;
    private String title;
    private String context;
    private Integer isTop;
    private Integer status;
    private Integer type;
    private Integer receiverType;
    @User(inject = "createByUserName")
    private String createBy;
    private String createByUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    private String attachmentId;
    private String readSign;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getIsTop() {
        return isTop;
    }
    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getReceiverType() {
        return receiverType;
    }
    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCreateByUserName() {
        return createByUserName;
    }
    public void setCreateByUserName(String createByUserName) {
        this.createByUserName = createByUserName;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getReadSign() {
        return readSign;
    }
    public void setReadSign(String readSign) {
        this.readSign = readSign;
    }
}
