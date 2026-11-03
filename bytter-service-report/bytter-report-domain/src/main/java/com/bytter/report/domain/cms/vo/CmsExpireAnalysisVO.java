package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.StringUtils;
import java.math.BigDecimal;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */
public class CmsExpireAnalysisVO{

    /**
     *单位
     */
    private String corpName;

    /**
     * 单位id
     */
    private String corpId;

    /**
     * 融资类型
     */
    @Dict(dictGroup = "Loan.Sign",inject = "loanTypeName")
    private String loanType;
    private String loanTypeName;

    /**
     * 剩余期限
     */
    private String expireAnalysis;

    /**
     * 币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    /**
     * 转化前金额
     */
    private BigDecimal money;

    /**
     * 转换本位币金额
     */
    private BigDecimal convertMoney;

    /**
     * 剩余期限id
     */
    private String remainTermId;

    /**
    *是否折算币种
    */
    private String isConvert;

    /**
     *折算币种
     */
    private String convertCurId;

    /**
     *是否包含在途
     */
    private String isInclude;


    private String moneyType;

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getExpireAnalysis() {
        return expireAnalysis;
    }

    public void setExpireAnalysis(String expireAnalysis) {
        this.expireAnalysis = expireAnalysis;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getRemainTermId() {
        return remainTermId;
    }

    public void setRemainTermId(String remainTermId) {
        this.remainTermId = remainTermId;
    }

    public String getIsConvert() {
        if (StringUtils.isEmpty(isConvert)) {
            isConvert = "0";
        }
        return isConvert;
    }

    public void setIsConvert(String isConvert) {
        this.isConvert = isConvert;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getIsInclude() {
//        if (StringUtils.isEmpty(isInclude)) {
//            isInclude = "0";
//        } //条件查询时，不选择时，需要为 null
        return isInclude;
    }

    public void setIsInclude(String isInclude) {
        this.isInclude = isInclude;
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

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public void setLoanTypeName(String loanTypeName) {
        this.loanTypeName = loanTypeName;
    }
}
