package com.bytter.accountReceivable.docManage.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yangxd
 * @version 1.0
 * @Title:
 * @Description:
 * @date 2025-03-26
 */
@Entity
@Table(name = "DOC_MANAGE")
public class DocManage extends IdEntity implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 业务类型
     */
    private String batchBusType;
    /**
     * 单据来源
     */
    private String source;
    /**
     * 结算方式
     */
    private String voucherType;
    /**
     * 单据日期
     */
    private Date receiptDate;
    /**
     * 到期日期
     */
    private Date endDate;
    /**
     * 账龄起算日期
     */
    private Date agingDate;
    /**
     * 收款账号
     */
    private String payeeAcc;
    /**
     * 收款账户名
     */
    private String payeeAccName;
    /**
     * 收款账户开户行名称
     */
    private String payeeBankName;
    /**
     * 收款账户开户行号
     */
    private String payeeBankCode;
    /**
     * 应收金额
     */
    private BigDecimal receivableMoney;
    /**
     * 应收币种
     */
    private String curId;
    /**
     * 汇率类型
     */
    private String exRateType;
    /**
     * 折算金额
     */
    private BigDecimal convertPreMoney;
    /**
     * 付款账号
     */
    private String payerAccNo;
    /**
     * 付款账号名称
     */
    private String payerAccName;
    /**
     * 付款开户行名称
     */
    private String payerBankName;
    /**
     * 付款开户行行号
     */
    private String payerBankNo;
    /**
     * 客商名称
     */
    private String externalName;
    /**
     * 客商类型
     */
    private String externalType;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同金额
     */
    private BigDecimal contractMoney;
    /**
     * 合同日期
     */
    private Date contractDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 核销状态
     */
    private String writeOffStatus;
    /**
     * 核销类型
     */
    private String writeOffType;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    /**
     * 状态
     */
    private String status;
    /**
     * 租户id
     */
    private String tenantId;
    /**发票附件Id*/
    private String attachmentId;
    /**发票附件Id*/
    private String contractAttachmentId;
    /**变更作废原因标题*/
    private String reasonTitle;
    /**变更作废原因内容*/
    private String reasonContent;

    private String externalCode;
    /**变更作废状态，为空则为工作流的审批标准状态*/
    private String reasonStatus;

    @Column(name = "CONTRACT_ATTACHMENT_ID", nullable = false, length = 32)
    public String getContractAttachmentId() {
        return contractAttachmentId;
    }

    public void setContractAttachmentId(String contractAttachmentId) {
        this.contractAttachmentId = contractAttachmentId;
    }



    @Column(name ="REASON_STATUS",nullable=false)
    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }

    @Column(name ="EXTERNAL_CODE",nullable=false)
    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    @Column(name ="REASON_TITLE",nullable=false)
    public String getReasonTitle() {
        return reasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        this.reasonTitle = reasonTitle;
    }

    @Column(name ="REASON_CONTENT",nullable=false)
    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    @Column(name = "ATTACHMENT_ID", nullable = false, length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name = "TENANT_ID", nullable = false, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="CODE",nullable=false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Column(name ="BATCH_BUS_TYPE",nullable=false)
    public String getBatchBusType() {
        return batchBusType;
    }

    public void setBatchBusType(String batchBusType) {
        this.batchBusType = batchBusType;
    }
    @Column(name ="SOURCE")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    @Column(name ="VOUCHER_TYPE",nullable=false)
    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    @Column(name ="RECEIPT_DATE",nullable=false)
    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }
    @Column(name ="END_DATE",nullable=false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name ="AGING_DATE",nullable=false)
    public Date getAgingDate() {
        return agingDate;
    }

    public void setAgingDate(Date agingDate) {
        this.agingDate = agingDate;
    }
    @Column(name ="PAYEE_ACC")
    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    @Column(name ="PAYEE_ACC_NAME")
    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    @Column(name ="PAYEE_BANK_NAME")
    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }
    @Column(name ="PAYEE_BANK_CODE")
    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }
    @Column(name ="RECEIVABLE_MONEY",nullable=false)
    public BigDecimal getReceivableMoney() {
        return receivableMoney;
    }

    public void setReceivableMoney(BigDecimal receivableMoney) {
        this.receivableMoney = receivableMoney;
    }
    @Column(name ="CUR_ID",nullable=false)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="EXRATE_TYPE")
    public String getExRateType() {
        return exRateType;
    }

    public void setExRateType(String exRateType) {
        this.exRateType = exRateType;
    }
    @Column(name ="CONVERT_PRE_MONEY")
    public BigDecimal getConvertPreMoney() {
        return convertPreMoney;
    }

    public void setConvertPreMoney(BigDecimal convertPreMoney) {
        this.convertPreMoney = convertPreMoney;
    }
    @Column(name ="PAYER_ACC_NO")
    public String getPayerAccNo() {
        return payerAccNo;
    }

    public void setPayerAccNo(String payerAccNo) {
        this.payerAccNo = payerAccNo;
    }
    @Column(name ="PAYER_ACC_NAME")
    public String getPayerAccName() {
        return payerAccName;
    }

    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }
    @Column(name ="PAYER_BANK_NAME")
    public String getPayerBankName() {
        return payerBankName;
    }

    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    @Column(name ="PAYER_BANK_NO")
    public String getPayerBankNo() {
        return payerBankNo;
    }

    public void setPayerBankNo(String payerBankNo) {
        this.payerBankNo = payerBankNo;
    }
    @Column(name ="EXTERNAL_NAME",nullable=false)
    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
    @Column(name ="EXTERNAL_TYPE",nullable=false)
    public String getExternalType() {
        return externalType;
    }

    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }
    @Column(name ="CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name ="CONTRACT_NAME")
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    @Column(name ="CONTRACT_MONEY")
    public BigDecimal getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(BigDecimal contractMoney) {
        this.contractMoney = contractMoney;
    }
    @Column(name ="CONTRACT_DATE")
    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="WRITE_OFF_STATUS")
    public String getWriteOffStatus() {
        return writeOffStatus;
    }

    public void setWriteOffStatus(String writeOffStatus) {
        this.writeOffStatus = writeOffStatus;
    }

    @Column(name ="WRITE_OFF_TYPE")
    public String getWriteOffType() {
        return writeOffType;
    }

    public void setWriteOffType(String writeOffType) {
        this.writeOffType = writeOffType;
    }

    @Column(name ="STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name ="WRITE_OFF_BAL")
    public BigDecimal getWriteOffBal() {
        return writeOffBal;
    }

    public void setWriteOffBal(BigDecimal writeOffBal) {
        this.writeOffBal = writeOffBal;
    }
}
