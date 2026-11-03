package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
public class FcBusRaceVo implements java.io.Serializable {
	@Dict(tableName="FC_BUS_RACE",valueFieldName="id",showFiledName="RACE_NAME",inject="raceName")
	private String id;
	/**业务种类代码*/
	@BtCompositeSearch(fieldName = "fc.race_code")
	private String raceCode;
	/**业务种类名称*/
	@BtCompositeSearch(fieldName = "fc.race_name")
	private String raceName;
	private String raceNameShow;
	/**系统业务种类*/
	@BtCompositeSearch(fieldName = "fc.sys_race_code")
	private String sysRaceCode;
	@Dict(tableName="FC_SYS_BUS_RACE",valueFieldName="id",showFiledName="SYS_RACE_NAME",inject="sysRaceName")
	private String sysRaceId;
	private String sysRaceName;
	/**上级业务种类*/
	@BtCompositeSearch(fieldName = "fc.father_id")
	private String fatherId;
	/**所属网点*/
	@BtCompositeSearch(fieldName = "fc.net_id")
	@Corp(inject="netName")
	private String netId;
	private String netName;
	
	private String ids;
	private String netIds;
	
	/**有效标识 0 无效 1 有效*/
	@BtCompositeSearch(fieldName = "fc.valid_Sign")
	private String validSign;
	
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
	
	public String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
	public String getRaceName() {
		return raceName;
	}
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getSysRaceName() {
		return sysRaceName;
	}
	public void setSysRaceName(String sysRaceName) {
		this.sysRaceName = sysRaceName;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getNetIds() {
		return netIds;
	}
	public void setNetIds(String netIds) {
		this.netIds = netIds;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
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
