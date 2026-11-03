package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 汇款人信息表
 * @author huangx
 * @date 2019-07-15 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_PAYER_INFO", schema = "")
@SuppressWarnings("serial")
public class FexPayerInfo extends IdEntity implements java.io.Serializable {
    /**汇款账号*/
    private String payerAcc;
    /**汇款人名称（英文）*/
    private String payerNameEn;
    /**汇款人地址（英文）*/
    private String payerAddEn;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="PAYER_ACC",nullable=true,length=30)
    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    @Column(name ="PAYER_NAME_EN",nullable=true,length=50)
    public String getPayerNameEn() {
        return payerNameEn;
    }

    public void setPayerNameEn(String payerNameEn) {
        this.payerNameEn = payerNameEn;
    }

    @Column(name ="PAYER_ADD_EN",nullable=true,length=200)
    public String getPayerAddEn() {
        return payerAddEn;
    }

    public void setPayerAddEn(String payerAddEn) {
        this.payerAddEn = payerAddEn;
    }

    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
