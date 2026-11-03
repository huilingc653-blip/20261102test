package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 银团贷款还款明细表
 * @author onlineGenerator
 * @date 2022-07-21 15:15:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROVIDE_SYNDICATEDDTL", schema = "")
@SuppressWarnings("serial")
public class ProvideSyndicatedDtl extends IdEntity implements java.io.Serializable {
    /**
     * 借款主表id
     */
    private String directFinanceId;
    /**
     * 还款明细id
     */
    private String scheduleId;
    /**
     * 还款明细id
     */
    private String scheduleDetailedId;
    /**
     * 还款支付id
     */
    private String repayId;
    /**
     * 是否牵头行
     */
    private String leadingBank;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行开户行号
     */
    private String bankCode;
    /**
     * 待还款金额
     */
    private BigDecimal surplusMoney;
    /**
     * 本次还款金额
     */
    private BigDecimal repaymentMoney;
    /**
     * 还款本金占比%
     */
    private BigDecimal repaymentMoneyShare;
    /**
     * 还款利息
     */
    private BigDecimal repaymentIst;
    /**
     * 状态95正常状态-2删除
     */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "SCHEDULE_ID")
    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
    @Column(name = "SCHEDULEDETAILED_ID")
    public String getScheduleDetailedId() {
        return scheduleDetailedId;
    }
    public void setScheduleDetailedId(String scheduleDetailedId) {
        this.scheduleDetailedId = scheduleDetailedId;
    }
    @Column(name = "REPAY_ID")
    public String getRepayId() {
        return repayId;
    }
    public void setRepayId(String repayId) {
        this.repayId = repayId;
    }
    @Column(name = "LEADING_BANK")
    public String getLeadingBank() {
        return leadingBank;
    }
    public void setLeadingBank(String leadingBank) {
        this.leadingBank = leadingBank;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name = "SURPLUS_MONEY")
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    @Column(name = "REPAYMENT_MONEY")
    public BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }
    public void setRepaymentMoney(BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }
    @Column(name = "REPAYMENT_MONEY_SHARE")
    public BigDecimal getRepaymentMoneyShare() {
        return repaymentMoneyShare;
    }
    public void setRepaymentMoneyShare(BigDecimal repaymentMoneyShare) {
        this.repaymentMoneyShare = repaymentMoneyShare;
    }
    @Column(name = "REPAYMENT_IST")
    public BigDecimal getRepaymentIst() {
        return repaymentIst;
    }
    public void setRepaymentIst(BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
