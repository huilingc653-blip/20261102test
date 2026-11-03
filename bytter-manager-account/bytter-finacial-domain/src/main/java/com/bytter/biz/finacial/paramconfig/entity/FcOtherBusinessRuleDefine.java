package com.bytter.biz.finacial.paramconfig.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: FC_BUSINESS_RULE_DEFINE
 * @author onlineGenerator
 * @date 2017-05-22 19:23:13
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_OTHER_BUSINESS_RULE_DEFINE", schema = "")
@SuppressWarnings("serial")
public class FcOtherBusinessRuleDefine extends IdEntity implements java.io.Serializable {

	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**所属网点*/
	private String netId;
	/**业务种类编号*/
	private String sysRaceCode;
	/**收支标识*/
	private String cdSign;
	/**币别代码*/
	private String curCode;
	/**凭证种类*/
	private String voucherType;
	/**有效标志*/
	private String validSign;

	/**createTime*/
	private Date createTime;
	/**createBy*/
	private String createBy;
	/**updateTime*/
	private Date updateTime;
	/**updateBy*/
	private String updateBy;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(String netId){
		this.netId = netId;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN")
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  createBy
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  createBy
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类
	 */
	@Column(name ="VOUCHER_TYPE")
	public String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类
	 */
	public void setVoucherType(String voucherType){
		this.voucherType = voucherType;
	}

	@Column(name ="CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name ="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name ="CD_SIGN")
	public String getCdSign() {
		return cdSign;
	}

	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}

	@Column(name ="SYS_RACE_CODE")
	public String getSysRaceCode() {
		return sysRaceCode;
	}

	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
}
