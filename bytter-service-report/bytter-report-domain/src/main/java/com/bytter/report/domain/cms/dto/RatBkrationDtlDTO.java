package com.bytter.report.domain.cms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;

/**
 * 授信录入明细DTO
 * 
 * @author liyh
 *
 */
public class RatBkrationDtlDTO implements Serializable {

	private String id;

	/** 授信主键 */
	private java.lang.String ratCode;
	/** 品种标识 */
	private java.lang.String itemFlag;
	/** 授信品种排序 */
	private java.lang.Integer itemOrder;
	/** 授信品种 */
	@Dict(tableName="RAT_ITEM_CUSTOM",valueFieldName="id",showFiledName="NAME",inject="itemName")
	private java.lang.String itemCode;
	/** 授信品种名称 */
	private java.lang.String itemName;
	/** 授信金额 */
	private java.math.BigDecimal money;
	/** 敞口金额 */
	private java.math.BigDecimal openMoney;
	/** 占用授信金额 */
	private java.math.BigDecimal disMoney;
	/** 占用敞口金额 */
	private java.math.BigDecimal disOpenMoney;
	/** 未使用授信额度 */
	private java.math.BigDecimal unUserMoney;
	/** 未使用敞口额度 */
	private java.math.BigDecimal unUserOpenMoney;
	/** 剩余授信额度 */
	private java.math.BigDecimal leftMoney;
	/** 剩余敞口额度 */
	private java.math.BigDecimal leftOpenMoney;
	/** 最大金额 */
	private java.math.BigDecimal maxMoney;
	/** 已分配最高金额 */
	private java.math.BigDecimal disMaxMoney;
	/** 剩余最高金额 */
	private java.math.BigDecimal leftMaxMoney;
	/** 已分配授信金额 */
	private java.math.BigDecimal superDistributeMoney;
	/** 已分配授信敞口金额 */
	private java.math.BigDecimal superDistributeOpenMoney;
	/** 追加金额 */
	private java.math.BigDecimal superAddMoney;
	/** 追加敞口金额 */
	private java.math.BigDecimal superAddOpenMoney;
	/** 追加最高金额 */
	private java.math.BigDecimal superAddMaxMoney;
	/** 担保条件 */
	private java.lang.String assurecondition;
	/** 保证金比例 */
	private java.math.BigDecimal assurepercent;
	/** 利率 */
	private java.math.BigDecimal interestrate;
	/** 总额共享追加金额 */
	private java.math.BigDecimal totalMoneyAddMoney;
	/** 总额共享追加敞口额度 */
	private java.math.BigDecimal totalMoneyAddOpenMoney;
	/**保证金金额**/
	private java.math.BigDecimal bondMoney;
	/**保证金费率**/
	private java.math.BigDecimal procedureFeeRate;
	/**保证金费用**/
	private java.math.BigDecimal procedureFee;
	/**
	 * 实际调剂额度
	 */
	private BigDecimal reliefMoney;
	/**
	 * 不能选的品种
	 */
	private String disItemCodes;
	
	/**
	 * 第一个综合授信品种
	 */
	private String firstComp = "0";
	
	/**
	 * 综合额度使用标识
	 */
	private String compSign;
	
	/** 调剂金额 */
	private java.math.BigDecimal superReliefMoney;
	/** 被调剂金额 */
	private java.math.BigDecimal superByReliefMoney;
	/** 调剂敞口金额 */
	private java.math.BigDecimal superReliefOpenMoney;
	/** 被调剂敞口金额 */
	private java.math.BigDecimal superByReliefOpenMoney;
	/** 调剂最高金额 */
	private java.math.BigDecimal reliefMaxMoney;
	/** 被调剂最高金额 */
	private java.math.BigDecimal byReliefMaxMoney;
	/**是否启用授信敞口(1=启用，0=不启用)*/
	private java.lang.String isOpenRat;
	/**调整额度*/
	private java.math.BigDecimal adjustMoney;
	/**调整后额度*/
	private java.math.BigDecimal adjustedMoney;

	/**额度类型 0-不循环 1-循环*/
	private java.lang.String creditType;

    //冻结额度
    private BigDecimal freezeMoney;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.lang.String getRatCode() {
		return ratCode;
	}

	public void setRatCode(java.lang.String ratCode) {
		this.ratCode = ratCode;
	}

	public java.lang.String getItemFlag() {
		return itemFlag;
	}

	public void setItemFlag(java.lang.String itemFlag) {
		this.itemFlag = itemFlag;
	}

	public java.lang.Integer getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(java.lang.Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

	public java.lang.String getItemCode() {
		return itemCode;
	}

	public void setItemCode(java.lang.String itemCode) {
		this.itemCode = itemCode;
	}

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public java.math.BigDecimal getOpenMoney() {
		if (null == this.openMoney) {
			this.openMoney = BigDecimal.ZERO;
		}
		return openMoney;
	}

	public java.math.BigDecimal getDisMoney() {
		if (null == this.disMoney) {
			this.disMoney = BigDecimal.ZERO;
		}
		return disMoney;
	}

	public void setDisMoney(java.math.BigDecimal disMoney) {
		this.disMoney = disMoney;
	}

	public java.math.BigDecimal getDisOpenMoney() {
		if (null == this.disOpenMoney) {
			this.disOpenMoney = BigDecimal.ZERO;
		}
		return disOpenMoney;
	}

	public void setDisOpenMoney(java.math.BigDecimal disOpenMoney) {
		this.disOpenMoney = disOpenMoney;
	}

	public void setOpenMoney(java.math.BigDecimal openMoney) {
		this.openMoney = openMoney;
	}

	public java.math.BigDecimal getUnUserMoney() {
		return unUserMoney;
	}

	public void setUnUserMoney(java.math.BigDecimal unUserMoney) {
		this.unUserMoney = unUserMoney;
	}

	public java.math.BigDecimal getUnUserOpenMoney() {
		return unUserOpenMoney;
	}

	public void setUnUserOpenMoney(java.math.BigDecimal unUserOpenMoney) {
		this.unUserOpenMoney = unUserOpenMoney;
	}

	public java.math.BigDecimal getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(java.math.BigDecimal leftMoney) {
		this.leftMoney = leftMoney;
	}

	public java.math.BigDecimal getLeftOpenMoney() {
		return leftOpenMoney;
	}

	public void setLeftOpenMoney(java.math.BigDecimal leftOpenMoney) {
		this.leftOpenMoney = leftOpenMoney;
	}

	public java.math.BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(java.math.BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public java.math.BigDecimal getDisMaxMoney() {
		return NumberUtil.formatNumericNull(this.disMaxMoney);
	}

	public void setDisMaxMoney(java.math.BigDecimal disMaxMoney) {
		this.disMaxMoney = disMaxMoney;
	}

	public java.math.BigDecimal getLeftMaxMoney() {
		return leftMaxMoney;
	}

	public void setLeftMaxMoney(java.math.BigDecimal leftMaxMoney) {
		this.leftMaxMoney = leftMaxMoney;
	}

	public java.math.BigDecimal getSuperDistributeMoney() {
		return superDistributeMoney;
	}

	public void setSuperDistributeMoney(java.math.BigDecimal superDistributeMoney) {
		this.superDistributeMoney = superDistributeMoney;
	}

	public java.math.BigDecimal getSuperDistributeOpenMoney() {
		return superDistributeOpenMoney;
	}

	public void setSuperDistributeOpenMoney(java.math.BigDecimal superDistributeOpenMoney) {
		this.superDistributeOpenMoney = superDistributeOpenMoney;
	}

	public java.math.BigDecimal getSuperAddMoney() {
		return superAddMoney;
	}

	public void setSuperAddMoney(java.math.BigDecimal superAddMoney) {
		this.superAddMoney = superAddMoney;
	}

	public java.math.BigDecimal getSuperAddOpenMoney() {
		return superAddOpenMoney;
	}

	public void setSuperAddOpenMoney(java.math.BigDecimal superAddOpenMoney) {
		this.superAddOpenMoney = superAddOpenMoney;
	}

	public java.lang.String getAssurecondition() {
		return assurecondition;
	}

	public void setAssurecondition(java.lang.String assurecondition) {
		this.assurecondition = assurecondition;
	}

	public java.math.BigDecimal getAssurepercent() {
		if (null == this.assurepercent) {
			this.assurepercent = BigDecimal.ZERO;
		}
		return assurepercent;
	}

	public void setAssurepercent(java.math.BigDecimal assurepercent) {
		this.assurepercent = assurepercent;
	}

	public java.math.BigDecimal getInterestrate() {
		if (null == this.interestrate) {
			this.interestrate = BigDecimal.ZERO;
		}
		return interestrate;
	}

	public void setInterestrate(java.math.BigDecimal interestrate) {
		this.interestrate = interestrate;
	}

	public java.math.BigDecimal getTotalMoneyAddMoney() {
		return totalMoneyAddMoney;
	}

	public void setTotalMoneyAddMoney(java.math.BigDecimal totalMoneyAddMoney) {
		this.totalMoneyAddMoney = totalMoneyAddMoney;
	}

	public java.math.BigDecimal getTotalMoneyAddOpenMoney() {
		return totalMoneyAddOpenMoney;
	}

	public void setTotalMoneyAddOpenMoney(java.math.BigDecimal totalMoneyAddOpenMoney) {
		this.totalMoneyAddOpenMoney = totalMoneyAddOpenMoney;
	}

	public BigDecimal getReliefMoney() {
		return reliefMoney;
	}

	public void setReliefMoney(BigDecimal reliefMoney) {
		this.reliefMoney = reliefMoney;
	}

	public String getDisItemCodes() {
		return disItemCodes;
	}

	public void setDisItemCodes(String disItemCodes) {
		this.disItemCodes = disItemCodes;
	}

	public String getFirstComp() {
		return firstComp;
	}

	public void setFirstComp(String firstComp) {
		this.firstComp = firstComp;
	}

	public String getCompSign() {
		return compSign;
	}

	public void setCompSign(String compSign) {
		this.compSign = compSign;
	}
	
	public java.math.BigDecimal getSuperAddMaxMoney() {
		return superAddMaxMoney;
	}

	public void setSuperAddMaxMoney(java.math.BigDecimal superAddMaxMoney) {
		this.superAddMaxMoney = superAddMaxMoney;
	}

	public java.math.BigDecimal getSuperReliefMoney() {
		if (null == this.superReliefMoney) {
			this.superReliefMoney = BigDecimal.ZERO;
		}
		return superReliefMoney;
	}

	public void setSuperReliefMoney(java.math.BigDecimal superReliefMoney) {
		this.superReliefMoney = superReliefMoney;
	}

	public java.math.BigDecimal getSuperByReliefMoney() {
		if (null == this.superByReliefMoney) {
			this.superByReliefMoney = BigDecimal.ZERO;
		}
		return superByReliefMoney;
	}

	public void setSuperByReliefMoney(java.math.BigDecimal superByReliefMoney) {
		this.superByReliefMoney = superByReliefMoney;
	}

	public java.math.BigDecimal getSuperReliefOpenMoney() {
		return superReliefOpenMoney;
	}

	public void setSuperReliefOpenMoney(java.math.BigDecimal superReliefOpenMoney) {
		this.superReliefOpenMoney = superReliefOpenMoney;
	}

	public java.math.BigDecimal getSuperByReliefOpenMoney() {
		return superByReliefOpenMoney;
	}

	public void setSuperByReliefOpenMoney(java.math.BigDecimal superByReliefOpenMoney) {
		this.superByReliefOpenMoney = superByReliefOpenMoney;
	}

	public java.math.BigDecimal getReliefMaxMoney() {
		return reliefMaxMoney;
	}

	public void setReliefMaxMoney(java.math.BigDecimal reliefMaxMoney) {
		this.reliefMaxMoney = reliefMaxMoney;
	}

	public java.math.BigDecimal getByReliefMaxMoney() {
		return byReliefMaxMoney;
	}

	public void setByReliefMaxMoney(java.math.BigDecimal byReliefMaxMoney) {
		this.byReliefMaxMoney = byReliefMaxMoney;
	}

	public BigDecimal getBondMoney() {
		return bondMoney;
	}

	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}

	public BigDecimal getProcedureFeeRate() {
		return procedureFeeRate;
	}

	public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
		this.procedureFeeRate = procedureFeeRate;
	}

	public BigDecimal getProcedureFee() {
		return procedureFee;
	}

	public void setProcedureFee(BigDecimal procedureFee) {
		this.procedureFee = procedureFee;
	}

	public String getIsOpenRat() {
		return isOpenRat;
	}

	public void setIsOpenRat(String isOpenRat) {
		this.isOpenRat = isOpenRat;
	}

	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}

	public BigDecimal getAdjustedMoney() {
		return adjustedMoney;
	}

	public void setAdjustedMoney(BigDecimal adjustedMoney) {
		this.adjustedMoney = adjustedMoney;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

    public BigDecimal getFreezeMoney() {
	    if (EmptyUtils.isEmpty(freezeMoney)) {
	        freezeMoney = BigDecimal.ZERO;
        }
        return freezeMoney;
    }

    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
}
