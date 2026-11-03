package com.bytter.ras.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhengg
 * @projectName T2
 * @description: 自动稽核时使用稽核表和营收实收对应中间表
 * @date 2019/5/15
 */
@Entity
@Table(name = "RAS_AUDIT_INTER", schema = "")
public class RasAuditInter implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 稽核表ID
	 */
	private String id;
	
	/**
	 * 营收表ID
	 */
    private String revenueId;
	
	/**
	 * 实收表ID
	 */
    private String receivedId;

    /**用来做线程锁定*/
    private String unId;
    
    /** 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
	private String payType;
	/** 门店id */
	private String shopId;
	/** 稽核码 */
	private String auditNo;
	
	/** 稽核金额 */
	private BigDecimal money;
	/**
	 * 规则：多个以,隔开01订单号02交易日期03交易流水04门店名称05单位名称06现金汇总稽核07其他方式汇总稽核
	 */
	private String rules;
	
	/** 创建日期 */
	private Date createDate;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "REVENUE_ID")
	public String getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(String revenueId) {
		this.revenueId = revenueId;
	}

    @Column(name = "RECEIVED_ID")
	public String getReceivedId() {
		return receivedId;
	}

	public void setReceivedId(String receivedId) {
		this.receivedId = receivedId;
	}

    @Column(name = "UN_ID")
	public String getUnId() {
		return unId;
	}

	public void setUnId(String unId) {
		this.unId = unId;
	}

    @Column(name = "PAY_TYPE")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

    @Column(name = "SHOP_ID")
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

    @Column(name = "AUDIT_NO")
	public String getAuditNo() {
		return auditNo;
	}

	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}

    @Column(name = "MONEY")
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

    @Column(name = "RULES")
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

    @Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "RasAuditInter [id=" + id + ", revenueId=" + revenueId + ", receivedId=" + receivedId + ", unId=" + unId
				+ ", getId()=" + getId() + ", getRevenueId()=" + getRevenueId() + ", getReceivedId()=" + getReceivedId()
				+ ", getUnId()=" + getUnId() + "]";
	}
    
}
