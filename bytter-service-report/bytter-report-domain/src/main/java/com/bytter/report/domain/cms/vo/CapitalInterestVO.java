package com.bytter.report.domain.cms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CapitalInterestVO implements Serializable {
    /**
     * 申请单位
     */
    //@Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 借款单位
     */
    private String borrow;
    /**
     * 融资类型
     */
    //@Dict(dictGroup = "Loan.finType", inject = "finTypeName")
    private String finType;

    private String finTypeName;
    /**
     * 申请单号
     */
    private String billCode;
    /**
     * 币种
     */
    //@Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 借款/注册金额
     */
    private BigDecimal borrowMoney;
    /**
     * 发放/发行单号
     */
    private String sendCode;
    /**
     * 发放/发行金额
     */
    private BigDecimal sendMoney;
    /**
     * 发放/发行日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 待还开始时间
     */

    private String rbeginDate;
    /**
     * 待还结束时间
     */

    private String rendDate;
    /**
     * 待还本金,利息
     */
    private List<BigDecimal> unMoney;

    /**
     * 待还时间
     */
    private String reTime;

    /**
     * 待还本金
     */
    private BigDecimal reMoney;

    /**
     * 待还利息
     */
    private BigDecimal reInterest;

    /**
     * 当前页
     */
    private Integer start;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 导出类型
     */
    private String exportType;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public String getFinType() {
        return finType;
    }

    public void setFinType(String finType) {
        this.finType = finType;
    }

    public String getFinTypeName() {
        return finTypeName;
    }

    public void setFinTypeName(String finTypeName) {
        this.finTypeName = finTypeName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRbeginDate() {
        return rbeginDate;
    }

    public void setRbeginDate(String rbeginDate) {
        this.rbeginDate = rbeginDate;
    }

    public String getRendDate() {
        return rendDate;
    }

    public void setRendDate(String rendDate) {
        this.rendDate = rendDate;
    }

    public List<BigDecimal> getUnMoney() {
        return unMoney;
    }

    public void setUnMoney(List<BigDecimal> unMoney) {
        this.unMoney = unMoney;
    }

    public String getReTime() {
        return reTime;
    }

    public void setReTime(String reTime) {
        this.reTime = reTime;
    }

    public BigDecimal getReMoney() {
        return reMoney;
    }

    public void setReMoney(BigDecimal reMoney) {
        this.reMoney = reMoney;
    }

    public BigDecimal getReInterest() {
        return reInterest;
    }

    public void setReInterest(BigDecimal reInterest) {
        this.reInterest = reInterest;
    }
}
