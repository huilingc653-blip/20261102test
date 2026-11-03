package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
public class LoanRepayAddBean implements Serializable {
	/**
	 * 单位ID
	 */
	private String corpId;
	/**
	 *贷款账号 
	 */
	private String loanAcc;
	/**
	 * 还款账号
	 */
	private String repayAcc;
	/**
	 * 应收利息账号
	 */
	private String needinterestAcc;
	/**
	 * 还款明细ID
	 */
	private String repayId;
	/**
	 * 记账日期
	 */
	private Date tallyDate;
	/**
	 * 利息
	 */
	private BigDecimal interest;
	/**
	 * 操作类型
	 */
	private Integer opType;
	/**
	 * 分录明细
	 */
	private List<FcAccbookDtl> accbookDtls;
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getLoanAcc() {
		return loanAcc;
	}
	public void setLoanAcc(String loanAcc) {
		this.loanAcc = loanAcc;
	}
	public String getRepayAcc() {
		return repayAcc;
	}
	public void setRepayAcc(String repayAcc) {
		this.repayAcc = repayAcc;
	}
	public String getNeedinterestAcc() {
		return needinterestAcc;
	}
	public void setNeedinterestAcc(String needinterestAcc) {
		this.needinterestAcc = needinterestAcc;
	}
	public String getRepayId() {
		return repayId;
	}
	public void setRepayId(String repayId) {
		this.repayId = repayId;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public Integer getOpType() {
		return opType;
	}
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	public List<FcAccbookDtl> getAccbookDtls() {
		return accbookDtls;
	}
	public void setAccbookDtls(List<FcAccbookDtl> accbookDtls) {
		this.accbookDtls = accbookDtls;
	} 
}
