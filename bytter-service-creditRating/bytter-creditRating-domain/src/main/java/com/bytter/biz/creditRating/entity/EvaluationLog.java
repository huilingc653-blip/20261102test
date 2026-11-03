package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 评估操作日志
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */
@Entity
@Table(name = "EVALUATION_LOG", schema = "")
public class EvaluationLog extends IdEntity implements java.io.Serializable {
    /**外部引用ID(流水表ID)*/
    private String quoteId;

    /**日志类型*/
    private String logType;

    /**日志消息*/
    private String msg;

    /**创建人名称*/
    private String createName;

    /**创建人登录名称*/
    private String createBy;

    /**创建日期*/
    private Date createDate;

    @Column(name ="QUOTE_ID",nullable=true,length=32)
    public String getQuoteId(){
        return this.quoteId;
    }

    public void setQuoteId(String quoteId){
        this.quoteId = quoteId;
    }

    @Column(name ="LOG_TYPE",nullable=true,length=10)
    public String getLogType(){
        return this.logType;
    }

    public void setLogType(String logType){
        this.logType = logType;
    }

    @Column(name ="MSG",nullable=true,length=1000)
    public String getMsg(){
        return this.msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    @Column(name ="CREATE_NAME",nullable=true,length=50)
    public String getCreateName(){
        return this.createName;
    }

    public void setCreateName(String createName){
        this.createName = createName;
    }

    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy(){
        return this.createBy;
    }

    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate(){
        return this.createDate;
    }

    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
}
