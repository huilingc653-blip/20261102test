package com.bytter.credit.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 授信额度统计DTO
 * 
 * @author liyh
 *
 */
public class RatUrationDisDtlDTO implements Serializable {
    /**
     * 授信品种类型
     */
    private String itemFlag;
    /**
     * 品种
     */
    private String itemCode;
    private Integer itemOrder;
    /**
     * 品种名称
     */
    private String itemName;
    /** 授信总额度 **/
    private BigDecimal money;
    /** 占用额度 **/
    private BigDecimal useMoney;
    /** 在审占用额度 **/
    private BigDecimal approveMoney;
    /** 剩余额度 **/
    private BigDecimal leftMoney;
    /** 可用额度 **/
    private BigDecimal overPlusMoney;
    private Integer rationSize;
    /**
     * 分配明细
     */
    List<RatUrationDtlDTO> rationDlts;
    /** 授信占用比列 **/
    private double ratOccupyRatio;
    /** 保证金比例 **/
    private BigDecimal assurepercent;
    /** 授信方式 */
    private String creditType;
    /** 银行编号 */
    private String bankCode;
    /** 银行名称 */
    private String bankName;
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
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getUseMoney() {
        return useMoney;
    }
    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }
    public BigDecimal getApproveMoney() {
        return approveMoney;
    }
    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public BigDecimal getOverPlusMoney() {
        if (null == this.overPlusMoney) {
            this.overPlusMoney = BigDecimal.ZERO;
        }
        return overPlusMoney;
    }
    public void setOverPlusMoney(BigDecimal overPlusMoney) {
        this.overPlusMoney = overPlusMoney;
    }
    public List<RatUrationDtlDTO> getRationDlts() {
        return rationDlts;
    }
    public void setRationDlts(List<RatUrationDtlDTO> rationDlts) {
        this.rationDlts = rationDlts;
    }
    public Integer getRationSize() {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
        return rationSize;
    }
    public void setRationSize(Integer rationSize) {
        this.rationSize = rationSize;
    }
    public double getRatOccupyRatio() {
        return ratOccupyRatio;
    }
    public void setRatOccupyRatio(double ratOccupyRatio) {
        this.ratOccupyRatio = ratOccupyRatio;
    }
    public void addRationDtl(RatUrationDtlDTO rationDtl) {
        if (null == this.rationDlts) {
            rationDlts = new ArrayList<RatUrationDtlDTO>();
        }
        this.rationDlts.add(rationDtl);
        addRationSize();
    }
    public void addRationSize() {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
