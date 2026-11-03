package com.bytter.biz.bdlink.vo;
import java.io.Serializable;
import java.sql.Blob;
/**
 * @Title: vo
 * @description: 银行电子回单附件
 * @author: qijh
 * @create: 2019-12-06 17:41
 **/
public class BisBankReceiptPdfVO implements  Serializable {
    private String id;
    /**
     * 明细ID
     */
    private String receiptId;
    /**
     * 图片内容
     */
    private Blob ebillimg;
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
    public Blob getEbillimg() {
        return ebillimg;
    }
    public void setEbillimg(Blob ebillimg) {
        this.ebillimg = ebillimg;
    }
    public byte[] getPdfToImg() {
        return pdfToImg;
    }
    public void setPdfToImg(byte[] pdfToImg) {
        this.pdfToImg = pdfToImg;
    }
}
