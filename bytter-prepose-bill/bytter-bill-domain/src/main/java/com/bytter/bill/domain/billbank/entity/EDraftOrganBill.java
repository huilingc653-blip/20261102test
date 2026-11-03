package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 机构信息
 * @author onlineGenerator
 * @date 2017-09-02
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_DRAFT_ORGAN", schema = "")
@SuppressWarnings("serial")
public class EDraftOrganBill extends IdEntity implements java.io.Serializable {
	/** 机构号码 */
	private Long organId;
	/**机构名称*/
	private String organName;
	/** 机构类型0-全部,1-承兑,2-贴现,3-质押*/
	private String organType;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  机构号码
	 */
	@Column(name ="ORGAN_ID",nullable=true,length=4)
	public Long getOrganId(){
		return this.organId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  机构号码
	 */
	public void setOrganId(Long organId){
		this.organId = organId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  机构名称
	 */
	@Column(name ="ORGAN_NAME",nullable=true)
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  机构类型
	 */
	@Column(name ="ORGAN_TYPE",nullable=true)
	public String getOrganType() {
		return organType;
	}
	public void setOrganType(String organType) {
		this.organType = organType;
	}
	@Override
	public String toString() {
		return "EDraftOrganBill [organId=" + organId + ", organName=" + organName + ", organType=" + organType + "]";
	}
	
}
