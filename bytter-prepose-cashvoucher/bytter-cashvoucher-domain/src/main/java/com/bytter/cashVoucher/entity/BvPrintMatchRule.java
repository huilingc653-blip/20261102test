package com.bytter.cashVoucher.entity;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.enums.CommonDictEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: 凭证打印格式匹配规则
 * @author onlineGenerator
 * @date 2017-05-31 15:25:46
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_PRINT_MATCH_RULE", schema = "")
@SuppressWarnings("serial")
public class BvPrintMatchRule extends IdEntity implements java.io.Serializable {
	/**打印主信息ID*/
	private String printId;
	/**取值字段*/
	private String code;
	// @Dict(inject="codeName",tableName="bv_voutype_data_field",valueFieldName="id",showFiledName="field_name")
	private String codeId;
	/**匹配值*/
	private String matchVal;
	/**匹配规则*/
	// @Dict(dictGroup = "bv_print_match_rule", inject = "matchRuleName")
	private String matchRule;
	/**有效标志*/
	// @Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	/**排序字段*/
	private Integer ordernum;
	private String codeName;
	private String matchRuleName;
	private String validSignName;
	// @Dict(tableName="bv_voucher_type",valueFieldName="id",showFiledName="name", inject = "vouTidName")
	private String vouTid;
	private String vouTidName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印主信息ID
	 */
	@Column(name ="PRINT_ID",nullable=true,length=32)
	public String getPrintId(){
		return this.printId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印主信息ID
	 */
	public void setPrintId(String printId){
		this.printId = printId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  取值字段
	 */
	@Column(name ="CODE",nullable=true,length=20)
	public String getCode(){
		return this.code;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  取值字段
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  匹配值
	 */
	@Column(name ="MATCH_VAL",nullable=true,length=50)
	public String getMatchVal(){
		return this.matchVal;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  匹配值
	 */
	public void setMatchVal(String matchVal){
		this.matchVal = matchVal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  匹配规则
	 */
	@Column(name ="MATCH_RULE",nullable=true,length=20)
	public String getMatchRule(){
		return this.matchRule;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  匹配规则
	 */
	public void setMatchRule(String matchRule){
		this.matchRule = matchRule;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
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
	@Column(name ="ORDERNUM",nullable=true,length=10)
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	@Transient
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	@Transient
	public String getMatchRuleName() {
		return matchRuleName;
	}
	public void setMatchRuleName(String matchRuleName) {
		this.matchRuleName = matchRuleName;
	}
	@Transient
	public String getValidSignName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", validSign);
		}
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	@Transient
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	@Transient
	public String getVouTidName() {
		return vouTidName;
	}
	public void setVouTidName(String vouTidName) {
		this.vouTidName = vouTidName;
	}
	@Transient
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	@Override
	public String toString() {
		return "BvPrintMatchRule [printId=" + printId + ", code=" + code + ", matchVal=" + matchVal + ", matchRule="
				+ matchRule + ", validSign=" + validSign + ", ordernum=" + ordernum + ", codeName=" + codeName
				+ ", matchRuleName=" + matchRuleName + ", validSignName=" + validSignName + ", vouTid=" + vouTid
				+ ", vouTidName=" + vouTidName + "]";
	}
}
