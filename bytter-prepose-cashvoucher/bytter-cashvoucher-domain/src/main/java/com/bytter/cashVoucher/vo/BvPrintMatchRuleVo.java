package com.bytter.cashVoucher.vo;
import com.bytter.core.annotation.Dict;
/**   
 * @Title: Entity
 * @Description: 凭证打印格式匹配规则
 * @author onlineGenerator
 * @date 2017-05-31 15:25:46
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvPrintMatchRuleVo implements java.io.Serializable {
	/**打印主信息ID*/
	private String printId;
	/**取值字段*/
	private String code;
	@Dict(inject="codeName",tableName="bv_voutype_data_field",valueFieldName="id",showFiledName="field_name")
	private String codeId;
	/**匹配值*/
	private String matchVal;
	/**匹配规则*/
	@Dict(dictGroup = "bv_print_match_rule", inject = "matchRuleName")
	private String matchRule;
	/**有效标志*/
	@Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	@Dict(tableName="bv_voucher_type",valueFieldName="id",showFiledName="name", inject = "vouTidName")
	private String vouTid;
	private String vouTidName;
	private String validSignName;
	private String matchRuleName;
	private String field_name;
	private Integer ordernum;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印主信息ID
	 */
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
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getVouTidName() {
		return vouTidName;
	}
	public void setVouTidName(String vouTidName) {
		this.vouTidName = vouTidName;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getMatchRuleName() {
		return matchRuleName;
	}
	public void setMatchRuleName(String matchRuleName) {
		this.matchRuleName = matchRuleName;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	@Override
	public String toString() {
		return "BvPrintMatchRuleVo [printId=" + printId + ", code=" + code + ", codeId=" + codeId + ", matchVal="
				+ matchVal + ", matchRule=" + matchRule + ", validSign=" + validSign + ", vouTid=" + vouTid
				+ ", vouTidName=" + vouTidName + ", validSignName=" + validSignName + ", matchRuleName=" + matchRuleName
				+ ", field_name=" + field_name + ", ordernum=" + ordernum + "]";
	}
	
}
