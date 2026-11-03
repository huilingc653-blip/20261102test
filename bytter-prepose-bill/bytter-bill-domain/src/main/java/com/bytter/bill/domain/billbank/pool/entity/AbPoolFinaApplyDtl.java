package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @Title:  出票融资申请登记薄明细表entity
 * @author: zhenghj
 * @date:   2022-3-9
 * @version V1.0
 */
@Entity
@Table(name = "AB_POOL_FINAAPPLY_DTL", schema = "")
@SuppressWarnings("serial")
public class AbPoolFinaApplyDtl extends IdEntity implements java.io.Serializable{
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
    @Column(name = "BOOK_ID", nullable = true)
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    @Column(name="CORP_ID",nullable = true)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "APPLY_CODE", nullable = true)
    public String getApplyCode() {
        return applyCode;
    }
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }
    @Column(name = "CUST_ID", nullable = true)
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    @Column(name = "ORIG_NO", nullable = true)
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    @Column(name = "BILL_MONEY", nullable = true)
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    @Column(name = "BUS_TYPE", nullable = true)
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name = "BILL_STATUS", nullable = true)
    public String getBillStatus() {
        return billStatus;
    }
    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    @Column(name = "PAYER", nullable = true)
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    @Column(name = "PAYERACC", nullable = true)
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    @Column(name = "PAYER_BANK_NAME", nullable = true)
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    @Column(name = "PAYERBANKNO", nullable = true)
    public String getPayerBankNo() {
        return payerBankNo;
    }
    public void setPayerBankNo(String payerBankNo) {
        this.payerBankNo = payerBankNo;
    }
    @Column(name = "PAYEE", nullable = true)
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    @Column(name = "PAYEEACC", nullable = true)
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    @Column(name = "PAYEE_BANK_NAME", nullable = true)
    public String getPayeeBankName() {
        return payeeBankName;
    }
    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }
    @Column(name = "PAYEEBANKNO", nullable = true)
    public String getPayeeBankno() {
        return payeeBankno;
    }
    public void setPayeeBankno(String payeeBankno) {
        this.payeeBankno = payeeBankno;
    }
    @Column(name = "ACCEPTOR", nullable = true)
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    @Column(name = "ACCEPTORACC", nullable = true)
    public String getAcceptorAcc() {
        return acceptorAcc;
    }
    public void setAcceptorAcc(String acceptorAcc) {
        this.acceptorAcc = acceptorAcc;
    }
    @Column(name = "ACCEPTORBANKNO", nullable = true)
    public String getAcceptorBankNo() {
        return acceptorBankNo;
    }
    public void setAcceptorBankNo(String acceptorBankNo) {
        this.acceptorBankNo = acceptorBankNo;
    }
    @Column(name = "ACCEPTOR_BANK_NAME", nullable = true)
    public String getAcceptorBankName() {
        return acceptorBankName;
    }
    public void setAcceptorBankName(String acceptorBankName) {
        this.acceptorBankName = acceptorBankName;
    }
    @Column(name = "START_DATE", nullable = true)
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "BILL_TYPE", nullable = true)
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    @Column(name = "INVCNO", nullable = true)
    public String getInvcNo() {
        return invcNo;
    }
    public void setInvcNo(String invcNo) {
        this.invcNo = invcNo;
    }
    @Column(name = "REALPAY_AMT", nullable = true)
    public BigDecimal getRealpayAmt() {
        return realpayAmt;
    }
    public void setRealpayAmt(BigDecimal realpayAmt) {
        this.realpayAmt = realpayAmt;
    }
    @Column(name = "REDMREALPAY_AMT", nullable = true)
    public BigDecimal getRedmrealpayAmt() {
        return redmrealpayAmt;
    }
    public void setRedmrealpayAmt(BigDecimal redmrealpayAmt) {
        this.redmrealpayAmt = redmrealpayAmt;
    }
    @Column(name = "BANENDRSMTMK", nullable = true)
    public String getBanendrsmtmk() {
        return banendrsmtmk;
    }
    public void setBanendrsmtmk(String banendrsmtmk) {
        this.banendrsmtmk = banendrsmtmk;
    }
    @Column(name = "PROMSFEE", nullable = true)
    public BigDecimal getPromsfee() {
        return promsfee;
    }
    public void setPromsfee(BigDecimal promsfee) {
        this.promsfee = promsfee;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
	@Override
	public String toString() {
		return "AbPoolFinaApplyDtl [bookId=" + bookId + ", corpId=" + corpId
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
