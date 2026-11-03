package com.bytter.biz.report.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.math.BigDecimal;

public class EnterpriseBankGoOnlineVo implements Serializable {
    private String id;
    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "sc.id")
    private String corpId;
    private String corpName;
    /**
     * 大区
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
     * 合作银行数量
     */
    private Integer cooperationBankNum;
    /**
     * 总账户数
     */
    private Integer accSumNum;
    /**
     * 已上银企账户数量
     */
    private Integer isOnlineEnterpriseBankNum;
    /**
     * 可上银企账户数量
     */
    private Integer isNotOnlineEnterpriseBankNum;
    /**
     * 已上银企上线占比
     */
    private BigDecimal isNotOnlineProportion;
    /**
     * 银企上线进度
     */
    private BigDecimal isOnlineProportion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getCooperationBankNum() {
        if (cooperationBankNum==null){
            return 0;
        }
        return cooperationBankNum;
    }

    public void setCooperationBankNum(Integer cooperationBankNum) {
        this.cooperationBankNum = cooperationBankNum;
    }

    public Integer getAccSumNum() {
        if (accSumNum==null){
            return 0;
        }
        return accSumNum;
    }

    public void setAccSumNum(Integer accSumNum) {
        this.accSumNum = accSumNum;
    }

    public Integer getIsOnlineEnterpriseBankNum() {
        if (isOnlineEnterpriseBankNum==null){
            return 0;
        }
        return isOnlineEnterpriseBankNum;
    }

    public void setIsOnlineEnterpriseBankNum(Integer isOnlineEnterpriseBankNum) {
        this.isOnlineEnterpriseBankNum = isOnlineEnterpriseBankNum;
    }

    public Integer getIsNotOnlineEnterpriseBankNum() {
        if (isNotOnlineEnterpriseBankNum==null){
            return 0;
        }
        return isNotOnlineEnterpriseBankNum;
    }

    public void setIsNotOnlineEnterpriseBankNum(Integer isNotOnlineEnterpriseBankNum) {
        this.isNotOnlineEnterpriseBankNum = isNotOnlineEnterpriseBankNum;
    }

    public BigDecimal getIsNotOnlineProportion() {
        return isNotOnlineProportion;
    }

    public void setIsNotOnlineProportion(BigDecimal isNotOnlineProportion) {
        this.isNotOnlineProportion = isNotOnlineProportion;
    }

    public BigDecimal getIsOnlineProportion() {
        return isOnlineProportion;
    }

    public void setIsOnlineProportion(BigDecimal isOnlineProportion) {
        this.isOnlineProportion = isOnlineProportion;
    }
}
