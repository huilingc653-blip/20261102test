package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcLoanRepayVO implements Serializable {
	
	private String id;
	/**网点*/
	private String netId;
	/**网点名称*/
	private String netName;
	/**业务类型*/
	private String busType;
	/**贷款账号*/
	@BtCompositeSearch(fieldName = "r.loan_acc")
	private String loanAcc;
	/**户名*/
	private String accName;
	/**应收利息账号*/
	private String needInterestAcc;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "r.corp_id")
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**币别ID*/
	@BtCompositeSearch(fieldName = "r.cur_code")
	private String curCode;
	/**币别名称*/
	private String curName;
	/**还款账号*/
	private String repayAcc;
	/**还款类型*/
	private String repayType;
	/**利息*/
	private BigDecimal interest;
	/**经办利息*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date enterDate;
	/**状态*/
	@BtCompositeSearch(fieldName = "r.status")
	private Integer status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getLoanAcc() {
		return loanAcc;
	}
	public void setLoanAcc(String loanAcc) {
		this.loanAcc = loanAcc;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getRepayAcc() {
		return repayAcc;
	}
	public void setRepayAcc(String repayAcc) {
		this.repayAcc = repayAcc;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getNeedInterestAcc() {
		return needInterestAcc;
	}
	public void setNeedInterestAcc(String needInterestAcc) {
		this.needInterestAcc = needInterestAcc;
	}
	
}
