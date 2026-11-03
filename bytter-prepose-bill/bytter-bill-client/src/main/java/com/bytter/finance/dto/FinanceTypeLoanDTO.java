package com.bytter.finance.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <br>
 * <b>功能：</b> 融资类别余额数据DTO <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/12/8 0008 <br>
 */
public class FinanceTypeLoanDTO implements Serializable {
    /** 业务模块 */
    private String sysCode;
    /**
     * 融资品种编号
     */
    private String typeCode;
    /**
     * 融资品种
     */
    private String typeName;
    /** 保证金金额 */
    private BigDecimal bondMoney;
    /** 业务金额 */
    private BigDecimal money;
    /** 敞口金额 */
    private BigDecimal openMoney;
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
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
}
