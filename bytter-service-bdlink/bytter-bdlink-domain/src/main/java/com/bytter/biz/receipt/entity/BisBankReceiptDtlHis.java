package com.bytter.biz.receipt.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Entity
@Table(name = "BIS_BANK_RECEIPT_DTL_HIS", schema = "")
@SuppressWarnings("serial")
public class BisBankReceiptDtlHis extends IdEntity implements java.io.Serializable {
	/**
	 * 付款人账号
	 */
	private String payAccNo;
	/**
	 * 付款人账户名称
	 */
	private String payAccName;
	/**
	 * 付款人开户行名称
	 */
	private String payBankName;
	/**
	 * 银行编码ID
	 */
	private String bifCode;
	/**
	 * 收款人账号
	 */
	private String payeeAccNo;
	/**
	 * 收款人账户名称
	 */
	private String payeeAccName;
	/**
	 * 收款人开户行名称
	 */
	private String payeeBankName;
	/**
	 * 交易金额
	 */
	private BigDecimal transAmt;
	/**
	 * 币别ID
	 */
	private String curId;
	/**
	 * 用途
	 */
	private String usefor;
	/**
	 * 摘要
	 */
	private String abs;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 业务类型
	 */
	private String busType;
	/**
	 * 业务编号
	 */
	private String busNo;
	/**
	 * 记账日期
	 */
	private Date charageDate;
	/**
	 * 回单编号
	 */
	private String receiptNo;
	/**
	 * 回单验证码
	 */
	private String receiptCheckCode;
	/**
	 * 交易流水号
	 */
	private String transSerialNo;
	/**
	 * 网点号
	 */
	private String netCode;
	/**
	 * 柜员号
	 */
	private String tellerNo;
	/**
	 * 文件类型：pdf,jpg,png等
	 */
	private String filetype;
	/**
	 * 打印次数
	 */
	private Integer prints;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 借贷标志 0-收入 1-支持
	 */
	private String cdSign;
	@Column(name ="PAY_ACC_NO")
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	@Column(name ="PAY_ACC_NAME")
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	@Column(name ="PAY_BRANCH_NAME")
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	@Column(name ="BIF_CODE_ID")
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	@Column(name ="PAYEE_ACC_NO")
	public String getPayeeAccNo() {
		return payeeAccNo;
	}
	public void setPayeeAccNo(String payeeAccNo) {
		this.payeeAccNo = payeeAccNo;
	}
	@Column(name ="PAYEE_ACC_NAME")
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	@Column(name ="PAYEE_BRACN_NAME")
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	@Column(name ="TRANS_AMT")
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	@Column(name ="CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name ="USEFOR")
	public String getUsefor() {
		return usefor;
	}
	public void setUsefor(String usefor) {
		this.usefor = usefor;
	}
	@Column(name ="ABS")
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	@Column(name ="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name ="BUS_TYPE")
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	@Column(name ="BUS_NO")
	public String getBusNo() {
		return busNo;
	}
	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}
	@Column(name ="CHARAGE_DATE")
	public Date getCharageDate() {
		return charageDate;
	}
	public void setCharageDate(Date charageDate) {
		this.charageDate = charageDate;
	}
	@Column(name ="RECEIPT_NO")
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	@Column(name ="RECEIPT_CHECK_CODE")
	public String getReceiptCheckCode() {
		return receiptCheckCode;
	}
	public void setReceiptCheckCode(String receiptCheckCode) {
		this.receiptCheckCode = receiptCheckCode;
	}
	@Column(name ="TRANS_SERIAL_NO")
	public String getTransSerialNo() {
		return transSerialNo;
	}
	public void setTransSerialNo(String transSerialNo) {
		this.transSerialNo = transSerialNo;
	}
	@Column(name ="NET_CODE")
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	@Column(name ="TELLER_NO")
	public String getTellerNo() {
		return tellerNo;
	}
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}
	@Column(name ="FILETYPE")
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	@Column(name ="PRINTS")
	public Integer getPrints() {
		return prints;
	}
	public void setPrints(Integer prints) {
		this.prints = prints;
	}
	@Column(name ="FILE_PATH")
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Column(name ="CD_SIGN")
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
}
