package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FexBankAccBalReportVO implements Serializable, IBaseDataInject {
    /**日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date balDate;

    /**币别*/
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String englishCode;

    /**外币资金存量余额*/
    private BigDecimal bal;
    /**外币资金存量余额(折算币种)*/
    private BigDecimal reducedBal;
    /**存量份额*/
    private BigDecimal stockShare;

    public Date getBalDate() {
        return balDate;
    }

    public void setBalDate(Date balDate) {
        this.balDate = balDate;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public BigDecimal getReducedBal() {
        return reducedBal;
    }

    public void setReducedBal(BigDecimal reducedBal) {
        this.reducedBal = reducedBal;
    }

    public BigDecimal getStockShare() {
        return stockShare;
    }

    public void setStockShare(BigDecimal stockShare) {
        this.stockShare = stockShare;
    }
}
