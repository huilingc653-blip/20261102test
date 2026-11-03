package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author wangzx
 * @Description 结算量统计表明细
 * @date 2019.9.20
 */
public class AllocationSettlementDtlVO implements Serializable {
	private String id;
	/**交易时间*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName = "d.trans_Time")
	private Date transTime;
	/*** 单位ID*/
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpId;
	private String corpName;
	/*** 本方账号*/
	@BtCompositeSearch(fieldName = "acc.bank_acc")
	private String bankAcc;
	/*** 本方账户名*/
	private String accName;
	/*** 本方账户开户行*/
	@BtCompositeSearch(fieldName = "d.bank_name")
	private String bankName;
	/*** 借贷标志*/
	@BtCompositeSearch(fieldName = "d.cd_sign")
	private String cdSign;
	/*** 币别ID*/
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName = "d.cur_id")
	private String curId;
	private String curName;
	/**
	 * 交易金额
	 */
	@BtCompositeSearch(fieldName = "d.amt")
	private BigDecimal amt;
	/**
	 * 余额
	 */
	@BtCompositeSearch(fieldName = "d.bal")
	private BigDecimal bal;
	/*** 对方账号*/
	private String oppAccNo;
	/*** 对方账户名*/
	private String oppAccName;
	/*** 对方账户开户行*/
	private String oppAccBank;
	/** 摘要*/
	@BtCompositeSearch(fieldName = "d.remark")
	private String remark;
	/**账户属性**/
	@BtCompositeSearch(fieldName = "acc.attribute_id")
	private String attributeId;
	@BtCompositeSearch(fieldName = "acc.attribute_id")
	private String attributeName;
	/**项目信息**/
	private String projectRemark;
	/**开始日期*/
	@BtCompositeSearch(fieldName = "d.trans_Time")
	private Date beginDate;
	/**结束日期*/
	@BtCompositeSearch(fieldName = "d.trans_Time")
	private Date endDate;
	@BtCompositeSearch(fieldName = "acc.bank_type_id")
	private String bankTypeId;
	@BtCompositeSearch(fieldName = "bt.type_name")
	private String typeName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
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
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
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
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getProjectRemark() {
		return projectRemark;
	}
	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
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
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
