package com.bytter.increment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description: 理财产品赎回DTO
 * @author qinl
 * @date 2020-07098
 * @version 1.0
 *
 */
public class ChoProductRedeemDTO implements Serializable {
    private String id;
    /**业务单号*/
    private String billNo;
    /**产品登记单号*/
    private String reqId;
    /**单位Id*/
    private String corpId;
    /**赎回金额*/
    private BigDecimal redeemMoney;
    /**赎回日期*/
    private Date redeemDate;
    /**到账日期*/
    private Date transferDate;
    /**收益金额*/
    private BigDecimal incomeMoney;
    /**实际收益率*/
    private BigDecimal incomeRate;
    /**备注*/
    private String rmk;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**赎回费*/
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**状态*/
    private Integer status;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新时间*/
    private Date updateDate;
    /**银行流水认领组件挂接确认*/
    private String collectionConfirm;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getReqId() {
        return reqId;
    }
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public BigDecimal getRedeemMoney() {
        return redeemMoney;
    }
    public void setRedeemMoney(BigDecimal redeemMoney) {
        this.redeemMoney = redeemMoney;
    }
    public Date getRedeemDate() {
        return redeemDate;
    }
    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }
    public Date getTransferDate() {
        return transferDate;
    }
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }
    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }
    public BigDecimal getIncomeRate() {
        return incomeRate;
    }
    public void setIncomeRate(BigDecimal incomeRate) {
        this.incomeRate = incomeRate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }
    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }
    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }
    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }
    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getCollectionConfirm() {
        return collectionConfirm;
    }
    public void setCollectionConfirm(String collectionConfirm) {
        this.collectionConfirm = collectionConfirm;
    }
}
