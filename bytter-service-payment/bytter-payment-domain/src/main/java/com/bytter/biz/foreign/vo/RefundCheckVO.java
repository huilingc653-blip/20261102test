package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundCheckVO implements Serializable, IBaseDataInject {
    private String id;
    /**交易时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @BtCompositeSearch(fieldName = "a.trans_Time")
    private Date transTime;
    /** 摘要*/
    @BtCompositeSearch(fieldName = "a.remark")
    private String remark;
    /*** 单位ID*/
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /*** 本方账号*/
    @BtCompositeSearch(fieldName = "a.bank_acc")
    private String bankAcc;
    /*** 本方账户名*/
    private String accName;
    /*** 本方账户开户行*/
    private String bankName;
    /*** 接口代码*/
    private String bifCode;
    /*** 借贷标志*/
    private String cdSign;
    private String cdSignName;
    /*** 红蓝标志*/
    private String rbSign;
    /*** 币别ID*/
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curId;
    private String curName;
    /**用途*/
    @BtCompositeSearch(fieldName = "a.uses")
    private String uses;
    /*** 附言*/
    private String postscript;
    /*** 对方账号*/
    @BtCompositeSearch(fieldName = "a.opp_acc_no")
    private String oppAccNo;
    /*** 对方账户名*/
    @BtCompositeSearch(fieldName = "a.opp_acc_name")
    private String oppAccName;
    /*** 对方账户开户行*/
    private String oppAccBank;
    /*** 交易金额*/
    @BtCompositeSearch(fieldName = "a.amt")
    private BigDecimal amt;
    /*** 余额*/
    private BigDecimal bal;
    /*** 计划状态*/
    private String fbsStatus;
    /*** 企业流水号*/
    private String voucherNo;
    /*** 银行流水号*/
    private String bankSerialNo;
    /*** 返回时间*/
    private Date returnTime;
    /*** 业务单号*/
    private String accNo;
    /*** 创建人登录名称*/
    private String createBy;
    /*** 创建日期*/
    private Date createDate;
    /*** 更新人登录名称*/
    private String updateBy;
    /*** 更新日期*/
    private Date updateDate;
    /*** 银行接口导出标识 */
    private String importSign;
    /*** 导出日期*/
    private Date importDate;
    /**资金池主体**/
    @BtCompositeSearch(fieldName = "c.id")
    private String poolId;
    /**记账状态**/
    private String tallySign;
    /**流水记账关联表Id**/
    private String accDtlId;
    /*** 付款方式*/
    private String settlType;
    /**
     * 备注
     */
    private String rmk;

    /**
     *原单处理方式 1-不处理原单、2-生成新单、3-打回原单
     */
    private String refundBlendType;
    private String refundBlendTypeName;

    /**
     * 业务单id
     */
    private String billId;
    /**
     * 合并后单据ID
     */
    private String mergeId;
    /**
     * 拆分前单据ID
     */
    private String splitId;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 业务类型
     */
    private String busType;

    /**
     * 票据认领流水id
     */
    private String claimId;

    /**
     * 项目信息
     */
    private String projectRemark;

    /**
     * 电子回单勾兑标志 0-未勾兑，1-已勾兑
     */
    @BtCompositeSearch(fieldName = "t.RECEIPT_BLEND_SIGN")
    @Dict(dictGroup = "paymentblending_status",inject = "receiptBlendSignName")
    private String receiptBlendSign;
    private String receiptBlendSignName;

    /**
     * 关联电子回单id
     */
    private String receiptId;

    /**现金流项目*/
    private String cashItem;
    private String cashItemName;

    /**
     * 付款明细勾兑标志 0-未勾兑，1-已勾兑
     */
    @BtCompositeSearch(fieldName = "a.BLEND_SIGN")
    @Dict(dictGroup = "paymentblending_status",inject = "blendSignName")
    private String blendSign;
    private String blendSignName;

    /**
     * 勾对日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentBlendingDate;
    /**
     * 退款勾对-勾对规则
     */
    private String refundCheckRule;

    /**
     * 操作类型 0-手工勾对 1-勾对确认
     */
    private String opType;

    /**现金流ID*/
    private String cashFlowId;
    /**流水号*/
    private String billNo;
    /**交易ID*/
    private String tranBillId;
    private String tranBillNo;
    /**结算日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date settleDate;
    /**金额*/
    private BigDecimal payMoney;
    /**主体单位银行账户*/
    private String corpBankAcc;
    private String corpAccName;
    /**交易对手银行账号*/
    private String cotpBankAcc;
    private String cotpAccName;

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getRefundBlendType() {
        return refundBlendType;
    }

    public void setRefundBlendType(String refundBlendType) {
        this.refundBlendType = refundBlendType;
    }

    public String getRefundBlendTypeName() {
        return refundBlendTypeName;
    }

    public void setRefundBlendTypeName(String refundBlendTypeName) {
        this.refundBlendTypeName = refundBlendTypeName;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getSettlType() {
        return settlType;
    }

    public void setSettlType(String settlType) {
        this.settlType = settlType;
    }

    public String getAccDtlId() {
        return accDtlId;
    }

    public void setAccDtlId(String accDtlId) {
        this.accDtlId = accDtlId;
    }

    public String getTallySign() {
        return tallySign;
    }

    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
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

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
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

    public String getImportSign() {
        return importSign;
    }

    public void setImportSign(String importSign) {
        this.importSign = importSign;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getFbsStatus() {
        return fbsStatus;
    }

    public void setFbsStatus(String fbsStatus) {
        this.fbsStatus = fbsStatus;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getCdSignName() {
        return cdSignName;
    }

    public void setCdSignName(String cdSignName) {
        this.cdSignName = cdSignName;
    }

    public String getProjectRemark() {
        return projectRemark;
    }

    public void setProjectRemark(String projectRemark) {
        this.projectRemark = projectRemark;
    }

    public String getReceiptBlendSign() {
        return receiptBlendSign;
    }

    public void setReceiptBlendSign(String receiptBlendSign) {
        this.receiptBlendSign = receiptBlendSign;
    }

    public String getReceiptBlendSignName() {
        return receiptBlendSignName;
    }

    public void setReceiptBlendSignName(String receiptBlendSignName) {
        this.receiptBlendSignName = receiptBlendSignName;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    public String getCashItemName() {
        return cashItemName;
    }

    public void setCashItemName(String cashItemName) {
        this.cashItemName = cashItemName;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    public String getBlendSignName() {
        return blendSignName;
    }

    public void setBlendSignName(String blendSignName) {
        this.blendSignName = blendSignName;
    }

    public Date getPaymentBlendingDate() {
        return paymentBlendingDate;
    }

    public void setPaymentBlendingDate(Date paymentBlendingDate) {
        this.paymentBlendingDate = paymentBlendingDate;
    }

    public String getRefundCheckRule() {
        return refundCheckRule;
    }

    public void setRefundCheckRule(String refundCheckRule) {
        this.refundCheckRule = refundCheckRule;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getCashFlowId() {
        return cashFlowId;
    }

    public void setCashFlowId(String cashFlowId) {
        this.cashFlowId = cashFlowId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTranBillId() {
        return tranBillId;
    }

    public void setTranBillId(String tranBillId) {
        this.tranBillId = tranBillId;
    }

    public String getTranBillNo() {
        return tranBillNo;
    }

    public void setTranBillNo(String tranBillNo) {
        this.tranBillNo = tranBillNo;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    public String getCorpAccName() {
        return corpAccName;
    }

    public void setCorpAccName(String corpAccName) {
        this.corpAccName = corpAccName;
    }

    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    public String getCotpAccName() {
        return cotpAccName;
    }

    public void setCotpAccName(String cotpAccName) {
        this.cotpAccName = cotpAccName;
    }
}
