package com.bytter.credential.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-22 15:15
 * @Version 1.0
 */
public class GuaContractInternationalDTO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 开征人
     */
    private String applicant;
    private String applicantName;
    /**
     * 受益人
     */
    private String beneficiary;
    /**
     * 开证银行
     */
    private String issuingBank;
    private String bankName;
    /**
     * 信用证号
     */
    private String creditNo;
    /**
     * 信用证类型
     */
    @Dict(dictGroup = "letter_of_credit_inter", inject = "lcInterName")
    private String lcInter;
    private String lcInterName;
    /**
     * 开证金额
     */
    private BigDecimal creditMoney;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 开证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issuingDate;
    /**
     * 信用证有效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    /**
     * 使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    /**
     * 状态
     */
//    @BtCompositeSearch(fieldName = "gco.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 系统代码
     */
//    @BtCompositeSearch(fieldName = "gco.syscode")
    private String sysCode;
    /**
     * 担保合同ID
     */
//    @BtCompositeSearch(fieldName = "gco.contract_id")
    private String contractId;
    /**
     * 币种
     */
    private String curId;
    private String curName;
    /**
     * 单号
     */
    private String billCode;
    /**
     * 单位
     */
    private String corpId;
    private String corpName;
    public String getApplicant() {
        return applicant;
    }
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getIssuingBank() {
        return issuingBank;
    }
    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public Date getIssuingDate() {
        return issuingDate;
    }
    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getApplicantName() {
        return applicantName;
    }
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
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
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getLcInterName() {
        return lcInterName;
    }
    public void setLcInterName(String lcInterName) {
        this.lcInterName = lcInterName;
    }
}
