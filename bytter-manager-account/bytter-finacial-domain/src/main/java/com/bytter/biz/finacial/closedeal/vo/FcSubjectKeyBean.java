package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
public class FcSubjectKeyBean implements Serializable {
	/** 网点代码 */
	private java.lang.String netCode;
	/** 科目代码 */
	private java.lang.String subjectCode;
	/** 币别代码 */
	private java.lang.String currencyCode;
	public FcSubjectKeyBean() {
	}
	public FcSubjectKeyBean(String netCode, String subjectCode, String currencyCode) {
		super();
		this.netCode = netCode;
		this.subjectCode = subjectCode;
		this.currencyCode = currencyCode;
	}
	public java.lang.String getNetCode() {
		return netCode;
	}
	public void setNetCode(java.lang.String netCode) {
		this.netCode = netCode;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.lang.String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(java.lang.String currencyCode) {
		this.currencyCode = currencyCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.netCode.hashCode();
		hash = hash * prime + this.subjectCode.hashCode();
		hash = hash * prime + this.currencyCode.hashCode();
		return hash;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FcSubjectKeyBean)) {
			return false;
		}
		FcSubjectKeyBean bean = (FcSubjectKeyBean) obj;
		return this.netCode.equals(bean.getNetCode()) && this.subjectCode.equals(bean.getSubjectCode())
				&& this.currencyCode.equals(bean.getCurrencyCode());
	}
}
