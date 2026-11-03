package com.bytter.credit.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信占用明细DTO
 * 
 * @author liyh
 *
 */
public class RatUrationDtlDTO implements Serializable {
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /**
     * 品种
     */
    private String itemCode;
    /**
     * 品种名称
     */
    private String itemName;
    /**
     * 占用额度
     */
    private BigDecimal itemMoney;
    /**
     * 本次授信占用的最高额度
     */
    private BigDecimal useMaxMoney;
    /** 授信方式 */
    private String creditType;
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public BigDecimal getItemMoney() {
        return itemMoney;
    }
    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }
    public BigDecimal getUseMaxMoney() {
        return useMaxMoney;
    }
    public void setUseMaxMoney(BigDecimal useMaxMoney) {
        this.useMaxMoney = useMaxMoney;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
