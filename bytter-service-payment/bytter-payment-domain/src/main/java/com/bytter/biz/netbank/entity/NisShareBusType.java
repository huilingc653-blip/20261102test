package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 共享业务类型表
 *
 * @author fangcf
 * @date 2025-05-15
 */
@Entity
@Table(name = "NIS_SHARE_BUS_TYPE", schema = "")
public class NisShareBusType extends IdEntity implements java.io.Serializable {

    /**
     * 共享业务类型编码
     */
    private String shareBusType;
    /**
     * 共享业务类型名称
     */
    private String shareBusTypeName;
    /**
     * 关联业务类别：业务类别表主键id
     */
    private String busTypeId;
    /**
     * 状态：1-正常，0-禁用
     */
    private String status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;

    @Column(name = "SHARE_BUS_TYPE", nullable = false, length = 200)
    public String getShareBusType() {
        return shareBusType;
    }

    public void setShareBusType(String shareBusType) {
        this.shareBusType = shareBusType;
    }

    @Column(name = "SHARE_BUS_TYPE_NAME", nullable = false, length = 200)
    public String getShareBusTypeName() {
        return shareBusTypeName;
    }

    public void setShareBusTypeName(String shareBusTypeName) {
        this.shareBusTypeName = shareBusTypeName;
    }

    @Column(name = "BUS_TYPE_ID", nullable = false, length = 32)
    public String getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }

    @Column(name = "STATUS", nullable = false, length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CREATE_BY", nullable = false, length = 32)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY", nullable = false, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE", nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
