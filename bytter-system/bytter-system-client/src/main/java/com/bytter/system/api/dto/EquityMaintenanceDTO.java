package com.bytter.system.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class EquityMaintenanceDTO implements Serializable {
    private String id;
    private String equityCode;
    private String equityName;

    /**
     * 资金上划金额和
     */
    private BigDecimal sumUpward;
    /**
     * 资金上划同比
     */
    private BigDecimal tongBiUpward;
    /**
     * 资金上划环比
     */
    private BigDecimal huanBiUpward;
    /**
     * 资金下拨金额和
     */
    private BigDecimal sumAllocation;
    /**
     * 资金下拨同比
     */
    private BigDecimal tongBiAllocation;
    /**
     * 资金下拨环比
     */
    private BigDecimal huanBiAllocation;
    /**
     * 留存金额
     */
    private BigDecimal retainedAmount;
    /**
     * 留存同比
     */
    private BigDecimal tongBiRetained;
    /**
     * 留存环比
     */
    private BigDecimal huanBiRetained;
    /**
     * 留存率
     */
    private BigDecimal retentionRate;

    public BigDecimal getSumUpward() {
        return sumUpward;
    }

    public void setSumUpward(BigDecimal sumUpward) {
        this.sumUpward = sumUpward;
    }

    public BigDecimal getTongBiUpward() {
        return tongBiUpward;
    }

    public void setTongBiUpward(BigDecimal tongBiUpward) {
        this.tongBiUpward = tongBiUpward;
    }

    public BigDecimal getHuanBiUpward() {
        return huanBiUpward;
    }

    public void setHuanBiUpward(BigDecimal huanBiUpward) {
        this.huanBiUpward = huanBiUpward;
    }

    public BigDecimal getSumAllocation() {
        return sumAllocation;
    }

    public void setSumAllocation(BigDecimal sumAllocation) {
        this.sumAllocation = sumAllocation;
    }

    public BigDecimal getTongBiAllocation() {
        return tongBiAllocation;
    }

    public void setTongBiAllocation(BigDecimal tongBiAllocation) {
        this.tongBiAllocation = tongBiAllocation;
    }

    public BigDecimal getHuanBiAllocation() {
        return huanBiAllocation;
    }

    public void setHuanBiAllocation(BigDecimal huanBiAllocation) {
        this.huanBiAllocation = huanBiAllocation;
    }

    public BigDecimal getRetainedAmount() {
        return retainedAmount;
    }

    public void setRetainedAmount(BigDecimal retainedAmount) {
        this.retainedAmount = retainedAmount;
    }

    public BigDecimal getTongBiRetained() {
        return tongBiRetained;
    }

    public void setTongBiRetained(BigDecimal tongBiRetained) {
        this.tongBiRetained = tongBiRetained;
    }

    public BigDecimal getHuanBiRetained() {
        return huanBiRetained;
    }

    public void setHuanBiRetained(BigDecimal huanBiRetained) {
        this.huanBiRetained = huanBiRetained;
    }

    public BigDecimal getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(BigDecimal retentionRate) {
        this.retentionRate = retentionRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquityCode() {
        return equityCode;
    }

    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }

    public String getEquityName() {
        return equityName;
    }

    public void setEquityName(String equityName) {
        this.equityName = equityName;
    }

    public EquityMaintenanceDTO() {
    }

    public EquityMaintenanceDTO(String id, String equityCode, String equityName) {
        this.id = id;
        this.equityCode = equityCode;
        this.equityName = equityName;
    }
}
