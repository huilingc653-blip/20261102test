package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Title: Entity
 * @Description: 职工信息维护表
 * @author onlineGenerator
 * @date 2017-05-26 09:31:22
 * @version 1.0
 *
 */
@ApiModel("职工信息维护表")
@Entity
@Table(name = "NIS_PERSONNEL_DETAIL", schema = "")
@SuppressWarnings("serial")
public class NisPersonnelDetail extends IdEntity implements java.io.Serializable {
	/**职工编号*/
	@ApiModelProperty("职工编号")
	private java.lang.String workNo;

	/**职工姓名*/
	@ApiModelProperty("职工姓名")
	private java.lang.String userName;

	/**身份证号*/
	@ApiModelProperty("身份证号")
	private java.lang.String idcard;

	/**性别*/
	@ApiModelProperty("性别")
	private java.lang.String sex;

	/**部门名称*/
	@ApiModelProperty("部门名称")
	private java.lang.String deptName;

	/**联系电话*/
	@ApiModelProperty("联系电话")
	private java.lang.String mobile;

	/**单位名称*/
	@ApiModelProperty("单位名称")
	private java.lang.String corpId;

	/**银行账号*/
	@ApiModelProperty("银行账号")
	private java.lang.String bankAcc;

	/**户名*/
	@ApiModelProperty("户名")
	private java.lang.String accName;

	/**接口代码*/
	@ApiModelProperty("接口代码")
	private java.lang.String bifCode;

	/**开户行行号*/
	@ApiModelProperty("开户行行号")
	private java.lang.String bankCode;

	/**开户银行名称*/
	@ApiModelProperty("开户银行名称")
	private java.lang.String bankName;

	/**账户类型*/
	@ApiModelProperty("账户类型")
	private java.lang.String cardType;

	/**账号所在省*/
	@ApiModelProperty("账号所在省")
	private java.lang.String prov;

	/**账号所在市*/
	@ApiModelProperty("账号所在市")
	private java.lang.String city;

	/**备注*/
	@ApiModelProperty("备注")
	private java.lang.String rmk;

	/**状态*/
	@ApiModelProperty(value = "状态", example = "0")
	private java.lang.Integer status;

	/**创建人登录名称*/
	@ApiModelProperty("创建人登录名称")
	private java.lang.String createBy;

	/**创建日期*/
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**更新人登录名称*/
	@ApiModelProperty("更新人登录名称")
	private java.lang.String updateBy;

	/**更新日期*/
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职工编号
	 */
	@Column(name ="WORK_NO",nullable=true,length=30)
	public java.lang.String getWorkNo(){
		return this.workNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职工编号
	 */
	public void setWorkNo(java.lang.String workNo){
		this.workNo = workNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职工姓名
	 */
	@Column(name ="USER_NAME",nullable=false,length=40)
	public java.lang.String getUserName(){
		return this.userName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职工姓名
	 */
	public void setUserName(java.lang.String userName){
		this.userName = userName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证号
	 */
	@Column(name ="IDCARD",nullable=true,length=20)
	public java.lang.String getIdcard(){
		return this.idcard;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证号
	 */
	public void setIdcard(java.lang.String idcard){
		this.idcard = idcard;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  性别
	 */
	@Column(name ="SEX",nullable=true,length=4)
	public java.lang.String getSex(){
		return this.sex;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  性别
	 */
	public void setSex(java.lang.String sex){
		this.sex = sex;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门名称
	 */
	@Column(name ="DEPT_NAME",nullable=true,length=30)
	public java.lang.String getDeptName(){
		return this.deptName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门名称
	 */
	public void setDeptName(java.lang.String deptName){
		this.deptName = deptName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联系电话
	 */
	@Column(name ="MOBILE",nullable=true,length=40)
	public java.lang.String getMobile(){
		return this.mobile;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联系电话
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位名称
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位名称
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC",nullable=false,length=30)
	public java.lang.String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(java.lang.String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  户名
	 */
	@Column(name ="ACC_NAME",nullable=true,length=150)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  户名
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=4)
	public java.lang.String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户行行号
	 */
	@Column(name ="BANK_CODE",nullable=true,length=20)
	public java.lang.String getBankCode(){
		return this.bankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行行号
	 */
	public void setBankCode(java.lang.String bankCode){
		this.bankCode = bankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户银行名称
	 */
	@Column(name ="BANK_NAME",nullable=true,length=150)
	public java.lang.String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户银行名称
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户类型
	 */
	@Column(name ="CARD_TYPE",nullable=true,length=1)
	public java.lang.String getCardType(){
		return this.cardType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户类型
	 */
	public void setCardType(java.lang.String cardType){
		this.cardType = cardType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号所在省
	 */
	@Column(name ="PROV",nullable=true,length=32)
	public java.lang.String getProv(){
		return this.prov;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号所在省
	 */
	public void setProv(java.lang.String prov){
		this.prov = prov;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号所在市
	 */
	@Column(name ="CITY",nullable=true,length=32)
	public java.lang.String getCity(){
		return this.city;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号所在市
	 */
	public void setCity(java.lang.String city){
		this.city = city;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
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
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
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
