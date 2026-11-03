package com.bytter.biz.bdlink.api.dto;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
/**
 * 银行电子回单
 *
 * @author qijh
 */
@SuppressWarnings("serial")
public class BisBankReceiptDtlDTO  implements java.io.Serializable {
	private String id;
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
	 * 图片内容
	 */
	private Blob ebillimg;
	/**
	 * 文件类型：pdf,jpg,png等
	 */
	private String filetype;
	/**
	 * PDF转图片blob
	 */
	private byte[] pdfToImg;
	/**
	 * 打印次数
	 */
	private Integer prints;
	/**
	 * 借贷标志 0-收入 1-支出
	 */
	private String cdSign;

	/**
	 * '电子回单保存类型:1,保存数据库,2,保存FTP'
	 */
	private String saveType;
	/**
	 * 电子回单文件保存路径
	 */
	private String filePath;

	/**
	 * 关联的电子回单大字段表id集合
	 */
	private String pdfId;

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPdfId() {
		return pdfId;
	}

	public void setPdfId(String pdfId) {
		this.pdfId = pdfId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getPayeeAccNo() {
		return payeeAccNo;
	}
	public void setPayeeAccNo(String payeeAccNo) {
		this.payeeAccNo = payeeAccNo;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getUsefor() {
		return usefor;
	}
	public void setUsefor(String usefor) {
		this.usefor = usefor;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusNo() {
		return busNo;
	}
	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}
	public Date getCharageDate() {
		return charageDate;
	}
	public void setCharageDate(Date charageDate) {
		this.charageDate = charageDate;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getReceiptCheckCode() {
		return receiptCheckCode;
	}
	public void setReceiptCheckCode(String receiptCheckCode) {
		this.receiptCheckCode = receiptCheckCode;
	}
	public String getTransSerialNo() {
		return transSerialNo;
	}
	public void setTransSerialNo(String transSerialNo) {
		this.transSerialNo = transSerialNo;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String getTellerNo() {
		return tellerNo;
	}
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}
	public Blob getEbillimg() {
		return ebillimg;
	}
	public void setEbillimg(Blob ebillimg) {
		this.ebillimg = ebillimg;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public byte[] getPdfToImg() {
		return pdfToImg;
	}
	public void setPdfToImg(byte[] pdfToImg) {
		this.pdfToImg = pdfToImg;
	}
	public Integer getPrints() {
		return prints;
	}
	public void setPrints(Integer prints) {
		this.prints = prints;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
}
