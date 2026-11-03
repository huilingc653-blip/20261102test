package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 外汇资金池限额设置表
 * @author wangzx
 * @date 2020-02-12 14:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CORP_POOL_LIMIT", schema = "")
@SuppressWarnings("serial")
public class FexCorpPoolLimit extends IdEntity implements java.io.Serializable {

    /**关联ID*/
    private String relatedId;
    /**限额类型 1-外债余额、2-对外放款、3-净流入额*/
    private String limitType;
    /**限额金额*/
    private BigDecimal limitMoney;
    /**资金池标志 1-资金池主体 0-资金池主账户*/
    private String poolSign;

    @Column(name ="RELATED_ID",nullable=true,length=32)
    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    @Column(name ="LIMIT_TYPE",nullable=true,length=1)
    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    @Column(name ="LIMIT_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(BigDecimal limitMoney) {
        this.limitMoney = limitMoney;
    }

    @Column(name ="POOL_SIGN",nullable=true,length=1)
    public String getPoolSign() {
        return poolSign;
    }

    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
}
