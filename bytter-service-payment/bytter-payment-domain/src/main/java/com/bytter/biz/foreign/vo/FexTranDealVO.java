package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 交易交割表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
public class FexTranDealVO implements Serializable, IBaseDataInject, IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**交易ID*/
    private String tranReqId;
    @BtCompositeSearch(fieldName = "r.bill_No")
    private String tranReqNo;
    /**单位*/
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpId;
    private String corpName;
    /**交割方向  1-买入，2-卖出*/
    @BtCompositeSearch(fieldName = "t.delivery_Sign")
    private String deliverySign;
    /**原结算日期*/
    private Date oldSettleDate;
    /**新结算日期*/
    @BtCompositeSearch(fieldName = "t.new_Settle_Date")
    private Date newSettleDate;
    /**结算金额*/
    @BtCompositeSearch(fieldName = "t.settle_Money")
    private BigDecimal settleMoney;
    /**结算汇率*/
    private BigDecimal settleRate;
    /**即期汇率*/
    private BigDecimal wishRate;
    /**余额*/
    private BigDecimal bal;
    /**违约日期*/
    @BtCompositeSearch(fieldName = "t.default_Date")
    private Date defaultDate;
    /**币种*/
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private String curId;
    private String curName;
    private String englishCode;
    /**违约金*/
    @BtCompositeSearch(fieldName = "t.default_Money")
    private BigDecimal defaultMoney;
    /**交割汇率*/
    private BigDecimal deliveryRate;
    /**交割日期*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private Date deliveryDate;
    /**状态*/
    private Integer status;
    /**交易地点*/
    private String tranAddr;
    /**交易地点ID*/
    private String tranAddrId;
    /**备注*/
    private String rmk;
    /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
    private String opType;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    /**计划科目*/
    private String[] itemCode;
    /**计划科目名称*/
    private String[] itemName;
    /**执行金额*/
    private BigDecimal[] processMoney;
    /**汇率*/
    private BigDecimal[] exchangeRate;
    /**执行金额（折合本身币别）*/
    private BigDecimal[] processCurMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranReqId() {
        return tranReqId;
    }

    public void setTranReqId(String tranReqId) {
        this.tranReqId = tranReqId;
    }

    public String getTranReqNo() {
        return tranReqNo;
    }

    public void setTranReqNo(String tranReqNo) {
        this.tranReqNo = tranReqNo;
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

    public String getDeliverySign() {
        return deliverySign;
    }

    public void setDeliverySign(String deliverySign) {
        this.deliverySign = deliverySign;
    }

    public Date getOldSettleDate() {
        return oldSettleDate;
    }

    public void setOldSettleDate(Date oldSettleDate) {
        this.oldSettleDate = oldSettleDate;
    }

    public Date getNewSettleDate() {
        return newSettleDate;
    }

    public void setNewSettleDate(Date newSettleDate) {
        this.newSettleDate = newSettleDate;
    }

    public BigDecimal getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(BigDecimal settleMoney) {
        this.settleMoney = settleMoney;
    }

    public BigDecimal getSettleRate() {
        return settleRate;
    }

    public void setSettleRate(BigDecimal settleRate) {
        this.settleRate = settleRate;
    }

    public BigDecimal getWishRate() {
        return wishRate;
    }

    public void setWishRate(BigDecimal wishRate) {
        this.wishRate = wishRate;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public BigDecimal getDefaultMoney() {
        return defaultMoney;
    }

    public void setDefaultMoney(BigDecimal defaultMoney) {
        this.defaultMoney = defaultMoney;
    }

    public BigDecimal getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(BigDecimal deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
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

    public String[] getItemCode() {
        return itemCode;
    }

    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }

    public String[] getItemName() {
        return itemName;
    }

    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }

    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }

    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }

    public BigDecimal[] getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal[] exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal[] getProcessCurMoney() {
        return processCurMoney;
    }

    public void setProcessCurMoney(BigDecimal[] processCurMoney) {
        this.processCurMoney = processCurMoney;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
