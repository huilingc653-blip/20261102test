package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 资产证券化到账表
 * @date 2022-08-22
 */
@Entity
@Table(name = "CMS_SECURITY_TO_ACCOUNT", schema = "")
public class CmsSecurityToAccount extends IdEntity implements java.io.Serializable {
    /**
     * 证券发行编号
     */
    private String securityIssueCode;
    /**
     * 币种
     */
    private String curId;
    /**
     * 单位
     */
    private String corpId;
    /**
     * 到账日期
     */
    private Date toAccountDate;
    /**
     * 本次到账金额
     */
    private BigDecimal toAccountMoney;
    /**
     * 收款账号
     */
    private String payee;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "SECURITY_ISSUE_CODE")
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "TO_ACCOUNT_DATE")
    public Date getToAccountDate() {
        return toAccountDate;
    }
    public void setToAccountDate(Date toAccountDate) {
        this.toAccountDate = toAccountDate;
    }
    @Column(name = "TO_ACCOUNT_MONEY")
    public BigDecimal getToAccountMoney() {
        return toAccountMoney;
    }
    public void setToAccountMoney(BigDecimal toAccountMoney) {
        this.toAccountMoney = toAccountMoney;
    }
    @Column(name = "PAYEE")
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    @Column(name = "ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
