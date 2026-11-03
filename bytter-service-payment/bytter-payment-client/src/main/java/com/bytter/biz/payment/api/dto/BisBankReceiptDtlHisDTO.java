package com.bytter.biz.payment.api.dto;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
public class BisBankReceiptDtlHisDTO {
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
    private byte[] ebillimg;
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
     * 0-银企直联，1-OCR识别，默认0-银企直联
     */
    private String isFrom;
    /**
     * OCR状态
     */
    private String status;
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
    public byte[] getEbillimg() {
        return ebillimg;
    }
    public void setEbillimg(byte[] ebillimg) {
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
    public String getIsFrom() {
        return isFrom;
    }
    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
