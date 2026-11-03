package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author chenf
 * @version 1.0
 * @Title: Entity
 * @Description: 限额管理对应实体
 * @date 2022-05-05 15:43:05
 */
@Entity
@Table(name = "TP_QUOTA", schema = "")
public class TpQuota extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**单位id*/
    private String corpId;
    /**商户号id*/
    private String merchantId;
    /**商户号*/
    private String merchantNo;
    /**支付方式*/
    private String payWay;
    /**备注*/
    private String rmk;
    /**有效标志*/
    private String validSign;
    /**生效日期*/
    private Date effectDate;
    private java.math.BigDecimal daySingle;
    private java.math.BigDecimal dayTotal;
    private java.math.BigDecimal monthTotal;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    @Column(name = "corp_Id")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "merchant_id")
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    @Column(name = "merchant_No")
    public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    @Column(name = "rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "valid_Sign")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "PAY_WAY")
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    @Column(name = "effect_Date")
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    @Column(name = "day_Single")
    public BigDecimal getDaySingle() {
        return daySingle;
    }
    public void setDaySingle(BigDecimal daySingle) {
        this.daySingle = daySingle;
    }
    @Column(name = "day_Total")
    public BigDecimal getDayTotal() {
        return dayTotal;
    }
    public void setDayTotal(BigDecimal dayTotal) {
        this.dayTotal = dayTotal;
    }
    @Column(name = "month_Total")
    public BigDecimal getMonthTotal() {
        return monthTotal;
    }
    public void setMonthTotal(BigDecimal monthTotal) {
        this.monthTotal = monthTotal;
    }
    @Column(name = "create_By")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "create_Date")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "update_By")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "update_Date")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
