package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
@SuppressWarnings("serial")
public class NisBusTypeDTO  implements Serializable{
	private String id;
	/**业务类别编码*/
	private String busType;
	/**业务类别名称*/
	private String busTypeName;
	private String busTypeNameShow;
	/**收付款标志*/
	@Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignStr")
	private String cdSign;
	private String cdSignStr;
	/**计划科目*/
	private String itemCode;
	/**计划科目名称*/
	private String itemName;
	/**有效标志*/
	private String validSign;
	/** 对应系统业务类别*/
	private String sysBusType;
	/** 结算类型*/
	private String settleType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getBusTypeNameShow() {
		return busTypeNameShow;
	}
	public void setBusTypeNameShow(String busTypeNameShow) {
		this.busTypeNameShow = busTypeNameShow;
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
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
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
}
