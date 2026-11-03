package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
/**
 * @author zuoy
 * @version 1.0
 * @Title: Entity
 * @Description: 银行电子回单表
 * @date 2018-04-09
 */
public class BisBankReceiptDtlHisVO implements  Serializable {
	private String id;
	/**
	 * 开户单位ID
	 */
	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "t.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "t.corp_id")
	private String corpName;
	/**
	 * 付款人账号
	 */
	@BtCompositeSearch(fieldName = "t.pay_acc_no")
	private String payAccNo;
	/**
	 * 付款人账户名称
	 */
	@BtCompositeSearch(fieldName = "t.pay_acc_name")
	private String payAccName;
	/**
	 * 付款人开户行名称
	 */
	private String payBankName;
	/**
	 * 银行编码ID
	 */
	@BtCompositeSearch(fieldName = "t.bif_code_id")
	@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	private String bifName;
	/**
	 * 收款人账号
	 */
	@BtCompositeSearch(fieldName = "t.payee_acc_no")
	private String payeeAccNo;
	/**
	 * 收款人账户名称
	 */
	@BtCompositeSearch(fieldName = "t.payee_acc_name")
	private String payeeAccName;
	/**
	 * 收款人开户行名称
	 */
	private String payeeBankName;
	/**
	 * 交易金额
	 */
	@BtCompositeSearch(fieldName = "t.trans_amt")
	private BigDecimal transAmt;
	/**
	 * 币别ID
	 */
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName = "t.cur_id")
	private String curId;
	private String curName;
	/**
	 * 用途
	 */
	@BtCompositeSearch(fieldName = "t.usefor")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@BtCompositeSearch(fieldName = "t.charage_date")
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
	private Blob pdfToImg;
	/**
	 * 打印次数
	 */
	private Integer prints;
	/**
	 * 借贷标志 0-收入 1-支持
	 */
	@BtCompositeSearch(fieldName = "t.cd_sign")
	@Dict(dictGroup = "btBankAccDtl.cdSign",inject = "cdSignName")
	private String cdSign;
	private String cdSignName;
	/**
	 * 勾兑标志 0-未勾兑，1-已勾兑
	 */
	@BtCompositeSearch(fieldName = "t.BLEND_SIGN")
	@Dict(dictGroup = "paymentblending_status",inject = "blendSignName")
	private String blendSign;
	private String blendSignName;
	/**
	 * 0-银企直联，1-OCR识别，默认0-银企直联
	 */
	@BtCompositeSearch(fieldName = "t.is_from")
	@Dict(dictGroup = "BisBank.Receipt.Dtl",inject = "isFromName")
	private String isFrom;
	private String isFromName;
	/**
	 * 文件路径
	 */
	private String filePath;
	/*
   关联的流水号
    */
	private String dtlId;
	/*
   关联的付款单号
    */
	private String billId;
	/*
    付款单来源
     */
	private String billFrom;
	/**
	 * 本方账号
	 */
	@BtCompositeSearch(fieldName = "t.bank_acc")
	private String bankAcc;
	/**
	 * 本方账户名
	 */
	@BtCompositeSearch(fieldName = "t.acc_name")
	private String accName;
	/**
	 * 本方账户开户行
	 */
	@BtCompositeSearch(fieldName = "t.bank_name")
	private String bankName;
	/**
	 * 对方账号
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_no")
	private String oppAccNo;
	/**
	 * 对方账户名
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_name")
	private String oppAccName;
	/**
	 * 对方账户开户行
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_bank")
	private String oppAccBank;
	private List<BisBankDtlBillVO> bisBankDtlBillVOList;
	public List<BisBankDtlBillVO> getBisBankDtlBillVOList() {
		return bisBankDtlBillVOList;
	}
	public void setBisBankDtlBillVOList(List<BisBankDtlBillVO> bisBankDtlBillVOList) {
		this.bisBankDtlBillVOList = bisBankDtlBillVOList;
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

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
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
	public Blob getPdfToImg() {
		return pdfToImg;
	}
	public void setPdfToImg(Blob pdfToImg) {
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public String getCdSignName() {
		return cdSignName;
	}
	public void setCdSignName(String cdSignName) {
		this.cdSignName = cdSignName;
	}
	public String getBlendSign() {
		return blendSign;
	}
	public void setBlendSign(String blendSign) {
		this.blendSign = blendSign;
	}
	public String getBlendSignName() {
		return blendSignName;
	}
	public void setBlendSignName(String blendSignName) {
		this.blendSignName = blendSignName;
	}
	public String getIsFrom() {
		return isFrom;
	}
	public void setIsFrom(String isFrom) {
		this.isFrom = isFrom;
	}
	public String getIsFromName() {
		return isFromName;
	}
	public void setIsFromName(String isFromName) {
		this.isFromName = isFromName;
	}
}
