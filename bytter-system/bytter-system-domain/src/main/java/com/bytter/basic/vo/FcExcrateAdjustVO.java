package com.bytter.basic.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 币别汇率表
 * @date 2017-05-24 09:57:15
 */
@SuppressWarnings("serial")
public class FcExcrateAdjustVO implements Serializable {
    private String id;
    /**
     * 汇率类型
     */
    @BtCompositeSearch(fieldName = "t.type_id")
    @Dict(tableName = "BT_RATE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "voucherName")
    private String typeId;
    @BtCompositeSearch(fieldName = "t.type_id")
    private String typeName;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "t.cur_id")
    @Dict(tableName = "bt_currency", valueFieldName = "id", showFiledName = "curName", inject = "curName")
    private String curId;
    @BtCompositeSearch(fieldName = "t.cur_id")
    private String curName;
    /**
     * 参考币别
     */
    @BtCompositeSearch(fieldName = "t.refer_cur_id")
    @Dict(tableName = "bt_currency", valueFieldName = "id", showFiledName = "referCurName", inject = "referCurName")
    private String referCurId;
    @BtCompositeSearch(fieldName = "t.refer_cur_id")
    private String referCurName;
    /**
     * 执行日期
     */
    @BtCompositeSearch(fieldName = "t.pre_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date preTime;
    /**
     * 原汇率
     */
    @BtCompositeSearch(fieldName = "t.old_exchange_rate")
    private BigDecimal oldExchangeRate;
    /**
     * 当前汇率
     */
    @BtCompositeSearch(fieldName = "t.new_exchange_rate")
    private BigDecimal newExchangeRate;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "t.rmk")
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
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    /**
     * 汇买价
     */
    @BtCompositeSearch(fieldName = "t.foreign_exchange_purchase")
    private BigDecimal foreignExchangePurchase;
    /**
     * 汇卖价
     */
    @BtCompositeSearch(fieldName = "t.foreign_exchange_sell")
    private BigDecimal foreignExchangeSell;
    /**
     * 中间价
     */
    @BtCompositeSearch(fieldName = "t.foreign_exchange_middle")
    private BigDecimal foreignExchangeMiddle;
    /**
     * 汇率保留小数位数
     */
    private Integer decimalPlaces;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getReferCurId() {
        return referCurId;
    }
    public void setReferCurId(String referCurId) {
        this.referCurId = referCurId;
    }
    public String getReferCurName() {
        return referCurName;
    }
    public void setReferCurName(String referCurName) {
        this.referCurName = referCurName;
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
    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }
    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
