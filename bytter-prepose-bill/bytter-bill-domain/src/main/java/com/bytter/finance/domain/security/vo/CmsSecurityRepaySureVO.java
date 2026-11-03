package com.bytter.finance.domain.security.vo;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsSecurityRepaySureVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 计划主表id
     */
    private String securityPlanId;
    /**
     * 计划明细表id
     */
    private String securityPlanDetailId;
    /**
     * 证券发行编号
     */
    private String securityIssueCode;
    /**
     * 登记主信息表id
     */
    private String securityInfoId;
    /**
     * 单位
     */
    private String corpId;
    /**
     * 层级
     */
    private String levelName;
    /**
     * 产品
     */
    private String productName;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 最小还款日期
     */
    private Date gedRepayDate;
    /**
     * 最大还款日期
     */
    private Date ledRepayDate;
    /**
     * 计划还款日
     */
    private Date planRepayDate;
    /**
     * 还款本金
     */
    private BigDecimal repayMoney;
    /**
     * 执行利率
     */
    private BigDecimal rate;
    /**
     * 还款利息
     */
    private BigDecimal repayInterest;
    /**
     * 还款金额
     */
    private BigDecimal repayAmount;
    /**
     * 剩余本金
     */
    private BigDecimal remainMoney;
    /**
     * 是否已还
     */
    private String isRepay;
    /**
     * 是否选择
     */
    private String isChose;
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
    public String getSecurityPlanId() {
        return securityPlanId;
    }
    public void setSecurityPlanId(String securityPlanId) {
        this.securityPlanId = securityPlanId;
    }
    public String getSecurityPlanDetailId() {
        return securityPlanDetailId;
    }
    public void setSecurityPlanDetailId(String securityPlanDetailId) {
        this.securityPlanDetailId = securityPlanDetailId;
    }
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public Date getGedRepayDate() {
        return gedRepayDate;
    }
    public void setGedRepayDate(Date gedRepayDate) {
        this.gedRepayDate = gedRepayDate;
    }
    public Date getLedRepayDate() {
        return ledRepayDate;
    }
    public void setLedRepayDate(Date ledRepayDate) {
        this.ledRepayDate = ledRepayDate;
    }
    public Date getPlanRepayDate() {
        return planRepayDate;
    }
    public void setPlanRepayDate(Date planRepayDate) {
        this.planRepayDate = planRepayDate;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    public BigDecimal getRepayAmount() {
        return repayAmount;
    }
    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }
    public BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    public String getIsChose() {
        return isChose;
    }
    public void setIsChose(String isChose) {
        this.isChose = isChose;
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
