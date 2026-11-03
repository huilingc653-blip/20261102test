package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@SuppressWarnings("serial")
@ExcelTarget("LeaseCapitalPlanDelVO")
public class LeaseCapitalPlanDelVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * 租赁表id
     */
    private String leaseId;
    /**
     * 租赁计划主表id
     */
    private String leasePlanId;
    /**
     * 付租日期
     */
    @Excel(name = "付租日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 计划付租日
     */
    @Excel(name = "*计划付租日", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payPlanDate;
    /**
     * 租赁本金
     */
    @Excel(name = "*租赁本金", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal leaseMonry;
    /**
     * 租赁利息
     */
    @Excel(name = "*租赁利息", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal leaseInterest;
    /**
     * 本期租金
     */
    private BigDecimal thisMoney;
    /**
     * 本期费用
     */
    @Excel(name = "*本期费用", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal thisCost;
    /**
     * 保证金金额
     */
    @Excel(name = "*保证金", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal bailMoney;
    /**
     * 剩余本金
     */
    private BigDecimal surplusMoney;
    /**
     * 是否已还/抵扣
     */
    @Dict(dictGroup = "sf_yn", inject = "isRepayName")
    private String isRepay;
    @Excel(name = "*是否已还/抵扣", isImportField = "true")
    @BtCompositeSearch(fieldName = "isRepay")
    private String isRepayName;
    /**
     * 支付订单号
     */
    private String payNo;
    /**
     * 是否有效状态 -2:已删除
     */
    private String validSign;
    /**
     * 备注
     */
    @Excel(name = "备注", isImportField = "true")
    private String rmk;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public String getLeasePlanId() {
        return leasePlanId;
    }
    public void setLeasePlanId(String leasePlanId) {
        this.leasePlanId = leasePlanId;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public Date getPayPlanDate() {
        return payPlanDate;
    }
    public void setPayPlanDate(Date payPlanDate) {
        this.payPlanDate = payPlanDate;
    }
    public BigDecimal getLeaseMonry() {
        return leaseMonry;
    }
    public void setLeaseMonry(BigDecimal leaseMonry) {
        this.leaseMonry = leaseMonry;
    }
    public BigDecimal getLeaseInterest() {
        return leaseInterest;
    }
    public void setLeaseInterest(BigDecimal leaseInterest) {
        this.leaseInterest = leaseInterest;
    }
    public BigDecimal getThisMoney() {
        return thisMoney;
    }
    public void setThisMoney(BigDecimal thisMoney) {
        this.thisMoney = thisMoney;
    }
    public BigDecimal getThisCost() {
        return thisCost;
    }
    public void setThisCost(BigDecimal thisCost) {
        this.thisCost = thisCost;
    }
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
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
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
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
    public String getBeanName() {
        return "LeaseCapitalPlanDelService";
    }
}
