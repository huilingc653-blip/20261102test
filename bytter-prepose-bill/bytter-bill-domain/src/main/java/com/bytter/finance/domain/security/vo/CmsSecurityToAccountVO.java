package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 资产证券化到账表
 *
 * @author tongqr
 */
public class CmsSecurityToAccountVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 证券发行编号
     */
    @BtCompositeSearch(fieldName = "toa.SECURITY_ISSUE_CODE")
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Date getToAccountDate() {
        return toAccountDate;
    }
    public void setToAccountDate(Date toAccountDate) {
        this.toAccountDate = toAccountDate;
    }
    public BigDecimal getToAccountMoney() {
        return toAccountMoney;
    }
    public void setToAccountMoney(BigDecimal toAccountMoney) {
        this.toAccountMoney = toAccountMoney;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    @Override
    public String getBusinessKey() {
        return id;
    }
}
