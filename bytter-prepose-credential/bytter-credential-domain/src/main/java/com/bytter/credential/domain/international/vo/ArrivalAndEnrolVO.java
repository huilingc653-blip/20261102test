package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-18 18:40
 * @Version 1.0
 */
public class ArrivalAndEnrolVO implements Serializable, IBusinessKey {
    @BtCompositeSearch(fieldName = "id")
    private String id;
    /*信用证类型*/
    @BtCompositeSearch(fieldName = "lc_inter")
    private String lcInter;
    /*信用证单号*/
    @BtCompositeSearch(fieldName = "lc_no")
    private String lcNo;
    /*信用证号*/
    @BtCompositeSearch(fieldName = "credit_no")
    private String creditNo;
    /*合同号*/
    @BtCompositeSearch(fieldName = "contract_no")
    private String contractNo;
    /*开证日期*/
    @BtCompositeSearch(fieldName = "issuing_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issuingDate;
    /*信用证有效期*/
    @BtCompositeSearch(fieldName = "expiry_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    /*开证金额*/
    @BtCompositeSearch(fieldName = "credit_money")
    private BigDecimal creditMoney;
    /*开证币别*/
    @BtCompositeSearch(fieldName = "issuing_cur_id")
    private String issuingCurId;
    /*状态*/
    @BtCompositeSearch(fieldName = "status")
    private Integer status;
    /*受益人*/
    @BtCompositeSearch(fieldName = "beneficiary")
    private String beneficiary;
    /*币别名*/
    @BtCompositeSearch(fieldName = "cur_name")
    private String curName;
    /*开证银行*/
    @BtCompositeSearch(fieldName = "bank_name")
    private String issuingBankName;
    /*已到单金额*/
    private BigDecimal hadbillMoney;
    /*未到单金额*/
    private BigDecimal hadNotbillMoney;
    /*付汇金额*/
    private BigDecimal sumPaymentMoney;
    /**
     * 附件id
     */
    private String attachmentId;
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public String getLcNo() {
        return lcNo;
    }
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
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
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public String getIssuingCurId() {
        return issuingCurId;
    }
    public void setIssuingCurId(String issuingCurId) {
        this.issuingCurId = issuingCurId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getIssuingBankName() {
        return issuingBankName;
    }
    public void setIssuingBankName(String issuingBankName) {
        this.issuingBankName = issuingBankName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getHadbillMoney() {
        return hadbillMoney;
    }
    public void setHadbillMoney(BigDecimal hadbillMoney) {
        this.hadbillMoney = hadbillMoney;
    }
    public BigDecimal getHadNotbillMoney() {
        return hadNotbillMoney;
    }
    public void setHadNotbillMoney(BigDecimal hadNotbillMoney) {
        this.hadNotbillMoney = hadNotbillMoney;
    }
    public BigDecimal getSumPaymentMoney() {
        return sumPaymentMoney;
    }
    public void setSumPaymentMoney(BigDecimal sumPaymentMoney) {
        this.sumPaymentMoney = sumPaymentMoney;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
