package com.bytter.biz.receipt.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Blob;
/**
 * 银行电子回单附件关联表
 *
 * @author zuoy 2018-04-09
 */
@Entity
@Table(name = "E_BANK_BILL_ATTACHMENT", schema = "")
@SuppressWarnings("serial")
public class EBankBillAttachment extends IdEntity implements java.io.Serializable {
	/**
	 * 银行电子回单ID
	 */
	private String bankBillId;
	/**
	 * 图片附件
	 */
	private Blob attachmentPhoto;
	@Column(name = "BANK_BILL_ID", nullable = true)
	public String getBankBillId() {
		return bankBillId;
	}
	public void setBankBillId(String bankBillId) {
		this.bankBillId = bankBillId;
	}
	@Column(name = "ATTACHMENT_PHOTO", nullable = true)
	public Blob getAttachmentPhoto() {
		return attachmentPhoto;
	}
	public void setAttachmentPhoto(Blob attachmentPhoto) {
		this.attachmentPhoto = attachmentPhoto;
	}
}
