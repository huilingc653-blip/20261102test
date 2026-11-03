package com.bytter.system.api.dto;

/**
 * @Title: Entity
 * @Description: 组织架构对应关系维护表
 * @author zhengg
 * @date 2020-05-13 17:18:25
 * @version V1.0   
 *
 */
@SuppressWarnings("serial")
public class OrgantypeModuleDTO implements java.io.Serializable {

	private String id;
	/**模块代码*/
	private String sysCode;
	
	/**组织机构ID*/
	private String orgtId;
	
	/**更新人ID*/
	private String updateBy;
	
	/**更新时间*/
	private java.util.Date updateTime;

	/**是否启用**/
	private Integer isValid;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模块代码
	 */
	public String getSysCode(){
		return this.sysCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模块代码
	 */
	public void setSysCode(String sysCode){
		this.sysCode = sysCode;
	}
	

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  组织机构ID
	 */
	public String getOrgtId(){
		return this.orgtId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  组织机构ID
	 */
	public void setOrgtId(String orgtId){
		this.orgtId = orgtId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人ID
	 */
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人ID
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "OrgantypeModule ["+
		"id=" + 
		getId() + ", "+
		"sysCode=" + 
		sysCode + ", "+
		"orgtId=" +
		orgtId + ", "+
		"updateBy=" + 
		updateBy + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
