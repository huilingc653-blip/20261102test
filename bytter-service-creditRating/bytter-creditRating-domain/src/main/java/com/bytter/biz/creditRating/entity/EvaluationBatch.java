package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 评估批次
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "EVALUATION_BATCH", schema = "")
public class EvaluationBatch extends IdEntity implements java.io.Serializable {

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    private Date createDate;

    /** 评估任务*/
    private String taskNo;

    @Column(name ="TASK_NO",nullable=true,length=32)
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="CREATOR",nullable=true,length=100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
