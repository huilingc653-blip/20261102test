package com.bytter.supply.domain.payable.dto;

import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.payable.vo.ScfVoucherCustomFieldVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("应付债权凭证数据DTO")
public class ScfPayableVoucherDataDTO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("单据编号")
    private String voucherNo;
    @ApiModelProperty("债权凭证编码")
    private String voucherCode;
    @ApiModelProperty("债权凭证模板ID")
    private String templateId;
    @ApiModelProperty("模板版本ID")
    private String versionId;
    @ApiModelProperty("平台ID")
    private String platformId;
    @ApiModelProperty("平台名称")
    private String platformName;
    @ApiModelProperty("产品ID")
    private String productId;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("企业ID")
    private String corpId;
    @ApiModelProperty("企业名称")
    private String corpName;
    @ApiModelProperty("开立日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date openDate;
    @ApiModelProperty("到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    @ApiModelProperty("核心企业")
    private String coreCorp;
    @ApiModelProperty("核心企业名称")
    private String coreCorpName;
    @ApiModelProperty("币种ID")
    private String curId;
    @ApiModelProperty("币种名称")
    private String curName;
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherMoney;
    @ApiModelProperty("供应商ID")
    private String supplyId;
    @ApiModelProperty("供应商名称")
    private String supplyName;
    @ApiModelProperty("收款账户")
    private String receiveAccount;
    @ApiModelProperty("收款银行")
    private String receiveBank;
    @ApiModelProperty("线上签章(0=线下,1=线上)")
    private String onlineSign;
    @ApiModelProperty("额度编码")
    private String quotaCode;
    @ApiModelProperty("额度名称")
    private String quotaName;
    @ApiModelProperty("额度余额")
    private BigDecimal quotaBalance;
    @ApiModelProperty("合同编码")
    private String contractCode;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同金额")
    private BigDecimal contractMoney;
    @ApiModelProperty("合同日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractDate;
    @ApiModelProperty("合同文件ID")
    private String contractFile;
    @ApiModelProperty("发票文件ID")
    private String invoiceFile;
    @ApiModelProperty("单据来源")
    private String voucherSource;
    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    private String interestMode;
    @ApiModelProperty("承诺付款日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date promisePayDate;
    @ApiModelProperty("供应商统一社会信用代码")
    private String supplyCreditCode;
    @ApiModelProperty("凭证业务状态")
    private String voucherStatus;
    @ApiModelProperty("审批状态")
    private String approvalStatus;
    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("修改人")
    private String updateBy;
    @ApiModelProperty("修改时间")
    private Date updateDate;
    @ApiModelProperty("保存标志(0=暂存,1=提交)")
    private String saveFlag;
    @ApiModelProperty("自定义字段列表")
    private List<ScfVoucherCustomFieldDTO> customFields;
    @ApiModelProperty("发票信息")
    private List<ScfInvoiceInfoDTO> invoiceInfo;

    public List<ScfInvoiceInfoDTO> getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(List<ScfInvoiceInfoDTO> invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getSaveFlag() {
        return saveFlag;
    }
    public void setSaveFlag(String saveFlag) {
        this.saveFlag = saveFlag;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
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

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public String getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public String getVoucherSource() {
        return voucherSource;
    }

    public void setVoucherSource(String voucherSource) {
        this.voucherSource = voucherSource;
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

    public List<ScfVoucherCustomFieldDTO> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<ScfVoucherCustomFieldDTO> customFields) {
        this.customFields = customFields;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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
