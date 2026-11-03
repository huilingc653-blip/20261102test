package com.bytter.biz.finacial.paramconfig.vo;
import java.io.Serializable;
public class FcSubjectInitVO implements Serializable {
	/**
	 * 科目代码
	 */
	private String subjectCode;
	/**
	 * 科目代码
	 */
	private String subjectName;
	/**
	 * 科目类别
	 */
	private String subjectSort;
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	/**
	 * 系统科目代码
	 */
	private String sysSubjectCode;
	/**
	 * 系统科目代码
	 */
	private String sysSubjectName;
	/**
	 * 开户种类
	 */
	private String accType;
	/**
	 * 科目代码
	 */
	private String validSign;
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectSort() {
		return subjectSort;
	}
	public void setSubjectSort(String subjectSort) {
		this.subjectSort = subjectSort;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public String getSysSubjectCode() {
		return sysSubjectCode;
	}
	public void setSysSubjectCode(String sysSubjectCode) {
		this.sysSubjectCode = sysSubjectCode;
	}
	public String getSysSubjectName() {
		return sysSubjectName;
	}
	public void setSysSubjectName(String sysSubjectName) {
		this.sysSubjectName = sysSubjectName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
}
