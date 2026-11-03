package com.bytter.biz.finacial.report.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
public class VFcAccbookSearchVO implements Serializable {
	/** 摘要 */
	@BtCompositeSearch(fieldName="fab.rmk")
	private java.lang.String summaryContent;
	/**借方发生额*/
	@BtCompositeSearch(fieldName="fab.debitMoney")
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	@BtCompositeSearch(fieldName = "fab.creditMoney")
	private java.math.BigDecimal creditMoney;
	/** 记账人 */
	@BtCompositeSearch(fieldName="fab.operatorCode")
	private java.lang.String operatorLoginName;
	/** 复核人 */
	@BtCompositeSearch(fieldName="fab.checkerCode")
	private java.lang.String checkLoginName;
	
	public java.lang.String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(java.lang.String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public java.math.BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(java.math.BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public java.math.BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public java.lang.String getOperatorLoginName() {
		return operatorLoginName;
	}
	public void setOperatorLoginName(java.lang.String operatorLoginName) {
		this.operatorLoginName = operatorLoginName;
	}
	public java.lang.String getCheckLoginName() {
		return checkLoginName;
	}
	public void setCheckLoginName(java.lang.String checkLoginName) {
		this.checkLoginName = checkLoginName;
	}
	
	
}
