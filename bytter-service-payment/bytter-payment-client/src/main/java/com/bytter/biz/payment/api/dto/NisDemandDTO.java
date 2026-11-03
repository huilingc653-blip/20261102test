package com.bytter.biz.payment.api.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 付款通知VO
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
public class NisDemandDTO implements Serializable {
	private String id;
	/**资金需求编号*/
	private String billNo;
	/**单位*/
	private String corpId;
	private String corpName;
	/**需求账号*/
	private String bankAcc;
	/**需求账号id*/
	private String bankAccId;
	/**户名*/
	private String accName;
	/**开户银行*/
	private String bankName;
	/**资金需求类型  01-付款,02-承兑到期,03-融资到期*/
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
	private String superCorpName;
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
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**余额*/
	private BigDecimal bal;
	/**可用余额*/
	private BigDecimal availBal;
	/**级别*/
	private Integer corpLevel;
	/**上级id*/
	private String superId;
	/**是否是汇合的数据*/
	private String isConverge;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getBankAcc() {
		return bankAcc;
	}

	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}

	public String getBankAccId() {
		return bankAccId;
	}

	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
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

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public Date getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}

	public BigDecimal getDemandMoney() {
		return demandMoney;
	}

	public void setDemandMoney(BigDecimal demandMoney) {
		this.demandMoney = demandMoney;
	}

	public BigDecimal getDemandAllocationMoney() {
		return demandAllocationMoney;
	}

	public void setDemandAllocationMoney(BigDecimal demandAllocationMoney) {
		this.demandAllocationMoney = demandAllocationMoney;
	}

	public String getPayBillId() {
		return payBillId;
	}

	public void setPayBillId(String payBillId) {
		this.payBillId = payBillId;
	}

	public String getPayBillNo() {
		return payBillNo;
	}

	public void setPayBillNo(String payBillNo) {
		this.payBillNo = payBillNo;
	}

	public Date getPayBillDate() {
		return payBillDate;
	}

	public void setPayBillDate(Date payBillDate) {
		this.payBillDate = payBillDate;
	}

	public String getSuperCorpId() {
		return superCorpId;
	}

	public void setSuperCorpId(String superCorpId) {
		this.superCorpId = superCorpId;
	}

	public String getSuperBankAcc() {
		return superBankAcc;
	}

	public void setSuperBankAcc(String superBankAcc) {
		this.superBankAcc = superBankAcc;
	}

	public String getSuperBankAccId() {
		return superBankAccId;
	}

	public void setSuperBankAccId(String superBankAccId) {
		this.superBankAccId = superBankAccId;
	}

	public String getSuperAccName() {
		return superAccName;
	}

	public void setSuperAccName(String superAccName) {
		this.superAccName = superAccName;
	}

	public String getSuperBankName() {
		return superBankName;
	}

	public void setSuperBankName(String superBankName) {
		this.superBankName = superBankName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAllocationNo() {
		return allocationNo;
	}

	public void setAllocationNo(String allocationNo) {
		this.allocationNo = allocationNo;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public Date getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(Date allocationDate) {
		this.allocationDate = allocationDate;
	}

	public String getIsSummary() {
		return isSummary;
	}

	public void setIsSummary(String isSummary) {
		this.isSummary = isSummary;
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

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getSuperCorpName() {
		return superCorpName;
	}

	public void setSuperCorpName(String superCorpName) {
		this.superCorpName = superCorpName;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public BigDecimal getAvailBal() {
		return availBal;
	}

	public void setAvailBal(BigDecimal availBal) {
		this.availBal = availBal;
	}

	public Integer getCorpLevel() {
		return corpLevel;
	}

	public void setCorpLevel(Integer corpLevel) {
		this.corpLevel = corpLevel;
	}

	public String getSuperId() {
		return superId;
	}

	public void setSuperId(String superId) {
		this.superId = superId;
	}

	public String getIsConverge() {
		return isConverge;
	}

	public void setIsConverge(String isConverge) {
		this.isConverge = isConverge;
	}
}
