package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: 票据账户余额明细Entity
 * @Description: AB_BILL_ACC_DTL
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_ACC_DTL", schema = "")
@SuppressWarnings("serial")
public class AbBillAccDtl extends IdEntity implements java.io.Serializable {
	/**票据流水表ID*/
	private String recordId;
	/**流水标志0借1贷2冻结*/
	private String cdSign;
	/**有效标记0无效1有效*/
	private String validSign;
	/**交易时间*/
	private Date transTime;
	/**解冻时间*/
	private Date thawTime;
	/**交易金额*/
	private BigDecimal money;
	/**创建人ID*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人ID*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**票据流水表ID*/
	@Column(name ="RECORD_ID",nullable=true,length=32)
	public String getRecordId() {
		return recordId;
	}
	/**票据流水表ID*/
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**流水标志0借1贷2冻结*/
	@Column(name ="CD_SIGN",nullable=true,length=1)
	public String getCdSign() {
		return cdSign;
	}
	/**流水标志0借1贷2冻结*/
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	/**有效标记0无效1有效*/
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign() {
		return validSign;
	}
	/**有效标记0无效1有效*/
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	/**交易时间*/
	@Column(name ="TRANS_TIME",nullable=true)
	public Date getTransTime() {
		return transTime;
	}
	/**交易时间*/
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	/**交易金额*/
	@Column(name ="MONEY",nullable=true,scale=2,length=19)
	public BigDecimal getMoney() {
		return money;
	}
	/**交易金额*/
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**创建人ID*/
	@Column(name ="CREATE_BY",nullable=true)
	public String getCreateBy() {
		return createBy;
	}
	/**创建人ID*/
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**创建日期*/
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate() {
		return createDate;
	}
	/**创建日期*/
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name ="UPDATE_BY",nullable=true)
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name ="THAW_TIME",nullable=true)
	public Date getThawTime() {
		return thawTime;
	}
	public void setThawTime(Date thawTime) {
		this.thawTime = thawTime;
	}
	@Override
	public String toString() {
		return "AbBillAccDtl{" +
				"recordId='" + recordId + '\'' +
				", cdSign='" + cdSign + '\'' +
				", validSign='" + validSign + '\'' +
				", transTime=" + transTime +
				", money=" + money +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}
