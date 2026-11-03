package com.bytter.finance.domain.equity.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 股权融资回购计划明细表
 * @date 2022-06-29
 */
@Entity
@Table(name = "CMS_EQUITY_REPO_PLAN_DETAIL", schema = "")
public class CmsEquityRepoPlanDetail extends IdEntity implements java.io.Serializable {
    /**
     * 融资编号
     */
    private String equityCode;
    /**
     * 回购计划主表id
     */
    private String repoPlanId;
    /**
     * 回购日期
     */
    private Date repoDate;
    /**
     * 本次回购金额
     */
    private BigDecimal repoMoney;
    /**
     * 剩余回购金额
     */
    private BigDecimal reRepoMoney;
    /**
     * 本次占比
     */
    private BigDecimal accounted;
    /**
     * 是否付款
     */
    private String isPay;
    /**
     * 是否有效
     */
    private String validSign;
    /**
     * 登记/修改标识 0登记、1修改
     */
    private String addOrUpdate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
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
    @Column(name = "EQUITY_CODE")
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    @Column(name = "REPO_PLAN_ID")
    public String getRepoPlanId() {
        return repoPlanId;
    }
    public void setRepoPlanId(String repoPlanId) {
        this.repoPlanId = repoPlanId;
    }
    @Column(name = "REPO_DATE")
    public Date getRepoDate() {
        return repoDate;
    }
    public void setRepoDate(Date repoDate) {
        this.repoDate = repoDate;
    }
    @Column(name = "REPO_MONEY")
    public BigDecimal getRepoMoney() {
        return repoMoney;
    }
    public void setRepoMoney(BigDecimal repoMoney) {
        this.repoMoney = repoMoney;
    }
    @Column(name = "RE_REPO_MONEY")
    public BigDecimal getReRepoMoney() {
        return reRepoMoney;
    }
    public void setReRepoMoney(BigDecimal reRepoMoney) {
        this.reRepoMoney = reRepoMoney;
    }
    @Column(name = "ACCOUNTED")
    public BigDecimal getAccounted() {
        return accounted;
    }
    public void setAccounted(BigDecimal accounted) {
        this.accounted = accounted;
    }
    @Column(name = "IS_PAY")
    public String getIsPay() {
        return isPay;
    }
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "ADD_OR_UPDATE")
    public String getAddOrUpdate() {
        return addOrUpdate;
    }
    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
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
