package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("应付债权凭证数据VO(含自定义字段)")
public class ScfPayableVoucherDataVO implements Serializable, IBusinessKey {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 单据编号
     */
    @ApiModelProperty("单据编号")
    @BtCompositeSearch(fieldName = "c.voucher_no")
    private String voucherNo;

    /**
     * 债权凭证编码
     */
    @BtCompositeSearch(fieldName = "c.voucher_code")
    @ApiModelProperty("债权凭证编码")
    private String voucherCode;
    @ApiModelProperty("债权凭证模板ID")
    private String templateId;
    @ApiModelProperty("模板版本ID")
    private String versionId;
    @ApiModelProperty("平台ID")
    @BtCompositeSearch(fieldName = "c.platform_id")
    private String platformId;
    @ApiModelProperty("平台名称")
    @BtCompositeSearch(fieldName = "p.platform_name")
    private String platformName;
    @ApiModelProperty("产品ID")
    @BtCompositeSearch(fieldName = "c.product_id")
    private String productId;
    @ApiModelProperty("产品名称")
    @BtCompositeSearch(fieldName = "pr.product_name")
    private String productName;
    @ApiModelProperty("企业ID")
    @BtCompositeSearch(fieldName = "c.corp_id")
    private String corpId;
    @ApiModelProperty("企业名称")
    @BtCompositeSearch(fieldName = "c.corp_name")
    private String corpName;
    @ApiModelProperty("开立日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "c.open_date")
    private Date openDate;
    @ApiModelProperty("到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "c.expire_date")
    private Date expireDate;
    @ApiModelProperty("核心企业")
    @BtCompositeSearch(fieldName = "c.core_corp")
    private String coreCorp;
    @ApiModelProperty("核心企业名称")
    private String coreCorpName;
    @ApiModelProperty("币种ID")
    @BtCompositeSearch(fieldName = "c.cur_id")
    private String curId;
    @ApiModelProperty("币种名称")
    @BtCompositeSearch(fieldName = "cur.cur_name")
    private String curName;
    @ApiModelProperty("凭证金额")
    @BtCompositeSearch(fieldName = "c.voucher_money")
    private BigDecimal voucherMoney;
    @ApiModelProperty("供应商ID")
    @BtCompositeSearch(fieldName = "c.supply_id")
    private String supplyId;
    @ApiModelProperty("供应商名称")
    @BtCompositeSearch(fieldName = "c.supply_name")
    private String supplyName;
    @ApiModelProperty("收款账户")
    private String receiveAccount;
    @ApiModelProperty("收款银行")
    private String receiveBank;
    @ApiModelProperty("线上签章(0=线下,1=线上)")
    @BtCompositeSearch(fieldName = "c.online_sign")
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
    @BtCompositeSearch(fieldName = "c.voucher_source")
    private String voucherSource;
    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    @BtCompositeSearch(fieldName = "c.interest_mode")
    private String interestMode;
    @ApiModelProperty("承诺付款日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "c.promise_pay_date")
    private Date promisePayDate;
    @ApiModelProperty("供应商统一社会信用代码")
    @BtCompositeSearch(fieldName = "c.supply_credit_code")
    private String supplyCreditCode;
    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("修改人")
    private String updateBy;
    @ApiModelProperty("修改时间")
    private Date updateDate;

    /** 凭证业务状态 */
    @ApiModelProperty("凭证业务状态")
    /** 凭证业务状态 */
    private String voucherStatus;
    private String voucherStatusName;

    /** 审批状态 */
    @ApiModelProperty("审批状态")
    private String approvalStatus;
    private String approvalStatusName;

    /** 关联付款单ID（付款单审批通过后写入） */
    @ApiModelProperty("关联付款单ID")
    private String payId;

    /** 关联付款单号（付款单审批通过后写入） */
    @ApiModelProperty("关联付款单号")
    private String payNo;

    /** 作废原因 */
    @ApiModelProperty("作废原因")
    private String cancelReason;

    /** 作废时间 */
    @ApiModelProperty("作废时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelDate;

    @ApiModelProperty("自定义字段列表")
    private List<ScfVoucherCustomFieldVO> customFields;

    @ApiModelProperty("发票信息列表")
    private List<ScfInvoiceInfoVO> invoiceInfoList;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherStatusName() {
        return voucherStatusName;
    }

    public void setVoucherStatusName(String voucherStatusName) {
        this.voucherStatusName = voucherStatusName;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
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

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public List<ScfInvoiceInfoVO> getInvoiceInfoList() {
        return invoiceInfoList;
    }

    public void setInvoiceInfoList(List<ScfInvoiceInfoVO> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
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

    public List<ScfVoucherCustomFieldVO> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<ScfVoucherCustomFieldVO> customFields) {
        this.customFields = customFields;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
