package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import java.io.Serializable;
import java.util.Date;
public class FcChargeRuleVo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	
	private String voucherId;
	private String voucherType;
	private String sysRaceId;
	@BtCompositeSearch(fieldName = "fcr.race_code")
	private String raceCode;
	
	private String fsSubjId;
	private String debitSubjCode;
	
	private String fssSubjId;
	private String creditSubjCode;
	
	private String summary;
	@BtCompositeSearch(fieldName = "fcr.net_id")
	private String netId;
	private Date create_time;
	private String create_by;
	private Date update_time;
	private String update_by;
	/**币别*/
	private String curId;
	private java.lang.String curName;
	private java.lang.String curCode;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
	public String getDebitSubjCode() {
		return debitSubjCode;
	}
	public void setDebitSubjCode(String debitSubjCode) {
		this.debitSubjCode = debitSubjCode;
	}
	public String getCreditSubjCode() {
		return creditSubjCode;
	}
	public void setCreditSubjCode(String creditSubjCode) {
		this.creditSubjCode = creditSubjCode;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getFsSubjId() {
		return fsSubjId;
	}
	public void setFsSubjId(String fsSubjId) {
		this.fsSubjId = fsSubjId;
	}
	public String getFssSubjId() {
		return fssSubjId;
	}
	public void setFssSubjId(String fssSubjId) {
		this.fssSubjId = fssSubjId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getSysRaceId() {
		return sysRaceId;
	}
	public void setSysRaceId(String sysRaceId) {
		this.sysRaceId = sysRaceId;
	}
}
