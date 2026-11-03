package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 票据业务字段是否可以修改配置
 * @author zhengg
 * @date 2020-04-26 14:29:10
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_FIELD_ISMDF", schema = "")
@SuppressWarnings("serial")
public class AbFieldIsmdf extends IdEntity implements java.io.Serializable {
	/**对应业务ID*/
	private String busId;
	/**字段代码*/
	private String fieldCode;
	/**操作类型：1可以修改 0不可以修改*/
	private String mdfSign;
	/**字段说明*/
	private String rmk;
	/**创建日期*/
	private Date createTime;
	/**创建人ID*/
	private String createBy;
	public AbFieldIsmdf(){
	}
	public AbFieldIsmdf(String busId,String fieldCode,String mdfSign,String rmk,String createBy){
		this.busId=busId;
		this.fieldCode=fieldCode;
		this.mdfSign=mdfSign;
		this.rmk=rmk;
		this.createTime=new Date();
		this.createBy=createBy;
	}
	public AbFieldIsmdf(String busId,String fieldCode,String mdfSign,String rmk,Date createTime,String createBy){
		this.busId=busId;
		this.fieldCode=fieldCode;
		this.mdfSign=mdfSign;
		this.rmk=rmk;
		this.createTime=createTime;
		this.createBy=createBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对应业务ID
	 */
	@Column(name ="BUS_ID",nullable=true,length=32)
	public String getBusId(){
		return this.busId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对应业务ID
	 */
	public void setBusId(String busId){
		this.busId = busId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  字段代码
	 */
	@Column(name ="FIELD_CODE",nullable=true,length=50)
	public String getFieldCode(){
		return this.fieldCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  字段代码
	 */
	public void setFieldCode(String fieldCode){
		this.fieldCode = fieldCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型：1可以修改 0不可以修改
	 */
	@Column(name ="MDF_SIGN",nullable=true,length=2)
	public String getMdfSign(){
		return this.mdfSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型：1可以修改 0不可以修改
	 */
	public void setMdfSign(String mdfSign){
		this.mdfSign = mdfSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  字段说明
	 */
	@Column(name ="RMK",nullable=true,length=200)
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  字段说明
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_TIME",nullable=true,scale=6)
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人ID
	 */
	@Column(name ="CREATE_BY",nullable=true,length=32)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人ID
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	
	@Override
	public String toString() {
		return "AbFieldIsmdf ["+
		"id=" + 
		getId() + ", "+
		"busId=" + 
		busId + ", "+
		"fieldCode=" + 
		fieldCode + ", "+
		"mdfSign=" + 
		mdfSign + ", "+
		"rmk=" + 
		rmk + ", "+
		"createTime=" + 
		createTime + ", "+
		"createBy=" + 
		createBy + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
