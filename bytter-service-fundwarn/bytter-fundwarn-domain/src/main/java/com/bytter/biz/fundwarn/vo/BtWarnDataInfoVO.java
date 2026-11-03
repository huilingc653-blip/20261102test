package com.bytter.biz.fundwarn.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description: 预警策略主数据信息VO
 * @author zhengg
 * @date 2018-10-15
 */
public class BtWarnDataInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 数据ID */
	private String id;
	
	/** 策略ID */
	private String wsId;
	
	/** 网点ID */
	private String netId;
	
	/** 网点名称 */
	@BtCompositeSearch(fieldName="c.net_Id")
	private String netName;
	
	/** 单位ID */
	@BtCompositeSearch(fieldName="a.CORP_ID")
	private String corpId;
	/** 单位名称 */
	@BtCompositeSearch(fieldName="a.CORP_NAME")
	private String corpName;
	/** 策略代码 */
	@BtCompositeSearch(fieldName="a.code")
	private String code;
	/** 策略名称 */
	@BtCompositeSearch(fieldName="a.name")
	private String name;
	
	/** 预警币别 */
	@BtCompositeSearch(fieldName="a.cur_Id")
	private String curId;
	/** 预警币别名称 */
	@BtCompositeSearch(fieldName="a.cur_Id")
	private String curName;
	
	/**
	 * "预警类型(balOver余额超标、eqCorpPay相同公司付款、bankAccPay大额账户支出、inflowOver净流入透支、balDown账户余额持续下降、outPay系统外付款、expendInflow支出户流入、cashOver库存现金超标、depositLoanOver存贷比超标、cashPay大额现金支出)"
	 */
	@BtCompositeSearch(fieldName="a.type_Code")
	private String typeCode;
	/**
	 * "预警类型(balOver余额超标、eqCorpPay相同公司付款、bankAccPay大额账户支出、inflowOver净流入透支、balDown账户余额持续下降、outPay系统外付款、expendInflow支出户流入、cashOver库存现金超标、depositLoanOver存贷比超标、cashPay大额现金支出)"
	 */
	@BtCompositeSearch(fieldName="a.type_Code")
	private String typeName;
	/** 预警维度(corp单位、bankAcc银行账户、fcAcc内部账户) */
	@BtCompositeSearch(fieldName="a.dimension_Code")
	private String dimensionCode;
	/** 预警维度名称(corp单位、bankAcc银行账户、fcAcc内部账户) */
	@BtCompositeSearch(fieldName="a.dimension_Code")
	private String dimensionName;
	/** 预警日期 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="a.warn_Date")
	private Date warnDate;
	/** 单号 */
	@BtCompositeSearch(fieldName="a.no")
	private String no;
	/** 触发金额、持续下降月数、存贷比比例、支出笔数、流入金额 */
	@BtCompositeSearch(fieldName="a.trigger_Num")
	private BigDecimal triggerNum;
	/** 触发条件 */
	@BtCompositeSearch(fieldName="a.conditions")
	private String conditions;
	/** 超额金额 */
	@BtCompositeSearch(fieldName="a.excess_Money")
	private BigDecimal excessMoney;
	
	/** 银行类型id */
	private String bankTypeId;
	/** 银行类型名称 */
	@BtCompositeSearch(fieldName="a.BANK_TYPE_NAME")
	private String bankTypeName;
	/** 银行账号 */
	@BtCompositeSearch(fieldName="a.bank_Acc")
	private String bankAcc;
	/** 内部账户 */
	@BtCompositeSearch(fieldName="a.fc_Acc")
	private String fcAcc;
	/** 交易对手 */
	@BtCompositeSearch(fieldName="a.payee_Name")
	private String payeeName;
	/** 月份 */
	@BtCompositeSearch(fieldName="a.month_Num")
	private Integer monthNum;
	/** 流入笔数、流出笔数 */
	@BtCompositeSearch(fieldName="a.size_Num")
	private Integer sizeNum;
	/** 流入资金\内部账号存款实时余额 */
	@BtCompositeSearch(fieldName="a.one_Num")
	private BigDecimal oneNum;
	/** 流出资金、内部账号贷款实时余额 */
	@BtCompositeSearch(fieldName="a.two_Num")
	private BigDecimal twoNum;
	/** 创建日期 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="a.create_Date")
	private Date createDate;
	/** 更新日期 **/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="a.update_Date")
	private Date updateDate;
	
	/**
	 * 是否发生消息 1是0或null否
	 */
	private String sendMsgSign;
	
	private String reserve1;
	/** 预警级别*/
	@BtCompositeSearch(fieldName="ws.warn_Level")
	private String warnLevel;
	private String warnLevelName;
	public String getWarnLevel() {
		return warnLevel;
	}
	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}
	public String getWarnLevelName() {
		return warnLevelName;
	}
	public void setWarnLevelName(String warnLevelName) {
		this.warnLevelName = warnLevelName;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getSendMsgSign() {
		return sendMsgSign;
	}
	public void setSendMsgSign(String sendMsgSign) {
		this.sendMsgSign = sendMsgSign;
	}
	public String getWsId() {
		return wsId;
	}
	public void setWsId(String wsId) {
		this.wsId = wsId;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDimensionCode() {
		return dimensionCode;
	}
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}
	public String getDimensionName() {
		return dimensionName;
	}
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}
	public Date getWarnDate() {
		return warnDate;
	}
	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public BigDecimal getTriggerNum() {
		return triggerNum;
	}
	public void setTriggerNum(BigDecimal triggerNum) {
		this.triggerNum = triggerNum;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public BigDecimal getExcessMoney() {
		return excessMoney;
	}
	public void setExcessMoney(BigDecimal excessMoney) {
		this.excessMoney = excessMoney;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getFcAcc() {
		return fcAcc;
	}
	public void setFcAcc(String fcAcc) {
		this.fcAcc = fcAcc;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public Integer getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	public Integer getSizeNum() {
		return sizeNum;
	}
	public void setSizeNum(Integer sizeNum) {
		this.sizeNum = sizeNum;
	}
	public BigDecimal getOneNum() {
		return oneNum;
	}
	public void setOneNum(BigDecimal oneNum) {
		this.oneNum = oneNum;
	}
	public BigDecimal getTwoNum() {
		return twoNum;
	}
	public void setTwoNum(BigDecimal twoNum) {
		this.twoNum = twoNum;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "BtWarnDataInfoVO [wsId=" + wsId + ", netId=" + netId + ", netName=" + netName + ", corpId=" + corpId
				+ ", corpName=" + corpName + ", code=" + code + ", name=" + name + ", curId=" + curId + ", curName="
				+ curName + ", typeCode=" + typeCode + ", typeName=" + typeName + ", dimensionCode=" + dimensionCode
				+ ", dimensionName=" + dimensionName + ", warnDate=" + warnDate + ", no=" + no + ", triggerNum="
				+ triggerNum + ", condition=" + conditions + ", excessMoney=" + excessMoney + ", bankTypeId="
				+ bankTypeId + ", bankTypeName=" + bankTypeName + ", bankAcc=" + bankAcc + ", fcAcc=" + fcAcc
				+ ", payeeName=" + payeeName + ", monthNum=" + monthNum + ", sizeNum=" + sizeNum + ", oneNum=" + oneNum
				+ ", twoNum=" + twoNum + ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}
	
}
