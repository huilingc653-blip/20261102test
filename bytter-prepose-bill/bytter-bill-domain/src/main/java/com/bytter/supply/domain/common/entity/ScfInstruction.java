package com.bytter.supply.domain.common.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @Title: ScfInstruction
 * @Description: 指令管理
 */
@Entity
@Table(name = "SCF_INSTRUCTION")
public class ScfInstruction extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 指令编码 */
    private String instrCode;

    /** 指令类型 */
    private String instrType;

    private String instrTypeName;

    /** 供应链平台ID */
    private String platformId;
    private String platformName;

    /** 融资产品 */
    private String productId;

    private String productName;

    /** 直联接口ID */
    private String directIfId;

    /** 债权凭证编码 */
    private String voucherCode;

    /** 关联编号（转让记录编号/融资单号等，用于精确关联对应业务表） */
    private String relateNo;

    /** 开立单位ID */
    private String corpId;
    /** 开立单位名称 */
    private String corpName;
    /** 币别ID */
    private String curId;

    private String curName;

    /** 指令金额 */
    private BigDecimal instrAmt;

    /** 指令日期 */
    private Date instrDate;

    /** 到期日 */
    private Date expireDate;

    /** 交易状态 */
    private String transStatus;

    /** 交易原因 */
    private String transReason;

    /** 应付应收标识(0为应付，1为应收) */
    private String sign;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 修改人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

    @Column(name = "INSTR_TYPE_NAME")
    public String getInstrTypeName() {
        return instrTypeName;
    }

    public void setInstrTypeName(String instrTypeName) {
        this.instrTypeName = instrTypeName;
    }
    @Column(name = "PLATFORM_NAME")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Column(name = "PRODUCT_NAME", length = 32)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "CORP_NAME", length = 32)
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name = "CUR_NAME", length = 32)
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "INSTR_CODE", length = 64)
    public String getInstrCode() { return instrCode; }
    public void setInstrCode(String instrCode) { this.instrCode = instrCode; }

    @Column(name = "INSTR_TYPE", length = 32)
    public String getInstrType() { return instrType; }
    public void setInstrType(String instrType) { this.instrType = instrType; }

    @Column(name = "PLATFORM_ID", length = 32)
    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }

    @Column(name = "PRODUCT_ID", length = 64)
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @Column(name = "DIRECT_IF_ID", length = 32)
    public String getDirectIfId() { return directIfId; }
    public void setDirectIfId(String directIfId) { this.directIfId = directIfId; }

    @Column(name = "VOUCHER_CODE", length = 64)
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    @Column(name = "RELATE_NO", length = 64)
    public String getRelateNo() { return relateNo; }
    public void setRelateNo(String relateNo) { this.relateNo = relateNo; }

    @Column(name = "CORP_ID", length = 32)
    public String getCorpId() { return corpId; }
    public void setCorpId(String corpId) { this.corpId = corpId; }

    @Column(name = "CUR_ID", length = 32)
    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    @Column(name = "INSTR_AMT")
    public BigDecimal getInstrAmt() { return instrAmt; }
    public void setInstrAmt(BigDecimal instrAmt) { this.instrAmt = instrAmt; }

    @Column(name = "INSTR_DATE")
    public Date getInstrDate() { return instrDate; }
    public void setInstrDate(Date instrDate) { this.instrDate = instrDate; }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }

    @Column(name = "TRANS_STATUS", length = 32)
    public String getTransStatus() { return transStatus; }
    public void setTransStatus(String transStatus) { this.transStatus = transStatus; }

    @Column(name = "TRANS_REASON", length = 500)
    public String getTransReason() { return transReason; }
    public void setTransReason(String transReason) { this.transReason = transReason; }

    @Column(name = "SIGN", length = 1)
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
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
