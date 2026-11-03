package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcBankBalanceAdjustVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**银行账面余额*/
	@BtCompositeSearch(fieldName="a.bank_init_balance")
	private java.math.BigDecimal bankInitBalance;
	/**企业账面余额*/
	@BtCompositeSearch(fieldName="a.bln_init_balance")
	private java.math.BigDecimal blnInitBalance;
	/**银行调节余额*/
	@BtCompositeSearch(fieldName="a.bank_adj_bal")
	private java.math.BigDecimal bankAdjBal;
	/**企业调节余额*/
	@BtCompositeSearch(fieldName="a.bln_adj_balance")
	private java.math.BigDecimal blnAdjBalance;
	/**结转日期*/
	@BtCompositeSearch(fieldName="a.adjust_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date adjustDate;
	/**开始日期*/
	@BtCompositeSearch(fieldName="a.begin_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date beginDate;
	/**结束日期*/
	@BtCompositeSearch(fieldName="a.end_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endDate;
	/**结转用户*/
	@BtCompositeSearch(fieldName="a.adj_code")
	private String adjCode;
	/**状态*/
	private Integer status;
	/**反结转时间*/
	@BtCompositeSearch(fieldName="a.readj_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date readjDate;
	/**反结转用户*/
	private String readjUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;
	/**企业账号*/
	@BtCompositeSearch(fieldName="a.account")
	private String account;
	/**币别代码*/
	@Currency(inject="curName")
	@BtCompositeSearch(fieldName="a.cur_code")
	private String curCode;
	/**币别名称*/
	private String curName;
	/**银行账号*/
	@BtCompositeSearch(fieldName="a.bank_acc")
	private String bankAcc;
	/**账户名*/
	private String accName;
	/**开户行*/
	private String bankName;
	/**对账类型*/
	@BtCompositeSearch(fieldName="a.check_type")
	private String checkType;
	/**网点*/
    @Corp(inject = "corpName")
	private String netId;
	
	/**单位名称*/
	private String corpName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.math.BigDecimal getBankInitBalance() {
		return bankInitBalance;
	}
	public void setBankInitBalance(java.math.BigDecimal bankInitBalance) {
		this.bankInitBalance = bankInitBalance;
	}
	public java.math.BigDecimal getBlnInitBalance() {
		return blnInitBalance;
	}
	public void setBlnInitBalance(java.math.BigDecimal blnInitBalance) {
		this.blnInitBalance = blnInitBalance;
	}
	public java.math.BigDecimal getBankAdjBal() {
		return bankAdjBal;
	}
	public void setBankAdjBal(java.math.BigDecimal bankAdjBal) {
		this.bankAdjBal = bankAdjBal;
	}
	public java.math.BigDecimal getBlnAdjBalance() {
		return blnAdjBalance;
	}
	public void setBlnAdjBalance(java.math.BigDecimal blnAdjBalance) {
		this.blnAdjBalance = blnAdjBalance;
	}
	public java.util.Date getAdjustDate() {
		return adjustDate;
	}
	public void setAdjustDate(java.util.Date adjustDate) {
		this.adjustDate = adjustDate;
	}
	public java.util.Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public String getAdjCode() {
		return adjCode;
	}
	public void setAdjCode(String adjCode) {
		this.adjCode = adjCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public java.util.Date getReadjDate() {
		return readjDate;
	}
	public void setReadjDate(java.util.Date readjDate) {
		this.readjDate = readjDate;
	}
	public String getReadjUser() {
		return readjUser;
	}
	public void setReadjUser(String readjUser) {
		this.readjUser = readjUser;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
