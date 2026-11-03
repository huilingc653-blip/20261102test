package com.bytter.biz.wage.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发工资下拨金额配置
 *
 * @author loveCamille
 */
@Entity
@Table(name = "nis_wage_release_config")
public class NisWageReleaseConfig extends IdEntity implements Serializable {

    /**
     * 单位id
     */
    private String corpId;

    /**
     * 银行账号
     */
    private String bankAcc;

    /**
     * 银行账号名称
     */
    private String bankAccName;

    /**
     * 开户行行号
     */
    private String bankCode;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 下拨日期
     */
    private Date releaseDate;

    /**
     * 本月下拨金额
     */
    private BigDecimal releaseAmount;

    /**
     * 上月下拨金额
     */
    private BigDecimal lastMonthReleaseAmount;

    /**
     * 本月单据累计下拨金额
     */
    private BigDecimal totalReleaseMoney;

    /**
     * 下拨状态
     */
    private Integer status;

    /**
     * 确认人id
     */
    private String confirmId;

    /**
     * 确认人名称
     */
    private String confirmName;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 是否生成需求清单
     */
    private Integer generateRequire;

    /**
     * 需求清单生成时间
     */
    private Date generateTime;

    /**
     * 大参林脚本审核，需要添加该字段
     */
    private String isDeleted;

    /**
     * 创建人登录名称
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人登录名称
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateTime;

    @Column(name = "corp_id")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "bank_acc")
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name = "bank_acc_name")
    public String getBankAccName() {
        return bankAccName;
    }

    public void setBankAccName(String bankAccName) {
        this.bankAccName = bankAccName;
    }

    @Column(name = "bank_code")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "release_date")
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column(name = "release_amount")
    public BigDecimal getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(BigDecimal releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    @Column(name = "last_month_release_amount")
    public BigDecimal getLastMonthReleaseAmount() {
        return lastMonthReleaseAmount;
    }

    public void setLastMonthReleaseAmount(BigDecimal lastMonthReleaseAmount) {
        this.lastMonthReleaseAmount = lastMonthReleaseAmount;
    }

    @Column(name = "total_release_money")
    public BigDecimal getTotalReleaseMoney() {
        return totalReleaseMoney;
    }

    public void setTotalReleaseMoney(BigDecimal totalReleaseMoney) {
        this.totalReleaseMoney = totalReleaseMoney;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "confirm_id")
    public String getConfirmId() {
        return confirmId;
    }

    public void setConfirmId(String confirmId) {
        this.confirmId = confirmId;
    }

    @Column(name = "confirm_name")
    public String getConfirmName() {
        return confirmName;
    }

    public void setConfirmName(String confirmName) {
        this.confirmName = confirmName;
    }

    @Column(name = "confirm_time")
    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    @Column(name = "generate_require")
    public Integer getGenerateRequire() {
        return generateRequire;
    }

    public void setGenerateRequire(Integer generateRequire) {
        this.generateRequire = generateRequire;
    }

    @Column(name = "generate_time")
    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    @Column(name = "is_deleted")
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "create_by")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_by")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
