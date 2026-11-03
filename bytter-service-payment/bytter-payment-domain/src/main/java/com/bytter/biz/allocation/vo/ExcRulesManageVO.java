package com.bytter.biz.allocation.vo;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
public class ExcRulesManageVO implements Serializable{
	private String id;
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
	/**生效日期*/
	private Date   luanchTime;
	/**有效标识*/
	private String validSign;
	/**附言*/
	private String rmk;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	/**付款账号*/
	private List<ExcRulesManagePayerAccNoVO> payerAccNos;
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
	public Date getLuanchTime() {
		return luanchTime;
	}
	public void setLuanchTime(Date luanchTime) {
		luanchTime = luanchTime;
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
	public List<ExcRulesManagePayerAccNoVO> getPayerAccNos() {
		return payerAccNos;
	}
	public void setPayerAccNos(List<ExcRulesManagePayerAccNoVO> payerAccNos) {
		this.payerAccNos = payerAccNos;
	}
	
}
