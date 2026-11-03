package com.bytter.system.entity;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.DateUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * @author lifh
 * @description 客商资料审核日志表。提交和审批的操作才要记录
 * @createDate 2019/3/11
 */
@Entity
@Table(name = "SYS_EXTERNAL_CORP_AUDIT_LOG")
public class ExternalCorpAuditLog extends IdEntity implements Serializable {
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
    @Column(name = "EXTERNAL_CORP_ID")
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    @Column(name = "AUDIT_TYPE")
    public Integer getAuditType() {
        return auditType;
    }
    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
    @Column(name = "AUDIT_MESSAGE")
    public String getAuditMessage() {
        return auditMessage;
    }
    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_LOGIN_NAME")
    public String getCreateLoginName() {
        return createLoginName;
    }
    public void setCreateLoginName(String createLoginName) {
        this.createLoginName = createLoginName;
    }
    @Column(name = "CREATE_USERNAME")
    public String getCreateUsername() {
        return createUsername;
    }
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "ExternalCorpAuditLog{" +
                "externalCorpId='" + externalCorpId + '\'' +
                ", auditType=" + auditType +
                ", auditMessage='" + auditMessage + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                '}';
    }
    public ExternalCorpAuditLog() {
    }
    public ExternalCorpAuditLog(String externalCorpId, Integer auditType, String auditMessage, ICurrentUser currentUser) {
        this.externalCorpId = externalCorpId;
        this.auditType = auditType;
        this.auditMessage = auditMessage;
        if (currentUser != null) {
            this.setCreateUsername(currentUser.getUsername());
            this.setCreateLoginName(currentUser.getLoginName());
            this.setCreateBy(currentUser.getId());
        }
        this.setCreateTime(DateUtils.getDate());
    }
}
