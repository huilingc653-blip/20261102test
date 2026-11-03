package com.bytter.system.vo;
import java.io.Serializable;
import java.util.Date;
/**
 * @author lifh
 * @description 客商资料审核日志表VO， 用于查看
 * @createDate 2019/3/11
 */
public class ExternalCorpAuditLogVO implements Serializable {
    private String id;
    /**
     * 客商id
     */
    private String externalCorpId;
    /**
     * 操作类型：11:提交，95:审核通过，-1:	打回
     */
    private Integer auditType;
    /**
     * 审批信息
     */
    private String auditMessage;
    private String createBy;
    private String createLoginName;
    private String createUsername;
    private Date createTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    public Integer getAuditType() {
        return auditType;
    }
    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
    public String getAuditMessage() {
        return auditMessage;
    }
    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateLoginName() {
        return createLoginName;
    }
    public void setCreateLoginName(String createLoginName) {
        this.createLoginName = createLoginName;
    }
    public String getCreateUsername() {
        return createUsername;
    }
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "ExternalCorpAuditLogVO{" +
                "id='" + id + '\'' +
                ", externalCorpId='" + externalCorpId + '\'' +
                ", auditType=" + auditType +
                ", auditMessage='" + auditMessage + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createLoginName='" + createLoginName + '\'' +
                ", createUsername='" + createUsername + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
