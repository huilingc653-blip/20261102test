package com.bytter.finance.domain.equity.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 缴付/过户计划明细vo
 * @date 2022-06-20
 */
public class CmsEquityPayPlanDetailVO extends ExcelModel implements Serializable {
    private String id;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*缴付日期/过户日期")
    private Date payDate;
    /**
     * 本次缴付金额
     */
    @Excel(name = "*本次缴付金额")
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
    @Excel(name = "资产名称")
    private String assName;
    /**
     * 是否已缴付/过户
     */
    @Excel(name = "是否已缴付/过户")
    private String isPayName;
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
    @Excel(name = "备注")
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public BigDecimal getRePayMoney() {
        return rePayMoney;
    }
    public void setRePayMoney(BigDecimal rePayMoney) {
        this.rePayMoney = rePayMoney;
    }
    public BigDecimal getAccounted() {
        return accounted;
    }
    public void setAccounted(BigDecimal accounted) {
        this.accounted = accounted;
    }
    public String getAssName() {
        return assName;
    }
    public void setAssName(String assName) {
        this.assName = assName;
    }
    public String getIsPayName() {
        return isPayName;
    }
    public void setIsPayName(String isPayName) {
        this.isPayName = isPayName;
    }
    public String getIsPay() {
        return isPay;
    }
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getAddOrUpdate() {
        return addOrUpdate;
    }
    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
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
        return "cmsEquityPayPlanDetailService";
    }
    @Override
    public String toString() {
        return "CmsEquityPayPlanDetailVO{" + "id='" + id + '\'' + ", equityCode='" + equityCode + '\'' + ", planId='"
                + planId + '\'' + ", payDate=" + payDate + ", payMoney=" + payMoney + ", rePayMoney=" + rePayMoney
                + ", accounted=" + accounted + ", assName='" + assName + '\'' + ", isPay='" + isPay + '\''
                + ", validSign=" + validSign + ", addOrUpdate='" + addOrUpdate + '\'' + ", rmk='" + rmk + '\''
                + ", createBy='" + createBy + '\'' + ", createDate=" + createDate + ", updateBy='" + updateBy + '\''
                + ", updateDate=" + updateDate + '}';
    }
}
