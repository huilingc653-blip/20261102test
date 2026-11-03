package com.bytter.credit.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 年度融资规划登记表实体
 *
 * @author fangcf
 */
@Entity
@Table(name = "RAT_YEARFIN_PLAN_REGISTER")
@SuppressWarnings("serial")
public class RatYearFinPlanRegister extends IdEntity implements java.io.Serializable {

    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 年度
     */
    private String years;
    /**
     * 年采购量
     */
    private BigDecimal yearPurchaseAmount;
    /**
     * 并购投资
     */
    private BigDecimal mergeInvestmentAmount;
    /**
     * 解决措施
     */
    private String solveMeasure;
    /**
     * 存在的困难
     */
    private String existDifficulty;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name = "YEARS")
    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    @Column(name = "YEAR_PURCHASE_AMOUNT")
    public BigDecimal getYearPurchaseAmount() {
        return yearPurchaseAmount;
    }

    public void setYearPurchaseAmount(BigDecimal yearPurchaseAmount) {
        this.yearPurchaseAmount = yearPurchaseAmount;
    }

    @Column(name = "MERGE_INVESTMENT_AMOUNT")
    public BigDecimal getMergeInvestmentAmount() {
        return mergeInvestmentAmount;
    }

    public void setMergeInvestmentAmount(BigDecimal mergeInvestmentAmount) {
        this.mergeInvestmentAmount = mergeInvestmentAmount;
    }

    @Column(name = "SOLVE_MEASURE")
    public String getSolveMeasure() {
        return solveMeasure;
    }

    public void setSolveMeasure(String solveMeasure) {
        this.solveMeasure = solveMeasure;
    }

    @Column(name = "EXIST_DIFFICULTY")
    public String getExistDifficulty() {
        return existDifficulty;
    }

    public void setExistDifficulty(String existDifficulty) {
        this.existDifficulty = existDifficulty;
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
