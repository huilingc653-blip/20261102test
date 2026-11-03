package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 银行流水智能导入
 */
@SuppressWarnings("serial")
@ExcelTarget("BtBankAccDtlImportVO")
public class BtBankAccDtlImportVO extends ExcelModel implements java.io.Serializable {
    /**
     * 对应字段
     * 本方账号bankAcc  对方账号oppAccNo  对方账户名oppAccName  对方开户行oppAccBank
     * 支出金额payMoney 收入金额incomeMoney 收支方向cdSignStr 交易金额transMoney 币别curId 交易日期transDate 交易时间transTime
     * 余额bal 摘要remark  用途uses 项目信息projectRemark
     */
    /** 单位ID */
    private String corpId;
    /** 本方账号 */
    @Excel(name = "本方账号")
    private String bankAcc;
    /** 本方账户名 */
    private String accName;
    /** 本方账户开户行 */
    private String bankName;
    /** 接口代码 */
    private String bifCode;
    /** 对方账号 */
    @Excel(name = "对方账号")
    private String oppAccNo;
    /** 对方账户名 */
    @Excel(name = "对方账户名")
    private String oppAccName;
    /** 对方账户开户行 */
    @Excel(name = "对方开户行")
    private String oppAccBank;
    /** 借贷标志 0-收 1-支 */
    private String cdSign;
    @Excel(name = "*收支方向")
    private String cdSignStr;
    /** 交易金额 */
    private BigDecimal transMoney;
    @Excel(name = "*交易金额",align = HorizontalAlignment.RIGHT)
    private String transMoneyStr;
    /** 红蓝标志 1-红 2-蓝 */
    private String rbSign;
    /** 交易金额 */
    private BigDecimal amt;
    /** 支出金额 */
    @Excel(name = "*支出金额",align = HorizontalAlignment.RIGHT)
    private String payMoneyStr;
    private BigDecimal payMoney;
    /** 收入金额 */
    @Excel(name = "*收入金额",align = HorizontalAlignment.RIGHT)
    private String incomeMoneyStr;
    private BigDecimal incomeMoney;
    /** 币别ID */
    private String curId;
    /** 币别 */
    @Excel(name = "币别")
    private String curName;
    /** 交易日期 */
    @Excel(name = "交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String transDateStr;
    private Date transDate;
    /** 交易时间 */
    @Excel(name = "*交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String transTimeStr;
    private Date transTime;
    /** 余额 */
    @Excel(name = "余额",align = HorizontalAlignment.RIGHT)
    private String balStr;
    private BigDecimal bal;
    /** 用途 */
    @Excel(name = "用途")
    private String uses;
    /** 附言 */
    private String postscript;
    /** 摘要 */
    @Excel(name = "摘要")
    private String remark;
    /** 项目信息 */
    @Excel(name = "项目信息")
    private String projectRemark;
    /** 月份 */
    private Integer months;
    /**
     * 银行流水号
     */
    @Excel(name = "银行流水号")
    private String bankSerialNo;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppAccBank() {
        return oppAccBank;
    }
    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public String getCdSignStr() {
        return cdSignStr;
    }
    public void setCdSignStr(String cdSignStr) {
        this.cdSignStr = cdSignStr;
    }
    public BigDecimal getTransMoney() {
        return transMoney;
    }
    public void setTransMoney(BigDecimal transMoney) {
        this.transMoney = transMoney;
    }
    public String getTransMoneyStr() {
        return transMoneyStr;
    }
    public void setTransMoneyStr(String transMoneyStr) {
        this.transMoneyStr = transMoneyStr;
    }
    public String getRbSign() {
        return rbSign;
    }
    public void setRbSign(String rbSign) {
        this.rbSign = rbSign;
    }
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }
    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
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
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public String getUses() {
        return uses;
    }
    public void setUses(String uses) {
        this.uses = uses;
    }
    public String getPostscript() {
        return postscript;
    }
    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getProjectRemark() {
        return projectRemark;
    }
    public void setProjectRemark(String projectRemark) {
        this.projectRemark = projectRemark;
    }
    public Integer getMonths() {
        return months;
    }
    public void setMonths(Integer months) {
        this.months = months;
    }
    public String getBankSerialNo() {
        return bankSerialNo;
    }
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
    public String getPayMoneyStr() {
        return payMoneyStr;
    }
    public void setPayMoneyStr(String payMoneyStr) {
        this.payMoneyStr = payMoneyStr;
    }
    public String getIncomeMoneyStr() {
        return incomeMoneyStr;
    }
    public void setIncomeMoneyStr(String incomeMoneyStr) {
        this.incomeMoneyStr = incomeMoneyStr;
    }
    public Date getTransDate() {
        return transDate;
    }
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }
    public String getTransTimeStr() {
        return transTimeStr;
    }
    public void setTransTimeStr(String transTimeStr) {
        this.transTimeStr = transTimeStr;
    }
    public String getBalStr() {
        return balStr;
    }
    public void setBalStr(String balStr) {
        this.balStr = balStr;
    }
    public String getTransDateStr() {
        return transDateStr;
    }
    public void setTransDateStr(String transDateStr) {
        this.transDateStr = transDateStr;
    }
    @Override
    public String getBeanName() {
        return "btBankAccDtlImportService";
    }
}
