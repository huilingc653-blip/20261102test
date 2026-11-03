package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: DTO
 * Description: NIS_EXPENSE_DETAIL
 * @author huangx
 * @since 2017-09-02 11:35:12
 * @version 1.0
 *
 */
public class NisExpenseDetailDTO implements Serializable {
    private String id;
    /**报销编号*/
    private String expenseId;
    /**单位*/
    private String corpId;
    /**报账日期*/
    private Date expenseDate;
    /**职工编号*/
    private String workId;
    /**职工姓名*/
    private String userName;
    /**部门名称*/
    private String deptName;
    /**操作类型*/
    private String opType;
    /**业务类型*/
    private String busType;
    /**接口代码*/
    private String bifCode;
    /**跨行标识*/
    private String difBank;
    /**地域标识*/
    private String areaSign;
    /**报账金额*/
    private BigDecimal expenseMoney;
    /**开户行行号*/
    private String bankCode;
    /**开户银行名称*/
    private String bankName;
    /**银行卡类型*/
    private String cardType;
    /**银行账号*/
    private String bankAcc;
    /**账户名*/
    private String accName;
    /**所在省*/
    private String prov;
    /**所在市*/
    private String city;
    /**用途*/
    private String purpose;
    /**说明*/
    private String rmk;
    /**付款单号*/
    private String billId;
    /**交易状态*/
    private String voucherStat;
    /**返回信息（交易前）*/
    private String firstReturnMsg;
    /**返回信息（交易后）*/
    private String nextReturnMsg;
    /**返回时间*/
    private Date returnTime;
    /**单据来源*/
    private String billFrom;
    /**ERP编号*/
    private String erpId;
    /**状态*/
    private Integer status;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**附件ID*/
    private String attachmentId;


    /**指令编号*/
    private String voucherNo;
    /**签名*/
    private String signatureCode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExpenseId() {
        return expenseId;
    }
    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Date getExpenseDate() {
        return expenseDate;
    }
    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }
    public String getWorkId() {
        return workId;
    }
    public void setWorkId(String workId) {
        this.workId = workId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    public String getAreaSign() {
        return areaSign;
    }
    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }
    public BigDecimal getExpenseMoney() {
        return expenseMoney;
    }
    public void setExpenseMoney(BigDecimal expenseMoney) {
        this.expenseMoney = expenseMoney;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public String getFirstReturnMsg() {
        return firstReturnMsg;
    }
    public void setFirstReturnMsg(String firstReturnMsg) {
        this.firstReturnMsg = firstReturnMsg;
    }
    public String getNextReturnMsg() {
        return nextReturnMsg;
    }
    public void setNextReturnMsg(String nextReturnMsg) {
        this.nextReturnMsg = nextReturnMsg;
    }
    public Date getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    public String getBillFrom() {
        return billFrom;
    }
    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }
    public String getErpId() {
        return erpId;
    }
    public void setErpId(String erpId) {
        this.erpId = erpId;
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
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getSignatureCode() {
        return signatureCode;
    }

    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }
}
