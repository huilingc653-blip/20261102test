package com.bytter.guarantee.domain.vo;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class GuaContractInfoPlanVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /** 业务单据号 */
    private String billCode;
    /** 批次号 */
    private String ids;
    /** 还款日期 */
    @Excel(name = "*还款日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDate;
    /** 还款金额 */
    @Excel(name = "*还款本金", isImportField = "true")
    private BigDecimal repaymentMoney;
    /** 还款利息 */
    @Excel(name = "*还款利息", isImportField = "true")
    private BigDecimal repaymentIst;
    /** 本期还款金额 */
    private BigDecimal currentRepayMoney;
    /** 归还标识 */
    @Excel(name = "*是否已还", isImportField = "true")
    private String isRepay;
    /** 备注 */
    @Excel(name = "备注", isImportField = "true")
    private String remark;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Override
    public String getBeanName() {
        return "GuaranteePlanService";
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
    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public Date getPrediceRepayDate() {
        return prediceRepayDate;
    }
    public void setPrediceRepayDate(Date prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }
    public BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }
    public void setRepaymentMoney(BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }
    public BigDecimal getRepaymentIst() {
        return repaymentIst;
    }
    public void setRepaymentIst(BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    public BigDecimal getCurrentRepayMoney() {
        return currentRepayMoney;
    }
    public void setCurrentRepayMoney(BigDecimal currentRepayMoney) {
        this.currentRepayMoney = currentRepayMoney;
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
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
