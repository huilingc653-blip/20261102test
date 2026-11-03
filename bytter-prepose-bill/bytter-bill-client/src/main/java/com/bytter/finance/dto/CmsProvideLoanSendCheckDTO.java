package com.bytter.finance.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 信贷银行借款检查Bean
 * 
 * @author liyh
 *
 */
public class CmsProvideLoanSendCheckDTO implements Serializable {
    /**
     * 单号
     */
    private String billCode;
    /**
     * 借款单位
     */
    private String corpName;
    /**
     * 借款单位
     */
    private String corpCode;
    /** 网点 **/
    private String netId;
    private String corpId;
    /**
     * 借款账号
     */
    private String accCode;
    /**
     * 借款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enterDate;
    /**
     * 币别
     */
    private String curName;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 发放日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**
     * 还款方式
     */
    private String repayType;
    private String repayTypeName;
    /**
     * 借款方式
     */
    private String plClass;
    /**
     * 单号
     */
    private String plType;
    /**
     * 期限
     */
    private String plTerm;
    /**
     * 期限单位
     */
    private String ternUnit;
    /**
     * 状态
     */
    private Integer status;
    /** 账务表ID */
    private String bookId;
    /** 记账流水号 **/
    private String orderNo;
    /** 借款账号 **/
    private String loanAcc;
    /** 币别 **/
    private String curCode;
    /** 银行活期账户 */
    private String blnctrAcc;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getAccCode() {
        return accCode;
    }
    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }
    public Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(String plTerm) {
        this.plTerm = plTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
