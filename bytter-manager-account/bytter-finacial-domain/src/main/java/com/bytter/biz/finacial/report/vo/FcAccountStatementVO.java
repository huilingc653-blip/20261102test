package com.bytter.biz.finacial.report.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author luxy
 * @Title: FcAccountStatementVO
 * @ProjectName T3.3
 * @Description: TODO
 * @date 2020/6/30 16:27
 */
public class FcAccountStatementVO  {
    /**网点 */
    private java.lang.String netId;
    /**网点名称*/
    private java.lang.String netName;
    /**单位*/
    private java.lang.String corpId;
    /**币别代码*/
    private java.lang.String curId;
    private java.lang.String curName;
    /**凭证种类*/
    private String voucherType;
    private String voucherName;
    /**摘要*/
    private java.lang.String summaryContent;
    /**账号*/
    private java.lang.String account;
    private java.lang.String accName;
    /**查询开始日期**/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**查询结束日期**/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private java.math.BigDecimal yesterBalance;
    /**余额日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balanceDate;
    /**借方发生额汇总*/
    private java.math.BigDecimal debitMoneySum;
    /**贷方发生额汇总*/
    private java.math.BigDecimal creditMoneySum;
    private List<FcAccountDtlVO> accountDtlVOList;
    private java.lang.String blnctrAcc;
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public List<FcAccountDtlVO> getAccountDtlVOList() {
        return accountDtlVOList;
    }
    public void setAccountDtlVOList(List<FcAccountDtlVO> accountDtlVOList) {
        this.accountDtlVOList = accountDtlVOList;
    }
    public Date getBalanceDate() {
        return balanceDate;
    }
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public BigDecimal getYesterBalance() {
        return yesterBalance;
    }
    public void setYesterBalance(BigDecimal yesterBalance) {
        this.yesterBalance = yesterBalance;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public BigDecimal getDebitMoneySum() {
        return debitMoneySum;
    }
    public void setDebitMoneySum(BigDecimal debitMoneySum) {
        this.debitMoneySum = debitMoneySum;
    }
    public BigDecimal getCreditMoneySum() {
        return creditMoneySum;
    }
    public void setCreditMoneySum(BigDecimal creditMoneySum) {
        this.creditMoneySum = creditMoneySum;
    }
}
