package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 债券还款计划子表
 * @date 2022-06-06 10:07:46
 */
@SuppressWarnings("serial")
@ExcelTarget("CmsBondRepayPlanDtlVo")
public class CmsBondRepayPlanDtlVo extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /** 债券还款计划主表id */
    private String planId;
    /** 债券还款支付表id */
    private String payId;
    /** 债劵发行id */
    private String issueId;
    /** 债劵发行编号 */
    @Excel(name = "*债券发行编号", isImportField = "true")
    private String bondIssueNum;
    /** 还款日期 */
    @Excel(name = "还款日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /** 计划还款日 */
    @Excel(name = "*计划还款日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planRepayDate;
    /** 还款本金值 */
    @Excel(name = "*还款本金值", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal repayMoney;
    /** 剩余本金值 */
    private BigDecimal remainMoney;
    /** 本次还款金额 */
    @Excel(name = "*本次还款金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal repayAmount;
    /** 发行编号 发行金额 */
    private BigDecimal issueMoney;
    /** 发行编号 未还本金 */
    private BigDecimal unpaidMoney;
    /** 测算利息 */
    private BigDecimal repayInterest;
    /** 本次偿还占发行总额比例% */
    private BigDecimal repayRate;
    /** 支付状态 */
    private Integer payStatus;
    /** 是否已还 */
    @Dict(dictGroup = "sf_yn", inject = "isRepayName")
    private String isRepay;
    @Excel(name = "*是否已还", isImportField = "true")
    @BtCompositeSearch(fieldName = "isRepay")
    private String isRepayName;
    /** 有效标志 */
    private String validSign;
    /** 备注 */
    @Excel(name = "备注", isImportField = "true")
    private String rmk;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
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
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    public Integer getPayStatus() {
        return payStatus;
    }
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
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
    public BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    public BigDecimal getRepayAmount() {
        return repayAmount;
    }
    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    public BigDecimal getUnpaidMoney() {
        return unpaidMoney;
    }
    public void setUnpaidMoney(BigDecimal unpaidMoney) {
        this.unpaidMoney = unpaidMoney;
    }
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    public BigDecimal getRepayRate() {
        return repayRate;
    }
    public void setRepayRate(BigDecimal repayRate) {
        this.repayRate = repayRate;
    }
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    public String getIsRepayName() {
        return isRepayName;
    }
    public void setIsRepayName(String isRepayName) {
        this.isRepayName = isRepayName;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
        return getId();
    }
    @Override
    public String getBeanName() {
        return "CmsBondRepayPlanDtlService";
    }
}