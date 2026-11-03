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
 * @Description: 股权融资缴付到账表
 * @date 2022-06-20
 */
@Entity
@Table(name = "CMS_EQUITY_PAY_THE", schema = "")
public class CmsEquityPayThe extends IdEntity implements java.io.Serializable {
    /**
     * 计划主表id
     */
    private String planId;
    /**
     * 计划明细表id
     */
    private String planDetailId;
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
     * 剩余缴付金额
     */
    private BigDecimal rePayMoney;
    /**
     * 验资账号
     */
    private String account;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 开户行行号
     */
    private String bankNo;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 缴付日期
     */
    private Date payDate;
    /**
     * 缴付币种
     */
    private String payCurId;
    /**
     * 缴付币种
     */
    private String payCurName;
    /**
     * 本次缴付金额
     */
    private BigDecimal payMoney;
    /**
     * 汇率
     */
    private BigDecimal rate;
    /**
     * 折合本位币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 本次应缴付金额
     */
    private BigDecimal needPayMoney;
    /**
     * 汇总损益
     */
    private BigDecimal profitAndLoss;
    /**
     * 备注
     */
    private String corpId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 资产名称
     */
    private String assName;
    /**
     * 缴付/过户标识
     */
    private String payOrCh;
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
    @Column(name = "PLAN_ID")
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    @Column(name = "PLAN_DETAIL_ID")
    public String getPlanDetailId() {
        return planDetailId;
    }
    public void setPlanDetailId(String planDetailId) {
        this.planDetailId = planDetailId;
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
    @Column(name = "RE_PAY_MONEY")
    public BigDecimal getRePayMoney() {
        return rePayMoney;
    }
    public void setRePayMoney(BigDecimal rePayMoney) {
        this.rePayMoney = rePayMoney;
    }
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Column(name = "ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name = "BANK_NO")
    public String getbankNo() {
        return bankNo;
    }
    public void setbankNo(String bankNo) {
        this.bankNo = bankNo;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "PAY_CUR_ID")
    public String getPayCurId() {
        return payCurId;
    }
    public void setPayCurId(String payCurId) {
        this.payCurId = payCurId;
    }
    @Column(name = "PAY_MONEY")
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "RMB_MONEY")
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    @Column(name = "NEED_PAY_MONEY")
    public BigDecimal getNeedPayMoney() {
        return needPayMoney;
    }
    public void setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }
    @Column(name = "PROFIT_AND_LOSS")
    public BigDecimal getProfitAndLoss() {
        return profitAndLoss;
    }
    public void setProfitAndLoss(BigDecimal profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "ASS_NAME")
    public String getAssName() {
        return assName;
    }
    public void setAssName(String assName) {
        this.assName = assName;
    }
    @Column(name = "PAY_OR_CH")
    public String getPayOrCh() {
        return payOrCh;
    }
    public void setPayOrCh(String payOrCh) {
        this.payOrCh = payOrCh;
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
    @Column(name = "PAY_CUR_NAME")
    public String getPayCurName() {
        return payCurName;
    }
    public void setPayCurName(String payCurName) {
        this.payCurName = payCurName;
    }
}
