package com.bytter.biz.report.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class IncomeAndExpenditureOfFundsVo implements Serializable {
    private String id;
    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "sc.id")
    private String corpId;
    private String corpCode;
    private String corpName;

    private String parentIds;
    /**
     *区域
     */
    private String regionId;
    private String regionName;
    /**
     * 营运区
     */
    private String operateAreaId;
    private String operateAreaName;

    /**
     * 数据级别
     */
    private Integer level;
    /**
     * 股权Id
     */
    private String equityMaintenanceId;
    private String equityMaintenanceName;
    /**
     * 最高利率
     */
    private BigDecimal maxRate;
    /**
     * 期初余额
     */
    private BigDecimal beginBal;
    /**
     * 收入
     */
    private BigDecimal income;
    /**
     * 支出
     */
    private BigDecimal expenditure;
    /**
     * 期末余额
     */
    private BigDecimal endBal;
    /**
     * 实际期末余额
     */
    private BigDecimal realEndBal;

    /**
     * 理财金额
     */
    private BigDecimal manageMoney;
    /**
     * 备注
     */
    private String remark;

    /**
     * 省份
     */
    private String provinceId;
    private String provinceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getEquityMaintenanceId() {
        return equityMaintenanceId;
    }

    public void setEquityMaintenanceId(String equityMaintenanceId) {
        this.equityMaintenanceId = equityMaintenanceId;
    }

    public String getEquityMaintenanceName() {
        return equityMaintenanceName;
    }

    public void setEquityMaintenanceName(String equityMaintenanceName) {
        this.equityMaintenanceName = equityMaintenanceName;
    }

    public String getOperateAreaId() {
        return operateAreaId;
    }

    public void setOperateAreaId(String operateAreaId) {
        this.operateAreaId = operateAreaId;
    }

    public String getOperateAreaName() {
        return operateAreaName;
    }

    public void setOperateAreaName(String operateAreaName) {
        this.operateAreaName = operateAreaName;
    }

    public BigDecimal getMaxRate() {
        if (maxRate==null){
            return BigDecimal.ZERO;
        }
        return maxRate;
    }

    public void setMaxRate(BigDecimal maxRate) {
        this.maxRate = maxRate;
    }

    public BigDecimal getBeginBal() {
        if (beginBal==null){
            return BigDecimal.ZERO;
        }
        return beginBal;
    }

    public void setBeginBal(BigDecimal beginBal) {
        this.beginBal = beginBal;
    }

    public BigDecimal getIncome() {
        if (income==null){
            return BigDecimal.ZERO;
        }
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpenditure() {
        if (expenditure==null){
            return BigDecimal.ZERO;
        }
        return expenditure;
    }

    public void setExpenditure(BigDecimal expenditure) {
        this.expenditure = expenditure;
    }

    public BigDecimal getEndBal() {
        if (endBal==null){
            return BigDecimal.ZERO;
        }
        return endBal;
    }

    public void setEndBal(BigDecimal endBal) {
        this.endBal = endBal;
    }

    public BigDecimal getRealEndBal() {
        if (realEndBal==null){
            return BigDecimal.ZERO;
        }
        return realEndBal;
    }

    public void setRealEndBal(BigDecimal realEndBal) {
        this.realEndBal = realEndBal;
    }

    public BigDecimal getManageMoney() {
        if (manageMoney==null){
            return BigDecimal.ZERO;
        }
        return manageMoney;
    }

    public void setManageMoney(BigDecimal manageMoney) {
        this.manageMoney = manageMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
