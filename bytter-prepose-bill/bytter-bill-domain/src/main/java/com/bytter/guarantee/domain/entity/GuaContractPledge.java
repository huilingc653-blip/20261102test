package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractPledge
 * @Description 担保合同关联抵质押物表
 * @date 2018年12月20日
 */
@Entity
@Table(name = "GUA_CONTRACT_PLEDGE", schema = "")
@SuppressWarnings("serial")
public class GuaContractPledge extends IdEntity implements java.io.Serializable {
    /** 担保方式id */
    private String guaContractWayId;
    /** 抵质押物id */
    private String pledgeId;
    /** 担保占用额度 */
    private java.math.BigDecimal guaMoney;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保合同方式id
     */
    @Column(name = "GUA_CONTRACT_WAY_ID")
    public String getGuaContractWayId() {
        return guaContractWayId;
    }
    /**
     * 方法: 设置ava.lang.String
     * 
     * @return: java.lang.String 担保合同方式id
     */
    public void setGuaContractWayId(String guaContractWayId) {
        this.guaContractWayId = guaContractWayId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抵质押物id
     */
    @Column(name = "PLEDGE_ID")
    public String getPledgeId() {
        return pledgeId;
    }
    /**
     * 方法: 设置ava.lang.String
     * 
     * @return: java.lang.String 抵质押物id
     */
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return:java.math.BigDecimal 担保占用额度
     */
    @Column(name = "GUA_MONEY")
    public java.math.BigDecimal getGuaMoney() {
        return guaMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return:java.math.BigDecimal 担保占用额度
     */
    public void setGuaMoney(java.math.BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
}
