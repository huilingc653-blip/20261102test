package com.bytter.biz.fundwarn.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Description: 预警策略明细数据信息VO
 * @author zhengg
 * @date 2018-10-15
 */
public class BtWarnDataDtlInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主数据ID */
	@BtCompositeSearch(fieldName="a.data_Id")
	private String dataId;
	/** 单位ID */
	@BtCompositeSearch(fieldName="b.CORP_ID")
	private String corpId;
	/** 单位名称 */
	@BtCompositeSearch(fieldName="b.CORP_NAME")
	private String corpName;
	/** 预警币别 */
	private String curId;
	/** 预警币别名称 */
	@BtCompositeSearch(fieldName="a.cur_Id")
	private String curName;
	
	/** 单号 */
	@BtCompositeSearch(fieldName="a.no")
	private String no;
	/** 付款账号 */
	@BtCompositeSearch(fieldName="a.payer_Acc")
	private String payerAcc;
	/** 付款户名 */
	@BtCompositeSearch(fieldName="a.payer_Name")
	private String payerName;
	/** 付款银行名称 */
	@BtCompositeSearch(fieldName="a.payer_Bank_Name")
	private String payerBankName;
	/** 交易金额 */
	@BtCompositeSearch(fieldName="a.pay_Money")
	private BigDecimal payMoney;
	/** 交易日期 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="a.trans_Date")
	private Date transDate;
	/** 收款账号 */
	@BtCompositeSearch(fieldName="a.payee_Acc")
	private String payeeAcc;
	/** 收款户名 */
	@BtCompositeSearch(fieldName="a.payee_Name")
	private String payeeName;
	/** 收款银行名称 */
	@BtCompositeSearch(fieldName="a.payee_Bank_Name")
	private String payeeBankName;
	/** 用途 */
	@BtCompositeSearch(fieldName="a.purpose")
	private String purpose;
	/** 触发条件 */
	@BtCompositeSearch(fieldName="a.warn_condition")
	private String condition;
	/** 超额金额 */
	@BtCompositeSearch(fieldName="a.excess_Money")
	private BigDecimal excessMoney;
	/** 创建日期 */
	@BtCompositeSearch(fieldName="a.create_Date")
	private Date createDate;
	/** 月份 */
	@BtCompositeSearch(fieldName="a.month_Num")
	private Integer monthNum;
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public BigDecimal getExcessMoney() {
		return excessMoney;
	}
	public void setExcessMoney(BigDecimal excessMoney) {
		this.excessMoney = excessMoney;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	@Override
	public String toString() {
		return "BtWarnDataDtlInfoVO [dataId=" + dataId + ", corpId=" + corpId + ", corpName=" + corpName + ", curId="
				+ curId + ", curName=" + curName + ", no=" + no + ", payerAcc=" + payerAcc + ", payerName=" + payerName
				+ ", payerBankName=" + payerBankName + ", payMoney=" + payMoney + ", transDate=" + transDate
				+ ", payeeAcc=" + payeeAcc + ", payeeName=" + payeeName + ", payeeBankName=" + payeeBankName + ", purpose="
				+ purpose + ", condition=" + condition + ", excessMoney=" + excessMoney + ", createDate=" + createDate
				+ ", monthNum=" + monthNum + "]";
	}
	
}
