package com.bytter.basic.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.util.Date;

/**
 * @ClassName BtInputBankSwiftVO
 * @Author zx
 * @Description 境内银行Swift Code管理
 * @Date 2026/1/21 14:05
 */
public class BtInputBankSwiftVO implements java.io.Serializable{
    private String id;

    /**
     * 开户行行号
     */
    @BtCompositeSearch(fieldName = "b.BANK_CODE")
    private String bankCode;

    /**
     * 开户行名称
     */
    @BtCompositeSearch(fieldName = "b.BANK_NAME")
    private String bankName;

    /**
     * swiftCode
     */
    @BtCompositeSearch(fieldName = "b.SWIFT_CODE")
    private String swiftCode;

    /**
     * 创建人登录名称
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新人登录名称
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
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
