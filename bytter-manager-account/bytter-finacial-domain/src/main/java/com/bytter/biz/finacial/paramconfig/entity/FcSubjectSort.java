package com.bytter.biz.finacial.paramconfig.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 系统科目
 * @author onlineGenerator
 * @date 2017-05-23 15:11:52
 * @version 1.0
 * 实体已迁移到sys模块
 */
@SuppressWarnings("serial")
public class FcSubjectSort implements java.io.Serializable {
	/**系统科目代码*/
	private java.lang.String sysSubjectCode;
	/**系统科目名称*/
	private java.lang.String sysSubjectName;
	/**是否唯一*/
	private java.lang.String ifUnique;
	/**备注*/
	private java.lang.String rmk;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目代码
	 */
	
	@Id
	@Column(name ="SYS_SUBJECT_CODE")
	public java.lang.String getSysSubjectCode(){
		return this.sysSubjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目代码
	 */
	public void setSysSubjectCode(java.lang.String sysSubjectCode){
		this.sysSubjectCode = sysSubjectCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目名称
	 */
	@Column(name ="SYS_SUBJECT_NAME")
	public java.lang.String getSysSubjectName(){
		return this.sysSubjectName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目名称
	 */
	public void setSysSubjectName(java.lang.String sysSubjectName){
		this.sysSubjectName = sysSubjectName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否唯一
	 */
	@Column(name ="IF_UNIQUE")
	public java.lang.String getIfUnique(){
		return this.ifUnique;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否唯一
	 */
	public void setIfUnique(java.lang.String ifUnique){
		this.ifUnique = ifUnique;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
}
