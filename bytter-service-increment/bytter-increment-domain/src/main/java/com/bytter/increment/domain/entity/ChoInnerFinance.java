package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHO_INNER_FINANCE", schema = "")
@SuppressWarnings("serial")
public class ChoInnerFinance extends IdEntity implements java.io.Serializable {
    /**业务单号*/
    private String billNo;
    /**单位ID*/
    private String corpId;
    /**网点*/
    private String netId;

    /**单位名称*/
    private String corpName;
    /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
    private Integer busType;
    /**付款账号ID*/
    private String payerAccId;
    /** 付款账户名称*/
    private String payerAccName;
    /**付款账号利率*/
    private BigDecimal accInterestRate;
    /**付款账号*/
    private String payerAcc;
    /**收款账号ID*/
    private String payeeAccId;
    /**`PAYEE_ACC_NAME` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '收款账户名称',*/
    private String payeeAccName;
    /**收款账号*/
    private String payeeAcc;
    /**本金存入账号ID*/
    private String capitalAccId;
    /** `CAPITAL_ACC` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '本金存入结算中心账号',*/
    private String capitalAcc;
    /** `CAPITAL_ACC_NAME` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '本金存入账户名称',*/
    private String capitalAccName;
    /**利息存入账号ID*/
    private String interestAccId;
    /**`INTEREST_ACC` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '利息存入结算中心账号',*/
    private String interestAcc;
    /**`INTEREST_ACC_NAME` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '利息存入账户名称',*/
    private String interestAccName;
    /**存款类型*/
    private String depositRateType;
    /**`PERIOD_NAME` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '贷款周期的名称',*/
    private String periodName;
    /**币别*/
    private String curId;
    /** `CUR_NAME` varchar(30) COLLATE utf8_bin DEFAULT '' COMMENT '币种名称',*/
    private String curName;

    /**金额*/
    private BigDecimal payMoney;
    /**期限*/
    private Integer dayLimit;
    /**日期单位 Y-年、M-月、D-日*/
    private String dateUnit;
    /**通知类型*/
    private String noticeType;
    /**到期处理方式 0-自动终止、1-本金续存、2-本息续存*/
    private Integer certificateWay;
    /**起息日期*/
    private Date interestStartDate;
    /**到期日期*/
    private Date deadlineDate;
    /**用途*/
    private String purpose;
    /**备注*/
    private String addWord;
    /**利率*/
    private BigDecimal interestRate;
    /**取款日期*/
    private Date payDate;
    /**关联单据ID*/
    private String relatedBillId;
    /**预计利息*/
    private BigDecimal expectInterest;
    /**状态*/
    private Integer status;
    /**记账日期*/
    private Date tallyDate;
    /**凭证流水号*/
    private String orderNo;
    /**记账凭证号码*/
    private String voucherNo;
    /**记账标志*/
    private String tallySign;
    /**记账员代码*/
    private String tallyCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    @Column(name ="BILL_NO",nullable=false,length=12)
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="BUS_TYPE",nullable=false,length=4)
    public Integer getBusType() {
        return busType;
    }
    public void setBusType(Integer busType) {
        this.busType = busType;
    }
    @Column(name ="PAYER_ACC_ID",nullable=true,length=32)
    public String getPayerAccId() {
        return payerAccId;
    }
    public void setPayerAccId(String payerAccId) {
        this.payerAccId = payerAccId;
    }
    @Column(name ="PAYER_ACC",nullable=true,length=30)
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    @Column(name ="PAYEE_ACC_ID",nullable=true,length=32)
    public String getPayeeAccId() {
        return payeeAccId;
    }
    public void setPayeeAccId(String payeeAccId) {
        this.payeeAccId = payeeAccId;
    }
    @Column(name ="PAYEE_ACC",nullable=true,length=30)
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    @Column(name ="CAPITAL_ACC_ID",nullable=true,length=32)
    public String getCapitalAccId() {
        return capitalAccId;
    }
    public void setCapitalAccId(String capitalAccId) {
        this.capitalAccId = capitalAccId;
    }
    @Column(name ="INTEREST_ACC_ID",nullable=true,length=32)
    public String getInterestAccId() {
        return interestAccId;
    }
    public void setInterestAccId(String interestAccId) {
        this.interestAccId = interestAccId;
    }
    @Column(name ="DEPOSIT_RATE_TYPE",nullable=true,length=32)
    public String getDepositRateType() {
        return depositRateType;
    }
    public void setDepositRateType(String depositRateType) {
        this.depositRateType = depositRateType;
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
    @Column(name ="DAY_LIMIT",nullable=true,length=4)
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    @Column(name ="DATE_UNIT",nullable=true,length=2)
    public String getDateUnit() {
        return dateUnit;
    }
    public void setDateUnit(String dateUnit) {
        this.dateUnit = dateUnit;
    }
    @Column(name ="NOTICE_TYPE",nullable=true,length=1)
    public String getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
    @Column(name ="CERTIFICATE_WAY",nullable=true,length=2)
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
    @Column(name ="INTEREST_START_DATE",nullable=true)
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    @Column(name ="DEADLINE_DATE",nullable=true)
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    @Column(name ="PURPOSE",nullable=true,length=60)
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name ="ADD_WORD",nullable=true,length=200)
    public String getAddWord() {
        return addWord;
    }
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
    @Column(name ="INTEREST_RATE",nullable=true,scale=6,length=8)
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name ="PAY_DATE",nullable=true)
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name ="RELATED_BILL_ID",nullable=true)
    public String getRelatedBillId() {
        return relatedBillId;
    }
    public void setRelatedBillId(String relatedBillId) {
        this.relatedBillId = relatedBillId;
    }
    @Column(name ="EXPECT_INTEREST",nullable=true,scale=2,length=15)
    public BigDecimal getExpectInterest() {
        return expectInterest;
    }
    public void setExpectInterest(BigDecimal expectInterest) {
        this.expectInterest = expectInterest;
    }
    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="TALLY_DATE",nullable=true)
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    @Column(name = "VOUCHER_NO", nullable = true, length = 40)
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    @Column(name = "TALLY_SIGN", nullable = true, length = 1)
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    @Column(name = "TALLY_CODE", nullable = true, length = 32)
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "NET_ID", nullable = false, length = 32)
    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    @Column(name = "CORP_NAME", nullable = true, length = 200)
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }


    @Column(name = "PAYER_ACC_NAME", nullable = true, length = 500)
    public String getPayerAccName() {
        return payerAccName;
    }

    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }

    @Column(name = "ACC_INTEREST_RATE", nullable = true, length = 8)
    public BigDecimal getAccInterestRate() {
        return accInterestRate;
    }

    public void setAccInterestRate(BigDecimal accInterestRate) {
        this.accInterestRate = accInterestRate;
    }

    @Column(name = "PAYEE_ACC_NAME", nullable = true, length = 500)
    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }

    @Column(name = "CAPITAL_ACC", nullable = true, length = 500)
    public String getCapitalAcc() {
        return capitalAcc;
    }

    public void setCapitalAcc(String capitalAcc) {
        this.capitalAcc = capitalAcc;
    }

    @Column(name = "CAPITAL_ACC_NAME", nullable = true, length = 500)
    public String getCapitalAccName() {
        return capitalAccName;
    }

    public void setCapitalAccName(String capitalAccName) {
        this.capitalAccName = capitalAccName;
    }

    @Column(name = "INTEREST_ACC", nullable = true, length = 50)
    public String getInterestAcc() {
        return interestAcc;
    }

    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }

    @Column(name = "INTEREST_ACC_NAME", nullable = true, length = 500)
    public String getInterestAccName() {
        return interestAccName;
    }

    public void setInterestAccName(String interestAccName) {
        this.interestAccName = interestAccName;
    }

    @Column(name = "PERIOD_NAME", nullable = true, length = 128)
    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    @Column(name = "CUR_NAME", nullable = true, length = 30)
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }
}
