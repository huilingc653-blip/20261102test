package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 票据附件操作
 * @author onlineGenerator
 * @date 2017-10-24 14:33:51
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_ATTACHMENT", schema = "")
@SuppressWarnings("serial")
public class AbBillAttachment extends IdEntity implements java.io.Serializable {
	/**附件ID*/
	private String attachmentId;
	/**附件数量*/
	private Integer num;
	/**票据从表id*/
	private String vvid;
	/**流水ID*/
	private String acid;
	/**业务类型*/
	private String briefId;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 * 方法: 获取附件ID
	 * @return: 附件ID
	 */
	@Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
	public String getAttachmentId() {
		return attachmentId;
	}
	/**
	 * 方法: 设置附件ID
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  附件数量
	 */
	@Column(name ="NUM",nullable=true,length=10)
	public Integer getNum(){
		return this.num;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  附件数量
	 */
	public void setNum(Integer num){
		this.num = num;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据从表id
	 */
	@Column(name ="VVID",nullable=true,length=32)
	public String getVvid(){
		return this.vvid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据从表id
	 */
	public void setVvid(String vvid){
		this.vvid = vvid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水ID
	 */
	@Column(name ="ACID",nullable=true,length=32)
	public String getAcid(){
		return this.acid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水ID
	 */
	public void setAcid(String acid){
		this.acid = acid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=10)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBriefId(String briefId){
		this.briefId = briefId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
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
	@Column(name ="CREATE_DATE",nullable=true)
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
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
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
	@Column(name ="UPDATE_DATE",nullable=true)
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
