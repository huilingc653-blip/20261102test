package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 机构和评价任务主表
 */
@Entity
@Data
@Table(name = "CR_TASK_ORG_INFO", schema = "")
public class CrTaskOrgInfo extends IdEntity implements Serializable {

    /**
     * 评价任务编号
     */
    private String taskNo;

    /**
     * 评价机构
     */
    private String orgId;

    //表没这个字段，vo也没有，应该是没用到
//    /**
//     * 模板id
//     */
//    private String computationalModelId;

    /**
     * 评价机构
     */
    private String orgName;

    /**
     * 机构平均分
     */
    private BigDecimal orgAvg;

    /**
     * 状态
     */
    private int status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    @Column(name = "TASK_NO", length = 32)
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    @Column(name = "ORG_ID", length = 32)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

//    @Column(name = "COMPUTATIONAL_MODEL_ID", length = 32)
//    public String getComputationalModelId() {
//        return computationalModelId;
//    }
//
//    public void setComputationalModelId(String computationalModelId) {
//        this.computationalModelId = computationalModelId;
//    }

    @Column(name = "ORG_NAME", length = 100)
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Column(name = "ORG_AVG")
    public BigDecimal getOrgAvg() {
        return orgAvg;
    }

    public void setOrgAvg(BigDecimal orgAvg) {
        this.orgAvg = orgAvg;
    }

    @Column(name = "STATUS", length = 18)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CREATOR", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY", length = 100)
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
}
