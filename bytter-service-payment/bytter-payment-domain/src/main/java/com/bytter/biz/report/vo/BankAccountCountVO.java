package com.bytter.biz.report.vo;
import com.bytter.core.annotation.*;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * @Title: Entity
 * @Description:银行账号数量统计表
 * @author shenc
 * @date 2018.12.11
 * @version 1.0
 *
 */
public class BankAccountCountVO extends ExcelModel implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6490814688726935044L;
	/**单位ID*/
	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpName;
	@BtCompositeSearch(fieldName = "c.cur_id")
	private String curName;
	@Currency(inject="curName")
	@BtCompositeSearch(fieldName = "c.cur_id")
	private String curId;
	/**账户类别*/
	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	@BtCompositeSearch(fieldName = "b.acc_type")
	private String accType;
	@BtCompositeSearch(fieldName = "b.acc_type")
	private String accTypeName;
	/**账户性质*/
	@BtCompositeSearch(fieldName = "b.nature_id")
	private String natureId;
	@BtCompositeSearch(fieldName = "b.nature_id")
	private String natureName;
	/**银行类别*/
	@BtCompositeSearch(fieldName = "b.bank_type_id")
	private String bankTypeId;
	@BtCompositeSearch(fieldName = "b.bank_type_id")
	private String bankTypeName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.reg_date")
	private Date regDate;
	private String regDateStr;
	private Date startDate;
	private Date endDate;
	/**账户状态:N-正常,D-已注销*/
	private String status;
	/**银行账号总数量*/
	private Integer count;
	/**已注销的数量*/
	private Integer cancelCount;
	/**未注销的数量*/
	private Integer availableCount;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getRegDateStr() {
		return regDateStr;
	}
	public void setRegDateStr(String regDateStr) {
		this.regDateStr = regDateStr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCancelCount() {
		return cancelCount;
	}
	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}
	public Integer getAvailableCount() {
		return availableCount;
	}
	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
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
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
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
}
