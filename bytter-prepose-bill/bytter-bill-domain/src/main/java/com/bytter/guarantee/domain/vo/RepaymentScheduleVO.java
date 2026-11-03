package com.bytter.guarantee.domain.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class RepaymentScheduleVO extends ExcelModel implements Serializable {
    private String id;
    /** 业务单据号 */
    private String billCode;
    /** 还款周期 */
    private String repayCycel;
    /** 预计还款日 */
    @Excel(name = "*预计还款日", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDate;
    /**
     * 还款金额
     */
    @Excel(name = "*还款本金", isImportField = "true", numFormat = "##0.00")
    private java.math.BigDecimal repaymentMoney;
    /**
     * 还款利息
     */
    @Excel(name = "*还款利息", isImportField = "true", numFormat = "##0.00")
    private java.math.BigDecimal repaymentIst;
    /** 本期还款金额 */
    private java.math.BigDecimal currentRepayMoney;
    /** 所有还款利息之和 */
    private java.math.BigDecimal sumRepaymentIst;
    /** 实际还款日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date realityRepayDate;
    /** 实际还款本金 */
    private java.math.BigDecimal realityRepayMoney;
    /** 剩余本金 */
    private java.math.BigDecimal surplusMoney;
    /** 归还标识 */
    private String isRepay;
    /** 备注 */
    @Excel(name = "备注", isImportField = "true")
    private String remark;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 还款开始日 */
    private Date repaymentBeginDate;
    /** 删除标志 -2:已删除 */
    private String valid_Sign;
    /** 利息测算基数 */
    private String rateBase;
    /**
     * A1 -内部贷款录入 A2 -- 内部贷款发放 B1 -银行借款录入 B2 -- 银行借款发放
     */
    private String internalSign;
    private String relationId;
    /** 是否使用 */
    private Integer used;
    /** 最后一条还款计划数据 */
    private boolean lastSign = false;
    /**
     * 前端页面页数
     */
    private String rowNum;
    /**
     * 预计还款日是否可编辑
     */
    private boolean prediceRepayDateIsEdit;
    /**
     * 还款本金是否可编辑
     */
    private boolean paymentMoneyIsEdit;
    /**
     * 还款利息是否可编辑
     */
    private boolean inerestIsEdit;
    @Override
    public String getBeanName() {
        return "RepaymentScheduleService";
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    public Date getPrediceRepayDate() {
        return prediceRepayDate;
    }
    public void setPrediceRepayDate(Date prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }
    public java.math.BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }
    public void setRepaymentMoney(java.math.BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }
    public java.math.BigDecimal getRepaymentIst() {
        return repaymentIst;
    }
    public void setRepaymentIst(java.math.BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public java.math.BigDecimal getCurrentRepayMoney() {
        return currentRepayMoney;
    }
    public void setCurrentRepayMoney(java.math.BigDecimal currentRepayMoney) {
        this.currentRepayMoney = currentRepayMoney;
    }
    public Date getRealityRepayDate() {
        return realityRepayDate;
    }
    public void setRealityRepayDate(Date realityRepayDate) {
        this.realityRepayDate = realityRepayDate;
    }
    public java.math.BigDecimal getRealityRepayMoney() {
        return realityRepayMoney;
    }
    public void setRealityRepayMoney(java.math.BigDecimal realityRepayMoney) {
        this.realityRepayMoney = realityRepayMoney;
    }
    public java.math.BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(java.math.BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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
    public Date getRepaymentBeginDate() {
        return repaymentBeginDate;
    }
    public void setRepaymentBeginDate(Date repaymentBeginDate) {
        this.repaymentBeginDate = repaymentBeginDate;
    }
    public String getValid_Sign() {
        return valid_Sign;
    }
    public void setValid_Sign(String valid_Sign) {
        this.valid_Sign = valid_Sign;
    }
    public String getInternalSign() {
        return internalSign;
    }
    public void setInternalSign(String internalSign) {
        this.internalSign = internalSign;
    }
    public String getRelationId() {
        return relationId;
    }
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
    public Integer getUsed() {
        return used;
    }
    public void setUsed(Integer used) {
        this.used = used;
    }
    public java.math.BigDecimal getSumRepaymentIst() {
        return sumRepaymentIst;
    }
    public void setSumRepaymentIst(java.math.BigDecimal sumRepaymentIst) {
        this.sumRepaymentIst = sumRepaymentIst;
    }
    public boolean isLastSign() {
        return lastSign;
    }
    public void setLastSign(boolean lastSign) {
        this.lastSign = lastSign;
    }
    public String getRowNum() {
        return rowNum;
    }
    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }
    public boolean isPrediceRepayDateIsEdit() {
        return prediceRepayDateIsEdit;
    }
    public void setPrediceRepayDateIsEdit(boolean prediceRepayDateIsEdit) {
        this.prediceRepayDateIsEdit = prediceRepayDateIsEdit;
    }
    public boolean isPaymentMoneyIsEdit() {
        return paymentMoneyIsEdit;
    }
    public void setPaymentMoneyIsEdit(boolean paymentMoneyIsEdit) {
        this.paymentMoneyIsEdit = paymentMoneyIsEdit;
    }
    public boolean isInerestIsEdit() {
        return inerestIsEdit;
    }
    public void setInerestIsEdit(boolean inerestIsEdit) {
        this.inerestIsEdit = inerestIsEdit;
    }
}
