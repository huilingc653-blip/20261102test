package com.bytter.report.domain.cms.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;

import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 银行授信使用统计VO
 * @author yangl
 *
 */
public class RatBkUseVO implements Serializable{

    private String id;
	/**
	 * 授信code
	 */
	private String ratCode;
	/**
	 * 分配ID
	 */
	private String disCode;   
	/**
	 * 授信编号
	 */
	private String ratNo;
	/** 单位代码 */
	private java.lang.String corpId;
	/** 单位代码 */
	private java.lang.String corpName;
	/** 单位代码 */
	private java.lang.String corpNo;
	/** 授信种类 */
	private java.lang.String ratType;
	/** 银行类型 */
	private java.lang.String bankType;
	/** 银行编号 */
	private java.lang.String bankCode;
	/** 银行编号 */
	private java.lang.String bankName;
	/** 币别*/
	@Currency(inject = "curName")
	private java.lang.String curCode;
	private java.lang.String curName;
	/** 授信总额度 **/
	private BigDecimal money;
	/** 占用额度 **/
	private BigDecimal useMoney;
	/** 在审占用额度 **/
	private BigDecimal approveMoney;
	/** 剩余额度 **/
	private BigDecimal leftMoney;
	/** 可用额度 **/
	private BigDecimal overPlusMoney;
	/** 授信开始日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date beginDate;

	/** 授信结束日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	/**
	 * 分配方式
	 */
	private String disWay;

	//授信模式
	private String ratMode;
	
	//冻结额度
	private BigDecimal freezeMoney;
	//追加在途额度
	private BigDecimal addMoney;
	//虚拟占用额度
	private BigDecimal virtualUration;
	//虚拟调剂额度
	private BigDecimal virtualRelief;
	//虚拟占用额度
	private BigDecimal reliefAndUrationMoney;
	/**保证金比例**/
	private BigDecimal assurepercent;
	/**费率**/
	private BigDecimal procedureFeeRate;
	
	/**
	 * 授信品种
	 */
	private String itemFlag;
	@Dict(tableName="RAT_ITEM_CUSTOM",valueFieldName="id",showFiledName="NAME",inject="itemName")
	private String itemCode;
	private String itemName;
	
	private Integer itemOrder;
	
	//综合使用标识
	private String compSign;
	
	//最高额度
	private BigDecimal maxMoney;
	//最高占用额度
	private BigDecimal useMaxMoney;
	//最高冻结额度
	private BigDecimal freezeMaxMoney;
	//最高追加在途额度
	private BigDecimal addMaxMoney;
	//虚拟占用的最高额度
	private BigDecimal virtualMaxMoney;
    //虚拟在途占用的最高额度
    private BigDecimal virtualUrationMaxMoney;
	//单位
	private List<RatBkUseCorpVO> corpDtls;
	private int totalSize;

	/**备注*/
	private String remark;
	/**授信方式*/
	private String creditType;
	/**保证金（已执行）*/
	private BigDecimal bondApply;
	/**保证金（已执行汇率）*/
	private BigDecimal exchangeRateApply;
	/**保证金（已执行币别）*/
	private String bondCurIdApply;
	/**保证金（在途）*/
	private BigDecimal bondAway;
	/**保证金（在途汇率）*/
	private BigDecimal exchangeRateAway;
	/**保证金（在途币别）*/
	private String bondCurIdAway;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRatCode() {
		return ratCode;
	}

	public void setRatCode(String ratCode) {
		this.ratCode = ratCode;
	}

	public String getDisCode() {
		return disCode;
	}

	public void setDisCode(String disCode) {
		this.disCode = disCode;
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

	public java.lang.String getCorpNo() {
		return corpNo;
	}

	public void setCorpNo(java.lang.String corpNo) {
		this.corpNo = corpNo;
	}

	public java.lang.String getRatType() {
		return ratType;
	}

	public void setRatType(java.lang.String ratType) {
		this.ratType = ratType;
	}

	public java.lang.String getBankType() {
		return bankType;
	}

	public void setBankType(java.lang.String bankType) {
		this.bankType = bankType;
	}

	public java.lang.String getBankCode() {
		return bankCode;
	}

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public java.lang.String getBankName() {
		return bankName;
	}

	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}

	public java.lang.String getCurCode() {
		return curCode;
	}

	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}

	public java.lang.String getCurName() {
		return curName;
	}

	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getUseMoney() {
        if (EmptyUtils.isEmpty(useMoney)) {
            useMoney = BigDecimal.ZERO;
        }
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public BigDecimal getApproveMoney() {
		return approveMoney;
	}

	public void setApproveMoney(BigDecimal approveMoney) {
		this.approveMoney = approveMoney;
	}

	public BigDecimal getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(BigDecimal leftMoney) {
		this.leftMoney = leftMoney;
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

	public String getDisWay() {
		return disWay;
	}

	public void setDisWay(String disWay) {
		this.disWay = disWay;
	}

	public String getRatMode() {
		return ratMode;
	}

	public void setRatMode(String ratMode) {
		this.ratMode = ratMode;
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

	public BigDecimal getAddMoney() {
        if (null == this.addMoney) {
            this.addMoney = BigDecimal.ZERO;
        }
		return addMoney;
	}

	public void setAddMoney(BigDecimal addMoney) {
		this.addMoney = addMoney;
	}

	public BigDecimal getOverPlusMoney() {
        if (null == this.overPlusMoney) {
            this.overPlusMoney = BigDecimal.ZERO;
        }
		return overPlusMoney;
	}

	public void setOverPlusMoney(BigDecimal overPlusMoney) {
		this.overPlusMoney = overPlusMoney;
	}

	public BigDecimal getVirtualUration() {
        if (null == virtualUration) {
            virtualUration = BigDecimal.ZERO;
        }
		return virtualUration;
	}

	public void setVirtualUration(BigDecimal virtualUration) {
		this.virtualUration = virtualUration;
	}

	public BigDecimal getVirtualRelief() {
        if (null == this.virtualRelief) {
            this.virtualRelief = BigDecimal.ZERO;
        }
		return virtualRelief;
	}

	public void setVirtualRelief(BigDecimal virtualRelief) {
		this.virtualRelief = virtualRelief;
	}

	public BigDecimal getReliefAndUrationMoney() {
        if (null == reliefAndUrationMoney) {
            reliefAndUrationMoney = BigDecimal.ZERO;
        }
		return reliefAndUrationMoney;
	}

	public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
		this.reliefAndUrationMoney = reliefAndUrationMoney;
	}

    public BigDecimal getVirtualUrationMaxMoney() {
        if (null == this.virtualUrationMaxMoney) {
            this.virtualUrationMaxMoney = BigDecimal.ZERO;
        }
        return virtualUrationMaxMoney;
    }

    public void setVirtualUrationMaxMoney(BigDecimal virtualUrationMaxMoney) {
        this.virtualUrationMaxMoney = virtualUrationMaxMoney;
    }

	public String getRatNo() {
		return ratNo;
	}

	public void setRatNo(String ratNo) {
		this.ratNo = ratNo;
	}

	public String getItemFlag() {
		return itemFlag;
	}

	public void setItemFlag(String itemFlag) {
		this.itemFlag = itemFlag;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getCompSign() {
		return compSign;
	}

	public void setCompSign(String compSign) {
		this.compSign = compSign;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public BigDecimal getUseMaxMoney() {
        if (null == this.useMaxMoney) {
            this.useMaxMoney = BigDecimal.ZERO;
        }
		return useMaxMoney;
	}

	public void setUseMaxMoney(BigDecimal useMaxMoney) {
		this.useMaxMoney = useMaxMoney;
	}

	public BigDecimal getFreezeMaxMoney() {
        if (null == this.freezeMaxMoney) {
            this.freezeMaxMoney = BigDecimal.ZERO;
        }
		return freezeMaxMoney;
	}

	public void setFreezeMaxMoney(BigDecimal freezeMaxMoney) {
		this.freezeMaxMoney = freezeMaxMoney;
	}

	public BigDecimal getAddMaxMoney() {
		return addMaxMoney;
	}

	public void setAddMaxMoney(BigDecimal addMaxMoney) {
		this.addMaxMoney = addMaxMoney;
	}

	public BigDecimal getVirtualMaxMoney() {
		if (null == this.virtualMaxMoney) {
			this.virtualMaxMoney = BigDecimal.ZERO;
		}
		return virtualMaxMoney;
	}

	public void setVirtualMaxMoney(BigDecimal virtualMaxMoney) {
		this.virtualMaxMoney = virtualMaxMoney;
	}

	public List<RatBkUseCorpVO> getCorpDtls() {
		return corpDtls;
	}

	public void setCorpDtls(List<RatBkUseCorpVO> corpDtls) {
		this.corpDtls = corpDtls;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	// 添加单位明细
	public void addCorpDtl(RatBkUseCorpVO corpDtl) {
		if (null == this.corpDtls) {
			this.corpDtls = new ArrayList<RatBkUseCorpVO>();
		}
		this.corpDtls.add(corpDtl);
	}

	public BigDecimal getAssurepercent() {
		return assurepercent;
	}

	public void setAssurepercent(BigDecimal assurepercent) {
		this.assurepercent = assurepercent;
	}

	public BigDecimal getProcedureFeeRate() {
		return procedureFeeRate;
	}

	public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
		this.procedureFeeRate = procedureFeeRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public BigDecimal getBondApply() {
		return bondApply;
	}

	public void setBondApply(BigDecimal bondApply) {
		this.bondApply = bondApply;
	}

	public BigDecimal getExchangeRateApply() {
		return exchangeRateApply;
	}

	public void setExchangeRateApply(BigDecimal exchangeRateApply) {
		this.exchangeRateApply = exchangeRateApply;
	}

	public BigDecimal getBondAway() {
		return bondAway;
	}

	public void setBondAway(BigDecimal bondAway) {
		this.bondAway = bondAway;
	}

	public BigDecimal getExchangeRateAway() {
		return exchangeRateAway;
	}

	public void setExchangeRateAway(BigDecimal exchangeRateAway) {
		this.exchangeRateAway = exchangeRateAway;
	}

	public String getBondCurIdApply() {
		return bondCurIdApply;
	}

	public void setBondCurIdApply(String bondCurIdApply) {
		this.bondCurIdApply = bondCurIdApply;
	}

	public String getBondCurIdAway() {
		return bondCurIdAway;
	}

	public void setBondCurIdAway(String bondCurIdAway) {
		this.bondCurIdAway = bondCurIdAway;
	}
}
