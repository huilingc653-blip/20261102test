package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
/**
 * 记账规则列表显示视图
 * @author liyh
 *
 */
public class FcBusinessRuleDefineListVo implements java.io.Serializable {
	private String id;
	
	/** 所属网点 */
	private java.lang.String netId;
	
	/**
	 * 网点编码
	 */
	private String netCode;
	
	/**
	 * 网点名称
	 */
	private String netName;
	private String corpId;
	
	
	/** 系统业务品种 */
	@BtCompositeSearch(fieldName="fs.sys_race_code")
	private java.lang.String sysRaceCode;
	/** 系统业务品种名称*/
	private String sysRaceId;
	@BtCompositeSearch(fieldName="fs.sys_race_name")
	private java.lang.String sysRaceName;
	/**科目I*/
	private String subjId;
	/** 指定科目 */
	private java.lang.String subjCode;
	/** 科目名称 */
	private java.lang.String subName;
	/**币别Id*/
	private String curId;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 币别名称*/
	private java.lang.String curName;
	/** 有效标志 */
	private java.lang.String validSign;
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
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
	public java.lang.String getSubjCode() {
		return subjCode;
	}
	public void setSubjCode(java.lang.String subjCode) {
		this.subjCode = subjCode;
	}
	public java.lang.String getSubName() {
		return subName;
	}
	public void setSubName(java.lang.String subName) {
		this.subName = subName;
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
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
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
