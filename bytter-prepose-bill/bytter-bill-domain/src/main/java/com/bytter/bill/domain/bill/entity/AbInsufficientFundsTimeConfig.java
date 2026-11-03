package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 银承开票占比目标维护表
 * @author zhous
 * @date 2025/4/22 10:23
 */
@Entity
@Table(name = "ab_short_funds_time_config", schema = "")
@SuppressWarnings("serial")
public class AbInsufficientFundsTimeConfig extends IdEntity implements java.io.Serializable{

    /**银行类型*/
    private String bankTypeId;

    /**银行名称*/
    private String bankTypeName;

    /**时间*/
    private String time;

    /**状态 1-正常 0-禁用*/
    private String status;

    /**是否删除 1-是*/
    private String isDeleted;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createTime;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private Date updateTime;

    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "TIME")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "IS_DELETED")
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
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

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
