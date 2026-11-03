package com.bytter.dual.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * Created by zhoul on 2017/9/1.
 */
@Entity
@Table(name = "SYS_DUAL_MANAGER")
public class DualManager extends IdEntity {
    private String managerType;
    private String businessKey;
    private String content;
    private Integer status;
    private Date createDate;
    @Column(name = "MANAGER_TYPE")
    public String getManagerType() {
        return managerType;
    }
    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }
    @Column(name = "BUSINESS_KEY")
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
