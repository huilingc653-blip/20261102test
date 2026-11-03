package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * 授信调整明细实体
 * 
 * @author luxy Title: RatBkrationAdjustDetail
 */
@Entity
@Table(name = "RAT_BKRATION_ADJUST_DETAIL", schema = "")
public class RatBkrationAdjustDetail extends IdEntity implements java.io.Serializable {
    /** 授信调整主键 */
    private String adjustCode;
    /** 综合标识 */
    private String itemFlag;
    /** 授信品种 */
    private String itemCode;
    /** 调整额度 */
    private BigDecimal adjustMoney;
    /** 调整后额度 */
    private BigDecimal money;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信调整主键
     */
    @Column(name = "ADJUST_CODE")
    public String getAdjustCode() {
        return adjustCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 授信调整主键
     */
    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 综合标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 综合标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调整金额
     */
    @Column(name = "ADJUST_MONEY")
    public BigDecimal getAdjustMoney() {
        return adjustMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调整金额
     */
    public void setAdjustMoney(BigDecimal adjustMoney) {
        this.adjustMoney = adjustMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调整后额度
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调整后额度
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
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
