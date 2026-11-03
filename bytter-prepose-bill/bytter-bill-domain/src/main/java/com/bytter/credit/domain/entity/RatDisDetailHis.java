package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatDisDetailHis
 * @Description:授信分配历史明细表
 * @date 2018年11月26日 下午6:24:42
 */
@Entity
@Table(name = "RAT_RATION_DETAIL_DISHIS")
@SuppressWarnings("serial")
public class RatDisDetailHis extends IdEntity implements java.io.Serializable {
    /** 授信分配id */
    private String disCode;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
    /** 品种类型 */
    private String itemFlag;
    /** 分配金额 */
    private java.math.BigDecimal money;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 分配授信id
     */
    @Column(name = "DIS_CODE")
    public String getDisCode() {
        return this.disCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             分配授信id
     */
    public void setDisCode(String disCode) {
        this.disCode = disCode;
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
     * @return: java.lang.String 品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信金额
     */
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             授信金额
     */
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
}
