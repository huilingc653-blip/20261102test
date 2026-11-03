package com.bytter.subject.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 系统科目
 * @author onlineGenerator
 * @date 2017-05-23 15:11:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_SUBJECT_SORT", schema = "")
@SuppressWarnings("serial")
public class FcSubjectSort implements java.io.Serializable {
	/**系统科目代码*/
	private String sysSubjectCode;
	/**系统科目名称*/
	private String sysSubjectName;
	/**是否唯一*/
	private String ifUnique;
	/**备注*/
	private String rmk;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目代码
	 */
	
	@Id
	@Column(name ="SYS_SUBJECT_CODE")
	public String getSysSubjectCode(){
		return this.sysSubjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目代码
	 */
	public void setSysSubjectCode(String sysSubjectCode){
		this.sysSubjectCode = sysSubjectCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目名称
	 */
	@Column(name ="SYS_SUBJECT_NAME")
	public String getSysSubjectName(){
		return this.sysSubjectName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目名称
	 */
	public void setSysSubjectName(String sysSubjectName){
		this.sysSubjectName = sysSubjectName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否唯一
	 */
	@Column(name ="IF_UNIQUE")
	public String getIfUnique(){
		return this.ifUnique;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否唯一
	 */
	public void setIfUnique(String ifUnique){
		this.ifUnique = ifUnique;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
}
