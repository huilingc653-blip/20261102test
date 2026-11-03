package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇资金池表
 * @author wangzx
 * @date 2020-02-12 14:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CORP_POOL", schema = "")
@SuppressWarnings("serial")
public class FexCorpPool extends IdEntity implements java.io.Serializable {

    /**资金池ID*/
    private String poolId;
    /**资金池性质 1-跨境资金集中运营、2-境内外币资金池、3-境外外币资金池、4-跨境双向人民币资金池*/
    private String poolNature;
    /**是否限额管控 0-否、1-是*/
    private String isLimitControl;
    /**限额折算币种*/
    private String curId;
    /**限额折算汇率类型 0-记账汇率、1-市场汇率*/
    private String rateType;
    /**备注*/
    private String rmk;
    /**有效标志 1-有效 0-无效*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="POOL_ID",nullable=true,length=32)
    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    @Column(name ="POOL_NATURE",nullable=true,length=1)
    public String getPoolNature() {
        return poolNature;
    }

    public void setPoolNature(String poolNature) {
        this.poolNature = poolNature;
    }

    @Column(name ="IS_LIMIT_CONTROL",nullable=true,length=1)
    public String getIsLimitControl() {
        return isLimitControl;
    }

    public void setIsLimitControl(String isLimitControl) {
        this.isLimitControl = isLimitControl;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="RATE_TYPE",nullable=true,length=32)
    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    @Column(name ="RMK",nullable=true,length=100)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="VALID_SIGN",nullable=true,length=1)
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
