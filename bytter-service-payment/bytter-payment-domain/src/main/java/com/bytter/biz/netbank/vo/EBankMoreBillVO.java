package com.bytter.biz.netbank.vo;
import java.math.BigDecimal;
import java.util.Date;
public class EBankMoreBillVO {
    /*
      关联的流水号
       */
    private String dtlId;
    /*
   关联的付款单号
    */
    private String billId;
    /*
    付款单来源
     */
    private String isFrom;
    private Date  transTime;
    private String corpName;
    private String bankAcc;
    private String accName;
    private String bankName;
    private String cdSignName;
    private String curName;
    private BigDecimal amt;
    private BigDecimal bal;
    private String oppAccNo;
    private String oppAccName;
    private String oppAccBank;
    private String remark;
    private String postscript;
    private String uses;
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getIsFrom() {
        return isFrom;
    }
    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCdSignName() {
        return cdSignName;
    }
    public void setCdSignName(String cdSignName) {
        this.cdSignName = cdSignName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppAccBank() {
        return oppAccBank;
    }
    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getPostscript() {
        return postscript;
    }
    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }
    public String getUses() {
        return uses;
    }
    public void setUses(String uses) {
        this.uses = uses;
    }
}
