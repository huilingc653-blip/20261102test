package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
* <br>
* <b>功能：</b> 融资余额数据DTO <br>
* <b>作者：</b>liyh<br>
* <b>日期：</b> 2020/12/8 0008 <br>
*/
public class FinanceLoanDTO implements Serializable {
    /**
     * 业务类别
     * 01 借款  02 票据  03 债券  04 利息支出
     * */
    private String busType;
    /**单位ID*/
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**保证金金额*/
    private BigDecimal bondMoney;
    /**业务金额*/
    private BigDecimal money;
    /**敞口金额*/
    private BigDecimal openMoney;
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public BigDecimal getBondMoney() {
        if(null == bondMoney){
            bondMoney = BigDecimal.ZERO;
        }
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
