package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信调整明细DTO
 * 
 * @author luxy Title: RatBkrationAdjustDtlVO
 */
public class RatBkrationAdjustDtlVO implements Serializable {
    private String id;
    /** 授信调整主键 */
    private String adjustCode;
    /** 综合标识 */
    private String itemFlag;
    /** 授信品种 */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    /** 授信品种名称 */
    private String itemName;
    /** 调整额度 */
    private BigDecimal adjustMoney;
    /** 调整后额度 */
    private BigDecimal money;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAdjustCode() {
        return adjustCode;
    }
    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public BigDecimal getAdjustMoney() {
        return adjustMoney;
    }
    public void setAdjustMoney(BigDecimal adjustMoney) {
        this.adjustMoney = adjustMoney;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
