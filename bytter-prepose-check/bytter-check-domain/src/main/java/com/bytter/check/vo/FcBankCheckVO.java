package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
public class FcBankCheckVO implements  java.io.Serializable {
    private String id;
    /**
     * 对账类型
     */
    @BtCompositeSearch(fieldName = "fc.check_type")
    private String checkType;
    /**
     * 企业账号
     */
    @BtCompositeSearch(fieldName = "fc.account")
    private String account;
    /**
     * 银行账号
     */
    @BtCompositeSearch(fieldName = "fc.bank_Acc")
    private String bankAcc;
    /**
     * 对账初始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "fc.check_date")
    private Date checkDate;
    /**
     * 企业余额
     */
    @BtCompositeSearch(fieldName = "b.bln_balance")
    private BigDecimal blnBalance;
    /**
     * 银行余额
     */
    @BtCompositeSearch(fieldName = "b.bank_balance")
    private BigDecimal bankBalance;
    /**
     * 单位
     */
    private String corpId;
    /**
     * 中心账户名称
     */
    private String accName;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 币别ID
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "fc.cur_id")
    private String curId;
    /**
     * 币别ID
     */
    private String curName;
    /**
     * 单位名称
     **/
    private String corpName;
    /**
     * ERP名称
     **/
    private String erpName;
    /**
     * 上次对账开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 上次对账结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * ERP账单未对笔数
     */
    private Integer erpCnt;
    /**
     * 银行账单未对笔数
     */
    private Integer bankCnt;
    /**银行调节余额*/
    private BigDecimal bankAdjBal;
    /**企业调节余额*/
    private BigDecimal blnAdjBal;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    public BigDecimal getBlnBalance() {
        return blnBalance;
    }
    public void setBlnBalance(BigDecimal blnBalance) {
        this.blnBalance = blnBalance;
    }
    public BigDecimal getBankBalance() {
        return bankBalance;
    }
    public void setBankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getErpName() {
        return erpName;
    }
    public void setErpName(String erpName) {
        this.erpName = erpName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getErpCnt() {
        return erpCnt;
    }
    public void setErpCnt(Integer erpCnt) {
        this.erpCnt = erpCnt;
    }
    public Integer getBankCnt() {
        return bankCnt;
    }
    public void setBankCnt(Integer bankCnt) {
        this.bankCnt = bankCnt;
    }
    public BigDecimal getBankAdjBal() {
        return bankAdjBal;
    }
    public void setBankAdjBal(BigDecimal bankAdjBal) {
        this.bankAdjBal = bankAdjBal;
    }
    public BigDecimal getBlnAdjBal() {
        return blnAdjBal;
    }
    public void setBlnAdjBal(BigDecimal blnAdjBal) {
        this.blnAdjBal = blnAdjBal;
    }
}
