package com.bytter.biz.allocation.entity;
import com.bytter.core.base.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * @Title:entity
 * @Description:例外规则表
 * @author zhoup
 *
 */
@Entity
@Table(name="BIS_POLICY_FILTER",schema="")
@SuppressWarnings("serial")
public class ExcRulesManage extends IdEntity implements Serializable{
	/**规则编码*/
	private String filterCode;
	/**规则名称*/
	private String filterName;
	/**单位编号*/
	private String corpId;
	/**收款账号ID*/
	private String payeeAccId;
	/**收款账号*/
	private String payeeAccNo;
	/**付款账号ID*/
	private String payerAccId;
	/**付款账号*/
	private String payerAccNo;
	/**生效日期*/
	private Date   luanchTime;
	/**有效标识*/
	private String validSign;
	/**附言*/
	private String rmk;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	
	@Column(name ="FILTER_CODE",nullable=true,length=8)
	public String getFilterCode() {
		return filterCode;
	}
	public void setFilterCode(String filterCode) {
		this.filterCode = filterCode;
	}
	@Column(name ="FILTER_NAME",nullable=true,length=50)
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="PAYEE_ACC_ID",nullable=true,length=32)
	public String getPayeeAccId() {
		return payeeAccId;
	}
	public void setPayeeAccId(String payeeAccId) {
		this.payeeAccId = payeeAccId;
	}
	@Column(name ="PAYEE_ACC_NO",nullable=true,length=50)
	public String getPayeeAccNo() {
		return payeeAccNo;
	}
	public void setPayeeAccNo(String payeeAccNo) {
		this.payeeAccNo = payeeAccNo;
	}
	@Column(name ="PAYER_ACC_ID",nullable=true,length=32)
	public String getPayerAccId() {
		return payerAccId;
	}
	public void setPayerAccId(String payerAccId) {
		this.payerAccId = payerAccId;
	}
	@Column(name ="PAYER_ACC_NO",nullable=true,length=50)
	public String getPayerAccNo() {
		return payerAccNo;
	}
	public void setPayerAccNo(String payerAccNo) {
		this.payerAccNo = payerAccNo;
	}
	@Column(name ="LAUNCH_TIME",nullable=true)
	public Date getLuanchTime() {
		return luanchTime;
	}
	public void setLuanchTime(Date luanchTime) {
		luanchTime = luanchTime;
	}
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name ="RMK",nullable=true,length=100)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="CREATE_BY",nullable=true,length=32)
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name ="UPDATE_BY",nullable=true,length=32)
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
}
