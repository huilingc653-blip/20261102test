package com.bytter.credit.domain.vo;
import java.io.Serializable;
/**
 * 授信调剂页面明细对象
 * 
 * @author liyh
 *
 */
public class RatBkrationReliefDtlPageVO implements Serializable {
    /** reliefId */
    private String reliefId;
    /** 调剂品种标识 */
    private String reliefItemFlag;
    private Integer itemOrder;
    /** 调剂额度品种 */
    private String reliefItemCode;
    private String reliefItemName;
    /** 被调剂品种标识 */
    private String byReliefItemFlag;
    private Integer byItemOrder;
    /** 被调剂额度品种 */
    private String byReliefItemCode;
    private String byReliefItemName;
    /** 调剂最高额度 */
    private java.math.BigDecimal reliefMaxMoney;
    /** 被调剂最高额度 */
    private java.math.BigDecimal byReliefMaxMoney;
    /**
     * 附加调剂额度
     */
    private java.math.BigDecimal reliefMoney;
    public String getReliefId() {
        return reliefId;
    }
    public void setReliefId(String reliefId) {
        this.reliefId = reliefId;
    }
    public String getReliefItemFlag() {
        return reliefItemFlag;
    }
    public void setReliefItemFlag(String reliefItemFlag) {
        this.reliefItemFlag = reliefItemFlag;
    }
    public String getReliefItemCode() {
        return reliefItemCode;
    }
    public void setReliefItemCode(String reliefItemCode) {
        this.reliefItemCode = reliefItemCode;
    }
    public String getReliefItemName() {
        return reliefItemName;
    }
    public void setReliefItemName(String reliefItemName) {
        this.reliefItemName = reliefItemName;
    }
    public String getByReliefItemFlag() {
        return byReliefItemFlag;
    }
    public void setByReliefItemFlag(String byReliefItemFlag) {
        this.byReliefItemFlag = byReliefItemFlag;
    }
    public String getByReliefItemCode() {
        return byReliefItemCode;
    }
    public void setByReliefItemCode(String byReliefItemCode) {
        this.byReliefItemCode = byReliefItemCode;
    }
    public String getByReliefItemName() {
        return byReliefItemName;
    }
    public void setByReliefItemName(String byReliefItemName) {
        this.byReliefItemName = byReliefItemName;
    }
    public java.math.BigDecimal getReliefMaxMoney() {
        return reliefMaxMoney;
    }
    public void setReliefMaxMoney(java.math.BigDecimal reliefMaxMoney) {
        this.reliefMaxMoney = reliefMaxMoney;
    }
    public java.math.BigDecimal getByReliefMaxMoney() {
        return byReliefMaxMoney;
    }
    public void setByReliefMaxMoney(java.math.BigDecimal byReliefMaxMoney) {
        this.byReliefMaxMoney = byReliefMaxMoney;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public Integer getByItemOrder() {
        return byItemOrder;
    }
    public void setByItemOrder(Integer byItemOrder) {
        this.byItemOrder = byItemOrder;
    }
    public java.math.BigDecimal getReliefMoney() {
        return reliefMoney;
    }
    public void setReliefMoney(java.math.BigDecimal reliefMoney) {
        this.reliefMoney = reliefMoney;
    }
}
