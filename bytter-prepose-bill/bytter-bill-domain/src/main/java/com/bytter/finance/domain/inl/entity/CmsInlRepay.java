package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_REPAY", schema = "")
@SuppressWarnings("serial")
public class CmsInlRepay extends IdEntity implements Serializable {
    /**
     * 内部拆借申请id
     *
     */
    private String inlInfoId;
    /**
     * 内部拆借发放id
     *
     */
    private String inlSendId;
    /**
     * 还款计划id
     *
     */
    private String inlRepayplanId;
    /**
     * 内部拆借还款流水号
     */
    private String billNo;
    /**
     * 状态
     *
     */
    private Integer status;
    /**
     * 业务状态
     *
     */
    private Integer bizStatus;
    /**
     * 拆入单位
     *
     */
    private String borrowInCorp;
    /**
     * 拆出单位
     *
     */
    private String borrowOutCorp;
    /**
     * 还款金额
     *
     */
    private BigDecimal repayMoney;
    /**
     * 还款币种
     *
     */
    private String curId;
    /**
     * 合计金额
     *
     */
    private BigDecimal totalMoney;
    /**
     * 还款日期
     *
     */
    private Date repayDate;
    /**
     * 拆入方还款账户
     *
     */
    private String borrowInAcc;
    /**
     * 拆入方还款账户ID
     *
     */
    private String borrowInAccId;
    /**
     * 拆出方还款账户
     *
     */
    private String borrowOutAcc;
    /**
     * 拆出方还款账户ID
     *
     */
    private String borrowOutAccId;
    /**
     * 备注
     *
     */
    private String rmk;
    /**
     * 附件
     *
     */
    private String attachmentId;
    /**
     * 是否挂接资金计划
     *
     */
    private String isBudget;
    /**
     * 实际还款日期
     *
     */
    private Date realRepayDate;
    /**
     * 创建人
     *
     */
    private String createBy;
    /**
     * 创建时间
     *
     */
    private Date createDate;
    /**
     * 修改人
     *
     */
    private String updateBy;
    /**
     * 修改时间
     *
     */
    private Date updateDate;
    /**
     * 还款利息
     */
    private BigDecimal repayInterest;
    /**
     * 利息明细id
     */
    private String inlInterestId;
    @Column(name = "INL_INFO_ID")
    public String getInlInfoId() {
        return inlInfoId;
    }
    public void setInlInfoId(String inlInfoId) {
        this.inlInfoId = inlInfoId;
    }
    @Column(name = "INL_SEND_ID")
    public String getInlSendId() {
        return inlSendId;
    }
    public void setInlSendId(String inlSendId) {
        this.inlSendId = inlSendId;
    }
    @Column(name = "INL_REPAYPLAN_ID")
    public String getInlRepayplanId() {
        return inlRepayplanId;
    }
    public void setInlRepayplanId(String inlRepayplanId) {
        this.inlRepayplanId = inlRepayplanId;
    }
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
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
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "TOTAL_MONEY")
    public BigDecimal getTotalMoney() {
        return totalMoney;
    }
    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "BORROW_IN_ACC")
    public String getBorrowInAcc() {
        return borrowInAcc;
    }
    public void setBorrowInAcc(String borrowInAcc) {
        this.borrowInAcc = borrowInAcc;
    }
    @Column(name = "BORROW_IN_ACCID")
    public String getBorrowInAccId() {
        return borrowInAccId;
    }
    public void setBorrowInAccId(String borrowInAccId) {
        this.borrowInAccId = borrowInAccId;
    }
    @Column(name = "BORROW_OUT_ACC")
    public String getBorrowOutAcc() {
        return borrowOutAcc;
    }
    public void setBorrowOutAcc(String borrowOutAcc) {
        this.borrowOutAcc = borrowOutAcc;
    }
    @Column(name = "BORROW_OUT_ACCID")
    public String getBorrowOutAccId() {
        return borrowOutAccId;
    }
    public void setBorrowOutAccId(String borrowOutAccId) {
        this.borrowOutAccId = borrowOutAccId;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "IS_BUDGET")
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    @Column(name = "REAL_REPAY_DATE")
    public Date getRealRepayDate() {
        return realRepayDate;
    }
    public void setRealRepayDate(Date realRepayDate) {
        this.realRepayDate = realRepayDate;
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
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "REPAY_INTEREST")
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    @Column(name = "INL_INTEREST_ID")
    public String getInlInterestId() {
        return inlInterestId;
    }
    public void setInlInterestId(String inlInterestId) {
        this.inlInterestId = inlInterestId;
    }
}
