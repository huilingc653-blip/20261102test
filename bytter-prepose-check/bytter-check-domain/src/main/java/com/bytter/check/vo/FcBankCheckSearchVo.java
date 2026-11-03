package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcBankCheckSearchVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * 结束日期
     */
    @BtCompositeSearch(operCode = "<=", fieldName = "a.tally_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transTime;
    /**
     * 中心账号
     */
    @BtCompositeSearch(operCode = "LR%", fieldName = "a.account")
    private String account;
    /**
     * 银行帐号
     */
    @BtCompositeSearch(operCode = "LR%", fieldName = "a.bank_Acc")
    private String bankAcc;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 网点id
     */
    private String netId;
    /**
     * 核对标准
     */
    @BtCompositeSearch(operCode = "=", fieldName = "a.import_Type")
    private String checkType;
    private String importType;
    
    
    /**
     * 币别
     */
    private String curId;
    /**
     * 中心账面余额
     */
    private BigDecimal blnBalance;
    /**
     * 银行账面余额
     */
    private BigDecimal bankBalance;
    
    /**
     * 银企对账类型
     */
    private String paramType;
    
    /**
     * 是否生成余额调节汇总表
     */
    private boolean isGenerateSum = false;
    /**
     * 中心初始余额（只用记录初始化余额）
     */
    private BigDecimal blnInitBalance;
    /**
     * 银行初始余额（只用记录初始化余额）
     */
    private BigDecimal bankInitBalance;
    public String getImportType() {
        return importType;
    }
    public void setImportType(String importType) {
        this.importType = importType;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
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
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    
    public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
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
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
    public boolean isGenerateSum() {
        return isGenerateSum;
    }
    public void setGenerateSum(boolean isGenerateSum) {
        this.isGenerateSum = isGenerateSum;
    }
    public BigDecimal getBlnInitBalance() {
        return blnInitBalance;
    }
    public void setBlnInitBalance(BigDecimal blnInitBalance) {
        this.blnInitBalance = blnInitBalance;
    }
    public BigDecimal getBankInitBalance() {
        return bankInitBalance;
    }
    public void setBankInitBalance(BigDecimal bankInitBalance) {
        this.bankInitBalance = bankInitBalance;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
}
