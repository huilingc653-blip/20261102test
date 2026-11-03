package com.bytter.credential.api.dto;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title Entity
 * @Description 信用证到单实体
 * @Author liusy
 * @Date 2018-11-28 19:15
 * @Version 1.0
 */
@Entity
@Table(name = "LC_BILL_ARRIVAL")
public class LcBillArrivalDTO extends IdEntity implements Serializable {
    /**
     * 到单流水号
     */
    private String arrivalCode;
    /**
     * 到单类型
     */
    private Integer arrivalType;
    /**
     * 到单号
     */
    private String arrivalNo;
    /**
     * 信用证登记ID
     */
    private String lcId;
    /**
     * 单位编号
     */
    private String corpId;
    /**
     * 本次来单金额
     */
    private BigDecimal billMoney;
    /**
     * 本次付款/付汇金额
     */
    private BigDecimal payMoney;
    /**
     * 币别
     */
    private String curId;
    /**
     * 汇率
     */
    private BigDecimal exchange;
    /**
     * 手续费
     */
    private BigDecimal handlingCharge;
    /**
     * 到单日期
     */
    private Date paymentDate;
    /**
     * 期限
     */
    private Integer period;
    /**
     * 交单银行
     */
    private String handDocBank;
    /**
     * 交单银行编号
     */
    private String docBankNo;
    /**
     * 付汇日期
     */
    private Date payDate;
    /**
     * 增值税款金额
     */
    private BigDecimal addTaxMoney;
    /*关税款金额*/
    private BigDecimal taxMoney;
    /*缴税日期*/
    private Date taxDate;
    /*税款种类*/
    private String taxType;
    /*核销单号*/
    private String vbCode;
    /*取回日期*/
    private Date getDate;
    /*远期天数*/
    private String delayDates;
    /*受益人*/
    private String beneficiary;
    /*备注*/
    private String rmk;
    /*状态*/
    private Integer status;
    /*创建人登录名称*/
    private String createBy;
    /*创建日期*/
    private Date createDate;
    /*更新人登录名称*/
    private String updateBy;
    /*更新日期*/
    private Date updateDate;
    /*关单号*/
    private String closeLcNo;
    
    /*发票号*/
    private String invoiceNo;
    /**
     * 银行流水认领功能挂接标记，未认领=0,认领=1
     */
    private String bankFlowSign;
    public String getArrivalCode() {
        return arrivalCode;
    }
    public void setArrivalCode(String arrivalCode) {
        this.arrivalCode = arrivalCode;
    }
    public Integer getArrivalType() {
        return arrivalType;
    }
    public void setArrivalType(Integer arrivalType) {
        this.arrivalType = arrivalType;
    }
    public String getArrivalNo() {
        return arrivalNo;
    }
    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getExchange() {
        return exchange;
    }
    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }
    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }
    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public Integer getPeriod() {
        return period;
    }
    public void setPeriod(Integer period) {
        this.period = period;
    }
    public String getHandDocBank() {
        return handDocBank;
    }
    public void setHandDocBank(String handDocBank) {
        this.handDocBank = handDocBank;
    }
    public String getDocBankNo() {
        return docBankNo;
    }
    public void setDocBankNo(String docBankNo) {
        this.docBankNo = docBankNo;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public BigDecimal getAddTaxMoney() {
        return addTaxMoney;
    }
    public void setAddTaxMoney(BigDecimal addTaxMoney) {
        this.addTaxMoney = addTaxMoney;
    }
    public BigDecimal getTaxMoney() {
        return taxMoney;
    }
    public void setTaxMoney(BigDecimal taxMoney) {
        this.taxMoney = taxMoney;
    }
    public Date getTaxDate() {
        return taxDate;
    }
    public void setTaxDate(Date taxDate) {
        this.taxDate = taxDate;
    }
    public String getTaxType() {
        return taxType;
    }
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }
    public String getVbCode() {
        return vbCode;
    }
    public void setVbCode(String vbCode) {
        this.vbCode = vbCode;
    }
    public Date getGetDate() {
        return getDate;
    }
    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    public String getDelayDates() {
        return delayDates;
    }
    public void setDelayDates(String delayDates) {
        this.delayDates = delayDates;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    public String getCloseLcNo() {
        return closeLcNo;
    }
    public void setCloseLcNo(String closeLcNo) {
        this.closeLcNo = closeLcNo;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public String getBankFlowSign() {
        return bankFlowSign;
    }
    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }
    @Override
    public String toString() {
        return "LcBillArrival{" +
                "arrivalCode='" + arrivalCode + '\'' +
                ", arrivalType=" + arrivalType +
                ", arrivalNo='" + arrivalNo + '\'' +
                ", lcId='" + lcId + '\'' +
                ", corpId='" + corpId + '\'' +
                ", billMoney=" + billMoney +
                ", payMoney=" + payMoney +
                ", curId='" + curId + '\'' +
                ", exchange=" + exchange +
                ", handlingCharge=" + handlingCharge +
                ", paymentDate=" + paymentDate +
                ", period=" + period +
                ", handDocBank='" + handDocBank + '\'' +
                ", docBankNo='" + docBankNo + '\'' +
                ", payDate=" + payDate +
                ", addTaxMoney=" + addTaxMoney +
                ", taxMoney=" + taxMoney +
                ", taxDate=" + taxDate +
                ", taxType='" + taxType + '\'' +
                ", vbCode='" + vbCode + '\'' +
                ", getDate=" + getDate +
                ", delayDates='" + delayDates + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", rmk='" + rmk + '\'' +
                ", status=" + status +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                ", closeLcNo='" + closeLcNo + '\'' +
                '}';
    }
}
