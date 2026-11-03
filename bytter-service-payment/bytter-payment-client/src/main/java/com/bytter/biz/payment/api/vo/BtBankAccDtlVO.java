package com.bytter.biz.payment.api.vo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
/**   
 * @Title: Entity
 * @Description: 银行账号明细录入表
 * @author onlineGenerator
 * @date 2017-06-29 13:51:01
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("BtBankAccDtlVO")
public class BtBankAccDtlVO extends ExcelModel implements java.io.Serializable,IBusinessKey {
	@BtCompositeSearch(fieldName="t.id")
	private java.lang.String id;
	/** 业务单号 */
	@BtCompositeSearch(fieldName="t.acc_No")
	private java.lang.String accNo;
	/** 单位ID */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName="t.corp_id")
	private java.lang.String corpId;
	/** 单位 */
	@BtCompositeSearch(fieldName="t.corp_id")
	private java.lang.String corpName;
	/** 本方账号 */
	@Excel(name = "*本方账号")
	@BtCompositeSearch(fieldName="t.bank_acc")
	private java.lang.String bankAcc;
	/** 本方账户名 */
	@BtCompositeSearch(fieldName="t.acc_name")
	private java.lang.String accName;
	/** 本方账户开户行 */
	@BtCompositeSearch(fieldName="t.bank_name")
	private java.lang.String bankName;
	/** 接口代码 */
	@BtCompositeSearch(fieldName="t.bif_code")
	@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private java.lang.String bifCode;
	@BtCompositeSearch(fieldName="t.bif_code")
	private java.lang.String bifName;
	/** 对方账号 */
	@Excel(name = "对方账号")
	@BtCompositeSearch(fieldName="t.opp_acc_no")
	private java.lang.String oppAccNo;
	/** 对方账户名 */
	@Excel(name = "对方账户名")
	@BtCompositeSearch(fieldName="t.opp_acc_name")
	private java.lang.String oppAccName;
	/** 对方账户开户行 */
	@Excel(name = "对方开户行")
	@BtCompositeSearch(fieldName="t.opp_acc_bank")
	private java.lang.String oppAccBank;
	/** 借贷标志 0-收 1-支 */
	@Excel(name = "*借贷标志")
	@BtCompositeSearch(fieldName="t.cd_sign")
	private java.lang.String cdSign;
	/** 红蓝标志 1-红 2-蓝 */
	@BtCompositeSearch(fieldName = "t.rb_sign")
	private java.lang.String rbSign;
	/** 交易金额 */
	@Excel(name = "*金额",numFormat = "##########.00",align = HorizontalAlignment.RIGHT)
	@BtCompositeSearch(fieldName="t.amt")
	private java.math.BigDecimal amt;
	/** 余额 */
	@Excel(name = "余额",numFormat = "##########.00",align = HorizontalAlignment.RIGHT)
	@BtCompositeSearch(fieldName="t.bal")
	private java.math.BigDecimal bal;
	/** 币别ID */
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName="t.cur_id")
	private java.lang.String curId;
	/** 币别 */
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName="t.cur_id")
	private java.lang.String curName;
	/** 用途 */
	@Excel(name = "用途")
	private java.lang.String uses;
	/** 附言 */
	private java.lang.String postscript;
	/** 摘要 */
	@Excel(name = "摘要")
	@BtCompositeSearch(fieldName="t.remark")
	private java.lang.String remark;
	/** 交易时间 */
	@Excel(name = "*交易时间", format = "yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="t.trans_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date transTime;
	/** 顺序号 */
	@Excel(name = "排序")
	private java.lang.Integer orderNo;
	/** 月份 */
	private java.lang.Integer months;
	/** 状态 */
	@BtCompositeSearch(fieldName="t.status")
	private java.lang.Integer status;
	/** 创建人登录名称 */
	private java.lang.String createBy;
	/** 创建日期 */
	@BtCompositeSearch(fieldName="t.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createDate;
	/** 更新人登录名称 */
	private java.lang.String updateBy;
	/** 更新日期 */
	private java.util.Date updateDate;
	/** 暂存的流水记录 */
	private List<Map<String, Object>> tempBtBankAccDtlList;
	/** 项目信息 */
	@Excel(name = "项目信息")
	@BtCompositeSearch(fieldName="t.project_remark")
	private java.lang.String projectRemark;
	/**
	 * 银行流水号
	 */
	@Excel(name = "银行流水号")
	@BtCompositeSearch(fieldName="t.BANK_SERIAL_NO")
	private java.lang.String bankSerialNo;
	/**
	 * 查询银行账户集合
	 */
	private List<Map<String, Object>> btBankAccList;
	/**
	 * 由明细计算余额开始时间
	 */
	private Date beginDate;
	/**
	 * 由明细计算余额结束时间
	 */
	private Date endDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getAccNo() {
		return accNo;
	}
	public void setAccNo(java.lang.String accNo) {
		this.accNo = accNo;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getBankName() {
		return bankName;
	}
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	public java.lang.String getBifCode() {
		return bifCode;
	}
	public void setBifCode(java.lang.String bifCode) {
		this.bifCode = bifCode;
	}
	public java.lang.String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(java.lang.String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public java.lang.String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(java.lang.String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public java.lang.String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(java.lang.String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public java.lang.String getCdSign() {
		return cdSign;
	}
	public void setCdSign(java.lang.String cdSign) {
		this.cdSign = cdSign;
	}
	public java.lang.String getRbSign() {
		return rbSign;
	}
	public void setRbSign(java.lang.String rbSign) {
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
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getUses() {
		return uses;
	}
	public void setUses(java.lang.String uses) {
		this.uses = uses;
	}
	public java.lang.String getPostscript() {
		return postscript;
	}
	public void setPostscript(java.lang.String postscript) {
		this.postscript = postscript;
	}
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	public java.util.Date getTransTime() {
		return transTime;
	}
	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}
	public java.lang.Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.Integer orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.Integer getMonths() {
		return months;
	}
	public void setMonths(java.lang.Integer months) {
		this.months = months;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
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
	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "btBankAccDtlService";
	}
	
	@Override
	public Map<String, String> getLangMap(){
		super.addLangMap("cdSign", "btBankAccDtl.cdSign");
		super.addLangMap("rbSign", "color_sign");
		return super.getLangMap();
	}
}
