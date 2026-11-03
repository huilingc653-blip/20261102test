package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsInlDeferredVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     */
    private String id;
    /**
     * 内部拆借申请id
     */
    private String inlInfoId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 拆入单位
     */
    private String corpId;
    /**
     * 流水号
     */
    private String billNo;
    /**
     * 展期次数
     */
    private Integer deferredCount;
    /**
     * 上次展期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastDeferredDate;
    /**
     * 当前展期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nowDeferredDate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 内部拆借单据号
     */
    @BtCompositeSearch(fieldName = "info.bill_no")
    private String infoBillNo;
    /**
     * 拆借金额
     */
    private BigDecimal infoMoney;
    /**
     * 拆借利率
     */
    private BigDecimal infoRate;
    /**
     * 拆借利息
     */
    private BigDecimal infoInterest;
    /**
     * 用途
     */
    private String infoPurpose;
    /**
     * 发放表id
     */
    private String inlSendId;
    /**
     * 流水号
     */
    private String sendBillNo;
    /**
     * 发放编号
     */
    @BtCompositeSearch(fieldName = "send.send_code")
    private String sendCode;
    /**
     * 拆入单位
     */
//    @Corp(inject = "borrowInCorpName")
    @BtCompositeSearch(fieldName = "send.borrow_in_corp")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
//    @Corp(inject = "borrowOutCorpName")
    @BtCompositeSearch(fieldName = "send.borrow_out_corp")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 拆借日期
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
     * 拆借资金币种
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "send.cur_id")
    private String curId;
    private String curName;
    /**
     * 拆借金额
     */
    private BigDecimal sendMoney;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Integer getDeferredCount() {
        return deferredCount;
    }
    public void setDeferredCount(Integer deferredCount) {
        this.deferredCount = deferredCount;
    }
    public Date getLastDeferredDate() {
        return lastDeferredDate;
    }
    public void setLastDeferredDate(Date lastDeferredDate) {
        this.lastDeferredDate = lastDeferredDate;
    }
    public Date getNowDeferredDate() {
        return nowDeferredDate;
    }
    public void setNowDeferredDate(Date nowDeferredDate) {
        this.nowDeferredDate = nowDeferredDate;
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
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
    public String getSendBillNo() {
        return sendBillNo;
    }
    public void setSendBillNo(String sendBillNo) {
        this.sendBillNo = sendBillNo;
    }
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }
    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }
    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
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
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
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
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public BigDecimal getRepayInterestTotal() {
        return repayInterestTotal;
    }
    public void setRepayInterestTotal(BigDecimal repayInterestTotal) {
        this.repayInterestTotal = repayInterestTotal;
    }
    public String getInlSendId() {
        return inlSendId;
    }
    public void setInlSendId(String inlSendId) {
        this.inlSendId = inlSendId;
    }
    public BigDecimal getInfoMoney() {
        return infoMoney;
    }
    public void setInfoMoney(BigDecimal infoMoney) {
        this.infoMoney = infoMoney;
    }
    public BigDecimal getInfoRate() {
        return infoRate;
    }
    public void setInfoRate(BigDecimal infoRate) {
        this.infoRate = infoRate;
    }
    public BigDecimal getInfoInterest() {
        return infoInterest;
    }
    public void setInfoInterest(BigDecimal infoInterest) {
        this.infoInterest = infoInterest;
    }
    public String getInfoPurpose() {
        return infoPurpose;
    }
    public void setInfoPurpose(String infoPurpose) {
        this.infoPurpose = infoPurpose;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
