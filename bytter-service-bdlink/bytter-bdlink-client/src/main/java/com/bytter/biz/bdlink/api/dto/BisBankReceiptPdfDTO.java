package com.bytter.biz.bdlink.api.dto;
/**
 * 银行电子回单
 *
 * @author qijh
 */
@SuppressWarnings("serial")
public class BisBankReceiptPdfDTO  implements java.io.Serializable {
	private String id;
	/**
	 * 明细ID
	 */
	private String receiptId;
	/**
	 * 图片内容
	 */
	private byte[] ebillimg;
	/**
	 * PDF转图片blob
	 */
	private byte[] pdfToImg;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public byte[] getEbillimg() {
		return ebillimg;
	}
	public void setEbillimg(byte[] ebillimg) {
		this.ebillimg = ebillimg;
	}
	public byte[] getPdfToImg() {
		return pdfToImg;
	}
	public void setPdfToImg(byte[] pdfToImg) {
		this.pdfToImg = pdfToImg;
	}
}
