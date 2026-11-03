package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: Entity
 * @Description: 银行账号明细录入表
 * @author onlineGenerator
 * @date 2017-06-29 13:51:01
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("BtBankAccDtlRpaVO")
@Data
public class BtBankAccDtlRpaVO extends ExcelModel implements java.io.Serializable,IBusinessKey {
	private String id;
	/** 业务单号 */
	@BtCompositeSearch(fieldName="t.acc_No")
	private String accNo;
	/** 单位ID */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName="t.corp_id")
	private String corpId;
	/** 单位 */
	@BtCompositeSearch(fieldName="t.corp_id")
	private String corpName;
	/** 本方账号 */
	@Excel(name = "银行账号(必填)")
	@BtCompositeSearch(fieldName="t.bank_acc")
	private String bankAcc;
	/** 本方账户名 */
	@BtCompositeSearch(fieldName="t.acc_name")
	private String accName;
	/** 本方账户开户行 */
	@BtCompositeSearch(fieldName="t.bank_name")
	private String bankName;
	/** 接口代码 */
	@BtCompositeSearch(fieldName="t.bif_code")
	@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	@BtCompositeSearch(fieldName="t.bif_code")
	private String bifName;
	/** 对方账号 */
	@Excel(name = "对方账户(必填)")
	@BtCompositeSearch(fieldName="t.opp_acc_no")
	private String oppAccNo;
	/** 对方账户名 */
	@Excel(name = "对方户名(必填)")
	@BtCompositeSearch(fieldName="t.opp_acc_name")
	private String oppAccName;
	/** 对方账户开户行 */
	@Excel(name = "对方开户行")
	@BtCompositeSearch(fieldName="t.opp_acc_bank")
	private String oppAccBank;

	/*单据类型*/
	private String receiptType;


	@Excel(name = "收支标志(必填)")
	@BtCompositeSearch(fieldName="t.cd_sign")
	private String cdSign;

	/** 红蓝标志 1-红 2-蓝 */
	@BtCompositeSearch(fieldName = "t.rb_sign")
	private String rbSign;

	/** 交易金额 */
	@Excel(name = "金额(必填)",numFormat = "##########.00",align = HorizontalAlignment.RIGHT)
	@BtCompositeSearch(fieldName="t.amt")
	private java.math.BigDecimal amt;

	/** 即时金额 */
	@Excel(name = "即时金额",numFormat = "##########.00",align = HorizontalAlignment.RIGHT)
	@BtCompositeSearch(fieldName="t.bal")
	private java.math.BigDecimal bal;

	/** 币别ID */
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName="t.cur_id")
	private String curId;

	/** 币别：固定人民币 */
	@Excel(name = "币种编号")
	@BtCompositeSearch(fieldName="t.cur_id")
	private String curName;

	/** 用途 */
//	@Excel(name = "用途")
	private String uses;
	/** 附言 */
	private String postscript;
	/** 摘要 */
	@Excel(name = "摘要(必填)")
	@BtCompositeSearch(fieldName="t.remark")
	private String remark;

	/*交易日期*/
	@Excel(name="交易日期(必填)",  format = "yyyyMMdd")
	@JsonFormat(pattern = "yyyyMMdd")
	private Date transDate;

	/*交易时间*/
	@Excel(name="交易时间(必填)",  format = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss")
	private Date transDtlTime;

	/** 交易时间 =  交易日期+交易时间*/
	@BtCompositeSearch(fieldName="t.trans_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date transTime;


	/** 顺序号 */
	@Excel(name = "排序")
	private Integer orderNo;

	/** 月份 */
	private Integer months;

	/** 状态 */
	@BtCompositeSearch(fieldName="t.status")
	private Integer status;

	/** 创建人登录名称 */
	private String createBy;

	/** 创建日期 */
	@BtCompositeSearch(fieldName="t.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/** 更新人登录名称 */
	private String updateBy;

	/** 更新日期 */
	private Date updateDate;

	/** 暂存的流水记录 */
	private List<Map<String, Object>> tempBtBankAccDtlList;

	/** 项目信息 */
//	@Excel(name = "项目信息")
	@BtCompositeSearch(fieldName="t.project_remark")
	private String projectRemark;

	/**
	 * 银行流水号
	 */
//	@Excel(name = "银行流水号")
	@BtCompositeSearch(fieldName="t.BANK_SERIAL_NO")
	private String bankSerialNo;
	/**
	 * 查询银行账户集合
	 */
	private List<Map<String, Object>> btBankAccList;
	/**
	 * 由明细计算余额开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private Date beginDate;
	/**
	 * 由明细计算余额结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private Date endDate;

	/**
	 * 账户属性id
	 */
	private String attributeId;
	private String attributeName;
	/**
	 * 账户性质id
	 */
	private String natureId;
	private String natureName;
	/**
	 * 账户类别id
	 */
	private String accTypeId;

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	public String getNatureId() {
		return natureId;
	}

	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}

	public String getAccTypeId() {
		return accTypeId;
	}

	public void setAccTypeId(String accTypeId) {
		this.accTypeId = accTypeId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getNatureName() {
		return natureName;
	}

	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	public String getRbSign() {
		return rbSign;
	}
	public void setRbSign(String rbSign) {
		this.rbSign = rbSign;
	}
	public java.math.BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}
	public java.math.BigDecimal getBal() {
		return bal;
	}
	public void setBal(java.math.BigDecimal bal) {
		this.bal = bal;
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
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
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
	public List<Map<String, Object>> getTempBtBankAccDtlList() {
		return tempBtBankAccDtlList;
	}
	public void setTempBtBankAccDtlList(List<Map<String, Object>> tempBtBankAccDtlList) {
		this.tempBtBankAccDtlList = tempBtBankAccDtlList;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public String getProjectRemark() {
		return projectRemark;
	}
	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public List<Map<String, Object>> getBtBankAccList() {
		return btBankAccList;
	}
	public void setBtBankAccList(List<Map<String, Object>> btBankAccList) {
		this.btBankAccList = btBankAccList;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public Date getTransDtlTime() {
		return transDtlTime;
	}

	public void setTransDtlTime(Date transDtlTime) {
		this.transDtlTime = transDtlTime;
	}

	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "btBankAccDtlRpaImportService";
	}
	
	@Override
	public Map<String, String> getLangMap(){
		super.addLangMap("cdSign", "btBankAccDtl.cdSign");
		super.addLangMap("rbSign", "color_sign");
		return super.getLangMap();
	}
}
