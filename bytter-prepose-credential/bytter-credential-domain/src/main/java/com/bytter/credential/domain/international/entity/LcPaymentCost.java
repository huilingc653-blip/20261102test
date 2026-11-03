package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title 付汇费用纪录表
 * @Description
 * @Author liusy
 * @Date 2018-12-20 15:06
 * @Version 1.0
 */
@Entity
@Table(name = "LC_PAYMENT_COST", schema = "")
public class LcPaymentCost extends IdEntity implements java.io.Serializable{
    /*付汇表主键*/
    private String paymentId;
    /*费用类型*/
    private String feeType;
    /*费用金额*/
    private BigDecimal feeMoney;
    /*创建人*/
    private String createBy;
    /*创建时间*/
    private Date createDate;
    /*修改人*/
    private String updateBy;
    /*修改时间*/
    private Date updateDate;
    @Column(name = "PAYMENT_ID")
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    @Column(name = "FEE_TYPE")
    public String getFeeType() {
        return feeType;
    }
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    @Column(name = "FEE_MONEY")
    public BigDecimal getFeeMoney() {
        return feeMoney;
    }
    public void setFeeMoney(BigDecimal feeMoney) {
        this.feeMoney = feeMoney;
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
}
