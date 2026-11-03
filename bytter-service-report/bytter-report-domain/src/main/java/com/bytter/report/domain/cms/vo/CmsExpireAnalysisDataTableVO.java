package com.bytter.report.domain.cms.vo;

import java.math.BigDecimal;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */
public class CmsExpireAnalysisDataTableVO {

    /**
     * 剩余期限
     */
    private String remainTermName;

    private BigDecimal loanData;

    private BigDecimal precativeData;

    private BigDecimal privodeData;

    private BigDecimal totalData;

    public String getRemainTermName() {
        return remainTermName;
    }

    public void setRemainTermName(String remainTermName) {
        this.remainTermName = remainTermName;
    }

    public BigDecimal getLoanData() {
        return loanData;
    }

    public void setLoanData(BigDecimal loanData) {
        this.loanData = loanData;
    }


    public BigDecimal getPrecativeData() {
        return precativeData;
    }

    public void setPrecativeData(BigDecimal precativeData) {
        this.precativeData = precativeData;
    }

    public BigDecimal getPrivodeData() {
        return privodeData;
    }

    public void setPrivodeData(BigDecimal privodeData) {
        this.privodeData = privodeData;
    }

    public BigDecimal getTotalData() {
        return totalData;
    }

    public void setTotalData(BigDecimal totalData) {
        this.totalData = totalData;
    }
}
