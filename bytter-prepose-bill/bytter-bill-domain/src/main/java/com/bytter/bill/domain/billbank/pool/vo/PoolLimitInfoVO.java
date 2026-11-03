package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 集团单位额度和池信息VO
 * 
 */
public class PoolLimitInfoVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** <CUSTNAME/> 客户名称 必回 */
	private String custName;
	/** <CONTRACTNO/> 合同编号 必回 */
	private String conNo;
	/** <CONTRACTDATE/> 合同日期 必回 */
	private Date conDate;
	/** <SIGNORGNO/> 签约机构号 必回 */
	private String signOrigNo;
	/** <SIGNORGNAME/> 签约机构名称 必回 */
	private String signOrigName;
	/** <SIGNDATE/> 签约日期 必回 */
	private Date signDate;
	/** <BILLPOOLSTATUS/> 票据池状态，0-已解约 1-已签约 必回 */
	private String poolStatus;
	/** <BILLREMAINS/> 票据质押余额 必回 */
	private BigDecimal pledgeMoney;
	/** <BILLPOOLLIMIT/> 票据池额度 必回 */
	private BigDecimal poolMoney;
	/** <LOWRISKLIMIT/> 低风险额度 必回 */
	private BigDecimal lowriskMoney;
	/** <INLOWRISKLIMIT/> 非低风险额度 必回 */
	private BigDecimal inLowriskMoney;
	/** <ADJUSTLIMIT/> 票据池调剂额度 必回 */
	private BigDecimal adjustMoney;
	/** <USEDLIMIT/> 已用额度 必回 */
	private BigDecimal usedMoney;
	/** <USEDLOWRISKLIMIT/> 已用低风险额度 必回 */
	private BigDecimal usedLowriskMoney;
	/** <USEDINLOWRISKLIMIT/> 已用非低风险额度 必回 */
	private BigDecimal usedInLowriskMoney;
	
	/** 
	 * <AVAIL_LIMIT/> 可用额度
	 * 额度管控模式为“1-统收统支”时，赋集团可用额度，为“2-自收自支”时赋单一客户可用额度
	 * 版本标识为1.0时不显示
	 */
	private BigDecimal validMoney;
	/** <AVAIL_LOWRISK_LIMIT/>
	 * 可用低风险额度
	 * 额度管控模式为“1-统收统支”时，赋集团可用低风险额度，为“2-自收自支”时赋单一客户可用低风险额度
	 * 版本标识为1.0时不显示
	 *  
	 *  */
	private BigDecimal validLowriskMoney;
	/** <AVAIL_INLOWRISK_LIMIT/> 可用非低风险额度
	 * 额度管控模式为“1-统收统支”时，赋集团可用非低风险额度，为“2-自收自支”时赋单一客户可用非低风险额度
	 * 版本标识为1.0时不显示
	 */
	private BigDecimal validInLowriskMoney;
	/**
	 * 保证金账号
	 */
	private String bailBankAcc;
	/**
	 * 保证金金额
	 */
	private BigDecimal bailMoney;
	/**
	 * 备注
	 */
	private String rmk;
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}
	public Date getConDate() {
		return conDate;
	}
	public void setConDate(Date conDate) {
		this.conDate = conDate;
	}
	public String getSignOrigNo() {
		return signOrigNo;
	}
	public void setSignOrigNo(String signOrigNo) {
		this.signOrigNo = signOrigNo;
	}
	public String getSignOrigName() {
		return signOrigName;
	}
	public void setSignOrigName(String signOrigName) {
		this.signOrigName = signOrigName;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getPoolStatus() {
		return poolStatus;
	}
	public void setPoolStatus(String poolStatus) {
		this.poolStatus = poolStatus;
	}
	public BigDecimal getPledgeMoney() {
		return pledgeMoney;
	}
	public void setPledgeMoney(BigDecimal pledgeMoney) {
		this.pledgeMoney = pledgeMoney;
	}
	public BigDecimal getPoolMoney() {
		return poolMoney;
	}
	public void setPoolMoney(BigDecimal poolMoney) {
		this.poolMoney = poolMoney;
	}
	public BigDecimal getLowriskMoney() {
		return lowriskMoney;
	}
	public void setLowriskMoney(BigDecimal lowriskMoney) {
		this.lowriskMoney = lowriskMoney;
	}
	public BigDecimal getInLowriskMoney() {
		return inLowriskMoney;
	}
	public void setInLowriskMoney(BigDecimal inLowriskMoney) {
		this.inLowriskMoney = inLowriskMoney;
	}
	public BigDecimal getUsedMoney() {
		return usedMoney;
	}
	public void setUsedMoney(BigDecimal usedMoney) {
		this.usedMoney = usedMoney;
	}
	public BigDecimal getUsedLowriskMoney() {
		return usedLowriskMoney;
	}
	public void setUsedLowriskMoney(BigDecimal usedLowriskMoney) {
		this.usedLowriskMoney = usedLowriskMoney;
	}
	public BigDecimal getUsedInLowriskMoney() {
		return usedInLowriskMoney;
	}
	public void setUsedInLowriskMoney(BigDecimal usedInLowriskMoney) {
		this.usedInLowriskMoney = usedInLowriskMoney;
	}
	public String getBailBankAcc() {
		return bailBankAcc;
	}
	public void setBailBankAcc(String bailBankAcc) {
		this.bailBankAcc = bailBankAcc;
	}
	public BigDecimal getBailMoney() {
		return bailMoney;
	}
	public void setBailMoney(BigDecimal bailMoney) {
		this.bailMoney = bailMoney;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}
	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}
	
	public BigDecimal getValidMoney() {
		return validMoney;
	}
	public void setValidMoney(BigDecimal validMoney) {
		this.validMoney = validMoney;
	}
	public BigDecimal getValidLowriskMoney() {
		return validLowriskMoney;
	}
	public void setValidLowriskMoney(BigDecimal validLowriskMoney) {
		this.validLowriskMoney = validLowriskMoney;
	}
	public BigDecimal getValidInLowriskMoney() {
		return validInLowriskMoney;
	}
	public void setValidInLowriskMoney(BigDecimal validInLowriskMoney) {
		this.validInLowriskMoney = validInLowriskMoney;
	}
	@Override
	public String toString() {
		return "PoolLimitInfoVO [custName=" + custName + ", conNo=" + conNo
				+ ", conDate=" + conDate + ", signOrigNo=" + signOrigNo
				+ ", signOrigName=" + signOrigName + ", signDate=" + signDate
				+ ", poolStatus=" + poolStatus + ", pledgeMoney=" + pledgeMoney
				+ ", poolMoney=" + poolMoney + ", lowriskMoney=" + lowriskMoney
				+ ", inLowriskMoney=" + inLowriskMoney + ", adjustMoney="
				+ adjustMoney + ", usedMoney=" + usedMoney
				+ ", usedLowriskMoney=" + usedLowriskMoney
				+ ", usedInLowriskMoney=" + usedInLowriskMoney
				+ ", validMoney=" + validMoney + ", validLowriskMoney="
				+ validLowriskMoney + ", validInLowriskMoney="
				+ validInLowriskMoney + ", bailBankAcc=" + bailBankAcc
				+ ", bailMoney=" + bailMoney + ", rmk=" + rmk + "]";
	}
}
