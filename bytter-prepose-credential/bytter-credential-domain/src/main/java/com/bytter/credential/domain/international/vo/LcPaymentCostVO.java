package com.bytter.credential.domain.international.vo;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title 付汇费用纪录表
 * @Description
 * @Author liusy
 * @Date 2018-12-20 15:06
 * @Version 1.0
 */
public class LcPaymentCostVO implements java.io.Serializable, IBusinessKey {
    private String id;
    /*付汇表主键*/
    private String paymentId;
    /*费用类型*/
    private String feeType;
    /*费用金额*/
    private BigDecimal feeMoney;
    /*创建人*/
    private String createBy;
    /*创建时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /*修改人*/
    private String updateBy;
    /*修改时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getFeeType() {
        return feeType;
    }
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    public BigDecimal getFeeMoney() {
        return feeMoney;
    }
    public void setFeeMoney(BigDecimal feeMoney) {
        this.feeMoney = feeMoney;
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
    @Override
    public String getBusinessKey() {
        return id;
    }
}
