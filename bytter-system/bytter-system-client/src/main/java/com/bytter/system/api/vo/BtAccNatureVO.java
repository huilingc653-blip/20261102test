package com.bytter.system.api.vo;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 账户性质表
 * @author onlineGenerator
 * @date 2017-05-12 17:06:43
 * @version V1.0
 *
 */
public class BtAccNatureVO extends IdEntity implements java.io.Serializable {
	/**账户性质编码*/
	private String natureCode;
	/**账户性质名称*/
	private String natureName;
	/**备注*/
	private String rmk;
	/**有效标志*/
	private String validSign;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户性质编码
	 */
	//@Column(name ="NATURE_CODE",nullable=false,length=4)
	public String getNatureCode(){
		return this.natureCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户性质编码
	 */
	public void setNatureCode(String natureCode){
		this.natureCode = natureCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户性质名称
	 */
	//@Column(name ="NATURE_NAME",nullable=false,length=20)
	public String getNatureName(){
		return this.natureName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户性质名称
	 */
	public void setNatureName(String natureName){
		this.natureName = natureName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	//@Column(name ="RMK",nullable=true,length=40)
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	//@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	//@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	//@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	//@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	//@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
