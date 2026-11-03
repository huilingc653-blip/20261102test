package com.bytter.biz.inneraccount.vo;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.*;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
/**   
 * @Title: Entity
 * @Description: 银行账号申请表
 * @author onlineGenerator
 * @date 2017-05-23 17:37:39
 * @version 1.0
 *
 */
@Data
public class BtBankAccAppVO  extends IdEntity implements java.io.Serializable,IBusinessKey {
	/**
	 * 
	 */
	private String id;
	private static final long serialVersionUID = -1547778611031878163L;
	/**序列号*/
	@BtCompositeSearch(fieldName = "acc.acc_no")
	private String accNo;
	/**银行账号*/
	@BtCompositeSearch(fieldName = "acc.bank_acc")
	private String bankAcc;
	/**账户名称*/
	@BtCompositeSearch(fieldName = "acc.acc_name")
	private String accName;
	/**单位ID*/
	//@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpName;
	/**币别ID*/
	@Currency(inject = "curName")
	private String curId;
	@BtCompositeSearch(fieldName = "acc.cur_id")
	private String curName;
	/**业务类型*/
	@BtCompositeSearch(fieldName = "acc.bus_type")
	private String busType;
	/**账户类别*/
	//@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	private String accType;
	@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accTypeName;
	
	/**账户属性*/
	//@Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
	private String attributeId;
	@BtCompositeSearch(fieldName = "acc.attribute_id")
	private String attributeName;
	
	/**账户性质*/
	//@Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
	private String natureId;
	@BtCompositeSearch(fieldName = "acc.nature_id")
	private String natureName;
	/**银行类别*/
	@BtCompositeSearch(fieldName = "acc.bank_type_id")
	//@Dict(tableName="BT_BANK_TYPE",valueFieldName="id",showFiledName="TYPE_NAME",inject="bankTypeName")
	private String bankTypeId;
	@BtCompositeSearch(fieldName = "acc.bank_type_id")
	private String bankTypeName;
	/**开户行行号*/
	private String bankCode;
	/**开户行名称*/
	@BtCompositeSearch(fieldName = "acc.bank_name")
	private String bankName;
	/**所在省*/
	@Region(inject="provName")
	private String prov;
	@BtCompositeSearch(fieldName = "acc.prov")
	private String provName;
	/**所在市*/
	@Region(inject="cityName")
	private String city;
	@BtCompositeSearch(fieldName = "acc.city")
	private String cityName;
	/**电票专户*/
	@Dict(dictGroup = "sf_yn", inject = "electricBillName")
	private String electricBill;
	@BtCompositeSearch(fieldName = "acc.electric_bill")
	private String electricBillName;
	/**实体账号**/
	private java.lang.String entityAccId;
	
	/**定期期限*/
	private String regulars;
	/**定期到期时间*/
	private Date regularDate;
	/**通知类型*/
	private String noticeType;
	/**协定金额*/
	private BigDecimal agrMoney;
	/**协定利率*/
	private BigDecimal agrInterest;
	/**协定合同*/
	private String agrContract;
	/**协定开始时间*/
	private Date agrBeginDate;
	/**协定结束时间*/
	private Date agrEndDate;
	/**申请原因*/
	@BtCompositeSearch(fieldName = "acc.op_ression")
	private String opRession;
	/**状态*/
	@BtCompositeSearch(fieldName = "acc.status")
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "acc.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "acc.update_Date")
	private Date updateDate;
	/**查询数据来源app为账号申请表，acc为银行账号表*/
	private String isForm;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**
	 * 流通方式 0-线下 1-线上
	 **/
	private java.lang.String circulationMode;
	/**
	 * 是否境内银行 1-境内，0-境外
	 */
	//@Dict(dictGroup = "btBankAcc.isDomesticBank", inject = "isDomesticBankName")
	private String isDomesticBank;
	@BtCompositeSearch(fieldName = "acc.is_domestic_bank")
	private String isDomesticBankName;
	/**附件数*/
	private java.lang.Integer attachmentNum;
	/**创建人名称*/
	private java.lang.String createByName;
	/**更新人名称*/
	private java.lang.String updateByName;
	private String isOffshoreAccount;
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
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
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getNatureId() {
		return natureId;
	}
	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
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
	public String getElectricBill() {
		return electricBill;
	}
	public void setElectricBill(String electricBill) {
		this.electricBill = electricBill;
	}
	public String getRegulars() {
		return regulars;
	}
	public void setRegulars(String regulars) {
		this.regulars = regulars;
	}
	public Date getRegularDate() {
		return regularDate;
	}
	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public BigDecimal getAgrMoney() {
		return agrMoney;
	}
	public void setAgrMoney(BigDecimal agrMoney) {
		this.agrMoney = agrMoney;
	}
	public BigDecimal getAgrInterest() {
		return agrInterest;
	}
	public void setAgrInterest(BigDecimal agrInterest) {
		this.agrInterest = agrInterest;
	}
	public String getAgrContract() {
		return agrContract;
	}
	public void setAgrContract(String agrContract) {
		this.agrContract = agrContract;
	}
	public Date getAgrBeginDate() {
		return agrBeginDate;
	}
	public void setAgrBeginDate(Date agrBeginDate) {
		this.agrBeginDate = agrBeginDate;
	}
	public Date getAgrEndDate() {
		return agrEndDate;
	}
	public void setAgrEndDate(Date agrEndDate) {
		this.agrEndDate = agrEndDate;
	}
	public String getOpRession() {
		return opRession;
	}
	public void setOpRession(String opRession) {
		this.opRession = opRession;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	
	public String getElectricBillName() {
		return electricBillName;
	}
	public void setElectricBillName(String electricBillName) {
		this.electricBillName = electricBillName;
	}
	public String getIsForm() {
		return isForm;
	}
	public void setIsForm(String isForm) {
		this.isForm = isForm;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getEntityAccId() {
		return entityAccId;
	}
	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}
	public String getCirculationMode() {
		return circulationMode;
	}
	public void setCirculationMode(String circulationMode) {
		this.circulationMode = circulationMode;
	}
	public String getIsDomesticBank() {
		return isDomesticBank;
	}
	public void setIsDomesticBank(String isDomesticBank) {
		this.isDomesticBank = isDomesticBank;
	}
	public String getIsDomesticBankName() {
		return isDomesticBankName;
	}
	public void setIsDomesticBankName(String isDomesticBankName) {
		this.isDomesticBankName = isDomesticBankName;
	}
	public Integer getAttachmentNum() {
		return attachmentNum;
	}
	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getUpdateByName() {
		return updateByName;
	}
	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}
}
