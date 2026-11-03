package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractWay
 * @Description 担保合同关联担保方式表
 * @date 2018年12月20日
 */
@Entity
@Table(name = "GUA_CONTRACT_WAY", schema = "")
@SuppressWarnings("serial")
public class GuaContractWay extends IdEntity implements java.io.Serializable {
    /** 担保合同id */
    private String guaId;
    /** 担保方式id */
    private String wayCodes;
    /** 担保金额 */
    private BigDecimal money;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保合同id
     */
    @Column(name = "GUA_ID")
    public String getGuaId() {
        return guaId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 担保合同id
     */
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保方式id
     */
    @Column(name = "WAY_CODES")
    public String getWayCodes() {
        return wayCodes;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 担保方式id
     */
    public void setWayCodes(String wayCodes) {
        this.wayCodes = wayCodes;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 担保额度
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 担保额度
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
