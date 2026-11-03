package com.bytter.biz.wage.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Region;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
/**   
 * @Title: Entity
 * @Description: 代发工资VO
 * @author onlineGenerator
 * @date 2017-10-31 15:20:33
 * @version 1.0
 *
 */
@ExcelTarget("NisWageDetailVO")
public class NisWageDetailVO extends ExcelModel implements Serializable {
	private String id;
	/**单据号码*/
	@BtCompositeSearch(fieldName = "n.wage_Dtl_No")
	private String wageDtlNo;
	/**代发工资ID*/
	@BtCompositeSearch(fieldName = "n.wage_Id")
	private String wageId;
	/**工号*/
	@BtCompositeSearch(fieldName = "n.work_No")
	@Excel(name = "工号")
    /*@NotNull(message = "nis.netbank.message.workno.null")*/
	private String workNo;
	/**姓名*/
	/*@Excel(name = "*姓名")
	@NotNull(message = "nis.netbank.message.username.null")*/
	private String userName;
	/**银行账号*/
	@BtCompositeSearch(fieldName = "n.bank_Acc")
	@Excel(name = "*银行账号")
    @NotNull(message = "nis.netbank.message.bankacc.null")
	private String bankAcc;
	/**账户名*/
	@BtCompositeSearch(fieldName = "n.acc_Name")
	@Excel(name = "*账户名")
	@NotNull(message = "nis.netbank.message.accname.null")
    private String accName;
	/**实发工资*/
	private String wagesVerify;
	/**实发工资*/
	@BtCompositeSearch(fieldName = "n.wages_Verify")
	@Excel(name = "*实发工资", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal wages;
	/**用途*/
	@BtCompositeSearch(fieldName = "n.purpose")
	@Excel(name = "*工资用途")
	@NotNull(message = "nis.wage.message.purpose.null")
    private String purpose;
	/**所在省*/
	@Region(inject="provName")
	private String prov;
	/*@Excel(name = "账号所在省")
	@NotNull(message = "nis.netbank.message.provname.null")*/
    private String provName;
	/**所在市*/
	@Region(inject="cityName")
	private String city;
	/*@Excel(name = "账号所在市")
	@NotNull(message = "nis.netbank.message.cityname.null")*/
    private String cityName;
	/**开户行行号*/
	@BtCompositeSearch(fieldName = "n.bank_Code")
	@Excel(name = "开户行行号")
	private String bankCode;
	/**开户银行名称*/
	@BtCompositeSearch(fieldName = "n.bank_Name")
	@Excel(name = "开户行名称")
	private String bankName;
	/**跨行标识 replace={"同行_0","他行_1"}*/
	@Dict(dictGroup = "nisBillhead.difBank", inject = "difBankName")
	@BtCompositeSearch(fieldName = "n.dif_Bank")
	@Excel(name = "跨行标志")
	private String difBank;
	private String difBankName;
	/**地域标识, replace={"同城_0","异地_1"}*/
	@Dict(dictGroup = "nisBillhead.areaSign", inject = "areaSignName")
	@BtCompositeSearch(fieldName = "n.area_Sign")
	@Excel(name = "地域标志")
	private String areaSign;
	private String areaSignName;
	/**交易状态*/
	@Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
	@BtCompositeSearch(fieldName = "n.voucher_Stat")
	private String voucherStat;
	private String voucherStatName;
	/**返回时间*/
	@BtCompositeSearch(fieldName = "n.return_Time")
	@Excel(name = "银行返回时间",format = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date returnTime;
	/**返回信息（交易前）*/
	@BtCompositeSearch(fieldName = "n.first_Return_Msg")
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	@BtCompositeSearch(fieldName = "n.next_Return_Msg")
	private String nextReturnMsg;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**指令单号*/
	private String voucherNo;
	/**证件类型*/
	@Excel(name = "证件类型")
	@Dict(dictGroup = "idType", inject = "idTypeName")
	private String idType;
	private String idTypeName;
	/**身份证号（部分银行需要身份证信息，已知北京银行、沧州银行）*/
	@BtCompositeSearch(fieldName = "n.id_No")
	@Excel(name = "证件号")
	private String idNo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWageDtlNo() {
		return wageDtlNo;
	}
	public void setWageDtlNo(String wageDtlNo) {
		this.wageDtlNo = wageDtlNo;
	}
	public String getWageId() {
		return wageId;
	}
	public void setWageId(String wageId) {
		this.wageId = wageId;
	}
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getWagesVerify() {
		return wagesVerify;
	}
	public void setWagesVerify(String wagesVerify) {
		this.wagesVerify = wagesVerify;
	}
	public BigDecimal getWages() {
		return wages;
	}
	public void setWages(BigDecimal wages) {
		this.wages = wages;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public String getDifBank() {
		return difBank;
	}
	public void setDifBank(String difBank) {
		this.difBank = difBank;
	}
	public String getDifBankName() {
		return difBankName;
	}
	public void setDifBankName(String difBankName) {
		this.difBankName = difBankName;
	}
	public String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(String areaSign) {
		this.areaSign = areaSign;
	}
	public String getAreaSignName() {
		return areaSignName;
	}
	public void setAreaSignName(String areaSignName) {
		this.areaSignName = areaSignName;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getFirstReturnMsg() {
		return firstReturnMsg;
	}
	public void setFirstReturnMsg(String firstReturnMsg) {
		this.firstReturnMsg = firstReturnMsg;
	}
	public String getNextReturnMsg() {
		return nextReturnMsg;
	}
	public void setNextReturnMsg(String nextReturnMsg) {
		this.nextReturnMsg = nextReturnMsg;
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
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdTypeName() {
		return idTypeName;
	}
	public void setIdTypeName(String idTypeName) {
		this.idTypeName = idTypeName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	@Override
	public Map<String, String> getLangMap() {
		super.addLangMap("difBank", "nisBillhead.difBank");
		super.addLangMap("areaSign", "nisBillhead.areaSign");
		return super.getLangMap();
	}
}
