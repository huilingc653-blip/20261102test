package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
/**
 * 记账规则列表显示视图
 * @author liyh
 *
 */
public class FcBusinessRuleDefinePoolListVo implements java.io.Serializable {
	private String id;
	
	/** 所属网点 */
	private String netId;
	private String poolId;
	/**
	 * 网点编码
	 */
	private String netCode;
	
	/**
	 * 网点名称
	 */
//	@BtCompositeSearch(fieldName="sc.pool_name")
	private String netName;
	
	
	/** 系统业务品种 */
	@BtCompositeSearch(fieldName="fs.sys_race_code")
	private String sysRaceCode;
	/** 系统业务品种名称*/
	private String sysRaceId;
	@BtCompositeSearch(fieldName="fs.sys_race_name")
	private String sysRaceName;
	/**科目I*/
	private String subjId;
	/** 指定科目 */
	private String subjCode;
	/** 科目名称 */
	private String subName;
	/**币别Id*/
	private String curId;
	/** 币别代码 */
	private String curCode;
	/** 币别名称*/
	private String curName;
	/** 有效标志 */
	private String validSign;
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
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
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
	public String getSubjCode() {
		return subjCode;
	}
	public void setSubjCode(String subjCode) {
		this.subjCode = subjCode;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getSubjId() {
		return subjId;
	}
	public void setSubjId(String subjId) {
		this.subjId = subjId;
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
