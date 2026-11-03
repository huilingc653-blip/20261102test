package com.bytter.credit.domain.entity;

import com.bytter.core.base.entity.IdEntity;
import com.bytter.credit.enums.RatMessageEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 授信合作银行维护表实体
 *
 * @author fangcf
 */
@Entity
@Table(name = "RAT_CREDIT_COOPERATION_BANK", schema = "")
@SuppressWarnings("serial")
public class RatCreditCooperationBank extends IdEntity implements java.io.Serializable {

    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 授信银行合作情况：0-未合作、1-已合作、2-计划合作
     */
    private String cooperationType;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;

    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "COOPERATION_TYPE")
    public String getCooperationType() {
        return cooperationType;
    }

    public void setCooperationType(String cooperationType) {
        this.cooperationType = cooperationType;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
