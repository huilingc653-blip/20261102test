package com.bytter.finance.domain.equity.entity;
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
 * @Description: 股权融资回购表
 * @date 2022-06-27
 */
@Entity
@Table(name = "CMS_EQUITY_REPO", schema = "")
public class CmsEquityRepo extends IdEntity implements java.io.Serializable {
    /**
     * 回购计划主表id
     */
    private String repoPlanId;
    /**
     * 回购计划明细表id
     */
    private String repoPlanDetailId;
    /**
     * 融资编号
     */
    private String equityCode;
    /**
     * 融资方式
     */
    private String financingWay;
    /**
     * 出资人
     */
    private String investor;
    /**
     * 出资方式
     */
    private String invWay;
    /**
     * 出资金额
     */
    private BigDecimal invMoney;
    /**
     * 回购金额
     */
    private BigDecimal repoMoney;
    /**
     * 剩余回购金额
     */
    private BigDecimal reRepoMoney;
    /**
     * 回购日期
     */
    private Date repoDate;
    /**
     * 本次回购金额
     */
    private BigDecimal thisRepoMoney;
    /**
     * 单位
     */
    private String corpId;
    /**
     * 收款人
     */
    private String payer;
    /**
     * 收款账号
     */
    private String onlinePaymentAcc;
    /**
     * 收款账户名称
     */
    private String onlineAccName;
    /**
     * 收款开户行名称
     */
    private String onlineBankName;
    /**
     * 线上线下标识
     */
    private String onlineSign;
    /**
     * 用途
     */
    private String purpose;
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
    @Column(name = "REPO_PLAN_ID")
    public String getRepoPlanId() {
        return repoPlanId;
    }
    public void setRepoPlanId(String repoPlanId) {
        this.repoPlanId = repoPlanId;
    }
    @Column(name = "REPO_PLAN_DETAIL_ID")
    public String getRepoPlanDetailId() {
        return repoPlanDetailId;
    }
    public void setRepoPlanDetailId(String repoPlanDetailId) {
        this.repoPlanDetailId = repoPlanDetailId;
    }
    @Column(name = "EQUITY_CODE")
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    @Column(name = "FINANCING_WAY")
    public String getFinancingWay() {
        return financingWay;
    }
    public void setFinancingWay(String financingWay) {
        this.financingWay = financingWay;
    }
    @Column(name = "INVESTOR")
    public String getInvestor() {
        return investor;
    }
    public void setInvestor(String investor) {
        this.investor = investor;
    }
    @Column(name = "INV_WAY")
    public String getInvWay() {
        return invWay;
    }
    public void setInvWay(String invWay) {
        this.invWay = invWay;
    }
    @Column(name = "INV_MONEY")
    public BigDecimal getInvMoney() {
        return invMoney;
    }
    public void setInvMoney(BigDecimal invMoney) {
        this.invMoney = invMoney;
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
    @Column(name = "REPO_DATE")
    public Date getRepoDate() {
        return repoDate;
    }
    public void setRepoDate(Date repoDate) {
        this.repoDate = repoDate;
    }
    @Column(name = "THIS_REPO_MONEY")
    public BigDecimal getThisRepoMoney() {
        return thisRepoMoney;
    }
    public void setThisRepoMoney(BigDecimal thisRepoMoney) {
        this.thisRepoMoney = thisRepoMoney;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "PAYER")
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    @Column(name = "ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    @Column(name = "ONLINE_ACC_NAME")
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    @Column(name = "ONLINE_BANK_NAME")
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
