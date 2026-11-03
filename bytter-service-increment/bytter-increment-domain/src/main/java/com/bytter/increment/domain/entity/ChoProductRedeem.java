package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Table
 * @Description: 理财产品赎回表
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHO_PRODUCT_REDEEM", schema = "")
public class ChoProductRedeem extends IdEntity implements java.io.Serializable {
    /**业务单号*/
    private String billNo;
    /**产品登记单号*/
    private String reqId;
    /**单位Id*/
    private String corpId;
    private String corpName;
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
    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;
    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;
    /**
     * 理财类型
     */
    private String financialType;
    /**
     * 净值维护日期
     */
    private Date netValDate;
    /**理财账号*/
    private String bankAcc;
    /**购买银行*/
    private String bankName;
    /**产品代码*/
    private String productCode;
    /**
     * 到期是否续存
     */
    private String isStore;
    /**续存模式 1-本金续存 2-本息续存*/
    private Integer certificateWay;

    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Transient
    public String getFinancialType() {
        return financialType;
    }
    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }
    @Transient
    public Date getNetValDate() {
        return netValDate;
    }
    public void setNetValDate(Date netValDate) {
        this.netValDate = netValDate;
    }
    @Column(name = "REDEEM_DAILY_NET_VAL")
    public BigDecimal getRedeemDailyNetVal() {
        return redeemDailyNetVal;
    }
    public void setRedeemDailyNetVal(BigDecimal redeemDailyNetVal) {
        this.redeemDailyNetVal = redeemDailyNetVal;
    }
    @Column(name = "REDEEM_PORTION")
    public BigDecimal getRedeemPortion() {
        return redeemPortion;
    }
    public void setRedeemPortion(BigDecimal redeemPortion) {
        this.redeemPortion = redeemPortion;
    }
    @Column(name ="COLLECTION_CONFIRM",nullable=false,length=1)
    public String getCollectionConfirm() {
        return collectionConfirm;
    }
    public void setCollectionConfirm(String collectionConfirm) {
        this.collectionConfirm = collectionConfirm;
    }
    @Column(name ="BILL_NO",length=12)
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name ="REQ_ID",length=32)
    public String getReqId() {
        return reqId;
    }
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }
    @Column(name ="CORP_ID",length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="REDEEM_MONEY",scale = 2,length=15)
    public BigDecimal getRedeemMoney() {
        return redeemMoney;
    }
    public void setRedeemMoney(BigDecimal redeemMoney) {
        this.redeemMoney = redeemMoney;
    }
    @Column(name ="REDEEM_DATE")
    public Date getRedeemDate() {
        return redeemDate;
    }
    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }
    @Column(name ="TRANSFER_DATE")
    public Date getTransferDate() {
        return transferDate;
    }
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    @Column(name ="INCOME_MONEY",scale = 2,length=15)
    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }
    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }
    @Column(name ="INCOME_RATE",scale = 6,length=8)
    public BigDecimal getIncomeRate() {
        return incomeRate;
    }
    public void setIncomeRate(BigDecimal incomeRate) {
        this.incomeRate = incomeRate;
    }
    @Column(name ="RMK",length=200)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="SUB_FEE_MONEY",scale = 2,length=15)
    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }
    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }
    @Column(name ="REDEEM_CHARGE_MONEY",scale = 2,length=15)
    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }
    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }
    @Column(name ="CHARGE_MONEY",scale = 2,length=15)
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }
    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
    @Column(name ="STATUS",length=12)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="CREATE_BY",length=12)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE",length=12)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",length=12)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE",length=12)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name ="BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name ="PRODUCT_CODE")
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    @Column(name = "IS_STORE")
    public String getIsStore() {
        return isStore;
    }
    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }

    @Column(name = "CERTIFICATE_WAY")
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
}
