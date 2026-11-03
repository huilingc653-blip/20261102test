package com.bytter.biz.netbank.vo;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
@SuppressWarnings("serial")
@ExcelTarget("NisBusTypeVO")
public class NisBusTypeVO extends ExcelModel implements Serializable{
	@Dict(tableName="NIS_BUS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
	private String id;
	/**业务类别编码*/
	@Excel(name = "*业务种类编码")
	@NotNull(message = "nis.assign.message.bustype.null")
	@BtCompositeSearch(fieldName = "n.bus_type")
	private java.lang.String busType;
	/**业务类别名称*/
	@Excel(name = "*业务种类名称")
	@NotNull(message = "nis.assign.message.bustypename.null")
	@BtCompositeSearch(fieldName = "n.bus_type_name")
	private java.lang.String busTypeName;

	@BtCompositeSearch(fieldName = "n.bus_type_name")
	private java.lang.String busTypeNameShow;
	/**收付款标志*/
	@Excel(name = "*收付款标志",type = 1,numFormat = "0")
	@NotNull(message = "nis.assign.message.cdsign.null")
	@BtCompositeSearch(fieldName = "n.cd_sign")
	@Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignStr")
	private java.lang.String cdSign;

	@BtCompositeSearch(fieldName = "n.cd_sign")
	private java.lang.String cdSignStr;
	/**计划科目*/
	@Excel(name = "计划科目")
	@NotNull(message = "nis.assign.message.itemcode.null")
	@BtCompositeSearch(fieldName = "n.item_code")
	private java.lang.String itemCode;
	/**计划科目名称*/
	@BtCompositeSearch(fieldName = "f.item_name")
	private java.lang.String itemName;
	/**有效标志*/
	@BtCompositeSearch(fieldName = "n.valid_sign")
	private java.lang.String validSign;
	/** 对应系统业务类别*/
	private String sysBusType;
	/** 结算类型*/
	private String settleType;
	/** 单位Id*/
	private String corpId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别编码
	 */
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别编码
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别名称
	 */
	public java.lang.String getBusTypeName(){
		return this.busTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别名称
	 */
	public void setBusTypeName(java.lang.String busTypeName){
		this.busTypeName = busTypeName;
	}
	public String getBusTypeNameShow() {
		return busTypeNameShow;
	}
	public void setBusTypeNameShow(String busTypeNameShow) {
		this.busTypeNameShow = busTypeNameShow;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收付款标志
	 */
	public java.lang.String getCdSign(){
		return this.cdSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收付款标志
	 */
	public void setCdSign(java.lang.String cdSign){
		this.cdSign = cdSign;
	}
	
	
	public java.lang.String getCdSignStr() {
		return cdSignStr;
	}
	public void setCdSignStr(java.lang.String cdSignStr) {
		this.cdSignStr = cdSignStr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划科目
	 */
	public java.lang.String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划科目
	 */
	public void setItemCode(java.lang.String itemCode){
		this.itemCode = itemCode;
	}
	public java.lang.String getItemName() {
		return itemName;
	}
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	@Override
	public String getBeanName() {
		return "nisBusTypeService";
	}
	public String getSysBusType() {
		return sysBusType;
	}
	public void setSysBusType(String sysBusType) {
		this.sysBusType = sysBusType;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
}
