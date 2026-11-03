package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * 摘要vo
 * @author songkk
 *
 */
@SuppressWarnings("serial")
public class BtSummaryVo implements java.io.Serializable {
	/**摘要id*/
	private java.lang.String id;
	/**摘要内容*/
	@BtCompositeSearch(fieldName="summary_Content")
	private java.lang.String summaryContent;
	/**备注*/
	private java.lang.String rmk;
	/**有效标识 0 无效 1 有效*/
	@BtCompositeSearch(fieldName="valid_Sign")
	private java.lang.String validSign;
	/**用途分类*/
	@BtCompositeSearch(fieldName="usingtype")
	private java.lang.String usingtype;
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
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
	
	
}
