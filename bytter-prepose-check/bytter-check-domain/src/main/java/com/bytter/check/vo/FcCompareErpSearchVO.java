package com.bytter.check.vo;
import java.io.Serializable;
import java.math.BigDecimal;
public class FcCompareErpSearchVO implements Serializable {
    private String queryType;
    private String bankAcc;
    private String account;
    private String curId;
    private String transDate;
    private String noHandRadios;
    private String stayRadios;
    private String handRadios;
    private String checkRadios;
    private String checkFlag;
    private String orderType;
    private String orderKey;
//    企业日记账金额
    private BigDecimal startMoney;
    private BigDecimal endMoney;
    public String getQueryType() {
        return queryType;
    }
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getTransDate() {
        return transDate;
    }
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }
    public String getNoHandRadios() {
        return noHandRadios;
    }
    public void setNoHandRadios(String noHandRadios) {
        this.noHandRadios = noHandRadios;
    }
    public String getStayRadios() {
        return stayRadios;
    }
    public void setStayRadios(String stayRadios) {
        this.stayRadios = stayRadios;
    }
    public String getHandRadios() {
        return handRadios;
    }
    public void setHandRadios(String handRadios) {
        this.handRadios = handRadios;
    }
    public String getCheckRadios() {
        return checkRadios;
    }
    public void setCheckRadios(String checkRadios) {
        this.checkRadios = checkRadios;
    }
    public String getCheckFlag() {
        return checkFlag;
    }
    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }
    public String getOrderType() {
        return orderType;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getOrderKey() {
        return orderKey;
    }
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
    public BigDecimal getStartMoney() {
        return startMoney;
    }
    public void setStartMoney(BigDecimal startMoney) {
        this.startMoney = startMoney;
    }
    public BigDecimal getEndMoney() {
        return endMoney;
    }
    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }
}
