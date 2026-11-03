package com.bytter.biz.pool.entity;
import com.bytter.core.base.entity.TreeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
/**
 * @Title: Entity
 * @Description: 资金池单位表
 * @author wangzx
 * @date 2019-04-30 16:44:38
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CORP_POOL", schema = "")
@SuppressWarnings("serial")
public class FcCorpPool extends TreeEntity<FcCorpPool> implements java.io.Serializable {
    /**单位ID*/
    private java.lang.String corpId;
    /**资金池代码*/
    private java.lang.String poolCode;
    /**资金池名称*/
    private java.lang.String poolName;
    /**状态*/
    private java.lang.String status;
    /**所属资金池*/
    private java.lang.String poolId;
    /**主体单位标识*/
    private java.lang.String poolSign;
    /**备注*/
    private java.lang.String rmk;
    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="POOL_CODE",nullable=false,length=20)
    public String getPoolCode() {
        return poolCode;
    }
    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }
    @Column(name ="POOL_NAME",nullable=true,length=40)
    public String getPoolName() {
        return poolName;
    }
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
    @Column(name ="STATUS",nullable=true,length=1)
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name ="POOL_ID",nullable=true,length=32)
    public String getPoolId() {
        return poolId;
    }
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    @Column(name ="POOL_SIGN",nullable=true,length=1)
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    @Column(name ="RMK",nullable=true,length=100)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Override
    @Transient
    public String getParentId() {
        return this.poolId;
    }
}
