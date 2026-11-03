package com.bytter.cashVoucher.entity;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.enums.CommonDictEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: 凭证打印格式明细
 * @author onlineGenerator
 * @date 2017-05-31 15:26:09
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_PRINT_FORMAT_DTL", schema = "")
@SuppressWarnings("serial")
public class BvPrintFormatDtl extends IdEntity implements java.io.Serializable {
	/**打印主信息ID*/
	private String printId;
	/**取值字段*/
	private String code;
	// @Dict(inject="codeName",tableName="bv_voutype_data_field",valueFieldName="id",showFiledName="field_name")
	private String codeId;
	/**名称描述*/
	private String name;
	/**打印坐标*/
	private String printCoord;
	/**是否打印*/
	// @Dict(dictGroup = "sf_yn", inject = "isPrintName")
	private String isPrint;
	/**字体大小*/
	private Integer fontSize;
	/**字体间距*/
	private Integer fontSpace;
	/**多少字换行*/
	private Integer charSizeBr;
	/**前缀显示值*/
	private String prefixVal;
	/**默认值*/
	private String otherVal;
	/**值打印类型*/
	@Dict(dictGroup = "bv_val_print_type", inject = "valPrintTypeName")
	private String valPrintType;
	/**值打印格式*/
	@Dict(dictGroup = "bv_val_print_fmt", inject = "valPrintFmtName")
	private String valPrintFmt;
	/**排序字段*/
	private Integer ordernum;
	private String codeName;
	private String fieldCode;
	private String isPrintName;
	private String valPrintTypeName;
	private String valPrintFmtName;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  取值字段
	 */
	@Column(name ="CODE",nullable=true,length=20)
	public String getCode(){
		return this.code;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  取值字段
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名称描述
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public String getName(){
		return this.name;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名称描述
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印坐标
	 */
	@Column(name ="PRINT_COORD",nullable=true,length=15)
	public String getPrintCoord(){
		return this.printCoord;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印坐标
	 */
	public void setPrintCoord(String printCoord){
		this.printCoord = printCoord;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否打印
	 */
	@Column(name ="IS_PRINT",nullable=true,length=1)
	public String getIsPrint(){
		return this.isPrint;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否打印
	 */
	public void setIsPrint(String isPrint){
		this.isPrint = isPrint;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  字体大小
	 */
	@Column(name ="FONT_SIZE",nullable=true,length=10)
	public Integer getFontSize(){
		return this.fontSize;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  字体大小
	 */
	public void setFontSize(Integer fontSize){
		this.fontSize = fontSize;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  字体间距
	 */
	@Column(name ="FONT_SPACE",nullable=true,length=10)
	public Integer getFontSpace(){
		return this.fontSpace;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  字体间距
	 */
	public void setFontSpace(Integer fontSpace){
		this.fontSpace = fontSpace;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  多少字换行
	 */
	@Column(name ="CHAR_SIZE_BR",nullable=true,length=10)
	public Integer getCharSizeBr(){
		return this.charSizeBr;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  多少字换行
	 */
	public void setCharSizeBr(Integer charSizeBr){
		this.charSizeBr = charSizeBr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  前缀显示值
	 */
	@Column(name ="PREFIX_VAL",nullable=true,length=20)
	public String getPrefixVal(){
		return this.prefixVal;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  前缀显示值
	 */
	public void setPrefixVal(String prefixVal){
		this.prefixVal = prefixVal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  默认值
	 */
	@Column(name ="OTHER_VAL",nullable=true,length=50)
	public String getOtherVal(){
		return this.otherVal;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  默认值
	 */
	public void setOtherVal(String otherVal){
		this.otherVal = otherVal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  值打印类型
	 */
	@Column(name ="VAL_PRINT_TYPE",nullable=true,length=50)
	public String getValPrintType(){
		return this.valPrintType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  值打印类型
	 */
	public void setValPrintType(String valPrintType){
		this.valPrintType = valPrintType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  值打印格式
	 */
	@Column(name ="VAL_PRINT_FMT",nullable=true,length=50)
	public String getValPrintFmt(){
		return this.valPrintFmt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  值打印格式
	 */
	public void setValPrintFmt(String valPrintFmt){
		this.valPrintFmt = valPrintFmt;
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
	public String getIsPrintName() {
		if (ObjectUtil.isNotEmpty(isPrint)) {
			return CommonDictEnum.getDictValue("sf_yn", isPrint);
		}
		return isPrintName;
	}
	public void setIsPrintName(String isPrintName) {
		this.isPrintName = isPrintName;
	}
	@Transient
	public String getValPrintTypeName() {
		return valPrintTypeName;
	}
	public void setValPrintTypeName(String valPrintTypeName) {
		this.valPrintTypeName = valPrintTypeName;
	}
	@Transient
	public String getValPrintFmtName() {
		return valPrintFmtName;
	}
	public void setValPrintFmtName(String valPrintFmtName) {
		this.valPrintFmtName = valPrintFmtName;
	}
	@Transient
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	@Transient
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	@Override
	public String toString() {
		return "BvPrintFormatDtl [printId=" + printId + ", code=" + code + ", codeId=" + codeId + ", name=" + name
				+ ", printCoord=" + printCoord + ", isPrint=" + isPrint + ", fontSize=" + fontSize + ", fontSpace="
				+ fontSpace + ", charSizeBr=" + charSizeBr + ", prefixVal=" + prefixVal + ", otherVal=" + otherVal
				+ ", valPrintType=" + valPrintType + ", valPrintFmt=" + valPrintFmt + ", ordernum=" + ordernum
				+ ", codeName=" + codeName + ", fieldCode=" + fieldCode + ", isPrintName=" + isPrintName
				+ ", valPrintTypeName=" + valPrintTypeName + ", valPrintFmtName=" + valPrintFmtName + "]";
	}
	
}
