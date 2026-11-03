package com.bytter.credit.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信分配明细DTO
 * 
 * @author liyh
 *
 */
public class RatBkrationDisDtlDTO implements Serializable {
    private String id;
    /** 授信编号 */
    private String disCode;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
    private String itemName;
    /** 品种标识 */
    private String itemFlag;
    /** 授信金额 */
    private BigDecimal money;
    /** 敞口金额 */
    private BigDecimal openMoney;
    /** 最大金额 */
    private BigDecimal maxMoney;
    /** 占用金额 */
    private BigDecimal superProcessMoney;
    /** 占用敞口金额 */
    private BigDecimal superProcessOpenMoney;
    /** 调剂金额 */
    private BigDecimal superReliefMoney;
    /** 调剂敞口金额 */
    private BigDecimal superReliefOpenMoney;
    /** 被调剂金额 */
    private BigDecimal superByReliefMoney;
    /** 被调剂敞口金额 */
    private BigDecimal superByReliefOpenMoney;
    /** 冻结金额 */
    private BigDecimal freezeMoney;
    /** 冻结敞口金额 */
    private BigDecimal freezeOpenMoney;
    /** 虚拟调剂金额 */
    private BigDecimal virtualReliefMoney;
    /** 虚拟敞口调剂金额 */
    private BigDecimal virtualReliefOpenMoney;
    /** 虚拟占用金额 */
    private BigDecimal virtualUration;
    /** 虚拟敞口占用金额 */
    private BigDecimal virtualOpenUration;
    /** 虚拟占用金额 */
    private BigDecimal reliefAndUrationMoney;
    /** 虚拟敞口占用金额 */
    private BigDecimal urationOpenMoney;
    /** 可追减额度 */
    private BigDecimal allDisMoney;
    /** 可追减敞口额度 */
    private BigDecimal allDisOpenMoney;
    /** 可追加额度 */
    private BigDecimal allAddMoney;
    /** 可追加敞口额度 */
    private BigDecimal allAddOpenMoney;
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 利率 */
    private BigDecimal interestrate;
    private String compSign;
    /**
     * 占用最高额度
     */
    private BigDecimal processMaxMoney;
    /**
     * 调剂最高额度
     */
    private BigDecimal reliefMaxMoney;
    /**
     * 被调剂最高额度
     */
    private BigDecimal byReliefMaxMoney;
    /**
     * 冻结最高额度
     */
    private BigDecimal freezeMaxMoney;
    /**
     * 虚拟占用最高额度
     */
    private BigDecimal virtualMaxMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public BigDecimal getSuperProcessMoney() {
        return superProcessMoney;
    }
    public void setSuperProcessMoney(BigDecimal superProcessMoney) {
        this.superProcessMoney = superProcessMoney;
    }
    public BigDecimal getSuperReliefMoney() {
        return superReliefMoney;
    }
    public void setSuperReliefMoney(BigDecimal superReliefMoney) {
        this.superReliefMoney = superReliefMoney;
    }
    public BigDecimal getSuperByReliefMoney() {
        return superByReliefMoney;
    }
    public void setSuperByReliefMoney(BigDecimal superByReliefMoney) {
        this.superByReliefMoney = superByReliefMoney;
    }
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    public BigDecimal getReliefAndUrationMoney() {
        return reliefAndUrationMoney;
    }
    public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
        this.reliefAndUrationMoney = reliefAndUrationMoney;
    }
    public BigDecimal getFreezeOpenMoney() {
        return freezeOpenMoney;
    }
    public void setFreezeOpenMoney(BigDecimal freezeOpenMoney) {
        this.freezeOpenMoney = freezeOpenMoney;
    }
    public BigDecimal getSuperByReliefOpenMoney() {
        return superByReliefOpenMoney;
    }
    public void setSuperByReliefOpenMoney(BigDecimal superByReliefOpenMoney) {
        this.superByReliefOpenMoney = superByReliefOpenMoney;
    }
    public BigDecimal getSuperReliefOpenMoney() {
        return superReliefOpenMoney;
    }
    public void setSuperReliefOpenMoney(BigDecimal superReliefOpenMoney) {
        this.superReliefOpenMoney = superReliefOpenMoney;
    }
    public BigDecimal getSuperProcessOpenMoney() {
        return superProcessOpenMoney;
    }
    public void setSuperProcessOpenMoney(BigDecimal superProcessOpenMoney) {
        this.superProcessOpenMoney = superProcessOpenMoney;
    }
    public BigDecimal getVirtualReliefOpenMoney() {
        return virtualReliefOpenMoney;
    }
    public void setVirtualReliefOpenMoney(BigDecimal virtualReliefOpenMoney) {
        this.virtualReliefOpenMoney = virtualReliefOpenMoney;
    }
    public BigDecimal getVirtualReliefMoney() {
        return virtualReliefMoney;
    }
    public void setVirtualReliefMoney(BigDecimal virtualReliefMoney) {
        this.virtualReliefMoney = virtualReliefMoney;
    }
    public BigDecimal getVirtualUration() {
        return virtualUration;
    }
    public void setVirtualUration(BigDecimal virtualUration) {
        this.virtualUration = virtualUration;
    }
    public BigDecimal getVirtualOpenUration() {
        return virtualOpenUration;
    }
    public void setVirtualOpenUration(BigDecimal virtualOpenUration) {
        this.virtualOpenUration = virtualOpenUration;
    }
    public BigDecimal getUrationOpenMoney() {
        return urationOpenMoney;
    }
    public void setUrationOpenMoney(BigDecimal urationOpenMoney) {
        this.urationOpenMoney = urationOpenMoney;
    }
    public BigDecimal getAllDisMoney() {
        return allDisMoney;
    }
    public void setAllDisMoney(BigDecimal allDisMoney) {
        this.allDisMoney = allDisMoney;
    }
    public BigDecimal getAllDisOpenMoney() {
        return allDisOpenMoney;
    }
    public void setAllDisOpenMoney(BigDecimal allDisOpenMoney) {
        this.allDisOpenMoney = allDisOpenMoney;
    }
    public BigDecimal getAllAddMoney() {
        return allAddMoney;
    }
    public void setAllAddMoney(BigDecimal allAddMoney) {
        this.allAddMoney = allAddMoney;
    }
    public BigDecimal getAllAddOpenMoney() {
        return allAddOpenMoney;
    }
    public void setAllAddOpenMoney(BigDecimal allAddOpenMoney) {
        this.allAddOpenMoney = allAddOpenMoney;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public BigDecimal getInterestrate() {
        return interestrate;
    }
    public void setInterestrate(BigDecimal interestrate) {
        this.interestrate = interestrate;
    }
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public BigDecimal getProcessMaxMoney() {
        return processMaxMoney;
    }
    public void setProcessMaxMoney(BigDecimal processMaxMoney) {
        this.processMaxMoney = processMaxMoney;
    }
    public BigDecimal getReliefMaxMoney() {
        return reliefMaxMoney;
    }
    public void setReliefMaxMoney(BigDecimal reliefMaxMoney) {
        this.reliefMaxMoney = reliefMaxMoney;
    }
    public BigDecimal getByReliefMaxMoney() {
        return byReliefMaxMoney;
    }
    public void setByReliefMaxMoney(BigDecimal byReliefMaxMoney) {
        this.byReliefMaxMoney = byReliefMaxMoney;
    }
    public BigDecimal getFreezeMaxMoney() {
        return freezeMaxMoney;
    }
    public void setFreezeMaxMoney(BigDecimal freezeMaxMoney) {
        this.freezeMaxMoney = freezeMaxMoney;
    }
    public BigDecimal getVirtualMaxMoney() {
        return virtualMaxMoney;
    }
    public void setVirtualMaxMoney(BigDecimal virtualMaxMoney) {
        this.virtualMaxMoney = virtualMaxMoney;
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
