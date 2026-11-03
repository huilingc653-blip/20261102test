package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 评价任务组织表
 */
@Entity
@Table(name = "CR_TASK_CORP_INFO", schema = "")
public class CrTaskCorpInfo extends IdEntity implements Serializable {

    /**
     * 评价任务编号
     */
    private String taskNo;

    /**
     * 评价组织
     */
    private String corpId;

    /**
     * 状态
     */
    private int status;

    /**
     * 评价人
     */
    private String evaluateBy;

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

    @Column(name = "CORP_ID", length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "STATUS", length = 18)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "EVALUATE_BY", length = 100)
    public String getEvaluateBy() {
        return evaluateBy;
    }

    public void setEvaluateBy(String evaluateBy) {
        this.evaluateBy = evaluateBy;
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
