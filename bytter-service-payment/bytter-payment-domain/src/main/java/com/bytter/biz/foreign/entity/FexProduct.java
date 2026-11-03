package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇产品表
 * @author huangx
 * @date 2020-02-16 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_PRODUCT", schema = "")
@SuppressWarnings("serial")
public class FexProduct extends IdEntity implements java.io.Serializable {
    /**层级 0-产品组 1-产品*/
    private String proLevel;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**名称*/
    private String proName;
    /**描述*/
    private String proDesc;
    /**所属组ID*/
    private String proId;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType;
    /**外汇交易类型 0-即期、1-远期*/
    private String fexTranType;
    /**交割类型 0-本金交割、1-无本金交割*/
    private String deliveryType;
    /**交割币种*/
    private String deliveryCurId;
    /**是否允许多次交割 1-是 0-否*/
    private String isAllowedDelivery;
    /**非工作日是否允许结算 1-是 0-否*/
    private String isAllowedSettle;
    /**买入币种*/
    private String buyCurId;
    /**卖出币种*/
    private String sellCurId;
    /**非工作日适用原则 1-币种、2-交易主体单位所在地、3-主体银行所在地、4-交易对手所在地*/
    private String nonWorkdayRule;
    /**交易组规则ID*/
    private String trangroupRuleId;
    /**交易组规则是否可以在交易组中更改 1-是 0-否*/
    private String isAllowedChange;
    /**有效标志 1-是 0-否*/
    private String validSign;
    /**备注*/
    private String rmk;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    /**外汇期权品种1 0-普通期权、1-奇异期权*/
    private String fexOption1;
    /**外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权*/
    private String fexOption2;
    /**外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)*/
    private String fexOption3;
    /**外汇期权类型 0-欧式、1-美式*/
    private String fexOptionType;
    /**期权费汇率 0-使用协定汇率、1-使用即期汇率*/
    private String fexOptionRate;
    /**退款时间点 0-到期日、1-敲出日*/
    private String refundTime;
    /**加权天数规则 0-按日历（不区分是否工作日）、1-仅工作日*/
    private String daysRule;
    /**交易时障碍验证标准 0-即期汇率、1-即期汇率对应的货币价值*/
    private String validStandard;
    /**交易结算方式 0-现金、0-即期交易*/
    private String tranSettleMethod;
    /**在交易时展示经纪人 0-否、1-是*/
    private String isShowAgent;
    /**在交易时展示清算所 0-否、1-是*/
    private String isShowClearHouse;

    /**即期是否自动交割 1-是 0-否*/
    private String isAutoDelivery;

    @Column(name ="IS_AUTO_DELIVERY",nullable=false,length=1)
    public String getIsAutoDelivery() {
        return isAutoDelivery;
    }

    public void setIsAutoDelivery(String isAutoDelivery) {
        this.isAutoDelivery = isAutoDelivery;
    }

    @Column(name ="PRO_LEVEL",nullable=false,length=1)
    public String getProLevel() {
        return proLevel;
    }

    public void setProLevel(String proLevel) {
        this.proLevel = proLevel;
    }

    @Column(name ="TRAN_TYPE",nullable=false,length=1)
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="PRO_NAME",nullable=false,length=60)
    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    @Column(name ="PRO_DESC",nullable=true,length=100)
    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    @Column(name ="PRO_ID",nullable=true,length=32)
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name ="FEX_BUS_TYPE",nullable=true,length=1)
    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    @Column(name ="FEX_TRAN_TYPE",nullable=true,length=1)
    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    @Column(name ="DELIVERY_TYPE",nullable=true,length=1)
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Column(name ="DELIVERY_CUR_ID",nullable=true,length=32)
    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
    }

    @Column(name ="IS_ALLOWED_DELIVERY",nullable=true,length=1)
    public String getIsAllowedDelivery() {
        return isAllowedDelivery;
    }

    public void setIsAllowedDelivery(String isAllowedDelivery) {
        this.isAllowedDelivery = isAllowedDelivery;
    }

    @Column(name ="IS_ALLOWED_SETTLE",nullable=true,length=1)
    public String getIsAllowedSettle() {
        return isAllowedSettle;
    }

    public void setIsAllowedSettle(String isAllowedSettle) {
        this.isAllowedSettle = isAllowedSettle;
    }

    @Column(name ="BUY_CUR_ID",nullable=true,length=32)
    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    @Column(name ="SELL_CUR_ID",nullable=true,length=32)
    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    @Column(name ="NON_WORKDAY_RULE",nullable=true,length=30)
    public String getNonWorkdayRule() {
        return nonWorkdayRule;
    }

    public void setNonWorkdayRule(String nonWorkdayRule) {
        this.nonWorkdayRule = nonWorkdayRule;
    }

    @Column(name ="TRANGROUP_RULE_ID",nullable=true,length=32)
    public String getTrangroupRuleId() {
        return trangroupRuleId;
    }

    public void setTrangroupRuleId(String trangroupRuleId) {
        this.trangroupRuleId = trangroupRuleId;
    }

    @Column(name ="IS_ALLOWED_CHANGE",nullable=true,length=1)
    public String getIsAllowedChange() {
        return isAllowedChange;
    }

    public void setIsAllowedChange(String isAllowedChange) {
        this.isAllowedChange = isAllowedChange;
    }

    @Column(name ="VALID_SIGN",nullable=true,length=1)
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="FEX_OPTION1",nullable=true)
    public String getFexOption1() {
        return fexOption1;
    }

    public void setFexOption1(String fexOption1) {
        this.fexOption1 = fexOption1;
    }

    @Column(name ="FEX_OPTION2",nullable=true)
    public String getFexOption2() {
        return fexOption2;
    }

    public void setFexOption2(String fexOption2) {
        this.fexOption2 = fexOption2;
    }

    @Column(name ="FEX_OPTION3",nullable=true)
    public String getFexOption3() {
        return fexOption3;
    }

    public void setFexOption3(String fexOption3) {
        this.fexOption3 = fexOption3;
    }

    @Column(name ="FEX_OPTION_TYPE",nullable=true)
    public String getFexOptionType() {
        return fexOptionType;
    }

    public void setFexOptionType(String fexOptionType) {
        this.fexOptionType = fexOptionType;
    }

    @Column(name ="FEX_OPTION_RATE",nullable=true)
    public String getFexOptionRate() {
        return fexOptionRate;
    }

    public void setFexOptionRate(String fexOptionRate) {
        this.fexOptionRate = fexOptionRate;
    }

    @Column(name ="REFUND_TIME",nullable=true)
    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    @Column(name ="DAYS_RULE",nullable=true)
    public String getDaysRule() {
        return daysRule;
    }

    public void setDaysRule(String daysRule) {
        this.daysRule = daysRule;
    }

    @Column(name ="VALID_STANDARD",nullable=true)
    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
    }

    @Column(name ="TRAN_SETTLE_METHOD",nullable=true)
    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    @Column(name ="IS_SHOW_AGENT",nullable=true)
    public String getIsShowAgent() {
        return isShowAgent;
    }

    public void setIsShowAgent(String isShowAgent) {
        this.isShowAgent = isShowAgent;
    }

    @Column(name ="IS_SHOW_CLEAR_HOUSE",nullable=true)
    public String getIsShowClearHouse() {
        return isShowClearHouse;
    }

    public void setIsShowClearHouse(String isShowClearHouse) {
        this.isShowClearHouse = isShowClearHouse;
    }
}
