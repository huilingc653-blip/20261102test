package com.bytter.credit.domain.entity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信分配明细表
 * @author onlineGenerator
 * @date 2017-07-31 16:07:49
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_BKRATION_DIS_DETAIL", schema = "")
@SuppressWarnings("serial")
public class RatBkrationDisDetail extends IdEntity implements java.io.Serializable {
    /** 授信编号 */
    private String disCode;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
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
    /** 调剂金额 */
    private BigDecimal superReliefMoney;
    /** 被调剂金额 */
    private BigDecimal superByReliefMoney;
    /** 冻结金额 */
    private BigDecimal freezeMoney;
    /** 虚拟占用金额 */
    private BigDecimal reliefAndUrationMoney;
    /** 冻结敞口金额 */
    private BigDecimal freezeOpenMoney;
    /** 被调剂敞口金额 */
    private BigDecimal superByReliefOpenMoney;
    /** 调剂敞口金额 */
    private BigDecimal superReliefOpenMoney;
    /** 已正式占用金额 */
    private BigDecimal superProcessOpenMoney;
    /** 虚拟敞口调剂金额 */
    private BigDecimal virtualReliefOpenMoney;
    /** 虚拟调剂金额 */
    private BigDecimal virtualReliefMoney;
    /** 虚拟占用金额 */
    private BigDecimal virtualUration;
    /** 虚拟敞口占用金额 */
    private BigDecimal virtualOpenUration;
    /** 虚拟敞口占用金额 */
    private BigDecimal urationOpenMoney;
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
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信编号
     */
    @Column(name = "DIS_CODE")
    public String getDisCode() {
        return this.disCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信编号
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
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             授信金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 敞口金额
     */
    @Column(name = "OPEN_MONEY")
    public BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             敞口金额
     */
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 最大金额
     */
    @Column(name = "MAX_MONEY")
    public BigDecimal getMaxMoney() {
        return this.maxMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             最大金额
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 占用金额
     */
    @Column(name = "SUPER_PROCESS_MONEY")
    public BigDecimal getSuperProcessMoney() {
        return this.superProcessMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             占用金额
     */
    public void setSuperProcessMoney(BigDecimal superProcessMoney) {
        this.superProcessMoney = superProcessMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调剂金额
     */
    @Column(name = "SUPER_RELIEF_MONEY")
    public BigDecimal getSuperReliefMoney() {
        return this.superReliefMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             调剂金额
     */
    public void setSuperReliefMoney(BigDecimal superReliefMoney) {
        this.superReliefMoney = superReliefMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 被调剂金额
     */
    @Column(name = "SUPER_BY_RELIEF_MONEY")
    public BigDecimal getSuperByReliefMoney() {
        return this.superByReliefMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             被调剂金额
     */
    public void setSuperByReliefMoney(BigDecimal superByReliefMoney) {
        this.superByReliefMoney = superByReliefMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 冻结金额
     */
    @Column(name = "FREEZE_MONEY")
    public BigDecimal getFreezeMoney() {
        return this.freezeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             冻结金额
     */
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟占用金额
     */
    @Column(name = "RELIEF_AND_URATION_MONEY")
    public BigDecimal getReliefAndUrationMoney() {
        return this.reliefAndUrationMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟占用金额
     */
    public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
        this.reliefAndUrationMoney = reliefAndUrationMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 冻结敞口金额
     */
    @Column(name = "FREEZE_OPEN_MONEY")
    public BigDecimal getFreezeOpenMoney() {
        return this.freezeOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             冻结敞口金额
     */
    public void setFreezeOpenMoney(BigDecimal freezeOpenMoney) {
        this.freezeOpenMoney = freezeOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 被调剂敞口金额
     */
    @Column(name = "SUPER_BY_RELIEF_OPEN_MONEY")
    public BigDecimal getSuperByReliefOpenMoney() {
        return this.superByReliefOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             被调剂敞口金额
     */
    public void setSuperByReliefOpenMoney(BigDecimal superByReliefOpenMoney) {
        this.superByReliefOpenMoney = superByReliefOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 调剂敞口金额
     */
    @Column(name = "SUPER_RELIEF_OPEN_MONEY")
    public BigDecimal getSuperReliefOpenMoney() {
        return this.superReliefOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             调剂敞口金额
     */
    public void setSuperReliefOpenMoney(BigDecimal superReliefOpenMoney) {
        this.superReliefOpenMoney = superReliefOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 已正式占用金额
     */
    @Column(name = "SUPER_PROCESS_OPEN_MONEY")
    public BigDecimal getSuperProcessOpenMoney() {
        return this.superProcessOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             已正式占用金额
     */
    public void setSuperProcessOpenMoney(BigDecimal superProcessOpenMoney) {
        this.superProcessOpenMoney = superProcessOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟敞口调剂金额
     */
    @Column(name = "VIRTUAL_RELIEF_OPEN_MONEY")
    public BigDecimal getVirtualReliefOpenMoney() {
        return this.virtualReliefOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟敞口调剂金额
     */
    public void setVirtualReliefOpenMoney(BigDecimal virtualReliefOpenMoney) {
        this.virtualReliefOpenMoney = virtualReliefOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟调剂金额
     */
    @Column(name = "VIRTUAL_RELIEF_MONEY")
    public BigDecimal getVirtualReliefMoney() {
        return this.virtualReliefMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟调剂金额
     */
    public void setVirtualReliefMoney(BigDecimal virtualReliefMoney) {
        this.virtualReliefMoney = virtualReliefMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟占用金额
     */
    @Column(name = "VIRTUAL_URATION")
    public BigDecimal getVirtualUration() {
        return this.virtualUration;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟占用金额
     */
    public void setVirtualUration(BigDecimal virtualUration) {
        this.virtualUration = virtualUration;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟敞口占用金额
     */
    @Column(name = "VIRTUAL_OPEN_URATION")
    public BigDecimal getVirtualOpenUration() {
        return this.virtualOpenUration;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟占用金额
     */
    public void setVirtualOpenUration(BigDecimal virtualOpenUration) {
        this.virtualOpenUration = virtualOpenUration;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 虚拟敞口占用金额
     */
    @Column(name = "URATION_OPEN_MONEY")
    public BigDecimal getUrationOpenMoney() {
        return this.urationOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             虚拟敞口占用金额
     */
    public void setUrationOpenMoney(BigDecimal urationOpenMoney) {
        this.urationOpenMoney = urationOpenMoney;
    }
    @Column(name = "PROCESS_MAX_MONEY")
    public BigDecimal getProcessMaxMoney() {
        return processMaxMoney;
    }
    public void setProcessMaxMoney(BigDecimal processMaxMoney) {
        this.processMaxMoney = processMaxMoney;
    }
    @Column(name = "RELIEF_MAX_MONEY")
    public BigDecimal getReliefMaxMoney() {
        return reliefMaxMoney;
    }
    public void setReliefMaxMoney(BigDecimal reliefMaxMoney) {
        this.reliefMaxMoney = reliefMaxMoney;
    }
    @Column(name = "BY_RELIEF_MAX_MONEY")
    public BigDecimal getByReliefMaxMoney() {
        return byReliefMaxMoney;
    }
    public void setByReliefMaxMoney(BigDecimal byReliefMaxMoney) {
        this.byReliefMaxMoney = byReliefMaxMoney;
    }
    @Column(name = "FREEZE_MAX_MONEY")
    public BigDecimal getFreezeMaxMoney() {
        return freezeMaxMoney;
    }
    public void setFreezeMaxMoney(BigDecimal freezeMaxMoney) {
        this.freezeMaxMoney = freezeMaxMoney;
    }
    @Column(name = "VIRTUAL_MAX_MONEY")
    public BigDecimal getVirtualMaxMoney() {
        return virtualMaxMoney;
    }
    public void setVirtualMaxMoney(BigDecimal virtualMaxMoney) {
        this.virtualMaxMoney = virtualMaxMoney;
    }
    /**
     * 方法: 取得String
     * 
     * @param: String
     *             综合额度使用标识
     */
    @Column(name = "COMP_SIGN")
    public String getCompSign() {
        return compSign;
    }
    /**
     * 方法: 设置comSign
     * 
     * @return: String 综合额度使用标识
     */
    public void setCompSign(String compSign) {
        this.compSign = compSign;
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
