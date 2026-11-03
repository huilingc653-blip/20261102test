package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;

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
public class NisDemandVO  implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 3650731181127660208L;
	private String id;
	/**资金需求编号*/
	@BtCompositeSearch(fieldName = "n.bill_No")
	private String billNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "n.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "corp1.name")
	private String corpName;
	/**需求账号*/
	@BtCompositeSearch(fieldName = "n.bank_acc")
	private String bankAcc;
	/**需求账号id*/
	private String bankAccId;
	/**户名*/
	private String accName;
	/**开户银行*/
	private String bankName;
	/**资金需求类型  01-付款,02-承兑到期,03-融资到期，04-工资配置，05-工资单据*/
	@BtCompositeSearch(fieldName = "n.demand_type")
	private String demandType;

	@BtCompositeSearch(fieldName = "n.demand_type")
	private String demandTypeName;
	/**需求日期*/
	@BtCompositeSearch(fieldName = "n.demand_date")
	private Date  demandDate;
	/**需求金额*/
	@BtCompositeSearch(fieldName = "n.demand_money")
	private BigDecimal demandMoney;
	/**需求下拨金额*/
	private BigDecimal demandAllocationMoney;
	/**单据id*/
	private String payBillId;
	/**单据单号*/
	@BtCompositeSearch(fieldName = "n.pay_bill_no")
	private String payBillNo;
	/**单据的日期*/
	@BtCompositeSearch(fieldName = "n.pay_bill_date")
	private Date payBillDate;
	/**上级单位*/
	@BtCompositeSearch(fieldName = "n.super_corp_id")
	private String superCorpId;
	private String superCorpName;
	/**上级银行账号*/
	@BtCompositeSearch(fieldName = "n.super_bank_acc")
	private String superBankAcc;
	/**上级银行账号id*/
	private String superBankAccId;
	/**上级账号户名*/
	private String superAccName;
	/**上级账号开户行*/
	private String superBankName;
	/**状态*/
	@BtCompositeSearch(fieldName = "n.status")
	private Integer status;

	@BtCompositeSearch(fieldName = "n.status")
	private String statusName;
	/**下拨指令编码*/
	private String allocationNo;
	/**下拨指令id*/
	private String allocationId;
	/**下拨时间*/
	private Date allocationDate;
	/**是否汇总  1.汇总，0为未汇总*/
	private String isSummary;

	private String isSummaryName;


	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**余额*/
	private BigDecimal bal;
	/**可用余额*/
	private BigDecimal availBal;
	/**上级账号余额*/
	private BigDecimal superBal;
	/**上级账号可用余额*/
	private BigDecimal superAvailBal;
	/**级别*/
	private Integer corpLevel;
	/**上级id*/
	private String superId;
	/**记账标志*/
	private String tallySign;
	/**记账凭证号码*/
	private String voucherNo;
	/**账号余额*/
	private BigDecimal bankAccBal;
	/**账号可用余额*/
	private BigDecimal bankAccAvailBal;
	/**指令状态*/
	private String voucherStat;

	private String voucherStatName;
	/**是否是汇合的数据*/
	@BtCompositeSearch(fieldName = "n.is_converge")
	private String isConverge;
	/**是否是生成过头寸调拨的数据*/
	private String isPos;
	private String isPosName;

	/**失败原因*/
	private String failReason;

	/**头寸指令状态*/
	private String posStatus;

	private String posStatusName;

	/**头寸数量*/
	private Integer posNum;


	public String getDemandTypeName() {
		return demandTypeName;
	}

	public void setDemandTypeName(String demandTypeName) {
		this.demandTypeName = demandTypeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getIsSummaryName() {
		return isSummaryName;
	}

	public void setIsSummaryName(String isSummaryName) {
		this.isSummaryName = isSummaryName;
	}

	public String getVoucherStatName() {
		return voucherStatName;
	}

	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}

	public String getPosStatusName() {
		return posStatusName;
	}

	public void setPosStatusName(String posStatusName) {
		this.posStatusName = posStatusName;
	}

	public String getIsPosName() {
		return isPosName;
	}

	public void setIsPosName(String isPosName) {
		this.isPosName = isPosName;
	}

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

	public BigDecimal getSuperBal() {
		return superBal;
	}

	public void setSuperBal(BigDecimal superBal) {
		this.superBal = superBal;
	}

	public BigDecimal getSuperAvailBal() {
		return superAvailBal;
	}

	public void setSuperAvailBal(BigDecimal superAvailBal) {
		this.superAvailBal = superAvailBal;
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

	public String getTallySign() {
		return tallySign;
	}

	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public BigDecimal getBankAccBal() {
		return bankAccBal;
	}

	public void setBankAccBal(BigDecimal bankAccBal) {
		this.bankAccBal = bankAccBal;
	}

	public BigDecimal getBankAccAvailBal() {
		return bankAccAvailBal;
	}

	public void setBankAccAvailBal(BigDecimal bankAccAvailBal) {
		this.bankAccAvailBal = bankAccAvailBal;
	}

	public String getVoucherStat() {
		return voucherStat;
	}

	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}

	public String getIsConverge() {
		return isConverge;
	}

	public void setIsConverge(String isConverge) {
		this.isConverge = isConverge;
	}

	public String getIsPos() {
		return isPos;
	}

	public void setIsPos(String isPos) {
		this.isPos = isPos;
	}


	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getPosStatus() {
		return posStatus;
	}

	public void setPosStatus(String posStatus) {
		this.posStatus = posStatus;
	}

	public Integer getPosNum() {
		return posNum;
	}

	public void setPosNum(Integer posNum) {
		this.posNum = posNum;
	}

	@Override
	public String getBusinessKey() {
		return getId();
	}
}
