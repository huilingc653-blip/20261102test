package com.bytter.tally.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class BisAccDtlTallyVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 交易日期
     */
    @BtCompositeSearch(fieldName = "dtl.trans_Time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date transTime;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "acc.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 收支标识
     */
    private String cdSign;
    /**
     * 币别代码
     */
    @BtCompositeSearch(fieldName = "dtl.cur_Id")
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 本方账号ID
     */
    private String bankAccId;
    /**
     * 本方账号
     */
    @BtCompositeSearch(fieldName = "dtl.bank_Acc")
    private String bankAcc;
    /**
     * 本方账号开户行
     */
    private String bankName;
    /**
     * 本方账号名
     */
    @BtCompositeSearch(fieldName = "dtl.acc_Name")
    private String accName;
    /**
     * 本方内部账号
     */
    private String cntAccount;

    /**
     * 本方内部账号名称
     */
    private String cntAccName;

    /**
     * 对方内部账号
     */
    private String oppCntAccount;

    /**
     * 对方内部账号名称
     */
    private String oppCntAccName;

    /**
     * 对方账号
     */
    @BtCompositeSearch(fieldName = "dtl.opp_Acc_No")
    private String oppAccNo;
    /**
     * 对方账号开户行
     */
    @BtCompositeSearch(fieldName = "dtl.opp_Bank_Name")
    private String oppBankName;
    /**
     * 对方账户名
     */
    @BtCompositeSearch(fieldName = "dtl.opp_Acc_Name")
    private String oppAccName;
    /**
     * 摘要
     */
    @BtCompositeSearch(fieldName = "dtl.USES")
    private String purpose;
    /**
     * 金额
     */
    @BtCompositeSearch(fieldName = "dtl.amt")
    private java.math.BigDecimal money;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "dtl.estate")
    @Dict(dictGroup = "btNetChalk.estate", inject = "state")
    private String estate;
    private String state;
    /**
     * 记账员代码
     */
    private String operatorCode;
    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tallyDate;
    /**
     * 流水号
     */
    private String orderNo;
    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 复核员代码
     */
    private String checkerCode;
    /**
     * 复核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date checkTime;

    /**
     * 附言
     */
    @BtCompositeSearch(fieldName = "dtl.postscript")
    private String postscript;

    /**
     * 摘要
     */
    @BtCompositeSearch(fieldName = "dtl.remark")
    private String remark;

    /**
     * 凭证id
     */
    private String bookId;

    /**
     * 全局业务id
     */
    private String busGlobalId;

    /**
     * 创建时间
     */
    private java.util.Date createTime;

    /**
     * 创建用户代码
     */
    private String createBy;

    /**
     * 最后更新时间
     */
    private java.util.Date updateTime;

    /**
     * 最后更新用户代码
     */
    private String updateBy;

    /**
     * 自动记账标识
     **/
    private String autoTallySign;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.Date getTransTime() {
        return transTime;
    }

    public void setTransTime(java.util.Date transTime) {
        this.transTime = transTime;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getCntAccount() {
        return cntAccount;
    }

    public void setCntAccount(String cntAccount) {
        this.cntAccount = cntAccount;
    }

    public String getOppAccNo() {
        return oppAccNo;
    }

    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }

    public String getOppBankName() {
        return oppBankName;
    }

    public void setOppBankName(String oppBankName) {
        this.oppBankName = oppBankName;
    }

    public String getOppAccName() {
        return oppAccName;
    }

    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public java.math.BigDecimal getMoney() {
        return money;
    }

    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public java.util.Date getTallyDate() {
        return tallyDate;
    }

    public void setTallyDate(java.util.Date tallyDate) {
        this.tallyDate = tallyDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getCheckerCode() {
        return checkerCode;
    }

    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }

    public java.util.Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(java.util.Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getBusGlobalId() {
        return busGlobalId;
    }

    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getAutoTallySign() {
        return autoTallySign;
    }

    public void setAutoTallySign(String autoTallySign) {
        this.autoTallySign = autoTallySign;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCntAccName() {
        return cntAccName;
    }

    public void setCntAccName(String cntAccName) {
        this.cntAccName = cntAccName;
    }

    public String getOppCntAccount() {
        return oppCntAccount;
    }

    public void setOppCntAccount(String oppCntAccount) {
        this.oppCntAccount = oppCntAccount;
    }

    public String getOppCntAccName() {
        return oppCntAccName;
    }

    public void setOppCntAccName(String oppCntAccName) {
        this.oppCntAccName = oppCntAccName;
    }
}
