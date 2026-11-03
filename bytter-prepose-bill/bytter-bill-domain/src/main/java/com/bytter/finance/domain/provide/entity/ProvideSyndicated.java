package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 银团明细信息
 * @author onlineGenerator
 * @date 2022-07-21 15:15:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROVIDE_SYNDICATED", schema = "")
@SuppressWarnings("serial")
public class ProvideSyndicated extends IdEntity implements java.io.Serializable {
    /**
     * 借款主表id
     */
    private String directFinanceId;
    /**
     * 是否牵头行
     */
    private String leadingBank;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行开户行号
     */
    private String bankCode;
    /**
     * 承贷份额%
     */
    private BigDecimal commitmentShare;
    /**
     * 承贷金额
     */
    private BigDecimal commitmentMoney;
    /** 备注 */
    private String rmk;
    /** 创建人登录名称 */
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 状态
     */
    private Integer status;
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "LEADING_BANK")
    public String getLeadingBank() {
        return leadingBank;
    }
    public void setLeadingBank(String leadingBank) {
        this.leadingBank = leadingBank;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name = "COMMITMENT_SHARE")
    public BigDecimal getCommitmentShare() {
        return commitmentShare;
    }
    public void setCommitmentShare(BigDecimal commitmentShare) {
        this.commitmentShare = commitmentShare;
    }
    @Column(name = "COMMITMENT_MONEY")
    public BigDecimal getCommitmentMoney() {
        return commitmentMoney;
    }
    public void setCommitmentMoney(BigDecimal commitmentMoney) {
        this.commitmentMoney = commitmentMoney;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
