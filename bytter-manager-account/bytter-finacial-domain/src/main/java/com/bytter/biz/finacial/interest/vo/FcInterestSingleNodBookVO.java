package com.bytter.biz.finacial.interest.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 企业通知存款个别计息-通知预约
 *
 * @author zuoy 2020-12-22
 */
public class FcInterestSingleNodBookVO implements java.io.Serializable {
    private String id;
    /**
     * 单据号码
     */
    @BtCompositeSearch(fieldName = "t.bill_No")
    private String billNo;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpName;
    /**
     * 通知存款账号ID
     */
    private String noticeAccountId;
    /**
     * 通知存款账号
     */
    @BtCompositeSearch(fieldName = "t.notice_Account")
    private String noticeAccount;
    /**
     * 通知存款账号名称
     */
    private String noticeAccountName;
    /**
     * 币种
     */
    private String curId;
    private String curName;
    /**
     * 预约金额
     */
    @BtCompositeSearch(fieldName = "t.book_Money")
    private BigDecimal bookMoney;
    /**
     * 预约日期
     */
    @BtCompositeSearch(fieldName = "t.book_Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bookDate;
    /**余额*/
    private BigDecimal balance;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 起息日期
     */
    private Date interestStartDate;
    /**
     * 业务状态
     */
    @BtCompositeSearch(fieldName = "t.status")
    private String status;
    /**
     * 通知类型
     */
    @BtCompositeSearch(fieldName = "t.notice_Type")
    private String noticeType;
    private String noticeTypeName;
    /**
     * 本金存入账号ID
     */
    private String capitalAccId;
    /**
     * 本金存入账号
     */
    private String capitalAcc;
    /**
     * 本金存入账户名称
     */
    private String capitalAccName;
    /**
     * 利息存入账号ID
     */
    private String interestAccId;
    /**
     * 利息存入账号
     */
    private String interestAcc;
    /**
     * 利息存入账户名称
     */
    private String interestAccName;
    /**
     * 预计利息
     */
    private BigDecimal expectInterest;
    /**
     * 用途
     */
    @BtCompositeSearch(fieldName = "t.purpose")
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    public String getNoticeAccountId() {
        return noticeAccountId;
    }
    public void setNoticeAccountId(String noticeAccountId) {
        this.noticeAccountId = noticeAccountId;
    }
    public String getNoticeAccount() {
        return noticeAccount;
    }
    public void setNoticeAccount(String noticeAccount) {
        this.noticeAccount = noticeAccount;
    }
    public String getNoticeAccountName() {
        return noticeAccountName;
    }
    public void setNoticeAccountName(String noticeAccountName) {
        this.noticeAccountName = noticeAccountName;
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
    public BigDecimal getBookMoney() {
        return bookMoney;
    }
    public void setBookMoney(BigDecimal bookMoney) {
        this.bookMoney = bookMoney;
    }
    public Date getBookDate() {
        return bookDate;
    }
    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
    public String getNoticeTypeName() {
        return noticeTypeName;
    }
    public void setNoticeTypeName(String noticeTypeName) {
        this.noticeTypeName = noticeTypeName;
    }
    public String getCapitalAccId() {
        return capitalAccId;
    }
    public void setCapitalAccId(String capitalAccId) {
        this.capitalAccId = capitalAccId;
    }
    public String getCapitalAcc() {
        return capitalAcc;
    }
    public void setCapitalAcc(String capitalAcc) {
        this.capitalAcc = capitalAcc;
    }
    public String getCapitalAccName() {
        return capitalAccName;
    }
    public void setCapitalAccName(String capitalAccName) {
        this.capitalAccName = capitalAccName;
    }
    public String getInterestAccId() {
        return interestAccId;
    }
    public void setInterestAccId(String interestAccId) {
        this.interestAccId = interestAccId;
    }
    public String getInterestAcc() {
        return interestAcc;
    }
    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }
    public String getInterestAccName() {
        return interestAccName;
    }
    public void setInterestAccName(String interestAccName) {
        this.interestAccName = interestAccName;
    }
    public BigDecimal getExpectInterest() {
        return expectInterest;
    }
    public void setExpectInterest(BigDecimal expectInterest) {
        this.expectInterest = expectInterest;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
