package com.bytter.biz.erp.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款通知单对象 NIS_BILLHEAD
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Entity
@Table(name = "NIS_BILLHEAD", schema = "")
public class NisBillhead extends IdEntity implements Serializable {
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 付款单号
     */
    private String billNo;

    /**
     * 单位
     */
    private String corpId;

    /**
     * 付款方式
     */
    private String voucherTypeId;

    /**
     * 账号
     */
    private String accId;

    /**
     * 付款账号
     */
    private String payerAcc;

    /**
     * 付款账号名称
     */
    private String payerAccName;

    /**
     * 付款开户行名称
     */
    private String payerBank;

    /**
     * 下拨账号
     */
    private String downAccId;

    /**
     * 币别
     */
    private String curId;

    /**
     * 金额
     */
    private BigDecimal payMoney;

    /**
     * 收款账号
     */
    private String payeeAcc;

    /**
     * 收款账号名称
     */
    private String payeeAccName;

    /**
     * 收款方开户行行号
     */
    private String payeeBankCode;

    /**
     * 收款开户行名称
     */
    private String payeeBank;

    /**
     * 收款行别
     */
    private String bankTypeId;

    /**
     * 所在省
     */
    private String prov;

    /**
     * 所在市
     */
    private String city;

    /**
     * 期望支付日期
     */
    private Date wishPayday;

    /**
     * 加急标志
     */
    private String isQuick;

    /**
     * 是否对私付款
     */
    private String isForindividual;

    /**
     * 接口代码
     */
    private String bifCode;

    /**
     * 跨行标识
     */
    private String difBank;

    /**
     * 地域标识
     */
    private String areaSign;

    /**
     * 会计凭证号
     */
    private String bursarBillNo;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 附言
     */
    private String addWord;

    /**
     * 是否直接付款
     */
    private String directPayFlag;

    /**
     * 证书信息
     */
    private String caInfoId;

    /**
     * 记账日期
     */
    private Date tallyDate;

    /**
     * 凭证流水号
     */
    private String orderNo;

    /**
     * 记账凭证号码
     */
    private String voucherNo;

    /**
     * 票据信息
     */
    private String billId;

    /**
     * ERP编号
     */
    private String erpId;

    /**
     * 业务类别
     */
    private String busType;

    /**
     * 单据来源
     */
    private String billFrom;

    /**
     * 拆分前单据号
     */
    private String splitId;

    /**
     * 合并后单据号
     */
    private String mergeId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 附件ID
     */
    private String attachmentId;

    /**
     * 是否已付款
     */
    private String isPayed;

    /**
     * 收款单位ID
     */
    private String payeeCorpId;

    /**
     * 记账标志
     */
    private String tallySign;

    /**
     * 记账员代码
     */
    private String tallyCode;

    /**
     * 内部资金计划标识
     */
    private String innerSign;

    /**
     * 营运资金比例
     */
    private BigDecimal workCapitalRatio;

    /**
     * 预计收回日期
     */
    private Date takeBackDate;

    /**
     * 是否垫支
     */
    private String isAdvance;

    /**
     * 是否已开票
     */
    private String isOpenBill;

    /**
     * 银行流水号
     */
    private String bankSerialNo;

    /**
     * 原始拆分前单据号
     */
    private String origSplitId;

    /**
     * 票据类型
     */
    private Long billType;

    /**
     * 票据流通介质
     */
    private Long billFlag;

    /**
     * 是否自动支付
     */
    private String isAutoPay;

    /**
     * 策略ID
     */
    private String policyId;

    private Date createDate;

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "VOUCHER_TYPE_ID")
    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    @Column(name = "ACC_ID")
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Column(name = "PAYER_ACC")
    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    @Column(name = "PAYER_ACC_NAME")
    public String getPayerAccName() {
        return payerAccName;
    }

    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }

    @Column(name = "PAYER_BANK")
    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    @Column(name = "DOWN_ACC_ID")
    public String getDownAccId() {
        return downAccId;
    }

    public void setDownAccId(String downAccId) {
        this.downAccId = downAccId;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "PAY_MONEY")
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name = "PAYEE_ACC")
    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    @Column(name = "PAYEE_ACC_NAME")
    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }

    @Column(name = "PAYEE_BANK_CODE")
    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }

    @Column(name = "PAYEE_BANK")
    public String getPayeeBank() {
        return payeeBank;
    }

    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "PROV")
    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "WISH_PAYDAY")
    public Date getWishPayday() {
        return wishPayday;
    }

    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }

    @Column(name = "IS_QUICK")
    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    @Column(name = "IS_FORINDIVIDUAL")
    public String getIsForindividual() {
        return isForindividual;
    }

    public void setIsForindividual(String isForindividual) {
        this.isForindividual = isForindividual;
    }

    @Column(name = "BIF_CODE")
    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    @Column(name = "DIF_BANK")
    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Column(name = "AREA_SIGN")
    public String getAreaSign() {
        return areaSign;
    }

    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }

    @Column(name = "BURSAR_BILL_NO")
    public String getBursarBillNo() {
        return bursarBillNo;
    }

    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
    }

    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name = "ADD_WORD")
    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    @Column(name = "DIRECT_PAY_FLAG")
    public String getDirectPayFlag() {
        return directPayFlag;
    }

    public void setDirectPayFlag(String directPayFlag) {
        this.directPayFlag = directPayFlag;
    }

    @Column(name = "CA_INFO_ID")
    public String getCaInfoId() {
        return caInfoId;
    }

    public void setCaInfoId(String caInfoId) {
        this.caInfoId = caInfoId;
    }

    @Column(name = "TALLY_DATE")
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

    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    @Column(name = "BILL_ID")
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Column(name = "ERP_ID")
    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    @Column(name = "BUS_TYPE")
    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    @Column(name = "BILL_FROM")
    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    @Column(name = "SPLIT_ID")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Column(name = "MERGE_ID")
    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name = "IS_PAYED")
    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    @Column(name = "PAYEE_CORP_ID")
    public String getPayeeCorpId() {
        return payeeCorpId;
    }

    public void setPayeeCorpId(String payeeCorpId) {
        this.payeeCorpId = payeeCorpId;
    }

    @Column(name = "TALLY_SIGN")
    public String getTallySign() {
        return tallySign;
    }

    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }

    @Column(name = "TALLY_CODE")
    public String getTallyCode() {
        return tallyCode;
    }

    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }

    @Column(name = "INNER_SIGN")
    public String getInnerSign() {
        return innerSign;
    }

    public void setInnerSign(String innerSign) {
        this.innerSign = innerSign;
    }

    @Column(name = "WORK_CAPITAL_RATIO")
    public BigDecimal getWorkCapitalRatio() {
        return workCapitalRatio;
    }

    public void setWorkCapitalRatio(BigDecimal workCapitalRatio) {
        this.workCapitalRatio = workCapitalRatio;
    }

    @Column(name = "TAKE_BACK_DATE")
    public Date getTakeBackDate() {
        return takeBackDate;
    }

    public void setTakeBackDate(Date takeBackDate) {
        this.takeBackDate = takeBackDate;
    }

    @Column(name = "IS_ADVANCE")
    public String getIsAdvance() {
        return isAdvance;
    }

    public void setIsAdvance(String isAdvance) {
        this.isAdvance = isAdvance;
    }

    @Column(name = "IS_OPEN_BILL")
    public String getIsOpenBill() {
        return isOpenBill;
    }

    public void setIsOpenBill(String isOpenBill) {
        this.isOpenBill = isOpenBill;
    }

    @Column(name = "BANK_SERIAL_NO")
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    @Column(name = "ORIG_SPLIT_ID")
    public String getOrigSplitId() {
        return origSplitId;
    }

    public void setOrigSplitId(String origSplitId) {
        this.origSplitId = origSplitId;
    }

    @Column(name = "BILL_TYPE")
    public Long getBillType() {
        return billType;
    }

    public void setBillType(Long billType) {
        this.billType = billType;
    }

    @Column(name = "BILL_FLAG")
    public Long getBillFlag() {
        return billFlag;
    }

    public void setBillFlag(Long billFlag) {
        this.billFlag = billFlag;
    }

    @Column(name = "IS_AUTO_PAY")
    public String getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }

    @Column(name = "POLICY_ID")
    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
