package com.bytter.biz.allocation.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
public class ExcRulesManageAdCorpVO extends ExcelModel implements Serializable,IBusinessKey {
	private String id;
	/**规则编码*/
	@BtCompositeSearch(fieldName = "b.filter_code")
	private String filterCode;
	/**规则名称*/
	@BtCompositeSearch(fieldName = "b.filter_name")
	private String filterName;
	/**单位编号*/
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpId;
	/**单位名称*/
	@Excel(name = "*单位名称")
	@NotNull( message = "nis.netbank.message.corpname.null")
	private String corpName;
	/**单位编码*/
//	@BtCompositeSearch(fieldName = "s.code")
	private String code;
	/**收款账号ID*/
	@BtCompositeSearch(fieldName = "b.payee_acc_id")
	private String payeeAccId;
	/**收款账号*/
	@BtCompositeSearch(fieldName = "b.payee_acc_no")
	private String payeeAccNo;
	/**付款账号ID*/
	@BtCompositeSearch(fieldName = "b.payer_acc_id")
	private String payerAccId;
	/**付款账号*/
	@BtCompositeSearch(fieldName = "b.payer_acc_no")
	private String payerAccNo;
	/**生效日期*/
	@BtCompositeSearch(fieldName = "b.launch_time")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date   luanchTime;
	/**有效标识*/
	@BtCompositeSearch(fieldName = "b.valid_sign")
	@Dict(dictGroup = "valid_sign",inject = "validSignName")
	private String validSign;
	private String validSignName;
	/**附言*/
	@BtCompositeSearch(fieldName = "b.rmk")
	private String rmk;
	/**创建人登录名称*/
	@BtCompositeSearch(fieldName = "b.create_by")
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "b.create_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	@BtCompositeSearch(fieldName = "b.update_by")
	private String updateBy;
	/**更新日期*/
	@BtCompositeSearch(fieldName = "b.update_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilterCode() {
		return filterCode;
	}
	public void setFilterCode(String filterCode) {
		this.filterCode = filterCode;
	}
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPayeeAccId() {
		return payeeAccId;
	}
	public void setPayeeAccId(String payeeAccId) {
		this.payeeAccId = payeeAccId;
	}
	public String getPayeeAccNo() {
		return payeeAccNo;
	}
	public void setPayeeAccNo(String payeeAccNo) {
		this.payeeAccNo = payeeAccNo;
	}
	public String getPayerAccId() {
		return payerAccId;
	}
	public void setPayerAccId(String payerAccId) {
		this.payerAccId = payerAccId;
	}
	public String getPayerAccNo() {
		return payerAccNo;
	}
	public void setPayerAccNo(String payerAccNo) {
		this.payerAccNo = payerAccNo;
	}
	public Date getLuanchTime() {
		return luanchTime;
	}
	public void setLuanchTime(Date luanchTime) {
		this.luanchTime = luanchTime;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
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

	public String getValidSignName() {
		return validSignName;
	}

	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}

	@Override
	public String getBusinessKey() {
		
		return "excRulesManageService";
	}
	
	
}
