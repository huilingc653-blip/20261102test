package com.bytter.finance.domain.security.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsSecurityPlanDetailVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * 证券发行编号
     */
    private String securityIssueCode;
    /**
     * 计划主表id
     */
    private String securityPlanId;
    /**
     * 还款日期
     */
    @Excel(name = "*还款日期")
    private Date repayDate;
    /**
     * 计划还款日
     */
    @Excel(name = "*计划还款日期")
    private Date planRepayDate;
    /**
     * 还款本金
     */
    @Excel(name = "*还款本金")
    private BigDecimal repayMoney;
    /**
     * 还款利息
     */
    @Excel(name = "*还款利息")
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
    @Excel(name = "是否已还")
    private String isRepay;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 是否有效
     */
    private String validSign;
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
    public String getBeanName() {
        return "cmsSecurityPlanDetailService";
    }
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
    public String getSecurityPlanId() {
        return securityPlanId;
    }
    public void setSecurityPlanId(String securityPlanId) {
        this.securityPlanId = securityPlanId;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
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
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
