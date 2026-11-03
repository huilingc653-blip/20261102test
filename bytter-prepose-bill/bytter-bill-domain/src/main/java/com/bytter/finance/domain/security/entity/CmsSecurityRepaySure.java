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
 * @Description: 资产证券化还款确认表
 * @date 2022-08-30
 */
@Entity
@Table(name = "CMS_SECURITY_REPAY_SURE", schema = "")
public class CmsSecurityRepaySure extends IdEntity implements java.io.Serializable {
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
    @Column(name = "SECURITY_PLAN_ID")
    public String getSecurityPlanId() {
        return securityPlanId;
    }
    public void setSecurityPlanId(String securityPlanId) {
        this.securityPlanId = securityPlanId;
    }
    @Column(name = "SECURITY_PLAN_DETAIL_ID")
    public String getSecurityPlanDetailId() {
        return securityPlanDetailId;
    }
    public void setSecurityPlanDetailId(String securityPlanDetailId) {
        this.securityPlanDetailId = securityPlanDetailId;
    }
    @Column(name = "SECURITY_ISSUE_CODE")
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    @Column(name = "SECURITY_INFO_ID")
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "LEVEL_NAME")
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "PLAN_REPAY_DATE")
    public Date getPlanRepayDate() {
        return planRepayDate;
    }
    public void setPlanRepayDate(Date planRepayDate) {
        this.planRepayDate = planRepayDate;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "REPAY_INTEREST")
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    @Column(name = "REPAY_AMOUNT")
    public BigDecimal getRepayAmount() {
        return repayAmount;
    }
    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }
    @Column(name = "REMAIN_MONEY")
    public BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    @Column(name = "IS_REPAY")
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    @Column(name = "IS_CHOSE")
    public String getIsChose() {
        return isChose;
    }
    public void setIsChose(String isChose) {
        this.isChose = isChose;
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
