package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 多级单位余额核算-单位归集流水维护表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BisCorpPoolDtlVO {
    @BtCompositeSearch(fieldName = "t.id")
    private String id;
    @BtCompositeSearch(fieldName = "t.id")
    private String dtlId;

    /**业务单号*/
    @BtCompositeSearch(fieldName = "t.acc_No")
    private String accNo;

    /**单位ID*/
    @BtCompositeSearch(fieldName = "acc.corp_Id")
    private String corpId;
    @BtCompositeSearch(fieldName = "c.name")
    private String corpName;

    /**本方账号*/
    @BtCompositeSearch(fieldName = "t.bank_Acc")
    private String bankAcc;

    /**本方账户名*/
    @BtCompositeSearch(fieldName = "t.acc_Name")
    private String accName;

    /**本方账户开户行*/
    @BtCompositeSearch(fieldName = "t.bank_Name")
    private String bankName;

    /**接口代码*/
    @BtCompositeSearch(fieldName = "t.bif_Code")
    private String bifCode;
    @BtCompositeSearch(fieldName = "bif.bif_Name")
    private String bifName;

    /**对方账号*/
    @BtCompositeSearch(fieldName = "t.opp_Acc_No")
    private String oppAccNo;

    /**对方账户名*/
    @BtCompositeSearch(fieldName = "t.opp_Acc_Name")
    private String oppAccName;

    /**对方账户开户行*/
    @BtCompositeSearch(fieldName = "t.opp_Acc_Bank")
    private String oppAccBank;

    /**借贷标志*/
    @BtCompositeSearch(fieldName = "t.cd_Sign")
    private String cdSign;

    /**红蓝标志 1-红 2-蓝*/
    @BtCompositeSearch(fieldName = "t.rb_Sign")
    private String rbSign;

    /**交易金额*/
    @NumberFormat(pattern = "#,##0.00")
    @BtCompositeSearch(fieldName = "t.amt")
    private BigDecimal amt;
    @NumberFormat(pattern = "#,##0.00")
    @BtCompositeSearch(fieldName="t.amt")
    private BigDecimal amtIn;
    @BtCompositeSearch(fieldName="t.amt")
    private BigDecimal amtOut;

    /**余额*/
    @BtCompositeSearch(fieldName = "t.bal")
    private BigDecimal bal;

    /**红蓝标志 1-红 2-蓝*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private String curId;
    @BtCompositeSearch(fieldName = "cur.cur_Name")
    private String curName;

    /**用途*/
    @BtCompositeSearch(fieldName = "t.uses")
    private String uses;

    /**附言*/
    @BtCompositeSearch(fieldName = "t.postscript")
    private String postscript;

    /**红蓝标志 1-红 2-蓝*/
    @BtCompositeSearch(fieldName = "t.remark")
    private String remark;

    /**交易时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @BtCompositeSearch(fieldName = "t.trans_Time")
    private Date transTime;

    /**企业流水号*/
    @BtCompositeSearch(fieldName = "t.voucher_No")
    private String voucherNo;

    /**银行流水号*/
    @BtCompositeSearch(fieldName = "t.bank_Serial_No")
    private String bankSerialNo;

    /**红蓝标志 1-红 2-蓝*/
    @BtCompositeSearch(fieldName = "t.return_Time")
    private Date returnTime;

    /**类型 1-归集 2-下拨 3-其他*/
    @BtCompositeSearch(fieldName = "t.pool_Type")
    private String poolType;
    private String poolTypeName;

    /**认领标志*/
    @BtCompositeSearch(fieldName = "t.pool_Blend_Sign")
    private String poolBlendSign;

    /**创建人登录名称*/
    private String createBy;

    /**创建日期*/
    private Date createDate;

    /**更新人登录名称*/
    private String updateBy;

    /**更新日期*/
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDtlId() {
        return dtlId;
    }

    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
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

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
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

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getRbSign() {
        return rbSign;
    }

    public void setRbSign(String rbSign) {
        this.rbSign = rbSign;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getAmtIn() {
        return amtIn;
    }

    public void setAmtIn(BigDecimal amtIn) {
        this.amtIn = amtIn;
    }

    public BigDecimal getAmtOut() {
        return amtOut;
    }

    public void setAmtOut(BigDecimal amtOut) {
        this.amtOut = amtOut;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
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

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
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

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getPoolTypeName() {
        return poolTypeName;
    }

    public void setPoolTypeName(String poolTypeName) {
        this.poolTypeName = poolTypeName;
    }

    public String getPoolBlendSign() {
        return poolBlendSign;
    }

    public void setPoolBlendSign(String poolBlendSign) {
        this.poolBlendSign = poolBlendSign;
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
}
