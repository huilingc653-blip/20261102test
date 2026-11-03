package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据下拨流水关联信息
 * @author onlineGenerator
 * @date 2017-11-1 16:40:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_TRANSFER_DTL", schema = "")
@SuppressWarnings("serial")
public class AbBillTransferDtl extends IdEntity implements java.io.Serializable {
	/**下拨单id*/
	private String transferId;
	/**票据流水ID*/
	private String billAid;
	@Column(name ="TRANSFER_ID",nullable=true,length=60)
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	@Column(name ="BILL_AID",nullable=true,length=60)
	public String getBillAid() {
		return billAid;
	}
	public void setBillAid(String billAid) {
		this.billAid = billAid;
	}
}
