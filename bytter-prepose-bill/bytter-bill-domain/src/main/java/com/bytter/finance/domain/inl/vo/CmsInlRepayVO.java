package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsInlRepayVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     *
     */
    private String id;
    /**
     * 内部拆借申请id
     *
     */
    private String inlInfoId;
    /**
     * 拆借金额
     */
    private BigDecimal inlInfoMoney;
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
    @BtCompositeSearch(fieldName = "repay.bill_no")
    private String billNo;
    /**
     * 状态
     *
     */
    @BtCompositeSearch(fieldName = "repay.status")
    private Integer status;
    /**
     * 业务状态
     *
     */
    private Integer bizStatus;
    /**
     * 拆入单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_in_corp")
//    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_out_corp")
//    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 还款金额
     *
     */
    private BigDecimal repayMoney;
    /**
     * 还款币种
     *
     */
    @BtCompositeSearch(fieldName = "send.cur_id")
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 合计金额
     *
     */
    private BigDecimal totalMoney;
    /**
     * 还款日期
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**
     * 拆入方还款账户
     *
     */
    private String borrowInAcc;
    /**
     * I 拆入方还款账户ID
     *
     */
    private String borrowInAccId;
    /**
     * 拆出方收款账户
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 内部拆借申请单号
     */
    @BtCompositeSearch(fieldName = "info.bill_no")
    private String infoBillNo;
    /**
     * 内部拆借发放编号
     */
    @BtCompositeSearch(fieldName = "send.send_code")
    private String sendCode;
    /**
     * 发放单号
     */
    private String sendBillNO;
    /**
     * 拆借期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 放款金额
     */
    private BigDecimal sendMoney;
    /**
     * 资金拆借利率
     */
    private BigDecimal rate;
    /**
     * 拆借利息
     */
    private String interest;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "send.repay_type")
    private String repayType;
    private String repayTypeName;
    /**
     * 已还本金合计
     */
    private BigDecimal repayMoneyTotal;
    /**
     * 已还利息合计
     */
    private BigDecimal repayInterestTotal;
    /**
     * 下一个还款日期
     */
    @BtCompositeSearch(fieldName = "cpi.REPAY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextRepayDate;
    /**
     * 下一个还款金额
     */
    private BigDecimal nextRepayMoney;
    /**
     * 发放日期
     */
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "send.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 还款状态
     */
    @BtCompositeSearch(fieldName = "send.repayStatus")
    private Integer repayStatus;
    /**
     * 拆借资金用途
     */
    private String purpose;
    /**
     * 利息明细id
     */
    private String inlInterestId;
    /** 资金预算 */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getInlInfoId() {
        return inlInfoId;
    }
    public void setInlInfoId(String inlInfoId) {
        this.inlInfoId = inlInfoId;
    }
    public String getInlSendId() {
        return inlSendId;
    }
    public void setInlSendId(String inlSendId) {
        this.inlSendId = inlSendId;
    }
    public String getInlRepayplanId() {
        return inlRepayplanId;
    }
    public void setInlRepayplanId(String inlRepayplanId) {
        this.inlRepayplanId = inlRepayplanId;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getTotalMoney() {
        return totalMoney;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getBorrowInAcc() {
        return borrowInAcc;
    }
    public void setBorrowInAcc(String borrowInAcc) {
        this.borrowInAcc = borrowInAcc;
    }
    public String getBorrowInAccId() {
        return borrowInAccId;
    }
    public void setBorrowInAccId(String borrowInAccId) {
        this.borrowInAccId = borrowInAccId;
    }
    public String getBorrowOutAcc() {
        return borrowOutAcc;
    }
    public void setBorrowOutAcc(String borrowOutAcc) {
        this.borrowOutAcc = borrowOutAcc;
    }
    public String getBorrowOutAccId() {
        return borrowOutAccId;
    }
    public void setBorrowOutAccId(String borrowOutAccId) {
        this.borrowOutAccId = borrowOutAccId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    public Date getRealRepayDate() {
        return realRepayDate;
    }
    public void setRealRepayDate(Date realRepayDate) {
        this.realRepayDate = realRepayDate;
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
    public String getInfoBillNo() {
        return infoBillNo;
    }
    public void setInfoBillNo(String infoBillNo) {
        this.infoBillNo = infoBillNo;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getInterest() {
        return interest;
    }
    public void setInterest(String interest) {
        this.interest = interest;
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

    public BigDecimal getRepayMoneyTotal() {
        return repayMoneyTotal;
    }
    public void setRepayMoneyTotal(BigDecimal repayMoneyTotal) {
        this.repayMoneyTotal = repayMoneyTotal;
    }
    public BigDecimal getRepayInterestTotal() {
        return repayInterestTotal;
    }
    public void setRepayInterestTotal(BigDecimal repayInterestTotal) {
        this.repayInterestTotal = repayInterestTotal;
    }
    public Date getNextRepayDate() {
        return nextRepayDate;
    }
    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }
    public BigDecimal getNextRepayMoney() {
        return nextRepayMoney;
    }
    public void setNextRepayMoney(BigDecimal nextRepayMoney) {
        this.nextRepayMoney = nextRepayMoney;
    }
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }
    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }
    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    public BigDecimal getInlInfoMoney() {
        return inlInfoMoney;
    }
    public void setInlInfoMoney(BigDecimal inlInfoMoney) {
        this.inlInfoMoney = inlInfoMoney;
    }
    public Integer getRepayStatus() {
        return repayStatus;
    }
    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }
    public String getSendBillNO() {
        return sendBillNO;
    }
    public void setSendBillNO(String sendBillNO) {
        this.sendBillNO = sendBillNO;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getInlInterestId() {
        return inlInterestId;
    }
    public void setInlInterestId(String inlInterestId) {
        this.inlInterestId = inlInterestId;
    }
    public String[] getItemCode() {
        return itemCode;
    }
    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }
    public String[] getItemName() {
        return itemName;
    }
    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }
    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }
    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
