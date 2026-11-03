package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇资金池账户表
 * @author wangzx
 * @date 2020-02-12 14:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CORP_POOL_BANKACC", schema = "")
@SuppressWarnings("serial")
public class FexCorpPoolBankacc extends IdEntity implements java.io.Serializable {

    /**上级ID（主账户上级ID为资金池主体ID，成员账户上级ID为主账户ID）*/
    private String parentId;
    /**单位ID*/
    private String corpId;
    /**账号ID*/
    private String accId;
    /**币别ID*/
    private String curId;
    /**账户资金性质 1-国内主账户、2-国外主账户、3-经常项目、4-资本项目、5-NRA账户*/
    private String accNature;
    /**备注*/
    private String rmk;
    /**是否主账户 0-否、1-是*/
    private String parentSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="PARENT_ID",nullable=true,length=32)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="ACC_ID",nullable=true,length=32)
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="ACC_NATURE",nullable=true,length=1)
    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    @Column(name ="RMK",nullable=true,length=100)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="PARENT_SIGN",nullable=true,length=100)
    public String getParentSign() {
        return parentSign;
    }

    public void setParentSign(String parentSign) {
        this.parentSign = parentSign;
    }

    @Column(name ="CREATE_BY",nullable=true,length=32)
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

    @Column(name ="UPDATE_BY",nullable=true,length=32)
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
