package com.bytter.credit.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信结转Bean
 *
 * @author liyh
 */
public class RatUrationSysBean implements Serializable {
    /**
     * 授信ID
     */
    private String ratCode;
    /**
     * 占用业务类别
     */
    private String sysCode;
    /**
     * 占用品种
     */
    private String itemCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 占用金额
     */
    private BigDecimal useMoney;
    /**
     * 在途金额
     */
    private BigDecimal appMoney;
    /**
     * 排序
     */
    private Integer order;
    public RatUrationSysBean() {
        useMoney = BigDecimal.ZERO;
        appMoney = BigDecimal.ZERO;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getMoney() {
        if (null == money) {
            money = BigDecimal.ZERO;
        }
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
    public BigDecimal getAppMoney() {
        return appMoney;
    }
    public void setAppMoney(BigDecimal appMoney) {
        this.appMoney = appMoney;
    }
    public Integer getOrder() {
        return order;
    }
    public void setOrder(Integer order) {
        this.order = order;
    }
}
