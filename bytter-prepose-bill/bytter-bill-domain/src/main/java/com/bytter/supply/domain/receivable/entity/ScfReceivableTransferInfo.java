package com.bytter.supply.domain.receivable.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SCF_RECEIVABLE_TRANSFER_INFO")
public class ScfReceivableTransferInfo extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转让记录编号 */
    private String transferNo;

    /**转让批次号*/
    private String transferBatchNo;

    /** 债权凭证编号 */
    private String voucherCode;

    /** 转让审批状态 */
    private String transferStatus;

    /** 转让标识: 1.一对多，凭证拆分转让多家企业;2.多对一,多张凭证转让一家企业 */
    private String transferFlag;

    /** 平台ID */
    private String platformId;

    /** 平台名称 */
    private String platformName;

    /** 融资产品ID */
    private String productId;

    /** 融资产品名称 */
    private String productName;

    /** 开立/转让单位 */
    private String issueTransferCompany;

    /** 开立/转让企业ID */
    private String issueTransferCompanyId;

    /** 币别ID */
    private String curId;

    /** 币别名称 */
    private String curName;

    /** 接收单位 */
    private String receiveCompany;

    /** 接收单位ID */
    private String receiveCompanyId;

    /** 接收方统一社会信用代码 */
    private String receiverCreditCode;

    /** 转让日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date transferDate;

    /** 转让金额 */
    private BigDecimal transferAmt;

    /** 用途 */
    private String purpose;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 修改人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

    @Column(name = "TRANSFER_NO")
    public String getTransferNo() { return transferNo; }
    public void setTransferNo(String transferNo) { this.transferNo = transferNo; }

    @Column(name = "TRANSFER_BATCH_NO")
    public String getTransferBatchNo() {
        return transferBatchNo;
    }

    public void setTransferBatchNo(String transferBatchNo) {
        this.transferBatchNo = transferBatchNo;
    }

    @Column(name = "VOUCHER_CODE")
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    @Column(name = "TRANSFER_STATUS")
    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String transferStatus) { this.transferStatus = transferStatus; }

    @Column(name = "TRANSFER_FLAG")
    public String getTransferFlag() { return transferFlag; }
    public void setTransferFlag(String transferFlag) { this.transferFlag = transferFlag; }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }

    @Column(name = "PLATFORM_NAME")
    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    @Column(name = "PRODUCT_ID")
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    @Column(name = "ISSUE_TRANSFER_COMPANY")
    public String getIssueTransferCompany() { return issueTransferCompany; }
    public void setIssueTransferCompany(String issueTransferCompany) { this.issueTransferCompany = issueTransferCompany; }

    @Column(name = "ISSUE_TRANSFER_COMPANY_ID")
    public String getIssueTransferCompanyId() { return issueTransferCompanyId; }
    public void setIssueTransferCompanyId(String issueTransferCompanyId) { this.issueTransferCompanyId = issueTransferCompanyId; }

    @Column(name = "CUR_ID")
    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    @Column(name = "CUR_NAME")
    public String getCurName() { return curName; }
    public void setCurName(String curName) { this.curName = curName; }

    @Column(name = "RECEIVE_COMPANY")
    public String getReceiveCompany() { return receiveCompany; }
    public void setReceiveCompany(String receiveCompany) { this.receiveCompany = receiveCompany; }

    @Column(name = "RECEIVE_COMPANY_ID")
    public String getReceiveCompanyId() { return receiveCompanyId; }
    public void setReceiveCompanyId(String receiveCompanyId) { this.receiveCompanyId = receiveCompanyId; }

    @Column(name = "RECEIVER_CREDIT_CODE")
    public String getReceiverCreditCode() { return receiverCreditCode; }
    public void setReceiverCreditCode(String receiverCreditCode) { this.receiverCreditCode = receiverCreditCode; }

    @Column(name = "TRANSFER_DATE")
    public Date getTransferDate() { return transferDate; }
    public void setTransferDate(Date transferDate) { this.transferDate = transferDate; }

    @Column(name = "TRANSFER_AMT")
    public BigDecimal getTransferAmt() { return transferAmt; }
    public void setTransferAmt(BigDecimal transferAmt) { this.transferAmt = transferAmt; }

    @Column(name = "PURPOSE")
    public String getPurpose() { return purpose; }
    public void setPurpose(String usage) { this.purpose = usage; }

    @Column(name = "REMARK")
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    @Column(name = "CREATE_BY")
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
