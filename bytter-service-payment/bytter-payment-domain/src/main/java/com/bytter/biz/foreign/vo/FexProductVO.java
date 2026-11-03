package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 外汇产品表VO
 * @author huangx
 * @date 2020-02-16 15:20:33
 * @version 1.0
 *
 */
public class FexProductVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**层级 0-产品组 1-产品*/
    @BtCompositeSearch(fieldName = "p.pro_Level")
    private String proLevel;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**名称*/
    @BtCompositeSearch(fieldName = "p.pro_Name")
    private String proName;
    /**描述*/
    private String proDesc;
    /**所属组ID*/
    private String proId;
    @BtCompositeSearch(fieldName = "f.pro_Name")
    private String parentProName;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType;
    /**外汇交易类型 0-即期、1-远期*/
    @BtCompositeSearch(fieldName = "p.fex_Tran_Type")
    private String fexTranType;
    /**交割类型 0-本金交割、1-无本金交割*/
    @BtCompositeSearch(fieldName = "p.delivery_Type")
    @Dict(dictGroup = "fexProduct.deliveryType",inject = "deliveryTypeName")
    private String deliveryType;
    private String deliveryTypeName;
    /**交割币种*/
    @Currencys(
            {
                    @Currency(inject="deliveryCurName"),
                    @Currency(inject="deliveryEnglishCode", field = "englishCode")
            }
    )
    private String deliveryCurId;
    private String deliveryCurName;
    private String deliveryEnglishCode;
    /**是否允许多次交割 1-是 0-否*/
    private String isAllowedDelivery;
    /**非工作日是否允许结算 1-是 0-否*/
    @BtCompositeSearch(fieldName = "p.is_Allowed_Settle")
    private String isAllowedSettle;
    /**买入币种*/
    @Currencys(
            {
                    @Currency(inject="buyCurName"),
                    @Currency(inject="buyEnglishCode", field = "englishCode")
            }
    )
    private String buyCurId;
    private String buyCurName;
    private String buyEnglishCode;
    /**卖出币种*/
    @Currencys(
            {
                    @Currency(inject="sellCurName"),
                    @Currency(inject="sellEnglishCode", field = "englishCode")
            }
    )
    private String sellCurId;
    private String sellCurName;
    private String sellEnglishCode;
    /**非工作日适用原则 1-币种、2-交易主体单位所在地、3-主体银行所在地、4-交易对手所在地*/
    @BtCompositeSearch(fieldName = "p.nonWorkday_Rule")
    private String nonWorkdayRule;
    /**交易组规则ID*/
    @BtCompositeSearch(fieldName = "p.trangroup_Rule_Id")
    private String trangroupRuleId;

    @BtCompositeSearch(fieldName = "r.rule_Name")
    private String trangroupRuleName;
    /**交易组规则是否可以在交易组中更改 1-是 0-否*/
    private String isAllowedChange;
    /**有效标志 1-是 0-否*/
    @BtCompositeSearch(fieldName = "p.valid_Sign")
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

    /**成员单号*/
    private String billId;

    /**外汇期权品种1 0-普通期权、1-奇异期权*/
    @BtCompositeSearch(fieldName = "p.fex_Option1")
    private String fexOption1;
    /**外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权*/
    private String fexOption2;
    /**外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)*/
    private String fexOption3;
    /**外汇期权类型 0-欧式、1-美式*/
    @BtCompositeSearch(fieldName = "p.fex_Option_Type")
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
    @BtCompositeSearch(fieldName = "p.tran_Settle_Method")
    private String tranSettleMethod;
    /**在交易时展示经纪人 0-否、1-是*/
    private String isShowAgent;
    /**在交易时展示清算所 0-否、1-是*/
    private String isShowClearHouse;
    /**是否可以删除 1-是 0-否*/
    private String isAllowedDel;

    /**即期是否自动交割 1-是 0-否*/
    private String isAutoDelivery;

    public String getIsAutoDelivery() {
        return isAutoDelivery;
    }

    public void setIsAutoDelivery(String isAutoDelivery) {
        this.isAutoDelivery = isAutoDelivery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProLevel() {
        return proLevel;
    }

    public void setProLevel(String proLevel) {
        this.proLevel = proLevel;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getParentProName() {
        return parentProName;
    }

    public void setParentProName(String parentProName) {
        this.parentProName = parentProName;
    }

    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
    }

    public String getDeliveryCurName() {
        return deliveryCurName;
    }

    public void setDeliveryCurName(String deliveryCurName) {
        this.deliveryCurName = deliveryCurName;
    }

    public String getDeliveryEnglishCode() {
        return deliveryEnglishCode;
    }

    public void setDeliveryEnglishCode(String deliveryEnglishCode) {
        this.deliveryEnglishCode = deliveryEnglishCode;
    }

    public String getIsAllowedDelivery() {
        return isAllowedDelivery;
    }

    public void setIsAllowedDelivery(String isAllowedDelivery) {
        this.isAllowedDelivery = isAllowedDelivery;
    }

    public String getIsAllowedSettle() {
        return isAllowedSettle;
    }

    public void setIsAllowedSettle(String isAllowedSettle) {
        this.isAllowedSettle = isAllowedSettle;
    }

    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    public String getBuyCurName() {
        return buyCurName;
    }

    public void setBuyCurName(String buyCurName) {
        this.buyCurName = buyCurName;
    }

    public String getBuyEnglishCode() {
        return buyEnglishCode;
    }

    public void setBuyEnglishCode(String buyEnglishCode) {
        this.buyEnglishCode = buyEnglishCode;
    }

    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    public String getSellCurName() {
        return sellCurName;
    }

    public void setSellCurName(String sellCurName) {
        this.sellCurName = sellCurName;
    }

    public String getSellEnglishCode() {
        return sellEnglishCode;
    }

    public void setSellEnglishCode(String sellEnglishCode) {
        this.sellEnglishCode = sellEnglishCode;
    }

    public String getNonWorkdayRule() {
        return nonWorkdayRule;
    }

    public void setNonWorkdayRule(String nonWorkdayRule) {
        this.nonWorkdayRule = nonWorkdayRule;
    }

    public String getTrangroupRuleId() {
        return trangroupRuleId;
    }

    public void setTrangroupRuleId(String trangroupRuleId) {
        this.trangroupRuleId = trangroupRuleId;
    }

    public String getTrangroupRuleName() {
        return trangroupRuleName;
    }

    public void setTrangroupRuleName(String trangroupRuleName) {
        this.trangroupRuleName = trangroupRuleName;
    }

    public String getIsAllowedChange() {
        return isAllowedChange;
    }

    public void setIsAllowedChange(String isAllowedChange) {
        this.isAllowedChange = isAllowedChange;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getFexOption1() {
        return fexOption1;
    }

    public void setFexOption1(String fexOption1) {
        this.fexOption1 = fexOption1;
    }

    public String getFexOption2() {
        return fexOption2;
    }

    public void setFexOption2(String fexOption2) {
        this.fexOption2 = fexOption2;
    }

    public String getFexOption3() {
        return fexOption3;
    }

    public void setFexOption3(String fexOption3) {
        this.fexOption3 = fexOption3;
    }

    public String getFexOptionType() {
        return fexOptionType;
    }

    public void setFexOptionType(String fexOptionType) {
        this.fexOptionType = fexOptionType;
    }

    public String getFexOptionRate() {
        return fexOptionRate;
    }

    public void setFexOptionRate(String fexOptionRate) {
        this.fexOptionRate = fexOptionRate;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getDaysRule() {
        return daysRule;
    }

    public void setDaysRule(String daysRule) {
        this.daysRule = daysRule;
    }

    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
    }

    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    public String getIsShowAgent() {
        return isShowAgent;
    }

    public void setIsShowAgent(String isShowAgent) {
        this.isShowAgent = isShowAgent;
    }

    public String getIsShowClearHouse() {
        return isShowClearHouse;
    }

    public void setIsShowClearHouse(String isShowClearHouse) {
        this.isShowClearHouse = isShowClearHouse;
    }

    public String getIsAllowedDel() {
        return isAllowedDel;
    }

    public void setIsAllowedDel(String isAllowedDel) {
        this.isAllowedDel = isAllowedDel;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }
}
