package com.bytter.credit.domain.vo;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatDisDetailHisVO
 * @Description:
 * @date 2018年11月27日 下午6:47:58
 */
public class RatDisDetailHisVO {
    private String id;
    /** 授信主键 */
    private String ratCode;
    /** 授信分配id */
    private String disCode;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
    /** 授信品种名称 */
    private String itemName;
    /** 品种类型 */
    private String itemFlag;
    /** 分配金额 */
    private java.math.BigDecimal money;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
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
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
}
