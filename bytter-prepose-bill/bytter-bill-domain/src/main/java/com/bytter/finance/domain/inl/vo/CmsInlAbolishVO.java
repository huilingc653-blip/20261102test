package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsInlAbolishVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     */
    private String id;
    /**
     * 内部拆借申请id
     */
    private String inlInfoId;
    /**
     * 内部拆借申请id
     */
    @BtCompositeSearch(fieldName = "info.bill_no")
    private String inlInfoBillNo;
    /**
     * 内部拆借申请id
     */
    @BtCompositeSearch(fieldName = "info.status")
    private Integer inlInfoStatus;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "abolish.status")
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
     * 作废原因
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
     * 拆入单位
     */
    @BtCompositeSearch(fieldName = "info.borrow_in_corp")
//    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @BtCompositeSearch(fieldName = "info.borrow_out_corp")
//    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 拆借日期
     */
    @BtCompositeSearch(fieldName = "info.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "info.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 拆借资金币种
     */
    @BtCompositeSearch(fieldName = "info.cur_id")
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 拆借期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 拆借金额
     */
    private BigDecimal infoMoney;
    /**
     * 资金拆借利率
     */
    private BigDecimal infoRate;
    /**
     * 拆借利息
     */
    private String infoInterest;
    /**
     * 拆借资金用途
     */
    @BtCompositeSearch(fieldName = "info.purpose")
    private String infoPurpose;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "info.repay_type")
    private String repayType;
    private String repayTypeName;
    /**
     * 放款业务 放款状态
     */
    private Integer sendStatus;
    /**
     * 拆借资金用途
     */
    @BtCompositeSearch(fieldName = "info.purpose")
    private String purpose;
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
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
    public String getInfoInterest() {
        return infoInterest;
    }
    public void setInfoInterest(String infoInterest) {
        this.infoInterest = infoInterest;
    }
    public String getInfoPurpose() {
        return infoPurpose;
    }
    public void setInfoPurpose(String infoPurpose) {
        this.infoPurpose = infoPurpose;
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

    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
    public Integer getInlInfoStatus() {
        return inlInfoStatus;
    }
    public void setInlInfoStatus(Integer inlInfoStatus) {
        this.inlInfoStatus = inlInfoStatus;
    }
    public String getInlInfoBillNo() {
        return inlInfoBillNo;
    }
    public void setInlInfoBillNo(String inlInfoBillNo) {
        this.inlInfoBillNo = inlInfoBillNo;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
