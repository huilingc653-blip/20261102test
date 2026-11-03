package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 付款通知表
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_DEMAND", schema = "")
@SuppressWarnings("serial")
public class NisDemand extends IdEntity implements java.io.Serializable {
	/**资金需求编号*/
	private String billNo;
	/**单位*/
	private String corpId;
	/**需求账号*/
	private String bankAcc;
	/**需求账号id*/
	private String bankAccId;
	/**户名*/
	private String accName;
	/**开户银行*/
	private String bankName;
	/**资金需求类型  01-付款,02-承兑到期,03-融资到期，04-一级汇总，05-二级汇总，06-三级汇总*/
	private String demandType;
	/**需求日期*/
	private Date  demandDate;
	/**需求金额*/
	private BigDecimal demandMoney;
	/**需求下拨金额*/
	private BigDecimal demandAllocationMoney;
	/**单据id*/
	private String payBillId;
	/**单据单号*/
	private String payBillNo;
	/**单据的日期*/
	private Date payBillDate;
	/**上级单位*/
	private String superCorpId;
	/**上级银行账号*/
	private String superBankAcc;
	/**上级银行账号id*/
	private String superBankAccId;
	/**上级账号户名*/
	private String superAccName;
	/**上级账号开户行*/
	private String superBankName;
	/**状态*/
	private Integer status;
	/**下拨指令编码*/
	private String allocationNo;
	/**下拨指令id*/
	private String allocationId;
	/**下拨时间*/
	private Date allocationDate;
	/**是否汇总  1.汇总，0为未汇总*/
	private String isSummary;


	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**级别*/
	private Integer corpLevel;
	/**上级id*/
	private String superId;
	/**记账标志*/
	private String tallySign;
	/**记账凭证号码*/
	private String voucherNo;
	/**是否是汇合的数据*/
	private String isConverge = "0";
	/**是否是生成过头寸调拨的数据*/
	private String isPos;

	/**失败原因*/
	private String failReason;

	/**头寸调拨指令状态*/
	private String posStatus;

	/**头寸调拨数量*/
	private Integer posNum;


	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单号
	 */
	@Column(name ="BILL_NO")
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单号
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}

	@Column(name ="CORP_ID")
	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	@Column(name ="BANK_ACC")
	public String getBankAcc() {
		return bankAcc;
	}

	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}

	@Column(name ="BANK_ACC_ID")
	public String getBankAccId() {
		return bankAccId;
	}

	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}

	@Column(name ="ACC_NAME")
	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	@Column(name ="BANK_NAME")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name ="DEMAND_TYPE")
	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	@Column(name ="DEMAND_DATE")
	public Date getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}

	@Column(name ="DEMAND_MONEY")
	public BigDecimal getDemandMoney() {
		return demandMoney;
	}

	public void setDemandMoney(BigDecimal demandMoney) {
		this.demandMoney = demandMoney;
	}

	@Column(name ="DEMAND_ALLOCATION_MONEY")
	public BigDecimal getDemandAllocationMoney() {
		return demandAllocationMoney;
	}

	public void setDemandAllocationMoney(BigDecimal demandAllocationMoney) {
		this.demandAllocationMoney = demandAllocationMoney;
	}

	@Column(name ="PAY_BILL_ID")
	public String getPayBillId() {
		return payBillId;
	}

	public void setPayBillId(String payBillId) {
		this.payBillId = payBillId;
	}

	@Column(name ="PAY_BILL_NO")
	public String getPayBillNo() {
		return payBillNo;
	}

	public void setPayBillNo(String payBillNo) {
		this.payBillNo = payBillNo;
	}

	@Column(name ="PAY_BILL_DATE")
	public Date getPayBillDate() {
		return payBillDate;
	}

	public void setPayBillDate(Date payBillDate) {
		this.payBillDate = payBillDate;
	}

	@Column(name ="SUPER_CORP_ID")
	public String getSuperCorpId() {
		return superCorpId;
	}

	public void setSuperCorpId(String superCorpId) {
		this.superCorpId = superCorpId;
	}

	@Column(name ="SUPER_BANK_ACC")
	public String getSuperBankAcc() {
		return superBankAcc;
	}

	public void setSuperBankAcc(String superBankAcc) {
		this.superBankAcc = superBankAcc;
	}

	@Column(name ="SUPER_BANK_ACC_ID")
	public String getSuperBankAccId() {
		return superBankAccId;
	}

	public void setSuperBankAccId(String superBankAccId) {
		this.superBankAccId = superBankAccId;
	}

	@Column(name ="SUPER_ACC_NAME")
	public String getSuperAccName() {
		return superAccName;
	}

	public void setSuperAccName(String superAccName) {
		this.superAccName = superAccName;
	}

	@Column(name ="SUPER_BANK_NAME")
	public String getSuperBankName() {
		return superBankName;
	}

	public void setSuperBankName(String superBankName) {
		this.superBankName = superBankName;
	}

	@Column(name ="STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name ="ALLOCATION_NO")
	public String getAllocationNo() {
		return allocationNo;
	}

	public void setAllocationNo(String allocationNo) {
		this.allocationNo = allocationNo;
	}

	@Column(name ="ALLOCATION_ID")
	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	@Column(name ="ALLOCATION_DATE")
	public Date getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(Date allocationDate) {
		this.allocationDate = allocationDate;
	}

	@Column(name ="IS_SUMMARY")
	public String getIsSummary() {
		return isSummary;
	}

	public void setIsSummary(String isSummary) {
		this.isSummary = isSummary;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE")
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE")
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}

	@Column(name ="CORP_LEVEL")
	public Integer getCorpLevel() {
		return corpLevel;
	}

	public void setCorpLevel(Integer corpLevel) {
		this.corpLevel = corpLevel;
	}

	@Column(name ="SUPER_ID")
	public String getSuperId() {
		return superId;
	}

	public void setSuperId(String superId) {
		this.superId = superId;
	}

	@Column(name ="TALLY_SIGN")
	public String getTallySign() {
		return tallySign;
	}

	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}

	@Column(name ="VOUCHER_NO")
	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	@Column(name ="IS_CONVERGE")
	public String getIsConverge() {
		return isConverge;
	}

	public void setIsConverge(String isConverge) {
		this.isConverge = isConverge;
	}

	@Column(name ="IS_POS")
	public String getIsPos() {
		return isPos;
	}

	public void setIsPos(String isPos) {
		this.isPos = isPos;
	}

	@Column(name ="FAIL_REASON")
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Column(name ="POS_STATUS")
	public String getPosStatus() {
		return posStatus;
	}

	public void setPosStatus(String posStatus) {
		this.posStatus = posStatus;
	}

	@Column(name ="POS_NUM")
	public Integer getPosNum() {
		return posNum;
	}

	public void setPosNum(Integer posNum) {
		this.posNum = posNum;
	}
}
