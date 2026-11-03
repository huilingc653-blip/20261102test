package com.bytter.accountReceivable.docManage.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Entity
@Table(name = "DOC_INVOICE")
public class InvoiceDoc extends IdEntity implements Serializable {
    /**发票代码*/
    private String code;
    /**发票类型*/
    private String type;
    /**发票编号*/
    private String number;
    /**票面金额*/
    private BigDecimal parAmt;
    /**不含税金额*/
    private BigDecimal taxincAmt;
    /**校验码*/
    private String checkCode;
    /**开票日期*/
    private Date draDate;
    /**挂接业务Id*/
    private String  billId;
    /**挂接业务编码*/
    private String  billNo;
    /*** 创建人*/
    private String createBy;
    /*** 创建时间*/
    private Date createDate;
    /*** 更新人*/
    private String updateBy;
    /*** 更新时间*/
    private Date updateDate;
    /**
     * 租户id
     */
    private String tenantId;

    @Column(name = "tenant_Id", nullable = false, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name ="CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Column(name ="TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name ="NUMBER")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @Column(name ="PAR_AMT")
    public BigDecimal getParAmt() {
        return parAmt;
    }

    public void setParAmt(BigDecimal parAmt) {
        this.parAmt = parAmt;
    }

    @Column(name ="TAXINC_AMT")
    public BigDecimal getTaxincAmt() {
        return taxincAmt;
    }

    public void setTaxincAmt(BigDecimal taxincAmt) {
        this.taxincAmt = taxincAmt;
    }



    @Column(name ="CHECK_CODE")
    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
    @Column(name ="DRA_DATE")
    public Date getDraDate() {
        return draDate;
    }

    public void setDraDate(Date draDate) {
        this.draDate = draDate;
    }
    @Column(name ="BILL_ID")
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
    @Column(name ="BILL_NO")
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
}
