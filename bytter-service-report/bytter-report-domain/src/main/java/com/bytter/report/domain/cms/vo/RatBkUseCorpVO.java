package com.bytter.report.domain.cms.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.bytter.core.annotation.Currency;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.fasterxml.jackson.annotation.JsonFormat;

public class RatBkUseCorpVO implements Serializable{
	/**
	 * 授信ID
	 */
	private String id;
	private String disCode;
	/**
	 * 分配方式
	 */
	private String disWay;
	// 授信模式
	private String ratMode;
	/**
	 * 授信CODE
	 */
	private String ratCode;
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
	/** 币别名称*/
	private java.lang.String curName;
	/** 授信开始日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date beginDate;

	/** 授信结束日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	/**
	 * 品种总条数
	 */
	private Integer rationSize;
	//时间
	private String ratDateStr;
	
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

	/**保证金比例**/
	private BigDecimal assurepercent;
	/**费率**/
	private BigDecimal procedureFeeRate;

	/**备注*/
	private String remark;

	/**
	 * 分配明细
	 */
	List<RatUrationUseDtlVO> rationDlts;

	/**保证金（已执行）*/
	private BigDecimal bondApply;
	/**保证金（在途）*/
	private BigDecimal bondAway;
	/**预测释放额度**/
	private BigDecimal calReleaseMoney;
	/**预测剩余额度**/
	private BigDecimal calLeftMoney;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getDisCode() {
        return disCode;
    }

    public void setDisCode(String disCode) {
        this.disCode = disCode;
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

	public String getRatCode() {
		return ratCode;
	}

	public void setRatCode(String ratCode) {
		this.ratCode = ratCode;
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

	public Integer getRationSize() {
		if (null == this.rationSize) {
			this.rationSize = Integer.valueOf(0);
		}
		return rationSize;
	}

	public void setRationSize(Integer rationSize) {
		this.rationSize = rationSize;
	}

	public List<RatUrationUseDtlVO> getRationDlts() {
		return rationDlts;
	}

	public void setRationDlts(List<RatUrationUseDtlVO> rationDlts) {
		this.rationDlts = rationDlts;
	}

	public java.lang.String getCurName() {
		return curName;
	}

	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	
	public String getRatDateStr() {
		return ratDateStr;
	}

	public void setRatDateStr(String ratDateStr) {
		this.ratDateStr = ratDateStr;
	}

	public BigDecimal getMoney() {
        if (EmptyUtils.isEmpty(money)) {
            money = BigDecimal.ZERO;
        }
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
	    if (EmptyUtils.isEmpty(approveMoney)) {
            approveMoney = BigDecimal.ZERO;
        }
		return approveMoney;
	}

	public void setApproveMoney(BigDecimal approveMoney) {
		this.approveMoney = approveMoney;
	}

	public BigDecimal getLeftMoney() {
        if (EmptyUtils.isEmpty(leftMoney)) {
            leftMoney = BigDecimal.ZERO;
        }
		return leftMoney;
	}

	public void setLeftMoney(BigDecimal leftMoney) {
		this.leftMoney = leftMoney;
	}

	public BigDecimal getOverPlusMoney() {
        if (EmptyUtils.isEmpty(overPlusMoney)) {
            overPlusMoney = BigDecimal.ZERO;
        }
		return overPlusMoney;
	}

	public void setOverPlusMoney(BigDecimal overPlusMoney) {
		this.overPlusMoney = overPlusMoney;
	}

	// 添加综合品种
	public void addRationDtl(RatUrationUseDtlVO rationDtl) {
		if (null == this.rationDlts) {
			this.rationDlts = new ArrayList<RatUrationUseDtlVO>();
		}
		this.rationDlts.add(rationDtl);
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

	public BigDecimal getBondApply() {
        if (EmptyUtils.isEmpty(bondApply)) {
            bondApply = BigDecimal.ZERO;
        }
		return bondApply;
	}

	public void setBondApply(BigDecimal bondApply) {
		this.bondApply = bondApply;
	}

	public BigDecimal getBondAway() {
        if (EmptyUtils.isEmpty(bondAway)) {
            bondAway = BigDecimal.ZERO;
        }
		return bondAway;
	}

	public void setBondAway(BigDecimal bondAway) {
		this.bondAway = bondAway;
	}

	public BigDecimal getCalReleaseMoney() {
		return calReleaseMoney;
	}

	public void setCalReleaseMoney(BigDecimal calReleaseMoney) {
		this.calReleaseMoney = calReleaseMoney;
	}

	public BigDecimal getCalLeftMoney() {
		return calLeftMoney;
	}

	public void setCalLeftMoney(BigDecimal calLeftMoney) {
		this.calLeftMoney = calLeftMoney;
	}
}
