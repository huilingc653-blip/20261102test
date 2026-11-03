package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
public class FcBankBillSearchVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 开始发生日期
     */
    @BtCompositeSearch(operCode = ">=", fieldName = "a.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date importDateStart;
    /**
     * 结束发生日期
     */
    @BtCompositeSearch(operCode = "<=", fieldName = "a.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date importDateEnd;
    
    /**
     * 发生日期
     */
    @BtCompositeSearch(operCode = "=", fieldName = "a.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate ;    
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
     * 对账标志
     */
    @BtCompositeSearch(operCode = "=", fieldName = "a.compare_Sign")
    private String compareSign;
    
    /**
     * 输入类型
     */
    @BtCompositeSearch(operCode = "=", fieldName = "a.import_Type")
    private String importType;
    
    private String bankAccTemp;
    private String curId;
	public Date getImportDateStart() {
		return importDateStart;
	}
	public void setImportDateStart(Date importDateStart) {
		this.importDateStart = importDateStart;
	}
	public Date getImportDateEnd() {
		return importDateEnd;
	}
	public void setImportDateEnd(Date importDateEnd) {
		this.importDateEnd = importDateEnd;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public String getCompareSign() {
		return compareSign;
	}
	public void setCompareSign(String compareSign) {
		this.compareSign = compareSign;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getBankAccTemp() {
		return bankAccTemp;
	}
	public void setBankAccTemp(String bankAccTemp) {
		this.bankAccTemp = bankAccTemp;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
}
