package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 余额汇总VO
 * @author lvwl
 */
public class CmsBalanceGatherVO implements Serializable{

	/**单位*/
	private String corpId;
	private String corpName;
	private String corpCode;
	/**币别*/
	@Currency(inject = "curName")
	private String curId;
	private String curName;
	private String naturalSign;
	private BigDecimal curExrate;
	
	/**融资方式*/
	@Dict(dictGroup = "Loan.Sign", inject = "loanSignName")
	private String loanSign;
	private String loanSignName;
	
	/**期初余额*/
	private BigDecimal beformMoney;
	/**收入*/
	private BigDecimal earningMoney;
	/**支出*/
	private BigDecimal defrayMoney;
	
	/**日期范围*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	/**借款编号*/
	private String infoCode;
	/**借款合同同*/
	private String inofContractNo;
	/**借款金额*/
	private BigDecimal plMoney;
	/**借款开始日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date plStartDate;
	/**借款结束日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date plEndDate;
	/**发放编号*/
	private String sendCode;
	/**发放合同*/
	private String sendContractNo;
	/**发放金额*/
	private BigDecimal sendMoney;
	/**发放日期*/
	private Date sendDate;
	/**还款金额*/
	private BigDecimal repayMoney;
	/**还款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date repayDate;
	/**发放余额*/
	private BigDecimal sendBalance;

	private List<CmsBalanceGatherVO> provideList;
	private List<CmsBalanceGatherVO> precativeList;
	private List<CmsBalanceGatherVO> loanList;
	private BigDecimal balance;
	private String searchDate;
	
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getLoanSign() {
		return loanSign;
	}
	public void setLoanSign(String loanSign) {
		this.loanSign = loanSign;
	}
	public String getLoanSignName() {
		return loanSignName;
	}
	public void setLoanSignName(String loanSignName) {
		this.loanSignName = loanSignName;
	}
	public BigDecimal getBeformMoney() {
		return beformMoney;
	}
	public void setBeformMoney(BigDecimal beformMoney) {
		this.beformMoney = beformMoney;
	}
	public BigDecimal getEarningMoney() {
		return earningMoney;
	}
	public void setEarningMoney(BigDecimal earningMoney) {
		this.earningMoney = earningMoney;
	}
	public BigDecimal getDefrayMoney() {
		return defrayMoney;
	}
	public void setDefrayMoney(BigDecimal defrayMoney) {
		this.defrayMoney = defrayMoney;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getNaturalSign() {
		return naturalSign;
	}
	public void setNaturalSign(String naturalSign) {
		this.naturalSign = naturalSign;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}
	public String getInfoCode() {
		return infoCode;
	}
	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}
	public String getInofContractNo() {
		return inofContractNo;
	}
	public void setInofContractNo(String inofContractNo) {
		this.inofContractNo = inofContractNo;
	}
	public BigDecimal getPlMoney() {
		return plMoney;
	}
	public void setPlMoney(BigDecimal plMoney) {
		this.plMoney = plMoney;
	}
	public Date getPlStartDate() {
		return plStartDate;
	}
	public void setPlStartDate(Date plStartDate) {
		this.plStartDate = plStartDate;
	}
	public Date getPlEndDate() {
		return plEndDate;
	}
	public void setPlEndDate(Date plEndDate) {
		this.plEndDate = plEndDate;
	}
	public String getSendCode() {
		return sendCode;
	}
	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}
	public String getSendContractNo() {
		return sendContractNo;
	}
	public void setSendContractNo(String sendContractNo) {
		this.sendContractNo = sendContractNo;
	}
	public BigDecimal getSendMoney() {
		return sendMoney;
	}
	public void setSendMoney(BigDecimal sendMoney) {
		this.sendMoney = sendMoney;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public BigDecimal getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(BigDecimal repayMoney) {
		this.repayMoney = repayMoney;
	}
	public Date getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}
	public BigDecimal getSendBalance() {
		return sendBalance;
	}
	public void setSendBalance(BigDecimal sendBalance) {
		this.sendBalance = sendBalance;
	}

	public List<CmsBalanceGatherVO> getProvideList() {
		return provideList;
	}

	public void setProvideList(List<CmsBalanceGatherVO> provideList) {
		this.provideList = provideList;
	}

	public List<CmsBalanceGatherVO> getPrecativeList() {
		return precativeList;
	}

	public void setPrecativeList(List<CmsBalanceGatherVO> precativeList) {
		this.precativeList = precativeList;
	}

	public List<CmsBalanceGatherVO> getLoanList() {
		return loanList;
	}

	public void setLoanList(List<CmsBalanceGatherVO> loanList) {
		this.loanList = loanList;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
}
