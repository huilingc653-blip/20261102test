package com.bytter.report.domain.cms.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 各融资品种融资余额报表对象
 *
 * @author zuoy 2020-12-07
 */
public class FinancingBalanceVO implements Serializable {
    /**
     * 业务模块
     */
    private String sysCode;
    /**
     * 融资品种编号
     */
    private String typeCode;
    /**
     * 融资品种
     */
    private String typeName;
    /**
     * 保证金金额
     */
    private BigDecimal bondMoney;
    /**
     * 业务金额
     */
    private BigDecimal money;
    /**
     * 敞口金额
     */
    private BigDecimal openMoney;
    /**
     * 占总额的比例
     */
    private BigDecimal scale;
    /**
     * 占敞口的比例
     */
    private BigDecimal openMoneyScale;
    /**
     * 是否计算比例
     */
    private Boolean calculationScale;
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
    public BigDecimal getScale() {
        return scale;
    }
    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }
    public BigDecimal getOpenMoneyScale() {
        return openMoneyScale;
    }
    public void setOpenMoneyScale(BigDecimal openMoneyScale) {
        this.openMoneyScale = openMoneyScale;
    }
    public Boolean getCalculationScale() {
        return calculationScale;
    }
    public void setCalculationScale(Boolean calculationScale) {
        this.calculationScale = calculationScale;
    }
}
