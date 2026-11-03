package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信追加明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:00:34
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_BKRATION_ADD_DETAIL", schema = "")
@SuppressWarnings("serial")
public class RatBkrationAddDetail extends IdEntity implements java.io.Serializable {
    /** 授信追加主键 */
    private String addCode;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
    /** 综合标志 */
    private String itemFlag;
    /** 追加金额 */
    private java.math.BigDecimal money;
    /** 追加敞口额度 */
    private java.math.BigDecimal openMoney;
    /** 追加最高额度 */
    private java.math.BigDecimal maxMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信追加主键
     */
    @Column(name = "ADD_CODE")
    public String getAddCode() {
        return this.addCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信追加主键
     */
    public void setAddCode(String addCode) {
        this.addCode = addCode;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 授信品种排序
     */
    @Column(name = "ITEM_ORDER")
    public Integer getItemOrder() {
        return this.itemOrder;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             授信品种排序
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 综合标志
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             综合标志
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加金额
     */
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加金额
     */
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加敞口额度
     */
    @Column(name = "OPEN_MONEY")
    public java.math.BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加敞口额度
     */
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加最高额度
     */
    public void setMaxMoney(java.math.BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加最高额度
     */
    @Column(name = "MAX_MONEY")
    public java.math.BigDecimal getMaxMoney() {
        return this.maxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信额度类型
     */
    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信额度类型
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
