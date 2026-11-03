package com.bytter.finance.domain.equity.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 股权融资缴付到账表vo
 * @date 2022-06-20
 */
public class CmsEquityPayTheVO implements Serializable, IBusinessKey {
    private String id;
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
    @BtCompositeSearch(fieldName = "p.EQUITY_CODE")
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
    @BtCompositeSearch(fieldName = "p.INV_WAY")
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
//    @Currency(inject = "payCurName")
    private String payCurId;
    /**
     * 缴付币种名称
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
     * 汇总损益
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
    private String assetName;

    /**
     * 缴付/过户标识
     */
    private String payOrCh;
    /**
     * 状态
     */
    private String status;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getPlanDetailId() {
        return planDetailId;
    }
    public void setPlanDetailId(String planDetailId) {
        this.planDetailId = planDetailId;
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
    public BigDecimal getRePayMoney() {
        return rePayMoney;
    }
    public void setRePayMoney(BigDecimal rePayMoney) {
        this.rePayMoney = rePayMoney;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getbankNo() {
        return bankNo;
    }
    public void setbankNo(String bankNo) {
        this.bankNo = bankNo;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public String getPayCurId() {
        return payCurId;
    }
    public void setPayCurId(String payCurId) {
        this.payCurId = payCurId;
    }
    public String getPayCurName() {
        return payCurName;
    }
    public void setPayCurName(String payCurName) {
        this.payCurName = payCurName;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    public BigDecimal getNeedPayMoney() {
        return needPayMoney;
    }
    public void setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }
    public BigDecimal getProfitAndLoss() {
        return profitAndLoss;
    }
    public void setProfitAndLoss(BigDecimal profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getAssName() {
        return assName;
    }
    public void setAssName(String assName) {
        this.assName = assName;
    }
    public String getPayOrCh() {
        return payOrCh;
    }
    public void setPayOrCh(String payOrCh) {
        this.payOrCh = payOrCh;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
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

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}
