package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.StringUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author huangx
 * @Description 销售回款分析表
 * @date 2019-3-26
 */
public class FbsSalesReturnAnalyseVO  implements Serializable {
    private String id;
    /**单位ID*/
    //@Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "fbs.corp_id")
    private String corpId;
    private String corpName;
    /**计划开始时间*/
    private Date beginDate;
    /**客商名称*/
    private String oppAccName;
    /**计划现金回款*/
    private BigDecimal planCashMoney;
    /**计划票据回款*/
    private BigDecimal planBillMoney;
    /**实际现金回款*/
    private BigDecimal factCashMoney;
    /**实际票据回款*/
    private BigDecimal factBillMoney;
    /**差异原因*/
    private String diffReason;
    /**状态*/
    private Integer analyseStatus;
    /**现金差异金额*/
    private BigDecimal diffCashMoney;
    /**票据差异金额*/
    private BigDecimal diffBillMoney;
    /**差异总额*/
    private BigDecimal diffMoney;
    /**差异比率*/
    private BigDecimal diffRatio;
    /**是否显示差异原因*/
    private  Boolean showDiffReason;
    /**回款方式*/
    private String settlType;
    /**计划金额*/
    private BigDecimal planMoney;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public BigDecimal getPlanCashMoney() {
        return planCashMoney;
    }
    public void setPlanCashMoney(BigDecimal planCashMoney) {
        this.planCashMoney = planCashMoney;
    }
    public BigDecimal getPlanBillMoney() {
        return planBillMoney;
    }
    public void setPlanBillMoney(BigDecimal planBillMoney) {
        this.planBillMoney = planBillMoney;
    }
    public BigDecimal getFactCashMoney() {
        return factCashMoney;
    }
    public void setFactCashMoney(BigDecimal factCashMoney) {
        this.factCashMoney = factCashMoney;
    }
    public BigDecimal getFactBillMoney() {
        return factBillMoney;
    }
    public void setFactBillMoney(BigDecimal factBillMoney) {
        this.factBillMoney = factBillMoney;
    }
    public String getDiffReason() {
        return diffReason;
    }
    public void setDiffReason(String diffReason) {
        this.diffReason = diffReason;
    }
    public Integer getAnalyseStatus() {
        return analyseStatus;
    }
    public void setAnalyseStatus(Integer analyseStatus) {
        this.analyseStatus = analyseStatus;
    }
    public BigDecimal getDiffCashMoney() {
        return diffCashMoney;
    }
    public void setDiffCashMoney(BigDecimal diffCashMoney) {
        this.diffCashMoney = diffCashMoney;
    }
    public BigDecimal getDiffBillMoney() {
        return diffBillMoney;
    }
    public void setDiffBillMoney(BigDecimal diffBillMoney) {
        this.diffBillMoney = diffBillMoney;
    }
    public BigDecimal getDiffMoney() {
        return diffMoney;
    }
    public void setDiffMoney(BigDecimal diffMoney) {
        this.diffMoney = diffMoney;
    }
    public BigDecimal getDiffRatio() {
        return diffRatio;
    }
    public void setDiffRatio(BigDecimal diffRatio) {
        this.diffRatio = diffRatio;
    }
    public Boolean getShowDiffReason() {
        return showDiffReason;
    }
    public void setShowDiffReason(Boolean showDiffReason) {
        this.showDiffReason = showDiffReason;
    }
    public String getSettlType() {
        return settlType;
    }
    public void setSettlType(String settlType) {
        this.settlType = settlType;
    }
    public BigDecimal getPlanMoney() {
        return planMoney;
    }
    public void setPlanMoney(BigDecimal planMoney) {
        this.planMoney = planMoney;
    }
}
