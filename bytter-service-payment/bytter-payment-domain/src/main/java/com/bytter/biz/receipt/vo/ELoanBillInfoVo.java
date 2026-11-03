package com.bytter.biz.receipt.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhengg
 * @date 2017/8/22
 * @Description 电子回单信息vo
 */
public class ELoanBillInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**账户*/
	private String account;
	/**借方金额*/
	private BigDecimal debitMoney;
	/**贷方金额*/
	private BigDecimal creditMoney;
	/**系统会计科目*/
	private String sysSubjectCode;
	/**系统会计科目名称*/
	private String sysSubjectCodeName;
	/**借方会计科目*/
	private String debitSubjec;
	/**贷方会计科目*/
	private String creditSubjec;
	/**借方账号名*/
	private String debitName;
	/**贷方账号名*/
	private String creditName;
	/**借方会计科目名称*/
	private String debitSubjecName;
	/**贷方会计科目名称*/
	private String creditSubjecName;
	/**借方账号*/
	private String debitAcc;
	/**贷方账号*/
	private String creditAcc;
	/**委托贷款账号*/
	private String precativeAcc;
	/**委托单位*/
	private String precativeCorp;
	/**借款单位*/
	private String loanAcc;
	/**借款单位*/
	private String loanCorp;
	/**户名*/
	private String accNo;
	/**账号名称*/
	private String accName;
	/**开户种类*/
	private String accTypeCode;
	/**借方账号名称*/
	private String debitAccName;
	/**贷方账号名称*/
	private String creditAccName;
	/**网点id*/
	private String netId;
	/**网点名称*/
	private String netName;
	/**借方网点Id*/
	private String debitNetId;
	/**借方网点名称*/
	private String debitNetName;
	/**贷方网点Id*/
	private String creditNetId;
	/**贷方网点名称*/
	private String creditNetName;
	/**币别*/
	private String cur;
	/**币别*/
	private String curIn;
	/**币别*/
	private String curOut;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**票号*/
	private String billNo;
	/**票类型*/
	private String billType;
	/**加密随机数*/
	private String random;
	/**验证码*/
	private String validcode;
	/**编码源*/
	private String encrySource;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**凭证种类名*/
	private String voucherName;
	/**打印次数*/
	private Integer cnt;
	/**附件张数*/
	private Integer affixAmount;
	/**打印次数*/
	private Integer prints;
	/**打印次数*/
	private Integer prints2;
	/**旧票号*/
	private String oldBillno;
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallydate;
	/**还款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date returnDate;
	/**贷款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date loanDate;
	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deadlineDate;
	/**还款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date repaymentDate;
	/**委托贷款金额*/
	private BigDecimal loanMoney;
	/**委托贷款利率*/
	private BigDecimal loanRate;
	/**合同号*/
	private String contractCode;
	/**大写金额*/
	private String captail;
	/**金额*/
	private BigDecimal money;
	/**金额*/
	private BigDecimal moneyRmb;
	/**利率*/
	private BigDecimal tradeRate;
	/**币别名称*/
	private String curName;
	/**起存开始日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**起存结束日期*/
	private Date endDate;
	/**期限天数,注意定期延期处理*/
	private Integer limit;
	/**天数*/
	private Integer days;
	/**通知类型*/
	private Integer notifyType;
	/**利率*/
	private BigDecimal rate;
	/**存款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date depositDate;
	/**委托日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date consignDate ;
	/**存款利率*/
	private BigDecimal depositRate;
	/**利息*/
	private BigDecimal interest;
	/**积数*/
	private BigDecimal cumu;
	/**本息合计*/
	private BigDecimal numMoney;
	/**放款金额*/
	private BigDecimal totolmoney;
	/**个别计息的还款金额*/
	private BigDecimal repayMoney;
	/**对方科目*/
	private String otherSubject;
	/**本方科目*/
	private String homeSubject;
	/**备注*/
	private String rmk;
	/**预提标志*/
	private String predrawSign;
	/**利息ID*/
	private String interestDtlId;
	/***/
	private String interestSign;
	/**借方单位名*/
	private String debitCorpId;
	/**借方单位名*/
	private String debitCorpName;
	/**贷方单位名*/
	private String creditCorpId;
	/**贷方单位名*/
	private String creditCorpName;
	/**付款单位开户行*/
	private String payerBank;
	/**收款单位开户行*/
	private String payeeBank;
	/**摘要*/
	private String summaryContent ;
	/**票据业务类型*/
	private String briefId ;
	/**票据单位ID*/
	private String oldDeptId;
	/**原凭证*/
	private String origVoucherNo ;
	/**原始凭证id*/
	private String origVoucherId;
	/**凭证类型ID*/
	private String voucherType;
	/**凭证类型系统代码*/
	private String voucherCode;
	/**复核员*/
	private String checkerCode ;
	/**记帐员*/
	private String operatorCode;
	/**凭证状态*/
	private String voucherState;
	/**委托存款科目*/
	private String precativeSubject;
	/**委托存款科目名称*/
	private String precativeSubjectName;
	/**委托存款账号名称*/
	private String precativeAccName;
	/**委托贷款科目*/
	private String loanSubject;
	/**委托贷款科目名称*/
	private String loanSubjectName;
	/**委托贷款账号名称*/
	private String loanAccName;
	/**借方利息账号名称*/
	private String debitInterestAccName;
	/**借方利息科目*/
	private String debitInterestSubject;
	/**借方利息科目名称*/
	private String debitInterestSubjectName;
	/**贷方利息账号名称*/
	private String creditInterestAccName;
	/**贷方利息科目*/
	private String creditInterestSubject;
	/**贷方利息科目名称*/
	private String creditInterestSubjectName;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getTradeRate() {
		return tradeRate;
	}
	public void setTradeRate(BigDecimal tradeRate) {
		this.tradeRate = tradeRate;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public BigDecimal getTotolmoney() {
		return totolmoney;
	}
	public void setTotolmoney(BigDecimal totolmoney) {
		this.totolmoney = totolmoney;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public Date getConsignDate() {
		return consignDate;
	}
	public void setConsignDate(Date consignDate) {
		this.consignDate = consignDate;
	}
	public String getDebitCorpId() {
		return debitCorpId;
	}
	public void setDebitCorpId(String debitCorpId) {
		this.debitCorpId = debitCorpId;
	}
	public String getDebitCorpName() {
		return debitCorpName;
	}
	public void setDebitCorpName(String debitCorpName) {
		this.debitCorpName = debitCorpName;
	}
	public String getDebitNetName() {
		return debitNetName;
	}
	public void setDebitNetName(String debitNetName) {
		this.debitNetName = debitNetName;
	}
	public String getCreditNetName() {
		return creditNetName;
	}
	public void setCreditNetName(String creditNetName) {
		this.creditNetName = creditNetName;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getCreditCorpName() {
		return creditCorpName;
	}
	public void setCreditCorpName(String creditCorpName) {
		this.creditCorpName = creditCorpName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
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
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(Integer notifyType) {
		this.notifyType = notifyType;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Date getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}
	public BigDecimal getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(BigDecimal depositRate) {
		this.depositRate = depositRate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public String getSysSubjectCode() {
		return sysSubjectCode;
	}
	public void setSysSubjectCode(String sysSubjectCode) {
		this.sysSubjectCode = sysSubjectCode;
	}
	public String getDebitSubjec() {
		return debitSubjec;
	}
	public void setDebitSubjec(String debitSubjec) {
		this.debitSubjec = debitSubjec;
	}
	public String getCreditSubjec() {
		return creditSubjec;
	}
	public void setCreditSubjec(String creditSubjec) {
		this.creditSubjec = creditSubjec;
	}
	public String getDebitSubjecName() {
		return debitSubjecName;
	}
	public void setDebitSubjecName(String debitSubjecName) {
		this.debitSubjecName = debitSubjecName;
	}
	public String getCreditSubjecName() {
		return creditSubjecName;
	}
	public void setCreditSubjecName(String creditSubjecName) {
		this.creditSubjecName = creditSubjecName;
	}
	public String getDebitAcc() {
		return debitAcc;
	}
	public void setDebitAcc(String debitAcc) {
		this.debitAcc = debitAcc;
	}
	public String getCreditAcc() {
		return creditAcc;
	}
	public void setCreditAcc(String creditAcc) {
		this.creditAcc = creditAcc;
	}
	public String getPrecativeAcc() {
		return precativeAcc;
	}
	public void setPrecativeAcc(String precativeAcc) {
		this.precativeAcc = precativeAcc;
	}
	public String getPrecativeCorp() {
		return precativeCorp;
	}
	public void setPrecativeCorp(String precativeCorp) {
		this.precativeCorp = precativeCorp;
	}
	public String getLoanAcc() {
		return loanAcc;
	}
	public void setLoanAcc(String loanAcc) {
		this.loanAcc = loanAcc;
	}
	public String getLoanCorp() {
		return loanCorp;
	}
	public void setLoanCorp(String loanCorp) {
		this.loanCorp = loanCorp;
	}
	public String getDebitAccName() {
		return debitAccName;
	}
	public void setDebitAccName(String debitAccName) {
		this.debitAccName = debitAccName;
	}
	public String getCreditAccName() {
		return creditAccName;
	}
	public void setCreditAccName(String creditAccName) {
		this.creditAccName = creditAccName;
	}
	public String getCur() {
		return cur;
	}
	public void setCur(String cur) {
		this.cur = cur;
	}
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
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	public String getEncrySource() {
		return encrySource;
	}
	public void setEncrySource(String encrySource) {
		this.encrySource = encrySource;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public Integer getPrints() {
		return prints;
	}
	public void setPrints(Integer prints) {
		this.prints = prints;
	}
	public Integer getPrints2() {
		return prints2;
	}
	public void setPrints2(Integer prints2) {
		this.prints2 = prints2;
	}
	public String getOldBillno() {
		return oldBillno;
	}
	public void setOldBillno(String oldBillno) {
		this.oldBillno = oldBillno;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public Date getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public BigDecimal getLoanRate() {
		return loanRate;
	}
	public void setLoanRate(BigDecimal loanRate) {
		this.loanRate = loanRate;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getCaptail() {
		return captail;
	}
	public void setCaptail(String captail) {
		this.captail = captail;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public BigDecimal getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(BigDecimal loanMoney) {
		this.loanMoney = loanMoney;
	}
	public String getSysSubjectCodeName() {
		return sysSubjectCodeName;
	}
	public void setSysSubjectCodeName(String sysSubjectCodeName) {
		this.sysSubjectCodeName = sysSubjectCodeName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccTypeCode() {
		return accTypeCode;
	}
	public void setAccTypeCode(String accTypeCode) {
		this.accTypeCode = accTypeCode;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getCumu() {
		return cumu;
	}
	public void setCumu(BigDecimal cumu) {
		this.cumu = cumu;
	}
	public BigDecimal getNumMoney() {
		return numMoney;
	}
	public void setNumMoney(BigDecimal numMoney) {
		this.numMoney = numMoney;
	}
	public BigDecimal getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(BigDecimal repayMoney) {
		this.repayMoney = repayMoney;
	}
	public String getOtherSubject() {
		return otherSubject;
	}
	public void setOtherSubject(String otherSubject) {
		this.otherSubject = otherSubject;
	}
	public String getHomeSubject() {
		return homeSubject;
	}
	public void setHomeSubject(String homeSubject) {
		this.homeSubject = homeSubject;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getPredrawSign() {
		return predrawSign;
	}
	public void setPredrawSign(String predrawSign) {
		this.predrawSign = predrawSign;
	}
	public String getInterestDtlId() {
		return interestDtlId;
	}
	public void setInterestDtlId(String interestDtlId) {
		this.interestDtlId = interestDtlId;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getTallydate() {
		return tallydate;
	}
	public void setTallydate(Date tallydate) {
		this.tallydate = tallydate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getCreditCorpId() {
		return creditCorpId;
	}
	public void setCreditCorpId(String creditCorpId) {
		this.creditCorpId = creditCorpId;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getDebitNetId() {
		return debitNetId;
	}
	public void setDebitNetId(String debitNetId) {
		this.debitNetId = debitNetId;
	}
	public String getCreditNetId() {
		return creditNetId;
	}
	public void setCreditNetId(String creditNetId) {
		this.creditNetId = creditNetId;
	}
	public String getOldDeptId() {
		return oldDeptId;
	}
	public void setOldDeptId(String oldDeptId) {
		this.oldDeptId = oldDeptId;
	}
	public String getDebitName() {
		return debitName;
	}
	public void setDebitName(String debitName) {
		this.debitName = debitName;
	}
	public String getCreditName() {
		return creditName;
	}
	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}
	public BigDecimal getMoneyRmb() {
		return moneyRmb;
	}
	public void setMoneyRmb(BigDecimal moneyRmb) {
		this.moneyRmb = moneyRmb;
	}
	public String getCurIn() {
		return curIn;
	}
	public void setCurIn(String curIn) {
		this.curIn = curIn;
	}
	public String getCurOut() {
		return curOut;
	}
	public void setCurOut(String curOut) {
		this.curOut = curOut;
	}
	public Integer getAffixAmount() {
		return affixAmount;
	}
	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}
	public String getPrecativeSubject() {
		return precativeSubject;
	}
	public void setPrecativeSubject(String precativeSubject) {
		this.precativeSubject = precativeSubject;
	}
	public String getPrecativeSubjectName() {
		return precativeSubjectName;
	}
	public void setPrecativeSubjectName(String precativeSubjectName) {
		this.precativeSubjectName = precativeSubjectName;
	}
	public String getLoanSubject() {
		return loanSubject;
	}
	public void setLoanSubject(String loanSubject) {
		this.loanSubject = loanSubject;
	}
	public String getLoanSubjectName() {
		return loanSubjectName;
	}
	public void setLoanSubjectName(String loanSubjectName) {
		this.loanSubjectName = loanSubjectName;
	}
	public String getPrecativeAccName() {
		return precativeAccName;
	}
	public void setPrecativeAccName(String precativeAccName) {
		this.precativeAccName = precativeAccName;
	}
	public String getLoanAccName() {
		return loanAccName;
	}
	public void setLoanAccName(String loanAccName) {
		this.loanAccName = loanAccName;
	}
	public String getDebitInterestSubject() {
		return debitInterestSubject;
	}
	public void setDebitInterestSubject(String debitInterestSubject) {
		this.debitInterestSubject = debitInterestSubject;
	}
	public String getDebitInterestSubjectName() {
		return debitInterestSubjectName;
	}
	public void setDebitInterestSubjectName(String debitInterestSubjectName) {
		this.debitInterestSubjectName = debitInterestSubjectName;
	}
	public String getCreditInterestSubject() {
		return creditInterestSubject;
	}
	public void setCreditInterestSubject(String creditInterestSubject) {
		this.creditInterestSubject = creditInterestSubject;
	}
	public String getCreditInterestSubjectName() {
		return creditInterestSubjectName;
	}
	public void setCreditInterestSubjectName(String creditInterestSubjectName) {
		this.creditInterestSubjectName = creditInterestSubjectName;
	}
	public String getDebitInterestAccName() {
		return debitInterestAccName;
	}
	public void setDebitInterestAccName(String debitInterestAccName) {
		this.debitInterestAccName = debitInterestAccName;
	}
	public String getCreditInterestAccName() {
		return creditInterestAccName;
	}
	public void setCreditInterestAccName(String creditInterestAccName) {
		this.creditInterestAccName = creditInterestAccName;
	}
	public String getOrigVoucherId() {
		return origVoucherId;
	}
	public void setOrigVoucherId(String origVoucherId) {
		this.origVoucherId = origVoucherId;
	}
	@Override
	public String toString() {
		return "ELoanBillInfoVo{" +
				"account='" + account + '\'' +
				", debitMoney=" + debitMoney +
				", creditMoney=" + creditMoney +
				", sysSubjectCode='" + sysSubjectCode + '\'' +
				", sysSubjectCodeName='" + sysSubjectCodeName + '\'' +
				", debitSubjec='" + debitSubjec + '\'' +
				", creditSubjec='" + creditSubjec + '\'' +
				", debitName='" + debitName + '\'' +
				", creditName='" + creditName + '\'' +
				", debitSubjecName='" + debitSubjecName + '\'' +
				", creditSubjecName='" + creditSubjecName + '\'' +
				", debitAcc='" + debitAcc + '\'' +
				", creditAcc='" + creditAcc + '\'' +
				", precativeAcc='" + precativeAcc + '\'' +
				", precativeCorp='" + precativeCorp + '\'' +
				", loanAcc='" + loanAcc + '\'' +
				", loanCorp='" + loanCorp + '\'' +
				", accNo='" + accNo + '\'' +
				", accName='" + accName + '\'' +
				", accTypeCode='" + accTypeCode + '\'' +
				", debitAccName='" + debitAccName + '\'' +
				", creditAccName='" + creditAccName + '\'' +
				", netId='" + netId + '\'' +
				", netName='" + netName + '\'' +
				", debitNetId='" + debitNetId + '\'' +
				", debitNetName='" + debitNetName + '\'' +
				", creditNetId='" + creditNetId + '\'' +
				", creditNetName='" + creditNetName + '\'' +
				", cur='" + cur + '\'' +
				", curIn='" + curIn + '\'' +
				", curOut='" + curOut + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", billNo='" + billNo + '\'' +
				", billType='" + billType + '\'' +
				", random='" + random + '\'' +
				", validcode='" + validcode + '\'' +
				", encrySource='" + encrySource + '\'' +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", voucherName='" + voucherName + '\'' +
				", cnt=" + cnt +
				", affixAmount=" + affixAmount +
				", prints=" + prints +
				", prints2=" + prints2 +
				", oldBillno='" + oldBillno + '\'' +
				", tallydate=" + tallydate +
				", returnDate=" + returnDate +
				", loanDate=" + loanDate +
				", deadlineDate=" + deadlineDate +
				", repaymentDate=" + repaymentDate +
				", loanMoney=" + loanMoney +
				", loanRate=" + loanRate +
				", contractCode='" + contractCode + '\'' +
				", captail='" + captail + '\'' +
				", money=" + money +
				", moneyRmb=" + moneyRmb +
				", tradeRate=" + tradeRate +
				", curName='" + curName + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", limit=" + limit +
				", days=" + days +
				", notifyType=" + notifyType +
				", rate=" + rate +
				", depositDate=" + depositDate +
				", consignDate=" + consignDate +
				", depositRate=" + depositRate +
				", interest=" + interest +
				", cumu=" + cumu +
				", numMoney=" + numMoney +
				", totolmoney=" + totolmoney +
				", repayMoney=" + repayMoney +
				", otherSubject='" + otherSubject + '\'' +
				", homeSubject='" + homeSubject + '\'' +
				", rmk='" + rmk + '\'' +
				", predrawSign='" + predrawSign + '\'' +
				", interestDtlId='" + interestDtlId + '\'' +
				", interestSign='" + interestSign + '\'' +
				", debitCorpId='" + debitCorpId + '\'' +
				", debitCorpName='" + debitCorpName + '\'' +
				", creditCorpId='" + creditCorpId + '\'' +
				", creditCorpName='" + creditCorpName + '\'' +
				", payerBank='" + payerBank + '\'' +
				", payeeBank='" + payeeBank + '\'' +
				", summaryContent='" + summaryContent + '\'' +
				", briefId='" + briefId + '\'' +
				", oldDeptId='" + oldDeptId + '\'' +
				", origVoucherNo='" + origVoucherNo + '\'' +
				", origVoucherId='" + origVoucherId + '\'' +
				", voucherType='" + voucherType + '\'' +
				", voucherCode='" + voucherCode + '\'' +
				", checkerCode='" + checkerCode + '\'' +
				", operatorCode='" + operatorCode + '\'' +
				", voucherState='" + voucherState + '\'' +
				", precativeSubject='" + precativeSubject + '\'' +
				", precativeSubjectName='" + precativeSubjectName + '\'' +
				", precativeAccName='" + precativeAccName + '\'' +
				", loanSubject='" + loanSubject + '\'' +
				", loanSubjectName='" + loanSubjectName + '\'' +
				", loanAccName='" + loanAccName + '\'' +
				", debitInterestAccName='" + debitInterestAccName + '\'' +
				", debitInterestSubject='" + debitInterestSubject + '\'' +
				", debitInterestSubjectName='" + debitInterestSubjectName + '\'' +
				", creditInterestAccName='" + creditInterestAccName + '\'' +
				", creditInterestSubject='" + creditInterestSubject + '\'' +
				", creditInterestSubjectName='" + creditInterestSubjectName + '\'' +
				'}';
	}
}
