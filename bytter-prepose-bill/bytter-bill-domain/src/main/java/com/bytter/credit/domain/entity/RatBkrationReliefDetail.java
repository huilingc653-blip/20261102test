package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信调剂明细表
 * @author onlineGenerator
 * @date 2017-08-13 16:33:55
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_BKRATION_RELIEF_DETAIL", schema = "")
@SuppressWarnings("serial")
public class RatBkrationReliefDetail extends IdEntity implements java.io.Serializable {
    /**
     * 调剂主表ID
     */
    private String refCode;
    /** 授信调剂类型 **/
    private String refType;
    /** 调剂授信编号 */
    private String reliefCode;
    /** 调剂品种标识 */
    private String itemFlag;
    /** 调剂品种顺序 */
    private Integer itemOrder;
    /** 调剂额度品种 */
    private String reliefItemCode;
    /** 调剂最高额度 */
    private java.math.BigDecimal reliefMaxMoney;
    /** 实际调剂最高额度 */
    private java.math.BigDecimal reliefMoney;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂主表编号
     */
    @Column(name = "REF_CODE")
    public String getRefCode() {
        return this.refCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂主表编号
     */
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂主表编号
     */
    @Column(name = "REF_TYPE")
    public String getRefType() {
        return this.refType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂主表编号
     */
    public void setRefType(String refType) {
        this.refType = refType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂授信编号
     */
    @Column(name = "RELIEF_CODE")
    public String getReliefCode() {
        return this.reliefCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂授信编号
     */
    public void setReliefCode(String reliefCode) {
        this.reliefCode = reliefCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂品种顺序
     */
    @Column(name = "ITEM_ORDER")
    public Integer getItemOrder() {
        return this.itemOrder;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂品种顺序
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调剂额度品种
     */
    @Column(name = "RELIEF_ITEM_CODE")
    public String getReliefItemCode() {
        return this.reliefItemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             调剂额度品种
     */
    public void setReliefItemCode(String reliefItemCode) {
        this.reliefItemCode = reliefItemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调剂最高额度
     */
    @Column(name = "RELIEF_MAX_MONEY")
    public java.math.BigDecimal getReliefMaxMoney() {
        return this.reliefMaxMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             调剂最高额度
     */
    public void setReliefMaxMoney(java.math.BigDecimal reliefMaxMoney) {
        this.reliefMaxMoney = reliefMaxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 实际调剂最高额度
     */
    @Column(name = "RELIEF_MONEY")
    public java.math.BigDecimal getReliefMoney() {
        return this.reliefMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             实际调剂最高额度
     */
    public void setReliefMoney(java.math.BigDecimal reliefMoney) {
        this.reliefMoney = reliefMoney;
    }
}
