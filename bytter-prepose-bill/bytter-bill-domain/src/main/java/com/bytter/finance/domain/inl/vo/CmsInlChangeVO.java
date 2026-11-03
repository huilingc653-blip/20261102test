package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class CmsInlChangeVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     */
    private String id;
    /**
     * 申请信息表
     */
    private String inlInfoId;
    /**
     * 发放ID
     */
    private String inlSendId;
    /**
     * 拆入单位
     */
    private String corpId;
    /**
     * 还款开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayStartDate;
    /**
     * 更改还款方式
     */
    @BtCompositeSearch(fieldName = "send.repay_type")
    @Dict(dictGroup = "cms.LoanRepayType",inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    //新还款方式
    private String repayTypeName1;
    /**
     * 还款周期
     */
    private String repayCycel;

    private String repayCycelName;
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String rmk;
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
     * 用途
     */
    private String infoPurpose;
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
    @BtCompositeSearch(fieldName = "send.cur_id")
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 拆借金额
     */
    private BigDecimal sendMoney;
    /**
     * 已还本金合计
     */
    private BigDecimal repayMoneyTotal;
    /**
     * 已还利息合计
     */
    private String repayInterestTotal;
    /**
     * 拆借期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 展期到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredDate;
    /**
     * 还款计划
     */
    private List<RepayPlanDTO> repayMents;

    public String getRepayCycelName() {
        return repayCycelName;
    }

    public void setRepayCycelName(String repayCycelName) {
        this.repayCycelName = repayCycelName;
    }

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
    public Date getRepayStartDate() {
        return repayStartDate;
    }
    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
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

    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
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
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
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
    public BigDecimal getRepayMoneyTotal() {
        return repayMoneyTotal;
    }
    public void setRepayMoneyTotal(BigDecimal repayMoneyTotal) {
        this.repayMoneyTotal = repayMoneyTotal;
    }
    public String getRepayInterestTotal() {
        return repayInterestTotal;
    }
    public void setRepayInterestTotal(String repayInterestTotal) {
        this.repayInterestTotal = repayInterestTotal;
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
    public Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    public String getCorpId() {
        return corpId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getInfoPurpose() {
        return infoPurpose;
    }
    public void setInfoPurpose(String infoPurpose) {
        this.infoPurpose = infoPurpose;
    }
    public List<RepayPlanDTO> getRepayMents() {
        return repayMents;
    }
    public void setRepayMents(List<RepayPlanDTO> repayMents) {
        this.repayMents = repayMents;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
