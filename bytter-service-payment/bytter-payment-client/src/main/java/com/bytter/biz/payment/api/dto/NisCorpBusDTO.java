package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
public class NisCorpBusDTO implements Serializable {
	private static final long serialVersionUID = 2313478363314999846L;
	private String id;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "n.corp_id")
	private String corpId;
	/**单位编码*/
	@BtCompositeSearch(fieldName = "code")
	private String corpCode;
	@BtCompositeSearch(fieldName = "sort")
	private java.math.BigDecimal corpSort;
	/**单位名称*/
	private String corpName;
	/**业务类别ID*/
	@Dict(tableName="NIS_BUS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
	private String busId;
	/**业务编码*/
	private String busType;
	/**业务类别名称*/
	@BtCompositeSearch(fieldName = "t.bus_type_name")
	private String busTypeName;
	/**计划科目*/
	private String itemCode;
	/**用于显示业务种类*/
	private String showName;
	/** 对应系统业务类别*/
	private String sysBusType;
	@Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignStr")
	@BtCompositeSearch(fieldName = "t.cd_Sign")
	private String cdSign;
	private String cdSignStr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public java.math.BigDecimal getCorpSort() {
		return corpSort;
	}
	public void setCorpSort(java.math.BigDecimal corpSort) {
		this.corpSort = corpSort;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getSysBusType() {
		return sysBusType;
	}
	public void setSysBusType(String sysBusType) {
		this.sysBusType = sysBusType;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	public String getCdSignStr() {
		return cdSignStr;
	}
	public void setCdSignStr(String cdSignStr) {
		this.cdSignStr = cdSignStr;
	}
}
