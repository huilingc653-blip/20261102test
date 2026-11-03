package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhangxb
 * @Description 内部账户改户
 */
public class InnerAccChangingVO implements Serializable{
	/**id**/
	private String id;
	/**改户id**/
	private String hId;
	/**网点**/
	@BtCompositeSearch(fieldName="q.netId")
	@Corp(inject="netName")
	private String netId;
	private String netName;
	
	/**单位**/
	@BtCompositeSearch(fieldName="q.corpId")
	@Corp(inject="corpName")
	private String corpId;
	private String corpName;
	/**开户数**/
	private Integer acc;
	/**变更数**/
	private Integer history;
	/**注销数**/
	private Integer writeoff;
	/**冻结数**/
	private Integer freeze;
	/**解冻数**/
	private Integer thaw;
	/**账户**/
	private String blnctrAcc;
	/**账户名称**/
	private String accName;
	/**账户类别**/
	private String accType;
	/**币别**/
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**账面余额**/
	private BigDecimal checkBalance;
	/**可用余额**/
	private BigDecimal availableBalance;
	/**可透支金额**/
	private BigDecimal overdraftMoney;
	/**开户日期**/
	@BtCompositeSearch(fieldName="q.accOpenTime")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accOpenTime;
	/**当前冻结金额**/
	private BigDecimal freezeMoney;
	/**解冻金额**/
	private BigDecimal wfMoney;
	/**账户性质**/
	private String natureCodeId;
	/**账户属性**/
	private String attributeId;
	/**预提标志**/
	private String predrawSign;
	/**透支利率**/
	private BigDecimal overdraftRate;
	/**账号启用日期**/
	private Date accStartDate;
	/**冻结标志**/
	private String freezeSign;
	/**冻结(解冻)类型**/
	private String freezeType;
	/**冻结开始时间**/
	private Date beginDate;
	/**冻结结束时间**/
	private Date endDate;
	/**冻结/解冻原因**/
	private String freezeReason;
	/**复核日期**/
	private Date checkTime;
	/**账户状态**/
	private String accState;
	/**冻结/解冻Id**/
	private String feId;
	private Date ged_accOpenTime;
	private Date led_accOpenTime;
	
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Integer getAcc() {
		return acc;
	}
	public void setAcc(Integer acc) {
		this.acc = acc;
	}
	public Integer getHistory() {
		return history;
	}
	public void setHistory(Integer history) {
		this.history = history;
	}
	public Integer getWriteoff() {
		return writeoff;
	}
	public void setWriteoff(Integer writeoff) {
		this.writeoff = writeoff;
	}
	public Integer getFreeze() {
		return freeze;
	}
	public void setFreeze(Integer freeze) {
		this.freeze = freeze;
	}
	public Integer getThaw() {
		return thaw;
	}
	public void setThaw(Integer thaw) {
		this.thaw = thaw;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
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
	public BigDecimal getCheckBalance() {
		return checkBalance;
	}
	public void setCheckBalance(BigDecimal checkBalance) {
		this.checkBalance = checkBalance;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	public BigDecimal getOverdraftMoney() {
		return overdraftMoney;
	}
	public void setOverdraftMoney(BigDecimal overdraftMoney) {
		this.overdraftMoney = overdraftMoney;
	}
	public Date getAccOpenTime() {
		return accOpenTime;
	}
	public void setAccOpenTime(Date accOpenTime) {
		this.accOpenTime = accOpenTime;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getWfMoney() {
		return wfMoney;
	}
	public void setWfMoney(BigDecimal wfMoney) {
		this.wfMoney = wfMoney;
	}
	public String getNatureCodeId() {
		return natureCodeId;
	}
	public void setNatureCodeId(String natureCodeId) {
		this.natureCodeId = natureCodeId;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getPredrawSign() {
		return predrawSign;
	}
	public void setPredrawSign(String predrawSign) {
		this.predrawSign = predrawSign;
	}
	public BigDecimal getOverdraftRate() {
		return overdraftRate;
	}
	public void setOverdraftRate(BigDecimal overdraftRate) {
		this.overdraftRate = overdraftRate;
	}
	public Date getAccStartDate() {
		return accStartDate;
	}
	public void setAccStartDate(Date accStartDate) {
		this.accStartDate = accStartDate;
	}
	public String getFreezeSign() {
		return freezeSign;
	}
	public void setFreezeSign(String freezeSign) {
		this.freezeSign = freezeSign;
	}
	public String getFreezeType() {
		return freezeType;
	}
	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
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
	public String getFreezeReason() {
		return freezeReason;
	}
	public void setFreezeReason(String freezeReason) {
		this.freezeReason = freezeReason;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String gethId() {
		return hId;
	}
	public void sethId(String hId) {
		this.hId = hId;
	}
	public String getAccState() {
		return accState;
	}
	public void setAccState(String accState) {
		this.accState = accState;
	}
	public String getFeId() {
		return feId;
	}
	public void setFeId(String feId) {
		this.feId = feId;
	}
	public Date getGed_accOpenTime() {
		return ged_accOpenTime;
	}
	public void setGed_accOpenTime(Date ged_accOpenTime) {
		this.ged_accOpenTime = ged_accOpenTime;
	}
	public Date getLed_accOpenTime() {
		return led_accOpenTime;
	}
	public void setLed_accOpenTime(Date led_accOpenTime) {
		this.led_accOpenTime = led_accOpenTime;
	}
}
