package com.bytter.biz.finacial.paramconfig.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeBankTypeRel;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
public class FcChargeTypeVo implements Serializable{
	/**费用明细*/
	private List<FcChargeTypeDtl> chargeTypeDtls = new ArrayList();
	/**银行类别*/
	private List<ChargeTypeBankTypeRel>  bankTypeRels = new ArrayList();
	
	@BtCompositeSearch(fieldName = "fc.id")
	private java.lang.String id;
	/**费用代码*/
	@BtCompositeSearch(fieldName = "fc.charge_type_code")
	private java.lang.String chargeTypeCode;
	/**费用名称*/
	@BtCompositeSearch(fieldName = "fc.charge_type_name")
	private java.lang.String chargeTypeName;
	/**币别*/
	private java.lang.String curCode;
	/**加急标志*/
	private java.lang.String emergencySign;
	/**跨行标志*/
	private java.lang.String difBank;
	/**地域标志*/
	private java.lang.String areaSign;
	/**收取费用*/
	private java.math.BigDecimal charge;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	public List<FcChargeTypeDtl> getChargeTypeDtls() {
		return chargeTypeDtls;
	}
	public void setChargeTypeDtls(List<FcChargeTypeDtl> chargeTypeDtls) {
		this.chargeTypeDtls = chargeTypeDtls;
	}
	public List<ChargeTypeBankTypeRel> getBankTypeRels() {
		return bankTypeRels;
	}
	public void setBankTypeRels(List<ChargeTypeBankTypeRel> bankTypeRels) {
		this.bankTypeRels = bankTypeRels;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getChargeTypeCode() {
		return chargeTypeCode;
	}
	public void setChargeTypeCode(java.lang.String chargeTypeCode) {
		this.chargeTypeCode = chargeTypeCode;
	}
	public java.lang.String getChargeTypeName() {
		return chargeTypeName;
	}
	public void setChargeTypeName(java.lang.String chargeTypeName) {
		this.chargeTypeName = chargeTypeName;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getEmergencySign() {
		return emergencySign;
	}
	public void setEmergencySign(java.lang.String emergencySign) {
		this.emergencySign = emergencySign;
	}
	public java.lang.String getDifBank() {
		return difBank;
	}
	public void setDifBank(java.lang.String difBank) {
		this.difBank = difBank;
	}
	public java.lang.String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(java.lang.String areaSign) {
		this.areaSign = areaSign;
	}
	public java.math.BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(java.math.BigDecimal charge) {
		this.charge = charge;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	@Override
	public String toString() {
		return "FcChargeTypeVo [chargeTypeDtls=" + chargeTypeDtls + ", bankTypeRels=" + bankTypeRels
				+ ", chargeTypeCode=" + chargeTypeCode + ", chargeTypeName=" + chargeTypeName + ", curCode=" + curCode
				+ ", emergencySign=" + emergencySign + ", difBank=" + difBank + ", areaSign=" + areaSign + ", charge="
				+ charge + ", createTime=" + createTime + ", createBy=" + createBy + ", updateTime=" + updateTime
				+ ", updateBy=" + updateBy + "]";
	}
	
	
}
