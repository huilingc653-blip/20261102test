package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName LoanBalanceTrendVO
 * @Description TODO
 * @Author fusb
 * @Date 2020/1/14 10:23
 * @Version 1.0
 **/
public class LoanBalanceTrendVO implements Serializable  {
    /**贷款类型*/
    private String loanSign;
    /**单位*/
    @Corp(inject = "corpName")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**币种*/
    @Currency(inject="curName")
    private String curCode;
    /**币种名称*/
    private String curName;
    /**趋势日期*/
    private String trendDate;
    /**发放金额*/
    private BigDecimal sendMoney;
    /**已还款金额*/
    private BigDecimal repayMoney;
    /**还款在途金额*/
    private BigDecimal repayAwayMoney;
    /**贷款余额*/
    private BigDecimal balMoney;
    /**开始年月*/
    private String startDate;
    /**结束年月*/
    private String endDate;
    /**是否折合本位币*/
    private String natureCurrency;
    /**金额单位*/
    private String moneyType;
    /**余额是否包含在途*/
    private String includeFreezeMoney;
    /**折合币种*/
    private String convertCurId;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getLoanSign() {
        return loanSign;
    }

    public void setLoanSign(String loanSign) {
        this.loanSign = loanSign;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getTrendDate() {
        return trendDate;
    }

    public void setTrendDate(String trendDate) {
        this.trendDate = trendDate;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public BigDecimal getRepayAwayMoney() {
        return repayAwayMoney;
    }

    public void setRepayAwayMoney(BigDecimal repayAwayMoney) {
        this.repayAwayMoney = repayAwayMoney;
    }

    public BigDecimal getBalMoney() {
        return balMoney;
    }

    public void setBalMoney(BigDecimal balMoney) {
        this.balMoney = balMoney;
    }

    public String getStartDate() {
        if (StringUtils.isEmpty(startDate)) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM");
            startDate = sdf.format(new Date());
        }
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        if (StringUtils.isEmpty(endDate)) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM");
            endDate = sdf.format(new Date());
        }
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNatureCurrency() {
        if (StringUtils.isEmpty(natureCurrency)) {
            natureCurrency = CommonConstant.NO;
        }
        return natureCurrency;
    }

    public void setNatureCurrency(String natureCurrency) {
        this.natureCurrency = natureCurrency;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getMoneyType() {
        if (StringUtils.isEmpty(moneyType)) {
            moneyType = "1";
        }
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getIncludeFreezeMoney() {
        if (StringUtils.isEmpty(includeFreezeMoney)) {
            includeFreezeMoney = CommonConstant.NO;
        }
        return includeFreezeMoney;
    }

    public void setIncludeFreezeMoney(String includeFreezeMoney) {
        this.includeFreezeMoney = includeFreezeMoney;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }
}
