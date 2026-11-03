package com.bytter.bill.domain.billbank.vo;
import java.io.Serializable;
/**
 * <b>描述：</b>机构实体类<br>
 * <b>作者：</b>李杰<br>
 * <b>日期：</b> 2013-12-24 <br>
 *
 */
public class EDraftOrganBillVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 机构号码
	 */
	private String organId ; 
	
	/**
	 * 机构名称
	 */
	private String organName;
	
	/**
	 * 机构类型0-全部,1-承兑,2-贴现,3-质押
	 */
	private String organType;
	
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getOrganType() {
		return organType;
	}
	public void setOrganType(String organType) {
		this.organType = organType;
	}
	public String toString() {
		return "EDraftOrganBillVo [organId=" + organId + ", organName=" + organName + ", organType=" + organType + "]";
	}
	
}
