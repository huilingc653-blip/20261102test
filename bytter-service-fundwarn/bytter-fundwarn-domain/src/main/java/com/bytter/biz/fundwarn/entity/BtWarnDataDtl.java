package com.bytter.biz.fundwarn.entity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 预警策略数据明细信息
 * @author zhengg
 * @date 2018-10-12
 */
@Entity
@Table(name = "BT_WARN_DATA_DTL", schema = "")
@SuppressWarnings("serial")
public class BtWarnDataDtl extends IdEntity implements java.io.Serializable {
	/** DATA_ID VARCHAR2(32)主数据ID */
	private String dataId;
	/** NO VARCHAR2(50)单号 */
	private String no;
	/** PAYER_ACC VARCHAR2(50)付款账号 */
	private String payerAcc;
	/** PAYER_NAME VARCHAR2(200)付款户名 */
	private String payerName;
	/** PAYER_BANK_NAME VARCHAR2(200)付款银行名称 */
	private String payerBankName;
	/** PAY_MONEY NUMBER(15,2)交易金额 */
	private BigDecimal payMoney;
	/** TRANS_DATE TIMESTAMP(6)交易日期 */
	private Date transDate;
	/** PAYEE_ACC VARCHAR2(50)收款账号 */
	private String payeeAcc;
	/** PAYEE_NAME VARCHAR2(200)收款户名 */
	private String payeeName;
	/** PAYEE_BANK_NAME VARCHAR2(200)收款银行名称 */
	private String payeeBankName;
	/** USE VARCHAR2(500)用途 */
	private String purpose;
	/** CONDITION VARCHAR2(200)触发条件 */
	private String condition;
	/** EXCESS_MONEY NUMBER(15,2)超额金额 */
	private BigDecimal excessMoney;
	/** CREATE_DATE TIMESTAMP(6)创建日期 */
	private Date createDate;
	/** MONTH_NUM NUMBER(5)月份 */
	private Integer monthNum;
	
	private String corpId;
	@Transient
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "DATA_ID")
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	@Column(name = "NO")
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Column(name = "PAYER_ACC")
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	@Column(name = "PAYER_NAME")
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	@Column(name = "PAYER_BANK_NAME")
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	@Column(name = "PAY_MONEY")
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	@Column(name = "TRANS_DATE")
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	@Column(name = "PAYEE_ACC")
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	@Column(name = "PAYEE_NAME")
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	@Column(name = "PAYEE_BANK_NAME")
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	@Column(name = "PURPOSE")
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Column(name = "WARN_CONDITION")
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Column(name = "EXCESS_MONEY")
	public BigDecimal getExcessMoney() {
		return excessMoney;
	}
	public void setExcessMoney(BigDecimal excessMoney) {
		this.excessMoney = excessMoney;
	}
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "MONTH_NUM")
	public Integer getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	
	@Override
	public String toString() {
		return "BtWarnDataDtl [dataId=" + dataId + ", no=" + no + ", payerAcc=" + payerAcc + ", payerName=" + payerName
				+ ", payerBankName=" + payerBankName + ", payMoney=" + payMoney + ", transDate=" + transDate
				+ ", payeeAcc=" + payeeAcc + ", payeeName=" + payeeName + ", payeeBankName=" + payeeBankName + ", purpose="
				+ purpose + ", condition=" + condition + ", excessMoney=" + excessMoney + ", createDate=" + createDate
				+ "]";
	}
}
