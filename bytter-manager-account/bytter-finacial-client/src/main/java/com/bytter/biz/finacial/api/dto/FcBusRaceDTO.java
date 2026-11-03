package com.bytter.biz.finacial.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
public class FcBusRaceDTO implements java.io.Serializable{
	private String id;
	/**业务种类代码*/
	@BtCompositeSearch(fieldName = "fc.race_code")
	private java.lang.String raceCode;
	/**业务种类名称*/
	@BtCompositeSearch(fieldName = "fc.race_name")
	private java.lang.String raceName;
	/**系统业务种类*/
	@BtCompositeSearch(fieldName = "fc.sys_race_code")
	private java.lang.String sysRaceCode;
	/**上级业务种类*/
	@BtCompositeSearch(fieldName = "fc.father_id")
	private java.lang.String fatherId;
	/**所属网点*/
	@BtCompositeSearch(fieldName = "fc.net_id")
	private String netId;
	private String netName;
	
	private java.lang.String ids;
	private java.lang.String netIds;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	
	public java.lang.String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(java.lang.String raceCode) {
		this.raceCode = raceCode;
	}
	public java.lang.String getRaceName() {
		return raceName;
	}
	public void setRaceName(java.lang.String raceName) {
		this.raceName = raceName;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(java.lang.String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public java.lang.String getFatherId() {
		return fatherId;
	}
	public void setFatherId(java.lang.String fatherId) {
		this.fatherId = fatherId;
	}
	public java.lang.String getIds() {
		return ids;
	}
	public void setIds(java.lang.String ids) {
		this.ids = ids;
	}
	public java.lang.String getNetIds() {
		return netIds;
	}
	public void setNetIds(java.lang.String netIds) {
		this.netIds = netIds;
	}
	
}
