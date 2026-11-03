package com.bytter.credit.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信占用明细DTO
 * 
 * @author liyh
 *
 */
public class RatUrationApplyDtlDTO implements Serializable {
    private String id;
    /**
     * 占用类型
     */
    private String applyType;
    /**
     * 占用主键
     */
    private String applyCode;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 系统品种
     */
    private String sysItemCode;
    /**
     * 品种
     */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    private String itemName;
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /**
     * 占用编号
     */
    private Integer itemOrder;
    /**
     * 占用额度
     */
    private BigDecimal maxMoney;
    /**
     * 剩余最高额度
     */
    private BigDecimal leftMaxMoney;
    /**
     * 实际占用额度
     */
    private BigDecimal applyMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getApplyType() {
        return applyType;
    }
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    public String getApplyCode() {
        return applyCode;
    }
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getSysItemCode() {
        return sysItemCode;
    }
    public void setSysItemCode(String sysItemCode) {
        this.sysItemCode = sysItemCode;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public BigDecimal getLeftMaxMoney() {
        return leftMaxMoney;
    }
    public void setLeftMaxMoney(BigDecimal leftMaxMoney) {
        this.leftMaxMoney = leftMaxMoney;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
