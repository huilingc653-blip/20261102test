package com.bytter.biz.finacial.outaccbook.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcOutAccbookVo implements Serializable{
	
	/**Id**/
	@BtCompositeSearch(fieldName = "o.id")
	private java.lang.String id;
	
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "o.tally_date")
	private java.util.Date tallyDate;
	
	/**分户账用到的记账日期参数**/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date ge_tallyDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date le_tallyDate;
	
	/**凭证号*/
	@BtCompositeSearch(fieldName = "o.voucher_no")
	private java.lang.String voucherNo;
	
	/**流水号*/
	@BtCompositeSearch(fieldName = "o.order_no")
	private java.lang.String orderNo;
	/**是否选明细科目**/
	private String isShowChild;
	/**账户*/
	@BtCompositeSearch(fieldName = "d.account")
	private java.lang.String account;
	/**余额表账号**/
	@BtCompositeSearch(fieldName = "b.account")
	private java.lang.String balanceAccount;
	/**户名**/
	@BtCompositeSearch(fieldName = "a.acc_name")
	private String accname;
	
	/***借方发生额**/
	@BtCompositeSearch(fieldName = "d.debit_money")
	private java.math.BigDecimal debitMoney;
	
	/***贷方发生额**/
	@BtCompositeSearch(fieldName = "d.credit_money")
	private java.math.BigDecimal creditMoney;
	
	/**科目名称*/
	@BtCompositeSearch(fieldName = "s.subject_name")
	private java.lang.String subjectName;
	
	/**计量单位**/
	@BtCompositeSearch(fieldName = "s.measure_unit")
	private java.lang.String measureUnit;
	private java.lang.String measureName;
	
	/**摘要**/
	@BtCompositeSearch(fieldName = "d.summary_content")
	private java.lang.String summaryContent;
	
	/**凭证状态*/
	@BtCompositeSearch(fieldName = "o.status")
	private java.lang.Integer status;
	private String statusName;
	/**科目类别**/
	@BtCompositeSearch(fieldName = "s.subject_sort")
	private String subjectSort;
	/**余额查询表里的日期**/
	@BtCompositeSearch(fieldName = "b.opt_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balanceOptDate;
	/**所属网点*/
	@BtCompositeSearch(fieldName = "a.net_Id")
	private java.lang.String netId;
	/**网点名称**/
	private java.lang.String netName;
	/**科目代码**/
	private java.lang.String subjectCode;
	/**期初笔数**/
	private Integer yCount;
	/**期初个数**/
	private Integer yNumber;
	/**期初张数**/
	private Integer yTicket;
	/**收入个数**/
	private Integer debitNumber;
	/**收入张数**/
	private Integer debitTicket;
	/**支出张数**/
	private Integer creditTicket;
	/**期末金额**/
	private BigDecimal tbalance;
	/**期末个数**/
	private Integer tNumber;
	/**期末张数**/
	private Integer tTicket;
	/****/
	private Integer creditNumber;
	/**开户单位ID*/
	@BtCompositeSearch(fieldName = "a.corp_Id")
	private String corpId;
	private String corpName;
	
	/**科目id**/
	private String subjectId;
	
	/**记账员代码*/
	@BtCompositeSearch(fieldName = "o.operator_code")
	private java.lang.String operatorCode;
	
	/**记帐员名称？***/
	@BtCompositeSearch(fieldName = "u.username")
	private String username;    //用户名
	
	/**票据号码*/
	@BtCompositeSearch(fieldName = "o.bills_no")
	private java.lang.String billsNo;
	
	/**结束日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "o.end_date")
	private java.util.Date endDate;
	/**开始日期**/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date startDate;
	/**复核员代码*/
	@BtCompositeSearch(fieldName = "u.login_name")
	private java.lang.String loginName;
	
	/**复核员名称*/
	private java.lang.String checkerName;
	
	/**合同号码*/
	@BtCompositeSearch(fieldName = "o.contract_on")
	private java.lang.String contractNo;
	
	/**结算中心账号**/
	@BtCompositeSearch(fieldName = "a.blnctr_acc")
	private String blnctrAcc;
	
	/**余额方向*/
	@BtCompositeSearch(fieldName = "a.balance_way")
	private Integer balanceWay;
	
	/**日期*/
	@BtCompositeSearch(fieldName = "b.opt_date")
	private String optDate;
	
	/**今天余额*/
	@BtCompositeSearch(fieldName = "b.t_balance")
	private java.math.BigDecimal tBalance;
	/**期初余额*/
	private java.math.BigDecimal yBalance;
	/** 是否显示 子级科目 */
    private boolean viewDetail;
	
    /**余额**/
    private BigDecimal balance;
    
    
    /**期初余额***/
    private String balanceStr;
  
    @BtCompositeSearch(fieldName = "balance_w")
    private String balancew;
	
    /**币别**/
    @BtCompositeSearch(fieldName = "c.cur_code")
	private java.lang.String curId;
    private java.lang.String curName;
    /**当前页**/
    private java.lang.Integer pageNum;
    /**收方笔数**/
    private BigDecimal debitCount;
    /**付方笔数**/
    private BigDecimal creditCount;
    /**余额笔数**/
    private BigDecimal tCount;
    /**总账查询日期**/
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date ledgerOptDate;
    /**是否日结**/
    @BtCompositeSearch(fieldName = "o.over_sign")
    private String overSign;
   
    public Date getLedgerOptDate() {
		return ledgerOptDate;
	}
	public void setLedgerOptDate(Date ledgerOptDate) {
		this.ledgerOptDate = ledgerOptDate;
	}
	public boolean isViewDetail() {
		return viewDetail;
	}
	public void setViewDetail(boolean viewDetail) {
		this.viewDetail = viewDetail;
	}
	
	public java.math.BigDecimal gettBalance() {
		return tBalance;
	}
	public void settBalance(java.math.BigDecimal tBalance) {
		this.tBalance = tBalance;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public java.math.BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(java.math.BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public java.math.BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.lang.String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(java.lang.String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public java.lang.String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(java.lang.String summaryContent) {
		this.summaryContent = summaryContent;
	}
	
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public java.lang.String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(java.lang.String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.lang.String getBillsNo() {
		return billsNo;
	}
	public void setBillsNo(java.lang.String billsNo) {
		this.billsNo = billsNo;
		
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.lang.String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(java.lang.String checkerName) {
		this.checkerName = checkerName;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getContractNo() {
		return contractNo;
	}
	public void setContractNo(java.lang.String contractNo) {
		this.contractNo = contractNo;
	}
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public String getBalanceStr() {
		return balanceStr;
	}
	public void setBalanceStr(String balanceStr) {
		this.balanceStr = balanceStr;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public String getBalancew() {
		return balancew;
	}
	public void setBalancew(String balancew) {
		this.balancew = balancew;
	}
	public String getCorpId() {
		return corpId;
	}
	public java.lang.String getNetName() {
		return netName;
	}
	public void setNetName(java.lang.String netName) {
		this.netName = netName;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubjectSort() {
		return subjectSort;
	}
	public void setSubjectSort(String subjectSort) {
		this.subjectSort = subjectSort;
	}
	public String getOptDate() {
		return optDate;
	}
	public void setOptDate(String optDate) {
		this.optDate = optDate;
	}
	public Date getBalanceOptDate() {
		return balanceOptDate;
	}
	public void setBalanceOptDate(Date balanceOptDate) {
		this.balanceOptDate = balanceOptDate;
	}
	public java.lang.String getBalanceAccount() {
		return balanceAccount;
	}
	public void setBalanceAccount(java.lang.String balanceAccount) {
		this.balanceAccount = balanceAccount;
	}
	public java.lang.Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(java.lang.Integer pageNum) {
		this.pageNum = pageNum;
	}
	public java.util.Date getGe_tallyDate() {
		return ge_tallyDate;
	}
	public void setGe_tallyDate(java.util.Date ge_tallyDate) {
		this.ge_tallyDate = ge_tallyDate;
	}
	public java.util.Date getLe_tallyDate() {
		return le_tallyDate;
	}
	public void setLe_tallyDate(java.util.Date le_tallyDate) {
		this.le_tallyDate = le_tallyDate;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public BigDecimal getCreditCount() {
		return creditCount;
	}
	public void setCreditCount(BigDecimal creditCount) {
		this.creditCount = creditCount;
	}
	public BigDecimal gettCount() {
		return tCount;
	}
	public void settCount(BigDecimal tCount) {
		this.tCount = tCount;
	}
	public java.math.BigDecimal getyBalance() {
		return yBalance;
	}
	public void setyBalance(java.math.BigDecimal yBalance) {
		this.yBalance = yBalance;
	}
	public String getIsShowChild() {
		return isShowChild;
	}
	public void setIsShowChild(String isShowChild) {
		this.isShowChild = isShowChild;
	}
	public Integer getyCount() {
		return yCount;
	}
	public void setyCount(Integer yCount) {
		this.yCount = yCount;
	}
	public Integer getyNumber() {
		return yNumber;
	}
	public void setyNumber(Integer yNumber) {
		this.yNumber = yNumber;
	}
	public Integer getyTicket() {
		return yTicket;
	}
	public void setyTicket(Integer yTicket) {
		this.yTicket = yTicket;
	}
	public Integer getDebitNumber() {
		return debitNumber;
	}
	public void setDebitNumber(Integer debitNumber) {
		this.debitNumber = debitNumber;
	}
	public Integer getDebitTicket() {
		return debitTicket;
	}
	public void setDebitTicket(Integer debitTicket) {
		this.debitTicket = debitTicket;
	}
	public Integer getCreditTicket() {
		return creditTicket;
	}
	public void setCreditTicket(Integer creditTicket) {
		this.creditTicket = creditTicket;
	}
	public BigDecimal getTbalance() {
		return tbalance;
	}
	public void setTbalance(BigDecimal tbalance) {
		this.tbalance = tbalance;
	}
	public Integer gettNumber() {
		return tNumber;
	}
	public void settNumber(Integer tNumber) {
		this.tNumber = tNumber;
	}
	public Integer gettTicket() {
		return tTicket;
	}
	public void settTicket(Integer tTicket) {
		this.tTicket = tTicket;
	}
	public BigDecimal getDebitCount() {
		return debitCount;
	}
	public void setDebitCount(BigDecimal debitCount) {
		this.debitCount = debitCount;
	}
	public Integer getCreditNumber() {
		return creditNumber;
	}
	public void setCreditNumber(Integer creditNumber) {
		this.creditNumber = creditNumber;
	}
	public java.lang.String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(java.lang.String measureName) {
		this.measureName = measureName;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getOverSign() {
		return overSign;
	}
	public void setOverSign(String overSign) {
		this.overSign = overSign;
	}
	public java.lang.String getLoginName() {
		return loginName;
	}
	public void setLoginName(java.lang.String loginName) {
		this.loginName = loginName;
	}
	
}
