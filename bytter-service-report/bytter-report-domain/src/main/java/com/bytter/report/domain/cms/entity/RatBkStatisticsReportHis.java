package com.bytter.report.domain.cms.entity;

import com.bytter.core.annotation.Currency;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RAT_BK_STAT_REPORT_HISTORY", schema = "")
@SuppressWarnings("serial")
public class RatBkStatisticsReportHis extends IdEntity implements Serializable {

    /**
     * 单位代码
     */
    private String corpId;

    /**
     * 单位代码
     */
    @Excel(name = "授信单位")
    private String corpName;


    /**
     * 状态
     */
    private String status;

    /**
     * 状态中文
     */
    private String statusStr;

    /**
     * 银行类型
     */
    private String bankType;

    /**
     * 银行编号
     */
    private String bankName;

    /**
     * 授信币种
     */
    private String curCode;

    private String curName;

    /**
     * 授信开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 授信结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 授信总额度
     **/
    @Excel(name = "授信额度")
    private BigDecimal money;

    /**
     * 占用额度
     */
    @Excel(name = "占用额度")
    private BigDecimal useMoney;

    /**
     * 在审占用额度
     **/
    @Excel(name = "在审占用额度")
    private BigDecimal approveMoney;

    /**
     * 可用额度
     **/
    @Excel(name = "可用额度")
    private BigDecimal overPlusMoney;

    /**
     * 剩余额度
     **/
    @Excel(name = "剩余额度")
    private BigDecimal leftMoney;

    /**
     * 分配方式
     */
    private String disWay;

    //授信模式
    private String ratMode;

    private Date reportDate;

    private String ratCode;

    private String tenantId;

    @Column(name = "BANK_TYPE", nullable = true, length = 32)
    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    @Column(name = "REPORT_DATE", nullable = true)
    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    @Column(name = "CORP_ID", nullable = true, length = 100)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CORP_NAME", nullable = true, length = 100)
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name = "BANK_NAME", nullable = true, length = 32)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "CUR_CODE", nullable = true, length = 32)
    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    @Column(name = "CUR_NAME", nullable = true, length = 255)
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "BEGIN_DATE", nullable = true)
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "USE_MONEY", nullable = true, scale = 2, length = 15)
    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }

    @Column(name = "DIS_WAY", nullable = true, length = 32)
    public String getDisWay() {
        return disWay;
    }

    public void setDisWay(String disWay) {
        this.disWay = disWay;
    }

    @Column(name = "MONEY", nullable = true, scale = 2, length = 15)
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name = "APPROVE_MONEY", nullable = true, scale = 2, length = 15)
    public BigDecimal getApproveMoney() {
        return approveMoney;
    }

    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }

    @Column(name = "OVER_PLUS_MONEY", nullable = true, scale = 2, length = 15)
    public BigDecimal getOverPlusMoney() {
        if (null == overPlusMoney) {
            overPlusMoney = BigDecimal.ZERO;
        }
        return overPlusMoney;
    }

    public void setOverPlusMoney(BigDecimal overPlusMoney) {
        this.overPlusMoney = overPlusMoney;
    }

    @Column(name = "LEFT_MONEY", nullable = true, scale = 2, length = 15)
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    @Column(name = "RAT_MODE", nullable = true, length = 32)
    public String getRatMode() {
        return ratMode;
    }

    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }

    @Column(name = "STATUS", nullable = true, length = 4)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "STATUS_STR", nullable = true, length = 32)
    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    @Column(name = "RAT_CODE", nullable = true, length = 32)
    public String getRatCode() {
        return ratCode;
    }

    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }

    @Column(name = "TENANT_ID", nullable = true, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}
