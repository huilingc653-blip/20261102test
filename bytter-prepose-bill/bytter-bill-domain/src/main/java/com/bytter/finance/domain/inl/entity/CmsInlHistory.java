package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_IN_HISTORY", schema = "")
@SuppressWarnings("serial")
public class CmsInlHistory extends IdEntity implements Serializable {
    /**
     * 内部拆借流水号
     */
    private String billNo;
    /**
     * 拆入单位
     */
    private String borrowInCorp;
    /**
     * 拆出单位
     */
    private String borrowOutCorp;
    /**
     * 拆借资金币种
     */
    private String curId;
    /**
     * 总拆借金额
     */
    private BigDecimal money;
    /**
     * 拆借日期
     */
    private Date startDate;
    /**
     * 到期日期
     */
    private Date endDate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 已放款金额
     */
    private BigDecimal sendMoney;
    /**
     * 待放款金额
     */
    private BigDecimal leftSendMoney;
    /**
     * 放款状态
     */
    private Integer sendStatus;
    /**
     * 已还本金金额
     */
    private BigDecimal repayMoney;
    /**
     * 已还利息
     */
    private BigDecimal repayInterest;
    /**
     * 还款状态
     */
    private Integer repayStatus;
    /**
     * 剩余还款本金
     */
    private BigDecimal leftRepayMoeny;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 拆入单位名称
     */
    private String borrowInCorpName;
    /**
     * 拆出单位名称
     */
    private String borrowOutCorpName;
    /**
     * 拆借资金币种名称
     */
    private String curName;

    @Column(name = "BORROW_IN_CORP_NAME")
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }

    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }

    @Column(name = "BORROW_OUT_CORP_NAME")
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }

    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
    }

    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "BORROW_IN_CORP")
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    @Column(name = "BORROW_OUT_CORP")
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "SEND_MONEY")
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    @Column(name = "LEFT_SEND_MONEY")
    public BigDecimal getLeftSendMoney() {
        return leftSendMoney;
    }
    public void setLeftSendMoney(BigDecimal leftSendMoney) {
        this.leftSendMoney = leftSendMoney;
    }
    @Column(name = "SEND_STATUS")
    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "REPAY_INTEREST")
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    @Column(name = "REPAY_STATUS")
    public Integer getRepayStatus() {
        return repayStatus;
    }
    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }
    @Column(name = "LEFT_REPAY_MOENY")
    public BigDecimal getLeftRepayMoeny() {
        return leftRepayMoeny;
    }
    public void setLeftRepayMoeny(BigDecimal leftRepayMoeny) {
        this.leftRepayMoeny = leftRepayMoeny;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "TERM")
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
