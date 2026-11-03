package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 结算现金流表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CASH_FLOW", schema = "")
public class FexCashFlow extends IdEntity implements java.io.Serializable {
    /**流水号*/
    private String billNo;
    /**交易ID*/
    private String tranBillId;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**现金流日期*/
    private Date cashFlowDate;
    /**结算类型：1-全额，2-净额*/
    private String settleType;
    /**单位*/
    private String corpId;
    /**现金流类型：0本金，1利息*/
    private String cashFlowType;
    /**主体单位银行账户ID*/
    private String corpAccId;
    /**主体单位银行*/
    private String corpBank;
    /**主体单位银行账户*/
    private String corpBankAcc;
    private String corpAccName;
    /**交易对手*/
    private String cotpId;
    /**交易对手银行*/
    private String cotpBank;
    /**交易对手银行账户*/
    private String cotpAccId;
    private String cotpBankAcc;
    private String cotpAccName;
    /**币别*/
    private String curId;
    /**金额*/
    private BigDecimal payMoney;
    /**收付方向 0-收款 1-付款*/
    private String cdSign;
    /**结算日期*/
    private Date settleDate;
    /**支付方式*/
    private String voucherTypeId;
    /**实际收款金额*/
    private BigDecimal factsMoney;
    /**实际收款日期*/
    private Date factsDate;
    /**净额结算号*/
    private String settleNo;
    /**备注*/
    private String rmk;
    /**附件*/
    private String attachmentId;
    /**状态*/
    private Integer status;
    /**支付状态 0-待付款 1-已付款*/
    private String payStatus;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**跨行标识*/
    private String difBank;
    /** 银行流水认领功能挂接标记，未认领=0,认领=1*/
    private String bankFlowSign;
    /** 银行流水认领前状态*/
    private Integer oldStatus;
    /**付款账号对应的币别*/
    private String payerCurId;
    /**收款账号对应的币种*/
    private String payeeCurId;
    /**现金流项目*/
    private String cashItem;
    /**银行流水号*/
    private String bankSerialNo;
    /**单据来源 0-交易生成（默认） 1-退款勾对生成*/
    private String billFrom;
    /**关联现金流号码*/
    private String relBillNo;


    /**交易状态*/
    private String voucherStat;
    /**银行落地限额处理结果：A同意/R拒绝*/
    private String landingLimitResult;

    @Column(name ="BILL_NO",nullable=false,length=50)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column(name ="TRAN_BILL_ID",nullable=false,length=50)
    public String getTranBillId() {
        return tranBillId;
    }

    public void setTranBillId(String tranBillId) {
        this.tranBillId = tranBillId;
    }

    @Column(name ="TRAN_TYPE",nullable=true,length=1)
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="CASH_FLOW_DATE",nullable=true)
    public Date getCashFlowDate() {
        return cashFlowDate;
    }

    public void setCashFlowDate(Date cashFlowDate) {
        this.cashFlowDate = cashFlowDate;
    }

    @Column(name ="SETTLE_TYPE",nullable=true,length=1)
    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    @Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CASH_FLOW_TYPE")
    public String getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(String cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    @Column(name ="CORP_ACC_ID",nullable=true,length=32)
    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    @Column(name ="CORP_BANK",nullable=true,length=100)
    public String getCorpBank() {
        return corpBank;
    }

    public void setCorpBank(String corpBank) {
        this.corpBank = corpBank;
    }

    @Column(name ="CORP_BANK_ACC",nullable=true,length=50)
    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    @Column(name ="CORP_ACC_NAME",nullable=true,length=150)
    public String getCorpAccName() {
        return corpAccName;
    }

    public void setCorpAccName(String corpAccName) {
        this.corpAccName = corpAccName;
    }

    @Column(name ="COTP_ID",nullable=true,length=32)
    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    @Column(name ="COTP_BANK",nullable=true,length=32)
    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    @Column(name ="COTP_ACC_ID",nullable=true,length=32)
    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    @Column(name ="COTP_BANK_ACC",nullable=true,length=50)
    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    @Column(name ="COTP_ACC_NAME",nullable=true,length=150)
    public String getCotpAccName() {
        return cotpAccName;
    }

    public void setCotpAccName(String cotpAccName) {
        this.cotpAccName = cotpAccName;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="PAY_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name ="CD_SIGN",nullable=true,length=1)
    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    @Column(name ="SETTLE_DATE",nullable=true)
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name ="VOUCHER_TYPE_ID",nullable=false,length=32)
    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    @Column(name ="FACTS_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getFactsMoney() {
        return factsMoney;
    }

    public void setFactsMoney(BigDecimal factsMoney) {
        this.factsMoney = factsMoney;
    }

    @Column(name ="FACTS_DATE",nullable=true)
    public Date getFactsDate() {
        return factsDate;
    }

    public void setFactsDate(Date factsDate) {
        this.factsDate = factsDate;
    }

    @Column(name ="SETTLE_NO",nullable=false,length=30)
    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="ATTACHMENT_ID",nullable=true,length=32)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="PAY_STATUS",nullable=true,length=1)
    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
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

    @Column(name ="DIF_BANK",nullable=true,length=1)
    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Column(name ="BANK_FLOW_SIGN")
    public String getBankFlowSign() {
        return bankFlowSign;
    }

    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }

    @Column(name ="OLD_STATUS")
    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    @Column(name ="PAYER_CUR_ID")
    public String getPayerCurId() {
        return payerCurId;
    }

    public void setPayerCurId(String payerCurId) {
        this.payerCurId = payerCurId;
    }

    @Column(name ="PAYEE_CUR_ID")
    public String getPayeeCurId() {
        return payeeCurId;
    }

    public void setPayeeCurId(String payeeCurId) {
        this.payeeCurId = payeeCurId;
    }

    @Column(name ="CASH_ITEM",nullable=true)
    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    @Column(name ="BANK_SERIAL_NO",nullable=true)
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    @Column(name ="BILL_FROM",nullable=true)
    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    @Column(name ="REL_BILL_NO",nullable=true)
    public String getRelBillNo() {
        return relBillNo;
    }

    public void setRelBillNo(String relBillNo) {
        this.relBillNo = relBillNo;
    }

    @Column(name ="voucher_stat",nullable=false,length=30)
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    @Column(name ="LANDING_LIMIT_RESULT", length=1)
    public String getLandingLimitResult() {
        return landingLimitResult;
    }

    public void setLandingLimitResult(String landingLimitResult) {
        this.landingLimitResult = landingLimitResult;
    }


}
