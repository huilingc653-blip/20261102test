package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 接口代码业务规则表VO
 * @author onlineGenerator
 * @date 2017-05-17 10:16:20
 * @version 1.0
 *
 */
public class BisBifRuleVO implements Serializable {
	/**id*/
	private String id;
	/**接口代码*/
	@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifId;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	private String bifCode;
	/**接口代码名称*/
    @BtCompositeSearch(fieldName = "b.bif_Name")
	private String bifName;
	/**付款人*/
    @BtCompositeSearch(fieldName = "b.payer_Name")
	private Integer payerName;
	/**付款账号*/
    @BtCompositeSearch(fieldName = "b.payer_Acc")
	private Integer payerAcc;
	/**付款开户行*/
    @BtCompositeSearch(fieldName = "b.payer_Bank")
	private Integer payerBank;
	/**收款人*/
    @BtCompositeSearch(fieldName = "b.payee_Name")
	private Integer payeeName;
	/**收款账号*/
    @BtCompositeSearch(fieldName = "b.payee_Acc")
	private Integer payeeAcc;
	/**收款开户行*/
    @BtCompositeSearch(fieldName = "b.payee_Bank")
	private Integer payeeBank;
	/**用途*/
    @BtCompositeSearch(fieldName = "b.purpose")
	private Integer purpose;
	/**附言*/
    @BtCompositeSearch(fieldName = "b.add_Word")
	private Integer addWord;
	/**用途是否支持特殊字符*/
    @BtCompositeSearch(fieldName = "b.is_Purpose")
	private String isPurpose;
	/**附言是否额外包含业务单号*/
    @BtCompositeSearch(fieldName = "b.is_Add_Bill_No")
	private String isAddBillNo;
	/**银行代发是否在线*/
    @BtCompositeSearch(fieldName = "b.is_Bank_Online")
	private String isBankOnline;
	/**银行代发发送数*/
    @BtCompositeSearch(fieldName = "b.send_Num")
	private Integer sendNum;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**同行是否必填联行号*/
	private String isMustBankCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBifId() {
		return bifId;
	}
	public void setBifId(String bifId) {
		this.bifId = bifId;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public Integer getPayerName() {
		return payerName;
	}
	public void setPayerName(Integer payerName) {
		this.payerName = payerName;
	}
	public Integer getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(Integer payerAcc) {
		this.payerAcc = payerAcc;
	}
	public Integer getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(Integer payerBank) {
		this.payerBank = payerBank;
	}
	public Integer getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(Integer payeeName) {
		this.payeeName = payeeName;
	}
	public Integer getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(Integer payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public Integer getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(Integer payeeBank) {
		this.payeeBank = payeeBank;
	}
	public Integer getPurpose() {
		return purpose;
	}
	public void setPurpose(Integer purpose) {
		this.purpose = purpose;
	}
	public Integer getAddWord() {
		return addWord;
	}
	public void setAddWord(Integer addWord) {
		this.addWord = addWord;
	}
	public String getIsPurpose() {
		return isPurpose;
	}
	public void setIsPurpose(String isPurpose) {
		this.isPurpose = isPurpose;
	}
	public String getIsAddBillNo() {
		return isAddBillNo;
	}
	public void setIsAddBillNo(String isAddBillNo) {
		this.isAddBillNo = isAddBillNo;
	}
	public String getIsBankOnline() {
		return isBankOnline;
	}
	public void setIsBankOnline(String isBankOnline) {
		this.isBankOnline = isBankOnline;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
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
	public String getIsMustBankCode() {
		return isMustBankCode;
	}
	public void setIsMustBankCode(String isMustBankCode) {
		this.isMustBankCode = isMustBankCode;
	}
}
