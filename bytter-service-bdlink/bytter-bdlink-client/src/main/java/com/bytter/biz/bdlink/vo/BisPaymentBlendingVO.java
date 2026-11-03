package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhupw
 * @version 1.0
 * @Title: Entity
 * @Description: 录入表
 * @date 2021-07-13
 */
public class BisPaymentBlendingVO implements Serializable {
    private String id;
    /**是否存在唯一标识 0，不存在，1存在*/
    private String  isExistId;
    /**付款单唯一标识*/
    private String  billOnlyId;
    /**银行流水唯一标识*/
    private String  dtlOnlyId;
    /**执行规则*/
    private String  rules;
    /**执行规则名称*/
    private String  rulesName;
    /**
     * 银行流水唯一数
     */
    private String bisDtlNo;
    /**
     * 对方账号
     */
    private String oppAccNo;
    /**
     * 业务单号
     */
    private String accNo;
    /**
     * 交易金额
     */
    private BigDecimal amt;
    /**
     * 本方账号
     */
    private String bankAcc;
    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date transTime;
    /**
     * 币别ID
     */
    @BtCompositeSearch(fieldName = "t.cur_id")
    private String curId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 借贷标志 0-收 1-支
     */
    @Dict(dictGroup = "btBankAccDtl.cdSign",inject = "cdSignName")
    private String cdSign;
    /**
     * 用途
     */
    private String uses;
    /**
     * 本方账户名
     */
    private String accName;
    /**
     * 对方账户名
     */
    private String oppAccName;

    /**交易日期-相差天数*/
    private Integer transDays;

    /**
     * 银行流水号
     */
    private String bankSerialNo;

    private String abs;

    private String strategyId;

    /**
     *付款明细扫描次数
     */
    private String againSign;

    /**
     *共享单号
     */
    private String sourceNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsExistId() {
        return isExistId;
    }

    public void setIsExistId(String isExistId) {
        this.isExistId = isExistId;
    }

    public String getBillOnlyId() {
        return billOnlyId;
    }

    public void setBillOnlyId(String billOnlyId) {
        this.billOnlyId = billOnlyId;
    }

    public String getDtlOnlyId() {
        return dtlOnlyId;
    }

    public void setDtlOnlyId(String dtlOnlyId) {
        this.dtlOnlyId = dtlOnlyId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public String getBisDtlNo() {
        return bisDtlNo;
    }

    public void setBisDtlNo(String bisDtlNo) {
        this.bisDtlNo = bisDtlNo;
    }

    public String getOppAccNo() {
        return oppAccNo;
    }

    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getOppAccName() {
        return oppAccName;
    }

    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }

    public Integer getTransDays() {
        return transDays;
    }

    public void setTransDays(Integer transDays) {
        this.transDays = transDays;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getAgainSign() {
        return againSign;
    }

    public void setAgainSign(String againSign) {
        this.againSign = againSign;
    }

    public String getSourceNum() {
        return sourceNum;
    }

    public void setSourceNum(String sourceNum) {
        this.sourceNum = sourceNum;
    }
}
