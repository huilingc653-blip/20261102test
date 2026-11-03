package com.bytter.biz.finacial.api.dto;
import java.io.Serializable;
public class BtSummaryDTO implements Serializable{
	private String id;
	/**摘要代码*/
	private java.lang.String summaryCode;
	/**摘要内容*/
	private java.lang.String summaryContent;
	/**备注*/
	private java.lang.String rmk;
	/**有效标识 0 无效 1 有效*/
	private java.lang.String validSign;
	/**用途分类*/
	private java.lang.String usingtype;
	/**createTime*/
	private java.util.Date createTime;
	/**createBy*/
	private java.lang.String createBy;
	/**updateTime*/
	private java.util.Date updateTime;
	/**updateBy*/
	private java.lang.String updateBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getSummaryCode() {
		return summaryCode;
	}
	public void setSummaryCode(java.lang.String summaryCode) {
		this.summaryCode = summaryCode;
	}
	public java.lang.String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(java.lang.String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public java.lang.String getUsingtype() {
		return usingtype;
	}
	public void setUsingtype(java.lang.String usingtype) {
		this.usingtype = usingtype;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	
}
