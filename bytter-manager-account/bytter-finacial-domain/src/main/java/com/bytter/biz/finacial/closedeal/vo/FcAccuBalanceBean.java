package com.bytter.biz.finacial.closedeal.vo;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 账户积数Bean
 * @author liyh
 *
 */
public class FcAccuBalanceBean implements Serializable {
	
	private String id;
	/**
	 * 账户
	 */
	private String account;
    private String curId;
	
	/**
	 * 积数日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balanceDate;
	
	/**
	 * 计息标识
	 */
	private String interestSign;
	
	/**
	 * 预提标识
	 */
	private String preSign;
	
	/**
	 * 积数
	 */
	private BigDecimal cumu;
	/**
	 * 累计调整积数
	 */
	private BigDecimal adjustedCumu;
	
	/**
	 * 当天调整积数
	 */
	private BigDecimal adjustCumu;
	/**
	 * 协定ID
	 */
	private String agreeDepositId;
	
	private String agreedDepositId;
	private Integer agreedType;
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
	* 协定段
	*/
	private String agreeType;
	public String getAgreeDepositId() {
		return agreeDepositId;
	}
	public void setAgreeDepositId(String agreeDepositId) {
		this.agreeDepositId = agreeDepositId;
	}
	public String getAgreeType() {
		return agreeType;
	}
	public void setAgreeType(String agreeType) {
		this.agreeType = agreeType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public String getInterestSign() {
		if (EmptyUtils.isEmpty(this.interestSign)) {
			this.interestSign = CloseConstant.INTEREST_SIGN_N;
		}
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public String getPreSign() {
		if (EmptyUtils.isEmpty(this.preSign)) {
			this.preSign = CloseConstant.INTEREST_SIGN_N;
		}
		return preSign;
	}
	public void setPreSign(String preSign) {
		this.preSign = preSign;
	}
	public BigDecimal getCumu() {
		return cumu;
	}
	public void setCumu(BigDecimal cumu) {
		this.cumu = cumu;
	}
	
	public BigDecimal getAdjustedCumu() {
		return adjustedCumu;
	}
	public void setAdjustedCumu(BigDecimal adjustedCumu) {
		this.adjustedCumu = adjustedCumu;
	}
	public BigDecimal getAdjustCumu() {
		return adjustCumu;
	}
	public void setAdjustCumu(BigDecimal adjustCumu) {
		this.adjustCumu = adjustCumu;
	}
	public void addCumu(BigDecimal balance) {
		if (null == this.cumu) {
			this.cumu = BigDecimal.ZERO;
		}
		this.cumu = cumu.add(balance);
	}
	public void addAdjustedCumu(BigDecimal balance) {
		if (null == this.adjustedCumu) {
			this.adjustedCumu = BigDecimal.ZERO;
		}
		this.adjustedCumu = adjustedCumu.add(balance);
	}
	public void addAdjustCumu(BigDecimal balance) {
		if (null == this.adjustCumu) {
			this.adjustCumu = BigDecimal.ZERO;
		}
		this.adjustCumu = adjustCumu.add(balance);
	}
	public String getAgreedDepositId() {
		return agreedDepositId;
	}
	public void setAgreedDepositId(String agreedDepositId) {
		this.agreedDepositId = agreedDepositId;
	}
	public void setAgreedType(Integer agreedType) {
		this.agreedType = agreedType;
	}
	public Integer getAgreedType() {
		return agreedType;
	}
}
