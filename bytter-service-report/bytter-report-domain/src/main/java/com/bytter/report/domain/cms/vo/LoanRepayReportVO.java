package com.bytter.report.domain.cms.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 内部贷款汇总表
 */
public class LoanRepayReportVO implements Serializable {
    // 单位
    @BtCompositeSearch(fieldName = "l.CORP_ID")
    private String corpId;
    // 单位名称
    private String corpName;
    // 网点
    private String netCode;
    // 网点名称
    private String netName;
    @BtCompositeSearch(fieldName = "l.LOAN_DATE")
    private Date loanDate;
    // 期初贷款余额
    private BigDecimal beginningLoan;
    // 本期贷款
    private BigDecimal currentLoan;
    // 本期还款
    private BigDecimal currentRepay;
    // 期末贷款余额
    private BigDecimal endingLoan;
    public Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public BigDecimal getCurrentRepay() {
        return currentRepay;
    }
    public void setCurrentRepay(BigDecimal currentRepay) {
        this.currentRepay = currentRepay;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public BigDecimal getBeginningLoan() {
        return beginningLoan;
    }
    public void setBeginningLoan(BigDecimal beginningLoan) {
        this.beginningLoan = beginningLoan;
    }
    public BigDecimal getCurrentLoan() {
        return currentLoan;
    }
    public void setCurrentLoan(BigDecimal currentLoan) {
        this.currentLoan = currentLoan;
    }
    public BigDecimal getEndingLoan() {
        return endingLoan;
    }
    public void setEndingLoan(BigDecimal endingLoan) {
        this.endingLoan = endingLoan;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
}
