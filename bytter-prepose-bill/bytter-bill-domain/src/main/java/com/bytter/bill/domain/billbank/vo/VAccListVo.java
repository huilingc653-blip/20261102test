package com.bytter.bill.domain.billbank.vo;
import java.io.Serializable;
/**
 * 银行账号信息视图
 * @author 郑鸿杰
 */
public class VAccListVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1352381408211600242L;
	/**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 父账号
     */
    private String prentAcc;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 币别类型
     */
    private String curCode;
    /**
     * 币别Id
     */
    private String curId;
    /**
     * 内部账号
     */
    private String counterAcc;
    /**
     * 上级账号
     */
    private String superCounterAcc;
    /**
     * 父账号标识
     */
    private String parentSign;
    /**
     * 账户类型
     */
    private String accType;
    /**
     * 联行号
     */
    private String bankCode;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 省份
     */
    private String prov;
    /**
     * 城市
     */
    private String city;
    /**
     * 对私编号
     */
    private String privateNo;
    /**
     * 建行机构号
     */
    private String orgCode;
    /**
     * 银行编码
     */
    private String bifCode;
    /**
     * 在线标志
     */
    private String isOnline;
    /**
     * 有效标志
     */
    private String validSign;
	private String briefId;
	private String billType;
	/**
	 * 是否支持等分化票据标志，0否，1是
	 */
	private String newDraftFlg;
	public String getNewDraftFlg() {
		return newDraftFlg;
	}
	public void setNewDraftFlg(String newDraftFlg) {
		this.newDraftFlg = newDraftFlg;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getPrentAcc() {
		return prentAcc;
	}
	public void setPrentAcc(String prentAcc) {
		this.prentAcc = prentAcc;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCounterAcc() {
		return counterAcc;
	}
	public void setCounterAcc(String counterAcc) {
		this.counterAcc = counterAcc;
	}
	public String getSuperCounterAcc() {
		return superCounterAcc;
	}
	public void setSuperCounterAcc(String superCounterAcc) {
		this.superCounterAcc = superCounterAcc;
	}
	public String getParentSign() {
		return parentSign;
	}
	public void setParentSign(String parentSign) {
		this.parentSign = parentSign;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPrivateNo() {
		return privateNo;
	}
	public void setPrivateNo(String privateNo) {
		this.privateNo = privateNo;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String toString() {
		return "VAccListVo{" +
				"bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", prentAcc='" + prentAcc + '\'' +
				", corpId='" + corpId + '\'' +
				", curCode='" + curCode + '\'' +
				", curId='" + curId + '\'' +
				", counterAcc='" + counterAcc + '\'' +
				", superCounterAcc='" + superCounterAcc + '\'' +
				", parentSign='" + parentSign + '\'' +
				", accType='" + accType + '\'' +
				", bankCode='" + bankCode + '\'' +
				", bankName='" + bankName + '\'' +
				", prov='" + prov + '\'' +
				", city='" + city + '\'' +
				", privateNo='" + privateNo + '\'' +
				", orgCode='" + orgCode + '\'' +
				", bifCode='" + bifCode + '\'' +
				", isOnline='" + isOnline + '\'' +
				", validSign='" + validSign + '\'' +
				", briefId='" + briefId + '\'' +
				", billType='" + billType + '\'' +
				", newDraftFlg='" + newDraftFlg + '\'' +
				'}';
	}
}
