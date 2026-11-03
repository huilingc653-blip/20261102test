package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("凭证兑付记录VO")
public class ScfVoucherRedemptionVO implements Serializable, IBusinessKey {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("应付凭证ID")
    private String voucherId;
    @ApiModelProperty("开立单号")
    private String voucherNo;
    @ApiModelProperty("债权凭证编号")
    @BtCompositeSearch(fieldName = "v.voucher_code")
    private String voucherCode;
    @ApiModelProperty("兑付单号")
    @BtCompositeSearch(fieldName = "c.cash_no")
    private String cashNo;
    @ApiModelProperty("平台")
    @BtCompositeSearch(fieldName = "p.platform_name")
    private String platformName;

    @ApiModelProperty("平台id")
    @BtCompositeSearch(fieldName = "c.platform_id")
    private String platformId;
    @ApiModelProperty("融资产品")
    @BtCompositeSearch(fieldName = "c.product_id")

    private String productId;
    @ApiModelProperty("产品名称")
    @BtCompositeSearch(fieldName = "pr.product_name")
    private String productName;
    @ApiModelProperty("开立单位")
    @BtCompositeSearch(fieldName = "c.corp_id")
    private String corpId;
    @ApiModelProperty("开立单位名称")
    private String corpName;
    @ApiModelProperty("开立日期")
    @BtCompositeSearch(fieldName = "c.open_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date openDate;
    @ApiModelProperty("到期日")
    @BtCompositeSearch(fieldName = "c.expire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    @ApiModelProperty("核心企业")
    private String coreCorp;
    @ApiModelProperty("核心企业名称")
    private String coreCorpName;
    @ApiModelProperty("币别")
    private String curId;
    @ApiModelProperty("币种名称")
    @BtCompositeSearch(fieldName = "cur.cur_name")
    private String curName;
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherMoney;
    @ApiModelProperty("供应商ID")
    private String supplyId;
    @ApiModelProperty("供应商名称")
    private String supplyName;
    @ApiModelProperty("付款账号")
    private String payAccount;
    @ApiModelProperty("兑付金额")
    @BtCompositeSearch(fieldName = "c.cash_money")
    private BigDecimal cashMoney;
    @ApiModelProperty("付款日期")
    @BtCompositeSearch(fieldName = "c.pay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    @ApiModelProperty("收款方名称")
    private String receiveName;
    @ApiModelProperty("收款方账号")
    private String receiveAccount;
    @ApiModelProperty("收款开户行")
    private String receiveBank;
    /** 审批状态 */
    @BtCompositeSearch(fieldName = "c.approval_status")
    private String approvalStatus;
    @BtCompositeSearch(fieldName = "c.approval_status")
    private String approvalStatusName;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getCashNo() {
        return cashNo;
    }

    public void setCashNo(String cashNo) {
        this.cashNo = cashNo;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCoreCorp() {
        return coreCorp;
    }

    public void setCoreCorp(String coreCorp) {
        this.coreCorp = coreCorp;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(BigDecimal voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCoreCorpName() {
        return coreCorpName;
    }

    public void setCoreCorpName(String coreCorpName) {
        this.coreCorpName = coreCorpName;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
