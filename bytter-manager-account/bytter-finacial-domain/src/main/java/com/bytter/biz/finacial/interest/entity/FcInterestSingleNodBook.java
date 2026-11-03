package com.bytter.biz.finacial.interest.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 企业通知存款个别计息-通知预约
 *
 * @author zuoy 2020-12-22
 */
@Entity
@Table(name = "FC_INTEREST_SINGLE_NOD_BOOK", schema = "")
public class FcInterestSingleNodBook extends IdEntity implements java.io.Serializable {
    /**
     * 单据号码
     */
    private String billNo;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 通知存款账号ID
     */
    private String noticeAccountId;
    /**
     * 通知存款账号
     */
    private String noticeAccount;
    /**
     * 币种
     */
    private String curId;
    /**
     * 预约金额
     */
    private BigDecimal bookMoney;
    /**
     * 预约日期
     */
    private Date bookDate;
    /**余额*/
    private BigDecimal balance;
    /**利率*/
    private BigDecimal interestRate;
    /**
     * 起息日期
     */
    private Date interestStartDate;
    /**
     * 业务状态
     */
    private Integer status;
    /**
     * 通知类型
     */
    private String noticeType;
    /**
     * 本金存入账号ID
     */
    private String capitalAccId;
    /**
     * 本金存入账号
     */
    private String capitalAcc;
    /**
     * 利息存入账号ID
     */
    private String interestAccId;
    /**
     * 利息存入账号
     */
    private String interestAcc;
    /**
     * 预计利息
     */
    private BigDecimal expectInterest;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 最后更新时间
     */
    private Date updateDate;
    /**
     * 新增冗余字段 NOTICE_ACC_TYPE
     */
    private String noticeAccType;
    @Column(name = "NOTICE_ACC_TYPE")
    public String getNoticeAccType() {
        return noticeAccType;
    }
    public void setNoticeAccType(String noticeAccType) {
        this.noticeAccType = noticeAccType;
    }
    @Column(name ="BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="NOTICE_ACCOUNT_ID")
    public String getNoticeAccountId() {
        return noticeAccountId;
    }
    public void setNoticeAccountId(String noticeAccountId) {
        this.noticeAccountId = noticeAccountId;
    }
    @Column(name ="NOTICE_ACCOUNT")
    public String getNoticeAccount() {
        return noticeAccount;
    }
    public void setNoticeAccount(String noticeAccount) {
        this.noticeAccount = noticeAccount;
    }
    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="BOOK_MONEY")
    public BigDecimal getBookMoney() {
        return bookMoney;
    }
    public void setBookMoney(BigDecimal bookMoney) {
        this.bookMoney = bookMoney;
    }
    @Column(name ="BOOK_DATE")
    public Date getBookDate() {
        return bookDate;
    }
    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }
    @Column(name ="BALANCE")
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    @Column(name ="INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name ="INTEREST_START_DATE")
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="NOTICE_TYPE")
    public String getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
    @Column(name ="CAPITAL_ACC_ID")
    public String getCapitalAccId() {
        return capitalAccId;
    }
    public void setCapitalAccId(String capitalAccId) {
        this.capitalAccId = capitalAccId;
    }
    @Column(name ="CAPITAL_ACC")
    public String getCapitalAcc() {
        return capitalAcc;
    }
    public void setCapitalAcc(String capitalAcc) {
        this.capitalAcc = capitalAcc;
    }
    @Column(name ="INTEREST_ACC_ID")
    public String getInterestAccId() {
        return interestAccId;
    }
    public void setInterestAccId(String interestAccId) {
        this.interestAccId = interestAccId;
    }
    @Column(name ="INTEREST_ACC")
    public String getInterestAcc() {
        return interestAcc;
    }
    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }
    @Column(name ="EXPECT_INTEREST")
    public BigDecimal getExpectInterest() {
        return expectInterest;
    }
    public void setExpectInterest(BigDecimal expectInterest) {
        this.expectInterest = expectInterest;
    }
    @Column(name ="PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name ="REMARK")
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
