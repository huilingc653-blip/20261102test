package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhoup
 * @Description 结算量统计表
 * @date 2018.4.18
 */
public class AllocationSettlementVO implements Serializable {
	/**
	 * 单位ID
	 */
	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName="hl.hcorpId")
	private String corpId;
	/**
	 * 单位名称
	 */
	@BtCompositeSearch(fieldName="sc.name")
	private String corpName;
	/**
	 * 单位编码
	 */
	private String code;
	/**
	 * 银行账号
	 */
	@BtCompositeSearch(fieldName="hl.hbankAcc")
	private String bankAcc;
	/**
	 * 开户行
	 */
	@BtCompositeSearch(fieldName="hl.hbankName")
	private String bankName;
	/**
	 * 银行类别
	 */
	@BtCompositeSearch(fieldName="bbt.type_name")
	private String bankType;

	/**
	 * 银行类别id
	 */
	private String bankTypeId;
	/**
	 * 币别ID
	 */
	@Currency(inject="curName")
	@BtCompositeSearch(fieldName="hl.hcurId")
	private String curId;
	/**
	 * 币别名称
	 */
	@BtCompositeSearch(fieldName="hl.hcurId")
	private String curName;
	/**
	 * 期初余额
	 */
	@BtCompositeSearch(fieldName="hl.startMoney")
	private BigDecimal startMoney;
	/**
	 * 借方发生笔数
	 */
	@BtCompositeSearch(fieldName="hl.getNumbers")
	private Integer getNumbers;
	/**
	 * 贷方发生笔数
	 */
	@BtCompositeSearch(fieldName="hl.borrowNumbers")
	private Integer borrowNumbers;
	/**
	 * 借方发生额
	 */
	@BtCompositeSearch(fieldName="hl.getamt")
	private BigDecimal getMoney;
	/**
	 * 贷方发生额
	 */
	@BtCompositeSearch(fieldName="hl.borrowamt")
	private BigDecimal borrowMoney;
	/**
	 * 期末余额
	 */
	@BtCompositeSearch(fieldName="hl.endMoney")
	private BigDecimal endMoney;
	/**
	 * 占比（不是百分数，无百分符号）
	 */
	private BigDecimal rate;
	/**
	 * 分页条数
	 */
	private Integer length;
	/**
	 * 从第几条开始
	 */
	private Integer start;
	/**
	 * 期初余额的String类型
	 */
	private String startMoneyStr;
	/**
	 * 期末余额的String类型
	 */
	private String endMoneyStr;
	private Date beginDate;
	private Date endDate;
	private Integer draw;
	private String type;
	/**
	 * 排序参数
	 */
	private String orderPara;
	/**
	 * 排序规则
	 */
	private String orderByPara;
	/**
	 * 账户属性
	 */
	private String attributeId;
	private String attributeName;
	/**
	 * 账户性质
	 */
	private String natureId;
	private String natureName;
	/**账户类别*/
	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	private String accType;
	private String accTypeName;
	/**是否折合币种*/
	private String changeCur;
	/**折合币种*/
	private String reducedCurId;
	/**折算汇率类型*/
	private String rateType;
	/**汇率日期*/
	private Date rateDate;
	/**折合汇率*/
	private BigDecimal curExrate;
	public String getOrderPara() {
		return orderPara;
	}
	public void setOrderPara(String orderPara) {
		this.orderPara = orderPara;
	}
	public String getOrderByPara() {
		return orderByPara;
	}
	public void setOrderByPara(String orderByPara) {
		this.orderByPara = orderByPara;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
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
	public BigDecimal getStartMoney() {
		return startMoney;
	}
	public void setStartMoney(BigDecimal startMoney) {
		this.startMoney = startMoney;
	}
	public Integer getGetNumbers() {
		return getNumbers;
	}
	public void setGetNumbers(Integer getNumbers) {
		this.getNumbers = getNumbers;
	}
	public Integer getBorrowNumbers() {
		return borrowNumbers;
	}
	public void setBorrowNumbers(Integer borrowNumbers) {
		this.borrowNumbers = borrowNumbers;
	}
	public BigDecimal getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(BigDecimal getMoney) {
		this.getMoney = getMoney;
	}
	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	public BigDecimal getEndMoney() {
		return endMoney;
	}
	public void setEndMoney(BigDecimal endMoney) {
		this.endMoney = endMoney;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public String getStartMoneyStr() {
		return startMoneyStr;
	}
	public void setStartMoneyStr(String startMoneyStr) {
		this.startMoneyStr = startMoneyStr;
	}
	public String getEndMoneyStr() {
		return endMoneyStr;
	}
	public void setEndMoneyStr(String endMoneyStr) {
		this.endMoneyStr = endMoneyStr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getNatureId() {
		return natureId;
	}
	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public String getChangeCur() {
		return changeCur;
	}
	public void setChangeCur(String changeCur) {
		this.changeCur = changeCur;
	}
	public String getReducedCurId() {
		return reducedCurId;
	}
	public void setReducedCurId(String reducedCurId) {
		this.reducedCurId = reducedCurId;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public Date getRateDate() {
		return rateDate;
	}
	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}

	public String getBankTypeId() {
		return bankTypeId;
	}

	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
}
