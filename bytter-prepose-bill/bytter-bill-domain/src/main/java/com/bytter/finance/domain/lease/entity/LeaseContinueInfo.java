package com.bytter.finance.domain.lease.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * @Title 融资租赁退租数据
 * @Description
 * @Author wanggl
 * @Date 2022-4-15
 * @Version 1.0
 */
@Entity
@Table(name = "CMS_LEASE_CONTINUE", schema = "")
@SuppressWarnings("serial")
public class LeaseContinueInfo extends IdEntity implements Serializable {
    /**
     * 租赁表ID
     */
    private String leaseId;
    /**
     * 退租日期
     */
    private Date continueDate;
    /**
     * 是否提前退租
     */
    private String continueCode;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 创建日期
     */
    private Date createDate;
    @Column(name = "LEASE_ID")
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    @Column(name = "CONTINUE_DATE")
    public Date getContinueDate() {
        return continueDate;
    }
    public void setContinueDate(Date continueDate) {
        this.continueDate = continueDate;
    }
    @Column(name = "CONTINUE_CODE")
    public String getContinueCode() {
        return continueCode;
    }
    public void setContinueCode(String continueCode) {
        this.continueCode = continueCode;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
