package com.bytter.system.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 货币汇率维护DTO
 * @date 2017-10-17
 */
public class FcExcrateAdjustDTO implements Serializable {
    private String id;
    /**
     * 币别
     */
    private String curId;
    /**
     * 参考币别
     */
    private String referCurId;
    /**
     * 执行日期
     */
    private Date preTime;
    /**
     * 原汇率
     */
    private BigDecimal oldExchangeRate;
    /**
     * 当前汇率
     */
    private BigDecimal newExchangeRate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 状态 0-暂存 95- 复核
     */
    private Integer status;
    /**
     * 汇买价
     */
    private BigDecimal foreignExchangePurchase;
    /**
     * 汇卖价
     */
    private BigDecimal foreignExchangeSell;
    /**
     * 中间价
     */
    private BigDecimal foreignExchangeMiddle;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getReferCurId() {
        return referCurId;
    }
    public void setReferCurId(String referCurId) {
        this.referCurId = referCurId;
    }
    public Date getPreTime() {
        return preTime;
    }
    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }
    public BigDecimal getOldExchangeRate() {
        return oldExchangeRate;
    }
    public void setOldExchangeRate(BigDecimal oldExchangeRate) {
        this.oldExchangeRate = oldExchangeRate;
    }
    public BigDecimal getNewExchangeRate() {
        return newExchangeRate;
    }
    public void setNewExchangeRate(BigDecimal newExchangeRate) {
        this.newExchangeRate = newExchangeRate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public BigDecimal getForeignExchangePurchase() {
        return foreignExchangePurchase;
    }
    public void setForeignExchangePurchase(BigDecimal foreignExchangePurchase) {
        this.foreignExchangePurchase = foreignExchangePurchase;
    }
    public BigDecimal getForeignExchangeSell() {
        return foreignExchangeSell;
    }
    public void setForeignExchangeSell(BigDecimal foreignExchangeSell) {
        this.foreignExchangeSell = foreignExchangeSell;
    }
    public BigDecimal getForeignExchangeMiddle() {
        return foreignExchangeMiddle;
    }
    public void setForeignExchangeMiddle(BigDecimal foreignExchangeMiddle) {
        this.foreignExchangeMiddle = foreignExchangeMiddle;
    }
}
