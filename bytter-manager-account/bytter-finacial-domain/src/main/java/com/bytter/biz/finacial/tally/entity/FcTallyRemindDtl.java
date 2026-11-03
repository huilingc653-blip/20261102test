package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: BT_NET_CHALK
 * @author onlineGenerator
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_TALLY_REMIND_DTL", schema = "")
@SuppressWarnings("serial")
public class FcTallyRemindDtl extends IdEntity implements java.io.Serializable {
	private String billId;
	@Column(name ="BILL_ID")
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
}
