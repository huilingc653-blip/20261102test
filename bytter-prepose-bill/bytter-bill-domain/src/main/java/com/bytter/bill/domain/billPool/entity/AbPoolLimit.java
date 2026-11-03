package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 
* @Title:  额度信息entity   
* @Package com.bytter.biz.billPool.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2020-1-13 上午10:07:38   
* @version V1.0
 */
@Entity
@Table(name = "AB_POOL_LIMIT", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimit extends IdEntity implements java.io.Serializable{
	 /**池接口代码**/
    private String corpId;
    
    /**
     * 票据池ID
     */
    private String bpId;
    
    /**
     * 票据质押额度
     */
    private BigDecimal pledgeMoney;
    
    
    /**
     * 保证金额度
     */
    private BigDecimal bailMoney;
    
    
    /**
     * 票据池总额度
     */
    private BigDecimal poolMoney;
    
    /**
     * 票据池可用额度
     */
    private BigDecimal validMoney;
    
    /**
     * 票据池已用额度
     */
    private BigDecimal lockMoney;
    
    /**
     * 状态：1启用、0禁用 默认禁用
     */
	private String validSign;
    
    /**
     * 更新日期
     */
    private Date limitDate;
    
    
    /**
     * 创建日期
     */
    private Date createTime;
	/**
	 * 低风险额度
	 */
	private BigDecimal lowRiskMoney;
	/**
	 * 非低风险额度
	 */
	private BigDecimal inLowRiskMoney;
	/**
	 * 已用低风险额度
	 */
	private BigDecimal usedLowRiskMoney;
	/**
	 * 已用非低风险额度
	 */
	private BigDecimal usedInLowRiskMoney;
	/**
	 * 可用低风险额度
	 */
	private BigDecimal validLowRiskMoney;
	/**
	 * 可用非低风险额度
	 */
	private BigDecimal validInLowRiskMoney;
    @Column(name = "CORP_ID", nullable = true, length = 32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "BP_ID", nullable = true, length = 32)
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	@Column(name = "PLEDGE_MONEY", nullable = true)
	public BigDecimal getPledgeMoney() {
		return pledgeMoney;
	}
	public void setPledgeMoney(BigDecimal pledgeMoney) {
		this.pledgeMoney = pledgeMoney;
	}
	@Column(name = "BAIL_MONEY", nullable = true)
	public BigDecimal getBailMoney() {
		return bailMoney;
	}
	public void setBailMoney(BigDecimal bailMoney) {
		this.bailMoney = bailMoney;
	}
	@Column(name = "POOL_MONEY", nullable = true)
	public BigDecimal getPoolMoney() {
		return poolMoney;
	}
	public void setPoolMoney(BigDecimal poolMoney) {
		this.poolMoney = poolMoney;
	}
	@Column(name = "VALID_MONEY", nullable = true)
	public BigDecimal getValidMoney() {
		return validMoney;
	}
	public void setValidMoney(BigDecimal validMoney) {
		this.validMoney = validMoney;
	}
	@Column(name = "LOCK_MONEY", nullable = true)
	public BigDecimal getLockMoney() {
		return lockMoney;
	}
	public void setLockMoney(BigDecimal lockMoney) {
		this.lockMoney = lockMoney;
	}
    @Column(name = "VALID_SIGN", nullable = true, length = 2)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name = "LIMIT_DATE", nullable = true)
	public Date getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}
	@Column(name = "CREATE_TIME", nullable = true)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "LOWRISK_MONEY", nullable = true)
	public BigDecimal getLowRiskMoney() {
		return lowRiskMoney;
	}
	public void setLowRiskMoney(BigDecimal lowRiskMoney) {
		this.lowRiskMoney = lowRiskMoney;
	}
	@Column(name = "INLOWRISK_MONEY", nullable = true)
	public BigDecimal getInLowRiskMoney() {
		return inLowRiskMoney;
	}
	public void setInLowRiskMoney(BigDecimal inLowRiskMoney) {
		this.inLowRiskMoney = inLowRiskMoney;
	}
	@Column(name = "USED_LOWRISK_MONEY", nullable = true)
	public BigDecimal getUsedLowRiskMoney() {
		return usedLowRiskMoney;
	}
	public void setUsedLowRiskMoney(BigDecimal usedLowRiskMoney) {
		this.usedLowRiskMoney = usedLowRiskMoney;
	}
	@Column(name = "USED_INLOWRISK_MONEY", nullable = true)
	public BigDecimal getUsedInLowRiskMoney() {
		return usedInLowRiskMoney;
	}
	public void setUsedInLowRiskMoney(BigDecimal usedInLowRiskMoney) {
		this.usedInLowRiskMoney = usedInLowRiskMoney;
	}
	@Column(name = "VALID_LOWRISK_MONEY", nullable = true)
	public BigDecimal getValidLowRiskMoney() {
		return validLowRiskMoney;
	}
	public void setValidLowRiskMoney(BigDecimal validLowRiskMoney) {
		this.validLowRiskMoney = validLowRiskMoney;
	}
	@Column(name = "VALID_INLOWRISK_MONEY", nullable = true)
	public BigDecimal getValidInLowRiskMoney() {
		return validInLowRiskMoney;
	}
	public void setValidInLowRiskMoney(BigDecimal validInLowRiskMoney) {
		this.validInLowRiskMoney = validInLowRiskMoney;
	}
}
