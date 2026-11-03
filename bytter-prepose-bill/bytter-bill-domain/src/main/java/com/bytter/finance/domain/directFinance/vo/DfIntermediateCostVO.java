package com.bytter.finance.domain.directFinance.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-09 16:39
 * @Version 1.0
 */
public class DfIntermediateCostVO implements Serializable {
    /**
     * 业务主键
     */
    private String id;
    /**
     * 直融业务id
     */
    private String directFinanceId;
    /**
     * 费用类型id
     */
    private String costTypeId;
    private String costName;
    /**
     * 费率
     */
    private BigDecimal rate;
    private String rateShow;
    /**
     * 预计费用
     */
    private BigDecimal advanceFees;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public String getCostTypeId() {
        return costTypeId;
    }
    public void setCostTypeId(String costTypeId) {
        this.costTypeId = costTypeId;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getAdvanceFees() {
        return advanceFees;
    }
    public void setAdvanceFees(BigDecimal advanceFees) {
        this.advanceFees = advanceFees;
    }
    public String getCostName() {
        return costName;
    }
    public void setCostName(String costName) {
        this.costName = costName;
    }

    public String getRateShow() {
        return rateShow;
    }

    public void setRateShow(String rateShow) {
        this.rateShow = rateShow;
    }
}
