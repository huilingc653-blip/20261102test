package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 额度信息VO
 * @Description: 额度信息Vo
 * @date 2020-2-20
 */
@SuppressWarnings("serial")
public class AbPoolLimitVO implements java.io.Serializable {
	/**流水Id*/
    private String id;
    /**单位Id*/
    @BtCompositeSearch(fieldName="pc.corp_id")
    private String corpId;
    
    /**单位代码*/
    private String corpCode;
    
    /**单位名称*/
    @BtCompositeSearch(fieldName="pc.corp_name")
    private String corpName;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 票据质押额度
     */
    @BtCompositeSearch(fieldName="l.pledge_Money")
    private BigDecimal pledgeMoney;
    /**保证金余额*/
    @BtCompositeSearch(fieldName="l.bail_money")
    private BigDecimal bailMoney;
    /**
     * 票据池总额度
     */
    @BtCompositeSearch(fieldName="l.pool_Money")
    private BigDecimal poolMoney;
    /**
     * 票据池可用额度
     */
    @BtCompositeSearch(fieldName="l.valid_Money")
    private BigDecimal validMoney;
    /**
     * 票据池已用额度
     */
    @BtCompositeSearch(fieldName="l.lock_Money")
    private BigDecimal lockMoney;
    /**
     * 状态：1启用、0禁用 默认禁用
     */
    private String validSign;
    /**
     * 额度日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date limitDate;
    /**
     * 额度日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;
    /**
     * 创建日期
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="l.create_Time")
    private Date createTime;
    /**票据池编号*/
    @BtCompositeSearch(fieldName="p.id")
    private String poolNo;
    
    /**票据池名称*/
    @BtCompositeSearch(fieldName="p.bp_name")
    private String poolName;
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
    /**设置票据池日资产明细信息**/
    private Map<String,Object> poolDtl=null;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public BigDecimal getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    public BigDecimal getPoolMoney() {
        return poolMoney;
    }
    public void setPoolMoney(BigDecimal poolMoney) {
        this.poolMoney = poolMoney;
    }
    public BigDecimal getValidMoney() {
        return validMoney;
    }
    public void setValidMoney(BigDecimal validMoney) {
        this.validMoney = validMoney;
    }
    public BigDecimal getLockMoney() {
        return lockMoney;
    }
    public void setLockMoney(BigDecimal lockMoney) {
        this.lockMoney = lockMoney;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public Date getLimitDate() {
        return limitDate;
    }
    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getPoolNo() {
		return poolNo;
	}
	public void setPoolNo(String poolNo) {
		this.poolNo = poolNo;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
    public Map<String, Object> getPoolDtl() {
        return poolDtl;
    }
    public void setPoolDtl(Map<String, Object> poolDtl) {
        this.poolDtl = poolDtl;
    }
    public Date getQueryDate() {
        return queryDate;
    }
    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }
    public BigDecimal getLowRiskMoney() {
        return lowRiskMoney;
    }
    public void setLowRiskMoney(BigDecimal lowRiskMoney) {
        this.lowRiskMoney = lowRiskMoney;
    }
    public BigDecimal getInLowRiskMoney() {
        return inLowRiskMoney;
    }
    public void setInLowRiskMoney(BigDecimal inLowRiskMoney) {
        this.inLowRiskMoney = inLowRiskMoney;
    }
    public BigDecimal getUsedLowRiskMoney() {
        return usedLowRiskMoney;
    }
    public void setUsedLowRiskMoney(BigDecimal usedLowRiskMoney) {
        this.usedLowRiskMoney = usedLowRiskMoney;
    }
    public BigDecimal getUsedInLowRiskMoney() {
        return usedInLowRiskMoney;
    }
    public void setUsedInLowRiskMoney(BigDecimal usedInLowRiskMoney) {
        this.usedInLowRiskMoney = usedInLowRiskMoney;
    }
    public BigDecimal getValidLowRiskMoney() {
        return validLowRiskMoney;
    }
    public void setValidLowRiskMoney(BigDecimal validLowRiskMoney) {
        this.validLowRiskMoney = validLowRiskMoney;
    }
    public BigDecimal getValidInLowRiskMoney() {
        return validInLowRiskMoney;
    }
    public void setValidInLowRiskMoney(BigDecimal validInLowRiskMoney) {
        this.validInLowRiskMoney = validInLowRiskMoney;
    }
    @Override
    public String toString() {
        return "AbPoolLimitVO{" +
                "id='" + id + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", bpId='" + bpId + '\'' +
                ", pledgeMoney=" + pledgeMoney +
                ", bailMoney=" + bailMoney +
                ", poolMoney=" + poolMoney +
                ", validMoney=" + validMoney +
                ", lockMoney=" + lockMoney +
                ", validSign='" + validSign + '\'' +
                ", limitDate=" + limitDate +
                ", queryDate=" + queryDate +
                ", createTime=" + createTime +
                ", poolNo='" + poolNo + '\'' +
                ", poolName='" + poolName + '\'' +
                ", lowRiskMoney=" + lowRiskMoney +
                ", inLowRiskMoney=" + inLowRiskMoney +
                ", usedLowRiskMoney=" + usedLowRiskMoney +
                ", usedInLowRiskMoney=" + usedInLowRiskMoney +
                ", validLowRiskMoney=" + validLowRiskMoney +
                ", validInLowRiskMoney=" + validInLowRiskMoney +
                ", poolDtl=" + poolDtl +
                '}';
    }
}
