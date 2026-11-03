package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
public class FcBusRaceVo implements java.io.Serializable {
	private java.lang.String id;
	/**业务种类代码*/
	@BtCompositeSearch(fieldName = "fc.race_code")
	private java.lang.String raceCode;
	/**业务种类名称*/
	@BtCompositeSearch(fieldName = "fc.race_name")
	private java.lang.String raceName;
	private java.lang.String raceNameShow;
	/**系统业务种类*/
	@BtCompositeSearch(fieldName = "fc.sys_race_code")
	private java.lang.String sysRaceCode;
	private java.lang.String sysRaceId;
	private java.lang.String sysRaceName;
	/**上级业务种类*/
	@BtCompositeSearch(fieldName = "fc.father_id")
	private java.lang.String fatherId;
	/**所属网点*/
	@BtCompositeSearch(fieldName = "fc.net_id")
	private java.lang.String netId;
	private java.lang.String netName;
	
	private java.lang.String ids;
	private java.lang.String netIds;
	
	/**有效标识 0 无效 1 有效*/
	@BtCompositeSearch(fieldName = "fc.valid_Sign")
	private java.lang.String validSign;
	private String poolId;
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
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
	public java.lang.String getSysRaceName() {
		return sysRaceName;
	}
	public void setSysRaceName(java.lang.String sysRaceName) {
		this.sysRaceName = sysRaceName;
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
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public String getSysRaceId() {
		return sysRaceId;
	}
	public void setSysRaceId(String sysRaceId) {
		this.sysRaceId = sysRaceId;
	}
	public String getRaceNameShow() {
		return raceNameShow;
	}
	public void setRaceNameShow(String raceNameShow) {
		this.raceNameShow = raceNameShow;
	}
}
