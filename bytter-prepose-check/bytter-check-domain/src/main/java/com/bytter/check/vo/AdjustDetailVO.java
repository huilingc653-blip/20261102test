package com.bytter.check.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class AdjustDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**摘要*/
	private String summaryContent;
	/**凭证号码*/
	private String voucherNo;
	/**金额*/
	private BigDecimal money;
	/**查对记录*/
	private String checkRecord;
	/**单据号*/
	private String billNo;
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getCheckRecord() {
		return checkRecord;
	}
	public void setCheckRecord(String checkRecord) {
		this.checkRecord = checkRecord;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
