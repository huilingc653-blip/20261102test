package com.bytter.bill.domain.billbank.pool.vo;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @Title:  出票融资申请登记薄明细表Vo
 * @author: zhenghj
 * @date:   2022-3-11
 * @version V1.0
 */
public class AbPoolFinaApplyDtlVo implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 登记薄ID
     */
    private String bookId;
    /**
     * 单位id
     */
    private String corpId;
    /**
     *申请代码
     */
    private String applyCode;
    /**
     *客户标识号
     */
    private String custId;
    /**
     *票据号码
     */
    private String origNo;
     /**
     *票据金额
     */
    private BigDecimal billMoney;
     /**
     *业务种类 03:(出票、承兑）05:(出票、承兑、提示收票) 09：贴现
     */
    private String busType;
     /**
     *票据状态
     */
    private String billStatus;
     /**
     *出票人
     */
    private String payer;
     /**
     *出票人账号
     */
    private String payerAcc;
     /**
     *出票人开户银行名称
     */
    private String payerBankName;
     /**
     *出票人开户银行行号
     */
    private String payerBankNo;
     /**
     *收票人全称
     */
    private String payee;
     /**
     *收票人账号
     */
    private String payeeAcc;
     /**
     *收票人开户银行名称
     */
    private String payeeBankName;
     /**
     *收票人开户银行行号
     */
    private String payeeBankno;
     /**
     *承兑人
     */
    private String acceptor;
     /**
     *承兑人账号
     */
    private String acceptorAcc;
     /**
     *承兑人开户行行号
     */
    private String acceptorBankNo;
     /**
     *承兑人开户行名称
     */
    private String acceptorBankName;
     /**
     * 出票日期
     */
    private Date startDate;
     /**
     *到期日期
     */
    private Date endDate;
     /**
     *票据类型：10 银票、11 商票
     */
    private Integer billType;
     /**
     *发票号
     */
    private String invcNo;
     /**
     *实付金额
     */
    private BigDecimal realpayAmt;
     /**
     *赎回实付金额,
     */
    private BigDecimal redmrealpayAmt;
     /**
     *不得转让标记EM00可再转让EM01不得转让
     */
    private String banendrsmtmk;
     /**
     *手续费
     */
    private BigDecimal promsfee;
     /**
     *创建日期
     */
    private Date createDate;
     /**
     *更新日期
     */
    private Date updateDate;
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getPayerBankNo() {
		return payerBankNo;
	}
	public void setPayerBankNo(String payerBankNo) {
		this.payerBankNo = payerBankNo;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPayeeBankno() {
		return payeeBankno;
	}
	public void setPayeeBankno(String payeeBankno) {
		this.payeeBankno = payeeBankno;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptorAcc() {
		return acceptorAcc;
	}
	public void setAcceptorAcc(String acceptorAcc) {
		this.acceptorAcc = acceptorAcc;
	}
	public String getAcceptorBankNo() {
		return acceptorBankNo;
	}
	public void setAcceptorBankNo(String acceptorBankNo) {
		this.acceptorBankNo = acceptorBankNo;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
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
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getInvcNo() {
		return invcNo;
	}
	public void setInvcNo(String invcNo) {
		this.invcNo = invcNo;
	}
	public BigDecimal getRealpayAmt() {
		return realpayAmt;
	}
	public void setRealpayAmt(BigDecimal realpayAmt) {
		this.realpayAmt = realpayAmt;
	}
	public BigDecimal getRedmrealpayAmt() {
		return redmrealpayAmt;
	}
	public void setRedmrealpayAmt(BigDecimal redmrealpayAmt) {
		this.redmrealpayAmt = redmrealpayAmt;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public BigDecimal getPromsfee() {
		return promsfee;
	}
	public void setPromsfee(BigDecimal promsfee) {
		this.promsfee = promsfee;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "AbPoolFinaApplyDtlVo [bookId=" + bookId + ", corpId=" + corpId
				+ ", applyCode=" + applyCode + ", custId=" + custId
				+ ", origNo=" + origNo + ", billMoney=" + billMoney
				+ ", busType=" + busType + ", billStatus=" + billStatus
				+ ", payer=" + payer + ", payerAcc=" + payerAcc
				+ ", payerBankName=" + payerBankName + ", payerBankNo="
				+ payerBankNo + ", payee=" + payee + ", payeeAcc=" + payeeAcc
				+ ", payeeBankName=" + payeeBankName + ", payeeBankno="
				+ payeeBankno + ", acceptor=" + acceptor + ", acceptorAcc="
				+ acceptorAcc + ", acceptorBankNo=" + acceptorBankNo
				+ ", acceptorBankName=" + acceptorBankName + ", startDate="
				+ startDate + ", endDate=" + endDate + ", billType=" + billType
				+ ", invcNo=" + invcNo + ", realpayAmt=" + realpayAmt
				+ ", redmrealpayAmt=" + redmrealpayAmt + ", banendrsmtmk="
				+ banendrsmtmk + ", promsfee=" + promsfee + ", createDate="
				+ createDate + ", updateDate=" + updateDate + "]";
	}
    
}
