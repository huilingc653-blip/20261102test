package com.bytter.biz.fundwarn.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 预警策略数据信息
 * @author zhengg
 * @date 2018-10-12
 */
@Entity
@Table(name = "BT_WARN_DATA", schema = "")
@SuppressWarnings("serial")
public class BtWarnData extends IdEntity implements java.io.Serializable {
	/** WS_ID VARCHAR2(32)策略ID */
	private String wsId;
	/** CORP_ID VARCHAR2(32)单位ID */
	private String corpId;
	/** CODE VARCHAR2(20)策略代码 */
	private String code;
	/** NAME VARCHAR2(200)策略名称 */
	private String name;
	/** CUR_ID VARCHAR2(32)预警币别 */
	private String curId;
	/**
	 * TYPE_CODE VARCHAR2(20)
	 * "预警类型(balOver余额超标、eqCorpPay相同公司付款、bankAccPay大额账户支出、inflowOver净流入透支、balDown账户余额持续下降、outPay系统外付款、expendInflow支出户流入、cashOver库存现金超标、depositLoanOver存贷比超标、cashPay大额现金支出)"
	 */
	private String typeCode;
	/** DIMENSION_CODE VARCHAR2(20)预警维度(corp单位、bankAcc银行账户、fcAcc内部账户) */
	public String dimensionCode;
	/** WARN_DATE DATE预警日期 */
	private Date warnDate;
	/** NO VARCHAR2(50)单号 */
	private String no;
	/** TRIGGER_NUM NUMBER(15,2)触发金额、持续下降月数、存贷比比例、支出笔数、流入金额 */
	private BigDecimal triggerNum;
	/** CONDITION VARCHAR2(200)触发条件 */
	private String condition;
	/** EXCESS_MONEY NUMBER(15,2)超额金额 */
	private BigDecimal excessMoney;
	/** BANK_TYPE_ID VARCHAR2(32)银行类型id */
	private String bankTypeId;
	/** BANK_ACC VARCHAR2(32)银行账号 */
	private String bankAcc;
	/** FC_ACC VARCHAR2(32)内部账户 */
	private String fcAcc;
	/** PAYEE_NAME VARCHAR2(200)交易对手 */
	private String payeeName;
	/** MONTH_NUM NUMBER(5)月份 */
	private Integer monthNum;
	/** SIZE_NUM NUMBER(5)流入笔数、流出笔数 */
	private Integer sizeNum;
	/** ONE_NUM NUMBER(15,2)流入资金\内部账号存款实时余额 */
	private BigDecimal oneNum;
	/** TWO_NUM NUMBER(15,2)流出资金、内部账号贷款实时余额 */
	private BigDecimal twoNum;
	/** CREATE_DATE TIMESTAMP(6)创建日期 */
	private Date createDate;
	/** UPDATE_DATE TIMESTAMP(6)更新日期 **/
	private Date updateDate;
	private String reserve1;
	private String bankTypeName;
	
	@Column(name = "BANK_TYPE_NAME")
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	/**
	 * 对应明细数据
	 */
	private  List<BtWarnDataDtl>  dataDtlList = null;
	@NotFound(action=NotFoundAction.IGNORE)
	@Transient
	public List<BtWarnDataDtl> getDataDtlList() {
		return dataDtlList;
	}
	public void setDataDtlList(List<BtWarnDataDtl> dataDtlList) {
		this.dataDtlList = dataDtlList;
	}
	@Column(name = "RESERVE1")
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	@Column(name = "WS_ID")
	public String getWsId() {
		return wsId;
	}
	public void setWsId(String wsId) {
		this.wsId = wsId;
	}
	@Column(name = "CORP_ID")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name = "TYPE_CODE")
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	@Column(name = "DIMENSION_CODE")
	public String getDimensionCode() {
		return dimensionCode;
	}
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}
	@Column(name = "WARN_DATE")
	public Date getWarnDate() {
		return warnDate;
	}
	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}
	@Column(name = "NO")
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Column(name = "TRIGGER_NUM")
	public BigDecimal getTriggerNum() {
		return triggerNum;
	}
	public void setTriggerNum(BigDecimal triggerNum) {
		this.triggerNum = triggerNum;
	}
	@Column(name = "CONDITIONS")
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Column(name = "EXCESS_MONEY")
	public BigDecimal getExcessMoney() {
		return excessMoney;
	}
	public void setExcessMoney(BigDecimal excessMoney) {
		this.excessMoney = excessMoney;
	}
	@Column(name = "BANK_TYPE_ID")
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	@Column(name = "BANK_ACC")
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	@Column(name = "FC_ACC")
	public String getFcAcc() {
		return fcAcc;
	}
	public void setFcAcc(String fcAcc) {
		this.fcAcc = fcAcc;
	}
	@Column(name = "PAYEE_NAME")
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	@Column(name = "MONTH_NUM")
	public Integer getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	@Column(name = "SIZE_NUM")
	public Integer getSizeNum() {
		return sizeNum;
	}
	public void setSizeNum(Integer sizeNum) {
		this.sizeNum = sizeNum;
	}
	@Column(name = "ONE_NUM")
	public BigDecimal getOneNum() {
		return oneNum;
	}
	public void setOneNum(BigDecimal oneNum) {
		this.oneNum = oneNum;
	}
	@Column(name = "TWO_NUM")
	public BigDecimal getTwoNum() {
		return twoNum;
	}
	public void setTwoNum(BigDecimal twoNum) {
		this.twoNum = twoNum;
	}
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "BtWarnData [wsId=" + wsId + ", corpId=" + corpId + ", code=" + code + ", name=" + name + ", curId="
				+ curId + ", typeCode=" + typeCode + ", dimensionCode=" + dimensionCode + ", warnDate=" + warnDate
				+ ", no=" + no + ", triggerNum=" + triggerNum + ", condition=" + condition + ", excessMoney="
				+ excessMoney + ", bankTypeId=" + bankTypeId + ", bankAcc=" + bankAcc + ", fcAcc=" + fcAcc
				+ ", payeeName=" + payeeName + ", monthNum=" + monthNum + ", sizeNum=" + sizeNum + ", oneNum=" + oneNum
				+ ", twoNum=" + twoNum + ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}
}
