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
 * @Description: 股权融资回购计划明细vo
 * @date 2022-06-29
 */
public class CmsEquityRepoPlanDetailVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * 融资编号
     */
    private String equityCode;
    /**
     * 回购计划主表id
     */
    private String repoPlanId;
    /**
     * 回购日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*回购日期")
    private Date repoDate;
    /**
     * 本次回购金额
     */
    @Excel(name = "*本次回购金额")
    private BigDecimal repoMoney;
    /**
     * 剩余回购金额
     */
    private BigDecimal reRepoMoney;
    /**
     * 本次占比
     */
    private BigDecimal accounted;
    /**
     * 是否付款
     */
    @Excel(name = "是否付款")
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
    @Override
    public String getBeanName() {
        return "cmsEquityRepoPlanDetailService";
    }
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
    public String getRepoPlanId() {
        return repoPlanId;
    }
    public void setRepoPlanId(String repoPlanId) {
        this.repoPlanId = repoPlanId;
    }
    public Date getRepoDate() {
        return repoDate;
    }
    public void setRepoDate(Date repoDate) {
        this.repoDate = repoDate;
    }
    public BigDecimal getRepoMoney() {
        return repoMoney;
    }
    public void setRepoMoney(BigDecimal repoMoney) {
        this.repoMoney = repoMoney;
    }
    public BigDecimal getReRepoMoney() {
        return reRepoMoney;
    }
    public void setReRepoMoney(BigDecimal reRepoMoney) {
        this.reRepoMoney = reRepoMoney;
    }
    public BigDecimal getAccounted() {
        return accounted;
    }
    public void setAccounted(BigDecimal accounted) {
        this.accounted = accounted;
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
    public String toString() {
        return "CmsEquityRepoPlanDetailVO{" + "id='" + id + '\'' + ", equityCode='" + equityCode + '\'' + ", planId='"
                + repoPlanId + '\'' + ", payDate=" + repoDate + ", repoMoney=" + repoMoney + ", reRepoMoney="
                + reRepoMoney + ", accounted=" + accounted + ", isPay='" + isPay + '\'' + ", validSign=" + validSign
                + ", addOrUpdate='" + addOrUpdate + '\'' + ", rmk='" + rmk + '\'' + ", createBy='" + createBy + '\''
                + ", createDate=" + createDate + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
