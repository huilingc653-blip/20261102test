package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 评价任务主表
 */
@Entity
@Data
@Table(name = "CR_TASK_INFO", schema = "")
public class CrTaskInfo extends IdEntity implements Serializable {

    /**
     * 评价任务编号
     */
    private String taskNo;

    /**
     * 评价任务名称
     */
    private String taskName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 评价模板名称
     */

    private String templateName;

    /**
     * 评价组织权重
     */
    private String taskWeight;

    /**
     * 模型id
     */
    private String computationalModelId;

    /**
     * 状态
     */
    private int status;

    /**
     * 评价周期
     */
    private String taskCycle;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 评价开始日期
     */
    private Date evaluateStartDate;

    /**
     * 评价结束日期
     */
    private Date evaluateEndDate;

    /**
     * 组织评价次数
     */
    private int evaluateCnt;

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

    @Column(name = "TASK_NAME", length = 100)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name = "ORG_TYPE", length = 100)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    @Column(name = "TEMPLATE_NAME", length = 100)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name = "TASK_WEIGHT", length = 10)
    public String getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(String taskWeight) {
        this.taskWeight = taskWeight;
    }

    @Column(name = "STATUS", length = 18)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "TASK_CYCLE", length = 20)
    public String getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(String taskCycle) {
        this.taskCycle = taskCycle;
    }

    @Column(name = "RMK", length = 500)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "EVALUATE_START_DATE")
    public Date getEvaluateStartDate() {
        return evaluateStartDate;
    }

    public void setEvaluateStartDate(Date evaluateStartDate) {
        this.evaluateStartDate = evaluateStartDate;
    }

    @Column(name = "EVALUATE_END_DATE")
    public Date getEvaluateEndDate() {
        return evaluateEndDate;
    }

    public void setEvaluateEndDate(Date evaluateEndDate) {
        this.evaluateEndDate = evaluateEndDate;
    }

    @Column(name = "EVALUATE_CNT", length = 18)
    public int getEvaluateCnt() {
        return evaluateCnt;
    }

    public void setEvaluateCnt(int evaluateCnt) {
        this.evaluateCnt = evaluateCnt;
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

    @Column(name = "COMPUTATIONAL_MODEL_ID")
    public String getComputationalModelId() {
        return computationalModelId;
    }

    public void setComputationalModelId(String computationalModelId) {
        this.computationalModelId = computationalModelId;
    }
}
