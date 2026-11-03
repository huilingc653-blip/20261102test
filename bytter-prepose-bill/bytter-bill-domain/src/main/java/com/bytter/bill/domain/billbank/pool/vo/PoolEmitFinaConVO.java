package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
/**
 * 出票融资申请合同信息
 * 
 */
public class PoolEmitFinaConVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 合同ID
	 */
	private String conId;
	/**
	 * 融资申请业务ID
	 */
	private String dtlId;
	
	/**
	 * 融资申请业务编号
	 */
	private String busAppNo;
	
	/**
	 * 合同文件类型（合同名称后缀取·后面的值）
	 */
	private String conFtype;
	
	/**
	 * 合同名称
	 */
	private String conName;
	
	/**
	 * 合同信息（base64以后的二进制流字符串）
	 */
	private String conInf;
	
	/**
	 * 合同信息MD5串
	 */
	private String conInfMd5;
	public String getConId() {
		return conId;
	}
	public void setConId(String conId) {
		this.conId = conId;
	}
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getBusAppNo() {
		return busAppNo;
	}
	public void setBusAppNo(String busAppNo) {
		this.busAppNo = busAppNo;
	}
	public String getConFtype() {
		return conFtype;
	}
	public void setConFtype(String conFtype) {
		this.conFtype = conFtype;
	}
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	public String getConInf() {
		return conInf;
	}
	public void setConInf(String conInf) {
		this.conInf = conInf;
	}
	public String getConInfMd5() {
		return conInfMd5;
	}
	public void setConInfMd5(String conInfMd5) {
		this.conInfMd5 = conInfMd5;
	}
	@Override
	public String toString() {
		return "PoolEmitFinaConVO [conId=" + conId + ", dtlId=" + dtlId
				+ ", busAppNo=" + busAppNo + ", conFtype=" + conFtype
				+ ", conName=" + conName + ", conInf=" + conInf
				+ ", conInfMd5=" + conInfMd5 + "]";
	}
	
}
