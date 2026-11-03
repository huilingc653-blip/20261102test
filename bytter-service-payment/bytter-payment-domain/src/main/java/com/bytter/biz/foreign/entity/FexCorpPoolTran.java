package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇资金池交易表
 * @author wangzx
 * @date 2020-02-21 16:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CORP_POOL_TRAN", schema = "")
@SuppressWarnings("serial")
public class FexCorpPoolTran extends IdEntity implements java.io.Serializable {

    /**资金池ID*/
    private String poolId;
    /**关联主账号ID*/
    private String bankaccId;
    /**交易标志 0-借入外债交易、1-对外放款交易*/
    private String tranSign;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**产品ID*/
    private String proId;
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

    @Column(name ="BANKACC_ID",nullable=true,length=32)
    public String getBankaccId() {
        return bankaccId;
    }

    public void setBankaccId(String bankaccId) {
        this.bankaccId = bankaccId;
    }

    @Column(name ="TRAN_SIGN",nullable=true,length=1)
    public String getTranSign() {
        return tranSign;
    }

    public void setTranSign(String tranSign) {
        this.tranSign = tranSign;
    }

    @Column(name ="TRAN_TYPE",nullable=true,length=1)
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="PRO_ID",nullable=true,length=32)
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
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
