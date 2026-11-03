package com.bytter.biz.budget.spi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class FbsBudgetProjectDetailDTO implements java.io.Serializable {
    /**
     * 计划执行ID
     */
    private String id;
    /**
     * 业务单号
     */
    private String billNo;
    /**
     * 业务单号ID
     */
    private String billId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 科目编码
     */
    private String itemCode;
    /**
     * 默认科目编码
     */
    private String defItemCode;
    /**
     * 执行日期
     */
    private Date processDate;
    /**
     * 执行业务名称
     */
    private String businessName;
    /**
     * 执行系统名称
     */
    private String processSysname;
    /**
     * 币别ID
     */
    private java.lang.String curId;

    private String curName;
    /**
     * 结算方式  0-现款 1-票据
     */
    private java.lang.String settlType;
    /**
     * 部门
     */
    private java.lang.String corpName;
    /**
     * 用途
     */
    private java.lang.String purpose;
    /**
     * 期望支付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date wishPayday;
    private String wishPaydayStr;
    /**
     * 付款账号
     */
    private java.lang.String payerAcc;
    /**
     * 收款人
     */
    private java.lang.String payeeAccName;
    /**
     * 收款账号
     */
    private java.lang.String payeeAcc;
    /**
     * 收款开户行
     */
    private java.lang.String payeeBank;
    /**
     * 合同号
     */
    private java.lang.String contract;
    /**
     * 计划执行科目余额控制方式   0-受本科目控制 1-受大科目控制 2-受上级科目控制
     */
    private java.lang.String fbsItemBalance;
    /**
     * 单位是否控制
     */
    private Boolean controlBudget;
    /**
     * 是否根据明细控制
     */
    private Boolean detailControl;
    /**
     * 明细控制字段
     */
    private String dtlColIds;
    /**
     * 资金流向
     */
    private Integer sign;
    /**
     * 是否支持多选科目 0-否 1-是（默认）
     */
    private String isMultiItem;

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

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
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getDefItemCode() {
        return defItemCode;
    }
    public void setDefItemCode(String defItemCode) {
        this.defItemCode = defItemCode;
    }
    public Date getProcessDate() {
        return processDate;
    }
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getProcessSysname() {
        return processSysname;
    }
    public void setProcessSysname(String processSysname) {
        this.processSysname = processSysname;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getSettlType() {
        return settlType;
    }
    public void setSettlType(String settlType) {
        this.settlType = settlType;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public Date getWishPayday() {
        return wishPayday;
    }
    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }
    public String getWishPaydayStr() {
        return wishPaydayStr;
    }
    public void setWishPaydayStr(String wishPaydayStr) {
        this.wishPaydayStr = wishPaydayStr;
    }
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getPayeeBank() {
        return payeeBank;
    }
    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }
    public String getContract() {
        return contract;
    }
    public void setContract(String contract) {
        this.contract = contract;
    }
    public String getFbsItemBalance() {
        return fbsItemBalance;
    }
    public void setFbsItemBalance(String fbsItemBalance) {
        this.fbsItemBalance = fbsItemBalance;
    }
    public Boolean getControlBudget() {
        return controlBudget;
    }
    public void setControlBudget(Boolean controlBudget) {
        this.controlBudget = controlBudget;
    }
    public Boolean getDetailControl() {
        return detailControl;
    }
    public void setDetailControl(Boolean detailControl) {
        this.detailControl = detailControl;
    }
    public String getDtlColIds() {
        return dtlColIds;
    }
    public void setDtlColIds(String dtlColIds) {
        this.dtlColIds = dtlColIds;
    }
    public Integer getSign() {
        return sign;
    }
    public void setSign(Integer sign) {
        this.sign = sign;
    }
    public String getIsMultiItem() {
        return isMultiItem;
    }
    public void setIsMultiItem(String isMultiItem) {
        this.isMultiItem = isMultiItem;
    }
}
