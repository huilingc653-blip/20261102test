package com.bytter.supply.domain.payable.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ScfPayableVoucherExcelVO extends ExcelModel implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 8558494688624410263L;

    /**
     * 债权凭证编码
     */

    @Excel(name = "*债权凭证编码")
    @ApiModelProperty("债权凭证编码")
    @NotNull(message = "债权凭证编码不能为空")
    private String voucherCode;

    @ApiModelProperty("单位ID")
    private String corpId;

    @Excel(name = "*单位名称")
    @NotNull(message = "单位名称不能为空")
    @ApiModelProperty("单位名称")
    private String corpName;

    @ApiModelProperty("平台ID")
    private String platformId;

    @Excel(name = "*平台名称")
    @NotNull(message = "平台名称不能为空")
    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("产品ID")
    private String productId;

    @Excel(name = "*产品名称")
    @NotNull(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("核心企业ID")
    private String coreCorp;

    @Excel(name = "核心企业名称")
//    @NotNull(message = "核心企业名称不能为空")
    @ApiModelProperty("核心企业名称")
    private String coreCorpName;

    @ApiModelProperty("币种ID")
    private String curId;

    @Excel(name = "*币种名称")
    @NotNull(message = "币种名称不能为空")
    @ApiModelProperty("币种名称")
    private String curName;

    @Excel(name = "*凭证金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NotNull(message = "凭证金额不能为空")
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherMoney;

    @ApiModelProperty("供应商ID")
    private String supplyId;

    @Excel(name = "供应商名称")
    @ApiModelProperty("供应商名称")
    private String supplyName;

    @Excel(name = "收款账户")
    @ApiModelProperty("收款账户")
    private String receiveAccount;

    @Excel(name = "收款银行")
    @ApiModelProperty("收款银行")
    private String receiveBank;

    @Excel(name = "*线上标识", replace = {"线下_0", "线上_1"})
    @NotNull(message = "线上标识不能为空")
    @ApiModelProperty("线上标识(0=线下,1=线上)")
    private String onlineSign;

    @Excel(name = "*开立日期", format = "yyyy-MM-dd")
    @NotNull(message = "开立日期不能为空")
    @ApiModelProperty("开立日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date openDate;

    @Excel(name = "*到期日期", format = "yyyy-MM-dd")
    @NotNull(message = "到期日期不能为空")
    @ApiModelProperty("到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    @Excel(name = "额度编码")
    @ApiModelProperty("额度编码")
    private String quotaCode;

    @Excel(name = "额度名称")
    @ApiModelProperty("额度名称")
    private String quotaName;

    @Excel(name = "额度余额")
    @ApiModelProperty("额度余额")
    private BigDecimal quotaBalance;

    @Excel(name = "合同编码")
    @ApiModelProperty("合同编码")
    private String contractCode;

    @Excel(name = "合同名称")
    @ApiModelProperty("合同名称")
    private String contractName;

    @Excel(name = "合同金额")
    @ApiModelProperty("合同金额")
    private BigDecimal contractMoney;

    @Excel(name = "合同日期", format = "yyyy-MM-dd")
    @ApiModelProperty("合同日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractDate;

    @Excel(name = "*融资付息方式", replace = {"卖方付息_1", "买方付息_2"})
    @NotNull(message = "融资付息方式不能为空")
    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    private String interestMode;

    @Excel(name = "承诺付款日", format = "yyyy-MM-dd")
    @ApiModelProperty("承诺付款日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date promisePayDate;

    @Excel(name = "供应商统一社会信用代码")
    @ApiModelProperty("供应商统一社会信用代码")
    private String supplyCreditCode;

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public String getBeanName() {
        return "scfPayableVoucherExcelService";
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
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

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCoreCorp() {
        return coreCorp;
    }

    public void setCoreCorp(String coreCorp) {
        this.coreCorp = coreCorp;
    }

    public String getCoreCorpName() {
        return coreCorpName;
    }

    public void setCoreCorpName(String coreCorpName) {
        this.coreCorpName = coreCorpName;
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

    public String getOnlineSign() {
        return onlineSign;
    }

    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
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

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public BigDecimal getQuotaBalance() {
        return quotaBalance;
    }

    public void setQuotaBalance(BigDecimal quotaBalance) {
        this.quotaBalance = quotaBalance;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public BigDecimal getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(BigDecimal contractMoney) {
        this.contractMoney = contractMoney;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getInterestMode() {
        return interestMode;
    }

    public void setInterestMode(String interestMode) {
        this.interestMode = interestMode;
    }

    public Date getPromisePayDate() {
        return promisePayDate;
    }

    public void setPromisePayDate(Date promisePayDate) {
        this.promisePayDate = promisePayDate;
    }

    public String getSupplyCreditCode() {
        return supplyCreditCode;
    }

    public void setSupplyCreditCode(String supplyCreditCode) {
        this.supplyCreditCode = supplyCreditCode;
    }
}
