package com.bytter.finance.domain.equity.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 缴付/过户计划明细表
 * @date 2022-06-20
 */
@Entity
@Table(name = "CMS_EQUITY_PAY_PLAN_DETAIL", schema = "")
public class CmsEquityPayPlanDetail extends IdEntity implements java.io.Serializable {
    /**
     * 融资编号
     */
    private String equityCode;
    /**
     * 计划主表id
     */
    private String planId;
    /**
     * 缴付/过户日期
     */
    private Date payDate;
    /**
     * 本次缴付金额
     */
    private BigDecimal payMoney;
    /**
     * 剩余缴付金额
     */
    private BigDecimal rePayMoney;
    /**
     * 本次占比
     */
    private BigDecimal accounted;
    /**
     * 资产名称
     */
    private String assName;
    /**
     * 是否已缴付/过户
     */
    private String isPay;
    /**
     * 是否有效
     */
    private String validSign;
    /**
     * 登记/修改标识 0登记、1修改
     */
    private String addOrUpdate;
    /**
     * 备注
     */
    private String rmk;
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
    @Column(name = "EQUITY_CODE")
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    @Column(name = "PLAN_ID")
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "PAY_MONEY")
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    @Column(name = "RE_PAY_MONEY")
    public BigDecimal getRePayMoney() {
        return rePayMoney;
    }
    public void setRePayMoney(BigDecimal rePayMoney) {
        this.rePayMoney = rePayMoney;
    }
    @Column(name = "ACCOUNTED")
    public BigDecimal getAccounted() {
        return accounted;
    }
    public void setAccounted(BigDecimal accounted) {
        this.accounted = accounted;
    }
    @Column(name = "ASS_NAME")
    public String getAssName() {
        return assName;
    }
    public void setAssName(String assName) {
        this.assName = assName;
    }
    @Column(name = "IS_PAY")
    public String getIsPay() {
        return isPay;
    }
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "ADD_OR_UPDATE")
    public String getAddOrUpdate() {
        return addOrUpdate;
    }
    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
