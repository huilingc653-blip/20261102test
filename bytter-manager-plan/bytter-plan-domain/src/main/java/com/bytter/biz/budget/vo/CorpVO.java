package com.bytter.biz.budget.vo;
import java.io.Serializable;
/**
 * @author zhangxb
 * @date 2017-08-03
 */
public class CorpVO implements Serializable{
	private String id;
	/**
	 * 单位ID
	 */
	private String corpId;
	private String corpName;
	private String corpCode;
	private String corpParentId;
	private Integer corpType;
	private String corpNetId;
	/**
	 * 下级单位数量
	 */
	private Integer childCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpParentId() {
		return corpParentId;
	}
	public void setCorpParentId(String corpParentId) {
		this.corpParentId = corpParentId;
	}
	public Integer getCorpType() {
		return corpType;
	}
	public void setCorpType(Integer corpType) {
		this.corpType = corpType;
	}
	public String getCorpNetId() {
		return corpNetId;
	}
	public void setCorpNetId(String corpNetId) {
		this.corpNetId = corpNetId;
	}
	public Integer getChildCount() {
		return childCount;
	}
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
	
}
