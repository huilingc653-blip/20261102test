package com.bytter.finance.domain.equity.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsEquityRepoVO implements Serializable, IBusinessKey {
    private String id;
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
    @BtCompositeSearch(fieldName = "r.EQUITY_CODE")
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
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRepoPlanId() {
        return repoPlanId;
    }
    public void setRepoPlanId(String repoPlanId) {
        this.repoPlanId = repoPlanId;
    }
    public String getRepoPlanDetailId() {
        return repoPlanDetailId;
    }
    public void setRepoPlanDetailId(String repoPlanDetailId) {
        this.repoPlanDetailId = repoPlanDetailId;
    }
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    public String getFinancingWay() {
        return financingWay;
    }
    public void setFinancingWay(String financingWay) {
        this.financingWay = financingWay;
    }
    public String getInvestor() {
        return investor;
    }
    public void setInvestor(String investor) {
        this.investor = investor;
    }
    public String getInvWay() {
        return invWay;
    }
    public void setInvWay(String invWay) {
        this.invWay = invWay;
    }
    public BigDecimal getInvMoney() {
        return invMoney;
    }
    public void setInvMoney(BigDecimal invMoney) {
        this.invMoney = invMoney;
    }
    public BigDecimal getRepoMoney() {
        return repoMoney;
    }
    public void setRepoMoney(BigDecimal repoMoney) {
        this.repoMoney = repoMoney;
    }
    public BigDecimal getReRepoMoney() {
        return reRepoMoney;
    }
    public void setReRepoMoney(BigDecimal reRepoMoney) {
        this.reRepoMoney = reRepoMoney;
    }
    public Date getRepoDate() {
        return repoDate;
    }
    public void setRepoDate(Date repoDate) {
        this.repoDate = repoDate;
    }
    public BigDecimal getThisRepoMoney() {
        return thisRepoMoney;
    }
    public void setThisRepoMoney(BigDecimal thisRepoMoney) {
        this.thisRepoMoney = thisRepoMoney;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
}
