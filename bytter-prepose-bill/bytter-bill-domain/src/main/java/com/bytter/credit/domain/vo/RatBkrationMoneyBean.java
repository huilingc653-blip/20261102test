package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
public class RatBkrationMoneyBean implements Serializable {
    // 授信编号
    private String ratCode;
    // 单位
    private String corpId;
    // 授信品种标识
    private String itemFlag;
    // 授信品种
    private String itemCode;
    // 授信品种名称
    private String itemName;
    // 授信品种顺序
    private Integer itemOrder;
    // 总额度
    private BigDecimal money;
    private BigDecimal openMoney;
    // 已分配额度
    private BigDecimal disMoney;
    private BigDecimal disOpenMoney;
    private BigDecimal disMaxMoney;
    // 剩余额度
    private BigDecimal leftMoney;
    private BigDecimal leftOpenMoney;
    // 总品种的未使用额度
    private BigDecimal unUseMoney;
    private BigDecimal unUseOpenMoney;
    private BigDecimal unUseMaxMoney;
    // 最高额度和剩余最高额度
    private BigDecimal maxMoney;
    private BigDecimal leftMaxMoney;
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
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
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getDisMoney() {
        return disMoney;
    }
    public void setDisMoney(BigDecimal disMoney) {
        this.disMoney = disMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public BigDecimal getDisOpenMoney() {
        return disOpenMoney;
    }
    public void setDisOpenMoney(BigDecimal disOpenMoney) {
        this.disOpenMoney = disOpenMoney;
    }
    public BigDecimal getDisMaxMoney() {
        return disMaxMoney;
    }
    public void setDisMaxMoney(BigDecimal disMaxMoney) {
        this.disMaxMoney = disMaxMoney;
    }
    public BigDecimal getLeftOpenMoney() {
        return leftOpenMoney;
    }
    public void setLeftOpenMoney(BigDecimal leftOpenMoney) {
        this.leftOpenMoney = leftOpenMoney;
    }
    public BigDecimal getUnUseMoney() {
        return unUseMoney;
    }
    public void setUnUseMoney(BigDecimal unUseMoney) {
        this.unUseMoney = unUseMoney;
    }
    public BigDecimal getUnUseOpenMoney() {
        return unUseOpenMoney;
    }
    public void setUnUseOpenMoney(BigDecimal unUseOpenMoney) {
        this.unUseOpenMoney = unUseOpenMoney;
    }
    public BigDecimal getUnUseMaxMoney() {
        return unUseMaxMoney;
    }
    public void setUnUseMaxMoney(BigDecimal unUseMaxMoney) {
        this.unUseMaxMoney = unUseMaxMoney;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public BigDecimal getLeftMaxMoney() {
        return leftMaxMoney;
    }
    public void setLeftMaxMoney(BigDecimal leftMaxMoney) {
        this.leftMaxMoney = leftMaxMoney;
    }
}
