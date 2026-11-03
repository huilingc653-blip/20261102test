package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信占用明细表
 * @author onlineGenerator
 * @date 2017-08-22 17:45:20
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_URATION_APPLY_DTL", schema = "")
@SuppressWarnings("serial")
public class RatUrationApplyDtl extends IdEntity implements java.io.Serializable {
    /** 占用类型 */
    private String applyType;
    /** 占用表ID */
    private String applyCode;
    /** 占用品种标识 */
    private String itemFlag;
    /** 占用品种排序 */
    private Integer itemOrder;
    /** 占用授信品种 */
    private String itemCode;
    /** 占用最高额度 */
    private java.math.BigDecimal maxMoney;
    /** 实际占用最高额度 */
    private java.math.BigDecimal applyMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用类型
     */
    @Column(name = "APPLY_TYPE")
    public String getApplyType() {
        return this.applyType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用类型
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用表ID
     */
    @Column(name = "APPLY_CODE")
    public String getApplyCode() {
        return this.applyCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用表ID
     */
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 占用品种排序
     */
    @Column(name = "ITEM_ORDER")
    public Integer getItemOrder() {
        return this.itemOrder;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             占用品种排序
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 占用最高额度
     */
    @Column(name = "MAX_MONEY")
    public java.math.BigDecimal getMaxMoney() {
        return this.maxMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             占用最高额度
     */
    public void setMaxMoney(java.math.BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 实际占用额度
     */
    @Column(name = "APPLY_MONEY")
    public java.math.BigDecimal getApplyMoney() {
        return this.applyMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             实际占用额度
     */
    public void setApplyMoney(java.math.BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信额度类型
     */
    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 授信额度类型
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
