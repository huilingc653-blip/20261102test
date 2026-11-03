package com.bytter.biz.receipt.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
/**
 * 银行电子回单
 *
 * @author huangx 2019-10-08
 */
@Entity
@Table(name = "BIS_BANK_RECEIPT_PDF", schema = "")
@SuppressWarnings("serial")
public class BisBankReceiptPdf extends IdEntity implements java.io.Serializable {
	/**
	 * 明细ID
	 */
	private String receiptId;
	/**
	 * 图片内容
	 */
	private byte[]  ebillimg;
	/**
	 * PDF转图片blob
	 */
	private byte[] pdfToImg;
	@Column(name ="RECEIPT_ID")
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	@Column(name ="EBILLIMG")
	public byte[]  getEbillimg() {
		return ebillimg;
	}
	public void setEbillimg(byte[]  ebillimg) {
		this.ebillimg = ebillimg;
	}
	@Column(name ="PDF_TO_IMG")
	public byte[] getPdfToImg() {
		return pdfToImg;
	}
	public void setPdfToImg(byte[] pdfToImg) {
		this.pdfToImg = pdfToImg;
	}
}
